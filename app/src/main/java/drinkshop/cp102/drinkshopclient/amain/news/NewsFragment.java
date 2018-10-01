package drinkshop.cp102.drinkshopclient.amain.news;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.News;
import drinkshop.cp102.drinkshopclient.server.Common;
import drinkshop.cp102.drinkshopclient.server.task.CommonTask;

/**
 * 輪播牆
 * @author
 * */
public class NewsFragment extends Fragment {
    public static final String TAG = "NewsFragment";
    private ViewPager viewPager;
    private boolean isRunning = true;  //開關（暫無作用）
    private FragmentActivity fragmentActivity;

    /**
     * 初始化
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(inflater, container);
        fragmentActivity = getActivity();
        return viewPager;

    }

    /**
     * 連結 初始化資料 定時器
     */
    @SuppressLint("ClickableViewAccessibility")
    public void initView(LayoutInflater inflater, ViewGroup container) {
        View newsFragment = inflater.inflate(R.layout.news_fragment, container, false);
        viewPager = newsFragment.findViewById(R.id.view_pager);
        initData();
        startLunBO();
    }

    /**
     * 取資料 轉型
     * */
    private List<News> getNews() {
        List<News> newsList = new ArrayList<>();
        if (Common.networkConnected(getActivity())) {  //檢查有無網路
            String url = Common.URL + "/NewsServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getNews_activity_id"); //向server取得所有
            String jsonOut = jsonObject.toString();
            try {
                String result = new CommonTask(url, jsonOut).execute().get(); //CommonTask是JSON去JSON回
                Type listType = new TypeToken<List<News>>() {  //泛型用TypeToken
                }.getType();
                newsList = new Gson().fromJson(result, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        return newsList;
    }

    /**
     * 初始化資料
     * */
    private void initData() {
        List<News> myNews = getNews();

        for(News news : myNews) {
            Log.e(TAG, "News = " + news.getActivity_id());
        }

        NewsAdapter viewPagerAdapter = new NewsAdapter(myNews, getActivity());
        viewPager.setAdapter(viewPagerAdapter);

        int pos = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % myNews.size());
        viewPager.setCurrentItem(pos);
    }

    /**
     * 定時器 每2秒刷新一次 viewPager
     * */
    private void startLunBO() {
        //開啟輪播
        new Thread() {
            public void run() {
                while (isRunning) {
                    SystemClock.sleep(3000);
                    fragmentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }.start();
    }



}
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import drinkshop.cp102.drinkshopclient.bean.News;
//import drinkshop.cp102.drinkshopclient.R;
//
//public class NewsFragment extends Fragment {
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.news_fragment, container, false);
//        List<News> newsList = new ArrayList<>();
//        newsList.add(new News("Title 1", "Detail 1"));
//        newsList.add(new News("Title 2", "Detail 2"));
//        newsList.add(new News("Title 3", "Detail 3"));
//
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(new NewsAdapter(inflater, newsList));
//        return view;
//    }
//
//    private class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
//        private LayoutInflater inflater;
//        private List<News> newsList;
//        private View visibleView;
//
//        NewsAdapter(LayoutInflater inflater, List<News> newsList) {
//            this.inflater = inflater;
//            this.newsList = newsList;
//        }
//
//        @Override
//        public int getItemCount() {
//            return newsList.size();
//        }
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//            TextView tvTitle, tvDetail;
//
//            ViewHolder(View itemView) {
//                super(itemView);
//                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
//                tvDetail = (TextView) itemView.findViewById(R.id.tvDetail);
//            }
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//            View itemView = inflater.inflate(R.layout.news_item, viewGroup, false);
//            return new ViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
//            News news = newsList.get(position);
//            viewHolder.tvTitle.setText(news.getTitle());
//            viewHolder.tvDetail.setText(news.getDetail());
//            viewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    switch (viewHolder.tvDetail.getVisibility()) {
//                        case View.VISIBLE:
//                            viewHolder.tvDetail.setVisibility(View.GONE);
//                            break;
//                        case View.GONE:
//                            if (visibleView != null) {
//                                visibleView.setVisibility(View.GONE);
//                            }
//                            viewHolder.tvDetail.setVisibility(View.VISIBLE);
//                            visibleView = viewHolder.tvDetail;
//                            break;
//                    }
//                }
//            });
//        }
//    }
//}
