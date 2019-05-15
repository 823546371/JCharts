package com.jwenfeng.jcharts.library.util

import android.content.Context

/**
 * 当前类注释:
 * 作者：jinwenfeng on 2019-05-15 22:03
 * 邮箱：823546371@qq.com
 * QQ： 823546371
 * 公司：南京穆尊信息科技有限公司
 * © 2017～2019 jinwenfeng
 * © 版权所有，未经允许不得传播
 */
class DisplayUtil {

    companion object {
        fun dpToPx(context: Context, dpValue: Float): Float {
            val scale = context.resources.displayMetrics.density
            return dpValue * scale + 0.5f
        }
    }

}