package levelGenerators.jcatlos

/*
    Class representing an instance of a Section
        - Start/Finish points are the ones of the whole section (used to connect sections)
            - The ones of the rooms are irrelevant
 */

class Section(var section: StringBuilder, var sectionSpace: RoomSpace){
    var startPoint: Coords = sectionSpace.startAnchor
    var finishPoints: ArrayList<Coords> = sectionSpace.finishAnchors
}