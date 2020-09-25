package br.com.zupfilms.ui.home.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

import br.com.zupfilms.R;
import br.com.zupfilms.model.MovieDetailsModelDB;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MoviesAdapter.OnItemClickListener onItemClickListener;
    private MoviesAdapter.OnCheckBoxClickListener onCheckBoxClickListener;
    private List<MovieDetailsModelDB> movieList;
    private final Context context;


    public MoviesAdapter(Context context, List<MovieDetailsModelDB> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, List<MovieDetailsModelDB> currentList);
    }

    public interface OnCheckBoxClickListener {
        void OnCheckBoxClick(int position, List<MovieDetailsModelDB> currentList, Boolean isChecked);
    }

    public void setOnCheckBoxClickListener(MoviesAdapter.OnCheckBoxClickListener onCheckBoxClickListener) {
        this.onCheckBoxClickListener = onCheckBoxClickListener;
    }

    public void setOnItemClickListener(MoviesAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_film, viewGroup, false);
        return new FavoriteViewHolder(view, this.onItemClickListener, this.onCheckBoxClickListener, movieList);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof FavoriteViewHolder) {
            ((FavoriteViewHolder) viewHolder).setFilmResponseInformation(movieList.get(position));
        } else {
            TastyToast.makeText(context, "Não foi possível carregar os detalhes deste filme.", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER, 0, 700);
        }

    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    public void setMovieList(List<MovieDetailsModelDB> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, movieList.size());

    }
}
