package net.nofool.dev.finalproject;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.design.widget.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class fragmentforlist extends ListFragment {

    public final static String EXTRA_TITLE = "net.nofool.dev.finalproject.name";
    public final static String EXTRA_MESSAGE ="net.nofool.dev.finalproject.message";
    public final static String EXTRA_ID ="net.nofool.dev.finalproject.id";
    public final static String EXTRA_CATEGORY= "net.nofool.dev.finalproject.category";
    final String PREFS = "net.nofool.dev.finalproject";
    final String fileName = "stocks.json";
    static String bol="";
    static String n="";
    ListItemsAdapter adapter;
    ArrayList<String> symbols = new ArrayList<>();
    final String TAG = fragmentforlist.class.getSimpleName();
    static ArrayList<ListCompanies> comp = new ArrayList<>();
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Log.v(TAG, "Created");
        getActivity().registerReceiver(receiver, new IntentFilter(MainActivity.MYB));
       /* // Create Array of Strings
        String [] items = new String[]{"Company 1","Company 2", "Company 3", "Company 4", "Company 5"};

      // Add List

        // gets Activity     //Android ready to use row    //choose array
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,items);

        //Add Adapter
        setListAdapter(adapter);
        */



        //Create Array
        SharedPreferences first = getActivity().getSharedPreferences(PREFS, 0);
        if (first.getBoolean("first_time", true)){
            Log.v(TAG, "First");
           first.edit().putBoolean("first_time",false);
            symbols.add("AAPL");
            symbols.add("RHT");
            symbols.add("MSFT");
            symbols.add("IBM");
            jsonWrite(symbols);
        }



        Log.v(TAG, "READ: " +read());
        symbols = read();
//        Log.v(TAG, symbols.toString());
        getArray();

/*
        ArrayList<ListCompanies> c5 = new ArrayList<>();

        c5 = new ArrayList<ListCompanies>();
        c5.add(new ListCompanies("Apple","APPLE","The Company Apple", ListCompanies.Category.UpTrend));
        c5.add(new ListCompanies("RedHat","RH","The Company Apple", ListCompanies.Category.DownTrend));
        c5.add(new ListCompanies("IBM","IBM","The Company Apple", ListCompanies.Category.NoTrend));
        c5.add(new ListCompanies("Microsoft","MC","The Company Microsoft", ListCompanies.Category.DownTrend));
*/

        while (comp.size()!=symbols.size()){
            //do nothing
            try{
                wait(3000);
            }catch (Exception e){}
        }

        adapter = new ListItemsAdapter(getActivity(), comp);
        setListAdapter(adapter);
    }
    private void setAd(){
      // adapter = new ListItemsAdapter(getActivity(), comp);
        //adapter.clear();
        adapter.notifyDataSetChanged();
      //  setListAdapter(adapter);
    }

    private void getArray() {
        for (String temp : symbols) {
            OkHttpClient client = new OkHttpClient();
            final String text = temp;
            String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20%28%22" + text + "%22%29&env=store://datatables.org/alltableswithkeys&format=json";
            if (text != "" && text != null) {
                Request request = new Request.Builder().url(url).get().build();
                Call call = client.newCall(request);
                Log.v(TAG, "getArray");
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) { Log.v(TAG, e.getLocalizedMessage());}

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String jsonData = response.body().string();
                            Log.v(TAG, "This is JSONDATA from getArray: "+jsonData);
                            if (response.isSuccessful()){
                                JSONObject jsonObject = new JSONObject(jsonData);
                                JSONObject layer2 = jsonObject.getJSONObject("query");
                                JSONObject layer3 = layer2.getJSONObject("results");
                                JSONObject layer4 = layer3.getJSONObject("quote");

                                ListCompanies.Category cat;
                                if (Double.parseDouble(layer4.getString("Change"))<0.0){
                                    cat = ListCompanies.Category.DownTrend;
                                } else if (Double.parseDouble(layer4.getString("Change"))>0.0){
                                    cat = ListCompanies.Category.UpTrend;
                                } else {
                                    cat = ListCompanies.Category.NoTrend;
                                }
                                String tempName = getName(text);
                                String tempSym = text;
                                String tempMess = layer4.getString("Change");
                                comp.add(new ListCompanies(tempName,tempSym,tempMess,cat));
                            }
                        } catch (Exception e){
                            Log.v(TAG, e.getLocalizedMessage());
                        }
                    }
                });
            }
        }
    }

    private String getName(String s){
        n="";
        bol = " ";
        OkHttpClient client = new OkHttpClient();
        String text = s;
        String url = "http://chstocksearch.herokuapp.com/api/" + text;
        if (text != "" && text != null) {
            Request request = new Request.Builder().url(url).get().build();
            Call call = client.newCall(request);
            Log.v(TAG, url);
            n="";
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, "JSONDATA FROM GETNAME"+jsonData);
                        if (response.isSuccessful()) {
                            JSONArray array = new JSONArray(jsonData);
                            for (int i = 0; i < array.length(); i++) {
                                if (i < 3) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    n = jsonObject.getString("company");
                                    bol = jsonObject.getString("symbol");

                                }
                            }


                        }
                    } catch (Exception e) {
                        Log.v(TAG, e.getLocalizedMessage());
                    }
                }
            });
        }

        while (!(s.equalsIgnoreCase(bol))){

        }
        return n;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(getActivity(), SelectedItem.class);
        ListCompanies item = (ListCompanies) getListAdapter().getItem(position);

        i.putExtra(EXTRA_TITLE, item.getName());
        i.putExtra(EXTRA_MESSAGE, item.getMessage());
        i.putExtra(EXTRA_ID, item.getID());
        i.putExtra(EXTRA_CATEGORY, item.getCategory().toString());
        startActivity(i);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String sym = intent.getStringExtra("sym");
            Log.v(TAG, "Receiver: "+sym);
            symbols.add(sym);
            jsonWrite(symbols);
            Log.v(TAG, "RECIEVER 2: " + read());
            symbols=read();
            comp.clear();
            getArray();
            while(comp.size()!=symbols.size()){
                //Do Nothing
                try{
                    wait(3000);
                }catch(Exception e){}
            }
            setAd();
        }
    };

    private ArrayList<String> read(){
       List<String> temp = Arrays.asList(readList().substring(1,readList().length()-1).split(", "));
        ArrayList<String> tobe = new ArrayList<>(temp);
        return tobe;
    }

    private String readList(){
        try{
            File f = new File(getActivity().getFilesDir().getPath()+"/"+fileName);
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        }catch(Exception e){
            return null;
        }
    }

    private void appendFile(String s){
        try{
            FileWriter file = new FileWriter(getActivity().getFilesDir().getPath()+"/"+fileName);
            file.write(s);
            file.flush();
            file.close();
        }catch(Exception e){}
    }

    private void jsonWrite(ArrayList<String> s){
        appendFile(s.toString());
        Log.v(TAG, s.toString());
    }
}
