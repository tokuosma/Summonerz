package com.example.summonerz

import android.content.Context
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.random.Random
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.random.nextUInt


class CreateMonster() {

    companion object {

        private const val RANDOM_MAX_VALUE = 5000000
        fun create_monster(context:Context?, rawValueString:String?, rawValueBytes: ByteArray?): Monster {


            var monster = Monster(
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

            var jsonString = getJsonDataFromAsset(context)
            val gson = Gson()
            val listMonsterPrototypes = object : TypeToken<List<MonsterPrototype>>() {}.type

            var monsterPrototypes: List<MonsterPrototype> = gson.fromJson(jsonString, listMonsterPrototypes)

            val byteBuffer= ByteBuffer.wrap(rawValueBytes)
            val seed = byteBuffer.long
            var random = Random(seed)

            val nextInt = random.nextInt(RANDOM_MAX_VALUE)
            val index = nextInt.rem( monsterPrototypes.size)
            var prototype = monsterPrototypes[index.toInt()]

            monster.scan_raw_value = rawValueString
            monster.name = prototype.name
            monster.icon = prototype.icon
            monster.type = prototype.type

            return monster
        }
        private fun getJsonDataFromAsset(context: Context?): String? {
            val jsonString: String
            jsonString = try {
                context?.assets?.open("monsters.json")?.bufferedReader()?.use { it.readText() } ?: ""
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }

    }




}
