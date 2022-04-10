package com.example.stocks.presentation.listings

sealed class ListingsEvent{
    object Refresh:ListingsEvent()
    data class OnSearchQueryChange(val query:String):ListingsEvent()
}
