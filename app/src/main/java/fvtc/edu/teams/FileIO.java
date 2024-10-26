package fvtc.edu.teams;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileIO {
    public static final String TAG = "FileIO";
    public ArrayList<String> readFile(String filename, AppCompatActivity activity){
        Log.d(TAG, "readFile: start");
        ArrayList<String> items = new ArrayList<String>();
        try{
            InputStream inputStream = activity.openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null){
                items.add(line);
            }
            bufferedReader = null;
            inputStreamReader.close();
            inputStream.close();

        }catch (Exception e){
            Log.d(TAG, "readFile: " + e.getMessage());
        }
        Log.d(TAG, "readFile: end");
        Log.d(TAG, "readFile: " + items);
        return items;
    }
    public void writeFile(String filename, AppCompatActivity activity, String[] items) /*throws FileNotFoundException if not in try catch block*/ {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(activity.openFileOutput(filename, Context.MODE_PRIVATE));
            Log.d(TAG, "writeFile: " + filename);
            String line = "";
            for(int counter = 0; counter < items.length; counter++){
                line = items[counter];
                if(counter < items.length - 1) {line += "\n";}
                writer.write(line);
                Log.d(TAG, "writeFile: " + line);
                writer.close();
            }

        } catch (FileNotFoundException e) {
            Log.d(TAG, "WriteFile: FileNotFoundException" + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "WriteFile: IOException" + e.getMessage());
        }
        catch (Exception e){
            Log.i(TAG, "WriteFile: " + e.getMessage());
        }
    }

    public ArrayList<Team> readXMLFile(String filename, AppCompatActivity activity){
        ArrayList<Team> Teams = new ArrayList<Team>();
        Log.d(TAG, "readXMLFile: start");
        try{
            InputStream inputStream = activity.openFileInput(filename);
            XmlPullParser xmlPullParser = Xml.newPullParser();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            xmlPullParser.setInput(inputStreamReader);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


            while (xmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT){
                if(xmlPullParser.getEventType() == XmlPullParser.START_TAG){
                    if(xmlPullParser.getName().equals("Team")){
                        int id = Integer.parseInt(xmlPullParser.getAttributeValue(null, "id"));
                        String name = xmlPullParser.getAttributeValue(null, "name");
                        String city = xmlPullParser.getAttributeValue(null, "city");
                        String cellphone = xmlPullParser.getAttributeValue(null, "lastname");
                        float rating = Float.parseFloat(xmlPullParser.getAttributeValue(null, "cellphone"));;
                        int imgId = Integer.parseInt(xmlPullParser.getAttributeValue(null, "rating"));;
                        boolean isFavorite = Boolean.parseBoolean(xmlPullParser.getAttributeValue(null, "isFavorite"));;
                        double latitude = Double.parseDouble(xmlPullParser.getAttributeValue(null, "latitude"));;
                        double longitude = Double.parseDouble(xmlPullParser.getAttributeValue(null, "longitude"));;
                        Team Team = new Team(id, name, city, cellphone, rating, imgId, isFavorite, latitude, longitude);
                        Teams.add(Team);
                        Log.d(TAG, "readXMLFile: " + Team.toString());
                    }
                }
                xmlPullParser.next();
            }
            bufferedReader = null;
            inputStreamReader.close();
            inputStream.close();

        }catch (Exception e){
            Log.d(TAG, "readFile: " + e.getMessage());
        }
        Log.d(TAG, "readXMLFile: end");
        return Teams;
    }
    public void writeXMLFile(String filename, AppCompatActivity activity, ArrayList<Team> Teams) {
        Log.d(TAG, "writeXMLFile: start");
        XmlSerializer serializer = Xml.newSerializer();
        File file = new File(filename);
        try {
            file.createNewFile();
            OutputStreamWriter writer = new OutputStreamWriter(activity.getApplicationContext().openFileOutput(filename,Context.MODE_PRIVATE));

            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("","Teams");
            serializer.attribute("","number", String.valueOf(Teams.size()));

            for (Team Team : Teams){
                serializer.startTag("", "Team");
                serializer.attribute("","id", String.valueOf(Team.getId()));
                serializer.attribute("", "name", String.valueOf(Team.getName()));
                serializer.attribute("", "city", String.valueOf(Team.getCity()));
                serializer.attribute("","cellphone", String.valueOf(Team.getCellPhone()));
                serializer.attribute("", "rating", String.valueOf(Team.getName()));
                serializer.attribute("", "imgId", String.valueOf(Team.getCity()));
                serializer.attribute("","rating", String.valueOf(Team.getId()));
                serializer.attribute("", "latitude", String.valueOf(Team.getName()));
                serializer.attribute("", "longitude", String.valueOf(Team.getCity()));
                serializer.endTag("", "Team");
                Log.d(TAG, "writeXMLFile: " + Team.toString());
            }

            serializer.endTag("", "Teams");
            serializer.endDocument();
            serializer.flush();
            writer.close();
            Log.d(TAG, "writeXMLFile: " + Teams.size() + " Teams written.");
        }catch (Exception e){
            Log.d(TAG, "writeXMLFile: " + e.getMessage());
        }
        Log.d(TAG, "writeXMLFile: end");
    }
}
