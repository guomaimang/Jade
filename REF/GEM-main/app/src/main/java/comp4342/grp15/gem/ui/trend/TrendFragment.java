package comp4342.grp15.gem.ui.trend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import comp4342.grp15.gem.TrendDetailActivity;
import comp4342.grp15.gem.model.ClientPostMeta;
import comp4342.grp15.gem.databinding.FragmentTrendBinding;

public class TrendFragment extends Fragment {

    private FragmentTrendBinding binding;
    private ArrayList<ClientPostMeta> postMetaList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TrendViewModel trendViewModel =
                new ViewModelProvider(this).get(TrendViewModel.class);

        binding = FragmentTrendBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView listView= binding.trendListview;

        //Activity成为此LiveData的观察者，LiveData能够监听到Activity的生命周期
        trendViewModel.getPostMetas().observe(getActivity(), new Observer<ArrayList<ClientPostMeta>>() {
            @Override
            public void onChanged(ArrayList<ClientPostMeta> postMetas) {
                listView.setAdapter(new TrendListAdapter(getActivity(), postMetas));
                postMetaList = postMetas;
            }
        });

        // click监听器
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TrendDetailActivity.class);
                ClientPostMeta clientPostMeta = postMetaList.get(position);
                intent.putExtra("username",clientPostMeta.getUsername());
                intent.putExtra("pic_name",clientPostMeta.getPic_name());
                intent.putExtra("post_time",clientPostMeta.getPost_time());
                intent.putExtra("message",clientPostMeta.getMessage());
                intent.putExtra("location",clientPostMeta.getLocation());
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}