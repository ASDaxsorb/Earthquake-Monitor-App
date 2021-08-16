package com.axellsolis.earthquakemonitor.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.axellsolis.earthquakemonitor.R
import com.axellsolis.earthquakemonitor.databinding.FragmentMainBinding
import com.axellsolis.earthquakemonitor.ui.adapter.ViewPagerAdapter
import com.axellsolis.earthquakemonitor.viewmodel.EarthquakeViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect

class MainFragment : Fragment(), TabLayout.OnTabSelectedListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var mPagerAdapter: ViewPagerAdapter
    private val earthquakeViewModel by activityViewModels<EarthquakeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
        setUpViewPager()
        initUi()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                onSettingsSelected()
                true
            }
            else -> false
        }
    }

    private fun setUpViewPager() {
        val fragments = arrayListOf<Fragment>(
            LatestEarthquakesFragment(),
            SavedEarthquakesFragment()
        )
        val manager = childFragmentManager
        mPagerAdapter = ViewPagerAdapter(fragments, manager, lifecycle)

        binding.viewPager.adapter = mPagerAdapter
    }

    private fun initUi() {
        setHasOptionsMenu(true)
        binding.apply {
            setTabLayout()
            cvEarthquakesInfo.setOnClickListener {
                val action = R.id.action_viewPagerFragment_to_earthquakeMapFragment
                findNavController().navigate(action)
            }
        }
    }

    private fun setTabLayout() {
        binding.apply {
            val tabLayoutMediator =
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    when (position) {
                        0 -> {
                            tab.icon =
                                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_time)
                        }
                        1 -> {
                            tab.icon =
                                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_save)
                        }
                    }
                }
            tabLayoutMediator.attach()
            tabLayout.addOnTabSelectedListener(this@MainFragment)
        }
    }

    private fun setCollectors() {
        lifecycleScope.launchWhenStarted {
            earthquakeViewModel.counter.collect {
                setCount(it)
            }
        }
    }

    private fun setCount(count: Int) {
        binding.tvCount.text = getString(R.string.template_earthquakes, count)
    }

    private fun onSettingsSelected() {
        val action = R.id.action_viewPagerFragment_to_settingsFragment
        findNavController().navigate(action)
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        tab?.let {
            if (it.position == 0) {
                val homeFragment = childFragmentManager.findFragmentByTag("f0") as LatestEarthquakesFragment
                homeFragment.scrollToTop()
            }
        }
    }
}