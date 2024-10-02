package com.example.movieparadise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movieparadise.Adapter.MovieShowAdapter;
import com.example.movieparadise.Model.GetMovieDetails;
import com.example.movieparadise.Model.MovieItemClickListnerNew;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements MovieItemClickListnerNew {

    ImageView MoviesThumbnail,MoviesCoverImage;
    TextView tv_title,tv_description,tv_hero,tv_heroine,tv_comedian,tv_director,tv_music,tv_production,tv_year;
    FloatingActionButton play_fab;
    RecyclerView recyclerView_similarMovies;
    MovieShowAdapter movieShowAdapter;
    DatabaseReference databaseReference;
    List<GetMovieDetails> uploads,actionmovies,sportsmovies,comedymovies,romanticmovies,thrillermovies;
    String current_Video_url;
    String current_Video_Category;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        inView();
        similarMoviesRecycle();
        similarMovies();

        play_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MovieDetailsActivity.this,MoviePlayerActivity.class);
                intent.putExtra("videoUri",current_Video_url);
                startActivity(intent);
            }
        });

    }

    private void similarMovies() {
        if (current_Video_Category.equals("Action")){
            movieShowAdapter=new MovieShowAdapter(this,actionmovies,this);
            recyclerView_similarMovies.setAdapter(movieShowAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager
                    .HORIZONTAL,false));

            movieShowAdapter.notifyDataSetChanged();
        }
        if (current_Video_Category.equals("Sports")){
            movieShowAdapter=new MovieShowAdapter(this,sportsmovies,this);
            recyclerView_similarMovies.setAdapter(movieShowAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager
                    .HORIZONTAL,false));

            movieShowAdapter.notifyDataSetChanged();
        }
        if (current_Video_Category.equals("Romantic")){
            movieShowAdapter=new MovieShowAdapter(this,romanticmovies,this);
            recyclerView_similarMovies.setAdapter(movieShowAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager
                    .HORIZONTAL,false));

            movieShowAdapter.notifyDataSetChanged();
        }
        if (current_Video_Category.equals("Comedy")){
            movieShowAdapter=new MovieShowAdapter(this,comedymovies,this);
            recyclerView_similarMovies.setAdapter(movieShowAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager
                    .HORIZONTAL,false));

            movieShowAdapter.notifyDataSetChanged();
        }
        if (current_Video_Category.equals("Thriller")){
            movieShowAdapter=new MovieShowAdapter(this,thrillermovies,this);
            recyclerView_similarMovies.setAdapter(movieShowAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager
                    .HORIZONTAL,false));

            movieShowAdapter.notifyDataSetChanged();
        }

    }

    private void similarMoviesRecycle() {
        uploads=new ArrayList<>();
        actionmovies=new ArrayList<>();
        sportsmovies=new ArrayList<>();
        romanticmovies=new ArrayList<>();
        thrillermovies=new ArrayList<>();
        comedymovies=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("Movies");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    GetMovieDetails upload=postSnapshot.getValue(GetMovieDetails.class);
                    if (upload.getCategory().equals("Action")){
                        actionmovies.add(upload);
                    }
                    if (upload.getCategory().equals("Sports")){
                        sportsmovies.add(upload);
                    }
                    if (upload.getCategory().equals("Romantic")){
                        romanticmovies.add(upload);
                    }
                    if (upload.getCategory().equals("Comedy")){
                        comedymovies.add(upload);
                    }
                    if (upload.getCategory().equals("Thriller")){
                        thrillermovies.add(upload);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });
    }

    private void inView() {

        play_fab=findViewById(R.id.play_fab);
        tv_title=findViewById(R.id.detail_movie_title);
        tv_description=findViewById(R.id.detail_movie_desc);
        tv_hero=findViewById(R.id.hero_type);
        tv_heroine=findViewById(R.id.heroine_type);
        tv_year=findViewById(R.id.year_type);
        tv_comedian=findViewById(R.id.comedian_type);
        tv_director=findViewById(R.id.director_type);
        tv_music=findViewById(R.id.music_type);
        tv_production=findViewById(R.id.production_type);
        MoviesThumbnail=findViewById(R.id.details_movie_image);
        MoviesCoverImage=findViewById(R.id.detail_movie_cover);
        recyclerView_similarMovies=findViewById(R.id.recycle_similar_movies);



        String movieTitle=getIntent().getExtras().getString("title");
        String imgRecoverId=getIntent().getExtras().getString("imgURL");
        String imageCover=getIntent().getExtras().getString("imgCover");
        String movieDescText=getIntent().getExtras().getString("movieDesc");
        String movieUrl=getIntent().getExtras().getString("movieUrl");
        String movieCategory=getIntent().getExtras().getString("movieCategory");
        String hero=getIntent().getExtras().getString("hero");
        String heroine=getIntent().getExtras().getString("heroine");
        String comedian=getIntent().getExtras().getString("comedian");
        String director=getIntent().getExtras().getString("director");
        String music=getIntent().getExtras().getString("music");
        String production=getIntent().getExtras().getString("production");
        String year=getIntent().getExtras().getString("year");

        current_Video_url=movieUrl;
        current_Video_Category=movieCategory;
        Glide.with(this).load(imgRecoverId).into(MoviesThumbnail);
        Glide.with(this).load(imageCover).into(MoviesCoverImage);
        tv_title.setText(movieTitle);
        tv_description.setText(movieDescText);
        tv_hero.setText(hero);
        tv_heroine.setText(heroine);
        tv_comedian.setText(comedian);
        tv_director.setText(director);
        tv_music.setText(music);
        tv_production.setText(production);
        tv_year.setText(year);
    }

    @Override
    public void onMovieClick(GetMovieDetails movie, ImageView imageView) {

        tv_title.setText(movie.getMovie_name());
        tv_hero.setText(movie.getHero());
        tv_heroine.setText(movie.getHeroine());
        tv_comedian.setText(movie.getComedian());
        tv_director.setText(movie.getDirector());
        tv_music.setText(movie.getMusic());
        tv_production.setText(movie.getProduction());
        tv_year.setText(movie.getYear());
        Glide.with(this).load(movie.getThumbnail_URL()).into(MoviesThumbnail);
        Glide.with(this).load(movie.getThumbnail_URL()).into(MoviesCoverImage);
        tv_description.setText(movie.getMovie_description());
        current_Video_url =movie.getVideoUrl();
        current_Video_Category=movie.getCategory();
        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MovieDetailsActivity.this,
                imageView,"sharedName");
        options.toBundle();
    }
}