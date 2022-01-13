
package com.example.csci571hw9yuan.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.csci571hw9yuan.R;
import com.example.csci571hw9yuan.dao.ExtraFuncs;
import com.example.csci571hw9yuan.dao.Values;

import java.lang.reflect.Field;

public class todayFragment extends Fragment {
    int position;
    private TextView textView;
    ExtraFuncs extraFuncs;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        todayFragment tabFragment = new todayFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        threeCharts charts = (threeCharts) getActivity();
        assert charts != null;
        Values weather = charts.getValues();
        extraFuncs = new ExtraFuncs();
        super.onViewCreated(view, savedInstanceState);
        TextView card1 = view.findViewById(R.id.card1);
        card1.setText(weather.getWindSpeed() + " mph");
        TextView card2 = view.findViewById(R.id.card2);
        card2.setText(weather.getPressureSeaLevel() + " inHg");
        TextView card3 = view.findViewById(R.id.card3);
        card3.setText(weather.getPrecipitationProbability() + " %");
        TextView card4 = view.findViewById(R.id.card4);
        card4.setText(Math.round(Float.parseFloat(weather.getTemperature())) + " °F");
        TextView card5 = view.findViewById(R.id.card5);
        int code = Integer.parseInt(weather.getWeatherCode());
        String status = extraFuncs.getStatusInfo(code);
        card5.setText(status);
        String imgSrc = extraFuncs.getStatusImgSrc(code);
        int statusImg = getResId(imgSrc, R.drawable.class);
        ImageView card55 = view.findViewById(R.id.statusImg);
        card55.setImageResource(statusImg);

        TextView card6 = view.findViewById(R.id.card6);
        card6.setText(weather.getHumidity() + " %");
        TextView card7 = view.findViewById(R.id.card7);
        card7.setText(weather.getVisibility() + " mi");
        TextView card8 = view.findViewById(R.id.card8);
        card8.setText(weather.getCloudCover() + " %");
        TextView card9 = view.findViewById(R.id.card9);
        card9.setText(weather.getUvIndex());
    }


    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
