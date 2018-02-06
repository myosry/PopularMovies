package com.example.dell.popularmovies.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.popularmovies.activities.DetailsActivity;
import com.example.dell.popularmovies.R;
import com.example.dell.popularmovies.model.Movie;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RecyclerMovieAdapter extends RecyclerView.Adapter<RecyclerMovieAdapter.MovieViewHolder> {

    private List<Movie> movieList ;
    private Context context;

    public RecyclerMovieAdapter(Context context,List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layout = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layout, viewGroup, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);


    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        String orignial_title = movieList.get(position).getTitle();
        holder.movie_name.setText(orignial_title);
        String vote = "Rating : "+Double.toString(movieList.get(position).getVoteAverage());
        holder.movie_rating.setText(vote);
        Log.e("poster",movieList.get(position).getPosterPath());

        Picasso.with(context)
                .load(movieList.get(position).getPosterPath())
                .placeholder(R.drawable.loading)
                .into(holder.movie_poster
                ,PicassoPalette.with(movieList.get(position).getPosterPath(), holder.movie_poster)
                        .use(PicassoPalette.Profile.MUTED_DARK)
                        .intoBackground( holder.movie_name)
                        .intoTextColor( holder.movie_name)
                        .use(PicassoPalette.Profile.VIBRANT)
                        .intoBackground( holder.movie_name, PicassoPalette.Swatch.RGB)
                        .intoTextColor( holder.movie_name, PicassoPalette.Swatch.BODY_TEXT_COLOR)

        );

    }


    @Override
    public int getItemCount() {
        if (null == movieList) return 0;

        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView movie_name;
        private TextView movie_rating;
        private ImageView movie_poster;


        public MovieViewHolder(View view) {
            super(view);

            movie_name   = view.findViewById(R.id.movie_name);
            movie_rating = view.findViewById(R.id.movie_rating);
            movie_poster = view.findViewById(R.id.movie_poster);

            view.setOnClickListener(this);
        }
        Toast mToast;

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            Intent intent = new Intent(context, DetailsActivity.class);

            if(position != RecyclerView.NO_POSITION) {

                Movie clickedDataItem = movieList.get(position);
                intent.putExtra("movies", clickedDataItem);

                if (mToast != null) {
                    mToast.cancel();}
                mToast =Toast.makeText(context,"You choose :"+movieList.get(position).getOriginalTitle() ,Toast.LENGTH_SHORT);
                mToast.show();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        }
    }
}