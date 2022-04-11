package com.example.stocks.presentation.info

import com.example.stocks.domain.model.CompanyInfo
import com.example.stocks.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos:List<IntradayInfo> = emptyList(),
    val company:CompanyInfo? = null,
    val isLoading:Boolean = false,
    val error:String?  = null
)
