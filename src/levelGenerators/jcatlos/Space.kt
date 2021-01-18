package levelGenerators.jcatlos

/**
 * contains data about a space (rectangle) in a Level
 *
 * @param width of the space
 * @param height of the space
 * @param DL_Corner the down-left corner of the space
 */

open class Space(
        var width: Int = 0,
        var height: Int = 0,
        protected var DL_Corner: Coords
)

{
    /**
     * Getter for the down-left corner
     */
    fun DL_Corner(): Coords{
        return DL_Corner
    }

    /**
     * Getter for the down-right corner
     */
    fun DR_Corner(): Coords{
        return Coords(DL_Corner.x + width, DL_Corner.y)
    }

    /**
     * Getter for the up-left corner
     */
    fun UL_Corner(): Coords{
        return Coords(DL_Corner.x, DL_Corner.y + height)
    }

    /**
     * Getter for the up-right corner
     */
    fun UR_Corner(): Coords{
        return Coords(DL_Corner.x + width, DL_Corner.y + height)
    }
}