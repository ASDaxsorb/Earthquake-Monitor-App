package com.axellsolis.earthquakemonitor.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.axellsolis.earthquakemonitor.R
import com.axellsolis.earthquakemonitor.databinding.FragmentEarthquakeMapBinding
import com.axellsolis.earthquakemonitor.viewmodel.EarthquakeViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class EarthquakeMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentEarthquakeMapBinding
    private lateinit var mMap: GoogleMap
    private val viewModel by activityViewModels<EarthquakeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEarthquakeMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
}