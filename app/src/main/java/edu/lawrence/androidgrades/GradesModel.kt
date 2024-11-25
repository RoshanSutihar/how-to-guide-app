package edu.lawrence.androidgrades

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GradesModel(application: Application): AndroidViewModel(application) {
    lateinit var dao : GradesDao
    val guides = mutableStateOf<List<Guides>?>(null)
    val firstGuide = mutableStateOf<Guides?>(null)

    val firstNarration = mutableStateOf<listGuides?>(null)
    private val _narrationList = MutableStateFlow<List<listGuides>>(emptyList())
    val narrationList: StateFlow<List<listGuides>> = _narrationList

    init {
        val db = Room.databaseBuilder(
            application,
            GradesDatabase::class.java, "grades"
        ).build()
        dao = db.gradesDao()

        viewModelScope.launch {
            loadGuides()

        }
    }

    fun loadGuides() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val guideList = dao.getAllGuides()
                guides.value = guideList

                if (guideList.isNotEmpty()) {
                    firstGuide.value = guideList[0]
                }



            }
        }
    }


    fun addGuide(guideName: String, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {

                    val guide = Guides(guideName = guideName)
                    dao.insertGuide(guide)

                    loadGuides()


                    callback(true, "Guide added successfully.")
                } catch (e: Exception) {

                    callback(false, "Failed to add guide: ${e.message}")
                }
            }
        }
    }

    fun getCommentsForGuide(guideId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Fetch the comments from your DAO/repository
                val commentListForGuide = dao.getCommentsByGuideId(guideId)

                // Update narrationList with the fetched comments
                _narrationList.value = commentListForGuide

                // Optionally, set the first narration (if any)
                if (commentListForGuide.isNotEmpty()) {
                    firstNarration.value = commentListForGuide[0]
                }
            }
        }
    }


    fun addCommentToGuide(guideId: Int, heading: String, narration: String) {
        viewModelScope.launch {
            val newListGuide = listGuides(guideId = guideId, heading = heading, narration = narration)
            withContext(Dispatchers.IO) {
                try {
                    // Insert the new comment into the database
                    dao.insertListGuide(newListGuide)

                    // Fetch the updated list of comments for the guide
                    val updatedComments = dao.getCommentsByGuideId(guideId)

                    // Update the narrationList with the latest comments
                    _narrationList.value = updatedComments
                } catch (e: Exception) {
                    // Handle the error (optional)
                    Log.e("AddComment", "Error adding comment: ${e.message}")
                }
            }
        }
    }


}