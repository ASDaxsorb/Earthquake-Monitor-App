package com.axellsolis.earthquakemonitor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axellsolis.earthquakemonitor.data.model.Earthquake
import com.axellsolis.earthquakemonitor.data.repository.GeneralMapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralMapViewModel
@Inject
constructor(private val repository: GeneralMapRepository) : ViewModel() {

    private val _earthquakes: MutableStateFlow<List<Earthquake>> = MutableStateFlow(listOf())
    val earthquakes: StateFlow<List<Earthquake>> = _earthquakes

    fun getLastHourEarthquakes() {
        viewModelScope.launch {
            repository.getAllHourEarthquakes().collect { list ->
                _earthquakes.value = list
            }
        }
    }
}