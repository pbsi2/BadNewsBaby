package com.pbsi2.badnewsbaby;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsActivity extends AppCompatActivity implements NewsAdapter.ClickAdapterListener {

    RecyclerView mRecyclerView;
    NewsAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<BadNews> yourNews = new ArrayList<BadNews>();
    FloatingActionButton fab;
    private GetNews getnews;
    boolean asyncdone = false;
    private String TAG = NewsActivity.class.getSimpleName();
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar nTopToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main);
        nTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(nTopToolbar);

        mRecyclerView = findViewById(R.id.newsview);
        mRecyclerView.setHasFixedSize(true);
        // For LINEAR
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        // For a GRID
        //mLayoutManager = new GridLayoutManager(this,5);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        actionModeCallback = new ActionModeCallback();
        getnews = new GetNews(mRecyclerView,this);
        getnews.execute();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setVisibility(View.GONE);
                getnews.execute();

            }
        });
    }

    @Override
    public void onRowClicked(int position) {
        enableActionMode(position);
    }

    @Override
    public void onRowLongClicked(int position) {
        enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {

        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
            actionMode = null;
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private void selectAll() {
        mAdapter.selectAll();
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }

        actionMode = null;
    }

    private void deleteRows() {
        actionMode = null;
    }

    private void updateColoredRows() {

        actionMode = null;
    }

    private class GetNews extends AsyncTask<Object, Object, Object> {
        private String TAG = NewsActivity.class.getSimpleName();
        private NewsAdapter.ClickAdapterListener listener;
        private RecyclerView view;
        public GetNews(RecyclerView view, NewsAdapter.ClickAdapterListener listener) {
            this.view = view;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Object... params) {
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
                        yourNews.add(new BadNews(wtitle, section, link, author, type, date, false));
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
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            NewsAdapter mAdapter = new NewsAdapter(this.view.getContext(),yourNews,this.listener);
            mRecyclerView.setAdapter(mAdapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Toast.makeText(NewsActivity.this, "News are refreshing", Toast.LENGTH_LONG).show();
            getnews = new GetNews(mRecyclerView, this);
            getnews.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_news, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Log.d("Clicked Action", "here");
            switch (item.getItemId()) {


                case R.id.action_delete:
                    // delete all the selected rows
                    deleteRows();
                    mode.finish();
                    return true;

                case R.id.action_color:
                    updateColoredRows();
                    mode.finish();
                    return true;

                case R.id.action_select_all:
                    selectAll();
                    return true;

                case R.id.action_refresh:
                    Toast.makeText(getApplicationContext(),
                            "Refresh has been clicked",
                            Toast.LENGTH_LONG).show();
                    getnews.execute();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
        }
    }
}