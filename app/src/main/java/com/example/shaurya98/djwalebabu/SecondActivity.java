package com.example.shaurya98.djwalebabu;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class SecondActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE1 = "extra1";
    private String movie_name;
    TextView stringTextView;
    ProgressBar spinner;
    Dictionary movies;
    Dictionary probables;
    Integer limit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("RESULTS");

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        movie_name=message;
        movie_name=movie_name.toLowerCase();

        stringTextView = (TextView)findViewById(R.id.textView2);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);


        AsyncTask tas = new Connection().execute();

    }

    private class Connection extends AsyncTask<Object,String,ArrayList<String> >{

        @Override
        protected ArrayList<String> doInBackground(Object... arg0) {
            ArrayList<String> c = Exe();
            return c;
        }

        @Override
        protected void onPostExecute(ArrayList<String> prob){
            spinner.setVisibility(View.GONE);
            stringTextView.setText(stringTextView.getText() + "\n" + "\n" + "\n"+"\n" + "\n" + "\n");
            limit = prob.size();
            if (prob.size()==0)
                stringTextView.setText(stringTextView.getText() + "            No Match Found!!");
            for(int i=0; i < prob.size(); i++){
                stringTextView.setText(stringTextView.getText() + Integer.toString(i+1) + '.'+" "+prob.get(i) + "\n");
            }

        }

    }




    public ArrayList<String> Exe(){
        Document dc= null;
        ArrayList<String> prob = new ArrayList<String> ();
        try {


            String url = "";
            char c = movie_name.charAt(0);
            if (c!='b'&& c!='t')
                url = "https://downloadming.pro/bollywood-mp3-";
            else
                url = "https://downloadming.pro/bollywood-mp3-songs-";

            if(c>='0' && c<='9')
                url += "0-9";
            else
                url = url + c;

            dc = Jsoup.connect(url).get();
            Element all = dc.select("ul.lcp_catlist").first();
            Elements movies_list = all.select("li");

            movies = new Hashtable();
            for (Element m:movies_list){
                movies.put(m.children().first().text(),m.children().first().attr("href"));
            }


            for (Enumeration k = movies.keys(); k.hasMoreElements();)
            {
                String temp =  k.nextElement().toString();
                if(temp.toLowerCase().contains(movie_name.toLowerCase())){
                    prob.add(temp);
                }
            }

            probables =  new Hashtable();
            int counter = 1;

            for (String i : prob)
            {
                probables.put(counter,i);
                counter+=1;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return prob;

    }


    public void SelectMovie(View view){
        EditText editText = (EditText) findViewById(R.id.editText);
        String m = editText.getText().toString();
        Integer n = Integer.parseInt(m);
        if(n>=1 && n<=limit){
            String t2 = movies.get(probables.get(n).toString()).toString();
            Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
            intent.putExtra(EXTRA_MESSAGE1,t2);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid Serial Number!!", Toast.LENGTH_SHORT).show();
        }


    }
}


