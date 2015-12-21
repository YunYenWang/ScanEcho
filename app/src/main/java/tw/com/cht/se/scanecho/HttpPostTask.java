package tw.com.cht.se.scanecho;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anna on 2015/12/21.
 */
public class HttpPostTask extends AsyncTask<String, Long, String> {
    final static String TAG = HttpPostTask.class.getName();

    final TextView text;

    public HttpPostTask(TextView text) {
        this.text = text;
    }

    protected String toString(Reader r) throws IOException {
        StringWriter sw = new StringWriter();
        char[] cbuf = new char[4096];
        int s;

        while ((s = r.read(cbuf)) > 0) {
            sw.write(cbuf, 0, s);
        }

        sw.flush();

        return sw.toString();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]); // URL
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/plain");
            con.setDoInput(true);
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            try {
                OutputStreamWriter osw = new OutputStreamWriter(os);
                osw.write(params[1]); // message
                osw.flush();

            } finally {
                os.close();
            }

            con.connect();

            Log.i(TAG, String.format("%d : %s", con.getResponseCode(), con.getResponseMessage()));

            InputStream is = con.getInputStream();
            try {
                InputStreamReader isr = new InputStreamReader(is);
                String message = toString(isr);

                Log.i(TAG, "@@@@@@@@@@@@@@@@@@ " + message);

                return message;

            } finally {
                is.close();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        text.setText(s);
    }
}
