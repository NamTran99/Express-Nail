package com.example.nailexpress.utils

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.extension.*
import com.example.nailexpress.views.widgets.PasswordLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import java.util.*

typealias TabChangeCallBack = ((Int) -> Unit)

object BindingAdapterUtils {


    @InverseBindingAdapter(attribute = "text", event = "textAttrChanged")
    @JvmStatic
    fun getText(view: PasswordLayout): String {
        return view.binding.edtPassword.getTextString()
    }

    @BindingAdapter("textAttrChanged")
    @JvmStatic
    fun setListeners(
        view: PasswordLayout,
        attrChange: InverseBindingListener
    ) {
        view.binding.edtPassword.doOnTextChanged { _, _, _, _ ->
            attrChange.onChange()
        }
    }

    // Search view
    @BindingAdapter("query")
    @JvmStatic
    fun setQueryValue(view: SearchView, value: String?) {
        if (value != view.query) view.setQuery(value, true)
    }

    @InverseBindingAdapter(attribute = "query", event = "queryAttrChanged")
    @JvmStatic
    fun getTextValue(view: SearchView): String = view.query.toString()


    @BindingAdapter(value = ["queryAttrChanged", "setOnChangeTextListener"], requireAll = false)
    @JvmStatic
    fun setListener(
        view: SearchView,
        listener: InverseBindingListener,
        callback: ((String) -> Unit)
    ) {
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                listener.onChange()
                callback.invoke(newText)
                return true
            }
        })
    }

    //Tab Layout

    @BindingAdapter("tabPos")
    @JvmStatic
    fun setTab(view: TabLayout, pos: Int) {
        //
    }

    @InverseBindingAdapter(attribute = "tabPos", event = "tabPosAttrChanged")
    @JvmStatic
    fun getTabValue(view: TabLayout): Int = view.selectedTabPosition

    @BindingAdapter(value = ["tabPosAttrChanged", "onTabChangeListener"], requireAll = false)
    @JvmStatic
    fun setListener(
        view: TabLayout,
        listener: InverseBindingListener,
        onTabChange: TabChangeCallBack
    ) {
        view.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                onTabChange.invoke(tab.position)
                listener.onChange()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // do nothing
            }

        })
    }

    // SwitchCompat
    @InverseBindingAdapter(attribute = "value", event = "valueAttrChanged")
    @JvmStatic
    fun getValue(view: SwitchCompat) = view.isChecked or1 1 or2 0

    @BindingAdapter(value = ["valueAttrChanged"])
    @JvmStatic
    fun setValueChanged(view: SwitchCompat, listener: InverseBindingListener) {
        view.setOnCheckedChangeListener { buttonView, isChecked -> listener.onChange() }
    }

    @BindingAdapter(value = ["value"])
    @JvmStatic
    fun setValue(view: SwitchCompat, value: Int) {
        view.isChecked = value == 1
    }

    // spinner
    @BindingAdapter(value = ["items", "onItemSelected"], requireAll = true)
    @JvmStatic
    fun setItems(view: AppCompatSpinner, value: Array<String>?, onItemSelected: ((Int) -> Unit)?) {
        value?.let {
            view.configSpinner(true, content = value) {
                onItemSelected?.invoke(it)
            }
        }
    }

    // EditText

    @BindingAdapter(value = ["isPhoneUS"])
    @JvmStatic
    fun setTypePhoneUS(view: AppCompatEditText, isPhoneUS: Boolean) {
        if (isPhoneUS) {
            view.inputTypePhoneUS()
            view.inputType = android.text.InputType.TYPE_CLASS_PHONE
        }
    }

    @BindingAdapter(value = ["onSearchChange"])
    @JvmStatic
    fun setOnSearchChange(view: AppCompatEditText, onSearch: (text: String) -> Unit) {
        var timer: Timer? = null
        val handler = Handler(Looper.getMainLooper())
        view.doOnTextChanged { text, start, before, count ->
            timer?.cancel()
            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    handler.post {
                        onSearch(text.toString())
                    }
                }
            }, AppConfig.timeSearch)

        }
    }

    // View
    @BindingAdapter(value = ["visibility"])
    @JvmStatic
    fun setVisibility(view: View, isVisible: Boolean) {
        view.show(isVisible)
    }

    // Textview
    @BindingAdapter(value = ["drawableTint"])
    @JvmStatic
    fun setDrawableTint(view: AppCompatTextView, color: Int) {
        if (color == 0) return

        view.compoundDrawablesRelative.forEach {
            if (it != null)
                it.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(view.context, color),
                    PorterDuff.Mode.SRC_IN
                )
        }
    }

    // Material Button
    @BindingAdapter(value = ["android:drawableTint"])
    @JvmStatic
    fun setDrawableTint(view: MaterialButton, color: Int) {
        if (color == 0) return

        view.compoundDrawablesRelative.forEach {
            if (it != null)
                it.colorFilter = PorterDuffColorFilter(
                    color,
                    PorterDuff.Mode.SRC_IN
                )
        }
    }
}

