package com.gmail.sstr224a.transience;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {
    private double counter;
    private double happyCounter;
    private double sadCounter;
    private LineGraphSeries<DataPoint> posLineGraph;
    private Viewport viewport;
    private GraphView graphView;
    private ArrayList<Integer> Ypoint;
    private ArrayList<Integer> Xpoint;
    private Listener listener;
    private long startTime;
    private SimpleDateFormat dateFormat;


    public GraphFragment() {
        // Required empty public constructor
    }

    interface Listener {
        void updateProgressBar(long elapsedTime, double counter, double emotionCounter, boolean emotion);
    }



    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);


        graphView = view.findViewById(R.id.graphview);
        posLineGraph = new LineGraphSeries<>();
        init();

        //display tutorial about the graph the first time the app is opened
        if (TutorialClass.isFirst(getActivity())) {
            TapTargetView.showFor(getActivity(),
                    TapTarget.forView(view.findViewById(R.id.linearLayout4),
                                "It's time to practice! Set aside some time everyday to think about things that make you happy! " +
                                    "While you're at it, try to simply observe the emotions passing by in your brain continuously and press the happy or sad button. " +
                                    "Let the negative emotions simply pass by and focus on your positive thoughts. Try it!")
                            .outerCircleColor(R.color.blue)      // Specify a color for the outer circle
                            .outerCircleAlpha(0.96f)// Specify the alpha amount for the outer circle
                            .targetRadius(95)
                            .textColor(R.color.black)
                            .transparentTarget(true)
                            .targetCircleColor(R.color.dark_grey));
        }


        Ypoint = new ArrayList<>();
        Xpoint = new ArrayList<>();

        dateFormat = new SimpleDateFormat("mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        //restore the linegraph  and all counters on rotation
        if (savedInstanceState != null) {
            double[] counters = savedInstanceState.getDoubleArray("counters");
            this.counter = counters[1];
            this.happyCounter = counters[0];
            this.sadCounter = counters[2];

            Xpoint = savedInstanceState.getIntegerArrayList("Xpoints");
            Ypoint = savedInstanceState.getIntegerArrayList("Ypoints");

            for (int i = 0; i < Xpoint.size(); i++) {
                int x = Xpoint.get(i);
                double y;
                if (Ypoint.get(i) == 1) {
                    y = 1.8;
                } else {
                    y = 0.5;
                }
                posLineGraph.appendData(new DataPoint(x, y), true, Xpoint.size());
                graphView.addSeries(posLineGraph);
                viewport.setXAxisBoundsManual(true);
                viewport.setMinX(0.0);
                viewport.setMaxX(Xpoint.size());

            }
        }

        final ImageButton negativeButton = (ImageButton) view.findViewById(R.id.negative);
        final ImageButton positiveButton = (ImageButton) view.findViewById(R.id.button);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a larger vector asset if the device is a tablet
                if (getResources().getBoolean(R.bool.islarge)) {
                    negativeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_sentiment_dissatisfied_black_24dp_large));
                    positiveButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_sentiment_very_satisfied_black_24dp_large));
                } else {
                    negativeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_sentiment_dissatisfied_black_24dp));
                    positiveButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_sentiment_very_satisfied_black_24dp));
                }
                updateGraph(true);
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a larger vector asset if the device is a tablet
                if (getResources().getBoolean(R.bool.islarge)) {
                    positiveButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_mood_black_24dp_large));
                    negativeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_sentiment_very_dissatisfied_black_24dp_large));
                } else {
                    positiveButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_mood_black_24dp));
                    negativeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_sentiment_very_dissatisfied_black_24dp));
                }
                updateGraph(false);

            }
        });

        return view;

    }

    //calculate duration between clicks, update counters and append data to the linegraph
    private void updateGraph(boolean emotion) {
        counter++;
        Xpoint.add((int) counter);

        if (emotion) {
            startTime = System.currentTimeMillis();
            happyCounter++;
            Ypoint.add(1);
            if (listener != null) {
                listener.updateProgressBar(0, counter, happyCounter, true);
            }
            posLineGraph.appendData(new DataPoint(counter, 1.8), true, (int) counter);

            viewport.setXAxisBoundsManual(true);
            viewport.setMinX(0.0);
            viewport.setMaxX(counter);

        } else {
            long elapsedTime = System.currentTimeMillis() - startTime;
            sadCounter++;
            Ypoint.add(0);
            if (listener != null) {
                listener.updateProgressBar(elapsedTime, counter, sadCounter, false);
            }
            posLineGraph.appendData(new DataPoint(counter, 0.5), true, (int) counter);

        }
        graphView.addSeries(posLineGraph);

    }

    //initialize the linegraph
    private void init() {
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphView.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphView.getGridLabelRenderer().setPadding(0);


        posLineGraph.setColor(Color.parseColor("#FF8A65"));
        posLineGraph.setDrawDataPoints(true);
        posLineGraph.setDataPointsRadius(8);
        posLineGraph.setThickness(5);

        viewport = graphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0.0);
        viewport.setMaxY(2.0);
        viewport.setScrollable(true);
        viewport.setScalable(true);
        viewport.setScalableY(false);
        viewport.setBackgroundColor(Color.TRANSPARENT);

    }

    // save counters and data points on rotation
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        double[] counters = {happyCounter, counter, sadCounter};
        savedInstanceState.putDoubleArray("counters", counters);
        savedInstanceState.putIntegerArrayList("Xpoints", Xpoint);
        savedInstanceState.putIntegerArrayList("Ypoints", Ypoint);

    }


}
