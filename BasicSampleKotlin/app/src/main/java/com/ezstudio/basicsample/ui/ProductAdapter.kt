package com.ezstudio.basicsample.ui

import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ezstudio.basicsample.R
import com.ezstudio.basicsample.db.entity.Product
import com.ezstudio.basicsample.utils.autoNotify
import kotlinx.android.synthetic.main.product_item.view.*
import kotlin.properties.Delegates

/**
 * Created by enzowei on 2017/12/1.
 */
class ProductAdapter(private val fragmentManager: FragmentManager) : RecyclerView.Adapter<CommonViewHolder>() {
  var productList: List<Product> by Delegates.observable(emptyList()) { prop, old, new ->
    autoNotify(old, new) { o, n -> o.id == n.id }
  }

  override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
    holder.bind(productList[position]) { product ->
      name_tv.text = product.name
      price_tv.text = "Price: $${product.price}"
      description_tv.text = product.description
      setOnClickListener {
        fragmentManager.show(product)
      }
    }
  }

  override fun getItemCount(): Int = productList.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder =
      CommonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false))

  /** Shows the product detail fragment  */
  private fun FragmentManager.show(product: Product) {
    val productFragment = ProductFragment.forProduct(product.id)
    this.beginTransaction()
        .addToBackStack("product")
        .replace(R.id.fragment_container,
            productFragment, null).commit()
  }
}


