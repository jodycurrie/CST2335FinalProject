package com.example.lab8;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
/**
 * BaseActivity provides the common functionality for activities with a navigation drawer.
 * It includes toolbar setup, handling navigation items, and managing navigation to different screens.
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // DrawerLayout for the navigation drawer
    protected DrawerLayout drawerLayout;


    /**
     * Called when the activity is created. Initializes the layout, toolbar, and navigation drawer.
     * @param savedInstanceState Bundle containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Set the layout for the activity
        setContentView(getLayoutResId());

        // Initialize the toolbar and set it as the app's action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        //drawer layout and navigation view
        // Initialize the navigation drawer and its listener
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //toggle for drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    /**
     * Inflates the options menu for the activity.
     * @param menu Menu to be inflated
     * @return true if the menu was successfully inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate the menu
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;

    }

    /**
     * Handles item selection for toolbar menu items.
     * @param item Menu item selected
     * @return true if the item was handled, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection for toolbar icons
        if (item.getItemId() == (R.id.icon_1)) {
            //go to favorites activity
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);

            return true;

        }else if (item.getItemId() == (R.id.icon_2)) {

            //go to main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            return true;

        }else if (item.getItemId() == (R.id.icon_3)) {

        //go to main activity
        Intent intent = new Intent(this, SharedPref.class);
        startActivity(intent);

        return true;

    } else {

            return super.onOptionsItemSelected(item);

        }


    }

    /**
     * Handles selection of items from the navigation drawer.
     * @param item Menu item selected from the navigation drawer
     * @return true if the item was handled, false otherwise
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        //navigation drawer item selection
        if (item.getItemId() == (R.id.nav_home)) {

            if (!this.getClass().equals(MainActivity.class)) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

        } else if (item.getItemId() == (R.id.nav_favorites)) {

            if (!this.getClass().equals(FavoritesActivity.class)) {

                Intent intent = new Intent(this, FavoritesActivity.class);
                startActivity(intent);

            }
        }else if (item.getItemId() == (R.id.nav_shared_pref)) {

            if (!this.getClass().equals(SharedPref.class)) {

                Intent intent = new Intent(this, SharedPref.class);
                startActivity(intent);

            }

        }else if (item.getItemId() == (R.id.home_help)) {

            showHelpDialog(getString(R.string.home_help), getString(R.string.home_help_message));

        }else if (item.getItemId() == (R.id.favorites_help)) {


            showHelpDialog(getString(R.string.favorites_help),getString(R.string.favorites_help_message));


        }else if (item.getItemId() == (R.id.shared_pref_help)) {

            showHelpDialog(getString(R.string.last_accessed_date_help),getString(R.string.last_accessed_date_help_message));


        }else if (item.getItemId() == (R.id.nav_exit)) {

            Toast.makeText(this,getString(R.string.exit_toast), Toast.LENGTH_SHORT).show();
            finishAffinity();

        }

        //close the drawer after selection
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    /**
     * Abstract method to get the layout resource ID for the activity.
     * @return Layout resource ID for the activity
     */
    @LayoutRes
    protected abstract int getLayoutResId();

    /**
     * Sets the title of the toolbar.
     * @param title The title to be set
     */
    protected void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Displays a help dialog with the specified title and message.
     * @param title The title of the dialog
     * @param message The message to display in the dialog
     */
    private void showHelpDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }


}

