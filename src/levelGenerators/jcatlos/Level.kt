package levelGenerators.jcatlos

/* Level creation follows these steps:
    1.) Create starting and finishing rooms
    2.) Use LayerBuilder class to create specified amount of layers of rooms
    3.) Connect created layers using information:
        // (Not true anymore) I.)     LayerBuilder returns rooms in layers in reverse order (from bottom to top) -
        II.)    Rooms at the lowest layer create a ground level - no "underground" rooms (yet)
        III.)   RoomH2.height = 2 * RoomH1.height (Pridat nakres ako funguju susedne miestnosti)
 */

import com.sun.jdi.ArrayReference
import java.nio.channels.NotYetConnectedException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.random.Random
import kotlin.reflect.typeOf


const val LEVEL_H2_HEIGHT = 16
const val LEVEL_H1_HEIGHT = 8


class Level(var length: Int, var state: State, layerBuilder: LayerBuilder){
    var layers: ArrayList<Layer> = ArrayList()
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

    init {
        println("starting initialization")

        layers.add(Layer(arrayListOf(layerBuilder.createStart())))

        while(!state.shouldEnd()){
            //println("add layer")
            //println("lenghth = ${state.layerCount}")
            layers.add(layerBuilder.next())
        }
        /*for (i in 0 until length){
            layers.add(layerBuilder.next())
        }*/

        var lastRoom = Layer(arrayListOf(layerBuilder.createFinish()))
        layers.add(lastRoom)
        state.updateByLayer(lastRoom)
        println("finishing initialization")
    }


    fun generateMap() : String{
        //println("starting generation")
        var level : Array<StringBuilder> = Array<StringBuilder>(size = LEVEL_H2_HEIGHT, init =  {StringBuilder()})

        var maxHeight = 0
        for(layer in layers){
            maxHeight = max(maxHeight, layer.height)
        }

        //var emptyRoomString = EmptyRoomH1().file.toString()

        var layersRows: ArrayList<ArrayList<String>> = ArrayList()
        for(layer in layers){
            var layerRows: ArrayList<String> = ArrayList()
            /*for(i in 0 until maxHeight - layer.height){
                layerStringBuilder.append(emptyRoomString)
            }*/
            for(room in layer.getRooms().reversed()){
                for(row in room.room.lines().reversed()){
                    layerRows.add(row)
                }
            }
            layersRows.add(layerRows)
        }

        // Pospajat Vrstvy
        var allFinished = false
        var iterator: Int = 0
        var levelStringBuilder = StringBuilder()
        while(!allFinished){
            var row = StringBuilder()
            allFinished = true
            for(i in 0 until layersRows.size){
                if(iterator < layersRows[i].size){
                    row.append(layersRows[i][iterator])
                    allFinished = false
                }
                else{
                    row.append("CCCCCCCCCCCCCCCCCCCCCCCCC") //for debugging
                    //row.append("-------------------------")
                }
            }
            if(!allFinished){
                levelStringBuilder.insert(0, "$row\n")
            }
            iterator++
        }
        print(levelStringBuilder.toString())
        return levelStringBuilder.toString()
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