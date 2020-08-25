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
    var lastLayer: Layer = Layer()
    var currentDifficulty: Float = 0F
    var layerCount: Int = 1
    init {
        // Here we are modifiing the length so it's little bit varied (little bit of magic involved)
        val deviation = floor(java.util.Random().nextGaussian() * targetLength / 4).toInt()
        if(deviation.absoluteValue <= targetLength/3){
            targetLength += deviation
        }
        println("level length = $targetLength")
    }

    fun updateByLayer(layer: Layer){
        var sum: Float = 0F
        for(room in layer){
            sum += room.difficulty
        }
        currentDifficulty += sum/layer.getRooms().size
        lastLayer = layer
        layerCount ++
    }

    fun shouldEnd(): Boolean{
        /*var rnd = sqrt(java.util.Random().nextGaussian().absoluteValue)
        println("$rnd <= ${(layerCount).toFloat() / (avgLength*2)}")
        return rnd <= (layerCount).toFloat() / (avgLength*2)*/
        return layerCount > targetLength
    }
}