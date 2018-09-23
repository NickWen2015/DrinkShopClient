package drinkshop.cp102.drinkshopclient.amain.orderrecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.amain.addingproduct.AddingProductToShoppingCartActivity;
import drinkshop.cp102.drinkshopclient.bean.OrderDetail;
import drinkshop.cp102.drinkshopclient.bean.ShoppingCart;
import drinkshop.cp102.drinkshopclient.bean.ShoppingCartTotol;
import drinkshop.cp102.drinkshopclient.constant.ShoppingCartActivityConstant;
import drinkshop.cp102.drinkshopclient.decoder.ProductDetails;
import drinkshop.cp102.drinkshopclient.helper.LogHelper;
import drinkshop.cp102.drinkshopclient.helper.ShoppingCartDBHelper;

/**
 * 實作 RecyclerView.Adapter（建立 ShoppingCartActivity(RecyclerView) 畫面）
 *
 * @author mrosstro
 */
public class OrderRecordAdapter extends RecyclerView.Adapter<OrderRecordAdapter.MyViewHolder> {
    public static final String TAG = "OrderRecordAdapter";
    private Activity activity;
    private List<OrderDetail> orderDetails;

    public OrderRecordAdapter(Activity activity, List<OrderDetail> orderDetails) {
        this.activity = activity;
        this.orderDetails = orderDetails;
    }

    /**
     * 你有幾筆資料？
     */
    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    /**
     * 開始
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.templatwe_order_detail, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    /**
     * 連結 templatwe_order_detail UI 元件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView tvProductDetail;
        TextView tvProductQuantity;

        MyViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductDetail = itemView.findViewById(R.id.tvProductDetail);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
        }
    }

    /**
     * 產生畫面及處理觸發事件
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        final OrderDetail orderDetail = orderDetails.get(position);

        //定義 UI 內容
        String productName = orderDetail.getProduct_name();
        String productDetail = orderDetail.getSize_name() + "/" +
                orderDetail.getSugar_name() + "/" +
                orderDetail.getIce_name() + "/" +
                "$" + orderDetail.getProduct_price();
        int productQuantity = orderDetail.getProduct_quantity();

        //設定 UI 內容
        viewHolder.tvProductName.setText(productName);
        viewHolder.tvProductDetail.setText(productDetail);
        viewHolder.tvProductQuantity.setText("x" + productQuantity);

    }
}