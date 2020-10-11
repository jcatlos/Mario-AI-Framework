package levelGenerators.jcatlos

import kotlin.math.sqrt
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.random.Random

/*
    Class containing all data accesible for an instance of a level
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

    fun updateByCoords(coords:Coords){
        if(coords.y > highestY) highestY = coords.y
        if(coords.x > highestX) highestX = coords.x
    }

    fun updateBySection(section: Section){
        sectionCount ++
        if(section.sectionSpace.UR_Corner().y > highestY) highestY = section.sectionSpace.UR_Corner().y
        if(section.sectionSpace.UR_Corner().x > highestX) highestX = section.sectionSpace.UR_Corner().x
    }

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