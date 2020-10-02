package com.medlogi.medlogi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.medlogi.medlogi.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class dashboardmainadapter  extends RecyclerView.Adapter<dashboardmainadapter.viewholder1>{
    ArrayList<medicineBaseClass> list;
    SharedPreferences quantity_pref;
    SharedPreferences.Editor editor;
    productCount dashBoard;
    Context context;
    public dashboardmainadapter(ArrayList<medicineBaseClass> list, Context context)
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
        View inflator=LayoutInflater.from(context).inflate(R.layout.recycler_medicine, parent,
                false);
        viewholder1 viewhold=new viewholder1(inflator);
     return viewhold;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder1 holder, final int position) {
        quantity_pref=context.getApplicationContext().
                getSharedPreferences("quantity_details",context.getApplicationContext().MODE_PRIVATE);
        editor=quantity_pref.edit();
        final medicineBaseClass adapter=list.get(position);
        holder.medicine_price.setText(adapter.getNew_rate());
        if(adapter.getOld_rate().length()>4) {
            holder.medicine_price_old.setText(adapter.getOld_rate());
        }
        else
        {
            holder.modify_1.setVisibility(View.INVISIBLE);
        }
        holder.medicine_name.setText(adapter.getMedicine());
        holder.medicine_decription.setText(adapter.getDescription());
        holder.medicine_quantity.setText(adapter.getQuantity());
        holder.medicine_strip.setText(adapter.getStrip());
        holder.medicine_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           Intent intent=new Intent(context,Medicine_Show_Activity.class);
           intent.putExtra("product_id",adapter.id);
           context.startActivity(intent);
            }
        });
        if(!adapter.getImage_url().equals("")) {
            Picasso.with(context).load(adapter.getImage_url())
                    .error(R.drawable.ic_medicine)
                    .into(holder.medicine_url);
        }
        holder.medicine_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          String quant=holder.medicine_quantity.getText().toString();
          int quant2=Integer.parseInt(quant);
          quant2++;
                String get_price=adapter.getNew_rate();
                if(get_price.startsWith("Rs."))
                {
                    get_price=get_price.substring(3,get_price.length());
                }
                double price_1=Double.parseDouble(get_price);
                price_1=price_1*quant2;
                String price_change=String.valueOf(price_1);
                holder.medicine_price.setText("Rs."+price_change);

                dashBoard.map2.containsKey(adapter.id);
                {
                    dashBoard.map2.remove(adapter.id);
                    dashBoard.map3.remove(adapter.id);
                    dashBoard.map2.put(adapter.id,quant2);
                    dashBoard.map3.put(adapter.id,new cartBaseClass(adapter.getImage_url(),
                            adapter.getMedicine(),
                            adapter.id,String.valueOf(quant2),adapter.getNew_rate(),adapter.getOld_rate()));
                }
          quant=String.valueOf(quant2);
          holder.medicine_quantity.setText(quant);
            }
        });
        holder.medicine_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quant=holder.medicine_quantity.getText().toString();
                String price_change;
                int quant2=Integer.parseInt(quant);
                if(quant2==0)
                {
                    Toast.makeText(context,"Quantity Cannot Be Negative",Toast.LENGTH_SHORT).show();
                    return;
                }
                quant2--;
                if(quant2>0) {
                    String get_price = adapter.getNew_rate();
                    if (get_price.startsWith("Rs.")) {
                        get_price = get_price.substring(3, get_price.length());
                    }
                    double price_1 = Double.parseDouble(get_price);
                    price_1 = price_1 * quant2;
                    price_change = String.valueOf(price_1);
                }
                else
                {
                    price_change=adapter.price_2;
                }
                holder.medicine_price.setText("Rs."+price_change);
                dashBoard.map2.containsKey(adapter.id);
                {
                    dashBoard.map2.remove(adapter.id);
                    dashBoard.map3.remove(adapter.id);
                    dashBoard.map2.put(adapter.id,quant2);
                    dashBoard.map3.put(adapter.id,new cartBaseClass(adapter.getImage_url(),adapter.getMedicine(),
                            adapter.id,String.valueOf(quant2),adapter.getNew_rate(),adapter.getOld_rate()));
                }
                quant=String.valueOf(quant2);
                holder.medicine_quantity.setText(quant);
            }
        });
        }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class viewholder1 extends RecyclerView.ViewHolder
    {
        LinearLayout medicine_info;
        ImageView medicine_url;
        TextView medicine_name,medicine_decription,medicine_strip;
        TextView medicine_quantity,medicine_price,medicine_price_old;
        RelativeLayout modify_1;
        ImageButton medicine_add,medicine_minus;
        public viewholder1(@NonNull View itemView) {
            super(itemView);
            medicine_info=(LinearLayout)itemView.findViewById(R.id.medicine_info);
            medicine_url=(ImageView)itemView.findViewById(R.id.medicine_image);
            medicine_name=(TextView)itemView.findViewById(R.id.medicine_name);
            medicine_decription=(TextView)itemView.findViewById(R.id.medicine_decription);
            medicine_strip=(TextView)itemView.findViewById(R.id.medicine_strip);
            medicine_quantity=(TextView) itemView.findViewById(R.id.medicine_quantity);
            medicine_price=(TextView)itemView.findViewById(R.id.medicine_price);
            modify_1=(RelativeLayout)itemView.findViewById(R.id.modify_1);
            medicine_price_old=(TextView)itemView.findViewById(R.id.medicine_price_old);
            medicine_add=(ImageButton)itemView.findViewById(R.id.medicine_add);
            medicine_minus=(ImageButton)itemView.findViewById(R.id.medicine_minus);
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
