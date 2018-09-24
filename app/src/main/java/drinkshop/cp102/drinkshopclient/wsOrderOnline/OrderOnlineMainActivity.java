package drinkshop.cp102.drinkshopclient.wsOrderOnline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import drinkshop.cp102.drinkshopclient.R;

public class OrderOnlineMainActivity extends AppCompatActivity {
    private static final String TAG = "OrderOnlineMainActivity";
    private EditText etUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_online_main);
        etUserName = findViewById(R.id.etUserName);
        if(getLoginStatus()){
            String[] memberInfo = getMemberInfo();
            if(!memberInfo[0].equals("0") && !memberInfo[1].equals("")){
                etUserName.setText(memberInfo[2]+"(id:"+memberInfo[0]+")");
                etUserName.setEnabled(false);
            }
        }
    }

    public void clickAllChat(View view) {
        Intent intent = new Intent(this, AllChatActivity.class);
        intent.putExtra("wsuserName", etUserName.getText().toString());//也可以用bundle
        startActivity(intent);
    }

    public void clickTwoChat(View view) {
        String userName = etUserName.getText().toString();
        OrderOnlineCommon.setUserName(this, userName);//存偏好設定檔
        Intent intent = new Intent(this, OrderOnlineActivity.class);
        startActivity(intent);
    }
    public boolean getLoginStatus() {
        SharedPreferences pref = getSharedPreferences("preference", MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);
        Log.d(TAG, "loginStatus = " + login);
        return login;
    }

    public String[] getMemberInfo() {
        String[] memberInfo = new String[3];
        SharedPreferences pref =
                getSharedPreferences("preference", MODE_PRIVATE);
        memberInfo[0] = String.valueOf(pref.getInt("member_id", 0));
        memberInfo[1] = pref.getString("member_account", "");
        memberInfo[2] = pref.getString("member_name", "");
        return memberInfo;
    }
}
