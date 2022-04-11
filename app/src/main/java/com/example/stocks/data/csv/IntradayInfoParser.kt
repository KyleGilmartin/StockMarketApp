package com.example.stocks.data.csv

import com.example.stocks.data.mapper.toIntradayInfo
import com.example.stocks.data.remote.dto.IntradayInfoDto
import com.example.stocks.domain.model.IntradayInfo
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDate
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntradayInfoParser @Inject constructor() :CSVParser<IntradayInfo> {
    override suspend fun parse(stream: InputStream): List<IntradayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        val calendar: Calendar = Calendar.getInstance()
        val day: Int = calendar.get(Calendar.DAY_OF_WEEK)
        return withContext(Dispatchers.IO){
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val timestamp = line.getOrNull(0) ?: return@mapNotNull  null
                    val close = line.getOrNull(4) ?: return@mapNotNull  null
                   val dto = IntradayInfoDto(timestamp,close.toDouble())
                    dto.toIntradayInfo()

                }.filter {
                    it.date.dayOfMonth == LocalDate.now().minusDays(4).dayOfMonth

//
                }.sortedBy {
                    it.date.hour
                }
                .also {
                    csvReader.close()
                }
        }
    }
}