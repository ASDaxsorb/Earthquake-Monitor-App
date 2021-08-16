package com.axellsolis.earthquakemonitor.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axellsolis.earthquakemonitor.data.model.Earthquake
import com.axellsolis.earthquakemonitor.data.repository.DatabaseRepository
import com.axellsolis.earthquakemonitor.data.repository.EarthquakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EarthquakeViewModel
@Inject
constructor(
    private val repository: EarthquakeRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _progressVisible = MutableStateFlow(false)
    val progressVisible: StateFlow<Boolean> = _progressVisible

    private val _selectedItem: MutableStateFlow<Earthquake?> = MutableStateFlow(null)
    val selectedItem: StateFlow<Earthquake?> = _selectedItem

    private val _earthquakeList: MutableStateFlow<List<Earthquake>> = MutableStateFlow(listOf())
    val earthquakeList: StateFlow<List<Earthquake>> = _earthquakeList

    private val _counter: MutableStateFlow<Int> = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter

    private val _savedEarthquakes: MutableStateFlow<List<Earthquake>> = MutableStateFlow(mutableListOf())
    val savedEarthquakes: StateFlow<List<Earthquake>> = _savedEarthquakes

    init {
        getAllDayEarthquakes()
    }

    private fun getAllDayEarthquakes() {
        Log.d("Earthquakes", "getAllDayEarthquakes: ")
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

    fun saveEarthquake() {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedItem.value?.let { eq ->
                val entity = eq.toEntity()
                databaseRepository.saveEarthquake(entity)
            }
        }
    }

    fun getEntities() {
        viewModelScope.launch {
            databaseRepository.getEarthquakesEntity().collect { entities ->
                val list = entities.map { entity ->
                    entity.toModel()
                }
                _savedEarthquakes.value = list
            }
        }
    }

    fun deleteEarthquake(earthquake: Earthquake) {
        val newList = _savedEarthquakes.value.toMutableList()
        val isDeleted = newList.remove(earthquake)
        val id = earthquake.id.toInt()
        if (isDeleted) {
            viewModelScope.launch(Dispatchers.IO) {
                val entity = databaseRepository.getEarthquake(id)
                databaseRepository.deleteEarthquake(entity)
                _savedEarthquakes.value = newList
            }
        }
    }
}