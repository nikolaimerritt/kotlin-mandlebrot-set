
 1 // Fig. 21.2: LogoAnimatorJPanel.java
 2 // Animation of a series of images.
 3 import java.awt.Dimension;
 4 import java.awt.event.ActionEvent;
 5 import java.awt.event.ActionListener;
 6 import java.awt.Graphics;
 7 import javax.swing.ImageIcon;
 8 import javax.swing.JPanel;
 9 import javax.swing.Timer;
10
11 public class LogoAnimatorJPanel extends JPanel
12 {
13 private final static String IMAGE_NAME = "deitel"; // base image name
14 protected ImageIcon images[]; // array of images
15 private final int TOTAL_IMAGES = 30; // number of images
16 private int currentImage = 0; // current image index
17 private final int ANIMATION_DELAY = 50; // millisecond delay
18 private int width; // image width
19 private int height; // image height
20
21 private Timer animationTimer; // Timer drives animation
22
23 // constructor initializes LogoAnimatorJPanel by loading images
24 public LogoAnimatorJPanel()
25 {
26 images = new ImageIcon[ TOTAL_IMAGES ];
27
28 // load 30 images
29 for ( int count = 0; count < images.length; count++ )
30 images[ count ] = new ImageIcon( getClass().getResource(
31 "https://underpop-online-fr.cdn.ampproject.org/i/underpop.online.fr/j/java/img/" + IMAGE_NAME + count + ".gif" ) ); 
32
33 // this example assumes all images have the same width and height
34 width = images[ 0 ].getIconWidth(); // get icon width
35 height = images[ 0 ].getIconHeight(); // get icon height
36 } // end LogoAnimatorJPanel constructor
37
38 // display current image
39 public void paintComponent( Graphics g )
40 {
41 super.paintComponent( g ); // call superclass paintComponent
42
43 images[ currentImage ].paintIcon( this, g, 0, 0 );
44
45 // set next image to be drawn only if timer is running
46 if ( animationTimer.isRunning() )
47 currentImage = ( currentImage + 1 ) % TOTAL_IMAGES;
48 } // end method paintComponent
49
50 // start animation, or restart if window is redisplayed
51 public void startAnimation()
52 {
53 if ( animationTimer == null )
54 {
55 currentImage = 0; // display first image
56
57 // create timer 
58 animationTimer = 
59 new Timer( ANIMATION_DELAY, new TimerHandler() );
60
61 animationTimer.start(); // start timer
62 } // end if
63 else // animationTimer already exists, restart animation
64 {
65 if ( ! animationTimer.isRunning() )
66 animationTimer.restart();
67 } // end else
68 } // end method startAnimation
69
70 // stop animation timer
71 public void stopAnimation()
72 {
73 animationTimer.stop();
74 } // end method stopAnimation
75
76 // return minimum size of animation
77 public Dimension getMinimumSize() 78 { 
79 return getPreferredSize(); 
80 } // end method getMinimumSize 
81
82 // return preferred size of animation 
83 public Dimension getPreferredSize() 
84 { 
85 return new Dimension( width, height );
86 } // end method getPreferredSize 
87
88 // inner class to handle action events from Timer
89 private class TimerHandler implements ActionListener
90 {
91 // respond to Timer's event
92 public void actionPerformed( ActionEvent actionEvent )
93 {
94 repaint(); // repaint animator
95 } // end method actionPerformed
96 } // end class TimerHandler
97 } // end class LogoAnimatorJPanel