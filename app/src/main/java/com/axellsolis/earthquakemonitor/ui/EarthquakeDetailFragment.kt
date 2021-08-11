package com.axellsolis.earthquakemonitor.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.axellsolis.earthquakemonitor.R
import com.axellsolis.earthquakemonitor.data.model.Earthquake
import com.axellsolis.earthquakemonitor.databinding.FragmentEarthquakeDetailBinding
import com.axellsolis.earthquakemonitor.ui.SavedEarthquakesFragment.Companion.IS_SAVED_KEY
import com.axellsolis.earthquakemonitor.utils.longToDate
import com.axellsolis.earthquakemonitor.utils.longToTime
import com.axellsolis.earthquakemonitor.viewmodel.EarthquakeViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlin.math.abs

class EarthquakeDetailFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentEarthquakeDetailBinding
    private lateinit var mMap: GoogleMap
    private var earthquakeData: Earthquake? = null
    private var mCoordinates: LatLng? = null
    private val earthquakeViewModel by activityViewModels<EarthquakeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEarthquakeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setMapFragment()
        setCollectors()

        arguments?.let { bundle ->
            val isSaved = bundle.getBoolean(IS_SAVED_KEY)
            setHasOptionsMenu(!isSaved)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.savedEarthquakes -> {
                onSaveEarthquake()
                true
            }
            else -> false
        }
    }

    private fun setMapFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setCollectors() {
        lifecycleScope.launchWhenStarted {
            earthquakeViewModel.selectedItem.collect { earthquake ->
                if (earthquake != null) {
                    earthquakeData = earthquake
                    initUi()
                }
            }
        }
    }

    private fun initUi() {
        earthquakeData?.let { earthquake ->
            val time = earthquake.properties.time
            val depth = earthquake.geometry.coordinates[earthquake.geometry.coordinates.size - 1]
            val lat = abs(earthquake.geometry.coordinates[1])
            val lng = abs(earthquake.geometry.coordinates[0])

            binding.apply {
                tvTitle.text = earthquake.properties.place
                tvMagnitude.text =
                    getString(R.string.template_magnitude, earthquake.properties.magnitude)
                tvDepth.text = getString(R.string.template_depth, depth)
                tvDate.text = longToDate(time)
                tvTime.text = longToTime(time)
                tvLocation.text = getString(R.string.template_location, lng, lat)
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.setOnMapClickListener { }
        setMarker()
    }

    private fun setMarker() {
        earthquakeData?.let {
            mCoordinates = LatLng(
                it.geometry.coordinates[1],
                it.geometry.coordinates[0]
            )
            mCoordinates?.let { coordinates ->
                mMap.addMarker(
                    MarkerOptions()
                        .title(getString(R.string.marker_epicenter))
                        .position(coordinates)
                )?.showInfoWindow()
                mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates))
            }

        }
    }

    private fun onSaveEarthquake() {
        earthquakeViewModel.saveEarthquake()
        Snackbar.make(requireView(), "Store saved", Snackbar.LENGTH_SHORT).show()
    }
}