package drinkshop.cp102.drinkshopclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.Member;

/**
 * 會員登入頁
 * @author Nick
 * @date 2018/8/30
 * */
public class MemberFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            getActivity().setTitle(R.string.textMemberRegister);
        }
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

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberRegisterFragment memberRegisterFragment = new MemberRegisterFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content, memberRegisterFragment, "fragmentMemberRegister");
                /* 將Fragment異動前的狀態儲存至返回堆疊，
                   方便用戶點擊返回鍵後返回上一頁。
                   給予名稱則方便之後呼叫popBackStack()直接返回 */
                transaction.addToBackStack("fragmentMemberRegister");//＊＊手動加到Stack裏，並給名字可以讓user等等可以返回指定頁，可以給名字或不給名字transaction.addToBackStack(null)，等等只能回上一頁，不能回指定頁
                transaction.commit();
            }
        });


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etMember_account.getText().toString();
                String password = etMember_password.getText().toString();
                boolean isValid = vaildationAccountAndPassword(account, password);
                if (isValid) {
                    Bundle bundle = new Bundle();
                    Member member = new Member(1,account, password, "Nick", "2000-01-01", "0912345678", "y2ka.tw@yahoo.com.tw", "中央大學", "1");//暫時帶入帳號及會員編號
                    bundle.putSerializable("member", member);
                    Toast.makeText(getActivity(), R.string.textLoginSuccess, Toast.LENGTH_SHORT).show();
                    MemberFunctionMenuFragment memberFunctionMenuFragment = new MemberFunctionMenuFragment();
                    memberFunctionMenuFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, memberFunctionMenuFragment, "fragmentMemberFunctionMenu");
                /* 將Fragment異動前的狀態儲存至返回堆疊，
                   方便用戶點擊返回鍵後返回上一頁。
                   給予名稱則方便之後呼叫popBackStack()直接返回 */
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

    /**
     * 判斷登入帳密是否正確,未來要改寫對應資料庫資料是否正確
     * */
    private boolean vaildationAccountAndPassword(String account, String password) {
        if (account.equals("nick") && password.equals("0000")) {
            return true;
        } else {
            return false;
        }

    }

}
