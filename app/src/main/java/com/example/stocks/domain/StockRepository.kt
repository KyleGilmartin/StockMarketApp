package com.example.stocks.domain


import com.example.stocks.Util.Resource
import com.example.stocks.domain.model.CompanyInfo
import com.example.stocks.domain.model.IntradayInfo
import com.example.stocks.domain.model.Listing
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListing(
        fetchFromRemote:Boolean,
        query:String
    ): Flow<Resource<List<Listing>>>

    suspend fun getIntradayInfo(
        symbol:String,

    ):Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(
        symbol:String,

    ):Resource<CompanyInfo>
}