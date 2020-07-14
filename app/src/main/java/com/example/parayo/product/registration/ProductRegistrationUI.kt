package com.example.parayo.product.registration

import android.view.View
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.scrollView
import org.jetbrains.anko.verticalLayout

class ProductRegistrationUI(
    private val viewModel: ProductRegistrationViewModel
) : AnkoComponent<ProductRegistrationActivity>{

    override fun createView(ui: AnkoContext<ProductRegistrationActivity>)
            = ui.scrollView { // 등록 화면은 세로가 길어서 스크롤 뷰가 필요하다.
        verticalLayout {

        }
    }
}