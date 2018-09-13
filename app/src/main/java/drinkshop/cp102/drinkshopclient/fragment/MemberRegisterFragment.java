package drinkshop.cp102.drinkshopclient.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


import java.util.Calendar;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.activity.MainActivity;
import drinkshop.cp102.drinkshopclient.bean.Member;

/**
 * 會員註冊頁
 *
 * @author Nick
 * @date 2018/8/30
 */
public class MemberRegisterFragment extends Fragment {
    private EditText etRegisterAccount, etRegisterPassword, etRegisterConfirmPassword,
            etRegisterPhone, etRegisterEmail, etRegisterAddress, etRegisterBirthday;
    private Button btRegisterSubmit;
    private Member member;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            member = (Member) bundle.getSerializable("member");
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
            etRegisterPassword.setText(member.getMember_password());
            etRegisterConfirmPassword.setText(member.getMember_password());
            etRegisterPhone.setText(member.getMember_mobile());
            etRegisterEmail.setText(member.getMember_email());
            etRegisterAddress.setText(member.getMember_address());
            if (member.getMember_birthday() != null && member.getMember_birthday().length()>0){
                etRegisterBirthday.setText(member.getMember_birthday());
            }
        }

        btRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = isValid(etRegisterPassword, etRegisterConfirmPassword);
                if (!isValid) {
                    return;
                }
                int id = 0;
                if(member != null){
                    id = member.getMember_id();
                }
                String account = etRegisterAccount.getText().toString().trim();
                String password = etRegisterPassword.getText().toString().trim();
                String confirmPassword = etRegisterConfirmPassword.getText().toString().trim();
//                String sex = radioButton.getText().toString();
                String birthday = etRegisterBirthday.getText().toString().trim();
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
