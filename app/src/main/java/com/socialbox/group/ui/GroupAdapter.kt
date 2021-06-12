package com.socialbox.group.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.socialbox.R
import com.socialbox.group.data.dto.GroupDTO
import com.socialbox.group.ui.GroupAdapter.SongSearchViewHolder
import com.socialbox.login.data.model.User
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.util.ArrayList
import java.util.Locale

class GroupAdapter(
  val context: Context,
  val user: User?
) :
  RecyclerView.Adapter<SongSearchViewHolder>(), Filterable {

  var groupList = mutableListOf<GroupDTO>()
  var groupListFilter = mutableListOf<GroupDTO>()

  class SongSearchViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
    val image: ImageView = cardView.findViewById(R.id.shapeableImageView)
    val groupName: TextView = cardView.findViewById(R.id.group_name)
    val memberCount: TextView = cardView.findViewById(R.id.members)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): SongSearchViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater
      .inflate(R.layout.group_card_layout, parent, false)

    return SongSearchViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: SongSearchViewHolder,
    position: Int
  ) {
    val group = groupList[position]

    holder.itemView.setOnClickListener {
      Timber.i("Opening groups ${group.groupName}")
      val intent = Intent(context, GroupDetailsActivity::class.java)
      intent.putExtra("groupId", group.groupId)
      context.startActivity(intent)
    }

    holder.groupName.text = group.groupName
    holder.memberCount.text = String.format(holder.memberCount.text.toString(), group.memberCount)

    // Todo: Set image later
    // Picasso.get().load(group.groupPhotoURL).into(holder.image)
  }

  override fun getItemCount(): Int = this.groupList.size

  override fun getFilter(): Filter {
    return object : Filter() {
      override fun performFiltering(charSequence: CharSequence): FilterResults? {
        val charString = charSequence.toString()
        if (charString.isEmpty()) {
          groupListFilter = groupList
        } else {
          val filteredList: MutableList<GroupDTO> = mutableListOf()
          for (group in groupList) {
            if (group.groupName.lowercase(Locale.getDefault())
                .contains(charString.lowercase(Locale.getDefault()))
            ) {
              filteredList.add(group)
            }
          }
          groupListFilter = filteredList
        }
        val filterResults = FilterResults()
        filterResults.values = groupListFilter
        return filterResults
      }

      override fun publishResults(
        charSequence: CharSequence?,
        filterResults: FilterResults
      ) {
        groupListFilter = filterResults.values as MutableList<GroupDTO>
        notifyDataSetChanged()
      }
    }
  }
}