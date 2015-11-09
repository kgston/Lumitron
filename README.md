# Lumitron
Just because we need a little Christmas bling

## Task board
View all tasks here --> <https://www.pivotaltracker.com/n/projects/1462924> 

## Prerequesites
1. [Eclipse](https://eclipse.org/downloads/) (for development only)
2. [Gradle IDE Pack](http://marketplace.eclipse.org/content/gradle-ide-pack) (for development only)
    *If you already installed the previous Buildship one, I recommend this instead. It is faster to run from eclipse and has less bugs*
3. [Gradle](http://gradle.org/download) (for development only) *Not necessary if you run gradle directly from CLI*
4. [Java JDK 1.7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)

*Final build is expected to compile into an all-in-one JAR with an embedded webserver* 

## Development Rules 
While no one really like to follow rules, let try to keep a few important ones to prevent the code base from becoming a mess

- Create a new branch when you start something new 
  * Follow the following convension 
    * feature-YourFeatureName 
    * bugfix-FeatureName-FixName
  * Create a pull request when you're done with your work and I'll merge it in if there aren't any issues
    * Please don't merge broken code
- Comment your code as much as you can 
  * Especially if you program like Yoda

## Setting up
1. Clone the project to your local evnironment
2. Install all prerequsites (or use your existing ones)
3. Open Eclipse (I'm using Mars) > File > Import
4. Choose Gradle > Gradle Project
5. Set the folder Lumitron as the project root > Click Build Model button (top right corner) > Select project > Finish
6. Go to Window > Preference > Gradle > Set your Gradle home (I'm using v2.8)
7. Under Gradle/Arguments > Set your Workspace JRE (I'm using 1.7.0_79)
8. Go to Gradle EnIDE > Set your Gradle home and JAVA_HOME
9. If its not already there, open the Gradle Tasks view at Window > Show View > Others. Select Gradle > Gradle Tasks
10. You should be able to see all project tasks in the view. Double click tomcatRun to start the webserver
  * If you are hardcore and like CLI interface, go to the project root and run ` gradlew tomcatRun`
  *Assuming that you have set gradle/bin in your path*
  * A few other useful commands
  * `tomcatStop` - Stop the tomcat server (run from another terminal instance)
  * `clean` - To clean the build dir
  * `build` - To rebuild class files
  * `war` - Packages a WAR file into build/lib
11. App is accessable at <http://localhost:8080/Lumitron>
12. If you have eclipse project issues, go to the Gradle Tasks window abd run the eclipseClasspath task. E.g. If it complains it cannot find class or etc...

## Final notes
While I sometimes like to think I'm a coding god, reality is tougher than it seems... So like everyone else, I'm learning new things everyday. And while I'm supposed to "lead" the project, I'll probably make rookie mistakes every now and then or ask stupid questions during pull request, so please be patient with me. I'll try to be smart 80% of the time.. haha... :D

## Contribuitors
- Kingston Chan
- Christophe Michard
- Wang Zhiyue
- Vadim Burlakin

## Acknoledgements
[Entypo+ pictograms](http://www.entypo.com/index.php)