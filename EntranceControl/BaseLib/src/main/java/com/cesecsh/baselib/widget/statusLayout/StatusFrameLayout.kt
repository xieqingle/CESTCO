package com.cesecsh.baselib.widget.statusLayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

/**
 * 作者：RockQ on 2018/7/16
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class StatusFrameLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), StatusInterface {
    /**
     * 构造函数
     */
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null, 0)

    /**
     * 布局托管器
     */
    private lateinit var mStatusLayoutFactory: StatusLayoutFactory
    private lateinit var mContextLayout: View
    private lateinit var mEmptyLayout: View
    private lateinit var mErrorLayout: View
    private lateinit var mNetworkErrorLayout: View
    private lateinit var mLoadingView: View
    private var mOnRetryClickListener: OnRetryClickListener? = null

    /**
     * 初始化
     */
    init {
        mStatusLayoutFactory = StatusLayoutFactory(this)
        mLoadingView = mStatusLayoutFactory.getLoadingLayout()
        mEmptyLayout = mStatusLayoutFactory.getEmptyLayout()
        mErrorLayout = mStatusLayoutFactory.getErrorLayout()
        mNetworkErrorLayout = mStatusLayoutFactory.getNetworkErrorLayout()
        addView(mLoadingView)
        addView(mEmptyLayout)
        addView(mErrorLayout)
        addView(mNetworkErrorLayout)
        initListener()
    }

    private fun initListener() {
        mEmptyLayout.setOnClickListener { it -> if (mOnRetryClickListener != null) mOnRetryClickListener!!.onRetryClick(it) }
        mErrorLayout.setOnClickListener { it -> if (mOnRetryClickListener != null) mOnRetryClickListener!!.onRetryClick(it) }
        mNetworkErrorLayout.setOnClickListener { it -> if (mOnRetryClickListener != null) mOnRetryClickListener!!.onRetryClick(it) }
    }

    public fun setOnRetryClickListener(onRetryClickListener: OnRetryClickListener) {
        this.mOnRetryClickListener = onRetryClickListener
    }

    companion object {
        /**
         * 状态 ： 内容显示
         */
        public const val STATUS_CONTENT = 0x11
        /**
         * 状态 ： 异常显示
         */
        public const val STATUS_ERROR = 0x12
        /**
         * 状态 ：加载中
         */
        public const val STATUS_LOADING = 0x13
        /**
         * 状态 ： 网络异常
         */
        public const val STATUS_NETWORK_ERROR = 0x14
        /**
         * 状态 ：数据空
         */
        public const val STATUS_EMPTY = 0x15

    }

    private var currentStatus = STATUS_LOADING

    override fun showContent() {
        setStatus(STATUS_CONTENT)
    }

    override fun showError() {
        setStatus(STATUS_ERROR)
    }

    override fun showLoading() {
        setStatus(STATUS_LOADING)
    }

    override fun showNetWorkError() {
        setStatus(STATUS_NETWORK_ERROR)
    }

    override fun showEmpty() {
        setStatus(STATUS_EMPTY)
    }

    /**
     * 流程：状态显示
     */

    private fun setStatus(status: Int) {
        this.currentStatus = status
        getContentView()
        when (currentStatus) {
            STATUS_CONTENT -> {
                mContextLayout.visibility = View.VISIBLE
                mEmptyLayout.visibility = View.GONE
                mErrorLayout.visibility = View.GONE
                mNetworkErrorLayout.visibility = View.GONE
                mLoadingView.visibility = View.GONE
            }
            STATUS_LOADING -> {
                mContextLayout.visibility = View.GONE
                mErrorLayout.visibility = View.GONE
                mEmptyLayout.visibility = View.GONE
                mNetworkErrorLayout.visibility = View.GONE
                mLoadingView.visibility = View.VISIBLE
            }
            STATUS_EMPTY -> {
                mContextLayout.visibility = View.GONE
                mErrorLayout.visibility = View.GONE
                mEmptyLayout.visibility = View.VISIBLE
                mNetworkErrorLayout.visibility = View.GONE
                mLoadingView.visibility = View.GONE
            }
            STATUS_NETWORK_ERROR -> {
                mContextLayout.visibility = View.GONE
                mErrorLayout.visibility = View.GONE
                mEmptyLayout.visibility = View.GONE
                mNetworkErrorLayout.visibility = View.VISIBLE
                mLoadingView.visibility = View.GONE
            }
            STATUS_ERROR -> {
                mContextLayout.visibility = View.GONE
                mEmptyLayout.visibility = View.GONE
                mErrorLayout.visibility = View.VISIBLE
                mNetworkErrorLayout.visibility = View.GONE
                mLoadingView.visibility = View.GONE
            }
        }

    }

    fun getStatus(): Int {
        return currentStatus;
    }


    private fun getContentView() {
        if (childCount != 4)
            mContextLayout = getChildAt(childCount - 1)
        else {
            mContextLayout = mStatusLayoutFactory.getContentView()
            addView(mContextLayout)
        }
    }

}