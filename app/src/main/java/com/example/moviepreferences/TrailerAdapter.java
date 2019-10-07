package com.example.moviepreferences;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.YoutubeVideoHolder> {

    private Context mContext;
    private List<Trailers> videoList;

    VideoAdapterOnClickHandler mClickHandler;

    public interface VideoAdapterOnClickHandler {
        void onClick(Trailers trailers);
    }

    public TrailerAdapter(Context context, TrailerAdapter.VideoAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public TrailerAdapter.YoutubeVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_controls, parent, false);
        return new TrailerAdapter.YoutubeVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.YoutubeVideoHolder holder, int position) {
        holder.BindView(position);
    }

    @Override
    public int getItemCount() {
        if (videoList == null) return 0;
        return videoList.size();
    }

    public void loadTrailers(List<Trailers> videos) {
        videoList = videos;
    }

    public class YoutubeVideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView contentTxt;

        public YoutubeVideoHolder(@NonNull View itemView) {
            super(itemView);
            contentTxt = itemView.findViewById(R.id.listItem);
            contentTxt.setCompoundDrawablePadding(10);
            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_play);
            contentTxt.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            itemView.setOnClickListener(this);

        }

        public void BindView(final int position) {
            Trailers video = videoList.get(position);
            contentTxt.setText(video.getName());
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Trailers loadMovie = videoList.get(adapterPosition);
            mClickHandler.onClick(loadMovie);
        }
    }
}
