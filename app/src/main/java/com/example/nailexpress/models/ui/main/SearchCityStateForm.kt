package com.example.nailexpress.models.ui.main

import android.support.core.route.BundleArgument
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchCityStateForm(
    var stateSearch: String = "",
    var stateFormat: String = "",
    var citySearch: String = "",
    var format: String = ""
) : BundleArgument {

    fun setSate(item: StateDTO): String {
        stateSearch = item.state_code
        stateFormat = "${item.state} (${item.state_code})"
        format = stateFormat

        return stateFormat
    }

    fun setCity(item: CityDTO): String {
        citySearch = item.city
        format = "$citySearch, $stateFormat"
        return citySearch
    }

    fun formatDisplay(): String {
        stateFormat = stateSearch
        format = "$citySearch, $stateSearch"
        return format
    }

    fun getStateSearch(): SearchState {
        return if (stateSearch.isBlank()) {
            SearchState.STATE
        } else if (citySearch.isBlank()) {
            SearchState.CITY
        } else
            SearchState.COMPLETE
    }

    fun clearCity() {
        citySearch = ""
        format = stateFormat
    }

    fun clearAll() {
        stateSearch = ""
        stateFormat = ""
        citySearch = ""
        format = ""
    }
}

data class StateDTO(
    val id: Int = 0,
    val state_code: String = "",
    val state: String = ""
)

data class CityDTO(
    val id: Int = 0,
    val city: String = "",
    val state_code: String = ""
)

enum class SearchState {
    STATE, CITY, COMPLETE
}