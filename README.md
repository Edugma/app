# Edugma
Edugma - децентрализованное приложение для учебных заведений. Чтобы приложение могло взаимодействовать с учебным заведением, нужно реализовать [Edugma API](https://edugma.github.io/docs/).

[Документация](https://edugma.github.io/docs/)


## Стэк технологий

### Структура
- Многомодульная Clean архитектура
- MVI (самописный)
- [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html)

### UI
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - декларативный UI фрейворк от Google
- [Compose Material 3](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary) - Material You для Compose
- [Coil](https://coil-kt.github.io/coil/) - загрузка изображений
- [Jetpack Navigation (Compose)](https://developer.android.com/jetpack/compose/navigation) - навигация в приложении (с самописной оболочкой)
- [Fluent UI System Icons](https://github.com/microsoft/fluentui-system-icons) - коллекция иконок из Microsoft Fluent UI
- [Lottie](https://lottiefiles.com/) - для анимаций
- [Glance AppWidget](https://developer.android.com/jetpack/androidx/releases/glance) - Compose для виджетов
- [Splash Screen API](https://developer.android.com/guide/topics/ui/splash-screen) - для реализации Splash Screen по новому API

### Получение и хранение данных
- [Coroutines](https://developer.android.com/kotlin/coroutines) и [Flow](https://developer.android.com/kotlin/flow)
- [Koin](https://insert-koin.io/) - DI фреймворк
- [Paging V3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - пагинация
- [Retrofit](https://square.github.io/retrofit/) - взаимодействие с сетью
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - сериализация данных
- [KodeinDB](https://docs.kodein.org/kodein-db/) - NoSQL база данных с kotlinx.serialization для сериализации под капотом

# Запуск Web версии
```shell
./gradlew :web:app:jsRun
```

# Сборка Web версии
```shell
./gradlew :web:app:jsBrowserProductionWebpack
```

## Скриншоты
Пример применения динамических цветов из Material You (Android 12+)

![material_you](https://github.com/edugma/app/raw/develop/screenshots/material_you.png)

Остальное

![screenshots_1](https://github.com/edugma/app/raw/develop/screenshots/screenshots_1.png)

### Скриншоты прошлого приложения

![screenshots 1](https://github.com/mospolyhelper/mospolyhelper-android/raw/master/docs/screenshots/screenshots_1.png)
![screenshots 2](https://github.com/mospolyhelper/mospolyhelper-android/raw/master/docs/screenshots/screenshots_2.png)


```bash
./gradlew :ios:shared:embedAndSignAppleFrameworkForXcode
```


```kotlin
./gradlew :android:app:assembleRelease
```

```kotlin
./gradlew :android:app:assembleDebug
```
