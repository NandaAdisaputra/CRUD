package com.maysarohnikenayusaraswati.crud.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LoginDao {
    @Insert
    fun insertData(loginData: LoginModel)

    @Query("SELECT * FROM LoginModel")
    fun getAll(): LiveData<List<LoginModel>>

    @Update
    fun update(loginData: LoginModel)

    @Delete
    fun delete(loginData: LoginModel)
}


