# Technical documentation

## LevelGenerator class
Overloaded `LevelGenerator` only creates an instance of a `Level` with specified beginning `State` and returns the level's string. Parameters of the state may be modified by the user.

## Level class
At initialization, it creates its attributes:
 - `levelColumns: ArrayList<Array<Char>>` - Represent the level in a matrix of characters
 - `level: StringBuilder` - Serves only to store the created level in a `StringBuilder` for `LevelGenerator`
 
 `levelColumns` is at first filled with `'.'` characters which in the whole process of level generation are used to represent unoccupied free space (unlike `'-'`, which represents free space but occupied by a room). Then one by one, fitting sections are chosen, generated and placed into `levelColumns` in a way, that start and finish anchors are in the right places (more on that in RoomGenerator). After a section is generated, the `state.updateBySection()` method is called (with the section as a parameter).

 Sections are always connected to the lowest current exit (found by `LevelConnector.findLowestExit()`). If there is none, generation is finished

 After generation is finished, the `levelColumns` are trimmed using `state.highestX` and `state.highestY` and `level` stringbuilder is filled with the level to be used by the `LevelGenerator`.

## RoomGenerator
Room Generator is an object used to fill `section`-s with rooms they require. The default RoomGenerator object is the `RandomRoomGenerator`. `RoomGeneratorInterface` serves to provide an interface to create another `RoomGenerator`s to generate rooms. Should you use another `RoomGenerator`, be sure to update the `SharedData` object to use it.

### RoomGenerator Interface
This interface requires only 1 method from the `RoomGenerator`.
1. `generateToFitRoomspace(roomSpace: RoomSpace, sectionTags: ArrayList<String>): Room` - should return a room that fits the provided `roomSpace` (including the start and finish points) and also satisfies the provided tags.

### Random Room Generator
This is the default `RoomGenerator` object. It overlads the interface method as follows: 
`generateToFitRoomSpace()` Looks at all the `RoomTemplates` in `SharedData.RoomTemplates` and adds every room that fits by their `width` and `height`, then checks their anchors (start and finish-es) and if they fit, it checks if their `tags` comply (using the `checkTags()` method described below)

The generator also defines its own private method `checkTags(roomTags: ArrayList<String>, sectionTags: ArrayList<String>):Boolean` to check whether the room's tags comply to the tags provided.

## Templates
Templates are classes holding all the data provided by the given files. They are meant to reduce the amount of file-reads during level generation - each file is read only once (at initialization of the `SharedData class`) and then kept as a template.

### RoomTemplate

#### Attributes
- `var room: StringBuilder` - Contains the room itself as a file (stored as a string with line-endings). Every macro is replaced by an adequate number of '-' -s
- `var width: Int` - Width of the room (by default initialized as `room.split('\n')[0].length`)
- `var height: Int` - Height of the room (by default initialized as `room.lines().size`)
- `var diff: Int` - Difficulty of the room (as given in its file)
- `var tags: ArrayList<String>` - Tags of the room (as given in its file)
- `var macros: Map<Coords, Macro>` - Macros in the room stored in a map, to be executed at generation of the room
- `var start: Coords` - Coordinates of the entrance to the room ((0,0) is bottom-left corner)
- `var finish: ArrayList<Coords>` - Coordinates of the exits from the room ((0,0) is bottom-left corner)

#### generate Method
`RoomTemplate.generate()` Copies the room from the template, executes all of its macros, counts enemies in the updated room and returns the resulting room.

### SectionTemplate

### Attributes
- `var sectionRoomSpace: RoomSpace` - Is the RoomSpace containing the whole section
- `var roomSpaces: MutableMap<Char, RoomSpace>`- Contains a `RoomSpace` for each character used to define that room in the section file
- `var sectionTags: MutableMap<Char, ArrayList<String>>`  - Contains an ArrayList of tags for the room provided in the section file

### Methods

#### generate()
The method calls the currently set `SharedData.roomGenerator` to create a room for every `RoomSpace` in the section and emplaces it using the `emplaceRoom()` method. At the end it returns created `Section`.

#### emplaceRoom()
Note: This method is similiar to `Level.emplaceRoom()` and `Level.emplaceSection()` and thus should be generalized to be grouped into one.

## Coords
Coords holds coordinates. It has 2 attributes `x` and `y` standing for x-coordinate and y-coordinate respectively. It also has an overloaded comparator - they are ordered by `x` then by `y`.

## SharedData
SharedData is an object, which holds data accessible across the whole generator. 
At initialization, it calls the `RoomParser` and `SectionParser` to parse every room and section in any subfolder of `blocks` and `section`. 
### Contained data
 - `RoomTemplates: ArrayList<RoomTemplate>`- A list containing templates of all the found rooms.
 - `SectionTemplates: ArrayList<SectionTemplate>` - A list containing templates of all the found sections.
 - `roomGenerator: RoomGenerator` - RoomGenerator used to generate rooms

## Parsers
Parsers are objects used to create templates from files.
### RoomParser
### SectionParser


 ## Space
Is a class used to store information about space. It is initialized by width, height and the coordinates of the down-left corner.
 ### Attributes
 * `width: Int`
 * `height: Int`
 ### Methods
 One method to return each of of the corners (down-left, down-right, up-left, up-right)
 * `DL_Corner(): Coords`
 * `DR_Corner(): Coords`
 * `UL_Corner(): Coords`
 * `UR_Corner(): Coords`

 ## RoomSpace: Space
 Is a derived class of `Space`, so only added attributes and methods are mentioned. It takes 2 more parameters than space: 
 * `startAnchor: Coords` - Coordinates of the entry point
 * `finishAnchors: ArrayList<Coords>` - Coordinates of each exit

 These 2 are attributes of the class. It also creates a private list `anchorDiffs` - differences of start point and all the exit points.
 ### anchorsFit() method
 This method takes a start point and exit points of a `RoomTemplate` (in the same way as the class itself). It then calculates `inputAnchorDiffs` (in the same way it calculates `anchordiffs`) and checks whether every `anchorDiff` is in the `inputAnchorDiffs` - All the exits from the `RoomSpace` mus be in the `RoomTemplate` with the same position relative to starting point.

 
