package com.example.dell.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.popularmovies.R;
import com.example.dell.popularmovies.model.ReviewResult;

import java.util.List;



public class RecyclerMovieReviewsAdapter extends RecyclerView.Adapter<RecyclerMovieReviewsAdapter.ReviewsViewHolder> {

    private List<ReviewResult> reviewsList ;
    private Context context;

    public RecyclerMovieReviewsAdapter( Context context,List<ReviewResult> reviewsList) {
        this.reviewsList = reviewsList;
        this.context = context;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layout = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layout, viewGroup, shouldAttachToParentImmediately);
        return new ReviewsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        String author  = reviewsList.get(position).getAuthor();
        String content = reviewsList.get(position).getContent();

        holder.author_textview.setText(author);
        holder.content_textview.setText(content);

    }

    @Override
    public int getItemCount() {
        if (null == reviewsList) return 0;

        return reviewsList.size();    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        private TextView author_textview;
        private TextView content_textview;

        public ReviewsViewHolder(View view) {
            super(view);
            author_textview = view.findViewById(R.id.review_author_name);
            content_textview = view.findViewById(R.id.review_content);
        }
    }
}
