package kotokotobokan.java_conf.gr.jp.signalrtest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import java.util.Timer;
import java.util.TimerTask;

public class ServiceTest extends Service {

    Timer timer;

    public static final String SERVICE_ACTION="service_action";
    private int counter=0;

    public ServiceTest() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);

        if(timer==null) {
            timer = new Timer();
            Log.d("xxx", "xxx:ServiceTest_onStartCommand");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    counter++;
                    Intent intent = new Intent(SERVICE_ACTION);
                    intent.putExtra("counter", counter);
                    sendBroadcast(intent);
                    Log.d("xxx", "xxx:ServiceTest_sendBroadcast");
                }
            }, 5000, 5000);
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("xxx","xxx:ServiceTest_onDestroy() ");
        timer.cancel();;
        super.onDestroy();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new RBinder();
    }

    public  class RBinder extends Binder{

        public void initilal(int num){
            counter=num;
        }


    }


}
