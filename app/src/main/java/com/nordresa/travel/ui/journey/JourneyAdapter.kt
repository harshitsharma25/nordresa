package com.nordresa.travel.ui.journey

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nordresa.travel.R
import com.nordresa.travel.diffUtil.UpdatedJourneyDiffCallback
import com.nordresa.travel.models.JourneyData

class JourneyAdapter(
    private val context : Context,
//    private val onItemClicked : (StopsData) -> Unit,
//    private val onItemAddToCart : (StopsData) -> Unit
) : ListAdapter<JourneyData, JourneyAdapter.UpdatedJourneyViewholder>(UpdatedJourneyDiffCallback()) {

    class UpdatedJourneyViewholder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdatedJourneyViewholder {
        return UpdatedJourneyViewholder(LayoutInflater.from(context).inflate(R.layout.item_trip,parent,false))
    }

    override fun onBindViewHolder(holder: UpdatedJourneyViewholder, position: Int) {

        if (holder is UpdatedJourneyViewholder) {
            val item = getItem(position)

            println("--> In adapter, data is : $item")
            holder.itemView.findViewById<TextView>(R.id.tvDepartureTime).text = item.departTime
            holder.itemView.findViewById<TextView>(R.id.tvArrivalTime).text = item.destTime

            val duration = parseIsoDuration(item.duration)
            holder.itemView.findViewById<TextView>(R.id.tvDuration).text = duration

        }
    }


    fun parseIsoDuration(isoDuration: String): String {
        val regex = Regex("""PT(?:(\d+)H)?(?:(\d+)M)?""")
        val match = regex.matchEntire(isoDuration)

        val (hoursStr, minutesStr) = match?.destructured ?: return "Invalid format"

        val hours = hoursStr.toIntOrNull() ?: 0
        val minutes = minutesStr.toIntOrNull() ?: 0

        return buildString {
            if (hours > 0) append("$hours hr ")
            if (minutes > 0) append("$minutes min")
        }.trim()
    }


}