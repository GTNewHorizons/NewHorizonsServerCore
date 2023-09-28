# NewHorizonsServerCore
Bukkit Plugin for Servers running the GT:NH Pack

## Building locally

First, install [Maven](https://maven.apache.org/) (or use IntelliJ's built-in).

### In IntelliJ

1. Open the project directory in IntelliJ
2. Import the Maven project (might happen automatically, check the right sidebar for a Maven panel)
3. Right click on the project (NewHorizonsServerCore) in the explorer on the left, Open Module Settings, in the Project subwindow choose Java 8 as the JDK.
4. In the Maven sidebar, Lifecycle->package
5. Find the jar in `target/NewHorizonsServerCore-VERSION.jar`

### From the terminal

1. Make sure JAVA_HOME points to Java 8
2. Run `mvn package`
3. Find the jar in `target/NewHorizonsServerCore-VERSION.jar`
