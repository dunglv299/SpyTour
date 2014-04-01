package com.teusoft.spytour.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.teusoft.spytour.R;
import com.teusoft.spytour.entity.Tour;
import com.teusoft.spytour.util.Constant;

/**
 * Created by DungLV on 1/4/2014.
 */
public class DetailActivity extends BaseActivity implements View.OnClickListener {
    private Tour tour;
    TextView tourName;
    ImageView tourImg;
    TextView tourDes;
    ImageLoader imageLoader;
    private DisplayImageOptions options;
    Button bookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        init();
    }

    public void initView() {
        setContentView(R.layout.activity_detail_tour);
        tourName = (TextView) findViewById(R.id.tour_name);
        tourImg = (ImageView) findViewById(R.id.tour_img);
        tourDes = (TextView) findViewById(R.id.tour_des);
        bookBtn = (Button) findViewById(R.id.book_btn);
        bookBtn.setOnClickListener(this);
    }

    private void init() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        if (getIntent().hasExtra(Constant.TOUR)) {
            tour = (Tour) getIntent().getExtras().get("tour");
            tourName.setText(tour.getTourName());
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.ARGB_8888)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .build();
            imageLoader.displayImage(tour.getImageUrl(), tourImg, options);
            tourDes.setText(tour.getDescription());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.book_btn:
                Intent i = new Intent(this, BookActivity.class);
                i.putExtra("tour", tour);
                startActivity(i);
                break;
        }
    }
}
