package drinkshop.cp102.drinkshopclient.amain.productpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import drinkshop.cp102.drinkshopclient.server.Common;
import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.Product;
import drinkshop.cp102.drinkshopclient.helper.LogHelper;
import drinkshop.cp102.drinkshopclient.server.task.CommonTask;

public class ProductPageFragment extends Fragment {
    public static final String TAG = "ProductPageFragment";
    private ProductAdapter adapter;
    private FragmentActivity activity;
    private CommonTask productGetAllProductTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewPagerFragment = inflater.inflate(R.layout.fragment_productpage, container, false);
        activity = getActivity();
        ProductRecyclerView(viewPagerFragment, inflater);
        LogHelper.e(TAG, "onCreateView：成功");
        return viewPagerFragment;
    }

    /**
     * 商品列表（RecyclerView）
     * */
    private void ProductRecyclerView(View viewPagerFragment, LayoutInflater inflater) {
        RecyclerView rvRecyclerView = viewPagerFragment.findViewById(R.id.rvRecyclerView);
        rvRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));  //主管 使用線性顯示（外模板）
        if (adapter == null) {  //如果adapter沒有被產生出來就產生
            adapter = new ProductAdapter(activity, getProduct());
        }
        rvRecyclerView.setAdapter(adapter);  //員工（內模板）
        LogHelper.e(TAG, "ProductRecyclerView： 成功");
    }

    /**
     * 去資料庫取得商品資料 並回傳結果
     *
     * @return products 商品資訊（不含圖片）
     * */
    private List<Product> getProduct() {
        List<Product> products = null;
        if (Common.networkConnected(activity)) {
            LogHelper.e(TAG, "執行讀取資料");
            String url = Common.URL + "/ProductServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllProduct");
            String jsonOut = jsonObject.toString();
            productGetAllProductTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = productGetAllProductTask.execute().get();
                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                products = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                LogHelper.e(TAG, e.toString());
            }
            if (products == null || products.isEmpty()) {
                Common.showToast(activity, R.string.msg_NoProductsFound);
            }
        } else {
            Common.showToast(activity, R.string.msg_NoNetwork);
        }
        return products;
    }

    /**
     * 重新更新畫面
     * */
    public void updateRecyclerView() {
        adapter.notifyDataSetChanged();
        LogHelper.e(TAG, "updateRecyclerView：更新成功");
    }

}
