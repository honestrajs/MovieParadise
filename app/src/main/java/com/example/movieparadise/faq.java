package com.example.movieparadise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class faq extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private Handler slideHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        viewPager2=findViewById(R.id.faq_slider);
        List<slideritem> slideritems=new ArrayList<>();
        slideritems.add(new slideritem(R.drawable.image1));
        slideritems.add(new slideritem(R.drawable.image2));
        slideritems.add(new slideritem(R.drawable.image3));
        slideritems.add(new slideritem(R.drawable.image4));
        slideritems.add(new slideritem(R.drawable.image5));


        viewPager2.setAdapter(new sliderAdapter(slideritems,viewPager2));

        viewPager2.setClipToPadding(false);        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
viewPager2.setPageTransformer(compositePageTransformer);
viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        slideHandler.removeCallbacks(sliderRunnable);
        slideHandler.postDelayed(sliderRunnable,4000);

    }
});
    }

private Runnable sliderRunnable=new Runnable() {
    @Override
    public void run() {
        viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
    }
};
}