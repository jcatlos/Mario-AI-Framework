package levelGenerators.jcatlos

// Contains declarations and implementations of room classes and interfaces

import java.io.BufferedReader
import java.io.File
import java.lang.StringBuilder

val BONUS_PROB = 0.10
val DIVIDE_PROB = 0.20
val DROP_PROB = 0.50

val DEFAULT_ROOM_HEIGHT = 8

interface Room{
    val height : Int
    val file : StringBuilder
    //var prevRooms : ArrayList<Room>
    //var nextRooms : ArrayList<Room>

    //abstract fun addFollowing(layer: ArrayList<Room>): Unit

}

interface RoomH1 : Room {

}

class ChallengeRoomH1(h: Int = DEFAULT_ROOM_HEIGHT, u: Boolean = true) : RoomH1{
    override val height = h
    val upper = u
    val dir = File("src/levelGenerators/jcatlos/blocks/challenge1")
    override val file: StringBuilder = RoomParser.parseRoom(dir.listFiles().random())
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class BonusRoomH1(h: Int = DEFAULT_ROOM_HEIGHT) : RoomH1 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/bonus1")
    override val file: StringBuilder = RoomParser.parseRoom(dir.listFiles().random())
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class EmptyRoomH1(h: Int = DEFAULT_ROOM_HEIGHT) : RoomH1{
    override val height = h
    override val file: StringBuilder = RoomParser.parseRoom(File("src/levelGenerators/jcatlos/blocks/empty/h1.txt"))
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
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class StartRoom(h: Int = DEFAULT_ROOM_HEIGHT * 2) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/start")
    override val file: StringBuilder = RoomParser.parseRoom(dir.listFiles().random())
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()

}

class FinishRoom(h: Int = DEFAULT_ROOM_HEIGHT * 2) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/finish")
    override val file: StringBuilder = RoomParser.parseRoom(dir.listFiles().random())
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class ChallengeRoomH2(h: Int = DEFAULT_ROOM_HEIGHT * 2) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/challenge2")
    override val file: StringBuilder = RoomParser.parseRoom(dir.listFiles().random())
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class BonusRoomH2(h: Int = DEFAULT_ROOM_HEIGHT * 2) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/bonus2")
    override val file: StringBuilder = RoomParser.parseRoom(dir.listFiles().random())
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class EmptyRoomH2(h: Int = DEFAULT_ROOM_HEIGHT * 2) : RoomH2{
    override val height = h
    override val file: StringBuilder = RoomParser.parseRoom(File("src/levelGenerators/jcatlos/blocks/empty/h2.txt"))
    //override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    //override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

