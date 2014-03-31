package com.teusoft.spytour.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.teusoft.spytour.R;
import com.teusoft.spytour.loader.ImageLoader;
import com.teusoft.spytour.views.ScaleImageView;

public class StaggeredAdapter extends ArrayAdapter<String> {
    private ImageLoader mLoader;
    private Context context;


    public StaggeredAdapter(Context context, int textViewResourceId,
                            String[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        mLoader = new ImageLoader(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(getContext());
            convertView = layoutInflator.inflate(R.layout.row_staggered_demo,
                    null);
            holder = new ViewHolder();
            holder.imageView = (ScaleImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        mLoader.DisplayImage(getItem(position), holder.imageView);
        return convertView;
    }

    static class ViewHolder {
        ScaleImageView imageView;
    }
}
