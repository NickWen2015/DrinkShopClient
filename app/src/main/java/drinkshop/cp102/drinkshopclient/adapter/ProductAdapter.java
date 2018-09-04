package drinkshop.cp102.drinkshopclient.adapter;

import android.content.Context;
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
import drinkshop.cp102.drinkshopclient.activity.AddingProductToShoppingCartActivity;
import drinkshop.cp102.drinkshopclient.bean.Product;
import drinkshop.cp102.drinkshopclient.bean.ShoppingCart;
import drinkshop.cp102.drinkshopclient.helper.ShoppingCartDBHelper;

/**
 * 實作 RecyclerView.Adapter
 *
 * @author mrosstro
 * @date 2018/8/28
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    List<Product> products;

    /**
     * 初始化
     *
     * @author mrosstro
     * @date 2018/8/28
     */
    public ProductAdapter(Context context, LayoutInflater inflater, List<Product> products) {
        this.context = context;
        this.inflater = inflater;
        this.products = products;
    }

    /**
     * 你有幾筆資料？
     *
     * @author mrosstro
     * @date 2018/8/28
     */
    @Override
    public int getItemCount() {
        return products.size();
    }


    /**
     * 開始
     *
     * @author mrosstro
     * @date 2018/8/28
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /*載入item_view (間接依存輸入false 直接依存true)*/
        View item_view = inflater.inflate(R.layout.templatwe_product, viewGroup, false);
        return new MyViewHolder(item_view);
    }

    /**
     * 連結 item_view
     *
     * @author mrosstro
     * @date 2018/8/28
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

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {

        ShoppingCartDBHelper shoppingCartDBHelper = new ShoppingCartDBHelper(context);
        int totalQuantityOfProduct = 0;

        final Product product = products.get(position);

        if (shoppingCartDBHelper.readProductQuantity(product.getProductName()) != null) {
            for (ShoppingCart shoppingCart : shoppingCartDBHelper.readProductQuantity(product.getProductName())) {
                totalQuantityOfProduct += shoppingCart.getQuantity();
            }
        }

        MyViewHolder myViewHolder = viewHolder;
        myViewHolder.ivProductImg.setImageResource(product.getProductImg());
        myViewHolder.tvProductName.setText(product.getProductName());
        myViewHolder.tvProductMPrice.setText("M : NT $ " + String.valueOf(product.getProductMPrice()));
        myViewHolder.tvProductLPrice.setText("L : NT $ " + String.valueOf(product.getProductLPrice()));
        myViewHolder.tvProductQuantity.setText(String.valueOf(totalQuantityOfProduct));


        /* 檢查有無購買 */
        if (totalQuantityOfProduct == 0) {
            myViewHolder.ivFlag.setVisibility(View.INVISIBLE);
            myViewHolder.tvProductQuantity.setVisibility(View.INVISIBLE);
        } else {
            myViewHolder.ivFlag.setVisibility(View.VISIBLE);
            myViewHolder.tvProductQuantity.setVisibility(View.VISIBLE);

        }

        /*itemView 是 點擊的那個項目*/
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddingProductToShoppingCartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("onProductPage", 0);
                bundle.putString("ProductCategory", product.getProductCategory());
                bundle.putString("ProductName", product.getProductName());
                bundle.putInt("ProductImage", product.getProductImg());
                bundle.putInt("ProductMPrice", product.getProductMPrice());
                bundle.putInt("ProductLPrice", product.getProductLPrice());
                intent.putExtras(bundle);
                context.startActivity(intent);
//                Toast.makeText(context, product.getProductName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 傳入資訊
     *
     * @author mrosstro
     * @date 2018/8/28
     */
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position, @NonNull List<Object> payloads) {
//
//    }


}