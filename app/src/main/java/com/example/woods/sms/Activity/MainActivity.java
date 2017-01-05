package com.example.woods.sms.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.woods.sms.R;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.accessRequestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.checkDefaultSms();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            Log.v("grant", grantResults.length + "");
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions accepted.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Please add permissions for use sms.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void accessRequestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (checkSelfPermission(SEND_SMS) == PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(READ_SMS) == PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        requestPermissions(new String[]{SEND_SMS, READ_SMS, RECEIVE_SMS}, 0);
    }

    private void checkDefaultSms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!Telephony.Sms.getDefaultSmsPackage(this).equals(getPackageName())) {
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
                startActivity(intent);
            }
        }
    }
}
