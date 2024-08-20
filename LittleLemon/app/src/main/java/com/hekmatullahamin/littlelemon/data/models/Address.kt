package com.hekmatullahamin.littlelemon.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "address",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
//        If a user is deleted, all their associated addresses will also be deleted automatically.
        onDelete = ForeignKey.CASCADE
    )]
)
data class Address(
    @PrimaryKey(autoGenerate = true)
    val addressId: Int = 0,
    @ColumnInfo(name = "user_id")
    val userId: Int? = null, //Foreign key column
    @ColumnInfo(name = "address_line_1")
    val addressLineOne: String = "",
    @ColumnInfo(name = "address_line_2")
    val addressLineTwo: String = "",
    val city: String = "",
    val country: String = "",
    @ColumnInfo("postal_code")
    val postalCode: String = "",
    @ColumnInfo("is_default")
    val isDefault: Boolean = false
)
