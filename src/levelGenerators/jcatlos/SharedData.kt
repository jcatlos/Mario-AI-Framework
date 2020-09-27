package levelGenerators.jcatlos

import java.io.File
import java.lang.StringBuilder

object SharedData {

    init{ println("Initializing shared data") }


    var SectionTemplates: ArrayList<SectionTemplate> = ArrayList()


    var RoomTemplates: ArrayList<RoomTemplate> = ArrayList()

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
        //println("loaded ${SectionTemplates.size} files")
    }



}