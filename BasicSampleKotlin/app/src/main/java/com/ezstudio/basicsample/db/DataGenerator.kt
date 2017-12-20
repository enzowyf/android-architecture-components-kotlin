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


import com.ezstudio.basicsample.db.entity.Comment
import com.ezstudio.basicsample.db.entity.Product

import java.util.Date
import java.util.Random
import java.util.concurrent.TimeUnit

/**
 * Generates data to pre-populate the database
 */
object DataGenerator {

  private val FIRST = arrayOf("Special edition", "New", "Cheap", "Quality", "Used")
  private val SECOND = arrayOf("Three-headed Monkey", "Rubber Chicken", "Pint of Grog", "Monocle")
  private val DESCRIPTION = arrayOf("is finally here", "is recommended by Stan S. Stanman", "is the best sold product on Mêlée Island", "is \uD83D\uDCAF", "is ❤️", "is fine")
  private val COMMENTS = arrayOf("Comment 1", "Comment 2", "Comment 3", "Comment 4", "Comment 5", "Comment 6")

  fun generateProducts(): List<Product> {
    val products = mutableListOf<Product>()
    val rnd = Random()
    FIRST.indices.forEach { i ->
      SECOND.indices.mapTo(products) { j -> Product(FIRST.size * i + j + 1, "${FIRST[i]} ${SECOND[j]}", "${FIRST[i]} ${SECOND[j]} ${DESCRIPTION[j]}", rnd.nextInt(240)) }
    }
    return products
  }

  fun generateCommentsForProducts(
      products: List<Product>): List<Comment> {
    val comments = mutableListOf<Comment>()
    val rnd = Random()

    products.forEach { product ->
      val commentsNumber = rnd.nextInt(5) + 1
      (0 until commentsNumber).mapTo(comments) { Comment(product.id, "${COMMENTS[it]} for ${product.name}", Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis((commentsNumber - it).toLong()) + TimeUnit.HOURS.toMillis(it.toLong()))) }
    }

    return comments
  }
}
