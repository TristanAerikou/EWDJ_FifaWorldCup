# EWDJ_FifaWorldCup

## Booting the application
1. run the command `docker compose up` in root directory
2. run [FifaWorldCupApplication.java](src/main/java/lv/ewdj/fifaworldcup/FifaWorldCupApplication.java)
3. go to `http://localhost:8080`

## Run tests
1. run the command `docker compose up` in root directory 
2. run the tests

Why do the tests need docker to run?
: The way the application is made, some of the tests cannot be ran without spinning up a (mini) SpringBoot context (using `@SpringBootTest`).  
The outlier in this is [InputGameDtoTest.java](src/test/java/lv/ewdj/fifaworldcup/dto/InputGameDtoTest.java).

## Seeding & Notable Login Credentials for testing
> See [InitDataConfig.java](src/main/java/lv/ewdj/fifaworldcup/config/InitDataConfig.java)
>
> Please note that the absolute necessary seeding has been deployed (users, games) but other entities will have to be entered manually through the application. Only a handful of prognoses have been seeded.
> 
> Lastly, please note that [application.properties](src/main/resources/application.properties) currently defines the database ddl as "create-drop".

| Username      | Password | Role  |
|---------------|----------|-------|
| RockFromSpace | Meteor   | Admin |
| Mercy         | fifa     | User  |
| Spaghetti     | fifa     | User  |
| Flowie        | fifa     | User  |

| User          | Game | Prognosis |
|---------------|------|-----------|
| Mercy         | Sixth Street vs. Eighth Street | 1 - 1     |
| RockFromSpace | Sixth Street vs. Eighth Street     | 2 - 3     |
| Mercy         | Ravensburger vs. Google     | 5 - 1     |