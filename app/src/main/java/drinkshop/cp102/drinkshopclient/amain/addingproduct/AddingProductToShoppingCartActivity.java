package drinkshop.cp102.drinkshopclient.amain.addingproduct;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.Product;
import drinkshop.cp102.drinkshopclient.bean.ShoppingCart;
import drinkshop.cp102.drinkshopclient.helper.LogHelper;
import drinkshop.cp102.drinkshopclient.helper.ShoppingCartDBHelper;
import drinkshop.cp102.drinkshopclient.server.Common;
import drinkshop.cp102.drinkshopclient.server.task.CommonTask;
import drinkshop.cp102.drinkshopclient.task.ImageTask;


/**
 * 冷 = 1 熱 = 2
 * M = 1 L = 2
 * 正常糖 = 1 少糖 = 2 半糖 = 3 微糖 = 4 無糖 = 5
 * 正常冰 = 1 少冰 = 2 微冰 = 3 去冰 = 4 正常熱 = 5 熱一點 = 6
 * 添加選取的商品至購物車
 *
 * @author mrosstro
 * @date 2018/8/29
 */
public class AddingProductToShoppingCartActivity extends AppCompatActivity {
    public static final String TAG = "AddingProductToShoppingCartActivity";

    private ImageView ivProductImage;  //產品圖案
    //產品冷熱（btnHot（onClickHot） 和 btnIce（onClickIce））
    //產品尺寸（btnSizeM（onClickSizeM） 和 btnSizeL（onClickSizeL）
    private int L, M;  //尺寸的價錢
    private TextView tvQuantity;  //產品數量
    private Button btnAddQuantity, btnBackQuantity;  //添加/減少按鈕
    private SeekBar sbSuger;  //產品甜度
    private LinearLayout linearIce;
    private SeekBar sbProductIce;  //產品冰塊
    private LinearLayout linearHot;
    private SeekBar sbProductHot;  //產品熱度
    private Button btnAddingProductToShoppingCart;  //加入/修改 購物車（btnAddingProductToShoppingCard（onClickAddingProductToShoppingCard））

    private ShoppingCart addingProduct; //購買的商品
    private int whereDoComeFrom;  //從哪裡來

    ShoppingCartDBHelper shoppingCartDBHelper = new ShoppingCartDBHelper(this);  //購物車資料庫
    private CommonTask productGetProductDetailTask;
    private ImageTask productImageTask;
    private Activity activity;
    List<Product> productDetail;

