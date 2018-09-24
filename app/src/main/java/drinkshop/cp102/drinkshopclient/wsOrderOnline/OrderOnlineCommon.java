package drinkshop.cp102.drinkshopclient.wsOrderOnline;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class OrderOnlineCommon {

    private static final String TAG = "OrderOnlineCommon";
    public final static String SERVER_URI =
            "ws://10.0.2.2:8080/DrinkShop_Web/ChatServer/";
    public static ChatWebSocketClient chatWebSocketClient;
    private static List<String> friendList = new ArrayList<>();

    // 建立WebSocket連線
    public static void connectServer(String userName, Context context) {
        if (chatWebSocketClient == null) {
            URI uri = null;
            try {
                uri = new URI(SERVER_URI + userName);
            } catch (URISyntaxException e) {
                Log.e(TAG, e.toString());
            }
            chatWebSocketClient = new ChatWebSocketClient(uri, context);
            chatWebSocketClient.connect();
        }
    }

    // 中斷WebSocket連線
    public static void disconnectServer() {
        if (chatWebSocketClient != null) {
            chatWebSocketClient.close();
            chatWebSocketClient = null;
        }
        friendList.clear();
    }

    public static List<String> getFriendList() {
        return friendList;
    }

    public static void setFriendList(List<String> friendList) {
        OrderOnlineCommon.friendList = friendList;
    }

    public static Bitmap downSize(Bitmap srcBitmap, int newSize) {
        if (newSize <= 50) {
            // 如果欲縮小的尺寸過小，就直接定為128
            newSize = 128;
        }
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        String text = "source image size = " + srcWidth + "x" + srcHeight;
        Log.d(TAG, text);
        int longer = Math.max(srcWidth, srcHeight);

        if (longer > newSize) {
            double scale = longer / (double) newSize;
            int dstWidth = (int) (srcWidth / scale);
            int dstHeight = (int) (srcHeight / scale);
            srcBitmap = Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, false);
            System.gc();
            text = "\nscale = " + scale + "\nscaled image size = " +
                    srcBitmap.getWidth() + "x" + srcBitmap.getHeight();
            Log.d(TAG, text);
        }
        return srcBitmap;
    }
    public static void setUserName(Context context, String userName) {//userName存在偏好設定裡
        SharedPreferences preferences =
                context.getSharedPreferences("wsuser", MODE_PRIVATE);
        preferences.edit().putString("wsuserName", userName).apply();
    }

    public static String getUserName(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("wsuser", MODE_PRIVATE);
        String userName = preferences.getString("wsuserName", "");
        Log.d(TAG, "wsuserName = " + userName);
        return userName;
    }
}
