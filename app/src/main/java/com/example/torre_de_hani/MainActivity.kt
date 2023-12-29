package com.example.torre_de_hani

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.torre_de_hani.databinding.ActivityMainBinding
import com.example.torre_de_hani.databinding.StyleDialogNumDiscosBinding


class MainActivity : AppCompatActivity() {

    //criando pilhas
    private val torre1 = ArrayDeque(listOf(10))
    private val torre2 = ArrayDeque(listOf(10))
    private val torre3 = ArrayDeque(listOf(10))

    //definidado pelo nome do xml + Binding no final
    private lateinit var binding: ActivityMainBinding

    var numDisco=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //deita a tela
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        //abre dialog
        showDialog_SetNumDiscos(this)

        val idViewtorre1: LinearLayout = findViewById(R.id.ViewTorre1)
        val idViewtorre2: LinearLayout = findViewById(R.id.ViewTorre2)
        val idViewtorre3: LinearLayout = findViewById(R.id.ViewTorre3)

        val arraySendblock = HashMap<String, Int>()
        var torreSeletecRement = 0

        if (numDisco>7){
            val torreHeight= when(numDisco){
                8->260
                9->400
                else->100
            }
            binding.viewBGTorre.layoutParams = LinearLayout.LayoutParams(R.dimen.area_torres, torreHeight)
        }

        idViewtorre1.setOnClickListener {
            if (torreSeletecRement == 0) {
                arraySendblock["Rementente"] = 1 //passa o id da torre que envia
                torreSeletecRement++             //roda para definir destinatario
            } else {
                arraySendblock["Destinatario"] = 1 //passa o id da torre que recebe
                torreSeletecRement = 0

                val valuesRementente = arraySendblock["Rementente"]
                val valuesDestinatario = arraySendblock["Destinatario"]

                if (valuesRementente != valuesDestinatario) {
                    tranfereBloco(
                        getArrayElemto(valuesRementente),
                        getArrayElemto(valuesDestinatario),
                        getIdElemto(valuesRementente),
                        getIdElemto(valuesDestinatario)
                    )
                }


            }
        }

        idViewtorre2.setOnClickListener {
            if (torreSeletecRement == 0) {
                arraySendblock["Rementente"] = 2
                torreSeletecRement++
            } else {
                arraySendblock["Destinatario"] = 2
                torreSeletecRement = 0

                val valuesRementente = arraySendblock["Rementente"]
                val valuesDestinatario = arraySendblock["Destinatario"]

                if (valuesRementente != valuesDestinatario) {
                    tranfereBloco(
                        getArrayElemto(valuesRementente),
                        getArrayElemto(valuesDestinatario),
                        getIdElemto(valuesRementente),
                        getIdElemto(valuesDestinatario)
                    )
                }
            }
        }

