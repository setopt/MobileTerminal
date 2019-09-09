package com.example.tokin.mobileterminal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tokin on 15.05.2018.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    //  table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_MENU = "menu";
    private static final String TABLE_COOK = "cook";

    // Login Table Columns names user
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_UID = "uid";
    private static final String KEY_IDPOSITION = "id_position";

    // Menu Table Columns names menu
    private static final String MENU_KEY_ID = "id";
    private static final String MENU_KEY_NAME = "name";
    private static final String MENU_KEY_VALUE = "value";
    private static final String MENU_KEY_CAT = "cat";
    private static final String MENU_KEY_KIT = "kit";

    // Cook Table Columns names menu
    private static final String COOK_KEY_ID = "id";

    private static final String COOK_KEY_ID_DISH = "id_dish";
    private static final String COOK_KEY_ID_ORD = "id_ord";

    private static final String COOK_KEY_DISH = "dish";
    private static final String COOK_KEY_WORKER = "worker";
    private static final String COOK_KEY_COUNT = "count";
    private static final String COOK_KEY_KIT = "kit";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) { //Создание таблицы бд
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT, "
                + KEY_LOGIN + " TEXT UNIQUE, " + KEY_UID + " TEXT,"
                + KEY_IDPOSITION + " INTEGER" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);


        String CREATE_MENU_TABLE = "CREATE TABLE " + TABLE_MENU + "("
                + MENU_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MENU_KEY_NAME + " TEXT, "
                + MENU_KEY_VALUE  + " REAL, " + MENU_KEY_CAT + " TEXT, "
                + MENU_KEY_KIT + " TEXT" + ")";
        db.execSQL(CREATE_MENU_TABLE);

        String CREATE_MENU_COOK = "CREATE TABLE " + TABLE_COOK + "("
                + COOK_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COOK_KEY_DISH + " TEXT, "
                + COOK_KEY_WORKER   + " TEXT, "
                + COOK_KEY_COUNT + " INTEGER, "
                + COOK_KEY_KIT + " TEXT" + ")";
        db.execSQL(CREATE_MENU_COOK);


        Log.d(TAG, "Database tables created");

        Log.d(TAG, CREATE_MENU_COOK);
    }



    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COOK);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */

    public void addUser(String name, String login, String uid, String id_position) { // Добавление работника в бд
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_LOGIN, login); // Login
        values.put(KEY_UID, uid); // id
        values.put(KEY_IDPOSITION, id_position); // position

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addMenu (String name, Double value, String cat, String kit){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MENU_KEY_NAME, name); // Name
        values.put(MENU_KEY_VALUE, value); // Login
        values.put(MENU_KEY_CAT, cat); // id
        values.put(MENU_KEY_KIT, kit); // position

        long id = db.insert(TABLE_MENU, null, values);
        db.close(); // Closing database connection
        Log.d(TAG, "New menu inserted into sqlite: " + id);

    }



    public void addCook (String dish, String worker, int count, String kit){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesCook = new ContentValues();
        valuesCook.put(COOK_KEY_DISH, dish); // Name
        valuesCook.put(COOK_KEY_WORKER, worker); // Login
        valuesCook.put(COOK_KEY_COUNT, count); // id
        valuesCook.put(COOK_KEY_KIT, kit); // position

        long id = db.insert(TABLE_COOK, null, valuesCook);
        db.close(); // Closing database connection
        Log.d(TAG, "New cook inserted into sqlite: " + id);

    }

    /**
     * Getting user data from database
     * */

    public HashMap<String, String> getUserDetails() { // листы
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null); // ответ бд
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("login", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("id_position", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public  List<Map<String, String>> getMenuDetails() { // листы
        List<Map<String, String>> listMenu = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_MENU;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null); // ответ бд

        while(cursor.moveToNext()){
            Map<String, String> menu = new HashMap<>();
            menu.put("name", cursor.getString(1));
            menu.put("value", cursor.getString(2));
            menu.put("cat", cursor.getString(3));
            menu.put("kit", cursor.getString(4));

            listMenu.add(menu);
        }


        cursor.close();
        db.close();

        return listMenu;
    }


    public  List<Map<String, String>> getCookDetails() { // листы
        List<Map<String, String>> listCook = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_COOK;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null); // ответ бд

        while(cursor.moveToNext()){
            Map<String, String> menu = new HashMap<>();
            menu.put("dish", cursor.getString(1));
            menu.put("worker", cursor.getString(2));
            menu.put("count", cursor.getString(3));
            menu.put("kit", cursor.getString(4));

            listCook.add(menu);
        }

        cursor.close();
        db.close();

        return listCook;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */

    public void deleteUsers() { //
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
    public void deleteMenu() { // можно удалить
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_MENU, null, null);
        db.close();

        Log.d(TAG, "Deleted all menu info from sqlite");
    }

    public void delMenu(){
        SQLiteDatabase db = this.getReadableDatabase();
        String QueryDel = "DELETE FROM "+TABLE_MENU;

        Log.d(TAG, "Таблица меню очищена");
        db.execSQL(QueryDel);
    }


    public void delCook(){
        SQLiteDatabase db = this.getReadableDatabase();
        String QueryDel = "DELETE FROM "+TABLE_COOK;

        Log.d(TAG, "Таблица cook очищена");
        db.execSQL(QueryDel);
    }
}
