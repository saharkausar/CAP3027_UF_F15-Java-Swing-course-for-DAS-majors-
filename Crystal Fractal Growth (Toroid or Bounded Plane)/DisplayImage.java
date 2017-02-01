/*
 Created by Sahar Kausar (Fall 2015 CAP3027 - Introduction to Digital Arts & Sciences Course with Java Swing)
 Please do not copy or redistribute without permission.
 
 Assignment: Crystal Fractal Growth (Toroid or Bounded Plane)
 */


/**
 What the program does:
 
This program allows the user to create a crystalized fractal based image based off of crystal growth in either a toroid or bounded plane.
 
 The user can choose File > Crystal (Toroid) or File > Crystal (Bounded Plane). The user can then input the desired image size they would like, desired number of seeds, desired number of particles, and the desired maximum number of steps the particle will take. The program will then produce the desired image result.
 
 This program works in that particles are randomly populated in the image plane and interpolate the image plane looking for other particles or seeds. Seeds allow the particles to grow from them while particles attach to other particles. Thus, a smaller image plane and smaller number of seeds allow for a more beautiful image. More particles as well as more steps the particle will take (the amount of growth each "stem" grows from the seed) will allow for a more beautiful image too!
 
 
 Example Run (For Best Results):
 
 OUTPUT>> Please input desired image size you would like
 USER INPUT >> 300
 
 OUTPUT>> Please input the desired number of seeds
 USER INPUT >> 1
 
 OUTPUT>> Please input the desired number of particles
 USER INPUT >> 10,000
 
 OUTPUT>> Please input the desired maximum number of steps the particles will take
 USER INPUT>> 10,000
 
 OUTPUT>> (Image of a Beautiful Crystal Fractal Image! Enjoy!)
 
 **/

/**
 How to Compile and Execute:
 
 Download all project files (DisplayImage.java and Particle.java) to your desired root folder on your computer. Open the terminal or executable IDE program to compile and run the file. Enter the following in the quotes (make sure to omit the quotes): "javac (desiredNameOfFile) DisplayImage.java"
 
 For example, you may enter the following: "javac DisplayImage.java"
 
 The program will then compile. Run the program by typing "java (nameOfFile)"
 
 In the above example, you may have entered: "java DisplayImage"
 
 The program will then run and the user may follow the prompt. Enjoy!
 
 **/



import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.Random;
import javax.imageio.*;
import javax.swing.*;


public class DisplayImage
{
    private static final int WIDTH  = 401;
    private static final int HEIGHT = 401;
    
    public static void main( String[] args )
    {
        JFrame frame = new ImageFrame( WIDTH, HEIGHT );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setVisible( true );
    }
}

class ImageFrame extends JFrame
{
    String decider = "";
    
    // Sets up the frame's attributes and displays the menu
    
    public ImageFrame(int width, int height)
    {
        this.setTitle("CAP3027 2015 - HW03 - Sahar Kausar");
        this.setSize( width, height );
        
        addMenu();
    }
    
