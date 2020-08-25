package levelGenerators.jcatlos

// Contains declarations and implementations of room classes and interfaces

import java.io.BufferedReader
import java.io.File
import java.lang.StringBuilder
import kotlin.random.Random

val BONUS_PROB = 0.10
val DIVIDE_PROB = 0.20
val DROP_PROB = 0.50

val DEFAULT_ROOM_HEIGHT = 8

val fileToType: Map<File, ROOM_TYPE> = mapOf(
        File("src/levelGenerators/jcatlos/blocks/start") to ROOM_TYPE.START,
        File("src/levelGenerators/jcatlos/blocks/finish") to ROOM_TYPE.FINISH,
        File("src/levelGenerators/jcatlos/blocks/divide") to ROOM_TYPE.DIVIDE,
        File("src/levelGenerators/jcatlos/blocks/challenge1") to ROOM_TYPE.CHALLENGE_H1,
        File("src/levelGenerators/jcatlos/blocks/challenge2") to ROOM_TYPE.CHALLENGE_H2,
        File("src/levelGenerators/jcatlos/blocks/bonus1") to ROOM_TYPE.BONUS_H1,
        File("src/levelGenerators/jcatlos/blocks/bonus2") to ROOM_TYPE.BONUS_H2,
        File("src/levelGenerators/jcatlos/blocks/empty1") to ROOM_TYPE.EMPTY_H1,
        File("src/levelGenerators/jcatlos/blocks/empty2") to ROOM_TYPE.EMPTY_H2
)

enum class SAFETY {
    SAFE,
    CHOOSE,
    UNSAFE
}

enum class ROOM_TYPE {
    START,
    FINISH,
    DIVIDE,
    CHALLENGE_H1,
    CHALLENGE_H2,
    BONUS_H1,
    BONUS_H2,
    EMPTY_H1,
    EMPTY_H2,
    NULL
}


class Room(
    val room : StringBuilder,
    val difficulty: Int,
    val safety: SAFETY,
    val type: ROOM_TYPE)
{
    fun height(): Int{
        return when(type){
            ROOM_TYPE.CHALLENGE_H1, ROOM_TYPE.BONUS_H2, ROOM_TYPE.EMPTY_H2 -> 1
            else -> 2
        }
    }

    fun applySafety(): Unit{
        var s = safety
        if(s == SAFETY.CHOOSE){
            if(Random.nextBoolean()){
                s = SAFETY.SAFE
            }
            else{
                s = SAFETY.UNSAFE
            }
        }
        if(s == SAFETY.SAFE){
            room.append("\n${"X".repeat(room.lines().last().length)}")
        }
        else{
            room.append("\n${room.lines().last()}")
        }
    }

}
/*
interface RoomH1 : Room {

}

class ChallengeRoomH1(h: Int = DEFAULT_ROOM_HEIGHT) : RoomH1{
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/challenge1")
    override val file: StringBuilder = RoomParser.parseRoom(dir.listFiles().random())
    override val difficulty = 50
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class BonusRoomH1(h: Int = DEFAULT_ROOM_HEIGHT) : RoomH1 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/bonus1")
    override val file: StringBuilder = RoomParser.parseRoom(dir.listFiles().random())
    override val difficulty = 50
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class EmptyRoomH1(h: Int = DEFAULT_ROOM_HEIGHT) : RoomH1{
    override val height = h
    override val file: StringBuilder = RoomParser.parseRoom(File("src/levelGenerators/jcatlos/blocks/empty/h1.txt"))
    override val difficulty = 50
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

interface RoomH2 : Room{

}

/*abstract class Hub(height: Int) : RoomH2(height){

}*/

class DivideHub(h: Int = DEFAULT_ROOM_HEIGHT * 2) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/divide")
    override val file: StringBuilder = RoomParser.parseRoom(dir.listFiles().random())
    override val difficulty = 50
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class StartRoom(h: Int = DEFAULT_ROOM_HEIGHT * 2) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/start")
    override val file: StringBuilder = RoomParser.parseRoom(dir.listFiles().random())
    override val difficulty = 50
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()

}

class FinishRoom(h: Int = DEFAULT_ROOM_HEIGHT * 2) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/finish")
    override val file: StringBuilder = RoomParser.parseRoom(dir.listFiles().random())
    override val difficulty = 50
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class ChallengeRoomH2(h: Int = DEFAULT_ROOM_HEIGHT * 2) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/challenge2")
    override val file: StringBuilder = RoomParser.parseRoom(dir.listFiles().random())
    override val difficulty = 50
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class BonusRoomH2(h: Int = DEFAULT_ROOM_HEIGHT * 2) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/bonus2")
    override val file: StringBuilder = RoomParser.parseRoom(dir.listFiles().random())
    override val difficulty = 50
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class EmptyRoomH2(h: Int = DEFAULT_ROOM_HEIGHT * 2) : RoomH2{
    override val height = h
    override val file: StringBuilder = RoomParser.parseRoom(File("src/levelGenerators/jcatlos/blocks/empty/h2.txt"))
    override val difficulty = 50
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

*/