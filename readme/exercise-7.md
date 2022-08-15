## Exercise 7 - Map I

Let's migrate our current map to an amazing hexagonal grid!

<kbd> <img src="exercise_7_header.png" /> </kbd>

[Home](../README.md) | [Exercise 6 - Transport II](exercise-6.md)

## Summary

So, looking at our current map, it's not realistic to have 90 degrees turns for our connections. So let's try to use an
hexagonal grid that will allow us smoother turns and will change our current timings.

You can read anything you need about hexagonal grids in [here](https://www.redblobgames.com/grids/hexagons/), an amazing
page from [@redblobgames](https://github.com/redblobgames) where you can learn a lot about algorithms, not only about
hexagonal grids!

Following Amit's page I will use an _“odd-r” horizontal layout_ in an offset coordinates

The exercise will be to change our current squared grid for a hexagonal grid, and try to visualise it! I will propose
the following visualisation:

         . . . . . . .
          . . . . . . .
         . . . . . . .
          . . . . . . .
         . . . . . . .
          . . . . . . .
         . . . . . . .

So, our current exercise with A(0,0) B at (2,3) and C at (1,4) and connections between them, will be represented like
this:

         A x x . . . .
          x . x . . . .
         x . . B . . .
          x . x . . . .
         . C x . . . .
          . . . . . . .
         . . . . . . .


