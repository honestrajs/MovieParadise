package com.example.movieparadise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.movieparadise.Adapter.MovieShowAdapter;
import com.example.movieparadise.Adapter.SliderPagerAdapterNew;
import com.example.movieparadise.Model.GetMovieDetails;
import com.example.movieparadise.Model.MovieItemClickListnerNew;
import com.example.movieparadise.Model.SliderSide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,MovieItemClickListnerNew{
    long backPressedTime;
    Toast backToast;
    EditText SearchBar;
    ImageView navigation_button;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView logout,Search;



    MovieShowAdapter movieShowAdapter;
    DatabaseReference databaseReference;
     List<GetMovieDetails> uploads,uploads_list_latest,uploads_list_popular;
     List<GetMovieDetails> action_movies,sports_movies,comedy_movies,romantic_movies,thriller_movies;
    ViewPager sliderPage;
     List<SliderSide> uploads_slide;
    TabLayout indicator,tabmoviesactions;
    RecyclerView Movies_best_RV,Movies_latest_RV,tab;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        navigation_button=findViewById(R.id.navi);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        logout=findViewById(R.id.logout);
        SearchBar=findViewById(R.id.Search_bar);
        Search=findViewById(R.id.Search_button);
        swipeRefreshLayout=findViewById(R.id.refresh);
        navigationView.setNavigationItemSelectedListener(this);
       progressDialog=new ProgressDialog(homepage.this);



Search.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        progressDialog.show();
        String Movie_Name=SearchBar.getEditableText().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("Movies");
        Query query=databaseReference.orderByChild("movie_name").equalTo(Movie_Name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    String movie=""+ds.child("movie_name").getValue();
                    String comedian=""+ds.child("comedian").getValue();
                    String director=""+ds.child("director").getValue();
                    String hero=""+ds.child("hero").getValue();
                    String heroine=""+ds.child("heroine").getValue();
                    String movie_desc=""+ds.child("movie_description").getValue();
                    String music=""+ds.child("music").getValue();
                    String production=""+ds.child("production").getValue();
                    String video_url=""+ds.child("videoUrl").getValue();
                    String year=""+ds.child("year").getValue();
                    String thumbnail_url=""+ds.child("Thumbnail_URL").getValue();
                    String Thumb_cover=""+ds.child("Thumbnail_URL").getValue();
                    String Category=""+ds.child("category").getValue();
                    Intent intent=new Intent(homepage.this,MovieDetailsActivity.class);
                    intent.putExtra("title",movie);
                    intent.putExtra("imgURL",thumbnail_url);
                    intent.putExtra("movieDesc",movie_desc);
                    intent.putExtra("imgCover",Thumb_cover);
                    intent.putExtra("movieUrl",video_url);
                    intent.putExtra("hero",hero);
                    intent.putExtra("heroine",heroine);
                    intent.putExtra("comedian",comedian);
                    intent.putExtra("director",director);
                    intent.putExtra("music",music);
                    intent.putExtra("production",production);
                    intent.putExtra("year",year);
                    intent.putExtra("movieCategory",Category);
                    startActivity(intent);
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"No such Movie Try Another movie",Toast.LENGTH_LONG).show();
            }
        });
    }
});

swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        navigationDrawer();
        inViews();
        addAllMovies();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        },4000);
    }
});
        //Navigation Drawer Menu Called by Function
        navigationDrawer();
        inViews();
        addAllMovies();
        InPopularMovies();
        InLatestMovies();
        moviesViewTab();

                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth fauth;
                        fauth=FirebaseAuth.getInstance();
                        fauth.signOut();
                        Intent ds=new Intent(homepage.this,login.class);
                        startActivity(ds);
                        finish();
                        Toast.makeText(getApplicationContext(),"Logged OUT Successfully",Toast.LENGTH_SHORT).show();
                    }
                });
            }




    public void addAllMovies() {
        uploads = new ArrayList<>();
        uploads_slide = new ArrayList<>();
        uploads_list_latest = new ArrayList<>();
        uploads_list_popular = new ArrayList<>();
        action_movies = new ArrayList<>();
        sports_movies = new ArrayList<>();
        romantic_movies = new ArrayList<>();
        thriller_movies = new ArrayList<>();
        comedy_movies = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Movies");
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

       databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapShot : dataSnapshot.getChildren())
                {
                    GetMovieDetails upload=postSnapShot.getValue(GetMovieDetails.class);
                   SliderSide slide=postSnapShot.getValue(SliderSide.class);

                   if (upload.getMovie_Type().equals("Latest")){
                        uploads_list_latest.add(upload);
                    }
                    if (upload.getMovie_Type().equals("Popular")){
                        uploads_list_popular.add(upload);
                    }

                    if (upload.getCategory().equals("Action")){
                        action_movies.add(upload);
                    }
                    if (upload.getCategory().equals("Sports")){
                        sports_movies.add(upload);
                    }
                    if (upload.getCategory().equals("Romantic")){
                        romantic_movies.add(upload);
                    }
                    if (upload.getCategory().equals("Comedy")){
                        comedy_movies.add(upload);
                    }
                    if (upload.getCategory().equals("Thriller")){
                        thriller_movies.add(upload);
                    }
                    if (upload.getMovie_Type().equals("Slide")){
                        uploads_slide.add(slide);
                    }
                    uploads.add(upload);
                }
             inSlider();
               progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void inSlider() {

        SliderPagerAdapterNew adapterNew=new SliderPagerAdapterNew(this,uploads_slide);
        sliderPage.setAdapter(adapterNew);
        adapterNew.notifyDataSetChanged();

        //timer
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(),1000,10000);
        indicator.setupWithViewPager(sliderPage,true);
    }



    public void InPopularMovies(){
        movieShowAdapter=new MovieShowAdapter(this,uploads_list_popular,this);
        Movies_best_RV.setAdapter(movieShowAdapter);
        Movies_best_RV.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieShowAdapter.notifyDataSetChanged();
    }
    public void InLatestMovies(){
        movieShowAdapter=new MovieShowAdapter(this,uploads_list_latest,this);
        Movies_latest_RV.setAdapter(movieShowAdapter);
        Movies_latest_RV.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieShowAdapter.notifyDataSetChanged();

    }

        public void moviesViewTab () {

            tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Action"));
            tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Romantic"));
            tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Sports"));
            tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Comedy"));
            tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Thriller"));

            tabmoviesactions.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                             getActionMovies();
                            break;
                        case 1:
                              getRomanticMovies();
                            break;
                        case 2:
                             getSportsMovies();
                            break;
                        case 3:
                             getComedyMovies();
                            break;
                        case 4:
                             getThrillerMovies();
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        }
        public void inViews () {

            sliderPage = findViewById(R.id.slide_page);
            indicator = findViewById(R.id.indicator);
            Movies_best_RV = findViewById(R.id.best_recycle);
            Movies_latest_RV = findViewById(R.id.latest_recycle);
            tab = findViewById(R.id.tab_movies_recycle);
            tabmoviesactions = findViewById(R.id.tab_action);
        }

        public void navigationDrawer () {

            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_home);

            navigation_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            });

        }

        @Override
        public void onBackPressed () {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                super.onBackPressed();
                return;
            } else {
                backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.nav_home:
                    break;
                case R.id.nav_about:
                    Intent aboutintent=new Intent(homepage.this,aboutus.class);
                    startActivity(aboutintent);
                    break;
                case R.id.nav_contact:
                    Intent contactintent=new Intent(homepage.this,contactus.class);
                    startActivity(contactintent);
                    break;
                case R.id.nav_faq:
                    Intent faqintent=new Intent(homepage.this,faq.class);
                    startActivity(faqintent);
                    break;
                case  R.id.nav_logout:
                    FirebaseAuth mauth;
                    mauth=FirebaseAuth.getInstance();
                     mauth.signOut();
                    Intent ds=new Intent(homepage.this,login.class);
                    startActivity(ds);
                    finish();
                    Toast.makeText(getApplicationContext(),"Logged OUT Successfully",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_share:
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    databaseReference=FirebaseDatabase.getInstance().getReference("Links");
                    Query q=databaseReference.orderByChild("link");
                    q.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds:snapshot.getChildren()){
                                String link=""+ds.child("link").getValue();
                                String shareBody="Download Movie Paradise for free Movies:- " + link +" Thank You :)";
                                String shareSub="Online Movie Streaming App";
                                intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                                startActivity(Intent.createChooser(intent,"Share Using"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    break;
                case  R.id.nav_rateus:
                    Dialog dialog=new Dialog(this);
                    dialog.setContentView(R.layout.rating_box);
                    RatingBar ratingBar=dialog.findViewById(R.id.rating_bar);
                    dialog.show();
                    Button rateSubmit=dialog.findViewById(R.id.rate_submit);
                    rateSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Thank You for your Ratings!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case R.id.nav_profile:
                    Intent profintent=new Intent(homepage.this,UserProfile.class);
                    startActivity(profintent);
                    break;
            }

            return true;
        }

        @Override
        public void onMovieClick(GetMovieDetails movie, ImageView imageView) {

        Intent intent=new Intent(this,MovieDetailsActivity.class);
        intent.putExtra("title",movie.getMovie_name());
        intent.putExtra("imgURL",movie.getThumbnail_URL());
        intent.putExtra("movieDesc",movie.getMovie_description());
        intent.putExtra("imgCover",movie.getThumbnail_URL());
        intent.putExtra("movieUrl",movie.getVideoUrl());
        intent.putExtra("movieCategory",movie.getCategory());
            intent.putExtra("hero",movie.getHero());
            intent.putExtra("heroine",movie.getHeroine());
            intent.putExtra("comedian",movie.getComedian());
            intent.putExtra("director",movie.getDirector());
            intent.putExtra("music",movie.getMusic());
            intent.putExtra("production",movie.getProduction());
            intent.putExtra("year",movie.getYear());
            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(homepage.this,imageView,"sharedName");
            startActivity(intent,options.toBundle());
        }

        class SliderTimer extends TimerTask {
            public void run() {
                homepage.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (sliderPage.getCurrentItem() < uploads_slide.size() - 1) {
                            sliderPage.setCurrentItem(sliderPage.getCurrentItem());
                        } else {
                            sliderPage.setCurrentItem(0);
                        }
                    }
                });
            }

        }

        public void getActionMovies () {
            movieShowAdapter = new MovieShowAdapter(this, action_movies, this);
            tab.setAdapter(movieShowAdapter);
            tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            movieShowAdapter.notifyDataSetChanged();
        }
        public void getRomanticMovies () {
            movieShowAdapter = new MovieShowAdapter(this, romantic_movies, this);
            tab.setAdapter(movieShowAdapter);
            tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            movieShowAdapter.notifyDataSetChanged();
        }
        public void getSportsMovies () {
            movieShowAdapter = new MovieShowAdapter(this, sports_movies, this);
            tab.setAdapter(movieShowAdapter);
            tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            movieShowAdapter.notifyDataSetChanged();
        }
        public void getComedyMovies () {
            movieShowAdapter = new MovieShowAdapter(this, comedy_movies, this);
            tab.setAdapter(movieShowAdapter);
            tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            movieShowAdapter.notifyDataSetChanged();
        }
        public void getThrillerMovies () {
            movieShowAdapter = new MovieShowAdapter(this, thriller_movies, this);
            tab.setAdapter(movieShowAdapter);
            tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            movieShowAdapter.notifyDataSetChanged();
        }
    }
