package in.srain.cube.views.loadmore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;

public interface LoadMoreContainer {

    public void setShowLoadingForFirstPage(boolean showLoading);

    public void setAutoLoadMore(boolean autoLoadMore);

    public void setOnScrollListener(AbsListView.OnScrollListener l);

    public void setOnScrollListener(RecyclerView.OnScrollListener mOnScrollListener);

    public void setLoadMoreView(View view);

    public void setLoadMoreUIHandler(LoadMoreUIHandler handler);

    public void setLoadMoreHandler(LoadMoreHandler handler);

    /**
     * When data has loaded
     *
     * @param emptyResult
     * @param hasMore
     */
    public void loadMoreFinish(boolean emptyResult, boolean hasMore);

    /**
     * When data has loaded
     *
     * @param emptyResult
     * @param hasMore
     */
    public void loadMoreFinish(boolean emptyResult, String emptyMsg, boolean hasMore, String noMoreMsg);

    /**
     * When something unexpected happened while loading the data
     *
     * @param errorCode
     * @param errorMessage
     */
    public void loadMoreError(int errorCode, String errorMessage);
}
