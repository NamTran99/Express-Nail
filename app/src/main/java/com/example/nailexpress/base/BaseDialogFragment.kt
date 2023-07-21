package com.example.nailexpress.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.nailexpress.R
import com.example.nailexpress.helper.network.ExceptionType
import kotlinx.coroutines.launch

abstract class BaseDialogFragment<T : ViewDataBinding> : DialogFragment(){
    val TAG by lazy { this::class.java.name }

    lateinit var binding: T
        private set

    @get:LayoutRes
    abstract val layoutId: Int

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

    fun setDialogOnTop(){
        dialog?.window?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP)
        val param  = dialog?.window?.attributes
        param?.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = param
    }


    override fun onStart() {
        super.onStart()
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