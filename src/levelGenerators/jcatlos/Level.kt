package levelGenerators.jcatlos

import kotlin.math.max

/**
 * Level class is responsible for the creation of the level
 *
 * @param state [State] the only information provided by LevelGenerator
 */

class Level(var config: Config){
    var levelChunk = Chunk(config.maxLength, config.maxHeight)
    var startingCoords = Coords(-1, 30)

    var maxCoords = Coords(0,0)

    init{

        // Emplace Starting room into the level
        var startRoom: Room = SharedData.getRoomTemplatesByTags(arrayListOf("start", config.challengeTag)).random().generate()
        var entryCoords = this.addRoomByEntryPoint(startRoom, startingCoords)


        // Fill in the level
        var currentType = config.getNextSectionType()
        while(currentType != SectionType.FINISH){
            println("next iteration ")

            // Allocate RoomSpace for next section
            var rs = LevelConnector.calculateFreeRoomSpace(this, entryCoords)
            if(rs == null) break

            // Generate section according to the section type
            var sectionTemplate = SharedData.SectionTemplates.random()
            var challengeTags = arrayListOf(config.challengeTag)
            var inputTags = ArrayList<String>()
            when(currentType){
                SectionType.INTRO -> {
                    inputTags.add("!hard")
                    inputTags.add("!twist")
                    //inputTags.add("!bonus")
                    sectionTemplate = SharedData.getSimpleSectionTemplates().random()
                }
                SectionType.HARD -> {
                    inputTags.add("!easy")
                    inputTags.add("!twist")
                }
                SectionType.TWIST -> {
                    //inputTags.add("twist")
                    sectionTemplate = SharedData.TwistSectionTemplates.random()
                }
            }

            // Generate the next section

            var section = sectionTemplate.generate(rs, config.roomGenerator, inputTags, challengeTags)
            this.updateByCoords(section.sectionSpace.DR_Corner())

            levelChunk.emplaceChunk(section.section, section.sectionSpace.ULByEntryPoint(entryCoords))
            padToEnd(section.sectionSpace.DL_Corner(), section.section.getLastLine())


            // Calculate the next entry coordinates
            entryCoords = Coords(
                    section.finishPoints.first().x + section.sectionSpace.UL_Corner().x + 1,
                     //  +1 because an entry to the next room will be 1 to the right from the exit
                    section.finishPoints.first().y + section.sectionSpace.UL_Corner().y
            )

            println("Current state of the level:")
            println(levelChunk.getAsStringBuilder())

            // Get type of the next section
            currentType = config.getNextSectionType()
        }

        // Emplace a finish room at the end of the level
        var finishRoom: Room = SharedData.getRoomTemplatesByTags(arrayListOf("finish", config.challengeTag)).random().generate()
        this.addRoomByEntryPoint(finishRoom, entryCoords)

    }

    private fun updateByCoords(coords: Coords){
        maxCoords.x = max(maxCoords.x, coords.x)
        maxCoords.y = max(maxCoords.y, coords.y)
    }

    private fun padToEnd(dlCorner: Coords, lastLine: String){
        var xIndex = dlCorner.x
        for(char in lastLine){
            for(y in dlCorner.y until levelChunk.height){
                levelChunk.content[xIndex][y] = char
            }
            xIndex++
        }
    }

    private fun addRoomByEntryPoint(room: Room, entryPoint: Coords): Coords{
        var ul = Coords(
                entryPoint.x - room.start.x,
                entryPoint.y - room.start.y
        )
        levelChunk.emplaceChunk(room.room, ul)
        padToEnd(Coords(ul.x, ul.y + room.room.height), room.room.getLastLine())

        this.updateByCoords(
                Coords(
                        ul.x + room.room.width,
                        ul.y + room.room.height
                )
        )

        if(room.finish.isEmpty()) return Coords(-1, -1)

        return Coords(
                ul.x + room.finish.first().x + 1,
                ul.y + room.finish.first().y
        )
    }

    fun getLevel() : String{
        print(levelChunk.getAsMarioAILevel(0, maxCoords.y))
        return levelChunk.getAsMarioAILevel(0, maxCoords.y).toString()
    }

}