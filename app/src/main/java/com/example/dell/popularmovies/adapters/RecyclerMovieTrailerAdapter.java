package com.example.dell.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.popularmovies.R;
import com.example.dell.popularmovies.model.Movie;
import com.example.dell.popularmovies.model.TralierResult;

import java.util.List;


public class RecyclerMovieTrailerAdapter extends RecyclerView. Adapter<RecyclerMovieTrailerAdapter.TrailerViewHolder> {

    private List<TralierResult> trailerList;
    private Context context;

    public RecyclerMovieTrailerAdapter( Context context,List<TralierResult> trailerList) {
        this.trailerList = trailerList;
        this.context = context;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layout = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layout, viewGroup, shouldAttachToParentImmediately);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        String trailer_name = trailerList.get(position).getName();
        holder.video_btn.setText(trailer_name);

    }

    @Override
    public int getItemCount() {
        if (null == trailerList) return 0;

        return trailerList.size();
    }

    public  class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       public TextView video_btn;
       public ImageView thumbnail;


        public TrailerViewHolder(View view) {
            super(view);
            video_btn = view.findViewById(R.id.video_tralier_btn);
            thumbnail=view.findViewById(R.id.youtube_img);
            video_btn.setOnClickListener(this);
        }

        Toast mToast;



        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            String video_link = trailerList.get(position).getKey();
            String video_name = trailerList.get(position).getName();

            if (position != RecyclerView.NO_POSITION) {
                TralierResult clickedData = trailerList.get(position);

                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(context, "You choose :" + trailerList.get(position).getName(), Toast.LENGTH_SHORT);
                mToast.show();

                // Explecet Intent
                Intent yt_play = new Intent(Intent.ACTION_VIEW, Uri.parse(video_link));
//                Intent chooser = Intent.createChooser(yt_play , "Open With");
                yt_play.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (yt_play .resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(yt_play);
                }
            }
        }
    }
}