package com.example.stocks.data.repository

import com.example.stocks.Util.Resource
import com.example.stocks.data.csv.CSVParser
import com.example.stocks.data.csv.ListingsParser
import com.example.stocks.data.local.StockDatabase
import com.example.stocks.data.mapper.toCompanyInfo
import com.example.stocks.data.mapper.toMapper
import com.example.stocks.data.mapper.toMapperEntity
import com.example.stocks.data.remote.StockApi
import com.example.stocks.domain.StockRepository
import com.example.stocks.domain.model.CompanyInfo
import com.example.stocks.domain.model.IntradayInfo
import com.example.stocks.domain.model.Listing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api:StockApi,
    val db:StockDatabase,
    val listingsParser:CSVParser<Listing>,
    val IntradayInfoParser:CSVParser<IntradayInfo>
):StockRepository{
    private val dao = db.dao

    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<Listing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListing = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListing.map {
                    it.toMapper()
                }
            ))
            val isDbEmpty = localListing.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache){
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListing = try {
                val response = api.getListings()
                listingsParser.parse(response.byteStream())
            }catch (e:IOException){
                e.printStackTrace()
                emit(Resource.Error("couldn't load data from storage"))
                null
            }catch (e:HttpException){
                e.printStackTrace()
                emit(Resource.Error("couldn't load data from api"))
                null
            }
            remoteListing?.let { listings ->
                dao.clearCompanyListing()
                dao.insertCompanyListing(
                    listings.map { it.toMapperEntity() }
                )
                emit(Resource.Success(
                    data = dao
                        .searchCompanyListing("")
                        .map { it.toMapper() }
                ))
                emit(Resource.Loading(false))

            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val result = IntradayInfoParser.parse(response.byteStream())
            Resource.Success(result)
        }catch (e:IOException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )

        }catch (e:HttpException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        }catch (e:IOException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info"
            )

        }catch (e:HttpException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info"
            )
        }
    }
}