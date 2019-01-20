package kotokotobokan.java_conf.gr.jp.notificationproject;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FCMHandler {
    //https://notificationproject-a0485.firebaseio.com
    public static String SCOPES = "https://www.googleapis.com/auth/firebase.messaging";
    public static String SERVICE_ACCOUNT_FILE = "notificationproject-a0485-firebase-adminsdk-qa7nw-71043b1ab8.json";
    public static String FCM_ENDPOINT = "https://fcm.googleapis.com/v1/projects/notificationproject-a0485/messages:send";
    public static String GROUP="notification";

    //https://fcm.googleapis.com/v1/projects/myproject-b5ae1/messages:send
    public static String userID;
    public static String oAuthToken;
    private static MainActivity mainActivity;

    public FCMHandler(MainActivity act) {
        mainActivity = act;
    }
/**
     * OAuth2アクセストークンを取得し保持する。
     *
     * @return
     * @throws IOException
     */
    public static void MadeOAuth2Token()  {

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] object) {

                try {
                    userID = FirebaseInstanceId.getInstance().getToken();
                    GoogleCredential googleCredential = GoogleCredential
                            .fromStream(mainActivity.getAssets().open(SERVICE_ACCOUNT_FILE))
                            .createScoped(Arrays.asList(SCOPES));
                    googleCredential.refreshToken();
                    oAuthToken = googleCredential.getAccessToken();

                    //https://firebase.google.com/docs/cloud-messaging/android/send-multiple
                    //グループ登録
                    FirebaseMessaging.getInstance().subscribeToTopic(GROUP)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//                                    String msg = getString(R.string.msg_subscribed);
//                                    if (!task.isSuccessful()) {
//                                        msg = getString(R.string.msg_subscribe_failed);
//                                    }
//                                    Log.d(TAG, msg);
//                                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });




                } catch (Error e) {
                    Log.d("xxx", "xxx:FCMHandler_GetAccessToken()" + e.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //Log.d("xxx", "xxx:FCMHandler_GetAccessToken()" + response.to);
                }
                return true;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("oauth2_token");
                myRef.setValue(oAuthToken);
                myRef = database.getReference("register_id");
                myRef.setValue(userID);
                mainActivity.txt1_.setText("登録ID:" + userID);
                mainActivity.txt2_.setText("OAut2:" + oAuthToken);

            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        };
        asyncTask.execute();

    }
    public static void SendMessageToUserID(String notificationID,String title) {

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] object) {
                Response response=null;
                final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                OkHttpClient httpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(FCM_ENDPOINT)
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .addHeader("Authorization", "Bearer " + oAuthToken)
                        .post(RequestBody.create(mediaType, "{'message':{'token' : '" + userID + "', 'data' : "+
                                "{'body' : '" + object[1]+ "', 'title' : '" + object[0] + "'}}}"));
                Request request = builder.build();
                try {
                    response = httpClient.newCall(request).execute();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("title");
                    myRef.setValue( object[0]);
                    myRef = database.getReference("body");
                    myRef.setValue(object[1]);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Toast.makeText(mainActivity,o.toString(),Toast.LENGTH_SHORT);
            }
        };

        String[] strings=new String[2];
        strings[0]=notificationID;
        strings[1]=title;

        asyncTask.execute(strings);
    }


    public static void SendMessageToGroup(String notificationID, String title) {

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] object) {
                Response response=null;
                final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                OkHttpClient httpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(FCM_ENDPOINT)
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .addHeader("Authorization", "Bearer " + oAuthToken)
                        .post(RequestBody.create(mediaType, "{'message':{'topic' : '" +GROUP +"', 'data' : "+
                                "{'body' : '" + object[1]+ "', 'title' : '" + object[0] + "'}}}"));
                Request request = builder.build();
                try {
                     response = httpClient.newCall(request).execute();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("title");
                    myRef.setValue( object[0]);
                    myRef = database.getReference("body");
                    myRef.setValue(object[1]);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Toast.makeText(mainActivity,o.toString(),Toast.LENGTH_SHORT);
            }
        };

        String[] strings=new String[2];
        strings[0]=notificationID;
        strings[1]=title;

        asyncTask.execute(strings);
    }

    private Notification GetNotification() {
        final Notification.Builder builder_ = new Notification.Builder(mainActivity)
                .setTicker("表示されない")
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.dango_roll)
                .setContentTitle("Title_Test")
                .setContentText("aaa");

        Intent i = new Intent(mainActivity, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(mainActivity, 0, i, 0);

        builder_.setContentIntent(pi);

        long[] vibrate_ptn = {0, 100, 300, 1000}; // 独自バイブレーションパターン
        return builder_.build();
    }

    public void sddds() throws IOException, JSONException {

        GoogleCredential googleCredential = GoogleCredential
                .fromStream(mainActivity.getAssets().open(SERVICE_ACCOUNT_FILE))
                .createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshToken();
        oAuthToken = googleCredential.getAccessToken();


        JSONObject json = new JSONObject("aa");


        final MediaType mediaType = MediaType.parse("application/json");

        OkHttpClient httpClient = new OkHttpClient();

        Request.Builder builder = new Request.Builder();
        builder.url(FCM_ENDPOINT);
        builder.addHeader("Content-Type", "application/json; UTF-8");
        builder.addHeader("Authorization", "Bearer " + oAuthToken);
        //builder.post(RequestBody.create(mediaType, json));
        Request request = builder.build();


        Response response = httpClient.newCall(request).execute();
        if (response.isSuccessful()) {

            Log.d("xxx", "xxx:Message sent to FCM server");

        }

    }

    public static void GetAOuth2Token_xx() throws IOException {


        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] object) {
                String ans_ = "";
                Log.d("xxx", "xxx:FCMHandler_GetAccessToken()_start");
                try {
                    userID = FirebaseInstanceId.getInstance().getToken();
                    GoogleCredential googleCredential = GoogleCredential
                            .fromStream(mainActivity.getAssets().open(SERVICE_ACCOUNT_FILE))
                            .createScoped(Arrays.asList(SCOPES));
                    googleCredential.refreshToken();
                    oAuthToken = googleCredential.getAccessToken();
                    Log.d("xxx", "xxx:FCMHandler_GetAccessToken()" + ans_);

//                    JSONObject json = new JSONObject();

                    final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                    OkHttpClient httpClient = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(FCM_ENDPOINT)
                            .addHeader("Content-Type", "application/json;charset=utf-8")
                            .addHeader("Authorization", "Bearer " + oAuthToken)
                            //RequestBody requestBody=RequestBody.create(mediaType, "{'message':{'token' : '" + userID + "', 'notification' : {'body' : 'abc', 'title' : 'bdf'}}}");
                            .post(RequestBody.create(mediaType, "{'message':{'token' : '" + userID + "', 'notification' : {'body' : 'abc', 'title' : 'bdf'}}}"));
                    //.post(RequestBody.create(mediaType, "{'message':'token'}"));
                    Request request = builder.build();
                    //"{\"message\":{\"token\" : \"" + userID + "\",\"notification\" :{\"body\" : \"This is an FCM notification message,\"title\" : \"FCM Message\",}}}"
                    Response response = httpClient.newCall(request).execute();


                } catch (Error e) {
                    Log.d("xxx", "xxx:FCMHandler_GetAccessToken()" + e.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //Log.d("xxx", "xxx:FCMHandler_GetAccessToken()" + response.to);
                }
                return true;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.d("xxx", "xxx:Curl  " + "curl -X POST -H \"Authorization: Bearer " + oAuthToken + "\" -H \"Content-Type: application/json\" -d '{ \"message\":{\"token\": \"" + userID + "\",\"data\": {\"title\": \"FCM Message\",\"body\": \"This is an FCM Message\"} }}' https://fcm.googleapis.com/v1/projects/notificationproject-a0485/messages:send HTTP/1.1)");


            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        };
        asyncTask.execute();


    }




}
