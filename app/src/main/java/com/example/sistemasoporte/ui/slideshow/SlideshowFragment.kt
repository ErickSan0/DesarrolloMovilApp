package com.example.sistemasoporte.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sistemasoporte.Ticket
import com.example.sistemasoporte.databinding.FragmentSlideshowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private var ticketListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow

        // Inicializar Firebase
        databaseReference = FirebaseDatabase.getInstance().reference.child("ticket")
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser ?: return root // Verificar si el usuario está autenticado

        // Escuchar cambios en la base de datos y mostrar solo los tickets del usuario actual
        ticketListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tickets = mutableListOf<String>()
                for (snapshot in dataSnapshot.children) {
                    val ticket = snapshot.getValue(Ticket::class.java)
                    if (ticket != null && ticket.id == currentUser.uid) {
                        val ticketInfo =
                            "Estacion: ${ticket.estacion}, Área: ${ticket.area}, Descripción: ${ticket.description}, ID: ${ticket.id}, "
                        tickets.add(ticketInfo)
                    }
                }
                textView.text = if (tickets.isNotEmpty()) {
                    tickets.joinToString("\n\n")
                } else {
                    "No hay tickets para mostrar para este usuario"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores de Firebase
            }
        }

        databaseReference.addValueEventListener(ticketListener as ValueEventListener)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ticketListener?.let { databaseReference.removeEventListener(it) }
        _binding = null
    }
}

