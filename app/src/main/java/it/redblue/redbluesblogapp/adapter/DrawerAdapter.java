package it.redblue.redbluesblogapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.redblue.redbluesblogapp.databinding.NavItemBinding;
import it.redblue.redbluesblogapp.model.DrawerItem;

/**
 * Created by redblue on 09/07/16.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.NavItemViewHolder> {

    private List<DrawerItem> items;
    private Context context;

    public DrawerAdapter(List<DrawerItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public NavItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NavItemBinding binding = NavItemBinding.inflate(inflater, parent, false);
        return new NavItemViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(NavItemViewHolder holder, int position) {
        DrawerItem item = items.get(position);
        holder.binding.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class NavItemViewHolder extends RecyclerView.ViewHolder {
        NavItemBinding binding;

        public NavItemViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
