package edu.lawrence.androidgrades

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity
data class Guides(
    @PrimaryKey(autoGenerate = true) val guideId : Int = 0,
    val guideName: String
)

@Entity
data class listGuides(
    @PrimaryKey(autoGenerate = true) val listId: Int = 0,
    val guideId: Int,
    val heading: String,
    val narration: String
)

@Dao
interface GradesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGuide(guide: Guides): Long

    @Query("SELECT * FROM Guides ORDER BY guideId ASC")
    suspend fun getAllGuides(): List<Guides>

    @Query("SELECT * FROM listGuides WHERE guideId = :guideId ORDER BY listId ASC")
    suspend fun getAllList(guideId: Int): List<listGuides>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListGuide(listGuide: listGuides)

    @Query("SELECT * FROM listGuides WHERE guideId = :guideId")
    fun getCommentsByGuideId(guideId: Int): List<listGuides>
}

@Database(entities = [Guides::class,listGuides::class], version = 1)
abstract class GradesDatabase : RoomDatabase() {
    abstract fun gradesDao() : GradesDao
}

