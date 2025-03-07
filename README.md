# Edugma
Edugma - приложение для учебных заведений. Чтобы приложение могло взаимодействовать с учебным заведением, нужно реализовать [Edugma API](https://edugma.github.io/docs/).

- [Сайт](https://edugma.com/)
- [Документация](https://edugma.github.io/docs/)
- [Google Play](https://play.google.com/store/apps/details?id=com.edugma.android)
- [RuStore](https://www.rustore.ru/catalog/app/com.edugma.android)

# Запуск Web версии
```shell
./gradlew :web:app:jsRun
```

# Сборка Web версии
```shell
./gradlew jsBrowserDistribution
```

```shell
./gradlew :ios:shared:embedAndSignAppleFrameworkForXcode
```

Make all scripts executable
```shell
chmod -R +x scripts/
```

Build release apk
```shell
./scripts/build_release_apk.sh
```

Build release bundle
```shell
./scripts/build_release_bundle.sh
```

Build qa apk
```shell
./gradlew :android:app:assembleQa -Pbuildkonfig.flavor=qa
```

Build debug apk
```shell
./gradlew :android:app:assembleDebug
```
