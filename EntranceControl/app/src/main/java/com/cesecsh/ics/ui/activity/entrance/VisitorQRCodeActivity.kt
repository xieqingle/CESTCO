package com.cesecsh.ics.ui.activity.entrance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.widget.Toast
import com.cesecsh.baselib.data.BaseUrls
import com.cesecsh.baselib.helper.SMSHelper
import com.cesecsh.baselib.ui.base.BaseActivity
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.baselib.utils.StringUtils
import com.cesecsh.ics.R
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import cn.cestco.dialoglib.bottomSheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_visitor_qrcode.*
import java.util.*


/**
 * 作者：RockQ on 2018/6/13
 * 邮箱：qingle6616@sina.com
 *
 * msg：访客二维码显示界面
 */
class VisitorQRCodeActivity : BaseActivity() {
    companion object {
        fun launch(context: Context, url: String, strTitle: String, phone: String) {
            if (StringUtils.isNullOrEmpty(url)) {
                return
            }
            val intent = Intent(context, VisitorQRCodeActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", strTitle)
            intent.putExtra("phone", phone)
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)

        }
    }

    private var title: String? = null
    private lateinit var url: String
    private lateinit var phone: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visitor_qrcode)
        ScreenFitUtils.auto(this)
        mTitleBar.addLeftBackImageButton().setOnClickListener { finish() }
        mTitleBar.addRightImageButton(R.mipmap.share, R.id.rightViewId).setOnClickListener {
            //            SMSHelper.sendSmsTo(mContext, phone, "欢迎光临金融城808房，您通行访问权限的链接为：\t $url")
            showShareSelectDialog()

        }
        url = intent.getStringExtra("url")
        title = intent.getStringExtra("title")
        phone = intent.getStringExtra("phone")
        title?.let { mTitleBar.setTitle(it) }


    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun showShareSelectDialog() {

        val TAG_SHARE_WECHAT_FRIEND = 0
        val TAG_SHARE_WECHAT_MOMENT = 1
        val TAG_SHARE_WEIBO = 2
        val TAG_SHARE_CHAT = 3
        val TAG_SHARE_LOCAL = 4

        val builder = BottomSheetDialog.BottomGridSheetBuilder(this@VisitorQRCodeActivity)
        builder.addItem(R.mipmap.icon_more_operation_share_friend, "分享到微信", TAG_SHARE_WECHAT_FRIEND, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
//                .addItem(R.mipmap.icon_more_operation_share_moment, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
//                .addItem(R.mipmap.icon_more_operation_share_weibo, "分享到微博", TAG_SHARE_WEIBO, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_chat, "分享到私信", TAG_SHARE_CHAT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
//                .addItem(R.mipmap.icon_more_operation_save, "保存到本地", TAG_SHARE_LOCAL, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE)
                .setOnSheetItemClickListener { dialog, itemView ->
                    dialog.dismiss()
                    val tag = itemView.tag as Int
                    when (tag) {
                        TAG_SHARE_WECHAT_FRIEND -> Toast.makeText(this@VisitorQRCodeActivity, "开发中……", Toast.LENGTH_SHORT).show()
                        TAG_SHARE_WECHAT_MOMENT -> Toast.makeText(this@VisitorQRCodeActivity, "分享到朋友圈", Toast.LENGTH_SHORT).show()
                        TAG_SHARE_WEIBO -> Toast.makeText(this@VisitorQRCodeActivity, "分享到微博", Toast.LENGTH_SHORT).show()
                        TAG_SHARE_CHAT -> SMSHelper.sendSmsTo(mContext, phone, "欢迎光临金融城808房，您通行访问权限的链接为：\t $url")
                        TAG_SHARE_LOCAL -> Toast.makeText(this@VisitorQRCodeActivity, "保存到本地", Toast.LENGTH_SHORT).show()
                    }
                }.build().show()
    }

    private fun initView() {
        val webSettings = mWebView.settings
        webSettings.useWideViewPort = true//设置webview推荐使用的窗口
        webSettings.loadWithOverviewMode = true//设置webview加载的页面的模式
        webSettings.displayZoomControls = false//隐藏webview缩放按钮
//        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
//        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.builtInZoomControls = true // 设置显示缩放按钮
        webSettings.setSupportZoom(true) // 支持缩放
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        if (url.startsWith("http://"))
            mWebView.loadUrl(url)
        else
            mWebView.loadUrl(String.format(Locale.CHINA, "%s%s", BaseUrls.base_url, url))

        mImgShare.setOnClickListener { showShareSelectDialog() }
    }
}