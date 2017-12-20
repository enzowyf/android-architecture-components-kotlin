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

package com.ezstudio.basicsample.db


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.ezstudio.basicsample.db.converter.DateConverter
import com.ezstudio.basicsample.db.dao.CommentDao
import com.ezstudio.basicsample.db.dao.ProductDao
import com.ezstudio.basicsample.db.entity.Comment
import com.ezstudio.basicsample.db.entity.Product
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay

@Database(entities = [Product::class, Comment::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

  private val mIsDatabaseCreated = MutableLiveData<Boolean>()

  fun getDatabaseCreated(): LiveData<Boolean> = mIsDatabaseCreated

  abstract fun productDao(): ProductDao

  abstract fun commentDao(): CommentDao

  /**
   * Check whether the database already exists and expose it via [.getDatabaseCreated]
   */
  private fun updateDatabaseCreated(context: Context) {
    if (context.getDatabasePath(DATABASE_NAME).exists()) {
      setDatabaseCreated()
    }
  }

  private fun setDatabaseCreated() {
    mIsDatabaseCreated.postValue(true)
  }

  companion object {

    @Volatile
    private var sInstance: AppDatabase? = null

    val DATABASE_NAME = "basic-sample-db"

    fun getInstance(context: Context): AppDatabase =
        sInstance ?: Unit.let {
          synchronized(AppDatabase::class.java) {
            sInstance ?: buildDatabase(context.applicationContext).apply {
              updateDatabaseCreated(context.applicationContext)
            }
          }
        }

    /**
     * Build the database. [Builder.build] only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private fun buildDatabase(appContext: Context): AppDatabase {
      return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
          .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
              super.onCreate(db)
              async {
                // Add a delay to simulate a long-running operation
                delay(4000)
                // Generate the data for pre-population
                val products = DataGenerator.generateProducts()
                val comments = DataGenerator.generateCommentsForProducts(products)

                AppDatabase.getInstance(appContext).let {
                  it.runInTransaction {
                    it.productDao().insertAll(products)
                    it.commentDao().insertAll(comments)
                  }
                  // notify that the database was created and it's ready to be used
                  it.setDatabaseCreated()
                }
              }
            }
          }).build()
    }
  }
}
