package com.example.stocks.data.mapper

import com.example.stocks.data.local.ListingEntity
import com.example.stocks.data.remote.dto.CompanyInfoDto
import com.example.stocks.domain.model.CompanyInfo
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

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}


