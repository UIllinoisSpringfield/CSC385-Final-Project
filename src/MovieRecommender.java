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
 * MovieRecommender.java
 * 
 * @author Mark Procarione
 * @version 1.0 12/09/18
 */

import java.lang.String;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.io.*;

public class MovieRecommender {
	
	private HashMap<Integer, String> movies;
	private HashMap<Integer, User> ratings;
	
	private double table[][];
	private double similarities[][];
	
	List<String> lines = new ArrayList<String>();
	
	public MovieRecommender() {
		
	}
	
	public MovieRecommender(String moviePath, String ratingsPath) {
		initialize(moviePath, ratingsPath);
	}
	
	public void initialize(String moviePath, String ratingsPath) {
		loadMovies(moviePath);
		loadRatings(ratingsPath);
		similarities();
		predictor();
	}

	private void loadMovies(String filePath) {
		movies = new HashMap<Integer, String>();
		
		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.ISO_8859_1)) {
			stream.forEach(s -> {
				String[] split = s.split("\\|");
				Integer i = Integer.parseInt(split[0]);
				String str = split[1];
				movies.put(i, str);
			});
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadRatings(String filePath) {
		ratings = new HashMap<Integer, User>();
		
		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.ISO_8859_1)) {
			stream.forEach(s -> {
				String[] split = s.split("\\t");
				Integer user = Integer.parseInt(split[0]);
				Integer movie = Integer.parseInt(split[1]);
				Double rating = Double.parseDouble(split[2]);
				
				if(!ratings.containsKey(user)) {
					User userObj = new User(user);
					ratings.put(user, userObj);
				}
				
				ratings.get(user).addMovieRating(movie, rating);
			});
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void similarities() {
		// User-movie rating matrix
		table = new double[movies.size()][ratings.size()];
		movies.forEach((i, v) -> {
			int j = 0;
			for(Map.Entry<Integer, User> entry : ratings.entrySet()) {
				table[i-1][j] = entry.getValue().getMovieRating(i);
				j++;
			}
		});
		
		// Calculate similarities
		similarities = new double[movies.size()][movies.size()];
		for(int i = 0; i < movies.size(); i++) {
			for(int j = i; j < movies.size(); j++) {
				// Rating vectors
				double v1[] = new double[ratings.size()];
				double v2[] = new double[ratings.size()];
				for(int x = 0; x < ratings.size(); x++) {
					v1[x] = table[i][x];
					v2[x] = table[j][x];
				}
				// Cosine similarity
				double num1 = 0;
				double num2 = 0;
				double num3 = 0;
				for(int x = 0; x < ratings.size(); x++) {
					num1 += v1[x] * v2[x];
					num2 += v1[x] * v1[x];
					num3 += v2[x] * v2[x];
				}

				//DecimalFormat df = new DecimalFormat("#.##");
				//df.setRoundingMode(RoundingMode.DOWN);
				//double p = Double.parseDouble(df.format((num1 / (Math.sqrt(num2) * Math.sqrt(num3)))));
				
				similarities[i][j] = (num1 / (Math.sqrt(num2) * Math.sqrt(num3)));
				similarities[j][i] = similarities[i][j];
			}
		}
	}
	
	private void predictor() {
		// For each user...
		int k = 0; // k = user count
		for(Map.Entry<Integer, User> u : ratings.entrySet()) {
			
			String userStr = "User ID: " + u.getKey() + " Top 5 recommendations: ";
			
			// List all movies the user has rated
			int rated[] = new int[u.getValue().getMovieRatings().size()];
			int r = 0;
			for(Map.Entry<Integer, Double> entry : u.getValue().getMovieRatings().entrySet()) {
				rated[r] = entry.getKey();
				r++;
			}
			
			// Storage for tope five recommendations
			int topFiveID[] = new int[5];
			double topFiveRat[] = new double[5];
			
			// TODO: Do I need to change i to the actual movie ID? It would be safer.
			
			// For each movie in our table...
			for(int i = 0; i < table.length; i++) { 
				
				// If the movie hasn't been rated...
				if(table[i][k] == 0) {							
					double sum = 0;
					double count = 0;
					// For each movie the user has rated...
					for(int e = 0; e < rated.length; e++) {
						double s = similarities[i][rated[e]-1]; // similarity value for unrated movie <-> next rated movie in list
						
						sum += s * u.getValue().getMovieRatings().get(rated[e]);
						count += s;
					}
					double rating = sum/count;
					
					// Store only top five recommendations
					int a;
					for(a = 0; a < topFiveRat.length-1; a++) {
						  if(topFiveRat[a] < rating)
					    	break;
						  }
					for(int b = topFiveRat.length-2; b >= a; b--) {
						topFiveID[b+1] = topFiveID[b];
						topFiveRat[b+1] = topFiveRat[b];
					}
					topFiveID[a] = i + 1;
					topFiveRat[a] = rating;
				}
			}
			
			// Convert top five to string
			for(int c = 0; c < 5; c++) {
				String movieTitle = movies.get(topFiveID[c]);
				if(movieTitle != null)
					//userStr = userStr.concat(movieTitle + "::" + topFiveRat[c] + "| ");
					userStr = userStr.concat(movieTitle + "::" + topFiveRat[c]);
			}
			// Add to write list
			lines.add(userStr);
			
			k++; // Next movie
		}
	}
	
	public HashMap<Integer, String> getMovies() {
		return movies;
	}
	
	public HashMap<Integer, User> getUsers() {
		return ratings;
	}
	
	public List<String> getTopFiveList() {
		return lines;
	}
	
	public void writeToFile(String file) {
		// Write to file
		Path filepath = Paths.get(file);
		try {
			Files.write(filepath, lines, Charset.forName("UTF-8"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MovieRecommender rec = new MovieRecommender();
		Scanner sc = new Scanner(System.in);
		boolean running = true;
		String command = "";
		
		System.out.println("---Commands---\nopen moviesFilepath ratingsFilepath\nsave outputFilePath\nquit\n");
		
		while(running) {
			// Get input
			String str = sc.nextLine();
			String[] input = str.split(" ");
			
			// Get command if entered
			if(input.length > 0)
				command = input[0];
			
			// Command functions
			if(command.equals("open") && input.length == 3) {
				if(command.equals("open")) {
					rec.initialize(input[1], input[2]);
					System.out.println("Recommendations complete!");
				}
			}
			else if(command.equals("save")) {
				if(input.length == 2)
					rec.writeToFile(input[1]);
				else
					rec.writeToFile("output");
				System.out.println("Output saved!");
			}
			else if(command.equals("quit"))
				running = false;
			else
				System.out.println("Invalid input. Please try again.");
		}
		
		sc.close();
	}
	
}
