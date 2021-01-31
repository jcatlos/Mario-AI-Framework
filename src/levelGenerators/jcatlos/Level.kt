package levelGenerators.jcatlos

import com.sun.jdi.ArrayReference
import java.nio.channels.NotYetConnectedException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.random.Random
import kotlin.reflect.typeOf

/**
*   Level class is responsible for the creation of the level
*
 *
 * @param state [State] the only information provided by LevelGenerator
*
*/

class Level(var state: State){
    var levelChunk = Chunk()
    var level = StringBuilder()

    init{
        // Initialize levelColumns - create an empty level
        for(y in 0 until state.maxHeight){
            for(x in 0 until state.maxLength){
                levelChunk.append('.')
            }
            levelChunk.append('\n')
            //levelColumns.add(Array(state.maxHeight) { _ -> '.'})
        }

        var startRoom: Room = SharedData.getRoomTemplatesByTags(arrayListOf("start")).random().generate()
        //var startRoom: Room = SharedData.roomGenerator.generateToFitRoomspace(LevelConnector.calculateFreeRoomSpace(this, Coords(0,5))!!, arrayListOf("start"))
        println("startroom = ${startRoom.room.getAsStringBuilder()}")
        state.updateByCoords(Coords(startRoom.room.width, startRoom.room.height))

        levelChunk.emplaceChunk(startRoom.room, Coords(0,30))
        var exitCoords:Coords = startRoom.finish.first()
        exitCoords.y += 30
        println("start finish coords = ${startRoom.finish.first()}");

        //println("exit: ${findLowestExit(this)}")

        while(!state.shouldEnd()){
            println("next iteration ")
            println("exit coords = $exitCoords")
            print(levelChunk.getAsStringBuilder())
            if(exitCoords == null) break
            println("removing exit at $exitCoords")
            levelChunk.content[exitCoords.x][exitCoords.y] = '-'
            //var exitCoords = findLowestExit(this)
            var sectionTemplate = SharedData.SectionTemplates.random()
            //println("chosen ${sectionTemplate.roomSpaces.size}")
            println("\t exit coords: $exitCoords")
            var rs = LevelConnector.calculateFreeRoomSpace(this, exitCoords)
            if(rs == null) break

            println("rs entry point: ${rs.startAnchor}")
            var section = sectionTemplate.generate(rs.UL_Corner())
            state.updateBySection(section)

            var entryPoint = exitCoords;
            entryPoint.x++;
            //entryPoint.y -= section.startPoint.y - section.sectionSpace.UL_Corner().y
            levelChunk.emplaceChunk(section.section, section.sectionSpace.ULByEntryPoint(entryPoint))
            // Remove exit from generated map

            var newExit = LevelConnector.findLowestExit(this)
            if(newExit == null) break
            exitCoords = newExit
        }

        println("removing exit at $exitCoords")
        levelChunk.content[exitCoords.x][exitCoords.y] = '-'

        var finishSpace = LevelConnector.calculateFreeRoomSpace(this, exitCoords)!!
        var finishRoom: Room = SharedData.getRoomTemplatesByTags(arrayListOf("finish")).random().generate()
        state.updateByCoords(
                Coords(
                        finishSpace.DL_Corner().x + finishRoom.room.width,
                        finishSpace.DL_Corner().y +finishRoom.room.height
                )
        )
        exitCoords.x++;
        levelChunk.emplaceChunk(finishRoom.room, finishSpace.ULByEntryPoint(exitCoords))

        //printLevel()

        /*for(y in state.highestY downTo 0){
            for(x in 0 until state.highestX + 1){
                when(levelColumns[x][y]){
                    '.' -> level.append('-')
                    else -> level.append(levelColumns[x][y])
                }
            }
            level.append('\n')
        }         */


        level = levelChunk.getAsMarioAILevel()
        println("out level = ")
        print(level)

    }

    /**
     * Emplaces a [Room] at the provided place
     *
     * @param room to be emplaced
     * @param dl_corner coordinates, where the down-left corner of the room is placed
     */

    /*fun emplaceRoom(room: Room, dl_corner: Coords){
        var xCounter = 0
        var yCounter = 0
        for(line in room.room.getAsStringBuilder().lines()){
            for(char in line){
                when (char) {
                    '.' -> {}
                    else -> levelColumns[dl_corner.x + xCounter][dl_corner.y - yCounter] = char
                }
                xCounter++
            }
            xCounter = 0
            yCounter++
        }
    }*/

    /**
     * Emplaces a [Section] at the provided place
     *
     * @param section to be emplaced
     * @param dl_corner coordinates, where the down-left corner of the section is placed
     */

    /*fun emplaceSection(section: Section, start: Coords){
        var dl_corner = start;
        dl_corner.y += section.sectionSpace.startAnchor.y - section.sectionSpace.DL_Corner().y;
        var xCounter = 0
        var yCounter = 0
        println("empl sec dl corner = $dl_corner")
        for(line in section.section.getAsStringBuilder().lines()){
            for(char in line){
                when (char) {
                    '.' -> {}
                    else -> levelColumns[dl_corner.x + xCounter][dl_corner.y - yCounter] = char
                }
                xCounter++
            }
            xCounter = 0
            yCounter++
        }
    }*/

    /**
     * Prints the [Level] in the representation accepted by the MarioAI Framework
     */
    /*fun printLevel(): Unit{
        for(y in state.maxHeight-1 downTo 0){
            for(x in 0 until state.maxLength){
                print(levelColumns[x][y])
            }
            println()
        }
    }*/
}