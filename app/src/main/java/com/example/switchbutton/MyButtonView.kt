package com.example.switchbutton

import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.switchbutton.databinding.ComponentSwitchButtomBinding


class MyButtonView : ConstraintLayout, View.OnTouchListener{
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
    var wrappedOnTouchListener: OnTouchListener ? = null

    private val defaultTxtUnChooseColor = "#999999"
    private val defaultTxtChooseColor = "#ffffff"

    //會是view的一半
    private var initX = 0f
    private var initChoose = 0
    private var nowchoose = 0
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

        binding.myButton.setOnTouchListener(this)

        //動畫設定
        val container = binding.myButton
        val choose = binding.choose
        val tv1 = binding.tv1
        val tv2 = binding.tv2
    }

    var beginX = 0f
    var finishX = 0f
    var beginY = 0f
    var finishY = 0f
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        if(event == null) return true
        when (event.action) {
            MotionEvent.ACTION_DOWN ->{
                beginX = event.x
                beginY = event.y
            }
            MotionEvent.ACTION_UP ->{
                finishX = event.x
                finishY = event.y
                if (finishX - beginX > ViewConfiguration.get(context).scaledTouchSlop && getChoose() == leftChoose) {//右滑 && 是左邊被選的時候

                    val container = binding.myButton
                    val choose = binding.choose
                    val tv1 = binding.tv1
                    val tv2 = binding.tv2
                    setTextViewColorGradient(tv1,unChooseTxtColor,unChooseTxtSecondColor)
                    setTextViewColorGradient(tv2,chooseTxtColor,chooseTxtSecondColor)
                    val background_w = choose.width
                    val container_w = container.width
                    val x: Int = container_w - background_w
                    val animator: ObjectAnimator = ObjectAnimator.ofFloat(choose, "translationX", 0f, x.toFloat())
                    nowchoose = rightChoose
                    animator.duration = 200
                    animator.start()
                }

                if ((finishX - beginX)*-1 > ViewConfiguration.get(context).scaledTouchSlop  && getChoose() == rightChoose) {//左滑 && 是右邊被選的時候

                    val container = binding.myButton
                    val choose = binding.choose
                    val tv1 = binding.tv1
                    val tv2 = binding.tv2
                    setTextViewColorGradient(tv1,chooseTxtColor,chooseTxtSecondColor)
                    setTextViewColorGradient(tv2,unChooseTxtColor,unChooseTxtSecondColor)
                    val background_w = choose.width
                    val container_w = container.width
                    val x: Int = container_w - background_w
                    val animator: ObjectAnimator = ObjectAnimator.ofFloat(choose, "translationX",  x.toFloat(),0f)
                    nowchoose = leftChoose
                    animator.duration = 200
                    animator.start()
                }

                //檢測移動的距離，如果很微小可以認為是點選事件
                if (Math.abs(finishX - beginX) < 10 && Math.abs(finishY - beginY) < 10) {

                    val container = binding.myButton
                    val choose = binding.choose
                    val tv1 = binding.tv1
                    val tv2 = binding.tv2
                    if (event.x < initX && getChoose() == rightChoose){  //點擊左半邊
                        setTextViewColorGradient(tv1,chooseTxtColor,chooseTxtSecondColor)
                        setTextViewColorGradient(tv2,unChooseTxtColor,unChooseTxtSecondColor)
                        val background_w = choose.width
                        val container_w = container.width
                        val x: Int = container_w - background_w
                        val animator: ObjectAnimator = ObjectAnimator.ofFloat(choose, "translationX",  x.toFloat(),0f)
                        nowchoose = leftChoose
                        animator.duration = 200
                        animator.start()
                    }
                    if (event.x > initX && getChoose() == leftChoose) {  //點擊右半邊
                        setTextViewColorGradient(tv1,unChooseTxtColor,unChooseTxtSecondColor)
                        setTextViewColorGradient(tv2,chooseTxtColor,chooseTxtSecondColor)
                        val background_w = choose.width
                        val container_w = container.width
                        val x: Int = container_w - background_w
                        val animator: ObjectAnimator = ObjectAnimator.ofFloat(choose, "translationX", 0f, x.toFloat())
                        nowchoose = rightChoose
                        animator.duration = 200
                        animator.start()
                    }

                }

                beginX = 0f
                finishX = 0f
                beginY = 0f
                finishY = 0f
            }
        }

        wrappedOnTouchListener?.onTouch(v, event)
        //通知 ViewGroup 要接收此事件，事件將不往下傳遞
        return true
    }

    override fun setOnTouchListener(l: OnTouchListener?) {
        wrappedOnTouchListener = l
    }

    //0 left 1 right
    fun setDefaultChooseLeftOrRight(leftOrRight : Int){
        initChoose = leftOrRight
        nowchoose = initChoose
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
                setTextViewColorGradient(tv1, chooseTxtColor, chooseTxtSecondColor)
                setTextViewColorGradient(tv2, unChooseTxtColor, unChooseTxtSecondColor)
                val background_w = choose.width
                val container_w = container.width
                val x: Int = container_w - background_w
                val animator: ObjectAnimator =
                    ObjectAnimator.ofFloat(choose, "translationX", initX, 0f)
                animator.duration = 200
                animator.start()
            } else {
                setTextViewColorGradient(tv1, unChooseTxtColor, unChooseTxtSecondColor)
                setTextViewColorGradient(tv2, chooseTxtColor, chooseTxtSecondColor)
                val background_w = choose.width
                val container_w = container.width
                val x: Int = container_w - background_w
                val animator: ObjectAnimator =
                    ObjectAnimator.ofFloat(choose, "translationX", 0f, initX)
                animator.duration = 200
                animator.start()
            }
            firstTimeOnLayout = false
        }
    }


    // Because we call this from onTouchEvent, this code will be executed for both
    // normal touch events and for when the system calls this using Accessibility
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun getChoose() : Int{
        return nowchoose
    }
}