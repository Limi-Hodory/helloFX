package anomalyDetection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TimeSeries 
{
	
    private LinkedHashMap<String, Integer> features;
    private List<List<Float>> values;
    private long timeSteps;

    public TimeSeries(String csvFileName) {
        loadCSV(csvFileName);
    }

    public TimeSeries() {
        features = new LinkedHashMap<String, Integer>();
        values = new ArrayList<List<Float>>();
        timeSteps = 0;
    }

    public long getTimeSteps() {
        return timeSteps;
    }

    public void loadCSV(String csvFileName) {
        timeSteps = 0;
        features = new LinkedHashMap<String, Integer>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(csvFileName));
            String line = in.readLine();

            if (line == null) {
                in.close();
                return;
            }

            String[] names = line.split(",");
            for (int i = 0; i < names.length; i++) {
                if (features.containsKey(names[i])) {
                    features.put(names[i] + "1", i);
                } else {
                    features.put(names[i], i);
                }

            }

            values = new ArrayList<List<Float>>(names.length);
            for (int i = 0; i < names.length; i++) {
                List<Float> arr = new ArrayList<Float>();
                values.add(i, arr);
            }
            int currentRow = 0;
            while ((line = in.readLine()) != null) {
                currentRow++;
                addRow(currentRow, line);
            }
            in.close();
        } catch (IOException e) {
        }
    }

    public void addRow(int currentRow, String row) {
        String[] valsStr = row.split(",");
        float[] vals = new float[valsStr.length];
        for (int i = 0; i < vals.length; i++)
            vals[i] = Float.parseFloat(valsStr[i]);
        addRow(vals);
    }

    public void addRow(float[] rowVals) {
        if (rowVals.length != values.size())
            return;
        for (int i = 0; i < rowVals.length; i++)
            values.get(i).add(rowVals[i]);
        timeSteps++;
    }

    public String[] getFeatures() {
        String[] toReturn = new String[features.size()];
        features.keySet().toArray(toReturn);
        return toReturn;
    }

    public float[] valuesOf(String featureName) {
        if (!features.containsKey(featureName))
            return null;

        float[] arr = new float[(int) timeSteps];
        List<Float> vals = values.get(features.get(featureName));
        for (int i = 0; i < timeSteps; i++)
            arr[i] = vals.get(i);
        return arr;
    }

    public Map<String, Float> row(int r) {
        if (r >= timeSteps)
            return null;

        Map<String, Float> m = new LinkedHashMap<String, Float>();
        for (String s : features.keySet())
            m.put(s, values.get(features.get(s)).get(r));
        return m;
    }


    public TimeSeries subSeries(List<String> featureNames) {
        TimeSeries ts = new TimeSeries();

        int i = 0;
        for (String s : featureNames) {
            if (features.containsKey(s) && !ts.features.containsKey(s)) {
                ts.features.put(s, i);
                ts.values.add(new ArrayList<Float>(values.get(features.get(s))));
                i++;
            }
        }

        ts.timeSteps = timeSteps;

        return ts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : features.keySet())
            sb.append(s);
        sb.append(System.lineSeparator());
        for (int i = 0; i < timeSteps; i++) {
            for (String s : features.keySet())
                sb.append(values.get(features.get(s)).get(i).toString()).append(", ");
            sb.delete(sb.length() - 2, sb.length());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

}
