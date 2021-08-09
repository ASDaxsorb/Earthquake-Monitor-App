package com.axellsolis.earthquakemonitor.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.axellsolis.earthquakemonitor.R
import com.axellsolis.earthquakemonitor.databinding.FragmentHomeBinding
import com.axellsolis.earthquakemonitor.ui.adapter.EarthquakeAdapter
import com.axellsolis.earthquakemonitor.utils.hide
import com.axellsolis.earthquakemonitor.utils.show
import com.axellsolis.earthquakemonitor.viewmodel.EarthquakeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAdapter: EarthquakeAdapter
    private val earthquakeViewModel by activityViewModels<EarthquakeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerView()
        setCollectors()
        initUi()
    }

    private fun setRecyclerView() {
        mAdapter = EarthquakeAdapter {
            earthquakeViewModel.selectItem(it)
            findNavController().navigate(R.id.action_homeFragment_to_earthquakeDetailFragment)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
            setHasFixedSize(true)
        }
    }

    private fun setCollectors() {
        lifecycleScope.launchWhenStarted {
            earthquakeViewModel.earthquakeList.collect { earthquakeList ->
                if (earthquakeList.isNotEmpty()) {
                    mAdapter.submitList(earthquakeList)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            earthquakeViewModel.progressVisible.collect { visible ->
                if (visible) {
                    binding.progressBar.show()
                } else {
                    binding.progressBar.hide()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            earthquakeViewModel.counter.collect {
                setCount(it)
            }
        }
    }

    private fun initUi() {
        binding.apply {
        }
    }

    private fun setCount(count: Int) {
        binding.tvCount.text = getString(R.string.template_earthquakes, count)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.cvEarthquakesInfo.hide()
        }

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.cvEarthquakesInfo.show()
        }

        super.onConfigurationChanged(newConfig)
    }
}