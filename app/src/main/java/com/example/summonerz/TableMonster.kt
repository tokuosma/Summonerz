package com.example.summonerz

import androidx.lifecycle.LiveData
import androidx.room.*
import java.io.Serializable

@Entity(tableName = "monsters",
    indices = [Index(value = ["scan_raw_value"], unique = true)]
)
data class Monster(
    @PrimaryKey(autoGenerate = true) var uid : Int?,
    @ColumnInfo(name = "name") var name : String,
    @ColumnInfo(name = "type") var type : String,
    @ColumnInfo(name="icon") var icon:String, // since version 2
    @ColumnInfo(name = "time_of_scan") var time_of_scan : Long?,
    @ColumnInfo(name = "hp") var hp: Int?,
    @ColumnInfo(name = "mp") var mp: Int?,
    @ColumnInfo(name = "strength") var strength:Int?,
    @ColumnInfo(name = "speed") var speed:Int?,
    @ColumnInfo(name = "intelligence") var intelligence:Int?,
    @ColumnInfo(name = "scan_raw_value") var scan_raw_value:String?
) : Serializable

@Dao
interface MonsterDao{
    @Transaction
    @Insert
    suspend fun insert(monster: Monster)

    @Query("SELECT * FROM monsters")
    fun getAllMonsters(): LiveData<List<Monster>>

    @Query("SELECT * FROM monsters WHERE uid=:id")
    fun getMonsterById(id:Int): Monster

    @Query("SELECT * FROM monsters WHERE scan_raw_value=:scan_raw_value")
    fun getMonsterByScanValue(scan_raw_value: String?): Monster

    @Query("DELETE FROM monsters WHERE uid=:id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM monsters")
    suspend fun deleteAll()
}

data class MonsterPrototype(val name: String, val icon: String, val type: String)
data class MonsterStatsPrototype(val hp: Int?, val mp: Int?, val strength: Int?, val speed: Int?, val intelligence: Int?)
