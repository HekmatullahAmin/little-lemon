package com.hekmatullahamin.littlelemon.ui.screens

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.ui.navigation.ProfileDestination
import com.hekmatullahamin.littlelemon.utils.Constants
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

//TODO: Use another methods like hilt for passing context
class ProfileScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val context: Context
) : ViewModel() {
    private val userId: Int = checkNotNull(savedStateHandle[ProfileDestination.userIdArg])
    val user: StateFlow<User> =
        userRepository.getUserStreamUsingId(userId)
            .filterNotNull()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
                initialValue = User()
            )

    private fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val fileName = "profile_image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        inputStream.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
    }

    fun updateUserImageUri(user: User, uri: Uri) {
        viewModelScope.launch {
            val filePath = saveImageToInternalStorage(context, uri)
            filePath?.let {
                val updatedUser = user.copy(userImageUri = it)
                userRepository.updateUser(updatedUser)
            }
        }
    }
}