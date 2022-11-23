package com.kartik.ether;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView tv_from, tv_to, tv_blockNumber, tv_tokenName, tv_value, tv_TokenSymbol, tv_timestamp, tv_hash;
    Button button;
    EditText et_address;
    String URL = "https://api.etherscan.io/api?module=account&action=tokentx&contractaddress=0x9f8f72aa9304c8b593d555f12ef6589cc3a579a2&address=0x4e83362442b8d1bec281594cea3050c8eb01311c&page=1&offset=100&startblock=0&endblock=27025780&sort=asc&apikey=YFXTWXKYM4V1XXB8ZUQE13VV2JXNJJVKD3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_from = findViewById(R.id.tvFrom);
        tv_to = findViewById(R.id.tvTo);
        tv_blockNumber = findViewById(R.id.tvBlockNumber);
        tv_tokenName = findViewById(R.id.tvTokenname);
        tv_value = findViewById(R.id.tv_value);
        tv_TokenSymbol = findViewById(R.id.tv_tokenSymbol);
        tv_timestamp = findViewById(R.id.tv_Timestamp);
        tv_hash = findViewById(R.id.tv_Hash);
        button = findViewById(R.id.button);
        et_address = findViewById(R.id.et_Add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

    private void getData() {
        String addr = et_address.getText().toString();
        String API_KEY = "YFXTWXKYM4V1XXB8ZUQE13VV2JXNJJVKD3";
        String url = "https://api.etherscan.io/api?module=account&action=tokentx&address="+addr+"&page=1&offset=1&startblock=0&endblock=27025780&sort=desc&apikey="+API_KEY;
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("result");

                    for (int i=0;i<1;i++){
                        JSONObject res = jsonArray.getJSONObject(i);
                        String from = res.getString("from");
                        String to = res.getString("to");
                        String blockNumber = res.getString("blockNumber");
                        String token_Name = res.getString("tokenName");
                        String value = res.getString("value");
                        double val = Integer.parseInt(value);
                        val = val * Math.pow(10, 18);
                        String v = Double.toString(val);

                        String tokenSymbol = res.getString("tokenSymbol");
                        long timestamp = res.getLong("timeStamp");
//                        Date timeD = new Date(timestamp * 1000);
//                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//                        String Time = sdf.format(timeD);
                        String time = timestampToDate(timestamp, "YYYY MM DD - hh:mm:ss");




                        String hash = res.getString("hash");
                        tv_from.append(" "+from);
                        tv_to.append(" "+to);
                        tv_blockNumber.append(" "+blockNumber);
                        tv_tokenName.append(" "+token_Name);
                        tv_value.append(" "+v);
                        tv_TokenSymbol.append(" "+tokenSymbol);
                        tv_timestamp.append(" "+ time);
                        tv_hash.append(" "+hash);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }
    String timestampToDate(long timestamp, String pattern)
    {
        Date timeD = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(timeD);
    }
}