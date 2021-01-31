package levelGenerators.jcatlos

/**
 * a Section
 *
 * Start/Finish points are the ones of the whole section (used to connect sections)
 * The ones of the rooms are irrelevant
 *
 * @param section a string representation of the [Section] (As provided in the input file)
 * @param sectionSpace a [RoomSppace] of the [Section] - All addresses inside [Section] are relative to the [Section]
 * @property startPoint the entry point to the [Section]
 * @property finishPoints the exit points from the [Section]
 */

class Section(var section: Chunk, var sectionSpace: RoomSpace){
    var startPoint: Coords = sectionSpace.startAnchor
    var finishPoints: ArrayList<Coords> = sectionSpace.finishAnchors
}