package com.example.bmis.ui.screens.LuongSuaChua.ChiTietDichVuSuaChuaDangKy


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bmis.data.models.FilterStep
import com.example.bmis.data.models.ItemsServices
import com.example.bmis.data.models.SortOption
import com.example.bmis.data.repository.FakePaidServicesRepository
import com.example.bmis.data.repository.PaidServicesRepository
import com.example.bmis.ui.screens.services.StatusType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PaidServicesUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val screenTitle: String = "",
    val allInvoices: List<ItemsServices> = emptyList(),
    val displayedInvoices: List<ItemsServices> = emptyList(),
    val serviceFilterOptions: List<String> = emptyList(),
    val apartmentFilterOptions: List<String> = emptyList(),
    val showSortSheet: Boolean = false,
    val showFilterSheet: Boolean = false,
    val temporarySortOption: SortOption = SortOption.ALL,
    val appliedSortOption: SortOption = SortOption.ALL,
    val currentFilterStep: FilterStep = FilterStep.MAIN,
    val appliedTime: String? = null,
    val appliedServices: Set<String> = emptySet(),
    val appliedApartment: String? = null,
    val tempTime: String? = null,
    val tempServices: Set<String> = emptySet(),
    val tempApartment: String? = null
) {
    val activeFilterCount: Int
        get() = listOfNotNull(
            appliedTime,
            appliedServices.takeIf { it.isNotEmpty() },
            appliedApartment
        ).size
}

