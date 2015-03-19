package com.umrecsports;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;
import java.util.Arrays;
import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.*;

public class MenuActivity extends Activity implements OnItemSelectedListener{

    private MobileServiceClient mClient;
	private Spinner gym_spinner;
	private Spinner day_spinner;
	private Button submitBtn;
	
	private String[] gym = {"IMSB", "CCRB", "NCRB"};
	private String[] day = {"Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun"};
	
	private UserTimeSelection page;

    public class CCRBSwipes {
        public String Id;
        public int hour;
        public int monday;
        public int tuesday;
        public int wednesday;
        public int thursday;
        public int friday;
        public int saturday;
        public int sunday;
    }

    private int day_swipes[] = new int[24];
    private XYPlot plot;

    MobileServiceTable<CCRBSwipes> mToDoTable;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        submitBtn = (Button) findViewById(R.id.submitbtn);

        gym_spinner = (Spinner) findViewById(R.id.choosegym_spinner);
        day_spinner = (Spinner) findViewById(R.id.day_spinner);
        
        page = new UserTimeSelection();

        try {
            mClient = new MobileServiceClient(
                    "https://recsports.azure-mobile.net/",
                    "jjsdxtLxuqWnosOuDQMgzpeIBxyuaE84",
                    this
            );
           mToDoTable = mClient.getTable(CCRBSwipes.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        gym_spinner = (Spinner) findViewById(R.id.choosegym_spinner);
        day_spinner = (Spinner) findViewById(R.id.day_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> gym_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_text, gym);
        ArrayAdapter<String> day_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_text, day);

    	// Specify the layout to use when the list of choices appears
        gym_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        
     	// Apply the adapter to the spinner
        gym_spinner.setAdapter(gym_adapter);
        day_spinner.setAdapter(day_adapter);

        
        gym_spinner.setOnItemSelectedListener(this);
        day_spinner.setOnItemSelectedListener(this);


        addListenerOnButton();
        
        String[][] cells = new String[50][50];
        int i = 0;
        int swipes[][] = new int[22][8];
        
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
    		   long id) {
    	 System.out.println("location clicked");
    	
    	 Spinner spinner = (Spinner) parent;
         if(spinner.getId() == R.id.choosegym_spinner)
         {
        	 gym_spinner.setSelection(position);            
         }
         else if(spinner.getId() == R.id.day_spinner)
         {
        	 day_spinner.setSelection(position);
         }
   }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    
    public void addListenerOnButton() {
         
        submitBtn = (Button) findViewById(R.id.submitbtn);
 
        submitBtn.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View v) {

                try {
                    mToDoTable.execute(new TableQueryCallback<CCRBSwipes>() {
                        public void onCompleted(List<CCRBSwipes> result, int count,
                                                Exception exception, ServiceFilterResponse response) {
                            if (exception == null) {
                                int c = 0;
                                for (CCRBSwipes i : result) {
                                    Log.i("SuccessRead", "Read object with ID " + i.Id);
                                    switch(day_spinner.getSelectedItem().toString()){
                                        case "Mon":
                                            day_swipes[c] = i.monday;
                                            break;
                                        case "Tues":
                                            day_swipes[c] = i.tuesday;
                                            break;
                                        case "Wed":
                                            day_swipes[c] = i.wednesday;
                                            break;
                                        case "Thurs":
                                            day_swipes[c] = i.thursday;
                                            break;
                                        case "Fri":
                                            day_swipes[c] = i.friday;
                                            break;
                                        case "Sat":
                                            day_swipes[c] = i.saturday;
                                            break;
                                        case "Sun":
                                            day_swipes[c] = i.sunday;
                                            break;
                                        default:
                                            break;
                                    }
                                    c++;
                                }

                                setContentView(R.layout.fragment_landing);
                                //LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.BLUE, Color.YELLOW, null, null);

                                DataPoint[] series1Numbers = new DataPoint[day_swipes.length/2];
                                int maxSwipes = day_swipes[0];
                                for(int i = 0; i < day_swipes.length/2; i++){
                                    series1Numbers[i] = new DataPoint(5+i, day_swipes[i]);
                                    if(day_swipes[i] > maxSwipes)
                                        maxSwipes = day_swipes[i];
                                }


                                GraphView graph = (GraphView) findViewById(R.id.graph);
                                BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(series1Numbers);

                                GridLabelRenderer labelRenderer = graph.getGridLabelRenderer();
                                labelRenderer.setHorizontalAxisTitle("Hour");
                                labelRenderer.setVerticalAxisTitle("Traffic");
                                labelRenderer.setNumHorizontalLabels(day_swipes.length/2-5+1);

                                Viewport vp = graph.getViewport();
                                vp.setMinX(5);
                                vp.setMaxX(day_swipes.length/2);
                                vp.setXAxisBoundsManual(true);
                                vp.setMinY(0);
                                vp.setMaxY(maxSwipes);
                                vp.setYAxisBoundsManual(true);

                                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                                    @Override
                                    public String formatLabel(double value, boolean isValueX) {
                                        if (isValueX) {
                                            // show normal x values
                                            String tod = "am";
                                            if (value > 12)
                                                tod = "pm";

                                            return super.formatLabel(value % 12, isValueX) + tod;
                                        } else {
                                            // show currency for y values
                                            return super.formatLabel(value, isValueX);
                                        }
                                    }
                                });

                                graph.setTitle("CCRB Swipes");
                                graph.addSeries(series);
                            }else{
                                Log.i("Azure FAIL: ", exception.toString());
                            }
                        }
                    });
                } catch (MobileServiceException e) {
                    e.printStackTrace();
                    Log.i("FailRead", "Read nothing");
                }

                /*
                setContentView(R.layout.fragment_landing);
                //LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.BLUE, Color.YELLOW, null, null);

                DataPoint[] series1Numbers = new DataPoint[day_swipes.length];
                int maxSwipes = day_swipes[0];
                for(int i = 0; i < day_swipes.length/2; i++){
                    series1Numbers[i] = new DataPoint(5+i, day_swipes[i]);
                    if(day_swipes[i] > maxSwipes)
                        maxSwipes = day_swipes[i];
                }


                GraphView graph = (GraphView) findViewById(R.id.graph);
                BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(series1Numbers);

                GridLabelRenderer labelRenderer = graph.getGridLabelRenderer();
                labelRenderer.setHorizontalAxisTitle("Hour");
                labelRenderer.setVerticalAxisTitle("Traffic");
                labelRenderer.setNumHorizontalLabels(3);

                Viewport vp = graph.getViewport();
                vp.setMinX(5);
                vp.setMaxX(14);
                vp.setXAxisBoundsManual(true);
                vp.setMinY(0);
                vp.setMaxY(maxSwipes);
                vp.setYAxisBoundsManual(true);

                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            // show normal x values
                            String tod = "am";
                            if (value > 12)
                                tod = "pm";

                            return super.formatLabel(value % 12, isValueX) + tod;
                        } else {
                            // show currency for y values
                            return super.formatLabel(value, isValueX);
                        }
                    }
                });

                graph.setTitle("CCRB Swipes");
                graph.addSeries(series);

                */
            }
 
        });
 
    }
    

}
