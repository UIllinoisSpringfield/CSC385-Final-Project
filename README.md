
# CSC385 Semester Project

> Item-based recommendation system

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

---

## File Overview

> User.java:

The User class is a data structure encapsulating a user profile. It contains a user ID and a list of movie ratings.

> MovieRecommender.java

MovieRecommender has an initialize function that accepts two input files that contain a list of movies and a table of users and movie ratings, respectively. It generates a list of top five recommended movies for each user based on the similarity of each movie to all other movies the users have rated. The exhaustive list is stored in a string list and can be written to a file by invoking writeToFile(String). This file also contains the main method which accepts user input to generate recommendations from files and save the results to disk.

---

## Screenshots
**Terminal Usage**

![Recordit GIF](http://g.recordit.co/I4b1BRXfoe.gif)

---

## Example

```Java
MovieRecommender rec = new MovieRecommender("movie.dat", "ratings.dat");
rec.writeToFile("output");
```

---

## Team

> Contributers

| <a href="https://markproc.org" target="_blank">**Mark Procarione**</a> |
| :---: |
| [![FVCproductions](https://avatars0.githubusercontent.com/u/40586095?s=256&v=4)](http://fvcproductions.com) |
| <a href="https://github.com/markproc" target="_blank">`github.com/markproc`</a> |
