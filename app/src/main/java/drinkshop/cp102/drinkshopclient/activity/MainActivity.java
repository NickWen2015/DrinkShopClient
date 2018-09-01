package drinkshop.cp102.drinkshopclient.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import drinkshop.cp102.drinkshopclient.fragment.MemberFragment;
import drinkshop.cp102.drinkshopclient.fragment.NewsFragment;
import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.fragment.ProductPageFragment;
import drinkshop.cp102.drinkshopclient.fragment.SettingsFragment;
import drinkshop.cp102.drinkshopclient.helper.BottomNavigationViewHelper;

/**
 * APP的入口
 * @author mrosstro
 * @date 2018/8/28
 * */
public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;

    public MainActivity() {
        onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.item_News:  //最新消息
                fragment = new NewsFragment();
                changeFragment(fragment);
                setTitle(R.string.textNews);
                return true;
            case R.id.item_Product:  //產品頁面
                fragment = new ProductPageFragment();
                changeFragment(fragment);
                setTitle(R.string.textProduct);
                return true;
            case R.id.item_Member:  //會員中心
                fragment = new MemberFragment();
                changeFragment(fragment);
                setTitle(R.string.textMember);
                return true;
            case R.id.item_Settings:  //設定
                fragment = new SettingsFragment();
                changeFragment(fragment);
                setTitle(R.string.textSettings);
                return true;
            default:
                initContent();
                break;
        }
        return false;
    }

};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Navigation（底下的選單列） */
        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);  // 使用 BottomNavigationViewHelper 改變造型
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        initContent();
    }

    /**
     * 初始畫面
     * */
    private void initContent() {
        Fragment fragment = new NewsFragment();
        changeFragment(fragment);
        setTitle(R.string.textNews);
    }

    /**
     * 替換 fragment
     * */
    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }
}
