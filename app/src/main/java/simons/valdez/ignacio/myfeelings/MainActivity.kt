package simons.valdez.ignacio.myfeelings

import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import simons.valdez.ignacio.myfeelings.utilities.CustomBarDrawable
import simons.valdez.ignacio.myfeelings.utilities.CustomCircleDrawable
import simons.valdez.ignacio.myfeelings.utilities.Emociones
import simons.valdez.ignacio.myfeelings.utilities.JSONFile

class MainActivity : AppCompatActivity() {

    var jsonFile: JSONFile? = null
    var veryHappy = 0.0f
    var happy = 0.0f
    var neutral = 0.0f
    var sad = 0.0f
    var verySad = 0.0f
    var data:Boolean = false
    var lista = ArrayList<Emociones>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsonFile = JSONFile()

        fetchingData()

        if(!data){
            var emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones)

            var graph = findViewById<ConstraintLayout>(R.id.graph)
            graph.background = fondo

            var graphVeryHappy = findViewById<View>(R.id.graphVeryHappy)
            var graphHappy = findViewById<View>(R.id.graphHappy)
            var graphNeutral = findViewById<View>(R.id.graphNeutral)
            var graphSad = findViewById<View>(R.id.graphSad)
            var graphVerySad = findViewById<View>(R.id.graphVerySad)

            graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", veryHappy, R.color.mustard, veryHappy))
            graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", happy, R.color.orange, veryHappy))
            graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", neutral, R.color.greenie, veryHappy))
            graphSad.background = CustomBarDrawable(this, Emociones("Triste", sad, R.color.blue, veryHappy))
            graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", verySad, R.color.deepBlue, veryHappy))

        }else{
            actualizarGrafica()
            iconoMayoria()
        }

        var btn_guardar = findViewById<Button>(R.id.guardar)
        var btn_veryHappy = findViewById<ImageButton>(R.id.btn_veryHappy)
        var btn_happy = findViewById<ImageButton>(R.id.btn_happy)
        var btn_neutral = findViewById<ImageButton>(R.id.btn_neutral)
        var btn_sad = findViewById<ImageButton>(R.id.btn_sad)
        var btn_verySad = findViewById<ImageButton>(R.id.btn_verySad)

        btn_guardar.setOnClickListener {
            guardar()
        }

        btn_veryHappy.setOnClickListener {
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }

        btn_happy.setOnClickListener {
            happy++
            iconoMayoria()
            actualizarGrafica()
        }

        btn_neutral.setOnClickListener {
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }

        btn_sad.setOnClickListener {
            sad++
            iconoMayoria()
            actualizarGrafica()
        }

        btn_verySad.setOnClickListener {
            verySad++
            iconoMayoria()
            actualizarGrafica()
        }



    }

    fun fetchingData(){
        try{
            var json:String = jsonFile?.getData(this) ?: ""
            if(json != ""){
                this.data = true
                var jsonArray:JSONArray = JSONArray(json)

                this.lista = parseJSON(jsonArray)

                for(i in lista){
                    when(i.nombre){
                        "Muy feliz" -> veryHappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy triste" -> veryHappy = i.total
                    }
                }
            }else{
                this.data = false
            }
        }catch(e:JSONException){
            e.printStackTrace()
        }
    }

    fun iconoMayoria(){
        var icon = findViewById<ImageView>(R.id.icon)
        if(veryHappy > happy && veryHappy > neutral && veryHappy > sad && veryHappy > verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_outline_sentiment_very_satisfied_24))
        }
        if(veryHappy < happy && happy > neutral && happy > sad && happy > verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_happy))
        }
        if(veryHappy < sad && sad > neutral && happy < sad && sad > verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sad))
        }
        if(verySad > happy && verySad > neutral && verySad > sad && veryHappy < verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_verysad))
        }
        if(neutral > happy && veryHappy < neutral && neutral > sad && neutral > verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_neutral))
        }
    }

    fun actualizarGrafica(){

        var total = veryHappy+happy+neutral+sad+verySad

        var pVH:Float = ((veryHappy*100) / total).toFloat()
        var pH:Float = ((happy*100) / total).toFloat()
        var pN:Float = ((neutral*100) / total).toFloat()
        var pS:Float = ((sad*100) / total).toFloat()
        var pVS:Float = ((verySad*100) / total).toFloat()

        Log.d("Porcentajes", "very happy: ${veryHappy}")
        Log.d("Porcentajes", "happy: ${happy}")
        Log.d("Porcentajes", "neutral: ${neutral}")
        Log.d("Porcentajes", "sad: ${sad}")
        Log.d("Porcentajes", "very sad: ${verySad}")

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pS, R.color.blue, sad))
        lista.add(Emociones("Muy triste", pVS, R.color.deepBlue, verySad))

        val fondo = CustomCircleDrawable(this, lista)

        var graphVeryHappy = findViewById<View>(R.id.graphVeryHappy)
        var graphHappy = findViewById<View>(R.id.graphHappy)
        var graphNeutral = findViewById<View>(R.id.graphNeutral)
        var graphSad = findViewById<View>(R.id.graphSad)
        var graphVerySad = findViewById<View>(R.id.graphVerySad)
        var graph = findViewById<ConstraintLayout>(R.id.graph)

        graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", pH, R.color.orange, veryHappy))
        graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", pN, R.color.greenie, veryHappy))
        graphSad.background = CustomBarDrawable(this, Emociones("Triste", pS, R.color.blue, veryHappy))
        graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", pVS, R.color.deepBlue, veryHappy))

        graph.background = fondo

    }

    fun parseJSON(jsonArray: JSONArray):ArrayList<Emociones>{
        var lista = ArrayList<Emociones>()

        for(i in 0..jsonArray.length()){
            try{
                val nombre = jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                var emocion = Emociones(nombre, porcentaje, color, total)
                lista.add(emocion)
            }catch(exception:JSONException){
                exception.printStackTrace()
            }
        }

        return lista
    }

    fun guardar(){
        var jsonArray = JSONArray()
        var o:Int  = 0
        for(i in lista){
            Log.d("objetos", i.toString())
            var j:JSONObject = JSONObject()
            j.put("nombre",i.nombre)
            j.put("porcentaje",i.porcentaje)
            j.put("color",i.color)
            j.put("total",i.total)

            jsonArray.put(o, j)
            o++
        }

        jsonFile?.saveData(this, jsonArray.toString())

        Toast.makeText(this,"Datos guardados", Toast.LENGTH_LONG).show()
    }


}