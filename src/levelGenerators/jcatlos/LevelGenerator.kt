package levelGenerators.jcatlos

import levelGenerators.jcatlos.Level

import engine.core.MarioLevelGenerator
import engine.core.MarioLevelModel
import engine.core.MarioTimer


/*
    Overload of the MarioLevelGenerator
        * getGeneratedLevel() is responsible for creating the string containing the level
            - Creates an instance of Level class with a state (used to modify properties of the level)
 */

class LevelGenerator : MarioLevelGenerator{
    var state = State(15, 100, 1)
    //var layerBuilder = LayerBuilder(BasicLayerDesigner, RandomRoomGenerator, state)


    override fun getGeneratorName(): String {
        return("jcatlosGenerator")
    }

    override fun getGeneratedLevel(model: MarioLevelModel?, timer: MarioTimer?): String {
        var level: Level = Level(state)
        return(level.level.toString())
    }
}
