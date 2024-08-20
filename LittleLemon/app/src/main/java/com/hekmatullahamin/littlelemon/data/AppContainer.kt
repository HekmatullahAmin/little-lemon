package com.hekmatullahamin.littlelemon.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hekmatullahamin.littlelemon.data.repositories.AddressRepository
import com.hekmatullahamin.littlelemon.data.repositories.AddressRepositoryImpl
import com.hekmatullahamin.littlelemon.data.repositories.LocalMenuItemsRepository
import com.hekmatullahamin.littlelemon.data.repositories.LocalMenuItemsRepositoryImpl
import com.hekmatullahamin.littlelemon.data.repositories.MenuItemsRepository
import com.hekmatullahamin.littlelemon.data.repositories.MenuItemsRepositoryImpl
import com.hekmatullahamin.littlelemon.data.repositories.OrderRepository
import com.hekmatullahamin.littlelemon.data.repositories.OrderRepositoryImpl
import com.hekmatullahamin.littlelemon.data.repositories.UserPreferencesRepository
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.data.repositories.UserRepositoryImpl
import com.hekmatullahamin.littlelemon.network.NetworkMenuItemsRepository
import com.hekmatullahamin.littlelemon.network.NetworkMenuItemsRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val userRepository: UserRepository
    val networkMenuItemsRepository: NetworkMenuItemsRepository
    val localMenuItemsRepository: LocalMenuItemsRepository
    val menuItemsRepository: MenuItemsRepository
    val addressRepository: AddressRepository
    val userPreferencesRepository: UserPreferencesRepository
    val orderRepository: OrderRepository
}

class AppContainerImpl(private val context: Context) : AppContainer {
    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl(LittleLemonDatabase.getDatabase(context).userDao())
    }

    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    override val networkMenuItemsRepository: NetworkMenuItemsRepository by lazy {
        NetworkMenuItemsRepositoryImpl(httpClient)
    }

    override val localMenuItemsRepository: LocalMenuItemsRepository by lazy {
        LocalMenuItemsRepositoryImpl(LittleLemonDatabase.getDatabase(context).menuItemDao())
    }

    // New higher-level repository
    override val menuItemsRepository: MenuItemsRepository by lazy {
        MenuItemsRepositoryImpl(networkMenuItemsRepository, localMenuItemsRepository)
    }

    override val addressRepository: AddressRepository by lazy {
        AddressRepositoryImpl(LittleLemonDatabase.getDatabase(context).addressDao())
    }


    private val THEME_PREFERENCE_NAME = "theme_preferences"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = THEME_PREFERENCE_NAME
    )

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.dataStore)
    }

    override val orderRepository: OrderRepository by lazy {
        OrderRepositoryImpl(LittleLemonDatabase.getDatabase(context).orderDao())
    }
}
