package com.example.convert;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText currency1;
    EditText currency2;
    TextView ans;
    public class Download_Currency extends AsyncTask<String, Void, String>
    {
        String result = "";
        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream is = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                int i = reader.read();
                while(i != -1)
                {
                    char c = (char)i;
                    result += c;
            i = reader.read();
        }
    }catch (Exception e) {
        e.printStackTrace();
    }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject data = new JSONObject(result);
                String all_Currency = data.getString("rates");
                JSONObject curr1 = new JSONObject(all_Currency);
                String date = data.getString("date");
                String curry1 = curr1.getString(currency1.getText().toString().toUpperCase());
                String curry2 = curr1.getString(currency2.getText().toString().toUpperCase());
                double cal1 = Double.parseDouble(curry2)/Double.parseDouble(curry1);
                String cal = "" + cal1;
                ans.setText( " 1 " + currency1.getText().toString().toUpperCase() + " = " + cal + " " + currency2.getText().toString().toUpperCase()+ "\n\n This data is according to the date : " + date );
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void convert(View view)
    {

        ans = (TextView)findViewById(R.id.ans);
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(ans.getWindowToken(), 0);
        currency1 = (EditText)findViewById(R.id.currency1);
        currency2 = (EditText)findViewById(R.id.currency2);
        Download_Currency currency = new Download_Currency();
        currency.execute("https://api.exchangeratesapi.io/latest");
    }
}
