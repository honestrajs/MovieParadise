package com.example.movieparadise.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.movieparadise.Model.SliderSide;
import com.example.movieparadise.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SliderPagerAdapterNew extends PagerAdapter {

    private Context context;
    List<SliderSide> mList;

    public SliderPagerAdapterNew(Context context, List<SliderSide> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayout=inflater.inflate(R.layout.slide_item,null);
        ImageView slideImage=slideLayout.findViewById(R.id.slide_img);
        TextView slideTitle=slideLayout.findViewById(R.id.slide_title);

        FloatingActionButton floatingActionButton=slideLayout.findViewById(R.id.floating_action_button);
        Glide.with(context).load(mList.get(position).getThumbnail_URL()).into(slideImage);

        slideTitle.setText(mList.get(position).getMovie_name());
             //   +"\n"+mList.get(position).getMovie_description()

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // playing video
            }
        });

container.addView(slideLayout);
return slideLayout;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);

    }
}
