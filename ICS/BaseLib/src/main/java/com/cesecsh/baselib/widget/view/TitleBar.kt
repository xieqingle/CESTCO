package com.cesecsh.baselib.widget.view

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.cesecsh.baselib.R
import com.cesecsh.baselib.utils.DensityUtils
import com.cesecsh.baselib.utils.ResourceUtils
import com.cesecsh.baselib.utils.StringUtils
import com.cesecsh.baselib.widget.viewHelper.DrawableHelper
import com.cesecsh.baselib.widget.viewHelper.ViewHelper
import java.util.ArrayList

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：标题栏
 */
open class TitleBar(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    private val DEFAULT_VIEW_ID = -1
    private var mLeftLastViewId: Int = 0 // 左侧最右 view 的 id
    private var mRightLastViewId: Int = 0 // 右侧最左 view 的 id

    private var mCenterView: View? = null // 中间的 View
    private var mTitleContainerView: LinearLayout? = null // 包裹 title 和 subTitle 的容器
    private var mTitleView: TextView? = null // 显示 title 文字的 TextView
    private var mSubTitleView: TextView? = null // 显示 subTitle 文字的 TextView

    private var mLeftViewList: MutableList<View>? = null
    private var mRightViewList: MutableList<View>? = null

    private var mTopBarSeparatorColor: Int = 0
    private var mTopBarBgColor: Int = 0
    private var mTopBarSeparatorHeight: Int = 0

    private var mTopBarBgWithSeparatorDrawableCache: Drawable? = null

    private var mTitleGravity: Int = 0
    private var mLeftBackDrawableRes: Int = 0
    private var mTitleTextSize: Int = 0
    private var mTitleTextSizeWithSubTitle: Int = 0
    private var mSubTitleTextSize: Int = 0
    private var mTitleTextColor: Int = 0
    private var mSubTitleTextColor: Int = 0
    private var mTitleMarginHorWhenNoBtnAside: Int = 0
    private var mTitleContainerPaddingHor: Int = 0
    private var mTopBarImageBtnWidth: Int = 0
    private var mTopBarImageBtnHeight: Int = 0
    private var mTopBarTextBtnPaddingHor: Int = 0
    private var mTopBarTextBtnTextColor: ColorStateList? = null
    private var mTopBarTextBtnTextSize: Int = 0
    private var mTopbarHeight = -1
    private var mTitleContainerRect: Rect? = null

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

//    // ========================= title 相关的方法
//
//    // 这个构造器只用于QMUI内部，不开放给外面用，目前用于QMUITopBarLayout
//    internal fun QMUITopBar(context: Context, inTopBarLayout: Boolean): ??? {
//        (context)
//        initVar()
//        if (inTopBarLayout) {
//            val transparentColor = ContextCompat.getColor(context, R.color.qmui_config_color_transparent)
//            mTopBarSeparatorColor = transparentColor
//            mTopBarSeparatorHeight = 0
//            mTopBarBgColor = transparentColor
//        } else {
//            init(context, null, R.attr.QMUITopBarStyle)
//        }
//    }

    private fun initVar() {
        mLeftLastViewId = DEFAULT_VIEW_ID
        mRightLastViewId = DEFAULT_VIEW_ID
        mLeftViewList = ArrayList()
        mRightViewList = ArrayList()
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val array = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, 0)
        mTopBarSeparatorColor = array.getColor(R.styleable.TitleBar_separatorColor,
                ContextCompat.getColor(context, R.color.sepatarorColor))
        mTopBarSeparatorHeight = array.getDimensionPixelSize(R.styleable.TitleBar_separatorHeight, 1)
        mTopBarBgColor = array.getColor(R.styleable.TitleBar_backgroundColor, ResourceUtils.getColor(R.color.colorPrimary))
        getCommonFieldFormTypedArray(context, array)
        val hasSeparator = array.getBoolean(R.styleable.TitleBar_needSeparator, true)
        array.recycle()

        setBackgroundDividerEnabled(hasSeparator)
    }

    private fun getCommonFieldFormTypedArray(context: Context, array: TypedArray) {
        mLeftBackDrawableRes = array.getResourceId(R.styleable.TitleBar_leftDrawableRes, R.mipmap.back)
        mTitleGravity = array.getInt(R.styleable.TitleBar_titleGravity, Gravity.CENTER)
        mTitleTextSize = array.getDimensionPixelSize(R.styleable.TitleBar_titleTextSize, DensityUtils.dp2px(getContext(), ResourceUtils.getInteger(R.integer.titleTextSize).toFloat()))
        mTitleTextSizeWithSubTitle = array.getDimensionPixelSize(R.styleable.TitleBar_titleTextSize, DensityUtils.dp2px(getContext(), ResourceUtils.getInteger(R.integer.subTitleTextSize).toFloat()))
        mSubTitleTextSize = array.getDimensionPixelSize(R.styleable.TitleBar_subTitleTextSize, ResourceUtils.getInteger(R.integer.subTitleTextSize))
        mTitleTextColor = array.getColor(R.styleable.TitleBar_titleColor, Color.WHITE)
        mSubTitleTextColor = array.getColor(R.styleable.TitleBar_subTitleColor, Color.WHITE)
        mTitleMarginHorWhenNoBtnAside = array.getDimensionPixelSize(R.styleable.TitleBar_titleHorizontalMargin, 0)
        mTitleContainerPaddingHor = array.getDimensionPixelSize(R.styleable.TitleBar_titleHorizontalPadding, 0)
        mTopBarImageBtnWidth = array.getDimensionPixelSize(R.styleable.TitleBar_imageViewWidth, DensityUtils.dp2px(getContext(), ResourceUtils.getInteger(R.integer.titleBarHeight).toFloat()))
        mTopBarImageBtnHeight = array.getDimensionPixelSize(R.styleable.TitleBar_imageViewHeight, DensityUtils.dp2px(getContext(), ResourceUtils.getInteger(R.integer.titleBarHeight).toFloat()))
        mTopBarTextBtnPaddingHor = array.getDimensionPixelSize(R.styleable.TitleBar_textBtnHorizontalPadding, DensityUtils.dp2px(getContext(), ResourceUtils.getInteger(R.integer.defaultViewPadding).toFloat()))
        mTopBarTextBtnTextColor = array.getColorStateList(R.styleable.TitleBar_textBtnTextColorStateList)
        mTopBarTextBtnTextSize = array.getDimensionPixelSize(R.styleable.TitleBar_textBtnTextSize, DensityUtils.dp2px(getContext(), ResourceUtils.getInteger(R.integer.subTitleTextSize).toFloat()))

    }
