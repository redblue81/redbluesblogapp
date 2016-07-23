package it.redblue.redbluesblogapp.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by redblue on 23/07/16.
 */
public abstract class EndlessScrollingListener extends RecyclerView.OnScrollListener {

    private int visiblePosts = 5; // Soglia oltre la quale iniziare il caricamento di nuovi post
    private int currentPage = 1; // offset corrente
    private int lastLoadCount = 0; // numero di post all'ultimo caicamento
    private boolean loading = true;
    private int startingPageIndex = 0;
    RecyclerView.LayoutManager layoutManager;

    public EndlessScrollingListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public int getLastPost(int[] lastPosition) {
        int max = 0;
        for (int i = 0; i < lastPosition.length; i++) {
            if (i == 0)
                max = lastPosition[1];
            else if (lastPosition[i] > max)
                max = lastPosition[i];
        }
        return max;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int sx) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = layoutManager.getItemCount();

        if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        if (totalItemCount < lastLoadCount) {
            this.currentPage = this.startingPageIndex;
            this.lastLoadCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        if (loading && (totalItemCount > lastLoadCount)) {
            loading = false;
            lastLoadCount = totalItemCount;
        }
        if (!loading && (lastVisibleItemPosition + visiblePosts) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount);
            loading = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount);

}
