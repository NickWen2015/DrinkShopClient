package drinkshop.cp102.drinkshopclient.amain.productpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.amain.addingproduct.AddingProductToShoppingCartActivity;
import drinkshop.cp102.drinkshopclient.bean.Product;
import drinkshop.cp102.drinkshopclient.bean.ShoppingCart;
import drinkshop.cp102.drinkshopclient.constant.MainActivityConstant;
import drinkshop.cp102.drinkshopclient.helper.LogHelper;
import drinkshop.cp102.drinkshopclient.helper.ShoppingCartDBHelper;

/**
 * 實作 RecyclerView.Adapter（建立 ProductPage(RecyclerView) 畫面）
 *
 * @author mrosstro
 * @date 2018/9/10
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    public static final String TAG = "ProductAdapter";
    private Activity activity;
    private LayoutInflater inflater;
    private List<Product> products;

    private ShoppingCartDBHelper shoppingCartDBHelper;  //資料庫

    /**
     * 初始化
     */
    public ProductAdapter(Activity activity, List<Product> products) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.products = products;
    }

    /**
     * 你有幾筆資料？
     */
    @Override
    public int getItemCount() {
        shoppingCartDBHelper = new ShoppingCartDBHelper(activity);
        return products.size();
    }


    /**
     * 開始
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /*載入item_view (間接依存輸入false 直接依存true)*/
        View item_view = inflater.inflate(R.layout.templatwe_product, viewGroup, false);
        return new MyViewHolder(item_view);
    }

    /**
     * 連結 templatwe_product UI 元件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImg;
        TextView tvProductName;
        TextView tvProductMPrice;
        TextView tvProductLPrice;
        TextView tvProductQuantity;
        ImageView ivFlag;

        public MyViewHolder(@NonNull View item_view) {
            super(item_view);
            ivProductImg = item_view.findViewById(R.id.ivProductImg);
            tvProductName = item_view.findViewById(R.id.tvProductName);
            tvProductMPrice = item_view.findViewById(R.id.tvProductMPrice);
            tvProductLPrice = item_view.findViewById(R.id.tvProductLPrice);
            tvProductQuantity = item_view.findViewById(R.id.tvProductQuantity);
            ivFlag = item_view.findViewById(R.id.ivFlag);
        }
    }

    /**
     * 產生畫面及處理觸發事件
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        final Product product = products.get(position);
        int totalQuantityOfProduct = 0;

        /* 取得產品數量 */
        if (shoppingCartDBHelper.readProductQuantity(product.getName()) != null) {
            for (ShoppingCart shoppingCart : shoppingCartDBHelper.readProductQuantity(product.getName())) {
                totalQuantityOfProduct += shoppingCart.getQuantity();
            }
        }
        /* 設定內容 */
        viewHolder.tvProductName.setText(product.getName());
        viewHolder.tvProductMPrice.setText("M : NT $ " + String.valueOf(product.getMPrice()));
        viewHolder.tvProductLPrice.setText("L : NT $ " + String.valueOf(product.getLPrice()));
        viewHolder.tvProductQuantity.setText(String.valueOf(totalQuantityOfProduct));

        /* 檢查有無購買 */
        if (totalQuantityOfProduct == 0) {
            viewHolder.ivFlag.setVisibility(View.INVISIBLE);
            viewHolder.tvProductQuantity.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.ivFlag.setVisibility(View.VISIBLE);
            viewHolder.tvProductQuantity.setVisibility(View.VISIBLE);
        }

        LogHelper.e(TAG, "onBindViewHolder：" + product.getName() + " 建立成功");

        /*itemView 是 點擊的那個項目*/
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AddingProductToShoppingCartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("WhereDoComeFrom", 0);
                bundle.putInt("ProductID", product.getId());
                intent.putExtras(bundle);
                activity.startActivityForResult(intent, MainActivityConstant.PRODUCT_PAGE);
                LogHelper.d(TAG,"onBindViewHolder：選取" + product.getName() + "成功");
            }
        });
    }

}