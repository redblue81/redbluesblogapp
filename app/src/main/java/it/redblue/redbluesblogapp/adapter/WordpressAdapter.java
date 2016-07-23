package it.redblue.redbluesblogapp.adapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.databinding.tool.util.L;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import it.redblue.redbluesblogapp.R;
import it.redblue.redbluesblogapp.databinding.PostItemBinding;
import it.redblue.redbluesblogapp.databinding.ProgressBarBinding;
import it.redblue.redbluesblogapp.model.WordpressPost;

/**
 * Created by redblue on 02/07/16.
 */
public class WordpressAdapter extends RecyclerView.Adapter<WordpressAdapter.PostItemViewHolder> {

    private List<WordpressPost> posts;
    private Context context;

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
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @BindingAdapter("bind:htmlBinder")
    public static void toHtml(TextView view, String string) {
        view.setText(Html.fromHtml(string));
    }

    @BindingAdapter("bind:dataBinder")
    public static void toItalianDate(TextView view, String string) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date postDate = null;
        String postDateString = null;
        try {
            postDate = df.parse(string);
            postDateString = dateFormat.format(postDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        view.setText(postDateString != null ? postDateString : "");
    }

    public class PostItemViewHolder extends RecyclerView.ViewHolder {
        PostItemBinding binding;

        public PostItemViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBarBinding binding;

        public LoadingViewHolder(View view) {
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
