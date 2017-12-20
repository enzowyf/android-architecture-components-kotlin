package com.ezstudio.basicsample.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ezstudio.basicsample.R
import com.ezstudio.basicsample.db.entity.Comment
import com.ezstudio.basicsample.utils.autoNotify
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlin.properties.Delegates

/**
 * Created by enzowei on 2017/12/1.
 */
class CommentAdapter : RecyclerView.Adapter<CommonViewHolder>() {
  var commentList: List<Comment> by Delegates.observable(emptyList()) { prop, old, new ->
    autoNotify(old, new) { o, n -> o.id == n.id }
  }

  override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
    holder.bind(commentList[position]) { comment ->
      comment_tv.text = comment.text
    }
  }

  override fun getItemCount(): Int = commentList.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder =
      CommonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false))
}



