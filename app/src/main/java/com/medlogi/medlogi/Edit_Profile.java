package com.medlogi.medlogi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Edit_Profile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 2;
    EditText edit_name,edit_mobile,edit_address;
Spinner city_chooser;
SpinnerAdapter2 adapter2;
ArrayList<SpinnerClass> categories;
ProgressDialog progressDialog;
ImageView choose_profile_pic;
Bitmap img=null;
ImageButton go_back;
String convertImage="";
HashMap<String, Integer> city_map;
Button initiate_choose,update_profile;
boolean check_that=false;
String name="",address="",landmark="",city="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_edit__profile);
        edit_name=(EditText)findViewById(R.id.edit_name);
        edit_mobile=(EditText)findViewById(R.id.edit_mobile);
        edit_address=(EditText)findViewById(R.id.edit_address);
        city_chooser=(Spinner)findViewById(R.id.company_spinner);
       go_back=(ImageButton)findViewById(R.id.go_back);
       go_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
        initiate_choose=(Button)findViewById(R.id.initiate_choose);
       choose_profile_pic=(ImageView)findViewById(R.id.choose_profile_pic);
       update_profile=(Button)findViewById(R.id.update_profile);
       initiate_choose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openFileChooser();
           }
       });
       update_profile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
     name=edit_name.getText().toString();
     address=edit_address.getText().toString();
     landmark=edit_mobile.getText().toString();
     if(name.equals("")||address.equals(""))
     {
         AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Profile.this);
         builder.setTitle("Information")
                 .setMessage("Please Enter your name and adress they are mandatory")
                 .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {

                     }
                 });
         builder.create();
         builder.show();
     }
     else
     {
         if(convertImage.equals("")&&check_that)
         {
             Bitmap bitmap=((BitmapDrawable)choose_profile_pic.getDrawable()).getBitmap();
             img=bitmap;
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             img.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
             byte[] byteArray = byteArrayOutputStream.toByteArray();
             convertImage= Base64.encodeToString(byteArray,Base64.DEFAULT);
         }
         new updateProfile().execute();
     }
           }
       });
        new fromInformation2().execute();
        new getSetting().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            Uri imageuri = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(Edit_Profile.this.getContentResolver(), imageuri);
                //imagepro.setImageBitmap(bitmap);
                img=bitmap;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
                choose_profile_pic.setImageBitmap(img);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                convertImage= Base64.encodeToString(byteArray,Base64.DEFAULT);
                byte [] imageAsBytes=Base64.decode(convertImage.getBytes(),Base64.DEFAULT);
                //imageView_pic.setImageURI(imageuri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private class fromInformation2 extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(Edit_Profile.this);
            progressDialog.setMessage("Data Loading");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            super.onPreExecute();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection httpURLConnection = null;
            try
            {
                URL url2 = new URL("https://express.accountantlalaji.com/newapp/webapi/Orchidvendor/city");
                httpURLConnection=(HttpURLConnection)url2.openConnection();
                httpURLConnection.setRequestMethod("GET");
                String current="";
                InputStream ir=httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(ir);
                int data = inputStreamReader.read();
                while (data != -1) {
                    current += (char) data;
                    data = inputStreamReader.read();
                    System.out.print(current);
                }
                JSONObject object=new JSONObject(current);
                categories=new ArrayList<>();
                JSONArray jsonArray=object.getJSONArray("data");
                //categories.add(new SpinnerClass("krishna","City"));
                city_map=new HashMap<>();
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject object1 = jsonArray.getJSONObject(i);
                    categories.add(new SpinnerClass(String.valueOf(object1.get("id")),
                            String.valueOf(object1.get("name"))));
                    city_map.put(String.valueOf(object1.get("name")),i);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if(httpURLConnection!=null)
                {
                    httpURLConnection.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressDialog.dismiss();
            adapter2=new SpinnerAdapter2(Edit_Profile.this,categories);
            city_chooser.setAdapter(adapter2);
       city_chooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              SpinnerClass city3=categories.get(position);
              city=city3.getName();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
        }
    }
    String id;
    private class getSetting extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String url="https://express.accountantlalaji.com/newapp/webapi/saipharma/getprofile";
            SharedPreferences preferences=getApplicationContext().
                    getSharedPreferences("login_details",
                            getApplicationContext().MODE_PRIVATE);

            id=preferences.getString("auth_key","2");
            String data=new JsonParser().getProfile(url,id);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s!=null)
            {
                try {
                    JSONObject object=new JSONObject(s);
                    JSONObject data =object.getJSONObject("data");
                    String customer_name=String.valueOf(data.get("customer_name"));
                    String customer_mobile=String.valueOf(data.get("customer_mobile"));
                    //id=String.valueOf(data.get("id"));
                    String address=String.valueOf(data.get("address"));
                    String city=String.valueOf(data.get("city"));
                    String landmark=String.valueOf(data.get("landmark"));
                    String image_url=String.valueOf(data.get("photo"));
                    edit_name.setText(customer_name);
                    edit_mobile.setText(landmark);
                    edit_address.setText(address);
                    if(!city.equals("")) {
                        int pos = city_map.get(city);
                        city_chooser.setSelection(pos);
                    }
                    if(!image_url.equals(""))
                    {
                        check_that=true;
                        Picasso.with(Edit_Profile.this).load(image_url)
                                .placeholder(R.drawable.user_55)
                                .into(choose_profile_pic);
                    }
                    } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Edit_Profile.this,"JSON Problem",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(Edit_Profile.this,"Internet Problem",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class updateProfile extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url="https://express.accountantlalaji.com/newapp/webapi/saipharma/update_profile";
            SharedPreferences preferences=getApplicationContext().
                    getSharedPreferences("login_details",
                            getApplicationContext().MODE_PRIVATE);

            id=preferences.getString("auth_key","2");
            String data=new JsonParser().updateProfile(url,id,name,address,landmark,convertImage,city);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s!=null)
            {
            try
            {
            JSONObject object = new JSONObject(s);
            int status=object.getInt("status");
            if(status==1)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Profile.this);
                builder.setTitle("Information")
                        .setMessage(String.valueOf(object.get("msg")))
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            }
                        });
                builder.create();
                builder.show();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Profile.this);
                builder.setTitle("Warning !!!")
                        .setMessage(String.valueOf(object.get("msg")))
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create();
                builder.show();
            }
            }
            catch (Exception e)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Profile.this);
                builder.setTitle("Information")
                        .setMessage("Please click on update again")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create();
                builder.show();
            }
            }
            else
            {
       Toast.makeText(Edit_Profile.this,"Please check your internet coneection",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
