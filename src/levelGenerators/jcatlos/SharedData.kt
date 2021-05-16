package levelGenerators.jcatlos

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.nio.Buffer
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.collections.ArrayList

/**
 * Object responsible for loading all the assets for the LevelGenerator
 *
 * Contained data are accesible to all classes
 */

object SharedData {

    init{ println("Initializing shared data") }

    var cl = this.javaClass.classLoader

    var SectionTemplates: ArrayList<SectionTemplate> = ArrayList()
    var TwistSectionTemplates: ArrayList<SectionTemplate> = ArrayList()
    var RoomTemplates: ArrayList<RoomTemplate> = ArrayList()
    var Macros: MutableMap<String, Macro> = mutableMapOf()

    var roomGenerator: RoomGenerator = RandomRoomGenerator

    init{
        // Loading macros

        //var MacroFiles: ArrayList<File> = ArrayList()
        /*println("Loading macro files:")
        for(dir in File("src/levelGenerators/jcatlos/macros").listFiles()){
            println("\tDirectory ${dir.name}:")
            for(file in dir.listFiles()){
                println("\tFile ${file.name}")
                MacroFiles.add(file)
            }
        }*/

        println("Parsing macro files:")
        for(file in getFiles("levelGenerators/jcatlos/macros/premade/")){
            println("\tParsing $file")
            var br = BufferedReader(InputStreamReader(cl.getResourceAsStream(file)))
            var macro = MacroParser.parseFile(br)
            println("adding macro ${macro.name}")
            Macros[macro.name] = macro
        }
        println("loaded ${Macros.size} section files")


        // Loading rooms

        /*var RoomFiles: ArrayList<File> = ArrayList()
        println("Loading room files:")
        for(dir in File("src/levelGenerators/jcatlos/blocks").listFiles()){
            println("\tDirectory ${dir.name}:")
            for(file in dir.listFiles()){
                println("\t\tFile ${file.name}")
                RoomFiles.add(file)
            }
        }*/

        println("Parsing room files:")

        for(file in getFiles("levelGenerators/jcatlos/blocks/premade/")){
            println("\tParsing $file")
            var br = BufferedReader(InputStreamReader(cl.getResourceAsStream(file)))
            RoomTemplates.add(RoomParser.fileToTemplate(br))
        }
        for(file in getFiles("levelGenerators/jcatlos/blocks/custom/")){
            println("\tParsing $file")
            var br = BufferedReader(InputStreamReader(cl.getResourceAsStream(file)))
            RoomTemplates.add(RoomParser.fileToTemplate(br))
        }
        println("loaded ${RoomTemplates.size} room files")


        // Loading sections

        /*var SectionFiles: ArrayList<File> = ArrayList()
        println("Loading section files:")
        for(dir in File("src/levelGenerators/jcatlos/sections").listFiles()){
            println("\tDirectory ${dir.name}:")
            for(file in dir.listFiles()){
                println("\tFile ${file.name}")
                SectionFiles.add(file)
            }
        }*/

        println("Parsing section files:")
        for(file in getFiles("levelGenerators/jcatlos/sections/premade/")){
            println("\tParsing $file")
            var br = BufferedReader(InputStreamReader(cl.getResourceAsStream(file)))
            if("twist" in file) TwistSectionTemplates.add(SectionParser.fileToSectionTemplate(br))
            else SectionTemplates.add(SectionParser.fileToSectionTemplate(br))
        }
        println("loaded ${SectionTemplates.size} section files")
    }

    private fun getFiles(dir: String) : ArrayList<String>{
        var files: ArrayList<String> = arrayListOf()
        var jarFile: File = File(this.javaClass.protectionDomain.codeSource.location.path)
        var jar: JarFile = JarFile(jarFile)
        var entries: Enumeration<JarEntry> = jar.entries()
        while(entries.hasMoreElements()){
            var name = entries.nextElement().name
            if(name.startsWith(dir) && name != dir){
                println(name)
                files.add(name)
            }
        }
        return files
    }


    fun getSimpleSectionTemplates(): ArrayList<SectionTemplate>{
        var out = ArrayList<SectionTemplate>()
        for(template in SectionTemplates){
            if(template.roomSpaces.size < 4){
                out.add(template)
            }
        }
        return out
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