package com.medlogi.medlogi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.medlogi.medlogi.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class cartAdapter  extends RecyclerView.Adapter<cartAdapter.viewholder1>{
    ArrayList<cartBaseClass> list;
    SharedPreferences quantity_pref;
    SharedPreferences.Editor editor;
    productCount dashBoard;
    Context context;
    public cartAdapter(ArrayList<cartBaseClass> list, Context context)
    {
        this.list=list;
        this.context=context;
        setProgress();
        dashBoard=new productCount();
    }
    @NonNull
    @Override
    public viewholder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        View inflator=LayoutInflater.from(context).inflate(R.layout.cart_item, parent,
                false);
        viewholder1 viewhold=new viewholder1(inflator);
        return viewhold;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder1 holder, final int position) {
        final cartBaseClass adapter=list.get(position);
        holder.medicine_cart.setText(adapter.getMedicine());
        holder.price_cart.setText(adapter.getPrice());
        holder.price_old_cart.setText(adapter.getPrice_old());
        holder.medicine_quantity_cart.setText(adapter.getQty());
        if(!adapter.getImg().equals(""))
        {
            Picasso.with(context).load(adapter.getImg())
                    .placeholder(R.drawable.ic_medicine)
                    .into(holder.medicine_image_cart);
        }
        holder.medicine_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quant=holder.medicine_quantity_cart.getText().toString();
                int quant2=Integer.parseInt(quant);
                quant2++;
                String get_price=adapter.getPrice();
                if(get_price.startsWith("Rs."))
                {
                    get_price=get_price.substring(3,get_price.length());
                }
                double price_1=Double.parseDouble(get_price);
                price_1=price_1*quant2;
                String price_change=String.valueOf(price_1);
                holder.price_cart.setText("Rs."+price_change);
                dashBoard.map2.containsKey(adapter.id);
                {
                    dashBoard.map2.remove(adapter.id);
                    dashBoard.map3.remove(adapter.id);
                    dashBoard.map2.put(adapter.id,quant2);
                    dashBoard.map3.put(adapter.id,new cartBaseClass(adapter.getImg(),adapter.getMedicine(),
                            adapter.id,String.valueOf(quant2),adapter.getPrice(),adapter.getPrice_old()));
                }
                quant=String.valueOf(quant2);
                holder.medicine_quantity_cart.setText(quant);
            }
        });
        holder.medicine_minus_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quant=holder.medicine_quantity_cart.getText().toString();
                int quant2=Integer.parseInt(quant);
                if(quant2==0)
                {
                    Toast.makeText(context,"Quantity Cannot Be Negative",Toast.LENGTH_SHORT).show();
                    return;
                }
                quant2--;
                String get_price=adapter.getPrice();
                if(get_price.startsWith("Rs."))
                {
                    get_price=get_price.substring(3,get_price.length());
                }
                double price_1=Double.parseDouble(get_price);
               if(quant2>0) {
                   price_1 = price_1 * quant2;
               }
                String price_change=String.valueOf(price_1);
                holder.price_cart.setText("Rs."+price_change);
                dashBoard.map2.containsKey(adapter.id);
                {
                    dashBoard.map2.remove(adapter.id);
                    dashBoard.map3.remove(adapter.id);
                    dashBoard.map2.put(adapter.id,quant2);
                    dashBoard.map3.put(adapter.id,new cartBaseClass(adapter.getImg(),adapter.getMedicine(),
                            adapter.id,String.valueOf(quant2),adapter.getPrice(),adapter.getPrice_old()));
                }
                quant=String.valueOf(quant2);
                holder.medicine_quantity_cart.setText(quant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class viewholder1 extends RecyclerView.ViewHolder
    {
        TextView medicine_cart,qty_cart,price_cart,price_old_cart,medicine_quantity_cart;
        ImageView medicine_image_cart;
        ImageButton medicine_add_cart,medicine_minus_cart;
        public viewholder1(@NonNull View itemView) {
            super(itemView);
            medicine_cart=(TextView)itemView.findViewById(R.id.medicine_cart);
            price_cart=(TextView)itemView.findViewById(R.id.rate_cart);
            price_old_cart=(TextView)itemView.findViewById(R.id.rate_old_cart);
           medicine_quantity_cart=(TextView)itemView.findViewById(R.id.medicine_quantity_cart);
           medicine_add_cart=(ImageButton)itemView.findViewById(R.id.medicine_add_cart);
           medicine_minus_cart=(ImageButton)itemView.findViewById(R.id.medicine_minus_cart);
            medicine_image_cart=(ImageView)itemView.findViewById(R.id.medicine_image_cart);
        }
    }
    public void setProgress()
    {
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Data Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }
    ProgressDialog progressDialog;
}
