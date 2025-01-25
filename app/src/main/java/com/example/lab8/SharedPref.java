package com.example.lab8;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

/**
        * Activity that demonstrates the usage of SharedPreferences for saving and retrieving data.
        * In this example, it retrieves the last accessed time from SharedPreferences and displays it.
 */
public class SharedPref extends BaseActivity {

    private static final String PREFS_NAME = "NewsAppPrefsFile";
    private static final String KEY_LAST_ACCESSED = "last_accessed";


    /**
     * Called when the activity is created. Initializes the UI, retrieves last accessed time from SharedPreferences,
     * and displays it.
     *
     * @param savedInstanceState A bundle containing the activity's previously saved state, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        setToolbarTitle("Shared Pref Example");

        //get last accessed shared preferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lastAccessed = prefs.getString(KEY_LAST_ACCESSED, "No data found");

        //display last accessed time
        TextView lastAccessedTextView = findViewById(R.id.lastAccessedTextView);
        lastAccessedTextView.setText(lastAccessed);

    }

    /**
     * Returns the layout resource ID for the SharedPref activity.
     *
     * @return The layout resource ID.
     */
    @Override
    protected int getLayoutResId() {

        return R.layout.activity_shared_pref;

    }

}