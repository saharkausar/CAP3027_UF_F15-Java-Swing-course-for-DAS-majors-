// CAP 3027 Term Project by Sahar K Hussain - Fall 2015
// ArtBytes
// Not your standard Java Swing Paint program.
// ArtBytes is a custom GUI paint program that allows the user to paint with really cool effects instead of the regular MS paint programs out there
// and has other cool features as well

// This class is called by the ArtBytes main class


// ============================== Imports the Packages ================================== //
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseAdapter;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.geom.*;
import java.lang.*;
import java.awt.Polygon;

// ====================================================================================== //

public class LetsDrawCanvas extends JComponent {
    
    public BufferedImage image;
    public Graphics2D g2;
    
    private int x_New, y_New, x_Dead, y_Dead;
    char drawDecider;
    private Random randNumGen = new Random();
    char brushState = 'd';//'d' = default
    JSlider strokeWidthSlider;
    
    public LetsDrawCanvas(JSlider slider) {
        this.strokeWidthSlider = slider;
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            //MousePressed Event E
            public void mousePressed(MouseEvent e) {
                x_Dead = e.getX();
                y_Dead = e.getY();
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            
            //MouseDragged Event E
            public void mouseDragged(MouseEvent e) {
                x_New = e.getX();
                y_New = e.getY();
                
                if (g2 != null && brushState == 'd') {
                    g2.drawLine(x_Dead, y_Dead, x_New, y_New);
                    repaint();
                    x_Dead = x_New;
                    y_Dead = y_New;
                }
                else if (brushState == 'r') {
                    drawRainbow(x_New, y_New);
                }
                else if (brushState == 'p') {
                    prettyCircles(x_New, y_New);
                }
                else if (brushState == 'b') {
                    drawBubbles(x_New, y_New);
                }
                else if(brushState == 's') {
                    star(x_New, y_New);
                }
            }
        });
    }
    
    // ============================== Colors for the Buttons ================================== //
    
    public void repaintCanvas() {
        repaint();
    }
    
    public void drawRainbow(int x, int y){
        //System.out.println("drawing rainbow");
        g2.setPaint(Color.RED);
        g2.fillArc(x,y,strokeWidthSlider.getValue()+150,strokeWidthSlider.getValue()+150,0,180);
        
        g2.setPaint(Color.ORANGE);
        g2.fillArc(x+10,y+10,strokeWidthSlider.getValue()+130,strokeWidthSlider.getValue()+130,0,180);
        
        g2.setPaint(Color.YELLOW);
        g2.fillArc(x+15,y+15,strokeWidthSlider.getValue()+120,strokeWidthSlider.getValue()+120,0,180);
        
        g2.setPaint(Color.GREEN);
        g2.fillArc(x+20,y+20,strokeWidthSlider.getValue()+110,strokeWidthSlider.getValue()+110,0,180);
        
        g2.setPaint(Color.BLUE);
        g2.fillArc(x+25,y+25,strokeWidthSlider.getValue()+100,strokeWidthSlider.getValue()+100,0,180);
        
        g2.setPaint(Color.MAGENTA);
        g2.fillArc(x+30,y+30,strokeWidthSlider.getValue()+90,strokeWidthSlider.getValue()+90,0,180);
        
        g2.setPaint(Color.PINK);
        g2.fillArc(x+35,y+35,strokeWidthSlider.getValue()+80,strokeWidthSlider.getValue()+80,0,180);
        
        g2.setPaint(Color.BLACK);
        g2.fillArc(x+40,y+40,strokeWidthSlider.getValue()+70,strokeWidthSlider.getValue()+70,0,180);
        
        repaint();
    }
    
    public void prettyCircles(int x, int y){
        //System.out.println("Drawing circles");
        //int width = getWidth();
        //int widthDist = Math.abs(width/2 - x);
        g2.setPaint(createRandomColor());
        g2.fill(new Ellipse2D.Double(x,y,strokeWidthSlider.getValue(),strokeWidthSlider.getValue()));
        g2.draw(new Ellipse2D.Double(x+10,y+10,strokeWidthSlider.getValue(),strokeWidthSlider.getValue()));
        
        repaint();
    }
    
    public void drawBubbles(int x, int y){
        g2.setPaint(createRandomColor());
        g2.draw(new Ellipse2D.Double(x,y,strokeWidthSlider.getValue(),strokeWidthSlider.getValue()));
        
        g2.setPaint(createRandomColor());
        g2.draw(new Ellipse2D.Double(x+20,y+20,strokeWidthSlider.getValue(),strokeWidthSlider.getValue()));
        
        g2.setPaint(createRandomColor());
        g2.draw(new Ellipse2D.Double(x,y+15,strokeWidthSlider.getValue(),strokeWidthSlider.getValue()));
        
        g2.setPaint(createRandomColor());
        g2.draw(new Ellipse2D.Double(x+20,y+30,strokeWidthSlider.getValue(),strokeWidthSlider.getValue()));
        
        g2.setPaint(createRandomColor());
        g2.draw(new Ellipse2D.Double(x+30,y+30,strokeWidthSlider.getValue(),strokeWidthSlider.getValue()));
        
        g2.setPaint(createRandomColor());
        g2.draw(new Ellipse2D.Double(x,y+20,strokeWidthSlider.getValue(),strokeWidthSlider.getValue()));
        
        g2.setPaint(createRandomColor());
        g2.draw(new Ellipse2D.Double(x+20,y+40,strokeWidthSlider.getValue(),strokeWidthSlider.getValue()));
        repaint();
        
    }
    
    public void star(int x, int y) {
        
        g2.setPaint(createRandomColor());
        
        double points[][] = {
            { x+0+strokeWidthSlider.getValue(), y+85+strokeWidthSlider.getValue() }, { x+75+strokeWidthSlider.getValue(), y+75+strokeWidthSlider.getValue() }, { x+100+strokeWidthSlider.getValue(), y+10+strokeWidthSlider.getValue() }, { x+125+strokeWidthSlider.getValue(), y+75+strokeWidthSlider.getValue() },
            { x+200+strokeWidthSlider.getValue(), y+85+strokeWidthSlider.getValue() }, { x+150+strokeWidthSlider.getValue(), y+125+strokeWidthSlider.getValue() }, { x+160+strokeWidthSlider.getValue(), y+190+strokeWidthSlider.getValue() }, { x+100+strokeWidthSlider.getValue(), y+150+strokeWidthSlider.getValue() },
            { x+40+strokeWidthSlider.getValue(), y+190+strokeWidthSlider.getValue() }, { x+50+strokeWidthSlider.getValue(), y+125+strokeWidthSlider.getValue() }, { x+0+strokeWidthSlider.getValue(), y+85+strokeWidthSlider.getValue() }
        };
        
        GeneralPath star = new GeneralPath();
        star.moveTo(points[0][0], points[0][1]);
        for (int k = 1; k < points.length; k++)
            star.lineTo(points[k][0], points[k][1]);
        star.closePath();
        g2.fill(star);
    
        repaint();
    }
    
    public void returnStroke(){
        strokeWidthSlider.getValue();
    }
    
    public void drawRandomColors() {
        g2.setPaint(createRandomColor());
    }
    
    public void drawBlue() {
        g2.setPaint(Color.BLUE);
    }
    
    public void drawCyan() {
        g2.setPaint(Color.CYAN);
    }
    
    public void drawGreen() {
        g2.setPaint(Color.GREEN);
    }
    
    public void drawLight_gray() {
        g2.setPaint(Color.LIGHT_GRAY);
    }
    
    public void drawMagenta() {
        g2.setPaint(Color.MAGENTA);
    }
    
    public void drawOrange() {
        g2.setPaint(Color.ORANGE);
    }
    
    public void drawPink() {
        g2.setPaint(Color.PINK);
    }
    
    public void drawRed () {
        g2.setPaint(Color.RED);
    }
    
    public void drawWhite () {
        g2.setPaint(Color.WHITE);
    }
    
    public void drawYellow() {
        g2.setPaint(Color.YELLOW);
    }
    
    public void drawRandom() {
        g2.setPaint(createRandomColor());
    }
    
    public void eraser() {
        g2.setPaint(Color.BLACK);
    }
    
    public Color createRandomColor(){
        return new Color(randNumGen.nextFloat(),randNumGen.nextFloat(),randNumGen.nextFloat(), randNumGen.nextFloat());
    }
    
    // ====================================================================================== //
    
    public void emptyTheFrame(){ // Sets the image panel to black at default and continues to repaint the image black
        g2.setPaint(Color.BLACK);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        repaint();
    }
    
    protected void paintComponent(Graphics g)
        {
        if (image == null) {
            image = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
            //image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Draws smooth lines
            g2.setStroke(new BasicStroke(strokeWidthSlider.getValue()));
            emptyTheFrame(); // Destroys the image
        }
        
        g.drawImage(image, 0, 0, null);
        }
    
    public BufferedImage getImage(){
        return image;
    }
}