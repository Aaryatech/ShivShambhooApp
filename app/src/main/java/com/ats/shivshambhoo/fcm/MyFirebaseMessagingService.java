package com.ats.shivshambhoo.fcm;

import android.content.Intent;
import android.util.Log;

import com.ats.shivshambhoo.activity.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0 || !remoteMessage.getNotification().equals(null)) {

            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                // JSONObject json1 = new JSONObject(remoteMessage.getNotification().toString());

                Log.e("JSON DATA", "-----------------------------" + json);
                // Log.e("JSON NOTIFICATION", "-----------------------------" + json1);

                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "-----------------------------Exception: " + e.getMessage());
                e.printStackTrace();
            }

            super.onMessageReceived(remoteMessage);

        } else {
            Log.e("FIREBASE", "----------------------------------");
        }
    }

//    @Override
//    public void handleIntent(Intent intent) {
//        super.handleIntent(intent);
//    }

    private void sendPushNotification(JSONObject json) {

        Log.e(TAG, "--------------------------------JSON String" + json.toString());
        try {

            String title = json.getString("title");
            String message = json.getString("body");
            String imageUrl = "";
            int tag = json.getInt("tag");


            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            if (mNotificationManager.isAppIsInBackground(getApplicationContext())) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                mNotificationManager.showSmallNotification(title, message, intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                mNotificationManager.showBigNotification(title, message, intent);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: -----------" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Exception: ------------" + e.getMessage());
            e.printStackTrace();
        }

    }


}
