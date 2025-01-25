package com.example.lab8;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;


/**
 * A fragment that displays the details of a specific news item, with options to add or remove it from the favorites list.
 */
public class NewsDetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_LINK = "link";
    private static final String ARG_PUBDATE = "pubDate";
    private static final boolean ARG_FAVORITE = false;
    private static final Integer ARG_ID = 0;

    /**
     * Creates a new instance of NewsDetailFragment with the provided news item details.
     *
     * @param title The title of the news item
     * @param description The description of the news item
     * @param link The link to the news item
     * @param pubDate The publication date of the news item
     * @param favorite Whether the news item is a favorite
     * @param id The unique ID of the news item
     * @return A new instance of NewsDetailFragment
     */
    public static NewsDetailFragment newInstance(String title, String description, String link, String pubDate, boolean favorite,int id) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putString(ARG_LINK, link);
        args.putString(ARG_PUBDATE, pubDate);
        args.putBoolean(String.valueOf(ARG_FAVORITE), favorite);
        args.putInt(String.valueOf(ARG_ID),id);
        //args.putBoolean()


        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Inflates the layout for the fragment and retrieves the arguments passed to the fragment.
     *
     * @param inflater The LayoutInflater object used to inflate the view
     * @param container The parent view that the fragment's UI should be attached to
     * @param savedInstanceState A Bundle containing saved state (if any)
     * @return The View for the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();

        if (args != null)  {

            boolean favorite = args.getBoolean(String.valueOf(ARG_FAVORITE));
        }

        return inflater.inflate(R.layout.fragment_news_detail, container, false);

    }

    /**
     * Sets up the UI components and handles interactions for the fragment, including adding or removing the news item from favorites.
     *
     * @param view The view of the fragment
     * @param savedInstanceState A Bundle containing saved instance state (if any)
     */
     @Override
     public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        if (args != null) {

            String title = args.getString(ARG_TITLE);
            String description = args.getString(ARG_DESCRIPTION);
            String link = args.getString(ARG_LINK);
            String pubDate = args.getString(ARG_PUBDATE);
            boolean favorite = args.getBoolean(String.valueOf(ARG_FAVORITE));
            int id = args.getInt(String.valueOf(ARG_ID));

            // Set the details in the UI
            TextView titleView = view.findViewById(R.id.title);
            TextView descriptionView = view.findViewById(R.id.description);
            TextView linkView = view.findViewById(R.id.link);
            TextView pubDateView = view.findViewById(R.id.pubDate);
            Button actionButton = view.findViewById(R.id.favorite_button);

            titleView.setText(title);
            descriptionView.setText(description);
            linkView.setText(link);
            pubDateView.setText(pubDate);


            // Set action button text and functionality
            if (favorite) {

                actionButton.setText(getString(R.string.remove_from_favorites));

                actionButton.setOnClickListener(v -> {

                    removeFromFavorites(id,title);

                });

            } else {

                actionButton.setText(getString(R.string.add_to_favorites));

                actionButton.setOnClickListener(v -> {
                    saveToFavorites(title, description, link, pubDate);
                    logSavedData();

                });


            }


        }

         // Set up the close button
         Button closeButton = view.findViewById(R.id.close_button);
         closeButton.setOnClickListener(v -> {
             // Navigate back to the previous screen
             requireActivity().getSupportFragmentManager().popBackStack();
         });

    }

    /**
     * Removes the news item from the favorites list in the database.
     *
     * @param id The unique ID of the news item to be removed
     * @param title The title of the news item
     */
    private void removeFromFavorites(int id, String title) {

        SQLiteDatabase delDb = new DatabaseHelper(requireContext()).getWritableDatabase();

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        ContentValues values = new ContentValues();
        values.put("id", id);


        Snackbar.make(requireView(), getString(R.string.remove_from_favorites) + " " + title, Snackbar.LENGTH_LONG)
                .setAction("Delete", v -> {

                    int rowDel = delDb.delete("favorites", whereClause, whereArgs);
                    delDb.close();

                    if (rowDel > 0) {
                        Toast.makeText(requireContext(), getString(R.string.removed_from_favorites) + " " + title, Toast.LENGTH_SHORT).show();

                        //remove it from the listview and update view
                        if (getActivity() instanceof FavoritesActivity) {

                            FavoritesActivity activity = (FavoritesActivity) getActivity();
                            for (int i = 0; i < activity.favorites.size(); i++) {

                                if (activity.favorites.get(i).getId() == id) {

                                    activity.favorites.remove(i);
                                    activity.favAdapter.notifyDataSetChanged();
                                    break;

                                }

                            }

                        }


                    } else {

                        Toast.makeText(requireContext(), getString(R.string.error_removing_item_from_favorites), Toast.LENGTH_SHORT).show();

                    }


                })
                .show();

    }

    /**
     * Saves the news item to the favorites list in the database.
     *
     * @param title The title of the news item
     * @param description The description of the news item
     * @param link The link to the news item
     * @param pubDate The publication date of the news item
     */
    private void saveToFavorites(String title, String description, String link, String pubDate) {

        SQLiteDatabase database = new DatabaseHelper(requireContext()).getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("description",description);
        values.put("link",link);
        values.put("pubDate",pubDate);

        long newRowId = database.insert("favorites", null, values);
        if (newRowId != -1) {
            // Item saved successfully
            Toast.makeText(requireContext(), "Item saved to favorites", Toast.LENGTH_SHORT).show();
        } else {
            // Error occurred while saving the item
            Toast.makeText(requireContext(), "Error saving item to favorites", Toast.LENGTH_SHORT).show();

        }


        database.close();

    }

    /**
     * Logs the data from the 'favorites' table for debugging or auditing purposes.
     */
    private void logSavedData() {

        // Get readable database to query the data
        SQLiteDatabase db = new DatabaseHelper(requireContext()).getReadableDatabase();

        // Query all records in the 'favorites' table
        Cursor cursor = db.query("favorites", null, null, null, null, null, null);

        // Check if there are records in the table
        if (cursor != null && cursor.moveToFirst()) {

            int TitleColumnIndex = cursor.getColumnIndex("title");
            int DescriptionColumnIndex = cursor.getColumnIndex("description");
            int LinkColumnIndex = cursor.getColumnIndex("link");
            int PubDateColumnIndex = cursor.getColumnIndex("pubDate");

            if (TitleColumnIndex != -1 && DescriptionColumnIndex != -1 && LinkColumnIndex != -1) {
                // Loop through the results and log them
                do {
                    String title = cursor.getString(TitleColumnIndex);
                    String description = cursor.getString(DescriptionColumnIndex);
                    String link = cursor.getString(LinkColumnIndex);
                    String pubDate = cursor.getString(PubDateColumnIndex);

                    // Log the saved data (you can customize what you want to log)
                    Log.d("Database", "Saved Item - Title: " + title + ", Description: " + description +
                            ", Link: " + link + ", PubDate: " + pubDate);

                } while (cursor.moveToNext());
            } else {

                Log.e("Database", "Column index not found in cursor");

            }

        } else {
            Log.d("Database", "No records found in favorites");
        }
        // Close the cursor when done
        cursor.close();
        // Close the readable database
        db.close();


    }

}
