package levelGenerators.jcatlos

import kotlin.random.Random

/*
    This LayerDesigner is made to create levels of height 2 with top-down joining. This means,
    that there are 3 possible layers:
        1.) [2]:    there is 1 room with height = 2 (represented by LAYER_STATE.JOINED)
        2.) [1,1]:  there are 2 rooms on top of each other, both with height = 1
                    (represeted by LAYER_STATE.DIVIDED)
        3.) [0,1]:  the top room is joining into the bottom one (the joining is accomplished by
                    creating an empty room on top and letting the player to fall into the bottom one)
                    (represented by LAYER_STATE.JOINING)
 */

object BasicLayerDesigner: LevelDesigner{
    enum class LAYER_STATE {
        JOINED,
        DIVIDED,
        JOINING
    }

    private var currentState = LAYER_STATE.JOINED
    private val JOIN_PROB = 0.5

    override fun next(): Array<Int>{
        return when(currentState){
            LAYER_STATE.JOINED -> arrayOf(2)
            LAYER_STATE.JOINING -> {
                // Since the rooms are joining in this layer, they will be joined in the next one
                currentState = LAYER_STATE.JOINED
                arrayOf(0,1)
            }
            LAYER_STATE.DIVIDED ->{
                if(Random.nextFloat() <= JOIN_PROB){
                    join(0)
                }
                arrayOf(1,1)
            }
        }
    }

    override fun join(index: Int){
        currentState = LAYER_STATE.JOINING
    }

    override fun divide(index: Int){
        currentState = LAYER_STATE.DIVIDED
    }

    fun getState(): LAYER_STATE{
        return currentState
    }
}