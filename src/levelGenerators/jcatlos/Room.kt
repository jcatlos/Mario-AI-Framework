package levelGenerators.jcatlos


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
    var difficulty: Int,
    val tags: ArrayList<String>,
    val type: ROOM_TYPE,
    var start: Coords,
    var finish: ArrayList<Coords>)
{
    fun height(): Int{
        return when(type){
            ROOM_TYPE.CHALLENGE_H1, ROOM_TYPE.BONUS_H2, ROOM_TYPE.EMPTY_H2 -> 1
            else -> 2
        }
    }

    fun applySafety(safe: Boolean): Unit{
        if("safe" in tags || (safe && "unsafe" !in tags)){
            room.append("\n${"X".repeat(room.lines().last().length)}")
        }
        else{
            room.append("\n${room.lines().last()}")
            difficulty += 30 // UNSAFE DIFFICULTY INCREASE
        }
    }

}
