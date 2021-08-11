package com.axellsolis.earthquakemonitor.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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
import kotlinx.coroutines.flow.collect

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

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.savedEarthquakes -> {
                val action = R.id.action_homeFragment_to_savedEarthquakesFragment
                findNavController().navigate(action)
                true
            }
            else -> false
        }
    }

    private fun setRecyclerView() {
        mAdapter = EarthquakeAdapter {
            earthquakeViewModel.selectItem(it)
            findNavController().navigate(R.id.action_homeFragment_to_earthquakeDetailFragment)
        }
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

        lifecycleScope.launchWhenStarted {
            earthquakeViewModel.counter.collect {
                setCount(it)
            }
        }
    }

    private fun initUi() {
        binding.apply {
            cvEarthquakesInfo.setOnClickListener {
                val action = R.id.action_homeFragment_to_earthquakeMapFragment
                findNavController().navigate(action)
            }
        }
    }

    private fun setCount(count: Int) {
        binding.tvCount.text = getString(R.string.template_earthquakes, count)
    }
}