package com.example.sistemasoporte.ui.gallery
import com.example.sistemasoporte.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sistemasoporte.Ticket
import com.example.sistemasoporte.databinding.FragmentGalleryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private lateinit var autocompletaestacionr: AutoCompleteTextView
    private lateinit var autocompletarproblema: AutoCompleteTextView
    private lateinit var desctext: EditText
    private lateinit var btngenerar: Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        autocompletaestacionr = root.findViewById(R.id.autocompletarestacion)
        autocompletarproblema = root.findViewById(R.id.autocompletararea)
        desctext = root.findViewById(R.id.desctxt)
        btngenerar = root.findViewById(R.id.btnGenerar)

        databaseReference = FirebaseDatabase.getInstance().reference.child("ticket")
        auth = FirebaseAuth.getInstance()

        btngenerar.setOnClickListener {
            crearTicket()
        }

        val autoCompleteTextView = root.findViewById<AutoCompleteTextView>(R.id.autocompletarestacion)
        val opcionesEstacion = resources.getStringArray(R.array.Estacion)
        val adapterEstacion = ArrayAdapter(requireContext(), R.layout.seleccionarray, opcionesEstacion)
        autoCompleteTextView.setAdapter(adapterEstacion)

        val autoCompleteTextViewArea = root.findViewById<AutoCompleteTextView>(R.id.autocompletararea)
        val opcionesArea = resources.getStringArray(R.array.area)
        val adapterArea = ArrayAdapter(requireContext(), R.layout.seleccionarray, opcionesArea)
        autoCompleteTextViewArea.setAdapter(adapterArea)

        return root
    }

    private fun crearTicket() {
        val desc = desctext.text.toString()
        val estacion = autocompletaestacionr.text.toString()
        val area = autocompletarproblema.text.toString()
        val currentUser = auth.currentUser

        if (desc.isNotEmpty() && estacion.isNotEmpty() && area.isNotEmpty() && currentUser != null) {
            val ticketId = databaseReference.push().key ?: ""

            val ticket = Ticket(desc, estacion, area, ticketId, currentUser.uid, "abierto")

            databaseReference.child(ticketId).setValue(ticket)

            desctext.text.clear()
            autocompletaestacionr.text.clear()
            autocompletarproblema.text.clear()
            Toast.makeText(requireContext(), "Ticket generado exitosamente", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Todos los campos deben estar llenos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
