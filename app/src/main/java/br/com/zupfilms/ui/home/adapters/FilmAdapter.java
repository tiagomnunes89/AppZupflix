package br.com.zupfilms.ui.home.adapters;

import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.Objects;

import br.com.zupfilms.R;
import br.com.zupfilms.server.response.FilmGenres;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.ui.singleton.SingletonTotalResults;

public class FilmAdapter extends PagedListAdapter<FilmResponse, RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_PROGRESS = 1;
    private final Context mCtx;
    private FilmAdapter.OnItemClickListener onItemClickListener;
    private FilmAdapter.OnCheckBoxClickListener onCheckBoxClickListener;
    private final FilmGenres filmGenres;

    public interface OnItemClickListener {
        void onItemClick(int position, PagedList<FilmResponse> currentList);
    }

    public interface OnCheckBoxClickListener {
        void OnCheckBoxClick(int position, PagedList<FilmResponse> currentList, Boolean isChecked);
    }

    public void setOnCheckBoxClickListener(FilmAdapter.OnCheckBoxClickListener onCheckBoxClickListener) {
        this.onCheckBoxClickListener = onCheckBoxClickListener;
    }

    public void setOnItemClickListener(FilmAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FilmAdapter(Context mCtx, FilmGenres filmGenres) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
        this.filmGenres = filmGenres;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionLoader(position)) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean isPositionLoader(int position) {
        if (SingletonTotalResults.INSTANCE.getTotalResults() != null) {
            return getItemCount() - 1 == position && position != SingletonTotalResults.INSTANCE.getTotalResults() - 1;
        }
        return false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.item_film, parent, false);
            return new ItemViewHolder(view, this.onItemClickListener, this.onCheckBoxClickListener, getCurrentList());
        } else if (viewType == TYPE_PROGRESS) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.loading_layout_list, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            ((ItemViewHolder) viewHolder).setFilmResponseInformation(getItem(position), filmGenres);
        } else if (viewHolder instanceof LoadingViewHolder) {

        } else {
            TastyToast.makeText(mCtx, "Não foi possível carregar os detalhes deste filme.", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER, 0, 700);
        }
    }


    private static final DiffUtil.ItemCallback<FilmResponse> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<FilmResponse>() {
                @Override
                public boolean areItemsTheSame(FilmResponse oldItem, FilmResponse newItem) {
                    return Objects.equals(oldItem, newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull FilmResponse oldItem, @NonNull FilmResponse newItem) {
                    return Objects.equals(oldItem, newItem);
                }
            };

}
