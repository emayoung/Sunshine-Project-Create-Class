package ng.projectcreate.sunshine;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {WeatherData.class}, version = 1)
public abstract class SunshineDatabase extends RoomDatabase {

    public abstract WeatherDataDao weatherDataDao();

    public static SunshineDatabase sunshineDatabaseInstance;


    public static SunshineDatabase createDb(Context context){
        //check if database exists
        if(sunshineDatabaseInstance == null ){
            sunshineDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    SunshineDatabase.class, "sunshine.db")
                    .build();

            return sunshineDatabaseInstance;

        }  else {
            return sunshineDatabaseInstance;
        }


    }

}
