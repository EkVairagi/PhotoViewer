package com.xynderous.vatole.photoviewer.utils

sealed class DashboardState

object LoadingState : DashboardState()
object LoadingNextPageState : DashboardState()
object ContentState : DashboardState()
object ContentNextPageState : DashboardState()
class ErrorState(val message: String) : DashboardState()
class ErrorNextPageState(val message: String) : DashboardState()

