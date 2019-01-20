package kotokotobokan.java_conf.gr.jp.notificationproject;



/*
 * AsyncTask for GET Method
 * Created by noriseto on 2017/11/28.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GetAsyncTask extends AsyncTask<Object, Void, Object> {

    //①USE WEEK REFERENCE
    private final WeakReference<Activity> w_Activity;

    //②コンストラクタで、 呼び出し元Activityを弱参照で変数セット
    GetAsyncTask(Activity activity) {
        this.w_Activity = new WeakReference<>(activity);
    }

    //③バックグラウンド処理
    @Override
    protected Object doInBackground(Object[] data) {

        //Object配列でパラメータを持ってこれたか確認
        String url = (String)data[0];
        String queryString = (String)data[1];

        //④HTTP処理用オプジェクト作成
        OkHttpClient client = new OkHttpClient();

        //⑤送信用リクエストを作成
        Request request = new Request.Builder().url(url + queryString).get().build();

        //⑥受信用オブジェクトを作成
        Call call = client.newCall(request);
        String result = "";

        //⑦送信と受信
        try {
            Response response = call.execute();
            ResponseBody body = response.body();
            if (body != null) {
                result = body.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //⑧結果を返し、onPostExecute で受け取る
        return result;
    }

    //⑨バックグラウンド完了処理
    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        Log.d("xxx","xxx:onPostExecute"+(String)result);
        //⑩簡単にJSONレスポンスをパースしてみる
        String title = result.toString();
        String description = "";
        String publicTime = "";
        try {
            JSONObject json = new JSONObject((String)result);
            title = json.getString("title");
            JSONObject descriptionObj = (JSONObject)json.get("description");
            description = descriptionObj.getString("text");
            publicTime = descriptionObj.getString("publicTime");
        } catch (JSONException je) {
            je.getStackTrace();
        }

        //⑪画面表示
        Activity activity = w_Activity.get();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            // activity is no longer valid, don't do anything!
            return;
        }
        TextView tv = activity.findViewById(R.id.editText3);
        String showText = title + "\n" + publicTime + "\n" + description;
        tv.setText(showText);
    }

}