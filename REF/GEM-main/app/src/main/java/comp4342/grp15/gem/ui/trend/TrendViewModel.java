package comp4342.grp15.gem.ui.trend;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import comp4342.grp15.gem.model.ClientPostMeta;

public class TrendViewModel extends AndroidViewModel {
    private static RequestQueue requestQueue;
    private static MutableLiveData<ArrayList<ClientPostMeta>> mPostMetas;

    public TrendViewModel(@NonNull Application application) {
        super(application);
        requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());
    }

    public MutableLiveData<ArrayList<ClientPostMeta>> getPostMetas(){
        if(mPostMetas == null){
            mPostMetas = new MutableLiveData<>();
        }

        // get json String from server
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"https://comp4342.hjm.red/trend",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Type listType = new TypeToken<List<ClientPostMeta>>(){}.getType();
                        Gson gson = new Gson();
                        List<ClientPostMeta> lClientPostMetas = gson.fromJson(s, listType);
                        ArrayList<ClientPostMeta> aClientPostMetas = new ArrayList<>(lClientPostMetas);

                        // 先将内容更新到 mPostMetas，再返回 PostMetas
                        mPostMetas.setValue(aClientPostMetas);
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplication().getApplicationContext(), "Cannot get trends.....", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

        return mPostMetas;
    }
}