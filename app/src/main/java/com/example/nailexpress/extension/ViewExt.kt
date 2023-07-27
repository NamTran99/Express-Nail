package com.example.nailexpress.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.res.TypedArray
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.support.core.view.ViewScopeOwner
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ArrayRes
import androidx.annotation.DimenRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.GravityCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseActivity
import com.example.nailexpress.functional.DecimalDigitsInputFilter
import com.example.nailexpress.functional.UsPhoneNumberFormatter
import com.example.nailexpress.views.widgets.LoadingCircleImage
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.ref.WeakReference
import java.util.*


fun View.onClick(callback: View.OnClickListener?) {
    val clickTime = 300
    setOnClickListener {
        val lastClick = getTag(R.id.tag_view_click) as? Long ?: 0L
        val now = System.currentTimeMillis()
        if (now - lastClick > clickTime) {
            callback?.onClick(it)
            setTag(R.id.tag_view_click, now)
        }
    }
}

infix fun Boolean.lockButton(button: MaterialButton) {
    button.isEnabled = this
    button.backgroundTintList =
        button.context.colorStateList(if (this) R.color.color_primary else R.color.gray)
}

infix fun Boolean.lockTvButton(button: TextView) {
    button.isEnabled = this
    button.backgroundTintList =
        button.context.colorStateList(if (this) R.color.color_primary else R.color.gray)
}

fun AppCompatEditText.getTextString() = this.text.toString()
fun EditText.getTextString() = this.text.toString()

fun RecyclerView.LayoutParams.itemMargin(position: Int, margin: Int) {
    setMargins(margin, if (position == 0) margin else 0, margin, margin)
}

fun CompoundButton.setCustomChecked(
    value: Boolean,
    listener: CompoundButton.OnCheckedChangeListener
) {
    setOnCheckedChangeListener(null)
    isChecked = value
    setOnCheckedChangeListener(listener)
}

fun View.showPopUp(menu: Int, action: (Int) -> Unit) {
    setOnClickListener {
        PopupMenu(context, this).apply {
            inflate(menu)
            setOnMenuItemClickListener { item ->
                (it as? TextView)?.let { textView ->
                    textView.text = item.title
                    action.invoke(item.itemId)
                    return@setOnMenuItemClickListener true
                }
                (it as? EditText)?.let { editText ->
                    editText.setText(item.title)
                    action.invoke(item.itemId)
                    return@setOnMenuItemClickListener true
                }
                action.invoke(item.itemId)
                true
            }
            show()
        }
    }
}

fun EditText.focus() {
    requestFocus()
    setSelection(0, length())
}

fun View.alpha(value: Int = 170) {
    this.background.alpha = value
}

fun View.rotationDropdownWithView(view: View) {
    val deg = if (this.rotation == 90f) 0f else 90f
    this.animate().rotation(deg).interpolator = AccelerateDecelerateInterpolator()
    view.show(this.rotation == 0f)
}

fun ImageView.startAnimVector(delays: Long = 100) {
    android.os.Handler(Looper.getMainLooper()).postDelayed({
        val drawable = this.drawable
        if (drawable is AnimatedVectorDrawableCompat)
            drawable.start()
        else if (drawable is AnimatedVectorDrawable)
            drawable.start()
    }, delays)
}

fun EditText.clearText() {
    setText("")
}

fun EditText.displayErrorAndFocus(@StringRes errorID: Int) {
    error = context.getString(errorID)
    showKeyboard()
}

fun View.disableAlpha() {
    this.background.alpha = 255//max
}

fun List<View>.alpha() {
    this.forEach { it.alpha() }
}

fun View.setMarginTop(it: Float) {
    (layoutParams as ViewGroup.MarginLayoutParams).topMargin = it.toInt()
}

fun View.setMarginTop(@DimenRes dimen: Int) {
    (layoutParams as ViewGroup.MarginLayoutParams).topMargin = if (dimen == 0) 0 else
        resources.getDimensionPixelSize(dimen)
}

@Suppress("unchecked_cast")
fun ViewGroup.setContentView(id: Int) {
    removeAllViews()
    if (id == 0) return
    var cache = tag as? HashMap<Int, View>
    if (cache == null) {
        cache = hashMapOf()
        tag = cache
    }
    val view = if (cache.containsKey(id)) cache[id] else {
        LayoutInflater.from(context).inflate(id, this, false).also {
            cache[id] = it
        }
    }
    addView(view)
}

