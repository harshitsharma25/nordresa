package com.nordresa.travel.ui.journey

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nordresa.travel.databinding.ItemTripBinding
import com.nordresa.travel.diffUtil.TripDiffCallback

class TripsAdapter(
    private val onTripClick: (TripInfo) -> Unit
) : ListAdapter<TripInfo, TripsAdapter.TripViewHolder>(TripDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TripViewHolder(binding, onTripClick)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TripViewHolder(
        private val binding: ItemTripBinding,
        private val onTripClick: (TripInfo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(trip: TripInfo) {
            binding.apply {
                // Set basic trip info
                tvServiceName.text = trip.serviceName
                tvTripType.text = trip.tripType
                tvDepartureTime.text = trip.departureTime
                tvArrivalTime.text = trip.arrivalTime
                tvDuration.text = trip.duration

                // Set transport icon based on type
                tvTransportIcon.text = getTransportIcon(trip.transportType)

                // Set amenities
                tvAmenities.text = trip.amenities.joinToString(" | ")

                // Set price if available
                if (trip.price != null) {
                    tvPrice.text = trip.price
                    tvPrice.visibility = View.VISIBLE
                } else {
                    tvPrice.visibility = View.GONE
                }

                // Set click listener
                root.setOnClickListener {
                    onTripClick(trip)
                }

                // Set trip type color
                when (trip.tripType.lowercase()) {
                    "direct" -> {
                        tvTripType.setTextColor(root.context.getColor(R.color.gold_accent))
                    }
                    else -> {
                        tvTripType.setTextColor(root.context.getColor(R.color.secondary_text_color))
                    }
                }
            }
        }

        private fun getTransportIcon(transportType: String): String {
            return when (transportType.lowercase()) {
                "train", "snabbtåg", "intercity" -> "🚆"
                "bus", "express" -> "🚌"
                "metro", "tunnelbana" -> "🚇"
                "tram", "spårvagn" -> "🚋"
                "ferry", "färja" -> "⛴️"
                "flight", "flyg" -> "✈️"
                else -> "🚆" // Default to train
            }
        }
    }


}