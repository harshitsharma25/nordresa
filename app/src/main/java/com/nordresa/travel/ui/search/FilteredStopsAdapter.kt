package com.nordresa.travel.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.nordresa.travel.R
import com.nordresa.travel.diffUtil.UpdatedStopDiffCallback
import com.nordresa.travel.models.StopsData

class FilteredStopsAdapter(
    private val context : Context,
    private val onItemClicked : (StopsData) -> Unit,
//    private val onItemAddToCart : (StopsData) -> Unit
) : ListAdapter<StopsData, FilteredStopsAdapter.UpdatedStopViewHolder>(UpdatedStopDiffCallback()) {

    class UpdatedStopViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdatedStopViewHolder {
        return UpdatedStopViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_result,parent,false))
    }

    override fun onBindViewHolder(holder: UpdatedStopViewHolder, position: Int) {

        if (holder is UpdatedStopViewHolder) {
            val item = getItem(position)

            println("--> In adapter, data is : $item")
            holder.itemView.findViewById<TextView>(R.id.tvStationName).text = item.name

            val iconContainer = holder.itemView.findViewById<LinearLayout>(R.id.llTransportIcons)
            iconContainer.removeAllViews()

            item.transportTypes.forEach { cls ->
                val iconRes = when (cls) {
                    "2" -> R.drawable.ic_train
                    "4" -> R.drawable.ic_bus
                    "8" -> R.drawable.ic_ferry
                    "16" -> R.drawable.ic_metro
                    "32" -> R.drawable.ic_tram
                    "64" -> R.drawable.ic_flight
                    "128" -> R.drawable.ic_walk
                    else -> R.drawable.ic_unknown
                }

                val iconView = ImageView(holder.itemView.context).apply {
                    setImageResource(iconRes)
                    layoutParams = LinearLayout.LayoutParams(58, 58).apply {
                        marginEnd = 12
                    }
                    //setColorFilter(ContextCompat.getColor(context, R.color.secondary_text_color))
                }
                iconContainer.addView(iconView)
            }

            holder.itemView.findViewById<MaterialCardView>(R.id.mcvItemSearched).setOnClickListener {
                onItemClicked(item)
            }

        }
    }

}