# PwGen

Мобильное приложение для безопасной генерации паролей.

## Описание

PwGen - простое и надёжное приложение для создания криптографически стойких паролей на Android и iOS.

## Технологический стек

| Технология | Назначение |
|------------|------------|
| **Kotlin Multiplatform** | Кроссплатформенная разработка (Android/iOS) |
| **Compose Multiplatform** | Единый UI для всех платформ |
| **Orbit MVI** | Архитектура (Model-View-Intent) |
| **Koin** | Dependency Injection |
| **Voyager** | Навигация |
| **Kotest** | Тестирование (BDD-style) |

## Сборка

### Android
```bash
./gradlew :composeApp:assembleDebug
```

### iOS
Открыть `iosApp/iosApp.xcodeproj` в Xcode.