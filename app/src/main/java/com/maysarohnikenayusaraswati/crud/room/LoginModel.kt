package com.maysarohnikenayusaraswati.crud.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LoginModel(var name: String, var password:String, var pin:String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
