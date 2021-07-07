package simons.valdez.ignacio.myfeelings.utilities

import android.content.Context
import android.util.Log

class JSONFile {

    val MY_FEELINGS = "data.json"


    fun saveData(context: Context, json:String){
        try{
            context.openFileOutput(MY_FEELINGS, Context.MODE_PRIVATE).use{
                it.write(json.toByteArray())
            }
        }catch(e:Exception){
            Log.e("GUARDAR", "Error in writing: ${e.localizedMessage}")
        }
    }

    fun getData(context: Context):String{
        try{
            return context.openFileInput(MY_FEELINGS).bufferedReader().readLine()
        }catch(e:Exception){
            Log.e("OBTENER", "Error fetching dada: ${e.localizedMessage}")
            return ""
        }
    }

}