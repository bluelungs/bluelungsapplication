package com.example.bluelungs;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
    public class HomeFragment extends Fragment {

        private LineChart lineChart;
        private List<String> xValues;
        private DatabaseReference sensorDataRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {



        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lineChart= view.findViewById(R.id.chart);
        initLineChart();
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        sensorDataRef= database.getReference("UsersData/ypPNi5JlNRVKdSbOBNCgFVjFqVH3/sensor");

        sensorDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    SensorData sensorData = dataSnapshot.getValue(SensorData.class);
                    if (sensorData != null) {
                        Log.d("FirebaseData", "Temperature: " + sensorData.getTemperature() + ", Turbidity: " + sensorData.getTurbidity());
                        updateChart(sensorData.getTemperature(), sensorData.getTurbidity());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error: " + error.getMessage());

            }
        });
        return view;
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void updateChart(float temperature, float turbidity) {
        List<Entry> temperatureEntries= new ArrayList<>();
        List<Entry> turbidityEntries= new ArrayList<>();

        temperatureEntries.add(new Entry(0,temperature));
        turbidityEntries.add(new Entry(0,turbidity));

        LineData lineData=lineChart.getData();
        if (lineData == null) {
            LineDataSet temperatureDataSet = new LineDataSet(temperatureEntries, "Temperature");
            temperatureDataSet.setColor(Color.BLUE);

            LineDataSet turbidityDataSet = new LineDataSet(turbidityEntries, "Turbidity");
            turbidityDataSet.setColor(Color.RED);

            lineData = new LineData(temperatureDataSet, turbidityDataSet);
            lineChart.setData(lineData);
        } else {
            // LineData already exists, update the existing entries
            lineData.getDataSetByIndex(0).getEntriesForXValue(0).add(new Entry(0, temperature));
            lineData.getDataSetByIndex(1).getEntriesForXValue(0).add(new Entry(0, turbidity));

            // Notify the chart that the data has changed
            lineData.notifyDataChanged();
            lineChart.notifyDataSetChanged();
        }
        TextView tempNumberTextView = getView().findViewById(R.id.TempNumber);
        tempNumberTextView.setText(String.valueOf(temperature));
        TextView turbidityNumberTextView = getView().findViewById(R.id.turbudityNumber);
        turbidityNumberTextView.setText(String.valueOf(turbidity));

    }


    private void initLineChart() {
        Description description = new Description();
        description.setText("Sensor Data");
        description.setPosition(150f, 15f);
        lineChart.setDescription(description);
        lineChart.getAxisRight().setDrawLabels(false);

        xValues = Arrays.asList("January", "February", "March", "April");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setLabelCount(4);
        xAxis.setGranularity(1f);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);

        List<Entry> entries1 = new ArrayList<>();
        entries1.add(new Entry(0, 10f));
        entries1.add(new Entry(1, 10f));
        entries1.add(new Entry(2, 15));
        entries1.add(new Entry(3, 45f));

        List<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(0, 5f));
        entries2.add(new Entry(1, 15f));
        entries2.add(new Entry(2, 25f));
        entries2.add(new Entry(3, 30f));

        LineDataSet dataSet1 = new LineDataSet(entries1, "Temperature");
        dataSet1.setColor(Color.BLUE);

        LineDataSet dataSet2 = new LineDataSet(entries2, "Oxygen");
        dataSet2.setColor(Color.RED);

        LineData lineData = new LineData(dataSet1, dataSet2);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

}

