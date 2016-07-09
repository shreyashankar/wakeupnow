package com.shreya_shankar.wakeupnow;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import java.lang.Math;
import android.widget.Toast;
import com.shreya_shankar.wakeupnow.R;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.StrictMath.sin;

public class MainActivity extends Activity {

    Context context;
    TextView txtview;
    double RADIUS = 6378100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtview=(TextView)findViewById(R.id.info_message);
        txtview.setText(getString(R.string.info));

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        context = getApplicationContext();

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            private Location lastLocation;
            public void onLocationChanged(Location location) {
                double speed = 1.0;
                if (this.lastLocation != null) {
                    double dlon = location.getLongitude() - lastLocation.getLongitude();
                    double dlat = location.getLatitude() - lastLocation.getLatitude();
                    double a = (sin(dlat/2))*(sin(dlat/2)) + cos(lastLocation.getLatitude()) * cos(location.getLatitude()) * (sin(dlon/2))*(sin(dlon/2));
                    double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a) );
                    double distance = RADIUS * c;
                    speed = (distance/(location.getTime() - this.lastLocation.getTime()));
                }

                if (location.hasSpeed()) { //never happens lol
                    speed = location.getSpeed();
                    txtview.setText("Current speed: " + location.getSpeed() + " m/s");
                } else {
                    txtview.setText(speed + " m/s");
                }
                this.lastLocation = location;
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            public void onProviderEnabled(String provider) {

            }
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, locationListener);

    }

}
