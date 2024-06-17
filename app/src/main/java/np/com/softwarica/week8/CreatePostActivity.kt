package np.com.softwarica.week8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import np.com.softwarica.week8.constants.Constants
import np.com.softwarica.week8.databinding.ActivityCreatePostBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class CreatePostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.save.setOnClickListener {
            if(!binding.description.text.isNullOrBlank()) {
                createPost()
            }
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

    private fun createPost() {
        val post = hashMapOf(
            Constants.POST_DESCRIPTION to binding.description.text.toString(),
            Constants.POST_TIMESTAMP to FieldValue.serverTimestamp(),
        )
        Firebase.firestore
            .collection(Constants.POST)
            .add(post)
            .addOnSuccessListener {
                finish()
            }
            .addOnFailureListener {
                showError()
            }
    }

    private fun showError() {
        Toast.makeText(this, "Failed to save data.", Toast.LENGTH_SHORT).show()
    }
}