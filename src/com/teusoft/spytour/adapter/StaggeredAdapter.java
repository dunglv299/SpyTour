package com.teusoft.spytour.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.teusoft.spytour.R;
import com.teusoft.spytour.entity.Tour;
import com.teusoft.spytour.views.ScaleImageView;

import java.util.List;

public class StaggeredAdapter extends BaseAdapter {
    private ImageLoader mLoader;
    private Context context;
    private List<Tour> listTour;
    private DisplayImageOptions options;

    public StaggeredAdapter(Context context,
                            List<Tour> listTour) {
        this.context = context;
        this.listTour = listTour;

//        mLoader = new ImageLoader(context);
        mLoader = ImageLoader.getInstance();
        mLoader.init(ImageLoaderConfiguration.createDefault(context));

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public int getCount() {
        return listTour.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(context);
            convertView = layoutInflator.inflate(R.layout.row_staggered_demo,
                    null);
            holder = new ViewHolder();
            holder.imageView = (ScaleImageView) convertView.findViewById(R.id.imageView1);
            holder.descriptionTv = (TextView) convertView.findViewById(R.id.description_tv);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        Tour tour = listTour.get(position);
        // Bind data to view
//        mLoader.DisplayImage(tour.getImageUrl(), holder.imageView);
        mLoader.displayImage(tour.getImageUrl(), holder.imageView, options);
        holder.descriptionTv.setText(tour.getDescription());
        return convertView;
    }

    static class ViewHolder {
        ScaleImageView imageView;
        TextView descriptionTv;
    }
}
