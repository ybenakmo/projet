package com.example.projet

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.widget.TextView
import com.google.gson.Gson







class Game : AppCompatActivity() {

    var array = intArrayOf(1,2,3,4)

    private var pStatus = 10
    var cpt = 0
    var size = 0
    var j = 0
    var curlevel = 1
    var i = 0
    val last = arrayListOf<Answers>()
    var score = 0
    var cpt2 = 0
    var finalscoreposition = ""

    private val handler = Handler()  // Timer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val Test = intent.getStringExtra("position")
        val currentcategorie = intent.getStringExtra("position")
        val finalscoreposition = currentcategorie
        val textView1 = this.findViewById(R.id.categorie) as TextView
        textView1.text = currentcategorie
        val txtProgress = findViewById<TextView>(R.id.txtProgress)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        Thread(Runnable {
            while (pStatus >= 0) {
                handler.post(Runnable {
                    progressBar.setProgress(pStatus)
                    txtProgress.setText(Integer.toString(pStatus) + " s")
                    if (pStatus < 10)
                        txtProgress.setText("0" + Integer.toString(pStatus) + " s")
                })
                try {
                    Thread.sleep(1000) // Gestion du timer
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                pStatus--
            }
        }).start()
        if(pStatus == 0) {
            System.out.println("Time is up");
        }
        parseXML(Test)
    }


    private fun parseXML(Categorie: String?) {
        val parserFactory: XmlPullParserFactory
        try {
            parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory.newPullParser()
            val `is` = getAssets().open("data.xml")
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(`is`, null)
            processParsing(parser, Categorie)
        } catch (e: XmlPullParserException) {
        } catch (e: IOException) {
        }
    }

    @SuppressLint("ResourceType")
    @Throws(IOException::class, XmlPullParserException::class)
    private fun processParsing(parser: XmlPullParser, Categorie: String?) {
        val reponses = arrayListOf<String>()
        val questions = arrayListOf<String>()
        val nombres = arrayListOf<Int>()
        val justes = arrayListOf<Int>()
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            var eltName: String?
            var typeName: String?
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    eltName = parser.name
                    if (eltName == "Quizz") { // Parsing du type
                        typeName = parser.getAttributeValue(null, "type")
                        if (Categorie == typeName) {

                            cpt = 1
                        } else
                            cpt = 2
                    } else if (eltName == "Question" && cpt == 1) { // Parsing des questions
                        parser.next()
                        questions.add(parser.text)
                        size++

                    } else if (eltName == "Propositions" && cpt == 1) { // Parsing des propositions
                        cpt2 = 1
                    } else if (eltName == "Proposition" && cpt2 == 1 && cpt == 1) {
                        parser.next()
                        reponses.add(parser.text)
                    } else if (eltName == "Nombre" && cpt == 1) {
                        typeName = parser.getAttributeValue(null, "valeur")
                        nombres.add(typeName.toInt())
                    } else if (eltName == "Reponse" && cpt == 1) { // Parsing de la réponse juste
                        typeName = parser.getAttributeValue(null, "valeur")
                        justes.add(typeName.toInt() - 1)
                    }

                }
            }
            eventType = parser.next()

        }
        var j = 0

        for (x in 0 until (nombres.size)) {
            val a = Answers()
            a.question = questions[x]
            a.nombre = nombres[x]
            a.juste = justes[x]
            last.add(a)
        }
        for (element in last) {
            for (x in 0 until (element.nombre)) {
                (element.reponses).add(reponses[0].substring(18))
                reponses.removeAt(0)
            }
        }
        val a = Answers()
        a.question = "1234567890"
        last.add(a)
        val textView = this.findViewById(R.id.question) as TextView
        textView.text = last.get(i).question.substring(8)
        val dbHandler = QuestionDB(this, null)
        dbHandler.addQuestion(textView.text.toString())
        val listView = findViewById<ListView>(R.id.listViews)
        val adapter =
            ArrayAdapter<String>(this, R.layout.list_item_black_text, (last.get(i).reponses))
        listView.adapter = adapter
        val scoreView = this.findViewById(R.id.scoreView) as TextView
        val niveauView = this.findViewById(R.id.niveauView) as TextView
        niveauView.text = "" + curlevel + "/" + size
        listView.setOnItemClickListener { parent, view, position, id ->

            if (last.get(j).juste == position) {
                view.setBackgroundColor(0x00FF00);
                val toast: Toast =
                    Toast.makeText(applicationContext, "Bonne réponse!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                score += 50
                scoreView.text = "Score : " + score
            }
            else {
                val toast: Toast =
                    Toast.makeText(applicationContext, "Mauvase réponse!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 100)
                toast.show()
            }
            Handler().postDelayed(
                {
            pStatus = 10
            i++
            curlevel++
                    if (last.get(j).juste == position) {
                        update(i)
                    } else {
                        update(i)
                    }
                    j++
                },
                2000 // value in milliseconds
            )

        }

    }
    fun update(i: Int) { // Update next question
        val textView = this.findViewById(R.id.question) as TextView
        textView.text = last.get(i).question.substring(8)
        val dbHandler = QuestionDB(this, null)
        dbHandler.addQuestion(textView.text.toString())
        val listView = findViewById<ListView>(R.id.listViews)
        val adapter =
            ArrayAdapter<String>(this, R.layout.list_item_black_text, (last.get(i).reponses))
        listView.adapter = adapter
        val scoreView = this.findViewById(R.id.scoreView) as TextView
        val niveauView = this.findViewById(R.id.niveauView) as TextView
        niveauView.text = "Niveau : " + curlevel + "/" + size
        if (curlevel > size) {
            val res = resources // need this to fetch the drawable
            scoreView.text = ""
            niveauView.text = ""
            textView.text = ""

            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Vous avez répondu à " + score / 50 + "/" + size + " questions, votre score est : " + score + " points")
                .setCancelable(false)
                .setPositiveButton("Rejouer", DialogInterface.OnClickListener { dialog, id ->
                    val nextScreen = Intent(applicationContext, Choix::class.java)
                    nextScreen.putExtra("finalscore", array)
                    startActivity(nextScreen)
                })

            val alert = dialogBuilder.create()
            var FinalScore : Quizz
            alert.setTitle("Résultat ")
            alert.show()
            val test1 = this.findViewById(R.id.txtProgress) as TextView
            test1.setVisibility(View.INVISIBLE)
            val test2 = this.findViewById(R.id.progressBar) as ProgressBar
            test2.setVisibility(View.INVISIBLE)

        }
    }
}



