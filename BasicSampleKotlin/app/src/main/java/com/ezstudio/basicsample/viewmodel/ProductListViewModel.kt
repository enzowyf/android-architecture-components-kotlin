/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ezstudio.basicsample.viewmodel


import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.ezstudio.basicsample.BasicApp
import com.ezstudio.basicsample.db.entity.Product

class ProductListViewModel(application: Application) : AndroidViewModel(application) {

  // MediatorLiveData can observe other LiveData objects and react on their emissions.
  val products: MediatorLiveData<List<Product>> = MediatorLiveData()

  init {

    // set by default null, until we get data from the database.
    products.value = null

    val products = (application as BasicApp).getRepository()
        .getProducts()

    // observe the changes of the products from the database and forward them
    this@ProductListViewModel.products.addSource(products, this@ProductListViewModel.products::setValue)
  }
}
