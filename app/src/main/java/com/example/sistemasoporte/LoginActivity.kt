package com.example.sistemasoporte
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity(){

    private lateinit var loginbtn: Button //btn login
    private lateinit var msjbtn: Button //btn contacto
    private lateinit var usuario: EditText
    private lateinit var contra: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        supportActionBar?.hide()

        //Inicializar vistas
        usuario = findViewById(R.id.user)
        contra = findViewById(R.id.psw)
        loginbtn = findViewById(R.id.btnlogin)
        msjbtn = findViewById(R.id.btnprob)

        //Muestra msj, por el momento no esta contemplado q el user se registre
        msjbtn.setOnClickListener {MostrarMensaje()}
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

    private fun MostrarMensaje() {
        val mensaje = "Si tienes problemas para acceder, por favor póngase en contacto con el equipo de sistemas."
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Contacto Sistemas")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Confirmar") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}