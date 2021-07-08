package com.example.switchbutton

import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.switchbutton.databinding.ComponentSwitchButtomBinding


class MyButtonView : ConstraintLayout, View.OnClickListener{
    val TAG ="MyButtonView"
    var leftTxtString = ""
    var rightTxtString = ""
    var txtSize = 0f
    var buttonBackgroundColor = ""
    var buttonChooseColor = ""
    var buttonChooseTwoColor = ""
    var strokeColor = ""
    var strokeWidth = 0
    var buttonRadius = 0f
    var chooseTxtColor = ""
    var chooseTxtSecondColor = ""
    var unChooseTxtColor = ""
    var unChooseTxtSecondColor = ""
    val leftChoose = 0
    val rightChoose = 1
    private lateinit var binding : ComponentSwitchButtomBinding
    var wrappedOnClickListener: OnClickListener ? = null
    private val defaultTxtUnChooseColor = "#999999"
    private val defaultTxtChooseColor = "#ffffff"
    private var initX = 0f
    private var initChoose = 0
    private var firstTimeOnLayout = true

    constructor(context: Context): super(context){
        initView()
    }

    constructor(context: Context,attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.MyButtonView)
        getValue(typedArray)
        initView()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) :  super(context, attrs, defStyleAttr){
        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.MyButtonView,defStyleAttr,0)
        getValue(typedArray)
        initView()
    }


    @TargetApi(21)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.MyButtonView,defStyleAttr,0)
        getValue(typedArray)
        initView()
    }

    private fun getValue(typedArray : TypedArray) {
        leftTxtString = typedArray.getString(R.styleable.MyButtonView_leftTxtString) ?: ""
        rightTxtString = typedArray.getString(R.styleable.MyButtonView_rightTxtString) ?: ""
        chooseTxtColor = typedArray.getString(R.styleable.MyButtonView_chooseTxtColor) ?: defaultTxtChooseColor
        chooseTxtSecondColor = typedArray.getString(R.styleable.MyButtonView_chooseTxtSecondColor) ?: chooseTxtColor
        unChooseTxtColor = typedArray.getString(R.styleable.MyButtonView_unChooseTxtColor) ?: defaultTxtUnChooseColor
        unChooseTxtSecondColor = typedArray.getString(R.styleable.MyButtonView_unChooseTxtSecondColor) ?: unChooseTxtColor
        txtSize = typedArray.getDimension(R.styleable.MyButtonView_TxtSize,10f)
        buttonBackgroundColor = typedArray.getString(R.styleable.MyButtonView_buttonBackgroundColor) ?: "#000000"
        strokeColor = typedArray.getString(R.styleable.MyButtonView_strokeColor) ?: buttonBackgroundColor
        strokeWidth = typedArray.getInt(R.styleable.MyButtonView_strokeWidth,1)
        buttonChooseColor = typedArray.getString(R.styleable.MyButtonView_buttonChooseColor) ?: "#2bfbff"
        buttonChooseTwoColor = typedArray.getString(R.styleable.MyButtonView_buttonChooseTwoColor) ?: buttonChooseColor
        buttonRadius = typedArray.getDimension(R.styleable.MyButtonView_buttonRadius,15f)

        typedArray.recycle()
    }

    private fun initView(){
        binding = ComponentSwitchButtomBinding.inflate(LayoutInflater.from(context), this)
        val txtLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        binding.myButton.layoutParams = txtLayoutParams
        binding.tv1.text = leftTxtString
        binding.tv1.textSize = txtSize
        binding.tv2.text = rightTxtString
        binding.tv2.textSize = txtSize

        val myGrad = binding.myButton.background as GradientDrawable
        myGrad.setColor(Color.parseColor(buttonBackgroundColor))
        myGrad.cornerRadius = buttonRadius
        myGrad.setStroke(strokeWidth,Color.parseColor(strokeColor))

        val myChooseGrad = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(Color.parseColor(buttonChooseColor), Color.parseColor(buttonChooseTwoColor))
        )
        myChooseGrad.cornerRadius = buttonRadius
        binding.choose.background = myChooseGrad

        binding.myButton.setOnClickListener(this)

        //動畫設定
        val container = binding.myButton
        val choose = binding.choose
        val tv1 = binding.tv1
        val tv2 = binding.tv2
        tv1.isClickable = false


        tv1.setOnClickListener {
            setTextViewColorGradient(tv1,chooseTxtColor,chooseTxtSecondColor)
            setTextViewColorGradient(tv2,unChooseTxtColor,unChooseTxtSecondColor)
            val background_w = choose.width
            val container_w = container.width
            val x: Int = container_w - background_w
            Log.i("tag", "onClick: backgroud =%d%n$background_w")
            val animator: ObjectAnimator = ObjectAnimator.ofFloat(choose, "translationX", x.toFloat(), 0f)
            tv1.isClickable = false
            tv2.isClickable = true
            animator.duration = 200
            animator.start()
        }
        tv2.setOnClickListener {
            setTextViewColorGradient(tv1,unChooseTxtColor,unChooseTxtSecondColor)
            setTextViewColorGradient(tv2,chooseTxtColor,chooseTxtSecondColor)
            val background_w = choose.width
            val container_w = container.width
            val x: Int = container_w - background_w
            Log.i("tag", "onClick: backgroud =%d%n$background_w")
            val animator: ObjectAnimator = ObjectAnimator.ofFloat(choose, "translationX", 0f, x.toFloat())
            tv1.isClickable = true
            tv2.isClickable = false
            animator.duration = 200
            animator.start()
        }
    }

    override fun onClick(view: View?) {
//        binding.myButton.isSelected = !binding.myButton.isSelected
//        binding.myText.isSelected = !binding.myText.isSelected
        Log.e(TAG,"ONCLICK")
        wrappedOnClickListener?.onClick(view)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        wrappedOnClickListener = l
    }

    //0 left 1 right
    fun setDefaultChooseLeftOrRight(leftOrRight : Int){
        initChoose = leftOrRight
    }

    private fun setTextViewColorGradient(tv : TextView, beginColor: String, endColor :String){

        val textShader: Shader = LinearGradient(
            0f, 0f, tv.width.toFloat(), tv.height.toFloat(),
            Color.parseColor(beginColor),
            Color.parseColor(endColor),
            Shader.TileMode.CLAMP
        )

        tv.paint.shader = textShader
        tv.invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initX = (right - left).toFloat()/2

        if(firstTimeOnLayout) {
            val container = binding.myButton
            val choose = binding.choose
            val tv1 = binding.tv1
            val tv2 = binding.tv2
            if (initChoose == 0) {
                tv1.isClickable = false
                setTextViewColorGradient(tv1, chooseTxtColor, chooseTxtSecondColor)
                setTextViewColorGradient(tv2, unChooseTxtColor, unChooseTxtSecondColor)
                val background_w = choose.width
                val container_w = container.width
                val x: Int = container_w - background_w
                val animator: ObjectAnimator =
                    ObjectAnimator.ofFloat(choose, "translationX", initX, 0f)
                tv1.isClickable = false
                tv2.isClickable = true
                animator.duration = 200
                animator.start()
            } else {
                tv2.isClickable = false
                setTextViewColorGradient(tv1, unChooseTxtColor, unChooseTxtSecondColor)
                setTextViewColorGradient(tv2, chooseTxtColor, chooseTxtSecondColor)
                val background_w = choose.width
                val container_w = container.width
                val x: Int = container_w - background_w
                Log.i(TAG, "INIT X :" + initX.toString())

                Log.i(TAG, "onClick: backgroud =%d%n$background_w")
                val animator: ObjectAnimator =
                    ObjectAnimator.ofFloat(choose, "translationX", 0f, initX)
                tv1.isClickable = true
                tv2.isClickable = false
                animator.duration = 200
                animator.start()
            }
            firstTimeOnLayout = false
        }
    }
}