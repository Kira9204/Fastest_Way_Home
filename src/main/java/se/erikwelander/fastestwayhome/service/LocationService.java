package se.erikwelander.fastestwayhome.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class LocationService implements LocationListener
{
    private Context context;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;

    private Location location;
    private double latitude,
                   longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 50;
    private static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    public LocationService(Context context)
    {
        this.context = context;
        getLocation();
    }

    public Location getLocation()
    {
        try {
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // no network provider is enabled
            if (!isGPSEnabled && !isNetworkEnabled)
            {
                showSettingsAlert();
            }
            else
            {
                // First get location from Network Provider
                if (isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled)
                {
                    if (location == null)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e)
        {
            Log.e("LocationService", "Could not parse departures from JSON!", e);
        }

        return location;
    }

    public void stopUsingGPS()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(LocationService.this);
        }
    }

    public double getLatitude(){
        if(location != null)
        {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if(location != null)
        {
            longitude = location.getLongitude();
        }
        return longitude;
    }
    public boolean canGetLocation()
    {
        if(this.isNetworkEnabled || this.isGPSEnabled)
        {
            return true;
        }
        return false;
    }

    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Platsinställningar");

        // Setting Dialog Message
        alertDialog.setMessage("Du har inga platstjänster aktiverade, vill du gå till inställningar och aktivera dessa?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location)
    {
    }
    @Override
    public void onProviderDisabled(String provider)
    {
    }
    @Override
    public void onProviderEnabled(String provider)
    {
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }
}
