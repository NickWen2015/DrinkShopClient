package drinkshop.cp102.drinkshopclient.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.drinkshopclient.bean.AddingProduct;
import drinkshop.cp102.drinkshopclient.bean.ShoppingCart;

/**
 * 購物車資料庫（尚未結帳前先將購買的物品儲存在手機中）
 *
 * @author mrosstro
 * @date 2018/9/2
 */
public class ShoppingCartDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DrinkShop.db";  //資料庫名稱
    private static final int DATABASE_VERSION = 1;  //資料庫版本

    private static final String TABLE_NAME = "shoppingcart";  //資料表名稱
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
                    "_id INTEGER PRIMARY KEY NOT NULL , " +
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
        super.onDowngrade(db, oldVersion, newVersion);
    }

    /**
     * 新增購買的商品
     */
    public void addProduct(AddingProduct addingProduct) {
        ContentValues values = new ContentValues();
        values.put(CATEGORY, addingProduct.getMyCategory());
        values.put(PRODUCT_NAME, addingProduct.getMyProductName());
        values.put(HOT_ICE, addingProduct.getMyHotOrIce());
        values.put(SIZE, addingProduct.getMySize());
        values.put(SIZE_PRICE, addingProduct.getMySizePrice());
        values.put(QUANTITY, addingProduct.getMyQuantity());
        values.put(SUGER, addingProduct.getMySuger());
        values.put(TEMPERATURE, addingProduct.getMyTemperature());
        getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    /**
     * 修改購買的商品
     */
    public void updateProduct() {
    }

    /**
     * 刪除購買的商品
     */
    public void deleteProduct() {
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
        String where = PRODUCT_NAME + "=" + "'" + productName + "'";
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, null, where, null, null, null, null);
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }
        cursor.close();
        return result;
    }

    // 把Cursor目前的資料包裝為物件
    public ShoppingCart getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        ShoppingCart result = new ShoppingCart();

        result.setId(cursor.getLong(0));
        result.setCategory(cursor.getString(1));
        result.setProductName(cursor.getString(2));
        result.setHotOrice(cursor.getInt(3));
        result.setSize(cursor.getInt(4));
        result.setSizePrice(cursor.getInt(5));
        result.setQuantity(cursor.getInt(6));
        result.setSuger(cursor.getInt(7));
        result.setTemperature(cursor.getInt(8));
        // 回傳結果
        return result;
    }


}
