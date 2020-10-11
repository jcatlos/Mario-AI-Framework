package levelGenerators.jcatlos

/*
    Class containing data about a space (rectangle) in a Level
 */

open class Space(
        var width: Int = 0,
        var height: Int = 0,
        protected var DL_Corner: Coords
)
{
    fun DL_Corner(): Coords{
        return DL_Corner
    }

    fun DR_Corner(): Coords{
        return Coords(DL_Corner.x + width, DL_Corner.y)
    }

    fun UL_Corner(): Coords{
        return Coords(DL_Corner.x, DL_Corner.y + height)
    }

    fun UR_Corner(): Coords{
        return Coords(DL_Corner.x + width, DL_Corner.y + height)
    }
}