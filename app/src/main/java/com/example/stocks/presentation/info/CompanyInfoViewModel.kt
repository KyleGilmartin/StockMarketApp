package com.example.stocks.presentation.info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocks.Util.Resource
import com.example.stocks.domain.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandel:SavedStateHandle,
    private val repository: StockRepository
): ViewModel() {
    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandel.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)

            val companyInfoResult = async { repository.getCompanyInfo(symbol)}
            val intradayInfoResult = async {repository.getIntradayInfo(symbol)}

            when(val result = companyInfoResult.await()){
                is Resource.Success ->{
                    state = state.copy(
                        company = result.data,
                        isLoading = false,
                        error = null

                    )
                }
                is Resource.Error ->{
                    state = state.copy(
                        company = null,
                        isLoading = false,
                        error = result.message
                    )


                }
                else -> Unit
            }

            when(val result = intradayInfoResult.await()){
                is Resource.Success ->{
                    state = state.copy(
                        stockInfos = result.data ?: emptyList(),
                        isLoading = false,
                        error = null

                    )
                }
                is Resource.Error ->{
                    state = state.copy(
                        company = null,
                        isLoading = false,
                        error = result.message
                    )


                }
                else -> Unit
            }

        }
    }
}