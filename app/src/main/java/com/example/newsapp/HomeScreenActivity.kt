package com.example.newsapp

import ConnectFragment
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val connectFragment = ConnectFragment()
        val recoverFragment = RecoverFragment()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        replaceFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home-> {
                    replaceFragment(homeFragment)
                }
                R.id.connect -> {
                    replaceFragment(connectFragment)
                }
                R.id.recover -> {
                    replaceFragment(recoverFragment)
                }
                R.id.profile -> {
                    replaceFragment(profileFragment)
                }
            }
            true
        }

    }
    override fun onBackPressed() {
        // Create an intent to start the MainActivity
        val intent = Intent(this, MainActivity::class.java)
        // Start the MainActivity and finish the current activity
        startActivity(intent)
        finish()
    }

    private fun replaceFragment(fragment: Fragment){
        if (fragment!= null){
            val transaction =  supportFragmentManager.beginTransaction()
            transaction.replace(R.id.framelayout,fragment)
            transaction.commit()
        }
    }
}



