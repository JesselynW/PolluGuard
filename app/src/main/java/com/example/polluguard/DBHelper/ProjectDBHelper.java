package com.example.polluguard.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.polluguard.R;
import com.example.polluguard.model.Organizer;
import com.example.polluguard.model.Project;

import java.util.ArrayList;

public class ProjectDBHelper extends SQLiteOpenHelper {

    public static final String dbName = "project.db";

    public ProjectDBHelper(Context context){
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Project(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "projectName TEXT," +
                "projectImage INTEGER," +
                "date TEXT," +
                "time TEXT," +
                "location TEXT," +
                "about TEXT," +
                "price TEXT," +
                "reward INTEGER," +
                "slot INTEGER," +
                "maxSlot INTEGER," +
                "organizerName TEXT," +
                "organizerLogo INTEGER," +
                "organizerDesc TEXT);");


        //datanya bakal ketambah sekali pas databasenya start jadi kalo misalnya mau ganti isi datanya harus update pake query

        sqLiteDatabase.execSQL
                ("INSERT INTO Project VALUES(NULL, 'Garbage Collection Drive', " + R.drawable.children_collects_garbage_garbage_bags_park + ", '15th February 2024', '09.00-13.00', 'Jl. Jalur Sutera Bar. No.Kav. 21, Kota Tangerang, Banten', 'A volunteer-driven garbage collection event to clean up public roads. ', '$2', 20, 15, 15,'EcoSolutions Volunteers', " + R.drawable.rectangle_160 + ", 'Established in 1998, EcoVolunteers is a community-driven non-profit organization focused on environmental projects. Since its inception, they’ve been actively working to promotes sustainability.')");

        sqLiteDatabase.execSQL
                ("INSERT INTO Project VALUES(NULL, 'Plastic Waste Awareness', " + R.drawable.close_up_hand_collecting_bottle + ", '10th March 2024', '08.30-12.30', 'Kebayoran Baru, Jakarta', 'An act to raise awareness about plastic waste and recycling methods. ', 'FREE', 12, 30, 30,'GreenFuture Volunteers', " + R.drawable.rectangle_160__1_ + ", 'GreenFuture Volunteers focuses on environmental restoration and waste management projects since 2005.')");

        sqLiteDatabase.execSQL
                ("INSERT INTO Project VALUES(NULL, 'Tree Planting Initiative', " + R.drawable.close_up_picture_hand_holding_planting_sapling_plant + ", '20th April 2024', '07.00-10.00', 'Jl. Imam Bonjol No.12, Jakarta', 'An initiative to plant trees in urban areas to improve air quality. ', '$2', 45, 30, 30,'EcoSolutions Volunteers', " + R.drawable.rectangle_160 + ", 'Established in 1998, EcoVolunteers is a community-driven non-profit organization focused on environmental projects. Since its inception, they’ve been actively working to promotes sustainability.')");

        sqLiteDatabase.execSQL
                ("INSERT INTO Project VALUES(NULL, 'Sorting Plastics', " + R.drawable.different_people_doing_volunteer_work_with_food + ", '19th May 2024', '08.00-12.00', 'Jl. Setiabudi No. 19, Jakarta', 'A community effort to clean and sort public parks garbage.', '$1', 10, 25, 25,'CleanEarth Community', " + R.drawable.rectangle_160__2_ + ", 'CleanEarth Community is a group of eco-activists dedicated to urban cleanliness.')");

        sqLiteDatabase.execSQL
                ("INSERT INTO Project VALUES(NULL, 'Park Cleanup', " + R.drawable.group_volunteers_collecting_garbage + ", '17th June 2024', '06.00-10.00', 'Kuta Beach, Bali', 'A park cleanup event to remove plastic and other pollutants. ', 'FREE', 45, 30, 30, 'EcoMinds Initiative', " + R.drawable.rectangle_160__3_ + ", 'EcoMinds Initiative is an environmental group dedicated to waste reduction and sustainability. Their mission is to educate the public on better waste disposal methods and recycling techniques.')");

        sqLiteDatabase.execSQL
                ("INSERT INTO Project VALUES(NULL, 'Reforestation Drive', " + R.drawable.reforestation_done_by_voluntary_group + ", '04th July 2024', '07.00-12.00', 'Taman Nasional Gunung Gede, Bogor', 'Reforestation activities in national parks to restore green cover. ', 'FREE', 15, 10, 10, 'EcoMinds Initiative', " + R.drawable.rectangle_160__3_ + ", 'EcoMinds Initiative is an environmental group dedicated to waste reduction and sustainability. Their mission is to educate the public on better waste disposal methods and recycling techniques.')");

        sqLiteDatabase.execSQL
                ("INSERT INTO Project VALUES(NULL, 'Community Reforestation', " + R.drawable.reforestation_done_by_voluntary_group__1_ + ", '11th July 2024', '08.00-12.00', 'Taman Nasional Gunung Gede, Bogor', 'Reforestation activities in national parks to restore green cover. ', '$1', 25, 15, 15, 'EcoSolutions Volunteers', " + R.drawable.rectangle_160 + ", 'Established in 1998, EcoVolunteers is a community-driven non-profit organization focused on environmental projects. Since its inception, they’ve been actively working to promotes sustainability.')");

        sqLiteDatabase.execSQL
                ("INSERT INTO Project VALUES(NULL, 'Urban Green Project', " + R.drawable.team_environment_volunteers_digging_holes_planting_small_trees + ", '27th August 2024', '08.00-12.00', 'Jl. Sudirman, Jakarta', 'An urban greening initiative to transform unused spaces into green, sustainable areas.', '$2', 20, 30, 30, 'GreenFuture Volunteers', " + R.drawable.rectangle_160__1_ + ", 'GreenFuture Volunteers focuses on environmental restoration and waste management projects since 2005.')");

        sqLiteDatabase.execSQL
                ("INSERT INTO Project VALUES(NULL, 'Clean Breeze', " + R.drawable.volunteer_team_cleaning_city_grass_from_garbage + ", '14th October 2024', '08.00-12.00', 'Jl.Sudirman, Jakarta', 'A massive city-wide campaign to clean streets and public spaces.', '$4', 45, 30, 30, 'EcoMinds Initiative', " + R.drawable.rectangle_160__3_ + ", 'EcoMinds Initiative is an environmental group dedicated to waste reduction and sustainability. Their mission is to educate the public on better waste disposal methods and recycling techniques.')");

        sqLiteDatabase.execSQL
                ("INSERT INTO Project VALUES(NULL, 'SkySavers', " + R.drawable.volunteers_cleaning_up_nice_woods + ", '21th October 2024', '08.00-12.00', 'Setiabudi, Jakarta', 'A volunteer-driven garbage collection event to clean up public parks.', '$1', 25, 30, 30, 'EcoSolutions Volunteers', " + R.drawable.rectangle_160 + ", 'Established in 1998, EcoVolunteers is a community-driven non-profit organization focused on environmental projects. Since its inception, they’ve been actively working to promotes sustainability.')");

        sqLiteDatabase.execSQL
                ("INSERT INTO Project VALUES(NULL, 'Forest Conservation Advocacy', " + R.drawable.youngster_advocating_conservation_forest_ecosystems_by_holding_seedling + ", '30th November 2024', '07.00-11.00', 'Jl. Kebun Raya, Bogor', 'An act to promote conservation and awareness of forest ecosystems. ', '$4', 15, 30, 30, 'GreenFuture Volunteers', " + R.drawable.rectangle_160__1_ + ", 'GreenFuture Volunteers focuses on environmental restoration and waste management projects since 2005.')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Project");
    }

