package com.axellsolis.earthquakemonitor.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.axellsolis.earthquakemonitor.R
import com.axellsolis.earthquakemonitor.data.model.Earthquake
import com.axellsolis.earthquakemonitor.databinding.FragmentEarthquakeMapBinding
import com.axellsolis.earthquakemonitor.viewmodel.GeneralMapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class GeneralMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentEarthquakeMapBinding
    private lateinit var mMap: GoogleMap
    private val viewModel by viewModels<GeneralMapViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEarthquakeMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getAsyncMap()
        setCollectors()
    }

    private fun setCollectors() {
        lifecycleScope.launchWhenStarted {
            viewModel.earthquakes.collect { earthquakes ->
                setLocations(earthquakes)
            }
        }
    }

    private fun setLocations(earthquakes: List<Earthquake>) {
        if (earthquakes.isEmpty()) {
            return
        }
        earthquakes.forEachIndexed { index ,earthquake ->
            val coordinates = earthquake.geometry.coordinates
            val lat = coordinates[1]
            val lng = coordinates[0]

            val point = LatLng(lat, lng)

            val newCircle = drawCircle(point)
            mMap.addCircle(newCircle).tag = earthquake.properties.place

            if (index == earthquakes.size-1 ) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 7.0f))
            }
        }
    }

    private fun drawCircle(point: LatLng): CircleOptions {
        return CircleOptions()
            .center(point)
            .radius(50000.0)
            .fillColor(Color.YELLOW)
            .strokeColor(Color.RED)
            .strokeWidth(5.0f)
    }

    private fun getAsyncMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isRotateGesturesEnabled = false
        viewModel.getLastHourEarthquakes()
    }
}