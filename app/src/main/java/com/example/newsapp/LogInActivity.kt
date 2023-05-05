package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.newsapp.databinding.ActivityLogInBinding
import com.example.newsapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth  = FirebaseAuth.getInstance()
        binding.loginbtn.setOnClickListener{
            val email = binding.emailtext.text.toString()
            val password = binding.password.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
//                        val user = firebaseAuth.currentUser
//                        if(user != null && !user.isEmailVerified){
//                            Toast.makeText(this,"Please verify your email address.",Toast.LENGTH_LONG).show()
//                        }
//                        else{
                            val intent = Intent(this,HomeScreenActivity::class.java)
                            startActivity(intent)
//                        }
                    }
                    else{
                        val errorCode = (task.exception as FirebaseAuthException).errorCode
                        if(errorCode == "ERROR_USER_NOT_FOUND"){
                            Toast.makeText(this,"User not found. Please check your email address or sign up for an account.",Toast.LENGTH_LONG).show()
                        }
                        else if(errorCode == "ERROR_WRONG_PASSWORD"){
                            Toast.makeText(this,"Wrong password. Please try again or reset your password.",Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(this,task.exception.toString(),Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
            else{
                Toast.makeText(this,"Field cannot be empty",Toast.LENGTH_LONG).show()
            }
        }
        binding.forgotPassword.setOnClickListener {
            val email = binding.emailtext.text.toString()
            if(email.isNotEmpty()){
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"Password reset email sent.",Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()
                    }
                }
            }
            else{
                Toast.makeText(this,"Email cannot be empty",Toast.LENGTH_LONG).show()
            }
        }

        binding.signuptv.setOnClickListener {
            val signupintent = Intent(this,SignUpActivity::class.java)
            startActivity(signupintent)
        }
    }
}