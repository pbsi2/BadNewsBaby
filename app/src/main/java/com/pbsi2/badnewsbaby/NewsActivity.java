package com.pbsi2.badnewsbaby;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class NewsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String TAG = NewsActivity.class.getSimpleName();
    private ArrayList<BadNews> yourNews = new ArrayList<BadNews>();
    private SwipeRefreshLayout swipeContainer;


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
                        String wtitle = c.getString("webTitle");
                        String section = c.getString("sectionName");
                        String link = c.getString("webUrl");
                        String type = c.getString("type");
                        String date = c.getString("webPublicationDate");
                        String author = "N/A";

                        // Author is ginven in TAGS: node in JSON Object
                        JSONArray tags = c.getJSONArray("tags");
                        if (tags.length() > 0) {
                            JSONObject d = tags.getJSONObject(0);
                            JSONArray authorxx = d.names();
                            if (authorxx != null) {
                                int dddd = authorxx.length();
                                if (authorxx.toString().contains("\"firstName\"")) {
                                    author = d.optString("firstName", "N/A");
                                }
                                if (authorxx.toString().contains("\"lastName\"")) {
                                    author += " " + d.optString("lastName", " ");
                                }

                            }
                        }
                        yourNews.add(new BadNews(wtitle, section, link, author, type, date));
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