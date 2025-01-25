package com.example.lab8;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;



import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;

/**
 * Main activity that handles displaying news headlines from an RSS feed and managing the last accessed time.
 * It also allows users to view news details in a fragment when an item is clicked.
 */
public class MainActivity extends BaseActivity {

    // SharedPreferences file and key for storing the last accessed timestamp
    private static final String PREFS_NAME = "NewsAppPrefsFile";
    private static final String KEY_LAST_ACCESSED = "last_accessed";

    private ListView listView;
    private ArrayAdapter<NewsItem> adapter;
    private ArrayList<NewsItem> headlines = new ArrayList<NewsItem>();


    /**
     * Called when the activity is created. Initializes the UI, sets up the list view to display news items,
     * and retrieves the news headlines using an AsyncTask.
     *
     * @param savedInstanceState A bundle containing the activity's previously saved state, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Set toolbar title
        setToolbarTitle(getString(R.string.bbc_news_feed_v1_0));

        //initialize listview and adapter
        listView = findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, headlines);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Save and display last accessed time
        saveLastAccessedTime();

        //get headlines from BBC using AsyncTask
        new FetchHeadLinesTask().execute("https://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");

        //listener to display news item details in fragment
        listView.setOnItemClickListener((parent, view, position, id) -> {
            NewsItem selectedItem = headlines.get(position);

            // Display fragment with selected item's details
            NewsDetailFragment fragment = NewsDetailFragment.newInstance(
                    selectedItem.getTitle(),
                    selectedItem.getDescription(),
                    selectedItem.getLink(),
                    selectedItem.getPubDate(),
                    selectedItem.getFavorite(),
                    selectedItem.getId()
            );

            FrameLayout frameLayout = findViewById(R.id.fragment_container);
            frameLayout.setVisibility(FrameLayout.VISIBLE);

            FragmentManager fragmentManager = getSupportFragmentManager();

            // Hide fragment when backstack changes
            fragmentManager.addOnBackStackChangedListener(() -> {

                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

                    frameLayout.setVisibility(FrameLayout.GONE);

                }

            });

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        });

    }

    /**
     * Saves the current date and time as the last accessed time in SharedPreferences.
     */
    private void saveLastAccessedTime() {

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        editor.putString(KEY_LAST_ACCESSED, currentDateTime);
        editor.apply();

    }

    /**
     * Returns the layout resource ID for this activity.
     *
     * @return The layout resource ID.
     */
    @Override
    protected int getLayoutResId() {

        return R.layout.activity_main;

    }

    /**
     * AsyncTask to fetch news headlines from the provided RSS feed URL.
     * Parses the RSS feed and returns a list of NewsItem objects.
     */
    private class FetchHeadLinesTask extends AsyncTask<String, Void, ArrayList<NewsItem>> {


        /**
         * Performs the background task of fetching and parsing the RSS feed.
         *
         * @param urls The URLs to fetch the RSS feed from.
         * @return A list of parsed news items.
         */
        @Override
        protected ArrayList<NewsItem> doInBackground(String... urls) {

            ArrayList<NewsItem> result = new ArrayList<>();

            try {
                //setup connection to rss feed
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // Check the response code
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Connection successful
                    Log.d("ConnectionTest", "Connection successful! Response code: " + responseCode);
                } else {
                    // Connection failed
                    Log.e("ConnectionTest", "Connection failed! Response code: " + responseCode);
                }

                InputStream inputStream = connection.getInputStream();
                //setup xml parser
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(inputStream, null);


                int eventType = parser.getEventType();
                boolean insideItem = false;
                String title = null, description = null, link = null, pubDate = null;

                //process xml file/rss feed
                while (eventType != XmlPullParser.END_DOCUMENT) {

                        String tagName = parser.getName();

                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if ("item".equals(tagName)) {
                                    insideItem = true;
                                } else if (insideItem) {

                                    if ("title".equals(tagName)) {
                                        title = parser.nextText();
                                    } else if ("description".equals(tagName)) {
                                        description = parser.nextText();
                                    } else if ("link".equals(tagName)) {
                                        link = parser.nextText();
                                    } else if ("pubDate".equals(tagName)) {
                                        pubDate = parser.nextText();
                                    }

                                }
                                break;

                            case XmlPullParser.END_TAG:
                                if ("item".equals(tagName)) {

                                    insideItem = false;

                                    if (title != null && description != null && link != null) {

                                        //instantiate and add news item to arraylist
                                        boolean favorite = false;
                                        int id = 0;
                                        result.add(new NewsItem(title,description,link,pubDate,favorite,id));

                                    }

                                    title = null;
                                    description = null;
                                    link = null;

                                }
                                    break;

                    }

                    eventType = parser.next();
                }
                inputStream.close();

            } catch (Exception e) {

                e.printStackTrace();

            }
            return result;
        }

        /**
         * Updates the UI with the fetched news headlines after the background task is complete.
         *
         * @param result The list of fetched news items.
         */
        @Override
        protected void onPostExecute(ArrayList<NewsItem> result) {
            if (result != null && !result.isEmpty()) {
                headlines.clear();
                headlines.addAll(result);
                adapter.notifyDataSetChanged();
            }

        }

    }

}


