package com.cesecsh.ics.presenter.view

import com.cesecsh.baselib.presenter.view.BaseStatusView
import com.cesecsh.ics.data.domain.EntranceControl

/**
 * 作者：RockQ on 2018/8/27
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
interface MainView : BaseStatusView {
    fun showDoors(entranceControl: EntranceControl)
    fun updateQrUrl(QRCodeUrl: String   )
}
