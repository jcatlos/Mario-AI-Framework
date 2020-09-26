package levelGenerators.jcatlos

import java.io.File
import java.lang.Integer.max
import kotlin.random.Random
import kotlin.text.StringBuilder

data class MacroPair(var string: String, var prob: Int) {}

class Macro(var macros: ArrayList<MacroPair> = arrayListOf()){
    var length = 0
    init{
        for(macro in macros){
            length = max(length, macro.string.length)
        }
    }

    fun execute(): String{
        println("executing ${macros.toString()}")
        var rnd = Random.nextInt(100)
        var num: Int = macros[0].prob
        var index = 0
        while(rnd > num){
            println(index)
            rnd -= num
            index++
            if(index >= macros.size){
                return "-".repeat(length)
            }
            num = macros[index].prob
        }
        return macros[index].string.padEnd(macros[index].string.length, '-')
    }
    fun addPair(pair: MacroPair){
        length = max(length, pair.string.length)
        macros.add(pair)
    }
}


class RoomTemplate(
        var room: StringBuilder,
        var width: Int = room.split('\n')[0].length,
        var diff: Int,
        var tags: ArrayList<String>,
        var macros: Map<Coords, Macro>,
        var start: Coords,
        var finish: ArrayList<Coords>)
{
    var height: Int = room.lines().size

    init{
        println("Room Template start = $start")
        for(f in finish){
            println("Room Template finish = $f")
        }
    }

    fun generate(): Room{
        //println("Before")
        var newRoom = StringBuilder(room)
        //println(newRoom.toString())
        //println(macros.toString())
        var newDiff = diff
        for((coords, macro) in macros){
            var macroResult = macro.execute()
            newRoom.insert(coords.y * width + coords.x, macroResult)
            //newRoom[coords.y * width + coords.x] = macro.execute()
            var enemyCount = macroResult.count { c -> c == 'k' || c == 'g'}
            newDiff += enemyCount * 10
        }
        //println("After")
        //println(newRoom.toString())

        return Room(newRoom, newDiff, tags, start, finish)
    }
}


/*
interface RoomTemplateH1 : RoomTemplate {}

class ChallengeRoomTemplateH1(room: StringBuilder, diff: Int, macros: Map<Coords, Macro>) : RoomTemplateH1{
    override var room = room
    override var baseDifficulty = diff
    override var macros = macros

}

class BonusRoomTemplateH1(room: StringBuilder, diff: Int, macros: Map<Coords, Macro>) : RoomTemplateH1 {
    override var room = room
    override var baseDifficulty = diff
    override var macros = macros
}

class EmptyRoomTemplateH1(room: StringBuilder, diff: Int, macros: Map<Coords, Macro>) : RoomTemplateH1{
    override var room = room
    override var baseDifficulty = diff
    override var macros = macros

}



interface RoomTemplateH2 : RoomTemplate{}


class DivideHubTemplate(room: StringBuilder, diff: Int, macros: Map<Coords, Macro>) : RoomTemplateH2 {
    override var room = room
    override var baseDifficulty = diff
    override var macros = macros
}

class StartRoomTemplate(room: StringBuilder, diff: Int, macros: Map<Coords, Macro>) : RoomTemplateH2 {
    override var room = room
    override var baseDifficulty = diff
    override var macros = macros

}

class FinishRoomTemplate(room: StringBuilder, diff: Int, macros: Map<Coords, Macro>) : RoomTemplateH2 {
    override var room = room
    override var baseDifficulty = diff
    override var macros = macros
}

class ChallengeRoomTemplateH2(room: StringBuilder, diff: Int, macros: Map<Coords, Macro>) : RoomTemplateH2 {
    override var room = room
    override var baseDifficulty = diff
    override var macros = macros
}

class BonusRoomTemplateH2(room: StringBuilder, diff: Int, macros: Map<Coords, Macro>) : RoomTemplateH2 {
    override var room = room
    override var baseDifficulty = diff
    override var macros = macros
}

class EmptyRoomTemplateH2(room: StringBuilder, diff: Int, macros: Map<Coords, Macro>) : RoomTemplateH2{
    override var room = room
    override var baseDifficulty = diff
    override var macros = macros
}*/