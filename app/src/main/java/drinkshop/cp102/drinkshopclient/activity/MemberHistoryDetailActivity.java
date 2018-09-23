package drinkshop.cp102.drinkshopclient.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import drinkshop.cp102.drinkshopclient.Common;
import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.Order;
import drinkshop.cp102.drinkshopclient.bean.OrderDetail;

public class MemberHistoryDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvOrderDetail, tvTatolPrice;
    private Button btBuyAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_history_detail);
        setTitle(R.string.textMemberHistory);
        handleView();
    }

    private void handleView() {
        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderDetail = findViewById(R.id.tvOrderDetail);
        tvTatolPrice = findViewById(R.id.tvTatolPrice);
        btBuyAgain = findViewById(R.id.btBuyAgain);
        String orderDetailString = "";
        Bundle bundle = getIntent().getExtras();
        Order order = (Order) bundle.getSerializable("order");
        int detailPrice = bundle.getInt("detailPrice");
        if (bundle != null) {
            if (order != null) {
                tvOrderId.setText(String.valueOf(order.getOrder_id()));
                if (order.getCoupon_discount() != 0) {
                    tvTatolPrice.setText("折扣後為" + String.valueOf(detailPrice) + "元");
                } else {
                    tvTatolPrice.setText(String.valueOf(detailPrice) + "元");
                }

                List<OrderDetail> orderDetailList = order.getOrderDetailList();
                for (OrderDetail orderDetail : orderDetailList) {
                    int subPrice = orderDetail.getProduct_quantity() * orderDetail.getProduct_price();
                    orderDetailString += orderDetail.getProduct_name() + " "
                            + orderDetail.getSugar_name() + " "
                            + orderDetail.getIce_name() + " "
                            + orderDetail.getSize_name() + " "
                            + orderDetail.getProduct_price() + "元 "
                            + orderDetail.getProduct_quantity() + "杯 "
                            + subPrice + "元\n";
                }
                tvOrderDetail.setText(orderDetailString);
                btBuyAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.showToast(MemberHistoryDetailActivity.this, "再買一次");
                    }
                });
            }


        }
    }
}
