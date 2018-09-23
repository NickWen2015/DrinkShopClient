package drinkshop.cp102.drinkshopclient.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.Set;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.Order;

public class DeliveryActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private static final String TAG = "DeliveryActivity";
    private GoogleMap map;
    private Marker marker_delivery;
    private LatLng delivery_position;
    private Order order;
    private int detailPrice;
    private String orderDetailString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        initPoints();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            order = (Order) bundle.getSerializable("order");
            detailPrice = bundle.getInt("detailPrice");
            orderDetailString = bundle.getString("orderDetailString");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        askPermissions();
    }

    private static final int REQ_PERMISSIONS = 0;

    // New Permission see Appendix A
    private void askPermissions() {
        //因為是群組授權，所以請求ACCESS_COARSE_LOCATION就等同於請求ACCESS_FINE_LOCATION，因為同屬於LOCATION群組
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    REQ_PERMISSIONS);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;//同步google map資料並設值
        setUpMap();//顯示user位置
    }

    private void initPoints() {//初始化外送員位置,初始位置外送員初始位置應為訂購店家位置,之後跟server要資料定期更新外送員位置
        delivery_position = new LatLng(24.96842, 121.19566580000003);//緯經度
    }

    private void setUpMap() {
        //確定user同意存取自身位置,並將右上角定位按鈕打開
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
        //將縮放按鈕啟用
        map.getUiSettings().setZoomControlsEnabled(true);

        addMarkers();//將對應位置打標記
        map.setInfoWindowAdapter(new MyInfoWindowAdapter());//背後用list存
        moveMap();
        //addListeners();
    }

    private void addMarkers() {//addMarker回傳Marker物件
        String orderMessage = "";
        orderMessage += "\n訂單編號：" + order.getOrder_id();
        orderMessage += "\n訂單內容：\n" + orderDetailString;
        orderMessage += "\n訂購價格：" + detailPrice + "元";
        marker_delivery = map.addMarker(new MarkerOptions()
                .position(delivery_position)
                .title(getString(R.string.delivery_title))
                .snippet(orderMessage)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
    }

    private class MyInfoWindowAdapter implements InfoWindowAdapter {
        private final View infoWindow;

        MyInfoWindowAdapter() {
            infoWindow = View.inflate(DeliveryActivity.this, R.layout.info_window, null);
        }

        @Override//類似recyclerview onbindviewholder方法
        public View getInfoWindow(Marker marker) {
            int logoId = R.drawable.delivery;

            ImageView ivLogo = infoWindow.findViewById(R.id.ivLogo);
            ivLogo.setImageResource(logoId);

            String title = marker.getTitle();
            TextView tvTitle = infoWindow.findViewById(R.id.tvTitle);
            tvTitle.setText(title);

            String snippet = marker.getSnippet();
            TextView tvSnippet = infoWindow.findViewById(R.id.tvSnippet);
            tvSnippet.setText(snippet);
            return infoWindow;//跳出給user看
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    private void moveMap() {
        //建立CameraPosition.Builder物件並設定顯示地點與縮放層級
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(delivery_position)
                .zoom(7)
                .build();
        //建立CameraUpdate物件並套用CameraPosition
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);//移動地圖到指定地點,動畫效果
    }


//    private void addListeners() {
//        MyMarkerListener myMarkerListener = new MyMarkerListener();
//        map.setOnInfoWindowClickListener(myMarkerListener);
//
//    }
//
//    private class MyMarkerListener implements
//            OnInfoWindowClickListener {//實作三個監聽器
//
//        @Override
//        public void onInfoWindowClick(Marker marker) {
//
//        }
//
//
//    }


}

