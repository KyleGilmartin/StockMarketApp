package com.example.stocks.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListing(
        listingEntity:List<ListingEntity>
    )

    @Query("DELETE FROM listingentity")
    suspend fun clearCompanyListing()

    @Query("""
        select *
        from listingentity
        where LOSER(name) like '%' || LOWER(:query) || '%' or UPPER(:query) == symbol
    """)
    suspend fun searchCompanyListing(query: String):List<ListingEntity>


}