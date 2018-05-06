# Pinterest Downloader

> **This version of Pinterest Downloader is deprecated, please checkout [Juraji/Pinterest-Downloader-2](https://github.com/Juraji/Pinterest-Downloader-2).**

## Table of contents

## About
After trying many...many Pinterest download tools, all being either paid apps or very limited,
I decided to create my own version.

Pinterest Downloader is able to fetch all pins from all your boards and store them in a directory
on your machine with sub-directories corresponding to each board.

## Installation

### Prerequisites

* [Java 8 or higher](https://www.java.com/en/).
    1. Go to [java.com](https://www.java.com/en/).
    2. Click **Free Java Download**. *Yes, the big red advertisement look-a-like button at the center.*
    3. Read the [End user licence agreement](http://www.oracle.com/technetwork/java/javase/terms/license/)
    4. Click **Agree and Start Free Download**. *Indeed, the second big red advertisement look-a-like button at the center.*
    5. The Java installation tool, appropriate for your system will start downloading.
    6. When the download has finished open up the installer and follow the steps through the wizard.

### Installation

1. Download the latest release from [Pinterest-Downloader/releases](https://github.com/Juraji/Pinterest-Downloader/releases).
2. Extract the downloaded file to a location on your machine.
3. Navigate to the extracted directory and check if at least the following files are present:
    * **pinterest-downloader-x.x.jar** *(The main executable)*
    * **lib** *(A directory with libraries used by Pinterest Downloader)*
4. Start Pinterest Downloader by opening **pinterest-downloader-x.x.jar**.

For any further explanation on how to use Pinterest Downloader, please refer to the built in manuals.

## Getting started

The built in manuals should provide you with all the information you need to start downloading those precious pins.
Find the built in manuals via the top menu under **Help**.

## Development

### IDE limitations
This application uses libraries only available in **JetBrains IntelliJ**. *IntelliJ is love, IntelliJ is life.*
Already using **JetBrains IntelliJ**? Go ahead check-out or download the repository and open it up in **JetBrains IntelliJ**.

**JetBrains IntelliJ** is available as free community edition. Download it over at [JetBrains.com](http://www.JetBrains.com/idea/#chooseYourEdition).
Go ahead, you'll love it!

You could try opening the project in a alternative IDE, but you will have to cope with figuring out how to have the compiler
generate the sources for the UI.

### Minimum required JetBrains plugins for IntelliJ
* Maven
* GUI Designer

### Notice
Some of the libraries used in this repository are other *not-on-github* projects. I included the builds into a project local
Maven repository. The Maven pom.xml is configured to use this local repository as well as the Maven Central.

If you are experiencing trouble detecting packages in the local repository AND you are using a custom Maven settings.xml.
Then make sure to set any mirrors to only mirror external repositories or set them as specific as possible.
This might help: `<mirrorOf>external:*</mirrorOf>`.

### Testing
I have not implemented any tests yet, but in time I will.

### Building
Execute the Maven goal `mvn package`. This can be done using:

* the **Maven** integration of **JetBrains IntelliJ**.
* Opening the system console, navigating to the project directory and executing `mvn package`.

## Todo
These things are currently on my wishlist. Have suggestions? Create an issue on the [Issue Tracker](https://github.com/Juraji/Pinterest-Downloader/issues) and I shall have a look at it.

* Come up with a better name, this one is too cheesy...
* Implement better way of aquiring a Pinterest access token
* Rework messy UI code
* Update built in manual to feature the **entire** application.
