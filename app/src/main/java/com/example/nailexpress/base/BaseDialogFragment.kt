package com.example.nailexpress.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.example.nailexpress.datasource.AppEvent2
import com.example.nailexpress.extension.onClick
import javax.inject.Inject

abstract class BaseDialogFragment<T : ViewDataBinding> : DialogFragment() {
    open val gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP

    val TAG by lazy { this::class.java.name }

    lateinit var binding: T
        private set

    @get:LayoutRes
    abstract val layoutId: Int

    @Inject
    lateinit var appEvent: AppEvent2

    override fun onAttach(ctx: Context) {
        super.onAttach(ctx)
        Log.d(TAG, "onAttach: ")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ${this.javaClass.simpleName}")
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        Log.d(TAG, "onViewCreated: ")
    }

    abstract fun initView()

    fun hideKeyboard() {
        val view = requireActivity().currentFocus
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun showToast(res: Int) {
        Toast.makeText(requireContext(), res, Toast.LENGTH_SHORT).show()
    }

    fun setDialogOnTop() {
        dialog?.window?.setGravity(gravity)
        val param = dialog?.window?.attributes
        param?.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = param
    }

    override fun onStart() {
        super.onStart()
        setDialogOnTop()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }


    override fun onDetach() {
        Log.d(TAG, "onDetach: ")
        super.onDetach()
    }
}