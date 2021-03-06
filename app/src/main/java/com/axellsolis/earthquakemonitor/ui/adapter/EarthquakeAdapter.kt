package com.axellsolis.earthquakemonitor.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.axellsolis.earthquakemonitor.R
import com.axellsolis.earthquakemonitor.data.model.Earthquake
import com.axellsolis.earthquakemonitor.databinding.ItemEarthquakeBinding
import com.axellsolis.earthquakemonitor.utils.getScaleColor
import com.axellsolis.earthquakemonitor.utils.longToDate

class EarthquakeAdapter(val onClickItem: (Earthquake) -> Unit) :
    ListAdapter<Earthquake, EarthquakeAdapter.EarthquakeViewHolder>(DiffCallback) {

    inner class EarthquakeViewHolder(
        private val binding: ItemEarthquakeBinding,
        private val onClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(adapterPosition)
            }
        }

        fun bind(earthquake: Earthquake) {
            val time = longToDate(earthquake.properties.time)
            val depth = earthquake.geometry.coordinates[earthquake.geometry.coordinates.size - 1]
            binding.apply {
                tvMagnitude.text = String.format("%.2f", earthquake.properties.magnitude)
                tvPlace.text = earthquake.properties.place
                tvTime.text = time
                tvDepth.text = itemView.context.getString(R.string.depth, depth)
                scaleColor.setBackgroundColor(
                    getScaleColor(
                        binding.root.context,
                        earthquake.properties.magnitude
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EarthquakeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEarthquakeBinding.inflate(inflater, parent, false)
        return EarthquakeViewHolder(binding) { position ->
            onClickItem(getItem(position))
        }
    }

    override fun onBindViewHolder(holder: EarthquakeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Earthquake>() {
        override fun areItemsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

