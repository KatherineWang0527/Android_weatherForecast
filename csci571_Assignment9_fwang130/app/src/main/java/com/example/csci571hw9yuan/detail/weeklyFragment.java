package com.example.csci571hw9yuan.detail;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.csci571hw9yuan.R;
import com.example.csci571hw9yuan.dao.TempRange;
import com.example.csci571hw9yuan.dao.Values;
import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.common.HIGradient;
import com.highsoft.highcharts.common.HIStop;
import com.highsoft.highcharts.common.hichartsclasses.HIArearange;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HILegend;
import com.highsoft.highcharts.common.hichartsclasses.HIMarker;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HISeries;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.highsoft.highcharts.core.HIChartView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

public class weeklyFragment extends Fragment {
    int position;
    private TextView textView;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        weeklyFragment tabFragment = new weeklyFragment();
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
        return inflater.inflate(R.layout.fragment_weekly, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        threeCharts charts = (threeCharts)getActivity();
        assert charts != null;
        Values weather = charts.getValues();
        super.onViewCreated(view, savedInstanceState);

        TempRange[] tempRange = charts.tempRange;
        HIChartView chartView = view.findViewById(R.id.highChartsWeekly);

        HIOptions options = new HIOptions();

        HIChart chart = new HIChart();
        chart.setType("arearange");
        chart.setZoomType("x");
        options.setChart(chart);

        HITitle title = new HITitle();
        title.setText("Temperature variation by day");
        options.setTitle(title);

        HIXAxis xaxis = new HIXAxis();
        xaxis.setType("datetime");
        options.setXAxis(new ArrayList<HIXAxis>(){{add(xaxis);}});

        HIYAxis yaxis = new HIYAxis();
        yaxis.setTitle(new HITitle());
        options.setYAxis(new ArrayList<HIYAxis>(){{add(yaxis);}});

        HITooltip tooltip = new HITooltip();
        tooltip.setShadow(true);
        tooltip.setValueSuffix("°F");
        options.setTooltip(tooltip);

        HILegend legend = new HILegend();
        legend.setEnabled(false);
        options.setLegend(legend);

        HIArearange series = new HIArearange();
        series.setName("Temperatures");
        Object[][] seriesData = new Object[tempRange.length][3];

        HIGradient gradient = new HIGradient(0, 0, 0, 1);
        LinkedList<HIStop> stops = new LinkedList<>();
        stops.add(new HIStop(0, HIColor.initWithRGB(244, 180, 0)));
        stops.add(new HIStop(1, HIColor.initWithRGB(153, 193, 227)));

        series.setFillColor(HIColor.initWithLinearGradient(gradient, stops));

        HIPlotOptions plot = new HIPlotOptions();
        HISeries plotSeries = new HISeries();
        plotSeries.setColor(HIColor.initWithRGBA(244,180,0,0.35));
        HIMarker hiMarker = new HIMarker();
        hiMarker.setFillColor(HIColor.initWithRGBA(66,133,244, 0.35));
        hiMarker.setFillOpacity(0.1);
        plotSeries.setMarker(hiMarker);
        plot.setSeries(plotSeries);
        options.setPlotOptions(plot);

        for(int index = 0;index < tempRange.length;index++) {
            TempRange t = tempRange[index];
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
            try {
                date = formatter.parse(t.getStartTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            assert date != null;
            seriesData[index] = new Object[]{date.getTime(), Float.parseFloat(t.getMinTemp()), Float.parseFloat(t.getMaxTemp())};
        }

        series.setData(new ArrayList<>(Arrays.asList(seriesData)));
        options.setSeries(new ArrayList<>(Arrays.asList(series)));

        chartView.setOptions(options);

    }
}
