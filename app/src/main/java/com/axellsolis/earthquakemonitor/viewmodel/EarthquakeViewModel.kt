package com.axellsolis.earthquakemonitor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axellsolis.earthquakemonitor.data.model.Earthquake
import com.axellsolis.earthquakemonitor.data.repository.EarthquakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EarthquakeViewModel
@Inject
constructor(private val repository: EarthquakeRepository) : ViewModel() {

    private val _progressVisible = MutableStateFlow(false)
    val progressVisible: StateFlow<Boolean> = _progressVisible

    private val _selectedItem: MutableStateFlow<Earthquake?> = MutableStateFlow(null)
    val selectedItem: StateFlow<Earthquake?> = _selectedItem

    private val _earthquakeList: MutableStateFlow<List<Earthquake>> = MutableStateFlow(listOf())
    val earthquakeList: StateFlow<List<Earthquake>> = _earthquakeList

    private val _counter: MutableStateFlow<Int> = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter

    init {
        getAllDayEarthquakes()
    }

    private fun getAllDayEarthquakes() {
        _earthquakeList.value = listOf()
        viewModelScope.launch {
            _progressVisible.value = true
            repository.getAllEarthquakes().collect { earthquakes ->
                _earthquakeList.value = earthquakes
                setCounter()
            }
            _progressVisible.value = false
        }
    }

    private fun getLastHourEarthquakes() {
        _earthquakeList.value = listOf()
        viewModelScope.launch {
            _progressVisible.value = true
            repository.getAllHourEarthquakes().collect { earthquakes ->
                _earthquakeList.value = earthquakes
                setCounter()
            }
            _progressVisible.value = false
        }
    }

    private fun setCounter() {
        viewModelScope.launch {
            _counter.value = 0
            for (n in 1.._earthquakeList.value.size) {
                _counter.value += 1
                delay(4)
            }
        }
    }

    fun selectItem(earthquake: Earthquake) {
        _selectedItem.value = earthquake
    }

}