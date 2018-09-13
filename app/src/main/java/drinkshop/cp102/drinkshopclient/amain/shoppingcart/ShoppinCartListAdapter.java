package drinkshop.cp102.drinkshopclient.amain.shoppingcart;

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
import drinkshop.cp102.drinkshopclient.bean.ShoppingCart;
import drinkshop.cp102.drinkshopclient.bean.ShoppingCartTotol;
import drinkshop.cp102.drinkshopclient.constant.MainActivityConstant;
import drinkshop.cp102.drinkshopclient.constant.ShoppingCartActivityConstant;
import drinkshop.cp102.drinkshopclient.decoder.ProductDetails;
import drinkshop.cp102.drinkshopclient.helper.LogHelper;
import drinkshop.cp102.drinkshopclient.helper.ShoppingCartDBHelper;

/**
 * 實作 RecyclerView.Adapter（建立 ShoppingCartActivity(RecyclerView) 畫面）
 *
 * @author mrosstro
 * @date 2018/9/7
 */
public class ShoppinCartListAdapter extends RecyclerView.Adapter<ShoppinCartListAdapter.MyViewHolder> {
    public static final String TAG = "ShoppinCartListAdapter";
    private Activity activity;
    private List<ShoppingCart> shoppingCartList;  //購物車內容物
    ShoppingCartDBHelper shoppingCartDBHelper;
    WeakReference<TextView> textViewWeakReferenceTotalAmount;
    WeakReference<TextView> textViewWeakReferenceTotalCap;

    public ShoppinCartListAdapter(Activity activity, TextView tvTotalCap,TextView tvTotalAmount) {
        this.activity = activity;
        this.textViewWeakReferenceTotalAmount = new WeakReference<>(tvTotalAmount);
        this.textViewWeakReferenceTotalCap = new WeakReference<>(tvTotalCap);
    }

    /**
     * 購物車資料庫
     * */
    private List<ShoppingCart> getShoppingCardList(){
        shoppingCartDBHelper = new ShoppingCartDBHelper(activity);  //資料庫
        return shoppingCartDBHelper.readAllProduct();
    }

    /**
     * 你有幾筆資料？
     */
    @Override
    public int getItemCount() {
        shoppingCartList = getShoppingCardList();
        return shoppingCartList.size();
    }

    /**
     * 開始
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.templatwe_shopping_cart, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    /**
     * 連結 templatwe_shopping_cart UI 元件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView tvQuantity;
        TextView tvPrice;
        TextView tvSubtotal;
        ImageView ivProductImage;
        TextView tvHotOrIceSizeSugerTemperature;
        Button btnEdit;
        Button btnDelete;

        MyViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvHotOrIceSizeSugerTemperature = itemView.findViewById(R.id.tvHotOrIceSizeSugerTemperature);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    /**
     * 產生畫面及處理觸發事件
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        final ShoppingCart shoppingCart = shoppingCartList.get(position);
        ProductDetails productDetails = new ProductDetails();

        setTotalCapAndTotalAmount();  //設定總金額 及 總杯數

        //定義 UI 內容
        String showProductName = shoppingCart.getProductName();
        int showQuantity = shoppingCart.getQuantity();
        String showPrice = String.valueOf(shoppingCart.getSizePrice());
        int showSubtotal = shoppingCart.getQuantity() * shoppingCart.getSizePrice();
        String showHotOrIceSizeSugerTemperature = productDetails.valueOfSize(shoppingCart.getSize()) + "/" +
                productDetails.valueOfHotOrice(shoppingCart.getHotOrIce()) + "/" +
                productDetails.valueOfSuger(shoppingCart.getSuger()) + "/" +
                productDetails.valueOfTemperature(shoppingCart.getHotOrIce(), shoppingCart.getTemperature());

        //設定 UI 內容
        viewHolder.tvProductName.setText(showProductName);
        viewHolder.tvQuantity.setText("*" + showQuantity + "杯");
        viewHolder.tvPrice.setText("(單價：NT$" + showPrice + ")");
        viewHolder.tvSubtotal.setText("小計NT$" + showSubtotal);
        viewHolder.tvHotOrIceSizeSugerTemperature.setText(showHotOrIceSizeSugerTemperature);

        /* 按下編輯按鈕 */
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AddingProductToShoppingCartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("WhereDoComeFrom", 1);
                bundle.putInt("ShoppingCardID", shoppingCart.getID());
                bundle.putInt("ProductID", shoppingCart.getProductID());
                intent.putExtras(bundle);
                activity.startActivityForResult(intent, ShoppingCartActivityConstant.SHOPPING_CART);
                LogHelper.e(TAG, "onBindViewHolder：編輯 " + shoppingCart.getProductName());
            }
        });

        /* 按下刪除按鈕 */
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.e(TAG, "onBindViewHolder：刪除 " + shoppingCart.getID() + " " + shoppingCart.getProductName());
                shoppingCartDBHelper.deleteProduct(shoppingCart.getID());
                ShoppinCartListAdapter.this.notifyDataSetChanged();
                setTotalCapAndTotalAmount();  //設定總金額 及 總杯數
            }
        });

    }

    /**
     * 設定總金額及總杯數
     * */
    private void setTotalCapAndTotalAmount() {
        int totalCap = 0;
        int totalAmount = 0;
        List<ShoppingCartTotol> shoppingCartTotols = shoppingCartDBHelper.readTotalCapAndTotalAmount();
        for(ShoppingCartTotol shoppingCartTotol : shoppingCartTotols) {
            totalCap += shoppingCartTotol.getQuantity();
            totalAmount += shoppingCartTotol.getQuantity() * shoppingCartTotol.getPrice();
        }
        textViewWeakReferenceTotalCap.get().setText("總杯數：" + totalCap);
        textViewWeakReferenceTotalAmount.get().setText("總金額：" + totalAmount);
        LogHelper.e(TAG, "setTotalCapAndTotalAmount：\n總杯數：" + totalCap + "\n總金額：" + totalAmount);
    }
}