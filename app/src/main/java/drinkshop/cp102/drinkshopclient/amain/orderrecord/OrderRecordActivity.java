package drinkshop.cp102.drinkshopclient.amain.orderrecord;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import drinkshop.cp102.drinkshopclient.Common;
import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.amain.MainActivity;
import drinkshop.cp102.drinkshopclient.bean.Order;
import drinkshop.cp102.drinkshopclient.bean.OrderDetail;
import drinkshop.cp102.drinkshopclient.decoder.OrderTypeDecoder;
import drinkshop.cp102.drinkshopclient.helper.LogHelper;
import drinkshop.cp102.drinkshopclient.qrcode.Contents;
import drinkshop.cp102.drinkshopclient.qrcode.QRCodeEncoder;
import drinkshop.cp102.drinkshopclient.server.task.CommonTask;

import static android.content.ContentValues.TAG;

public class OrderRecordActivity extends AppCompatActivity {
    public static final String TAG = "OrderRecordActivity";
    ImageView ivQRCode;
    TextView tvPickUpTime;
    TextView tvOrderTextID;
    TextView tvOrderTextType;
    TextView tvOrderTextTotal;
    RecyclerView rvOrderDetails;
    TextView tvOfficiallanguage;

    OrderRecordAdapter adapter = null;

    /**
     * 開始
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_record);
        programAndUILink();
        initValue();

    }

    /**
     * 程式與畫面 連結
     */
    private void programAndUILink() {
        ivQRCode = findViewById(R.id.ivQRCode);
        tvPickUpTime = findViewById(R.id.tvPickUpTime);
        tvOrderTextID = findViewById(R.id.tvOrderTextID);
        tvOrderTextType = findViewById(R.id.tvOrderTextType);
        tvOrderTextTotal = findViewById(R.id.tvOrderTextTotal);
        rvOrderDetails = findViewById(R.id.rvOrderDetails);
        tvOfficiallanguage = findViewById(R.id.tvOfficiallanguage);
    }

    /**
     * 初始化
     */
    @SuppressLint("SetTextI18n")
    private void initValue() {
        setTitle("訂購完成");

        Bundle bundle = getIntent().getExtras();
        int orderId = bundle.getInt("orderId");
        List<Order> orderList = findOrderByOrderId(orderId);
        /* ivQRCode */
        String qrCodeText = null;

        /* PickUpTime */
        long orderAcceptTimeL = 0;
        long productTotalQuantity = 0;

        /* OrderTextID */
        int orderID = 0;

        /* OrderTextType */
        String orderType = null;

        /* OrderTextTotal */
        int orderTotalAmount = 0;
        float coupon_discount = 1;

        /* OrderDetails */
        List<OrderDetail> orderDetails = new ArrayList<>();

        /* Officiallanguage */
        String storeName = null;

        for (Order order : orderList) {

            /* ivQRCode */
            qrCodeText = String.valueOf(order.getOrder_id());

            /* PickUpTime */

            try {
                String acceptTimeS = order.getOrder_accept_time();
                LogHelper.e(TAG, "acceptTimeS " + acceptTimeS);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(acceptTimeS);
                orderAcceptTimeL = date.getTime();
                LogHelper.e(TAG, "orderAcceptTimeL = " + orderAcceptTimeL);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            /* OrderTextID */
            orderID = order.getOrder_id();

            /* OrderTextType */
            orderType = OrderTypeDecoder.valueOfOrderType(order.getOrder_type());

            /* OrderTextTotal */
            coupon_discount = (float) (order.getCoupon_discount() * 0.1);

            /* OrderDetails */
            orderDetails = order.getOrderDetailList();

            /* Officiallanguage */
            storeName = order.getStore_name();

            for (OrderDetail orderDetail : order.getOrderDetailList()) {

                /* ivQRCode */

                /* PickUpTime */
                productTotalQuantity += orderDetail.getProduct_quantity();

                /* OrderTextID */
                /* OrderTextType */

                /* OrderTextTotal */
                orderTotalAmount += (orderDetail.getProduct_quantity() * orderDetail.getProduct_price());

                /* OrderDetails */

                /* Officiallanguage */

            }
        }

        /* ivQRCode */
        String qrCode = qrCodeText;
        // QR code image's length is the same as the width of the window,
        int dimension = getResources().getDisplayMetrics().widthPixels;
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrCodeText, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
                dimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ivQRCode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            Log.e(TAG, e.toString());
        }

        /* PickUpTime */
        long waitingTime = productTotalQuantity * 60 * 1000;  //每杯製作時間預計1分鐘
        final long fiveMinutes = 10 * 60 * 1000; // 最低時間十分鐘
        if(waitingTime < fiveMinutes) {
            waitingTime = fiveMinutes;
        } else {
            waitingTime = productTotalQuantity * 60 * 1000;
        }
        String date = DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date(orderAcceptTimeL + waitingTime)).toString();
        LogHelper.e(TAG, "pickUpTime" + date);
        String pickUpTime = date;
        tvPickUpTime.setText(tvPickUpTime.getText() + "" + pickUpTime);

        /* OrderTextID */
        tvOrderTextID.setText("" + orderID);

        /* OrderTextType */
        tvOrderTextType.setText("" + orderType);

        /* OrderTextTotal */
        tvOrderTextTotal.setText("" + (int) (orderTotalAmount * coupon_discount));

        /* OrderDetails */
        rvOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        if (adapter == null) {
            adapter = new OrderRecordAdapter(OrderRecordActivity.this, orderDetails);
        }
        rvOrderDetails.setAdapter(adapter);

        /* Officiallanguage */
        tvOfficiallanguage.setText(R.string.textOfficiallanguage + "" + storeName);

    }

    /**
     * 建立訂單
     */
    private List<Order> findOrderByOrderId(int orderId) {
        List<Order> order = null;
        if (Common.networkConnected(this)) {
            String url = Common.URL + "/OrdersServlet";
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findOrderByOrderId");
            jsonObject.addProperty("orderId", orderId);

            String jsonOut = jsonObject.toString();
            CommonTask orderInsertTask = new CommonTask(url, jsonOut);
            try {
                String result = orderInsertTask.execute().get();
                Type orderListType = new TypeToken<List<Order>>() {
                }.getType();
                order = gson.fromJson(result, orderListType);  //雲端資料庫新建order成功傳回訂單編號
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            if (order == null) {
                Common.showToast(this, R.string.msg_NoFoundOrder);
            } else {
                return order;
            }
        }
        return order;
    }

    /**
     * 按下android底下的三按鍵
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 按下返回鍵
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return true;

    }

    /**
     * 重新更新畫面
     */
    public void updateRecyclerView() {
        adapter.notifyDataSetChanged();
        LogHelper.e(TAG, "updateRecyclerView：執行成功");
    }


}