class PaidServicesViewModel(
    private val repository: PaidServicesRepository,
    private val statusType: StatusType
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaidServicesUiState(isLoading = true))
    val uiState: StateFlow<PaidServicesUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val filterOptions = repository.getFilterOptions()
                val invoices = repository.getInvoices(statusType.name)
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        screenTitle = titleFor(statusType),
                        allInvoices = invoices,
                        serviceFilterOptions = filterOptions.serviceNames,
                        apartmentFilterOptions = filterOptions.apartmentOptions,
                        appliedTime = filterOptions.defaultTime,
                        appliedServices = filterOptions.defaultServices,
                        appliedApartment = filterOptions.defaultApartment,
                        errorMessage = null
                    )
                }
                refreshDisplayedInvoices()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Không thể tải danh sách"
                    )
                }
            }
        }
    }

    fun openSortSheet() {
        _uiState.update {
            it.copy(
                showSortSheet = true,
                temporarySortOption = it.appliedSortOption
            )
        }
    }

    fun dismissSortSheet() {
        _uiState.update { it.copy(showSortSheet = false) }
    }

    fun selectTemporarySort(option: SortOption) {
        _uiState.update { it.copy(temporarySortOption = option) }
    }

    fun applySort() {
        _uiState.update {
            it.copy(
                appliedSortOption = it.temporarySortOption,
                showSortSheet = false
            )
        }
        refreshDisplayedInvoices()
    }

    fun openFilterSheet() {
        _uiState.update {
            it.copy(
                showFilterSheet = true,
                currentFilterStep = FilterStep.MAIN
            )
        }
    }

    fun dismissFilterSheet() {
        _uiState.update { it.copy(showFilterSheet = false) }
    }

    fun applyFilterSheet() {
        _uiState.update { it.copy(showFilterSheet = false) }
        refreshDisplayedInvoices()
    }

    fun resetFilters() {
        _uiState.update {
            it.copy(
                appliedTime = null,
                appliedServices = emptySet(),
                appliedApartment = null
            )
        }
        refreshDisplayedInvoices()
    }

    fun openFilterStep(step: FilterStep) {
        _uiState.update { state ->
            when (step) {
                FilterStep.TIME -> state.copy(
                    currentFilterStep = step,
                    tempTime = state.appliedTime
                )
                FilterStep.SERVICES -> state.copy(
                    currentFilterStep = step,
                    tempServices = state.appliedServices
                )
                FilterStep.APARTMENT -> state.copy(
                    currentFilterStep = step,
                    tempApartment = state.appliedApartment
                )
                else -> state.copy(currentFilterStep = step)
            }
        }
    }

    fun selectTempTime(monthStr: String) {
        _uiState.update {
            it.copy(
                tempTime = monthStr,
                appliedTime = monthStr,
                currentFilterStep = FilterStep.MAIN
            )
        }
        refreshDisplayedInvoices()
    }

    fun clearTimeFilter() {
        _uiState.update {
            it.copy(
                tempTime = null,
                appliedTime = null,
                currentFilterStep = FilterStep.MAIN
            )
        }
        refreshDisplayedInvoices()
    }

    fun toggleTempService(service: String) {
        _uiState.update { state ->
            val updated = if (state.tempServices.contains(service)) {
                state.tempServices - service
            } else {
                state.tempServices + service
            }
            state.copy(tempServices = updated)
        }
    }

    fun clearTempServices() {
        _uiState.update { it.copy(tempServices = emptySet()) }
    }

    fun confirmTempServices() {
        _uiState.update {
            it.copy(
                appliedServices = it.tempServices,
                currentFilterStep = FilterStep.MAIN
            )
        }
        refreshDisplayedInvoices()
    }

    fun selectTempApartment(apartment: String) {
        _uiState.update { it.copy(tempApartment = apartment) }
    }

    fun confirmTempApartment() {
        _uiState.update {
            it.copy(
                appliedApartment = it.tempApartment,
                currentFilterStep = FilterStep.MAIN
            )
        }
        refreshDisplayedInvoices()
    }

    private fun refreshDisplayedInvoices() {
        _uiState.update { state ->
            state.copy(displayedInvoices = applyFiltersAndSort(state.allInvoices, state))
        }
    }

    private fun applyFiltersAndSort(
        invoices: List<ItemsServices>,
        state: PaidServicesUiState
    ): List<ItemsServices> {
        var result = invoices

        state.appliedTime?.let { timeFilter ->
            val parts = timeFilter.removePrefix("Tháng ").split(" - ")
            if (parts.size == 2) {
                val month = parts[0].trim().padStart(2, '0')
                val year = parts[1].trim()
                result = result.filter { it.registrationDate.contains("$month/$year") }
            }
        }

        if (state.appliedServices.isNotEmpty()) {
            result = result.filter { invoice ->
                state.appliedServices.any { service ->
                    invoice.servicesName.contains(service, ignoreCase = true)
                }
            }
        }

        state.appliedApartment?.let { apartment ->
            result = result.filter { it.apartment == apartment }
        }

        return sortInvoices(result, state.appliedSortOption)
    }

    private fun sortInvoices(invoices: List<ItemsServices>, option: SortOption): List<ItemsServices> {
        return when (option) {
            SortOption.ALL -> invoices
            SortOption.AMOUNT_ASC -> invoices.sortedBy { parseAmount(it.totalAmount) }
            SortOption.AMOUNT_DESC -> invoices.sortedByDescending { parseAmount(it.totalAmount) }
            SortOption.DATE_NEWEST -> invoices.sortedByDescending { parseDate(it.registrationDate) }
            SortOption.DATE_OLDEST -> invoices.sortedBy { parseDate(it.registrationDate) }
        }
    }

    private fun parseAmount(amount: String): Long =
        amount.filter { it.isDigit() }.toLongOrNull() ?: 0L

    private fun parseDate(date: String): Long {
        val parts = date.split("/")
        if (parts.size != 3) return 0L
        val day = parts[0].toIntOrNull() ?: 0
        val month = parts[1].toIntOrNull() ?: 0
        val year = parts[2].toIntOrNull() ?: 0
        return year * 10_000L + month * 100L + day
    }

    private fun titleFor(statusType: StatusType): String = when (statusType) {
        StatusType.SUCCESS -> "Đã xử lý"
        StatusType.WARNING -> "Đã tiếp nhận"
        StatusType.ERROR -> "Chưa tiếp nhận"
    }

    companion object {
        fun provideFactory(
            statusType: StatusType,
            repository: PaidServicesRepository = FakePaidServicesRepository()
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PaidServicesViewModel(repository, statusType) as T
            }
        }
    }
}
