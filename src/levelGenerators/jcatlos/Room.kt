package levelGenerators.jcatlos

import java.io.BufferedReader
import java.io.File

val BONUS_PROB = 0.10
val DIVIDE_PROB = 0.20
val DROP_PROB = 0.50

interface Room{
    val height : Int
    val file : BufferedReader
    var prevRooms : ArrayList<Room>
    var nextRooms : ArrayList<Room>

    //abstract fun addFollowing(layer: ArrayList<Room>): Unit

}

interface RoomH1 : Room {

}

class ChallengeRoomH1(h: Int, u: Boolean) : RoomH1{
    override val height = h
    val upper = u
    val dir = File("src/levelGenerators/jcatlos/blocks/challenge1")
    override val file: BufferedReader = BufferedReader(dir.listFiles().random().reader())
    override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

interface RoomH2 : Room{

}

/*abstract class Hub(height: Int) : RoomH2(height){

}*/

class DivideHub(h: Int) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/divide")
    override val file: BufferedReader = BufferedReader(dir.listFiles().random().reader())
    override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class StartRoom(h: Int) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/start")
    override val file: BufferedReader = BufferedReader(dir.listFiles().random().reader())
    override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    override var nextRooms : ArrayList<Room> = ArrayList<Room>()

}

class FinishRoom(h: Int) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/finish")
    override val file: BufferedReader = BufferedReader(dir.listFiles().random().reader())
    override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class ChallengeRoomH2(h: Int) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/challenge2")
    override val file: BufferedReader = BufferedReader(dir.listFiles().random().reader())
    override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

class BonusRoomH2(h: Int) : RoomH2 {
    override val height = h
    val dir = File("src/levelGenerators/jcatlos/blocks/bonus")
    override val file: BufferedReader = BufferedReader(dir.listFiles().random().reader())
    override var prevRooms : ArrayList<Room> = ArrayList<Room>()
    override var nextRooms : ArrayList<Room> = ArrayList<Room>()
}

