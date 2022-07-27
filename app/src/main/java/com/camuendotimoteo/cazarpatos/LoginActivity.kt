package com.camuendotimoteo.cazarpatos

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    //lateinit var fileHandler: FileHandler
    lateinit var fileHandlerEncripted: FileHandler
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var buttonLogin: Button
    lateinit var buttonNewUser: Button
    lateinit var mediaPlayer: MediaPlayer
    lateinit var checkBoxRememberMe: CheckBox
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Inicialización de variables
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNewUser = findViewById(R.id.buttonNewUser)

        //fileHandler = SharedPreferencesManager(this)
        fileHandlerEncripted = EncriptedSharedPreferenceManager(this)
        checkBoxRememberMe = findViewById(R.id.checkBoxRecordarme)


        // Initialize Firebase Auth
        auth = Firebase.auth


        LeerDatosPreferencias()

        //Eventos clic
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val clave = editTextPassword.text.toString()

            //Validaciones de datos requeridos y formatos
            if (!ValidarDatosRequeridos())
                return@setOnClickListener

            /*
            //guardamos datos en preferencias
            GuardarDatosEnPreferencias()
            //Si pasa validación de datos requeridos, ir a pantalla principal
            val intencion = Intent(this, MainActivity::class.java)
            intencion.putExtra(EXTRA_LOGIN, name)
            startActivity(intencion)

            Log.d("TAG", Environment.getExternalStorageDirectory().toString())
            Log.d("TAG", this.getExternalFilesDir(null).toString())

            FileExternalManager(this).saveInformation(email to clave)
            println(FileExternalManager(this).readInformation())

             */
            AutenticarUsuario(email, clave)
        }
        buttonNewUser.setOnClickListener {

        }
        mediaPlayer = MediaPlayer.create(this, R.raw.title_screen)
        mediaPlayer.start()


    }

    fun AutenticarUsuario(email: String, password: String) {
        var name: String
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(EXTRA_LOGIN, "signInWithEmail:success")
                    //Si pasa validación de datos requeridos, ir a pantalla principal
                    val intencion = Intent(this, MainActivity::class.java)
                    name = nombreRecortado(email)
                    intencion.putExtra(EXTRA_LOGIN, name)
                    startActivity(intencion)
                    //finish()
                } else {
                    Log.w(EXTRA_LOGIN, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun GuardarDatosEnPreferencias() {
        val email = editTextEmail.text.toString()

        val clave = editTextPassword.text.toString()
        val listadoAGrabar: Pair<String, String>

        if (checkBoxRememberMe.isChecked) {
            listadoAGrabar = email to clave
        } else {
            listadoAGrabar = "" to ""
        }
        fileHandlerEncripted.saveInformation(listadoAGrabar)
    }

    private fun LeerDatosPreferencias() {
        val listadoLeido = fileHandlerEncripted.readInformation()
        if (listadoLeido.first != null) {
            checkBoxRememberMe.isChecked = true
        }
        editTextEmail.setText(listadoLeido.first)
        editTextPassword.setText(listadoLeido.second)
    }

    private fun ValidarDatosRequeridos(): Boolean {
        val email = editTextEmail.text.toString()
        val clave = editTextPassword.text.toString()
        if (email.isEmpty()) {
            editTextEmail.setError("El email es obligatorio")
            editTextEmail.requestFocus()
            return false
        }
        if (clave.isEmpty()) {
            editTextPassword.setError("La clave es obligatoria")
            editTextPassword.requestFocus()
            return false
        }
        if (clave.length < 8) {
            editTextPassword.setError("La clave debe tener al menos 8 caracteres")
            editTextPassword.requestFocus()
            return false
        }
        if (!esCorreo(email)) {
            editTextEmail.setError("El email es incorrecto")
            editTextEmail.requestFocus()
            return false
        }

        return true
    }

    fun esCorreo(texto: String): Boolean {
        var patron: Pattern =
            Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        var comparador: Matcher = patron.matcher(texto)
        return comparador.find()
    }

    fun nombreRecortado(texto: String): String {
        var name: String
        var index = texto.indexOf("@")
        name = texto.substring(0, index)
        return name
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

}