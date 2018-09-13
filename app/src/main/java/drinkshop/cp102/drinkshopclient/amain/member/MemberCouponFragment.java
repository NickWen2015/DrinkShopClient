package drinkshop.cp102.drinkshopclient.amain.member;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.Coupon;
import drinkshop.cp102.drinkshopclient.bean.Member;

/**
 * 優惠卷管理頁
 * @author Nick
 * @date 2018/9/1
 */
public class MemberCouponFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            getActivity().setTitle(R.string.textMemberCoupon);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_member_coupon, container, false);
        handleViews(view);
        return view;
    }

    private void handleViews(View view) {
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//建一個線性佈局管理器，將context資源傳入
        recyclerView.setAdapter(new CouponAapter(getCoupons(), getContext()));//必須為RecyclerView.Adapter內部類別的子類別，先做
    }
    private class CouponAapter extends RecyclerView.Adapter {
        //變數等等要給下面使用
        List<Coupon> Coupons;
        Context context;

        public CouponAapter(List<Coupon> Coupons, Context context) {//建構式
            this.Coupons = Coupons;
            this.context = context;
        }

        @Override
        public int getItemCount() {//旅遊景點有幾個資料
            return Coupons.size();
        }

        private class MyViewHolder extends RecyclerView.ViewHolder {//建造一個類別hold住三個資源
            ImageView ivCouponPic;
            TextView tvCouponDate, tvCouponStatus;

            public MyViewHolder(View item_view) {//未來會有三個ViewHolder
                super(item_view);//呼叫父類別的建構式，並設定父類item_view
                ivCouponPic = item_view.findViewById(R.id.ivCoupon_pic);//要寫物件item_view.findViewById，否則會找activity_main
                tvCouponDate = item_view.findViewById(R.id.tvCoupon_date);
                tvCouponStatus = item_view.findViewById(R.id.tvCoupon_status);
            }
        }

        @NonNull
        @Override//View
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View item_view = layoutInflater.inflate(R.layout.coupon_item_view, parent, false);
            return new MyViewHolder(item_view);//建構式並呼叫服類別初始物件
        }


        @Override//Binding
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final Coupon coupon =Coupons.get(position);//根據位置判斷
            MyViewHolder myViewHolder = (MyViewHolder)holder;
            myViewHolder.ivCouponPic.setImageResource(R.drawable.coupon_icon);
            myViewHolder.tvCouponDate.setText(coupon.getCoupon_start() + " - " + coupon.getCoupon_end());
            myViewHolder.tvCouponStatus.setText(coupon.getCoupon_status());
            myViewHolder.ivCouponPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context,coupon.getCoupon_discount(),Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    //建Coupon物件
    private List<Coupon> getCoupons() {
        List<Coupon> Coupons = new ArrayList<>();
        Coupons.add(new Coupon("1", "1", "001", "5折", "未使用", "2018-09-01", "2019-01-01"));
        Coupons.add(new Coupon("2", "1", "002", "6折", "未使用", "2018-09-01", "2019-06-01"));
        Coupons.add(new Coupon("3", "1", "003", "8折", "未使用", "2018-09-01", "2019-12-01"));
        return Coupons;
    }

}
