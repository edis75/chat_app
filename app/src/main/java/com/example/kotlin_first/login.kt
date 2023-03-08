package com.example.kotlin_first

import android.content.Intent
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class login() : AppCompatActivity() {

    private lateinit var edt_email:EditText
    private lateinit var edt_password:EditText
    private lateinit var btnLogin:Button
    private lateinit var btnSignUp:Button

    private  lateinit var mAuth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        mAuth=FirebaseAuth.getInstance()

        edt_email=findViewById(R.id.edt_email)
        edt_password=findViewById(R.id.edt_password)
        btnLogin=findViewById(R.id.btnLogin)
        btnSignUp=findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener{
            val intent= Intent(this,Sign_in::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener{
            val email=edt_email.text
            val password=edt_password.text

            login_bir(email,password)
        }


    }

    private fun login_bir(email: Editable, password: Editable) {
        mAuth.signInWithEmailAndPassword(email.toString(), password.toString()).addOnCompleteListener(this){
                task->if(task.isSuccessful){

            // giriş yap
            val intent =Intent(this@login,MainActivity::class.java)
            finish()
            startActivity(intent)

        }
        else {
           Toast.makeText(this@login,"The user is not excit",Toast.LENGTH_SHORT).show()

        }
        }
    }
}