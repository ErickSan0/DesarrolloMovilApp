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
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity(){
    private lateinit var binding: LoginActivityBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("usuario")
        firebaseAuth = FirebaseAuth.getInstance()


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
    private fun loginUsuarios(correo: String, psw: String) {
        firebaseAuth.signInWithEmailAndPassword(correo, psw).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                if (user != null) {
                    databaseReference.orderByChild("correo").equalTo(correo)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (userSnapshot in dataSnapshot.children) {
                                        val tablabd = userSnapshot.getValue(TablasBD::class.java)
                                        if (tablabd != null) {
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
                                 } else {
                                    Toast.makeText(this@LoginActivity, "El usuario no existe en la base de datos!", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@LoginActivity, "Algo salió mal!: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
            } else {
                Toast.makeText(this@LoginActivity, "Error en la autenticación: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
