//package com.cesecsh.baselib.data.manager
//
//import android.content.Context
//import android.database.sqlite.SQLiteDatabase
//
///**
// * 作者：RockQ on 2018/6/14
// * 邮箱：qingle6616@sina.com
// *
// * msg：
// */
//class DbManager private constructor(mContext: Context) {
//    private val DB_NAME = "ics.db"
//    private var mDevOpenHelper: DaoMaster.DevOpenHelper? = null
//    private var mDaoMaster: DaoMaster? = null
//    private var mDaoSession: DaoSession? = null
//
//    init {
//        // 初始化数据库信息
//        mDevOpenHelper = DaoMaster.DevOpenHelper(mContext, DB_NAME)
//        getDaoMaster(mContext)
//        getDaoSession(mContext)
//    }
//
//    companion object {
//        @Volatile
//        private var instance: DbManager? = null
//
//        fun getInstance(context: Context): DbManager {
//            return instance ?: synchronized(this) {
//                instance ?: buildDatabase(context).also { instance = it }
//            }
//        }
//
//        private fun buildDatabase(context: Context): DbManager {
//
//        }
//    }
//
//    /**
//     * 获取可读数据库
//     *
//     * @param context
//     * @return
//     */
//    fun getReadableDatabase(context: Context): SQLiteDatabase? {
//        if (null == mDevOpenHelper) {
//            getInstance(context)
//        }
//        return mDevOpenHelper?.getReadableDatabase()
//    }
//
//    /**
//     * 获取可写数据库
//     *
//     * @param context
//     * @return
//     */
//    fun getWritableDatabase(context: Context): SQLiteDatabase? {
//        if (null == mDevOpenHelper) {
//            getInstance(context)
//        }
//
//        return mDevOpenHelper?.getWritableDatabase()
//    }
//
//    /**
//     * 获取DaoMaster
//     *
//     * @param context
//     * @return
//     */
//    fun getDaoMaster(context: Context): DaoMaster? {
//        if (null == mDaoMaster) {
//            synchronized(DbManager::class.java) {
//                if (null == mDaoMaster) {
//                    mDaoMaster = DaoMaster(getWritableDatabase(context))
//                }
//            }
//        }
//        return mDaoMaster
//    }
//
//    /**
//     * 获取DaoSession
//     *
//     * @param context
//     * @return
//     */
//    fun getDaoSession(context: Context): DaoSession? {
//        if (null == mDaoSession) {
//            synchronized(DbManager::class.java) {
//                mDaoSession = getDaoMaster(context)?.newSession()
//            }
//        }
//
//        return mDaoSession
//    }
//}