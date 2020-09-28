package levelGenerators.jcatlos

class Section(var section: StringBuilder, var sectionSpace: RoomSpace){
    var startPoint: Coords = sectionSpace.startAnchor
    var finishPoints: ArrayList<Coords> = sectionSpace.finishAnchors
}