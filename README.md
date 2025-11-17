-- Simple REST service --

Open the project in an IDE (i used Intellij) and run it.

data is stored locally in a txt file titled: "saved.txt"

Test the api: http://localhost:8080/api/lists

I used Postman for the taskboards API:

1. View All Lists (GET)

  GET http://localhost:8080/api/lists
Click "Send"

2. Create a List (POST)

  POST http://localhost:8080/api/lists  
Body → raw → JSON  
Body: {"name": "listName"}

3. Add a Task (POST)

  POST http://localhost:8080/api/lists/listName/tasks
Body: {"name": "taskName", "description": "taskDescription"}

4. Update a Task (PUT)

  PUT http://localhost:8080/api/lists/listName/tasks/taskName
Body: {"name": "newTaskName", "description": "newTaskDescription"}

5. Move a Task (POST)

  POST http://localhost:8080/api/lists/fromList/tasks/taskName/move
Body: {"toList": "In Progress"}

6. Delete a Task (DELETE)

  DELETE http://localhost:8080/api/lists/listName/tasks/taskName

7. Delete a List (DELETE)

  DELETE http://localhost:8080/api/lists/listName


-- thats it :)
