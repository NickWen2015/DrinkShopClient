package drinkshop.cp102.drinkshopclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.Member;

/**
 * 會員功能頁
 * @author Nick
 * @date 2018/8/30
 * */
public class MemberFunctionMenuFragment extends Fragment {

    private Member member2modify;
    @Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String title = "Hi ";
    Bundle bundle = getArguments();
    Member member = (Member)bundle.getSerializable("member");
//    member2modify = new Member(member.getMember_id(), member.getMember_account(), member.getMember_password(), member.getMember_name(), member.getMember_birthday(), member.getMember_mobile(), member.getMember_email(), member.getMember_address(), member.getMember_status());;
    member2modify = member;
    title += member.getMember_name();
    if(getActivity()!=null){
        getActivity().setTitle(title);
    }
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

        cvMemberModify.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.cvMemberModify:
                    Bundle mainBundle = new Bundle();
                    Bundle bundleMemberData = new Bundle();
                    Bundle bundleMemberStatus = new Bundle();
                    MemberRegisterFragment memberRegisterFragment = new MemberRegisterFragment();
                    bundleMemberData.putSerializable("member2modify", member2modify);
                    bundleMemberStatus.putString("modify","modify");
                    mainBundle.putBundle("bundleMemberData",bundleMemberData);
                    mainBundle.putBundle("bundleMemberStatus",bundleMemberStatus);
                    memberRegisterFragment.setArguments(mainBundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, memberRegisterFragment, "fragmentMemberRegisterModify");
                    transaction.addToBackStack("fragmentMemberRegisterModify");
                    transaction.commit();
                    break;
                case R.id.cvMemberHistory:
                    break;
                case R.id.cvMemberOrderStatus:
                    break;
                case R.id.cvMemberCoupon:
                    break;
                default:
                    break;
            }
        }
    };
}
