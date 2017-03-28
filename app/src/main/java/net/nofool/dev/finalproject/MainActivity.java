package net.nofool.dev.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView searchTV;
    final static String MYB = "net.nofool.dev.finalproject.main";
    final String TAG = MainActivity.class.getSimpleName();
    String[] name = new String[3];
    String[] symbol = new String[3];
    Intent intent = new Intent(MYB);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchTV = (AutoCompleteTextView)findViewById(R.id.searchBar);

        searchTV.addTextChangedListener(textWatcher);

        searchTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("sym", symbol[position]);
                sendBroadcast(intent);
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            OkHttpClient client = new OkHttpClient();
            String text = searchTV.getText().toString();
            String url = "http://chstocksearch.herokuapp.com/api/" + text;
            if (text != "" && text != null) {
                Request request = new Request.Builder().url(url).get().build();
                Call call = client.newCall(request);
                Log.v(TAG, url);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String jsonData = response.body().string();
                            Log.v(TAG, jsonData);
                            if (response.isSuccessful()) {
                                JSONArray array = new JSONArray(jsonData);
                                for (int i = 0; i < array.length(); i++) {
                                    if (i < 3) {
                                        JSONObject jsonObject = array.getJSONObject(i);
                                        String n = jsonObject.getString("company");
                                        Log.v(TAG, n);
                                        String symbolOfComp = jsonObject.getString("symbol");
                                        name[i] = n + "     " + symbolOfComp;
                                        symbol[i]=symbolOfComp;
                                    }
                                }


                            }
                        } catch (Exception e) {
                            Log.v(TAG, e.getLocalizedMessage());
                        }
                        final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, name);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchTV.setAdapter(adapter);
                                searchTV.setThreshold(1);
                            }
                        });
                    }
                });

            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
}
