package com.wcreation.sprinklesbakery;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.StatusResponse;

public class ItemViewActivity extends AppCompatActivity {
    Toolbar toolbar;
    View headView;
    SharedPreferences sharedPreferences;
    TextView txtItemName, txtItemPrice, txtIngre, txtInformation, txtQtyChange;
    ImageView imgMain;
    AppCompatButton btnMinas, btnPlus, btnAddToCard, btnBuyIt;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    int qty = 1;
    double itemPrice;
    final static int PAYHERE_REQUEST = 10001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_item_view);

        txtItemName = findViewById(R.id.txtItemName);
        txtItemPrice = findViewById(R.id.txtItemPrice);
        imgMain = findViewById(R.id.imgMain);
        txtIngre = findViewById(R.id.txtIngre);
        txtInformation = findViewById(R.id.txtInformation);
        txtQtyChange = findViewById(R.id.txtQtyChange);
        btnMinas = findViewById(R.id.btnMines);
        btnPlus = findViewById(R.id.btnPlus);
        btnBuyIt = findViewById(R.id.btnBuyIt);



        Intent i = getIntent();
        int i_id = i.getIntExtra("itemID",0);
        String i_name = i.getStringExtra("itemName");
        Double i_price = i.getDoubleExtra("itemPrice",0);
        String i_img = i.getStringExtra("itemImage");
        int i_qty = i.getIntExtra("itemQty",0);
        String i_ing = i.getStringExtra("itemIng");
        String i_Info = i.getStringExtra("itemMsg");

        txtItemName.setText(i_name);
        txtItemPrice.setText("Rs."+String.valueOf(i_price));
        txtIngre.setText(i_ing);
        txtInformation.setText(i_Info);
        Glide.with(ItemViewActivity.this).load(i_img).into(imgMain);

        btnPlus.setOnClickListener(view -> {
            if (i_qty>0){
                if (qty>i_qty){
                    qty = qty-1;
                    txtQtyChange.setText(String.valueOf(qty));
                    itemPrice = qty * i_price;
                }else{
                    qty = qty+1;
                    txtQtyChange.setText(String.valueOf(qty));
                    itemPrice = qty * i_price;
                }
            }else{
                txtQtyChange.setText("Stock Not Available");
            }


        });


        btnMinas.setOnClickListener(view ->{
            if(qty>1){
                qty = qty-1;
                txtQtyChange.setText(String.valueOf(qty));
                itemPrice = qty * i_price;
            }else if(qty==1){
                txtQtyChange.setText(String.valueOf(qty));
                itemPrice = i_price;
            }else{
                Toast.makeText(this, "You Should Choose At Least 1 Quantity", Toast.LENGTH_SHORT).show();
            }
        });

        if (Integer.valueOf(txtQtyChange.getText().toString()) == 1){
            itemPrice = i_price;
        }


        btnBuyIt.setOnClickListener(view -> {
//            payhere(txtItemName.getText().toString(), txtQtyChange.getText().toString(), Double.valueOf(txtItemPrice.getText().toString()));


            InitRequest req = new InitRequest();
            req.setMerchantId("1217636");       // Your Merchant PayHere ID
            req.setMerchantSecret("8cNdFsWL3Uz8MQsBHw5xYb4DrvJYudoH38Qna98EIo4H"); // Your Merchant secret (Add your app at Settings > Domains & Credentials, to get this))
            req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
            req.setAmount(itemPrice);             // Final Amount to be charged
            req.setOrderId("230000123");        // Unique Reference ID
            req.setItemsDescription(txtItemName.getText().toString());  // Item description title
            req.setCustom1("Quantity : "+txtQtyChange.getText().toString());
//            req.setCustom2("This is the custom message 2");
            req.getCustomer().setFirstName("Saman");
            req.getCustomer().setLastName("Perera");
            req.getCustomer().setEmail("samanp@gmail.com");
            req.getCustomer().setPhone("+94771234567");
            req.getCustomer().getAddress().setAddress("No.1, Galle Road");
            req.getCustomer().getAddress().setCity("Colombo");
            req.getCustomer().getAddress().setCountry("Sri Lanka");

////Optional Params
//            req.getCustomer().getDeliveryAddress().setAddress("No.2, Kandy Road");
//            req.getCustomer().getDeliveryAddress().setCity("Kadawatha");
//            req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
            req.getItems().add(new Item(null, txtItemName.getText().toString(), Integer.valueOf(txtQtyChange.getText().toString()), itemPrice));

            Intent intent = new Intent(this, PHMainActivity.class);
            intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
            PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
            startActivityForResult(intent, PAYHERE_REQUEST);

        });

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userName = sharedPreferences.getString("username", "");
        String userTel = sharedPreferences.getString("usertel", "");
        int userID = sharedPreferences.getInt("userid",0);


//
//        btnAddToCard.setOnClickListener(view->{
//            addtocard(userID, i_id, Integer.valueOf(txtQtyChange.getText().toString()), itemPrice);
//        });


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
                        Toast.makeText(ItemViewActivity.this, "Home is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_search_donor:
                        Toast.makeText(ItemViewActivity.this, "Message is Clicked",Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_blog:
                        Toast.makeText(ItemViewActivity.this, "Synch is Clicked",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;

                }
                return true;
            }
        });

    }

//    private void addtocard(final int userID, final int i_id, final Integer txtQty, final double itemPrice) {
//        mRequestQueue = Volley.newRequestQueue(ItemViewActivity.this);
//
//        mStringRequest = new StringRequest(Request.Method.POST,
//                addtoCartBaseUrl(), new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    String success = jsonObject.getString("success");
//                    String message = jsonObject.getString("message");
//
//
//                    if (success.equals("1")) {
//                        Toast.makeText(ItemViewActivity.this,message,Toast.LENGTH_SHORT).show();
//                        // Finish
//                        // Start activity dashboard
////                        recreate();
////                        startActivity(new Intent(HomeActivity.this,HomeActivity.class));
//
//                    }
//                    if (success.equals("0")) {
//                        Toast.makeText(ItemViewActivity.this,message,Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } catch (JSONException e) {
//                    Toast.makeText(ItemViewActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ItemViewActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                String userIdS = String.valueOf(userID);
//                String itemIds = String.valueOf(i_id);
//                String itemQty = String.valueOf(txtQty);
//                String itemPrices = String.valueOf(itemPrice);
//
//                Map<String, String> params = new HashMap<>();
//                params.put("u_id", userIdS);
//                params.put("i_id", itemIds);
//                params.put("cart_qty", itemQty);
//                params.put("cart_price",itemPrices);
//
//
//                return params;
//            }
//        };
//
//        mStringRequest.setShouldCache(false);
//        mRequestQueue.add(mStringRequest);
//
//    }
//
//    private String addtoCartBaseUrl() {
//        return "https://dineshwayaman.com/final/addtocart.php";
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            if (resultCode == Activity.RESULT_OK) {
                String msg;
                if (response != null)
                    if (response.isSuccess())
                        msg = "Activity result:" + response.getData().toString();
                    else
                        msg = "Result:" + response.toString();
                else
                    msg = "Result: no response";
                Log.d(TAG, msg);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response != null)
                    Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "User canceled the request", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}