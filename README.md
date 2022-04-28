XML-PARSER. TEST TASK
=====================

Настройка и запуск:
-------------------

1. В папке проекта resources находятся 2 xml-файла для тестирования.  
   В файле example2 есть дублирующие строки (по отношению к example), но есть и новая.
   
2. Приложение слушает базу postgresql на порту 5484, перед его запуском, нужно настроить  
   соединение с БД. Это можно сделать двумя способами:
    - запустить файл docker-compose, либо из среды разработки, либо командой из консоли в папке приложения: docker-compose up -d
    - настроить БД локально на Вашей машине.
    
3. Если вы настраиваете БД локально и порт 5484 занят, то в папке resources нужно изменить 
   файл с настройками application.properties:
   >> db.url=jdbc:postgresql://127.0.0.1:<ВАШ ПОРТ>/metaprime_db
   
4. Само приложение можно запустить из Вашей среды разработки, клонировав репозиторий github, либо запустив
   jar-файл командой 
   >> java -jar xml-parser.jar
   
5. Приложение общается с пользователем через консольный интерфейс.
   
6. Если при запуске приложения в консоли возникнут проблемы с кодировкой, то нужно выйти из приложения (команда q),
   и сменить кодировку:
        - UTF-8, команда: <chcp 65001>
        - Windows-кодировка, команда: <chcp 1251>
        - DOS-кодировка, команда: <chcp 866>
   
7. Приложение принимает на вход путь к файлу и тэг сущности, которую нужно получить в результате парсинга.
   
8. При парсинге 2 тестовых файлов в БД запишется 16 уникальных строк. Общее количество сущностей в 2 файлах - 
   33(30 - в первом файле), сущности в xml дублируются.

9. build проекта из intellij idea для работы в консоли нужно делать через плагин   
  >>shadowJar