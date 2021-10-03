# Colored-Tile-Puzzle

A search engine that supports several search algorithms (BFS, DFID, A*, IDA*, DFBnB) to solve the Colored NxM Tile Puzzle game.

In a given game an NxM board containing NxM-1 blocks numbered from 1 to NxM-1, and an empty block. Some of the numbered blocks are painted in black, some in green, and some in red. The blocks are arranged in some given initial order, and the goal is to find the smallest number of transactions from the initial arrangement to the final state. In the final mode all the blocks are arranged from 1 to NxM-1 from left to right and from top to bottom (regardless of their color), with the empty block in the lower right corner.

Unlike the regular tile-puzzle game, where each move is considered one step, in this game there are different rules and different costs that depend on the color of the block. It is not possible to move a black-painted block at all. You can move any other block next to the empty block. Moving a green block to the empty block costs 1, and moving a red block to the empty block costs 30.
