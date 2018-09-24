package drinkshop.cp102.drinkshopclient.wsOrderOnline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import drinkshop.cp102.drinkshopclient.R;


public class AllChatActivity extends AppCompatActivity {
    private static final String TAG = "AllChatActivity";
    private static final String SERVER_URI =
            "ws://10.0.2.2:8080/DrinkShop_Web/AllChatServer/";//ws >> websocket,等等要送到server

    private MyWebSocketClient myWebSocketClient;
    private TextView tvMessage;
    private EditText etMessage;
    private Button btSend, btConnect, btDisconnect;
    private ScrollView scrollView;
    private String userName;
    private URI uri;

    class MyWebSocketClient extends WebSocketClient {//建立內部類別MyWebSocketClient繼承WebSocketClient,覆寫onOpen,onClose,onMessage,onError

        MyWebSocketClient(URI serverURI) {
            // Draft_17是連接協議，就是標準的RFC 6455（JSR256）
            super(serverURI, new Draft_17());//Draft_17固定的
        }

        @Override
        public void onOpen(ServerHandshake handshakeData) {//client連線到server端onOpen時被呼叫
            runOnUiThread(new Runnable() {//呼叫runOnUiThread,由此可知webscoket開啟時是開新的thread,onOpen,onMessage,onError,onClose皆屬webscoket thread,執行階段需要秀畫面就呼叫runOnUiThread
                @Override
                public void run() {
                    changeConnectStatus(true);
                }
            });
            String text = String.format(Locale.getDefault(),
                    "onOpen: Http status code = %d; status message = %s",
                    handshakeData.getHttpStatus(),
                    handshakeData.getHttpStatusMessage());

            Log.d(TAG, text);
        }

        @Override
        public void onMessage(final String message) {//server端送訊息時呼叫client onMessage
            Log.d(TAG, "onMessage: " + message);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
                    String userName = jsonObject.get("userName").getAsString();
                    String message = jsonObject.get("message").getAsString();
                    String text = userName + ": " + message + "\n";
                    tvMessage.append(text);
                    scrollView.fullScroll(View.FOCUS_DOWN);//畫面要自動捲動到最後
                }
            });
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {//client斷線server端onClose時被呼叫
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeConnectStatus(false);
                }
            });
            String text = String.format(Locale.getDefault(),
                    "code = %d, reason = %s, remote = %b",
                    code, reason, remote);
            Log.d(TAG, "onClose: " + text);
        }

        @Override
        public void onError(Exception ex) {//client onError被呼叫
            Log.d(TAG, "onError: exception = " + ex.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chat);
        findViews();
        userName = getIntent().getStringExtra("wsuserName");

        try {
            uri = new URI(SERVER_URI + userName);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        connectServer();
    }

    private void findViews() {
        tvMessage = findViewById(R.id.tvMessage);
        etMessage = findViewById(R.id.etMessage);
        btSend = findViewById(R.id.btSend);
        btConnect = findViewById(R.id.btConnect);
        btDisconnect = findViewById(R.id.btDisconnect);
        scrollView = findViewById(R.id.scrollView);
    }

    /* 依照連線狀況改變按鈕enable狀態 */
    private void changeConnectStatus(boolean isConnected) {
        if (isConnected) {
            btSend.setEnabled(true);
            btConnect.setEnabled(false);
            btDisconnect.setEnabled(true);
            showToast(R.string.text_Connect);
        } else {
            btSend.setEnabled(false);
            btConnect.setEnabled(true);
            btDisconnect.setEnabled(false);
            showToast(R.string.text_Disconnect);
            finish();//關閉聊天畫面
        }

    }

    public void clickSend(View view) {
        String message = etMessage.getText().toString();
        if (message.trim().isEmpty()) {
            showToast(R.string.text_MessageEmpty);
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userName", userName);
        jsonObject.addProperty("message", message);
        myWebSocketClient.send(jsonObject.toString());//send出後Server端onMessage被呼叫
        Log.d(TAG, "output: " + jsonObject.toString());
    }

    public void clickConnect(View view) {
        connectServer();
    }

    public void clickDisconnect(View view) {
        if (myWebSocketClient != null) {
            myWebSocketClient.close();
            myWebSocketClient = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            disconnectServer();
            showToast(R.string.text_LeftChatRoom);

        }
        return super.onKeyDown(keyCode, event);
    }

    // 建立WebSocket連線
    private void connectServer() {
        if (myWebSocketClient == null) {
            myWebSocketClient = new MyWebSocketClient(uri);
            myWebSocketClient.connect();//與server端連結,server端的onOpen被呼叫
        }
    }

    // 中斷WebSocket連線
    public void disconnectServer() {
        if (myWebSocketClient != null) {
            myWebSocketClient.close();
            myWebSocketClient = null;
        }
    }

    private void showToast(int messageId) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
    }
}
