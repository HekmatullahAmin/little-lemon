package com.hekmatullahamin.littlelemon.viewmodels

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import com.hekmatullahamin.littlelemon.LittleLemonApplication
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.fake.FakeUserRepository
import com.hekmatullahamin.littlelemon.ui.navigation.ProfileDestination
import com.hekmatullahamin.littlelemon.ui.screens.ProfileScreenViewModel
import org.junit.Before
import org.junit.Test


//TODO: Use mockito for testing this viewmodel class
class ProfileViewModelTest {

    private lateinit var fakeUserRepository: UserRepository
    private lateinit var viewModel: ProfileScreenViewModel

    @Before
    fun setup() {
//        TODO: don't provide context like this
//        val context = ApplicationProvider.getApplicationContext<LittleLemonApplication>()
//        fakeUserRepository = FakeUserRepository()
//        viewModel = ProfileScreenViewModel(
//            savedStateHandle = SavedStateHandle(mapOf(ProfileDestination.userIdArg to 1)),
//            userRepository = fakeUserRepository,
//            context = context
//        )
    }


}
