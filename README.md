# Live-Coding Project as shown at the JUGH Talk "Xtext und die Integration in Java Anwendungen"
Hi there!
This repository contains the live-coding project as shown at the JUGH Talk "Xtext integration in Java Applications" ("Xtext und die Integration in Java Anwendungen" original title) on 29.03.18 at Kassel University. In a first step i showed how a java application can load, parse and consume a configuration written in a self desgined Xtext grammar. The little arkanoid clone (Project arkanoid3d) used the configuration files for creating the rows and their bricks for a level and to order multiple levels for the whole game. In the second part of the talk i enhanced the language with the possiblity to create bricks for a row with inline Xtend-Expressions (if you ask yourself right now what Xtend is: Xtend is a Java like language designed with XText that compile to 100% compatible bytecode). And finally bringing some "magic" to the show: create some Java-Code out of the Xtend-Expressions and reload it while the application is still running!

## Content
The following Java-/Xtext-/Eclipse-Plugin-Projects are part of this repository:

* arkanoid3d (JavaFX Arkanoid Clone (no game functionality included, feel free to implement it ;))).
* arkanoid3d.updatesite (for all of you who want to install the arkanoid3d xtext plugin in eclipse)
* arkanoid3d.dsl.parent (Maven parent project containing required xtext projects)
  * arkanoid3d.dsl (The arkanoid3d dsl language project. Here you find the xtext file with the language definitions and the other language relevant stuff like Validators and Generators)
  * arkanoid3d.dsl.feature (required by the arkanoid3d.update site project. Describes which dependencies are needed by the arkanoid3d xtext plugin for eclipse)
  * arkanoid3d.dsl.ide (Technical stuff required by xtext. Not part of the presentation)
  * arkanoid3d.dsl.target (Technical stuff required by xtext. Not part of the presentation)
  * arkanoid3d.dsl.tests (Contains Unit Tests that covers things like parsing, code generation and scoping)
  * arkanoid3d.dsl.ui (Contains Extension points to extend the functionallity of your ide to match special needs of our android3d language. Was not part of the presentation.)
  * arkanoid3d.dsl.ui.tests (Unit Tests for the arkanoid3d.dsl.ui project. Not part of the presentation.)

 ## Where to start?
 The most interesting projects are arkanoid3d and arkanoid3d.dsl. Head over to arkanoid3d to see how the applications loads, parse and consumes the dsl resources. The dsl resources are stored inside src/main/resources/dsl with the extension 'ark'. Inside package (src/main/java) de.jugh.loader you find three classes that do serve content to the application (some rows with bricks). Only one Implementation of these three is active at time. This is configured inside the ark.properties file, and defaults to DslXbaseGameContentLoader class. The main difference between DslXbaseGameContentLoader and DslGameContentLoader is that the Xbase version knows how to compile and reload the generated Java Class that contains all Xtend-Expressions created inside the dsl resources.

 If you want to know more about the grammar have a look at the arkanoid3d.dsl project. In package de.jugh you find the ArkanoidDsl.xtext file with all grammar rules. In package de.jugh.jvmmodel resides the model inferrer (ArkanoidDslJvmModelInferrer.xtend) or in easy words the thing that generates Java Source code :). The generator class in de.jugh.generator is not used cause of xbase.

 ## If you want to try it for your own
 ### Run only Arkanoid
 If you only like to see the Arkanoid clone in action and like to experiment with the dsl resource simply import the arkanoid3d project into your IDE as a maven project. Actually this is only tested with eclipse but should run with any other IDE. If import finished sucessfully edit the ark.properties file. You have to modify the paths inside so they match the structure in your filesystem (mainly game.dsl.srcpath, game.dsl.genpath). Then create a run configuration for de.jugh.Arkanoid that starts the application. If everthing wents fine you see some rows with colored bricks horray :). If game.dsl.genpath is active (no preceeding #) you should see a Java Source file RowCreators.java in your workspace. Change a expression in RowCreators.ark and press 'r' inside the running application to see how your changes get visible in RowCreators.java. If the application runs in debug mode it is also possible to add breakpoints.

 Beside the 'r' (reload all dsl files) key the application knows further keys that triggers some actions: left, right cycles through the available levels (add a new level while the applications runs hit 'r' and then left or right to get to your newly created level).

 ### Nice, but i want to play along with the language grammar!
 No problem! Simply import the arkanoid3d.dsl.parent project as a maven project into your Eclipse IDE (you have to make sure that XText Plugin is installed)! Other IDEs do not work! To make some changes to the language grammer open the ArkanoidDsl.xtext file and modifiy it. If you are fine with your changes run /arkanoid3d.dsl/src/de/jugh/GenerateArkanoidDsl.mwe2 (Run as ...)

#### Hmm...my IDE did provide me any language support for the arkanoid dsl
 If you want to see any language support for the arkanoid dsl files you need to build the plugin for eclipse and install it. To do so build the whole xtext project with maven (mvn clean install). The open the project arkanoid3d.updatesite and the file site.xml. There should already on feature listed. Remove this feature by selecting it and hitting delete on your keyboard. Click "Add feature" and chose arkanoid3d.dsl.feature with version 1.0.0.qualifier. Then hit the Button "Build". The version changes from 1.0.0.qualifier to a new version number with an actual timestamp. Now you are ready to install the plugin. To do so open the "Install new software" dialog and click "Add". In the following dialog set field name to Arkanoid DSL (or whatever you want) and location to "file:/MYWORSPACEPATH/arkanoid3d/arkanoid3d.updatesite/". Close the dialog with ok. Now the new update site should be available. Select it and you will see the dsl plugin ready for installation!