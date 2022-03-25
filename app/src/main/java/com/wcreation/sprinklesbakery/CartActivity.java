package com.wcreation.sprinklesbakery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wcreation.sprinklesbakery.Adapter.CartItemRecyclerviewAdapter;
import com.wcreation.sprinklesbakery.Model.CartItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    RecyclerView cartRecyclerview;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<CartItems> cartItems;
    TextView txtTotalVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        cartRecyclerview = findViewById(R.id.recyclerCart);
        txtTotalVal = findViewById(R.id.txtTotalVal);

        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        int userID = sharedPreferences.getInt("userid",0);


        linearLayoutManager = new LinearLayoutManager(this);
        cartRecyclerview.setLayoutManager(linearLayoutManager);

        cartItems = new ArrayList<>();

        getCartItems(userID);
    }

    private void getCartItems(final int userID) {
        requestQueue  = Volley.newRequestQueue(CartActivity.this);
        stringRequest = new StringRequest(Request.Method.POST, getBaseUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray array = new JSONArray(response);
                    for (int i=0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);

                        int cart_id = object.getInt("cart_id");
                        String item_name = object.getString("item_name");
                        String item_img = object.getString("item_img");
                        int cart_qty = object.getInt("cart_qty");
                        double cart_price = object.getDouble("cart_price");


                        CartItems cartItemList = new CartItems(cart_id,item_name,item_img,cart_qty,cart_price);
                        cartItems.add(cartItemList);

                    }
                }catch(JSONException e){
                    Toast.makeText(CartActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
                adapter = new CartItemRecyclerviewAdapter(CartActivity.this,cartItems);
                cartRecyclerview.setAdapter(adapter);
                if (adapter.getItemCount()==0){
                    Toast.makeText(CartActivity.this, "No Cart Items Found", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String userid = String.valueOf(userID);
                Map<String, String> params = new HashMap<>();
                params.put("u_id", userid);


                return params;
            }
        };

        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private String getBaseUrl() {
        return "https://dineshwayaman.com/final/getcartitem.php";
    }
}