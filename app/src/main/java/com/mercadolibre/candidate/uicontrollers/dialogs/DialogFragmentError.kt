package com.mercadolibre.candidate.uicontrollers.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.interfaces.OnDialogClickListener
import kotlinx.android.synthetic.main.dialog_error.*


class DialogFragmentError() : AppCompatDialogFragment() {

    var onDialogClickListener: OnDialogClickListener? = null

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // retain this fragment when activity is re-initialized
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_error, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog_error_button_cancel.setOnClickListener {
            onDialogClickListener?.onCancel()
            dismiss()
        }

        dialog_error_button_retry.setOnClickListener {
            onDialogClickListener?.onRetry()
            dismiss()
        }
    }
}