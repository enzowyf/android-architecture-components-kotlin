package com.ezstudio.basicsample.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ezstudio.basicsample.R

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .add(R.id.fragment_container, ProductListFragment(), ProductListFragment.TAG).commit()
    }
  }
}
