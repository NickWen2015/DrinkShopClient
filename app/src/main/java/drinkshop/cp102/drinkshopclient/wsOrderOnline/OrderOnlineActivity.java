package drinkshop.cp102.drinkshopclient.wsOrderOnline;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import drinkshop.cp102.drinkshopclient.R;

public class OrderOnlineActivity extends AppCompatActivity {

    private static final String TAG = "OrderOnlineActivity";
    // 線上可聊天對象清單

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_online);
        // 如果無法取得user name就開啟登入用的LoginFragment
        String userName = getUserName();
        Fragment fragment;
        if (userName.isEmpty()) {
            fragment = new LoginFragment();
        } else {
            OrderOnlineCommon.connectServer(userName, this);
            fragment = new FriendsFragment();
        }
        switchFragment(fragment, false);
    }

    public void switchFragment(Fragment fragment, boolean backStack) {
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        if (backStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void setUserName(String userName) {
        SharedPreferences preferences =
                getSharedPreferences("wsuser", MODE_PRIVATE);
        preferences.edit().putString("wsuserName", userName).apply();
    }

    public String getUserName() {
        SharedPreferences preferences =
                getSharedPreferences("wsuser", MODE_PRIVATE);
        String userName = preferences.getString("wsuserName", "");
        Log.d(TAG, "wsuserName = " + userName);
        return userName;
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int stringId) {
        Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 登出即中斷WebSocket連線
            case R.id.logout:
                setTitle("Not Login");
                setUserName("");
                OrderOnlineCommon.disconnectServer();
                Fragment fragment = new LoginFragment();
                switchFragment(fragment, false);
                showToast(R.string.textLogout);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
