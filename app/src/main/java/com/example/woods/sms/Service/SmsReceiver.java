package com.example.woods.sms.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Pooya.Azarpour on 1/5/2017.
 * Email: pooya_azarpour@yahoo.com
 */

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String SMS_DELIVER = "android.provider.Telephony.SMS_DELIVER";

    private SmsManager sms = SmsManager.getDefault();
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (intent.getAction().equals(SMS_DELIVER)) {
                this.smsReceived(intent.getExtras());
            }
        } else {
            if (intent.getAction().equals(SMS_RECEIVED)) {
                abortBroadcast();
                this.smsReceived(intent.getExtras());
            }
        }
    }

    private void smsReceived(Bundle bundle) {
        if (bundle != null) {
            Object[] pdusObj = (Object[]) bundle.get("pdus");

            if (pdusObj != null) {
                for (Object pdus : pdusObj) {
                    SmsMessage currentMessage;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = bundle.getString("format");
                        currentMessage = SmsMessage.createFromPdu((byte[]) pdus, format);
                    } else {
                        currentMessage = SmsMessage.createFromPdu((byte[]) pdus);
                    }

                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + phoneNumber + "; message: " + message);

                    Toast.makeText(this.context, "senderNum: " + phoneNumber + ", message: " + message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
