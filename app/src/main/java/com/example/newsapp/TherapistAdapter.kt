import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.Therapist
import com.google.android.material.chip.Chip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.log

class TherapistAdapter(private val context: Context, private val therapistList: MutableList<Therapist>) :
    RecyclerView.Adapter<TherapistAdapter.ViewHolder>() {

    private val database = FirebaseDatabase.getInstance().reference.child("therapist")

    var listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            therapistList.clear()
            for (therapistSnapshot in snapshot.children) {
                var therapist = therapistSnapshot.getValue(Therapist::class.java)
                therapist?.let {
                    therapistList.add(it)
                }
            }
            notifyDataSetChanged()
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
        }
    }

    init {
        database.addValueEventListener(listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.therapist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val therapist = therapistList[position]
        holder.name.text = therapist.name
        holder.speciality.text = therapist.speciality
        holder.details.text = therapist.about
        Glide.with(context).load(therapist.imageUrl).into(holder.imageView)
        holder.callchip.setOnClickListener {
            val phone = therapist.phoneNumber
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            context.startActivity(intent)
        }
        holder.messagechip.setOnClickListener {
            val email = therapist.email
            val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
            context.startActivity(Intent.createChooser(intent, "Send email..."))
        }
        Log.d(TAG, "onBindViewHolder: therapist name = ${therapist.name}, therapist speciality = ${therapist.speciality}, therapist about = ${therapist.about}, therapist image URL = ${therapist.imageUrl}")
    }

    override fun getItemCount(): Int {
        return therapistList.size
    }

    fun setData(newData: List<Therapist>) {
        therapistList.clear()
        therapistList.addAll(newData)
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.therapistname)
        val speciality: TextView = itemView.findViewById(R.id.speciality)
        val imageView: ImageView = itemView.findViewById(R.id.photo)
        val callchip: Chip = itemView.findViewById(R.id.contact)
        val messagechip: Chip = itemView.findViewById(R.id.email)
        val details: TextView = itemView.findViewById(R.id.about)
    }
}