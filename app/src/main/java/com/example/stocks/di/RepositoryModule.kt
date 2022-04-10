package com.example.stocks.di

import com.example.stocks.data.csv.CSVParser
import com.example.stocks.data.csv.ListingsParser
import com.example.stocks.data.repository.StockRepositoryImpl
import com.example.stocks.domain.StockRepository
import com.example.stocks.domain.model.Listing
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingParser(
        ListingsParser:ListingsParser
    ): CSVParser<Listing>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ):StockRepository
}