package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.data.repositories.MenuItemsRepository
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.ui.navigation.HomeDestination
import com.hekmatullahamin.littlelemon.ui.state.HomeUiState
import com.hekmatullahamin.littlelemon.utils.Constants
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val menuItemsRepository: MenuItemsRepository
) : ViewModel() {

    val userId: Int = checkNotNull(savedStateHandle[HomeDestination.userIdArg])
    val user: StateFlow<User> = userRepository.getUserStreamUsingId(userId)
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
            initialValue = User()
        )

    private var _homeUiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    //Search phrase state
    var searchPhrase by mutableStateOf("")
        private set

    private var selectedCategory by mutableStateOf<String?>(null)

    //     Store the original list of menu items
    private var originalMenuItems: List<MenuItemRoom> = listOf()

    init {
        fetchMenuItems()
    }

    private fun fetchMenuItems() {
        viewModelScope.launch {
            _homeUiState.value = HomeUiState.Loading
            try {
                // Fetch menu items from network and store them in the database
//                this is how they asked me to do: the Little Lemon Project from Meta Android Developer
//                Android App Capstone
                val menuItemsFromNetwork = menuItemsRepository.fetchMenuItems()
                val menuItemsRoom = menuItemsFromNetwork.map {
                    it.toMenuItemRoom()
                }
                menuItemsRepository.insertAll(menuItemsRoom)
                // get menu items from the database
                getMenuItemsFromDb()

            } catch (e: IOException) {
                _homeUiState.value = HomeUiState.Error
            }
        }
    }

    private suspend fun getMenuItemsFromDb() {
        menuItemsRepository.getAllMenuItems()
            .filterNotNull()
            .collectLatest {
                if (it.isEmpty()) {
                    _homeUiState.value = HomeUiState.Error
                } else {
                    _homeUiState.value = HomeUiState.Success(it)
                    originalMenuItems = it
                }
            }
    }

    fun updateSearchPhrase(phrase: String) {
        searchPhrase = phrase
        val filteredMenuItems =
            originalMenuItems.filter { it.itemName.contains(searchPhrase, ignoreCase = true) }
//        we will show the filtered menu items under the search box if it contains search phrase
        _homeUiState.value = HomeUiState.Success(filteredMenuItems)
    }

    fun updateCategory(category: String) {
        selectedCategory = category
        val filteredMenuItems = if (category == "All") {
            originalMenuItems
        } else {
            originalMenuItems.filter { it.itemCategory == selectedCategory }
        }
        _homeUiState.value = HomeUiState.Success(filteredMenuItems)
    }

    fun getCategories(): List<String> {
//        the "All" category will be included by default, and when selected, it will display all menu items.
        return listOf("All") + originalMenuItems.map {
            it.itemCategory
        }.distinct()
    }
}