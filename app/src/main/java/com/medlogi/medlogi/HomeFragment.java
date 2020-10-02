package com.medlogi.medlogi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomeFragment extends Fragment {
    AutoCompleteTextView search_key;
    ImageButton now_search;
    String search_word="";
    ArrayList<base_dashboard> list;
    dashmainadapter adapter;
    RecyclerView category_recycler;
    ProgressDialog progressDialog;
    Context context;
    SwipeRefreshLayout swipe_catgory;
    LinearLayout prescribtion_uploader;
    productGroup group;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    context=getContext();

    }
String[] fruits={"Surgrical","Medical","Injectable"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        group=new productGroup();
        category_recycler=(RecyclerView)view.findViewById(R.id.category_recycler);
        SharedPreferences preferences=context.getApplicationContext().
                getSharedPreferences("login_details",
                        context.getApplicationContext().MODE_PRIVATE);
        String str=preferences.getString("first","second");
        new getCategories().execute();
        new getProductGroups().execute();
        search_key=(AutoCompleteTextView) view.findViewById(R.id.search_key);
        //autocomplete adapter is made over here
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (context, android.R.layout.select_dialog_item, group.products);
        search_key.setThreshold(1);
        search_key.setAdapter(adapter);

        now_search=(ImageButton)view.findViewById(R.id.now_search);
        now_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_word=search_key.getText().toString();
             SharedPreferences preferences=getActivity().getApplicationContext().
                        getSharedPreferences("search_key",getActivity().
                                getApplicationContext().MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("value",search_word);
            editor.apply();
            editor.commit();
            //create a instance of new dashboard so that it return to the main screen
          new DashBoard();
            //
((FragmentActivity)context).getSupportFragmentManager().beginTransaction().
        replace(R.id.frame_base,new medicine_fragment_search()).commit();

            }
        });
        prescribtion_uploader=(LinearLayout)view.findViewById(R.id.prescribtion_uploader);
        prescribtion_uploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences=context.getApplicationContext().
                        getSharedPreferences("login_details",
                                context.getApplicationContext().MODE_PRIVATE);
                String number=preferences.getString("cmp_mobile","7389438159");
                String masg_at="+91"+number;
                String url = "https://api.whatsapp.com/send?phone="+masg_at;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        swipe_catgory=(SwipeRefreshLayout)view.findViewById(R.id.swipe_category);
        swipe_catgory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_catgory.setRefreshing(false);
                new getCategories().execute();
                new getProductGroups().execute();
            }
        });
        return view;
    }
private class  getCategories extends AsyncTask<String,String,String>
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
        String url="https://express.accountantlalaji.com/newapp/webapi/saipharma/category_list";
        String data=new JsonParser().baseGetRequest(url);
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(s!=null)
        {
            try {
                JSONObject object = new JSONObject(s);
             // System.out.println("Gand MARA "+s);
                JSONObject data=object.getJSONObject("data");
             // System.out.println("Some things ARE WORNG HERE:_ "+data);
              //JSONObject category3=data.getJSONObject("category");
              JSONArray arr=data.getJSONArray("category");
              list=new ArrayList<>();
              if(arr.length()==0)
              {
            Toast.makeText(context,"No Category in the Database",Toast.LENGTH_SHORT).show();
              }
              //  Toast.makeText(context,arr.length(),Toast.LENGTH_SHORT).show();
              for(int i=0;i<arr.length();i=i+2)
              {
                  JSONObject category = arr.getJSONObject(i);
                  String cat_id1=String.valueOf(category.get("category_id"));
                  String cat_name1=String.valueOf(category.get("category_name"));
                  String cat_image1=String.valueOf(category.get("category_image"));
               System.out.println(cat_id1+" "+cat_image1+" "+cat_name1);
                  if(i+1!=arr.length()) {
                      JSONObject category2 = arr.getJSONObject(i + 1);
                      String cat_id2 = String.valueOf(category2.get("category_id"));
                      String cat_name2 = String.valueOf(category2.get("category_name"));
                      String cat_image2 = String.valueOf(category2.get("category_image"));
     list.add(new base_dashboard(cat_id1, cat_id2, cat_name1, cat_name2, cat_image1, cat_image2));
                  }
                  else
                  {
     list.add(new base_dashboard(cat_id1, "blank", cat_name1, "blank",
             cat_image1,"blank"));
                  }
             }

            adapter=new dashmainadapter(list,context);
            category_recycler.setLayoutManager(new LinearLayoutManager(context));
            category_recycler.setHasFixedSize(true);
            category_recycler.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            Toast.makeText(context,"Some JSon error",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
        Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }
}
private class  getProductGroups extends AsyncTask<String,String,String>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
 String url="https://express.accountantlalaji.com/newapp/webapi/saipharma/listofproductgroup";
  String data=new JsonParser().baseGetRequest(url);
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        if(s!=null)
        {
    try {
        productGroup prod = new productGroup();
        JSONObject object=new JSONObject(s);
        JSONArray array=object.getJSONArray("data");
        for(int i=0;i<array.length();i++)
        {
            JSONObject obj=array.getJSONObject(i);
            String prod_name=String.valueOf(obj.get("product_group_name"));
            String id=String.valueOf(obj.get("id"));
            prod.group.put(prod_name,id);
        }
     //   Toast.makeText(context,String.valueOf(prod.group.size()),Toast.LENGTH_SHORT).show();
    }
    catch (Exception e)
    {
        e.printStackTrace();
        Toast.makeText(context,"RR",Toast.LENGTH_SHORT).show();
    }
        }
        else
        {
Toast.makeText(context,"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
        }
    }
}
}
