package levelGenerators.jcatlos

/**
 *  A class for storing coordinates
 *
 *  @param x the x coordinate
 *  @param y the y coordinate
 */

data class Coords(var x: Int, var y:Int) : Comparable<Coords> {

    /**
     * Override for the comparator
     *
     * Compares by the x coordinate. If equal, it uses the y coordinate.
     */
    override fun compareTo(other: Coords): Int{
        return when{
            x > other.x -> 1
            x < other.x -> -1
            y > other.y -> 1
            y < other.y -> -1
            else -> 0
        }
    }
}