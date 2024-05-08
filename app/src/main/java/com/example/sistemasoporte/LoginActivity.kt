package com.example.sistemasoporte
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(){

    private lateinit var loginbtn: Button
    private lateinit var usuario: EditText
    private lateinit var contra: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        supportActionBar?.hide()

        usuario = findViewById(R.id.user)
        contra = findViewById(R.id.psw)
        loginbtn = findViewById(R.id.btnlogin)

        //codigo tv para reg xd
        val registrar: TextView = findViewById(R.id.textView)
        registrar.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        //codigo iniciosesion
        loginbtn.setOnClickListener{
            val usuarioA = usuario.text.toString()
            val contraA = contra.text.toString()

            if (usuarioA.isNotEmpty() && contraA.isNotEmpty()) {
                // Aqui se verifican la cuenta en firebase por medio de correo y psw
                FirebaseAuth.getInstance().signInWithEmailAndPassword(usuarioA, contraA)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Si la cuenta es correcta se abre mainactivity
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // si presenta un problema de validacion muestra este mensaje
                            Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // si hay campos vacios...
                Toast.makeText(this@LoginActivity, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
