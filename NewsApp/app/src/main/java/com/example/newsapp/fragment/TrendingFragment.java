package com.example.newsapp.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.R;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TrendingFragment extends Fragment {
    View view;
    private EditText searchFilter;
    private LineChart lineChart;
    private String typedWord;

    private ArrayList<Entry> dataset;
    private RequestQueue requestQueue;

    public TrendingFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trending, container, false);
        requestQueue = Volley.newRequestQueue(getContext());   // Instantiate the RequestQueue.
        initView(view);
        return view;
    }

    /**
     * init view for trending fragment
     * @param view
     */
    private void initView(View view) {
        searchFilter = view.findViewById(R.id.searchFilter);
        lineChart = view.findViewById(R.id.lineChart);
        //default situation: parse default value coronavirus, show its lineChart
        parseJSON("CoronaVirus");

        //设置软键盘监听
        searchFilter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE){
                    typedWord = searchFilter.getText().toString();
                    Log.e("Typed Word: ", typedWord);
                    parseJSON(typedWord);
                }
                return false;
            }
        });


    }

    private void parseJSON(final String typedWord) {
        dataset = new ArrayList<Entry>();
        String url = "http://10.0.2.2:3000/googleTrends?keyword=" + typedWord;
        Log.e("typedWord", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.e("response", response.toString());
                try {
                    JSONArray value = response.getJSONArray("value");
                    //Log.e("value",value.toString());
                    for(int i = 0; i < value.length(); ++i){
                        int valueNum = (int)value.get(i);
                        //Log.e("valueNum", String.valueOf(valueNum));
                        dataset.add(new Entry(i, valueNum));
                        //Log.e("single value",value.get(i).toString());
                    }

                    LineDataSet lineDataSet = new LineDataSet(dataset, "Trending Chart for " + typedWord);
                    //set lineChart style
                    initLineChart(lineDataSet);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        requestQueue.add(request);
    }

    private void initLineChart(LineDataSet lineDataSet) {
        // black lines and points
        lineDataSet.setColor(Color.rgb(105,103,159));       //set the legend label color together
        lineDataSet.setCircleColor(Color.rgb(103,58,183));
        lineDataSet.setValueTextColor(Color.rgb(105,103,159));  //set point value color


        //set legend text size
        Legend legend = lineChart.getLegend();
        legend.setTextSize(15f);
        legend.setFormSize(15f);  //set legend icon size


        // draw points as solid circles
        lineDataSet.setDrawCircleHole(false);

        //add LineDataSet
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);


        //set data
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();         // refresh
        Log.e("dataset",dataset.toString());


        //get rid of grid background and left bottom axis
        lineChart.getAxisLeft().setDrawAxisLine(false); // no axis line
        lineChart.getXAxis().setDrawGridLines(false);  //去掉背景的纵轴    这里顺序不对有的地方显示竟然不对

        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);

//      lineChart.getXAxis().setSpaceMax(1.5f);
//      lineChart.getXAxis().setSpaceMin(0.5f);
//        lineChart.setScaleEnabled(true);

    }


}
