package levelGenerators.jcatlos

/* Level creation follows these steps:
    1.) Create starting and finishing rooms
    2.) Use LayerBuilder class to create specified amount of layers of rooms
    3.) Connect created layers using information:
        // (Not true anymore) I.)     LayerBuilder returns rooms in layers in reverse order (from bottom to top) -
        II.)    Rooms at the lowest layer create a ground level - no "underground" rooms (yet)
        III.)   RoomH2.height = 2 * RoomH1.height (Pridat nakres ako funguju susedne miestnosti)
 */

import com.sun.jdi.ArrayReference
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.random.Random
import kotlin.reflect.typeOf


const val LEVEL_H2_HEIGHT = 16
const val LEVEL_H1_HEIGHT = 8


class Level(length: Int, state: State, layerBuilder: LayerBuilder){

    /*class RoomChooser(){
        private val BONUS_PROB = 0.10
        private val DIVIDE_PROB = 0.20
        private val DROP_PROB = 0.50
        //val CHALLENGE_PROB = 0.90

        fun nextRoom(prevRoom: Room) : Room?{
            val v = Random.nextFloat()
            var room: Room?
            if (prevRoom is DivideHub){

            }
            else if(prevRoom.height == LEVEL_H2_HEIGHT && prevRoom){
                if(v <= BONUS_PROB){
                    room = BonusRoomH2(LEVEL_H2_HEIGHT)
                }
                else if(v <= DIVIDE_PROB){
                    println("divide")
                    room = DivideHub(LEVEL_H2_HEIGHT)
                }
                else{
                    room = ChallengeRoomH2(LEVEL_H2_HEIGHT)
                }
            }
            else{
                if(v <= BONUS_PROB){
                    room = BonusRoomH1(LEVEL_H1_HEIGHT)
                }
                else if(v <= DROP_PROB){
                    room = null
                }
                else{
                    room = ChallengeRoomH1(LEVEL_H1_HEIGHT)
                }
            }
            if(room!=null){
                prevRoom.nextRooms.add(room)
                room.prevRooms.add(room)
            }
            return room
        }
    }*/

    //val chooser : RoomChooser = RoomChooser();

    var layers: ArrayList<Layer> = ArrayList()

    //val firstRoom : StartRoom = StartRoom()
    //val lastRoom : FinishRoom = FinishRoom()


    init {
        println("starting initialization")

        /*var layer1: ArrayList<Room> = ArrayList()
        var layer2: ArrayList<Room> = ArrayList()
        var current = layer1
        var other = layer2
        current.add(firstRoom)
        //var room : Room = firstRoom
        //var divided: Boolean = false
        for (i in 0 until length){
            print(current.size)
            print(" round ")
            println(i)
            for(r in current){
                val new_room: Room? = chooser.nextRoom(r)
                if(new_room != null) other.add(new_room)
            }
            if(current == layer1){
                current = layer2
                layer1.clear()
                other = layer1
            }
            else{
                current = layer1
                layer2.clear()
                other = layer2
            }
        }
        for(r in current){
            r.nextRooms.add(lastRoom)
            lastRoom.prevRooms.add(r)
        }

        // Making the final string
        var levelStr : StringBuilder = StringBuilder()
        for(row in level){
            levelStr.append(row)
            levelStr.append("\n")
        }
        return levelStr.toString()
        */

        layers.add(Layer(arrayListOf(layerBuilder.createStart())))

        while(!state.shouldEnd()){
            println("add layer")
            var layer = layerBuilder.next()
            layers.add(layer)
            state.updateByLayer(layer)
        }
        /*for (i in 0 until length){
            layers.add(layerBuilder.next())
        }*/

        var lastRoom = Layer(arrayListOf(layerBuilder.createFinish()))
        layers.add(lastRoom)
        state.updateByLayer(lastRoom)
        println("finishing initialization")
    }


    fun generateMap() : String{
        //println("starting generation")
        var level : Array<StringBuilder> = Array<StringBuilder>(size = LEVEL_H2_HEIGHT, init =  {StringBuilder()})
        /*for(i in 0 until LEVEL_HEIGHT){
            var room : Room = firstRoom
            var j = 0
            while(room != lastRoom){
                level[i].append(room.file.readLine())
                room = rooms[++j][0]
            }
            level[i].append(lastRoom.file.readLine())
        }*/
        //var room : Room = firstRoom

        /*
        Odstranene pri zmene generacie levelov - branching
        var current = ArrayList<Room>()
        current.add(firstRoom)
        var next = ArrayList<Room>()
        while(current.isNotEmpty() || next.isNotEmpty()){
            var it = 0
            var roomCounter = 0
            //Adding current layer to level
            while(roomCounter<current.size){
                //Adding neighbors to next
                for(it in 0 until current[roomCounter].nextRooms.size){
                    next.add(current[roomCounter].nextRooms[it])
                }
                //Adding room to level
                val old_it = it
                while(it-old_it < current[roomCounter].height){
                    level[it].append(current[roomCounter].file.readLine())
                    it++
                }
                roomCounter++
            }
            current.clear()
            current.addAll(next)
            next.clear()
        }*/

        var maxHeight = 0
        for(layer in layers){
            maxHeight = max(maxHeight, layer.height)
        }

        //var emptyRoomString = EmptyRoomH1().file.toString()

        var layersRows: ArrayList<ArrayList<String>> = ArrayList()
        for(layer in layers){
            var layerRows: ArrayList<String> = ArrayList()
            /*for(i in 0 until maxHeight - layer.height){
                layerStringBuilder.append(emptyRoomString)
            }*/
            for(room in layer.getRooms().reversed()){
                for(row in room.room.lines().reversed()){
                    layerRows.add(row)
                }
            }
            layersRows.add(layerRows)
        }

        // Pospajat Vrstvy
        var allFinished = false
        var iterator: Int = 0
        var levelStringBuilder = StringBuilder()
        while(!allFinished){
            var row = StringBuilder()
            allFinished = true
            for(i in 0 until layersRows.size){
                if(iterator < layersRows[i].size){
                    row.append(layersRows[i][iterator])
                    allFinished = false
                }
                else{
                    row.append("CCCCCCCCCCCCCCCCCCCCCCCCC") //for debugging
                    //row.append("-------------------------")
                }
            }
            if(!allFinished){
                levelStringBuilder.insert(0, "$row\n")
            }
            iterator++
        }
        print(levelStringBuilder.toString())
        return levelStringBuilder.toString()
    }
}