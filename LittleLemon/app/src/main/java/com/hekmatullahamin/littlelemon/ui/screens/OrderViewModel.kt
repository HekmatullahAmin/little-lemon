package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.littlelemon.data.models.Order
import com.hekmatullahamin.littlelemon.data.models.OrderItem
import com.hekmatullahamin.littlelemon.data.models.Side
import com.hekmatullahamin.littlelemon.data.repositories.OrderRepository
import com.hekmatullahamin.littlelemon.ui.state.MenuItemUiState
import com.hekmatullahamin.littlelemon.ui.state.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {
    var currentUserId by mutableStateOf(0)
        private set

    private val _orderUiState = MutableStateFlow(OrderUiState())
    val orderUiState: StateFlow<OrderUiState> get() = _orderUiState.asStateFlow()

    fun setUserId(userId: Int) {
        currentUserId = userId
    }

    fun addItemToCart(cartItem: MenuItemUiState) {
        _orderUiState.value = _orderUiState.value.copy(
            cartItems = _orderUiState.value.cartItems + cartItem,
            subtotal = calculateSubtotal(_orderUiState.value.cartItems + cartItem)
        )
    }

    fun incrementItemQuantity(itemId: Int) {
//        first we find the item we want to increment it's quantity and then we update that
//        menu item quantity and later calculate the subtotal accordingly
        val updatedCartItems = _orderUiState.value.cartItems.map { menuItemUiState ->
            if (menuItemUiState.menuItem.itemId == itemId) {
                menuItemUiState.copy(
                    quantity = menuItemUiState.quantity + 1
                )
            } else
                menuItemUiState
        }
        _orderUiState.value = _orderUiState.value.copy(
            cartItems = updatedCartItems,
            subtotal = calculateSubtotal(updatedCartItems)
        )
    }

    fun decrementItemQuantity(itemId: Int) {
        val updatedCartItems = _orderUiState.value.cartItems.map { menuItemUiState ->
            if (menuItemUiState.menuItem.itemId == itemId && menuItemUiState.quantity > 1) {
                menuItemUiState.copy(
                    quantity = menuItemUiState.quantity - 1
                )
            } else
                menuItemUiState
        }
        _orderUiState.value = _orderUiState.value.copy(
            cartItems = updatedCartItems,
            subtotal = calculateSubtotal(updatedCartItems)
        )
    }

    //        subtotal is sum of totalCost of each menuItem
    private fun calculateSubtotal(cartItems: List<MenuItemUiState>): Double {
        return cartItems.sumOf { it.totalCost }
    }

    fun saveOrder() {
        viewModelScope.launch {
            val order = Order(
                userId = currentUserId,
//                we add 10% as delivery fee to total
                subTotal = _orderUiState.value.subtotal,
                deliveryFee = _orderUiState.value.subtotal * 0.1,
                orderDate = System.currentTimeMillis()
            )

            val orderId = orderRepository.insertOrder(order)

            val orderItems = _orderUiState.value.cartItems.map {
//                we receive menuItemUiState and passes it's value to the OrderItem
                OrderItem(
                    orderId = orderId.toInt(),
                    menuItemId = it.menuItem.itemId,
                    quantity = it.quantity,
                    spiceLevel = it.spiceLevel?.name ?: ""
                )
            }

            val orderItemsIds = orderRepository.insertAllOrderItems(orderItems)
            val sides = orderItemsIds.flatMapIndexed { index: Int, orderItemId: Long ->
                _orderUiState.value.cartItems[index].selectedSides.map { side ->
                    Side(
                        orderItemId = orderItemId.toInt(),
                        sideName = side.name,
                        sidePrice = side.price
                    )
                }
            }

            orderRepository.insertAllSides(sides)
        }
    }

    fun resetUiState() {
        _orderUiState.value = OrderUiState()
    }
}