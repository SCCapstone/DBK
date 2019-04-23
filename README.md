# DBK App

DBK is a company that uses heat and air based equipment to dry out homes after floods. In order to make this process possible, certain numbers and equations are used to determine how much power and heat is needed to fully dry a home. This mobile app (for android using Java SDK) will be designed to make calculations for drying easier to compute for the customer. The goal of this project is for a customer to be able to use this app, and based off the calculations, graphs, and equations, be able to assess how much thermal energy is needed to dry out the water. It will also be used in conjunction with the drymatic equipment, connecting to it via Bluetooth.

## Video

## Screenshots

## Installation 
- Download Android Studio
- Once Android Studio is downloaded and installed, download the program from the DBK Repository.   
- An alternative way to get the program is to Import From Version Control using the DBK repo URL
- Once the program is downloaded open it with Android Studio 

Note: make sure you are using at least API 21 or higher. can be downloaded in Tools > AVD Manager. Also, use gradle version 3.3.2 as used by the program. 

If the emulator is crashing delete it and reinstall it for this program, the emulator may not be using the correct version.

## Testing

### Unit Testing

To execute the tests on your own system, simply run `./gradlew test` from the project directory.

- located in app > java > edu.sc.dbkdrymatic(test) 
- run the .java unit test

### Behavior Testing

- located in app > java > edu.sc.dbkdrymatic(androidTest) 
- run the .java instrumented test 

## Built With 

- Android Studio
- Java SDK
- Room Persistence Libary

## Team

[George Brown](https://github.com/holtb4000)

[Nicholas Ford](https://github.com/ntford)

[Andrew Lokantsov](https://github.com/Lokantsov)

[Lane Rassmussen](https://github.com/lanerass)

[Peter Sanders](https://github.com/hxtk)

