package com.example.bmis.ui.screens.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProfileUiState(
    val userName: String = "Nguyễn Thị An",
    val phone: String = "0912415562",
    val profileImage: String? = null,
    val apartments: List<String> = listOf("CH002", "CH001", "CH004", "CH003"),
    val showApartmentSheet: Boolean = false,
    val showSecuritySheet: Boolean = false,
    val showChangePasswordDialog: Boolean = false,
    val showSuccessPopup: Boolean = false,
    val showAvatarOptions: Boolean = false,
    val isFingerprintEnabled: Boolean = true
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun toggleApartmentSheet(show: Boolean) {
        _uiState.update { it.copy(showApartmentSheet = show) }
    }

    fun toggleSecuritySheet(show: Boolean) {
        _uiState.update { it.copy(showSecuritySheet = show) }
    }

    fun toggleChangePasswordDialog(show: Boolean) {
        _uiState.update { it.copy(showChangePasswordDialog = show, showSecuritySheet = false) }
    }

    fun toggleSuccessPopup(show: Boolean) {
        _uiState.update { it.copy(showSuccessPopup = show, showChangePasswordDialog = false) }
    }

    fun toggleAvatarOptions(show: Boolean) {
        _uiState.update { it.copy(showAvatarOptions = show) }
    }

    fun setFingerprintEnabled(enabled: Boolean) {
        _uiState.update { it.copy(isFingerprintEnabled = enabled) }
    }

    fun logout() {
        // Logic logout
    }
}
