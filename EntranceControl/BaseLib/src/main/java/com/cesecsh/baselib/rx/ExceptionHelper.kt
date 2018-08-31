package com.cesecsh.baselib.rx

import com.alibaba.android.arouter.launcher.ARouter
import com.cesecsh.baselib.R
import com.cesecsh.baselib.common.ARouterPath
import com.cesecsh.baselib.common.ActivityManager
import com.cesecsh.baselib.common.DataResource
import com.cesecsh.baselib.presenter.view.BaseView
import com.cesecsh.baselib.utils.ResourceUtils
import com.kotlin.base.rx.BaseException
import com.kotlin.base.utils.AppPrefsUtils
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import java.net.SocketTimeoutException

/**
 * 作者：RockQ on 2018/8/27
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
object ExceptionHelper {
    fun handleException(mView: BaseView, e: Throwable) {
        if (e is BaseException) {
            when (e.status) {
                // 异地登陆
                406 -> {
                    QMUIDialog.MessageDialogBuilder(ActivityManager.instance.getTopActivity())
                            .setTitle(ResourceUtils.getString(R.string.downline))
                            .setMessage(ResourceUtils.getString(R.string.account_login_another_place))
                            .addAction(ResourceUtils.getString(R.string.exit)) { dialog, _ ->
                                dialog.dismiss()
                                deleteDatas()
                                ActivityManager.instance.finishAllActivity()
                            }
                            .addAction(0, ResourceUtils.getString(R.string.login_in_again), QMUIDialogAction.ACTION_PROP_NEGATIVE) { dialog, _ ->
                                dialog.dismiss()
                                deleteDatas()
                                ARouter.getInstance()
                                        .build(ARouterPath.PATH_LOGIN)
                                        .withTransition(R.anim.scale_enter, R.anim.slide_still)
                                        .navigation()

                            }
                            .create(com.qmuiteam.qmui.R.style.QMUI_Dialog)
                            .show()
                }
                else -> {
                    mView.onError(e.msg)
                }
            }

        } else if (e is SocketTimeoutException) {
            //连接超时
            mView.onError(ResourceUtils.getString(R.string.socket_time_out))
        }
    }

    /**
     * 删除app缓存，除了保存的用户名，其他都清空
     */
    private fun deleteDatas() {
        AppPrefsUtils.putBoolean(DataResource.IS_LOGIN, false)
        AppPrefsUtils.putString(DataResource.userInfo, "")
        AppPrefsUtils.putString(DataResource.token, "")
        AppPrefsUtils.putString(DataResource.userId, "")
    }
}