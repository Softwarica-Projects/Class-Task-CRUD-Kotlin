package np.com.softwarica.week8

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import np.com.softwarica.week8.constants.Constants
import np.com.softwarica.week8.databinding.ActivityPostBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            deletePost();
        };  findViewById<FloatingActionButton>(R.id.edit).setOnClickListener {
            editPost();
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getPost()
    }

    private fun getPost() {
        val id = intent.getStringExtra(Constants.PARAM_ID) ?: return
        Firebase.firestore
            .collection(Constants.POST)
            .document(id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    binding.description.text = document.getString(Constants.POST_DESCRIPTION) ?: ""
                    binding.progress.visibility = View.GONE
                } else {
                    showError()
                }
            }
    }
    private fun deletePost() {
        val id = intent.getStringExtra(Constants.PARAM_ID) ?: return
        binding.progress.visibility = View.VISIBLE
        val documentRef = Firebase.firestore.collection(Constants.POST).document(id).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                binding.progress.visibility = View.GONE;
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
    }
    private fun editPost() {
        val id = intent.getStringExtra(Constants.PARAM_ID) ?: return
        val intent = Intent(this, CreatePostActivity::class.java)
        intent.putExtra(Constants.PARAM_ID, id)
        startActivity(intent)
    }

    private fun showError() {
        Toast.makeText(this, "Failed to load data.", Toast.LENGTH_SHORT).show()
    }
}