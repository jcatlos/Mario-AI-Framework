package levelGenerators.jcatlos

import com.sun.jdi.ArrayReference
import java.nio.channels.NotYetConnectedException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.random.Random
import kotlin.reflect.typeOf

class Level(var length: Int, var state: State){
    var levelColumns: ArrayList<Column> = ArrayList()
    var level = StringBuilder()

    init{
        // Initialize levelColumns - create an empty level
        for(x in 0 until state.maxLength){
            levelColumns.add(Column(StringBuilder(".".repeat(state.maxHeight))))
        }

        var startRoom: Room = RandomRoomGenerator.generateStartRoom()
        var highestY = startRoom.room.lines().size
        var highestX = startRoom.room.lines()[0].length
        emplaceRoom(startRoom, Coords(0,0))
        var exitCoords = findLowestExit(this)

        println("exit: ${findLowestExit(this)}")

        for(i in 0 until state.sectionCount){
            println("iteration $i")
            printLevel()
            if(exitCoords == null) break
            //var exitCoords = findLowestExit(this)
            var sectionTemplate = SharedData.SectionTemplates.random()
            println("chosen ${sectionTemplate.roomSpaces.size}")
            //println("\t exit coords: $exitCoords")
            var rs = calculateFreeRoomSpace(this, exitCoords)
            if(rs == null) break
            //println("\t roomspace dl =  ${rs.DL_Corner()}")
            //var room = RandomRoomGenerator.generateToFitRoomspace(rs)
            //println("\t ${room.tags}")
            //emplaceRoom(room, rs.DL_Corner())
            var section = sectionTemplate.generate(rs.DL_Corner())
            if(section.sectionSpace.UR_Corner().y > highestY) highestY = section.sectionSpace.UR_Corner().y
            if(section.sectionSpace.UR_Corner().x > highestX) highestX = section.sectionSpace.UR_Corner().x
            emplaceSection(section, rs.DL_Corner())
            // Remove exit from generated map
            levelColumns[exitCoords.x][exitCoords.y] = '-'
            exitCoords = findLowestExit(this)
        }

        for(y in highestY downTo 0){
            for(x in 0 until highestX + 1){
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