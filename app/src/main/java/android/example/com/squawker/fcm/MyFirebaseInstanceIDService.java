package android.example.com.squawker.fcm;

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by pavin on 22.03.2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private final String LOG_TAG = "GENERATED_TOKEN";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(LOG_TAG, refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken){
        //send token to server
    }

}
