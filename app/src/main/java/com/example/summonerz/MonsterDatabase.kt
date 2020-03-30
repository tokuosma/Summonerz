package com.example.summonerz

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

//
//@Database(entities = [Monster::class], version = 1)
//abstract class MonsterDatabase : RoomDatabase(){
//    abstract fun monsterDao() : MonsterDao
//}
//

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Monster::class], version = 1, exportSchema = false)
abstract class MonsterDatabase : RoomDatabase() {

    abstract fun monsterDao(): MonsterDao

    private class MonsterDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val monsterDao = database.monsterDao()
//                    monsterDao.deleteAll()
                    val date = Date()
                    // Add sample words.
                    val monster = Monster(
                        uid = null,
                        name = "Bad Gnome 69",
                        type = "common",
                        icon = "ic_bad_gnome",
                        hp = null,
                        scan_raw_value = "666",
                        intelligence = null,
                        speed = null,
                        time_of_scan = date.time,
                        mp = null,
                        strength = null
                    )
                    monsterDao.insert(monster)
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MonsterDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MonsterDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MonsterDatabase::class.java,
                    "monster_database"
                )
//                  .addCallback(MonsterDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }


    }
}
