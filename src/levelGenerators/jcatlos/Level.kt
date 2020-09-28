package levelGenerators.jcatlos

import com.sun.jdi.ArrayReference
import java.nio.channels.NotYetConnectedException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.random.Random
import kotlin.reflect.typeOf

class Level(var state: State){
    var levelColumns: ArrayList<Array<Char>> = ArrayList()
    var level = StringBuilder()

    init{
        // Initialize levelColumns - create an empty level
        for(x in 0 until state.maxLength){
            levelColumns.add(Array(state.maxHeight) { _ -> '.'})
        }

        var startRoom: Room = SharedData.roomGenerator.generateToFitRoomspace(LevelConnector.calculateFreeRoomSpace(this, Coords(0,0))!!, arrayListOf("start"))
        state.updateByCoords(Coords(startRoom.room.lines()[0].length, startRoom.room.lines().size))

        emplaceRoom(startRoom, Coords(0,0))
        var exitCoords:Coords = startRoom.finish.first()

        //println("exit: ${findLowestExit(this)}")

        while(!state.shouldEnd()){
            //println("iteration $i")
            printLevel()
            if(exitCoords == null) break
            //var exitCoords = findLowestExit(this)
            var sectionTemplate = SharedData.SectionTemplates.random()
            println("chosen ${sectionTemplate.roomSpaces.size}")
            //println("\t exit coords: $exitCoords")
            var rs = LevelConnector.calculateFreeRoomSpace(this, exitCoords)
            if(rs == null) break

            var section = sectionTemplate.generate(rs.DL_Corner())
            state.updateBySection(section)

            emplaceSection(section, rs.DL_Corner())
            // Remove exit from generated map
            levelColumns[exitCoords.x][exitCoords.y] = '-'

            var newExit = LevelConnector.findLowestExit(this)
            if(newExit == null) break
            exitCoords = newExit
        }

        var finishSpace = LevelConnector.calculateFreeRoomSpace(this, exitCoords)!!
        var finishRoom: Room = SharedData.roomGenerator.generateToFitRoomspace(finishSpace, arrayListOf("finish"))
        state.updateByCoords(
                Coords(
                        finishSpace.DL_Corner().x + finishRoom.room.lines()[0].length,
                        finishSpace.DL_Corner().y +finishRoom.room.lines().size
                )
        )
        emplaceRoom(finishRoom, finishSpace.DL_Corner())

        printLevel()

        for(y in state.highestY downTo 0){
            for(x in 0 until state.highestX + 1){
                when(levelColumns[x][y]){
                    '.' -> level.append('-')
                    else -> level.append(levelColumns[x][y])
                }
            }
            level.append('\n')
        }
        println("out level = ")
        print(level)

    }

    fun emplaceRoom(room: Room, dl_corner: Coords){
        var xCounter = 0
        var yCounter = 0
        for(line in room.room.lines().reversed()){
            for(char in line){
                when (char) {
                    '.' -> {}
                    else -> levelColumns[xCounter + dl_corner.x][yCounter + dl_corner.y] = char
                }
                xCounter++
            }
            xCounter = 0
            yCounter++
        }
    }

    fun emplaceSection(section: Section, dl_corner: Coords){
        var xCounter = 0
        var yCounter = 0
        println("empl sec dl corner = $dl_corner")
        for(line in section.section.lines().reversed()){
            for(char in line){
                when (char) {
                    '.' -> {}
                    else -> levelColumns[xCounter + dl_corner.x][yCounter + dl_corner.y-1] = char
                }
                xCounter++
            }
            xCounter = 0
            yCounter++
        }
    }

    fun printLevel(): Unit{
        for(y in state.maxHeight-1 downTo 0){
            for(x in 0 until state.maxLength){
                print(levelColumns[x][y])
            }
            println()
        }
    }
}