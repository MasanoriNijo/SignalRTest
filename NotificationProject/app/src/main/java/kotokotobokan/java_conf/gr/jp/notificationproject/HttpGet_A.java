package kotokotobokan.java_conf.gr.jp.notificationproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class HttpGet_A extends AsyncTask<String,Void,String> {

    private MainActivity act_;

    public HttpGet_A(MainActivity act) {

        super();
        act_=act;

    }

    @Override
    protected String doInBackground(String... strings) {
        Socket socket=null;
        BufferedReader br=null;
        BufferedWriter bw=null;
        Log.d("xxx","doInBackground:"+strings[0]);
        String host=strings[0];
        String HTTP_GET="GET / HTTP/1.0 \n" +
        "Host: " + host +"\n" +
        "Connection: close" +"\n\n";
        StringBuffer stringBuffer=new StringBuffer();

        try {
//            /chatHub
            socket=new Socket(host,5001);
            bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //httpリクエスト送信
            bw.write(HTTP_GET);
            bw.flush();
            //httpレスポンス受信
            String line_;
            while ((line_=br.readLine())!=null){
                Log.d("xxx","xxx:br.readLine()"+line_);
                stringBuffer.append(line_);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "接続に失敗しました。";
        }finally {
            try{
                if(br!=null) br.close();
                if(bw!=null) bw.close();
                if(socket!=null) socket.close();;
            }catch(IOException e){
                e.printStackTrace();
            }


        }

        Log.d("xxx","xxx:stringBuffer.toString()"+stringBuffer.toString());
        return stringBuffer.toString();
    }

     public  boolean ChkConnect(){
         ConnectivityManager connMgr=(ConnectivityManager) act_.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo networkInfo_=connMgr.getActiveNetworkInfo();
         if(networkInfo_!=null && networkInfo_.isConnected()){
             Log.d("xxx","xxx:ChkConnect_true");
             return true;
         }else{
             return false;
         }

     }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        act_.txt3_.setText("");
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("xxx","xxx:onPostExecute:"+s);
        act_.txt3_.setText(s);
    }
}
