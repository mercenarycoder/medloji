package com.medlogi.medlogi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class OfferFragment extends Fragment {
    Context context;
    ArrayList<base_dashboard> list;
    RecyclerView recyclerView;
    AutoCompleteTextView searchOffer;
    ProgressDialog progressDialog;
    SwipeRefreshLayout refreshLayout;
    dashmainadapter2 adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    context=getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offer_ui, container, false);
        list=new ArrayList<>();
        String base_offer="https://bradenkelley.com/wp-content/uploads/2017/04/Special-Offer.png";
        list.add(new base_dashboard("11","22","25% off of First Order","25% off of First Order",base_offer,base_offer));
        list.add(new base_dashboard("11","22","25% off of First Order","25% off of First Order",base_offer,base_offer));
        list.add(new base_dashboard("11","22","25% off of First Order","25% off of First Order",base_offer,base_offer));
        list.add(new base_dashboard("11","22","25% off of First Order","25% off of First Order",base_offer,base_offer));
        recyclerView=(RecyclerView)view.findViewById(R.id.offer_recycler);
        refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_offer);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter=new dashmainadapter2(list,context);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });
        return view;
    }
}
