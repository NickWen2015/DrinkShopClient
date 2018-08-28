package drinkshop.cp102.drinkshopclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import drinkshop.cp102.drinkshopclient.R;

public class MemberFragment extends Fragment {

    /****會員登入頁****/

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
        View view = inflater.inflate(R.layout.member_fragment, container, false);
        handleViews(view);
        return view;
    }

    private void handleViews(View view) {

        EditText etMember_account = view.findViewById(R.id.etMember_account);

        Button btRegister = view.findViewById(R.id.btRegister);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberRegisterFragment memberRegisterFragment = new MemberRegisterFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content, memberRegisterFragment, "memberRegisterFragment");
                /* 將Fragment異動前的狀態儲存至返回堆疊，
                   方便用戶點擊返回鍵後返回上一頁。
                   給予名稱則方便之後呼叫popBackStack()直接返回 */
                transaction.addToBackStack("memberRegister");//＊＊手動加到Stack裏，並給名字可以讓user等等可以返回指定頁，可以給名字或不給名字transaction.addToBackStack(null)，等等只能回上一頁，不能回指定頁
                transaction.commit();
            }
        });


    }

}
