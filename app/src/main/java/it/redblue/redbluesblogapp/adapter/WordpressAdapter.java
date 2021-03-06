package it.redblue.redbluesblogapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.redblue.redbluesblogapp.databinding.PostItemBinding;
import it.redblue.redbluesblogapp.model.WPCategory;
import it.redblue.redbluesblogapp.model.WordpressPost;

/**
 * Created by redblue on 02/07/16.
 */
public class WordpressAdapter extends RecyclerView.Adapter<WordpressAdapter.PostItemViewHolder> {

    private List<WordpressPost> posts;
    private List<WPCategory> categories;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public WordpressAdapter(List<WordpressPost> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public PostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PostItemBinding binding = PostItemBinding.inflate(inflater, parent, false);
        return new PostItemViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(PostItemViewHolder holder, int position) {
        WordpressPost post = posts.get(position);
        holder.binding.setPost(post);
        holder.binding.getRoot().setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(position, post);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, WordpressPost item);
    }

    public class PostItemViewHolder extends RecyclerView.ViewHolder {
        PostItemBinding binding;

        public PostItemViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

    /*
    private List<WordpressPost> posts;
    private int rowLayout;
    private Context context;

    public WordpressAdapter(List<WordpressPost> posts, int rowLayout, Context context) {
        this.posts = posts;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void onBindViewHolder(PostItemViewHolder holder, int i) {
        ViewDataBinding viewDataBinding = holder.getViewDataBinding();
        viewDataBinding.setVariable(rowLayout, posts.get(i));
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.postTitle.setText(Html.fromHtml(posts.get(position).getTitle()));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date postDate = null;
        String postDateString = null;
        try {
            postDate = df.parse(posts.get(position).getData());
            postDateString = dateFormat.format(postDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.data.setText(postDateString != null ? postDateString : "");
        holder.postExcerpt.setText(Html.fromHtml(posts.get(position).getExcerpt()));
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        LinearLayout postsLayout;
        TextView postTitle;
        TextView data;
        TextView postExcerpt;

        public PostViewHolder(View v) {
            super(v);
            postsLayout = (LinearLayout) v.findViewById(R.id.posts_layout);
            postTitle = (TextView) v.findViewById(R.id.title);
            data = (TextView) v.findViewById(R.id.data);
            postExcerpt = (TextView) v.findViewById(R.id.excerpt);
        }
    }
*/
}
