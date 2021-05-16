package levelGenerators.randomMarioLevel

import engine.core.MarioLevelGenerator
import engine.core.MarioLevelModel
import engine.core.MarioTimer
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.random.Random

class LevelGenerator : MarioLevelGenerator{
    var cl: ClassLoader = this.javaClass.classLoader;

    override fun getGeneratedLevel(model: MarioLevelModel?, timer: MarioTimer?): String {
        val levelNum = Random.nextInt(13) + 2
        val levelName = "lvl-$levelNum.txt"
        var levelString = ""
        var levelReader = BufferedReader(InputStreamReader(cl.getResourceAsStream("original/$levelName")))
        levelReader.lines().forEach{
            levelString += it + "\n"
        }
        return levelString
        //return File("levels/original").listFiles().random().readText()
    }

    fun getTestLevel(): String{
        var levelString = ""
        var levelReader = BufferedReader(InputStreamReader(cl.getResourceAsStream("original/lvl-1.txt")))
        levelReader.lines().forEach{
            levelString += it + "\n"
        }
        return levelString
    }

    override fun getGeneratorName(): String {
        return "randomOriginalLevel"
    }

}