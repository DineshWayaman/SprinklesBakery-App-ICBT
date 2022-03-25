package com.wcreation.sprinklesbakery.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wcreation.sprinklesbakery.Adapter.ItemsRecyclerViewAdapter;
import com.wcreation.sprinklesbakery.Model.Categories;
import com.wcreation.sprinklesbakery.Model.Items;
import com.wcreation.sprinklesbakery.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryFragment extends Fragment {
    SharedPreferences sharedPreferencesCat;
    TextView txtCat;
    RecyclerView recyclerItems;
    RequestQueue requestQueue;
    StringRequest stringRequest;

//    private RecyclerView catRecyclerview;
private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<Items> items;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerItems = view.findViewById(R.id.recyclerItems);

        items = new ArrayList<>();

        sharedPreferencesCat = getContext().getSharedPreferences("cat", MODE_PRIVATE);
        String catName = sharedPreferencesCat.getString("cat_name", "");

        txtCat = view.findViewById(R.id.txtCat);

        if (catName.equals("")||catName.length()==0){
            catName = "all";
        }
        txtCat.setText(catName);


        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerItems.setLayoutManager(staggeredGridLayoutManager);
//        manager = new LinearLayoutManager(view.getContext());
//        recyclerItems.setLayoutManager(manager);
//        recyclerItems.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        getCatItems(catName);


        return view;
    }

    private void getCatItems(final String catName) {
            requestQueue  = Volley.newRequestQueue(getActivity());
            stringRequest = new StringRequest(Request.Method.POST, getBaseUrl(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try{
                        JSONArray array = new JSONArray(response);
                        for (int i=0; i<array.length(); i++){
                            JSONObject object = array.getJSONObject(i);

                            int item_id = object.getInt("i_id");
                            String i_name = object.getString("i_name");
                            String i_cat = object.getString("i_cat");
                            int i_qty = object.getInt("i_qty");
                            double i_price = object.getDouble("i_price");
                            String i_ingredients = object.getString("i_ingredients");
                            String i_message = object.getString("i_message");
                            String i_img = object.getString("i_img");

                            Items itemsList = new Items(item_id,i_name,i_cat,i_qty,i_price,i_ingredients,i_message,i_img);
                            items.add(itemsList);

                        }
                    }catch(JSONException e){
                        Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                    adapter = new ItemsRecyclerViewAdapter(getActivity(),items);
                    recyclerItems.setAdapter(adapter);
                    if (adapter.getItemCount()==0){
                        Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("cat_name", catName);


                    return params;
                }
            };

            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
    }

    private String getBaseUrl() {
        return "https://dineshwayaman.com/final/getitems.php";
    }
}