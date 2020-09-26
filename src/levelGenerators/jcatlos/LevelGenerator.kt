package levelGenerators.jcatlos

import levelGenerators.jcatlos.Level

import engine.core.MarioLevelGenerator
import engine.core.MarioLevelModel
import engine.core.MarioTimer


class LevelGenerator : MarioLevelGenerator{
    var state = State(15, 100, 8)
    //var layerBuilder = LayerBuilder(BasicLayerDesigner, RandomRoomGenerator, state)


    override fun getGeneratorName(): String {
        return("jcatlosGenerator")
    }

    override fun getGeneratedLevel(model: MarioLevelModel?, timer: MarioTimer?): String {
        var level: Level = Level(10, state)
        return(level.level.toString())
    }
}
