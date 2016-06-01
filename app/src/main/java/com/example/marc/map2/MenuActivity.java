package com.example.marc.map2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.EOFException;
import java.io.IOException;

public class MenuActivity extends AppCompatActivity {

    TextView geoloc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Localisation
        Location myLocation;

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        /* JSON test */
        /*
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("localhost/json_example.json");
        HttpResponse httpResponse = null;
        try
        {
            httpResponse = httpClient.execute(httpGet);
            String response = EntityUtils.toString(httpResponse.getEntity());

            Gson gson = new Gson();
            Contact myClassObj = gson.fromJson(response, Contact.class);
            Toast.makeText(getBaseContext(), "src: " + myClassObj.titi + " / dst: " + myClassObj.toto, Toast.LENGTH_LONG).show();


        } catch (IOException e) {
            e.printStackTrace();
        }
        */


        final Switch switchMoyenDeTransport = (Switch)findViewById(R.id.switchTransport);
        final Switch switchDisponibilite = (Switch)findViewById(R.id.switchDisponibilite);

        final Intent intentMaps = new Intent(MenuActivity.this,MapsActivity.class);

        final Button buttonGo = (Button)findViewById(R.id.buttonGo);
        //Checker les switchs lorsqu'on appui sur Go !
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringAdressPosition = "";
                String stringAdressDestination = "";

                if (switchMoyenDeTransport.isChecked()==true) {
                    Log.i("MenuActivity","true");
                }


                if (switchMoyenDeTransport.isChecked()==false) {
                    Log.i("MenuActivity","false");
                }



                if (switchDisponibilite.isChecked()==true) {
                    Log.i("MenuActivity","true");
                }


                if (switchDisponibilite.isChecked()==false) {
                    Log.i("MenuActivity","false");
                }


                EditText editTextAdressPosition = (EditText)findViewById(R.id.editTextPosition);
                stringAdressPosition = String.valueOf(editTextAdressPosition.getText());

                EditText editTextAdressDestination = (EditText)findViewById(R.id.editTextDestination);
                stringAdressDestination = String.valueOf(editTextAdressDestination.getText());

                intentMaps.putExtra("Demande", "Trajet");
                intentMaps.putExtra("stringAdressPosition",stringAdressPosition);
                intentMaps.putExtra("stringAdressDestination",stringAdressDestination);
                startActivity(intentMaps);
            }
        });


        final Button buttonLocaliser = (Button)findViewById(R.id.buttonLocaliser);
        //Checker les switchs lorsqu'on appui sur Localiser !
        buttonLocaliser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringAdressPosition = "";
                String stringAdressDestination = "";


                if (switchMoyenDeTransport.isChecked()==true) {
                    Log.i("MenuActivity","true");
                }


                if (switchMoyenDeTransport.isChecked()==false) {
                    Log.i("MenuActivity","false");
                }



                if (switchDisponibilite.isChecked()==true) {
                    Log.i("MenuActivity","true");
                }


                if (switchDisponibilite.isChecked()==false) {
                    Log.i("MenuActivity","false");
                }


                EditText editTextAdressPosition = (EditText)findViewById(R.id.editTextPosition);
                stringAdressPosition = String.valueOf(editTextAdressPosition.getText());

                intentMaps.putExtra("Demande","Location");
                intentMaps.putExtra("stringAdressPosition",stringAdressPosition);
                intentMaps.putExtra("stringAdressDestination",stringAdressDestination);
                startActivity(intentMaps);
            }
        });

    }
}
