package com.github.caay2000.ttk.domain.world

data class Cell(val position: Position) {
    constructor(x: Int, y: Int) : this(Position(x, y))
}
