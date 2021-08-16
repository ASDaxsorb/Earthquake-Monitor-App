package com.axellsolis.earthquakemonitor.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.axellsolis.earthquakemonitor.R
import com.axellsolis.earthquakemonitor.data.model.Earthquake
import com.axellsolis.earthquakemonitor.databinding.FragmentLatestEarthquakesBinding
import com.axellsolis.earthquakemonitor.ui.adapter.EarthquakeAdapter
import com.axellsolis.earthquakemonitor.utils.ItemClickListener
import com.axellsolis.earthquakemonitor.utils.hide
import com.axellsolis.earthquakemonitor.utils.show
import com.axellsolis.earthquakemonitor.viewmodel.EarthquakeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class LatestEarthquakesFragment : Fragment(), ItemClickListener {

    private lateinit var binding: FragmentLatestEarthquakesBinding
    private lateinit var mAdapter: EarthquakeAdapter
    private val earthquakeViewModel by activityViewModels<EarthquakeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLatestEarthquakesBinding.inflate(inflater, container, false)
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
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_save_earthquake)
            .setPositiveButton(R.string.button_yes) { _, _ ->
                onSaveEarthquake(earthquake)
            }.setNegativeButton(R.string.button_no, null).show()
    }

    private fun onSaveEarthquake(earthquake: Earthquake) {
        earthquakeViewModel.saveEarthquake(earthquake)
        Snackbar.make(
            requireView(),
            getString(R.string.snack_bar_earthquake_saved),
            Snackbar.LENGTH_SHORT
        ).show()
        val pager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
        pager?.let {
            it.currentItem = 1
        }
    }
}