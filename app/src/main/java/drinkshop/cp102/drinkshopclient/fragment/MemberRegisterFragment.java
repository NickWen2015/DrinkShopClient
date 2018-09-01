package drinkshop.cp102.drinkshopclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;



import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.Member;

/**
 * 會員註冊頁
 * @author Nick
 * @date 2018/8/30
 */
public class MemberRegisterFragment extends Fragment {


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
        View view = inflater.inflate(R.layout.fragment_member_register, container, false);
        handleViews(view);
        return view;
    }

    private void handleViews(View view) {
        final EditText etRegisterAccount = view.findViewById(R.id.etRegisterAccount);//final原理為，下面內部類要用
        final EditText etRegisterPassword = view.findViewById(R.id.etRegisterPassword);
        final EditText etRegisterConfirmPassword = view.findViewById(R.id.etRegisterConfirmPassword);
//        final RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
//        int radioButtonID = radioGroup.getCheckedRadioButtonId();
//        final RadioButton radioButton = radioGroup.findViewById(radioButtonID);
        final EditText etRegisterPhone = view.findViewById(R.id.etRegisterPhone);
        final EditText etRegisterEmail = view.findViewById(R.id.etRegisterEmail);
        final EditText etRegisterAddress = view.findViewById(R.id.etRegisterAddress);
        Button btRegisterSubmit = view.findViewById(R.id.btRegisterSubmit);

        Bundle bundle = getArguments();
        if(bundle != null) {
            Bundle bundleMemberData = bundle.getBundle("bundleMemberData");
            Bundle bundleMemberStatus = bundle.getBundle("bundleMemberStatus");
            if (bundleMemberData != null) {
                Member member2modify = (Member) bundleMemberData.getSerializable("member2modify");
                if (member2modify != null) {
                    etRegisterAccount.setText(member2modify.getMember_account());
                    etRegisterPassword.setText(member2modify.getMember_password());
                    etRegisterConfirmPassword.setText(member2modify.getMember_password());
                    etRegisterPhone.setText(member2modify.getMember_mobile());
                    etRegisterEmail.setText(member2modify.getMember_email());
                    etRegisterAddress.setText(member2modify.getMember_address());
                }

            }
            if (bundleMemberStatus != null) {
                if (bundleMemberStatus.getString("modify").equals("modify")) {
                    btRegisterSubmit.setText(R.string.textModifySubmit);
                }
            }
        }
        btRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = isValid(etRegisterPassword, etRegisterConfirmPassword);
                if (!isValid) {
                    return;
                }

                String account = etRegisterAccount.getText().toString().trim();
                String password = etRegisterPassword.getText().toString().trim();
                String confirmPassword = etRegisterConfirmPassword.getText().toString().trim();
//                String sex = radioButton.getText().toString();
                String phone = etRegisterPhone.getText().toString().trim();
                String email = etRegisterEmail.getText().toString().trim();
                String address = etRegisterAddress.getText().toString().trim();

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
}
