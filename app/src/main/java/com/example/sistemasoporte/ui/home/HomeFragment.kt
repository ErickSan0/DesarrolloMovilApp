package com.example.sistemasoporte.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sistemasoporte.TablasBD
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.example.sistemasoporte.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Referencia a la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().reference.child("usuario")

        // Obtener el usuario actualmente logueado
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        // Verificar si el usuario está autenticado
        if (currentUser != null) {
            // Consultar la base de datos de Firebase para obtener los datos del usuario actual
            databaseReference.orderByChild("correo").equalTo(currentUser.email).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Verificar si se encontraron datos
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val usuario = userSnapshot.getValue(TablasBD::class.java)
                            if (usuario != null) {
                                // Obtener los datos del usuario
                                val username = usuario.usuario
                                val email = usuario.correo

                                // Actualizar los TextViews en el layout con la información del usuario
                                binding.usernameText.text = "Nombre de usuario: $username"
                                binding.correo.text = "Correo electrónico: $email"
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar errores de la base de datos
                }
            })
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