fun ViewGroup.of(id: Int, function: ViewGroup.() -> Unit) {
    setContentView(id)
    function()
}

fun TextView.format(vararg format: Any): String {
    return text.toString().format(Locale.getDefault(), *format)
}

fun TextView.addSpan(
    spanValue: String,
    spanned: CharacterStyle,
    textValue: String = text.toString()
) {
    val span = SpannableString(textValue)
    val start = span.indexOf(spanValue)
    if (start == -1) return
    val end = start + spanValue.length
    span.setSpan(spanned, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    if (spanned is ClickableSpan) {
        movementMethod = LinkMovementMethod.getInstance()
    }
    setText(span, TextView.BufferType.SPANNABLE)
}

fun ViewGroup.inflate(id: Int): View {
    return LayoutInflater.from(context).inflate(id, this, false)
}

fun Context.with(
    attrs: AttributeSet?,
    type: IntArray,
    defStyleAttr: Int,
    function: (TypedArray) -> Unit
) {
    if (attrs != null) {
        val typed = obtainStyledAttributes(attrs, type, defStyleAttr, 0)
        function(typed)
        typed.recycle()
    }
}

operator fun View.plus(view: View): List<View> {
    return arrayListOf(this, view)
}

fun List<View>.visible(b: Boolean = true, callback: () -> Unit = {}) {
    val visible = if (b) View.VISIBLE else View.INVISIBLE
    forEach { it.visibility = visible }
    if (b) callback()
}

fun <T : View> T.show(b: Boolean = true, function: T.() -> Unit = {}) {
    visibility = if (b) {
        function()
        View.VISIBLE
    } else View.GONE
}

fun <T : View> T.visible(b: Boolean = true, function: T.() -> Unit = {}) {
    visibility = if (b) {
        function()
        View.VISIBLE
    } else View.INVISIBLE
}

fun View.hide(b: Boolean) {
    if (b) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
    }
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

infix fun Boolean.show(views: List<View>) {
    val visible = if (this) View.VISIBLE else View.GONE
    views.forEach { it.visibility = visible }
}

infix fun Boolean.visible(view: View) {
    view.visibility = if (this) View.VISIBLE else View.INVISIBLE
}

infix fun Boolean.show(view: View) {
    view.visibility = if (this) View.VISIBLE else View.GONE
}

infix fun Boolean.hide(view: View) {
    view.visibility = if (this) View.GONE else View.VISIBLE
}

fun List<View>.show(b: Boolean = true, callback: () -> Unit = {}) {
    val visible = if (b) View.VISIBLE else View.GONE
    forEach { it.visibility = visible }
    if (b) callback()
}

fun Array<View>.show(b: Boolean = true, callback: () -> Unit = {}) {
    val visible = if (b) View.VISIBLE else View.GONE
    forEach { it.visibility = visible }
    if (b) callback()
}

//fun Array<Button>.show(b: Boolean = true, callback: () -> Unit = {}) {
//    val visible = if (b) View.VISIBLE else View.GONE
//    forEach { it.visibility = visible }
//    if (b) callback()
//}

infix fun Boolean.lock(view: View) {
    view.isEnabled = !this
}

infix fun Boolean.lock(views: List<View>) {
    views.forEach { it.isEnabled = !this }
}

fun Array<View>.lock(b: Boolean = true, callback: () -> Unit = {}) {
    forEach { it.isEnabled = b }
    if (b) callback()
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Context.toPx(dp: Float): Int {
    return (dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

fun Context.toDp(px: Int): Float {
    return px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun View.addShadow(
    drawable: Drawable,
    radius: Float = 4f,
    blurMaskFilter: BlurMaskFilter.Blur = BlurMaskFilter.Blur.NORMAL
) {
    val originalBitmap = drawable.toBitmap(measuredWidth, measuredHeight)
    val blurFilter = BlurMaskFilter(radius, blurMaskFilter)
    val shadowPaint = Paint()
    shadowPaint.maskFilter = blurFilter

    val offsetXY = IntArray(2)
    val shadowImage = originalBitmap.extractAlpha(shadowPaint, offsetXY)

    /* Need to convert shadowImage from 8-bit to ARGB here. */
    val shadowImage32 = shadowImage.copy(Bitmap.Config.ARGB_8888, true)
    Canvas(shadowImage32)
        .drawBitmap(
            originalBitmap,
            -offsetXY[0].toFloat(),
            -offsetXY[1].toFloat(),
            null
        )
    background = BitmapDrawable(resources, shadowImage32)
}

fun List<View>.applyTo(callback: View.() -> Unit) {
    this.forEach { callback.invoke(it) }
}

fun <T> TextView.setTextAndTag(item: T, textOf: T.() -> String = { toString() }) {
    tag = item
    text = textOf(item)
}

fun Context.loadAttrs(attrs: AttributeSet?, attrType: IntArray, function: (TypedArray) -> Unit) {
    if (attrs == null) return
    val a = obtainStyledAttributes(attrs, attrType)
    function(a)
    a.recycle()
}

fun Context.getAppResourceId(attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.resourceId
}


fun AttributeSet?.load(
    view: View, attr: IntArray,
    function: TypedArray.() -> Unit
) {
    val context = view.context
    val ta = context.obtainStyledAttributes(this, attr)
    function(ta)
    ta.recycle()
}

@SuppressLint("RestrictedApi")
fun Context.getMenu(@MenuRes id: Int): Menu {
    val menu = MenuBuilder(this)
    MenuInflater(this).inflate(id, menu)
    return menu
}

fun LinearLayout.weightSum(weightSum: Float) {
    this.layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT,
        weightSum
    )
}

fun EditText.bind(any: (String) -> Unit) {
    doOnTextChanged { text, _, _, _ -> any(text?.toString().orEmpty()) }
}

fun CheckBox.bind(any: (Boolean) -> Unit) {
    setOnCheckedChangeListener { _, b -> any(b) }
}

fun View.setMargins(@DimenRes sizeRes: Int) {
    val size = resources.getDimensionPixelSize(sizeRes)
    this.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        this.setMargins(size, size, size, size)
    }
}

fun View.setMargins(@DimenRes horizontalSizeRes: Int, @DimenRes verticalSizeRes: Int) {
    val horizontalSize = resources.getDimensionPixelSize(horizontalSizeRes)
    val verticalSize = resources.getDimensionPixelSize(verticalSizeRes)
    this.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        this.setMargins(horizontalSize, verticalSize, horizontalSize, verticalSize)
    }
}

fun View.setMargins(
    @DimenRes leftRes: Int,
    @DimenRes topRes: Int,
    @DimenRes rightRes: Int,
    @DimenRes bottomRes: Int
) {
    val left = resources.getDimensionPixelSize(leftRes)
    val top = resources.getDimensionPixelSize(topRes)
    val right = resources.getDimensionPixelSize(rightRes)
    val bottom = resources.getDimensionPixelSize(bottomRes)
    this.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        this.setMargins(left, top, right, bottom)
    }
}

fun CheckBox.drawableStart(idDrawable: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(idDrawable, 0, 0, 0)
}

fun TextView.drawableStart(idDrawable: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(idDrawable, 0, 0, 0)
}


fun EditText.inputTypePhoneUS() {
    val addLineNumberFormatter = UsPhoneNumberFormatter(WeakReference(this))
    this.run {
        addTextChangedListener(addLineNumberFormatter)
        isFocusableInTouchMode = true
    }
}

fun SwipeRefreshLayout.colorSchemeDefault() {
    this.setColorSchemeResources(R.color.color_primary)
}

fun EditText.onSearchListener(onClickSearchAction: (String) -> Unit) {
    this.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onClickSearchAction.invoke(text.toString())
            true
        } else false
    }
}

