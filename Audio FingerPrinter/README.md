COMP 128 Audio Fingerprinter!
==========================

For this homework assignment, you will implement an audio recognition program that is similar in style to the Shazam app or what happens when you ask Siri/Alexa to identify a song.
The application works by first analyzing a directory of songs and creating digital fingerprints (e.g. a hash code) for each one. These fingerprints are
stored in a map to allow fast lookups. Once the map is created, your application will allow you to either load a song clip or listen
with your computer's microphone. The recorded sounds will be fingerprinted and compared to existing songs in the map to identify the
song that was played.

Your finished program will look like:
![Screenshot of the MainApp application](./screenshot.png)

There are several learning goals for this assignment:
- Gain experience reading, understanding, and implementing algorithms 
- Practice using map data structures and understanding an interesting use of hashing
- Work on the skill of reading/understanding existing code and how to modify it.
- Continue building your programming skills


### Getting Started and Setup

To get started, read and study the following description of the algorithm you will implement: [Shazam It! Music Recognition Algorithms, Fingerprinting, and Processing](http://www.toptal.com/algorithms/shazam-it-music-processing-fingerprinting-and-recognition). Pay particular attention to the sections labled "Fingerprinting a song" and "Song identification".

Then, fork and clone this repository. The repository has included code to display a user interface and get you started. The user interface code uses a library called JavaFX. Because of this, to run the MainApp application, you will need to download the VS Code Gradle extension.

1. Click the extensions icon in the left menu in VS Code (the one that looks like four cubes).
2. In the search box, search for "Gradle for Java" and install the extension.
3. To run the main app, click on the elephant icon in the left menu. Expand the Tasks > Application sections and click the play button next to "run".

![Image showing steps to run MainApp through gradle](./gradle.png)

### Task 0: Getting to know the existing code

Read through the existing code and make sure you understand how the different classes and methods work together. In particular,
there are two classes you will work closely with:

`SongDatabase` represents the database of songs that you will match against. It contains methods for reading in the audio data and converting it to the frequency domain using the fast fourier transform. Inside the res/music folder, we have included some songs you can use to load into the database. These songs are licensed under the Creative Commons Music license by Jason Shaw on [Audionautix.com](https://audionautix.com/)

`AudioFingerprinter` is an interface that you will need to implement in your own class. It specifies the method decomposition needed to fingerprint and recognize a song.

Before you continue, you should answer the following questions. We won't collect your answers for this assignment, but if you have questions on whether you understand the algorithm correctly then you should come to office hours to discuss it with your preceptors or professor.

1. In your own words, describe a general overview of the steps that need to be accomplished in order to recognize a song.
2. What is the purpose of the `DataPoint` class and its relationship to the `matcherDB` instance variable in the `SongDatabase` class? 
3. Why does the `matcherDB` object contain a List of DataPoints as the value rather than a single DataPoint?
4. In the matching algorithm, why is it not enough to just count datapoints that have the same hash as a match? Describe an algorithm that uses the relative timing of the datapoints to increase your match certainty.
5. The `convertToFrequencyDomain` method returns a 2D array of doubles (see section 7.6 for examples of how to use 2D arrays). How is the data in this array organized?

### Task 1: 

Create a new Java class that implements the `AudioFingerprinter` interface. Your class will likely need to take a `SongDatabase` object 
as a parameter to the constructor to initialize an instance variable. Look carefully at the javadoc comments in the interface 
to make sure you are implementing each method correctly.

You may make use of the code provided in the [Shazam It!](http://www.toptal.com/algorithms/shazam-it-music-processing-fingerprinting-and-recognition)
article to determine the key points and hash function, but keep in mind that this code is not complete, has some bugs, and will need to be modified.

You can run the tests to determine whether the keypoints and hash methods are working correctly. You will need to initialize the audioFingerPrinter variable at the in the TODO at the top of the file.

### Task 2:

Complete the `processFile` method in the `SongDatabase` class. This method is used to analyze each song to create a database of fingerprints.

### Task 3:

Test your code using the `SimpleFingerprinter` class. You will need to initialize the `AudioFingerprinter rec` variable to an instance of your implementing class.

The CarolOfTheBells mp3 that is included in the music directory should have somewhere between 980-1000 matches. Make sure the results are returned in sorted order as specified in the interface definition.

Once `SimpleFingerprinter` is working, complete the TODO comment in the MainApp class. You should now be able to run the MainApp using the process described above under the Getting Started section.


### Submission:

Make sure to commit and push, then check on github that everything is uploaded correctly.

As always, please make sure to follow the guidelines for [good java style](https://docs.google.com/document/d/1GT207Pia0q7bETKrqSi--C3X7N_67XgRXYEvQFrwHdI/edit?usp=sharing) that we have shared with you in the past.




