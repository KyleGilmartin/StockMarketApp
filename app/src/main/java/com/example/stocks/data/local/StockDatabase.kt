package com.example.stocks.data.local

import androidx.room.Database

@Database(
    entities = [ListingEntity::class],
    version = 1
)
abstract class StockDatabase() {
    abstract val dao:StockDao
}