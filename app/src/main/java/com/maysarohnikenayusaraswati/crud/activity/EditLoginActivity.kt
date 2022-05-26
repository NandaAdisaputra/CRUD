package com.maysarohnikenayusaraswati.crud.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
import com.maysarohnikenayusaraswati.crud.databinding.ActivityEditLoginBinding
import com.maysarohnikenayusaraswati.crud.room.LoginDatabase
import com.maysarohnikenayusaraswati.crud.room.LoginModel
import java.util.concurrent.Executors

class EditLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditLoginBinding
    private lateinit var database: LoginDatabase
    var idLogin = 0
    var name = ""
    var password = ""
    var pin = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        idLogin = intent.getIntExtra("KEY_ID", 0)
        name = intent.getStringExtra("KEY_USERNAME") ?: ""
        password = intent.getStringExtra("KEY_PASSWORD") ?: ""
        pin = intent.getStringExtra("KEY_PIN") ?: ""

        Log.d("name", name)

        binding.editUsername.setText(name)
        binding.editPassword.setText(password)
        binding.editPin.setText(pin)

        //TODO Kita inisialisasi database nya
        database = LoginDatabase.getDatabase(this)
    }

    fun editData(view: View?) {
        if (!validation()){
            return
        }
        //TODO Membuat kondisi ketika inputan tidak kosong
        if (binding.editUserNameTextInputLayout.isNotEmpty() && binding.editPasswordInputLayout.isNotEmpty() && binding.editPinInputLayout.isNotEmpty()) {
            //TODO mendeklarasikan variabel bioData
            val loginData = LoginModel(
                binding.editUsername.text.toString(),
                binding.editPassword.text.toString(),
                binding.editPin.text.toString()
            ).apply {
                id = idLogin
            }
            //database tidak bisa diakses langsung di main thread utama
            //TODO Kita perlu Executors agar dapat dieksekusi di tempat yang berbeda diluar EditBiodataActivity
            Executors.newSingleThreadExecutor().execute {
                //TODO membuat proses menyimpan data bioData
                database.loginDao().update(loginData)
                this.runOnUiThread(Runnable {
                    Toast.makeText(this, "Data Berhasil di Update", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                })
            }
        }
    }

    private fun validation(): Boolean {
        //Cek username kosong atau tidak
        if (binding.editUsername.text.toString().isBlank()){
            binding.editUsername.error = "Username Tidak Boleh Kosong"
            binding.editUsername.requestFocus()
            return false
        }

        //Cek password kosong atau tidak
        if (binding.editPassword.text.toString().isBlank()){
            binding.editPassword.error = " Password Tidak Boleh Kosong"
            binding.editPassword.requestFocus()
            return false
        }
        //Cek pin kosong atau tidak
        if (binding.editPin.text.toString().isBlank()){
            binding.editPin.error = " Pin Tidak Boleh Kosong"
            binding.editPin.requestFocus()
            return false
        }
        return true
    }

    fun deleteData(view: View) {
        val loginData = LoginModel(binding.editUsername.text.toString(), binding.editPassword.text.toString(), binding.editPin.text.toString()).apply {
            id = idLogin
        }
        //database tidak bisa diakses langsung di main thread utama
        Executors.newSingleThreadExecutor().execute {
            //TODO membuat proses menghapus data loginData
            database.loginDao().delete(loginData)
            this.runOnUiThread(Runnable {
                Toast.makeText(this, "Data Berhasil di Hapus", Toast.LENGTH_SHORT).show()
                val intent2 = Intent(this, MainActivity::class.java)
                startActivity(intent2)
            })
            Log.d("tes delete", loginData.toString())
        }
    }
}
