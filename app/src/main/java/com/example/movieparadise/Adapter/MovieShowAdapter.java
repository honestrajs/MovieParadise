package com.example.movieparadise.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieparadise.Model.GetMovieDetails;
import com.example.movieparadise.Model.MovieItemClickListnerNew;
import com.example.movieparadise.R;

import java.util.List;

public class MovieShowAdapter extends RecyclerView.Adapter<MovieShowAdapter.MyViewHolder> {

    public Context context;
    public List<GetMovieDetails> uploads;
    MovieItemClickListnerNew movieItemClickListnerNew;

    public MovieShowAdapter(Context context, List<GetMovieDetails> uploads,
                            MovieItemClickListnerNew movieItemClickListnerNew) {
        this.context = context;
        this.uploads = uploads;
        this.movieItemClickListnerNew = movieItemClickListnerNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.movie_item_new,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieShowAdapter.MyViewHolder holder, int position) {

        GetMovieDetails getMovieDetails=uploads.get(position);
        holder.mvTitle.setText(getMovieDetails.getMovie_name());
        Glide.with(context).load(getMovieDetails.getThumbnail_URL()).into(holder.mvImage);

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mvTitle;
        ImageView mvImage;
        ConstraintLayout Container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mvTitle=itemView.findViewById(R.id.item_movie_title);
            mvImage=itemView.findViewById(R.id.item_movie_img);
            Container=itemView.findViewById(R.id.container);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieItemClickListnerNew.onMovieClick(uploads.get(getAdapterPosition()),mvImage);
                }
            });
        }
    }
}
