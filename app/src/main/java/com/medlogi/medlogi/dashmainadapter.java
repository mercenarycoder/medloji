package com.medlogi.medlogi;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.medlogi.medlogi.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class dashmainadapter  extends RecyclerView.Adapter<dashmainadapter.viewholder1>{
    ArrayList<base_dashboard> list;
    Context context;
    SharedPreferences medi_pref;
    SharedPreferences.Editor editor;
    public dashmainadapter(ArrayList<base_dashboard> list, Context context)
    {
        this.list=list;
        this.context=context;
    }
    @NonNull
    @Override
    public viewholder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        View inflator=LayoutInflater.from(context).inflate(R.layout.main_recycler_layout, parent,
                false);
        viewholder1 viewhold=new viewholder1(inflator);
        return viewhold;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder1 holder, final int position) {
        final base_dashboard adapter=list.get(position);
      medi_pref=context.getApplicationContext().
              getSharedPreferences("service_id",context.getApplicationContext().MODE_PRIVATE);
      editor=medi_pref.edit();
        if(!adapter.getCat_img1().isEmpty()) {
          Picasso.with(context)
                  .load(adapter.getCat_img1())
                  .placeholder(R.drawable.ic_noimage)
                  .error(R.drawable.ic_noimage)
                  .into(holder.category1);
      }
      if(!adapter.getCat_img2().isEmpty()) {
          Picasso.with(context)
                  .load(adapter.getCat_img2())
                  .placeholder(R.drawable.ic_noimage)
                  .error(R.drawable.ic_noimage)
                  .into(holder.category2);
      }
      if(adapter.getCat_id2().equals("blank"))
      {
       holder.blank_catch.setVisibility(View.INVISIBLE);
       holder.blank_catch.setEnabled(false);
      }
        holder.category_name1.setText(adapter.getCat_name1());
        holder.category_name2.setText(adapter.getCat_name2());
      holder.category1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              new DashBoard();
 ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_base,new medicine_fragment()).commit();
      editor.putString("id_service",adapter.getCat_id1());
      editor.putString("name_service",adapter.getCat_name1());
      editor.apply();
          }
      });
      holder.category_name1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              new DashBoard();
((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_base,new medicine_fragment()).commit();
              editor.putString("id_service",adapter.getCat_id1());
              editor.putString("name_service",adapter.getCat_name1());
              editor.apply();

          }
      });
      if(holder.blank_catch.isEnabled())
      {
          holder.category2.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
  ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_base,new medicine_fragment()).commit();
                  editor.putString("id_service",adapter.getCat_id2());
                  editor.putString("name_service",adapter.getCat_name2());
                  editor.apply();

              }
          });
          holder.category_name2.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
 ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_base,new medicine_fragment()).commit();
                  editor.putString("id_service",adapter.getCat_id2());
                  editor.putString("name_service",adapter.getCat_name2());
                  editor.apply();
              }
          });
      }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class viewholder1 extends RecyclerView.ViewHolder
    {
        ImageButton category1,category2;
        LinearLayout blank_catch;
        TextView category_name1,category_name2;
        public viewholder1(@NonNull View itemView) {
            super(itemView);
        category1=(ImageButton)itemView.findViewById(R.id.category1);
        category2=(ImageButton)itemView.findViewById(R.id.category2);
        category_name1=(TextView)itemView.findViewById(R.id.category_name1);
        category_name2=(TextView)itemView.findViewById(R.id.category_name2);
        blank_catch=(LinearLayout)itemView.findViewById(R.id.blank_catch);
        }
    }
}
