package levelGenerators.jcatlos

import java.io.File
import java.lang.StringBuilder

/**
 * Object responsible for loading all the assets for the LevelGenerator
 *
 * Contained data are accesible to all classes
 */

object SharedData {

    init{ println("Initializing shared data") }

    var SectionTemplates: ArrayList<SectionTemplate> = ArrayList()
    var RoomTemplates: ArrayList<RoomTemplate> = ArrayList()

    var roomGenerator: RoomGenerator = RandomRoomGenerator

    init{
        // Loading rooms

        var RoomFiles: ArrayList<File> = ArrayList()
        println("Loading room files:")
        for(dir in File("src/levelGenerators/jcatlos/blocks").listFiles()){
            println("\tDirectory ${dir.name}:")
            for(file in dir.listFiles()){
                println("\t\tFile ${file.name}")
                RoomFiles.add(file)
            }
        }

        println("Parsing room files:")
        for(file in RoomFiles){
            println("\tParsing ${file.name}")
            RoomTemplates.add(RoomParser.fileToTemplate(file))
        }

        // Loading sections

        var SectionFiles: ArrayList<File> = ArrayList()
        println("Loading section files:")
        for(dir in File("src/levelGenerators/jcatlos/sections").listFiles()){
            println("\tDirectory ${dir.name}:")
            for(file in dir.listFiles()){
                println("\tFile ${file.name}")
                SectionFiles.add(file)
            }
        }

        println("Parsing section files:")
        for(file in SectionFiles){
            println("\tParsing ${file.name}")
            SectionTemplates.add(SectionParser.fileToSectionTemplate(file))
        }
        println("loaded ${SectionTemplates.size} files")
    }

    fun getRoomTemplatesByTags(tags: ArrayList<String>): ArrayList<RoomTemplate>{
        var out = ArrayList<RoomTemplate>()
        for(template in RoomTemplates){
            if(checkTags(template.tags, tags)){
                out.add(template)
            }
        }
        //println(out.size)
        return out
    }

    // Refactor - IT IS ALREADY IN ROOM GENERATOR
    private fun checkTags(roomTags: ArrayList<String>, sectionTags: ArrayList<String>): Boolean{
        var satisfy = true
        for(tag in sectionTags){
            if(tag.isEmpty()) continue
            else if(tag[0] == '!'){
                if(tag.substring(1) in roomTags) return false
            }
            else if(tag !in roomTags) return false
        }
        return true
    }

}