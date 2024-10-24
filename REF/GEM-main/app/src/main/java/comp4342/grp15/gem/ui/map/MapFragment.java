package comp4342.grp15.gem.ui.map;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import comp4342.grp15.gem.MainActivity;
import comp4342.grp15.gem.databinding.FragmentMapBinding;

public class MapFragment extends Fragment {

    private FragmentMapBinding binding;
    private MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 获取mainActivity
        mainActivity = (MainActivity) getActivity();
        String locationX = "114.191958";
        String locationY = "22.307471";

        // 获取地理位置
        Cursor cursor = ((MainActivity) getActivity()).getReadableDatabase().rawQuery("select * from user where id == 1", null);
        while (cursor.moveToNext()) {
            if (Objects.equals(cursor.getString(4), "null") || Objects.equals(cursor.getString(5), "null")) {
                // 无定位
                Toast.makeText(mainActivity, "Please open location service!", Toast.LENGTH_SHORT).show();
            } else {
                // 有定位
                locationX = cursor.getString(4);
                locationY = cursor.getString(5);
            }
            cursor.close();
        }

        // 加载 webview
        final WebView webView = binding.mapWebview;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(webView.getSettings().LOAD_NO_CACHE);//设置去缓存，防止加载的为上一次加载过的
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView.loadUrl("http://comp4342.hjm.red/map?X=" + locationX + "&Y=" + locationY);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}