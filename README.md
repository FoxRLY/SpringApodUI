# Как запускать:
1) Создать новую папку для удобства дальнейшей работы
2) Склонировать оба проекта SpringApod и SpringApodUI в эту папку(желательно в отдельные папки nasaapod и apodui соответственно)
3) Создать файл docker-compose.yml в этой новой папке с содержимым:
```
include:
  - ./nasaapod/docker-compose.yml
  - ./apodui/docker-compose.yml
version: "3.9"
```
---
На порту 8092 откроется веб-интерфейс, для теста можно ввести дату в формате ```yyyy-MM-dd```, например ```2020-02-02```
. API NASA работает довольно медленно в России, потому придется некоторое время подождать для загрузки картинки.