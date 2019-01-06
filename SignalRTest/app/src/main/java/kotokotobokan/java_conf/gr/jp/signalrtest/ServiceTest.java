package kotokotobokan.java_conf.gr.jp.signalrtest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.OnClosedCallback;

import java.util.Timer;
import java.util.TimerTask;

public class ServiceTest extends Service {

    Timer timer;
    //sample"http://localhost:1218/signalr"chatHub
    //    public  static String SGR_URL="http://localhost:5001/chatHub";
    //    public  static String SGR_URL="https://127.0.0.1:5001/chatHub";
    //  public  static String SGR_URL="https://192.168.11.10/";
    public  static String SGR_URL="http://192.168.11.2:5001/chatHub";
    //public  static String SGR_URL="http://10.0.2.2/chatHub";
    //http://10.0.2.2/
    //https://araramistudio.jimdo.com/2018/01/11/android%E3%81%AE%E3%82%A8%E3%83%9F%E3%83%A5%E3%83%AC%E3%83%BC%E3%82%BF%E3%83%BC%E3%81%8B%E3%82%89%E8%87%AA%E8%BA%AB%E3%81%AEpc-localhost-%E3%81%B8%E6%8E%A5%E7%B6%9A/
    //http://192.168.11.10/

    HubConnection hubConnection;


    public static final String SERVICE_ACTION="service_action";
    private int counter=0;

    public ServiceTest() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);


        this.ConctOn();

//        if(timer==null) {
//            timer = new Timer();
//            Log.d("xxx", "xxx:ServiceTest_onStartCommand");
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    counter++;
//                    Intent intent = new Intent(SERVICE_ACTION);
//                    intent.putExtra("counter", counter);
//                    sendBroadcast(intent);
//                    Log.d("xxx", "xxx:ServiceTest_sendBroadcast");
//                }
//            }, 5000, 5000);
//        }
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

    public void ConctOn(){

        if(hubConnection ==null) {

            try{
                hubConnection = HubConnectionBuilder.create(SGR_URL).build();
                Log.d("xxx","xxx:ConctOn_1");
                hubConnection.on("ReceiveMessage", (user,message) -> {

                    Log.d("xxx","xxx:ConctOn_Receive");
                    Intent intent = new Intent(SERVICE_ACTION);
                    intent.putExtra("user", user);
                    intent.putExtra("user", message);
                    sendBroadcast(intent);


                }, String.class,String.class);
                hubConnection.start().blockingAwait();

                hubConnection.onClosed(new OnClosedCallback() {
                    @Override
                    public void invoke(Exception exception) {
                        Log.d("xxx","xxx: hubConnection.onClosed:"+exception.toString());

                        hubConnection.start();

                    }
                });

                Log.d("xxx","xxx:ConctOn_2");

            }catch (Error e){
                Log.d("xxx","xxx:ConctOn_Err"+e.toString());

            }

        }


    }

    public void ConctStop(){
        if(hubConnection !=null) {
            Log.d("xxx","xxx:ConctStop");
            hubConnection.stop();
        }
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

        public void MsgSend(String st_){
            if(hubConnection !=null) {
                Log.d("xxx","xxx:MsgSend:" + st_);
                hubConnection.send("SendMessage", "abc",st_);
            }
        }


    }


}
