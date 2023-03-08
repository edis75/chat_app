package com.example.kotlin_first

import android.content.Intent
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Sign_in : AppCompatActivity() {

    private lateinit var edt_name: EditText
    private lateinit var edt_email: EditText
    private lateinit var edt_password: EditText
    private lateinit var btnSignUp: Button

    private  lateinit var mAuth: FirebaseAuth

    private lateinit var mDbRef: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()

        mAuth=FirebaseAuth.getInstance()

        edt_name=findViewById(R.id.edt_name)
        edt_email=findViewById(R.id.edt_email)
        edt_password=findViewById(R.id.edt_password)
        btnSignUp=findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener{
            val name=edt_name.text.toString()
            val email=edt_email.text.toString()
            val password=edt_password.text.toString()

            signUp(name,email,password)
        }

    }
    private  fun signUp(name:String,email:String,password:String){
        mAuth.createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(this){
                    task->if(task.isSuccessful){
                        finish()
                        // CODE JUMP HOME
                        addUserToDatabase(name,email,mAuth.currentUser?.uid!!)

                        val intent =Intent(this@Sign_in,MainActivity::class.java)
                        startActivity(intent)
                }
                    else{
                        Toast.makeText(this@Sign_in,"Some Error Occurd",Toast.LENGTH_SHORT).show()
                }
                }
    }

    private  fun addUserToDatabase(name:String,email: String,uid:String){
        mDbRef=FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email,uid))

    }
}