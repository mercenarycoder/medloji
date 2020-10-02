package com.medlogi.medlogi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Medicine_show_fragment extends Fragment
{
    ImageView medicine_image2;
    TextView medicine_name2,medicine_strip2,medicine_description2,medicine_count2,medicine_price2
            ,medicine_price_old2;
    ImageButton medicine_add2,medicine_minus2,go_back2,goback_medicine;
    String product_id="";
    ProgressDialog progressDialog;
    Context context;
    productGroup group;
    productCount productcount;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    context=getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.medicineshower_box, container, false);

     return view;
    }
}
