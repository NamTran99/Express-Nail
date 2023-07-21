package com.example.nailexpress.views.ui.main.staff.dialogs

import androidx.fragment.app.activityViewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseDialogFragment
import com.example.nailexpress.databinding.DialogSelectServiceBookStaffBinding
import com.example.nailexpress.extension.visible
import com.example.nailexpress.utils.ViewModelHandleUtils
import com.example.nailexpress.views.ui.main.staff.BookNowStaffVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookStaffServiceDialog : BaseDialogFragment<DialogSelectServiceBookStaffBinding>() {
    override val layoutId: Int
        get() = R.layout.dialog_select_service_book_staff

    val viewModel: BookNowStaffVM by activityViewModels()

    override fun initView() {
        setDialogOnTop()

        binding.apply {
            action = viewModel

            viewModel.dissmiss.observe(viewLifecycleOwner){
                dismissNow()
            }
            ViewModelHandleUtils.isLoading.observe(viewLifecycleOwner){
                progressBar.visible(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.filterTextSearch()
    }

}
