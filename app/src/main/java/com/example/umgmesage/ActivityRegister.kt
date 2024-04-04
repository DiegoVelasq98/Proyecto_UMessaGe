package com.example.umgmesage

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umgmesage.messaging.Models.User
import com.example.umgmesage.messaging.firebase.UsersCollection
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class ActivityRegister : AppCompatActivity() {

    private lateinit var tieneCuenta: TextView
    private lateinit var btnRegistrar: Button
    private lateinit var txtInputUsername: EditText
    private lateinit var txtInputEmail: EditText
    private lateinit var txtInputPassword: EditText
    private lateinit var txtInputConfirmPassword: EditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mProgressBar: ProgressDialog
    private lateinit var userCollections:UsersCollection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        userCollections=UsersCollection()
        txtInputEmail = findViewById(R.id.inputEmail_register)
        txtInputPassword = findViewById(R.id.inputPassword_register)
        txtInputConfirmPassword = findViewById(R.id.inputPassword_confirm)
        btnRegistrar = findViewById(R.id.btn_register)
        tieneCuenta = findViewById(R.id.txtIniciarSesion)

        btnRegistrar.setOnClickListener {
            verificarCredenciales()
        }

        tieneCuenta.setOnClickListener {
            startActivity(Intent(this@ActivityRegister, ActivityLogin::class.java))
        }

        mAuth = FirebaseAuth.getInstance()
        mProgressBar = ProgressDialog(this)
    }

    //para verificar credenciales de registro
    private fun verificarCredenciales() {
        val email = txtInputEmail.text.toString()
        val password = txtInputPassword.text.toString()
        val confirmPass = txtInputConfirmPassword.text.toString()
        val allowedDomain = "@miumg.edu.gt"

        when {
            email.isEmpty() -> showError(txtInputEmail, "Por favor, ingrese su correo electrónico")
            !email.endsWith(allowedDomain) -> showError(txtInputEmail, "Solo se permiten correos electrónicos con dominio $allowedDomain")
            password.isEmpty() || password.length < 7 -> showError(txtInputPassword, "La contraseña debe tener al menos 7 caracteres")
            confirmPass.isEmpty() || confirmPass != password -> showError(txtInputConfirmPassword, "La confirmación de la contraseña no coincide")
            else -> {
                mProgressBar.setTitle("Proceso de Registro")
                mProgressBar.setMessage("Registrando usuario, espere un momento")
                mProgressBar.setCanceledOnTouchOutside(false)
                mProgressBar.show()

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mProgressBar.dismiss()
                        val intent = Intent(this@ActivityRegister, ActivityLogin::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "No se pudo registrar", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }




    //aqui finaliza el metodo

    private fun showError(input: EditText, s: String) {
        input.error = s
        input.requestFocus()
    }
}
