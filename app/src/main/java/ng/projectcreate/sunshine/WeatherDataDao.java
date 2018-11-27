package ng.projectcreate.sunshine;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface WeatherDataDao {

    @Query("SELECT * FROM weatherData")
    List<WeatherData> getAllWeatherData();

    @Insert
    void insertAll(List<WeatherData> weatherData);

    @Delete
    void delete(WeatherData weatherData);


}
