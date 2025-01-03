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

import com.example.polluguard.model.Article;
import com.example.polluguard.model.Organizer;
import com.example.polluguard.model.Project;
import com.example.polluguard.model.User;
import com.google.android.material.shape.CutCornerTreatment;

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
        addArticle(db);
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
        Log.i("DB HELPER UPDATE", "CHECK PHONE NUMBER pertama sebelum update= " + user.getPhoneNumber());
        SQLiteDatabase db = this.getWritableDatabase();

        Bitmap image = user.getImage();
        byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byteImage = byteArrayOutputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        if(!user.getPassword().equals("")) values.put("password", user.getPassword());
        if(user.getPhoneNumber() != null){
            Log.i("DB HELPER UDPATE", "MASUK KAH? KE DALAM IF NYA");
            values.put("phoneNumber", user.getPhoneNumber());
        }
        if(user.getImage() != null) values.put("image", byteImage);


        long checkQuery = db.update("User", values, "userId = ?", new String[]{String.valueOf(id)});
        Log.i("DB HELPER UPDATE", "CHECK PHONE NUMBER= " + values.get("phoneNumber"));
        Log.i("DB HELPER UPDATE", "CHECK PHONE NUMBER setelah update= " + user.getPhoneNumber());
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
        String phoneNumber = "";
        int volunteerPoints = 0;
        Bitmap image = null;

        User user = null;

        try {
            if(cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                volunteerPoints = cursor.getInt(cursor.getColumnIndexOrThrow("volunteerPoints"));
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"));
                byte[] imageBlob = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                image = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
            }

            user = new User(name, email, password, phoneNumber, volunteerPoints, image);
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
                        "ve.reward, ve.slot, ve.linkWhatsapp, ve.whatsappQR, o.name AS organizerName, o.logo AS organizerLogo, o.description AS organizerDesc " +
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
                project.setLinkWA(cursor.getString(cursor.getColumnIndexOrThrow("linkWhatsapp")));
                project.setQr(cursor.getInt(cursor.getColumnIndexOrThrow("whatsappQR")));
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

    public void insertUserVolunteerEvent(int userId, String eventName){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT eventId FROM Volunteer_Event WHERE name = ?";
        Cursor cursor = db.rawQuery(query, new String[]{eventName});

        String query2 = "SELECT * FROM User_volunteer_event WHERE userId = ? AND eventId = ?";

        if(cursor.moveToFirst()){
            int eventId = cursor.getInt(cursor.getColumnIndexOrThrow("eventId"));

            Cursor cursor1 = db.rawQuery(query2, new String[]{String.valueOf(userId), String.valueOf(eventId)});

            if(!cursor1.moveToFirst()){
                ContentValues contentValues = new ContentValues();
                contentValues.put("userId", userId);
                contentValues.put("eventId", eventId);

                db.insert("User_volunteer_event", null, contentValues);

                Log.i("db insert", "successfully inserted data");
            }

            cursor1.close();
        }

        cursor.close();
        db.close();
    }

    public void addArticle(SQLiteDatabase db){
        db.execSQL("INSERT INTO Article VALUES(NULL, 'The Impact of Air Pollution on Public Health', 'Lestari Wibawa', '3 Desember 2024', 'Air pollution has become one of the most pressing environmental issues of our time. It leads to respiratory and cardiovascular diseases, impacts mental health, and increases mortality rates. The major sources of air pollution include vehicle emissions, industrial processes, deforestation, and burning of fossil fuels. Governments worldwide are implementing stricter regulations, while individuals can contribute by adopting eco-friendly habits like using public transportation and reducing waste. This article delves into the long-term effects on health, economic burdens, and innovative solutions like green technology.', " + R.drawable.article1 + ");");

        db.execSQL("INSERT INTO Article VALUES(NULL, 'Understanding AQI: How to Measure Air Quality', 'Al Hamasih', '10 November 2024', 'The Air Quality Index (AQI) is a standardized tool that provides real-time information about air pollution levels. It is calculated based on pollutants like PM2.5, PM10, ozone, sulfur dioxide, nitrogen dioxide, and carbon monoxide. This article explains the color-coded system used in AQI reporting and its significance in warning vulnerable populations, including children, the elderly, and people with pre-existing conditions. Practical tips on how to interpret AQI data and adjust daily activities for better health protection are also covered.', " + R.drawable.article2 + ")");

        db.execSQL("INSERT INTO Article VALUES(NULL, '10 Ways to Reduce Air Pollution in Your Community', 'Ali Jusuf', '15 Oktober 2024', 'Air pollution not only affects global ecosystems but also has immediate effects on local communities. This article lists 10 actionable ways to reduce air pollution, including switching to renewable energy, reducing vehicle usage, advocating for better public policies, and supporting clean energy initiatives. Community-driven actions such as planting trees, organizing recycling programs, and raising awareness about sustainable living can have a massive impact. Discover how small changes, when adopted collectively, can significantly improve air quality and public health.', " + R.drawable.article3 + ")");

        db.execSQL("INSERT INTO Article VALUES(NULL, 'The Role of Environmentalists in Combating Climate Change', 'Kelly Yue', '25 September 2024', 'Environmentalists are at the forefront of combating climate change, engaging in activities like tree planting, waste management campaigns, and climate change advocacy. This article explores their essential contributions, including policy lobbying, community education, and research into alternative technologies. Highlighting successful case studies worldwide, it discusses how their relentless efforts inspire governments, corporations, and individuals to join the fight against climate change.', " + R.drawable.article4 + ")");

        db.execSQL("INSERT INTO Article VALUES(NULL, 'Effects of Vehicle Emissions on Urban Air Quality', 'Laskar Cahyadi', '20 Agustus 2024', 'Urban areas face severe air quality degradation due to vehicle emissions, which release harmful pollutants like carbon monoxide, nitrogen oxides, and hydrocarbons. This article investigates the direct impact on urban populations, including increased cases of asthma, heart disease, and cancer. It also explores sustainable alternatives, such as electric vehicles, improved public transportation, and car-sharing initiatives. Learn about the latest technologies and policies designed to curb the environmental impact of transportation systems.', " + R.drawable.article5 + ")");

        db.execSQL("INSERT INTO Article VALUES(NULL, 'The Link Between Deforestation and Air Quality', 'Sophia Sheryl Susanto', '10 September 2024', 'Deforestation not only contributes to climate change but also severely impacts air quality. Forests act as natural air filters, absorbing pollutants and producing oxygen. This article examines the reasons behind deforestation, its effects on biodiversity, and its role in amplifying air pollution. The article also highlights ongoing reforestation efforts and what individuals can do to support these initiatives, including donating to conservation projects or participating in tree-planting campaigns.', " + R.drawable.article6 + ")");

        db.execSQL("INSERT INTO Article VALUES(NULL, 'How Industrial Pollution Affects Rural Areas', 'Hasan Imran', '12 Agustus 2024', 'Industrial pollution is often seen as an urban issue, but its impact on rural areas is significant and far-reaching. Pollutants from factories can travel long distances, contaminating water sources, soil, and air in rural regions. This article focuses on lesser-known consequences, such as the loss of agricultural productivity, health impacts on rural populations, and the challenges in monitoring and regulating these effects. It also discusses how industries can adopt cleaner production techniques to minimize their environmental footprint.', " + R.drawable.article7 + ")");

        db.execSQL("INSERT INTO Article VALUES(NULL, 'Air Purifiers: Do They Really Improve Indoor Air Quality?', 'Olivia Setiawan', '11 July 2024', 'With indoor air pollution becoming a growing concern, air purifiers have emerged as a popular solution. This article provides an in-depth analysis of how air purifiers work, their effectiveness in removing various pollutants, and their limitations. It also covers tips on choosing the right air purifier based on room size, pollutant type, and budget. Finally, it evaluates alternative methods to improve indoor air quality, such as increasing ventilation and using natural air-cleaning plants.', " + R.drawable.article8 + ")");

        db.execSQL("INSERT INTO Article VALUES(NULL, 'Plastic Waste and Its Contribution to Air Pollution', 'Emma Kusuma', '23 Juni 2024', 'Plastic waste is not just a land-based issue; it also contributes to air pollution. Burning plastic releases toxic chemicals like dioxins, furans, and carbon monoxide, which pose severe health and environmental risks. This article discusses the lifecycle of plastic, from production to disposal, and its cumulative impact on air quality. Solutions such as reducing plastic usage, adopting biodegradable materials, and promoting waste-to-energy technologies are also explored.', " + R.drawable.article9 + ")");

        db.execSQL("INSERT INTO Article VALUES(NULL, 'Environmental Policies: Progress and Challenges in 2024', 'James Christiano', '22 May 2024', 'Environmental policies in 2024 have shown remarkable progress, including the adoption of stricter emission standards, expansion of renewable energy projects, and increased funding for conservation efforts. However, significant challenges remain, such as global coordination, resistance from certain industries, and the need for public engagement. This article reviews the milestones achieved this year and discusses the road ahead in tackling environmental challenges through collaboration and innovation.', " + R.drawable.article10 + ")");
    }

    public ArrayList<Article> getAllArticle() {
        ArrayList<Article> articles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Article", null);

        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();

                article.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                article.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow("author")));
                article.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                article.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
                article.setImage(cursor.getInt(cursor.getColumnIndexOrThrow("image")));

                articles.add(article);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return articles;
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
            Log.i("DI DALAM DB HELPER = ", "USER PHONE NUMBER? = " + user.getPhoneNumber());
            if(bitmap != null) user.setImage(bitmap);
            updateUser(user, id);
            Toast.makeText(context, "Successfully change profile!", Toast.LENGTH_SHORT).show();

        }
    }
}