    public ArrayList<Project> getAllProjectInformation(){
        ArrayList<Project> projects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Project";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Project project = new Project();

                project.setProjectName(cursor.getString(cursor.getColumnIndexOrThrow("projectName")));
                project.setImageProject(cursor.getInt(cursor.getColumnIndexOrThrow("projectImage")));
                project.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                project.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                project.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                project.setAbout(cursor.getString(cursor.getColumnIndexOrThrow("about")));
                project.setPrice(cursor.getString(cursor.getColumnIndexOrThrow("price")));
                project.setReward(cursor.getInt(cursor.getColumnIndexOrThrow("reward")));
                project.setSlot(cursor.getInt(cursor.getColumnIndexOrThrow("slot")));
                project.setMaxSlot(cursor.getInt(cursor.getColumnIndexOrThrow("maxSlot")));
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

    public ArrayList<Project> getThreeProjectInformation(){
        ArrayList<Project> projects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Project LIMIT 3";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Project project = new Project();

                project.setProjectName(cursor.getString(cursor.getColumnIndexOrThrow("projectName")));
                project.setImageProject(cursor.getInt(cursor.getColumnIndexOrThrow("projectImage")));
                project.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                project.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                project.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                project.setAbout(cursor.getString(cursor.getColumnIndexOrThrow("about")));
                project.setPrice(cursor.getString(cursor.getColumnIndexOrThrow("price")));
                project.setReward(cursor.getInt(cursor.getColumnIndexOrThrow("reward")));
                project.setSlot(cursor.getInt(cursor.getColumnIndexOrThrow("slot")));
                project.setMaxSlot(cursor.getInt(cursor.getColumnIndexOrThrow("maxSlot")));
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
}
