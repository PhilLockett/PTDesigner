# PTDesigner

A simple application to design a periodic table layout.

## Overview

This project has been set up as a Maven project that uses JavaFX, FXML and 
CSS to render the GUI. Maven can be run from the command line, it resolves 
the dependencies and builds the application independently of an IDE.

The intention of this application is to easily enable changes to be made to a 
Periodic Table layout in order to try and find an improvement to the standard 
layout (good luck). The Status tab attempts to calculate the quality of the 
layout, however, this is a first attempt and I'm sure there are better methods 
but I haven't found them.  

## Dependencies

I built PTDesigner with the following:

  * Java 15.0.1
  * Apache Maven 3.6.3
  * Git 2.21.0

## Cloning and Running the GUI version

The code has been structured as a standard Maven project which requires Maven 
and a JDK to be installed. A quick web search will help, but if not 
[Apache](https://maven.apache.org/install.html) should guide you through the
install. Also [OpenJFX](https://openjfx.io/openjfx-docs/) can help set up your 
favourite IDE.

The following commands clone and execute the code:

    git clone https://github.com/PhilLockett/PTDesigner.git
	cd PTDesigner/
	mvn clean javafx:run

## Points of interest

This code has the following points of interest:

  * PTDesigner is Maven project that uses JavaFX, FXML and CSS.
  * PTDesigner uses Chemical Element data from the elements package.
  * The tabbed window is created using FXML and CSS.
  * The table/grid window is generated with code.
  * The GUI was developed using SceneBuilder.
  