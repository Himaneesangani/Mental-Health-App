package com.example.newsapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap


class ProfileFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize the views
        val profileImage: ImageView = view.findViewById(R.id.profile_image)
        val imageButton: ImageButton = view.findViewById(R.id.imageButton)
        val nameField: EditText = view.findViewById(R.id.name_field)
        val emailField: EditText = view.findViewById(R.id.email_field)
        val mobileField: EditText = view.findViewById(R.id.mobile_field)
        val genderGroup: RadioGroup = view.findViewById(R.id.gender_group)
        val maleRadio: RadioButton = view.findViewById(R.id.male_radio)
        val femaleRadio: RadioButton = view.findViewById(R.id.female_radio)
        val dateField: EditText = view.findViewById(R.id.date_field)
        val updateText: TextView = view.findViewById(R.id.update_profile)

        // Initialize the Firebase components
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val userReference: DatabaseReference = database.reference.child("users")
//        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser

        val name: String? = currentUser?.displayName
        val email: String? = currentUser?.email
        profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
        imageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                val imageUri: Uri = data.data!!

                // Display the selected image in the profileImage ImageView
                profileImage.setImageURI(imageUri)

                // Upload the image to Firebase Storage and get its download URL
                val storageReference = FirebaseStorage.getInstance().getReference("profile_images/${currentUser?.uid}")
                val uploadTask = storageReference.putFile(imageUri)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    storageReference.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        // Store the download URL in the database
                        currentUser?.uid?.let { uid ->
                            userReference.child(uid).child("profileImageUrl").setValue(downloadUri.toString())
                        }
                    } else {
                        // Handle the error
                    }
                }
            }
        }



        dateField.setOnClickListener {
            // Get current date
            val c = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)

            // Show DatePickerDialog
            val datePickerDialog = DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Set the selected date to the EditText
                    dateField.setText("$dayOfMonth/${monthOfYear + 1}/$year")
                }, mYear, mMonth, mDay)
            datePickerDialog.show()
        }


        // Retrieve the user's data from the database
        currentUser?.uid?.let { uid ->
            userReference.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get the user's data
                    val userData = dataSnapshot.value as Map<*, *>?

                    // Display the user's data in the UI
                    nameField.setText(name)
                    emailField.setText(email)
                    nameField.setText(userData?.get("name").toString())
//                    emailField.setText(userData?.get("email").toString())
                    mobileField.setText(userData?.get("mobile").toString())
                    if (userData?.get("gender").toString() == "Male") {
                        maleRadio.isChecked = true
                    } else {
                        femaleRadio.isChecked = true
                    }
                    dateField.setText(userData?.get("dateOfBirth").toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                }
            })
        }
        val logoutButton = view.findViewById<Button>(R.id.logoutbtn)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finish()
        }
        // Set up the click listener for the update button
        updateText.setOnClickListener {
            // Get the updated data from the UI
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val mobile = mobileField.text.toString()
            val gender = if (maleRadio.isChecked) "Male" else "Female"
            val dateOfBirth = dateField.text.toString()

            // Store the updated data in the database
            val userData = HashMap<String, Any>()
            userData["name"] = name
            userData["email"] = email
            userData["mobile"] = mobile
            userData["gender"] = gender
            userData["dateOfBirth"] = dateOfBirth
            currentUser?.uid?.let { uid ->
                userReference.child(uid).updateChildren(userData)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Show a success message
                            Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                        } else {
                            // Handle the error
                        }
                    }
            }
        }


        return view
    }

}