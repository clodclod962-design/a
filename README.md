# MyMod (Fabric 1.20.1, client-side)

Right Shift - открыть меню. ЛКМ - вкл/выкл модуль, ПКМ - настройки, СКМ - назначить клавишу (ESC - снять).
Настройки: .minecraft/config/mymod.json

ВАЖНО: этот архив - ИСХОДНИКИ. В mods/ или в Modrinth App нужен готовый .jar, его надо собрать.

## Способ 1 - без установки (GitHub Actions)
1. Создай репозиторий на github.com и загрузи в него все файлы из этой папки (вместе с .github).
2. Вкладка Actions -> build -> когда зелёная галочка, открой запуск -> Artifacts -> mymod-jar.
3. Внутри будет mymod-1.0.0.jar (файл *-sources.jar не нужен).

## Способ 2 - локально
1. Поставь JDK 17 и Gradle 8.5 (https://gradle.org/releases/).
2. В этой папке: gradle build
3. Jar: build/libs/mymod-1.0.0.jar

## Установка
Нужен профиль Fabric 1.20.1 + Fabric API 0.92.2+1.20.1.
Modrinth App: профиль -> Content -> Add content -> From file -> выбери mymod-1.0.0.jar
(или положи jar в папку mods профиля).
