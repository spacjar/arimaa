# Arimaa
## Setup
1. The game is played on an 8x8 board.
2. There are 2 players. One is golden and the other is silver.
3. Each player has 1 elephant, 1 camel, 2 horses, 2 dogs, 2 cats and 8 rabbits.
4. Before the game starts, the golden player places his pieces on the first 2 rows in any order he wants. The silver player does the same on the last 2 rows.

## Game
5. When the game starts, the golden player moves first.
6. During the players turn the player is allowed to move their pieces exactly 4x times (4x steps).
7. During your turn, you can distribute the moves with up to 4x pieces. The moves can be in any direction (that the pieces allow). (The player can move with any of their pieces.)
8. The player can use less than 4x moves but more than 0x (m > 0, m <= 4). The moves do not transfer to the next turn.
9. Every piece can move to back, front, left and right. This does not apply to the rabbit, who can only move front, left and right.
10. Every piece can move only to an empty square. They cannot share space with another piece (friendly or enemy).
11. Peices cannot move off the board.
12. Peices have a strength order: Elephant > Camel > Horse > Dog > Cat > Rabbit.
13. Stronger pieces can move their weaker oponent pieces.
14. Pieces cannot move oponent pieces that are equal or stronger to them.
15. Pieces cannot move friendly pieces.
16. It takes 2x steps to move an enemy piece. Both steps must be completed in the same turn.
17. You can move the enemy piece by either pushing it or pulling it.
18. In order to push an enemy piece, you must have a stronger piece adjacent to it (next to it).
19. Pushing: Move your enemie's piece to an available (empty) square adjacent to it (next to it) and then move your piece to its vacated (previous space of the enemy piece) space.
20. Pulling: Move your piece to any of its available adjecent spaces than move the enemy piece to the vacated space of your piece (previous space of your friendly piece).
21. Pushes and pulls don't need to be in a straight line.
22. A stronger piece cannot move multiple pieces in the same step.
23. Any combination of pushing and pulling can be done in the same turn.
24. You cannot push or pull pieces of the board.
---
25. A stronger friendly piece freezes a weaker enemy piece that is adjacent to it (next to it) and vise versa.
26. A frozen piece cannot move.
27. A frozen piece can be unfrozen if a friendly piece is adjacent to it.
---
28. There are 4x traps on the board.
29. A piece that falls into a trap is removed from the game (from the board). Unless there is a friendly piece next to that trap (adjecent to the trap).
30. You may not pass the trap unless the trap is protected by a friendly piece (frindly piece is adjecent to the trap).
---
31. You may push or pull an opponents rabbit into the goal row that is trying to reach and then back out of the goal row in the same turn without losing the game.

## End
32. If you cannot move because all of your pieces are frozen, you lose the game.
33. If you are unable to move, because you don't have a legal move, you lose the game.
34. If you lose all your rabbits, you lose the game.
35. If you lose all your pieces, you lose the game.
36. If both players lose all of their rabbits on the same turn, the player whose turn it was (and made that move) wins the game.
---
37. The first player to move a rabbit to the other side of the board wins. 

# Examples 
**FR1 is FROZEN**
```markdown
---------------
|    |     |   |
| EN | FR1 |   |
|    |     |   |
---------------  
```

**FR1 is UNFROZEN**
```markdown
---------------
|    | FR2 |   |
| EN | FR1 |   |
|    |     |   |
---------------   
```