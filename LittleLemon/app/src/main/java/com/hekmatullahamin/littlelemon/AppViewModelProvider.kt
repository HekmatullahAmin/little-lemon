package com.hekmatullahamin.littlelemon

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hekmatullahamin.littlelemon.ui.screens.AddressViewModel
import com.hekmatullahamin.littlelemon.ui.screens.CheckoutViewModel
import com.hekmatullahamin.littlelemon.ui.screens.EditProfileViewModel
import com.hekmatullahamin.littlelemon.ui.screens.LoginViewModel
import com.hekmatullahamin.littlelemon.ui.screens.HomeScreenViewModel
import com.hekmatullahamin.littlelemon.ui.screens.MenuItemViewModel
import com.hekmatullahamin.littlelemon.ui.screens.OrderViewModel
import com.hekmatullahamin.littlelemon.ui.screens.ProfileScreenViewModel
import com.hekmatullahamin.littlelemon.ui.screens.RegisterViewModel
import com.hekmatullahamin.littlelemon.ui.screens.ThemeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
//         Initializer for RegisterViewModel
        initializer {
            RegisterViewModel(
                inventoryApplication().container.userRepository
            )
        }

        initializer {
            LoginViewModel(
                inventoryApplication().container.userRepository
            )
        }

        initializer {
            HomeScreenViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.userRepository,
                inventoryApplication().container.menuItemsRepository
            )
        }

        initializer {
            ProfileScreenViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.userRepository,
                inventoryApplication().applicationContext
            )
        }

        initializer {
            EditProfileViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.userRepository,
                inventoryApplication().container.addressRepository
            )
        }

        initializer {
            AddressViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.addressRepository
            )
        }

        initializer {
            ThemeViewModel(
                inventoryApplication().container.userPreferencesRepository
            )
        }

        initializer {
            MenuItemViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.localMenuItemsRepository
            )
        }

        initializer {
            CheckoutViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.addressRepository
            )
        }

        initializer {
            OrderViewModel(
                inventoryApplication().container.orderRepository
            )
        }
    }
}

fun CreationExtras.inventoryApplication(): LittleLemonApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LittleLemonApplication)