package levelGenerators.jcatlos

import kotlin.random.Random

/**
 * There are 4 types of sections:
 * 1. [INTRO] Section meant for introduction. Should not be complex nor difficult
 * 2. [HARD] Section may be complex and/or difficult
 * 3. [TWIST] Rooms should contain a "twist on the mechanic"
 * 4. [FINISH] Section with a finish room - The last section to be generated
 */

enum class SectionType{
    INTRO,
    HARD,
    TWIST,
    FINISH
}

/**
 * Data class for configuration of the generation process
 *
 * @property challengeTag tag of the mechanic the level should be build around
 * @property maxLength Maximum length of the level
 * @property maxHeight Maximum height of the level
 * @property roomGenerator The [RoomGenerator] object that should be used for the generation
 * @property introLength the length (in [Section]-s) of the introductory part of the level
 * @property hardLength the length (in [Section]-s) of the challenge part of the level
 */
data class Config(
        var challengeTag: String = "",
        var maxLength: Int,
        var maxHeight: Int,
        var roomGenerator: RoomGenerator,
        var introLength: Int,
        var hardLength: Int
) {
    var sectionCount = 0
    private var twisted = false

    /**
     * Gets the desirecd [SectionType] of the next section
     *
     * @return the desired [SectionType]
     */
    fun getNextSectionType(): SectionType{
        sectionCount++
        if(sectionCount <= introLength) return SectionType.INTRO
        if(twisted) return SectionType.FINISH

        if(sectionCount+1 == introLength+hardLength){
            twisted = true
            return SectionType.TWIST
        }

        return SectionType.HARD
    }
}