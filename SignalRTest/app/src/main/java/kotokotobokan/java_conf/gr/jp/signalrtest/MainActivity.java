package kotokotobokan.java_conf.gr.jp.signalrtest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SignalR_A sgr_;
    Button btn1_;
    Button btn2_;
    Button btn3_;
    Button btn4_;

    EditText txt1_;
    EditText txt2_;
    EditText txt3_;
    EditText txt4_;

    MyConnection connection_;
    Myreceiver receiver_;
//    ServiceTest.RBinder binder_;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        btn1_=findViewById(R.id.button);
        btn1_.setOnClickListener(this);
        btn2_=findViewById(R.id.button2);
        btn2_.setOnClickListener(this);
        btn3_=findViewById(R.id.button3);
        btn3_.setOnClickListener(this);
        btn3_=findViewById(R.id.button4);
        btn3_.setOnClickListener(this);
        txt1_=findViewById(R.id.editText);
        txt2_=findViewById(R.id.editText2);
        txt3_=findViewById(R.id.editText3);
        txt4_=findViewById(R.id.editText4);

//        sgr_=new SignalR_A(this);

        Log.d("xxx","xxx:MainActivity_onCreate ");
    }

    @Override
    protected void onStart() {
        Log.d("xxx","xxx:MainActivity_onStart() ");
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("xxx","xxx:MainActivity_onStop() ");
    }

    @Override
    protected void onDestroy() {
//        Intent intent=new Intent(this,ServiceTest.class);
//        this.stopService(intent);
        super.onDestroy();

        Log.d("xxx","xxx:MainActivity_onDestroy() ");
    }

    @Override
    protected void onPause() {
        Log.d("xxx","xxx:MainActivity_onPause() ");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("xxx","xxx:MainActivity_onResume() ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void StopService(){
        Intent intent=new Intent(this,ServiceTest.class);
        this.stopService(intent);

    }


    public void StartService(){


        Intent intent=new Intent(this,ServiceTest.class);
        this.startService(intent);
//        connection_=new MyConnection();
//        receiver_=new Myreceiver();
//        IntentFilter filter=new IntentFilter(ServiceTest.SERVICE_ACTION);
//        this.registerReceiver(receiver_,filter);
//        this.bindService(intent,connection_,Context.BIND_AUTO_CREATE);
        Log.d("xxx","xxx:MainActivity_StartService");

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.button:{
                this.StartService();
//                sgr_.ConectOn();
                break;
            }
            case R.id.button2:{

                this.StopService();;
//                if(binder_!=null){
//                    binder_.initilal(1);
//                    binder_.MsgSend("ABC_TEST");
//                }

//                sgr_.MsgSend("TestMsg:"+txt2_.getText().toString());
                break;
            }
            case R.id.button3:{

//                binder_.NtfSend("ABCTEST");
                //④GET リクエストサンプルとして「Livedoor天気情報」にアクセス
//                String weatherURL = "192.168.11.10";
//                String weatherURL = "127.0.0.1";
//                HttpGet_A htga_=new HttpGet_A(this);
//                if(htga_.ChkConnect()){
//
//                    htga_.execute(weatherURL);

//                }


//            String queryString = "?city=130010"; //東京
//                String weatherURL = SignalR_A.SGR_URL;
//                String queryString = "";
//                //⑤GetAsyncTaskに渡すパラメータをObject配列に設定
//                Object[] getParams = new Object[2];
//                getParams[0] = weatherURL;
//                getParams[1] = queryString;
//                //⑥GetAsyncTaskを実行
//                new GetAsyncTask(this).execute(getParams);

                break;
            }
            case R.id.button4:{
                    this.StartService();
                break;
            }


        }

    }

class MyConnection implements ServiceConnection{

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

//        binder_=(ServiceTest.RBinder) iBinder;

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

//        binder_=null;

    }
}

class Myreceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
//        int counter=intent.getIntExtra("counter",0);
//        MainActivity act_=(MainActivity)context;
//        act_.txt4_.setText("counter:"+ counter);
//        Toast.makeText(act_,"タイマー:"+counter ,Toast.LENGTH_SHORT).show();

    }
}


}
