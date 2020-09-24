package levelGenerators.jcatlos

/*
    LayerDesigner object is meant to create layer "designs". Such design is an Array<Int>,
    where each number represents the height of a room (ordered top-down). Such object can
    then be given to a LevelBuilder, to be used to design its layers.
    Possible room heights are 0, 1, 2

    The default LayerDesigner object is BasicLayerDesigner
 */

interface LayerDesigner {
    fun next(): Array<Int>

    // Function to join RoomH1 at given index with following room (must be RoomH1) into a RoomH2
    fun join(index: Int): Unit

    // Function to divide RoomH2 at given index into 2 RoomH1
    fun divide(index: Int): Unit
}