package com.example.projet

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.marginBottom
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_question.*
import org.w3c.dom.Document
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.IOException
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import android.text.InputType
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.TypedValue
import android.view.Gravity


class AddQuestion : AppCompatActivity() {

    var selected = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)
        getreponse.inputType = InputType.TYPE_CLASS_NUMBER
        editText7.inputType = InputType.TYPE_CLASS_NUMBER
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F);

        parseXML()
    }

    private fun parseXML() {
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

    @Throws(IOException::class, XmlPullParserException::class)
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

                    if ("Quizzs" == eltName) {
                        currentQuizz = Quizz()
                        quizz.add(currentQuizz)
                    } else if (currentQuizz != null) {
                        if ("Quizz" == eltName) {
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

    private fun printPlayers(test: ArrayList<Model>) {
        val builder = StringBuilder()
        for (fast: Model in test) {
            builder.append(fast.title).append("\n")
        }
        var list = mutableListOf<String>()

        for (fast: Model in test) {
            list.add(fast.title)
            System.out.println(fast.title)
        }
        val adapter = ArrayAdapter(
            this, // Context
            R.layout.spinner_item, // Layout
            list // Array
        )

        // Set the drop down view resource
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        // Finally, data bind the spinner object with dapter
        spinner.adapter = adapter;

        // Set an on item selected listener for spinner object
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selected = parent.getItemAtPosition(position).toString()
                System.out.println("Selected is :  ")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
    }

    fun Generate(view: View) {
        var reponses = arrayListOf<String>()
        val btn : Button = this.findViewById(R.id.button4)
        val Reponses : TextView = this.findViewById(R.id.getreponse)
        val rootView = findViewById(R.id.rootLayout) as LinearLayout
        val param = LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WRAP_CONTENT)
        param.setMargins(0,0,0,10)
        for(i in 0 until Integer.valueOf(Reponses.text.toString())) {
            val myEditText = EditText(rootView.context)
            myEditText.setTextColor(Color.WHITE)
            myEditText.setPadding(0,5,0,30)
            myEditText.setBackgroundResource(R.drawable.border);
            myEditText.layoutParams = param
            rootView.addView(myEditText)
            btn.visibility = View.INVISIBLE
            Reponses.setFocusable(false)
        }
    }

    fun Ajouter(view: View) {
        val dbHandler = QuestionDB(this, null)
        val cursor = dbHandler.getAllName()
        cursor!!.moveToFirst()
        System.out.println(cursor.getString(cursor.getColumnIndex(QuestionDB.COLUMN_NAME)))
        while (cursor.moveToNext()) {
            System.out.println(cursor.getString(cursor.getColumnIndex(QuestionDB.COLUMN_NAME)))
            System.out.println("\n")
        }
        cursor.close()
        val toast: Toast =
            Toast.makeText(applicationContext, "Question ajoutée avec succès!", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
        val intent1 = Intent(this, Manager::class.java)
        startActivity(intent1)
    }
}
