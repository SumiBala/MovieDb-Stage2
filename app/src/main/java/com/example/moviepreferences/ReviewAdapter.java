package com.example.moviepreferences;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context mContext;
    List<String> mReview = new ArrayList<>();


    public ReviewAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_controls, parent, false);
        return new ReviewAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        holder.BindView(position);
    }

    @Override
    public int getItemCount() {
        if (mReview.size() > 0) return mReview.size();
        return 0;
    }

    public void loadReviews(List<String> reviews) {
        mReview = reviews;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView contentTxt;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTxt = itemView.findViewById(R.id.listItem);
        }

        public void BindView(final int position) {
            contentTxt.setText(mReview.get(position));
        }
    }
}