fun View.showKeyboard(value: Boolean = true) {
    this.postDelayed(
        Runnable {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (value) {
                requestFocusFromTouch()
                (this as? EditText)?.setSelection(text.length)
                imm.showSoftInput(this, 0)
            } else imm.hideSoftInputFromWindow(windowToken, 0)
        }, 100
    )
}

infix fun DrawerLayout.openDrawerLayout(isOpen: Boolean) {
    if (isOpen)
        this.openDrawer(GravityCompat.START, true)
    else this.closeDrawer(GravityCompat.START, true)
}

infix fun DrawerLayout.lockDrawerLayout(isClose: Boolean) {
    if (isClose)
        this.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    else
        this.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
}

fun TextView.setDrawableStart(resIcon: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(resIcon, 0, 0, 0)
}

fun TextView.setDrawableEnd(resIcon: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, resIcon, 0)
}

fun ShapeableImageView.setImageUrl(url: String) {
    if (url.isBlank()) return
    Picasso.get().load(url).placeholder(R.drawable.ic_logo).error(R.drawable.ic_logo)
        .into(this)
}


fun ShapeableImageView.setImageUri(uri: Uri) {
    if (uri.toString().isBlank()) return
    Picasso.get().load(uri).placeholder(R.drawable.ic_logo).error(R.drawable.ic_logo)
        .into(this)
}

