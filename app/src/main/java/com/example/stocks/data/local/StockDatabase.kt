package com.example.stocks.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ListingEntity::class],
    version = 1
)
abstract class StockDatabase() :RoomDatabase() {
    abstract val dao:StockDao
}