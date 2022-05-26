package com.maysarohnikenayusaraswati.crud.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.maysarohnikenayusaraswati.crud.databinding.ActivityDetailLoginBinding
import com.maysarohnikenayusaraswati.crud.room.LoginDatabase
import com.maysarohnikenayusaraswati.crud.room.LoginModel
import java.util.concurrent.Executors

class DetailLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLoginBinding
    private lateinit var database: LoginDatabase
    var idLogin = 0
    var name = ""
    var password = ""
    var pin = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //TODO Menerima data yang dibawa dari LoginAdapter
        database = LoginDatabase.getDatabase(this)
        idLogin = intent.getIntExtra("KEY_ID", 0)
        name = intent.getStringExtra("KEY_USERNAME") ?: ""
        password = intent.getStringExtra("KEY_PASSWORD") ?: ""
        pin = intent.getStringExtra("KEY_PIN") ?: ""

        binding.detailName.text = name
        binding.detailPassword.text = password
        binding.detailPin.text = pin

        Log.d("name", name)

    }

    fun deleteData(view: View) {
        val loginData = LoginModel(
            binding.detailName.text.toString(),
            binding.detailPassword.text.toString(),
            binding.detailPin.text.toString()
        ).apply {
            id = idLogin
        }
        //database tidak bisa diakses langsung di main thread utama
        Executors.newSingleThreadExecutor().execute {
            //TODO membuat proses menghapus data loginData
            database.loginDao().delete(loginData)
            //Ketika data berhasil terdelete akan pindah ke halaman MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.runOnUiThread(Runnable {
                Toast.makeText(this, "Data Berhasil di Hapus", Toast.LENGTH_SHORT).show()
            })
            Log.d("tes delete", loginData.toString())
        }
    }

    fun editDetail(view: View) {
        val intent = Intent(this, EditLoginActivity::class.java).apply {
            putExtra("KEY_ID", idLogin)
            putExtra("KEY_USERNAME", name)
            putExtra("KEY_PASSWORD", password)
            putExtra("KEY_PIN", pin)
        }
        startActivity(intent)
        finish()
    }
}