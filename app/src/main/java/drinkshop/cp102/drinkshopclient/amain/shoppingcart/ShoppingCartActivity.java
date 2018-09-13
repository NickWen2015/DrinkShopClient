package drinkshop.cp102.drinkshopclient.amain.shoppingcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.ShoppingCart;
import drinkshop.cp102.drinkshopclient.bean.ShoppingCartTotol;
import drinkshop.cp102.drinkshopclient.constant.ShoppingCartActivityConstant;
import drinkshop.cp102.drinkshopclient.helper.LogHelper;
import drinkshop.cp102.drinkshopclient.helper.ShoppingCartDBHelper;

/**
 * 購物車
 *
 * @author mrosstro
 * @date 2018/9/7
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

    private ShoppingCartDBHelper shoppingCartDBHelper = new ShoppingCartDBHelper(this);  //資料庫
    private ShoppinCartListAdapter adapter;

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
    }

    /**
     * 初始化
     */
    private void initValue() {
        setTitle("購物車");
        rvShoppinCardList.setLayoutManager(new LinearLayoutManager(this));
        if(adapter == null){
            adapter = new ShoppinCartListAdapter(this, tvTotalCap, tvTotalAmount);
        }
        rvShoppinCardList.setAdapter(adapter);
    }

    /**
     * 當此頁轉頁至別頁並返回後執行
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ShoppingCartActivityConstant.SHOPPING_CART:
                updateRecyclerView();
        }
    }

    /**
     * 重新更新畫面
     * */
    public void updateRecyclerView() {
        adapter.notifyDataSetChanged();
        LogHelper.e(TAG, "updateRecyclerView：執行成功");
    }


}
