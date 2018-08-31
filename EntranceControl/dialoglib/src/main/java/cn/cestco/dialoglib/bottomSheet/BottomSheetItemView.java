package cn.cestco.dialoglib.bottomSheet;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewStub;
import android.widget.TextView;

import com.cesecsh.baselib.widget.view.alpha.AlphaLinearLayout;

import cn.cestco.dialoglib.R;


/**
 * BottomSheetDialog 的ItemView
 *
 * @author rockq
 * @date 2018-08-21
 */
public class BottomSheetItemView extends AlphaLinearLayout {

    private AppCompatImageView mAppCompatImageView;
    private ViewStub mSubScript;
    private TextView mTextView;


    public BottomSheetItemView(Context context) {
        super(context);
    }

    public BottomSheetItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomSheetItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAppCompatImageView = (AppCompatImageView) findViewById(R.id.grid_item_image);
        mSubScript = (ViewStub) findViewById(R.id.grid_item_subscript);
        mTextView = (TextView) findViewById(R.id.grid_item_title);
    }

    public AppCompatImageView getAppCompatImageView() {
        return mAppCompatImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public ViewStub getSubScript() {
        return mSubScript;
    }
}
