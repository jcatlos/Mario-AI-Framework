package levelGenerators.jcatlos

import kotlin.random.Random


const val LEVEL_H2_HEIGHT = 16
const val LEVEL_H1_HEIGHT = 8

class RoomChooser(){
    val BONUS_PROB = 0.10
    val DIVIDE_PROB = 0.20
    val DROP_PROB = 0.50
    //val CHALLENGE_PROB = 0.90


    fun nextRoom(height: Int = LEVEL_H2_HEIGHT) : Room{
        val v = Random.nextFloat()
        if(v <= BONUS_PROB){
            return BonusRoomH2(height)
        }
        else{
            return ChallengeRoomH2(height)
        }
    }
}


class Level(length: Int){
    val chooser : RoomChooser = RoomChooser();
    val firstRoom : StartRoom = StartRoom(LEVEL_H2_HEIGHT)
    val lastRoom : FinishRoom = FinishRoom(LEVEL_H2_HEIGHT)

    //Layer first implementation
    var rooms = ArrayList<ArrayList<Room>>()

    init {
        println("starting initialization")
        var room : Room = firstRoom
        var divided: Boolean = false
        for (i in 0 until length){
            val new_room: Room = chooser.nextRoom(LEVEL_H2_HEIGHT)
            room.nextRooms.add(new_room)
            new_room.prevRooms.add(room)
            room = new_room
        }
        room.nextRooms.add(lastRoom)
        lastRoom.prevRooms.add(room)
        println("finishing initialization")
    }


    fun generateMap() : String{
        println("starting generation")
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
        }

        // Making the final string
        var levelStr : StringBuilder = StringBuilder()
        for(row in level){
            levelStr.append(row)
            levelStr.append("\n")
        }
        return levelStr.toString()
    }
}