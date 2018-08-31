package cn.cestco.dialoglib.tipDialog;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.cestco.dialoglib.R;

/**
 * 作者：RockQ on 2018/8/21
 * 邮箱：qingle6616@sina.com
 * <p>
 * msg：
 */
public class ToastDialogHelper {

    /**
     * 不显示任何icon
     */
    public static final int ICON_TYPE_NOTHING = 0;
    /**
     * 显示 Loading 图标
     */
    public static final int ICON_TYPE_LOADING = 1;
    /**
     * 显示成功图标
     */
    public static final int ICON_TYPE_SUCCESS = 2;
    /**
     * 显示失败图标
     */
    public static final int ICON_TYPE_FAIL = 3;
    /**
     * 显示信息图标
     */
    public static final int ICON_TYPE_INFO = 4;

    /**
     * 显示操作成功
     *
     * @param context 上下文
     * @see ToastType  == ICON_TYPE_INFO
     * <p>
     */
    public static void toastSuccess(Context context) {
        TipDialog tipDialog = new TipDialog.Builder(context)
                .setIconType(ICON_TYPE_SUCCESS)
                .setTipWord(context.getString(R.string.operate_success))
                .create();
        tipDialog.show();
        dismiss(tipDialog);

    }

    /**
     * 取消显示dialog
     *
     * @param tipDialog
     */
    public static void dismiss(final TipDialog tipDialog) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 1500);

    }

    /**
     * 显示操作失败
     *
     * @param context 上下文
     * @see ToastType  == ICON_TYPE_INFO
     * <p>
     */
    public static void toastError(Context context) {
        TipDialog tipDialog = new TipDialog.Builder(context)
                .setIconType(ICON_TYPE_FAIL)
                .setTipWord(context.getString(R.string.operate_failure))
                .create();
        tipDialog.show();
        dismiss(tipDialog);

    }

    /**
     * 显示加载中
     *
     * @param context 上下文
     * @see ToastType  == ICON_TYPE_INFO
     * <p>
     * 需要手动调用取消
     */
    public static TipDialog toastLoading(Context context) {
        TipDialog tipDialog = new TipDialog.Builder(context)
                .setIconType(ICON_TYPE_LOADING)
                .setTipWord(context.getString(R.string.loading))
                .create();
        tipDialog.show();
        return tipDialog;
    }

    /**
     * 请勿重复操作
     *
     * @param context 上下文
     * @see ToastType  == ICON_TYPE_INFO
     */
    public static void toastRepeat(Context context) {
        TipDialog tipDialog = new TipDialog.Builder(context)
                .setIconType(ICON_TYPE_INFO)
                .setTipWord(context.getString(R.string.pleace_not_repeat_operate))
                .create();
        tipDialog.show();
        dismiss(tipDialog);

    }

    /**
     * @param context 上下文
     * @param str     展示的文本
     * @see ToastType  ==
     */

    public static void toast(Context context, String str) {
        TipDialog tipDialog = new TipDialog.Builder(context)
                .setIconType(ICON_TYPE_NOTHING)
                .setTipWord(str)
                .create();
        tipDialog.show();
        dismiss(tipDialog);

    }

    /**
     * @param context   上下文
     * @param str       展示的文本
     * @param toastType {@link ToastType}
     * @see ToastType
     */

    public static void toast(Context context, String str, @ToastType int toastType) {
        TipDialog tipDialog = new TipDialog.Builder(context)
                .setIconType(toastType)
                .setTipWord(str)
                .create();
        tipDialog.show();
        dismiss(tipDialog);

    }


    @IntDef({ICON_TYPE_NOTHING, ICON_TYPE_LOADING, ICON_TYPE_SUCCESS, ICON_TYPE_FAIL, ICON_TYPE_INFO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ToastType {
    }
}
