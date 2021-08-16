package com.axellsolis.earthquakemonitor.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.axellsolis.earthquakemonitor.R
import com.axellsolis.earthquakemonitor.data.model.Earthquake
import com.axellsolis.earthquakemonitor.databinding.FragmentHomeBinding
import com.axellsolis.earthquakemonitor.ui.adapter.EarthquakeAdapter
import com.axellsolis.earthquakemonitor.utils.ItemClickListener
import com.axellsolis.earthquakemonitor.utils.hide
import com.axellsolis.earthquakemonitor.utils.show
import com.axellsolis.earthquakemonitor.viewmodel.EarthquakeViewModel
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment(), ItemClickListener {

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
    }

    private fun setRecyclerView() {
        mAdapter = EarthquakeAdapter(this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
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
    }

    fun scrollToTop() {
        view?.let {
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }

    override fun onClick(earthquake: Earthquake) {
        earthquakeViewModel.selectItem(earthquake)
        findNavController().navigate(R.id.action_viewPagerFragment_to_earthquakeDetailFragment)
    }

    override fun onLongClick(earthquake: Earthquake) {

    }
}