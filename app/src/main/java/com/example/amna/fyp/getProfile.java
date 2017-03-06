package com.example.amna.fyp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by NewShalimarComputer on 9/27/2016.
 */
public class getProfile extends AppCompatActivity {

    TextView tvsecQues;
    EditText etSecAns;
    Button btnOk;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        etSecAns=(EditText)findViewById(R.id.etSecAns);
        tvsecQues=(TextView)findViewById(R.id.tvSecQues);
        btnOk=(Button)findViewById(R.id.btnSend);
        String secretQues=getIntent().getStringExtra("secretQues");
        tvsecQues.setText(secretQues);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new HttpAsyncTaskAskSecretAns().execute("");
            }
        });
    }

    public  String getDetails(){
        String json = "";
        String uname= getIntent().getExtras().getString("uname");
        userData=new UserData();
        userData.setUname(uname);

        // 3. build jsonObject
        JSONObject jsonObject = new JSONObject();
        try {

            userData.setUname(uname);
            jsonObject.accumulate("uname", userData.getUname());
            jsonObject.accumulate("secretAns", etSecAns.getText().toString());

        }catch (JSONException e) {
            e.printStackTrace();
        }


        // 4. convert JSONObject to JSON to String
        json = jsonObject.toString();
        String response = null;
        boolean message=false;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://192.168.10.10:8081/matchsecretans/");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            // is output buffer writter
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
//set headers and method
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(json);
// json data
            writer.close();
            int status = urlConnection.getResponseCode();

            InputStream inputStream = urlConnection.getInputStream();
//input stream
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Toast.makeText(this, "Answer doesn't match!", Toast.LENGTH_LONG).show();
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + '\n');
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                Toast.makeText(this, "Answer doesn't match!", Toast.LENGTH_LONG).show();

            }

            response = buffer.toString();
            if (response.equals("")) {
                Toast.makeText(this, "Answer doesn't match!", Toast.LENGTH_LONG).show();
            }

            try {
                jsonObject = new JSONObject(response);
                message=jsonObject.getBoolean("message");

            }catch (JSONException j){
                j.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {

                }
            }
        }
        if(message)
            return "true";
        else return "false";

    }

    public void callToast()
    {
        Toast.makeText(this, "Answer doesn't match!", Toast.LENGTH_LONG).show();
    }

    public void openQuespage()
    {
        Intent i = new Intent(getBaseContext(),ask_newpassword.class);

        i.putExtra("uname",userData.getUname());
        startActivity(i);
    }
    private class HttpAsyncTaskAskSecretAns extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return getDetails();
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("false"))
                callToast();
            else
                openQuespage();
        }
    }
}
