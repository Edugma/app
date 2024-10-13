package com.edugma.features.misc.other.inAppUpdate.domain

import com.edugma.core.api.repository.BuildConfigRepository
import com.edugma.core.api.repository.MainSnackbarRepository
import com.edugma.core.api.repository.SettingsRepository
import com.edugma.core.api.utils.sendWarningWithResult
import com.edugma.core.navigation.core.router.external.ExternalRouter
import com.edugma.features.misc.other.inAppUpdate.data.InAppUpdateService
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.Duration.Companion.days

class CheckUpdateUseCase(
    private val inAppUpdateService: InAppUpdateService,
    private val parseSemVerUseCase: ParseSemVerUseCase,
    private val buildConfigRepository: BuildConfigRepository,
    private val appSnackbarRepository: MainSnackbarRepository,
    private val settingsRepository: SettingsRepository,
    private val router: ExternalRouter,
) {
    // TODO перепродумать и проверить время
    suspend operator fun invoke() {
        val lastUpdateIso = settingsRepository.getString(LastUpdateKey)
        val lastUpdate = if (lastUpdateIso == null) {
            Instant.DISTANT_PAST
        } else {
            Instant.parse(lastUpdateIso)
        }

        val now = Clock.System.now()
        val timeToCheck = 1.days
        val timeToShowRequired = 1.days
        val timeToShowSoft = 1.days

        if (now - lastUpdate < timeToCheck) return

        val version = inAppUpdateService.getLastVersion().getOrThrow()
        val minVersion = parseSemVerUseCase(version.min)
        val lastVersion = parseSemVerUseCase(version.last)
        val appVersion = parseSemVerUseCase(buildConfigRepository.getVersion())

        if (appVersion < minVersion) {
            if (now - lastUpdate > timeToShowRequired) {
                val actionResult = appSnackbarRepository.sendWarningWithResult(
                    title = "Срочно обновите приложение",
                    subtitle = "Версия приложения устарела и больше не поддерживается!",
                    action = "Обновить",
                    timeToDismiss = INFINITE,
                )
                settingsRepository.saveString(LastUpdateKey, now.toString())
                if (actionResult) {
                    router.openStore()
                }
            }
        } else if (appVersion < lastVersion) {
            if (now - lastUpdate > timeToShowSoft) {
                val actionResult = appSnackbarRepository.sendWarningWithResult(
                    title = "Обновите приложение",
                    subtitle = "Доступна новая версия приложения",
                    action = "Обновить",
                    timeToDismiss = INFINITE,
                )
                settingsRepository.saveString(LastUpdateKey, now.toString())
                if (actionResult) {
                    router.openStore()
                }
            }
        } else {
            settingsRepository.saveString(LastUpdateKey, now.toString())
        }
    }

    companion object {
        private const val LastUpdateKey = "inAppUpdateLastUpdate"
    }
}
