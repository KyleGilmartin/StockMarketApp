package com.example.stocks.presentation.listings

import com.example.stocks.domain.model.Listing

data class ListingsState(
    val companies:List<Listing> = emptyList(),
    val isLoading:Boolean = false,
    val isRefreshing: Boolean = false,
    val searchingQuery: String = ""
)
