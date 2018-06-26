package com.example.shaurya98.djwalebabu;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SideActivity extends AppCompatActivity {

    String song;
    EditText t;
    TextView v;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side);
        t = (EditText) findViewById(R.id.editText2);
        v = (TextView) findViewById(R.id.textView3);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);


    }

    public void finder(View view){

        spinner.setVisibility(View.VISIBLE);
        song = t.getText().toString();
        song += " song movie";
        new Connection().execute();
    }



    private class Connection extends AsyncTask<Object,String,String>{

        @Override
        protected String doInBackground(Object... arg0) {
            String c = Exe();
            return c;
        }

        @Override
        protected void onPostExecute(String prob){
            spinner.setVisibility(View.GONE);
            v.setText(prob);
        }

    }



    public String Exe(){

        String url = "https://www.google.com/search?q=";
        String ret = "";
        url += song;
        try {
            Document dc= Jsoup.connect(url).get();
            Elements l1 = dc.select("div.Z0LcW");
            if(l1.size()==1){
                ret = l1.first().text().toString();
            }
            else
            {
                ret = "Sorry!! Try Again";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  ret;

    }

}
