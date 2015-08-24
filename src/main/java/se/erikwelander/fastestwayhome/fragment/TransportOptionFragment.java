package se.erikwelander.fastestwayhome.fragment;

import android.app.ListFragment;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import se.erikwelander.fastestwayhome.R;
import se.erikwelander.fastestwayhome.adapter.TransportOptionAdpater;
import se.erikwelander.fastestwayhome.model.StationDeparture;
import se.erikwelander.fastestwayhome.model.TransportOptionRow;

public class TransportOptionFragment extends ListFragment
{
    private final String API_URL_STATIONS_IN_ZONE = "https://api.trafiklab.se/samtrafiken/resrobot/StationsInZone.json";
    private final String API_KEY_STATIONS_IN_ZONE = "JVmQZLfAIZDdbTVGINhmuUEyieYvArXq";
    private final String API_ARGS_STATION_IN_ZONE = "?apiVersion=2.1&radius=1000&coordSys=WGS84&key="+API_KEY_STATIONS_IN_ZONE;

    private final String API_URL_DEPARTURES = "https://api.trafiklab.se/samtrafiken/resrobotstops/GetDepartures.json";
    private final String API_KEY_DEPARTURES = "2ILGr4gJmosi46HNALdJXST60fl1Irxj";
    private final String API_ARGS_DEPARTURES = "?apiVersion=2.2&coordSys=WGS84&timeSpan=120&key="+API_KEY_DEPARTURES;

    private ListView listView;
    private ArrayList<TransportOptionRow> items = new ArrayList<>();

    private ArrayList<StationDeparture> stationDepartures = new ArrayList<>();
    private ArrayList<Long> stationIDs = new ArrayList<>();

    private TransportOptionAdpater adapter;

