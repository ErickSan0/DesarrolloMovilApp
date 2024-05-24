package com.example.sistemasoporte.ui.slideshow
import android.os.Bundle
import com.example.sistemasoporte.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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

        val ContenedorTicket: LinearLayout = binding.contenedor

        databaseReference = FirebaseDatabase.getInstance().reference.child("ticket")
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser ?: return root

        ticketListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                ContenedorTicket.removeAllViews()
                for (snapshot in dataSnapshot.children) {
                    val ticket = snapshot.getValue(Ticket::class.java)
                    if (ticket != null && ticket.usuarioID == currentUser.uid) {
                        val ticketInfo = """
                            Estación: ${ticket.estacion}
                            Área: ${ticket.area}
                            Descripción: ${ticket.description}
                            Estado: ${ticket.estado}
                        """.trimIndent()

                        val ticketTextView = TextView(requireContext()).apply {
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
                        }
                        ContenedorTicket.addView(ticketTextView)
                    }
                }
                if (ContenedorTicket.childCount == 0) {
                    val emptyTextView = TextView(requireContext()).apply {
                        text = "No hay tickets para mostrar para este usuario"
                        setPadding(10, 10, 10, 10)
                        setTextColor(resources.getColor(android.R.color.white))
                        textSize = 16f
                    }
                    ContenedorTicket.addView(emptyTextView)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
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