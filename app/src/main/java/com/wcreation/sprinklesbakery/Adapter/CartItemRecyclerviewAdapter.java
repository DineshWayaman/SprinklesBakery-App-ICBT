package com.wcreation.sprinklesbakery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wcreation.sprinklesbakery.Model.CartItems;
import com.wcreation.sprinklesbakery.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartItemRecyclerviewAdapter extends RecyclerView.Adapter<CartItemRecyclerviewAdapter.MyViewHolder>{
    private Context context;
    double totalValue;
    private List<CartItems> cartItems = new ArrayList<>();

    public CartItemRecyclerviewAdapter(Context mContext, List<CartItems> cartItemsList){
        this.context = mContext;
        this.cartItems = cartItemsList;
    }

    @NonNull
    @Override
    public CartItemRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemRecyclerviewAdapter.MyViewHolder holder, int position) {
        final CartItems cart = cartItems.get(position);
        holder.txtItemName.setText(cart.getItemName());
        holder.txtItemQtyPrice.setText("Qty: "+cart.getQty()+"   Rs."+cart.getItemPrice());

        Glide.with(context).load(cart.getItem_img()).into(holder.imgItem);



        holder.checkItem.setOnClickListener(view -> {
            totalValue = totalValue+ cart.getItemPrice();
//            Toast.makeText(context, String.valueOf(totalValue), Toast.LENGTH_SHORT).show();
//            txtTotalVal.setText(String.valueOf(totalValue));
        });



//        btnPayNow.setOnClickListener(view -> {
//            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
//        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtDelete, txtItemName, txtItemQtyPrice;
        CircleImageView imgItem;
        CheckBox checkItem;
        LinearLayout linearLayoutCart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDelete = itemView.findViewById(R.id.txtDeleteCart);
            txtItemName = itemView.findViewById(R.id.txtCartItemName);
            txtItemQtyPrice = itemView.findViewById(R.id.txtCartQtyPrice);
            imgItem = itemView.findViewById(R.id.imgCItem);
            checkItem = itemView.findViewById(R.id.checkItemCart);
            linearLayoutCart = itemView.findViewById(R.id.linearCart);



        }
    }
}
