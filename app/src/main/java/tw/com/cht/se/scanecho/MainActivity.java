package tw.com.cht.se.scanecho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.text);
    }

    /**
     * Fire the barcode scanning
     *
     * @param view
     */
    public void onScan(View view) {
        IntentIntegrator ii = new IntentIntegrator(this);
        ii.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                HttpPostTask task = new HttpPostTask(text); // background asynchronized task

                String url = "http://ap-yunyenwang.rhcloud.com/echo";

                task.execute(url, contents); // send message and get the reply into text view
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
