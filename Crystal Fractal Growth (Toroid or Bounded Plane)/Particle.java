/*
 Created by Sahar Kausar (Fall 2015 CAP3027 - Introduction to Digital Arts & Sciences Course with Java Swing)
 Please do not copy or redistribute without permission.
 
 Assignment: Crystal Fractal Growth (Toroid or Bounded Plane)
 */


// This is the Particle object class utilized by our DisplayImage.java in the crystal method to keep track of our unstuck particles. To run the program, you will need to follow the following compiling and execution instructions (save both DisplayImage.java and Particle.java). 

/**
 How to Compile and Execute:
 
 Download all project files (DisplayImage.java and Particle.java) to your desired root folder on your computer. Open the terminal or executable IDE program to compile and run the file. Enter the following in the quotes (make sure to omit the quotes): "javac (desiredNameOfFile) DisplayImage.java"
 
 For example, you may enter the following: "javac DisplayImage.java"
 
 The program will then compile. Run the program by typing "java (nameOfFile)"
 
 In the above example, you may have entered: "java DisplayImage"
 
 The program will then run and the user may follow the prompt. Enjoy!
 
 **/


public class Particle {
	private int x = 0;
	private int y = 0;
	String checker = "particleUnstuck";

	Particle(int x, int y) {
		this.x = x;
		this.y = y;
	}
	int getX() {
		return x;
	}
	int getY() {
		return y;
	}
	String getChecker() {
		return checker;
	}
	void setChecker(String checker) {
		this.checker = checker;
	}
	void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
