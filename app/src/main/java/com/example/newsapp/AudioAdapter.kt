import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.Music
import com.example.newsapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AudioAdapter(private val musicList: MutableList<Music>) :
    RecyclerView.Adapter<AudioAdapter.ViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private var currentPlayingPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.music_item, parent, false)
        return ViewHolder(view)
    }
    private val database = FirebaseDatabase.getInstance().reference.child("music")

    var listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            musicList.clear()
            for (musicSnapshot in snapshot.children) {
                var music = musicSnapshot.getValue(Music::class.java)
                music?.let {
                   musicList.add(it)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = musicList[position]
        holder.title.text = music.title
        holder.cardview.setOnClickListener {
            val musicUrl = music.url
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(musicUrl))
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return musicList.size
    }

    private fun stopMediaPlayer() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        currentPlayingPosition = -1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.name_text_view)
        val toggleButton: ToggleButton = itemView.findViewById(R.id.toggle_button)
        val cardview: CardView = itemView.findViewById(R.id.musiccardview)
    }
}




