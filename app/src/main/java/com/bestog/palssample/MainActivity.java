package com.bestog.palssample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bestog.pals.Pals;
import com.bestog.pals.interfaces.IRequest;
import com.bestog.pals.interfaces.ISubmit;
import com.bestog.pals.provider.LocationProvider;
import com.bestog.pals.utils.GeoResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Pals pals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init Pals
        pals = new Pals(this);

        // Location-Provider with your own access-token
        pals.enableProvider(LocationProvider.PROVIDER_MOZILLA, "[YOUR ACCESS-TOKEN]");

        // Location-Provider with a fallback access-token
        // pals.enableProvider(LocationProvider.PROVIDER_MOZILLA, null);

        // Other Location-Provider
        pals.enableProvider(LocationProvider.PROVIDER_OPENBMAP, null);
        pals.enableProvider(LocationProvider.PROVIDER_OPENMAP, null);
        // pals.enableProvider(LocationProvider.PROVIDER_GOOGLE, null);
        // pals.enableProvider(LocationProvider.PROVIDER_OPENCELLID, null);
        process();
    }

    private void process() {
        final TextView resultat = (TextView) findViewById(R.id.result);
        if (resultat != null) {
            resultat.setText("");
        }
        pals.request(new IRequest() {
            @Override
            public void onComplete(GeoResult result, boolean valid) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss", Locale.GERMAN);
                String out = String.format("Lat: %1$s\nLon: %2$s\nAcc: %3$s\n\nTime: %4$s", result.getLatitude(), result.getLongitude(), result.getAccuracy(), sdf.format(new Date()));
                if (resultat != null) {
                    resultat.setText(out);
                }
            }
        });
        final TextView submit = (TextView) findViewById(R.id.submit);
        pals.submit(new ISubmit() {
            @Override
            public void onComplete(boolean valid) {
                if (submit != null) {
                    submit.setText((valid) ? "Submit success" : "Submit error");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                process();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
