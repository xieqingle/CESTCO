package com.cesecsh.baselib.common

import android.app.Activity
import android.content.Context
import java.util.*

/**
 * Created by RockQ
 * on 2016/10/28
 *
 * activity 管理类
 */

class ActivityManager private constructor() {
    private val activityStack: Stack<Activity> = Stack()


    /**
     * 获取位于栈顶的activity
     * @return
     */
    fun getTopActivity(): Activity = activityStack.lastElement()

    /**
     * 添加activity到管理类中，以便一起管理所有的类
     * @param activity 放入管理类中的activity
     */
    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    /**
     * 销毁位于对顶端的activity
     */
    fun finishActivity(activity: Activity) {
        activity.finish()
        activityStack.remove(activity)
    }

    /**
     * 关闭所有的activity
     */
    fun finishAllActivity() {
        for (activity in activityStack) {
            activity.finish()
        }
        activityStack.clear()
    }

    /*
       退出应用程序
    */
    fun exitApp(context: Context) {
        finishAllActivity()
//        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
//        activityManager.killBackgroundProcesses(context.packageName)
//        System.exit(0)
    }

    companion object {
        /**
         * 获取管理类的实体对象
         * @return ActivityManager 对象
         */
        val instance: ActivityManager by lazy { ActivityManager() }
    }
}
