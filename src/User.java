/**
Movie Recommender - CSC385 Semester Project
Copyright (C) 2018  Mark D. Procarione

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

/**
 * CSC 385, Final Project
 * User.java
 * 
 * @author Mark Procarione
 * @version 1.0 12/09/18
 */

import java.util.*;

public class User {
	private int userID;
	private HashMap<Integer, Double> movieRatings;

	public User(int userID) {
		this.userID = userID;
		movieRatings = new HashMap<Integer, Double>();
	}
	
	public int getUserID() {
		return userID;
	}
	
	public String toString() {
		return movieRatings.toString();
	}
	
	public void addMovieRating(int movieID, double rating) {
		movieRatings.put(movieID, rating);
	}
	
	public double getMovieRating(int movieID) {
		Double rating = movieRatings.get(movieID);
		if(rating == null)
			return 0.0;
		else
			return rating;
	}
	
	public HashMap<Integer, Double> getMovieRatings() {
		return movieRatings;
	}
}
