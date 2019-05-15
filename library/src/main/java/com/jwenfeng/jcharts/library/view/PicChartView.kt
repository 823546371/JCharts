package com.jwenfeng.jcharts.library.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.jwenfeng.jcharts.library.data.PicChartData
import com.jwenfeng.jcharts.library.util.DisplayUtil

/**
 * 当前类注释:
 * 作者：jinwenfeng on 2019-05-15 21:56
 * 邮箱：823546371@qq.com
 * QQ： 823546371
 * 公司：南京穆尊信息科技有限公司
 * © 2017～2019 jinwenfeng
 * © 版权所有，未经允许不得传播
 */
class PicChartView : View {

    /**
     * 控件默认大小
     * */
    private var DEFAULT_SIZE: Int = 360
    /**
     *  画笔宽度，默认1dp
     */
    private var paintWidth = 0f
    /**
     * 折线部分长度，大小为20dp
     * */
    private var lineLength1 = 0f
    /**
     * 横线部分长度，大小为50dp
     * */
    private var lineLength2 = 0f
    /**
     * 文字大小，默认12sp
     * */
    private var fontSize = 28f

    /**
     * 绘制饼形图的画笔
     * */
    private lateinit var paint: Paint
    /**
     * 绘制横文字的的画笔
     * */
    private lateinit var textPaint: Paint

    private var datas = ArrayList<PicChartData>()


    constructor(context: Context) : super(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initDefaultData(context)
        initPaint()
    }

    private fun initDefaultData(context: Context) {
        paintWidth = DisplayUtil.dpToPx(context, 1f)
        lineLength1 = DisplayUtil.dpToPx(context, 20f)
        lineLength2 = DisplayUtil.dpToPx(context, 50f)
        DEFAULT_SIZE = DisplayUtil.dpToPx(context, 360f).toInt()
        fontSize = DisplayUtil.dpToPx(context, 12f)
    }

    private fun initPaint() {
        paint = Paint()
        paint.strokeWidth = paintWidth
        paint.isAntiAlias = true //开启抗锯齿
        paint.style = Paint.Style.FILL

        // 初始化文字paint
        textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.textSize = fontSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = getSize(widthMeasureSpec)
        val h = getSize(heightMeasureSpec)
        val size = Math.min(w, h)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        // 确定半径
        val r = Math.min(width, height) / 2 - ((lineLength2 + lineLength1))
        canvas.translate(width / 2f, height / 2f)
        val arcF = RectF(-r, -r, r, r)
        // 设置起始角度
        var currentAngle = 0f

        for (data in datas) {
            paint.color = data.color
            canvas.drawArc(arcF, currentAngle, data.angle, true, paint)
            // 绘制线及文字
            drawLineAndText(canvas, currentAngle, r, data)
            currentAngle += data.angle
        }
    }

    private fun drawLineAndText(canvas: Canvas, currentAngle: Float, r: Float, data: PicChartData) {
        // 绘制横线
        // 1，计算开始坐标和转折点坐标
        val startX = r * (Math.cos((currentAngle + (data.angle / 2)) * (Math.PI / 180))).toFloat()
        val startY = r * (Math.sin((currentAngle + (data.angle / 2)) * (Math.PI / 180))).toFloat()

        val stopX = (r + lineLength1) * (Math.cos((currentAngle + (data.angle / 2)) * (Math.PI / 180))).toFloat()
        val stopY = (r + lineLength1) * (Math.sin((currentAngle + (data.angle / 2)) * (Math.PI / 180))).toFloat()

        // 2、计算坐标在左边还是在右边，并计算横线结束坐标
        var endX: Float
        if (stopX - startX > 0) {
            endX = stopX + lineLength2
        } else {
            endX = stopX - lineLength2
        }
        // 3、绘制斜线和横线
        canvas.drawLine(startX, startY, stopX, stopY, paint)
        canvas.drawLine(stopX, stopY, endX, stopY, paint)

        //绘制文字
        // 绘制下方名称
        // 上下间距偏移量
        val offset = 10
        // 1、测量文字
        val textRect = Rect()
        textPaint.getTextBounds(data.name, 0, data.name.length, textRect)
        var w = textRect.width()
        var h = textRect.height()
        // 2、计算文字坐标

        var textStartX: Float
        if (stopX - startX > 0) {
            textStartX = if (w > lineLength2) {
                (stopX + offset)
            } else {
                (stopX + (lineLength2 - w) / 2)
            }
        } else {
            textStartX = if (w > lineLength2) {
                (stopX - offset - w)
            } else {
                (stopX - (lineLength2 - w) / 2 - w)
            }
        }
        textPaint.color = data.color
        canvas.drawText(data.name, 0, data.name.length, textStartX, stopY + h + offset, textPaint)

        // 绘制上方百分比，步骤同上
        // todo 保留2为小数，确保精准度
        val per = (data.angle /360 *100).toInt().toString() + "%"
        textPaint.getTextBounds(per, 0, per.length, textRect)

        w = textRect.width()
        h = textRect.height()
        if (stopX - startX > 0) {
            textStartX = if (w > lineLength2) {
                (stopX + offset)
            } else {
                (stopX + (lineLength2 - w) / 2)
            }
        } else {
            textStartX = if (w > lineLength2) {
                (stopX - offset - w)
            } else {
                (stopX - (lineLength2 - w) / 2 - w)
            }
        }

        canvas.drawText(per, 0, per.length, textStartX, stopY - offset, textPaint)

    }

    private fun getSize(measureSpec: Int): Int {
        var result: Int
        val specSize = MeasureSpec.getSize(measureSpec)
        val specMode = MeasureSpec.getMode(measureSpec)
        if (specMode == MeasureSpec.AT_MOST) {
            //默认值,此处要结合父控件给子控件的最多大小(要不然会填充父控件),所以采用最小值
            result = Math.min(DEFAULT_SIZE, specSize)
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = DEFAULT_SIZE
        }

        return result
    }

    private fun initData() {
        var count = 0f
        for (data in datas) {
            count += data.value
        }
        var sumAngle = 0f
        for (data in datas) {
            data.angle = (data.value / count) * 360
            sumAngle += data.angle
        }
        if (sumAngle < 360) {
            for (data in datas) {
                if (data.angle != 0f) {
                    data.angle = 360 - sumAngle + data.angle
                }
            }
        }
    }

/********************************************************************/

    /**
     * 对外开放方法
     * 设置数据源
     * */
    fun setData(data: ArrayList<PicChartData>){
        this.datas = data
        initData()
        invalidate()
    }

}