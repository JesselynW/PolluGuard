package com.example.polluguard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.polluguard.model.Organizer;
import com.example.polluguard.model.Project;
import com.example.polluguard.model.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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
        //table user
        db.execSQL("CREATE TABLE User (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT," +
                "password TEXT," +
                "phoneNumber TEXT," +
                "volunteerPoints INTEGER," +
                "dateOfBirth TEXT," +
                "image BLOB)");

        //table organizer
        db.execSQL("CREATE TABLE Organizer(" +
                "organizerId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "logo INTEGER," +
                "description TEXT);");

        //table volunteer_event
        db.execSQL("CREATE TABLE Volunteer_event(" +
                "eventId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "image INTEGER," +
                "date TEXT," +
                "time TEXT," +
                "location TEXT," +
                "about TEXT," +
                "reward INTEGER," +
                "slot INTEGER," +
                "linkWhatsapp TEXT," +
                "whatsappQR INTEGER," +
                "organizerId INTEGER," +
                "FOREIGN KEY (organizerId) REFERENCES Organizer(organizerId));");

        //table user_volunteer_event
        db.execSQL("CREATE TABLE User_volunteer_event(" +
                "userId INTEGER," +
                "eventId INTEGER," +
                "rewardStatus INTEGER, " +
                "FOREIGN KEY (eventId) REFERENCES Volunteer_event(eventId)," +
                "FOREIGN KEY (userId) REFERENCES User(userId)," +
                "PRIMARY KEY (userId, eventId));");

        //table article
        db.execSQL("CREATE TABLE Article(" +
                "articleId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "author TEXT," +
                "date TEXT," +
                "content TEXT," +
                "image INTEGER);");

        //table article comment
        db.execSQL("CREATE TABLE Article_comment(" +
                "commentId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "articleId INTEGER," +
                "userId INTEGER," +
                "FOREIGN KEY (articleId) REFERENCES Article(articleId)," +
                "FOREIGN KEY (userId) REFERENCES User(userId));");

        addUser(db);
        addOrganizer(db);
        addEvent(db);
        addUserEvent(db);
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
        db.execSQL("DROP TABLE IF EXISTS Organizer");
        db.execSQL("DROP TABLE IF EXISTS Volunteer_event");
        db.execSQL("DROP TABLE IF EXISTS User_volunteer_event");
        db.execSQL("DROP TABLE IF EXISTS Article");
        db.execSQL("DROP TABLE IF EXISTS Article_comment");
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
        values.put("volunteerPoints", 0);
        values.put("image", byteImage);

        long checkQuery = db.insert("User", null, values);
        if(checkQuery == -1) return false;
        else return true;


    }

    public boolean updateUser(User user, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Bitmap image = user.getImage();
        byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byteImage = byteArrayOutputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        if(!user.getPassword().equals("")) values.put("password", user.getPassword());
        else if(user.getPhoneNumber() != null) values.put("phoneNumber", user.getPhoneNumber());
        if(user.getImage() != null) values.put("image", byteImage);

        long checkQuery = db.update("User", values, "userId = ?", new String[]{String.valueOf(id)});
        Log.i("DB HELPER UPDATE", "CHECK QUERY = " + checkQuery);
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
        Cursor cursor = db.rawQuery("select * from User where userId = ?", new String[]{String.valueOf(id)});

        String name = "";
        String email = "";
        String password = "";
        int volunteerPoints = 0;
        Bitmap image = null;

        User user = null;

        try {
            if(cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                volunteerPoints = cursor.getInt(cursor.getColumnIndexOrThrow("volunteerPoints"));
                byte[] imageBlob = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                image = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
            }

            user = new User(name, email, password, volunteerPoints, image);
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return user;
    }

    public void addOrganizer(SQLiteDatabase db){
        db.execSQL("INSERT INTO Organizer VALUES(NULL, 'EcoSolutions Volunteers', "+ R.drawable.ecosolutions_volunteers + ", 'Established in 1998, EcoVolunteers is a community-driven non-profit organization focused on environmental projects. Since its inception, they’ve been actively working to promotes sustainability.')");

        db.execSQL("INSERT INTO Organizer VALUES(NULL, 'GreenFuture Community', "+ R.drawable.greenfuture_comunity + ", 'GreenFuture Volunteers focuses on environmental restoration and waste management projects since 2005.')");

        db.execSQL("INSERT INTO Organizer VALUES(NULL, 'CleanEarth Community', "+ R.drawable.cleanearth_community + ", 'CleanEarth Community is a group of eco-activists dedicated to urban cleanliness.')");

        db.execSQL("INSERT INTO Organizer VALUES(NULL, 'EcoMinds Foundation', "+ R.drawable.ecominds_foundation + ", 'EcoMinds Initiative is an environmental group dedicated to waste reduction and sustainability. Their mission is to educate the public on better waste disposal methods and recycling techniques.')");
    }

    public void addEvent(SQLiteDatabase db){
        db.execSQL
                ("INSERT INTO Volunteer_event VALUES(NULL, 'Garbage Collection Drive', " + R.drawable.milah_sampah + ", '15th February 2024', '09.00-13.00', 'Jl. Jalur Sutera Bar. No.Kav. 21, Kota Tangerang, Banten', 'A volunteer-driven garbage collection event to clean up public roads. ', 20, 15, 'https://web.whatsapp.com/', " + R.drawable.qr_dummy + ", 1)");

        db.execSQL
                ("INSERT INTO Volunteer_event VALUES(NULL, 'Plastic Waste Awareness', " + R.drawable.milah_plastik + ", '10th March 2024', '08.30-12.30', 'Kebayoran Baru, Jakarta', 'An act to raise awareness about plastic waste and recycling methods. ', 15, 30, 'https://web.whatsapp.com/', " + R.drawable.qr_dummy + ", 2)");

        db.execSQL
                ("INSERT INTO Volunteer_event VALUES(NULL, 'Tree Planting Initiative', " + R.drawable.tanam_pohon + ", '20th April 2024', '07.00-10.00', 'Jl. Imam Bonjol No.12, Jakarta', 'An initiative to plant trees in urban areas to improve air quality. ', 25, 30, 'https://web.whatsapp.com/', " + R.drawable.qr_dummy + ", 1)");

        db.execSQL
                ("INSERT INTO Volunteer_event VALUES(NULL, 'Sorting Plastics', " + R.drawable.milah_sampah_3 + ", '19th May 2024', '08.00-12.00', 'Jl. SetiaBudi No.19, Jakarta', 'A community effort to clean and sort public parks garbage.', 10, 25, 'https://web.whatsapp.com/', " + R.drawable.qr_dummy + ", 3)");

        db.execSQL
                ("INSERT INTO Volunteer_event VALUES(NULL, 'Beach Cleanup', " + R.drawable.milah_sampah_di_pinggir_pantai + ", '17th June 2024', '06.00-10.00', 'Kuta Beach, Bali', 'A beach cleanup event to remove plastic and other pollutants.', 10, 45, 'https://web.whatsapp.com/', " + R.drawable.qr_dummy + ", 4)");

        db.execSQL
                ("INSERT INTO Volunteer_event VALUES(NULL, 'Reforestation', " + R.drawable.tanam_pohon2 + ", '04th July 2024', '07.00-10.00', 'Taman Nasional Gunung Gede, Bogor', 'Reforestation activities in national parks to restore green cover. ', 30, 45, 'https://web.whatsapp.com/', " + R.drawable.qr_dummy + ", 4)");

        db.execSQL
                ("INSERT INTO Volunteer_event VALUES(NULL, 'Community Reforestation', " + R.drawable.tanam_pohon3 + ", '11th July 2024', '08.00-12.00', 'Taman Nasional Gunung Gede, Bogor', 'Reforestation activities in national parks to restore green cover. ', 20, 15, 'https://web.whatsapp.com/', " + R.drawable.qr_dummy + ", 1)");

        db.execSQL
                ("INSERT INTO Volunteer_event VALUES(NULL, 'Urban Green Project', " + R.drawable.tanam_pohon4 + ", '27th August 2024', '08.00-12.00', 'Jl. Sudirman, Jakarta', 'An urban greening initiative to transform unused spaces into green, sustainable areas.', 20, 30, 'https://web.whatsapp.com/', " + R.drawable.qr_dummy + ", 2)");

        db.execSQL
                ("INSERT INTO Volunteer_event VALUES(NULL, 'Clean Breeze', " + R.drawable.milah_sampah_4 + ", '14th August 2024', '08.00-12.00', 'Jl. Sudirman, Jakarta', 'A massive city-wide campaign to clean streets and public spaces.', 10, 30, 'https://web.whatsapp.com/', " + R.drawable.qr_dummy + ", 4)");

        db.execSQL
                ("INSERT INTO Volunteer_event VALUES(NULL, 'SkySavers', " + R.drawable.tanam_pohon5 + ", '21th October 2024', '08.00-12.00', 'Setiabudi, Jakarta', 'A volunteer-driven garbage collection event to clean up public parks.', 25, 30, 'https://web.whatsapp.com/', " + R.drawable.qr_dummy + ", 1)");

    }

    public void addUserEvent(SQLiteDatabase db) {
        db.execSQL("INSERT INTO User_volunteer_event VALUES(2, 1, 1)");

        db.execSQL("INSERT INTO User_volunteer_event VALUES(2, 2, 1)");

        db.execSQL("INSERT INTO User_volunteer_event VALUES(2, 3, 1)");

        db.execSQL("INSERT INTO User_volunteer_event VALUES(2, 4, 0)");

        db.execSQL("INSERT INTO User_volunteer_event VALUES(2, 5, 0)");

        db.execSQL("INSERT INTO User_volunteer_event VALUES(2, 6, 0)");

        db.execSQL("INSERT INTO User_volunteer_event VALUES(2, 7, 0)");

        db.execSQL("INSERT INTO User_volunteer_event VALUES(2, 8, 1)");

        db.execSQL("INSERT INTO User_volunteer_event VALUES(2, 9, 1)");

        db.execSQL("INSERT INTO User_volunteer_event VALUES(2, 10, 1)");
    }

    public void addUser(SQLiteDatabase db){
        String query = "INSERT INTO User VALUES(NULL, 'jeje', 'jeje@gmail.com', 'jeje123', '081234567890', 10, '29th july 2004', ?)";

        db.execSQL(query, new Object[]{ byteImage });
    }

    public Cursor getUserbyEmailandPass(String email, String pass){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM User WHERE email = ? AND password = ?", new String[]{email, pass});
    }

    public ArrayList<Project> getAllProjectInformation(){
        ArrayList<Project> projects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT ve.name, ve.image, ve.date, ve.time, ve.location, ve.about, " +
                        "ve.reward, ve.slot, o.name AS organizerName, o.logo AS organizerLogo, o.description AS organizerDesc " +
                        "FROM Volunteer_event AS ve " +
                        "INNER JOIN Organizer AS o ON ve.organizerId = o.organizerId";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Project project = new Project();

                project.setProjectName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                project.setImageProject(cursor.getInt(cursor.getColumnIndexOrThrow("image")));
                project.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                project.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                project.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                project.setAbout(cursor.getString(cursor.getColumnIndexOrThrow("about")));
                project.setReward(cursor.getInt(cursor.getColumnIndexOrThrow("reward")));
                project.setSlot(cursor.getInt(cursor.getColumnIndexOrThrow("slot")));
                project.setOrganizer(new Organizer(
                        cursor.getString(cursor.getColumnIndexOrThrow("organizerName")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("organizerLogo")),
                        cursor.getString(cursor.getColumnIndexOrThrow("organizerDesc"))));

//                Log.i("DB HELPERRRRRR", "ID EVENTTTT = " + cursor.getInt(cursor.getColumnIndexOrThrow("eventId")));

                projects.add(project);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return projects;
    }

    public ArrayList<Project> getThreeProjectInformation(){
        ArrayList<Project> projects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT ve.name, ve.image, ve.date, ve.time, ve.location, ve.about, " +
                "ve.reward, ve.slot, o.name AS organizerName, o.logo AS organizerLogo, o.description AS organizerDesc " +
                "FROM Volunteer_event AS ve " +
                "INNER JOIN Organizer AS o ON ve.organizerId = o.organizerId " +
                "ORDER BY ve.eventId LIMIT 3";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Project project = new Project();

                project.setProjectName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                project.setImageProject(cursor.getInt(cursor.getColumnIndexOrThrow("image")));
                project.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                project.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                project.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                project.setAbout(cursor.getString(cursor.getColumnIndexOrThrow("about")));
                project.setReward(cursor.getInt(cursor.getColumnIndexOrThrow("reward")));
                project.setSlot(cursor.getInt(cursor.getColumnIndexOrThrow("slot")));
                project.setOrganizer(new Organizer(
                        cursor.getString(cursor.getColumnIndexOrThrow("organizerName")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("organizerLogo")),
                        cursor.getString(cursor.getColumnIndexOrThrow("organizerDesc"))));

                projects.add(project);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return projects;
    }

    public ArrayList<Project> getAllUserProject(int userId) {
        ArrayList<Project> userProjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(
                "SELECT ve.eventId, rewardStatus, userId, ve.name, ve.image, ve.date, ve.location, ve.reward, o.logo AS organizerLogo FROM Volunteer_event AS ve JOIN User_volunteer_event AS uve ON ve.eventId = uve.eventId JOIN Organizer AS o ON o.organizerId = ve.organizerId WHERE userId = ? AND rewardStatus = 1", new String[]{String.valueOf(userId)});
//        Cursor cursor = db.rawQuery("SELECT * FROM User_Volunteer_event", new String[]{});
//        Log.i("DB HELPER", "Cursor = " + cursor);
//        Log.i("DB HELPER",  "Project Name = " + cursor.getString(cursor.getColumnIndexOrThrow("name")));
        try {
            if(cursor.moveToFirst()) {
                do{
                    Project project = new Project();
                    project.setProjectId(cursor.getInt(cursor.getColumnIndexOrThrow("eventId")));
                    project.setProjectName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    project.setImageProject(cursor.getInt(cursor.getColumnIndexOrThrow("image")));
                    project.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                    project.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                    project.setReward(cursor.getInt(cursor.getColumnIndexOrThrow("reward")));
                    project.setOrganizer(new Organizer(
                            cursor.getInt(cursor.getColumnIndexOrThrow("organizerLogo"))));

                    userProjects.add(project);

//                    Log.i("DB HELPER",  "Project ID = " + cursor.getString(cursor.getColumnIndexOrThrow("name")));
//                    Log.i("DB HELPER", "Project = " + project + " Project Name = " + project.getProjectName());
                    Log.i("DB HELPER",  "Project ID = " + cursor.getInt(cursor.getColumnIndexOrThrow("eventId")));
                    Log.i("DB HELPER", "User ID = " + cursor.getInt(cursor.getColumnIndexOrThrow("userId")));
                    Log.i("DB HELPER", "Status Reward = " + cursor.getInt(cursor.getColumnIndexOrThrow("rewardStatus")));
                }while(cursor.moveToNext());

            }

            cursor = db.rawQuery(
                    "SELECT ve.eventId, rewardStatus, userId, ve.name, ve.image, ve.date, ve.location, ve.reward, o.logo AS organizerLogo FROM Volunteer_event AS ve JOIN User_volunteer_event AS uve ON ve.eventId = uve.eventId JOIN Organizer AS o ON o.organizerId = ve.organizerId WHERE userId = ? AND rewardStatus = 0", new String[]{String.valueOf(userId)});

            if(cursor.moveToFirst()) {
                do{
                    Project project = new Project();
                    project.setProjectName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    project.setImageProject(cursor.getInt(cursor.getColumnIndexOrThrow("image")));
                    project.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                    project.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                    project.setReward(cursor.getInt(cursor.getColumnIndexOrThrow("reward")));
                    project.setOrganizer(new Organizer(
                            cursor.getInt(cursor.getColumnIndexOrThrow("organizerLogo"))));

                    userProjects.add(project);

//                    Log.i("DB HELPER",  "Project ID = " + cursor.getString(cursor.getColumnIndexOrThrow("name")));
//                    Log.i("DB HELPER", "Project = " + project + " Project Name = " + project.getProjectName());
                    Log.i("DB HELPER",  "Project ID = " + cursor.getInt(cursor.getColumnIndexOrThrow("eventId")));
                    Log.i("DB HELPER", "User ID = " + cursor.getInt(cursor.getColumnIndexOrThrow("userId")));
                    Log.i("DB HELPER", "Status Reward = " + cursor.getInt(cursor.getColumnIndexOrThrow("rewardStatus")));
                }while(cursor.moveToNext());

            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return userProjects;
    }

    public ArrayList<Project> getFiveUserProject(int userId) {
        ArrayList<Project> userProjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(
                "SELECT ve.eventId, userId, rewardStatus, ve.name, ve.image, ve.date, ve.location, ve.reward, o.logo AS organizerLogo FROM Volunteer_event AS ve JOIN User_volunteer_event AS uve ON ve.eventId = uve.eventId JOIN Organizer AS o ON o.organizerId = ve.organizerId WHERE userId = ? ORDER BY rewardStatus DESC LIMIT 5", new String[]{String.valueOf(userId)});
//        Cursor cursor = db.rawQuery("SELECT * FROM User_Volunteer_event", new String[]{});
//        Log.i("DB HELPER", "Cursor = " + cursor);
//        Log.i("DB HELPER",  "Project Name = " + cursor.getString(cursor.getColumnIndexOrThrow("name")));
        try {
            if(cursor.moveToFirst()) {
                do{
                    Project project = new Project();
                    project.setProjectId(cursor.getInt(cursor.getColumnIndexOrThrow("eventId")));
                    project.setProjectName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    project.setImageProject(cursor.getInt(cursor.getColumnIndexOrThrow("image")));
                    project.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                    project.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                    project.setReward(cursor.getInt(cursor.getColumnIndexOrThrow("reward")));
                    project.setOrganizer(new Organizer(
                            cursor.getInt(cursor.getColumnIndexOrThrow("organizerLogo"))));

                    userProjects.add(project);

//                    Log.i("DB HELPER",  "Project ID = " + cursor.getString(cursor.getColumnIndexOrThrow("name")));
//                    Log.i("DB HELPER", "Project = " + project + " Project Name = " + project.getProjectName());
                    Log.i("DB HELPER",  "Project ID = " + cursor.getInt(cursor.getColumnIndexOrThrow("eventId")));
                    Log.i("DB HELPER", "User ID = " + cursor.getInt(cursor.getColumnIndexOrThrow("userId")));
                    Log.i("DB HELPER", "Status Reward = " + cursor.getInt(cursor.getColumnIndexOrThrow("rewardStatus")));
                }while(cursor.moveToNext());

            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return userProjects;
    }

    public boolean getUserProjectRewardStatus(int userId, int eventId){

        Log.i("DB HELPER = ", " User ID = " + userId + "Event ID = " + eventId);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT rewardStatus FROM User_volunteer_event WHERE userId = ? AND eventId = ?", new String[]{String.valueOf(userId), String.valueOf(eventId)});

        boolean rewardStatus = false;
        if(cursor.moveToFirst()){
            if(cursor.getInt(cursor.getColumnIndexOrThrow("rewardStatus")) == 1){
                rewardStatus = true;
                Log.i("DB HELPER", " REWARD STATUS ? = " + rewardStatus + "cursor ? = " + cursor.getInt(cursor.getColumnIndexOrThrow("rewardStatus")) );
            }
        }
        cursor.close();
        db.close();
        return rewardStatus;
    }

    public void updateUserProjectStatus(int userId, int eventId){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE User_volunteer_event SET rewardStatus = 0 WHERE userId = ? AND eventId = ?";
        db.execSQL(query, new String[]{String.valueOf(userId), String.valueOf(eventId)});
        db.close();
    }

    public void updateUserPoint(int userId, int point){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE User SET volunteerPoints = volunteerPoints + ? WHERE userId = ?";
        db.execSQL(query, new Object[]{point, String.valueOf(userId)});
        db.close();
    }

    public boolean validatePassword(String currentPassword, String oldPassword){
        if(!currentPassword.equals(oldPassword)) return false;
        else return true;
    }

    public void validateProfile(Context context, int id, String name, String email, String oldPw, String newPw, String phoneNumber, Bitmap bitmap) {
        User user = getUserById(id);
        if(name.equals("")){
            Toast.makeText(context, "Please fill your name", Toast.LENGTH_SHORT).show();
        }
        else if(email.equals("")){
            Toast.makeText(context, "Please fill your email", Toast.LENGTH_SHORT).show();
        }
        else if(oldPw.equals("") && !newPw.equals("")){
            Toast.makeText(context, "Please fill your old password to change it", Toast.LENGTH_SHORT).show();
        }
        else if(!oldPw.equals("") && newPw.equals("")){
            Toast.makeText(context, "Please fill your new password to change it", Toast.LENGTH_SHORT).show();
        }
        else if(!oldPw.equals("") && !newPw.equals("") && !validatePassword(user.getPassword(), oldPw)){
            Toast.makeText(context, "Your old password is incorrect", Toast.LENGTH_SHORT).show();
        }
        else if(!phoneNumber.equals("") && phoneNumber.length() < 10 || phoneNumber.length() > 12){
            Toast.makeText(context, "Phone number length must be between 10-12 digits", Toast.LENGTH_SHORT).show();
        }
        else {
            user.setName(name);
            user.setEmail(email);
            if(!oldPw.equals("") && !newPw.equals("")) user.setPassword(newPw);
            if(!phoneNumber.equals("")) user.setPhoneNumber(phoneNumber);
            if(bitmap != null) user.setImage(bitmap);
            Log.i("DB HELPER USERRR di dalam", "user name = " + user.getName() + user.getEmail() + user.getPassword()+ user.getImage());
             updateUser(user, id);
        }
        Log.i("DB HELPER USERRR", "user name = " + user.getName() + user.getEmail() + user.getPassword()+ user.getImage());
    }
}
