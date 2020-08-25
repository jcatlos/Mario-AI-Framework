package levelGenerators.jcatlos

import java.io.File
import kotlin.random.Random

object RandomRoomGenerator: RoomGenerator{
    private val BONUS_PROB = 0.10
    private val DIVIDE_PROB = 0.20

    var roomTemplates: MutableMap<ROOM_TYPE, ArrayList<RoomTemplate>> = mutableMapOf()

    init {
        println("initialising Random Room Generator")
        for((dir, type) in fileToType){
            println("Reading room directory: $dir")
            roomTemplates[type] = arrayListOf()
            var templateArray: ArrayList<RoomTemplate> = arrayListOf()
            for(file in dir.listFiles()){
                println("    Reading file: ${file.name}")
                println("macros: ${RoomParser.fileToTemplate(file).macros.toString()}")
                roomTemplates[type]?.add(RoomParser.fileToTemplate(file))
                //templateArray.add(RoomParser.fileToTemplate(file))
            }
            //println("adding [$type] = ${templateArray}")
            //this.roomTemplates.put(type, templateArray.clone() as ArrayList<RoomTemplate>)
            println(this.roomTemplates.toString())
            templateArray.clear()
        }
        println(this.roomTemplates.toString())
    }

    private fun getRandomTemplate(type:ROOM_TYPE): RoomTemplate{
        //println("getting $type from ${roomTemplates.toString()}")
        var templates = roomTemplates[type]
        if(templates == null){
            return emptyRoomTemplate
        }
        else{
            return templates.random()
        }
    }

    private fun generateRoomH1(difficulty: Int): Room{
        val randomNumber = Random.nextFloat()
        return if(randomNumber <= BONUS_PROB){
            getRandomTemplate(ROOM_TYPE.BONUS_H1).generate()
        }
        else{
            getRandomTemplate(ROOM_TYPE.CHALLENGE_H1).generate()
        }
    }

    private fun generateRoomH2(difficulty: Int): Room{
        val randomNumber = Random.nextFloat()
        return when{
            randomNumber <= BONUS_PROB -> getRandomTemplate(ROOM_TYPE.BONUS_H2).generate()
            randomNumber <= DIVIDE_PROB -> getRandomTemplate(ROOM_TYPE.DIVIDE).generate()
            else -> getRandomTemplate(ROOM_TYPE.CHALLENGE_H2).generate()
        }
    }

    override fun generate(size: Int, difficulty: Int): Room{
        return when(size){
            0 -> getRandomTemplate(ROOM_TYPE.EMPTY_H1).generate()
            1 -> generateRoomH1(difficulty)
            2 -> generateRoomH2(difficulty)
            else -> getRandomTemplate(ROOM_TYPE.EMPTY_H2).generate()
        }
    }

    override fun generate(type: ROOM_TYPE): Room{
        return getRandomTemplate(type).generate()
    }


}