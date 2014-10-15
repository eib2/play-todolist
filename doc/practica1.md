# Primera iteración de la aplicación _play-todolist_

*Por: Eduardo Isabel Bernabé (eib2@alu.ua.es)*
* * *

##¿Qué es _play-todolist_?

Es una aplicación web de gestión de tareas personales que permite insertar tareas de forma anónima o para un usuario concreto junto a una fecha límite opcional y mostrar las tareas insertadas filtradas por usuario y fecha (opcional).

##¿Cómo funciona?

A continuación se muestra la estructura de uso:

###HOME
+ `GET /` Página Home.

####Feature 1
+ `GET /tasks` Devuelve todas las tareas del usuario _Anonymous_.
+ `GET /tasks/:id` Devuelve la tarea con el _id_ deseado.
+ `POST /tasks` Crea una tarea como usuario _Anonymous_.
+ `DELETE /tasks/:id` Borra la tarea con el _id_ deseado.

####Feature 2
+ `GET /:login/tasks` Devuelve todas las tareas del usuario deseado.
+ `POST /:login/tasks` Crea una tarea con el usuario deseado.

####Feature 3
+ `GET /dateBefore/before/tasks` Devuelve todas las tareas cuya fecha límite sea anterior a la indicada.
+ `GET /:login/:enddate/tasks` Crea una tarea con el usuario y fecha límite indicados.


##Desarrollo de _play-todolist_

Esta aplicación está implementado mediante el lenguaje de programación Scala usando el **Play framework**. Es una aplicación **REST** que usa el formato **JSON** para las respuestas HTTP.

Se ha desarrollado usando el control de versiones **Git** y los incrementos se han añadido en un repositorio de **BitBucket**.











