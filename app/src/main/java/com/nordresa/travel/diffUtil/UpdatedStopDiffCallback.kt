package com.nordresa.travel.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.nordresa.travel.models.StopsData


class UpdatedStopDiffCallback : DiffUtil.ItemCallback<StopsData>(){
    override fun areItemsTheSame(oldItem: StopsData, newItem: StopsData): Boolean {
        return oldItem.extId == newItem.extId
    }

    override fun areContentsTheSame(oldItem: StopsData, newItem: StopsData): Boolean {
        return oldItem == newItem
    }
}