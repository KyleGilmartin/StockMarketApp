package com.example.stocks.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ListingEntity(
    val name:String,
    val symbol:String,
    val exchange:String,
    @PrimaryKey val id: Int? = null
)
