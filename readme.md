TDD approach is followed to test the API endpoints.

Run MySQL docker container
- `docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql:8.0`
- Now we can run TDD classes once MySQL container is up.

#### Run application using docker.
Start the application
- `docker-compose up --build`

Go inside of container and create database
- `docker exec -it <container_name> mysql -uroot -proot`
- `create database grocery_booking`