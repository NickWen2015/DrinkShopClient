package drinkshop.cp102.drinkshopclient.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import drinkshop.cp102.drinkshopclient.Common;
import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.helper.LogHelper;
import drinkshop.cp102.drinkshopclient.task.MyTask;

// 此Activity將會以對話視窗模式顯示，呼叫setResult()設定回傳結果
public class LoginDialogActivity extends AppCompatActivity {
    private static final String TAG = "LoginDialogActivity";
    private EditText etUser;
    private EditText etPassword;
    private TextView tvMessage;
    private MyTask loginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        handleViews();
        setResult(RESULT_CANCELED);//設定結果代碼預設為登入沒成功
    }

    private void handleViews() {
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        Button btLogin = findViewById(R.id.btLogin);
        //tvMessage = findViewById(R.id.tvMessage);

        btLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String user = etUser.getText().toString().trim();//去空白
                String password = etPassword.getText().toString().trim();
                if (user.length() <= 0 || password.length() <= 0) {
                    showMessage(R.string.msg_InvalidUserOrPassword);
                    return;
                }

                if (isUserValid(user, password)) {
                    SharedPreferences pref = getSharedPreferences(Common.PREF_FILE,
                            MODE_PRIVATE);//取得偏好設定檔,內部儲存體,私有檔案
                    pref.edit()
                            .putBoolean("login", true)
                            .putString("user", user)
                            .putString("password", password)
                            .apply();//存檔
                    setResult(RESULT_OK);
                    finish();//此頁消失
                } else {
                    showMessage(R.string.msg_InvalidUserOrPassword);
                }
            }
        });
    }

    @Override
    protected void onStart() {//畫面還沒呈現
        super.onStart();
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);
        if (login) {
            String name = pref.getString("user", "");
            String password = pref.getString("password", "");
            if (isUserValid(name, password)) {
                setResult(RESULT_OK);//登入成功設定結果代碼為成功
                finish();
            } else {
                pref.edit().putBoolean("login", false).apply();
                showMessage(R.string.msg_InvalidUserOrPassword);
            }
        }
    }

    private void showMessage(int msgResId) {
        //tvMessage.setText(msgResId);
    }

    /**
     * 連server驗證帳號密碼
     * */
    private boolean isUserValid(String name, String password) {
        String url = Common.URL + "/LoginServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("password", password);
        LogHelper.d(TAG, "jsonObject:" + jsonObject.toString());
        loginTask = new MyTask(url, jsonObject.toString());
//        boolean isUserValid = false;
        boolean isUserValid = true;
//        try {
//            String jsonIn = loginTask.execute().get();
//            jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
//            isUserValid = jsonObject.get("isUserValid").getAsBoolean();
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
        return isUserValid;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (loginTask != null) {
            loginTask.cancel(true);
        }
    }
}
