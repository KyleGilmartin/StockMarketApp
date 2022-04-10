package com.example.stocks.data.mapper

import com.example.stocks.data.local.ListingEntity
import com.example.stocks.domain.model.Listing


fun ListingEntity.toMapper():Listing{
    return Listing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun Listing.toMapperEntity():ListingEntity{
    return ListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}


