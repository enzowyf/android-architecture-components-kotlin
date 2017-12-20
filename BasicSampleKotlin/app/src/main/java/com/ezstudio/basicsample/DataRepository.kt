package com.ezstudio.basicsample

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer


import com.ezstudio.basicsample.db.AppDatabase
import com.ezstudio.basicsample.db.entity.Comment
import com.ezstudio.basicsample.db.entity.Product

/**
 * Repository handling the work with products and comments.
 */
class DataRepository private constructor(private val mDatabase: AppDatabase) {
  private val mObservableProducts: MediatorLiveData<List<Product>> = MediatorLiveData()

  init {
    mObservableProducts.addSource(mDatabase.productDao().loadAllProducts()) { productEntities ->
      if (mDatabase.getDatabaseCreated().value != null) {
        mObservableProducts.postValue(productEntities)
      }
    }
  }

  /**
   * Get the list of products from the database and get notified when the data changes.
   */
  fun getProducts(): LiveData<List<Product>> = mObservableProducts

  fun loadProduct(productId: Int): LiveData<Product> = mDatabase.productDao().loadProduct(productId)

  fun loadComments(productId: Int): LiveData<List<Comment>> = mDatabase.commentDao().loadComments(productId)

  companion object {

    private val sInstance: DataRepository? = null

    fun getInstance(database: AppDatabase): DataRepository =
        sInstance ?: Unit.let {
          synchronized(DataRepository::class.java) {
            sInstance ?: DataRepository(database)
          }
        }
  }
}
