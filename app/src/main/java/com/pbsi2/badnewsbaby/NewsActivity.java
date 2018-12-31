package com.pbsi2.badnewsbaby;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String TAG = NewsActivity.class.getSimpleName();
    private ArrayList<BadNews> yourNews = new ArrayList<BadNews>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_news_layout);
        mRecyclerView = findViewById(R.id.newsview);
        mRecyclerView.setHasFixedSize(true);
        // For LINEAR
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        // For a GRID
        //mLayoutManager = new GridLayoutManager(this,5);
        mRecyclerView.setLayoutManager(mLayoutManager);
        new GetNews().execute();

    }

    private class GetNews extends AsyncTask<Void, Void, Void> {
        private String TAG = NewsActivity.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = MainActivity.guardianUrl;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG, "\nResponse 1st obj: " + jsonObj.toString());
                    JSONObject jsonResp = jsonObj.getJSONObject("response");
                    Log.e(TAG, "\nResponse 2st obj: " + jsonResp.toString());
                    // Getting JSON Array node
                    JSONArray mresults = jsonResp.getJSONArray("results");
                    Log.e(TAG, "\nResponse 3rd obj: " + mresults.toString());
                    // looping through All Contacts*/
                    for (int i = 0; i < mresults.length(); i++) {
                        JSONObject c = mresults.getJSONObject(i);
                        String title = c.getString("id");
                        String type = c.getString("type");
                        String sectionid = c.getString("sectionId");
                        String section = c.getString("sectionName");
                        String date = c.getString("webPublicationDate");
                        String wtitle = c.getString("webTitle");
                        String link = c.getString("webUrl");
                        String wapi = c.getString("apiUrl");
                        String summary = c.getString("id");
                        // Author is ginven in TAGS: node in JSON Object
                        JSONArray tags = c.getJSONArray("tags");
                        JSONObject d = mresults.getJSONObject(1);
                        String author = d.names().toString();

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        yourNews.add(new BadNews(title, section, author, link, summary, date));
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            NewsAdapter mAdapter = new NewsAdapter(yourNews);
            mRecyclerView.setAdapter(mAdapter);

        }
    }
}