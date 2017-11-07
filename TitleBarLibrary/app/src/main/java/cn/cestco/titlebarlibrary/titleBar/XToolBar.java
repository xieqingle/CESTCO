package cn.cestco.titlebarlibrary.titleBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.cestco.titlebarlibrary.R;
import cn.cestco.titlebarlibrary.utils.DensityUtils;
import cn.cestco.titlebarlibrary.utils.ResourceUtils;

/**
 * Created by RockQ on 2017/11/7.
 * 功能:使用自定义标题栏，统一封装
 */

public class XToolBar extends RelativeLayout {
    /**
     * 上下文
     */
    private Context mContext;
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
    private int mToolBarSeparatorColor;
    /**
     * 标题栏背景
     */
    private int mToolBarBgColor;
    /**
     * 标题栏分割线的高度
     */
    private int mToolBarSeparatorHeight;

    private Drawable mToolBarBgWithSeparatorDrawableCache;

    /**
     * 设置标题 Gravity
     */
    private int mTitleGravity;
    /**
     * 左边返回按钮 drawable
     */
    private int mLeftBackDrawableRes;
    /**
     * 标题栏的高度 默认为88px
     */
    private int mToolBarHeight = DensityUtils.dp2px(mContext, 88);
    /**
     * 标题栏图片按钮的宽度
     */
    private int mToolBarImageBtnWidth = -1;
    /**
     * 标题栏图片按钮的高度
     */
    private int mToolBarImageBtnHeight = -1;
    /**
     * 标题栏水平方向的 padding
     */
    private int mToolBarTextBtnPaddingHorizontal = -1;
    /**
     *
     */
    private Rect mTitleContainerRect;

    public XToolBar(Context context) {
        this(context, null);
    }

    public XToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XToolBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initVar();// 初始化变量
        init(context, attrs, defStyle);
    }


    private void initVar() {
        mLeftLastViewId = R.id.default_title_bar_id;
        mLeftLastViewId = R.id.default_title_bar_id;
        mLeftViewList = new ArrayList<>();
        mRightViewList = new ArrayList<>();
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.XToolBar, defStyle, 0);
        mToolBarBgColor = typedArray.getColor(R.styleable.XToolBar_xToolBarBgColor, ResourceUtils.getColor(R.color.colorPrimary));
        mToolBarHeight = (int) typedArray.getDimension(R.styleable.XToolBar_xToolBarHeight, mToolBarHeight);
        mToolBarSeparatorColor = typedArray.getColor(R.styleable.XToolBar_xToolBarSeparatorColor, Color.WHITE);
        typedArray.recycle();
    }
}
