package tr.org.akut.guvendeyim;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import tr.org.akut.guvendeyim.R;

public class MainActivity extends AppCompatActivity {

    Button send_btn;
    ImageView imgView;
    TextView textView;
    Boolean imgbool = false;
    String loc = "", googleLoc = "", sendMessage = "";
    String phonenumber1 = "",phonenumber2 = "",phonenumber3 = "",phonenumber4 = "",phonenumber5 = "";
    String getMessage = "",getterMsg="";
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    LocationManager locationManager;
    LocationListener locationListener;
    private final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.txt);
        imgView = findViewById(R.id.imageView);
        //Intent intent = getIntent();

        SharedPreferences result = getSharedPreferences("SaveData",Context.MODE_PRIVATE);
        getterMsg = result.getString("msg","Data Not Found!");
        phonenumber1 = result.getString("phone1","Data Not Found!");
        phonenumber2 = result.getString("phone2","Data Not Found!");
        phonenumber3 = result.getString("phone3","Data Not Found!");
        phonenumber4 = result.getString("phone4","Data Not Found!");
        phonenumber5 = result.getString("phone5","Data Not Found!");
        if (getterMsg.equalsIgnoreCase("Acil Durum!")){
            imgView.setImageResource(R.drawable.redcircle);
        }
        if (getterMsg.equalsIgnoreCase("Ben iyiyim")){
            imgView.setImageResource(R.drawable.greencircle);
        }

        //Toast.makeText(this,"Message: "+getterMsg,Toast.LENGTH_SHORT).show();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                loc = location.getLatitude() + "," + location.getLongitude();
                //Toast.makeText(getApplicationContext(),location.toString(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
        }

        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                /*phonenumber1 = intent.getStringExtra("telefon1");
                phonenumber2 = intent.getStringExtra("telefon2");
                phonenumber3 = intent.getStringExtra("telefon3");
                phonenumber4 = intent.getStringExtra("telefon4");
                phonenumber5 = intent.getStringExtra("telefon5");
                getMessage = intent.getStringExtra("message");*/

                googleLoc = "Konumum: " + "https://www.google.com/maps/place/" + loc;
                sendMessage = getterMsg + "\n" + googleLoc;
                if (loc != "" && phonenumber1 != "" && phonenumber2 != "" && phonenumber3 != ""){
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
                    } else {
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(phonenumber1, null, sendMessage, sentPI, deliveredPI);
                        sms.sendTextMessage(phonenumber2, null, sendMessage, sentPI, deliveredPI);
                        sms.sendTextMessage(phonenumber3, null, sendMessage, sentPI, deliveredPI);
                        sms.sendTextMessage(phonenumber4, null, sendMessage, sentPI, deliveredPI);
                        sms.sendTextMessage(phonenumber5, null, sendMessage, sentPI, deliveredPI);
                        Log.d("Message: ", phonenumber1);
                        Log.d("Message: ", phonenumber2);
                        Log.d("Message: ", phonenumber3);
                        Log.d("Message: ", phonenumber4);
                        Log.d("Message: ", phonenumber5);
                        Log.d("PhoneNum: ", getterMsg);
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Numaralarınızı ve Mesajınızı giriniz!",Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Konuma izin veriniz", Toast.LENGTH_SHORT).show();
                }
                if (getterMsg.equalsIgnoreCase("Acil Durum!")){
                    imgView.setImageResource(R.drawable.redcircle);
                }
                if (getterMsg.equalsIgnoreCase("Ben iyiyim")){
                    imgView.setImageResource(R.drawable.greencircle);
                }




            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String tstMsg = "";
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(MainActivity.this,Settings.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                Intent intent2 = new Intent(MainActivity.this,hakkimizda.class);
                startActivity(intent2);
                return true;
        }
        return true;
    }

    private boolean showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent ıntent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this, "SMS Sent!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(MainActivity.this, "Generic Failure!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainActivity.this, "No Service!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(MainActivity.this, "Null PDU!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(MainActivity.this, "Radio Off!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent ıntent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this, "SMS Delivered!", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(MainActivity.this, "SMS not Delivered!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsDeliveredReceiver);
        unregisterReceiver(smsSentReceiver);
    }

}
