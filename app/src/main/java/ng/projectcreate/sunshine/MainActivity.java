package ng.projectcreate.sunshine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    RecyclerView weatherRecyclerview;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherRecyclerview = findViewById(R.id.recyclerview_weather);
        progressBar = findViewById(R.id.progressbar);

        //get a handle to the shared pref
        SharedPreferences sharedPreferences = this.getSharedPreferences("sunshine",
                Context.MODE_PRIVATE);
        List<WeatherData> weatherData = getDataInDatabase();

        if(weatherData.isEmpty()){
            Toast.makeText(this, "You are a JJC",
                    Toast.LENGTH_SHORT).show();
            fetchDataFromNetwork();

        } else {

            progressBar.setVisibility(View.GONE);
            WeatherAdapter weatherAdapter = new WeatherAdapter(MainActivity.this,
                    new ArrayList<WeatherData>(weatherData));
            weatherRecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            weatherRecyclerview.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
            weatherRecyclerview.setAdapter(weatherAdapter);

        }

    }

    private void fetchDataFromNetwork(){
        //create  a new object of our network background task
        NetworkBackgroundTask networkBackgroundTask = new NetworkBackgroundTask();
        networkBackgroundTask.execute(); //prompt the async task to run.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //get menu inflater to inflate the menu
        inflater.inflate(R.menu.main, menu); // use the inflater to inflater the layout of the menu
        return true; //return true tells our main activity that we have a menu file to inflate
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemId = item.getItemId();

        if(menuItemId == R.id.refresh){
            fetchDataFromNetwork();
        } else if(menuItemId == R.id.settings){
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);

    }


    private URL buildApiUrlEndpoint(){
        // http://api.openweathermap.org/data/2.5/forecast?id=2350813&appid=1d369d8a214bc5d3b32ad99e58f93e73

        String baseUrl = "http://api.openweathermap.org";
        String path = "/data/2.5/forecast";
        String cityIdQueryParam = "id";
        String appIdQueryParam = "appid";

        Uri  uri = Uri.parse(baseUrl + path)
                .buildUpon()
                .appendQueryParameter(cityIdQueryParam, "2350813")
                .appendQueryParameter(appIdQueryParam, "1d369d8a214bc5d3b32ad99e58f93e73")
                .build();

        try {
            URL url = new URL(uri.toString());
            Log.e("TAG", "url " + url);
            return url;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }


    private String getWeatherDataFromUrl(URL url){

        String dataFromTheServer = null; //variable to hold data from the server
        HttpURLConnection  urlConnection = null;

        try {
            //1. connect to the server to read data
            urlConnection = (HttpURLConnection) url.openConnection();
            //2. read in the data
            InputStream input = urlConnection.getInputStream();

            Scanner scanner =new Scanner(input);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext(); //checks to see if we have finished reading data from our inputstream
            if(hasInput){ //we haven't finished reading data from the input stream
                dataFromTheServer = scanner.next();
            } else {
                dataFromTheServer =  null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //3. disconnect from the server
            urlConnection.disconnect();
        }

        return dataFromTheServer;
    }


    class NetworkBackgroundTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            //get the built Url
            URL url = buildApiUrlEndpoint();
            Log.e("tag", "url endpoint " + url);
            //get data from server
            String weatherData = getWeatherDataFromUrl(url);
            return weatherData;

        }

        @Override
        protected void onPostExecute(String weatherData) {
            super.onPostExecute(weatherData);
            progressBar.setVisibility(View.GONE);
            //insert the weather data into the textview
            try {
                ArrayList<WeatherData> weatherDataArrayList = parseJsonData2(weatherData);
                WeatherAdapter weatherAdapter = new WeatherAdapter(MainActivity.this, weatherDataArrayList);
                weatherRecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                weatherRecyclerview.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
                weatherRecyclerview.setAdapter(weatherAdapter);

                //save this data in the database
                saveDataInDatabase(weatherDataArrayList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void saveDataInSharedPreference(ArrayList<WeatherData> weatherDataArrayList){
        //get a handle to the shared preference of our app
        Context context = this;
        SharedPreferences sharedPref = context
                .getSharedPreferences("sunshine", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        //weather_data is the key we are using to store the weather data arraylist
        editor.putString("weather_data", weatherDataArrayList.toString());
        editor.commit();
    }


    private void saveDataInDatabase(ArrayList<WeatherData> weatherDataArrayList){
        SunshineDatabase.createDb(this).weatherDataDao().insertAll(weatherDataArrayList);
    }

    private List<WeatherData> getDataInDatabase() {
        return SunshineDatabase.createDb(this).weatherDataDao().getAllWeatherData();
    }

    public String getSavedWeatherDataInSharedPreference(){
        //get a handle to the shared preference of our app
        Context context = this;
        SharedPreferences sharedPref = context
                .getSharedPreferences("sunshine", Context.MODE_PRIVATE);

        String weatherData = sharedPref.getString("weather_data", "");
        return weatherData;
    }


    private ArrayList<WeatherData> parseJsonData2(String weather) throws JSONException{
        ArrayList<WeatherData> weatherDataArrayList = new ArrayList<>();

        //convert the json string to json object
        Log.e("tag", "weather data " + weather);
        JSONObject jsonDataFromServer =new JSONObject(weather);
        JSONArray weatherJsonArray = jsonDataFromServer.getJSONArray("list");

        for(int i =0; i<weatherJsonArray.length(); i++){
            JSONObject todayWeatherJsonObject =  weatherJsonArray.getJSONObject(i);

            String date = todayWeatherJsonObject.getString("dt_txt");

            JSONObject mainWeatherDetails = todayWeatherJsonObject.getJSONObject("main");
            Double minTemp = mainWeatherDetails.getDouble("temp_min");
            Double maxTemp = mainWeatherDetails.getDouble("temp_max");
            Double pressure =mainWeatherDetails.getDouble("pressure");
            int humidity = mainWeatherDetails.getInt("humidity");

            JSONArray todayWeatherArray = todayWeatherJsonObject.getJSONArray("weather");
            JSONObject weatherToday = todayWeatherArray.getJSONObject(0);

            String weatherConditionForToday = weatherToday.getString("main");
            String weatherDescriptionForToday = weatherToday.getString("description");

            WeatherData weatherData  = new WeatherData(weatherConditionForToday, date);
            weatherDataArrayList.add(weatherData);


        }


        return weatherDataArrayList;

    }

}
