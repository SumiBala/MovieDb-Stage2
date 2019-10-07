package com.example.moviepreferences;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context mContext;

    private final String BASE_URL;
    private List<Movie> movieArrayList;
    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        BASE_URL = mContext.getResources().getString(R.string.imgPathUrl);
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_image, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        movieViewHolder.BindView(position);
    }

    @Override
    public int getItemCount() {
        if (movieArrayList == null) return 0;
        return movieArrayList.size();
    }

    public void loadMovies(List<Movie> movies) {
        movieArrayList = movies;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView movieImage;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movie_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie loadMovie = movieArrayList.get(adapterPosition);
            mClickHandler.onClick(loadMovie);
        }

        public void BindView(final int position) {
            Movie loadMovie = movieArrayList.get(position);
            String path = BASE_URL + loadMovie.getImage();
            Picasso.with(mContext)
                    .load(path)
                    .error(R.drawable.placholder)
                    .placeholder(R.drawable.placholder)
                    .into(movieImage);
        }
    }

    //Avoid Duplication.
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}








