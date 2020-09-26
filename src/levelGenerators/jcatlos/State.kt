package levelGenerators.jcatlos

import kotlin.math.sqrt
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.random.Random

data class State(
        val difficultyIncrease: Int = 10,
        val levelDifficulty: Int = 100,
        var targetLength: Int = 10
){
    var expectedDifficulty: Int = 10
    var layerCount: Int = 1
    var maxHeight: Int = 100
    var maxLength: Int = 1000

    var sectionCount = 5

    var roomGenerator: RoomGenerator = RandomRoomGenerator

    init {
        // Here we are modifying the length so it's little bit varied (little bit of magic involved)
        val deviation = floor(java.util.Random().nextGaussian() * targetLength / 4).toInt()
        if(deviation.absoluteValue <= targetLength/3){
            targetLength += deviation
        }
        println("level length = $targetLength")
    }

    /*fun updateByLayer(layer: Layer){
        var sum: Float = 0F
        //currentDifficulty += layer.difficulty
        lastLayer = layer
        layerCount ++
        expectedDifficulty += difficultyIncrease
        //println("lenghth = $layerCount")
    }*/

    fun shouldEnd(): Boolean{
        /*var rnd = sqrt(java.util.Random().nextGaussian().absoluteValue)
        println("$rnd <= ${(layerCount).toFloat() / (avgLength*2)}")
        return rnd <= (layerCount).toFloat() / (avgLength*2)*/
        return layerCount > targetLength
    }

    /*fun layerSafety(): Boolean{
        println("expected diff = $expectedDifficulty, layer diff = ${lastLayer.difficulty}")
        return lastLayer.difficulty > expectedDifficulty
    }*/
}