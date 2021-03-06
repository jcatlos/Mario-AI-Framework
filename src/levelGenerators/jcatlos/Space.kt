package levelGenerators.jcatlos

/**
 * contains data about a space (rectangle) in a Level
 *
 * @param width of the space
 * @param height of the space
 * @param DL_Corner the down-left corner of the space
 */

open class Space(
        open var width: Int = 0,
        open var height: Int = 0,
        protected open var UL_Corner: Coords)

{
    /**
     * Getter for the down-left corner
     */
    fun DL_Corner(): Coords{
        return Coords(UL_Corner.x, UL_Corner.y + height)
    }

    /**
     * Getter for the down-right corner
     */
    fun DR_Corner(): Coords{
        return Coords(UL_Corner.x + width, UL_Corner.y + height)
    }

    /**
     * Getter for the up-left corner
     */
    fun UL_Corner(): Coords{
        return UL_Corner.copy()
    }

    /**
     * Getter for the up-right corner
     */
    fun UR_Corner(): Coords{
        return Coords(UL_Corner.x + width, UL_Corner.y)
    }
}