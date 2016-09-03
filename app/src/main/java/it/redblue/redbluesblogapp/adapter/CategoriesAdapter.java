package it.redblue.redbluesblogapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.redblue.redbluesblogapp.databinding.CatItemBinding;
import it.redblue.redbluesblogapp.model.WPCategory;

/**
 * Created by redblue on 24/08/16.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryItemViewHolder> {

    private List<WPCategory> categories;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public CategoriesAdapter(List<WPCategory> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }


    @Override
    public CategoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CatItemBinding binding = CatItemBinding.inflate(inflater, parent, false);
        return new CategoryItemViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(CategoryItemViewHolder holder, int position) {
        WPCategory cat = categories.get(position);
        holder.binding.setCategory(cat);
        holder.binding.getRoot().setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(position, cat);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, WPCategory item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class CategoryItemViewHolder extends RecyclerView.ViewHolder {
        CatItemBinding binding;

        public CategoryItemViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
