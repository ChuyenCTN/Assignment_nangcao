package com.chuyenctn.assignment_nangcao.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chuyenctn.assignment_nangcao.R;
import com.chuyenctn.assignment_nangcao.model.XMLDOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class News_Activity extends AppCompatActivity {
    private ListView lvNews;
    ArrayList<String> arrayTitle,arrayLink;
    ArrayAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        new ReadRSS().execute("https://vnexpress.net/rss/giao-duc.rss");
        lvNews = (ListView) findViewById(R.id.lv_News);
        arrayTitle=new ArrayList<>();
        arrayLink=new ArrayList<>();
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayTitle);
        lvNews.setAdapter(adapter);

        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent=new Intent(getApplicationContext(),show_ItemNews_Activity.class);
               intent.putExtra("linknews",arrayLink.get(position));
               startActivity(intent);
            }
        });
    }

    public void Back_Activity_News(View view) {
        startActivity(new Intent(getApplicationContext(), Home_Activity.class));
    }

    private class ReadRSS extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            String title = "";
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                title = parser.getValue(element, "title");
                arrayTitle.add(title);
                arrayLink.add(parser.getValue(element,"link"));
            }
            adapter.notifyDataSetChanged();
        }
    }
}
