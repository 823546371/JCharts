package com.jwenfeng.jcharts

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jwenfeng.jcharts.library.data.PicChartData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var data:ArrayList<PicChartData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        pic_chart_view.setData(data)
    }

    private fun initData() {
        data = arrayListOf(PicChartData("VUE", 10f, Color.parseColor("#b31939")),
            PicChartData("Android", 30f, Color.parseColor("#245bf5")),
            PicChartData("Other", 10f, Color.parseColor("#2abf4e")),
            PicChartData("iOS", 20f, Color.parseColor("#fd9308")))
    }
}
