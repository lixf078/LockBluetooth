package in.srain.cube.views.loadmore;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

/**
 * Created by hubing on 16/4/19.
 */
public class LoadMoreRecycleViewContainer extends LoadMoreContainerRecycleBase {

    private FamiliarRecyclerView recyclerView;

    public LoadMoreRecycleViewContainer(Context context) {
        super(context);
    }

    public LoadMoreRecycleViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void addFooterView(View view) {
        recyclerView.addFooterView(view);
    }

    @Override
    protected void removeFooterView(View view) {
        recyclerView.removeFooterView(view);
    }

    @Override
    protected RecyclerView retrieveRecyclerView() {
        recyclerView = (FamiliarRecyclerView) getChildAt(0);
        return recyclerView;
    }

    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener l) {

    }
}
