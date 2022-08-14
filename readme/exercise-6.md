## Exercise 6 - Transport II

Now that we move passengers, let's do it properly, with railcars and trains with engines and different coaches

<kbd> <img src="exercise_6_header.png" /> </kbd>

[Home](../README.md) | [Exercise 5 - Transport I](exercise-5.md)

## Summary

The idea is to have different type of vehicles, and prepare them for an specific amount of max passengers each. First of all
we are going to have 2 different vehicle types, passenger train and a rail-car.

* The passenger train could have up to 3 passengers coaches, each with 20 seats (max of 60 passengers)
* The rail car can handle up to 30 passengers, with no coaches at all.

So lets try this exercise:

With a simple route from 0,0 to 3,0 (each location with population 5000)

    | CITY SIZE  | 1 turn | 5 turns | 10 turns |
    |------------|--------|---------|----------|
    |     5000   | 10 PAX |  50 PAX |  100 PAX |

    | LOCATION   | 1 turn              | 7 turn | 12 turn | 17 turn | 22 turn |              
    |------------|---------------------|--------|---------|---------|---------|
    |    (0,0)   | 10 PAX / 0 Received |  6 / 0 | 11 / 0  | 16 / 0  | 21 / 20 | 
    |    (3,0)   | 10 PAX / 0 Received | 14 / 1 | 10 / 1  | 20 / 7  | 10 / 7  |