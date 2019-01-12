package kotokotobokan.java_conf.gr.jp.signalrtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.OnClosedCallback;

import java.util.Timer;
import java.util.TimerTask;

public class ServiceTest extends Service {

    public static final String SERVICE_ACTION = "service_action";
    //sample"http://localhost:1218/signalr"chatHub
    //    public  static String SGR_URL="http://localhost:5001/chatHub";
    //    public  static String SGR_URL="https://127.0.0.1:5001/chatHub";
    //  public  static String SGR_URL="https://192.168.11.10/";
    public static String SGR_URL = "http://192.168.11.2:5001/chatHub";
    //public  static String SGR_URL="http://10.0.2.2/chatHub";
    //http://10.0.2.2/
    //https://araramistudio.jimdo.com/2018/01/11/android%E3%81%AE%E3%82%A8%E3%83%9F%E3%83%A5%E3%83%AC%E3%83%BC%E3%82%BF%E3%83%BC%E3%81%8B%E3%82%89%E8%87%AA%E8%BA%AB%E3%81%AEpc-localhost-%E3%81%B8%E6%8E%A5%E7%B6%9A/
    //http://192.168.11.10/
    static Timer timer;
    HubConnection hubConnection;
    HubStatus hubStatus = HubStatus.NULL;
    Vibrator vibrator;
    private int counter = 0;

    public ServiceTest() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        this.ConectOn();

        this.startForeground(1, GetNotification());
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (timer != null) {
            timer.cancel();
        }
            timer = new Timer();
            Log.d("xxx", "xxx:ServiceTest_onStartCommand");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    GetHubStatus();
                    ResetHubConnect();
                }
            }, 5000, 5000);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("xxx", "xxx:ServiceTest_onDestroy() ");
        timer.cancel();
        super.onDestroy();
    }

    void GetHubStatus() {
        if (hubConnection == null) {
            hubStatus = HubStatus.NULL;
            Log.d("xxx", "xxx:ServiceTest_GetHubStatus():NULL");
        } else {
            switch (hubConnection.getConnectionState()) {
                case CONNECTED: {
                    hubStatus = HubStatus.CONNECTED;
                    Log.d("xxx", "xxx:ServiceTest_GetHubStatus():CONNECTED");
                    break;
                }
                case DISCONNECTED: {
                    hubStatus = HubStatus.DISCONNECTED;
                    Log.d("xxx", "xxx:ServiceTest_GetHubStatus():DISCONNECTED");
                    break;
                }
            }
        }
    }

    /**
     * Hubの接続状態に応じて、再接続を試みる。
     */
    void ResetHubConnect() {

        switch (hubStatus) {
            case NULL: {
                Log.d("xxx", "xxx:ServiceTest_ ResetHubConnect():ConectOn()");
                ConectOn();

                break;
            }
            case DISCONNECTED: {
                Log.d("xxx", "xxx:ServiceTest_ ResetHubConnect():hubConnection.start()");
                vibrator.vibrate(200);
                hubConnection.start();

                break;
            }
            case CONNECTED: {
                Log.d("xxx", "xxx:ServiceTest_ ResetHubConnect():何もしない。");
                //何もしない。
                break;
            }

        }

    }

    public void ConectOn() {

        if (hubConnection == null) {

            try {
                hubConnection = HubConnectionBuilder.create(SGR_URL).build();
                Log.d("xxx", "xxx:ConctOn_1");
                hubConnection.on("ReceiveMessage", (user, message) -> {

                    Log.d("xxx", "xxx:ConctOn_Receive");
                    Intent intent = new Intent(SERVICE_ACTION);
                    intent.putExtra("user", user);
                    intent.putExtra("user", message);
                    sendBroadcast(intent);
                    sendNotification(message);


                }, String.class, String.class);
                hubConnection.onClosed(new OnClosedCallback() {
                    @Override
                    public void invoke(Exception exception) {
                        Log.d("xxx", "xxx: hubConnection.onClosed:" + exception.toString());

                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                Log.d("xxx", "xxx: hubConnection.ReStart");
                                hubConnection.start();
                            }
                        };
                        timer.schedule(timerTask, 5000);

                    }
                });

                Log.d("xxx", "xxx:ConctOn_2");
                hubConnection.start();
            } catch (Error e) {
                Log.d("xxx", "xxx:ConctOn_Err" + e.toString());

            }

        }


    }

    public void ConctStop() {
        if (hubConnection != null) {
            Log.d("xxx", "xxx:ConctStop");
            hubConnection.stop();
        }
    }

    private Notification GetNotification() {
        final Notification.Builder builder_ = new Notification.Builder(getApplicationContext())
                .setTicker("表示されない")
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Title_Test")
                .setContentText("aaa");

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

        builder_.setContentIntent(pi);

        long[] vibrate_ptn = {0, 100, 300, 1000}; // 独自バイブレーションパターン
        return builder_.build();
    }


    private void sendNotification(String message_) {
        final Notification.Builder builder_ = new Notification.Builder(getApplicationContext())
                .setTicker("表示されない")
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Title_Test")
                .setContentText(message_);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

        builder_.setContentIntent(pi);

        long[] vibrate_ptn = {0, 100, 300, 1000}; // 独自バイブレーションパターン

        // NotificationManagerのインスタンス取得
        NotificationManager nm =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, builder_.build()); // 設定したNotificationを通知する
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new RBinder();
    }


    enum HubStatus {

        CONNECTED,
        DISCONNECTED,
        NULL

    }

    public class RBinder extends Binder {

        public void initilal(int num) {
            counter = num;
        }

        public void MsgSend(String st_) {
            if (hubConnection != null) {
                Log.d("xxx", "xxx:MsgSend:" + st_);
                hubConnection.send("SendMessage", "abc", st_);
            }
        }

        public void NtfSend(String st_) {

            final Notification.Builder builder_ = new Notification.Builder(getApplicationContext())
                    .setTicker("表示されない")
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Title_Test")
                    .setContentText(st_);

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);

            builder_.setContentIntent(pi);

            long[] vibrate_ptn = {0, 100, 300, 1000}; // 独自バイブレーションパターン

            // NotificationManagerのインスタンス取得
            NotificationManager nm =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(1, builder_.build()); // 設定したNotificationを通知する
        }

    }


}
