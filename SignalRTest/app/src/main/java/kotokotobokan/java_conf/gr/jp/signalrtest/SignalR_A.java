package kotokotobokan.java_conf.gr.jp.signalrtest;

import android.util.Log;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

public class SignalR_A {

//sample"http://localhost:1218/signalr"chatHub
//    public  static String SGR_URL="http://localhost:5001/chatHub";
//    public  static String SGR_URL="https://127.0.0.1:5001/chatHub";
  //  public  static String SGR_URL="https://192.168.11.10/";
public  static String SGR_URL="http://192.168.11.2:5001/chatHub";
//public  static String SGR_URL="http://10.0.2.2/chatHub";
    //http://10.0.2.2/
    //https://araramistudio.jimdo.com/2018/01/11/android%E3%81%AE%E3%82%A8%E3%83%9F%E3%83%A5%E3%83%AC%E3%83%BC%E3%82%BF%E3%83%BC%E3%81%8B%E3%82%89%E8%87%AA%E8%BA%AB%E3%81%AEpc-localhost-%E3%81%B8%E6%8E%A5%E7%B6%9A/
    //http://192.168.11.10/
    MainActivity act_;
    HubConnection hubConnection;


    public SignalR_A(MainActivity act){

        act_=act;

    }

    public void ConctOn(){

        if(hubConnection ==null) {

            try{
                hubConnection = HubConnectionBuilder.create(SGR_URL).build();
                Log.d("xxx","xxx:ConctOn_1");
                hubConnection.on("ReceiveMessage", (user,message) -> {

                    Log.d("xxx","xxx:ConctOn_Receive");
                    act_.txt3_.setText(user+":"+message);


                }, String.class,String.class);
                hubConnection.start().blockingAwait();



                Log.d("xxx","xxx:ConctOn_2");
                act_.txt1_.setText("ConctOn!");
            }catch (Error e){
                Log.d("xxx","xxx:ConctOn_Err"+e.toString());
                act_.txt1_.setText(e.toString());
            }

        }


    }

    public void ConctStop(){
        if(hubConnection !=null) {
            Log.d("xxx","xxx:ConctStop");
            hubConnection.stop();
        }
    }

    public void MsgSend(String st_){
        if(hubConnection !=null) {
            Log.d("xxx","xxx:MsgSend:" + st_);
            hubConnection.send("SendMessage", "abc",st_);
        }
    }


}
