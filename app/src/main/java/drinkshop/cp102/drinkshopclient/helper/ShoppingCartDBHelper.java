package drinkshop.cp102.drinkshopclient.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.drinkshopclient.bean.ShoppingCart;
import drinkshop.cp102.drinkshopclient.bean.ShoppingCartTotol;

/**
 * 購物車資料庫（尚未結帳前先將購買的物品儲存在手機中）
 *
 * @author mrosstro
 * @date 2018/9/2
 */
public class ShoppingCartDBHelper extends SQLiteOpenHelper {
    public static final String TAG = "ShoppingCartDBHelper";
    private static final String DATABASE_NAME = "DrinkShop.db";  //資料庫名稱
    private static final int DATABASE_VERSION = 2;  //資料庫版本

    private static final String TABLE_NAME = "shoppingcart";  //資料表名稱
    private static final String ID = "_id";
    private static final String PRODUCT_ID = "product_id";  //商品類型
    private static final String CATEGORY = "category";  //商品類型
    private static final String PRODUCT_NAME = "productname";  //商品名稱
    private static final String HOT_ICE = "hotOrice";  //商品冷熱
    private static final String SIZE = "size";  //商品價錢
    private static final String SIZE_PRICE = "sizePrice";  //商品價錢
    private static final String QUANTITY = "quantity";  //商品數量
    private static final String SUGER = "suger";  //商品甜度
    private static final String TEMPERATURE = "temperature";  //商品溫度

    /* 新增資料表 */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    " " + ID + " INTEGER PRIMARY KEY NOT NULL , " +
                    PRODUCT_ID + " INTEGER NOT NULL, " +
                    CATEGORY + " VARCHAR NOT NULL, " +
                    PRODUCT_NAME + " VARCHAR NOT NULL, " +
                    HOT_ICE + " INTEGER NOT NULL, " +
                    SIZE + " INTEGER NOT NULL, " +
                    SIZE_PRICE + " INTEGER NOT NULL, " +
                    QUANTITY + " INTEGER NOT NULL, " +
                    SUGER + " INTEGER NOT NULL, " +
                    TEMPERATURE + " INTEGER NOT NULL);";

    /* 刪除資料庫 */
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public ShoppingCartDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    /**
     * 清空資料表
     * */
    public void clearDB() {
        getWritableDatabase().execSQL(SQL_DELETE_ENTRIES);
        onCreate(getWritableDatabase());
    }

