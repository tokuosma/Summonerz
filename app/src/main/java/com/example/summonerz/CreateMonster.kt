package com.example.summonerz

import android.content.Context
import android.util.Log
import java.io.IOException
import kotlin.random.Random
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.math.floor


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
                hp_mutation = null,
                mp_mutation = null,
                strength_mutation = null,
                speed_mutation = null,
                intelligence_mutation = null,
                scan_raw_value = ""
            )

            val monstersString = getDataFromAsset(context, "monsters.json")
            val monsterStatsString = getDataFromAsset(context, "monster_stats.json")
            val monsterHPMutationsString = getDataFromAsset(context, "hp_mutations.json")
            val monsterMPMutationsString = getDataFromAsset(context, "mp_mutations.json")
            val monsterStrengthMutationsString = getDataFromAsset(context, "strength_mutations.json")
            val monsterSpeedMutationsString = getDataFromAsset(context, "speed_mutations.json")
            val monsterIntelligenceMutationsString = getDataFromAsset(context, "intelligence_mutations.json")
            val gson = Gson()
            val listMonsterPrototypes = object : TypeToken<List<MonsterPrototype>>() {}.type
            val listMonsterMutationPrototypes = object : TypeToken<List<MonsterMutationPrototype>>() {}.type


            val monsterPrototypesList: List<MonsterPrototype> = gson.fromJson(monstersString, listMonsterPrototypes)
            val monsterHPMutationList: List<MonsterMutationPrototype> = gson.fromJson(monsterHPMutationsString, listMonsterMutationPrototypes)
            val monsterMPMutationList: List<MonsterMutationPrototype> = gson.fromJson(monsterMPMutationsString, listMonsterMutationPrototypes)
            val monsterStrengthMutationList: List<MonsterMutationPrototype> = gson.fromJson(monsterStrengthMutationsString, listMonsterMutationPrototypes)
            val monsterSpeedMutationList: List<MonsterMutationPrototype> = gson.fromJson(monsterSpeedMutationsString, listMonsterMutationPrototypes)
            val monsterIntelligenceMutationList: List<MonsterMutationPrototype> = gson.fromJson(monsterIntelligenceMutationsString, listMonsterMutationPrototypes)
            val monsterStatsMap: Map<String, MonsterStatsPrototype> = gson.fromJson(monsterStatsString, object : TypeToken<Map<String, MonsterStatsPrototype>>() {}.type)

            val random = Random(rawValueString.hashCode())

            val prototype = monsterPrototypesList[random.nextInt(monsterPrototypesList.size)]
            val hpMutationPrototype = monsterHPMutationList[random.nextInt(monsterHPMutationList.size)]
            val mpMutationPrototype = monsterMPMutationList[random.nextInt(monsterMPMutationList.size)]
            val strengthMutationPrototype = monsterStrengthMutationList[random.nextInt(monsterStrengthMutationList.size)]
            val speedMutationPrototype = monsterSpeedMutationList[random.nextInt(monsterSpeedMutationList.size)]
            val intelligenceMutationPrototype = monsterIntelligenceMutationList[random.nextInt(monsterIntelligenceMutationList.size)]
            Log.d("HP", hpMutationPrototype.toString())
            Log.d("MP", mpMutationPrototype.toString())
            Log.d("Strength", strengthMutationPrototype.toString())
            Log.d("Speed", speedMutationPrototype.toString())
            Log.d("Intelligence", intelligenceMutationPrototype.toString())


            monster.scan_raw_value = rawValueString
            //TODO: Nimen satunnaistus?
            monster.name = prototype.name
            monster.icon = prototype.icon
            monster.type = prototype.type
            monster.time_of_scan = Date().time
            //Stat generation from type
            val monsterStats = monsterStatsMap[monster.type]
            monster.hp = monsterStats?.hp
            monster.mp = monsterStats?.mp
            monster.strength = monsterStats?.strength
            monster.speed = monsterStats?.speed
            monster.intelligence = monsterStats?.intelligence
            //Mutations
            monster.hp = floor(hpMutationPrototype.coefficient!!.times(monster.hp!!.toFloat())).toInt()
            monster.hp_mutation = hpMutationPrototype.name
            Log.d("HP", monster.hp.toString())
            Log.d("HP", monster.hp_mutation.toString())
            monster.mp = floor(mpMutationPrototype.coefficient!!.times(monster.mp!!.toFloat())).toInt()
            monster.mp_mutation = mpMutationPrototype.name
            Log.d("MP", monster.mp.toString())
            Log.d("MP", monster.mp_mutation.toString())
            monster.strength = floor(strengthMutationPrototype.coefficient!!.times(monster.strength!!.toFloat())).toInt()
            monster.strength_mutation = strengthMutationPrototype.name
            Log.d("Strength", monster.strength.toString())
            Log.d("Strength", monster.strength_mutation.toString())
            monster.speed = floor(speedMutationPrototype.coefficient!!.times(monster.speed!!.toFloat())).toInt()
            monster.speed_mutation = speedMutationPrototype.name
            Log.d("Speed", monster.speed.toString())
            Log.d("Speed", monster.speed_mutation.toString())
            monster.intelligence = floor(intelligenceMutationPrototype.coefficient!!.times(monster.intelligence!!.toFloat())).toInt()
            monster.intelligence_mutation = intelligenceMutationPrototype.name
            Log.d("Intelligence", monster.intelligence.toString())
            Log.d("Intelligence", monster.intelligence_mutation.toString())


            return monster
        }
        private fun getDataFromAsset(context: Context?, fileName: String): String? {
            val jsonString: String
            jsonString = try {
                context?.assets?.open(fileName)?.bufferedReader()?.use { it.readText() } ?: ""
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }
    }




}
