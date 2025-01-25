package com.example.lab8;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * FavoritesActivity is responsible for displaying and managing the user's favorite news items.
 * It loads the list of favorites from a database, allows users to view details of each favorite,
 * and provides an option to remove favorites.
 */
public class FavoritesActivity extends BaseActivity {


    private ListView favListView;
    public ArrayAdapter<NewsItem> favAdapter;
    public ArrayList<NewsItem> favorites = new ArrayList<NewsItem>();

    /**
     * Initializes the activity and sets up the favorites list view.
     * Also, loads the list of favorite news items from the database.
     *
     * @param savedInstanceState the saved instance state from a previous activity lifecycle, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //set toolbar title
        setToolbarTitle(getString(R.string.favorites));

        // Initialize the list view and adapter
        favListView = findViewById(R.id.fav_list_view);
        favAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favorites);
        favListView.setAdapter(favAdapter);

        //load favorites from database
        loadFavorites();
        //update view
        favAdapter.notifyDataSetChanged();

        //listener to display fragment with option to remove item from favorites
        favListView.setOnItemClickListener((parent, view, position, id) -> {

            NewsItem selectedItem = favorites.get(position);

            // create a fragment with selected item's details
            NewsDetailFragment fragment = NewsDetailFragment.newInstance(
                    selectedItem.getTitle(),
                    selectedItem.getDescription(),
                    selectedItem.getLink(),
                    selectedItem.getPubDate(),
                    selectedItem.getFavorite(),
                    selectedItem.getId()
            );

            // Make the fragment container visible
            FrameLayout frameLayout = findViewById(R.id.fav_fragment_container);
            frameLayout.setVisibility(FrameLayout.VISIBLE);

            FragmentManager fragmentManager = getSupportFragmentManager();

            // Set a listener to hide the fragment container when the back stack is empty
            fragmentManager.addOnBackStackChangedListener(() -> {

                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

                    frameLayout.setVisibility(FrameLayout.GONE);

                }

            });

            //begin fragment transaction
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fav_fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();



        });


    }

    /**
     * Loads the list of favorite news items from the database.
     * It queries the database for all records in the 'favorites' table and adds them
     * to the 'favorites' list.
     */
    private void loadFavorites() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        //open db for reading
        db = new DatabaseHelper(this).getReadableDatabase();

        //get all favorites from database
        cursor = db.query("favorites", null, null, null, null, null, null);

        favorites.clear();

        // Check if there are records in the table
        if (cursor != null && cursor.moveToFirst()) {

            int TitleColumnIndex = cursor.getColumnIndex("title");
            int DescriptionColumnIndex = cursor.getColumnIndex("description");
            int LinkColumnIndex = cursor.getColumnIndex("link");
            int PubDateColumnIndex = cursor.getColumnIndex("pubDate");
            int IdColumnIndex = cursor.getColumnIndex("id");

            if (TitleColumnIndex != -1 && DescriptionColumnIndex != -1 && LinkColumnIndex != -1) {
                // Loop through the results and log them
                do {
                    String title = cursor.getString(TitleColumnIndex);
                    String description = cursor.getString(DescriptionColumnIndex);
                    String link = cursor.getString(LinkColumnIndex);
                    String pubDate = cursor.getString(PubDateColumnIndex);
                    int id = cursor.getInt(IdColumnIndex);
                    boolean favorite = true;

                    //add to favorites list
                    favorites.add(new NewsItem(title,description,link,pubDate,favorite,id));


                } while (cursor.moveToNext());
            } else {

                Log.e("Database", "Column index not found in cursor");

            }

        } else {
            //show a toast if no records found.
            Toast.makeText(this, "No records found in favorites", Toast.LENGTH_SHORT).show();

        }

        cursor.close();
        db.close();


    }


    /**
     * Provides the layout resource ID for the FavoritesActivity.
     * This method is used to inflate the activity's layout.
     *
     * @return The layout resource ID for this activity
     */
    @Override
    protected int getLayoutResId() {

        return R.layout.activity_favorites;

    }
}