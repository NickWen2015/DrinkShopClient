package drinkshop.cp102.drinkshopclient.amain.member;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import drinkshop.cp102.drinkshopclient.Common;
import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.activity.MemberHistoryDetailActivity;
import drinkshop.cp102.drinkshopclient.activity.MemberOrderDetailActivity;
import drinkshop.cp102.drinkshopclient.bean.Member;
import drinkshop.cp102.drinkshopclient.bean.Order;
import drinkshop.cp102.drinkshopclient.bean.OrderDetail;
import drinkshop.cp102.drinkshopclient.task.MyTask;

/**
 * 訂單紀錄查詢
 *
 * @author Nick
 * @date 2018/9/1
 */
public class MemberOrderFragment extends Fragment {
    private SharedPreferences preferences;
    private static final String TAG = "MemberHFragment";
    private FragmentActivity fragmentActivity;
    private MyTask orderHistoryTask;
    private Member member;
    private int member_id = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentActivity = getActivity();
        if (fragmentActivity != null) {
            fragmentActivity.setTitle(R.string.textMemberMenuOrderStatus);

        }
        Bundle bundle = getArguments();

        if (bundle != null) {//登入帶資料過來或註冊成功跳轉頁面過來
            member = (Member) bundle.getSerializable("member");
            member_id = member.getMember_id();
        } else {//已經登入直接取資料
            member_id = preferences.getInt("member_id", 0);
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
        recyclerView.setAdapter(new OrderAapter(getOrders(member_id), getContext()));//必須為RecyclerView.Adapter內部類別的子類別，先做
    }

    private class OrderAapter extends RecyclerView.Adapter {
        //變數等等要給下面使用
        List<Order> orders;
        Context context;

        public OrderAapter(List<Order> orders, Context context) {//建構式
            this.orders = orders;
            this.context = context;
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "orders.size():" + orders.size());
            return orders.size();
        }

        private class MyViewHolder extends RecyclerView.ViewHolder {//建造一個類別hold住三個資源
            ImageButton ibOrderDetial;
            TextView tvOrderId, tvOrderAcceptTime, tvOrderQuantity, tvOrderPrice;

            public MyViewHolder(View item_view) {//未來會有三個ViewHolder
                super(item_view);//呼叫父類別的建構式，並設定父類item_view
                tvOrderId = item_view.findViewById(R.id.tvOrderId);//要寫物件item_view.findViewById，否則會找activity_main
                tvOrderAcceptTime = item_view.findViewById(R.id.tvOrderAcceptTime);
                tvOrderQuantity = item_view.findViewById(R.id.tvOrderQuantity);
                tvOrderPrice = item_view.findViewById(R.id.tvOrderPrice);
                ibOrderDetial = item_view.findViewById(R.id.ibOrderDetial);//看明細的按鈕
            }
        }

        @NonNull
        @Override//View
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View item_view = layoutInflater.inflate(R.layout.history_item_view, parent, false);
            return new MyViewHolder(item_view);//建構式並呼叫服類別初始物件
        }


        @Override//Binding
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final Order order = orders.get(position);//根據位置判斷
            List<OrderDetail> orderDetails = order.getOrderDetailList();
            int totalQuantity = 0;
            double totalPrice = 0;
            final int detailPrice;
            for(OrderDetail orderDetail : orderDetails){
                totalPrice += orderDetail.getProduct_quantity()*orderDetail.getProduct_price();
                totalQuantity += orderDetail.getProduct_quantity();
            }
            totalPrice = order.getCoupon_discount() == 0 ? totalPrice*1 : totalPrice*order.getCoupon_discount()/10;
            detailPrice = Common.getDoubleToInt(totalPrice);
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.tvOrderId.setText(String.valueOf(order.getOrder_id()));
            myViewHolder.tvOrderAcceptTime.setText(order.getOrder_accept_time());
            myViewHolder.tvOrderQuantity.setText(totalQuantity + "杯");
            myViewHolder.tvOrderPrice.setText(Common.getDoubleToInt(totalPrice) + "元");
            myViewHolder.ibOrderDetial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(fragmentActivity, MemberOrderDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order", order);
                    bundle.putInt("detailPrice",detailPrice);//帶到下一頁的總價
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }


    }

    //連server取List<Order>
    private List<Order> getOrders(int member_id) {
        String url = Common.URL + "/OrdersServlet";
        List<Order> orders = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findOrderByMemberId");
        jsonObject.addProperty("member_id", member_id);
        Log.d(TAG, "findOrderByMemberId jsonObject:" + jsonObject.toString());
        String jsonOut = jsonObject.toString();
        orderHistoryTask = new MyTask(url, jsonOut);
        try {
            String jsonIn = orderHistoryTask.execute().get();
            Log.d(TAG, "findOrderByMemberId Result:" + jsonIn.toString());
            Type listType = new TypeToken<List<Order>>() {
            }.getType();
            orders = new Gson().fromJson(jsonIn, listType);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (orders == null || orders.isEmpty()) {
            Common.showToast(fragmentActivity, R.string.msg_NoOrdersFound);
        }
        return orders;
    }


    @Override
    public void onStop() {
        super.onStop();
        Common.closeAsyncTask(orderHistoryTask);
    }

}
