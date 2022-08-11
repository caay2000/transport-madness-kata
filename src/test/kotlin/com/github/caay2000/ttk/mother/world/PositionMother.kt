package com.github.caay2000.ttk.mother.world

import com.github.caay2000.ttk.context.world.domain.Position
import kotlin.random.Random

object PositionMother {

    fun random(
        maxX: Int = 6,
        maxY: Int = 6
    ) = Position(
        x = Random.nextInt(0, maxX),
        y = Random.nextInt(0, maxY)
    )
}
