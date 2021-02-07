package levelGenerators.jcatlos

/**
 *  Class containing information of an instance of a room (returned by RoomTemplate.generate() and RoomGenerator.generateToFitRoomspace)
 *
 *  @param room the [StringBuilder] representation of the room
 *  @param tags the list of tags of the room
 */

class Room(
    val room : Chunk,
    val tags: ArrayList<String>,
    var start: Coords,
    var finish: ArrayList<Coords>)
{

    /*fun applySafety(safe: Boolean): Unit{
        if("safe" in tags || (safe && "unsafe" !in tags)){
            room.append("\n${"X".repeat(room.lines().last().length)}")
        }
        else{
            room.append("\n${room.lines().last()}")
            difficulty += 30 // UNSAFE DIFFICULTY INCREASE
        }
    }*/

}
