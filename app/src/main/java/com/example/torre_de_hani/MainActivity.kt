package com.example.torre_de_hani

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.fragment.app.FragmentContainerView


class MainActivity : AppCompatActivity() {

    //criando pilhas
    private val torre1 = ArrayDeque(listOf(10,5,4,3,2,1))
    private val torre2 = ArrayDeque(listOf(10))
    private val torre3 = ArrayDeque(listOf(10))


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //deita a tela
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val fragmentInicioJogo: FragmentContainerView = findViewById(R.id.fragmentInicioJogo)

        val numDiscos = 3
        //val numDiscos = intent.getIntExtra("numDiscos", 0)
        Log.d("numDiscos", numDiscos.toString())
        if (numDiscos!=0){
            fragmentInicioJogo.isGone
        }else{
            fragmentInicioJogo.isVisible
        }



//        //recebe o tamanho da tela
//        val displayMetrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(displayMetrics)
//        val height = displayMetrics.heightPixels* 0.10
//        val width = displayMetrics.widthPixels * 0.20
//
//        //define as margens da fragmente
//        val layoutParams = fragmentInicioJogo.layoutParams as ConstraintLayout.LayoutParams
//        layoutParams.setMargins(width.toInt(), height.toInt(), width.toInt(), height.toInt())
//        fragmentInicioJogo.layoutParams = layoutParams

        val idViewtorre1:LinearLayout = findViewById(R.id.ViewTorre1)
        val idViewtorre2:LinearLayout = findViewById(R.id.ViewTorre2)
        val idViewtorre3:LinearLayout = findViewById(R.id.ViewTorre3)

        //inverte a ordem
        for (value in torre1.reversed()){
            idViewtorre1.addView(dataBlocks(value,this))
        }

        val arraySendblock = HashMap<String, Int>()
        var torreSeletecRement=0
        

        idViewtorre1.setOnClickListener {
            if ( torreSeletecRement==0){
                arraySendblock["Rementente"] = 1 //passa o id da torre que envia
                torreSeletecRement++             //roda para definir destinatario
            }else{
                arraySendblock["Destinatario"] = 1 //passa o id da torre que recebe
                torreSeletecRement=0

                val valuesRementente= arraySendblock["Rementente"]
                val valuesDestinatario= arraySendblock["Destinatario"]

                if(valuesRementente!=valuesDestinatario){
                    tranfereBloco(getArrayElemto(valuesRementente), getArrayElemto(valuesDestinatario), getIdElemto(valuesRementente), getIdElemto(valuesDestinatario))
                }


            }
        }
        
        idViewtorre2.setOnClickListener {
            if ( torreSeletecRement==0){
                arraySendblock["Rementente"] = 2
                torreSeletecRement++
            }else{
                arraySendblock["Destinatario"] = 2
                torreSeletecRement=0

                val valuesRementente= arraySendblock["Rementente"]
                val valuesDestinatario= arraySendblock["Destinatario"]

                if(valuesRementente!=valuesDestinatario){
                    tranfereBloco(getArrayElemto(valuesRementente), getArrayElemto(valuesDestinatario), getIdElemto(valuesRementente), getIdElemto(valuesDestinatario))
                }
            }
        }
        
        idViewtorre3.setOnClickListener {
            if ( torreSeletecRement==0){
                arraySendblock["Rementente"] = 3
                torreSeletecRement++
            }else{
                arraySendblock["Destinatario"] = 3
                torreSeletecRement=0

                val valuesRementente= arraySendblock["Rementente"]
                val valuesDestinatario= arraySendblock["Destinatario"]

                if(valuesRementente!=valuesDestinatario){
                    tranfereBloco(getArrayElemto(valuesRementente), getArrayElemto(valuesDestinatario), getIdElemto(valuesRementente), getIdElemto(valuesDestinatario))
                }
            }
        }

    }


    private fun getIdElemto(id: Int?): LinearLayout {
        val idViewtorre1:LinearLayout = findViewById(R.id.ViewTorre1)
        val idViewtorre2:LinearLayout = findViewById(R.id.ViewTorre2)
        val idViewtorre3:LinearLayout = findViewById(R.id.ViewTorre3)

        when(id){
            1 ->  return idViewtorre1
            2 ->  return idViewtorre2
            3 ->  return idViewtorre3
            else -> {
                print("erro: Numero não LinearLayout")
            }

        }
        return idViewtorre1
    }

    private fun getArrayElemto(id: Int?): ArrayDeque<Int> {
         when(id){
            1 ->  return torre1
            2 ->  return torre2
            3 ->  return torre3
            else -> {
                print("erro: Numero não é array")
            }

        }
        return torre1
    }

    private fun tranfereBloco(remetente:ArrayDeque<Int>, destinatario:ArrayDeque<Int>, idRemetente:LinearLayout, idDestinatario:LinearLayout){
        if(remetente.last()!=10) {

            //evita view vazia
            if (remetente.isEmpty()) {
                remetente.addLast(0)
            }

            val valueBlock = remetente.last()
            Log.d("Ultimo bloco", "Bloco movido $valueBlock, Ultimo bloco ${destinatario.last()}")
            if (valueBlock < destinatario.last()) {
                Log.d("Movimento", "Permitido")
                val value = remetente.removeLast()//remove do remetente
                destinatario.addLast(value)//add no destinatario

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
                Log.d("Movimento", "Não Permitido")
                Toast.makeText(this, "Movimento Não Permitido", LENGTH_LONG).show()

            }
        }
    }

    fun verificaViroria():Boolean{
        if (torre2.size==6 || torre3.size==6){
            Toast.makeText(this, "Vitoriaaa", LENGTH_LONG).show()
            return true
        }
        return false
    }


    @SuppressLint("WrongViewCast")
    private fun dataBlocks(numBlock:Int, context: Context): TextView {
        val bloco = TextView(context)
        bloco.textAlignment = View.TEXT_ALIGNMENT_CENTER
        when (numBlock) {
            0 -> {
                bloco.layoutParams = LinearLayout.LayoutParams(10, 10)
                bloco.setBackgroundColor(Color.parseColor("#333333"))
                bloco.setText("0")
            }
            1 -> {
                bloco.layoutParams = LinearLayout.LayoutParams(100, 70)
                bloco.setBackgroundColor(Color.parseColor("#FF5733"))
                bloco.setText("1")
            }
            2 -> {
                bloco.layoutParams = LinearLayout.LayoutParams(200, 70)
                bloco.setBackgroundColor(Color.parseColor("#FF884D"))
                bloco.setText("2")
            }
            3 -> {
                bloco.layoutParams = LinearLayout.LayoutParams(300, 70)
                bloco.setBackgroundColor(Color.parseColor("#FFB366"))
                bloco.setText("3")
            }
            4 -> {
                bloco.layoutParams = LinearLayout.LayoutParams(400, 70)
                bloco.setBackgroundColor(Color.parseColor("#FFD699"))
                bloco.setText("4")
            }
            5 -> {
                bloco.layoutParams = LinearLayout.LayoutParams(500, 70)
                bloco.setBackgroundColor(Color.parseColor("#645B2E"))
                bloco.setText("5")
            }
            10 -> {
                bloco.layoutParams = LinearLayout.LayoutParams(0, 0)
                bloco.setBackgroundColor(Color.parseColor("#484744"))
            }
            else -> { // Note the block
                print("erro:Numero não existente")
            }
        }

        return bloco
    }

}


