package com.meetio.jobcandidate.quizapp

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction

fun Activity.hideSystemUi() {
    window.setDecorFitsSystemWindows(false)
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LOW_PROFILE
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
}

fun FragmentActivity.addFragment(fragment:Fragment, id: Int) {
    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    transaction.add(id, fragment)
    transaction.commit()
}

fun FragmentActivity.replaceFragment(fragment:Fragment, id: Int) {
    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    transaction.replace(id, fragment)
    transaction.commit()
}