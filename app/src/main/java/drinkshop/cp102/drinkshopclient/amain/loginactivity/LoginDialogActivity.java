package drinkshop.cp102.drinkshopclient.amain.loginactivity;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import drinkshop.cp102.drinkshopclient.Common;
import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.amain.member.MemberFunctionMenuFragment;
import drinkshop.cp102.drinkshopclient.bean.Member;
import drinkshop.cp102.drinkshopclient.helper.LogHelper;
import drinkshop.cp102.drinkshopclient.task.MyTask;

// 此Activity將會以對話視窗模式顯示，呼叫setResult()設定回傳結果
public class LoginDialogActivity extends AppCompatActivity {
    private static final String TAG = "LoginDialogActivity";
    private EditText etaccount;
    private EditText etPassword;

    private MyTask loginTask, memberTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        handleViews();
        setResult(RESULT_CANCELED);//設定結果代碼預設為登入沒成功
    }

    private void handleViews() {
        etaccount = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
    }

    /**
     * 按下登入
     * */
    public void onClickLogin(View view) {
        String account = etaccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean isValid = isUserValid(account, password);  //帳密是否正確
        if (isValid) {
            setLoginStatus();
            Member member = getMemberData(account, password);  //取得會員資料
            setLoginInfo(member.getMember_id(), member.getMember_account(), member.getMember_name());  //將會員資料 id, account, name
            finish();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("member", member);
//            Toast.makeText(this, R.string.textLoginSuccess, Toast.LENGTH_SHORT).show();
//            MemberFunctionMenuFragment memberFunctionMenuFragment = new MemberFunctionMenuFragment();
//            memberFunctionMenuFragment.setArguments(bundle);
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(R.id.content, memberFunctionMenuFragment, "fragmentMemberFunctionMenu");
//            transaction.addToBackStack("fragmentMemberFunctionMenu");//＊＊手動加到Stack裏，並給名字可以讓user等等可以返回指定頁，可以給名字或不給名字transaction.addToBackStack(null)，等等只能回上一頁，不能回指定頁
//            transaction.commit();
        } else {
            String LoginError = getString(R.string.textLoginError);
            etaccount.setError(LoginError);
            etPassword.setError(LoginError);
            Toast.makeText(this, R.string.textLoginError, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 判斷登入帳密是否正確,未來要改寫對應資料庫資料是否正確
     * */
    private boolean isUserValid(String account, String password) {
        String url = Common.URL + "/MemberServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "isUserValid");
        jsonObject.addProperty("name", account);
        jsonObject.addProperty("password", password);
        LogHelper.e(TAG, "isUserValid jsonObject:"+ jsonObject.toString());
        loginTask = new MyTask(url, jsonObject.toString());
        boolean isUserValid = false;
        try {
            String jsonIn = loginTask.execute().get();
            if(jsonIn.equals("true")){
                isUserValid = true;
            }else{
                isUserValid = false;
            }
            Log.d(TAG, "isUserValid Result:"+ isUserValid);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return isUserValid;
    }

    /**
     * 設定 偏好設定檔（SharedPreferences） login = true;
     * */
    public void setLoginStatus() {
        SharedPreferences preferences =
                this.getSharedPreferences("preference", MODE_PRIVATE);
        preferences.edit().putBoolean("login", true).apply();
    }

    /**
     * 取得資料
     * */
    private Member getMemberData(String account, String password) {
        String url = Common.URL + "/MemberServlet";
        Member member = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findMemberByAccountAndPassword");
        jsonObject.addProperty("name", account);
        jsonObject.addProperty("password", password);
        Log.d(TAG, "isUserValid jsonObject:"+ jsonObject.toString());
        memberTask = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = memberTask.execute().get();
            Log.d(TAG, "Member Result:"+ jsonIn.toString());
            member = new Gson().fromJson(jsonIn, Member.class);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return member;
    }

    /**
     * 將回傳值 設定 到 偏好設定檔（SharedPreferences）member_id, member_account, member_name
     * */
    public void setLoginInfo(Integer member_id, String member_account, String member_name) {
        SharedPreferences preference =
                this.getSharedPreferences("preference", MODE_PRIVATE);
        preference.edit().putInt("member_id", member_id).apply();
        preference.edit().putString("member_account", member_account).apply();
        preference.edit().putString("member_name", member_name).apply();
    }
}
