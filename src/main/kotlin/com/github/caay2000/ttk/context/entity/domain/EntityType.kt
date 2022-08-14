package com.github.caay2000.ttk.context.entity.domain

sealed class EntityType(
    val vehicleType: VehicleType
)

sealed class TrainEntityType(
    val maxNumCoaches: Int,
    val passengersPerCoach: Int
) : EntityType(vehicleType = VehicleType.TRAIN)

object PassengerTrain : TrainEntityType(maxNumCoaches = 3, passengersPerCoach = 20)
object Railcar : TrainEntityType(maxNumCoaches = 1, passengersPerCoach = 30)
