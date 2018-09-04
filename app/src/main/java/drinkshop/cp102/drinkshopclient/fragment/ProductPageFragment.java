package drinkshop.cp102.drinkshopclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.adapter.ProductAdapter;
import drinkshop.cp102.drinkshopclient.bean.Product;

public class ProductPageFragment extends Fragment {
    private RecyclerView rvRecyclerView;
    private View viewPagerFragment;
    ProductAdapter adapter;

    private List<Product> getProduct() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(
                "夏季特賣",
                R.drawable.product_activity_test_a001,
                "＠柚美粒",
                55,
                60,
                0));

        products.add(new Product(
                "果汁",
                R.drawable.product_activity_test_a002,
                "柚香多輕飲",
                45,
                50,
                0));

        products.add(new Product(
                "果汁",
                R.drawable.product_activity_test_a003,
                "蘋果多輕飲",
                50,
                55,
                0));
        return products;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewPagerFragment = inflater.inflate(R.layout.fragment_productpage, container, false);

        rvRecyclerView = viewPagerFragment.findViewById(R.id.rvRecyclerView);
        rvRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));  //主管 使用線性顯示（外模板）
        if (adapter == null) {
            adapter = new ProductAdapter(getActivity(), inflater, getProduct());
        }
        rvRecyclerView.setAdapter(adapter);  //員工（內模板）
        Log.e("ProductPageFragment","onCreateView");
        return viewPagerFragment;
    }


    public void updateRecyclerView() {
        adapter.notifyDataSetChanged();
    }
}
