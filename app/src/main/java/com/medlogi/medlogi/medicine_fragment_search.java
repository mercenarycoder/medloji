package com.medlogi.medlogi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class medicine_fragment_search extends Fragment
{
    ArrayList<medicineBaseClass>  list;
    RecyclerView recycler_medicine;
    SwipeRefreshLayout medicine_refresh;
    dashboardmainadapter adapter;
    TextView no_medicine;
    TextView highlight;
    Context context;
    ProgressDialog progressDialog;
    productCount count_pro;
    DashBoard dashBoard;
    getCategoryProducts products;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        dashBoard=new DashBoard();
        count_pro=new productCount();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.medicines_show, container, false);
        recycler_medicine=(RecyclerView)view.findViewById(R.id.recycler_medicine);
        no_medicine=(TextView)view.findViewById(R.id.no_medicine);
        medicine_refresh=(SwipeRefreshLayout)view.findViewById(R.id.medicine_refresh);
        medicine_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                medicine_refresh.setRefreshing(false);
                new getCategoryProducts().execute();
            }
        });
        SharedPreferences preferences=getActivity().getApplicationContext().
                getSharedPreferences("search_key",getActivity().
                        getApplicationContext().MODE_PRIVATE);

        String service_name=preferences.getString("value","2");

        highlight=(TextView)view.findViewById(R.id.highlight);
        highlight.setText("Search Results For : "+service_name);
        new getCategoryProducts().execute();
        return view;
    }
    boolean check_it=false;
    @Override
    public void onPause() {
        super.onPause();
        check_it=true;
    }

    @Override
    public void onResume() {
        if(check_it)
        {
            new getCategoryProducts().execute();
        }
        super.onResume();
//        new getCategoryProducts().execute();
    }

    private class getCategoryProducts extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(context);
            progressDialog.setMessage("Data Loading");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url="https://express.accountantlalaji.com/newapp/webapi/saipharma/searchproduct";
            SharedPreferences preferences=getActivity().getApplicationContext().
                    getSharedPreferences("search_key",getActivity().
                            getApplicationContext().MODE_PRIVATE);

            String id=preferences.getString("value","2");
            String data=new JsonParser().searchFunction(url,id);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s!=null)
            {
                try {
                    dashBoard.map=new HashMap<>();
                    list=new ArrayList<>();
                    JSONObject object = new JSONObject(s);
                    JSONArray arr=object.getJSONArray("product");
                    if(arr.length()==0)
                    {
                        no_medicine.setVisibility(View.VISIBLE);
                        no_medicine.setEnabled(true);
                        return;
                    }
                    for(int i=0;i<arr.length();i++)
                    {
                        JSONObject obj=arr.getJSONObject(i);
                        String url=String.valueOf(obj.get("product_image"));
                        String name=String.valueOf(obj.get("product_name"));
                        String price=String.valueOf(obj.get("product_price"));
                        String price_old=String.valueOf(obj.get("product_price_old"));
                        String description=String.valueOf(obj.get("product_des"));
                        String id=String.valueOf(obj.get("product_id"));
                        String point=String.valueOf(obj.get("product_point"));
                        String allot=String.valueOf(obj.get("allot_product_name"));
                        if(productCount.map2.containsKey(id))
                        {
                            list.add(new medicineBaseClass(id,url,name,description,allot,
                                    price,"Rs."+price
                                    ,"Rs."+price_old,String.valueOf(productCount.map2.get(id))));
                        }
                        else {
                            productCount.map2.put(id, 0);
                            productCount.map3.put(id,new cartBaseClass("a","a","a","a","a","a"));
                            list.add(new medicineBaseClass(id,url,name,description,allot,
                                    price,"Rs."+price
                                    ,"Rs."+price_old,"0"));
                        }
                    }
                    no_medicine.setVisibility(View.INVISIBLE);
                    no_medicine.setEnabled(false);
                    adapter=new dashboardmainadapter(list,context);
                    recycler_medicine.setLayoutManager(new LinearLayoutManager(context));
                    recycler_medicine.setHasFixedSize(true);
                    recycler_medicine.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Some Networking Error",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(context,"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
