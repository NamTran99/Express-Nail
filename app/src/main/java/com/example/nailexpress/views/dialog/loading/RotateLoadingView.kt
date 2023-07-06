package com.example.nailexpress.views.dialog.loading

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.nailexpress.R

class RotateLoadingView : View {

    private var mPaint: Paint? = null
    private var mLoadingBound: RectF? = null
    private var mShadowBound: RectF? = null

    private var mTopDegree = 10
    private var mBottomDegree = 190
    private var mWidth: Int = 0
    private var mShadowPosition: Int = 0
    var loadingColor: Int = 0
    private var mSpeedOfDegree: Int = 0
    private var mSpeedOfArc: Float = 0.toFloat()
    private var mArc: Float = 0.toFloat()
    private var mChangeBigger = true
    var isStart = false
        private set

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        loadingColor = Color.WHITE
        mWidth = dpToPx(context, DEFAULT_WIDTH.toFloat())
        mShadowPosition = dpToPx(getContext(), DEFAULT_SHADOW_POSITION.toFloat())
        mSpeedOfDegree = DEFAULT_SPEED_OF_DEGREE
        if (null != attrs) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RotateLoadingView)
            loadingColor =
                typedArray.getColor(R.styleable.RotateLoadingView_loading_color, Color.WHITE)
            mWidth = typedArray.getDimensionPixelSize(
                R.styleable.RotateLoadingView_loading_width,
                dpToPx(context, DEFAULT_WIDTH.toFloat())
            )
            mShadowPosition = typedArray.getInt(
                R.styleable.RotateLoadingView_shadow_position,
                DEFAULT_SHADOW_POSITION
            )
            mSpeedOfDegree = typedArray.getInt(
                R.styleable.RotateLoadingView_loading_speed,
                DEFAULT_SPEED_OF_DEGREE
            )
            typedArray.recycle()
        }
        mSpeedOfArc = (mSpeedOfDegree / 4).toFloat()
        mPaint = Paint()
        mPaint!!.color = loadingColor
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = mWidth.toFloat()
        mPaint!!.strokeCap = Paint.Cap.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mArc = 10f

        mLoadingBound = RectF(
            (2 * mWidth).toFloat(),
            (2 * mWidth).toFloat(),
            (w - 2 * mWidth).toFloat(),
            (h - 2 * mWidth).toFloat()
        )
        mShadowBound = RectF(
            (2 * mWidth + mShadowPosition).toFloat(),
            (2 * mWidth + mShadowPosition).toFloat(),
            (w - 2 * mWidth + mShadowPosition).toFloat(),
            (h - 2 * mWidth + mShadowPosition).toFloat()
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isStart) return

        render(canvas)

        update()

        invalidate()
    }

    private fun update() {
        mTopDegree += mSpeedOfDegree
        mBottomDegree += mSpeedOfDegree
        mTopDegree = if (mTopDegree > 360) mTopDegree - 360 else mTopDegree
        mBottomDegree = if (mBottomDegree > 360) mBottomDegree - 360 else mBottomDegree

        if (mChangeBigger) {
            if (mArc < 160) {
                mArc += mSpeedOfArc
                return
            }
        } else {
            if (mArc > mSpeedOfDegree) {
                mArc -= 2 * mSpeedOfArc
                return
            }
        }
        if (mArc >= 160 || mArc <= 10) {
            mChangeBigger = !mChangeBigger
        }
    }

    private fun render(canvas: Canvas) {
        drawShadow(canvas)
        drawLoading(canvas)
    }

    private fun drawLoading(canvas: Canvas) {
        mPaint!!.color = loadingColor
        canvas.drawArc(mLoadingBound!!, mTopDegree.toFloat(), mArc, false, mPaint!!)
        canvas.drawArc(mLoadingBound!!, mBottomDegree.toFloat(), mArc, false, mPaint!!)
    }

    private fun drawShadow(canvas: Canvas) {
        mPaint!!.color = DEFAULT_SHADOW_COLOR
        canvas.drawArc(mShadowBound!!, mTopDegree.toFloat(), mArc, false, mPaint!!)
        canvas.drawArc(mShadowBound!!, mBottomDegree.toFloat(), mArc, false, mPaint!!)
    }

    fun start() {
        if (isStart) return
        startAnimator()
        isStart = true
        invalidate()
    }

    fun stop() {
        if (!isStart) return
        stopAnimator()
        invalidate()
    }

    private fun startAnimator() {
        val scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 0.0f, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 0.0f, 1f)
        scaleXAnimator.setDuration(300).interpolator = LinearInterpolator()
        scaleYAnimator.setDuration(300).interpolator = LinearInterpolator()

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator)
        animatorSet.start()
    }

    private fun stopAnimator() {
        val scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0f)
        val scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0f)
        scaleXAnimator.setDuration(300).interpolator = LinearInterpolator()
        scaleYAnimator.setDuration(300).interpolator = LinearInterpolator()
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator)
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                isStart = false
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        animatorSet.start()
    }


    fun dpToPx(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal,
            context.resources.displayMetrics
        ).toInt()
    }

    companion object {

        private const val DEFAULT_WIDTH = 6
        private const val DEFAULT_SHADOW_POSITION = 2
        private const val DEFAULT_SPEED_OF_DEGREE = 10
        private val DEFAULT_SHADOW_COLOR = Color.parseColor("#1a000000")
    }

}
