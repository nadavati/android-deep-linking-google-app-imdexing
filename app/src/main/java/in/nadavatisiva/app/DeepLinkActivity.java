package in.nadavatisiva.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


/**
 * Created by Siva on 09-12-2015.
 */
public class DeepLinkActivity extends Activity {
    TextView deepLinkUrl;
    static final Uri APP_URI = Uri.parse("android-app://in.nadavatisiva.app/http/example.in/hackingString");
    static final Uri WEB_URL = Uri.parse("http://example.in/hackingString/");
    private GoogleApiClient mClient;
    private String mTitle;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        mTitle = "App Indexing";
        deepLinkUrl = (TextView) findViewById(R.id.deep_link);
        Intent intent = getIntent();
        if (intent.getAction() != null) {
            String data = intent.getDataString();
            if (data != null) {
                 value = data.replace("http://", "").replace("example.in/hackString/", "");
                if (!value.equals("")) {
                    deepLinkUrl.setText(value);
                }
            }
        }
    }
    public Action getAction() {
        Thing object = new Thing.Builder()
                .setName(mTitle)
                .setUrl(APP_URI)
                .setUrl(WEB_URL)
                .build();

        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
        try {
            if (value != null) {
                AppIndex.AppIndexApi.start(mClient, getAction());
            }
        }catch (Exception exe){
            exe.getStackTrace();
        }
    }
    @Override
    public void onStop() {
        try {
            if (value != null) {
                AppIndex.AppIndexApi.end(mClient, getAction());
            }
        }catch (Exception exe){
            exe.getStackTrace();
        }
        mClient.disconnect();
        super.onStop();
    }
}