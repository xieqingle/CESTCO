package cn.cestco.titlebarlibrary.titleBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.cestco.titlebarlibrary.R;
import cn.cestco.titlebarlibrary.utils.DensityUtils;
import cn.cestco.titlebarlibrary.utils.ResourceUtils;
import cn.cestco.titlebarlibrary.utils.StringUtils;

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
    /**
     * 是否显示分割线
     */
    private boolean mToolBarShowSpeparator;

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
    private int mToolBarTextBtnHorizontalPadding = -1;
    private int mXToolBarTitleHorizontalMargin = DensityUtils.dp2px(mContext, 28);
    /**
     * 标题栏的字体大小
     */
    private int mXToolBarTitleTextSize;
    /**
     * 标题栏的字体颜色
     */
    private int mXToolBarTitleTextColor;
    /**
     * 子标题栏的字体颜色
     */
    private int mXToolBarSubTitleTextSize;
    /**
     * 子表题的字体颜色
     */
    private int mXToolBarSubTitleTextColor;

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
        // 标题栏的背景色
        mToolBarBgColor = typedArray.getColor(R.styleable.XToolBar_xToolBarBgColor, ResourceUtils.getColor(R.color.colorPrimary));
        //标题栏高度
        mToolBarHeight = (int) typedArray.getDimension(R.styleable.XToolBar_xToolBarHeight, mToolBarHeight);
        // 标题的颜色
        mXToolBarTitleTextColor = typedArray.getColor(R.styleable.XToolBar_xToolBarTitleTextColor, Color.WHITE);
        // 子标题的颜色
        mXToolBarSubTitleTextColor = typedArray.getColor(R.styleable.XToolBar_xToolBarSubTitleTextColor, Color.WHITE);
        // 分割线的高度
        mXToolBarTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.XToolBar_xToolBarTitleTextSize, 0);
        // 分割线的高度
        mXToolBarSubTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.XToolBar_xToolBarSubTitleTextSize, 0);
        //是否显示分割线
        mToolBarShowSpeparator = typedArray.getBoolean(R.styleable.XToolBar_xToolBarShowSeparator, false);
        // 分割线的颜色
        mToolBarSeparatorColor = typedArray.getColor(R.styleable.XToolBar_xToolBarSeparatorColor, Color.WHITE);
        // 分割线的高度
        mToolBarSeparatorHeight = typedArray.getDimensionPixelSize(R.styleable.XToolBar_xToolBarSeparatorHeight, 0);
        //左边图标的资源文件
        mLeftBackDrawableRes = typedArray.getResourceId(R.styleable.XToolBar_xToolBarLeftViewRes, R.id.default_title_bar_left_back_id);
        //按钮的横向的padding
        mToolBarTextBtnHorizontalPadding = typedArray.getDimensionPixelSize(R.styleable.XToolBar_xToolBarHorizontalPadding, mToolBarTextBtnHorizontalPadding);
        // 按钮的宽度
        mToolBarImageBtnWidth = typedArray.getDimensionPixelSize(R.styleable.XToolBar_xToolBarImageBtnWidth, mToolBarTextBtnHorizontalPadding);
        // 按钮的高度
        mToolBarImageBtnHeight = typedArray.getDimensionPixelSize(R.styleable.XToolBar_xToolBarImageBtnHeight, mToolBarTextBtnHorizontalPadding);
        // 标题栏的距离左右的margin（在左右按钮为空的时候有效）
        mXToolBarTitleHorizontalMargin = typedArray.getDimensionPixelSize(R.styleable.XToolBar_xToolBarTitleHorizontalMargin, mToolBarTextBtnHorizontalPadding);
        typedArray.recycle();
        setBackgroundDividerEnabled(mToolBarShowSpeparator);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        while (parent != null && (parent instanceof View)) {
//            if (parent instanceof QMUICollapsingTopBarLayout) {
//                makeSureTitleContainerView();
//                return;
//            }
            parent = parent.getParent();
        }
    }

    /**
     * 在 TopBar 的中间添加 View，如果此前已经有 View 通过该方法添加到 TopBar，则旧的View会被 remove
     *
     * @param view 要添加到TopBar中间的View
     */
    public void setCenterView(View view) {
        if (mCenterView == view) {
            return;
        }
        if (mCenterView != null) {
            removeView(mCenterView);
        }
        mCenterView = view;
        LayoutParams params = (LayoutParams) mCenterView.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(view, params);
    }

    /**
     * 添加 TopBar 的标题
     *
     * @param resId TopBar 的标题 resId
     */
    public TextView setTitle(int resId) {
        return setTitle(getContext().getString(resId));
    }

    /**
     * 添加 TopBar 的标题
     *
     * @param title TopBar 的标题
     */
    public TextView setTitle(String title) {
        TextView titleView = getTitleView(false);
        titleView.setText(title);
        if (StringUtils.isEmpty(title)) {
            titleView.setVisibility(GONE);
        } else {
            titleView.setVisibility(VISIBLE);
        }
        return titleView;
    }

    public CharSequence getTitle() {
        if (mTitleView == null) {
            return null;
        }
        return mTitleView.getText();
    }

    public TextView setEmojiTitle(String title) {
        TextView titleView = getTitleView(true);
        titleView.setText(title);
        if (StringUtils.isEmpty(title)) {
            titleView.setVisibility(GONE);
        } else {
            titleView.setVisibility(VISIBLE);
        }
        return titleView;
    }

    public void showTitleView(boolean toShow) {
        if (mTitleView != null) {
            mTitleView.setVisibility(toShow ? VISIBLE : GONE);
        }
    }

    private TextView getTitleView(boolean isEmoji) {
        if (mTitleView == null) {
//            mTitleView = isEmoji ? new EmojiconTextView(getContext()) : new TextView(getContext());
            mTitleView = new TextView(getContext());
            mTitleView.setGravity(Gravity.CENTER);
            mTitleView.setSingleLine(true);
            mTitleView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            mTitleView.setTextColor(mXToolBarTitleTextColor);
            updateTitleViewStyle();
            LinearLayout.LayoutParams titleLp = generateTitleViewAndSubTitleViewLp();
            makeSureTitleContainerView().addView(mTitleView, titleLp);
        }

        return mTitleView;
    }

    /**
     * 更新 titleView 的样式（因为有没有 subTitle 会影响 titleView 的样式）
     */
    private void updateTitleViewStyle() {
        if (mTitleView != null) {
            if (mSubTitleView == null || StringUtils.isEmpty(mSubTitleView.getText())) {
                mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtils.sp2px(mContext, mXToolBarTitleTextSize));
            } else {
                mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtils.sp2px(mContext, mXToolBarSubTitleTextSize));
            }
        }
    }

    /**
     * 添加 TopBar 的副标题
     *
     * @param subTitle TopBar 的副标题
     */
    public void setSubTitle(String subTitle) {
        TextView titleView = getSubTitleView();
        titleView.setText(subTitle);
        if (StringUtils.isEmpty(subTitle)) {
            titleView.setVisibility(GONE);
        } else {
            titleView.setVisibility(VISIBLE);
        }
        // 更新 titleView 的样式（因为有没有 subTitle 会影响 titleView 的样式）
        updateTitleViewStyle();
    }

    /**
     * 添加 TopBar 的副标题
     *
     * @param resId TopBar 的副标题 resId
     */
    public void setSubTitle(int resId) {
        setSubTitle(getResources().getString(resId));
    }

    private TextView getSubTitleView() {
        if (mSubTitleView == null) {
            mSubTitleView = new TextView(getContext());
            mSubTitleView.setGravity(Gravity.CENTER);
            mSubTitleView.setSingleLine(true);
            mSubTitleView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            mSubTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtils.sp2px(mContext, mXToolBarSubTitleTextSize));
            mSubTitleView.setTextColor(mXToolBarSubTitleTextColor);
            LinearLayout.LayoutParams titleLp = generateTitleViewAndSubTitleViewLp();
            titleLp.topMargin = DensityUtils.dp2px(getContext(), 1);
            makeSureTitleContainerView().addView(mSubTitleView, titleLp);
        }

        return mSubTitleView;
    }

    /**
     * 设置 TopBar 的 gravity，用于控制 title 和 subtitle 的对齐方式
     *
     * @param gravity 参考 {@link android.view.Gravity}
     */
    public void setTitleGravity(int gravity) {
        mTitleGravity = gravity;
        if (mTitleView != null) {
            ((LinearLayout.LayoutParams) mTitleView.getLayoutParams()).gravity = gravity;
            if (gravity == Gravity.CENTER || gravity == Gravity.CENTER_HORIZONTAL) {
                mTitleView.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingLeft(), getPaddingBottom());
            }
        }
        if (mSubTitleView != null) {
            ((LinearLayout.LayoutParams) mSubTitleView.getLayoutParams()).gravity = gravity;
        }
        requestLayout();
    }

    public Rect getTitleContainerRect() {
        if (mTitleContainerRect == null) {
            mTitleContainerRect = new Rect();
        }
        if (mTitleContainerView == null) {
            mTitleContainerRect.set(0, 0, 0, 0);
        } else {
            mTitleContainerRect.set(0, 0, mTitleContainerView.getWidth(), mTitleContainerView.getHeight());
            ViewGroupHelper.offsetDescendantRect(this, mTitleContainerView, mTitleContainerRect);
        }
        return mTitleContainerRect;
    }

    /**
     * 便捷方法，在 TopBar 左边添加一个返回图标按钮
     *
     * @return 返回按钮
     */
    public ImageButton addLeftBackImageButton() {
        return addLeftImageButton(mLeftBackDrawableRes, R.id.default_title_bar_left_back_id);
    }


    // leftView rightView 操作

    private ImageButton addLeftImageButton(int leftBackDrawableRes, int leftBackId) {
        ImageButton leftButton = generateTopBarImageButton(leftBackDrawableRes);
        this.addLeftView(leftButton, leftBackId, generateTopBarImageButtonLayoutParams());
        return leftButton;


    }

    /**
     * 生成一个图片按钮，配合 {{@link #generateTopBarImageButtonLayoutParams()} 使用
     *
     * @param imageResourceId 图片的 resId
     */
    private ImageButton generateTopBarImageButton(int imageResourceId) {
        ImageButton backButton = new ImageButton(getContext());
        backButton.setBackgroundColor(Color.TRANSPARENT);
        backButton.setImageResource(imageResourceId);
        return backButton;
    }

    /**
     * 生成一个 LayoutParams，当把 Button addView 到 TopBar 时，使用这个 LayouyParams
     */
    public LayoutParams generateTopBarImageButtonLayoutParams() {
        LayoutParams lp = new LayoutParams(mToolBarImageBtnWidth, mToolBarImageBtnHeight);
        lp.topMargin = Math.max(0, (mToolBarHeight - mToolBarImageBtnHeight) / 2);
        return lp;
    }

    /**
     * 在TopBar的左侧添加View，如果此前已经有View通过该方法添加到TopBar，则新添加进去的View会出现在已有View的右侧。
     *
     * @param view         要添加到 TopBar 左边的 View。
     * @param viewId       该按钮的 id，可在 ids.xml 中找到合适的或新增。手工指定 viewId 是为了适应自动化测试。
     * @param layoutParams 传入一个 LayoutParams，当把 Button addView 到 TopBar 时，使用这个 LayouyParams。
     */
    public void addLeftView(View view, int viewId, LayoutParams layoutParams) {
        if (mLeftLastViewId == R.id.default_title_bar_id) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else {
            layoutParams.addRule(RelativeLayout.RIGHT_OF, mLeftLastViewId);
        }
        layoutParams.alignWithParent = true; // alignParentIfMissing
        mLeftLastViewId = viewId;
        view.setId(viewId);
        mLeftViewList.add(view);
        addView(view, layoutParams);

        // 消除按钮变动对 titleView 造成的影响
        refreshTitleViewLp();
    }

    /**
     * 在 TopBar 的右侧添加 View，如果此前已经有 iew 通过该方法添加到 TopBar，则新添加进去的View会出现在已有View的左侧
     *
     * @param view   要添加到 TopBar 右边的View
     * @param viewId 该按钮的id，可在 ids.xml 中找到合适的或新增。手工指定 viewId 是为了适应自动化测试。
     */
    public void addRightView(View view, int viewId) {
        ViewGroup.LayoutParams viewLayoutParams = view.getLayoutParams();
        LayoutParams layoutParams;
        if (viewLayoutParams != null && viewLayoutParams instanceof LayoutParams) {
            layoutParams = (LayoutParams) viewLayoutParams;
        } else {
            layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        this.addRightView(view, viewId, layoutParams);
    }

    /**
     * 在 TopBar 的右侧添加 View，如果此前已经有 View 通过该方法添加到 TopBar，则新添加进去的 View 会出现在已有View的左侧。
     *
     * @param view         要添加到 TopBar 右边的 View。
     * @param viewId       该按钮的 id，可在 ids.xml 中找到合适的或新增。手工指定 viewId 是为了适应自动化测试。
     * @param layoutParams 生成一个 LayoutParams，当把 Button addView 到 TopBar 时，使用这个 LayouyParams。
     */
    public void addRightView(View view, int viewId, LayoutParams layoutParams) {
        if (mRightLastViewId == R.id.default_title_bar_id) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            layoutParams.addRule(RelativeLayout.LEFT_OF, mRightLastViewId);
        }
        layoutParams.alignWithParent = true; // alignParentIfMissing
        mRightLastViewId = viewId;
        view.setId(viewId);
        mRightViewList.add(view);
        addView(view, layoutParams);

        // 消除按钮变动对 titleView 造成的影响
        refreshTitleViewLp();
    }

    /**
     * 若在 titleView 存在的情况下，改变 leftViews 和 rightViews，会导致 titleView 的位置不正确。
     * 此时要调用该方法，保证 titleView 的位置重新调整
     */
    private void refreshTitleViewLp() {
        // 若原本已经有 title，则需要将title移到新添加进去的按钮右边
        if (mTitleView != null) {
            LayoutParams titleLp = generateTitleContainerViewLp();
            makeSureTitleContainerView().setLayoutParams(titleLp);

        }
    }

    private LinearLayout makeSureTitleContainerView() {
        if (mTitleContainerView == null) {
            mTitleContainerView = new LinearLayout(getContext());
            // 垂直，后面要支持水平的话可以加个接口来设置
            mTitleContainerView.setOrientation(LinearLayout.VERTICAL);
            mTitleContainerView.setGravity(Gravity.CENTER);
            mTitleContainerView.setPadding(DensityUtils.dp2px(getContext(), 8), 0, DensityUtils.dp2px(getContext(), 8), 0);
            addView(mTitleContainerView, generateTitleContainerViewLp());
        }
        return mTitleContainerView;
    }

    /**
     * 生成 TitleContainerView 的 LayoutParams。
     * 左右有按钮时，该 View 在左右按钮之间；
     * 没有左右按钮时，该 View 距离 TopBar 左右边缘有固定的距离
     */
    private LayoutParams generateTitleContainerViewLp() {
        LayoutParams titleLp = new LayoutParams(LayoutParams.MATCH_PARENT, mToolBarHeight);

        // 左右没有按钮时，title 距离 TopBar 左右边缘的距离
        if (mLeftLastViewId == R.id.default_title_bar_id && mRightLastViewId == R.id.default_title_bar_id) {
            // 左右两边都没有按钮时，title 和 TopBar 两边保持一个按钮的距离
            titleLp.leftMargin = mXToolBarTitleHorizontalMargin;
            titleLp.rightMargin = mXToolBarTitleHorizontalMargin;
        }

        return titleLp;
    }

    /**
     * 生成 titleView 或 subTitleView 的 LayoutParams
     */
    private LinearLayout.LayoutParams generateTitleViewAndSubTitleViewLp() {
        LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        // 垂直居中
        titleLp.gravity = mTitleGravity;
        return titleLp;
    }

    /**
     * 移除 TopBar 左边所有的 View
     */
    public void removeAllLeftViews() {
        for (View leftView : mLeftViewList) {
            removeView(leftView);
        }
        mLeftLastViewId = R.id.default_title_bar_id;
        mLeftViewList.clear();
    }

    /**
     * 移除 TopBar 右边所有的 View
     */
    public void removeAllRightViews() {
        for (View rightView : mRightViewList) {
            removeView(rightView);
        }
        mRightLastViewId = R.id.default_title_bar_id;
        mRightViewList.clear();
    }

    /**
     * 移除 TopBar 的 centerView 和 titleView
     */
    public void removeCenterViewAndTitleView() {
        if (mCenterView != null) {
            if (mCenterView.getParent() == this) {
                removeView(mCenterView);
            }
            mCenterView = null;
        }

        if (mTitleView != null) {
            if (mTitleView.getParent() == this) {
                removeView(mTitleView);
            }
            mTitleView = null;
        }
    }

    /**
     * 设置 TopBar 背景的透明度
     *
     * @param alpha 取值范围：[0, 255]，255表示不透明
     */
    public void setBackgroundAlpha(int alpha) {
        getBackground().setAlpha(alpha);
    }
    // ======================== ToolBar自身相关的方法

    /**
     * 根据当前 offset、透明度变化的初始 offset 和目标 offset，计算并设置 Topbar 的透明度
     *
     * @param currentOffset     当前 offset
     * @param alphaBeginOffset  透明度开始变化的offset，即当 currentOffset == alphaBeginOffset 时，透明度为0
     * @param alphaTargetOffset 透明度变化的目标offset，即当 currentOffset == alphaTargetOffset 时，透明度为1
     */
    public int computeAndSetBackgroundAlpha(int currentOffset, int alphaBeginOffset, int alphaTargetOffset) {
        double alpha = (float) (currentOffset - alphaBeginOffset) / (alphaTargetOffset - alphaBeginOffset);
        // from 0 to 1
        alpha = Math.max(0, Math.min(alpha, 1));
        int alphaInt = (int) (alpha * 255);
        this.setBackgroundAlpha(alphaInt);
        return alphaInt;
    }

    public void setBackgroundDividerEnabled(boolean backgroundDividerEnabled) {
        if (backgroundDividerEnabled) {
            if (mToolBarBgWithSeparatorDrawableCache == null) {
                // 如果separatorDrawable 为空，则按照线的颜色，背景，绘制一个背景
                ShapeDrawable separator = new ShapeDrawable();
                separator.getPaint().setStyle(Paint.Style.FILL);
                separator.getPaint().setColor(mToolBarSeparatorColor);
                ShapeDrawable bg = new ShapeDrawable();
                bg.getPaint().setStyle(Paint.Style.FILL);
                bg.getPaint().setColor(mToolBarHeight);
                Drawable[] layers = {separator, bg};
                LayerDrawable layerDrawable = new LayerDrawable(layers);

                layerDrawable.setLayerInset(1, 0, 0, 0, mToolBarSeparatorHeight);
            }
            // 设置背景
            int[] padding = new int[]{getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom()};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(mToolBarBgWithSeparatorDrawableCache);
            } else {
                setBackgroundDrawable(mToolBarBgWithSeparatorDrawableCache);
            }
            setPadding(padding[0], padding[1], padding[2], padding[3]);
        } else {
            // 设置背景
            int[] padding = new int[]{getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom()};
            setBackgroundColor(mToolBarBgColor);
            setPadding(padding[0], padding[1], padding[2], padding[3]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTitleContainerView != null) {
            // 计算左侧 View 的总宽度
            int leftViewWidth = 0;
            for (int leftViewIndex = 0; leftViewIndex < mLeftViewList.size(); leftViewIndex++) {
                View view = mLeftViewList.get(leftViewIndex);
                if (view.getVisibility() != GONE) {
                    leftViewWidth += view.getMeasuredWidth();
                }
            }
            // 计算右侧 View 的总宽度
            int rightViewWidth = 0;
            for (int rightViewIndex = 0; rightViewIndex < mRightViewList.size(); rightViewIndex++) {
                View view = mRightViewList.get(rightViewIndex);
                if (view.getVisibility() != GONE) {
                    rightViewWidth += view.getMeasuredWidth();
                }
            }
            // 计算 titleContainer 的最大宽度
            int titleContainerWidth;
            if ((mTitleGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.CENTER_HORIZONTAL) {
                // 标题水平居中，左右两侧的占位要保持一致
                titleContainerWidth = MeasureSpec.getSize(widthMeasureSpec) - Math.max(leftViewWidth, rightViewWidth) * 2 - getPaddingLeft() - getPaddingRight();
            } else {
                // 标题非水平居中，左右两侧的占位按实际计算即可
                titleContainerWidth = MeasureSpec.getSize(widthMeasureSpec) - leftViewWidth - rightViewWidth - getPaddingLeft() - getPaddingRight();
            }
            int titleContainerWidthMeasureSpec = MeasureSpec.makeMeasureSpec(titleContainerWidth, MeasureSpec.EXACTLY);
            mTitleContainerView.measure(titleContainerWidthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mTitleContainerView != null) {
            int titleContainerViewWidth = mTitleContainerView.getMeasuredWidth();
            int titleContainerViewHeight = mTitleContainerView.getMeasuredHeight();
            int titleContainerViewTop = (b - t - mTitleContainerView.getMeasuredHeight()) / 2;
            int titleContainerViewLeft = getPaddingLeft();
            if ((mTitleGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.CENTER_HORIZONTAL) {
                // 标题水平居中
                titleContainerViewLeft = (r - l - mTitleContainerView.getMeasuredWidth()) / 2;
            } else {
                // 标题非水平居中
                // 计算左侧 View 的总宽度
                for (int leftViewIndex = 0; leftViewIndex < mLeftViewList.size(); leftViewIndex++) {
                    View view = mLeftViewList.get(leftViewIndex);
                    if (view.getVisibility() != GONE) {
                        titleContainerViewLeft += view.getMeasuredWidth();
                    }
                }
            }
            mTitleContainerView.layout(titleContainerViewLeft, titleContainerViewTop, titleContainerViewLeft + titleContainerViewWidth, titleContainerViewTop + titleContainerViewHeight);
        }
    }

    private static class ViewGroupHelper {
        private static final ThreadLocal<Matrix> sMatrix = new ThreadLocal<>();
        private static final ThreadLocal<RectF> sRectF = new ThreadLocal<>();

        public static void offsetDescendantRect(ViewGroup group, View child, Rect rect) {
            Matrix m = sMatrix.get();
            if (m == null) {
                m = new Matrix();
                sMatrix.set(m);
            } else {
                m.reset();
            }

            offsetDescendantMatrix(group, child, m);

            RectF rectF = sRectF.get();
            if (rectF == null) {
                rectF = new RectF();
                sRectF.set(rectF);
            }
            rectF.set(rect);
            m.mapRect(rectF);
            rect.set((int) (rectF.left + 0.5f), (int) (rectF.top + 0.5f),
                    (int) (rectF.right + 0.5f), (int) (rectF.bottom + 0.5f));
        }

        static void offsetDescendantMatrix(ViewParent target, View view, Matrix m) {
            final ViewParent parent = view.getParent();
            if (parent instanceof View && parent != target) {
                final View vp = (View) parent;
                offsetDescendantMatrix(target, vp, m);
                m.preTranslate(-vp.getScrollX(), -vp.getScrollY());
            }

            m.preTranslate(view.getLeft(), view.getTop());

            if (!view.getMatrix().isIdentity()) {
                m.preConcat(view.getMatrix());
            }
        }
    }
}