fun ViewScopeOwner.getActivityContext(): Context {
    return return when (this) {
        is BaseActivity<*> -> this
        is Fragment -> this.requireActivity()
        else -> error("")
    }
}


@SuppressLint("ClickableViewAccessibility")
fun EditText.scrollContentVertical() {
    this.setOnTouchListener { v, event ->
        if (this.hasFocus()) {
            v.parent.requestDisallowInterceptTouchEvent(true)
            when (event.action and android.view.MotionEvent.ACTION_MASK) {
                android.view.MotionEvent.ACTION_SCROLL -> {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                    return@setOnTouchListener true
                }
            }
        }
        return@setOnTouchListener false
    }
}

fun TextView.setTextViewDrawableColor(color: Int) {
    for (drawable in this.compoundDrawablesRelative) {
        if (drawable != null) {
            drawable.colorFilter =
                PorterDuffColorFilter(
                    ContextCompat.getColor(this.context, color),
                    PorterDuff.Mode.SRC_IN
                )
        }
    }
}

fun Spinner.configSpinner(
    isHaveHintFistItem: Boolean,
    @ArrayRes contentID: Int? = null,
    content: Array<String> = arrayOf(),
    onItemSelected: (position: Int) -> Unit = {}
) {
    val listItem =  contentID?.let {
        context.resources.getStringArray(it)
    }?:content
    adapter = object : ArrayAdapter<String>(
        context,
        R.layout.layout_spinner_item, listItem
    ) {
        override fun isEnabled(position: Int): Boolean {
            return if (isHaveHintFistItem) position != 0 else true
        }

        override fun getDropDownView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            val view: TextView =
                super.getDropDownView(position, convertView, parent) as TextView
            //set the color of first item in the drop down list to gray
            if (position == 0 && isHaveHintFistItem) {
                view.setTextColor(Color.GRAY)
            } else {
                //here it is possible to define color for other items by
                //view.setTextColor(Color.RED)
            }
            return view
        }
    }

    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            if (position == 0 && isHaveHintFistItem) {
                (view as TextView).setTextColor(Color.GRAY)
            } else {
                (view as TextView).setTextColor(Color.BLACK)
            }
            onItemSelected.invoke(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            //
        }
    }
}

fun Context.getLoadingCircleDrawable(): CircularProgressDrawable {
    val circularProgressDrawable = CircularProgressDrawable(this)
    circularProgressDrawable.strokeWidth = 10f
    circularProgressDrawable.centerRadius = 50f
    circularProgressDrawable.setColorSchemeColors(Color.GREEN, getColor(R.color.color_primary))
    circularProgressDrawable.start()
    return circularProgressDrawable
}

fun EditText.setInputSignedDecimal(digitsBeforeZero: Int = 4, digitsAfterZero: Int = 2) {
    filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(digitsBeforeZero, digitsAfterZero))
}

fun TabLayout.setDivider() {
    val root = getChildAt(0)
    if (root is LinearLayout) {
        root.apply {
            showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(context.getColor(R.color.line_divider))
            drawable.setSize(
                context.getDimension(R.dimen.size_1),
                context.getDimension(R.dimen.size_15)
            )
            dividerPadding = context.getDimension(R.dimen.size_10)
            dividerDrawable = drawable
        }
    }
}

fun Context.getDimension(@DimenRes id: Int) = resources.getDimension(id).toInt()

fun ImageView.setImageURICustom(link: String?, onLoadFinish: (() -> Unit) = {}) {
    if (link.isNullOrEmpty()) return
    if (link.contains("http")) {
        Picasso.get().load(link)
            .resize(1024, 1024)
            .centerInside()
            .placeholder(context.getLoadingCircleDrawable())
            .error(R.drawable.ic_logo)
            .into(this, object : Callback {
                override fun onSuccess() {
                    onLoadFinish.invoke()
                }

                override fun onError(e: Exception) {
                    onLoadFinish.invoke()
                }
            })
    } else {
        Picasso.get().load(link)
            .resize(1024, 1024)
            .centerInside()
            .transform(
                RotateTransformation(
                    getRotateDegree(context, link)
                )
            )
            .error(R.drawable.ic_logo)
            .into(this, object : Callback {
                override fun onSuccess() {
                    onLoadFinish.invoke()
                }

                override fun onError(e: Exception) {
                    onLoadFinish.invoke()
                }
            })
    }
}

