package drinkshop.cp102.drinkshopclient.wsOrderOnline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import drinkshop.cp102.drinkshopclient.R;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final OrderOnlineActivity activity = (OrderOnlineActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText etUserName = view.findViewById(R.id.etUserName);
        Button btLogin = view.findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = etUserName.getText().toString();
                if (etUserName.getText().toString().trim().isEmpty()) {
                    activity.showToast("姓名欄位是空的");
                    return;
                }
                // 將user name存入偏好設定
                activity.setUserName(userName);
                Fragment fragment = new FriendsFragment();
                activity.switchFragment(fragment, false);
                OrderOnlineCommon.connectServer(userName, activity);
            }
        });
        return view;
    }

}
