package com.axellsolis.earthquakemonitor.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.axellsolis.earthquakemonitor.R
import com.axellsolis.earthquakemonitor.data.model.Earthquake
import com.axellsolis.earthquakemonitor.databinding.FragmentSavedEarthquakesBinding
import com.axellsolis.earthquakemonitor.ui.adapter.EarthquakeAdapter
import com.axellsolis.earthquakemonitor.utils.ItemClickListener
import com.axellsolis.earthquakemonitor.utils.hide
import com.axellsolis.earthquakemonitor.utils.show
import com.axellsolis.earthquakemonitor.viewmodel.EarthquakeViewModel
import kotlinx.coroutines.flow.collect

class SavedEarthquakesFragment : Fragment(), ItemClickListener {

    companion object {
        const val IS_SAVED_KEY = "isSaved"
    }

    private lateinit var binding: FragmentSavedEarthquakesBinding
    private lateinit var mAdapter: EarthquakeAdapter
    private val viewModel by activityViewModels<EarthquakeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedEarthquakesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getEntities()
        setRecyclerView()
        getSavedEarthquakes()
    }

    private fun setRecyclerView() {
        mAdapter = EarthquakeAdapter(this)
        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getSavedEarthquakes() {
        lifecycleScope.launchWhenStarted {
            viewModel.savedEarthquakes.collect { earthquakes ->
                if (earthquakes.isNotEmpty()) {
                    binding.noEarthquakesError.hide()
                } else {
                    binding.noEarthquakesError.show()
                }
                mAdapter.submitList(earthquakes)
            }
        }
    }

    override fun onClick(earthquake: Earthquake) {
        viewModel.selectItem(earthquake)
        val action = R.id.action_savedEarthquakesFragment_to_earthquakeDetailFragment
        findNavController().navigate(action, bundleOf(IS_SAVED_KEY to true))
    }

    override fun onLongClick(earthquake: Earthquake) {
        viewModel.deleteEarthquake(earthquake)
    }
}