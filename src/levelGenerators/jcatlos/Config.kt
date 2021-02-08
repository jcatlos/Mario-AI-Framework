package levelGenerators.jcatlos

import kotlin.random.Random

/**
 * There are 4 types of sections:
 * 1. [INTRO] Section meant for introduction. Should not be complex nor difficult
 * 2. [HARD] Section may be complex and/or difficult
 * 3. [TWIST] Rooms should contain a "twist on the mechanic"
 * 4. [FINISH] Section with a finish room - Effectively the last section to be generated
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
 * @property twistMultiplier How much more should it be probable to introduce a twist with each challenge section
 */
data class Config(
        var challengeTag: String = "",
        var maxLength: Int,
        var maxHeight: Int,
        var roomGenerator: RoomGenerator,
        var introLength: Int,
        var hardLength: Int,
        var twistMultiplier: Double
) {
    var sectionCount = 0
    var twistChance = 0.1

    /**
     * Gets the desirecd [SectionType] of the next section
     *
     * @return the desired [SectionType]
     */
    fun getNextSectionType(): SectionType{
        sectionCount++
        if(sectionCount <= introLength) return SectionType.INTRO
        if(sectionCount >= introLength+hardLength) return SectionType.FINISH

        twistChance *= twistMultiplier
        if(Random.nextFloat() > twistChance) return SectionType.TWIST

        return SectionType.HARD
    }
}