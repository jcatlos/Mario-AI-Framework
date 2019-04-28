# Mario-AI Framework: 10th Anniversary Edition
This is an updated version for the Mario-AI framework. This new code will support better interface for planning/level generation/learning (future). We will also provide multiple different planning agents, level generators, original levels, and generated levels from previous years. Also, the framework is compatible with VGLC processed notations: https://github.com/TheVGLC/TheVGLC.

### Implemented Features
- The MarioAI framework core engine
- Implementing a forward model and multiple different observations (based around mario/based around the screen center)
- Implementing the original SMB graphics instead of Mario world graphics
- Adding multiple agents from the previous competition
- Isolating particle effects from game sprites
- Only using the first SMB action set (no more shell carrying/wall jumping)
- Documenting the interface
- Adding Generated Levels

### Missing Features
- Documenting the whole engine
- Better way to check the observation grid
- Adding a simple MCTS agent and simple A* agent
- Koopa shells can come back to life after stomping on it
- Adding Monte Mario agent
- Modifying the original Mario levels to include more details
- Adding the level generator interface
- Adding the level generators to the framework
- Multiple different backgrounds/palettes that the user can select from.
- Adding the learning track interface
- Mimicking the original SMB physics instead of SMW physics

### Copyrights
Mario is a Nintendo character which the authors doesn't own any rights to it. The framework is based on the core source code of Mario-AI framework which was a modification to Infinite Mario Bros by Notch.