package com.example.torre_de_hani

import android.app.Dialog
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.example.torre_de_hani.databinding.ActivityMainBinding
import com.example.torre_de_hani.databinding.StyleDialogContinuaJogoBinding
import com.example.torre_de_hani.databinding.StyleDialogInfosBinding
import com.example.torre_de_hani.databinding.StyleDialogNumDiscosBinding
import com.example.torre_de_hani.databinding.StyleDialogVitoriaBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity(), View.OnDragListener, View.OnLongClickListener {

    //criando pilhas
    private var torre1 = ArrayDeque(listOf(10))
    private var torre2 = ArrayDeque(listOf(10))
    private var torre3 = ArrayDeque(listOf(10))

    //definidado pelo nome do xml + Binding no final
    private lateinit var binding: ActivityMainBinding

    private var numDisco = 0

    //define tamanho
    private var widthDialog = 0
    private var heightDialog = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //inicia o anuncio
        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        //esconde staus bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        //deita a tela
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val widthTela=resources.displayMetrics.widthPixels
        val heightTela=resources.displayMetrics.heightPixels

        //define tamanho de dialog
        widthDialog= (widthTela * 0.80).toInt()
        heightDialog = (heightTela * 0.75).toInt()

        //---------retomar jogo------------------
        val tinyDB = TinyDB(applicationContext)
        val booelnaContinua = tinyDB.getBoolean("continuarJogo")

        if(booelnaContinua==true){
            showDialogContinuaJogo(this)
        }else{
            showDialogSetNumDiscos(this)
        }

        val idViewtorre1: LinearLayout = findViewById(R.id.ViewTorre1)
        val idViewtorre2: LinearLayout = findViewById(R.id.ViewTorre2)
        val idViewtorre3: LinearLayout = findViewById(R.id.ViewTorre3)

        //defione os lugares que poden ser receber
        idViewtorre1.setOnDragListener(this)
        idViewtorre2.setOnDragListener(this)
        idViewtorre3.setOnDragListener(this)

        binding.btnMenu.setOnClickListener {
            showDialogMenu(this)
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //esconde staus bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }

    //-----------------------dialog-----------------\\
    private fun showDialogSetNumDiscos(context: Context?) {
        var numDiscoDialog:Int

        val widthDialog= (resources.displayMetrics.widthPixels * 0.70).toInt()
        val heightDialog = (resources.displayMetrics.heightPixels * 0.75).toInt()

        val dialog = Dialog(context!!)
        val dialogBinding: StyleDialogNumDiscosBinding = StyleDialogNumDiscosBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_border_radius)
        dialog.window?.setLayout(widthDialog, heightDialog)
        dialog.setCancelable(false)
        dialog.show()

        //recebe valor e convert em int
        val txtNumDisco: String = dialogBinding.txtViewNumDiscos.text.toString()
        numDiscoDialog = txtNumDisco.toInt()

        dialogBinding.btnMaisDiscos.setOnClickListener {
            if (numDiscoDialog in 3..8){
                // mais um disco
                numDiscoDialog++
                dialogBinding.txtViewNumDiscos.text = numDiscoDialog.toString()
            }
       }

        dialogBinding.btnMenosDiscos.setOnClickListener {
            if (numDiscoDialog in 5..9){
                //menos um disco
                numDiscoDialog--
                dialogBinding.txtViewNumDiscos.text = numDiscoDialog.toString()
            }

        }

        dialogBinding.btnIniciaJogo.setOnClickListener {
            dialog.dismiss()
            numDisco=numDiscoDialog

            while (numDiscoDialog>=1){
                torre1.addLast(numDiscoDialog)
                numDiscoDialog--
            }

            //chama funçao para mostrar os blocos
            for (value in torre1.reversed()) {
                binding.ViewTorre1.addView(dataBlocks(value, this))
            }
        }
    }


    private fun showDialogMenu(context: Context?) {
        val dialogMenu = Dialog(context!!)
        val dialogMenuBinding: StyleDialogInfosBinding = StyleDialogInfosBinding.inflate(LayoutInflater.from(context))
        dialogMenu.setContentView(dialogMenuBinding.root)
        dialogMenu.window?.setBackgroundDrawableResource(R.drawable.dialog_border_radius)
        dialogMenu.window?.setLayout(widthDialog, heightDialog)
        dialogMenu.setCancelable(true)
        dialogMenu.show()

        dialogMenuBinding.btnCancelDialogMenu.setOnClickListener{
            dialogMenu.dismiss()
        }

        dialogMenuBinding.btnIniciaJogo.setOnClickListener{
            dialogMenu.dismiss()
            novoJogo()
        }

    }

    private fun showDialogVitoria(context: Context?) {
        val dialogVitoria = Dialog(context!!)
        val dialogVitoriaBinding: StyleDialogVitoriaBinding = StyleDialogVitoriaBinding.inflate(LayoutInflater.from(context))
        dialogVitoria.setContentView(dialogVitoriaBinding.root)
        dialogVitoria.window?.setBackgroundDrawableResource(R.drawable.dialog_border_radius)
        dialogVitoria.window?.setLayout(widthDialog, heightDialog)
        dialogVitoria.setCancelable(false)
        dialogVitoria.show()

        dialogVitoriaBinding.btnIniciaJogo.setOnClickListener{
            dialogVitoria.dismiss()
            novoJogo()
        }
    }

    private fun showDialogContinuaJogo(context: Context?) {
        val dialogContinuaJogo = Dialog(context!!)
        val dialogContinuaJogoBinding: StyleDialogContinuaJogoBinding = StyleDialogContinuaJogoBinding.inflate(LayoutInflater.from(context))
        dialogContinuaJogo.setContentView(dialogContinuaJogoBinding.root)
        dialogContinuaJogo.window?.setBackgroundDrawableResource(R.drawable.dialog_border_radius)
        dialogContinuaJogo.window?.setLayout(widthDialog, heightDialog)
        dialogContinuaJogo.setCancelable(false)
        dialogContinuaJogo.show()

        dialogContinuaJogoBinding.btnIniciaJogo.setOnClickListener{
            dialogContinuaJogo.dismiss()
            showDialogSetNumDiscos(this)
        }

        dialogContinuaJogoBinding.btnContinuaJogo.setOnClickListener{
            dialogContinuaJogo.dismiss()
            reconstruindoJogoAntigo()
        }
    }
    private fun vibratePhone() {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun novoJogo(){
        //zera os arrays
        torre1 = ArrayDeque(listOf(10))
        torre2 = ArrayDeque(listOf(10))
        torre3 = ArrayDeque(listOf(10))

        //recarrega
        binding.ViewTorre1.removeAllViewsInLayout()
        binding.ViewTorre2.removeAllViewsInLayout()
        binding.ViewTorre3.removeAllViewsInLayout()

        binding.ViewTorre1.addView(dataBlocks(10, this))
        binding.ViewTorre2.addView(dataBlocks(10, this))
        binding.ViewTorre3.addView(dataBlocks(10, this))

        showDialogSetNumDiscos(this)

        val tinyDB = TinyDB(applicationContext)
        tinyDB.putBoolean("continuarJogo",false)
    }

    private fun getIdElemto(id: Int?): LinearLayout {
        return when (id) {
            1 -> binding.ViewTorre1
            2 -> binding.ViewTorre2
            3 -> binding.ViewTorre3
            else -> {
                binding.ViewTorre1
            }
        }
    }

    private fun getArrayElemto(id: Int?): ArrayDeque<Int> {
        return when (id) {
            1 -> torre1
            2 -> torre2
            3 -> torre3
            else -> {
                torre1
            }
        }
    }

    private fun getNumBlockById(idBlockLastChar: Char?): Int {
        return when ( idBlockLastChar) {
            '0' -> 10
            '1' -> 1
            '2' -> 2
            '3' -> 3
            '4' -> 4
            '5' -> 5
            '6' -> 6
            '7' -> 7
            '8' -> 8
            '9' -> 9
            else -> 10
        }
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

        if(remetente.last() != 10) {
            if (valueBlock < destinatario.last()) {
                //son de madeira
                soundToc()

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
                Toast.makeText(this, "Movimento Não Permitido", LENGTH_SHORT).show()
                vibratePhone()
            }
        }
        //ao final de cada jogada salva o estado
        salveGame()
    }

    private fun verificaViroria(){
        if (torre2.size == numDisco+1 || torre3.size == numDisco+1) {
            showDialogVitoria(this)
        }
    }


    private fun dataBlocks(numBlock: Int, context: Context): TextView {
        val bloco = TextView(context)

        val widthViewTorre=binding.ViewTorre1.width
        val heightViewTorre=(binding.ViewTorre1.height * 0.75).toInt()

        //Define tamanhos
        val blocoWidth:Int = when(numBlock){
            1->(0.15 * numBlock * widthViewTorre).toInt()
            else->(0.1 *numBlock*widthViewTorre).toInt()
        }

        val blocoHeight:Int= heightViewTorre/numDisco

        bloco.layoutParams = LinearLayout.LayoutParams(blocoWidth, blocoHeight)
        bloco.setBackgroundResource(R.drawable.border_radius_disk)

        //mantem o 10 "invisivel"
        if (numBlock==10){
            bloco.layoutParams = LinearLayout.LayoutParams(0, 0)
        }

        //define cor dos discos
        val colorDisco = when (numBlock) {
           10 -> "#333333"//defoult

            1 -> "#008688"//ciano

            2 -> "#0095B7"//azul

            3 -> "#38731D"//verde escuro

            4 -> "#A8C93E"//verde

            5 -> "#F2AB27"//amarelo

            6 -> "#F25C05" //laranja

            7 -> "#D90404"//veremlho

            8 -> "#9450F2"//roxo

            9 -> "#3647EB"//azul

            else -> { // Note the block
                "#943E10"//cor generica
            }
        }

        //define cor
        bloco.backgroundTintList = ColorStateList.valueOf(Color.parseColor(colorDisco))
        //set tag de identificao
        val tagDisco = "disco$numBlock"
        bloco.tag=tagDisco

        bloco.setOnLongClickListener(this)

        return bloco
    }

    private fun salveGame(){
        //classe para armazenar
        val tinyDB = TinyDB(applicationContext)

        //convert array deque in arrayList<int>
        val torre1Store=ArrayList<Int>(torre1)
        val torre2Store=ArrayList<Int>(torre2)
        val torre3Store=ArrayList<Int>(torre3)

        // put /save
        tinyDB.putListInt("torre1_store", torre1Store)
        tinyDB.putListInt("torre2_store", torre2Store)
        tinyDB.putListInt("torre3_store", torre3Store)

        tinyDB.putInt("numDisco",numDisco)
        tinyDB.putBoolean("continuarJogo",true)
    }

    //recuperando
    private fun reconstruindoJogoAntigo(){
        //classe para armazenar
        val tinyDB = TinyDB(applicationContext)

        //get
        val valorTorre1: ArrayList<Int> = tinyDB.getListInt("torre1_store")
        val valorTorre2: ArrayList<Int> = tinyDB.getListInt("torre2_store")
        val valorTorre3: ArrayList<Int> = tinyDB.getListInt("torre3_store")

        torre1.clear()
        torre1.addAll(valorTorre1)

        torre2.clear()
        torre2.addAll(valorTorre2)

        torre3.clear()
        torre3.addAll(valorTorre3)
        numDisco=tinyDB.getInt("numDisco")

        //recarrega
        binding.ViewTorre1.removeAllViewsInLayout()
        binding.ViewTorre2.removeAllViewsInLayout()
        binding.ViewTorre3.removeAllViewsInLayout()

        //recontroi
        for (valueBlockTorre1 in torre1.reversed()) {
            binding.ViewTorre1.addView(dataBlocks(valueBlockTorre1, this))
        }

        for (valueBlockTorre2 in torre2.reversed()) {
            binding.ViewTorre2.addView(dataBlocks(valueBlockTorre2, this))
        }

        for (valueBlockTorre3 in torre3.reversed()) {
            binding.ViewTorre3.addView(dataBlocks(valueBlockTorre3, this))
        }
    }

    private fun soundToc(){
        val toc: MediaPlayer = MediaPlayer.create(this, R.raw.sound_toc)
        toc.start()
    }

    override fun onLongClick(v: View): Boolean {
        //usa tag para identificar
        val item = ClipData.Item(v.tag as CharSequence)
        val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
        val data = ClipData(v.tag.toString(), mimeTypes, item)

        // Instantiates the drag shadow builder.
        val dragshadow = View.DragShadowBuilder(v)
        // Starts the drag
        v.startDrag(data, dragshadow,v ,0)
        return true
    }

    //discos arrastaveis
    override fun onDrag(v: View, event: DragEvent): Boolean {
        when (event.action) {
            //inicia drag
            DragEvent.ACTION_DRAG_STARTED -> {
                val item = event.clipDescription.label
                val numblock = getNumBlockById( item.last())

                //ação iniciada
                //se não estiver no topo da pilha não permite
                return if (torre1.last()==numblock || torre2.last()==numblock|| torre3.last()==numblock){
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }else{
                    false
                }
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                //muda a cor da linear layou rementente
                when(v.id){
                    R.id.ViewTorre1-> {
                        binding.bgBarraTorre1.setBackgroundResource(R.drawable.bg_torre_select)
                    }

                    R.id.ViewTorre2-> {
                        binding.bgBarraTorre2.setBackgroundResource(R.drawable.bg_torre_select)
                    }

                    R.id.ViewTorre3-> {
                        binding.bgBarraTorre3.setBackgroundResource(R.drawable.bg_torre_select)
                    }
                }

                v.invalidate()
                return true
            }

            DragEvent.ACTION_DRAG_LOCATION ->                 // Ignore the event
                return true

            //volta ao normal
            DragEvent.ACTION_DRAG_EXITED -> {
                //deu certo, volta a cor para a que estava
                when(v.id){
                    R.id.ViewTorre1-> {
                        binding.bgBarraTorre1.setBackgroundResource(R.drawable.bg_torre)
                    }

                    R.id.ViewTorre2-> {
                        binding.bgBarraTorre2.setBackgroundResource(R.drawable.bg_torre)
                    }

                    R.id.ViewTorre3-> {
                        binding.bgBarraTorre3.setBackgroundResource(R.drawable.bg_torre)
                    }
                }
                v.invalidate()
                return true
            }

            //item solto
            DragEvent.ACTION_DROP -> {
                val item = event.clipData.getItemAt(0)
                //define o valor do disco
                val numblock = getNumBlockById( item.text.last())

                val destinatario:Int

                //define o remente com base no array que tiver aquele valor
                val remetente: Int =
                    if (torre1.contains(numblock)){
                        1
                    }else if(torre2.contains(numblock)){
                        2
                    }else{
                        3
                    }

                //destinatarios
                when (v.id) {
                    R.id.ViewTorre1 -> {
                        binding.bgBarraTorre1.setBackgroundResource(R.drawable.bg_torre)

                        destinatario=1
                        tranfereBloco(remetente = getArrayElemto(remetente), destinatario = getArrayElemto(destinatario),
                            idRemetente = getIdElemto(remetente), idDestinatario =  getIdElemto(destinatario)
                        )
                    }

                    R.id.ViewTorre2 -> {
                        binding.bgBarraTorre2.setBackgroundResource(R.drawable.bg_torre)

                        destinatario=2
                        tranfereBloco(remetente = getArrayElemto(remetente), destinatario = getArrayElemto(destinatario),
                            idRemetente = getIdElemto(remetente), idDestinatario =  getIdElemto(destinatario)
                        )
                    }

                    R.id.ViewTorre3 -> {
                        binding.bgBarraTorre3.setBackgroundResource(R.drawable.bg_torre)

                        destinatario=3
                        tranfereBloco(remetente = getArrayElemto(remetente), destinatario = getArrayElemto(destinatario),
                            idRemetente = getIdElemto(remetente), idDestinatario =  getIdElemto(destinatario)
                        )
                    }
                }
                return true
            }

            //fim do drag
            DragEvent.ACTION_DRAG_ENDED -> {
                v.invalidate()
                return true
            }

            else -> Log.i("DragDrop Example", "Unknown action type received by OnDragListener.")
        }
        return false
    }
}




