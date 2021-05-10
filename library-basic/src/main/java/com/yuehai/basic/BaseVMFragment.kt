package com.yuehai.basic

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.yuehai.util.DialogUtil

/**
 * Created by zhaoyuehai 2021/4/25
 */
abstract class BaseVMFragment<DB : ViewDataBinding, VM : BaseViewModel>(
    override val layout: Int,
    private val viewModelClass: Class<VM>,
    @IdRes protected val variableId: Int
) : BaseFragment(layout) {

    protected lateinit var viewModel: VM
    protected var viewDataBinding: DB? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(viewModelClass)
        viewDataBinding = DataBindingUtil.inflate(inflater, layout, container, false)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        return viewDataBinding?.root ?: inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.setVariable(variableId, viewModel)
        activity?.let { addObserver(it) }
        viewModel.init(savedInstanceState, arguments)
    }

    open fun addObserver(activity: Activity) {
        viewModel.finish.observe(this, {
            if (it != null) {
                if (it) activity.setResult(Activity.RESULT_OK)
                activity.finish()
            }
        })
        viewModel.toast.observe(this, {
            if (it != null && it.isNotEmpty()) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.bottomDialog.observe(this, {
            if (it != null) {
                DialogUtil.showBottomDialog(
                    activity, it.first
                ) { _, which ->
                    it.second.invoke(which == DialogInterface.BUTTON_POSITIVE)
                }
            }
        })
        if (activity is BaseActivity) {
            viewModel.showLoading.observe(this, {
                if (it == null) {
                    activity.dismissLoading()
                } else {
                    activity.showLoading(it)
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding?.unbind()
    }
}