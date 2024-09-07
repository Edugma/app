package com.edugma.core.navigation.core

import com.edugma.core.api.api.CrashAnalytics
import com.edugma.navigation.core.router.DefaultRouter
import com.edugma.navigation.core.router.NavigationCommand
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class TabMenuRouter : DefaultRouter() {
    override val commands: Flow<NavigationCommand>
        get() = super.commands.onEach {
            CrashAnalytics.log("Tab menu navigation", data = mapOf("command" to it))
        }
}
class HomeRouter : DefaultRouter() {
    override val commands: Flow<NavigationCommand>
        get() = super.commands.onEach {
            CrashAnalytics.log("Home navigation", data = mapOf("command" to it))
        }
}
class ScheduleRouter : DefaultRouter() {
    override val commands: Flow<NavigationCommand>
        get() = super.commands.onEach {
            CrashAnalytics.log("Schedule navigation", data = mapOf("command" to it))
        }
}
class AccountRouter : DefaultRouter() {
    override val commands: Flow<NavigationCommand>
        get() = super.commands.onEach {
            CrashAnalytics.log("Account navigation", data = mapOf("command" to it))
        }
}
class MiscRouter : DefaultRouter() {
    override val commands: Flow<NavigationCommand>
        get() = super.commands.onEach {
            CrashAnalytics.log("Misc navigation", data = mapOf("command" to it))
        }
}
