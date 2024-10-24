package comp4342.grp15.gem.ui.trend;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import comp4342.grp15.gem.model.ClientPostMeta;
import comp4342.grp15.gem.R;

public class TrendListAdapter extends BaseAdapter{
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final ArrayList<ClientPostMeta> postMetas;

    public TrendListAdapter(Context context, ArrayList<ClientPostMeta> postMetas){
        this.mContext = context;
        this.postMetas = postMetas;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return postMetas.size();
    }

    @Override
    public Object getItem(int position) {
        return postMetas.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //如果视图为空
        if (convertView == null){
            //此处需要导入包，填写ListView的图标和标题等控件的来源，来自于layout_list_item这个布局文件
            convertView = mLayoutInflater.inflate(R.layout.trend_listview_item,null);
            //生成一个ViewHolder的对象
            holder = new ViewHolder();
            //把layout_list_item对象转移过来，以便修改和赋值
            holder.imageView = (ImageView) convertView.findViewById(R.id.IV_list_Id);
            holder.tvTitle= (TextView) convertView.findViewById(R.id.TV_listTitle_Id);
            holder.tvTime = (TextView) convertView.findViewById(R.id.TV_listTime_Id);
            holder.tvContext= (TextView) convertView.findViewById(R.id.TV_listContext_Id);
            //把这个holder传递进去
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //给控件赋值
        ClientPostMeta clientPostMeta = postMetas.get(position);

        holder.tvTitle.setText("" + clientPostMeta.getUsername());
        holder.tvTime.setText("" + clientPostMeta.getPost_time());

        int lengthMess = Math.min(80, clientPostMeta.getMessage().length());
        lengthMess = Math.max(1,lengthMess);
        holder.tvContext.setText("" + clientPostMeta.getMessage().substring(0, lengthMess-1));

        Glide.with(mContext).load("https://comp4342.hjm.red/public/thumbnail/" + clientPostMeta.getPic_name()).into(holder.imageView);
        return convertView;

    }

    static class ViewHolder{
        public ImageView imageView;
        public TextView tvTitle,tvTime,tvContext;
    }
}


