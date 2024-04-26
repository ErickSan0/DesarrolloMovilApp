package com.example.sistemasoporte.ui.gallery


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sistemasoporte.R
import com.example.sistemasoporte.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private lateinit var imageView: ImageView
    private lateinit var autocompletaestacionr: AutoCompleteTextView
    private lateinit var autocompletarproblema: AutoCompleteTextView
    private lateinit var desctext: EditText
    private lateinit var btngenerar: Button

    private val PICK_IMAGE = 1

    private val galleryViewModel: GalleryViewModel by lazy {
        ViewModelProvider(this).get(GalleryViewModel::class.java)

    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        autocompletaestacionr = root.findViewById(R.id.autocompletarestacion)
        autocompletarproblema =  root.findViewById(R.id.autocompletararea)
        desctext = root.findViewById(R.id.desctxt)
        btngenerar =  root.findViewById(R.id.btnGenerar)

        btngenerar.setOnClickListener {
            val desc = desctext.toString()
            val estacion = autocompletaestacionr.toString()
            val area = autocompletarproblema.toString()


        }
        // Código opciones estación
        val autoCompleteTextView = root.findViewById<AutoCompleteTextView>(R.id.autocompletarestacion)
        val opcionesEstacion = resources.getStringArray(R.array.Estacion)
        val adapterEstacion = ArrayAdapter(requireContext(), R.layout.seleccionarray, opcionesEstacion)
        autoCompleteTextView.setAdapter(adapterEstacion)

        // Código opciones área
        val autoCompleteTextViewArea = root.findViewById<AutoCompleteTextView>(R.id.autocompletararea)
        val opcionesArea = resources.getStringArray(R.array.area)
        val adapterArea = ArrayAdapter(requireContext(), R.layout.seleccionarray, opcionesArea)
        autoCompleteTextViewArea.setAdapter(adapterArea)
        // Código imagen
        imageView = root.findViewById(R.id.imgSelected)

        val btnSeleccionarImagen = root.findViewById<Button>(R.id.selectimg)
        btnSeleccionarImagen.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE)
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}