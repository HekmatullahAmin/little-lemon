package com.hekmatullahamin.littlelemon.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.hekmatullahamin.littlelemon.data.models.MenuItemNetwork
import com.hekmatullahamin.littlelemon.data.repositories.MenuItemsRepository
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.fake.FakeDataSource
import com.hekmatullahamin.littlelemon.fake.FakeLocalMenuItemRepository
import com.hekmatullahamin.littlelemon.fake.FakeMenuItemsRepository
import com.hekmatullahamin.littlelemon.fake.FakeNetworkMenuItemsRepository
import com.hekmatullahamin.littlelemon.fake.FakeUserRepository
import com.hekmatullahamin.littlelemon.network.NetworkMenuItemsRepository
import com.hekmatullahamin.littlelemon.rules.TestDispatcherRule
import com.hekmatullahamin.littlelemon.ui.navigation.HomeDestination
import com.hekmatullahamin.littlelemon.ui.screens.HomeScreenViewModel
import com.hekmatullahamin.littlelemon.ui.state.HomeUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var fakeUserRepository: UserRepository
    private lateinit var fakeMenuItemsRepository: MenuItemsRepository

    @Before
    fun setup() {
//        Initialize the fake repositories
        val fakeNetworkRepo = FakeNetworkMenuItemsRepository()
        val fakeLocalRepo = FakeLocalMenuItemRepository()
        fakeMenuItemsRepository = FakeMenuItemsRepository(
            fakeNetworkMenuItemsRepository = fakeNetworkRepo,
            fakeLocalMenuItemsRepository = fakeLocalRepo
        )

        fakeUserRepository = FakeUserRepository()

        // Initialize the ViewModel with the default repositories
        viewModel = HomeScreenViewModel(
            SavedStateHandle(mapOf(HomeDestination.userIdArg to 1)),
            fakeUserRepository,
            fakeMenuItemsRepository
        )
    }

    @Test
    fun homeViewModel_Initialization_LoadingState() = runTest {
        // Immediately check the initial state
        assertEquals(HomeUiState.Loading, viewModel.homeUiState.value)

        // Run the currently enqueued tasks (Not needed in this case)
        // Run everything that's ready to go right now.
//        runCurrent()
    }

    @Test
    fun homeViewModel_FetchMenuItems_SuccessStateWithCorrectData() = runTest {

        //      advance the dispatcher to execute pending coroutines
//        Keep running until there's nothing left to do.
        advanceUntilIdle()
//        Assert that the state is Success and contains correct data
        assertTrue(viewModel.homeUiState.value is HomeUiState.Success)
        assertEquals(
            HomeUiState.Success(FakeDataSource.localMenuItemsList),
            viewModel.homeUiState.value
        )
    }


    @Test
    fun homeViewModel_FetchMenuItems_EmptyListResultsInErrorState() = runTest {
//        Set up the FakeMenuItemsRepository to return and empty list
        val fakeEmptyNetworkRepo = object : NetworkMenuItemsRepository {
            override suspend fun getMenuItems(): List<MenuItemNetwork> = emptyList()
        }

        val fakeEmptyMenuItemsRepository = FakeMenuItemsRepository(
            fakeNetworkMenuItemsRepository = fakeEmptyNetworkRepo,
            fakeLocalMenuItemsRepository = FakeLocalMenuItemRepository()
        )

//        initialize the ViewModel with the modified repository
        val viewModel = HomeScreenViewModel(
            SavedStateHandle(mapOf(HomeDestination.userIdArg to 1)),
            fakeUserRepository,
            fakeEmptyMenuItemsRepository
        )

        advanceUntilIdle()

        assertEquals(HomeUiState.Error, viewModel.homeUiState.value)
    }

//    No Asynchronous Operations: If your test only involves synchronous updates or operations that don't require waiting for coroutines, you don't need advanceUntilIdle().
//    For instance, if you are just updating a search phrase or a category that directly affects the StateFlow without requiring background work, you don’t need to advance the dispatcher.

    @Test
    fun homeViewModel_UpdateSearchPhrase_FiltersMenuItemsCorrectly() = runTest {
//        Update the search phrase
        viewModel.updateSearchPhrase("Rice")
        // Assert that the filtered menu items list only contains items that match the search phrase
        val filteredMenuItems = (viewModel.homeUiState.value as HomeUiState.Success).menuItems
        assertTrue(filteredMenuItems.all { it.itemName.contains("Rice", ignoreCase = true) })
    }

    @Test
    fun homeViewModel_UpdateCategory_FiltersMenuItemsByCategory() = runTest {
//        Update the category
        viewModel.updateCategory("drinks")
//        Assert that the filtered menu items list only contains items that match the selected category
        val filteredMenuItems = (viewModel.homeUiState.value as HomeUiState.Success).menuItems
        assertTrue(filteredMenuItems.all { it.itemCategory == "drinks" })
    }
}