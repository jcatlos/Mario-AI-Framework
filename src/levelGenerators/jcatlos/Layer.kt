package levelGenerators.jcatlos

/*
    Layer serves to represent a vertical "layer" of rooms.
    Layer can be initialised by an ArrayList<Room>.
    Rooms are ordered from top to bottom.
    When a RoomH1 is added, height increases by 1, if it is RoomH2,
    the height is increased by 2.
 */

class Layer(rooms: ArrayList<Room> = arrayListOf()){
    private var rooms: ArrayList<Room> = rooms
    private var difficultySum: Int = 0
    var difficulty: Int = 0
    var height = 0

    fun add(room: Room) : Unit{
        rooms.add(room)
        height += room.height()
        difficultySum += room.difficulty
        difficulty = difficultySum / rooms.count()
    }

    fun getRooms(): ArrayList<Room>{
        return rooms
    }

    operator fun iterator(): Iterator<Room>{
        return rooms.listIterator()
    }
}
