package drinkshop.cp102.drinkshopclient.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.amain.news.NewsFragment;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initContent();

    }

    /**
     * 第一次進入畫面
     * */
    private void initContent() {
        Fragment fragment = new NewsFragment();
        changeFragment(fragment);
        setTitle("ViewPager");
    }

    /**
     * 替換畫面
     * */
    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }
}
