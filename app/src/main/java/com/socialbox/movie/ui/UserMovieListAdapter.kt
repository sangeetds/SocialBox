package com.socialbox.movie.ui

import android.content.Context
import android.util.SparseBooleanArray
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.core.util.size
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R.*
import com.socialbox.movie.data.dto.UserMovieDTO
import com.socialbox.movie.ui.FlipAnimator.flipView

class UserMovieListAdapter(
  val context: Context,
  val updateCount: (Int) -> Unit,
  private var reverseAllAnimations: Boolean = false,
  private var currentSelectedIndex: Int = -1,
) :
  ListAdapter<UserMovieDTO, UserMovieListAdapter.MovieHolder>(UserMovieDiffCallback()) {

  val selectedItemsList: MutableSet<String> = mutableSetOf()
  val selectedItems: SparseBooleanArray = SparseBooleanArray()
  private val animationItemsIndex: SparseBooleanArray = SparseBooleanArray()

  inner class MovieHolder(cardView: View) : RecyclerView.ViewHolder(cardView), OnLongClickListener {

    val image: ShapeableImageView = cardView.findViewById(id.user_movie_image)
    val movieName: MaterialTextView = cardView.findViewById(id.user_movie_name)
    val imageBack: ShapeableImageView = cardView.findViewById(id.card_selected)

    override fun onLongClick(view: View): Boolean {
      onRowLongClicked(absoluteAdapterPosition)
      view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
      return true
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): MovieHolder {
    val itemView: View = LayoutInflater.from(parent.context)
      .inflate(layout.personal_movie_card_layout, parent, false)

    return MovieHolder(itemView)
  }

  override fun onBindViewHolder(
    holder: MovieHolder,
    position: Int,
  ) {
    val userMovie = getItem(position)
    holder.movieName.text = userMovie.name
    holder.itemView.isActivated = selectedItems.get(position, false)

    applyIconAnimation(holder, position)
    applyClickEvents(holder, position)
  }

  private fun applyClickEvents(
    holder: MovieHolder,
    position: Int,
  ) {
    holder.movieName.setOnLongClickListener { view ->
      onRowLongClicked(position)
      view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
      true
    }
  }

  private fun applyIconAnimation(
    holder: MovieHolder,
    position: Int,
  ) {
    if (selectedItems.get(position, false)) {
      holder.image.visibility = View.GONE
      resetIconYAxis(holder.imageBack)
      holder.imageBack.visibility = View.VISIBLE
      holder.imageBack.imageAlpha = 1
      if (currentSelectedIndex == position) {
        flipView(context, holder.imageBack, holder.image, true)
        resetCurrentIndex()
      }
    } else {
      holder.imageBack.visibility = View.GONE
      resetIconYAxis(holder.image)
      holder.image.visibility = View.VISIBLE
      holder.image.imageAlpha = 1
      if (reverseAllAnimations && animationItemsIndex.get(position,
          false) || currentSelectedIndex == position
      ) {
        flipView(context, holder.imageBack, holder.image, false)
        resetCurrentIndex()
      }
    }
  }

  private fun resetIconYAxis(view: View) {
    if (view.rotationY != 0f) {
      view.rotationY = 0f
    }
  }

  private fun onRowLongClicked(absoluteAdapterPosition: Int) {
    toggleSelection(absoluteAdapterPosition)
  }

  private fun resetCurrentIndex() {
    currentSelectedIndex = -1
  }

  private fun toggleSelection(pos: Int) {
    currentSelectedIndex = pos
    if (selectedItems[pos, false]) {
      selectedItems.delete(pos)
      selectedItemsList.remove(getItem(pos).id)
      animationItemsIndex.delete(pos)
      updateCount(selectedItems.size)
    } else {
      selectedItems.put(pos, true)
      selectedItemsList.add(getItem(pos).id)
      animationItemsIndex.put(pos, true)
      updateCount(selectedItems.size)
    }
    notifyItemChanged(pos)
  }
}

class UserMovieDiffCallback : DiffUtil.ItemCallback<UserMovieDTO>() {

  override fun areItemsTheSame(
    oldItem: UserMovieDTO,
    newItem: UserMovieDTO,
  ): Boolean = oldItem == newItem

  override fun areContentsTheSame(
    oldItem: UserMovieDTO,
    newItem: UserMovieDTO,
  ): Boolean = oldItem == newItem
}