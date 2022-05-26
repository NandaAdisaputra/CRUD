package com.nandaadisaputra.crud.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import com.nandaadisaputra.crud.R
import com.nandaadisaputra.crud.adapter.LoginAdapter
import com.nandaadisaputra.crud.databinding.ActivityMainBinding
import com.nandaadisaputra.crud.room.LoginDatabase
import com.nandaadisaputra.crud.room.LoginModel
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: LoginDatabase
    var idLogin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //TODO Kita inisialisasi database nya
        database = LoginDatabase.getDatabase(this)
    }

    //TODO Buatlah logic switch ketika switch ada aksi klik
    fun buttonSwitch(view: View) {
        if (binding.switchDarkMode.isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun deleteData(view: View) {
        binding.addUsername.text?.clear()
        binding.addPassword.text?.clear()
        binding.addPin.text?.clear()
        this.runOnUiThread(Runnable {
            Toast.makeText(
                this,
                "inputan yang Anda masukkan telah  berhasil di hapus",
                Toast.LENGTH_LONG
            ).show()
        })
    }

    fun saveData(view: View?) {
        if (!validation()) {
            return
        }
        //TODO Membuat kondisi ketika inputan tidak kosong
        if (binding.addUserNameTextInputLayout.isNotEmpty() && binding.addPasswordInputLayout.isNotEmpty() && binding.addPinInputLayout.isNotEmpty()) {
            val loginData = LoginModel(
                binding.addUsername.text.toString(),
                binding.addPassword.text.toString(),
                binding.addPin.text.toString()
            )
            //database tidak bisa diakses langsung di main thread utama
            //TODO Kita perlu Executors agar dapat dieksekusi di tempat yang berbeda diluar Activity
            Executors.newSingleThreadExecutor().execute {
                //TODO membuat proses menyimpan data login
                database.loginDao().insertData(loginData)
            }
            this.runOnUiThread(Runnable {
                Toast.makeText(this, "Data Berhasil di Simpan", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            })
            view?.let { viewData(it) }
            Log.d("tes", loginData.toString())
        }
    }

    private fun validation(): Boolean {
        //Cek username kosong atau tidak
        if (binding.addUsername.text.toString().isBlank()) {
            binding.addUsername.error = "Username Tidak Boleh Kosong"
            binding.addUsername.requestFocus()
            return false
        }

        //Cek password kosong atau tidak
        if (binding.addPassword.text.toString().isBlank()) {
            binding.addPassword.error = " Password Tidak Boleh Kosong"
            binding.addPassword.requestFocus()
            return false
        }
        //Cek pin kosong atau tidak
        if (binding.addPin.text.toString().isBlank()) {
            binding.addPin.error = " Pin Tidak Boleh Kosong"
            binding.addPin.requestFocus()
            return false
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === R.id.action_change_settings) {
            val mIntent = Intent(ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun viewData(view: View) {
        //TODO Kita akan menampilkan data yang disimpan ke dalam adapter berupa list data
        database.loginDao().getAll().observe(this, {
            binding.recyclerUser.adapter = LoginAdapter(it)
            binding.recyclerUser.layoutManager = LinearLayoutManager(this)
            this.runOnUiThread(Runnable {
                Toast.makeText(
                    this,
                    "Silahkan klik salah satu item untuk menuju detail login",
                    Toast.LENGTH_LONG
                ).show()
            })
        })
    }
}