package com.wcreation.sprinklesbakery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

public class UserHome extends AppCompatActivity {

    Toolbar toolbar;
    View headView;
    SharedPreferences sharedPreferences;

//    https://www.youtube.com/watch?v=aBSFWh2fw00
//    PHP MYSQL GET POST DATA
//    https://www.youtube.com/channel/UCeObq0MQ8O2noCehlN5B0rg

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_user_home);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userName = sharedPreferences.getString("username", "");
        String userTel = sharedPreferences.getString("usertel", "");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });



        headView = navigationView.inflateHeaderView(R.layout.main_nav_header);
        TextView txtUName = (TextView)headView.findViewById(R.id.name);
        TextView txtUTel = (TextView)headView.findViewById(R.id.phone);
        txtUName.setText(userName);
        txtUTel.setText(userTel);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {

                    case R.id.nav_home:
                        Toast.makeText(UserHome.this, "Home is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_search_donor:
                        Toast.makeText(UserHome.this, "Message is Clicked",Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_blog:
                        Toast.makeText(UserHome.this, "Synch is Clicked",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;

                }
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}