package com.example.sistemasoporte

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PresentacionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.presentacion_activity)

        val btnAcceder = findViewById<Button>(R.id.btnacceder)
        btnAcceder.setOnClickListener {
            val intent = Intent(this@PresentacionActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    fun onButtonClick(view: View) {
    }
}