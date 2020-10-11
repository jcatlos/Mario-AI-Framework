package levelGenerators.jcatlos

/* A class for storing coordinates */

data class Coords(var x: Int, var y:Int) : Comparable<Coords> {
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