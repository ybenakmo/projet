package com.example.projet
import android.content.DialogInterface
import android.content.Intent
import android.view.View;
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity() {

    var alertDialog1: AlertDialog? = null
    var values = arrayOf<CharSequence>("\n\n - Téléchargez un Quizz et commencez à jouer\n\n - Une question juste +50 points\n\n - Ce Quizz a été crée par BENAKMOUME Yacine\n\n")
    var scoremenu = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun OnPlayClick(view:View) { // Jouer au jeu
        this.scoremenu = 0
        val intent1 = Intent(this, Choix::class.java)
        intent1.putExtra("scoremenu", scoremenu)
        startActivity(intent1)
    }

    fun OnScoreClick(view:View) { // Mes Scores
        this.scoremenu = 1
        val intent1 = Intent(this, Choix::class.java)
        intent1.putExtra("scoremenu", scoremenu)
        startActivity(intent1)
    }

    fun OnManagerClick(view:View) { // Gestion des Quizz
        val intent1 = Intent(this, Manager::class.java)
        startActivity(intent1)
    }

    fun helpbtn(view: View) { // Aide
        CreateAlertDialogWithRadioButtonGroup()
    }
    fun CreateAlertDialogWithRadioButtonGroup() { // Dialog Box Aide - Help me

        val intent1 = Intent(this, AddQuestion::class.java)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Aide à l'utilisation")
        builder.setItems(values,
            DialogInterface.OnClickListener { dialog, item ->
                Toast.makeText(
                    applicationContext,
                    values[item],
                    Toast.LENGTH_SHORT
                ).show()
            })
        var alert = builder.create()
        alert = builder.create()
        alert!!.show()

    }

}

