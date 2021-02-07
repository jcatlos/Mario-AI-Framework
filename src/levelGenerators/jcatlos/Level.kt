package levelGenerators.jcatlos

/**
 * Level class is responsible for the creation of the level
 *
 * @param state [State] the only information provided by LevelGenerator
 */

class Level(var state: State){
    var levelChunk = Chunk(state.maxLength, state.maxHeight)
    var level = StringBuilder()
    var startingCoords = Coords(0, 30)

    init{

        // Emplace Starting room into the level
        var startRoom: Room = SharedData.getRoomTemplatesByTags(arrayListOf("start")).random().generate()
        state.updateByCoords(Coords(startRoom.room.width, startRoom.room.height + startingCoords.y))

        levelChunk.emplaceChunk(startRoom.room, startingCoords)
        var entryCoords:Coords = startRoom.finish.first().copy()
        entryCoords.y += startingCoords.y
        println("start finish coords = ${startRoom.finish.first()}");


        // Fill in the level
        while(!state.shouldEnd()){
            println("next iteration ")

            // Allocate RoomSpace for next section
            var rs = LevelConnector.calculateFreeRoomSpace(this, entryCoords)
            if(rs == null) break

            // Generate the next section
            var sectionTemplate = SharedData.SectionTemplates.random()
            var section = sectionTemplate.generate(rs)
            state.updateBySection(section)

            levelChunk.emplaceChunk(section.section, section.sectionSpace.ULByEntryPoint(entryCoords))

            // Calculate the next entry coordinates
            entryCoords = section.finishPoints.first().copy()
            entryCoords.x += section.sectionSpace.UL_Corner().x + 1     //  +1 because an entry to the next room will be 1 to the right from the exit
            entryCoords.y += section.sectionSpace.UL_Corner().y

            println("Current state of the level:")
            println(levelChunk.getAsStringBuilder())
        }


        // Emplace a finish room at the end of the level
        var finishSpace = LevelConnector.calculateFreeRoomSpace(this, entryCoords)!!
        var finishRoom: Room = SharedData.getRoomTemplatesByTags(arrayListOf("finish")).random().generate()

        var finishCoords = entryCoords
        finishCoords.x++
        finishCoords.x -= finishRoom.start.x
        finishCoords.y -= finishRoom.start.y
        //exitCoords.x++
        println("Emplacing finish at $finishCoords")
        levelChunk.emplaceChunk(finishRoom.room, finishCoords)

        state.updateByCoords(
                Coords(
                        finishCoords.x + finishRoom.room.width,
                        finishCoords.y +finishRoom.room.height
                )
        )
    }

    fun getLevel() : String{
        print(levelChunk.getAsMarioAILevel(0, state.highestY))
        return levelChunk.getAsMarioAILevel(0, state.highestY).toString()
    }

}