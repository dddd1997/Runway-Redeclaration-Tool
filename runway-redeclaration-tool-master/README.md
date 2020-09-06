# Runway redeclaration tool
A tool for redeclaration of airport runway parameters.\
The main entrypoint class for this project is 'Main' in package 'backend'.\
To run the GUI, you can also run the 'RedeclarationApp' class in package 'frontend'.

## Packaging
To package this project as an archive (.jar file) you can run the 'package' goal of Maven. This can be done either through the IDE or the terminal.

### IDE (IntelliJ IDEA):
1. Through the menu bar: View > Tool Windows > Maven.
2. Expand 'Lifecycle'.
3. Click 'package'.
### Terminal:
1. Change directory to the directory you have cloned the project.
2. Execute the command `$ mvn package`

The above will create a file named **"`runway-redeclaration-tool-3.0-jar-with-dependencies.jar`" in the 'target' sub-directory**. 

##Running
To run the package through the terminal, execute the following command:\
`$ java -jar runway-redeclaration-tool-3.0-jar-with-dependencies.jar OPTIONALLY_INSERT_ANY_ARGUMENTS_HERE`
* Running the package **without any arguments will run the GUI.**
* Running the package **with arguments** will redeclare parameters for a sample runway, plane and obstacle and export to a text file.
 