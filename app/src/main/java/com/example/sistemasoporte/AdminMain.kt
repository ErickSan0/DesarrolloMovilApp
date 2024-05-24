package com.example.sistemasoporte
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminMain : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var ticketid: EditText
    private lateinit var actuBtn: Button
    private lateinit var salirbtn: Button
    private lateinit var contenedor: LinearLayout
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private var ticketListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity)

        databaseReference = FirebaseDatabase.getInstance().reference.child("ticket")

        ticketid = findViewById(R.id.ticketIdEditText)
        actuBtn = findViewById(R.id.updateButton)
        salirbtn = findViewById(R.id.salirbtn)
        contenedor = findViewById(R.id.ticketsContainer)

        //sacar estado
        val tipoArray = resources.getStringArray(R.array.tipo)
        val tipoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipoArray)
        val tipoAutoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autocompletarestado)
        tipoAutoCompleteTextView.setAdapter(tipoAdapter)

        // Agregar líneas para el AutoCompleteTextView
        autoCompleteTextView = findViewById(R.id.autocompletarestado)
        val opcionesEstado = resources.getStringArray(R.array.estado)
        val adapterUsuario = ArrayAdapter(this, R.layout.seleccionarray, opcionesEstado)
        autoCompleteTextView.setAdapter(adapterUsuario)


        actuBtn.setOnClickListener {
            val ticketId = ticketid.text.toString()
            val nuevoEstado = autoCompleteTextView.text.toString()
            if (ticketId.isNotEmpty() && nuevoEstado.isNotEmpty()) {
                actualizarEstadoTicket(ticketId, nuevoEstado)
            } else {
                Toast.makeText(this, "Todos los campos deben estar llenos", Toast.LENGTH_SHORT).show()
            }
        }
        salirbtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        mostrarTickets()
    }

    private fun mostrarTickets() {
        ticketListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                contenedor.removeAllViews()
                for (snapshot in dataSnapshot.children) {
                    val ticket = snapshot.getValue(Ticket::class.java)
                    if (ticket != null) {
                        val ticketInfo = """
                            Estación: ${ticket.estacion}
                            Área: ${ticket.area}
                            Descripción: ${ticket.description}
                            Estado: ${ticket.estado}
                        """.trimIndent()

                        val ticketTextView = TextView(this@AdminMain).apply {
                            text = ticketInfo
                            setPadding(10, 10, 10, 10)
                            setTextColor(resources.getColor(android.R.color.white))
                            textSize = 16f
                            setBackgroundResource(R.drawable.borde)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 0, 0, 16)
                            }
                            setOnClickListener {
                                ticketid.setText(ticket.id)
                            }
                        }
                        contenedor.addView(ticketTextView)
                    }
                }
                if (contenedor.childCount == 0) {
                    val emptyTextView = TextView(this@AdminMain).apply {
                        text = "No hay tickets para mostrar"
                        setPadding(10, 10, 10, 10)
                        setTextColor(resources.getColor(android.R.color.black))
                        textSize = 16f
                    }
                    contenedor.addView(emptyTextView)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@AdminMain, "Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        }

        databaseReference.addValueEventListener(ticketListener as ValueEventListener)
    }

    private fun actualizarEstadoTicket(ticketId: String, nuevoEstado: String) {
        databaseReference.child(ticketId).child("estado").setValue(nuevoEstado)
            .addOnSuccessListener {
                Toast.makeText(this, "Estado actualizado exitosamente", Toast.LENGTH_SHORT).show()
                ticketid.text.clear()
                autoCompleteTextView.text.clear()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al actualizar el estado", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        ticketListener?.let { databaseReference.removeEventListener(it) }
    }
}

