package com.yahiee.gsmmode;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphActivity extends AppCompatActivity {

    private GraphView graph;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        graph = findViewById(R.id.graph);

        // Example data: percentages of water consumed over the week
        double[] percentages = {20, 30, 10, 50, 40, 70, 60};

        // Create DataPoint array
        DataPoint[] dataPoints = new DataPoint[percentages.length];
        for (int i = 0; i < percentages.length; i++) {
            dataPoints[i] = new DataPoint(i + 1, percentages[i]);
        }

        // Create series and set data
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

        // Customize the graph
        series.setTitle("Water Consumption");
        series.setColor(Color.BLUE);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);

        graph.addSeries(series);

        // Customize the viewport
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(7);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        // Set labels for X and Y axes
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Day of the Week");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Water Consumption (%)");
    }
}
