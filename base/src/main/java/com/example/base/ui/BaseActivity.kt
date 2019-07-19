package com.example.base.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val layoutResource: Int

    protected abstract fun initComponent(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)
        initComponent(savedInstanceState)
    }
}
