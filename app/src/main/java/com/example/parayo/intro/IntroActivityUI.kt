package com.example.parayo.intro

import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.view.Gravity
import android.view.View
import com.example.parayo.R
import org.jetbrains.anko.*

class IntroActivityUI() : AnkoComponent<IntroActivity>, Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IntroActivityUI> {
        override fun createFromParcel(parcel: Parcel): IntroActivityUI {
            return IntroActivityUI(parcel)
        }

        override fun newArray(size: Int): Array<IntroActivityUI?> {
            return arrayOfNulls(size)
        }
    }

    override fun createView(ui: AnkoContext<IntroActivity>): View {
        return ui.relativeLayout {
            gravity = Gravity.CENTER

            textView("PARAYO") {
                textSize = 24f
                textColorResource = R.color.colorPrimary
                typeface = Typeface.DEFAULT_BOLD
            }
        }
    }
}