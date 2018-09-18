package drinkshop.cp102.drinkshopclient.amain.member;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import drinkshop.cp102.drinkshopclient.Common;
import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.Member;
import drinkshop.cp102.drinkshopclient.task.MyTask;

import static android.content.Context.MODE_PRIVATE;

/**
 * 會員註冊頁,會員修改頁
 *
 * @author Nick
 * @date 2018/8/30
 */
public class MemberRegisterFragment extends Fragment {
    private EditText etRegisterAccount, etRegisterPassword, etRegisterConfirmPassword,
            etRegisterPhone, etRegisterEmail, etRegisterAddress, etRegisterBirthday, etRegisterName;
    private Button btRegisterSubmit;
    private Member member;
    private FragmentActivity fragmentActivity;
    private static final String TAG = "MemberRegisterFragment";
    private FragmentManager fragmentManager;
    private SharedPreferences preferences;
    private boolean login;
    private int member_id;
    private MyTask memberDataTask, memberDataByIdTask, findNewMemberTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActivity = getActivity();
        fragmentManager = getFragmentManager();
        preferences = fragmentActivity.getSharedPreferences("preference", MODE_PRIVATE);
        login = preferences.getBoolean("login", false);
        if (login) {
            member_id = preferences.getInt("member_id", 0);
            if (member_id != 0) {
                if (networkConnected(fragmentActivity)) {//連server前先檢查
                    member = getMemberDataById(String.valueOf(member_id));
                }
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_member_register, container, false);
        handleViews(view);
        return view;
    }


