package com.example.sistemasoporte
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemasoporte.databinding.RegistroActivityBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: RegistroActivityBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistroActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("usuarios")

        binding.btnregistrar.setOnClickListener {
            val txtusuario = binding.txtnombre.text.toString()
            val txtcorreo = binding.txtemail.text.toString()
            val txtpsw = binding.txtpsw.text.toString()
            val txtcpsw = binding.txtcpsw.text.toString()

            if (txtusuario.isNotEmpty() && txtcorreo.isNotEmpty() && txtpsw.isNotEmpty() && txtcpsw.isNotEmpty()) {
                if (txtpsw == txtcpsw) {
                    registrarUsuarios(txtusuario, txtpsw, txtcorreo)
                } else {
                    Toast.makeText(this@RegistroActivity, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@RegistroActivity, "Todos los campos deben estar llenos", Toast.LENGTH_SHORT).show()
            }
        }
        binding.textView.setOnClickListener {
            startActivity(Intent(this@RegistroActivity, LoginActivity::class.java))
            finish()
        }
    }
    private fun registrarUsuarios(usuario: String, psw: String,  correo: String){
        databaseReference.orderByChild("usuario").equalTo(usuario).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()){
                    val id = databaseReference.push().key
                    val tablasBD = TablasBD(usuario, psw, correo, id)
                    databaseReference.child(id!!).setValue(tablasBD)
                    Toast.makeText(this@RegistroActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegistroActivity, LoginActivity::class.java))
                } else {
                    Toast.makeText(this@RegistroActivity, "El usuario ya existe!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegistroActivity, "Algo salio mal!: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

}
