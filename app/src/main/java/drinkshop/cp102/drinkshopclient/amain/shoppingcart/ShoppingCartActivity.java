package drinkshop.cp102.drinkshopclient.amain.shoppingcart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.drinkshopclient.Common;
import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.amain.loginactivity.LoginDialogActivity;
import drinkshop.cp102.drinkshopclient.amain.orderrecord.OrderRecordActivity;
import drinkshop.cp102.drinkshopclient.bean.Order;
import drinkshop.cp102.drinkshopclient.bean.OrderDetail;
import drinkshop.cp102.drinkshopclient.bean.ShoppingCart;
import drinkshop.cp102.drinkshopclient.constant.ShoppingCartActivityConstant;
import drinkshop.cp102.drinkshopclient.helper.LogHelper;
import drinkshop.cp102.drinkshopclient.helper.ShoppingCartDBHelper;
import drinkshop.cp102.drinkshopclient.server.task.CommonTask;

/**
 * 購物車
 *
 * @author mrosstro
 */
public class ShoppingCartActivity extends AppCompatActivity {
    public static final String TAG = "ShoppingCartActivity";

    private TextView tvOrderDetails;  //訂單明細
    private Button btnTakeOut;  //外帶
    private Button btnOrderIn;  //外送
    private RecyclerView rvShoppinCardList;  //購物車清單
    private Button btnUseCoupon;  //使用優惠卷
    private TextView tvTotalCap;  //總杯數
    private TextView tvTotalAmount;  //總金額
    private Button btnOrderCompleted;  //訂單完成
    private ShoppinCartListAdapter adapter;
    private String order_type = "0";
    private Gson gson;
    ShoppingCartDBHelper shoppingCartDBHelper = new ShoppingCartDBHelper(this);
    List<OrderDetail> orderDetailList = new ArrayList<>();

    /**
     * 開始
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        programAndUILink();
        initValue();

    }

    /**
     * 程式與畫面 連結
     */
    private void programAndUILink() {
        this.rvShoppinCardList = findViewById(R.id.rvShoppinCardList);
        this.tvTotalCap = findViewById(R.id.tvTotalCap);
        this.tvTotalAmount = findViewById(R.id.tvTotalAmount);
        this.btnOrderCompleted = findViewById(R.id.btnOrderCompleted);
    }

    /**
     * 初始化
     */
    private void initValue() {
        setTitle("購物車");
        rvShoppinCardList.setLayoutManager(new LinearLayoutManager(this));
        if (adapter == null) {
            adapter = new ShoppinCartListAdapter(this, tvTotalCap, tvTotalAmount, btnOrderCompleted);
        }
        rvShoppinCardList.setAdapter(adapter);
    }

    /**
     * 當此頁轉頁至別頁並返回後執行
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ShoppingCartActivityConstant.SHOPPING_CART) {
            updateRecyclerView();
        }

        if(requestCode == ShoppingCartActivityConstant.LOGIN_DIALOG) {
            if(resultCode == RESULT_CANCELED) {
                LogHelper.e(TAG, "resultCode = " + resultCode);

            } else {
                LogHelper.e(TAG, "resultCode = " + resultCode + "use orderInsert()");
                orderInsert();
            }
        }
    }

    /**
     * 重新更新畫面
     */
    public void updateRecyclerView() {
        adapter.notifyDataSetChanged();
        LogHelper.e(TAG, "updateRecyclerView：更新成功");
    }

    /**
     * 按下 訂單完成
     */
    public void onClickOrderCompleted(View view) {
        if (!getLoginStatus()) {// 判斷是否已登入 沒有就...
            Intent intent = new Intent(ShoppingCartActivity.this, LoginDialogActivity.class);
            startActivityForResult(intent, ShoppingCartActivityConstant.LOGIN_DIALOG);
        } else {  // 有登入就...
            orderInsert();
        }

    }

    /**
     * 取得登入狀況
     */
    public boolean getLoginStatus() {
        SharedPreferences pref = getSharedPreferences("preference", MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);
        LogHelper.e(TAG, "getLoginStatus： 登入狀態：" + login);
        return login;
    }

    /**
     * 訂單資料
     * */
    private Order getOrder() {
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        int store_id = 1;
        int member_id = pref.getInt("member_id", 0);
        int coupon_id = 0;

        Order order = new Order();
        order.setStore_id(store_id);
        order.setMember_id(member_id);
        order.setOrder_type(order_type);
        order.setCoupon_id(coupon_id);
        return order;
    }

    /**
     * 購物車資料庫
     */
    private List<OrderDetail> getShoppingCardList() {

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart sc : shoppingCartDBHelper.readAllProduct()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder_detail_id(sc.getProductID());
            orderDetail.setProduct_id(sc.getProductID());
            orderDetail.setSize_id(sc.getSize());
            orderDetail.setSugar_id(sc.getSuger());
            orderDetail.setIce_id(sc.getHotOrIce());
            orderDetail.setProduct_quantity(sc.getQuantity());
            orderDetailList.add(orderDetail);
        }
        return orderDetailList;
    }

    /**
     * 建立訂單
     */
    private void orderInsert() {
        orderDetailList = getShoppingCardList();  //取得所有購買的商品詳細
        int orderId = 0;
        if (Common.networkConnected(this)) {
            String url = Common.URL + "/OrdersServlet";
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "orderInsert");
            jsonObject.addProperty("order",gson.toJson(getOrder()));
            jsonObject.addProperty("orderDetailList", gson.toJson(orderDetailList));
            String jsonOut = jsonObject.toString();
            CommonTask orderInsertTask = new CommonTask(url, jsonOut);
            try {
                String result = orderInsertTask.execute().get();
                LogHelper.e(TAG, "result = " + result);
                Type orderListType = new TypeToken<Integer>() {
                }.getType();
                orderId = gson.fromJson(result, orderListType);  //雲端資料庫新建order成功傳回訂單編號
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            if (orderId == 0) {
                Common.showToast(this, R.string.msg_FailCreateOrder);
            } else {
                shoppingCartDBHelper.clearDB();  //訂單成功建立 刪除本地資料庫內容
                Bundle bundle = new Bundle();
                bundle.putInt("orderId", Integer.valueOf(orderId));
                Intent intentOrder = new Intent(ShoppingCartActivity.this, OrderRecordActivity.class);
                intentOrder.putExtras(bundle);
                startActivity(intentOrder);
            }
        }
    }

    /**
     * 按下 自取
     */
    public void onClickTakeOut(View view) {
        order_type = "0";
    }

    /**
     * 按下 外送
     */
    public void onClickOrderIn(View view) {
        order_type = "1";
    }
}