    public void addMenu()
    {
        // File Menu Option Display
        
        JMenu fileMenu = new JMenu ("File");
        
        // Creates a JMenuItem Crystal (toroid) for the JMenu File if the user wishes to perform Diffusion Limited Aggregation for crystals on a toroid plane
        
        JMenuItem crystalToroidal = new JMenuItem("Crystal (toroid)");
        crystalToroidal.addActionListener(new ActionListener()
                                          {
            public void actionPerformed(ActionEvent event)
            {
                decider = "toroid";
                crystal();
            }
        }   );
        
        fileMenu.add(crystalToroidal);
        
        // Creates a JMenuItem Crystal (bounded plane) for the JMenu File if the user wishes to perform Diffusion Limited Aggregation for crystals on a bounded plane
        
        JMenuItem crystalBounded = new JMenuItem("Crystal (bounded plane)");
        crystalBounded.addActionListener(new ActionListener()
                                         {
            public void actionPerformed(ActionEvent event)
            {
                decider = "bounded";
                crystal();
            }
        }   );
        
        fileMenu.add(crystalBounded);
        
        
        // Creates a JMenuItem for the JMenu File if the user wishes to exit the program
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener()
                                   {
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0);
            }
        }   );
        
        fileMenu.add(exitItem);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }
    
    /** The method utilized allowing the user to input the desired image size, number of seeds, number of particles, and maximum number of steps a particle will take, that they wish to conduct or the diffusion limited aggregation for the crystals. This method will be called by the crystalToroidal() and crystalBounded() planes for the user prompt/input. **/
    
    private int[] userInput() {
        int[] vars = {0,0,0,0};
        
        try {
            String imgSize = JOptionPane.showInputDialog("Please input the desired image size you would like");
            vars[0] = Integer.parseInt(imgSize);
            
            String seeds = JOptionPane.showInputDialog("Please input the desired number of seeds");
            vars[1] = Integer.parseInt(seeds);
            
            String particles = JOptionPane.showInputDialog("Please input the desired number of particles");
            vars[2] = Integer.parseInt(particles);
            
            String steps = JOptionPane.showInputDialog("Please input the desired maximum number of steps the particles will take");
            vars[3] = Integer.parseInt(steps);
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error, please enter the correct input for the desired image size");
            String imgSize = JOptionPane.showInputDialog("Please enter the correct input for the desired image size");
            vars[0] = Integer.parseInt(imgSize);
            
            JOptionPane.showMessageDialog(this, "Error, please enter the correct input for the desired number of seeds");
            String seeds = JOptionPane.showInputDialog("Please enter the correct input for the desired number of seeds");
            vars[1] = Integer.parseInt(seeds);
            
            JOptionPane.showMessageDialog(this, "Error, please enter the correct input for the desired number of particles");
            String particles = JOptionPane.showInputDialog("Please enter the correct input for the desired numbeber of particles");
            vars[2] = Integer.parseInt(particles);
            
            JOptionPane.showMessageDialog(this, "Error, please enter the correct input for the maximum steps");
            String steps = JOptionPane.showInputDialog("Please enter the correct input for the maximum steps");
            vars[3] = Integer.parseInt(steps);
        }
        
        return vars;
    }
    
    /** Implements diffused limited aggregation simulation such that at the beginning of each round,
     particles that are adjacent to a non-white pixel get "stuck" and attached to the seed/growing dendrite parts.
     Then each particle that isn't already stuck, gets moved to an adjacent pixel chosen at random. This continues until all the particles are stuck or if the particles have taken the maximum number of steps (whichever comes first).
     **/
    

    private void crystal() {
        
        // Calls out userInput() method where grid[0] is the image size, grid[1] are the seeds, grid[2] are the particles, and grid[3] are the steps inputted by the user.
        
        int[] grid = userInput();
        
        Particle[] partObjs = new Particle[grid[2]];
        
        BufferedImage image = new BufferedImage(grid[0],grid[0],BufferedImage.TYPE_INT_ARGB);
        DisplayBufferedImage(image);
        
        // Sets the background color to white
        
        for(int i = 0; i < grid[0]; i++) {
            for(int j = 0; j < grid[0]; j++) {
                image.setRGB(i,j,0xFFFFFFFF);
            }
        }
        
        // Randomly chooses the seed pixels and sets them to red
        
        Random rand = new Random();
        
        for(int i = 0; i < grid[1]; i++) {
            int a = rand.nextInt(grid[0]);
            int b = rand.nextInt(grid[0]);
            image.setRGB(a,b,0xFFFF0000);
        }
        
        // Randomly chooses starting locations for each of the particles
        
        for(int i = 0; i < grid[2]; i++) {
            //random x pos
            int a = rand.nextInt(grid[0]);
            //random y pos
            int b = rand.nextInt(grid[0]);
            while(image.getRGB(a,b) == 0xFFFF0000) {
                a = rand.nextInt(grid[0]);
                b = rand.nextInt(grid[0]);
            }
            
            // Keeps track of our particle locations
            Particle part = new Particle(a,b);
            partObjs[i] = part;
            
        }
        
        while(grid[3] >= 0) {
            for(int i = 0; i < grid[2]; i++) {
                int x = partObjs[i].getX();
                int y = partObjs[i].getY();
                String move = partObjs[i].getChecker();
                
                if(move == "particleUnstuck") {
                    if(x == 0) {
                        if(y == 0) {
                            
                            //Checks to see if the surrounding Moore Neighborhood is occupied or if there is an adjacent non-white pixel
                            
                            if(image.getRGB(x+1,y+1) == 0xFFFFFFFF && image.getRGB(x+1,y) == 0xFFFFFFFF && image.getRGB(x,y+1) == 0xFFFFFFFF)
                                //If it is occupied or if there is an adjacent non-white pixel, then our particle becomes stuck
                                 //If it is not occupied, then the particle moves to an adjacent pixel at random
                            
                            {
                                int a = rand.nextInt(8);
                                if(a == 0) {
                                    x++; y++;
                                }
                                else if(a == 1) {
                                    x++;
                                }
                                else if(a == 2) {
                                    x++; y--;
                                }
                                else if(a == 3) {
                                    y++;
                                }
                                else if(a == 4) {
                                    x--; y++;
                                }
                                else if(a == 5) {
                                    y--;
                                }
                                else if(a == 6) {
                                    x--;
                                }
                                else if(a == 7) {
                                    x--; y--;
                                }
                                if(decider == "bounded") {
                                    if (x > (grid[0] - 1)) {
                                        x--;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y--;
                                    }
                                    if (x < 0) {
                                        x++;
                                    }
                                    if (y < 0) {
                                        y++;
                                    }
                                }
                                if(decider == "toroid") {
                                    if (x > (grid[0] - 1)) {
                                        x = 0;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y = 0;
                                    }
                                    if (x < 0) {
                                        x = (grid[0] - 1);
                                    }
                                    if (y < 0) {
                                        y = (grid[0] - 1);
                                    }
                                }
                                partObjs[i].setXY(x,y);
                            }
                            else {
                                image.setRGB(x,y,0xFF000000);
                                move = "stuck";
                                partObjs[i].setChecker(move);
                            }
                        }
                        else if(y == grid[0]-1) {
                            if(image.getRGB(x+1,y) == 0xFFFFFFFF && image.getRGB(x+1,y-1) == 0xFFFFFFFF && image.getRGB(x,y-1) == 0xFFFFFFFF) {
                                int a = rand.nextInt(8);
                                if(a == 0) {
                                    x++; y++;
                                }
                                else if(a == 1) {
                                    x++;
                                }
                                else if(a == 2) {
                                    x++; y--;
                                }
                                else if(a == 3) {
                                    y++;
                                }
                                else if(a == 4) {
                                    x--; y++;
                                }
                                else if(a == 5) {
                                    y--;
                                }
                                else if(a == 6) {
                                    x--;
                                }
                                else if(a == 7) {
                                    x--; y--;
                                }
                                if(decider == "bounded") {
                                    if (x > (grid[0] - 1)) {
                                        x--;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y--;
                                    }
                                    if (x < 0) {
                                        x++;
                                    }
                                    if (y < 0) {
                                        y++;
                                    }
                                }
                                if(decider == "toroid") {
                                    if (x > (grid[0] - 1)) {
                                        x = 0;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y = 0;
                                    }
                                    if (x < 0) {
                                        x = (grid[0] - 1);
                                    }
                                    if (y < 0) {
                                        y = (grid[0] - 1);
                                    }
                                }
                                partObjs[i].setXY(x,y);
                            }
                            else {
                                image.setRGB(x,y,0xFF000000);
                                move = "stuck";
                                partObjs[i].setChecker(move);
                            }
                        }
                        else if(y > 0 && y < grid[0]-1) {
                            if(image.getRGB(x+1,y+1) == 0xFFFFFFFF && image.getRGB(x+1,y) == 0xFFFFFFFF && image.getRGB(x+1,y-1) == 0xFFFFFFFF && image.getRGB(x,y+1) == 0xFFFFFFFF && image.getRGB(x,y-1) == 0xFFFFFFFF) {
                                int a = rand.nextInt(8);
                                if(a == 0) {
                                    x++; y++;
                                }
                                else if(a == 1) {
                                    x++;
                                }
                                else if(a == 2) {
                                    x++; y--;
                                }
                                else if(a == 3) {
                                    y++;
                                }
                                else if(a == 4) {
                                    x--; y++;
                                }
                                else if(a == 5) {
                                    y--;
                                }
                                else if(a == 6) {
                                    x--;
                                }
                                else if(a == 7) {
                                    x--; y--;
                                }
                                if(decider == "bounded") {
                                    if (x > (grid[0] - 1)) {
                                        x--;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y--;
                                    }
                                    if (x < 0) {
                                        x++;
                                    }
                                    if (y < 0) {
                                        y++;
                                    }
                                }
                                if(decider == "toroid") {
                                    if (x > (grid[0] - 1)) {
                                        x = 0;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y = 0;
                                    }
                                    if (x < 0) {
                                        x = (grid[0] - 1);
                                    }
                                    if (y < 0) {
                                        y = (grid[0] - 1);
                                    }
                                }
                                partObjs[i].setXY(x,y);
                            }
                            else {
                                image.setRGB(x,y,0xFF000000);
                                move = "stuck";
                                partObjs[i].setChecker(move);
                            }
                        }
                    }
                    
                    
                    if(y == 0) {
                        if(x == grid[0]-1) {
                            if(image.getRGB(x,y+1) == 0xFFFFFFFF && image.getRGB(x-1,y+1) == 0xFFFFFFFF && image.getRGB(x-1,y) == 0xFFFFFFFF) {
                                int a = rand.nextInt(8);
                                if(a == 0) {
                                    x++; y++;
                                }
                                else if(a == 1) {
                                    x++;
                                }
                                else if(a == 2) {
                                    x++; y--;
                                }
                                else if(a == 3) {
                                    y++;
                                }
                                else if(a == 4) {
                                    x--; y++;
                                }
                                else if(a == 5) {
                                    y--;
                                }
                                else if(a == 6) {
                                    x--;
                                }
                                else if(a == 7) {
                                    x--; y--;
                                }
                                if(decider == "bounded") {
                                    if (x > (grid[0] - 1)) {
                                        x--;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y--;
                                    }
                                    if (x < 0) {
                                        x++;
                                    }
                                    if (y < 0) {
                                        y++;
                                    }
                                }
                                if(decider == "toroid") {
                                    if (x > (grid[0] - 1)) {
                                        x = 0;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y = 0;
                                    }
                                    if (x < 0) {
                                        x = (grid[0] - 1);
                                    }
                                    if (y < 0) {
                                        y = (grid[0] - 1);
                                    }
                                }
                                partObjs[i].setXY(x,y);
                            }
                            else {
                                image.setRGB(x,y,0xFF000000);
                                move = "stuck";
                                partObjs[i].setChecker(move);
                            }
                        }
                        else if(x > 0 && x < grid[0]-1) {
                            if(image.getRGB(x+1,y+1) == 0xFFFFFFFF && image.getRGB(x+1,y) == 0xFFFFFFFF && image.getRGB(x,y+1) == 0xFFFFFFFF && image.getRGB(x-1,y+1) == 0xFFFFFFFF && image.getRGB(x-1,y) == 0xFFFFFFFF) {
                                int a = rand.nextInt(8);
                                if(a == 0) {
                                    x++; y++;
                                }
                                else if(a == 1) {
                                    x++;
                                }
                                else if(a == 2) {
                                    x++; y--;
                                }
                                else if(a == 3) {
                                    y++;
                                }
                                else if(a == 4) {
                                    x--; y++;
                                }
                                else if(a == 5) {
                                    y--;
                                }
                                else if(a == 6) {
                                    x--;
                                }
                                else if(a == 7) {
                                    x--; y--;
                                }
                                if(decider == "bounded") {
                                    if (x > (grid[0] - 1)) {
                                        x--;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y--;
                                    }
                                    if (x < 0) {
                                        x++;
                                    }
                                    if (y < 0) {
                                        y++;
                                    }
                                }
                                if(decider == "toroid") {
                                    if (x > (grid[0] - 1)) {
                                        x = 0;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y = 0;
                                    }
                                    if (x < 0) {
                                        x = (grid[0] - 1);
                                    }
                                    if (y < 0) {
                                        y = (grid[0] - 1);
                                    }
                                }
                                partObjs[i].setXY(x,y);
                            }
                            else {
                                image.setRGB(x,y,0xFF000000);
                                move = "stuck";
                                partObjs[i].setChecker(move);
                            }
                        }
                    }
                    
                    
                    if(x == grid[0]-1) {
                        if(y == grid[0]-1) {
                            if(image.getRGB(x,y-1) == 0xFFFFFFFF && image.getRGB(x-1,y) == 0xFFFFFFFF && image.getRGB(x-1,y-1) == 0xFFFFFFFF) {
                                int a = rand.nextInt(8);
                                if(a == 0) {
                                    x++; y++;
                                }
                                else if(a == 1) {
                                    x++;
                                }
                                else if(a == 2) {
                                    x++; y--;
                                }
                                else if(a == 3) {
                                    y++;
                                }
                                else if(a == 4) {
                                    x--; y++;
                                }
                                else if(a == 5) {
                                    y--;
                                }
                                else if(a == 6) {
                                    x--;
                                }
                                else if(a == 7) {
                                    x--; y--;
                                }
                                if(decider == "bounded") {
                                    if (x > (grid[0] - 1)) {
                                        x--;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y--;
                                    }
                                    if (x < 0) {
                                        x++;
                                    }
                                    if (y < 0) {
                                        y++;
                                    }
                                }
                                if(decider == "toroid") {
                                    if (x > (grid[0] - 1)) {
                                        x = 0;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y = 0;
                                    }
                                    if (x < 0) {
                                        x = (grid[0] - 1);
                                    }
                                    if (y < 0) {
                                        y = (grid[0] - 1);
                                    }
                                }
                                partObjs[i].setXY(x,y);
                            }
                            else {
                                image.setRGB(x,y,0xFF000000);
                                move = "stuck";
                                partObjs[i].setChecker(move);
                            }
                        }
                        else if(y > 0 && y < grid[0]-1) {
                            if(image.getRGB(x,y+1) == 0xFFFFFFFF && image.getRGB(x,y-1) == 0xFFFFFFFF && image.getRGB(x-1,y+1) == 0xFFFFFFFF && image.getRGB(x-1,y) == 0xFFFFFFFF && image.getRGB(x-1,y-1) == 0xFFFFFFFF) {
                                int a = rand.nextInt(8);
                                if(a == 0) {
                                    x++; y++;
                                }
                                else if(a == 1) {
                                    x++;
                                }
                                else if(a == 2) {
                                    x++; y--;
                                }
                                else if(a == 3) {
                                    y++;
                                }
                                else if(a == 4) {
                                    x--; y++;
                                }
                                else if(a == 5) {
                                    y--;
                                }
                                else if(a == 6) {
                                    x--;
                                }
                                else if(a == 7) {
                                    x--; y--;
                                }
                                if(decider == "bounded") {
                                    if (x > (grid[0] - 1)) {
                                        x--;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y--;
                                    }
                                    if (x < 0) {
                                        x++;
                                    }
                                    if (y < 0) {
                                        y++;
                                    }
                                }
                                if(decider == "toroid") {
                                    if (x > (grid[0] - 1)) {
                                        x = 0;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y = 0;
                                    }
                                    if (x < 0) {
                                        x = (grid[0] - 1);
                                    }
                                    if (y < 0) {
                                        y = (grid[0] - 1);
                                    }
                                }
                                partObjs[i].setXY(x,y);
                            }
                            else {
                                image.setRGB(x,y,0xFF000000);
                                move = "stuck";
                                partObjs[i].setChecker(move);
                            }
                        }
                    }
                    
                    
                    if(y == grid[0]-1) {
                        if(x > 0 && x < grid[0]-1) {
                            if(image.getRGB(x+1,y) == 0xFFFFFFFF && image.getRGB(x+1,y-1) == 0xFFFFFFFF && image.getRGB(x,y-1) == 0xFFFFFFFF && image.getRGB(x-1,y) == 0xFFFFFFFF && image.getRGB(x-1,y-1) == 0xFFFFFFFF)    {
                                int a = rand.nextInt(8);
                                if(a == 0) {
                                    x++; y++;
                                }
                                else if(a == 1) {
                                    x++;
                                }
                                else if(a == 2) {
                                    x++; y--;
                                }
                                else if(a == 3) {
                                    y++;
                                }
                                else if(a == 4) {
                                    x--; y++;
                                }
                                else if(a == 5) {
                                    y--;
                                }
                                else if(a == 6) {
                                    x--;
                                }
                                else if(a == 7) {
                                    x--; y--;
                                }
                                if(decider == "bounded") {
                                    if (x > (grid[0] - 1)) {
                                        x--;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y--;
                                    }
                                    if (x < 0) {
                                        x++;
                                    }
                                    if (y < 0) {
                                        y++;
                                    }
                                }
                                if(decider == "toroid") {
                                    if (x > (grid[0] - 1)) {
                                        x = 0;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y = 0;
                                    }
                                    if (x < 0) {
                                        x = (grid[0] - 1);
                                    }
                                    if (y < 0) {
                                        y = (grid[0] - 1);
                                    }
                                }
                                partObjs[i].setXY(x,y);
                            }
                            else {
                                image.setRGB(x,y,0xFF000000);
                                move = "stuck";
                                partObjs[i].setChecker(move);
                            }
                        }
                    }
                    
                    
                    if(x > 0 && x < grid[0]-1) {
                        if(y > 0 && y < grid[0]-1) {
                            if(image.getRGB(x+1,y+1) == 0xFFFFFFFF && image.getRGB(x+1,y) == 0xFFFFFFFF && image.getRGB(x+1,y-1) == 0xFFFFFFFF && image.getRGB(x,y+1) == 0xFFFFFFFF && image.getRGB(x,y-1) == 0xFFFFFFFF && image.getRGB(x-1,y+1) == 0xFFFFFFFF && image.getRGB(x-1,y) == 0xFFFFFFFF && image.getRGB(x-1,y-1) == 0xFFFFFFFF) {
                                int a = rand.nextInt(8);
                                if(a == 0) {
                                    x++; y++;
                                }
                                else if(a == 1) {
                                    x++;
                                }
                                else if(a == 2) {
                                    x++; y--;
                                }
                                else if(a == 3) {
                                    y++;
                                }
                                else if(a == 4) {
                                    x--; y++;
                                }
                                else if(a == 5) {
                                    y--;
                                }
                                else if(a == 6) {
                                    x--;
                                }
                                else if(a == 7) {
                                    x--; y--;
                                }
                                if(decider == "bounded") {
                                    if (x > (grid[0] - 1)) {
                                        x--;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y--;
                                    }
                                    if (x < 0) {
                                        x++;
                                    }
                                    if (y < 0) {
                                        y++;
                                    }
                                }
                                if(decider == "toroid") {
                                    if (x > (grid[0] - 1)) {
                                        x = 0;
                                    }
                                    if (y > (grid[0] - 1)) {
                                        y = 0;
                                    }
                                    if (x < 0) {
                                        x = (grid[0] - 1);
                                    }
                                    if (y < 0) {
                                        y = (grid[0] - 1);
                                    }
                                }
                                partObjs[i].setXY(x,y);
                            }
                            else {
                                image.setRGB(x,y,0xFF000000);
                                move = "stuck";
                                partObjs[i].setChecker(move);
                            }
                        }
                    }
                }
            }grid[3]--; // Decrements the steps
        }
        
    }
    
    public void DisplayBufferedImage(BufferedImage image) {
        this.setContentPane(new JScrollPane(new JLabel(new ImageIcon(image))));
        this.validate();
    }
}
