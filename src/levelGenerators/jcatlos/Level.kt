package levelGenerators.jcatlos

import kotlin.math.max

/**
 * Level class is responsible for the creation of the level
 *
 * @param state [State] the only information provided by LevelGenerator
 */

class Level(var config: Config){
    var levelChunk = Chunk(config.maxLength, config.maxHeight)
    var startingCoords = Coords(0, 30)

    var maxCoords = Coords(0,0)

    init{

        // Emplace Starting room into the level
        var startRoom: Room = SharedData.getRoomTemplatesByTags(arrayListOf("start", config.challengeTag)).random().generate()
        this.updateByCoords(
                Coords(
                        startRoom.room.width,
                        startRoom.room.height + startingCoords.y
                )
        )

        levelChunk.emplaceChunk(startRoom.room, startingCoords)
        var entryCoords:Coords = startRoom.finish.first().copy()
        entryCoords.y += startingCoords.y
        println("start finish coords = ${startRoom.finish.first()}");


        // Fill in the level
        var currentType = config.getNextSectionType()
        while(currentType != SectionType.FINISH){
            println("next iteration ")

            // Allocate RoomSpace for next section
            var rs = LevelConnector.calculateFreeRoomSpace(this, entryCoords)
            if(rs == null) break

            // Add desired room type
            var tags = arrayListOf(config.challengeTag)
            when(currentType){
                SectionType.INTRO -> tags.add("easy")
                SectionType.HARD -> tags.add("hard")
                SectionType.TWIST -> tags.add("twist")
            }

            // Generate the next section
            var sectionTemplate = SharedData.SectionTemplates.random()
            var section = sectionTemplate.generate(rs, config.roomGenerator, tags)
            this.updateByCoords(section.sectionSpace.DR_Corner())

            levelChunk.emplaceChunk(section.section, section.sectionSpace.ULByEntryPoint(entryCoords))

            // Calculate the next entry coordinates
            entryCoords = Coords(
                    section.finishPoints.first().x + section.sectionSpace.UL_Corner().x + 1,
                     //  +1 because an entry to the next room will be 1 to the right from the exit
                    section.finishPoints.first().y + section.sectionSpace.UL_Corner().y
            )
                    /*section.finishPoints.first().copy()
            entryCoords.x += section.sectionSpace.UL_Corner().x + 1
            entryCoords.y += section.sectionSpace.UL_Corner().y*/

            println("Current state of the level:")
            println(levelChunk.getAsStringBuilder())

            // Get type of the next section
            currentType = config.getNextSectionType()
        }


        // Emplace a finish room at the end of the level
        var finishRoom: Room = SharedData.getRoomTemplatesByTags(arrayListOf("finish", config.challengeTag)).random().generate()

        var finishCoords = Coords(
                entryCoords.x - finishRoom.start.x,
                entryCoords.y - finishRoom.start.y
        )

        println("Emplacing finish at $finishCoords")
        levelChunk.emplaceChunk(finishRoom.room, finishCoords)

        this.updateByCoords(
                Coords(
                        finishCoords.x + finishRoom.room.width,
                        finishCoords.y +finishRoom.room.height
                )
        )
    }

    private fun updateByCoords(coords: Coords){
        maxCoords.x = max(maxCoords.x, coords.x)
        maxCoords.y = max(maxCoords.y, coords.y)
    }

    fun getLevel() : String{
        print(levelChunk.getAsMarioAILevel(0, maxCoords.y))
        return levelChunk.getAsMarioAILevel(0, maxCoords.y).toString()
    }

}