package levelGenerators.jcatlos

import levelGenerators.jcatlos.Level

import engine.core.MarioLevelGenerator
import engine.core.MarioLevelModel
import engine.core.MarioTimer


/**
 * Overload of the MarioLevelGenerator
 *
 * @property state the [State] passed on initialization of the [Level] class responsible for creating the level
 */

class LevelGenerator : MarioLevelGenerator{
    var levelConfig = Config(
            challengeTag = "bullet",
            maxLength = 1000,
            maxHeight =  100,
            roomGenerator =  RandomRoomGenerator,
            introLength =  2,
            hardLength =  4,
            twistMultiplier =  1.5
    )


    override fun getGeneratorName(): String {
        return("jcatlosGenerator")
    }

    /**
     * Override for the Framework
     *
     * This function is called by the MarioAI Framework to recieve the generated level.
     * Creates an instance of Level class with a state (used to modify properties of the level)
     */

    override fun getGeneratedLevel(model: MarioLevelModel?, timer: MarioTimer?): String {
        var level: Level = Level(levelConfig)
        return(level.getLevel())
    }
}
