package com.cesecsh.baselib.widget.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.transition.Slide
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.cesecsh.baselib.R
import com.cesecsh.baselib.utils.StringUtils
import com.cesecsh.baselib.widget.view.alpha.AlphaRelativeLayout

/**
 * 作者：RockQ on 2018/7/18
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
open class MultiView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int = 0) : AlphaRelativeLayout(context, attributeSet, defStyleAttr) {
    companion object {
        const val DIVIDER_TYPE_NONE = 1
        const val DIVIDER_TYPE_TOP = 2
        const val DIVIDER_TYPE_BOTTOM = 3
        const val DIVIDER_TYPE_BOTH = 4

    }

    /**
     * 上下文
     */
    private var mContext: Context = context

    /**
     * 上面分割线
     */
    private var mViewTopLine: View? = null
    /**
     * 下面分割线
     */
    private var mViewBottomLine: View? = null
    /**
     * 左边 icon
     */
    private var mImgLeftView: ImageView? = null
    /**
     * 左边文本
     */
    private var mTvLeftTextView: TextView? = null
    /**
     * 右边 icon
     */
    private var mImgRightView: ImageView? = null
    /**
     * 右边文本
     */
    private var mTvRightTextView: TextView? = null

    /**
     * 左边 icon 图片
     */
    @DrawableRes
    private var mLeftIconSrc: Int = -1
    /**
     * 左边文本内容
     */
    private var mLeftText: String? = ""
    /**
     * 右边文本内容
     */
    private var mRightText: String? = ""
    /**
     * 右边 icon 图片
     */
    private var mRightIconSrc: Int = -1
    /**
     * 分割线类型
     */
    private var mDividerLineType: Int = DIVIDER_TYPE_NONE

    /**
     * 左边 icon 属性
     */
    private var mLeftIconPadding: Int = 0
    private var mLeftIconPaddingLeft: Int = dip2px(5f)
    private var mLeftIconPaddingTop: Int = dip2px(10f)
    private var mLeftIconPaddingRight: Int = dip2px(5f)
    private var mLeftIconPaddingBottom: Int = dip2px(10f)
    private var mLeftIconMargin: Int = 0
    private var mLeftIconMarginLeft: Int = 0
    private var mLeftIconMarginTop: Int = 0
    private var mLeftIconMarginRight: Int = 0
    private var mLeftIconMarginBottom: Int = 0
    private var mLeftIconVisible: Int = 1
    /**
     * 左边文本属性
     */
    private var mLeftTextPadding: Int = 0
    private var mLeftTextPaddingLeft: Int = dip2px(5f)
    private var mLeftTextPaddingTop: Int = dip2px(10f)
    private var mLeftTextPaddingRight: Int = dip2px(5f)
    private var mLeftTextPaddingBottom: Int = dip2px(10f)
    private var mLeftTextMargin: Int = 0
    private var mLeftTextMarginLeft: Int = 0
    private var mLeftTextMarginTop: Int = 0
    private var mLeftTextMarginRight: Int = 0
    private var mLeftTextMarginBottom: Int = 0
    private var mLeftTextGravity: Int = Gravity.START
    private var mLeftTextSize: Int = sp2px(14f)
    private var mLeftTextColor: Int = Color.BLACK
    private var mLeftTextSingleLine: Boolean = false
    private var mLeftTextLines: Int = 1
    private var mLeftTextEllipsize: Int = 1
    /**
     * 右边文本属性
     */
    private var mRightTextPadding: Int = 0
    private var mRightTextPaddingLeft: Int = dip2px(10f)
    private var mRightTextPaddingTop: Int = dip2px(10f)
    private var mRightTextPaddingRight: Int = dip2px(5f)
    private var mRightTextPaddingBottom: Int = dip2px(10f)
    private var mRightTextMargin: Int = 0
    private var mRightTextMarginLeft: Int = 0
    private var mRightTextMarginTop: Int = 0
    private var mRightTextMarginRight: Int = 0
    private var mRightTextMarginBottom: Int = 0
    private var mRightTextGravity: Int = Gravity.END
    private var mRightTextSize: Int = sp2px(12f)
    private var mRightTextColor: Int = Color.BLACK
    private var mRightTextSingleLine: Boolean = false
    private var mRightTextLines: Int = 1
    private var mRightTextEllipsize: Int = 1
    /**
     * 右边 icon 属性
     */
    private var mRightIconPadding: Int = 0
    private var mRightIconPaddingLeft: Int = dip2px(5f)
    private var mRightIconPaddingTop: Int = dip2px(10f)
    private var mRightIconPaddingRight: Int = dip2px(10f)
    private var mRightIconPaddingBottom: Int = dip2px(10f)
    private var mRightIconMargin: Int = 0
    private var mRightIconMarginLeft: Int = 0
    private var mRightIconMarginTop: Int = 0
    private var mRightIconMarginRight: Int = 0
    private var mRightIconMarginBottom: Int = 0
    private var mRightIconVisible: Int = 1

    /**
     * 分割线属性
     */
    private var mDividerMarginLeftRight: Int = 0
    private var mDividerHeight: Float = 1f
    private var mDividerColor: Int = 0


    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null, 0)

    /**
     * 初始化
     */
    init {
        initAttrs(context, attributeSet)
        initView()
    }

    /**
     * 初始化属性
     */
    private fun initAttrs(context: Context, attributeSet: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.MultiView)
        mLeftIconSrc = typedArray.getResourceId(R.styleable.MultiView_mLeftIconSrc, -1)
        mLeftText = typedArray.getString(R.styleable.MultiView_mLeftText)
        mRightIconSrc = typedArray.getResourceId(R.styleable.MultiView_mRightIconSrc, -1)
        mRightText = typedArray.getString(R.styleable.MultiView_mRightText)
        mDividerLineType = typedArray.getInt(R.styleable.MultiView_mDividerLineType, DIVIDER_TYPE_NONE)
        initLeftIconAttrs(typedArray)
        initLeftTextAttrs(typedArray)
        initRightTextAttrs(typedArray)
        initRightIconAttrs(typedArray)
        initDividerAttrs(typedArray)
        typedArray.recycle()
    }

    /**
     * 初始化左边 icon 属性
     */

    private fun initLeftIconAttrs(typedArray: TypedArray) {
        mLeftIconPadding = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftIconPadding, 0)
        mLeftIconPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftIconPaddingLeft, mLeftIconPaddingLeft)
        mLeftIconPaddingTop = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftIconPaddingTop, mLeftIconPaddingTop)
        mLeftIconPaddingRight = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftIconPaddingRight, mLeftIconPaddingRight)
        mLeftIconPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftIconPaddingBottom, mLeftIconPaddingBottom)
        mLeftIconMargin = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftIconMargin, 0)
        mLeftIconMarginLeft = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftIconMarginLeft, 0)
        mLeftIconMarginTop = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftIconMarginTop, 0)
        mLeftIconMarginRight = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftIconMarginRight, 0)
        mLeftIconMarginBottom = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftIconMarginBottom, 0)
        mLeftIconVisible = typedArray.getInt(R.styleable.MultiView_mLeftIconVisible, 1)
    }

    /**
     * 初始化左边文本属性
     */
    private fun initLeftTextAttrs(typedArray: TypedArray) {
        mLeftTextPadding = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftTextPadding, 0)
        mLeftTextPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftTextPaddingLeft, mLeftTextPaddingLeft)
        mLeftTextPaddingTop = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftTextPaddingTop, mLeftTextPaddingTop)
        mLeftTextPaddingRight = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftTextPaddingRight, mLeftTextPaddingRight)
        mLeftTextPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftTextPaddingBottom, mLeftTextPaddingBottom)
        mLeftTextMargin = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftTextMargin, 0)
        mLeftTextMarginLeft = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftTextMarginLeft, 0)
        mLeftTextMarginTop = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftTextMarginTop, 0)
        mLeftTextMarginRight = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftTextMarginRight, 0)
        mLeftTextMarginBottom = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftTextMarginBottom, 0)
        mLeftTextGravity = typedArray.getInt(R.styleable.MultiView_mLeftTextGravity, mLeftTextGravity)
        mLeftTextSize = typedArray.getDimensionPixelSize(R.styleable.MultiView_mLeftTextSize, mLeftTextSize)
        mLeftTextColor = typedArray.getColor(R.styleable.MultiView_mLeftTextColor, resources.getColor(R.color.normalColor))
        mLeftTextSingleLine = typedArray.getBoolean(R.styleable.MultiView_mLeftTextSingleLine, false)
        mLeftTextLines = typedArray.getInteger(R.styleable.MultiView_mLeftTextLines, 1)
        mLeftTextEllipsize = typedArray.getInt(R.styleable.MultiView_mLeftTextEllipsize, 1)


    }


    /**
     * 初始化右边文本属性
     */
    private fun initRightTextAttrs(typedArray: TypedArray) {
        mRightTextPadding = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightTextPadding, 0)
        mRightTextPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightTextPaddingLeft, mRightTextPaddingLeft)
        mRightTextPaddingTop = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightTextPaddingTop, mRightTextPaddingTop)
        mRightTextPaddingRight = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightTextPaddingRight, mRightTextPaddingRight)
        mRightTextPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightTextPaddingBottom, mRightTextPaddingBottom)
        mRightTextMargin = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightTextMargin, 0)
        mRightTextMarginLeft = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightTextMarginLeft, 0)
        mRightTextMarginTop = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightTextMarginTop, 0)
        mRightTextMarginRight = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightTextMarginRight, 0)
        mRightTextMarginBottom = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightTextMarginBottom, 0)
        mRightTextGravity = typedArray.getInt(R.styleable.MultiView_mRightTextGravity, mRightTextGravity)
        mRightTextSize = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightTextSize, mRightTextSize)
        mRightTextColor = typedArray.getColor(R.styleable.MultiView_mRightTextColor, resources.getColor(R.color.hintColor))
        mRightTextSingleLine = typedArray.getBoolean(R.styleable.MultiView_mRightTextSingleLine, false)
        mRightTextLines = typedArray.getInteger(R.styleable.MultiView_mRightTextLines, 1)
        mRightTextEllipsize = typedArray.getInt(R.styleable.MultiView_mRightTextEllipsize, 1)
    }

    /**
     * 初始化右边 icon 属性
     */
    private fun initRightIconAttrs(typedArray: TypedArray) {
        mRightIconPadding = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightIconPadding, 0)
        mRightIconPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightIconPaddingLeft, mRightIconPaddingLeft)
        mRightIconPaddingTop = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightIconPaddingTop, mRightIconPaddingTop)
        mRightIconPaddingRight = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightIconPaddingRight, mRightIconPaddingRight)
        mRightIconPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightIconPaddingBottom, mRightIconPaddingBottom)
        mRightIconMargin = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightIconMargin, 0)
        mRightIconMarginLeft = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightIconMarginLeft, 0)
        mRightIconMarginTop = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightIconMarginTop, 0)
        mRightIconMarginRight = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightIconMarginRight, 0)
        mRightIconMarginBottom = typedArray.getDimensionPixelSize(R.styleable.MultiView_mRightIconMarginBottom, 0)
        mRightIconVisible = typedArray.getInt(R.styleable.MultiView_mRightIconVisible, 1)
    }

    /**
     * 初始化分割线属性
     */
    private fun initDividerAttrs(typedArray: TypedArray) {
        mDividerMarginLeftRight = typedArray.getDimensionPixelSize(R.styleable.MultiView_mDividerMarginLeftRight, 0)
        mDividerHeight = typedArray.getDimensionPixelSize(R.styleable.MultiView_mDividerHeight, 1).toFloat()
        mDividerColor = typedArray.getColor(R.styleable.MultiView_mDividerColor, resources.getColor(R.color.separatorColor))


    }

    /**
     * 初始化布局
     */
    private fun initView() {
        initLeftView()
        initRightView()
        initLines()


    }

    /**
     * 初始化分割线布局
     */
    private fun initLines() {
        when (mDividerLineType) {
            // 默认表示没有分割线
            DIVIDER_TYPE_NONE -> {
                return
            }
            // 只显示上面分割线
            DIVIDER_TYPE_TOP -> {
                setDividerType(DIVIDER_TYPE_TOP)
            }
            // 只显示下面分割线
            DIVIDER_TYPE_BOTTOM -> {
                setDividerType(DIVIDER_TYPE_BOTTOM)
            }
            // 显示上下两个分割线
            DIVIDER_TYPE_BOTH -> {
                setDividerType(DIVIDER_TYPE_BOTH)
            }

        }

    }


    /**
     * 设置分割线类型
     */
    public fun setDividerType(type: Int): MultiView {
        mDividerLineType = type
        when (type) {
            // 默认表示没有分割线
            DIVIDER_TYPE_NONE -> return this
            // 只显示上面分割线
            DIVIDER_TYPE_TOP -> {
                initTopLineView()
            }
            // 只显示下面分割线
            DIVIDER_TYPE_BOTTOM -> {
                initBottomView()
            }
            // 显示上下两个分割线
            DIVIDER_TYPE_BOTH -> {
                initTopLineView()
                initBottomView()
            }
        }
        return this

    }

    /**
     * 初始化上分割线视图
     */
    private fun initTopLineView() {
        mViewTopLine = View(mContext)
        val mViewTopLineLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, (mDividerHeight + 0.5).toInt())
        mViewTopLineLayoutParams.addRule(ALIGN_PARENT_TOP, TRUE)
        mViewTopLineLayoutParams.setMargins(mDividerMarginLeftRight, 0, mDividerMarginLeftRight, 0)
        mViewTopLine?.layoutParams = mViewTopLineLayoutParams
        mViewTopLine?.setBackgroundColor(mDividerColor)
        addView(mViewTopLine)
    }

    /**
     * 初始化下分割线视图
     */
    private fun initBottomView() {
        mViewBottomLine = View(mContext)
        val mViewBottomViewLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, (mDividerHeight + 0.5).toInt())
        mViewBottomViewLayoutParams.setMargins(mDividerMarginLeftRight, 0, mDividerMarginLeftRight, 0)
        mViewBottomViewLayoutParams.addRule(ALIGN_PARENT_BOTTOM, TRUE)
        mViewBottomLine?.setBackgroundColor(mDividerColor)
        mViewBottomLine?.layoutParams = mViewBottomViewLayoutParams
        addView(mViewBottomLine)
    }


    /**
     * 初始化左边视图
     */
    private fun initLeftView() {
        initLeftIconView()
        initLeftTextView()
    }

    /**
     * 初始化左文本视图
     */
    private fun initLeftTextView() {
        if (mTvLeftTextView == null)
            mTvLeftTextView = TextView(mContext)
        mTvLeftTextView?.id = R.id.leftViewId
//        设置左视图 layoutParams
        val leftTextParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        leftTextParams.addRule(CENTER_VERTICAL, TRUE)
        leftTextParams.addRule(RIGHT_OF, R.id.leftIconViewId)
        if (mLeftTextMargin > 0) {
            leftTextParams.setMargins(mLeftTextMargin, mLeftTextMargin, mLeftTextMargin, mLeftTextMargin)
        }
        if (mLeftTextMarginLeft > 0 && mLeftTextMarginTop > 0 && mLeftTextMarginRight > 0 && mLeftTextMarginBottom > 0)
            leftTextParams.setMargins(mLeftTextMarginLeft, mLeftTextMarginTop, mLeftTextMarginRight, mLeftTextMarginBottom)
        mTvLeftTextView?.layoutParams = leftTextParams

        //       设置padding
        mTvLeftTextView?.setPadding(mLeftTextPaddingLeft, mLeftTextPaddingTop, mLeftTextPaddingRight, mLeftTextPaddingBottom)

        if (mLeftTextPadding > 0) {
            mTvLeftTextView?.setPadding(mLeftTextPadding, mLeftTextPadding, mLeftTextPadding, mLeftTextPadding)
        }
//        设置文本
        if (!StringUtils.isNullOrEmpty(mLeftText)) {
            mTvLeftTextView?.text = this.mLeftText
            mTvLeftTextView?.visibility = View.VISIBLE
        } else mTvLeftTextView?.visibility = View.GONE
//        设置其他属性
        mTvLeftTextView?.setSingleLine(mLeftTextSingleLine)
        mTvLeftTextView?.setLines(mLeftTextLines)
        mTvLeftTextView?.gravity = mLeftTextGravity
        mTvLeftTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize.toFloat())
        mTvLeftTextView?.setTextColor(mLeftTextColor)
        when (mLeftTextEllipsize) {
            1 -> mTvLeftTextView?.ellipsize = TextUtils.TruncateAt.END
            2 -> mTvLeftTextView?.ellipsize = TextUtils.TruncateAt.START
        }
        addView(mTvLeftTextView)
    }

    /**
     * 初始化左侧 icon 视图
     */
    private fun initLeftIconView() {
        if (mImgLeftView == null)
            mImgLeftView = ImageView(mContext)
        mImgLeftView?.id = R.id.leftIconViewId
        if (mLeftIconSrc != -1)
            mImgLeftView?.setImageResource(mLeftIconSrc)
//        设置左边 icon params
        val leftIconParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        if (mLeftIconMargin > 0)
            leftIconParams.setMargins(mLeftIconMargin, mLeftIconMargin, mLeftIconMargin, mLeftIconMargin)
        if (mLeftIconMarginLeft > 0 && mLeftIconMarginTop > 0 && mLeftIconMarginRight > 0 && mLeftIconMarginBottom > 0) {
            leftIconParams.setMargins(mLeftIconMarginLeft, mLeftIconMarginTop, mLeftIconMarginRight, mLeftIconMarginBottom)
        }
        leftIconParams.addRule(CENTER_VERTICAL, TRUE)
        leftIconParams.addRule(ALIGN_PARENT_LEFT, TRUE)
        mImgLeftView?.layoutParams = leftIconParams

        mImgLeftView?.setPadding(mLeftIconPaddingLeft, mLeftIconPaddingTop, mLeftIconPaddingRight, mLeftIconPaddingBottom)
        if (mLeftIconPadding > 0)
            mImgLeftView?.setPadding(mLeftIconPadding, mLeftIconPadding, mLeftIconPadding, mLeftIconPadding)
        mImgLeftView?.visibility = if (mLeftIconVisible == 1) View.VISIBLE else View.GONE
        addView(mImgLeftView)
    }


    /**
     * 初始化右视图
     */
    private fun initRightView() {
        initRightIconView()
        initRightTextView()

    }

    /**
     * 初始化右文本视图
     */

    private fun initRightTextView() {
        if (mTvRightTextView == null)
            mTvRightTextView = TextView(mContext)
        mTvRightTextView?.id = R.id.rightViewId
        val rightTextParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        rightTextParams.addRule(CENTER_VERTICAL, TRUE)
        rightTextParams.addRule(LEFT_OF, R.id.rightIconViewId)
        rightTextParams.addRule(RIGHT_OF, R.id.leftViewId)
        if (mRightTextMargin > 0) {
            rightTextParams.setMargins(mRightTextMargin, mRightTextMargin, mRightTextMargin, mRightTextMargin)
        }
        if (mRightTextMarginLeft > 0 && mRightTextMarginTop > 0 && mRightTextMarginRight > 0 && mRightTextMarginBottom > 0) {
            rightTextParams.setMargins(mRightTextMarginLeft, mRightTextMarginTop, mRightTextMarginRight, mRightTextMarginBottom)
        }
        mTvRightTextView?.layoutParams = rightTextParams
        mTvRightTextView?.setPadding(mRightTextPaddingLeft, mRightTextPaddingTop, mRightTextPaddingRight, mRightTextPaddingBottom)
        if (mRightTextPadding > 0) {
            mTvRightTextView?.setPadding(mRightTextPadding, mRightTextPadding, mRightTextPadding, mRightTextPadding)
        }
        mTvRightTextView?.gravity = mRightTextGravity
        mTvRightTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize.toFloat())
        mTvRightTextView?.setTextColor(mRightTextColor)
        mTvRightTextView?.setSingleLine(mRightTextSingleLine)
        mTvRightTextView?.setLines(mRightTextLines)
        when (mRightTextEllipsize) {
            1 -> mTvRightTextView?.ellipsize = TextUtils.TruncateAt.END
            2 -> mTvRightTextView?.ellipsize = TextUtils.TruncateAt.START
        }
        if (!StringUtils.isNullOrEmpty(mRightText)) {
            mTvRightTextView?.text = mRightText
            mTvRightTextView?.visibility = View.VISIBLE
        } else mTvRightTextView?.visibility = View.GONE
        addView(mTvRightTextView)
    }

    /**
     * 初始化右 icon 视图
     */
    private fun initRightIconView() {
        if (mImgRightView == null)
            mImgRightView = ImageView(mContext)
        mImgRightView?.id = R.id.rightIconViewId
        if (mRightIconSrc != -1)
            mImgRightView?.setImageResource(mRightIconSrc)
        val rightViewParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        rightViewParams.addRule(CENTER_VERTICAL, TRUE)
        rightViewParams.addRule(ALIGN_PARENT_RIGHT, TRUE)
        if (mRightIconMargin > 0)
            rightViewParams.setMargins(mRightIconMargin, mRightIconMargin, mRightIconMargin, mRightIconMargin)
        if (mRightIconMarginLeft > 0 && mRightIconMarginTop > 0 && mRightIconMarginRight > 0 && mRightIconMarginBottom > 0) {
            rightViewParams.setMargins(mRightIconMarginLeft, mRightIconMarginTop, mRightIconMarginRight, mRightIconMarginBottom)
        }
        mImgRightView?.layoutParams = rightViewParams
        mImgRightView?.setPadding(mRightIconPaddingLeft, mRightIconPaddingTop, mRightIconPaddingRight, mRightIconPaddingBottom)
        if (mRightIconPadding > 0) {
            mImgRightView?.setPadding(mRightIconPadding, mRightIconPadding, mRightIconPadding, mRightIconPadding)
        }
        mImgRightView?.visibility = if (mRightIconVisible == 1) View.VISIBLE else View.GONE
        addView(mImgRightView)

    }

    /**
     * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
     */
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
//        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
//        val sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec)
//        val sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec)
//        measureChild(mTvLeftTextView, widthMeasureSpec, heightMeasureSpec)
//        measureChild(mTvRightTextView, widthMeasureSpec, heightMeasureSpec)
//        measureChild(mImgLeftView, widthMeasureSpec, heightMeasureSpec)
//        measureChild(mImgRightView, widthMeasureSpec, heightMeasureSpec)
//        var heightMeasureSpecTemp = heightMeasureSpec
////        获取左边 icon 的横纵 margin
//        var lp = mImgLeftView?.layoutParams as LayoutParams
//        val leftIconHorizontalMargin = lp.leftMargin + lp.rightMargin
//        val leftIconVerticalMargin = lp.topMargin + lp.bottomMargin
////        获取左边 文本 的横纵 margin
//        lp = mTvLeftTextView?.layoutParams as LayoutParams
//        val leftTextHorizontalMargin = lp.leftMargin + lp.rightMargin
//        val leftTextVerticalMargin = lp.topMargin + lp.bottomMargin
//
////        获取右边 icon 的横纵 margin
//        lp = mImgRightView?.layoutParams as LayoutParams
//        val rightIconHorizontalMargin = lp.leftMargin + lp.rightMargin
//        val rightIconVerticalMargin = lp.topMargin + lp.bottomMargin
////        获取右边 文本 的横纵 margin
//        lp = mTvRightTextView?.layoutParams as LayoutParams
//        val rightTextHorizontalMargin = lp.leftMargin + lp.rightMargin
//        val rightTextVerticalMargin = lp.topMargin + lp.bottomMargin
//        if (heightMode != MeasureSpec.EXACTLY && childCount > 0) {
//            val leftIconHeight = (mImgLeftView?.measuredHeight ?: 0) + leftIconVerticalMargin
//            val leftTextHeight = (mTvLeftTextView?.measuredHeight ?: 0) + leftTextVerticalMargin
//            val leftMaxHeight = Math.max(leftIconHeight, leftTextHeight)
//            val rightIconHeight = (mImgRightView?.measuredHeight ?: 0) + rightIconVerticalMargin
//            val rightTextHeight = (mTvRightTextView?.measuredHeight ?: 0) + rightTextVerticalMargin
//            val rightMaxHeight = Math.max(rightIconHeight, rightTextHeight)
//            val maxHeight = Math.max(leftMaxHeight, rightMaxHeight)
//            val dividerHeight: Float = when (mDividerLineType) {
//                DIVIDER_TYPE_BOTH -> mDividerHeight * 2
//                DIVIDER_TYPE_BOTTOM -> mDividerHeight
//                DIVIDER_TYPE_TOP -> mDividerHeight
//                else -> 0f
//            }
//
//            val viewHeight = maxHeight + paddingTop + paddingBottom + dividerHeight
//            heightMeasureSpecTemp = MeasureSpec.makeMeasureSpec((viewHeight + 0.5f).toInt(), MeasureSpec.EXACTLY)
//            super.onMeasure(widthMeasureSpec, heightMeasureSpecTemp)
//        } else
//            super.onMeasure(widthMeasureSpec, heightMeasureSpecTemp)
//
//        val availableWidth = sizeWidth - paddingLeft - paddingRight - (mImgLeftView?.measuredWidth
//                ?: 0) - (mImgRightView?.measuredWidth
//                ?: 0) - leftIconHorizontalMargin - rightIconHorizontalMargin - leftTextHorizontalMargin - rightTextHorizontalMargin
//        val leftTvWidth = (mTvLeftTextView?.measuredWidth ?: 0) + leftTextHorizontalMargin
//        val rightTvWidth = (mTvRightTextView?.measuredWidth ?: 0) + rightTextHorizontalMargin
//        val textViewWidth = leftTvWidth + rightTvWidth
//        var leftViewSpec: Int = 0
//        var rightViewSpec: Int = 0
//        if (textViewWidth > availableWidth) {
//            if (mTvLeftTextView?.visibility != View.GONE
//                    && mTvRightTextView?.visibility != View.GONE
//                    && mTvLeftTextView?.visibility != View.INVISIBLE
//                    && mTvRightTextView?.visibility != View.INVISIBLE) {
//                if (leftTvWidth > availableWidth / 2) {
//                    leftViewSpec = View.MeasureSpec.makeMeasureSpec(availableWidth / 2, MeasureSpec.EXACTLY)
//                    rightViewSpec = View.MeasureSpec.makeMeasureSpec(availableWidth / 2, MeasureSpec.EXACTLY)
//                } else {
//                    leftViewSpec = View.MeasureSpec.makeMeasureSpec(leftTvWidth, MeasureSpec.EXACTLY)
//                    rightViewSpec = View.MeasureSpec.makeMeasureSpec(availableWidth - leftTvWidth, MeasureSpec.EXACTLY)
//                }
//            } else {
//                leftViewSpec = View.MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.EXACTLY)
//                rightViewSpec = View.MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.EXACTLY)
//            }
//        } else {
////            val availableLeftWidth = (mTvLeftTextView?.measuredWidth
////                    ?: 0) + rightTextHorizontalMargin
////            val availableRightWidth = availableWidth - availableLeftWidth
////            val layoutParams = mTvRightTextView?.layoutParams
////            layoutParams?.width = sizeWidth
////            mTvRightTextView?.layoutParams = layoutParams
////            leftViewSpec = View.MeasureSpec.makeMeasureSpec(availableLeftWidth, MeasureSpec.EXACTLY)
////            rightViewSpec = View.MeasureSpec.makeMeasureSpec(sizeWidth+10000, MeasureSpec.EXACTLY)
//        }
//        mTvLeftTextView?.measure(leftViewSpec, heightMeasureSpecTemp)
//        mTvRightTextView?.measure(rightViewSpec, heightMeasureSpecTemp)
//
//    }


    //    /////////////////////////////////////////////////////////////////
    /**
     * 判断左边 icon 是否存在
     */
    private fun checkLeftIconNull() {
        if (mImgLeftView == null)
            initLeftIconView()
    }

    /**
     * 设置左边 icon 图片资源
     * @param drawableRes 图片
     */
    public fun setLeftIconRes(@DrawableRes drawableRes: Int): MultiView {
        checkLeftIconNull()
        mImgLeftView!!.setImageResource(drawableRes)
        return this
    }

    /**
     * 设置左边 icon 的内边距 padding
     * @param padding 距离左、上、右、下的距离
     */
    public fun setLeftIconPadding(padding: Int): MultiView {
        return setLeftIconPadding(padding, padding, padding, padding)
    }

    /**
     * 设置左边 icon 的内边距 padding
     * @param left 左边距
     * @param top 上边距
     *@param right 右边距
     * @param bottom 下边距
     */
    public fun setLeftIconPadding(left: Int, top: Int, right: Int, bottom: Int): MultiView {
        checkLeftIconNull()
        mImgLeftView!!.setPadding(left, top, right, bottom)
        return this
    }

    /**
     * 设置左边 icon 的外边距 margin
     * @param margin 距离左、上、右、下的外边距值
     */
    public fun setLeftIconMargin(margin: Int): MultiView {
        return setLeftIconMargin(margin, margin, margin, margin)
    }

    /**
     * 设置左边 icon 的外边距 margin
     * @param left 左边距
     * @param top 上边距
     *@param right 右边距
     * @param bottom 下边距
     */
    public fun setLeftIconMargin(left: Int, top: Int, right: Int, bottom: Int): MultiView {
        checkLeftIconNull()
        val layoutParams = mImgLeftView!!.layoutParams as RelativeLayout.LayoutParams
        layoutParams.setMargins(left, top, right, bottom)
        mImgLeftView!!.layoutParams = layoutParams
        return this

    }


    //    /////////////////////////////////////////////////////////////////
    /**
     * 判断右边 icon 是否存在
     */
    private fun checkRightIconNull() {
        if (mImgRightView == null)
            initRightIconView()
    }

    /**
     * 设置右边 icon 图片资源
     * @param drawableRes 图片
     */
    public fun setRightIconRes(@DrawableRes drawableRes: Int): MultiView {
        checkRightIconNull()
        mImgRightView!!.setImageResource(drawableRes)
        return this
    }

    /**
     * 设置右边 icon 的内边距 padding
     * @param padding 距离左、上、右、下的距离
     */
    public fun setRightIconPadding(padding: Int): MultiView {
        return setRightIconPadding(padding, padding, padding, padding)
    }

    /**
     * 设置右边 icon 的内边距 padding
     * @param left 左边距
     * @param top 上边距
     *@param right 右边距
     * @param bottom 下边距
     */
    public fun setRightIconPadding(left: Int, top: Int, right: Int, bottom: Int): MultiView {
        checkRightIconNull()
        mImgRightView!!.setPadding(left, top, right, bottom)
        return this
    }

    /**
     * 设置右边 icon 的外边距 margin
     * @param margin 距离左、上、右、下的外边距值
     */
    public fun setRightIconMargin(margin: Int): MultiView {
        return setRightIconMargin(margin, margin, margin, margin)
    }

    /**
     * 设置右边 icon 的外边距 margin
     * @param left 左边距
     * @param top 上边距
     *@param right 右边距
     * @param bottom 下边距
     */
    public fun setRightIconMargin(left: Int, top: Int, right: Int, bottom: Int): MultiView {
        checkRightIconNull()
        val layoutParams = mImgRightView!!.layoutParams as RelativeLayout.LayoutParams
        layoutParams.setMargins(left, top, right, bottom)
        mImgRightView!!.layoutParams = layoutParams
        return this

    }
    //    /////////////////////////////////////////////////////////////////
    /**
     * 检查左边文本是否存在
     */
    private fun checkLeftTextNull() {
        if (mTvLeftTextView == null)
            initLeftTextView()
    }

    /**
     * 设置左边文本
     * @param charSequence 字符串格式
     */
    public fun setLeftText(charSequence: CharSequence): MultiView {
        checkLeftTextNull()
        if (mTvLeftTextView!!.visibility != View.VISIBLE)
            mTvLeftTextView!!.visibility = View.VISIBLE
        mTvLeftTextView!!.text = charSequence
        return this
    }

    /**
     * 设置左边文本
     * @param stringRes 文本内容
     */
    public fun setLeftText(@StringRes stringRes: Int): MultiView {
        checkLeftTextNull()
        if (mTvLeftTextView!!.visibility != View.VISIBLE)
            mTvLeftTextView!!.visibility = View.VISIBLE
        mTvLeftTextView!!.text = resources.getText(stringRes)
        return this
    }

    /**
     * 设置左边文本颜色
     * @param color 颜色值
     */
    public fun setLeftTextColor(@ColorInt color: Int): MultiView {
        checkLeftTextNull()
        mTvLeftTextView!!.setTextColor(color)
        return this
    }

    /**
     * 设置左边文本的字体大小
     * @param textSize 字体大小
     */
    public fun setLeftTextSize(textSize: Float): MultiView {
        checkLeftTextNull()
        mTvLeftTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, sp2px(textSize).toFloat())
        return this
    }

    /**
     * 设置左边文本显示单行
     * @param singleLine 是否单行显示
     */
    public fun setLeftTextSingleLine(singleLine: Boolean): MultiView {
        checkLeftTextNull()
        mTvLeftTextView!!.setSingleLine(singleLine)
        return this
    }

    /**
     * 设置左边文本的行数
     * @param lines 文本行数
     */
    public fun setLeftTextLines(lines: Int): MultiView {
        checkLeftTextNull()
        mTvLeftTextView!!.setLines(lines)
        return this
    }

    /**
     * 设置文本的省略文字的位置
     * @param ellipsize 省略文字的位置
     */
    public fun setLeftTextEllipsize(ellipsize: TextUtils.TruncateAt): MultiView {
        checkLeftTextNull()
        mTvLeftTextView!!.ellipsize = ellipsize
        return this
    }

    /**
     * 设置左边文本的 gravity
     * @param gravity
     */
    public fun setLeftTextGravity(@Slide.GravityFlag gravity: Int): MultiView {
        checkLeftTextNull()
        mTvLeftTextView!!.gravity = gravity
        return this
    }

    /**
     * 设置左边文本的内边距 padding
     * @param padding 距离左、上、右、下的距离
     */
    public fun setLeftTextPadding(padding: Int): MultiView {
        return setLeftTextPadding(padding, padding, padding, padding)
    }

    /**
     * 设置左边文本的内边距 padding
     * @param left 左距离值
     * @param top 上距离值
     * @param right 右距离值
     * @param bottom 下距离值
     */
    public fun setLeftTextPadding(left: Int, top: Int, right: Int, bottom: Int): MultiView {
        checkLeftTextNull()
        mTvLeftTextView!!.setPadding(left, top, right, bottom)
        return this
    }

    /**
     * 设置左边文本的外边距 margin
     * @param margin 距离左、上、右、下的距离
     */
    public fun setLeftTextMargin(margin: Int): MultiView {
        return setLeftTextMargin(margin, margin, margin, margin)
    }

    /**
     * 设置左边文本的外边距 margin
     * @param left 左距离值
     * @param top 上距离值
     * @param right 右距离值
     * @param bottom 下距离值
     */
    public fun setLeftTextMargin(left: Int, top: Int, right: Int, bottom: Int): MultiView {
        checkLeftTextNull()
        val layoutParams = mTvLeftTextView!!.layoutParams as RelativeLayout.LayoutParams
        layoutParams.setMargins(left, top, right, bottom)
        mTvLeftTextView!!.layoutParams = layoutParams
        return this

    }

    /**
     * 获取右边view
     * @return 左边文本view
     */
    public fun getLeftTextView(): TextView {
        checkLeftTextNull()
        return mTvLeftTextView!!
    }

    //    ////////////////////////////////////////////////////////////////////////

    /**
     * 检查右边文本是否存在
     */
    private fun checkRightTextNull() {
        if (mTvRightTextView == null)
            initRightTextView()
    }

    /**
     * 设置右边文本
     * @param charSequence 字符串格式
     */
    public fun setRightText(charSequence: CharSequence): MultiView {
        checkRightTextNull()
        if (mTvRightTextView!!.visibility != View.VISIBLE)
            mTvRightTextView!!.visibility = View.VISIBLE
        mTvRightTextView!!.text = charSequence
        return this
    }

    /**
     * 设置右边文本
     * @param stringRes 文本内容
     */
    public fun setRightText(@StringRes stringRes: Int): MultiView {
        checkRightTextNull()
        if (mTvRightTextView!!.visibility != View.VISIBLE)
            mTvRightTextView!!.visibility = View.VISIBLE
        mTvRightTextView!!.text = resources.getText(stringRes)
        return this
    }

    /**
     * 设置右边文本颜色
     * @param color 颜色值
     */
    public fun setRightTextColor(@ColorInt color: Int): MultiView {
        checkRightTextNull()
        mTvRightTextView!!.setTextColor(color)
        return this
    }

    /**
     * 设置右边文本的字体大小
     * @param textSize 字体大小
     */
    public fun setRightTextSize(textSize: Float): MultiView {
        checkRightTextNull()
        mTvRightTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, sp2px(textSize).toFloat())
        return this
    }

    /**
     * 设置右边文本显示单行
     * @param singleLine 是否单行显示
     */
    public fun setRightTextSingleLine(singleLine: Boolean): MultiView {
        checkRightTextNull()
        mTvRightTextView!!.setSingleLine(singleLine)
        return this
    }

    /**
     * 设置右边文本的行数
     * @param lines 文本行数
     */
    public fun setRightTextLines(lines: Int): MultiView {
        checkRightTextNull()
        mTvRightTextView!!.setLines(lines)
        return this
    }

    /**
     * 设置文本的省略文字的位置
     * @param ellipsize 省略文字的位置
     */
    public fun setRightTextEllipsize(ellipsize: TextUtils.TruncateAt): MultiView {
        checkRightTextNull()
        mTvRightTextView!!.ellipsize = ellipsize
        return this
    }

    /**
     * 设置右边文本的 gravity
     * @param gravity
     */
    public fun setRightTextGravity(@Slide.GravityFlag gravity: Int): MultiView {
        checkRightTextNull()
        mTvRightTextView!!.gravity = gravity
        return this
    }

    /**
     * 设置右边文本的内边距 padding
     * @param padding 距离左、上、右、下的距离
     */
    public fun setRightTextPadding(padding: Int): MultiView {
        return setRightTextPadding(padding, padding, padding, padding)
    }

    /**
     * 设置右边文本的内边距 padding
     * @param left 左距离值
     * @param top 上距离值
     * @param right 右距离值
     * @param bottom 下距离值
     */
    public fun setRightTextPadding(left: Int, top: Int, right: Int, bottom: Int): MultiView {
        checkRightTextNull()
        mTvRightTextView!!.setPadding(left, top, right, bottom)
        return this
    }

    /**
     * 设置右边文本的外边距 margin
     * @param margin 距离左、上、右、下的距离
     */
    public fun setRightTextMargin(margin: Int): MultiView {
        return setRightTextMargin(margin, margin, margin, margin)
    }

    /**
     * 设置右边文本的外边距 margin
     * @param left 左距离值
     * @param top 上距离值
     * @param right 右距离值
     * @param bottom 下距离值
     */
    public fun setRightTextMargin(left: Int, top: Int, right: Int, bottom: Int): MultiView {
        checkRightTextNull()
        val layoutParams = mTvRightTextView!!.layoutParams as RelativeLayout.LayoutParams
        layoutParams.setMargins(left, top, right, bottom)
        mTvRightTextView!!.layoutParams = layoutParams
        return this

    }

    /**
     * 获取右边view
     * @return 右边文本view
     */
    public fun getRightTextView(): TextView {
        checkRightTextNull()
        return mTvRightTextView!!
    }

    //    ////////////////////////////////////////////////////////////////////////

    /**
     * 判断上分割线是否为空
     */
    private fun checkTopDividerNull() {
        if (mViewTopLine == null)
            initTopLineView()
    }


    /**
     * 判断下分割线是否存在
     */
    private fun checkBottomDividerNull() {
        if (mViewBottomLine == null)
            initBottomView()
    }


    /**
     * 设置上下分割线的高度
     * @param height 分割线高度值
     */
    public fun setBothDividerHeight(height: Int): MultiView {
        setTopDividerHeight(height)
        setBottomDividerHeight(height)
        return this
    }

    /**
     * 设置上分割线高度
     * @param height 分割线高度值
     */
    public fun setTopDividerHeight(height: Int): MultiView {
        checkTopDividerNull()
        val layoutParams = mViewTopLine!!.layoutParams as RelativeLayout.LayoutParams
        layoutParams.height = height
        mViewTopLine!!.layoutParams = layoutParams
        return this
    }

    /**
     * 设置下分割线高度
     * @param height 分割线高度值
     */
    public fun setBottomDividerHeight(height: Int): MultiView {
        checkBottomDividerNull()
        val layoutParams = mViewBottomLine!!.layoutParams as RelativeLayout.LayoutParams
        layoutParams.height = height
        mViewBottomLine!!.layoutParams = layoutParams
        return this

    }

    /**
     * 设置上分割线的背景色
     * @param lineColor 颜色值
     */
    public fun setTopDividerColor(@ColorInt lineColor: Int): MultiView {
        checkTopDividerNull()
        mViewTopLine!!.setBackgroundColor(lineColor)
        return this
    }

    /**
     * 设置下分割线的背景色
     * @param lineColor 颜色值
     */
    public fun setBottomDividerColor(@ColorInt lineColor: Int): MultiView {
        checkBottomDividerNull()
        mViewBottomLine!!.setBackgroundColor(lineColor)
        return this
    }

    /**
     * 设置上分割线的距离左侧右侧距离
     * @param marginLeftRight 距离值
     */
    public fun setTopDividerMarginLeftRight(marginLeftRight: Int): MultiView {
        checkTopDividerNull()
        val layoutParams = mViewTopLine!!.layoutParams as RelativeLayout.LayoutParams
        layoutParams.setMargins(marginLeftRight, 0, marginLeftRight, 0)
        mViewTopLine!!.layoutParams = layoutParams
        return this
    }

    /**
     * 设置下分割线的距离左侧右侧距离
     * @param marginLeftRight 距离值
     */
    public fun setBottomDividerMarginLeftRight(marginLeftRight: Int): MultiView {
        checkBottomDividerNull()
        val layoutParams = mViewBottomLine!!.layoutParams as RelativeLayout.LayoutParams
        layoutParams.setMargins(marginLeftRight, 0, marginLeftRight, 0)
        mViewBottomLine!!.layoutParams = layoutParams
        return this
    }

    /**
     * 设置上下分割线的距离左侧右边的距离
     * @param marginLeftRight 距离值
     */
    public fun setBothDividerMarginLeftRight(marginLeftRight: Int): MultiView {
        setTopDividerMarginLeftRight(marginLeftRight)
        setBottomDividerMarginLeftRight(marginLeftRight)
        return this
    }


    // /////////////////////////////////////////////////////////////////////
    /**
     * 单位转换工具类
     *
     * @param spValue 值
     * @return 返回值
     */
    private fun sp2px(spValue: Float): Int {
        val scale = mContext.resources.displayMetrics.scaledDensity
        return (spValue * scale + 0.5f).toInt()
    }

    /**
     * 单位转换工具类
     *
     * @param dipValue 值
     * @return 返回值
     */
    private fun dip2px(dipValue: Float): Int {
        val scale = mContext.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}