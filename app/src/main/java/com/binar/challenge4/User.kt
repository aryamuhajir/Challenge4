package com.binar.challenge4

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true)
    var id : Int?,
    @ColumnInfo(name = "nohp")
    var nohp : String,
    @ColumnInfo(name = "email")
    var email : String,
    @ColumnInfo(name = "nama")
    var nama : String,
    @ColumnInfo(name = "password")
    var password : String

) : Parcelable
