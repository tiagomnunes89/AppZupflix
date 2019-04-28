package br.com.zupfilms.ui.home.adapters;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.Objects;

import br.com.zupfilms.R;
import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.server.response.FilmGenres;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.ui.singleton.SingletonTotalResults;

public class FilmAdapterDetailsList extends PagedListAdapter<FilmResponse, RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEADER = -1;
    private static final int TYPE_PROGRESS = 1;
    private Context mCtx;
    private FilmAdapterDetailsList.OnItemClickListener onItemClickListener;
    private MovieDetailsModel movieDetailsModel;
    private FilmAdapterDetailsList.OnCheckBoxClickListener onCheckBoxClickListener;
    private FilmGenres filmGenres;

    public interface OnItemClickListener {
        void onItemClick(int position, PagedList<FilmResponse> currentList);
    }

    public interface OnCheckBoxClickListener {
        void OnCheckBoxClick(int position, PagedList<FilmResponse> currentList, Boolean isChecked);
    }

    public void setOnCheckBoxClickListener(FilmAdapterDetailsList.OnCheckBoxClickListener onCheckBoxClickListener) {
        this.onCheckBoxClickListener = onCheckBoxClickListener;
    }

    public void setOnItemClickListener(FilmAdapterDetailsList.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FilmAdapterDetailsList(Context mCtx, MovieDetailsModel movieDetailsModel, FilmGenres filmGenres) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
        this.movieDetailsModel = movieDetailsModel;
        this.filmGenres = filmGenres;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)){
            return TYPE_HEADER;
        } else if (isPositionLoader(position)){
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionLoader(int position) {
        if(SingletonTotalResults.INSTANCE.getTotalResults() != null){
            return getItemCount() -1 == position && position != SingletonTotalResults.INSTANCE.getTotalResults()-1;
        }
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.item_film, parent, false);
            return new ItemViewHolderDetails(view, this.onItemClickListener, this.onCheckBoxClickListener,getCurrentList());
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.item_movie_details, parent, false);
            return new DetailsViewHolder(view,this.onCheckBoxClickListener,getCurrentList());
        } else if(viewType == TYPE_PROGRESS){
            View view = LayoutInflater.from(mCtx).inflate(R.layout.loading_layout_list, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolderDetails) {
            FilmResponse film = getItem(position-1);
            ((ItemViewHolderDetails) viewHolder).setFilmResponseInformation(film,filmGenres);
        } else if (viewHolder instanceof DetailsViewHolder) {
            if (movieDetailsModel != null) {
                ((DetailsViewHolder) viewHolder).setMovieDetailsInformation(this.movieDetailsModel);
            }
        } else if (viewHolder instanceof LoadingViewHolder) {

        } else {
            TastyToast.makeText(mCtx, "Não foi possível carregar os detalhes deste filme.", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER, 0, 700);
        }
    }


    private static DiffUtil.ItemCallback<FilmResponse> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<FilmResponse>() {
                @Override
                public boolean areItemsTheSame(FilmResponse oldItem, FilmResponse newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(FilmResponse oldItem, FilmResponse newItem) {
                    return Objects.equals(oldItem, newItem);
                }
            };
}
