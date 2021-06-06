package com.socialbox.group.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.socialbox.R
import com.socialbox.group.data.dto.GroupDTO
import com.socialbox.login.data.model.User
import com.socialbox.group.ui.GroupAdapter.SongSearchViewHolder
import timber.log.Timber

class GroupAdapter(
  val context: Context,
  val user: User?
) :
  RecyclerView.Adapter<SongSearchViewHolder>() {

  var songList = mutableListOf<GroupDTO>()

  class SongSearchViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
    val image: ImageView = cardView.findViewById(R.id.small_song_image)
    val groupName: TextView = cardView.findViewById(R.id.search_song_name)
    val memberCount: TextView = cardView.findViewById(R.id.search_album_name)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): SongSearchViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater
      .inflate(R.layout.search_song_list_view, parent, false)

    return SongSearchViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: SongSearchViewHolder,
    position: Int
  ) {
    val song = songList[position]

    holder.itemView.setOnClickListener {
      Timber.i("Rating song ${song.name} and opening up the rating dialog")
      val rateDialog =
        RatingsDialog(
          context = this.context, song = song, associatedFunction = update, user = user!!,
          removeRatingsButton = null
        )
      rateDialog.show()
    }

    holder.groupName.text = song.name
    holder.memberCount.text = song.album

    val smallestResolutionImage = song.image.minByOrNull { (_, height, width) -> height / width }!!

    Picasso.get().load(smallestResolutionImage.url).into(holder.image)
  }

  override fun getItemCount(): Int = this.songList.size
}