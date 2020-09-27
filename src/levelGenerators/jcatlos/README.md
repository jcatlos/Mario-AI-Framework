# jcatlos LevelGenerator
This level generator is generating levels using hand-crafted room and level designs. It's meant to create playable Super Mario levels with more managed playability and customization in mind. There are pre-made rooms and room layouts (sections), however the generator is designed to provide an easy way of creating more (more [here](#Customization))
## Set-up
## Customization
### Rooms
#### Directories
All room prototypes (blocks) are located in the `blocks` diretory seperated into 2 subdirectories:
 - `premade` Contains the pre-made blocks
 - `custom` Is empty by default but serves to contain your created blocks
These are only orientational and any subdirectory can be added or removed. However, only blocks with path `blocks/[someDirectory]/block` are loaded (they have to be in some subdirectory of blocks).
#### Schema of a block file
Block files have to follow a strict format and not contain anything else. Everything is whitespace sensitive.

**First line** must contain a positive number indicating the basic difficulty of the room. This number is arbitrary (pre-made rooms count every enemy for 10 and each jump for 2 in each block in width and 3 for each block of height). This number can be adjusted for fine-tuning.

**Second line** must contain a comma separated list of words - the tags for the room. If you don't want to assign any tags for the room, leave an empty line. More on tags [here](#SectionTags)

Starting by **third line** there must be lines of the room itself. Each line of the room in the file will be a line in the real room. It must contain only characters used in the MarioAI room files with 2 exceptions. Each room must contain a character `'m'` at the left-most place on any lline and can contain any number of character `'f'` at the right-most side of any lines. These characters mark the entrance (m) and the exits (f) of the room. The second exception are macros.
 #### Macros
 Macros are "scripts" which are executed every time a room is generated from its file. They are describing multiple possible strings (and the probability of their occurence) which can be inserted at their placemeant to add a little bit of variety to the generated rooms. They also follow a strict rules and format:
 - They are placed into the rooms (no new lines or any whitespace)
 - The first character of the macro indicates the place where the result can be inserted
 - Their format is as follows:
	 - They start by a `{` and end by a `}`
	 - Each option is in form `[character sequence]=[probability];`. `[character sequence]` must contain only characters accepted by MarioAI and every option must be of the same length. `[probability]` must be replaced by an integer from 0-100 and is the probability of the appearance. The sum of all probabilities must be 100 or less - if it is less than 100, there is an implicit option of repeated character `'-'`.
#### Example room file
```
35
unsafe, challenge
-------------------------
-------------------------
-------------------------
-------------------------
-----------ooo-----------
------------{g=30;}------------
m---------SSSSS---------f
XXXXX---------------XXXXX
```
### Sections
Sections are user-generated room layout patterns. They consist of multiple rooms and each room has assigned tags it **must contain** and tags it **must not contain**. The file itself is composed of 2 parts. The *Declaration part* declares the rooms' tags and the character they are represented by. The *Layout part* gives the rooms' dimensions, positions and how the rooms are connected.
#### Directories
Sections follow the same directory schema as room (block) files.
#### Schema of a section file
Similarly to block files, section files have to follow a strict format and not contain anything else. Everything is whitespace sensitive.
 
 First, there is the **Declaration part**. Each line of the part declares 1 room in format `[char]: [tags]` where `[char]` is a unique character by which the room is represented and `[tags]` is a list of comma separated *section tags*

Then there is a line containing only `---` separating the 2 parts

Lastly, there is the **Layout part**. It consists solely of the exact wished layout of the rooms, where the every room is represented by the character given to it in the *Declaration part*. Furthermore, every entrance to the room (except the first) is represented by a `'m'` and every exit from the room (except the last one) is represented by a `'f'`. The entrance to the first room (and therefore the whole section) is represented by a `'M'` and the exit(s) from the section are represented by 	`'F'`

#### Example section file
```
0: safe
1: !safe
---
00000000000000000000000001111111111111111111111111
00000000000000000000000001111111111111111111111111
00000000000000000000000001111111111111111111111111
00000000000000000000000001111111111111111111111111
00000000000000000000000001111111111111111111111111
00000000000000000000000001111111111111111111111111
00000000000000000000000001111111111111111111111111
00000000000000000000000001111111111111111111111111
00000000000000000000000001111111111111111111111111
00000000000000000000000001111111111111111111111111
00000000000000000000000001111111111111111111111111
00000000000000000000000001111111111111111111111111
00000000000000000000000001111111111111111111111111
00000000000000000000000001111111111111111111111111
M00000000000000000000000fm11111111111111111111111F
00000000000000000000000001111111111111111111111111
```
#### Section tags
Section tags is a comma separated list of words representing the tags for each room in the section. Tag can be either *positive* or *negative*. There are some pre-defined tags used in pre-made sections, but feel free to use any other tags!
##### Negative tags
Are tags represented by a string of characters starting with `!`. Their presence indicates that rooms with these tags are forbidden from being used in those places.
##### Positive tags
Are tags represented by any other string of characters. Their presence indicates that only rooms with those tags are allowed to be in those places.
##### Pre-defined tags
There are `start` and `finish` tags for starting and finishing rooms, `divide` tag for rooms used for rooms with multiple exits, `safe` tag for rooms that have safe floor (with no holes) and `ground` tag for rooms meant for rooms that are only supposed to be on the ground level (so if you don't want to see them in wrong places use the `!ground` tag for each room which is not on the ground level)
