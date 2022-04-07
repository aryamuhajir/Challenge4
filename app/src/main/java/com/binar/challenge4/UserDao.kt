package com.binar.challenge4

import androidx.room.*

@Dao
interface UserDao {

    @Insert
    fun insertUser(user : User) : Long

    @Query("SELECT * FROM User WHERE nohp = :a")
    fun getUser(a : String) : List<User>




}