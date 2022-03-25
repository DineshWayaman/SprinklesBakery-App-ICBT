package com.wcreation.sprinklesbakery.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.wcreation.sprinklesbakery.Fragments.CategoryFragment;
import com.wcreation.sprinklesbakery.Model.Categories;
import com.wcreation.sprinklesbakery.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<Categories> categories = new ArrayList<>();

    public CategoryRecyclerAdapter(Context mContext, List<Categories> categoriesList){
        this.context = mContext;
        this.categories = categoriesList;
    }

    @NonNull
    @Override
    public CategoryRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerAdapter.MyViewHolder holder, int position) {
        final Categories categorie = categories.get(position);
        holder.txtCatName.setText(categorie.getCat_name());

        holder.linearCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferencesCat;
                sharedPreferencesCat = context.getSharedPreferences("cat", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferencesCat.edit();
                editor.putString("cat_name", categorie.getCat_name());
                editor.commit();


                reloadFragment(new CategoryFragment());

                Toast.makeText(context, categorie.getCat_name(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void reloadFragment(Fragment fragment) {
        FragmentManager fragmentManager =  ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.categoriesFrames, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtCatName;
        private LinearLayout linearCat;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCatName = itemView.findViewById(R.id.txtCatName);
            linearCat = itemView.findViewById(R.id.linearCat);
        }
    }
}