    /**
     * 程式與畫面 連結
     */
    private void programAndUILink() {
        //產品圖案
        ivProductImage = findViewById(R.id.ivProductImage);
        //產品冷熱
        //產品尺寸
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
        //加入/修改 購物車
        btnAddingProductToShoppingCart = findViewById(R.id.btnAddingProductToShoppingCart);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_product_to_shoppingcart);
        programAndUILink();
        initValue();
        getQuantity();
        getSuger();
        switch (addingProduct.getHotOrIce()) {
            case 1:
                getProductIce();
            case 2:
                getProductHot();
        }
    }

    /**
     * 初始化
     */
    private void initValue() {
        activity = this;
        int index = 0;
        Bundle bundle = getIntent().getExtras();
        int productID = bundle.getInt("ProductID");

        String url = drinkshop.cp102.drinkshopclient.Common.URL + "/ProductServlet";
        int productImageSize = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            productImageSize = ivProductImage.getHeight();
            LogHelper.e(TAG, "productImageSize = " + productImageSize);
        }
        productImageTask = new ImageTask(url ,productID ,productImageSize ,ivProductImage);
        productImageTask.execute();

        productDetail = getProductDetail(productID);

        /* 從哪裡來（新增（ProductPageFragment）= 0 ; 購物車的編輯（ShoppingCartActivity）= 1） */
        this.whereDoComeFrom = bundle.getInt("WhereDoComeFrom");
        this.L = productDetail.get(index).getLPrice();
        this.M = productDetail.get(index).getMPrice();
        switch (whereDoComeFrom) {
            case 0:
                //新增商品
                addingProduct = new ShoppingCart(
                        productDetail.get(index).getId(),
                        productDetail.get(index).getCategory(),
                        productDetail.get(index).getName(),
                        1,
                        1,
                        M,
                        1,
                        1,
                        1);

                setTitle(addingProduct.getProductName());  // 產品名稱
                linearIce.setVisibility(View.VISIBLE);  //開啟ICE的選項
                linearHot.setVisibility(View.GONE);  //關閉HOT的選項
                btnAddingProductToShoppingCart.setText(R.string.text_AddingProductToShoppingCard);
                break;

            case 1:
                //編輯商品
                int shoppingCardID = bundle.getInt("ShoppingCardID");
                List<ShoppingCart> editProductDetail = shoppingCartDBHelper.readEditProductDetail(shoppingCardID);
                addingProduct = new ShoppingCart(
                        shoppingCardID,
                        editProductDetail.get(index).getProductID(),
                        editProductDetail.get(index).getCategory(),
                        editProductDetail.get(index).getProductName(),
                        editProductDetail.get(index).getHotOrIce(),
                        editProductDetail.get(index).getSize(),
                        editProductDetail.get(index).getSizePrice(),
                        editProductDetail.get(index).getQuantity(),
                        editProductDetail.get(index).getSuger(),
                        editProductDetail.get(index).getTemperature());

                setTitle(addingProduct.getProductName());  // 產品名稱
                tvQuantity.setText(String.valueOf(addingProduct.getQuantity()));  // 產品數量
                sbSuger.setProgress(addingProduct.getSuger() - 1);  //產品甜度
                switch (addingProduct.getHotOrIce()) {
                    case 1:
                        linearIce.setVisibility(View.VISIBLE);  //開啟ICE的選項
                        linearHot.setVisibility(View.GONE);  //關閉HOT的選項
                        sbProductIce.setProgress(addingProduct.getTemperature() - 1);  // 產品溫度
                        break;
                    case 2:
                        linearHot.setVisibility(View.VISIBLE);  //開啟HOT的選項
                        linearIce.setVisibility(View.GONE);  //關閉ICE的選項
                        sbProductHot.setProgress(addingProduct.getTemperature() - 1);  // 產品溫度
                        break;
                }
                btnAddingProductToShoppingCart.setText(R.string.text_EditProductToShoppingCart);
                break;
        }
    }

    /**
     * 取的雲端資料庫商品詳細
     */
    private List<Product> getProductDetail(int productID) {
        List<Product> productDetail = null;
        if (Common.networkConnected(activity)) {
            LogHelper.e(TAG, "執行讀取資料");
            String url = Common.URL + "/ProductServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getProductDetail");
            jsonObject.addProperty("productID", productID);
            //****//
            String jsonOut = jsonObject.toString();
            productGetProductDetailTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = productGetProductDetailTask.execute().get();
                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                productDetail = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                LogHelper.e(TAG, e.toString());
            }
            if (productDetail == null || productDetail.isEmpty()) {
                Common.showToast(activity, R.string.msg_NoProductsFound);
            }
        } else {
            Common.showToast(activity, R.string.msg_NoNetwork);
        }
        return productDetail;
    }

    /**
     * 觸發事件 按下冷熱飲（冷）
     */
    public void onClickIce(View view) {
        linearIce.setVisibility(View.VISIBLE);  //開啟ICE的選項
        linearHot.setVisibility(View.GONE);  //關閉HOT的選項
        addingProduct.setHotOrIce(1);
        sbProductIce.setProgress(0);
    }

    /**
     * 觸發事件 按下冷熱飲（熱）
     */
    public void onClickHot(View view) {
        linearHot.setVisibility(View.VISIBLE);  //開啟HOT的選項
        linearIce.setVisibility(View.GONE);  //關閉ICE的選項
        addingProduct.setHotOrIce(2);
        sbProductHot.setProgress(0);
    }

    /**
     * 觸發事件 按下容量（M）
     */
    public void onClickSizeM(View view) {
        addingProduct.setSize(1);
        addingProduct.setSizePrice(M);
    }

    /**
     * 觸發事件 按下容量（L）
     */
    public void onClickSizeL(View view) {
        addingProduct.setSize(2);
        addingProduct.setSizePrice(L);
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
        addingProduct.setQuantity(Integer.valueOf(tvQuantity.getText().toString()));
    }

    /**
     * 取得甜度
     */
    public void getSuger() {
        sbSuger.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                addingProduct.setSuger(progress + 1);
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
                addingProduct.setTemperature(progress + 1);
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
                addingProduct.setTemperature(progress + 5);
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
    public void onClickAddingProductToShoppingCart(View view) {
        //儲存至資料庫 如果來源來自商品頁面就使用添加 如果來自編輯頁面就使用修改
        switch (whereDoComeFrom) {
            case 0:
                shoppingCartDBHelper.addProduct(addingProduct);
                finish();
                break;
            case 1:
                shoppingCartDBHelper.updateProduct(addingProduct.getID(), addingProduct);
                finish();
                break;
        }
    }
}
