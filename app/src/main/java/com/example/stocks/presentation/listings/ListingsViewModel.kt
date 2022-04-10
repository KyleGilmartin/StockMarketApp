package com.example.stocks.presentation.listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocks.Util.Resource
import com.example.stocks.domain.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingsViewModel @Inject constructor(
    private val repository: StockRepository
):ViewModel() {
   var state by mutableStateOf(ListingsState())

    private var searchJob:Job? = null

    init {
        getCompanyListings()
    }

    fun onEvent(event: ListingsEvent){
        when(event){
            is ListingsEvent.Refresh ->{
                getCompanyListings(fetchFromRemote = true)
            }
            is ListingsEvent.OnSearchQueryChange ->{
                state = state.copy(searchingQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListings()
                }
            }
        }
    }

     private fun getCompanyListings(
        query:String = state.searchingQuery.lowercase(),
        fetchFromRemote:Boolean = false
    ){
        viewModelScope.launch {
            repository.getCompanyListing(fetchFromRemote, query)
                .collect{
                    result ->
                    when(result){
                        is Resource.Success ->{
                            result.data?.let { listings ->
                                state = state.copy(
                                    companies = listings
                                )
                            }
                        }
                        is Resource.Error -> Unit


                        is Resource.Loading ->{
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

}