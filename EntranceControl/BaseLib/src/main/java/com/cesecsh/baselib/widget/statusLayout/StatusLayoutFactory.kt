package com.cesecsh.baselib.widget.statusLayout

import android.graphics.Color
import android.support.annotation.LayoutRes
import android.view.View
import android.widget.FrameLayout
import com.cesecsh.baselib.R
import kotlinx.android.synthetic.main.status_loading_layout.view.*

/**
 * 作者：RockQ on 2018/8/15
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class StatusLayoutFactory constructor(var frameLayout: FrameLayout) {
    private lateinit var mContentLayout: View
    private lateinit var mEmptyLayout: View
    private lateinit var mLoadingLayout: View
    private lateinit var mErrorLayout: View
    private lateinit var mNetWorkErrorLayout: View

    init {
        mContentLayout = View.inflate(frameLayout.context, R.layout.status_content_layout, null)
        mEmptyLayout = View.inflate(frameLayout.context, R.layout.status_empty_layout, null)
        mLoadingLayout = View.inflate(frameLayout.context, R.layout.status_loading_layout, null)

        mErrorLayout = View.inflate(frameLayout.context, R.layout.status_error_layout, null)
        mNetWorkErrorLayout = View.inflate(frameLayout.context, R.layout.status_network_error_layout, null)

    }


    fun setContentLayout(@LayoutRes contentLayoutRes: Int) {
        this.mContentLayout = View.inflate(frameLayout.context, contentLayoutRes, null)
    }

    fun setEmptyLayout(@LayoutRes emptyLayoutRes: Int) {
        this.mEmptyLayout = View.inflate(frameLayout.context, emptyLayoutRes, null)
    }

    fun setLoadingLayout(@LayoutRes loadingLayoutRes: Int) {
        this.mContentLayout = View.inflate(frameLayout.context, loadingLayoutRes, null)
    }

    fun setErorrlayout(@LayoutRes errorLayoutRes: Int) {
        this.mErrorLayout = View.inflate(frameLayout.context, errorLayoutRes, null)
    }

    fun setNetworkErrorLayout(@LayoutRes networkErrorLayoutRes: Int) {
        this.mNetWorkErrorLayout = View.inflate(frameLayout.context, networkErrorLayoutRes, null)
    }

    fun setContentLayout(contentLayout: View) {
        this.mContentLayout = contentLayout
    }

    fun setEmptyLayout(emptyLayout: View) {
        this.mEmptyLayout = emptyLayout
    }

    fun setLoadingLayout(loadingLayout: View) {
        this.mContentLayout = loadingLayout
    }

    fun setErorrlayout(errorLayout: View) {
        this.mErrorLayout = errorLayout
    }

    fun setNetworkErrorLayout(networkErrorLayout: View) {
        this.mNetWorkErrorLayout = networkErrorLayout
    }

    fun getContentView(): View {
        return mContentLayout
    }

    fun getEmptyLayout(): View {
        return mEmptyLayout
    }

    fun getLoadingLayout(): View {
        return mLoadingLayout
    }

    fun getErrorLayout(): View {
        return mErrorLayout
    }

    fun getNetworkErrorLayout(): View {
        return mNetWorkErrorLayout
    }
}