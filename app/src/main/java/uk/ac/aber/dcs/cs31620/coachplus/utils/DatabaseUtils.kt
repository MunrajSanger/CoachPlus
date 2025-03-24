package uk.ac.aber.dcs.cs31620.coachplus.utils

import android.content.Context
import androidx.room.*
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// User Entity for Room
@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val password: String, // In production, hash this (e.g., with bcrypt)
    val role: String = "player"
)

// User DAO
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUser(email: String, password: String): User?

    @Query("DELETE FROM users WHERE email = :email")
    suspend fun deleteUser(email: String)
}

// Room Database
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "coachplus_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Utility object for database operations
object DatabaseUtils {
    private lateinit var userDao: UserDao

    fun initialize(context: Context) {
        userDao = AppDatabase.getDatabase(context).userDao()
    }

    suspend fun signUp(email: String, password: String, onComplete: (Boolean) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val user = User(email, password)
                userDao.insertUser(user)
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    suspend fun signOut(email: String) {
        withContext(Dispatchers.IO) {
            userDao.deleteUser(email)
        }
    }

    suspend fun getUser(email: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUser(email, password)
        }
    }
}