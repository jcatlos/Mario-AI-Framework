package levelGenerators.jcatlos

/**
 * Interface for RoomGenerators - allows to change [RoomGenerator]
 * - Only required function is generateToFitRoomspace()
 * - Does not need to use the whole RoomTemplate process
 */

interface RoomGenerator {
    /**
     * Reeturns a [Room] satisfying these conditions
     *
     * Conditions are:
     *  - Fitting into the provided [RoomSpace]
     *  - That the anchors align (that the anchors of the [Room] are a superset of the anchors of the [RoomSpace])
     *  - Satisfying the [sectionTags]
     *
     *  @param roomSpace the [RoomSpace] to be used by the output [Room]
     *  @param sectionTags the tags of a [Section] to be satisfied
     *  @return [Room] that satisfies all the conditions
     */
    fun generateToFitRoomspace(roomSpace: RoomSpace, sectionTags: ArrayList<String>): Room
}