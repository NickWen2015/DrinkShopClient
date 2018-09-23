package drinkshop.cp102.drinkshopclient.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.List;

import drinkshop.cp102.drinkshopclient.Common;
import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.Order;
import drinkshop.cp102.drinkshopclient.bean.OrderDetail;
import drinkshop.cp102.drinkshopclient.qrcode.Contents;
import drinkshop.cp102.drinkshopclient.qrcode.QRCodeEncoder;

import static android.content.ContentValues.TAG;

public class MemberOrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvOrderDetail, tvTatolPrice;
    private Button btBuyAgain;
    private ImageView ivQRCode;
    private static final String TAG = "MemberOdFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_order_detail);
        setTitle(R.string.textMemberMenuOrderStatus);
        handleView();
    }

    private void handleView() {
        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderDetail = findViewById(R.id.tvOrderDetail);
        tvTatolPrice = findViewById(R.id.tvTatolPrice);
        btBuyAgain = findViewById(R.id.btBuyAgain);
        ivQRCode = findViewById(R.id.ivQRCode);//產生QR code的圖

        String orderDetailString = "";
        Bundle bundle = getIntent().getExtras();
        Order order = (Order) bundle.getSerializable("order");
        int detailPrice = bundle.getInt("detailPrice");
        if (bundle != null) {
            if (order != null) {
                tvOrderId.setText(String.valueOf(order.getOrder_id()));
                if (order.getCoupon_discount() != 0) {
                    tvTatolPrice.setText("折扣後為" + String.valueOf(detailPrice) + "元");
                } else {
                    tvTatolPrice.setText(String.valueOf(detailPrice) + "元");
                }

                List<OrderDetail> orderDetailList = order.getOrderDetailList();
                for (OrderDetail orderDetail : orderDetailList) {
                    int subPrice = orderDetail.getProduct_quantity() * orderDetail.getProduct_price();
                    orderDetailString += orderDetail.getProduct_name() + " "
                            + orderDetail.getSugar_name() + " "
                            + orderDetail.getIce_name() + " "
                            + orderDetail.getSize_name() + " "
                            + orderDetail.getProduct_price() + "元 "
                            + orderDetail.getProduct_quantity() + "杯 "
                            + subPrice + "元\n";
                }
                tvOrderDetail.setText(orderDetailString);

                /**** 產生QR code的圖 start ****/
                String qrCodeText = tvOrderId.getText().toString();
                Log.d(TAG, qrCodeText);

                // QR code image's length is the same as the width of the window,
                int dimension = getResources().getDisplayMetrics().widthPixels;//取得螢幕寬度,dimension 只要給一邊 qr code 產生為正方形

                // Encode with a QR Code image
                QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrCodeText, null,
                        Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
                        dimension);//Contents.Type.TEXT 一般文字不加任何文字, BarcodeFormat.QR_CODE二維條碼格式
                try {
                    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                    ivQRCode.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    Log.e(TAG, e.toString());
                }
                /**** 產生QR code的圖 end ****/

                btBuyAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.showToast(MemberOrderDetailActivity.this, "外送追蹤");
                    }
                });
            }


        }
    }
}
