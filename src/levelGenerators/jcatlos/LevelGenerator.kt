package levelGenerators.jcatlos

import levelGenerators.jcatlos.Level

import engine.core.MarioLevelGenerator
import engine.core.MarioLevelModel
import engine.core.MarioTimer


class LevelGenerator : MarioLevelGenerator{
    var layerBuilder = LayerBuilder(BasicLayerDesigner, RandomRoomGenerator)

    override fun getGeneratorName(): String {
        return("jcatlosGenerator")
    }

    override fun getGeneratedLevel(model: MarioLevelModel?, timer: MarioTimer?): String {

        var level: Level = Level(6, layerBuilder)
        return(level.generateMap())
    }
}
