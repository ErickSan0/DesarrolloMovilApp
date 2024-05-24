package com.example.sistemasoporte
import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Toast
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemasoporte.databinding.RegistroActivityBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.auth.FirebaseAuth


class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: RegistroActivityBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistroActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("usuario")
        firebaseAuth = FirebaseAuth.getInstance()

        val tipoArray = resources.getStringArray(R.array.tipo)
        val tipoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipoArray)
        val tipoAutoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autocompletarcuenta)
        tipoAutoCompleteTextView.setAdapter(tipoAdapter)

        // Agregar líneas para el AutoCompleteTextView de la cuenta
        autoCompleteTextView = findViewById(R.id.autocompletarcuenta)
        val opcionesUsuario = resources.getStringArray(R.array.tipo)
        val adapterUsuario = ArrayAdapter(this, R.layout.seleccionarray, opcionesUsuario)
        autoCompleteTextView.setAdapter(adapterUsuario)


        binding.btnregistrar.setOnClickListener {
            val txtusuario = binding.txtnombre.text.toString()
            val txtcorreo = binding.txtemail.text.toString()
            val txtpsw = binding.txtpsw.text.toString()
            val txtcpsw = binding.txtcpsw.text.toString()

            if (txtusuario.isNotEmpty() && txtcorreo.isNotEmpty() && txtpsw.isNotEmpty() && txtcpsw.isNotEmpty()) {
                if (txtpsw == txtcpsw) {
                    val tipoSeleccionado = tipoAutoCompleteTextView.text.toString()
                    val tipoArray = resources.getStringArray(R.array.tipo)
                    val tipoCuenta = when (tipoSeleccionado) {
                        tipoArray[0] -> TipoCuenta.USUARIO
                        tipoArray[1] -> TipoCuenta.ADMINISTRADOR
                        else -> TipoCuenta.USUARIO
                    }
                    registrarUsuarios(txtusuario, txtpsw, txtcorreo, tipoCuenta)
                }
                    else {
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
    private fun registrarUsuarios(usuario: String, psw: String, correo: String, tipoCuenta: TipoCuenta) {
        firebaseAuth.createUserWithEmailAndPassword(correo, psw).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                val id = user?.uid
                val tablasBD = TablasBD(usuario, psw, correo, tipoCuenta, id)
                if (id != null) {
                    databaseReference.child(id).setValue(tablasBD).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@RegistroActivity,
                                "Registro exitoso",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@RegistroActivity, LoginActivity::class.java))
                        } else {
                            Toast.makeText(
                                this@RegistroActivity,
                                "Error al guardar los datos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this@RegistroActivity,
                    "Error en la autenticación: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
