package com.nordresa.travel.diffUtil

import androidx.recyclerview.widget.DiffUtil

class TripDiffCallback : DiffUtil.ItemCallback<TripInfo>() {
    override fun areItemsTheSame(oldItem: TripInfo, newItem: TripInfo): Boolean {
        return oldItem.serviceName == newItem.serviceName &&
                oldItem.departureTime == newItem.departureTime
    }

    override fun areContentsTheSame(oldItem: TripInfo, newItem: TripInfo): Boolean {
        return oldItem == newItem
    }
}