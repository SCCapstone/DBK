DBK is a company that uses heat and air based equipment to dry out homes after floods. In order to make this process possible, certain numbers and equations are used to determine how much power and heat is needed to fully dry a home. This mobile app (for android using Java SDK) will be designed to make calculations for drying easier to compute for the customer. The goal of this project is for a customer to be able to use this app, and based off the calculations, graphs, and equations, be able to assess how much thermal energy is needed to dry out the water. It will also be used in conjunction with the drymatic equipment, connecting to it via Bluetooth.

## Video

## Screenshots
<img src="https://user-images.githubusercontent.com/42427359/56622033-92c41a00-65fc-11e9-99b6-dd1ccd843879.png" width="500">
<img src="https://user-images.githubusercontent.com/42427359/56622176-14b44300-65fd-11e9-96f6-eff72bdc8924.png" width="500">
<img src="https://user-images.githubusercontent.com/42427359/56622195-1c73e780-65fd-11e9-8bcd-9635f59f4094.png" width="500">
<img src="https://user-images.githubusercontent.com/42427359/56622215-2c8bc700-65fd-11e9-8eea-b4034f7966b2.png" width="500">
<img src="https://user-images.githubusercontent.com/42427359/56622228-34e40200-65fd-11e9-9a71-4c48a77be3f6.png" width="500">

## Installation/ Getting Started
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

- [Android Studio](https://developer.android.com/studio)
- [Java SDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
- [Room Persistence Libary](https://developer.android.com/topic/libraries/architecture/room)

## Team

[George Brown](https://github.com/holtb4000)

[Nicholas Ford](https://github.com/ntford)

[Andrew Lokantsov](https://github.com/Lokantsov)

[Lane Rassmussen](https://github.com/lanerass)

[Peter Sanders](https://github.com/hxtk)

