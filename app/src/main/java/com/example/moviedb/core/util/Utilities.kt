package com.example.moviedb.core.util

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

object Utilities {

    @Suppress("DEPRECATION")
    fun checkForInternet(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    fun displayMetrics(fragment: Fragment, percentageView: Double): Int {
        val displayMetric: DisplayMetrics = fragment.requireActivity().resources.displayMetrics
        val height = displayMetric.heightPixels
        return (height * percentageView).toInt()
    }

    fun dialogBottom(dialog: Dialog, percentageView: Int): Dialog {
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { view ->
                val behavior = BottomSheetBehavior.from(view)
                setupFullHeight(view)
                behavior.isDraggable = true
                behavior.peekHeight = percentageView
            }
        }

        return dialog
    }

    private fun setupFullHeight(bottomShet: View) {
        val layoutParams = bottomShet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomShet.layoutParams = layoutParams
    }
}