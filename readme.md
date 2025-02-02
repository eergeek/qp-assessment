Run MySQL docker container
- `docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql:8.0`

Start the application
- `docker-compose up --build`

Go inside of container and create database
- `docker exec -it b9c mysql -uroot -proot`
- `create database grocery_booking`