package com.wcreation.sprinklesbakery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wcreation.sprinklesbakery.ItemViewActivity;
import com.wcreation.sprinklesbakery.Model.Categories;
import com.wcreation.sprinklesbakery.Model.Items;
import com.wcreation.sprinklesbakery.R;

import java.util.ArrayList;
import java.util.List;

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.MyViewHolder>{
    private Context context;
    private List<Items> items = new ArrayList<>();

    public ItemsRecyclerViewAdapter(Context mContext, List<Items> itemsList){
        this.context = mContext;
        this.items = itemsList;
    }

    @NonNull
    @Override
    public ItemsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ItemsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsRecyclerViewAdapter.MyViewHolder holder, int position) {
            final Items itemsNew = items.get(position);

            holder.txtName.setText(itemsNew.getI_name());
        Glide.with(context).load(itemsNew.getI_img()).into(holder.imgItem);

        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ItemViewActivity.class);
                i.putExtra("itemID", itemsNew.getI_id());
                i.putExtra("itemName", itemsNew.getI_name());
                i.putExtra("itemPrice", itemsNew.getI_price());
                i.putExtra("itemImage", itemsNew.getI_img());
                i.putExtra("itemQty", itemsNew.getI_qty());
                i.putExtra("itemIng", itemsNew.getI_ingredients());
                i.putExtra("itemMsg", itemsNew.getI_message());

                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       private TextView txtName, txtPrice;
       private ImageView imgItem;
       private RelativeLayout layoutMain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtItemname);
            txtPrice = itemView.findViewById(R.id.txtItemPrice);
            imgItem = itemView.findViewById(R.id.imgItem);
            layoutMain = itemView.findViewById(R.id.layoutItem);

        }
    }
}
