package com.example.summonerz

import kotlin.random.Random

class CreateMonster() {

    var monster = Monster(
        uid = null,
        name = "",
        type = "",
        time_of_scan = null,
        hp = null,
        mp = null,
        strength = null,
        speed = null,
        intelligence = null,
        scan_raw_value = ""
    )

    fun create_monster(raw_value:String?){

        this.monster.scan_raw_value = raw_value

        val randomValues = List(2){
            Random(raw_value!!.toInt())
            Random.nextFloat()
        }
        if(randomValues[0] < 1/2){
            this.monster.type = "common" // 50% chance
        }
        else if (randomValues[0] >= 1/2 && randomValues[0] < 9/12){
            this.monster.type = "uncommon" //25% chance
        }
        else if (randomValues[0] >= 9/12 && randomValues[0] < 21/24){
            this.monster.type = "rare" //12.5% chance
        }
        else if (randomValues[0] >= 21/24 && randomValues[0] < 45/48){
            this.monster.type = "epic" //6.25% chance
        }
        else if (randomValues[0] >= 45/48 && randomValues[0] < 92/94){
            this.monster.type = "master" //~~4.12% chance
        }
        else if (randomValues[0] >= 92/94){
            this.monster.type = "legendary" //~~2.13% chance
        }


    }


}
