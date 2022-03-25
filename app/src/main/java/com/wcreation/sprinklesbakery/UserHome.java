package com.wcreation.sprinklesbakery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.wcreation.sprinklesbakery.Adapter.CategoryRecyclerAdapter;
import com.wcreation.sprinklesbakery.Fragments.CategoryFragment;
import com.wcreation.sprinklesbakery.Model.Categories;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.beans.IndexedPropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

public class UserHome extends AppCompatActivity {

    Toolbar toolbar;
    View headView;
    SharedPreferences sharedPreferences;
    RequestQueue requestQueue;
    StringRequest stringRequest;

    private RecyclerView catRecyclerview;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter adapter;
    private List<Categories> categories;


//    https://www.youtube.com/watch?v=aBSFWh2fw00
//    PHP MYSQL GET POST DATA
//    https://www.youtube.com/channel/UCeObq0MQ8O2noCehlN5B0rg

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_user_home);

        catRecyclerview = findViewById(R.id.recyclerCategory);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        catRecyclerview.setLayoutManager(linearLayoutManager);

        categories = new ArrayList<>();

        getCatogories();

        reloadFragment(new CategoryFragment());

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


    private void reloadFragment(Fragment fragment) {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.categoriesFrames, fragment);
        fragmentTransaction.commit();
    }


    private void getCatogories() {
        stringRequest = new StringRequest(Request.Method.GET, baseUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try {
                        JSONArray array  = new JSONArray(response);
                        for (int i=0; i< array.length(); i++){
                            JSONObject object = array.getJSONObject(i);

                            int id = object.getInt("c_id");
                            String c_name = object.getString("c_name");
                            String c_desc = object.getString("c_desc");

                            Categories categoriesModel = new Categories(id, c_name, c_desc);
                            categories.add(categoriesModel);
                            
                        }

                    }catch (Exception e){
                        Toast.makeText(UserHome.this,e.toString(),Toast.LENGTH_SHORT).show();
                    }
                    
                    adapter = new CategoryRecyclerAdapter(UserHome.this, categories);
                    catRecyclerview.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserHome.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        Volley.newRequestQueue(UserHome.this).add(stringRequest);
    }

    private String baseUrl() {
        return "https://dineshwayaman.com/final/getallcat.php";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart:
                Intent i = new Intent(UserHome.this, CartActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}