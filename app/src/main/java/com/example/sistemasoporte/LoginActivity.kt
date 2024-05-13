package com.example.sistemasoporte
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemasoporte.databinding.LoginActivityBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity(){
    private lateinit var binding: LoginActivityBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("usuario")

        binding.btnlogin.setOnClickListener {
            val txtusuario = binding.user.text.toString()
            val txtpsw = binding.psw.text.toString()

            if (txtusuario.isNotEmpty() && txtpsw.isNotEmpty()) {
                    loginUsuarios(txtusuario, txtpsw)
            } else {
                Toast.makeText(this@LoginActivity, "Todos los campos deben estar llenos!", Toast.LENGTH_SHORT).show()
            }
        }

        //codigo tv para reg xd
        val registrar: TextView = findViewById(R.id.textView)
        registrar.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun loginUsuarios(usuario: String, psw: String) {
        databaseReference.orderByChild("usuario").equalTo(usuario).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var usuarioCorrecto = false
                    for (userSnapshot in dataSnapshot.children) {
                        val tablabd = userSnapshot.getValue(TablasBD::class.java)
                        if (tablabd != null && tablabd.psw == psw) {
                            usuarioCorrecto = true
                            Toast.makeText(this@LoginActivity, "Bienvenido", Toast.LENGTH_SHORT).show()
                            val intent = if (tablabd.tipo == TipoCuenta.ADMINISTRADOR) {
                                Intent(this@LoginActivity, AdminMain::class.java)
                            } else {
                                Intent(this@LoginActivity, MainActivity::class.java)
                            }
                                startActivity(intent)
                            finish()
                            return
                        }
                    }
                    if (!usuarioCorrecto) {
                        Toast.makeText(this@LoginActivity, "La contraseña es incorrecta", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "El usuario no existe!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Algo salió mal!: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
