package ng.projectcreate.sunshine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Emem on 11/13/18.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    Context context;
    ArrayList<WeatherData> weatherDataArrayList;

    public WeatherAdapter(Context context, ArrayList<WeatherData> weatherDataArrayList) {
        this.context = context;
        this.weatherDataArrayList = weatherDataArrayList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_layout_item, parent, false);

        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherData weatherData = weatherDataArrayList.get(position);

        holder.weatherDataTv.setText(weatherData.getWeatherData());
        holder.dateTv.setText(weatherData.getDate());
    }

    @Override
    public int getItemCount() {
        return weatherDataArrayList.size();
    }

    public void setData(ArrayList<WeatherData> weatherDataArrayList){
        this.weatherDataArrayList = weatherDataArrayList;
        notifyDataSetChanged();

    }

    //viewholder
    class WeatherViewHolder extends RecyclerView.ViewHolder {
//        1. get a reference to our layout view
        ImageView imageView;
        TextView weatherDataTv, dateTv;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            weatherDataTv = itemView.findViewById(R.id.textView_weather);
            dateTv = itemView.findViewById(R.id.textView_date);

        }
    }

}
