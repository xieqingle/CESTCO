package cn.cestco.titlebarlibrary.titleBar;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by RockQ on 2017/11/7.
 * 功能:使用自定义标题栏，统一封装
 */

public class XToolBar extends RelativeLayout {
    /**
     * 左侧最右 view 的 id
     */
    private int mLeftLastViewId;
    /**
     * 右侧最左 view 的 id
     */
    private int mRightLastViewId;
    /**
     * 中间的 View
     */
    private View mCenterView;
    /**
     * 包裹 title 和 subTitle 的容器
     */
    private LinearLayout mTitleContainerView;
    /**
     * 显示 title 文字的 TextView
     */
    private TextView mTitleView;
    /**
     * 显示 subTitle 文字的 TextView
     */
    private TextView mSubTitleView;
    /**
     * 标题栏左侧 View 集合
     */
    private List<View> mLeftViewList;
    /**
     * 标题栏右侧 View 集合
     */
    private List<View> mRightViewList;
    /**
     * 标题栏分割线颜色
     */
    private int mTopBarSeparatorColor;
    /**
     * 标题栏背景
     */
    private int mTopBarBgColor;
    /**
     * 标题栏分割线的高度
     */
    private int mTopBarSeparatorHeight;

    private Drawable mTopBarBgWithSeparatorDrawableCache;

    private int mTitleGravity;
    private int mLeftBackDrawableRes;
    private int mTopbarHeight = -1;
    private int mTopbarImageBtnWidth = -1;
    private int mTopbarImageBtnHeight = -1;
    private int mTopbarTextBtnPaddingHorizontal = -1;
    private Rect mTitleContainerRect;

    public XToolBar(Context context) {
        this(context, null);
    }

    public XToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XToolBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }
}