//
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//        var parent: ViewParent? = parent
//        while (parent != null && parent is View) {
//            if (parent is QMUICollapsingTopBarLayout) {
//                makeSureTitleContainerView()
//                return
//            }
//            parent = parent.parent
//        }
//    }

    /**
     * 在 TopBar 的中间添加 View，如果此前已经有 View 通过该方法添加到 TopBar，则旧的View会被 remove
     *
     * @param view 要添加到TopBar中间的View
     */
    fun setCenterView(view: View) {
        if (mCenterView === view) {
            return
        }
        if (mCenterView != null) {
            removeView(mCenterView)
        }
        mCenterView = view
        var params: RelativeLayout.LayoutParams? = mCenterView!!.layoutParams as RelativeLayout.LayoutParams
        if (params == null) {
            params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        }
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        addView(view, params)
    }

    /**
     * 添加 TopBar 的标题
     *
     * @param resId TopBar 的标题 resId
     */
    fun setTitle(resId: Int): TextView {
        return setTitle(context.getString(resId))
    }

    /**
     * 添加 TopBar 的标题
     *
     * @param title TopBar 的标题
     */
    fun setTitle(title: String): TextView {
        val titleView = getTitleView()
        titleView.text = title
        if (StringUtils.isNullOrEmpty(title)) {
            titleView.visibility = View.GONE
        } else {
            titleView.visibility = View.VISIBLE
        }
        return titleView
    }

    fun getTitle(): CharSequence? {
        return if (mTitleView == null) {
            null
        } else mTitleView!!.text
    }

    fun setEmojiTitle(title: String): TextView {
        val titleView = getTitleView()
        titleView.text = title
        if (StringUtils.isNullOrEmpty(title)) {
            titleView.visibility = View.GONE
        } else {
            titleView.visibility = View.VISIBLE
        }
        return titleView
    }

    fun showTitleView(toShow: Boolean) {
        if (mTitleView != null) {
            mTitleView!!.visibility = if (toShow) View.VISIBLE else View.GONE
        }
    }

    private fun getTitleView(): TextView {
        if (mTitleView == null) {
            //            mTitleView = isEmoji ? new EmojiconTextView(getContext()) : new TextView(getContext());
            mTitleView = TextView(context)
            mTitleView!!.gravity = Gravity.CENTER
            mTitleView!!.setSingleLine(true)
            mTitleView!!.ellipsize = TextUtils.TruncateAt.MIDDLE
            mTitleView!!.setTextColor(mTitleTextColor)
            updateTitleViewStyle()
            val titleLp = generateTitleViewAndSubTitleViewLp()
            makeSureTitleContainerView().addView(mTitleView, titleLp)
        }

        return mTitleView as TextView
    }

    /**
     * 更新 titleView 的样式（因为有没有 subTitle 会影响 titleView 的样式）
     */
    private fun updateTitleViewStyle() {
        if (mTitleView != null) {
            if (mSubTitleView == null || StringUtils.isNullOrEmpty(mSubTitleView!!.text)) {
                mTitleView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize.toFloat())
            } else {
                mTitleView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSizeWithSubTitle.toFloat())
            }
        }
    }

    /**
     * 添加 TopBar 的副标题
     *
     * @param subTitle TopBar 的副标题
     */
    fun setSubTitle(subTitle: String) {
        val titleView = getSubTitleView()
        titleView.text = subTitle
        if (StringUtils.isNullOrEmpty(subTitle)) {
            titleView.visibility = View.GONE
        } else {
            titleView.visibility = View.VISIBLE
        }
        // 更新 titleView 的样式（因为有没有 subTitle 会影响 titleView 的样式）
        updateTitleViewStyle()
    }

    /**
     * 添加 TopBar 的副标题
     *
     * @param resId TopBar 的副标题 resId
     */
    fun setSubTitle(resId: Int) {
        setSubTitle(resources.getString(resId))
    }

    private fun getSubTitleView(): TextView {
        if (mSubTitleView == null) {
            mSubTitleView = TextView(context)
            mSubTitleView!!.gravity = Gravity.CENTER
            mSubTitleView!!.setSingleLine(true)
            mSubTitleView!!.ellipsize = TextUtils.TruncateAt.MIDDLE
            mSubTitleView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSubTitleTextSize.toFloat())
            mSubTitleView!!.setTextColor(mSubTitleTextColor)
            val titleLp = generateTitleViewAndSubTitleViewLp()
            titleLp.topMargin = ResourceUtils.getInteger(R.integer.subTitleMarginTop)
            makeSureTitleContainerView().addView(mSubTitleView, titleLp)
        }

        return mSubTitleView as TextView
    }

    /**
     * 设置 TopBar 的 gravity，用于控制 title 和 subtitle 的对齐方式
     *
     * @param gravity 参考 [Gravity]
     */
    fun setTitleGravity(gravity: Int) {
        mTitleGravity = gravity
        if (mTitleView != null) {
            (mTitleView!!.layoutParams as LinearLayout.LayoutParams).gravity = gravity
            if (gravity == Gravity.CENTER || gravity == Gravity.CENTER_HORIZONTAL) {
                mTitleView!!.setPadding(paddingLeft, paddingTop, paddingLeft, paddingBottom)
            }
        }
        if (mSubTitleView != null) {
            (mSubTitleView!!.layoutParams as LinearLayout.LayoutParams).gravity = gravity
        }
        requestLayout()
    }

    fun getTitleContainerRect(): Rect {
        if (mTitleContainerRect == null) {
            mTitleContainerRect = Rect()
        }
        if (mTitleContainerView == null) {
            mTitleContainerRect!!.set(0, 0, 0, 0)
        } else {
            ViewHelper.getDescendantRect(this, mTitleContainerView!!, mTitleContainerRect!!)
        }
        return mTitleContainerRect as Rect
    }


    // ========================= leftView、rightView 相关的方法

    private fun makeSureTitleContainerView(): LinearLayout {
        if (mTitleContainerView == null) {
            mTitleContainerView = LinearLayout(context)
            // 垂直，后面要支持水平的话可以加个接口来设置
            mTitleContainerView!!.orientation = LinearLayout.VERTICAL
            mTitleContainerView!!.gravity = Gravity.CENTER
            mTitleContainerView!!.setPadding(mTitleContainerPaddingHor, 0, mTitleContainerPaddingHor, 0)
            addView(mTitleContainerView, generateTitleContainerViewLp())
        }
        return mTitleContainerView as LinearLayout
    }

    /**
     * 生成 TitleContainerView 的 LayoutParams。
     * 左右有按钮时，该 View 在左右按钮之间；
     * 没有左右按钮时，该 View 距离 TopBar 左右边缘有固定的距离
     */
    private fun generateTitleContainerViewLp(): RelativeLayout.LayoutParams {
        return RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                DensityUtils.dp2px(context, ResourceUtils.getInteger(R.integer.titleBarHeight).toFloat()))
    }

    /**
     * 生成 titleView 或 subTitleView 的 LayoutParams
     */
    private fun generateTitleViewAndSubTitleViewLp(): LinearLayout.LayoutParams {
        val titleLp = LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        // 垂直居中
        titleLp.gravity = mTitleGravity
        return titleLp
    }

    /**
     * 在TopBar的左侧添加View，如果此前已经有View通过该方法添加到TopBar，则新添加进去的View会出现在已有View的右侧
     *
     * @param view   要添加到 TopBar 左边的 View
     * @param viewId 该按钮的id，可在ids.xml中找到合适的或新增。手工指定viewId是为了适应自动化测试。
     */
    fun addLeftView(view: View, viewId: Int) {
        val viewLayoutParams = view.layoutParams
        val layoutParams: RelativeLayout.LayoutParams
        if (viewLayoutParams != null && viewLayoutParams is RelativeLayout.LayoutParams) {
            layoutParams = viewLayoutParams
        } else {
            layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        }
        this.addLeftView(view, viewId, layoutParams)
    }

    /**
     * 在TopBar的左侧添加View，如果此前已经有View通过该方法添加到TopBar，则新添加进去的View会出现在已有View的右侧。
     *
     * @param view         要添加到 TopBar 左边的 View。
     * @param viewId       该按钮的 id，可在 ids.xml 中找到合适的或新增。手工指定 viewId 是为了适应自动化测试。
     * @param layoutParams 传入一个 LayoutParams，当把 Button addView 到 TopBar 时，使用这个 LayouyParams。
     */
    fun addLeftView(view: View, viewId: Int, layoutParams: RelativeLayout.LayoutParams) {
        if (mLeftLastViewId == DEFAULT_VIEW_ID) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        } else {
            layoutParams.addRule(RelativeLayout.RIGHT_OF, mLeftLastViewId)
        }
        layoutParams.alignWithParent = true // alignParentIfMissing
        mLeftLastViewId = viewId
        view.id = viewId
        mLeftViewList!!.add(view)
        addView(view, layoutParams)
    }

    /**
     * 在 TopBar 的右侧添加 View，如果此前已经有 iew 通过该方法添加到 TopBar，则新添加进去的View会出现在已有View的左侧
     *
     * @param view   要添加到 TopBar 右边的View
     * @param viewId 该按钮的id，可在 ids.xml 中找到合适的或新增。手工指定 viewId 是为了适应自动化测试。
     */
    fun addRightView(view: View, viewId: Int) {
        val viewLayoutParams = view.layoutParams
        val layoutParams: RelativeLayout.LayoutParams
        if (viewLayoutParams != null && viewLayoutParams is RelativeLayout.LayoutParams) {
            layoutParams = viewLayoutParams
        } else {
            layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        }
        this.addRightView(view, viewId, layoutParams)
    }

    /**
     * 在 TopBar 的右侧添加 View，如果此前已经有 View 通过该方法添加到 TopBar，则新添加进去的 View 会出现在已有View的左侧。
     *
     * @param view         要添加到 TopBar 右边的 View。
     * @param viewId       该按钮的 id，可在 ids.xml 中找到合适的或新增。手工指定 viewId 是为了适应自动化测试。
     * @param layoutParams 生成一个 LayoutParams，当把 Button addView 到 TopBar 时，使用这个 LayouyParams。
     */
    fun addRightView(view: View, viewId: Int, layoutParams: RelativeLayout.LayoutParams) {
        if (mRightLastViewId == DEFAULT_VIEW_ID) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        } else {
            layoutParams.addRule(RelativeLayout.LEFT_OF, mRightLastViewId)
        }
        layoutParams.alignWithParent = true // alignParentIfMissing
        mRightLastViewId = viewId
        view.id = viewId
        mRightViewList!!.add(view)
        addView(view, layoutParams)
    }

    /**
     * 生成一个 LayoutParams，当把 Button addView 到 TopBar 时，使用这个 LayouyParams
     */
    fun generateTopBarImageButtonLayoutParams(): RelativeLayout.LayoutParams {
        val lp = RelativeLayout.LayoutParams(mTopBarImageBtnWidth, mTopBarImageBtnHeight)
        lp.topMargin = Math.max(0, (getTopBarHeight() - mTopBarImageBtnHeight) / 2)
        return lp
    }

    /**
     * 根据 resourceId 生成一个 TopBar 的按钮，并 add 到 TopBar 的右侧
     *
     * @param drawableResId 按钮图片的 resourceId
     * @param viewId        该按钮的 id，可在 ids.xml 中找到合适的或新增。手工指定 viewId 是为了适应自动化测试。
     * @return 返回生成的按钮
     */
    fun addRightImageButton(drawableResId: Int, viewId: Int): AlphaImageButton {
        val rightButton = generateTopBarImageButton(drawableResId)
        this.addRightView(rightButton, viewId, generateTopBarImageButtonLayoutParams())
        rightButton.setPadding(
                DensityUtils.dp2px(context, ResourceUtils.getInteger(R.integer.defaultViewPadding).toFloat()),
                0,
                DensityUtils.dp2px(context, ResourceUtils.getInteger(R.integer.defaultViewPadding).toFloat()),
                0)
        return rightButton
    }

    /**
     * 根据 resourceId 生成一个 TopBar 的按钮，并 add 到 TopBar 的左边
     *
     * @param drawableResId 按钮图片的 resourceId
     * @param viewId        该按钮的 id，可在ids.xml中找到合适的或新增。手工指定 viewId 是为了适应自动化测试。
     * @return 返回生成的按钮
     */
    fun addLeftImageButton(drawableResId: Int, viewId: Int): AlphaImageButton {
        val leftButton = generateTopBarImageButton(drawableResId)
        this.addLeftView(leftButton, viewId, generateTopBarImageButtonLayoutParams())
        leftButton.setPadding(
                DensityUtils.dp2px(context, ResourceUtils.getInteger(R.integer.defaultViewPadding).toFloat()),
                0,
                DensityUtils.dp2px(context, ResourceUtils.getInteger(R.integer.defaultViewPadding).toFloat()),
                0)
        return leftButton
    }

    /**
     * 生成一个LayoutParams，当把 Button addView 到 TopBar 时，使用这个 LayouyParams
     */
    fun generateTopBarTextButtonLayoutParams(): RelativeLayout.LayoutParams {
        val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, mTopBarImageBtnHeight)
        lp.topMargin = Math.max(0, (getTopBarHeight() - mTopBarImageBtnHeight) / 2)
        return lp
    }

    /**
     * 在 TopBar 左边添加一个 Button，并设置文字
     *
     * @param stringResId 按钮的文字的 resourceId
     * @param viewId      该按钮的id，可在 ids.xml 中找到合适的或新增。手工指定 viewId 是为了适应自动化测试。
     * @return 返回生成的按钮
     */
    fun addLeftTextButton(stringResId: Int, viewId: Int): Button {
        return addLeftTextButton(resources.getString(stringResId), viewId)
    }

    /**
     * 在 TopBar 左边添加一个 Button，并设置文字
     *
     * @param buttonText 按钮的文字
     * @param viewId     该按钮的 id，可在 ids.xml 中找到合适的或新增。手工指定 viewId 是为了适应自动化测试。
     * @return 返回生成的按钮
     */
    fun addLeftTextButton(buttonText: String, viewId: Int): Button {
        val button = generateTopBarTextButton(buttonText)
        this.addLeftView(button, viewId, generateTopBarTextButtonLayoutParams())
        return button
    }

    /**
     * 在 TopBar 右边添加一个 Button，并设置文字
     *
     * @param stringResId 按钮的文字的 resourceId
     * @param viewId      该按钮的id，可在 ids.xml 中找到合适的或新增。手工指定 viewId 是为了适应自动化测试。
     * @return 返回生成的按钮
     */
    fun addRightTextButton(stringResId: Int, viewId: Int): Button {
        return addRightTextButton(resources.getString(stringResId), viewId)
    }

    /**
     * 在 TopBar 右边添加一个 Button，并设置文字
     *
     * @param buttonText 按钮的文字
     * @param viewId     该按钮的 id，可在 ids.xml 中找到合适的或新增。手工指定 viewId 是为了适应自动化测试。
     * @return 返回生成的按钮
     */
    fun addRightTextButton(buttonText: String, viewId: Int): Button {
        val button = generateTopBarTextButton(buttonText)
        this.addRightView(button, viewId, generateTopBarTextButtonLayoutParams())
        return button
    }

    /**
     * 生成一个文本按钮，并设置文字
     *
     * @param text 按钮的文字
     * @return 返回生成的按钮
     */
    private fun generateTopBarTextButton(text: String): Button {
        val button = Button(context)
        button.setBackgroundResource(0)
        button.minWidth = 0
        button.minHeight = 0
        button.minimumWidth = 0
        button.minimumHeight = 0
        button.setPadding(mTopBarTextBtnPaddingHor, 0, mTopBarTextBtnPaddingHor, 0)
        button.setTextColor(if (mTopBarTextBtnTextColor != null) mTopBarTextBtnTextColor else ResourceUtils.getColorStateList(R.color.text_btn_color_state_list))
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTopBarTextBtnTextSize.toFloat())
        button.gravity = Gravity.CENTER
        button.text = text
        return button
    }

    /**
     * 生成一个图片按钮，配合 {[.generateTopBarImageButtonLayoutParams] 使用
     *
     * @param imageResourceId 图片的 resId
     */
    private fun generateTopBarImageButton(imageResourceId: Int): AlphaImageButton {
        val backButton = AlphaImageButton(context)
        backButton.setBackgroundColor(Color.TRANSPARENT)
        backButton.setImageResource(imageResourceId)
        return backButton
    }

    /**
     * 便捷方法，在 TopBar 左边添加一个返回图标按钮
     *
     * @return 返回按钮
     */
    fun addLeftBackImageButton(): AlphaImageButton {
        return addLeftImageButton(mLeftBackDrawableRes, R.id.leftViewId)
    }

    /**
     * 移除 TopBar 左边所有的 View
     */
    fun removeAllLeftViews() {
        for (leftView in mLeftViewList!!) {
            removeView(leftView)
        }
        mLeftLastViewId = DEFAULT_VIEW_ID
        mLeftViewList!!.clear()
    }

    /**
     * 移除 TopBar 右边所有的 View
     */
    fun removeAllRightViews() {
        for (rightView in mRightViewList!!) {
            removeView(rightView)
        }
        mRightLastViewId = DEFAULT_VIEW_ID
        mRightViewList!!.clear()
    }

    /**
     * 移除 TopBar 的 centerView 和 titleView
     */
    fun removeCenterViewAndTitleView() {
        if (mCenterView != null) {
            if (mCenterView!!.parent === this) {
                removeView(mCenterView)
            }
            mCenterView = null
        }

        if (mTitleView != null) {
            if (mTitleView!!.parent === this) {
                removeView(mTitleView)
            }
            mTitleView = null
        }
    }

    private fun getTopBarHeight(): Int {
        if (mTopbarHeight == -1) {
            mTopbarHeight = DensityUtils.dp2px(context, ResourceUtils.getInteger(R.integer.titleBarHeight).toFloat())
        }
        return mTopbarHeight
    }

    // ======================== TopBar自身相关的方法

    /**
     * 设置 TopBar 背景的透明度
     *
     * @param alpha 取值范围：[0, 255]，255表示不透明
     */
    fun setBackgroundAlpha(alpha: Int) {
        this.background.alpha = alpha
    }

    /**
     * 根据当前 offset、透明度变化的初始 offset 和目标 offset，计算并设置 Topbar 的透明度
     *
     * @param currentOffset     当前 offset
     * @param alphaBeginOffset  透明度开始变化的offset，即当 currentOffset == alphaBeginOffset 时，透明度为0
     * @param alphaTargetOffset 透明度变化的目标offset，即当 currentOffset == alphaTargetOffset 时，透明度为1
     */
    fun computeAndSetBackgroundAlpha(currentOffset: Int, alphaBeginOffset: Int, alphaTargetOffset: Int): Int {
        var alpha = ((currentOffset - alphaBeginOffset).toFloat() / (alphaTargetOffset - alphaBeginOffset)).toDouble()
        alpha = Math.max(0.0, Math.min(alpha, 1.0)) // from 0 to 1
        val alphaInt = (alpha * 255).toInt()
        this.setBackgroundAlpha(alphaInt)
        return alphaInt
    }

    /**
     * 设置是否要 Topbar 底部的分割线
     */
    fun setBackgroundDividerEnabled(enabled: Boolean) {
        if (enabled) {
            if (mTopBarBgWithSeparatorDrawableCache == null) {
                mTopBarBgWithSeparatorDrawableCache = DrawableHelper.createItemSeparatorBg(mTopBarSeparatorColor, mTopBarBgColor, mTopBarSeparatorHeight, false)
            }
            ViewHelper.setBackgroundKeepingPadding(this, mTopBarBgWithSeparatorDrawableCache!!)
        } else {
            ViewHelper.setBackgroundColorKeepPadding(this, mTopBarBgColor)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mTitleContainerView != null) {
            // 计算左侧 View 的总宽度
            var leftViewWidth = 0
            for (leftViewIndex in mLeftViewList!!.indices) {
                val view = mLeftViewList!![leftViewIndex]
                if (view.visibility != View.GONE) {
                    leftViewWidth += view.measuredWidth
                }
            }
            // 计算右侧 View 的总宽度
            var rightViewWidth = 0
            for (rightViewIndex in mRightViewList!!.indices) {
                val view = mRightViewList!![rightViewIndex]
                if (view.visibility != View.GONE) {
                    rightViewWidth += view.measuredWidth
                }
            }
            // 计算 titleContainer 的最大宽度
            val titleContainerWidth: Int
            if (mTitleGravity and Gravity.HORIZONTAL_GRAVITY_MASK == Gravity.CENTER_HORIZONTAL) {
                if (leftViewWidth == 0 && rightViewWidth == 0) {
                    // 左右没有按钮时，title 距离 TopBar 左右边缘的距离
                    leftViewWidth += mTitleMarginHorWhenNoBtnAside
                    rightViewWidth += mTitleMarginHorWhenNoBtnAside
                }

                // 标题水平居中，左右两侧的占位要保持一致
                titleContainerWidth = View.MeasureSpec.getSize(widthMeasureSpec) - Math.max(leftViewWidth, rightViewWidth) * 2 - paddingLeft - paddingRight
            } else {
                // 标题非水平居中，左右没有按钮时，间距分别计算
                if (leftViewWidth == 0) {
                    leftViewWidth += mTitleMarginHorWhenNoBtnAside
                }
                if (rightViewWidth == 0) {
                    rightViewWidth += mTitleMarginHorWhenNoBtnAside
                }

                // 标题非水平居中，左右两侧的占位按实际计算即可
                titleContainerWidth = View.MeasureSpec.getSize(widthMeasureSpec) - leftViewWidth - rightViewWidth - paddingLeft - paddingRight
            }
            val titleContainerWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(titleContainerWidth, View.MeasureSpec.EXACTLY)
            mTitleContainerView!!.measure(titleContainerWidthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (mTitleContainerView != null) {
            val titleContainerViewWidth = mTitleContainerView!!.measuredWidth
            val titleContainerViewHeight = mTitleContainerView!!.measuredHeight
            val titleContainerViewTop = (b - t - mTitleContainerView!!.measuredHeight) / 2
            var titleContainerViewLeft = paddingLeft
            if (mTitleGravity and Gravity.HORIZONTAL_GRAVITY_MASK == Gravity.CENTER_HORIZONTAL) {
                // 标题水平居中
                titleContainerViewLeft = (r - l - mTitleContainerView!!.measuredWidth) / 2
            } else {
                // 标题非水平居中
                // 计算左侧 View 的总宽度
                for (leftViewIndex in mLeftViewList!!.indices) {
                    val view = mLeftViewList!![leftViewIndex]
                    if (view.visibility != View.GONE) {
                        titleContainerViewLeft += view.measuredWidth
                    }
                }

                if (mLeftViewList!!.isEmpty()) {
                    //左侧没有按钮，标题离左侧间距
                    titleContainerViewLeft += mTitleMarginHorWhenNoBtnAside
                }
            }
            mTitleContainerView!!.layout(titleContainerViewLeft, titleContainerViewTop, titleContainerViewLeft + titleContainerViewWidth, titleContainerViewTop + titleContainerViewHeight)
        }
    }

    init {
        initVar()
        if (context != null) {
            init(context, attrs, defStyleAttr)
        }
    }


}