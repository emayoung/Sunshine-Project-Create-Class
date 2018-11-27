package ng.projectcreate.sunshine;

/**
 * Created by Emem on 11/13/18.
 */
public class WeatherData {
    int imageResource;
    String weatherData;
    String date;

    public WeatherData() {
    }

    public WeatherData(int imageResource, String weatherData, String date) {
        this.imageResource = imageResource;
        this.weatherData = weatherData;
        this.date = date;
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
