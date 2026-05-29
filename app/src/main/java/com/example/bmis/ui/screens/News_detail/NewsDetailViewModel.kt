package com.example.bmis.ui.screens.News_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bmis.data.models.NewsArticleDetail
import com.example.bmis.data.repository.NewsRepository
import com.example.bmis.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewsDetailUiState(
    val isLoading: Boolean = false,
    val article: NewsArticleDetail? = null,
    val errorMessage: String? = null
)

class NewsDetailViewModel(
    private val repository: NewsRepository,
    private val postId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsDetailUiState(isLoading = true))
    val uiState: StateFlow<NewsDetailUiState> = _uiState.asStateFlow()

    init {
        loadArticle()
    }

    fun loadArticle() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val article = repository.getNewsArticle(postId)
                if (article == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Không tìm thấy tin tức"
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, article = article, errorMessage = null)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Không thể tải tin tức"
                    )
                }
            }
        }
    }

    companion object {
        fun provideFactory(
            postId: Int,
            repository: NewsRepository = NewsRepository(RetrofitClient.apiService)
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NewsDetailViewModel(repository, postId) as T
            }
        }
    }
}
