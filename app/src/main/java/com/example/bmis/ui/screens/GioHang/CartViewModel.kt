package com.example.bmis.ui.screens.GioHang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bmis.data.models.CartItem
import com.example.bmis.data.repository.CartRepository
import com.example.bmis.data.repository.FakeCartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val items: List<CartItem> = emptyList(),
    val checkedStates: Map<String, Boolean> = emptyMap(),
    val quantities: Map<String, Int> = emptyMap(),
    val noteText: String = "",
    val selectedPaymentMethod: String = "VNPT PAY",
    val registrantName: String = "",
    val apartmentOptions: List<String> = emptyList(),
    val confirmedApartment: String = "",
    val confirmedPhone: String = "",
    val paymentMethods: List<String> = emptyList(),
    val showApartmentSheet: Boolean = false,
    val showPhoneSheet: Boolean = false,
    val temporaryApartment: String = "",
    val temporaryPhone: String = ""
)

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState(isLoading = true))
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val items = repository.getCartItems()
                val registration = repository.getRegistrationInfo()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        items = items,
                        checkedStates = items.associate { item -> item.id to true },
                        quantities = items.associate { item -> item.id to 1 },
                        registrantName = registration.registrantName,
                        apartmentOptions = registration.apartmentOptions,
                        confirmedApartment = registration.defaultApartment,
                        confirmedPhone = registration.defaultPhone,
                        temporaryApartment = registration.defaultApartment,
                        temporaryPhone = registration.defaultPhone,
                        selectedPaymentMethod = registration.paymentMethods.first(),
                        paymentMethods = registration.paymentMethods,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Không thể tải giỏ hàng"
                    )
                }
            }
        }
    }

    fun updateQuantity(itemId: String, delta: Int) {
        _uiState.update { state ->
            val currentQty = state.quantities[itemId] ?: 1
            val newQty = (currentQty + delta).coerceAtLeast(1)
            state.copy(quantities = state.quantities + (itemId to newQty))
        }
    }

    fun toggleChecked(itemId: String, isChecked: Boolean) {
        _uiState.update { it.copy(checkedStates = it.checkedStates + (itemId to isChecked)) }
    }

    fun updateNote(note: String) {
        _uiState.update { it.copy(noteText = note) }
    }

    fun updatePaymentMethod(method: String) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun openApartmentSheet() {
        _uiState.update {
            it.copy(
                showApartmentSheet = true,
                temporaryApartment = it.confirmedApartment
            )
        }
    }

    fun dismissApartmentSheet() {
        _uiState.update { it.copy(showApartmentSheet = false) }
    }

    fun selectTemporaryApartment(apartment: String) {
        _uiState.update { it.copy(temporaryApartment = apartment) }
    }

    fun confirmApartment() {
        _uiState.update {
            it.copy(
                confirmedApartment = it.temporaryApartment,
                showApartmentSheet = false
            )
        }
    }

    fun openPhoneSheet() {
        _uiState.update {
            it.copy(
                showPhoneSheet = true,
                temporaryPhone = it.confirmedPhone
            )
        }
    }

    fun dismissPhoneSheet() {
        _uiState.update { it.copy(showPhoneSheet = false) }
    }

    fun updateTemporaryPhone(phone: String) {
        _uiState.update { it.copy(temporaryPhone = phone) }
    }

    fun confirmPhone() {
        _uiState.update {
            it.copy(
                confirmedPhone = it.temporaryPhone,
                showPhoneSheet = false
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CartViewModel(FakeCartRepository()) as T
            }
        }
    }
}
