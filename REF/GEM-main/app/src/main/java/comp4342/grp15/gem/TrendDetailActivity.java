package comp4342.grp15.gem;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

public class TrendDetailActivity extends AppCompatActivity {

    private TextView location;
    private RequestQueue requestQueue;
    String locationX;
    String locationY;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_detail);
        TextView posterName = (TextView) findViewById(R.id.trend_detail_poster);
        location = (TextView) findViewById(R.id.trend_detail_location);
        TextView post_time =(TextView) findViewById(R.id.trend_detail_post_time);
        TextView message = (TextView)findViewById(R.id.trend_detail_message);
        ImageView image = (ImageView) findViewById(R.id.trend_detail_image);

        // 创建网络队列
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        location.setText("Location: " + getIntent().getStringExtra("location"));
        Glide.with(getBaseContext()).load("https://comp4342.hjm.red/public/picture/" + getIntent().getStringExtra("pic_name")).into(image);
        post_time.setText("Post time: " + getIntent().getStringExtra("post_time"));
        message.setText("Message: " + getIntent().getStringExtra("message"));
        posterName.setText("Poster: " + getIntent().getStringExtra("username"));
        parseLocation();

    }

    private void parseLocation(){
        String point = getIntent().getStringExtra("location");
        String[] locationArr = point.split(",");
        if(locationArr.length ==2){
            locationX = locationArr[0].substring(1);
            locationY = locationArr[1];
            locationY = locationY.substring(0,locationY.length()-2);
        }else {
            return;
        }

        // get txt String from server
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"https://comp4342.hjm.red/loc?X=" + locationX + "&Y=" + locationY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(!s.equals("Unknown")){
                            location.setText("Location: " + s);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplication().getApplicationContext(), "Networking Error", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

    }

    public void onBackPressed() {
        this.finish();//左滑退出activity
    }

}