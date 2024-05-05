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

        val registrar: TextView = findViewById(R.id.textView)
        registrar.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        loginbtn.setOnClickListener{
            val usuarioA = usuario.text.toString()
            val contraA = contra.text.toString()

            if (usuarioA.isNotEmpty() && contraA.isNotEmpty()) {
                // Verificar credenciales con Firebase
                FirebaseAuth.getInstance().signInWithEmailAndPassword(usuarioA, contraA)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Si el inicio de sesión es exitoso, abrir MainActivity
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // Si hay un error, mostrar un mensaje de error
                            Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Si los campos están vacíos, mostrar un mensaje
                Toast.makeText(this@LoginActivity, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
