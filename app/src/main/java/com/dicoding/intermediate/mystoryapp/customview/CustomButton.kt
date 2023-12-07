package com.dicoding.intermediate.mystoryapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.intermediate.mystoryapp.R

class CustomButton : AppCompatButton {

    private lateinit var enableBtn: Drawable
    private lateinit var disableBtn: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if(isEnabled) enableBtn else disableBtn
        gravity = Gravity.CENTER
    }

    private fun init() {
        enableBtn = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disableBtn = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }
}
