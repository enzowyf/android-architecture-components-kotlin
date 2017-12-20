package com.ezstudio.basicsample.ui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ezstudio.basicsample.R
import com.ezstudio.basicsample.db.entity.Comment
import com.ezstudio.basicsample.db.entity.Product
import com.ezstudio.basicsample.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.product_item.*
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay


/**
 * A simple [Fragment] subclass.
 */
class ProductFragment : Fragment() {

  private val mCommentAdapter: CommentAdapter by lazy { CommentAdapter() }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_product, container, false)

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    comment_list.layoutManager = LinearLayoutManager(context)
    comment_list.adapter = mCommentAdapter
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val factory = ProductViewModel.Factory(
        activity.application, arguments.getInt(KEY_PRODUCT_ID))
    val model = ViewModelProviders.of(this, factory)
        .get(ProductViewModel::class.java)

    async {
      delay(2000)
      subscribeToModel(model)
    }
  }

  private fun subscribeToModel(model: ProductViewModel) {
    // Observe product data
    model.observableProduct.observe(this, Observer<Product> { product ->
      product?.let {
        name_tv.text = it.name
        price_tv.text = "Price: $${it.price}"
        description_tv.text = it.description
      }
    })

    // Observe comments
    model.comments.observe(this, Observer<List<Comment>> { commentEntities ->
      if (commentEntities != null) {
        mCommentAdapter.commentList = commentEntities
        loading_comments_tv.visibility = View.GONE
      } else {
        loading_comments_tv.visibility = View.VISIBLE
      }
    })
  }

  companion object {
    val KEY_PRODUCT_ID = "product_id"

    /** Creates product fragment for specific product ID  */
    fun forProduct(productId: Int): ProductFragment =
        ProductFragment().apply {
          arguments = Bundle().also { it.putInt(KEY_PRODUCT_ID, productId) }
        }
  }
}
