package com.example.myswitchbutton

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.*
import android.widget.TextView
import kotlinx.android.synthetic.main.component_switch_buttom.view.*
import com.example.myswitchbutton.*
import com.example.myswitchbutton.MyButtonParameter.buttonBackgroundColor
import com.example.myswitchbutton.MyButtonParameter.buttonChooseColor
import com.example.myswitchbutton.MyButtonParameter.buttonChooseTwoColor
import com.example.myswitchbutton.MyButtonParameter.buttonRadius
import com.example.myswitchbutton.MyButtonParameter.chooseTxtColor
import com.example.myswitchbutton.MyButtonParameter.chooseTxtSecondColor
import com.example.myswitchbutton.MyButtonParameter.leftChoose
import com.example.myswitchbutton.MyButtonParameter.leftTxtString
import com.example.myswitchbutton.MyButtonParameter.noChoose
import com.example.myswitchbutton.MyButtonParameter.rightChoose
import com.example.myswitchbutton.MyButtonParameter.rightTxtString
import com.example.myswitchbutton.MyButtonParameter.strokeColor
import com.example.myswitchbutton.MyButtonParameter.strokeWidth
import com.example.myswitchbutton.MyButtonParameter.txtSize
import com.example.myswitchbutton.MyButtonParameter.unChooseTxtColor
import com.example.myswitchbutton.MyButtonParameter.unChooseTxtSecondColor

class MyButtonView : ConstraintLayout, View.OnTouchListener ,View.OnClickListener{
    val TAG ="MyButtonView"

    val ANIMATOR_LEFT_TO_RIGHT = 0
    val ANIMATOR_RIGHT_TO_LEFT = 1
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




    private fun getValue(typedArray : TypedArray) {
        leftTxtString = typedArray.getString(R.styleable.MyButtonView_left_txt_string) ?: ""
        rightTxtString = typedArray.getString(R.styleable.MyButtonView_right_txt_string) ?: ""
        chooseTxtColor = typedArray.getString(R.styleable.MyButtonView_choose_txt_color) ?: defaultTxtChooseColor
        chooseTxtSecondColor = typedArray.getString(R.styleable.MyButtonView_choose_txt_second_color) ?: chooseTxtColor
        unChooseTxtColor = typedArray.getString(R.styleable.MyButtonView_un_choose_txt_color) ?: defaultTxtUnChooseColor
        unChooseTxtSecondColor = typedArray.getString(R.styleable.MyButtonView_un_choose_txt_second_color) ?: unChooseTxtColor
        txtSize = typedArray.getDimension(R.styleable.MyButtonView_txt_size,10f)
        buttonBackgroundColor = typedArray.getString(R.styleable.MyButtonView_button_background_color) ?: "#000000"
        strokeColor = typedArray.getString(R.styleable.MyButtonView_stroke_color) ?: buttonBackgroundColor
        strokeWidth = typedArray.getInt(R.styleable.MyButtonView_stroke_width,1)
        buttonChooseColor = typedArray.getString(R.styleable.MyButtonView_button_choose_color) ?: "#2bfbff"
        buttonChooseTwoColor = typedArray.getString(R.styleable.MyButtonView_button_choose_two_color) ?: buttonChooseColor
        buttonRadius = typedArray.getDimension(R.styleable.MyButtonView_button_radius,15f)

        typedArray.recycle()
    }

    private fun initView(){
//        binding = ComponentSwitchButtominflate(LayoutInflater.from(context), this)
        LayoutInflater.from(context).inflate(R.layout.component_switch_buttom,this)
        val txtLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        myButton.layoutParams = txtLayoutParams
        tv1.text = leftTxtString
        tv1.textSize = txtSize
        tv2.text = rightTxtString
        tv2.textSize = txtSize

        val myGrad = myButton.background as GradientDrawable
        myGrad.setColor(Color.parseColor(buttonBackgroundColor))
        myGrad.cornerRadius = buttonRadius
        myGrad.setStroke(strokeWidth,Color.parseColor(strokeColor))

        val myChooseGrad = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(Color.parseColor(buttonChooseColor), Color.parseColor(buttonChooseTwoColor))
        )
        myChooseGrad.cornerRadius = buttonRadius
        choose.background = myChooseGrad

