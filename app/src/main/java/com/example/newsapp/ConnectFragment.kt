import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.Therapist
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ConnectFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TherapistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_connect, container, false)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE


        recyclerView = view.findViewById(R.id.homerecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = TherapistAdapter(requireContext(), mutableListOf())
        recyclerView.adapter = adapter
        FirebaseDatabase.getInstance().reference.child("therapist").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Data is loaded, update the adapter and hide the progress bar
                adapter.setData(snapshot.children.mapNotNull { it.getValue<Therapist>() })
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error here
            }
        })


        return view
    }

     override fun onDestroyView() {
        super.onDestroyView()
        FirebaseDatabase.getInstance().reference.child("therapist").removeEventListener(adapter.listener)
        view?.findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.GONE
    }

}
