package com.hekmatullahamin.littlelemon.viewmodels

import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import com.hekmatullahamin.littlelemon.data.models.Order
import com.hekmatullahamin.littlelemon.data.repositories.OrderRepository
import com.hekmatullahamin.littlelemon.fake.FakeOrderRepository
import com.hekmatullahamin.littlelemon.rules.TestDispatcherRule
import com.hekmatullahamin.littlelemon.ui.screens.OrderViewModel
import com.hekmatullahamin.littlelemon.ui.screens.Side
import com.hekmatullahamin.littlelemon.ui.screens.SpiceLevel
import com.hekmatullahamin.littlelemon.ui.state.MenuItemUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Rule

class OrderViewModelTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()
    //    because we use some additional methods for our test so we made the type to
//    FakeOrderRepository not OrderRepository
    private lateinit var fakeOrderRepository: FakeOrderRepository
    private lateinit var viewModel: OrderViewModel

    @Before
    fun setup() {
        fakeOrderRepository = FakeOrderRepository()
        viewModel = OrderViewModel(
            orderRepository = fakeOrderRepository
        )
    }

    @Test
    fun orderViewModel_SetUserId_SetsUserIdCorrectly() {
        viewModel.setUserId(1)
        assertTrue(viewModel.currentUserId == 1)
    }

    @Test
    fun orderViewModel_AddItemToCart_UpdatesCartItemsAndSubtotal() {
        val cartItem = MenuItemUiState(
            menuItem = MenuItemRoom(
                itemId = 1,
                itemName = "Pepsi",
                itemCategory = "Drink",
                itemImage = "",
                itemDescription = "Sugar free",
                itemPrice = 20.0
            ),
            spiceLevel = SpiceLevel.HOT,
            selectedSides = listOf(Side.CULT_CHAPATI, Side.THIN_WHEAT_BREAD)
        )

        viewModel.addItemToCart(cartItem)
        assertTrue(viewModel.orderUiState.value.cartItems.contains(cartItem))
        assertTrue(viewModel.orderUiState.value.subtotal == cartItem.totalCost)
    }

    @Test
    fun orderViewModel_IncrementItemQuantity_UpdatesQuantityAndSubtotal() {
        val cartItem = MenuItemUiState(
            menuItem = MenuItemRoom(
                itemId = 1,
                itemName = "Pepsi",
                itemCategory = "Drink",
                itemImage = "",
                itemDescription = "Sugar free",
                itemPrice = 20.0
            ),
            spiceLevel = SpiceLevel.HOT,
            selectedSides = listOf(Side.CULT_CHAPATI, Side.THIN_WHEAT_BREAD)
        )

//        first add the cartItem
        viewModel.addItemToCart(cartItem)

//        increment the quantity by one
        viewModel.incrementItemQuantity(cartItem.menuItem.itemId)

//        verify that our first cartItem with sides total plus another menuItem which is added, sum of them is equal to subtotal
        assertTrue(viewModel.orderUiState.value.subtotal == cartItem.totalCost + cartItem.menuItem.itemPrice)
    }

    @Test
    fun orderViewModel_DecrementItemQuantity_UpdatesQuantityAndSubtotal() {
        val menuItem = MenuItemRoom(
            itemId = 1,
            itemName = "Pepsi",
            itemCategory = "Drink",
            itemImage = "",
            itemDescription = "Sugar free",
            itemPrice = 20.0
        )
//        we put the quantity to 2 for the purpose of making it possible to decrease it by one
        val cartItem = MenuItemUiState(
            menuItem = menuItem,
            spiceLevel = SpiceLevel.HOT,
            quantity = 2,
            selectedSides = listOf(Side.CULT_CHAPATI, Side.THIN_WHEAT_BREAD)
        )

        viewModel.addItemToCart(cartItem)

//        decrement the quantity by one
        viewModel.decrementItemQuantity(itemId = 1)

//        verify that our subtotal is equal to the total cost(menuItems*quantity + sides price)
//        minus the price of one menuItem(menuItem.itemPrice) which decremented is equal
        assertTrue(viewModel.orderUiState.value.subtotal == cartItem.totalCost - cartItem.menuItem.itemPrice)
    }

    @Test
    fun orderViewModel_DecrementItemQuantity_DoesNotDecrementBelowOne() {
        val menuItem = MenuItemRoom(
            itemId = 1,
            itemName = "Pepsi",
            itemCategory = "Drink",
            itemImage = "",
            itemDescription = "Sugar free",
            itemPrice = 20.0
        )
        val cartItem = MenuItemUiState(
            menuItem = menuItem,
            spiceLevel = SpiceLevel.HOT,
            quantity = 1,
            selectedSides = listOf(Side.CULT_CHAPATI, Side.THIN_WHEAT_BREAD)
        )

        viewModel.addItemToCart(cartItem)

        viewModel.decrementItemQuantity(1)

//        it didn't got decremented because the total cost is still the same
        assertTrue(viewModel.orderUiState.value.subtotal == cartItem.totalCost)
    }

    @Test
    fun orderViewModel_SaveOrder_SavesOrderWithCorrectDetails() = runTest {
        val order = Order(
            orderId = 1,
            userId = 1,
            subTotal = 20.0,
            deliveryFee = 20.0 * 0.1,
            orderDate = System.currentTimeMillis(),
        )

        val insertedOrderId = fakeOrderRepository.insertOrder(order)

        assertTrue(fakeOrderRepository.getOrderStream(insertedOrderId).first() == order)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun orderViewModel_SaveOrder_SavesOrderItemsCorrectly() = runTest {
//        Arrange
        val menuItemUiState = MenuItemUiState(menuItem = MenuItemRoom(itemId = 1, itemPrice = 10.0))
        viewModel.addItemToCart(menuItemUiState)

//        Act
        viewModel.saveOrder()
        advanceUntilIdle()

//        we set orderId of getOrderItemsStream to 0
//        because when we call saveOrder function the default oderId = 0
        val savedOrderItems = fakeOrderRepository.getOrderItemsStream(0).first()

//        Verify
        assertEquals(1, savedOrderItems.size)
        assertEquals(1, savedOrderItems[0].menuItemId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun orderViewModel_SaveOrder_SavesSidesCorrectly() = runTest {
//        Arrange
        val sides = listOf(Side.CULT_CHAPATI)
        val menuItemUiState = MenuItemUiState(
            menuItem = MenuItemRoom(itemId = 1, itemPrice = 10.0),
            selectedSides = sides
        )

        viewModel.addItemToCart(menuItemUiState)

//        Act
        viewModel.saveOrder()
        advanceUntilIdle()
//        Assert
        val savedSides = fakeOrderRepository.getSidesStream(0).first()

        assertEquals(1, savedSides.size)
        assertEquals(Side.CULT_CHAPATI.name, savedSides[0].sideName)
        assertTrue(savedSides[0].sidePrice == Side.CULT_CHAPATI.price)
    }

    @Test
    fun orderViewModel_ResetUiState_ResetsStateToInitial() {
        // Arrange
        val menuItemUiState = MenuItemUiState(menuItem = MenuItemRoom(itemId = 1, itemPrice = 10.0))
        viewModel.addItemToCart(menuItemUiState)

        // Act
        viewModel.resetUiState()

        // Assert
        assertEquals(0, viewModel.orderUiState.value.cartItems.size)
        assertEquals(0.0, viewModel.orderUiState.value.subtotal, 0.01)
    }
}