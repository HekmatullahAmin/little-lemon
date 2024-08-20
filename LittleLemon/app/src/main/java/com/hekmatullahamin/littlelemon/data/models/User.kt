package com.hekmatullahamin.littlelemon.data.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hekmatullahamin.littlelemon.utils.Constants

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val userId: Int = 0,
    @ColumnInfo(name = "first_name")
    val userFirstName: String = "",
    @ColumnInfo(name = "last_name")
    val userLastName: String = "",
    @ColumnInfo(name = "date_of_birth")
    val userDateOfBirth: Long = 0L,
    @ColumnInfo(name = "email_address")
    val userEmailAddress: String = "",
    @ColumnInfo("password")
    val userPassword: String = "",
    @ColumnInfo(name = "image_uri")
    val userImageUri: String? = null
)
