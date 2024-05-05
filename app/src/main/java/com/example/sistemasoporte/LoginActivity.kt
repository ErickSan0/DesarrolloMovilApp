package com.example.sistemasoporte
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity(){


    private lateinit var loginbtn: Button //btn login
    private lateinit var usuario: EditText
    private lateinit var contra: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        supportActionBar?.hide()

        //Inicializar vistas
        val registrar: TextView = findViewById(R.id.textView)
        usuario = findViewById(R.id.user)
        contra = findViewById(R.id.psw)
        loginbtn = findViewById(R.id.btnlogin)

        //ss
        registrar.setOnClickListener {
            // Aquí puedes iniciar una nueva actividad o realizar alguna otra acción al hacer clic en el botón
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }


        //acceso segun credenciales al MainActivity
        loginbtn.setOnClickListener{
            val usuarioA = usuario.text.toString()
            val contraA = contra.text.toString()

            if (cuentalocal(usuarioA, contraA)) {
                // Credenciales válidas, abrir MainActivity
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                // Credenciales inválidas, mostrar mensaje de error
                Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cuentalocal(usuario: String, contra: String): Boolean {
        val usuarioLocal = "local"
        val contraLocal = "prueba"
        return usuario == usuarioLocal && contra == contraLocal
    }

}