package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        binding.button.setOnClickListener{

            val name = binding.username.text.toString()
            val email = binding.emailtext.text.toString()
            val password = binding.password.text.toString()
            val spinner = binding.spinner

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                if (password.length > 6){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful){
                            val userReference = database.reference.child("users").child(it.result?.user?.uid ?: "")
                            // Save user data to the Realtime Database
                            val userData = HashMap<String, Any>()
                            userData["name"] = name
                            userData["email"] = email
                            userReference.setValue(userData)

                            val intent = Intent(this, LogInActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password is too short", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(this,"Field cannot be empty",Toast.LENGTH_LONG).show()
            }
        }
    }
}