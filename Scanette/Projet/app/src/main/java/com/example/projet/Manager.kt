package com.example.projet

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AlertDialog
import android.widget.LinearLayout
import android.widget.EditText
import android.widget.Toast

class Manager : AppCompatActivity() {

    private var StringURL : String = ""
    private val STORAGE_PERMISSION_CODE: Int = 1000
    var alertDialog1: AlertDialog? = null
    var values = arrayOf<CharSequence>("Ajouter une question")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
    }
    fun OnAddClick(view: View) { // Ajouter un Quizz

        val dialogBuilder = AlertDialog.Builder(this)
        val input = EditText(this@Manager)

        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT

        )
        input.layoutParams = lp
        dialogBuilder.setView(input)
            .setMessage("URL :")
            .setCancelable(false)

            .setPositiveButton("Ajouter", DialogInterface.OnClickListener { dialog, id ->
                StringURL = input.text.toString()
                if(StringURL.endsWith(".xml")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                            requestPermissions(
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                STORAGE_PERMISSION_CODE
                            )
                        else {
                            startDownloading()

                        }
                    } else {
                        startDownloading()

                    }
                }
                else {
                    Toast.makeText(this,"Seuls fichiers XML autorisés",Toast.LENGTH_LONG).show()
                }
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Ajouter un Quizz")
        alert.show()
    }

    private fun startDownloading() {  // Téléchargement du Quizz et Stockage
        var url = StringURL
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("Quizz XML")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startDownloading()
                else
                    Toast.makeText(this,"permission Denied",Toast.LENGTH_LONG).show()
            }
        }
    }
    fun CreateAlertDialogWithRadioButtonGroup() {

            val intent1 = Intent(this, AddQuestion::class.java)
            val builder = AlertDialog.Builder(this@Manager)

            builder.setTitle("Faites votre choix")
            builder.setSingleChoiceItems(values, -1,
                DialogInterface.OnClickListener { dialog, item ->
                    when (item) {
                        0 ->
                            startActivity(intent1)
                    }
                    alertDialog1!!.dismiss()
                })
            alertDialog1 = builder.create()
            alertDialog1!!.show()

    }

    fun OnEditQuizz(view: View) {CreateAlertDialogWithRadioButtonGroup()}  // Ajouter d'une question
    fun DeleteQuizz(view: View) {  // Supprimer un Quizz
        val dialogBuilder = AlertDialog.Builder(this)
        val input = EditText(this@Manager)

        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT

        )
        input.layoutParams = lp
        dialogBuilder.setView(input) // uncomment this line
            .setMessage("Quizz Name :")
            .setCancelable(false)

            .setPositiveButton("Supprimer", DialogInterface.OnClickListener { dialog, id ->
                StringURL = input.text.toString()
                if(StringURL == "Departement Informatique" || StringURL == "Culture Générale" || StringURL == "Monde Animal" || StringURL == "Informatique") {
                    Toast.makeText(this,"Quizz supprimée avec succès",Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(this,"Le Quizz n'éxiste pas",Toast.LENGTH_LONG).show()
                }
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Supprimer un Quizz")
        alert.show()
    }
}
