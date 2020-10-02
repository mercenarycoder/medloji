package com.medlogi.medlogi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class medicine_fragment extends Fragment
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
                getSharedPreferences("service_id",getActivity().
                        getApplicationContext().MODE_PRIVATE);

        String service_name=preferences.getString("name_service","2");

        highlight=(TextView)view.findViewById(R.id.highlight);
        highlight.setText(service_name);
    new getCategoryProducts().execute();
    return view;
    }
boolean check_it=false;
    @Override
    public void onPause() {
        super.onPause();
        check_it=true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onResume() {
        if(check_it)
        {
            SharedPreferences preferences=context.getApplicationContext().
                    getSharedPreferences("group_info",context.getApplicationContext().MODE_PRIVATE);
            SharedPreferences preferences1=context.getSharedPreferences("direct_buy",
                    context.getApplicationContext().MODE_PRIVATE);
            if(preferences.getString("bool","null").equals("true"))
            {
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_base,new medicine_fragment2()).commit();
            }
            else if(preferences1.getString("direct","dsd").equals("doit"))
            {
                dashBoard.fab.setVisibility(View.INVISIBLE);
                dashBoard.fab.setEnabled(false);
                Editor editor = preferences1.edit();
                editor.putString("direct","donotdoit");
                editor.apply();
                editor.commit();
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_base,new cart_fragment()).commit();
            }
            else {
                new getCategoryProducts().execute();
            }
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
            String url="https://express.accountantlalaji.com/newapp/webapi/saipharma/category_with_product";
            SharedPreferences preferences=getActivity().getApplicationContext().
                    getSharedPreferences("service_id",getActivity().
                            getApplicationContext().MODE_PRIVATE);

            String id=preferences.getString("id_service","2");
            String data=new JsonParser().getProducts(url,id);
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
                    JSONArray arr=object.getJSONArray("data");
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
                        double pri=Double.parseDouble(price);
                        if(productCount.map2.get(id)>0) {
                            pri = (Double) pri * (productCount.map2.get(id));
                            price = String.valueOf(pri);
                            if (price.length() > 5) {
                                price = price.substring(0, 5);
                            }
                        }
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
                    Collections.sort(list, new Comparator<medicineBaseClass>() {
                        @Override
                        public int compare(medicineBaseClass o1, medicineBaseClass o2) {
                            return o1.medicine.compareTo(o2.medicine);
                        }
                    });
                    no_medicine.setVisibility(View.INVISIBLE);
                    no_medicine.setEnabled(false);
                    adapter=new dashboardmainadapter(list,context);
                    recycler_medicine.setLayoutManager(new LinearLayoutManager(context));
                    recycler_medicine.setHasFixedSize(true);
                    recycler_medicine.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
              Toast.makeText(context,"No Products Inside This",Toast.LENGTH_SHORT).show();
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
