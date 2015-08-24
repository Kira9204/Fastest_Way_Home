package se.erikwelander.fastestwayhome;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import se.erikwelander.fastestwayhome.fragment.TransportOptionFragment;
import se.erikwelander.fastestwayhome.service.LocationService;


public class HomeActivity extends ActionBarActivity
{
    private final int REFRESH_RATE = 1000 * 60 * 2;
    private Handler handler = new Handler();
    LocationService locationService;
    TransportOptionFragment fragmentUpper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        locationService = new LocationService(this);

        fragmentUpper = (TransportOptionFragment) getFragmentManager().findFragmentById(R.id.home_fragment_upper);
        updateThread.run();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final Runnable updateThread = new Runnable()
    {
        public void run()
        {
            if(locationService.canGetLocation())
            {
                fragmentUpper.update(locationService.getLocation());
                TextView numDepartures = (TextView) findViewById(R.id.home_text_top);
            }
            handler.postDelayed(this,REFRESH_RATE);
        }

    };

}
