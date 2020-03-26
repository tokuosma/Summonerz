package com.example.summonerz

import androidx.room.*

@Entity(tableName = "monsters",
    indices = [Index(value = ["scan_raw_value"], unique = true)]
)
data class Monster(
    @PrimaryKey(autoGenerate = true) var uid : Int?,
    @ColumnInfo(name = "name") var name : String?,
    @ColumnInfo(name = "type") var type : String?,
    @ColumnInfo(name = "time_of_scan") var time_of_scan : Long?,
    @ColumnInfo(name = "hp") var hp: Int?,
    @ColumnInfo(name = "mp") var mp: Int?,
    @ColumnInfo(name = "strength") var strength:Int?,
    @ColumnInfo(name = "speed") var speed:Int?,
    @ColumnInfo(name = "intelligence") var intelligence:Int?,
    @ColumnInfo(name = "scan_raw_value") var scan_raw_value:String?
)

@Dao
interface MonsterDao{
    @Transaction
    @Insert
    fun insert(reminder : Monster):Long

    @Query("SELECT * FROM monsters")
    fun getAllMonsters(): List<Monster>

    @Query("SELECT * FROM monsters WHERE uid=:id")
    fun getMonsterById(id:Int): Monster

    @Query("DELETE FROM monsters WHERE uid=:id")
    fun delete(id:Int)
}