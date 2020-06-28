package com.example.parayo.signin

import net.codephobia.ankomvvm.components.BaseActivity
import kotlin.reflect.KClass

class SigninActivity: BaseActivity<SigninViewModel>() {
    override val viewModelType = SigninViewModel::class
}