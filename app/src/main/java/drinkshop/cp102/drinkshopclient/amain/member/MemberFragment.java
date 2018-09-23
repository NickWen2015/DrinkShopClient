package drinkshop.cp102.drinkshopclient.amain.member;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import drinkshop.cp102.drinkshopclient.Common;
import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.Member;
import drinkshop.cp102.drinkshopclient.task.MyTask;

import static android.content.Context.MODE_PRIVATE;

/**
 * 會員登入頁
 * @author Nick
 * @date 2018/8/30
 * */
public class MemberFragment extends Fragment {
    private static final String TAG = "MemberFragment";
    private MyTask loginTask, memberTask;
    private FragmentActivity fragmentActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_member, container, false);
        handleViews(view);
        return view;
    }

    private void handleViews(final View view) {

        final EditText etMember_account = view.findViewById(R.id.etMember_account);
        final EditText etMember_password = view.findViewById(R.id.etMember_password);


        Button btRegister = view.findViewById(R.id.btRegister);
        Button btLogin = view.findViewById(R.id.btLogin);

        /**
         * 導向會員註冊fragment
         **/
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberRegisterFragment memberRegisterFragment = new MemberRegisterFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content, memberRegisterFragment, "fragmentMemberRegister");
                transaction.addToBackStack("fragmentMemberRegister");//＊＊手動加到Stack裏，並給名字可以讓user等等可以返回指定頁，可以給名字或不給名字transaction.addToBackStack(null)，等等只能回上一頁，不能回指定頁
                transaction.commit();
            }
        });

        /**
         * 登入驗證,成功導向會員功能,失敗顯示訊息
         **/
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etMember_account.getText().toString().trim();
                String password = etMember_password.getText().toString().trim();
                boolean isValid = isUserValid(account, password);
                if (isValid) {
                    setLoginStatus();
                    Member member = getMemberData(account, password);
                    setLoginInfo(member.getMember_id(), member.getMember_account(), member.getMember_name());

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("member", member);
                    Toast.makeText(getActivity(), R.string.textLoginSuccess, Toast.LENGTH_SHORT).show();
                    MemberFunctionMenuFragment memberFunctionMenuFragment = new MemberFunctionMenuFragment();
                    memberFunctionMenuFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, memberFunctionMenuFragment, "fragmentMemberFunctionMenu");
                    transaction.addToBackStack("fragmentMemberFunctionMenu");//＊＊手動加到Stack裏，並給名字可以讓user等等可以返回指定頁，可以給名字或不給名字transaction.addToBackStack(null)，等等只能回上一頁，不能回指定頁
                    transaction.commit();
                } else {
                    String LoginError = getString(R.string.textLoginError);
                    etMember_account.setError(LoginError);
                    etMember_password.setError(LoginError);
                    Toast.makeText(getActivity(), R.string.textLoginError, Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });


    }

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
     * 判斷登入帳密是否正確,未來要改寫對應資料庫資料是否正確
     * */
    private boolean isUserValid(String account, String password) {
        String url = Common.URL + "/MemberServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "isUserValid");
        jsonObject.addProperty("name", account);
        jsonObject.addProperty("password", password);
        Log.d(TAG, "isUserValid jsonObject:"+ jsonObject.toString());
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

    @Override
    public void onStop() {
        super.onStop();
        if (loginTask != null) {
            loginTask.cancel(true);
            loginTask = null;
        }
        if (memberTask != null) {
            memberTask.cancel(true);
            memberTask = null;
        }
    }

    public void setLoginStatus() {
        SharedPreferences preferences =
                getActivity().getSharedPreferences("preference", MODE_PRIVATE);
        preferences.edit().putBoolean("login", true).apply();
    }

    public boolean getLoginStatus() {
        SharedPreferences preference = getActivity().getSharedPreferences("preference", MODE_PRIVATE);
        boolean login = preference.getBoolean("login", false);
        Log.d(TAG, "loginStatus = " + login);
        return login;
    }

    public void setLoginInfo(Integer member_id, String member_account, String member_name) {
        SharedPreferences preference =
                getActivity().getSharedPreferences("preference", MODE_PRIVATE);
        preference.edit().putInt("member_id", member_id).apply();
        preference.edit().putString("member_account", member_account).apply();
        preference.edit().putString("member_name", member_name).apply();
    }

}
