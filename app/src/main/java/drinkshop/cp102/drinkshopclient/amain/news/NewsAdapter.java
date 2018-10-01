package drinkshop.cp102.drinkshopclient.amain.news;


import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

import drinkshop.cp102.drinkshopclient.Common;
import drinkshop.cp102.drinkshopclient.bean.News;
import drinkshop.cp102.drinkshopclient.task.ImageTask;

/**
 * 自訂
 * @author
 */
public class NewsAdapter extends PagerAdapter {
    public static final String TAG = "NewsAdapter";
    private List<News> mNews;
    private Activity activity;

    public NewsAdapter(List<News> news, Activity activity) {
        this.mNews = news;
        this.activity = activity;
    }

    /**
     * 設定為無限大（警告 viewPager 從最後一個跳到第一個會像倒帶一樣）
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    /**
     * 這個函數就是用來告訴框架，這個view的id是不是這個object。
     * */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 這是pageAdapter裡的函數，功能就是往瀏覽量裡添加自己需要的page，同時注意它還有個返回值object，這就是那個id。
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = new ImageView(activity);
        position = position % mNews.size();
        if (position < 0) {
            position = mNews.size() + position;
        }
        String url = Common.URL + "/NewsServlet";
        int id = mNews.get(position).getActivity_id();
        int imageSize = activity.getResources().getDisplayMetrics().widthPixels;
        try {
            ImageTask newsImageTask = new ImageTask(url, id, imageSize);
            view.setImageBitmap((Bitmap) newsImageTask.execute().get());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Log.e(TAG, "view = " + view.toString());
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }

    /**
     * 刪除
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 警告：別在這寫！
    }
}
