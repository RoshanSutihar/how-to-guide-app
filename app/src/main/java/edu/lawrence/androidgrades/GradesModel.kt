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


    fun addGuide(guideName: String, callback: (Boolean, String, Int?) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val guide = Guides(guideName = guideName)
                    val guideId = dao.insertGuide(guide)

                    loadGuides()

                    withContext(Dispatchers.Main) {
                        callback(true, "Guide added successfully.", guideId.toInt()) // Pass the newly inserted guideId
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        callback(false, "Failed to add guide: ${e.message}", null)
                    }
                }
            }
        }
    }




    fun getCommentsForGuide(guideId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val commentListForGuide = dao.getCommentsByGuideId(guideId)


                _narrationList.value = commentListForGuide


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

                    dao.insertListGuide(newListGuide)


                    val updatedComments = dao.getCommentsByGuideId(guideId)


                    _narrationList.value = updatedComments
                } catch (e: Exception) {
                    // Handle the error (optional)
                    Log.e("AddComment", "Error adding comment: ${e.message}")
                }
            }
        }
    }


}