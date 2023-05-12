package com.example.newsapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    lateinit var pieChart: PieChart
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val happyImage: ImageView = view.findViewById(R.id.happy)
        val sadImage: ImageView = view.findViewById(R.id.sad)
        val angryImage: ImageView = view.findViewById(R.id.angry)
        val neutralImage: ImageView = view.findViewById(R.id.neutral)
        pieChart = view.findViewById(R.id.piechart)
        pieChart.description.isEnabled = false
        pieChart.setUsePercentValues(true)
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)
        pieChart.setDrawCenterText(true)
        pieChart.setCenterText("Mood")
        pieChart.setCenterTextSize(20f)

        database = FirebaseDatabase.getInstance()
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        happyImage.setOnClickListener {
            saveMoodResponse("happy")
        }
        sadImage.setOnClickListener {
            saveMoodResponse("sad")
        }
        angryImage.setOnClickListener {
            saveMoodResponse("angry")
        }
        neutralImage.setOnClickListener {
            saveMoodResponse("neutral")
        }

        fetchMoodResponses()
                return view
            }

    private fun saveMoodResponse(mood: String) {
        val timestamp = System.currentTimeMillis()
        val response = MoodResponse(mood, timestamp)
        database.getReference("users").child(userId).child("moods").child(mood).push().setValue(response)
    }

    private fun populatePieChart(moodResponses: List<MoodResponse>) {
        val happyCount = moodResponses.count { it.getMooddata() == "happy" }
        val sadCount = moodResponses.count { it.getMooddata() == "sad" }
        val angryCount = moodResponses.count { it.getMooddata() == "angry" }
        val neutralCount = moodResponses.count { it.getMooddata() == "neutral" }

        val pieChartText = "Moods: Happy ($happyCount), Sad ($sadCount), Angry ($angryCount), Neutral ($neutralCount)"

        val entries = listOf(
            PieEntry(happyCount.toFloat(), "Happy"),
            PieEntry(sadCount.toFloat(), "Sad"),
            PieEntry(angryCount.toFloat(), "Angry"),
            PieEntry(neutralCount.toFloat(), "Neutral")
        )

        val dataSet = PieDataSet(entries, "Mood Responses")
        dataSet.colors = listOf(
            Color.rgb(255, 193, 7), // yellow for happy
            Color.rgb(76, 175, 80), // green for sad
            Color.rgb(244, 67, 54), // red for angry
            Color.rgb(63, 81, 181) // blue for neutral
        )

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate()

        // update the text in the Firebase database
        val firebaseRef = FirebaseDatabase.getInstance().getReference("responses").child(userId).child("moodText")
        firebaseRef.setValue(pieChartText)
    }

    private fun fetchMoodResponses() {
        val moodResponsesRef = database.getReference("users").child(userId).child("moods")
        moodResponsesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val moodResponses = mutableListOf<MoodResponse>()
                for (moodSnapshot in snapshot.children) {
                    for (responseSnapshot in moodSnapshot.children) {
                        val response = responseSnapshot.getValue(MoodResponse::class.java)
                        if (response != null) {
                            moodResponses.add(response)
                        }
                    }
                }
                populatePieChart(moodResponses)
            }

            override fun onCancelled(error: DatabaseError) {
                // handle error
            }
        })
    }

}



