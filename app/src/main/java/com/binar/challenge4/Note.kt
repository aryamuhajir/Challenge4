package com.binar.challenge4

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id : Int?,
    @ColumnInfo (name = "hari")
    var hari : String,
    @ColumnInfo(name = "catatan")
    var catatan : String
) : Parcelable
