package fvtc.edu.teams;


import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class TeamMapActivity extends AppCompatActivity {
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    public static final String TAG = " TeamMapActivity";
    final int PERMISSION_REQUEST_LOCATION = 101;

    SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    TextView txtHeading;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_team_map);
        Navbar.initListButton(this);
        Navbar.initMapButton(this);
        Navbar.initSettingsButton(this);

        initGetLocationButton();
        Log.d(TAG, "onCreate: after xml and navbar");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        createLocationCallback();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        Log.d(TAG, "onCreate: 1");
        
        if(accelerometer != null && magnetometer != null){
            Log.d(TAG, "onCreate: 2");
            //sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
            Log.d(TAG, "onCreate: 3");
            //sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
            Log.d(TAG, "onCreate: 4");
        }
        else Log.d(TAG, "onCreate: null sensors");

        startLoactionUpdates();
        
        txtHeading = findViewById(R.id.txtHeading);
        this.setTitle(getString(R.string.team_map));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d(TAG, "onCreate: end");
    }

    private void initGetLocationButton() {
        Button btnGetLcoation = findViewById(R.id.btnGetLocation);
        btnGetLcoation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geocoder geocoder = new Geocoder(TeamMapActivity.this);
                List<Address> addresses = null;
                EditText etAddress = findViewById(R.id.etAddress);
                EditText etCity = findViewById(R.id.etCity);
                EditText etState = findViewById(R.id.etState);
                EditText etZip = findViewById(R.id.etZipcode);
                String address = etAddress.getText() + ", " +
                                 etCity.getText() + ", " +
                                 etState.getText() + ", " +
                                 etZip.getText();
                try {
                    addresses = geocoder.getFromLocationName(address,1);
                    TextView textLatitude = findViewById(R.id.txtLatitude);
                    TextView textLongitude = findViewById(R.id.txtLongitude);
                    textLatitude.setText(String.valueOf(addresses.get(0).getLatitude()));
                    textLongitude.setText(String.valueOf(addresses.get(0).getLongitude()));
                    Log.d(TAG, "onClick: " + addresses.get(0).getLatitude()+ " " + addresses.get(0).getLongitude());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(locationResult == null){
                    return;
                }
                for(Location location : locationResult.getLocations()){
                    Log.d(TAG, "onLocationResult: Lat: " + location.getLatitude() +
                            "Lon: " + location.getLongitude() + "Accuracy: " + location.getAccuracy());

                    TextView txtLatitude = findViewById(R.id.txtLatitude);
                    TextView txtLongitude = findViewById(R.id.txtLongitude);
                    txtLatitude.setText(String.valueOf(location.getLatitude()));
                    txtLongitude.setText(String.valueOf(location.getLongitude()));

                }
            }
        };
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLoactionUpdates(){
        if(Build.VERSION.SDK_INT >= 23){
            if(ContextCompat.checkSelfPermission(TeamMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(TeamMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
                    Snackbar.make(findViewById(R.id.activity_team_edit), "Teams requires this permission to find your location.",
                            Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "onClick: snackBar");
                            ActivityCompat.requestPermissions(TeamMapActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                        }
                    }).show();
                } else {
                    Log.d(TAG, "check Location Request Permission: 1");
                    ActivityCompat.requestPermissions(TeamMapActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                }
            }else {
                Log.d(TAG, "check Location Request Permission: 2");
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        float[] accelerometerValues;
        float[] magnetometerValues;
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d(TAG, "onSensorChanged: ");
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) accelerometerValues = event.values;
            if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) magnetometerValues = event.values;
            if(accelerometerValues != null && magnetometerValues != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R,I,accelerometerValues,magnetometerValues);
                if(success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    float azimuth = (float) Math.toDegrees(orientation[0]);
                    if(azimuth< 0.0f) azimuth += 360;
                    String direction;
                    Log.d(TAG, "onSensorChanged: " + azimuth);
                    if(azimuth >= 315 || azimuth < 45) direction = "N";
                    else if(azimuth >= 225 && azimuth < 315) direction = "W";
                    else if(azimuth >= 135 && azimuth < 225) direction = "S";
                    else direction = "E";
                    Log.d(TAG, "onSensorChanged: " + direction);
                    txtHeading.setText(direction);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        stopLocationUpdates();
    }
}