    /**
     * 新增購買的商品
     */
    public void addProduct(ShoppingCart shoppingCart) {
        ContentValues values = new ContentValues();
        values.put(PRODUCT_ID, shoppingCart.getProductID());
        values.put(CATEGORY, shoppingCart.getCategory());
        values.put(PRODUCT_NAME, shoppingCart.getProductName());
        values.put(HOT_ICE, shoppingCart.getHotOrIce());
        values.put(SIZE, shoppingCart.getSize());
        values.put(SIZE_PRICE, shoppingCart.getSizePrice());
        values.put(QUANTITY, shoppingCart.getQuantity());
        values.put(SUGER, shoppingCart.getSuger());
        values.put(TEMPERATURE, shoppingCart.getTemperature());
        getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    /**
     * 修改購買的商品
     */
    public int updateProduct(int rowId, ShoppingCart shoppingCart) {
        final String WHERE = " " + ID + " = " + rowId;
        ContentValues values = new ContentValues();
        values.put(PRODUCT_ID, shoppingCart.getProductID());
        values.put(CATEGORY, shoppingCart.getCategory());
        values.put(PRODUCT_NAME, shoppingCart.getProductName());
        values.put(HOT_ICE, shoppingCart.getHotOrIce());
        values.put(SIZE, shoppingCart.getSize());
        values.put(SIZE_PRICE, shoppingCart.getSizePrice());
        values.put(QUANTITY, shoppingCart.getQuantity());
        values.put(SUGER, shoppingCart.getSuger());
        values.put(TEMPERATURE, shoppingCart.getTemperature());
        return getWritableDatabase().update(
                TABLE_NAME,      //資料表名稱
                values,          //VALUE
                WHERE,           //WHERE
                null  //WHERE的參數
        );
    }

    /**
     * 刪除購買的商品
     */
    public void deleteProduct(int rowId) {
        final String WHERE = " " + ID + " = " + rowId;
        getWritableDatabase().delete(
                TABLE_NAME,       //資料表名稱
                WHERE,            //WHERE
                null   //WHERE的參數
        );
    }

    /**
     * 顯示所有購買的商品
     */
    public List<ShoppingCart> readAllProduct() {
        List<ShoppingCart> result = new ArrayList<>();
        String sortOrder = CATEGORY + " DESC";
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, sortOrder);
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }
        cursor.close();
        return result;
    }

    /**
     * 顯示購買商品數量
     */
    public List<ShoppingCart> readProductQuantity(String productName) {
        List<ShoppingCart> result = new ArrayList<>();
        final String WHERE = " " + PRODUCT_NAME + " = " + " '" + productName + "' ";
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, new String[]{QUANTITY}, WHERE, null, null, null, null);
        while (cursor.moveToNext()) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setQuantity(cursor.getInt(0));
            LogHelper.e(TAG, "readProductQuantity：商品：" + productName + " 數量：" + cursor.getInt(0));
            result.add(shoppingCart);
        }
        cursor.close();
        return result;
    }

    /**
     * 顯示選取商品的資訊
     */
    public List<ShoppingCart> readEditProductDetail(int rowId) {
        List<ShoppingCart> result = new ArrayList<>();
        final String WHERE = " " + ID + " = " + rowId;
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, null, WHERE, null, null, null, null);
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }
        cursor.close();
        return result;
    }

    /**
     * 取的總杯數及總金額
     */
    public List<ShoppingCartTotol> readTotalCapAndTotalAmount() {
        List<ShoppingCartTotol> result = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, new String[]{SIZE_PRICE, QUANTITY}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            ShoppingCartTotol shoppingCartTotol = new ShoppingCartTotol();
            shoppingCartTotol.setPrice(cursor.getInt(0));
            shoppingCartTotol.setQuantity(cursor.getInt(1));
            LogHelper.e(TAG, "readTotalCapAndTotalAmount：價錢：" + cursor.getInt(0) + " 數量：" + cursor.getInt(1));
            result.add(shoppingCartTotol);
        }
        cursor.close();
        return result;
    }

    // 把Cursor目前的資料包裝為物件
    public ShoppingCart getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        ShoppingCart result = new ShoppingCart();
        result.setID(cursor.getInt(0));
        result.setProductID(cursor.getInt(1));
        result.setCategory(cursor.getString(2));
        result.setProductName(cursor.getString(3));
        result.setHotOrIce(cursor.getInt(4));
        result.setSize(cursor.getInt(5));
        result.setSizePrice(cursor.getInt(6));
        result.setQuantity(cursor.getInt(7));
        result.setSuger(cursor.getInt(8));
        result.setTemperature(cursor.getInt(9));
        LogHelper.e(TAG, "readAllProduct：" + "\n" +
                "ID：" + cursor.getInt(0) + "\n" +
                "商品ID：" + cursor.getInt(1) + "\n" +
                "類別：" + cursor.getInt(2) + "\n" +
                "名稱：" + cursor.getInt(3) + "\n" +
                "熱度：" + cursor.getInt(4) + "\n" +
                "大小：" + cursor.getInt(5) + "\n" +
                "單價：" + cursor.getInt(6) + "\n" +
                "數量：" + cursor.getInt(7) + "\n" +
                "甜度：" + cursor.getInt(8) + "\n" +
                "微度：" + cursor.getInt(9));

        // 回傳結果
        return result;
    }
}
