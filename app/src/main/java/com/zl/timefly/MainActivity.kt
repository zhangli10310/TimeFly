package com.zl.timefly

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import android.os.HandlerThread
import android.text.Html
import android.view.MotionEvent
import com.dingmouren.colorpicker.ColorPickerDialog
import com.dingmouren.colorpicker.OnColorPickerListener


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    lateinit var datePicker: DatePickerDialog
    lateinit var timePicker: TimePickerDialog
    lateinit var colorPicker: ColorPickerDialog

    private var cal: Calendar = Calendar.getInstance()
    private var date = "0000.00.00"
    private var time = "00:00"

    private lateinit var handlerThread: HandlerThread
    private lateinit var handler: Handler

    private var mainHandler = Handler()

    var show: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sp = getSharedPreferences("birth", Context.MODE_PRIVATE)
        date = sp.getString("birthday", "${cal.get(Calendar.YEAR)}.${cal.get(Calendar.MONTH) + 1}.${cal.get(Calendar.DAY_OF_MONTH)}")
        Log.i(TAG, "onCreate: date:$date")
        time = sp.getString("birthtime", "${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}")
        Log.i(TAG, "onCreate: time:$time")
        show = sp.getBoolean("show", true)

        if (show) {
            birthday.text = date
            birthtime.text = time
        } else {
            birthday.text = "****.**.**"
            birthtime.text = "**:**"
        }
        refresh()


        init(sp)

        birthday.setOnClickListener {
            datePicker.show()
        }

        birthtime.setOnClickListener {
            timePicker.show()
        }

        clockView.setOnTouchListener { v, event ->
            if (event.x > v.width/3 && event.x < v.width *2/3 && event.y > v.height/3 && event.y < v.height *2/3 && event.action == MotionEvent.ACTION_DOWN) {
                ColorPickerDialog(this, clockView.pointColor, true, object : OnColorPickerListener {

                    override fun onColorConfirm(dialog: ColorPickerDialog?, color: Int) {
                        clockView.pointColor = color
                    }

                    override fun onColorCancel(dialog: ColorPickerDialog?) {

                    }

                    override fun onColorChange(dialog: ColorPickerDialog?, color: Int) {

                    }

                }).show()
            } else if (event.action == MotionEvent.ACTION_DOWN){
                ColorPickerDialog(this, clockView.bgColor, true, object : OnColorPickerListener {

                    override fun onColorConfirm(dialog: ColorPickerDialog?, color: Int) {
                        clockView.bgColor = color
                    }

                    override fun onColorCancel(dialog: ColorPickerDialog?) {

                    }

                    override fun onColorChange(dialog: ColorPickerDialog?, color: Int) {

                    }

                }).show()
            }
            return@setOnTouchListener true
        }
        content.setOnClickListener {
            ColorPickerDialog(this, clockView.textColor, true, object : OnColorPickerListener {

                override fun onColorConfirm(dialog: ColorPickerDialog?, color: Int) {
                    clockView.textColor = color
                }

                override fun onColorCancel(dialog: ColorPickerDialog?) {

                }

                override fun onColorChange(dialog: ColorPickerDialog?, color: Int) {

                }

            }).show()
        }

        text.setOnClickListener {
            show = !show
            if (show) {
                birthday.text = date
                birthtime.text = time
            } else {
                birthday.text = "****.**.**"
                birthtime.text = "**:**"
            }
            sp.edit().putBoolean("show", show).apply()
        }

        handlerThread = HandlerThread("inner")
        handlerThread.start()
        handler = Handler(handlerThread.looper) {
            handler.sendEmptyMessageDelayed(0, 100)
            mainHandler.post {
                val pass = (System.currentTimeMillis() - cal.timeInMillis) / 100
                content.text = Html.fromHtml("您的生命已经过去<b><big><big><font color=black> ${pass / 10}.${pass % 10} </font></big></big></b>秒")
            }
        }

        handler.sendEmptyMessage(0)
    }

    private fun init(sp: SharedPreferences) {
        datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            if (view.isShown) {
                date = "${year}.${month + 1}.${dayOfMonth}"
                if (show) {
                    birthday.text = date
                } else {
                    birthday.text = "****.**.**"
                }

                sp.edit().putString("birthday", date).apply()

                refresh()
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

        timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            if (view.isShown) {
                time = "${hourOfDay}:${minute}"
                if (show) {
                    birthtime.text = time
                } else {
                    birthtime.text = "**:**"
                }
                sp.edit().putString("birthtime", time).apply()

                refresh()
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)

    }

    private fun refresh() {

        val dateFormat = SimpleDateFormat("yyyy.MM.ddHH:mm", Locale.getDefault())
        val d = dateFormat.parse(date + time)

        cal.clear()
        cal.time = d
    }

    override fun onDestroy() {
        super.onDestroy()
        handlerThread.quit()
    }
}
