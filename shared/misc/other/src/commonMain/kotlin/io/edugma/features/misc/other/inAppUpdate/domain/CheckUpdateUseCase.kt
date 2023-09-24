package io.edugma.features.misc.other.inAppUpdate.domain

import io.edugma.core.api.repository.BuildConfigRepository
import io.edugma.core.api.repository.MainSnackbarRepository
import io.edugma.core.api.repository.SettingsRepository
import io.edugma.core.api.utils.sendWarningWithResult
import io.edugma.features.misc.other.inAppUpdate.data.InAppUpdateService
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
) {
    suspend operator fun invoke() {
        val version = inAppUpdateService.getLastVersion().getOrThrow()
        val minVersion = parseSemVerUseCase(version.min)
        val lastVersion = parseSemVerUseCase(version.last)
        val appVersion = parseSemVerUseCase(buildConfigRepository.getVersion())

        val lastUpdateIso = settingsRepository.getString(LastUpdateKey)
        val lastUpdate = if (lastUpdateIso == null) {
            Instant.DISTANT_PAST
        } else {
            Instant.parse(lastUpdateIso)
        }

        val now = Clock.System.now()

        val timeToShow = 0.days

        if (now - lastUpdate > timeToShow) {
            if (appVersion < minVersion) {
                val actionResult = appSnackbarRepository.sendWarningWithResult(
                    title = "Срочно обновите приложение",
                    subtitle = "Версия приложения устарела и больше не поддерживается!",
                    action = "Обновить",
                    timeToDismiss = INFINITE,
                )
                settingsRepository.saveString(LastUpdateKey, now.toString())
            } else if (appVersion < lastVersion) {
                val actionResult = appSnackbarRepository.sendWarningWithResult(
                    title = "Обновите приложение",
                    subtitle = "Доступна новая версия приложения",
                    action = "Обновить",
                    timeToDismiss = INFINITE,
                )
                settingsRepository.saveString(LastUpdateKey, now.toString())
            }
        }
    }

    companion object {
        private const val LastUpdateKey = "inAppUpdateLastUpdate"
    }
}
