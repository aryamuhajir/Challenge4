package com.binar.challenge4

import androidx.room.*

@Dao
interface UserDao {

    @Insert
    fun insertUser(user : User) : Long

    @Query("SELECT nohp FROM User WHERE User.nohp = :nohp AND User.password = :password")
    fun cekUser(nohp: String, password : String) : String




}