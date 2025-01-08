package com.example.lab8;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(getLayoutResId());

        //get the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        //drawer layout and navigation view
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate the menu
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection for toolbar icons
        if (item.getItemId() == (R.id.icon_1)) {

            Toast.makeText(this,"You clicked on item 1", Toast.LENGTH_SHORT).show();
            return true;

        }else if (item.getItemId() == (R.id.icon_2)) {

            Toast.makeText(this,"You clicked on item 2", Toast.LENGTH_SHORT).show();
            return true;

        }else {

            return super.onOptionsItemSelected(item);

        }


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        //navigation drawer item selection
        if (item.getItemId() == (R.id.nav_home)) {

            if (!this.getClass().equals(MainActivity.class)) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

        }else if (item.getItemId() == (R.id.nav_dad_joke)) {

            if (!this.getClass().equals(DadJoke.class)) {

                Intent intent = new Intent(this, DadJoke.class);
                startActivity(intent);

            }

        } else if (item.getItemId() == (R.id.nav_exit)) {

            Toast.makeText(this,"Exiting App", Toast.LENGTH_SHORT).show();
            finishAffinity();

        }

        //close the drawer after selection
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    @LayoutRes
    protected abstract int getLayoutResId();

}

