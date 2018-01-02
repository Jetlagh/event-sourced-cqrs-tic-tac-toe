# Tic-Tac-Toe

## Assignment
* Implement the game of Tic-Tac-Toe based on the product backlog below.
* Use the given source code as a starting point.
* You do ***not*** have to support all requirements: choose whatever you're comfortable with to implement.
* Ensure that you're happy with the implemented functionality.
* Submit your implementation at the latest one day before the interview.
  * We'll review the application so that we can discuss it together.
  * The most important thing is that you can motivate your choices and solutions.
* ***Have fun doing the assignment and make a nice application!***  

## Product backlog
* *As* a player *I want* to play Tic-Tac-Toe against the computer *so that* I can entertain myself.
  * **Acceptance criteria**
    * The user and the computer take turns playing.
    * The first player to achieve three-in-a-row wins.
    * Three-in-a-row can be horizontal, vertical or diagonal.
    * When the game ends a new game can be started.
    * The achieved score is registered.
* *As* a player *I want* my top scores to be registered *so that* I can review my accomplishments.   
  * **Acceptance criteria**
    * The score is calculated based on the lowest number of moves combined with the duration of the game.
      (you don't get points when you lose, and you can assume that the time taken by the computer is negligible)
    * Only the top 10 scores are kept.
    * The scores can be tracked in memory, they don't need to be persisted to a data store.
* *As* a player *I want* to register my name *so that* others can see who achieved what score.
  * **Acceptance criteria**
    * The user can enter their name when the game starts.
* *As* a player *I want* to see my current score compared to that of other players *so that* I know how well I'm currently doing.
  * **Acceptance criteria**
    * The score of the current game is always displayed.
    * The current score is shown in comparison to the top scores.
  
## Given source code
* In the **Trifork - Tic-Tac-Toe** zip file you'll find a basic application.
* The technologies used are:
  * [Spring-Boot](http://projects.spring.io/spring-boot/)
  * [Material Design Light](http://www.getmdl.io/)
  * [Maven](https://maven.apache.org/)
  * [Java 8](http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html)
* The basic application contains the following elements to get you started:
  * A static index page
  * A CSS file containing the styling for the game
  * A JavaScript file which initializes the game
  * A Spring-MVC `RestController` handling POST requests to "/executeTurn" containing the following parameters:
    * **turn**: boolean
    * **column**: int
    * **row**: int
* Feel free to introduce new technologies as part of your implementation:
  * For instance, use Angular for the presentation and client-side components, 
    or use a server-side templating framework like Thymeleaf instead.
* The application can simply be started from your IDE, or by using the Maven command ```mvn spring-boot:run``` 