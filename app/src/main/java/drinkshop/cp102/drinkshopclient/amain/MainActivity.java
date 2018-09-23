package drinkshop.cp102.drinkshopclient.amain;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import drinkshop.cp102.drinkshopclient.Common;
import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.amain.member.MemberFragment;
import drinkshop.cp102.drinkshopclient.amain.member.MemberFunctionMenuFragment;
import drinkshop.cp102.drinkshopclient.amain.news.NewsFragment;
import drinkshop.cp102.drinkshopclient.amain.productpage.ProductPageFragment;
import drinkshop.cp102.drinkshopclient.amain.settings.SettingsFragment;
import drinkshop.cp102.drinkshopclient.amain.shoppingcart.ShoppingCartActivity;
import drinkshop.cp102.drinkshopclient.helper.BottomNavigationViewHelper;
import drinkshop.cp102.drinkshopclient.helper.ShoppingCartDBHelper;

/**
 * APP的入口
 *
 * @author mrosstro
 * @date 2018/8/28
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public Menu menuLogin;
    public MenuItem menuitemLogin;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;
    ShoppingCartDBHelper shoppingCartDBHelper = new ShoppingCartDBHelper(this);  //建立資料庫 避免使用者提前按購物車

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
                        if (getLoginStatus()) {
                            fragment = new MemberFunctionMenuFragment();
                        } else {
                            fragment = new MemberFragment();
                        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.productpage_menu, menu);
        MenuItem item = menu.findItem(R.id.logout);

        if (getLoginStatus()) {//判斷是否已登入,若未登入則不會出現登出按鈕
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemShoppingCart:
                Intent intentGoToShoppingCartActivity = new Intent(this, ShoppingCartActivity.class);
                startActivity(intentGoToShoppingCartActivity);
                break;
            case R.id.logout:
                setTitle(R.string.textNotLogin);
                SharedPreferences preference = getSharedPreferences("preference", MODE_PRIVATE);
                boolean login = preference.getBoolean("login", false);
                preference.edit().putBoolean("login", false).apply();//將偏好設定檔改為為登出
                preference.edit().putInt("member_id", 0).apply();
                preference.edit().putString("member_account", "").apply();
                preference.edit().putString("member_name", "").apply();
                Common.showToast(this, R.string.textLogout);
                item.setVisible(false);
                Fragment fragment = new MemberFragment();
                changeFragment(fragment);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * 開始
     */
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
     * 進入畫面
     */
    private void initContent() {
        Fragment fragment = new NewsFragment();
        changeFragment(fragment);
        setTitle(R.string.textNews);
    }

    /**
     * 替換 fragment
     */
    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 返回頁面執行
     */
    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        fragment = fragmentManager.findFragmentById(R.id.content);
        if (fragment != null && fragment instanceof ProductPageFragment) {
            ((ProductPageFragment) fragment).updateRecyclerView();
        }
    }

    public boolean getLoginStatus() {
        SharedPreferences pref = getSharedPreferences("preference", MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);
        Log.d(TAG, "loginStatus = " + login);
        return login;
    }

    /* popups the alert dialog after the user clicks back button
    and back stack entry count is 0 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int count = getSupportFragmentManager().getBackStackEntryCount();//取得裡面存了幾個歷史紀錄
        if (keyCode == KeyEvent.KEYCODE_BACK && count == 0) {
            new AlertDialogFragment().show(getSupportFragmentManager(), "exit");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class AlertDialogFragment
            extends DialogFragment implements DialogInterface.OnClickListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.text_Exit)
                    .setIcon(R.drawable.ic_alert)
                    .setMessage(R.string.msg_WantToExit)
                    .setPositiveButton(R.string.text_Yes, this)
                    .setNegativeButton(R.string.text_No, this)
                    .create();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                    break;
                default:
                    dialog.cancel();
                    break;
            }
        }
    }
}
