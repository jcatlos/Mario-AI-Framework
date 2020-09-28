package levelGenerators.jcatlos

class Room(
    val room : StringBuilder,
    var difficulty: Int,
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
