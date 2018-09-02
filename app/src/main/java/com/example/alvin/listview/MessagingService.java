package com.example.alvin.listview;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

/**
 * Created by Alvin on 01/09/2018.
 */

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        Log.d("NEW TOKEN",s );
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();

            if (data.containsKey("sender") && data.containsKey("message")) {
                String sender = data.get("sender");
                String message = data.get("message");

                Log.d("NOTIF_SENDER", sender);
                Log.d("NOTIF_MESSAGE", message);
            }
        }
    }
}
