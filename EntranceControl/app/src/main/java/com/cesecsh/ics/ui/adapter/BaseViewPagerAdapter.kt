package com.cesecsh.ics.ui.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.cesecsh.baselib.common.BaseViewPage
import com.cesecsh.ics.R
import com.cesecsh.ics.widget.EntranceView

/**
 * 作者：RockQ on 2018/7/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class BaseViewPagerAdapter(var baseViewPages: List<BaseViewPage>) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    fun setBaseViewPagers(baseViewPages: List<BaseViewPage>) {
        this.baseViewPages = baseViewPages;
    }

    override fun getCount(): Int {
        return if (baseViewPages == null) 0
        else baseViewPages.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val baseViewPage = baseViewPages[position]
        val mRootView = baseViewPage.mRootView
        container.addView(mRootView)
        baseViewPage.initData()
        return mRootView

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
        container.removeView(`object` as View)
    }
}