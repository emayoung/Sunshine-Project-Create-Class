package ng.projectcreate.sunshine;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Emem on 11/13/18.
 */
@Entity(tableName = "weatherData")
public class WeatherData {
    @PrimaryKey(autoGenerate = true)
    int id;
    int imageResource;
    String weatherData;
    String date;

    public WeatherData() {
    }

//    @Ignore
    public WeatherData(int imageResource, String weatherData, String date) {
        this.imageResource = imageResource;
        this.weatherData = weatherData;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WeatherData(String weatherData, String date) {
        this.weatherData = weatherData;
        this.date = date;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(String weatherData) {
        this.weatherData = weatherData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