        idViewtorre3.setOnClickListener {
            if (torreSeletecRement == 0) {
                arraySendblock["Rementente"] = 3
                torreSeletecRement++
            } else {
                arraySendblock["Destinatario"] = 3
                torreSeletecRement = 0

                val valuesRementente = arraySendblock["Rementente"]
                val valuesDestinatario = arraySendblock["Destinatario"]

                if (valuesRementente != valuesDestinatario) {
                    tranfereBloco(
                        getArrayElemto(valuesRementente),
                        getArrayElemto(valuesDestinatario),
                        getIdElemto(valuesRementente),
                        getIdElemto(valuesDestinatario)
                    )
                }
            }
        }

    }

    //-----------------------dialog-----------------\\
    fun showDialog_SetNumDiscos(context: Context?) {
        //define tamanho
        val width = (resources.displayMetrics.widthPixels * 0.70).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.75).toInt()

        val dialog = Dialog(context!!)
        val dialogBinding: StyleDialogNumDiscosBinding = StyleDialogNumDiscosBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_border_radius)
        dialog.window?.setLayout(width, height)
        dialog.setCancelable(false)
        dialog.show()

        //recebe valor e convert em int
        val txt_numDisco: String = dialogBinding.txtViewNumDiscos.getText().toString()
        numDisco = txt_numDisco.toInt()

        dialogBinding.btnMaisDiscos.setOnClickListener {
            if (numDisco in 3..8){
                // mais um disco
                numDisco++
                dialogBinding.txtViewNumDiscos.setText(numDisco.toString())
            }
       }

        dialogBinding.btnMenosDiscos.setOnClickListener {
            if (numDisco in 4..9){
                //menos um disco
                numDisco--
                dialogBinding.txtViewNumDiscos.setText(numDisco.toString())
            }
        }

        dialogBinding.btnIniciaJogo.setOnClickListener {
            dialog.dismiss()
            while (numDisco>=1){
                torre1.addLast(numDisco)
                numDisco--
            }

            //chama funçao para mostrar os blocos
            for (value in torre1.reversed()) {
                binding.ViewTorre1.addView(dataBlocks(value, this))
            }
        }
    }

    private fun getIdElemto(id: Int?): LinearLayout {
        val idViewtorre1: LinearLayout = findViewById(R.id.ViewTorre1)
        val idViewtorre2: LinearLayout = findViewById(R.id.ViewTorre2)
        val idViewtorre3: LinearLayout = findViewById(R.id.ViewTorre3)

        when (id) {
            1 -> return idViewtorre1
            2 -> return idViewtorre2
            3 -> return idViewtorre3
            else -> {
                print("erro: Numero não LinearLayout")
            }

        }
        return idViewtorre1
    }

    private fun getArrayElemto(id: Int?): ArrayDeque<Int> {
        when (id) {
            1 -> return torre1
            2 -> return torre2
            3 -> return torre3
            else -> {
                print("erro: Numero não é array")
            }

        }
        return torre1
    }

    private fun tranfereBloco(
        remetente: ArrayDeque<Int>,
        destinatario: ArrayDeque<Int>,
        idRemetente: LinearLayout,
        idDestinatario: LinearLayout
    ) {
        //evita view vazia
        if (remetente.isEmpty()) {
            remetente.addLast(0)
        }

        val valueBlock = remetente.last()
        Log.d("ultimo itens", valueBlock.toString())
        Log.d("remetentes", remetente.toString())
        Log.d("destinatario", destinatario.toString())

        if(remetente.last() != 10) {
            if (valueBlock < destinatario.last()) {
                val value = remetente.removeLast()//remove do remetente
                destinatario.addLast(value)//add no destinatario

                //recarrega
                idRemetente.removeAllViewsInLayout()
                idDestinatario.removeAllViewsInLayout()

                //atualiza blocos
                for (valueRemetente in remetente.reversed()) {
                    idRemetente.addView(dataBlocks(valueRemetente, this))
                }

                for (valueDestinatario in destinatario.reversed()) {
                    idDestinatario.addView(dataBlocks(valueDestinatario, this))
                }

                verificaViroria()
            } else {
                Toast.makeText(this, "Movimento Não Permitido", LENGTH_LONG).show()
            }
        }
    }

    fun verificaViroria(): Boolean {
        if (torre2.size == 6 || torre3.size == 6) {
            Toast.makeText(this, "Vitoriaaa", LENGTH_LONG).show()
            return true
        }
        return false
    }


    private fun dataBlocks(numBlock: Int, context: Context): TextView {
        val bloco = TextView(context)
        bloco.textAlignment = View.TEXT_ALIGNMENT_CENTER

        val blocoWidth = (numBlock * 70)
        bloco.layoutParams = LinearLayout.LayoutParams(blocoWidth, 50)
        bloco.text = numBlock.toString()
        bloco.setBackgroundResource(R.drawable.border_radius_disk)

        if (numBlock==10){
            bloco.layoutParams = LinearLayout.LayoutParams(0, 0)
        }

        //TODO: Colocar style
        val colorDisco = when (numBlock) {
           10 -> "#333333"//defoult

            1 -> "#FF3074"

            2 -> "#3128E8"

            3 -> "#23FFC9"

            4 -> "#B2E808"

            5 -> "#FFA11E"

            6 -> "#645B2E"

            7 -> "#645B2E"

            8 -> "#645B2E"

            9 -> "#645B2E"
             

            else -> { // Note the block
                print("erro:Numero não existente")
            }

        }
        //define cor
        bloco.backgroundTintList = ColorStateList.valueOf(Color.parseColor(colorDisco.toString()));

        return bloco
    }
}




