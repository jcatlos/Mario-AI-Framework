package levelGenerators.jcatlos

import kotlin.math.sqrt
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.random.Random

/**
 * contains all data accesible for an instance of a level
 *
 * @param difficultyIncrease the increase in difficulty by adding a [Section]  *OUTDATED*
 * @param levelDifficulty the maximum difficulty of a section in the level *OUTDATED*
 * @param levelLength how many sections should be in the level
 * @property highestX the highest point of the generated level so far
 * @property highestY the right-most point of the generated level so far
 * @property maxHeight the maximum height of the level
 * @property maxLength the maximum length of the level (in tiles)
 * @property sectionCount the count of the sections in the level so far
 */

data class State(
        val difficultyIncrease: Int = 10,
        val levelDifficulty: Int = 100,
        var levelLength: Int = 7
){
    var highestX = 0
    var highestY = 0
    var maxHeight: Int = 100
    var maxLength: Int = 1000

    var sectionCount = 0

    /**
     * Updates the variables according to the provided point
     *
     * @param coords the coordinates of the point that could be the highest or right-most in the current point of generation
     */
    fun updateByCoords(coords:Coords){
        if(coords.y > highestY) highestY = coords.y
        if(coords.x > highestX) highestX = coords.x
    }

    /**
     * Updates the variables according to the provided [Section]
     *
     * @param section the added [Section]
     */
    fun updateBySection(section: Section){
        sectionCount ++
        if(section.sectionSpace.UR_Corner().y > highestY) highestY = section.sectionSpace.UR_Corner().y
        if(section.sectionSpace.UR_Corner().x > highestX) highestX = section.sectionSpace.UR_Corner().x
    }


    /**
     * Check whether the generation should stop
     *
     * @return whether the level generation should stop
     */
    fun shouldEnd(): Boolean{
        /*var rnd = sqrt(java.util.Random().nextGaussian().absoluteValue)
        println("$rnd <= ${(layerCount).toFloat() / (avgLength*2)}")
        return rnd <= (layerCount).toFloat() / (avgLength*2)*/
        return sectionCount > levelLength
    }

    /*fun layerSafety(): Boolean{
        println("expected diff = $expectedDifficulty, layer diff = ${lastLayer.difficulty}")
        return lastLayer.difficulty > expectedDifficulty
    }*/
}