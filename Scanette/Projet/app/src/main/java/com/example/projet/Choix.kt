package com.example.projet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.gson.Gson
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Choix : AppCompatActivity() {

    lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix)
        parseXML()
    }

    private fun parseXML() { // Ouverture du fichier XML téléchargé et sauvegardé dans le dossier "Assets"
        val parserFactory: XmlPullParserFactory
        try {
            parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory.newPullParser()
            val `is` = getAssets().open("data.xml")
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(`is`, null)
            processParsing(parser)
        } catch (e: XmlPullParserException) {
        } catch (e: IOException) {
        }
    }

    @Throws(IOException::class, XmlPullParserException::class) // Parsing du fichier XML
    private fun processParsing(parser: XmlPullParser) {
        val quizz = arrayListOf<Quizz>()
        val test = arrayListOf<Model>()
        val test1: Model
        val descList = arrayListOf<String>()
        descList.add("Quizz sur le département Informatique de l'université de Franche Comté")
        descList.add("Grand Quizz sur Les animaux du monde")
        descList.add("Testez votre culture générale avec nos quizz")
        descList.add("Testez vos compétences informatiques!")
        var x = 0
        var eventType = parser.eventType
        var currentQuizz: Quizz? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            var eltName: String? = null
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    eltName = parser.name

                    if ("Quizzs" == eltName) { // Parse Catégories
                        currentQuizz = Quizz()
                        quizz.add(currentQuizz)
                    } else if (currentQuizz != null) {
                        if ("Quizz" == eltName) { // Creation d'objet et ajout des catégories
                            currentQuizz.type = parser.getAttributeValue(null, "type")
                            val test1 = Model(currentQuizz.type, descList.get(x), x)
                            test.add(test1)
                            println(currentQuizz.type)
                            x = x + 1
                        }

                    }
                }
            }

            eventType = parser.next()
        }
        printPlayers(test)
    }

    private fun printPlayers(test: ArrayList<Model>) { // Display affichage des catégories
        val builder = StringBuilder()
        for (fast: Model in test) {
            builder.append(fast.title).append("\n")
        }
        listView = findViewById(R.id.listView)
        var list = mutableListOf<Model>()
        val objects = ArrayList<Model>()

        for (fast: Model in test) {
            list.add(Model(fast.title, fast.desc, fast.photo))
            objects.add(Model(fast.title, fast.desc, fast.photo))
        }
        var score = 0
        listView.adapter = MyListAdapter(this, R.layout.row, list)
        val textview = findViewById(R.id.text) as TextView
        val Test = intent.getIntExtra("scoremenu", 0)
            listView.setOnItemClickListener { parent, view, position, id ->
            if (Test == 0) {
                val nextScreen = Intent(applicationContext, Game::class.java)
                val arrayAsString = Gson().toJson(list)
                val textView = view.findViewById(R.id.titleTv) as TextView
                val zest = textView.text
                nextScreen.putExtra("json", arrayAsString)
                nextScreen.putExtra("position", zest)
                startActivity(nextScreen)
            } else if (Test == 1) { // Scores
                var scores = findViewById(R.id.descTv) as TextView
                scores.text = score.toString()
                score = 250
            }

        }
    }
}
