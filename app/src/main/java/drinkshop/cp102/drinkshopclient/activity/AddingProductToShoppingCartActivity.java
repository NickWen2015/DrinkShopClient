package drinkshop.cp102.drinkshopclient.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.AddingProduct;
import drinkshop.cp102.drinkshopclient.builder.AddingProductBuilder;
import drinkshop.cp102.drinkshopclient.helper.ShoppingCartDBHelper;


/**
 * 冷 = 0 熱 = 1
 * M = 0 L = 1
 * 正常糖 = 0 少糖 = 1 半糖 = 2 微糖 = 3 無糖 = 4
 * 正常冰 = 0 少冰 = 1 微冰 = 2 去冰 = 3
 * 正常熱 = 0 熱一點 = 1
 * 添加選取的商品至購物車
 *
 * @author mrosstro
 * @date 2018/8/29
 */
public class AddingProductToShoppingCartActivity extends AppCompatActivity {

    private ImageView ivProductImage;  //產品圖案
    //產品冷熱（設定在 activity_adding_product_to_shoppingcart 的 btnHot（onClickHot） 和 btnIce（onClickIce））
    //產品尺寸（設定在 activity_adding_product_to_shoppingcart 的 btnSizeM（onClickSizeM） 和 btnSizeL（onClickSizeL）
    private int L, M;  //尺寸的價錢
    private TextView tvQuantity;  //產品數量
    private Button btnAddQuantity, btnBackQuantity;  //添加/減少按鈕
    private SeekBar sbSuger;  //產品甜度
    private LinearLayout linearIce;
    private SeekBar sbProductIce;  //產品冰塊
    private LinearLayout linearHot;
    private SeekBar sbProductHot;  //產品熱度
    //加入購物車（設定在 activity_adding_product_to_shoppingcart 的 btnAddingProductToShoppingCard（onClickAddingProductToShoppingCard））

    private AddingProduct addingProduct; //購買的商品
    private int whereDoComeFrom;  //從哪裡來

    ShoppingCartDBHelper shoppingCartDBHelper = new ShoppingCartDBHelper(this);  //購物車資料庫

    /**
     * 程式與畫面 連結
     */
    private void programAndUILink() {
        //產品圖案
        ivProductImage = findViewById(R.id.ivProductImage);
        //產品數量
        tvQuantity = findViewById(R.id.tvQuantity);
        btnAddQuantity = findViewById(R.id.btnAddQuantity);
        btnBackQuantity = findViewById(R.id.btnBackQuantity);
        //產品甜度
        sbSuger = findViewById(R.id.sbSuger);
        //產品冰塊
        linearIce = findViewById(R.id.linearIce);
        sbProductIce = findViewById(R.id.sbProductIce);
        //產品熱度
        linearHot = findViewById(R.id.linearHot);
        sbProductHot = findViewById(R.id.sbProductHot);
    }

    /**
     * 初始化
     */
    private void initValue() {

        Bundle bundle = getIntent().getExtras();

        this.L = bundle.getInt("ProductLPrice");
        this.M = bundle.getInt("ProductMPrice");

        addingProduct = new AddingProductBuilder()
                .setMyCategory(bundle.getString("ProductCategory"))
                .setMyProductName(bundle.getString("ProductName"))
                .setMyHotOrIce(0)
                .setMySize(0)
                .setMySizePrice(M)
                .setMyQuantity(1)
                .setMySuger(0)
                .setMyTemperature(0)
                .createAddingProduct();  //設定產品初始值

        setTitle(addingProduct.getMyProductName());  // 產品名稱
        int productImage = bundle.getInt("ProductImage");
        ivProductImage.setImageResource(productImage);  //設定產品圖案
        linearIce.setVisibility(View.VISIBLE);  //開啟ICE的選項
        linearHot.setVisibility(View.GONE);  //關閉HOT的選項
        this.whereDoComeFrom = bundle.getInt("onProductPage");


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_product_to_shoppingcart);
        programAndUILink();
        initValue();
        getQuantity();
        getSuger();
        switch (addingProduct.getMyHotOrIce()) {
            case 0:
                getProductIce();
            case 1:
                getProductHot();
        }


    }

    /**
     * 觸發事件 按下冷熱飲（冷）
     */
    public void onClickIce(View view) {
        linearIce.setVisibility(View.VISIBLE);  //開啟ICE的選項
        linearHot.setVisibility(View.GONE);  //關閉HOT的選項
        addingProduct.setMyHotOrIce(0);
        sbProductIce.setProgress(0);
    }

    /**
     * 觸發事件 按下冷熱飲（熱）
     */
    public void onClickHot(View view) {
        linearHot.setVisibility(View.VISIBLE);  //開啟HOT的選項
        linearIce.setVisibility(View.GONE);  //關閉ICE的選項
        addingProduct.setMyHotOrIce(1);
        sbProductHot.setProgress(0);
    }

    /**
     * 觸發事件 按下容量（M）
     */
    public void onClickSizeM(View view) {
        addingProduct.setMySize(0);
        addingProduct.setMySizePrice(M);
    }

    /**
     * 觸發事件 按下容量（L）
     */
    public void onClickSizeL(View view) {
        addingProduct.setMySize(1);
        addingProduct.setMySizePrice(L);
    }

    /**
     * 取得數量
     */
    public void getQuantity() {
        btnAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrBack(1);
            }
        });

        btnBackQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrBack(-1);
            }
        });
    }

    /**
     * 增加或減少 數量
     * 減少不得少於 1
     * 增加無上限
     */
    private void addOrBack(int quantity) {
        int nowQuantity = Integer.valueOf(tvQuantity.getText().toString());
        if (nowQuantity + quantity == 0) {
            tvQuantity.setText(String.valueOf(Integer.valueOf(tvQuantity.getText().toString())));
            setQuantity();
        } else {
            tvQuantity.setText(String.valueOf(Integer.valueOf(tvQuantity.getText().toString()) + quantity));
            setQuantity();
        }

    }

    /**
     * 設定數量
     */
    private void setQuantity() {
        addingProduct.setMyQuantity(Integer.valueOf(tvQuantity.getText().toString()));
    }

    /**
     * 取得甜度
     */
    public void getSuger() {
        sbSuger.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                addingProduct.setMySuger(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 取得溫度（冷）
     */
    public void getProductIce() {
        sbProductIce.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                addingProduct.setMyTemperature(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    /**
     * 取得溫度（熱）
     */
    public void getProductHot() {
        sbProductHot.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                addingProduct.setMyTemperature(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 觸發事件 按下加入購物車
     */
    public void onClickAddingProductToShoppingCard(View view) {
        //儲存至資料庫 如果來源來自商品頁面就使用添加 如果來自編輯頁面就使用修改
        switch (whereDoComeFrom) {
            case 0:
                shoppingCartDBHelper.addProduct(addingProduct);
                        finish();
                break;
            case 1:
                break;
        }


//        Toast.makeText(this,
//                " 類別：" + addingProduct.getMyCategory() +
//                        " 名稱：" + addingProduct.getMyProductName() +
//                        " 冷熱飲：" + addingProduct.getMyHotOrIce() +
//                        " 容量：" + addingProduct.getMySize() +
//                        " 價錢：" + addingProduct.getMySizePrice() +
//                        " 數量：" + addingProduct.getMyQuantity() +
//                        " 甜度：" + addingProduct.getMySuger() +
//                        " 溫度：" + addingProduct.getMyTemperature()
//                , Toast.LENGTH_LONG).show();
    }

}
