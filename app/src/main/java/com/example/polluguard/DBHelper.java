package com.example.polluguard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.polluguard.model.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "sqlite.db";
    private static final int DB_VERSION = 2;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] byteImage;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table User (" +
                "id integer primary key autoincrement," +
                "name text," +
                "email text," +
                "password text," +
                "image blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            if(oldVersion < 2){
//                db.execSQL("create table User_New (" +
//                        "id integer primary key autoincrement," +
//                        "name text," +
//                        "email text," +
//                        "password text," +
//                        "image blob)"); // Renaming column_a to column_b
//
//                // Step 2: Copy data from the old table to the new table
//                db.execSQL("INSERT INTO User_New (id, name, email, password, image) SELECT id, name, email, pasword, image FROM User;");
//
//                // Step 3: Drop the old table
//                db.execSQL("DROP TABLE IF EXISTS User;");
//
//                // Step 4: Rename the new table to the old table name
//                db.execSQL("ALTER TABLE User_New RENAME TO User;");
//            }
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);

    }

    public boolean insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap image = user.getImage();

        byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byteImage = byteArrayOutputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("image", byteImage);

        long checkQuery = db.insert("User", null, values);
        if(checkQuery == -1) return false;
        else return true;

    }

    public Cursor getUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from User", null);
        return cursor;
    }

    public User getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from User where id = ?", new String[]{String.valueOf(id)});

        String name = "";
        String email = "";
        String password = "";
        Bitmap image = null;

        User user = null;

        try {
            if(cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                password = cursor.getString(cursor.getColumnIndexOrThrow("password"));

                byte[] imageBlob = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                image = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
            }

            user = new User(name, email, password, image);
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return user;
    }
}