fun CircleImageView.setImageURICustom(link: String?, onLoadFinish: (() -> Unit) = {}) {
    if (link.isNullOrEmpty()) {
        this.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_logo))
        return
    }
    if (link.contains("http")) {
        Picasso.get().load(link)
            .resize(1024, 1024)
            .centerInside()
            .placeholder(context.getLoadingCircleDrawable())
            .error(R.drawable.ic_logo)
            .into(this, object : Callback {
                override fun onSuccess() {
                    onLoadFinish.invoke()
                }

                override fun onError(e: Exception) {
                    onLoadFinish.invoke()
                }
            })
    } else {
        Picasso.get().load(link)
            .resize(1024, 1024)
            .centerInside()
            .placeholder(context.getLoadingCircleDrawable())
            .transform(
                RotateTransformation(
                    getRotateDegree(context, link)
                )
            )
            .error(R.drawable.ic_logo)
            .into(this, object : Callback {
                override fun onSuccess() {
                    onLoadFinish.invoke()
                }

                override fun onError(e: Exception) {
                    onLoadFinish.invoke()
                }
            })
    }
}


fun getFilePathFromContentUri(inContext: Context, contentUri: Uri?): String? {
    var filePath: String? = null
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val resolver: ContentResolver = inContext.contentResolver
    val cursor: Cursor? = resolver.query(contentUri!!, projection, null, null, null)
    if (cursor != null && cursor.moveToFirst()) {
        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        filePath = cursor.getString(columnIndex)
        cursor.close()
    }
    return filePath
}


fun EditText.addFilterLimitMaxNumber(max: Double) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Không cần thiết phải xử lý
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            try {
                val input = s.toString().toDouble()

                if (input > max) {
                    setText(max.display())
                    setSelection(max.display().length)
                }
            } catch (e: NumberFormatException) {
                // Xử lý ngoại lệ
            }
        }

        override fun afterTextChanged(s: Editable?) {
            // Không cần thiết phải xử lý
        }
    })
}

fun View.moveViewFromLeft(windowManager: WindowManager, distance: Float = 100f) {
    val displayMetrics = DisplayMetrics()
    windowManager.getDefaultDisplay().getMetrics(displayMetrics)
    val screenWidth = displayMetrics.widthPixels
    translationX = -screenWidth.toFloat()
    animate().translationX(0f).setDuration(1000).start()
}

fun Handler.postDelayedLatest(runnable: Runnable, delayMillis: Long) {
    removeCallbacksAndMessages(null)
    postDelayed(runnable, delayMillis)
}

fun List<View>.moveViewFromRight(
    windowManager: WindowManager,
    duration: Long = 1000,
    delay: Long = 100
) {
    val displayMetrics = DisplayMetrics()
    windowManager.getDefaultDisplay().getMetrics(displayMetrics)
    val screenWidth = displayMetrics.widthPixels
    var delayStep: Long = 0
    forEach {
        it.translationX = screenWidth.toFloat()
        it.animate().translationX(0f).setDuration(duration).setStartDelay(delayStep).start()
        delayStep += delay
    }
}

fun View.moveViewFromBottom(windowManager: WindowManager, duration: Long = 1000) {
    val displayMetrics = DisplayMetrics()
    windowManager.getDefaultDisplay().getMetrics(displayMetrics)
    val screenHeight = displayMetrics.heightPixels
    translationY = screenHeight.toFloat()
    animate().translationY(0f).setDuration(duration).start()
}

fun View.moveViewFromTop(windowManager: WindowManager, duration: Long = 1000) {
    val displayMetrics = DisplayMetrics()
    windowManager.getDefaultDisplay().getMetrics(displayMetrics)
    val screenHeight = displayMetrics.heightPixels
    translationY = -200f
    animate().translationY(0f).setDuration(duration).start()
}

fun EditText.seekCursorToLast(): EditText {
    post { setSelection(length()) }
    return this
}


@SuppressLint("ClickableViewAccessibility")
fun EditText.drawableClickRight(action: () -> Unit) {
    this.setOnTouchListener { v, event ->
        this.compoundDrawables.getOrNull(2)?.bounds?.let {
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= this.right - it.width()
                ) {

                    action.invoke()
                    return@setOnTouchListener true
                }
            }
        }
        false
    }
}

fun View.hideKeyboard(v: View) {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
}