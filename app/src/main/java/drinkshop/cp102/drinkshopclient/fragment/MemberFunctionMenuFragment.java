package drinkshop.cp102.drinkshopclient.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import drinkshop.cp102.drinkshopclient.Common;
import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.activity.LoginDialogActivity;
import drinkshop.cp102.drinkshopclient.bean.Member;
import drinkshop.cp102.drinkshopclient.task.MyTask;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * 會員功能頁
 *
 * @author Nick
 * @date 2018/8/30
 */
public class MemberFunctionMenuFragment extends Fragment {

    private FragmentActivity activity;
    private TextView txt_username;
    private static final int REQ_LOGIN = 2;
    private Member member;
    private boolean login;
    private SharedPreferences preferences;
    private MyTask memberTask;;
    private ImageView ivTakePicture;
    private Button btLogout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        preferences = getActivity().getSharedPreferences("preference", MODE_PRIVATE);
        login = preferences.getBoolean("login", false);

    Bundle bundle = getArguments();
        if (bundle != null){//登入帶資料過來
            member = (Member)bundle.getSerializable("member");
        }else{//已經登入直接取資料
            int member_id = preferences.getInt("member_id", 0);
            member = getMemberData(member_id);
        }

    }


    private Member getMemberData(int member_id) {
        String url = Common.URL + "/MemberServlet";
        Member member = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findById");
        jsonObject.addProperty("member_id", member_id);
        Log.d(TAG, "findById jsonObject:"+ jsonObject.toString());
        memberTask = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = memberTask.execute().get();
            Log.d(TAG, "findById Member Result:"+ jsonIn.toString());
            member = new Gson().fromJson(jsonIn, Member.class);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return member;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_member_function_menu, container, false);
        handleViews(view);
        return view;
    }

    private void handleViews(final View view) {
        final CardView cvMemberModify = view.findViewById(R.id.cvMemberModify);
        final CardView cvMemberHistory = view.findViewById(R.id.cvMemberHistory);
        final CardView cvMemberOrderStatus = view.findViewById(R.id.cvMemberOrderStatus);
        final CardView cvMemberCoupon = view.findViewById(R.id.cvMemberCoupon);
        txt_username = view.findViewById(R.id.txt_username);
        String member_name = preferences.getString("member_name", "");
        String member_account = preferences.getString("member_account", "");
        if(login){
            if(member_name != null && member_name.length()>0){
                txt_username.setText(member_name);
            }else{
                txt_username.setText(member_account);
            }
        }else{
            txt_username.setText("hello");
        }

        cvMemberModify.setOnClickListener(listener);
        cvMemberCoupon.setOnClickListener(listener);

    }

    /**
     * 會員功能按鈕監聽器,監聽按哪個功能
     **/
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.cvMemberModify://會員資料修改

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("member", member);
                    MemberRegisterFragment memberRegisterFragment = new MemberRegisterFragment();
                    memberRegisterFragment.setArguments(bundle);
                    FragmentTransaction transactionModify = getFragmentManager().beginTransaction();
                    transactionModify.replace(R.id.content, memberRegisterFragment, "fragmentMemberRegisterModify");
                    transactionModify.addToBackStack("fragmentMemberRegisterModify");
                    transactionModify.commit();
                    break;
                case R.id.cvMemberHistory://訂單歷史紀錄

                    MemberHistoryFragment memberHistoryFragment = new MemberHistoryFragment();
                    FragmentTransaction transactionHistory = getFragmentManager().beginTransaction();
                    transactionHistory.replace(R.id.content, memberHistoryFragment, "fragmentMemberHistory");
                    transactionHistory.addToBackStack("fragmentMemberHistory");
                    transactionHistory.commit();
                    break;
                case R.id.cvMemberOrderStatus://訂單狀態查詢

                    break;
                case R.id.cvMemberCoupon://會員優惠卷管理

                    MemberCouponFragment memberCouponFragment = new MemberCouponFragment();
                    FragmentTransaction transactionCoupon = getFragmentManager().beginTransaction();
                    transactionCoupon.replace(R.id.content, memberCouponFragment, "fragmentMemberCoupon");
                    transactionCoupon.addToBackStack("fragmentMemberCoupon");//＊＊手動加到Stack裏，並給名字可以讓user等等可以返回指定頁，可以給名字或不給名字transaction.addToBackStack(null)，等等只能回上一頁，不能回指定頁
                    transactionCoupon.commit();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
//只要拍完照onActivityResult會被呼叫,回來會呼叫callback method,監聽方法,系統偵測到會自動呼叫,有被請求回來才會自動call,onStart是只要返回就會呼叫
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 也可取得自行設計登入畫面的帳號密碼
                case REQ_LOGIN:
                    SharedPreferences pref = activity.getSharedPreferences(Common.PREF_FILE,
                            MODE_PRIVATE);//取偏好設定
                    if (networkConnected()) {
//                        String url = Common.URL + "/DataUploadServlet";
//                        String name = pref.getString("user", "");//取不到值預設空字串
                        //byte[] image = Common.bitmapToPNG(picture);//拿記憶體的圖或儲存體的皆可
//                        JsonObject jsonObject = new JsonObject();
//                        jsonObject.addProperty("name", name);
//                        jsonObject.addProperty("imageBase64", Base64.encodeToString(image, Base64.DEFAULT));
                        //dataUploadTask = new MyTask(url, jsonObject.toString());
                        try {
//                            String jsonIn = dataUploadTask.execute().get();
//                            JsonObject jObject = new Gson().fromJson(jsonIn, JsonObject.class);
//                            name = jObject.get("name").getAsString();
//                            image = Base64.decode(jObject.get("imageBase64").getAsString(), Base64.DEFAULT);
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);//從0讀到總長度
//
//                            TextView tvResultUser = findViewById(R.id.tvResultUser);
//                            ImageView ivResultImage = findViewById(R.id.ivResponseImage);
//                            String text = "name: " + name;
//                            tvResultUser.setText(text);
//                            ivResultImage.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    } else {
                        Common.showToast(activity, R.string.msg_NoNetwork);
                    }
                    break;
            }
        }
    }

    //檢查裝置是否連網
    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (memberTask != null) {
            memberTask.cancel(true);
        }
    }

}
