package fvtc.edu.teams;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class TeamsDataSource {
    SQLiteDatabase database;
    DatabaseHelper dbHelper;
    public static final String TAG = "TeamsDataSource";

    public TeamsDataSource(Context context){
        dbHelper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, null, 1);
    }
    public void open() throws SQLException{
        open(false);
    }

    public void open(boolean refresh) throws SQLException{
        database = dbHelper.getWritableDatabase();
        Log.d(TAG, "open: " + database.isOpen());
        if(refresh) refreshData();
    }
    public  void close(){
        dbHelper.close();
    }

    public void refreshData(){
        ArrayList<Team> teams = new ArrayList<Team>();
        Log.d(TAG, "refreshData: start");
        teams = new ArrayList<Team>();

        teams.add(new Team(1,"Packers","Green Bay","9205551234", 1, R.drawable.packers, true, 0.0,0.0));
        teams.add(new Team(2,"Lions","Detroit","9204441234", 2, R.drawable.lions, false, 0.0,0.0));
        teams.add(new Team(3,"Vikings","Minneapolis","9203331234", 3, R.drawable.vikings, false, 0.0,0.0));
        teams.add(new Team(4,"Bears","Chicago","9202221234", 4, R.drawable.bears, false, 0.0,0.0));

        // delete and reinsert all teams
        int results = 0;
        for(Team team : teams){
            results += insert(team);
        }
        Log.d(TAG, "refreshData: end: " + results + " rows...");
    }

    public Team get(int id){
        Team team = null;

        try{
            String query = "Select * from tblTeam where id = " + id;
            Cursor cursor = database.rawQuery(query, null);

            //Cursor cursor = db.query("tblGroceryList",null, null, null, null, null, null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                team.setId(cursor.getInt(0));
                team.setName(cursor.getString(1));
                team.setCity(cursor.getString(2));
                team.setImgId(cursor.getInt(3));
                Boolean fav = (cursor.getInt(4) == 1);
                team.setIsFavorite(fav);
                team.setRating(cursor.getFloat(5));
                team.setCellPhone(cursor.getString(6));
                team.setLatitude(cursor.getDouble(7));
                team.setLongitude(cursor.getDouble(8));


                //item.setLatitude(cursor.getDouble(7));
                //item.setLongitude(cursor.getDouble(8));

                Log.d(TAG, "get: " + team.toString());

                cursor.moveToNext();

            }
        }
        catch(Exception e)
        {
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }
        return team;
    }
    public ArrayList<Team> get(){
        Log.d(TAG, "get: Start");
        ArrayList<Team> teams = new ArrayList<Team>();
        try{
            Log.i(TAG, "get: inside try block");
            String sql = "SELECT * from tblTeam";
            Cursor cursor = database.rawQuery(sql, null);
            Team team;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Log.i(TAG, "get: inside cursor");
                team = new Team();
                team.setId(cursor.getInt(0));
                team.setName(cursor.getString(1));
                team.setCity(cursor.getString(2));
                team.setImgId(cursor.getInt(3));
                Boolean fav = (cursor.getInt(4) == 1);
                team.setIsFavorite(fav);
                team.setRating(cursor.getFloat(5));
                team.setCellPhone(cursor.getString(6));
                team.setLatitude(cursor.getDouble(7));
                team.setLongitude(cursor.getDouble(8));
                if(team.getImgId() == 0) team.setImgId(R.drawable.photoicon);

                teams.add(team);
                Log.d(TAG, "get: " + team.toString());
                cursor.moveToNext();
            }
        }catch (Exception e){
            Log.i(TAG, "get: error");
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }
        return teams;
    }
    public int deleteAll(){
        Log.d(TAG, "deleteAll: ");
        try{
            return database.delete("tblTeam", null, null);
        }
        catch(Exception e)
        {
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    public int delete(Team team)
    {
        Log.d(TAG, "delete: Start item");
        try{
            int id = team.getId();
            if(id < 1)
                return 0;
            Log.d(TAG, "delete: " + id);
            return delete(id);
        }
        catch(Exception e)
        {
            Log.d(TAG, "Delete: failed: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    public int delete(int id){
        try{
            Log.d(TAG, "delete: Start : " + id);
            Log.d(TAG, "delete: database " + database.isOpen());
            int rowsaffected = database.delete("tblTeam", "id = " + id, null);
            Log.d(TAG, "delete: rowsaffected: " + rowsaffected);
            return rowsaffected;
        }
        catch(Exception e)
        {
            Log.d(TAG, "Delete: error" + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    public int getNewId(){
        int newId = -1;
        try {
            //get the highest id in table and add 1
            String sql = "SELECT max(id) from tblTeam";
            Cursor cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
            newId = cursor.getInt(0) + 1;
            cursor.close();
        }catch (Exception e){
            return newId;
        }
        return 0;
    }
    public int insert(Team team){

        Log.d(TAG, "insert: Start");
        int rowsaffected = 0;
        String rowAffected;
        try{
            if(database != null) {
                ContentValues values = new ContentValues();
                values.put("name", team.getName());
                values.put("city", team.getCity());
                values.put("imgId", team.getImgId());
                values.put("isFavorite", team.getIsFavorite());
                values.put("rating", team.getRating());
                values.put("phone", team.getCellPhone());
                values.put("latitude", team.getLatitude());
                values.put("longitude", team.getLongitude());

                rowsaffected = (int)database.insert("tblTeam", null, values);
            }
            else Log.d(TAG, "insert: db is null");

        }
        catch(Exception e)
        {
            Log.d(TAG, "insert: " + e.getMessage());
            e.printStackTrace();
        }
        return rowsaffected;
    }
    public int update(Team team){
        Log.d(TAG, "update: Start " + team.toString());
        int rowsaffected = 0;

        if(team.getId() < 1)
            return insert(team);
        try{
            ContentValues values = new ContentValues();
            values.put("name", team.getName());
            values.put("city", team.getCity());
            values.put("imgId", team.getImgId());
            values.put("isFavorite", team.getIsFavorite());
            values.put("rating", team.getRating());
            values.put("phone", team.getCellPhone());
            values.put("latitude", team.getLatitude());
            values.put("longitude", team.getLongitude());

            String where = "id = " + team.getId();
            rowsaffected = (int)database.update("tblTeam", values, where, null);
            Log.d(TAG, "update: end");
        } catch(Exception e)
        {
            Log.i(TAG, "update: hit error");
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }
        Log.d(TAG, "update: rowsaffected: " + rowsaffected);
        return rowsaffected;
    }
}
