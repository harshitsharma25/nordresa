package com.nordresa.travel.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.nordresa.travel.models.JourneyData

class UpdatedJourneyDiffCallback : DiffUtil.ItemCallback<JourneyData>(){
    override fun areItemsTheSame(oldItem: JourneyData, newItem: JourneyData): Boolean {
        return oldItem.tripId == newItem.tripId
    }

    override fun areContentsTheSame(oldItem: JourneyData, newItem: JourneyData): Boolean {
        return oldItem == newItem
    }
}