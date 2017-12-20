package com.ezstudio.basicsample.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ezstudio.basicsample.R
import com.ezstudio.basicsample.db.entity.Product
import com.ezstudio.basicsample.viewmodel.ProductListViewModel
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay


class ProductListFragment : Fragment() {

  private val mProductAdapter: ProductAdapter by lazy { ProductAdapter(fragmentManager) }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_product_list, container, false)

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    products_list.layoutManager = LinearLayoutManager(context)
    products_list.adapter = mProductAdapter
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val viewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
    async {
      delay(2000)
      subscribeUi(viewModel)
    }
  }

  private fun subscribeUi(viewModel: ProductListViewModel) {
    viewModel.products.observe(this, Observer<List<Product>> { newProducts ->
      if (newProducts != null) {
        mProductAdapter.productList = newProducts
        loading_tv.visibility = View.GONE
        products_list.visibility = View.VISIBLE
      } else {
        loading_tv.visibility = View.VISIBLE
        products_list.visibility = View.GONE
      }
    })
  }

  companion object {
    val TAG = "ProductListViewModel"
  }
}