    Resources resources;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.list_fragment, container, false);
        listView = (ListView) rootView.findViewById(android.R.id.list);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        resources = getResources();

        items.add(new TransportOptionRow(resources.getDrawable(R.mipmap.icon_loading_data), "Laddar...", "Söker efter närliggande avgångar...", "?"));
        adapter = new TransportOptionAdpater(getActivity(), android.R.id.list, items);
        listView.setAdapter(adapter);

    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        TransportOptionRow item = items.get(position);

        // do something
        Toast.makeText(getActivity(), item.title, Toast.LENGTH_SHORT).show();
    }

    public void update(Location location)
    {
        stationIDs = new ArrayList<>();
        stationDepartures = new ArrayList<>();
        items = new ArrayList<>();
        try
        {
            populateStationProximity populateStationProximity = new populateStationProximity();
            populateStationProximity.execute(API_URL_STATIONS_IN_ZONE+API_ARGS_STATION_IN_ZONE+"&centerX="+location.getLongitude()+"&centerY="+location.getLatitude());
            populateStationProximity.get();

            populateStationDepartures populateStationDepartures = new populateStationDepartures();

            for(int i = 0; i < stationIDs.size(); i++)
            {
                populateStationDepartures.execute(API_URL_DEPARTURES+API_ARGS_DEPARTURES+"&locationId="+stationIDs.get(i));
            }


        }
        catch (Exception e){}
    }

    private class populateStationProximity extends AsyncTask<String, String, Void>
    {
        final private String LOG_TAG = "STATION_PROX";
        private String result;

        protected void onPreExecute()
        {
        }

        @Override
        protected Void doInBackground(String... params)
        {
            BufferedReader reader = null;
            StringBuilder data = new StringBuilder();
            try
            {
                URL url = new URL(params[0]);
                Log.e(LOG_TAG, params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null)
                {
                    data.append(line);
                }
                result = data.toString();
            }
            catch(Exception e)
            {
                Log.e(LOG_TAG, "Could not start download", e);
            }
            finally
            {
                if(reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch (Exception e)
                    {
                        Log.e(LOG_TAG, "Could not close reader", e);
                    }
                }
            }

            boolean isArray = false;
            try
            {
                JSONObject jObject = new JSONObject(result);
                jObject = jObject.getJSONObject("stationsinzoneresult");

                JSONArray arr = jObject.getJSONArray("location");
                isArray = true;
                for (int i = 0; i < arr.length(); i++)
                {
                    stationIDs.add(arr.getJSONObject(i).getLong("@id"));
                }

            } catch (Exception e) {
                Log.e(LOG_TAG, "Could not parse Statations array in zone JSON data!", e);
            }
            if(!isArray)
            {
                try
                {
                    JSONObject jObject = new JSONObject(result);
                    jObject = jObject.getJSONObject("stationsinzoneresult");

                    jObject = jObject.getJSONObject("location");
                    stationIDs.add(jObject.getLong("@id"));

                } catch (Exception e) {
                    Log.e(LOG_TAG, "Could not parse Statations in zone JSON data!", e);
                }
            }
            return null;
        }
        protected void onPostExecute(Void v)
        {
        }

    }

    private class populateStationDepartures extends AsyncTask<String, String, Void>
    {
        String result = "";
        final private String LOG_TAG = "TOP_FRAG";

        protected void onPreExecute()
        {
        }

        @Override
        protected Void doInBackground(String... params)
        {
            BufferedReader reader = null;
            StringBuilder data = new StringBuilder();
            try
            {
                URL url = new URL(params[0]);
                Log.e(LOG_TAG, params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null)
                {
                    data.append(line);
                }
                result = data.toString();
            }
            catch(Exception e)
            {
                Log.e(LOG_TAG, "Could not start download", e);
            }
            finally
            {
                if(reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch (Exception e)
                    {
                        Log.e(LOG_TAG, "Could not close reader", e);
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(Void v)
        {
            boolean isArray = false;
            try
            {
                JSONObject jsonParse = new JSONObject(result);
                jsonParse = jsonParse.getJSONObject("getdeparturesresult");

                JSONArray jsonArray = jsonParse.getJSONArray("departuresegment");
                isArray = true;
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject depatureSegment = jsonArray.getJSONObject(i);
                    JSONObject departure = depatureSegment.getJSONObject("departure");
                    JSONObject locationObject = departure.getJSONObject("location");

                    long id = locationObject.getLong("@id");
                    double x = locationObject.getDouble("@x");
                    double y = locationObject.getDouble("@y");
                    String name = locationObject.getString("name");

                    String leaving = departure.getString("datetime");
                    String direction = depatureSegment.getString("direction");

                    try
                    {
                        stationDepartures.add(new StationDeparture(id, x, y, name, direction, leaving));
                    }
                    catch (ParseException e)
                    {
                        try
                        {
                            stationDepartures.add(new StationDeparture(0, 0, 0, "Det finns inga avgångar i närheten!", "Har du platstjänsterna på?", ""));
                        }
                        catch (ParseException e2)
                        {
                            Log.e(LOG_TAG, "Could not parse departure date!", e2);
                        }
                        Log.e(LOG_TAG, "Could not parse departure date!", e);
                    }
                }
            }
            catch(JSONException e)
            {
                Log.e(LOG_TAG, "Could not parse departures from JSON!", e);
            }
            if(!isArray) {
                try {
                    JSONObject jsonParse = new JSONObject(result);
                    jsonParse = jsonParse.getJSONObject("getdeparturesresult");
                    JSONObject depatureSegment = jsonParse.getJSONObject("departuresegment");
                    JSONObject departure = depatureSegment.getJSONObject("departure");
                    JSONObject locationObject = departure.getJSONObject("location");

                    long id = locationObject.getLong("@id");
                    double x = locationObject.getDouble("@x");
                    double y = locationObject.getDouble("@y");
                    String name = locationObject.getString("name");

                    String leaving = departure.getString("datetime");
                    String direction = depatureSegment.getString("direction");

                    try
                    {
                        stationDepartures.add(new StationDeparture(id, x, y, name, direction, leaving));
                    } catch (ParseException e)
                    {
                        Log.e(LOG_TAG, "Could not parse departure date!", e);
                    }
                }
                catch (JSONException e)
                {
                    Log.e(LOG_TAG, "Could not parse departures from JSON!", e);
                    try
                    {
                        stationDepartures.add(new StationDeparture(0, 0, 0, "Det finns inga avgångar i närheten!", "Har du platstjänsterna på?", ""));
                    }
                    catch (ParseException e2)
                    {
                        Log.e(LOG_TAG, "Could not parse departure date!", e2);
                    }
                }
            }

            items = new ArrayList<>();
            Date currentTime = new Date();
            for(int i = 0; i < stationDepartures.size(); i++)
            {

                StationDeparture stationDeparture = stationDepartures.get(i);

                long timediff = (long) (stationDeparture.leaving.getTime() - currentTime.getTime());
                long diffHours = timediff / (60 * 60 * 1000) % 24;
                long diffMinutes = timediff / (60 * 1000) % 60;

                items.add(new TransportOptionRow(resources.getDrawable(R.mipmap.icon_transportation_bus), stationDeparture.name, "Mot: "+stationDeparture.direction, "Om:\n"+diffHours+" tim\n"+diffMinutes+" min"));
            }
            adapter = new TransportOptionAdpater(getActivity(), android.R.id.list, items);
            listView.setAdapter(adapter);
        }
    }
}

