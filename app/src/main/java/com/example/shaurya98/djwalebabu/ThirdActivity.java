package com.example.shaurya98.djwalebabu;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    String message1;
    String t2;
    String name;
    LinkedHashMap<Integer,ArrayList<String>> songs_list;
    TextView stringTextView;
    ProgressBar spinner;
    Integer limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("RESULTS");

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);


        Intent intent = getIntent();
        message1 = intent.getStringExtra(SecondActivity.EXTRA_MESSAGE1);

        stringTextView = (TextView)findViewById(R.id.textView2);

        AsyncTask tas = new Connection().execute();
    }

    private class Connection extends AsyncTask<Object,String,LinkedHashMap<Integer,ArrayList<String>> >{
        @Override
        protected LinkedHashMap<Integer,ArrayList<String>> doInBackground(Object... arg0) {
            LinkedHashMap<Integer,ArrayList<String>> c = Exe();
            return c;
        }

        @Override
        protected void onPostExecute(LinkedHashMap<Integer,ArrayList<String>> prob){
            spinner.setVisibility(View.GONE);
            limit = prob.size();
            stringTextView.setText(stringTextView.getText() + "\n" + "\n" + "\n"+"\n" + "\n" + "\n");
            if (prob.size()==0)
                stringTextView.setText(stringTextView.getText() + "            No Match Found!!");

            for (int i = 0; i < prob.size(); i++) {
                stringTextView.setText(stringTextView.getText() + prob.get(i+1).get(0) + "\n");
            }

        }
    }

    public LinkedHashMap<Integer,ArrayList<String>> Exe(){
        Document dc= null;
        songs_list = new LinkedHashMap<Integer,ArrayList<String>>();

        try {
            String url = message1;
            dc = Jsoup.connect(url).get();
            dc.outputSettings().prettyPrint(false);

            Elements tables = dc.select("table");
            if (tables.size()>0){
                Element table = dc.select("table").first();
                Element table_body = table.select("tbody").first();
                Elements table_rows = table_body.select("tr");


                ArrayList<String> temp;
                int counter=0;

                for (Element s:table_rows){
                    if (counter==0)
                    {
                        counter = 1;
                        continue;
                    }
                    temp=new ArrayList<String>();
                    temp.add(s.select("td").first().text());

                    Elements z = s.select("td");
                    int tc=0;
                    Element m = null;
                    for (Element y : z)
                    {
                        m = y;
                        tc += 1;
                        if(tc==2)
                            break;
                    }

                    temp.add(m.select("a").first().attr("href"));

                    songs_list.put(counter,temp);
                    counter += 1;
                }



            }
            else
            {
                Elements para = dc.select("p");
                Element target = null;
                for (Element e : para){
                    Elements str = e.select("strong");

                    if (str.size()>1){
                        int tc1 = 0;
                        Element u = null;
                        for (Element i : str){
                            u = i;
                            tc1 += 1;
                            if(tc1==2)
                                break;

                        }


                        if (u!=null && u.text().equals("Download")){
                            target = e;
                            break;
                        }

                    }

                }

                String htm = target.html();
                Document.OutputSettings settings = new Document.OutputSettings();
                settings.prettyPrint(false);
                String str = Jsoup.clean(htm, "", Whitelist.none(), settings);
                String[] wor = str.split(System.getProperty("line.separator"));
                Log.e("c2",Integer.toString(wor.length));

                ArrayList<String> wo = new ArrayList( Arrays.asList(wor) );



                for(Integer i=0;i<wo.size();i++)
                {

                    if(wo.get(i).charAt(0)=='0' || wo.get(i).charAt(0)=='1' || wo.get(i).charAt(0)=='2')
                    {
                        String [] temp3 = wo.get(i).split("\\s");
                        String h = "";
                        for(int k = 0 ; k<temp3.length-1 ;k++)
                        {

                            h += temp3[k];
                            h += " ";
                        }
                        Log.e("c2",temp3[temp3.length-1]);
                        wo.set(i,h);
                    }
                    else
                    {
                        wo.remove(wo.get(i));
                        i--;
                    }
                }


                ArrayList<String> t2 = new ArrayList<>();


                Elements l2 = target.select("a");

                int counter2=0;

                for(Element t3:l2){
                    t2 = new ArrayList<>();
                    t2.add(wo.get(counter2));
                    counter2++;
                    t2.add(t3.attr("href"));
                    songs_list.put(counter2,t2);
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return songs_list;

    }

    public void SelectSong(View view){
        EditText editText = (EditText) findViewById(R.id.editText);
        String m = editText.getText().toString();
        Integer n = Integer.parseInt(m);
        if(n>=1 && n<=limit){
            t2 = songs_list.get(n).get(1);
            name = songs_list.get(n).get(0);
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Storage Permissions Not Given",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Exec();
                }
            }
            else {
                Exec();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid Serial Number!!", Toast.LENGTH_SHORT).show();
        }


    }

//    public void Permission() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.e("Permission error","You have permission");
//
//            } else {
//
//                Log.e("Permission error","You have asked for permission");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            }
//        }
//        else { //you dont need to worry about these stuff below api level 23
//            Log.e("Permission error","You already have the permission");
//        }
//
//    }


    public void Exec(){
        Uri u = Uri.parse(t2);
        DownloadManager.Request request = new DownloadManager.Request(u);
        String down_name = name + ".mp3";
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, down_name);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); // to notify when download is complete
        request.allowScanningByMediaScanner();// if you want to be available from media players
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }

}
