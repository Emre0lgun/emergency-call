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
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Set;

import tr.org.akut.guvendeyim.R;

public class Settings extends AppCompatActivity {

    EditText tel1, tel2, tel3, tel4, tel5;
    Button save_btn, back_btn;
    RadioButton red, green;
    RadioGroup radioGroup;
    String msg = "";
    String phonenum1 = "",phonenum2 = "",phonenum3 = "",phonenum4 = "",phonenum5 = "";
    String loc = "", googleLoc = "";
    private String getMsg = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tel1 = findViewById(R.id.tel1);
        tel2 = findViewById(R.id.tel2);
        tel3 = findViewById(R.id.tel3);
        tel4 = findViewById(R.id.tel4);
        tel5 = findViewById(R.id.tel5);

        red = findViewById(R.id.danger);
        green = findViewById(R.id.okey);
        save_btn = findViewById(R.id.save);
        back_btn = findViewById(R.id.bck);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String getPhonenum1 = prefs.getString("Tel1",phonenum1);
        String getPhonenum2 = prefs.getString("Tel2",phonenum2);
        String getPhonenum3 = prefs.getString("Tel3",phonenum3);
        String getPhonenum4 = prefs.getString("Tel4",phonenum4);
        String getPhonenum5 = prefs.getString("Tel5",phonenum5);



        tel1.setText(getPhonenum1);
        tel2.setText(getPhonenum2);
        tel3.setText(getPhonenum3);
        tel4.setText(getPhonenum4);
        tel5.setText(getPhonenum5);
        red.setChecked(prefs.getBoolean("Red", false));
        green.setChecked(prefs.getBoolean("Green", false));
        if (prefs.getBoolean("Red", false)){
            getMsg = "Acil Durum!";
        } else if (prefs.getBoolean("Green", false)) {
            getMsg = "Ben iyiyim";
        } else {
            getMsg = "";
        }
        //Toast.makeText(this,"Message :"+ getMsg,Toast.LENGTH_SHORT).show();





        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phonenum1 = tel1.getText().toString();
                phonenum2 = tel2.getText().toString();
                phonenum3 = tel3.getText().toString();
                phonenum4 = tel4.getText().toString();
                phonenum5 = tel5.getText().toString();
                radioGroup = findViewById(R.id.msg);
                if (radioGroup.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(Settings.this,"Mesajınızı Seciniz.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    msg = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                }

                if (!msg.matches("") && phonenum1.length() == 11 && phonenum2.length() == 11 && phonenum3.length() == 11 && phonenum4.length() == 11 && phonenum5.length() == 11) {
                    googleLoc = "Konumum: " + "https://www.google.com/maps/place/" + loc;

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Settings.this);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString("Tel1",phonenum1);
                    editor.putString("Tel2",phonenum2);
                    editor.putString("Tel3",phonenum3);
                    editor.putString("Tel4",phonenum4);
                    editor.putString("Tel5",phonenum5);
                    editor.putBoolean("Red", red.isChecked());
                    editor.putBoolean("Green", green.isChecked());
                    editor.apply();
                    SharedPreferences getPrefs = getSharedPreferences("SaveData",Context.MODE_PRIVATE);
                    SharedPreferences.Editor getEditor = getPrefs.edit();
                    getEditor.putString("msg",msg);
                    getEditor.putString("phone1",phonenum1);
                    getEditor.putString("phone2",phonenum2);
                    getEditor.putString("phone3",phonenum3);
                    getEditor.putString("phone4",phonenum4);
                    getEditor.putString("phone5",phonenum5);
                    getEditor.apply();

                    Intent intent = new Intent(Settings.this,MainActivity.class);
                    intent.putExtra("telefon1",phonenum1);
                    intent.putExtra("telefon2",phonenum2);
                    intent.putExtra("telefon3",phonenum3);
                    intent.putExtra("telefon4",phonenum4);
                    intent.putExtra("telefon5",phonenum5);
                    intent.putExtra("message",msg);
                    startActivity(intent);

                } else {
                    Toast.makeText(Settings.this,"Numaraları gösterildiği gibi giriniz ve mesajınızı ",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