        myButton.setOnTouchListener(this)
        myButton.setOnClickListener(this)
    }

    var beginX = 0f
    var finishX = 0f
    var beginY = 0f
    var finishY = 0f

    var moveBeginX = 0f
    var moveFinishX = 0f

    var totalMoveX = 0f
    var moveLeftOrRight = noChoose

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if(event == null) return true

        if(wrappedOnTouchListener != null) {
            wrappedOnTouchListener?.onTouch(v, event)
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN ->{
                beginX = event.x
                beginY = event.y

                moveBeginX = event.x
                return true

            }

            MotionEvent.ACTION_MOVE ->{ //移動動畫

                moveFinishX = event.x
                var tempMoveX = moveFinishX-beginX

                moveLeftOrRight = if(moveFinishX > moveBeginX) rightChoose else leftChoose

                if (getChoose() == leftChoose
                    &&  choose.x <= choose.width &&  choose.x >= 0f) {

                    if(tempMoveX > choose.width){
                        tempMoveX = if (tempMoveX > 0) choose.width*1f else choose.width*-1f
                    }
                    if(tempMoveX < 0){
                        tempMoveX = 0f
                    }
                    totalMoveX +=  moveFinishX - moveBeginX
                    val choose = choose
                    val tv1 = tv1
                    val tv2 = tv2
                    setTextViewColorGradient(tv1,unChooseTxtColor,unChooseTxtSecondColor)
                    setTextViewColorGradient(tv2,chooseTxtColor,chooseTxtSecondColor)
                    val animator: ObjectAnimator = ObjectAnimator.ofFloat(choose, "translationX", beginX, tempMoveX)
                    moveBeginX = moveFinishX
                    animator.duration = 0
                    animator.start()
                }

                // 右邊左右滑動
                if (getChoose() == rightChoose
                    &&  choose.x <= choose.width &&  choose.x >= 0f) {
                    if(tempMoveX * -1 > choose.width){
                        tempMoveX = choose.width*-1f
                    }
                    if(tempMoveX > 0){
                        tempMoveX = 0f
                    }
                    totalMoveX +=  moveFinishX - moveBeginX
                    val choose = choose
                    val tv1 = tv1
                    val tv2 = tv2
                    setTextViewColorGradient(tv1,chooseTxtColor,chooseTxtSecondColor)
                    setTextViewColorGradient(tv2,unChooseTxtColor,unChooseTxtSecondColor)
                    val animator: ObjectAnimator = ObjectAnimator.ofFloat(choose, "translationX", choose.width.toFloat(),choose.width+tempMoveX)
                    moveBeginX = moveFinishX
                    animator.duration = 0
                    animator.start()
                }
                return true

            }



            MotionEvent.ACTION_UP ->{
                //左邊開始 左右滑動 && 結束方向
                if(moveLeftOrRight == leftChoose && getChoose() == leftChoose){
                    if(choose.x > myButton.width*1/8) {
                        moveLeftOrRight = rightChoose
                    }
                }else if(moveLeftOrRight == rightChoose && getChoose() == leftChoose){
                    if(choose.x < myButton.width*1/8) {
                        moveLeftOrRight = leftChoose
                    }
                }
                //右邊開始 左右滑動 && 結束方向
                else if(moveLeftOrRight == leftChoose && getChoose() == rightChoose){
                    if(choose.x > myButton.width*3/8) {
                        moveLeftOrRight = rightChoose
                    }
                }else if(moveLeftOrRight == rightChoose && getChoose() == rightChoose){
                    if(choose.x < myButton.width*3/8) {
                        moveLeftOrRight = leftChoose
                    }
                }

                finishX = event.x
                finishY = event.y

                //是左邊被選的時候 && 最後動作是右滑
                if (Math.abs(finishX - beginX)  > ViewConfiguration.get(context).scaledTouchSlop
                    && getChoose() == leftChoose
                    && moveLeftOrRight == rightChoose) {
//                    Log.e(TAG,"ACTION_UP MOVE : 1 ")
                    val container = myButton
                    val choose = choose
                    val tv1 = tv1
                    val tv2 = tv2
                    setTextViewColorGradient(tv1,unChooseTxtColor,unChooseTxtSecondColor)
                    setTextViewColorGradient(tv2,chooseTxtColor,chooseTxtSecondColor)
                    val background_w = choose.width
                    val container_w = container.width
                    val x: Int = container_w - background_w
                    val animator: ObjectAnimator = ObjectAnimator.ofFloat(choose, "translationX", totalMoveX, x.toFloat())
                    nowchoose = rightChoose
                    animator.duration = 200
                    animator.start()
                }

                //是左邊被選的時候 && 動作是左滑
                if (Math.abs(finishX - beginX ) > ViewConfiguration.get(context).scaledTouchSlop
                    && getChoose() == leftChoose
                    && moveLeftOrRight == leftChoose) {
                    val container = myButton
                    val choose = choose
                    val tv1 = tv1
                    val tv2 = tv2
                    setTextViewColorGradient(tv1,chooseTxtColor,chooseTxtSecondColor)
                    setTextViewColorGradient(tv2,unChooseTxtColor,unChooseTxtSecondColor)
                    val background_w = choose.width
                    val container_w = container.width
                    val x: Int = container_w - background_w
                    val animator: ObjectAnimator = ObjectAnimator.ofFloat(choose, "translationX",  x.toFloat(),0f)
                    nowchoose = leftChoose
                    animator.duration = 0
                    animator.start()
                }


                //是右邊被選的時候 && 動作是右滑
                if (Math.abs(finishX - beginX) > ViewConfiguration.get(context).scaledTouchSlop
                    && getChoose() == rightChoose
                    && moveLeftOrRight == rightChoose) {
//                    Log.e(TAG,"ACTION_UP MOVE : 3 ")
                    val container = myButton
                    val choose = choose
                    val tv1 = tv1
                    val tv2 = tv2
                    setTextViewColorGradient(tv1,unChooseTxtColor,unChooseTxtSecondColor)
                    setTextViewColorGradient(tv2,chooseTxtColor,chooseTxtSecondColor)
                    val background_w = choose.width
                    val container_w = container.width
                    val x: Int = container_w - background_w
                    val animator: ObjectAnimator = ObjectAnimator.ofFloat(choose, "translationX",  totalMoveX , x.toFloat())
                    nowchoose = rightChoose
                    animator.duration = 200
                    animator.start()
                }

                //是右邊被選的時候 && 動作是左滑
                if (Math.abs(finishX - beginX) > ViewConfiguration.get(context).scaledTouchSlop
                    && getChoose() == rightChoose
                    && moveLeftOrRight == leftChoose) {
//                    Log.e(TAG,"ACTION_UP MOVE : 4 ")
                    val container = myButton
                    val choose = choose
                    val tv1 = tv1
                    val tv2 = tv2
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
                if (Math.abs(finishX - beginX) < ViewConfiguration.get(context).scaledTouchSlop){
                    val tv1 = tv1
                    val tv2 = tv2
                    if (event.x < initX && getChoose() == rightChoose){  //點擊左半邊
                        setTextViewColorGradient(tv1,chooseTxtColor,chooseTxtSecondColor)
                        setTextViewColorGradient(tv2,unChooseTxtColor,unChooseTxtSecondColor)
                        nowchoose = leftChoose
                        objectAnimator(200, ANIMATOR_RIGHT_TO_LEFT)
                    }else if (event.x < initX && getChoose() == leftChoose){  //點擊左半邊
//                        Log.e(TAG,"ACTION_UP MOVE : 6 ")
                        setTextViewColorGradient(tv1,chooseTxtColor,chooseTxtSecondColor)
                        setTextViewColorGradient(tv2,unChooseTxtColor,unChooseTxtSecondColor)
                        nowchoose = leftChoose
                        objectAnimator(0, ANIMATOR_RIGHT_TO_LEFT)
                    }

                    if (event.x > initX && getChoose() == leftChoose) {  //點擊右半邊
//                        Log.e(TAG,"ACTION_UP MOVE : 7 ")
                        setTextViewColorGradient(tv1,unChooseTxtColor,unChooseTxtSecondColor)
                        setTextViewColorGradient(tv2,chooseTxtColor,chooseTxtSecondColor)
                        nowchoose = rightChoose
                        objectAnimator(200, ANIMATOR_LEFT_TO_RIGHT)
                    }else if(event.x > initX && getChoose() == rightChoose) {  //點擊右半邊
//                        Log.e(TAG,"ACTION_UP MOVE : 8 ")
                        setTextViewColorGradient(tv1,unChooseTxtColor,unChooseTxtSecondColor)
                        setTextViewColorGradient(tv2,chooseTxtColor,chooseTxtSecondColor)
                        nowchoose = rightChoose
                        objectAnimator(0, ANIMATOR_LEFT_TO_RIGHT)
                    }
                }

                beginX = 0f
                finishX = 0f
                beginY = 0f
                finishY = 0f

                performClick()
                wrappedOnClickListener?.onClick(this)
                return false

            }
        }

        //通知 ViewGroup 要接收此事件，事件將不往下傳遞
        return false
    }

    override fun setOnTouchListener(l: OnTouchListener?) {
        wrappedOnTouchListener = l
    }

    //0 left 1 right
    fun setChooseLeftOrRight(leftOrRight : Int){
        initChoose = leftOrRight
        nowchoose = initChoose
        val container = myButton
        val choose = choose
        val tv1 = tv1
        val tv2 = tv2
        if(leftOrRight == leftChoose){
            setTextViewColorGradient(tv1,chooseTxtColor,chooseTxtSecondColor)
            setTextViewColorGradient(tv2,unChooseTxtColor,unChooseTxtSecondColor)
            val background_w = choose.width
            val container_w = container.width
            val x: Int = container_w - background_w
            val animator: ObjectAnimator = ObjectAnimator.ofFloat(choose, "translationX",  x.toFloat(),0f)
            nowchoose = leftChoose
            animator.duration = 0
            animator.start()
        }else if(leftOrRight == rightChoose){
            setTextViewColorGradient(tv1,unChooseTxtColor,unChooseTxtSecondColor)
            setTextViewColorGradient(tv2,chooseTxtColor,chooseTxtSecondColor)
            val background_w = choose.width
            val container_w = container.width
            val x: Int = container_w - background_w
            val animator: ObjectAnimator = ObjectAnimator.ofFloat(choose, "translationX", 0f, x.toFloat())
            nowchoose = rightChoose
            animator.duration = 0
            animator.start()
        }
    }

    private fun objectAnimator(timeToDelay:Long,ANIMATOR_FLAG:Int){
        val container = myButton
        val choose = choose
        val backgroundW = choose.width
        val containerW = container.width
        val x: Int = containerW - backgroundW
        var animator: ObjectAnimator
        animator = if(ANIMATOR_FLAG == ANIMATOR_LEFT_TO_RIGHT) {
            ObjectAnimator.ofFloat(choose, TRANSLATION_X, 0f, x.toFloat())
        }else {
            ObjectAnimator.ofFloat(choose, TRANSLATION_X, x.toFloat(), 0f)
        }
        animator.duration = timeToDelay
        animator.start()
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
            val choose = choose
            val tv1 = tv1
            val tv2 = tv2
            if (initChoose == 0) {
                setTextViewColorGradient(tv1, chooseTxtColor, chooseTxtSecondColor)
                setTextViewColorGradient(tv2, unChooseTxtColor, unChooseTxtSecondColor)
                val animator: ObjectAnimator =
                    ObjectAnimator.ofFloat(choose, "translationX", initX, 0f)
                animator.duration = 200
                animator.start()
            } else {
                setTextViewColorGradient(tv1, unChooseTxtColor, unChooseTxtSecondColor)
                setTextViewColorGradient(tv2, chooseTxtColor, chooseTxtSecondColor)
                val animator: ObjectAnimator =
                    ObjectAnimator.ofFloat(choose, "translationX", 0f, initX)
                animator.duration = 200
                animator.start()
            }
            firstTimeOnLayout = false
        }
    }


    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun getChoose() : Int{
        return nowchoose
    }

    override fun onClick(view: View?) {
        wrappedOnClickListener?.onClick(view)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        wrappedOnClickListener = l
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }
}