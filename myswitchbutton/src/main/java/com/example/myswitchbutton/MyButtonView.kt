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
import kotlin.math.abs

class MyButtonView : ConstraintLayout, View.OnTouchListener, View.OnClickListener {

    private val HALF_LENGTH_X: Float by lazy { (myButton.width - choose.width).toFloat() }
    private val A_QUARTER_LENGTH_X: Float by lazy { (myButton.width / 4).toFloat() }
    private val ANIMATOR_LEFT_TO_RIGHT = 0
    private val ANIMATOR_RIGHT_TO_LEFT = 1
    private var wrappedOnClickListener: OnClickListener? = null
    private var wrappedOnTouchListener: OnTouchListener? = null

    private val defaultTxtUnChooseColor = "#999999"
    private val defaultTxtChooseColor = "#ffffff"
    private val defaultBtnBackgroundColor = "#000000"
    private val defaultBtnChooseColor = "#2bfbff"

    //會是view的一半
    private var initX = 0f
    private var initChoose = 0
    var nowChoose = 0
        private set
    private var firstTimeOnLayout = true

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyButtonView)
        getValue(typedArray)
        initView()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MyButtonView, defStyleAttr, 0)
        getValue(typedArray)
        initView()
    }

    private fun getValue(typedArray: TypedArray) {
        leftTxtString = typedArray.getString(R.styleable.MyButtonView_left_txt_string) ?: ""
        rightTxtString = typedArray.getString(R.styleable.MyButtonView_right_txt_string) ?: ""
        chooseTxtColor =
            typedArray.getString(R.styleable.MyButtonView_choose_txt_color) ?: defaultTxtChooseColor
        chooseTxtSecondColor =
            typedArray.getString(R.styleable.MyButtonView_choose_txt_second_color) ?: chooseTxtColor
        unChooseTxtColor = typedArray.getString(R.styleable.MyButtonView_un_choose_txt_color)
            ?: defaultTxtUnChooseColor
        unChooseTxtSecondColor =
            typedArray.getString(R.styleable.MyButtonView_un_choose_txt_second_color)
                ?: unChooseTxtColor
        txtSize = typedArray.getDimension(R.styleable.MyButtonView_txt_size, 10f)
        buttonBackgroundColor =
            typedArray.getString(R.styleable.MyButtonView_button_background_color)
                ?: defaultBtnBackgroundColor
        strokeColor =
            typedArray.getString(R.styleable.MyButtonView_stroke_color) ?: buttonBackgroundColor
        strokeWidth = typedArray.getInt(R.styleable.MyButtonView_stroke_width, 1)
        buttonChooseColor = typedArray.getString(R.styleable.MyButtonView_button_choose_color)
            ?: defaultBtnChooseColor
        buttonChooseTwoColor =
            typedArray.getString(R.styleable.MyButtonView_button_choose_two_color)
                ?: buttonChooseColor
        buttonRadius = typedArray.getDimension(R.styleable.MyButtonView_button_radius, 15f)

        typedArray.recycle()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.component_switch_buttom, this)
        val txtLayoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val myGrad = myButton.background as GradientDrawable
        val myChooseGrad = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(Color.parseColor(buttonChooseColor), Color.parseColor(buttonChooseTwoColor))
        )
        myChooseGrad.cornerRadius = buttonRadius
        myButton.layoutParams = txtLayoutParams
        myButton.setOnTouchListener(this)
        myButton.setOnClickListener(this)
        myGrad.setColor(Color.parseColor(buttonBackgroundColor))
        myGrad.cornerRadius = buttonRadius
        myGrad.setStroke(strokeWidth, Color.parseColor(strokeColor))
        choose.background = myChooseGrad
        tv1.text = leftTxtString
        tv1.textSize = txtSize
        tv2.text = rightTxtString
        tv2.textSize = txtSize
    }

    private var beginX = 0f
    private var finishX = 0f

    private var moveBeginX = 0f
    private var moveFinishX = 0f

    private var totalMoveX = 0f
    private var endOfLeftOrRight = noChoose

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event == null) return true
        wrappedOnTouchListener?.onTouch(v, event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                beginX = event.x
                moveBeginX = event.x
                return true
            }

            MotionEvent.ACTION_MOVE -> { //移動動畫
                moveFinishX = event.x
                endOfLeftOrRight = if (moveFinishX > moveBeginX) rightChoose else leftChoose

                //左邊左右滑動  右邊左右滑動
                if (nowChoose == leftChoose && choose.x <= choose.width && choose.x >= 0f) {
                    leftMoveAnimation(moveFinishX - beginX)
                } else if (nowChoose == rightChoose && choose.x <= choose.width && choose.x >= 0f) {
                    rightMoveAnimation(moveFinishX - beginX)
                }
                return true
            }


            MotionEvent.ACTION_UP -> {

                finishX = event.x

                //是左邊被選的時候 && 最後動作是右滑
                if (abs(finishX - beginX) > ViewConfiguration.get(context).scaledTouchSlop
                    && nowChoose == leftChoose
                    && endOfLeftOrRight == rightChoose
                ) {
                    val x: Int = myButton.width - choose.width
                    val animator: ObjectAnimator =
                        ObjectAnimator.ofFloat(choose, TRANSLATION_X, totalMoveX, x.toFloat())
                    animator.duration = 200
                    animator.start()
                }

                //是左邊被選的時候 && 動作是左滑
                if (abs(finishX - beginX) > ViewConfiguration.get(context).scaledTouchSlop
                    && nowChoose == leftChoose
                    && endOfLeftOrRight == leftChoose
                ) {
                    objectAnimator(0, ANIMATOR_RIGHT_TO_LEFT)
                }


                //是右邊被選的時候 && 動作是右滑
                if (abs(finishX - beginX) > ViewConfiguration.get(context).scaledTouchSlop
                    && nowChoose == rightChoose
                    && endOfLeftOrRight == rightChoose
                ) {
                    val x: Int = myButton.width - choose.width
                    val animator: ObjectAnimator =
                        ObjectAnimator.ofFloat(choose, TRANSLATION_X, totalMoveX, x.toFloat())
                    animator.duration = 200
                    animator.start()
                }

                //是右邊被選的時候 && 動作是左滑
                if (abs(finishX - beginX) > ViewConfiguration.get(context).scaledTouchSlop
                    && nowChoose == rightChoose
                    && endOfLeftOrRight == leftChoose
                ) {
                    objectAnimator(200, ANIMATOR_RIGHT_TO_LEFT)
                }

                //檢測移動的距離，如果很微小可以認為是點選事件
                if (abs(finishX - beginX) < ViewConfiguration.get(context).scaledTouchSlop) {
                    if (event.x < initX && nowChoose == rightChoose) {  //右邊被選 點擊左半邊
                        objectAnimator(200, ANIMATOR_RIGHT_TO_LEFT)
                    } else if (event.x < initX && nowChoose == leftChoose) {  //左邊被選 點擊左半邊
                        objectAnimator(0, ANIMATOR_RIGHT_TO_LEFT)
                    } else if (event.x > initX && nowChoose == leftChoose) {  //左邊被選 點擊右半邊
                        objectAnimator(200, ANIMATOR_LEFT_TO_RIGHT)
                    } else if (event.x > initX && nowChoose == rightChoose) {  //右邊被選 點擊右半邊
                        objectAnimator(0, ANIMATOR_LEFT_TO_RIGHT)
                    }
                }
                nowChoose = endOfLeftOrRight
                beginX = 0f
                finishX = 0f

                performClick()
                wrappedOnClickListener?.onClick(this)
                return false

            }
        }

        //通知 ViewGroup 要接收此事件，事件將不往下傳遞
        return false
    }

    private fun rightMoveAnimation(tempMoveX: Float) {
        var tempMoveX1 = tempMoveX
        if (tempMoveX1 * -1 > choose.width) {
            tempMoveX1 = choose.width * -1f
        }
        if (tempMoveX1 > 0) {
            tempMoveX1 = 0f
        }
        totalMoveX += moveFinishX - moveBeginX
        autoSettingChooseTxtColor()
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(
            choose,
            TRANSLATION_X,
            choose.width.toFloat(),
            choose.width + tempMoveX1
        )
        moveBeginX = moveFinishX
        animator.duration = 0
        animator.start()
    }

    private fun leftMoveAnimation(tempMoveX: Float) {
        var tempMoveX1 = tempMoveX
        if (tempMoveX1 > choose.width) {
            tempMoveX1 = if (tempMoveX1 > 0) choose.width * 1f else choose.width * -1f
        }
        if (tempMoveX1 < 0) {
            tempMoveX1 = 0f
        }
        totalMoveX += moveFinishX - moveBeginX
        autoSettingChooseTxtColor()
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(
            choose,
            TRANSLATION_X,
            beginX,
            tempMoveX1
        )
        moveBeginX = moveFinishX
        animator.duration = 0
        animator.start()
    }

    private fun autoSettingChooseTxtColor() {
        if (choose.x < A_QUARTER_LENGTH_X) {
            endOfLeftOrRight = leftChoose
            setTextViewColorGradient(tv1, chooseTxtColor, chooseTxtSecondColor)
            setTextViewColorGradient(tv2, unChooseTxtColor, unChooseTxtSecondColor)
        } else {
            endOfLeftOrRight = rightChoose
            setTextViewColorGradient(tv1, unChooseTxtColor, unChooseTxtSecondColor)
            setTextViewColorGradient(tv2, chooseTxtColor, chooseTxtSecondColor)
        }
    }

    override fun setOnTouchListener(l: OnTouchListener?) {
        wrappedOnTouchListener = l
    }

    //0 left 1 right
    fun setChooseLeftOrRight(leftOrRight: Int) {
        initChoose = leftOrRight
        nowChoose = initChoose
        if (leftOrRight == leftChoose) {
            setTextViewColorGradient(tv1, chooseTxtColor, chooseTxtSecondColor)
            setTextViewColorGradient(tv2, unChooseTxtColor, unChooseTxtSecondColor)
            nowChoose = leftChoose
        } else if (leftOrRight == rightChoose) {
            setTextViewColorGradient(tv1, unChooseTxtColor, unChooseTxtSecondColor)
            setTextViewColorGradient(tv2, chooseTxtColor, chooseTxtSecondColor)
            nowChoose = rightChoose
        }
    }

    private fun objectAnimator(timeToDelay: Long, ANIMATOR_FLAG: Int) {
        if (HALF_LENGTH_X == null) return
        val animator: ObjectAnimator
        animator = if (ANIMATOR_FLAG == ANIMATOR_LEFT_TO_RIGHT) {
            setTextViewColorGradient(tv1, unChooseTxtColor, unChooseTxtSecondColor)
            setTextViewColorGradient(tv2, chooseTxtColor, chooseTxtSecondColor)
            ObjectAnimator.ofFloat(choose, TRANSLATION_X, 0f, HALF_LENGTH_X)
        } else {
            setTextViewColorGradient(tv1, chooseTxtColor, chooseTxtSecondColor)
            setTextViewColorGradient(tv2, unChooseTxtColor, unChooseTxtSecondColor)
            ObjectAnimator.ofFloat(choose, TRANSLATION_X, HALF_LENGTH_X, 0f)
        }
        animator.duration = timeToDelay
        animator.start()
    }

    private fun setTextViewColorGradient(tv: TextView, beginColor: String, endColor: String) {
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
        initX = (right - left).toFloat() / 2

        if (firstTimeOnLayout) {
            val choose = choose
            val tv1 = tv1
            val tv2 = tv2
            if (initChoose == 0) {
                setTextViewColorGradient(tv1, chooseTxtColor, chooseTxtSecondColor)
                setTextViewColorGradient(tv2, unChooseTxtColor, unChooseTxtSecondColor)
                val animator: ObjectAnimator =
                    ObjectAnimator.ofFloat(choose, TRANSLATION_X, initX, 0f)
                animator.duration = 200
                animator.start()
            } else {
                setTextViewColorGradient(tv1, unChooseTxtColor, unChooseTxtSecondColor)
                setTextViewColorGradient(tv2, chooseTxtColor, chooseTxtSecondColor)
                val animator: ObjectAnimator =
                    ObjectAnimator.ofFloat(choose, TRANSLATION_X, 0f, initX)
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