package drinkshop.cp102.drinkshopclient.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import drinkshop.cp102.drinkshopclient.R;
import drinkshop.cp102.drinkshopclient.bean.Order;
import drinkshop.cp102.drinkshopclient.bean.OrderDetail;
import drinkshop.cp102.drinkshopclient.qrcode.Contents;
import drinkshop.cp102.drinkshopclient.qrcode.QRCodeEncoder;

public class MemberOrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvOrderDetail, tvTatolPrice;
    private Button btDeliveryTrack, btNavigation;
    private ImageView ivQRCode;
    private static final String TAG = "MemberOdFragment";
    private String orderDetailString;
    private Order order;
    private int detailPrice;
    private final static int REQUEST_CODE_RESOLUTION = 1;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient provider;//提供地點查詢服務的物件
    private Location lastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_order_detail);
        setTitle(R.string.textMemberMenuOrderStatus);
        handleView();
    }


    private void handleView() {
        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderDetail = findViewById(R.id.tvOrderDetail);
        tvTatolPrice = findViewById(R.id.tvTatolPrice);
        btDeliveryTrack = findViewById(R.id.btDeliveryTrack);
        btNavigation = findViewById(R.id.btNavigation);
        ivQRCode = findViewById(R.id.ivQRCode);//產生QR code的圖

        orderDetailString = "";
        order = null;
        Bundle bundle = getIntent().getExtras();
        order = (Order) bundle.getSerializable("order");
        detailPrice = bundle.getInt("detailPrice");
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

                /**** 產生QR code的圖 start ****/
                String qrCodeText = tvOrderId.getText().toString();
                Log.d(TAG, qrCodeText);

                // QR code image's length is the same as the width of the window,
                int dimension = getResources().getDisplayMetrics().widthPixels;//取得螢幕寬度,dimension 只要給一邊 qr code 產生為正方形

                // Encode with a QR Code image
                QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrCodeText, null,
                        Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
                        dimension);//Contents.Type.TEXT 一般文字不加任何文字, BarcodeFormat.QR_CODE二維條碼格式
                try {
                    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                    ivQRCode.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    Log.e(TAG, e.toString());
                }
                /**** 產生QR code的圖 end ****/

                if (order.getOrder_type().equals("1")) {//若為外送則出現外送追蹤鈕
                    btNavigation.setVisibility(View.GONE);
                } else {//自取出現導航鈕
                    btDeliveryTrack.setVisibility(View.GONE);
                }
                btDeliveryTrack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MemberOrderDetailActivity.this, DeliveryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order", order);
                        bundle.putString("orderDetailString", orderDetailString);//帶到下一頁的訂單內容
                        bundle.putInt("detailPrice", detailPrice);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                btNavigation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double store_location_x = 24.96842;
                        double store_location_y = 121.19566580000003;
                        double fromLat = lastLocation.getLatitude();
                        double fromLng = lastLocation.getLongitude();
                        double toLat = store_location_x;
                        double toLng = store_location_y;

                        direct(fromLat, fromLng, toLat, toLng);


                    }
                });
            }


        }
    }

    private void direct(double fromLat, double fromLng, double toLat,
                        double toLng) {
        String uriStr = String.format(Locale.TAIWAN,
                "https://www.google.com/maps/dir/?api=1" +
                        "&origin=%f,%f&destination=%f,%f&travelmode=driving",
                fromLat, fromLng, toLat, toLng);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uriStr));
        intent.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        askPermissions();//詢問可否取得位置
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            lastLocation = locationResult.getLastLocation();

        }
    };

    private GoogleApiClient.ConnectionCallbacks connectionCallbacks =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {//
                    Log.i(TAG, "GoogleApiClient connected");
                    if (ActivityCompat.checkSelfPermission(MemberOrderDetailActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {

                        provider.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                lastLocation = task.getResult();//拿到lastLocation物件
                            }
                        });

                        //持續取得user位置
                        LocationRequest locationRequest = LocationRequest.create()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)//定位方式
                                .setInterval(10000)//每10秒定位一次
                                .setSmallestDisplacement(1000);//設定最短移動距離超過1公里
                        provider.requestLocationUpdates(locationRequest, locationCallback, null);
                        /* For background use cases,
                           the PendingIntent version of the method is recommended,
                           see requestLocationUpdates(GoogleApiClient, LocationRequest, PendingIntent). */
                    }
                }

                @Override
                public void onConnectionSuspended(int i) {
                }
            };

    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(@NonNull ConnectionResult result) {
                    if (!result.hasResolution()) {
                        GoogleApiAvailability.getInstance().getErrorDialog(
                                MemberOrderDetailActivity.this,
                                result.getErrorCode(),
                                0
                        ).show();
                        return;
                    }
                    try {
                        result.startResolutionForResult(
                                MemberOrderDetailActivity.this,
                                REQUEST_CODE_RESOLUTION);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e(TAG, "Exception while starting resolution activity");
                    }
                }
            };
    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(connectionCallbacks)//偵測連結是否成功
                    .addOnConnectionFailedListener(onConnectionFailedListener)
                    .build();
        }
        googleApiClient.connect();//呼叫connect

        if (provider == null) {
            provider = LocationServices.getFusedLocationProviderClient(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }

        if (provider != null) {
            provider.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_RESOLUTION) {
                googleApiClient.connect();
            }
        }
    }
    private static final int REQ_PERMISSIONS = 0;

    private void askPermissions() {
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSIONS:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        String text = getString(R.string.text_ShouldGrant);
                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                break;
        }
    }

}