    private void handleViews(View view) {
        etRegisterAccount = view.findViewById(R.id.etRegisterAccount);
        etRegisterPassword = view.findViewById(R.id.etRegisterPassword);
        etRegisterConfirmPassword = view.findViewById(R.id.etRegisterConfirmPassword);
        etRegisterName = view.findViewById(R.id.etRegisterName);
//        final RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
//        int radioButtonID = radioGroup.getCheckedRadioButtonId();
//        final RadioButton radioButton = radioGroup.findViewById(radioButtonID);
        etRegisterBirthday = view.findViewById(R.id.etRegisterBirthday);
        etRegisterPhone = view.findViewById(R.id.etRegisterPhone);
        etRegisterEmail = view.findViewById(R.id.etRegisterEmail);
        etRegisterAddress = view.findViewById(R.id.etRegisterAddress);
        btRegisterSubmit = view.findViewById(R.id.btRegisterSubmit);


        if (member != null) {
            etRegisterAccount.setText(member.getMember_account());
            etRegisterAccount.setEnabled(false);//編輯時無法改值
            etRegisterPassword.setText(member.getMember_password());
            etRegisterConfirmPassword.setText(member.getMember_password());
            etRegisterName.setText(member.getMember_name());
            etRegisterPhone.setText(member.getMember_mobile());
            etRegisterEmail.setText(member.getMember_email());
            etRegisterAddress.setText(member.getMember_address());
            etRegisterBirthday.setText(member.getMember_birthday());
        }

        btRegisterSubmit.setOnClickListener(new View.OnClickListener() {//修改資料及新增資料共用頁面
            @Override
            public void onClick(View view) {
                boolean isValid = isValid(etRegisterPassword, etRegisterConfirmPassword);
                if (!isValid) {
                    return;
                }

                /****準備要寫入db的資料****/
                String member_account = etRegisterAccount.getText().toString();
                String member_password = etRegisterPassword.getText().toString();
                String member_name = etRegisterName.getText().toString();
                String member_birthday = etRegisterBirthday.getText().toString();
                String member_sex = "1";
                String member_mobile = etRegisterPhone.getText().toString();
                String member_email = etRegisterEmail.getText().toString();
                String member_address = etRegisterAddress.getText().toString();

                int member_id = 0;
                Member memberdata;
                String action = "memberInsert";
                int messageFail = R.string.msg_InsertFail;
                int messageSuccess = R.string.msg_InsertSuccess;
                if (member != null) {
                    member_id = member.getMember_id();
                    memberdata = new Member(member_id, member_password, member_name, member_birthday, member_sex, member_mobile, member_email, member_address);
                    action = "memberUpdate";
                    messageFail = R.string.msg_UpdateFail;
                    messageSuccess = R.string.msg_UpdateSuccess;
                } else {
                    memberdata = new Member(member_account, member_password, member_name, member_birthday, member_sex, member_mobile, member_email, member_address);
                }

                if (networkConnected(fragmentActivity)) {
                    String url = Common.URL + "/MemberServlet";

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", action);
                    jsonObject.addProperty("member", new Gson().toJson(memberdata));

                    Log.d(TAG, action + "member jsonObject:" + jsonObject.toString());
                    int count = 0;
                    try {
                        memberDataTask = new MyTask(url, jsonObject.toString());
                        String result = memberDataTask.execute().get();
                        count = Integer.valueOf(result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(fragmentActivity, messageFail);
                        /* 回前一個Fragment */
                        fragmentManager.popBackStack();//資料寫入失敗就回前一頁
                    } else {
                        Common.showToast(fragmentActivity, messageSuccess);//若成功撈出DB將最新寫入的資料
                        if ("memberInsert".equals(action)) {
                            member = getNewMemberData();
                        } else {
                            member = getMemberDataById(String.valueOf(member_id));
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("member", member);

                        if (getLoginStatus() != true) {
                            setLoginStatus();
                            setLoginInfo(member.getMember_id(), member.getMember_account(), member.getMember_name());
                        }else {
                            setLoginInfo(member.getMember_id(), member.getMember_account(), member.getMember_name());
                        }

                        MemberFunctionMenuFragment memberFunctionMenuFragment = new MemberFunctionMenuFragment();
                        memberFunctionMenuFragment.setArguments(bundle);

                        if (fragmentManager != null) {
                            fragmentManager.beginTransaction().
                                    replace(R.id.content, memberFunctionMenuFragment).addToBackStack(null).commit();
                        }

                    }
                } else {
                    Common.showToast(fragmentActivity, R.string.msg_NoNetwork);
                    /* 回前一個Fragment */
                    fragmentManager.popBackStack();
                }

                /*將資料寫進資料庫並返回會員中心功能頁*/

            }
        });

    }

    /**
     * 檢查兩次輸入密碼是否相同
     */
    private boolean isValid(EditText registerPassword, EditText registerConfirmPassword) {
        /* 檢查輸入文字的格式是否為0~100的整數 */
        String textRegisterPassword = registerPassword.getText().toString();
        String textRegisterConfirmPassword = registerConfirmPassword.getText().toString();
        if (!textRegisterPassword.equals(textRegisterConfirmPassword)) {
            /* 如果輸入不正確則呼叫setError()，
               將錯誤訊息顯示在EditText右邊，並回傳false */
            String errorMessage = getString(R.string.textRegisterConfirmPasswordError);
            registerConfirmPassword.setError(errorMessage);
            return false;
        } else {
            return true;
        }

    }

    //檢查裝置是否連網
    private boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

    private Member getMemberDataById(String member_id) {
        String url = Common.URL + "/MemberServlet";
        Member member = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findById");
        jsonObject.addProperty("member_id", member_id);
        Log.d(TAG, "MemberData jsonObject:" + jsonObject.toString());
        memberDataByIdTask = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = memberDataByIdTask.execute().get();
            Log.d(TAG, "MemberData Result:" + jsonIn.toString());
            member = new Gson().fromJson(jsonIn, Member.class);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return member;
    }

    public boolean getLoginStatus() {
        SharedPreferences preference = getActivity().getSharedPreferences("preference", MODE_PRIVATE);
        boolean login = preference.getBoolean("login", false);
        Log.d(TAG, "loginStatus = " + login);
        return login;
    }

    public void setLoginStatus() {
        SharedPreferences preferences =
                fragmentActivity.getSharedPreferences("preference", MODE_PRIVATE);
        preferences.edit().putBoolean("login", true).apply();
    }

    public void setLoginInfo(Integer member_id, String member_account, String member_name) {
        SharedPreferences preference =
                fragmentActivity.getSharedPreferences("preference", MODE_PRIVATE);
        preference.edit().putInt("member_id", member_id).apply();
        preference.edit().putString("member_account", member_account).apply();
        preference.edit().putString("member_name", member_name).apply();
    }

    private Member getNewMemberData() {
        String url = Common.URL + "/MemberServlet";
        Member member = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findNewMember");
        Log.d(TAG, "findNewMember jsonObject:" + jsonObject.toString());
        findNewMemberTask = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = findNewMemberTask.execute().get();
            Log.d(TAG, "findNewMember Result:" + jsonIn.toString());
            member = new Gson().fromJson(jsonIn, Member.class);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return member;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (memberDataTask != null) {
            memberDataTask.cancel(true);
            memberDataTask = null;
        }
        if (memberDataByIdTask != null) {
            memberDataByIdTask.cancel(true);
            memberDataByIdTask = null;
        }

        if (findNewMemberTask != null) {
            findNewMemberTask.cancel(true);
            findNewMemberTask = null;
        }
    }

}
