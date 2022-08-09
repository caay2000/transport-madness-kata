## Exercise 5 - Transport I

Let's try to put everything together and start moving passengers from one city to another (and an optional visualisation
exercise)

<kbd> <img src="exercise_5_header.png" /> </kbd>

[Home](../README.md) | [Exercise 4 - Cities I](exercise-4.md)

## Summary (Optional Part)

First part of the exercise will be to visualize our network in some way. You can do whatever works for you, but I'll
propose something quite simple on the console. The same map we have been working with will be represented like this:

```
   0  1  2  3  4  5    
0  H  +  @  +  .  .
1  .  .  .  +  .  .
2  .  .  .  H  .  .
3  .  .  .  +  .  .
4  .  H  +  +  .  .
5  .  .  .  .  .  .

(0,0) 500 population - 2PAX waiting - 3 PAX Received
(3,2) 1000 population - 1PAX waiting - 1 PAX Received
(1,4) 250 population - 0PAX waiting - 2 PAX Received
```

`.` for empty cells (with no connection)
`+` for connected cells
`H` for cities
`@` for our running vehicles

This can be printed each turn to see how our simulation is working. Check that your vehicle is using the correct routes
and paths!

## Summary

We want now to transport passengers from our cities to other cities. The idea will be to create a simple map with some
connected cities, and let the vehicles do their job. You can take advantage of the already existing map we used before.

If we use the following data:

    | LOCATION   | POPULATION   | 1 turn | 5 turns | 10 turns |
    |------------|--------------|--------|---------|----------|
    |    (0,0)   |        500   |  1 PAX |   5 PAX |   10 PAX |
    |    (3,2)   |       1000   |  2 PAX |  10 PAX |   20 PAX |
    |    (1,4)   |        250   |  0 PAX |   2 PAX |    5 PAX |

If we use the following route: from A`(0,0)` to B`(3,2)`, from B to C`(1,4)` and from C to B again and back to A, we
should have the following numbers for PAX and received PAX

    | LOCATION   | 1 turn             | 7 turn | 12 turn | 17 turn | 22 turn |              
    |------------|--------------------|--------|---------|---------|---------|
    |    (0,0)   | 1 PAX / 0 Received |  6 / 0 | 11 / 0  | 16 / 0  | 21 / 20 | 
    |    (3,2)   | 2 PAX / 0 Received | 14 / 1 | 10 / 1  | 20 / 7  | 10 / 7  | 
    |    (1,4)   | 0 PAX / 0 Received |  3 / 0 |  6 / 14 |  2 / 14 |  5 / 14 | 
