package br.com.zupfilms.ui.home.adapters;

import androidx.paging.PagedList;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.zupfilms.R;
import br.com.zupfilms.server.response.FilmGenres;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.server.response.GenresResponse;

class ItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView textTitleFilm;
    private final ImageView imageView;
    private final TextView keywords;
    private final TextView movieDescription;
    private final LinearLayout information;
    private final TextView year;
    private final FrameLayout filmNoteFrameLayout;
    private final TextView filmNote;
    private CheckBox checkBox;
    private final CardView cardViewPoster;

    public ItemViewHolder(View itemView, final FilmAdapter.OnItemClickListener onItemClickListener,
                          final FilmAdapter.OnCheckBoxClickListener onCheckBoxClickListener,
                          final PagedList<FilmResponse> currentList) {
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
        checkBox.setOnClickListener(v -> {
            if (onCheckBoxClickListener != null) {
                int position = getAdapterPosition();
                if (checkBox.isChecked()) {
                    onCheckBoxClickListener.OnCheckBoxClick(position, currentList, true);
                } else {
                    onCheckBoxClickListener.OnCheckBoxClick(position, currentList, false);
                }
            }
        });
        itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                onItemClickListener.onItemClick(position, currentList);
            }
        });
    }

    public void setFilmResponseInformation(FilmResponse film, FilmGenres allGenresList) {
        this.textTitleFilm.setText(film.getTitle());
        if (film.getPosterPath() == null || film.getPosterPath().isEmpty()) {
            this.cardViewPoster.setVisibility(View.INVISIBLE);
        } else {
            this.cardViewPoster.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w342/" + film.getPosterPath())
                    .into(this.imageView);
        }
        List<Integer> filmGenreIdsList = film.getGenreIds();
        List<String> filmGenreList = new ArrayList<>();
        int index;
        for(Integer genreID: filmGenreIdsList){
            for (GenresResponse genre: allGenresList.getGenres()){
                if(genreID == genre.getId()){
                    index = allGenresList.getGenres().indexOf(genre);
                    filmGenreList.add(allGenresList.getGenres().get(index).getName());
                }
            }

        }
        if(film.getFavoriteChecked()){
            checkBox.setChecked(true);
        }
        keywords.setText(sentenceBuilder(filmGenreList));
        this.movieDescription.setText(film.getOverview());
        if(film.getReleaseDate() != null && !film.getReleaseDate().isEmpty() && film.getReleaseDate().length()>=4){
            year.setText(film.getReleaseDate().substring(0, 4));
        }
        this.filmNote.setText(String.valueOf(film.getVoteAverage()));
    }

    private String sentenceBuilder(List<String> listString) {
        StringBuilder keywordList = new StringBuilder();
        if(listString != null) {
            for (int i = 0; i < listString.size(); i++) {
                keywordList.append(listString.get(i));
                if (i < listString.size() - 1) {
                    keywordList.append(", ");
                }
            }
        }
        return keywordList.toString();
    }
}
