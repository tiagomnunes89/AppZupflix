package br.com.zupfilms.ui.home.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.zupfilms.R;
import br.com.zupfilms.model.MovieDetailsModelDB;

class FavoriteViewHolder extends RecyclerView.ViewHolder {

    private final TextView textTitleFilm;
    private final ImageView imageView;
    private final TextView keywords;
    private final TextView movieDescription;
    private final TextView year;
    private final TextView filmNote;
    private CheckBox checkBox;
    private final CardView cardViewPoster;
    private final LinearLayout information;
    private final FrameLayout filmNoteFrameLayout;

    public FavoriteViewHolder(View itemView, final MoviesAdapter.OnItemClickListener onItemClickListener,
                              final MoviesAdapter.OnCheckBoxClickListener onCheckBoxClickListener,
                              final List<MovieDetailsModelDB> currentList) {
        super(itemView);

        imageView = itemView.findViewById(R.id.movie_poster);
        keywords = itemView.findViewById(R.id.textView_keywords);
        movieDescription = itemView.findViewById(R.id.movie_description);
        information = itemView.findViewById(R.id.linearLayout_year);
        year = information.findViewById(R.id.textView_year);
        filmNoteFrameLayout = itemView.findViewById(R.id.frameLayout_filmNote);
        filmNote = filmNoteFrameLayout.findViewById(R.id.textView_filmNote);
        textTitleFilm = itemView.findViewById(R.id.text_title_film);
        checkBox = itemView.findViewById(R.id.checkbox_favorite);
        cardViewPoster = itemView.findViewById(R.id.cardview_poster_item);
        checkBox = itemView.findViewById(R.id.checkbox_favorite);
        if(!checkBox.isChecked()){
            checkBox.setChecked(true);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckBoxClickListener != null) {
                    int position = getAdapterPosition();
                    if (checkBox.isChecked()) {
                        onCheckBoxClickListener.OnCheckBoxClick(position, currentList, true);
                    } else {
                        onCheckBoxClickListener.OnCheckBoxClick(position, currentList, false);
                    }
                }
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    onItemClickListener.onItemClick(position, currentList);
                }
            }
        });
    }

    public void setFilmResponseInformation(MovieDetailsModelDB film) {
        this.textTitleFilm.setText(film.getTitle());
        if (film.getPosterPath() == null || film.getPosterPath().isEmpty()) {
            this.cardViewPoster.setVisibility(View.INVISIBLE);
        } else {
            this.cardViewPoster.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w342/" + film.getPosterPath())
                    .into(this.imageView);
        }
        keywords.setText(film.getGenres());
        this.movieDescription.setText(film.getOverview());
        if(!film.getReleaseDate().isEmpty() && film.getReleaseDate().length()>=4){
            year.setText(film.getReleaseDate().substring(0, 4));
        }
        this.filmNote.setText(String.valueOf(film.getVote_average()));
    }
}
