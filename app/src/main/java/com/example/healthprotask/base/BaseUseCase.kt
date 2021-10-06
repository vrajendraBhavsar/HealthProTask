package com.example.healthprotask.base

abstract class BaseUseCase<in Req, out Res> {
    abstract suspend fun execute(param: Req): Res
}