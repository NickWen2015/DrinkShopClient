package drinkshop.cp102.drinkshopclient.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.adapter.ShoppinCartListAdapter;
import drinkshop.cp102.drinkshopclient.bean.ShoppingCart;
import drinkshop.cp102.drinkshopclient.helper.ShoppingCartDBHelper;

/**
 * 購物車
 *
 * @author mrosstro
 * @date 2018/9/3
 */
public class ShoppingCartActivity extends AppCompatActivity {

    TextView tvOrderDetails;  //訂單明細
    Button btnTakeOut;  //外帶
    Button btnOrderIn;  //外送
    RecyclerView rvShoppinCardList;  //購物車清單
    Button btnUseCoupon;  //使用優惠卷
    TextView tvTotalCap;  //總杯數
    TextView tvTotalAmount;  //總金額
    Button btnOrderCompleted;  //訂單完成

    private List<ShoppingCart> getShoppingCardList(){
        ShoppingCartDBHelper shoppingCartDBHelper = new ShoppingCartDBHelper(this);
        return shoppingCartDBHelper.readAllProduct();
    }
    /**
     * 程式與畫面 連結
     */
    private void programAndUILink() {
        this.rvShoppinCardList = findViewById(R.id.rvShoppinCardList);
        rvShoppinCardList.setLayoutManager(new LinearLayoutManager(this));
        rvShoppinCardList.setAdapter(new ShoppinCartListAdapter(this, getShoppingCardList()));

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        programAndUILink();


    }


}
