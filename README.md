# Java_RU_text4tts
Попытка работы со словарями замен для подготовки текста к произношению голосовым движком. Пишется на Java + RegExp. Репозиторий содержит два проекта:

1) Папка Eclipse - проект под Winndows. В нём удобнее отлаживать алгоритмическую часть (алгоритмическая часть и сами словари лежат в папке Android) т.к. не надо каждый раз запускать тяжеловесный эмулятор Android.
     Для экспериментов с программой/словарями нужно:
       а) Скачать   Eclipse IDE (for Java)
       б) Скачать JRE- Java-машина
       в) Скачать https://github.com/yrasik/txt4tts_RU (кнопка "Clone or download")
       г) Для случая download - распаковать архив txt4tts_RU-master.zip, скажем на диск C  в корень
       д) Запустить Eclipse IDE -> File -> Import -> General-> Existing progect into workspace -> Select root directory -> путь к файлу c:\txt4tts_RU-master\Eclipse\.project.
       е) Запустить саму программу на выполнение (зелёный кружок с треугольничком). Результаты работы в папке tests ....

2) Папка Android - проект под Android 4.4 + (Android Studio 2.3.3). В этой папке содержатся проектные файлы Android Studio, файлы графического интерфейса и др.
Общая алгоритмическая часть лежит в папке: txt4tts_RU\Android\app\src\main\java\yras\txt4tts_ru\common\
Словари с форума http://mytts.info (допиливаемые по мере необходимости) лежат в папке txt4tts_RU\Android\app\src\main\assets\dic\
После сборки дистрибутив Android - программы (app-debug.apk) можно найти в папке txt4tts_RU\Android\app\build\outputs\apk\
Этот файл можно закинуть в телефон/планшет и установить в систему...