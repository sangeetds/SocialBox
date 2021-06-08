package com.socialbox.group.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.socialbox.group.data.model.Movie

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

  val cardList = mutableListOf<Movie>()

  class ViewHolder(cardView: View): RecyclerView.ViewHolder(cardView) {

  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    TODO("Not yet implemented")
  }

  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    TODO("Not yet implemented")
  }

  override fun getItemCount(): Int {
    TODO("Not yet implemented")
  }
}
