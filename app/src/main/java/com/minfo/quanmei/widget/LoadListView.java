package com.minfo.quanmei.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.minfo.quanmei.R;

/**
 * Created by min-fo-012 on 15/10/23.
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener {
    int firstVisibleItem;
    int scrollState;

    View footer;
    int totalItemCount;
    int lastVisibleItem;
    boolean isLoading;
    ILoadListener iLoadListener;

    boolean isCanLoad;

    public LoadListView(Context context) {
        super(context);
        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void setIsCanLoad(boolean isCanLoad) {
        this.isCanLoad = isCanLoad;
    }
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if(!isCanLoad) {
            footer = inflater.inflate(R.layout.footer_layout, null);
            footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
            this.addFooterView(footer);
            this.setOnScrollListener(this);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
        if (isCanLoad) {
            if (totalItemCount == lastVisibleItem
                    && scrollState == SCROLL_STATE_IDLE) {
                if (!isLoading) {
                    isLoading = true;
                    footer.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
                    iLoadListener.onLoad();
                }
            }
        }
    }
    public void loadComplete() {
        isLoading = false;
        footer.findViewById(R.id.load_layout).setVisibility(
                View.GONE);
    }

    public void setLoadListener(ILoadListener iLoadListener) {
        this.iLoadListener = iLoadListener;
    }

    public interface ILoadListener {
        void onLoad();
    }
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }
}

