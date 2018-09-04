package drinkshop.cp102.drinkshopclient.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.ShoppingCart;
import drinkshop.cp102.drinkshopclient.decoder.ProductDetails;


public class ShoppinCartListAdapter extends RecyclerView.Adapter<ShoppinCartListAdapter.MyViewHolder> {
    private Context context;
    private List<ShoppingCart> shoppingCartList;

    public ShoppinCartListAdapter(Context context, List<ShoppingCart> shoppingCartList) {
        this.context = context;
        this.shoppingCartList = shoppingCartList;
    }

    @Override
    public int getItemCount() {
        return shoppingCartList.size();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.templatwe_shopping_cart, viewGroup, false);
        return new MyViewHolder(itemView);
    }

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

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        final ShoppingCart shoppingCart = shoppingCartList.get(position);
        ProductDetails productDetails = new ProductDetails();

        String showProductName = shoppingCart.getProductName();
        String showQuantity = String.valueOf(shoppingCart.getQuantity());
        String showPrice = String.valueOf(shoppingCart.getSizePrice());
        String showSubtotal = String.valueOf(shoppingCart.getQuantity() * shoppingCart.getSizePrice());
        String showHotOrIceSizeSugerTemperature = productDetails.valueOfSize(shoppingCart.getSize()) + "/" +
                productDetails.valueOfHotOrice(shoppingCart.getHotOrice()) + "/" +
                productDetails.valueOfSuger(shoppingCart.getSuger()) + "/" +
                productDetails.valueOfTemperature(shoppingCart.getHotOrice(), shoppingCart.getTemperature());


        viewHolder.tvProductName.setText(showProductName);
        viewHolder.tvQuantity.setText("*" + showQuantity + "杯");
        viewHolder.tvPrice.setText("(單價：NT$" + showPrice + ")");
        viewHolder.tvSubtotal.setText("小計NT$" + showSubtotal);
        viewHolder.tvHotOrIceSizeSugerTemperature.setText(showHotOrIceSizeSugerTemperature);

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


}