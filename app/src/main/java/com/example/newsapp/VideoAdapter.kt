import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.Video
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class VideoAdapter(private val context: Context, private val videoList: MutableList<Video>):
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {
    private val storageRef = Firebase.storage.reference.child("videos")

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.videotitle)
        val video: VideoView = itemView.findViewById(R.id.videoView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.video_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videoList[position]
        holder.title.text = video.title


        // Load the video from the Firebase Storage URL into the VideoView
        holder.video.setVideoURI(Uri.parse(video.url))
        holder.video.setOnPreparedListener {
            holder.video.start()
        }
    }

    // Retrieve video data from Firebase Storage and update the videoList
    fun fetchVideoUrls() {
        storageRef.listAll().addOnSuccessListener { listResult ->
            val videoCount = listResult.items.size
            var loadedVideos = 0

            listResult.items.forEach { item ->
                item.downloadUrl.addOnSuccessListener { url ->
                    val video = Video(item.name, url.toString())
                    videoList.add(video)
                    loadedVideos++
                    if (loadedVideos == videoCount) {
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
}
