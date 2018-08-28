package drinkshop.cp102.drinkshopclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import drinkshop.cp102.drinkshopclient.R;

public class MemberRegisterFragment extends Fragment {

    /****會員註冊頁****/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity()!=null){
            getActivity().setTitle(R.string.textMemberRegister);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.member_register_fragment, container, false);
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

        btRegisterSubmit.setOnClickListener(new View.OnClickListener() {//傳統型
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

    /* 檢查兩次輸入密碼是否相同 */
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
