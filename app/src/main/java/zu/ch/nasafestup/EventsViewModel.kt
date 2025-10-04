/*
 * Copyright © 2014-2025, TWINT AG.
 * All rights reserved.
*/
package zu.ch.nasafestup

import Event
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fakeEvents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import zu.ch.nasafestup.data.RetrofitInstance
import zu.ch.nasafestup.data.toEvents

class EventsViewModel : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    init {
        viewModelScope.launch {

            try {
                val dtos = RetrofitInstance.api.getEvents()
                _events.value = dtos.toEvents()
            } catch (e: Exception) {
                e.printStackTrace()
                _events.value = fakeEvents
            }

        }
    }

    fun addEvent(event: Event) {
        _events.value = _events.value + event
    }
}
