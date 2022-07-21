# Transport Madness Kata

This is a kata inspired by Transport Tycoon Kata by SoftwarePark. You can find the original
one [here](https://github.com/Softwarepark/exercises/blob/master/transport-tycoon.md)

# Transport Madness Exercises

## Exercise 1

Create a 4x6 map and make a vehicle with a route from (0,0) (top,left) to (3,2).

Everytime the update method on the vehicle is called, the vehicle should do one step towards the destination, 
so after 5 updates it should be in the final destination (3,2)
    
    |                | data 1 | data 2 |
    |----------------|--------|--------|
    | start position |   0,0  |   0,0  |
    | destination    |   3,2  |   0,4  |
    | turns          |    5   |    4   |


## Exercise 2

Now that we have a moving vehicle, let's create a route for it.
The idea is to assign a route for the vehicle, for example A(0,0) - B(3,2) - C(1,4) and then back to A  

    |   | 0 | 1 | 2 | 3 | 4 | 5 |
    |---|---|---|---|---|---|---|
    | 0 | A | - | - | - | \ |   |
    | 1 | ¦ |   |   |   | C |   |
    | 2 | ¦ |   |   |   | ¦ |   |
    | 3 | \ | _ | B | _ | / |   |

We also want to stay 1 turn in each point, so the time to complete the route, from A to A again will be:
 
    |A stop  |  1 turn  |
    |A to B  |  5 turns |
    |B stop  |  1 turn  |
    |B to C  |  4 turn  |
    |C stop  |  1 turn  |
    |C to A  |  5 turn  |
    |TOTAL   | 17 turns |

