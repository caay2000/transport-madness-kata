package com.github.caay2000.ttk.context.entity.domain

sealed class EntityType(
    val vehicleType: VehicleType
) {
    abstract val numCoaches: Int
    abstract val maxNumCoaches: Int
    abstract val passengersPerCoach: Int

    val passengerCapacity: Int
        get() = passengersPerCoach * numCoaches

    val name: String = this::class.simpleName!!
}

sealed class TrainEntityType : EntityType(vehicleType = VehicleType.TRAIN) {

    protected fun validate() {
        if (numCoaches > maxNumCoaches) throw EntityInvalidNumOfCoachesException(numCoaches, this)
    }
}

data class PassengerTrain constructor(
    override val numCoaches: Int,
    override val maxNumCoaches: Int,
    override val passengersPerCoach: Int
) : TrainEntityType() {
    constructor(numCoaches: Int = 3) : this(numCoaches = numCoaches, maxNumCoaches = 3, passengersPerCoach = 20)

    init {
        validate()
    }
}

data class Railcar constructor(
    override val numCoaches: Int,
    override val maxNumCoaches: Int,
    override val passengersPerCoach: Int
) : TrainEntityType() {
    constructor() : this(numCoaches = 1, maxNumCoaches = 1, passengersPerCoach = 30)

    init {
        validate()
    }
}
