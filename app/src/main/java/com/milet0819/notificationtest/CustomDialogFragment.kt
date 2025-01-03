package com.milet0819.notificationtest

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.milet0819.notificationtest.databinding.DialogFragmentCustomBinding

class CustomDialogFragment: DialogFragment() {

    companion object {
        val TAG = CustomDialogFragment::class.java.simpleName
    }

    private var _binding: DialogFragmentCustomBinding? = null
    private val binding get() = _binding!!

    private var initFlag = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        _binding = DialogFragmentCustomBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "onCreateDialog")
        val dialog = super.onCreateDialog(savedInstanceState)

        return dialog
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")

        if (initFlag) {
            val displayMetrics = Resources.getSystem().displayMetrics
            val width = (displayMetrics.widthPixels * 0.9).toInt()
            val height = (displayMetrics.heightPixels * 0.5).toInt()
            dialog?.window?.setLayout(width, height)
            initFlag = false
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}