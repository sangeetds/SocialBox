package com.socialbox.movie.ui

import android.content.Context
import android.util.SparseBooleanArray
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.core.util.forEach
import androidx.core.util.size
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R.id
import com.socialbox.R.layout
import com.socialbox.R.string
import com.socialbox.group.data.model.Movie
import com.socialbox.movie.ui.FlipAnimator.flipView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import timber.log.Timber

class MovieListAdapter(
  val context: Context,
  val updateCount: ((Int) -> Unit)?,
  private val url: String,
  private var reverseAllAnimations: Boolean = false,
  private var currentSelectedIndex: Int = -1,
) :
  RecyclerView.Adapter<MovieListAdapter.MovieHolder>() {

  var movies = listOf<Movie>()

  val selectedItemsList: MutableSet<Movie> = mutableSetOf()
  val selectedItems: SparseBooleanArray = SparseBooleanArray()
  private val animationItemsIndex: SparseBooleanArray = SparseBooleanArray()

  inner class MovieHolder(cardView: View) : RecyclerView.ViewHolder(cardView), OnLongClickListener {
    val card: MaterialCardView = cardView.findViewById(id.searchMovieCard)
    val imageCard: MaterialCardView = cardView.findViewById(id.selectedImageView)
    val image: CircleImageView = cardView.findViewById(id.userMovieImage)
    val movieName: TextView = cardView.findViewById(id.movieName)
    val imageBack: CircleImageView = cardView.findViewById(id.cardSelected)

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
      .inflate(layout.search_movie_card_layout, parent, false)

    return MovieHolder(itemView)
  }

  override fun onBindViewHolder(
    holder: MovieHolder,
    position: Int,
  ) {
    val userMovie = movies[position]
    holder.movieName.text = userMovie.name
    holder.itemView.isActivated = selectedItems.get(position, false)
    Picasso.get().load("${url}${userMovie.photoURL}").resize(50,50)
      .into(holder.image)
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
      holder.imageCard.visibility = View.VISIBLE
      holder.imageBack.visibility = View.VISIBLE
      holder.imageBack.imageAlpha = 1
      if (currentSelectedIndex == position) {
        flipView(context, holder.imageBack, holder.image, true)
        resetCurrentIndex()
      }
      holder.card.setCardBackgroundColor(android.graphics.Color.parseColor("#B3E5FC"))
    } else {
      holder.imageCard.visibility = View.GONE
      holder.imageBack.visibility = View.GONE
      resetIconYAxis(holder.image)
      holder.image.visibility = View.VISIBLE
      holder.image.imageAlpha = 1
      if (reverseAllAnimations && animationItemsIndex.get(
          position,
          false
        ) || currentSelectedIndex == position
      ) {
        flipView(context, holder.imageBack, holder.image, false)
        resetCurrentIndex()
      }
      holder.card.setCardBackgroundColor((android.graphics.Color.parseColor("#FFFFFF")))
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
      selectedItemsList.remove(movies[pos])
      animationItemsIndex.delete(pos)
      updateCount?.invoke(selectedItemsList.size)
    } else {
      selectedItems.put(pos, true)
      selectedItemsList.add(movies[pos])
      animationItemsIndex.put(pos, true)
      updateCount?.invoke(selectedItemsList.size)
    }
    notifyItemChanged(pos)
  }

  fun clearSelection() {
    val itemsToChangeView = mutableListOf<Int>()
    selectedItems.forEach { key, _ -> itemsToChangeView.add(key) }
    selectedItems.clear()
    selectedItemsList.clear()
    animationItemsIndex.clear()
    itemsToChangeView.forEach { k -> notifyItemChanged(k) }
  }

  override fun getItemCount() = movies.size
}
