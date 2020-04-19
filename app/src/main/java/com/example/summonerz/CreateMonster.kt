package com.example.summonerz

import android.content.Context
import android.util.Log
import java.io.IOException
import kotlin.random.Random
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class CreateMonster() {

    companion object {

        private const val RANDOM_MAX_VALUE = 5000000
        fun createMonster(context: Context?, rawValueString: String?): Monster {
            val monster = Monster(
                uid = null,
                name = "",
                type = "",
                icon = "",
                time_of_scan = null,
                hp = null,
                mp = null,
                strength = null,
                speed = null,
                intelligence = null,
                scan_raw_value = ""
            )

            val jsonString = getMonsterDataFromAsset(context)
            val monsterStatsString = getMonsterStatsDataFromAsset(context)
            val gson = Gson()
            val listMonsterPrototypes = object : TypeToken<List<MonsterPrototype>>() {}.type


            val monsterPrototypesList: List<MonsterPrototype> = gson.fromJson(jsonString, listMonsterPrototypes)
            var monsterStatsMap: Map<String, MonsterStatsPrototype> = gson.fromJson(monsterStatsString, object : TypeToken<Map<String, MonsterStatsPrototype>>() {}.type)
            val random = Random(rawValueString.hashCode())
            val prototype = monsterPrototypesList[random.nextInt(monsterPrototypesList.size)]


            monster.scan_raw_value = rawValueString
            //TODO: Nimen satunnaistus?
            monster.name = prototype.name
            monster.icon = prototype.icon
            monster.type = prototype.type
            monster.time_of_scan = Date().time
            //TODO: Statsien generointi. Base arvot jsoniin?
            val monsterStats = monsterStatsMap[monster.type]
            monster.hp = monsterStats?.hp
            monster.mp = monsterStats?.mp
            monster.strength = monsterStats?.strength
            monster.speed = monsterStats?.speed
            monster.intelligence = monsterStats?.intelligence

            return monster
        }
        private fun getMonsterDataFromAsset(context: Context?): String? {
            val jsonString: String
            jsonString = try {
                context?.assets?.open("monsters.json")?.bufferedReader()?.use { it.readText() } ?: ""
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }
        //TODO: Jsonin lukufunktioiden yhdistäminen?
        private fun getMonsterStatsDataFromAsset(context: Context?): String? {
            val jsonString: String
            jsonString = try {
                context?.assets?.open("monster_stats.json")?.bufferedReader()?.use { it.readText() } ?: ""
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }

    }




}
