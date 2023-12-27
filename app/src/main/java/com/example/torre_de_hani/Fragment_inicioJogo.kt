package com.example.torre_de_hani

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class Fragment_inicioJogo : Fragment() {
    lateinit var txtViewNumDiscos:TextView
    var discos=6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_inicio_jogo, container, false)

        txtViewNumDiscos= view.findViewById(R.id.txtViewNumDiscos)
        txtViewNumDiscos.setText(discos.toString())

        val btn_menosDiscos:ImageButton = view.findViewById(R.id.btn_menosDiscos)
        btn_menosDiscos.setOnClickListener {
            menosDisco()
        }

        val btn_maisDiscos:ImageButton= view.findViewById(R.id.btn_maisDiscos)
        btn_maisDiscos.setOnClickListener {
            maisDisco()
        }

        val btnIniciaJogo: AppCompatButton = view.findViewById(R.id.btnIniciaJogo)
        btnIniciaJogo.setOnClickListener {
            iniciaJogo()
        }

        return view
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            Fragment_inicioJogo().apply {

            }
    }

    fun menosDisco(){
        if (discos in 5..9){
            Log.d("Discos", "Permitido")
            discos--
            txtViewNumDiscos.setText(discos.toString())
        }
        else{
            Log.d("Discos", "Não Permitido")

        }

    }

    fun maisDisco(){
        if (discos in 4..8){
            Log.d("Discos", "Permitido")
            discos++
            txtViewNumDiscos.setText(discos.toString())
        }
        else{
            Log.d("Discos", "Não Permitido")
        }
    }

    fun iniciaJogo(){
//        val intent = Intent(context, MainActivity::class.java)
//        intent.putExtra("numDiscos", discos)
//        context?.startActivity(intent)

        activity?.finish()
    }
}