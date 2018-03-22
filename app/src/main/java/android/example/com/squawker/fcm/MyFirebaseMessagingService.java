package android.example.com.squawker.fcm;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by pavin on 22.03.2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String JSON_KEY_AUTHOR_KEY = SquawkContract.COLUMN_AUTHOR_KEY;
    private static final String JSON_AUTHOR = SquawkContract.COLUMN_AUTHOR;
    private static final String JSON_KEY_MESSAGE = SquawkContract.COLUMN_MESSAGE;
    private static final String JSON_KEY_DATE = SquawkContract.COLUMN_DATE;

    private static final int NOTIFICATION_MAX_CHARACTERS = 30;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> remoteMessageData = remoteMessage.getData();

        if(remoteMessageData.size() > 0){
            sendNotification(remoteMessageData);
            insertSquawk(remoteMessageData);
        }

    }

    private void sendNotification(Map<String, String> remoteMessageData) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String author = remoteMessageData.get(JSON_AUTHOR);
        String message = remoteMessageData.get(JSON_KEY_MESSAGE);

        if(message.length() > NOTIFICATION_MAX_CHARACTERS)
            message = message.substring(0, NOTIFICATION_MAX_CHARACTERS) + "\u2026";

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_duck)
                .setContentText(message)
                .setContentTitle(author)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(0, builder.build());

    }

    private void insertSquawk(final Map<String, String> data){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(SquawkContract.COLUMN_AUTHOR, data.get(JSON_AUTHOR));
                contentValues.put(SquawkContract.COLUMN_AUTHOR_KEY, data.get(JSON_KEY_AUTHOR_KEY));
                contentValues.put(SquawkContract.COLUMN_MESSAGE, data.get(JSON_KEY_MESSAGE));
                contentValues.put(SquawkContract.COLUMN_DATE, data.get(JSON_KEY_DATE));
                return null;
            }
        };
        asyncTask.execute();
    }

}
