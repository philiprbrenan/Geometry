//------------------------------------------------------------------------------
// 𝕯raw draggable, reflectable, rotatable, translatable animated geometric shapes
// in two dimensions on Android via Canvas.drawVertices()
// 𝗣𝗵𝗶𝗹𝗶𝗽 𝗥 𝗕𝗿𝗲𝗻𝗮𝗻  at gmail dot com, Appa Apps Ltd Inc 2015/08/14 17:12:39
// 𝗜, the author of this work, hereby place this work in the public domain.
// 𝗻𝗯: Rotations are measured in degrees, clockwise, from the x axis.
//------------------------------------------------------------------------------
// 𝝰 𝝱 𝝲 𝝳 𝝷 𝝺 𝝻 𝝫 𝞅 𝝮 𝞈 say                                                    // Index
// Rotated radius makes a circle - diameter rotated draws the same circle - need the radius version as well as first step up
// Opposite angles are equal via two way reflection of a radius
// A diameter is the longest line in a circle
// Rotating a right angle triangle makes a rectangle, hence the formula for the area of a triangle
// A diamond is made  by reflecting an isosceles triangle once or a right angle triangle twice
// Rotate a mirror placed on the corner of a rectangle show that the reflection does not merge with the original, conversely a square does as do both if the mirror is through the centre. Construct a square by rotating a reflected rectangle onto itself.
// Rotate parallel lines crossed with a diagonal onto themselves. Likewise a single line
// Flash selection along a sine wave so that the selection flashing is less abrupt
// Resolve lack of name means no selection possible and everything flashing - everything that is selectable should flash slowly when nothing has been pressed for 10 or so seconds
// The quadrants of a rectangle have equal areas
// A kite, diamond, square has half the area of the enclosing square/rectangle
// Move items around line 170 into Drawing
// Remove x = cx etc. as they are now the defaults
// Two reflection (via two mirrors) equals a rotation of twice the angle between the mirrors angle
// Given a triangle(A,B,C), choose any point P, then draw a circle or a in-circle  through the centre of the in-circles of PAB, PBC, PAC: does this circle have the same centre as the centre of the circle through ABC?
// Given a triangle(A,B,C), draw a circle with centre 𝗖 through its vertices, then draw another circle 𝗗 with the same centre as 𝗖 and draw lines from each vertex through 𝗖 to 𝗗 to show that a similar triangle is formed where these lines cross 𝗗
// Are the in-circles of the quadrants of a rectangle the same size?  Do they reflect in the diagonals?
// Coordinated zoom - overlayable lines
// Trilinear coordinates of a point moved around a triangle by the user per haps not expressed as numbers but as a bar chart - ditto barycentric
// Tangential polygons: any triangle, any regular polygon, but also diamonds and  not rectangles
// https://en.wikipedia.org/wiki/Incircle_and_excircles_of_a_triangle#Gergonne_triangle_and_point
// https://en.wikipedia.org/wiki/Nagel_point
// https://en.wikipedia.org/wiki/Medial_triangle
// https://en.wikipedia.org/wiki/Morley's_trisector_theorem
// The colors that the user is not touching could become more translucent so that the touched overlay line can be seen more clearly
// CurrentGoal is unstaisfactory because setGoal() is action rather than description
// The altitude of a right angle triangle divides the triangle into two similar triangles
// Triangle of altitudes from altitudes and centroids from centroids
package com.appaapps.generic;

import android.graphics.*;
import android.view.*;
import java.util.*;
import com.appaapps.Colours;                                                    // All the colours from Wikipedia

public class Activity extends android.app.Activity                              // Create an activity around a drawing
 {final int debug = 1, debugSlow = 1, debugTiming = 2, debugMesh = 4;           // Debug capabilities
  public void onCreate(android.os.Bundle save)                                  // Create the app
   {super.onCreate(save);                                                       // Call through
    setContentView(new                                                          // Display a drawing
//  TestDrawing
//  DrawRectangle
//  DrawTriangle
//  DrawCircle
//  DrawTab
//  DrawPolyArea
//  SnowFlake
//  TwoSemiCirclesMakeACircle
//  LinesRotateTwice
//  RotatedRadiusMakesACircle
//  ReflectedRadiusMakesACircle
//  OppositeAnglesAreEqual
//  TangentToDiameter
//  TangentToDiameterMirrored
//  TwoEquilateralTrianglesReflectedInTwoMirrorsMakeAHexagon
//  Pentagon
//  ReflectedDiameter
//  IsoscelesTriangle
//  HalfAngle
//  ArrowHead
//  Tick
//  ParallelLinesPreserveAngles
//  InteriorAnglesOfATriangleMakeALine
//  DraggableTriangle
//  Sierpinski
//  Pythagoras
//  SquaringARectangle
//  ComparingLengths
//  SchwartzInequality
//  CongruentIsocelesTriangles
//  CongruentScaleneTriangles
    EulerLine
//  InCircleCentres
//  QuarterTriangles
     (this));
   }
//------------------------------------------------------------------------------
// 𝕮reate drawing surface and draw vertices upon it
//------------------------------------------------------------------------------
  abstract class DisplayDrawing extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener
   {final int 𝝺r = Colours.Red, 𝝺b = Colours.Blue, 𝝺g = Colours.Green,          // Useful colours
      𝝺c   = Colours.Cyan,            𝝺m   = Colours.Magenta,
      𝝺o   = Colours.BurntOrange,     𝝺y   = Colours.Yellow,
      𝝺fbe = Colours.FrenchBeige,     𝝺fbi = Colours.FrenchBistre,              // French colour scheme
      𝝺fbl = Colours.FrenchBlue,      𝝺ff  = Colours.FrenchFuchsia,
      𝝺fl  = Colours.FrenchLilac,     𝝺fL  = Colours.FrenchLime,
      𝝺fm  = Colours.FrenchMauve,     𝝺fpi = Colours.FrenchPink,
      𝝺fpl = Colours.FrenchPlum,      𝝺fpu = Colours.FrenchPuce,
      𝝺fr  = Colours.FrenchRaspberry, 𝝺fR  = Colours.FrenchRose,
      𝝺fs  = Colours.FrenchSkyBlue,   𝝺fv  = Colours.FrenchViolet,
      𝝺fw  = Colours.FrenchWine,
      𝝺sb  = Colours.SpanishBlue,      𝝺so = Colours.SpanishOrange,             // Spanish colour scheme
      𝝺sB  = Colours.SpanishBistre,    𝝺sp = Colours.SpanishPink,
      𝝺sc  = Colours.SpanishCrimson,   𝝺sr = Colours.SpanishRed,
      𝝺sC  = Colours.SpanishCarmine,   𝝺ss = Colours.SpanishSkyBlue,
      𝝺sg  = Colours.SpanishGreen,     𝝺sv = Colours.SpanishViridian,
      𝝺sG  = Colours.SpanishGray,      𝝺sV = Colours.SpanishViolet,
      𝝺w   = Colours.White,            𝝺ws = Colours.WhiteSmoke;
    final float
      flipTime = 10f, bumpSpeed = 40f,                                          // Time in one state before we flip to the next state, speed bump to jump to new state
      closeEnoughAngle = 5,                                                     // Angle close enough in degrees
      sdCloseEnoughAngle = sd(closeEnoughAngle);                                // Sine of angle close
    final int mirrorsNone=0, mirror1=1, mirror2=2, mirrorsBoth=3;               // Mirror selection
    final 𝝮
      𝞈1 = new 𝝮("Tracker1"), 𝞈2 = new 𝝮("Tracker2"),                           // Drag trackers
      𝞈3 = new 𝝮("Tracker3"), 𝞈4 = new 𝝮("Tracker4"),
      𝞈Rotation    = new 𝝮("TracingRotation"),                                  // Tracing rotation tracker
      𝞈Translation = new 𝝮("TracingTranslation");                               // Tracing translation tracker
    final Matrix tracingMatrix = new Matrix();                                  // Tracing matrix
    final long startTime = t();                                                 // Start time of animation
    final int period = 10000;                                                   // Default rotation period in ms
    final int loopTime = (debug & debugSlow) > 0 ? 400 : 1;                     // Minimum time between draws
    final Paint paint = new Paint();                                            // Paint for vertices
    final float flashCycle = 1;                                                 // Flash of selected item cycle time
    int touchedColour;                                                          // The colour the user is touching if the ColourAtTouch capability is in use
    double pressedTime, releasedTime;                                           // Time of last press, release
    Canvas canvas;                                                              // Canvas the drawing will be drawn on
    Drawing drawing;                                                            // The current drawing being displayed
    Drawing.SelectedItem selectedItem;                                          // The selected item on the current drawing
    SurfaceHolder vsh = null;                                                   // Surface
    int pointerId;                                                              // Pointer id of primary pointer
    boolean draw = false, stop = false;                                         // Draw status
    boolean pressed = false;                                                    // User is touching the screen
    float w, h, cx, cy;                                                         // Width, height, centre coordinates

    DisplayDrawing(final Activity Activity)                                     // Create display
     {super(Activity);
      thread.start();                                                           // Start drawing thread
      thread.setPriority(Thread.MAX_PRIORITY);                                  // Draw as quickly as possible
      vsh = getHolder();                                                        // Address Surface holder which allows us to address the surface associated with this view
      vsh.addCallback(this);                                                    // Notification when surface is ready
      setOnTouchListener(this);                                                 // Listen for touch events
      paint.setAntiAlias(true);                                                 // Anti-alias paint
     }
    final Thread thread = new Thread()                                          // Drawing thread
     {public void run()
       {for(;!stop;)                                                            // Draw while a surface is available
         {if (draw) draw(); else android.os.SystemClock.sleep(loopTime);        // Draw as fast as possible
         }
       }
     };

    public void surfaceChanged  (SurfaceHolder holder, int format, int width, int height) {startDrawing();}
    public void surfaceCreated  (SurfaceHolder holder) {startDrawing();}
    public void surfaceDestroyed(SurfaceHolder holder) {stopDrawing();}

    void startDrawing() {draw = true;  stop = false; thread.interrupt();}       // Start drawing once the surface is available
    void stopDrawing()  {draw = false; stop = true;  thread.interrupt();}       // Stop drawing if the surface is removed

    float baseRadius() {return 0.45f * m(w, h);}                                // A radius that will fit on the screen at any angle

    public boolean onTouch(final View v, final MotionEvent m)                   // Decode and forward touch event
     {final int   i = pressed ? m.findPointerIndex(pointerId) : 0;              // Position of primary pointer
      final float x = pressed ? m.getX(i) : m.getX();
      final float y = pressed ? m.getY(i) : m.getY();
      switch(m.getActionMasked())                                               // 𝞅𝝸𝝺
       {case MotionEvent.ACTION_DOWN:                                           // Primary down
          pointerId = m.getPointerId(m.getActionIndex());                       // Save pointer id of primary pointer
          pressed = true;                                                       // Pressed
          pressedTime = T();                                                    // Time of last press
          findTopMostSelectedItem(x, y);                                        // Select the top most touched item
          touchedTopMostItemPosition(x, y);                                     // Update effects of touch on drawing items
//        colourAtTouch(x, y);                                                  // Get colour at touch is possible
          if (!rotationControllerSelected())    𝞈Rotation   .reset();           // Remove tracing if selected item is not a tracing controller
          if (!translationControllerSelected()) 𝞈Translation.reset();           // Remove translation if selected item is not a translation controller
          if (currentGoal != null) currentGoal.reset();                         // Reset current goal
          pointerPressed();                                                     // Forward
        return true;
        case MotionEvent.ACTION_POINTER_DOWN:                                   // Secondary down and dragging an item on the translation tracker
        return true;
        case MotionEvent.ACTION_UP:                                             // Up
        case MotionEvent.ACTION_CANCEL:
          pressed = false;                                                      // Not pressed
          releasedTime = T();                                                   // Time of last release
          draggedTopMostItemPosition(x, y);                                     // Update effects of drag on drawing items
          pointerReleased();                                                    // Pointer release is processed before we reset the selected item - this allows improvement processing to occur on the tracing as well as the original
//        colourAtTouchReset();                                                 // Reset touch colour meaning that as soon as the user lifts their finger the colour is unselected
          if (drawing != null && currentGoal!=null) currentGoal.check();        // Check whether we have reached the current goal or not - presumably we need a drawing to be able to do this.
          selectedItem = null;                                                  // Must be last to allow pointerReleased() to process the selectedItem
        return true;
        case MotionEvent.ACTION_MOVE:                                           // Drag
          draggedTopMostItemPosition(x, y);                                     // Process primary pointer move on selected item
//        colourAtTouch(x, y);                                                  // Get colour at touch is possible
          pointerDragged();
          if (drawing!=null&&currentGoal!=null) currentGoal.indicateHowClose(); // Indicate how far we are form the current goal by the music level
        return true;
        default:
          pressed = false;
        return false;
       }
     }

    void pointerPressed () {if (drawing != null) drawing.pointerPressed ();}    // Pointer pressed
    void pointerDragged () {if (drawing != null) drawing.pointerDragged ();}    // Pointer dragged
    void pointerReleased() {if (drawing != null) drawing.pointerReleased();}    // Pointer released

    void findTopMostSelectedItem(final float x, final float y)                  // Find the top most selected item if an item has been selected
     {selectedItem = drawing == null ? null : drawing.findTopMostSelectedItem(x, y);
     }
    void touchedTopMostItemPosition(final float x, final float y)               // Update the item positions
     {if (selectedItem != null) selectedItem.updateItemPosition(true, x, y);
     }
    void draggedTopMostItemPosition(final float x, final float y)               // Update the item positions
     {if (selectedItem != null) selectedItem.updateItemPosition(false, x, y);
     }
    boolean rotationControllerSelected()                                        // Selected item is a rotation controller
     {return selectedItem != null && selectedItem.item.values.𝞈 == 𝞈Rotation;
     }
    boolean translationControllerSelected()                                     // Selected item is a translation controller
     {return selectedItem != null && selectedItem.item.values.𝞈==𝞈Translation;
     }
    float mirrorSeperation() {return nd(𝞈1.angle-𝞈2.angle);}                    // Separation angle between mirrors
//------------------------------------------------------------------------------
// 𝕮olour at touch capability
//------------------------------------------------------------------------------
//  boolean colourAtTouchTimedOut()                                             // Use colour at touch processing to reorder the drawing order if pressed or within timeout.
//   {return !pressed && Since(releasedTime) > 5;
//   }
//  int ColourAtTouchWanted() {return 0;}                                       // Override with number of elements to save if this facility is required
//  int colourAtTouch = 0;                                                      // Current touch colour
//  void colourAtTouchReset() {colourAtTouch = 0;}
//  void colourAtTouch(final float x, final float y)                            // Get colour at touch is possible
//   {final int c = drawing != null && drawing.ColourAtTouch != null ?          // Get colour or reset
//      drawing.ColourAtTouch.touchedColour(x, y) : 0;
//    if (c != 0) colourAtTouch = c;                                            // Update colour if a new colour has been found
//   }
//------------------------------------------------------------------------------
// 𝕯raw and time the display rate
//------------------------------------------------------------------------------
    void draw()                                                                 // Draw
     {canvas = vsh.lockCanvas();                                                // Get the canvas
      long s1, s2, d1, d2;                                                      // Timing
      if (canvas == null) return;                                               // Make sure canvas is not null
      canvas.drawColor(0xff000000);                                             // Set background to opaque black to clear the draw area - translucent does nothing
      w = canvas.getWidth(); h = canvas.getHeight();                            // Canvas dimensions
      cx = w / 2f; cy = h / 2;                                                  // Centre location
      s1 = t();
      drawing = loadDrawing();                                                  // Load the drawing to be drawn
      d1 = t() - s1; s2 = t();                                                  // Save construction time
      underlay();                                                               // Highlights drawn under the drawing
      drawing.draw();                                                           // Draw the drawing
      overlay();                                                                // Highlights drawn over the drawing
      if ((debug & debugMesh) > 0) drawing.showVertices();                      // Debug a mesh by showing the number of each vertex
      d2 = t() - s2;                                                            // Save draw time
      if ((debug & debugTiming) > 0) say("AAAA d1="+d1+" d2="+d2);              // Display statistics
       vsh.unlockCanvasAndPost(canvas);                                          // Display canvas
     }
    abstract Drawing loadDrawing();                                             // Load the drawing from a user supplied override
    void underlay() {if (drawing != null) drawing.underlay();}                  // Highlights drawn under the drawing
    void overlay()  {if (drawing != null) drawing.overlay();}                   // Highlights drawn over the drawing
//------------------------------------------------------------------------------
// 𝕻aint details and applications as used in overlays to elucidate the drawing
//------------------------------------------------------------------------------
    Stack<Mirror> overlayMirror;                                                // If present will produce a single reflection in each mirror of the items drawn below which differs from the behavior of items where reflections in one of the mirrors are reflected again in the other mirror if present. Reflected items are drawn at lower opacity.
    Integer opacityOverride;                                                    // Controls opacity if set
    DashPathEffect dashOverride;                                                // Controls dashing if set

    float innerThickness()                                                      // Half the thickness of central lines in the upper layer of this drawing
     {final float t = canvas == null ?  16f : m(64, M(8, sq(m(w, h))));         // Size to canvas
      return t / 8f;                                                            // Scaled
     }
    float outerThickness() {return 8 * innerThickness();}                       // Outer thickness based on inner thickness
    void setPaint() {setPaint(𝝺w);}                                             // Set paint to known state for drawing overlays with default colour
    void setPaint(final int colour)                                             // Set paint to known state for drawing overlays with supplied colour
     {paint.setColor(colour);                                                   // Set colour
      paint.setPathEffect(null);                                                // No line dashes
      paint.setAlpha(opacityOverride == null ? 255 : opacityOverride);          // Full opacity
      paint.setPathEffect(dashOverride);                                        // Dash effect
      paint.setStyle(Paint.Style.FILL_AND_STROKE);                              // Fill and Stroke
      paint.setStrokeWidth(innerThickness()*2);                                 // Standard thickness
     }
    void setPaint(final int colour, final float strokeWidth)                    // Set paint to known state for drawing overlays with supplied colour
     {setPaint(colour);                                                         // Set general characteristics
      paint.setStyle(Paint.Style.STROKE);                                       // Fill and Stroke
      paint.setStrokeWidth(strokeWidth);                                        // Fill and Stroke
     }
    void setPaint(final int colour, final DashPathEffect dpe)                   // Set paint to known state for drawing overlays with supplied colour and dash effect
     {setPaint(colour);                                                         // Set general characteristics
      paint.setPathEffect(dpe);                                                 // Dash effect
     }
    void setPaint(final int colour, final float strokeWidth,                    // Set paint to known state for drawing overlays with supplied colour and dash effect
                  final DashPathEffect dpe)
     {setPaint(colour);                                                         // Set general characteristics
      paint.setStyle(Paint.Style.STROKE);                                       // Fill and Stroke
      paint.setStrokeWidth(strokeWidth);                                        // Fill and Stroke
      paint.setPathEffect(dpe);                                                 // Dash effect
     }
    int defaultOpacity0() {return 255* 8/16;}                                   // Default opacity for lower layer
    int defaultOpacity1() {return 255*10/16;}                                   // Default opacity for upper layer

    void drawLine(final RectF r) {drawLine(r.left, r.top, r.right, r.bottom);}  // These draw routines save their colours so we can question what colour the user is touching
    void drawLine(final PointF p, PointF 𝗽) {drawLine(p.x, p.y, 𝗽.x, 𝗽.y);}     // Draw a line from a point(p) to point(𝗽)
    void drawLine(final PointF p, final float x, final float y)                 // Draw a line from a point(p) to point(x,y)
     {drawLine(p.x, p.y, x, y);
     }
    void drawLine(final float x, final float y, final float 𝘅, final float 𝘆)   // Draw line and save colour
     {canvas.drawLine(x, y, 𝘅, 𝘆, paint);
      if (overlayMirror != null)                                                // Draw refelections if there any mirrors present
       {final int alpha = paint.getAlpha();                                     // Save paint alpha
        for(final Mirror m : overlayMirror)                                     // Each mirror
         {m.reflectInMirror(x, y);                                              // Reflect one end of the line segment
          final float X = m.𝖃, Y = m.𝖄;
          m.reflectInMirror(𝘅, 𝘆);                                              // Reflect other end
          final float 𝗫 = m.𝖃, 𝗬 = m.𝖄;
          paint.setAlpha(alpha/2);                                              // Lower alpha for reflection
          canvas.drawLine(X, Y, 𝗫, 𝗬, paint);                                   // Draw reflection
         }
        paint.setAlpha(alpha);                                                  // Restore paint alpha
       }
     }
    void drawAlternatingLine(int c, int 𝗰,                                      // Draw a line that alternates between colour(c) and colouy(𝗰)
      final DashPathEffect 𝕕1, final DashPathEffect 𝕕2,                         // using these dash paths for each colout
      final float x, final float y, final float 𝘅, final float 𝘆)               //between point(x,y) and point(𝘅,𝘆)
     {setPaint(c, 𝕕1); drawLine(x, y, 𝘅, 𝘆);
      setPaint(𝗰, 𝕕2); drawLine(x, y, 𝘅, 𝘆);
     }

    void drawLines(final float[]lines)                                          // Draw a set of lines and save colour
     {final int 𝗻 = lines.length;
      final float[]𝗹 = lines;
      for(int i = 0; i < 𝗻; i += 4)
       {final int 𝗶 = i;
        drawLine(𝗹[𝗶+0], 𝗹[𝗶+1], 𝗹[𝗶+2], 𝗹[𝗶+3]);
       }
     }
    void drawCircle(final float x, final float y, final float 𝗿)                // Draw circle and save colour
     {canvas.drawCircle(x, y, 𝗿, paint);
      if (overlayMirror != null)                                                // Draw refelections if there any mirrors present
       {final int alpha = paint.getAlpha();                                     // Save paint alpha
        for(final Mirror m : overlayMirror)                                     // Each mirror
         {m.reflectInMirror(x, y);                                              // Reflect centre of circle
          final float X = m.𝖃, Y = m.𝖄;
          canvas.drawCircle(X, Y, 𝗿, paint);                                    // Draw reflection
         }
        paint.setAlpha(alpha);                                                  // Restore paint alpha
       }
     }
    void drawMirrors(final int 𝝺1, final int 𝝺2,
      final DashPathEffect 𝕕1, final DashPathEffect 𝕕2)                         // Draw mirrors in alternating colours
     {if (overlayMirror == null) return;                                        // Draw refelections if there any mirrors present
      for(final Mirror m : overlayMirror)                                       // Each mirror
       {final float dx = m.𝕏, dy = m.𝕐, d = d(dx, dy), 𝗱 = d(w,h)/d,            // Mirror vector
          mx = 𝗱 * dx,  my = 𝗱 * dy,                                            // Vector parallel to mirror long enough to cross the entire screen
           x = m.x+mx,   y = m.y+my, 𝘅 = m.x-mx, 𝘆 = m.y-my;                    // Start and end points of mirror long enough to cross the screen in any orientation
        setPaint(𝝺1, 𝕕1); canvas.drawLine(x, y, 𝘅, 𝘆, paint);                   // Draw mirror lines directly so they are not reflected
        setPaint(𝝺2, 𝕕2); canvas.drawLine(x, y, 𝘅, 𝘆, paint);
       }
     }
//------------------------------------------------------------------------------
// 𝕲oals - things we are trying to get the user to do - 1:1 with DisplayDrawing
//------------------------------------------------------------------------------
    final Stack<Goal> Goals = new Stack<Goal>();                                // All known goals
    Goal currentGoal = null;                                                    // The goal we are prompting the user to achieve
    void setCurrentGoal(final Goal goal) {currentGoal = goal;}                  // Set the current goal for the user to acheive
    void achieved(Goal g)                                                       // Achievement - override to receive any achievements that the user achieves
     {if (g == null) return;                                                    // Own goal
      say("Achieved "+(g == currentGoal ? "current" : "different")+" goal: "+g.name);
     }
    void volumeLevel(final float v) {}                                          // Set  volume level - override to process volume level

    abstract class Goal
     {final String name;
      boolean achieved;                                                         // Whether the goal was achieved at the last lift or not - reset in touch
      Goal(String Name) {name = Name; Goals.add(0, this);}                      // Add in reverse order so that searches find the most specific goal first
      void check()                                                              // Check that this goal has been achieved
       {if (achieved = checkAchieved())
         {improveUserEffort();
          achieved(this);
         }
        else for(Goal g: Goals) if (g.checkAchieved()) {achieved(g); break;}    // See if it could be any other goal
       }
      void indicateHowClose()                                                   // Indicate how close we are to the goal
       {final float c = a(howClose());
        volumeLevel(c > 1 ? 1-1/c : 1-c);                                       // Show closeness via volume level of music 0 to 1
       }
      void reset() {achieved = false;}                                          // Reset achieved state
      boolean achievedGoal() {return achieved;}                                 // Whether the goal has been achieved
      abstract boolean checkAchieved ();                                        // Override this to perform the specific checks for your subclassed goal
      abstract void    improveUserEffort();                                     // Override this to improve the user's solution
      abstract float   howClose();                                              // Override this to indicate how close 0 or >> 1 - far, 1 - close the current solution is
     } // Goal
//------------------------------------------------------------------------------
// 𝕸irror
//------------------------------------------------------------------------------
    class Mirror                                                                // Mirror specification
     {final float x, y, 𝘅, 𝘆, 𝕏, 𝕐;                                             // Mirror is through point(x,y) and point(𝘅, 𝘆), vector parallel to mirror
      final float 𝕩, 𝕪;                                                         // Unit vector normal to mirror pointing at the reflection
      final boolean both;                                                       // False - only stuff in front of the mirror is reflected as if we were observing from inside the drawing, else if true - everything is reflected as if we were observing from above
      final boolean reflectReal;                                                // True: reflect real vertices
      final boolean reflectReflections;                                         // True: reflect real reflections
      float opacity = 1;                                                        // 0-1 opacity multiplier for this mirror
      float 𝖃, 𝖄;                                                               // Reflection of the latest supplied point - not thread safe but much faster
      boolean reflection;                                                       // True - reflection, false - real
      Mirror(float X, float Y, float A)                                         // Mirror line extends through point(x,y) at angle A
       {this( X, Y, X + cd(A), Y + sd(A), true, true, true);
       }
      Mirror(float X, float Y, float 𝗫, float 𝗬)                                // Mirror line extends through point(x,y) and point(𝘅, 𝘆)
       {this( X, Y, 𝗫, 𝗬, true, true, true);
       }
      Mirror(float X, float Y, float 𝗫, float 𝗬,                                // Mirror line extends through point(x,y) and point(𝘅, 𝘆) and reflects on one or both sides
        boolean Both, boolean ReflectReal, boolean ReflectReflections)
       {both = Both;                                                            // Whether to reflect on one or both sides
        reflectReal = ReflectReal;                                              // Reflect real vertices
        reflectReflections = ReflectReflections;                                // Reflect reflected vertices
        x = X; y = Y; 𝘅 = 𝗫; 𝘆 = 𝗬;                                             // Mirror line
        𝕏 = 𝘅 - x; 𝕐 = 𝘆 - y;                                                   // Vector parallel to mirror
        final float d = d(𝕏, 𝕐);                                                // Length of vector parallel to mirror
        𝕩 = 𝕐 / d; 𝕪 = -𝕏 / d;                                                  // Unit vector normal to mirror pointing at the reflection
       }
      boolean reflectInMirror(final float X, final float Y)                     // Point to reflect
       {final float d = pointToLine(X, Y, x, y, 𝘅, 𝘆);                          // Distance to mirror
        reflection = d >= 0;                                                    // True - the returned point is to the left of the line and is thus in reflected space.
        final int D = reflection || both ? 2 : 1;                               // Dump stuff behind the mirror in the mirror unless both specified
        𝖃 = X + D * d * 𝕩; 𝖄 = Y + D * d * 𝕪;
        return reflection;                                                      // True - the returned point is to the left of the line and is thus in reflected space.
       }
      Mirror opacity(final float Opacity) {opacity = Opacity; return this;}     // Set opacity
     } // Mirror
    int mirrorsRequired() {return mirrorsNone;}                                 // Override to specify which mnirrors should be drawn
/*------------------------------------------------------------------------------
 𝕿rack rotation about a centre so that the user can drag linearly and circularly

Each drawable item can refer to a tracker to records its motion both in
translation and in rotation around a point specified by the item.  Multiple
items can refer to the same item so that dragging any one of them updates the
referenced tracker.

Trackers are not responsible for repositioning the item, this is done when by
the caller when the drawing is created i.e - the trackers are used by the
caller to update the position and configuration of the drawing and in so ding
might well update the position of the dragged item.
------------------------------------------------------------------------------*/
    class 𝝮                                                                     // Track drag rotation
     {final String name;                                                        // Name of rotation
      float Dx, Dy, lastTime, lastAngle;                                        // Start of last drag, time of last drag, angle at last drag
      float angle, speed, aldx, aldy;                                           // Current angle, angular speed, accumulated linear drag in x, accumulated linear drag in y
      float radial, contraRadial;                                               // Drag along radius, across radius
      𝝮(final String Name) {name = Name;}                                       // Name to facilitate debugging
      𝝮(final String Name, float Angle) {name = Name; angle = Angle;}           // Name to facilitate debugging, initial angle
      void updateMotion(final boolean start,                                    // Start or continuation of drag
        int reflectionDepth,                                                    // Reflection depth of selected item,
        float x, float y, float X, float Y)                                     // Track rotation about centre at point(x,y) of drag point(X, Y)
       {if (true)                                                               // Update rotation
         {final float a = at(X-x, Y-y), T = f(T());                             // Drag angle around point(x,y), time taken by last drag motion
          if (!start)                                                           // Drag
           {final int d = reflectionDepth;                                      // Shorten name
            final float da = a - lastAngle, dt = T - lastTime,                  // Change in drag angle, time delta of drag
              r = d == 0 ? +1f : d == 1 ? -1f : 0.5f;                           // How much the drag affects the original object by reflection depth
            angle = nd(angle + r*da);                                           // Sum angles dragged
            if (dt > 0) speed  = 0.5f * speed + 0.5f * r * da / dt;             // Angular speed from drag with averaging
           }
          lastTime  = T;                                                        // Update time of last motion event
          lastAngle = a;                                                        // Update last angle
         }
        if (!start)                                                             // Update radial and cross radial drag
         {final float r = d(X-x, Y-y) - d(Dx-x, Dy-y);                          // Radial drag
          radial       += r;                                                    // Sum radial drag
          contraRadial += os(r, d(X-Dx, Y-Dy));                                 // Contra radial is the other side of the drag vector from the radius
         }
        if (!start)                                                             // Accumulated linear drag accounting for only upto a single mirror at the moment
         {final float dx = X - Dx, dy = Y - Dy;                                 // Delta in normal coordinates
          if      (reflectionDepth == 0) {aldx += dx; aldy += dy;}              // No reflection
          else if (reflectionDepth == 1)                                        // Reflected delta when on mirror present, where parallel to the mirror is not transformed, but away from the mirror is reversed
           {final Mirror m = drawing.mirror1;
            final float d = 2 * pointToLine(X, Y, Dx, Dy, Dx + m.𝕏, Dy + m.𝕐);  // Twice distance to mirror to get reflection of drag
            aldx += dx + d * m.𝕩;                                               // Mirror normal in x
            aldy += dy + d * m.𝕪;                                               // Mirror normal in y
           }
         }
        Dx = X; Dy = Y;                                                         // Start of next drag
       }
      void reset() {aldx = aldy = 0; angle = 0; speed = 0;}                     // Reset
      public String toString()                                                  // Stringify rotation
       {return " Rotation: "+name+"(angle="+angle+"speed="+speed+"dx="+aldx+"dy="+aldy+")";
       }
     } // 𝝮
    void sra(final int a)                                                       // Set rotation tracker angle angle when close to an interesting angle
     {if (nearAngle(𝞈Rotation.angle, a) < 1) 𝞈Rotation.angle = a;
     }
    void sra1(final int a) {if (nearAngle(𝞈1.angle, a) < 1) 𝞈1.angle = a;}      // Set rotation tracker 1 angle angle when close to an interesting angle
    void sra2(final int a) {if (nearAngle(𝞈2.angle, a) < 1) 𝞈2.angle = a;}      // Set rotation tracker 2 angle angle when close to an interesting angle
    void sra3(final int a) {if (nearAngle(𝞈3.angle, a) < 1) 𝞈3.angle = a;}      // Set rotation tracker 3 angle angle when close to an interesting angle
    boolean makeTracing()                                                       // Tracing possible if rotated or translation
     {return a(𝞈Rotation.angle) > closeEnoughAngle ||                           // Rotated
             d(𝞈Translation.aldx, 𝞈Translation.aldy) > innerThickness();        // Translated
     }
/*------------------------------------------------------------------------------
𝕯rawings are comprised of various geometric shapes known generically as items.
Each item is drawn in two layers, the lower layer more spread out, the upper
layer more focussed. The code below assumes that there will always be the same
number of vertices in each layer for each item.
------------------------------------------------------------------------------*/
    abstract class Drawing                                                      // A geometrical drawing
     {final int layers=2, coordsPerVertexPair=4, vertexPairsPerFireBreak=2;     // Number of layers (0 - Background, 1 - Foreground), numbers per pair of coordinates, number of pairs of coordinates per firebreak
      final Stack<Item> items = new Stack<Item>();                              // Items used to draw this drawing
      float[]vertices;                                                          // Vertex array
      int[]colours;                                                             // This must be the same dimension as vertices, but only the colours in the first half are used
      boolean reflectAll = true, traceAll = true;                               // If all items should be reflected or traced without exceptions we can make some optimizations
      boolean[]doNotReflect, doNotTrace;                                        // Whether this vertex should be reflected or not, traced or not
      int layer;                                                                // Current layer
      int numberOfCoordinates, numberOfVertexPairs;                             // Number of coordinates, number of pairs of points
      int mirrorBlockSize, tracingBlockSize;                                    // Size of unmirrored base in pairs of coordinate pairs, size of rotation block = all mirrored data
      Mirror mirror1, mirror2;                                                  // Mirror definition chosen or null for no mirror - upto two mirrors allowed
      float tracingRotationOpacity = 1;                                         // Opacity multiplier for rotated tracing if present
      Float tracingRotationCentreX;                                             // Centre of rotation of drawing - X - else centre of drawing
      Float tracingRotationCentreY;                                             // Centre of rotation of drawing - Y - else centre of drawing
      int mirrors()      {return mirror2 != null ? 2 : mirror1 != null ? 1 : 0;}// Number of active mirrors
      int mirrorBlocks() {return mirror2 != null ? 5 : mirror1 != null ? 2 : 1;}// Number of blocks of data = unmirrored plus mirrored - mirrors are normalized so abbreviated tests are ok
      int tracingBlocks(){return makeTracing ? 2 : 1;}                          // Double the number of vertices if a tracing of the drawing is being rotated
      boolean makeTracing = makeTracing();                                      // Do tracing if tracing angle shows movement from zero or translation has been applied
      void allocate()                                                           // Allocate the vertex arrays
       {for(final Item i: items)                                                // Preprocess items
         {i.setSelectedColours();                                               // Set selected colours for each item based in unselected colours
          reflectAll = reflectAll && i.reflect();                               // All items are to be reflected
          traceAll   = traceAll   && i.trace();                                 // All items are to be traced on the tracing
         }
        int N = 0;                                                              // Number of pairs of vertices to use plus fire breaks
        for(layer = 0; layer < layers; ++layer)                                 // Each layer
         {for(final Item i: items)                                              // Add vertex pairs and colours for each item
           {final int nV = i.numberOfVertexPairs();                             // Number of vertex pairs in this element which could be zero for a composite element like a cross
            if (nV == 0) continue;                                              // Skip composite element with zero vertices as drawing will be done via components
            N += nV + vertexPairsPerFireBreak;                                  // Move up over fire break
           }
         }
        final int M = mirrorBlocks(), R = tracingBlocks();                      // Number of blocks of data required to accommodate reflections and tracing rotations
        mirrorBlockSize     = N;                                                // Record mirror block size
        tracingBlockSize    = N * M;                                            // Block subject to rotation = all mirrored data
        numberOfVertexPairs = N * M * R;                                        // Number of pairs of points including fire breaks
        numberOfCoordinates = coordsPerVertexPair * numberOfVertexPairs;        // Number of coordinates including fire breaks
        vertices = new float[numberOfCoordinates];                              // A vertex is represented by two coordinates
        colours  = new int  [numberOfCoordinates];                              // The colours array must be the same size, only the first half is actually loaded
        if (!reflectAll) doNotReflect = new boolean[mirrorBlockSize];           // Reflect this vertex or not - chosen this way around for default and therefore fast initialization of this array
        if (!traceAll)   doNotTrace   = new boolean[mirrorBlockSize];           // Trace this vertex or not - chosen this way around for default and therefore fast initialization of this array
       }
      void loadVertices()                                                       // Load the vertices to be drawn
       {final float[]v = vertices;                                              // Shorten name
        final int n = coordsPerVertexPair;                                      // Shorten name
        int N = 0;                                                              // Position in vertices/colour arrays measured in vertex pairs
        for(layer = 0; layer < layers; ++layer)                                 // Each layer
         {for(final Item i: items)                                              // Add vertex pairs and colours for each item
           {final int nV = i.numberOfVertexPairs();                             // Number of vertex pairs in this element which could be zero for a composite element like a cross
            if (nV == 0) continue;                                              // Skip composite element with zero vertices as drawing will be done via components
            final boolean 𝗹 = layer == 0;                                       // On layer 0
            final int s = N + vertexPairsPerFireBreak/2, 𝘀 = s * n;             // Half the fire break at the start

            if (𝗹) i.startVertexPair = s;                                       // Record starting vertex pair for this item in layer 0
            if (layer == 0 || i.C() != null)                                    // Add vertices and colours to appropriate arrays if needed of this layer
             {i.addVertices(s);                                                 // Add the vertices
              if (i.𝝻 != null) i.𝝻.mapPoints(v,𝘀, v,𝘀, nV*n);                   // Transform vertices if a transformation matrix  has been supplied
              i.addColours (s);                                                 // Add colours
             }
            v[𝘀-4] = v[𝘀-2] = v[𝘀+0];                                           // Half fire break at start X
            v[𝘀-3] = v[𝘀-1] = v[𝘀+1];                                           // Half fire break at start Y
            N = s + nV;                                                         // Move up
            if (𝗹) i.endVertexPair = N;                                         // Record 1 past final vertex pair for this item in layer 0
            final boolean nr = !i.reflect(), nt = !i.trace();                   // No reflection or tracing required for this item
            if (!reflectAll) java.util.Arrays.fill(doNotReflect, s, N, nr);     // Set reflection for vertices associated with this item
            if (!traceAll)   java.util.Arrays.fill(doNotTrace,   s, N, nt);     // Set tracing for vertices associated with this item
            final int 𝗲 = N * n;                                                // Half fire break at end
            v[𝗲+0] = v[𝗲+2] = v[𝗲-2];                                           // X
            v[𝗲+1] = v[𝗲+3] = v[𝗲-1];                                           // Y
            N += vertexPairsPerFireBreak/2;                                     // Size of fire break is two pairs of coordinates - the 2 is acceptable because we mention a pair
           }
         }
       }
      void reflectVertices()                                                    // Reflect each point
       {final Mirror m1 = mirror1, m2 = mirror2;                                // Mirror order will have been normalized when the mirrors were activated
        if (m1 == null) return;                                                 // No mirrors present
        final int 𝗻 = mirrorBlockSize*coordsPerVertexPair, n = 𝗻 / 2;           // Number of entries in vertex array for base unmirrored section, number of points to reflect
        for(int i = 0; i < n; ++i)                                              // Each coordinate pair
         {if (!reflectAll && doNotReflect[i/2]) continue;                       // Skip reflection of vertex if not reflected
          final int j = 2*i, 𝗷 = j + 1;                                         // x,y vertices for this coordinate pair
          final float x = vertices[j], y = vertices[𝗷];                         // Coordinates of vertex

          m1.reflectInMirror(x, y);                                             // Reflect original in first mirror
          final float x1 = m1.𝖃, y1 = m1.𝖄;
          if (m1.reflectReal)                                                   // Reflect real vertices
           {vertices  [1*𝗻+j] = x1;   vertices[1*𝗻+𝗷] = y1;
           }

          if (m2 != null)
           {m2.reflectInMirror(x, y);                                           // Reflect original in second mirror
            final float x2 = m2.𝖃, y2 = m2.𝖄;
            if (m2.reflectReal)                                                 // Reflect real vertices
             {vertices[2*𝗻+j] = x2;   vertices[2*𝗻+𝗷] = y2;
             }
            if (m1.reflectReflections)                                          // Reflect reflection in second mirror in first mirror
             {m1.reflectInMirror(x2, y2);
              final float x12 = m1.𝖃, y12 = m1.𝖄;
              vertices[3*𝗻+j] = x12; vertices[3*𝗻+𝗷] = y12;
             }

            if (m2.reflectReflections)                                          // Reflect reflection in second mirror in first mirror
             {m2.reflectInMirror(x1, y1);
              final float x21 = m2.𝖃, y21 = m2.𝖄;
              vertices[4*𝗻+j] = x21; vertices[4*𝗻+𝗷] = y21;
             }
           }
         }
       }
      void reflectColours()                                                     // Reflect colour for each point by halving their opacity
       {if (mirror1 == null) return;                                            // No mirrors present
        final boolean 𝗺 = mirror2 != null;                                      // Presence of each mirror
        final Mirror m1 = mirror1, m2 = mirror2;                                // Shorten names of mirrors whose order will already have been normalized
        final float   o1 =      m1.opacity;                                     // Opacity of mirrors
        final float   o2 = 𝗺 ? m2.opacity : 1;
        final boolean u1 = o1 != 1, u2 = o2 != 1;                               // Opacity requires adjustment
        final int 𝗻 = mirrorBlockSize*coordsPerVertexPair, n = 𝗻 / 2;           // Number of entries in vertex array for base unmirrored section, number of points to reflect
        final int p4 = 0xff;                                                    // Mask for least significant byte

        for(int i = 0; i < n; ++i)                                              // Reflect color
         {final int 𝗶 = i;                                                      // Finalize
          final int C = colours[i],                                             // Colour
            a  = (C>>24) & p4, r = (C>>16) & p4, g = (C>>8) & p4, b = C & p4,   // Alpha,  Colour components
            𝗴 = g>>1,                                                           // Filtered colour components for mirror 2 which absorbs green
            a0 = a,                                                             // Filtered alpha for mirror 1 - no change in opacity - let colour filtering do the job
            a1 = u1 ? i(o1 * a0) : a0;                                          // Filtered alpha for mirror 1 including mirror opacity if necessary

          colours[1*n+𝗶] = a1<<24 | r<<16 | 𝗴<<8 | b;                           // Mirror 1 absorbs green light

          if (𝗺)                                                                // Mirror 2
           {final int                                                           // Colour
              a2 = u2 ? i(o2 * a0) : a0,                                        // Filtered alpha for mirror 2
              𝗿  = r>>1,                                                        // Filtered colour components for mirror 1 which absorbs red
              a3 = (a1*a2)>>8;                                                  // Filtered alpha for reflections in both mirrors
            colours[2*n+𝗶] = a2<<24 | 𝗿<<16 | g<<8 | b;                         // Mirror 2 absorbs red light
            colours[3*n+𝗶] = colours[4*n+𝗶] = a3<<24 | 𝗿<<16 | 𝗴<<8 | b;        // Secondary reflections
           }
         }
       }
      void swapVertices()                                                       // Swap blocks of vertices so that when reflections are present, all of layer 1 is above all of layer 0 and all of the reflections are drawn under the unreflected vertices
       {if (mirror1 == null) return;                                            // No mirrors present - order has been normalized so we do not need to check mirror2
        final int 𝗻 = mirrorBlockSize*coordsPerVertexPair/2, n = 𝗻 / 2;         // Number of entries in vertex array for base unmirrored section by layer, number of points to reflect
        final float[]v = vertices;                                              // Shorten name         1 2     21
        if (mirror2 == null)                                                    // One reflection: swap AaBb to BAba
         {for(int i = 0; i < n; ++i)                                            // Each coordinate pair
           {final int j = 2*i, 𝗷 = j + 1;                                       // x,y vertices for this coordinate pair
            final float
              Ax = v[0*𝗻+j], Ay = v[0*𝗻+𝗷],
              ax = v[1*𝗻+j], ay = v[1*𝗻+𝗷],
              Bx = v[2*𝗻+j], By = v[2*𝗻+𝗷],
              bx = v[3*𝗻+j], by = v[3*𝗻+𝗷];
            v[0*𝗻+j] = Bx; v[0*𝗻+𝗷] = By;                                       // Swap
            v[1*𝗻+j] = Ax; v[1*𝗻+𝗷] = Ay;
            v[2*𝗻+j] = bx; v[2*𝗻+𝗷] = by;
            v[3*𝗻+j] = ax; v[3*𝗻+𝗷] = ay;
           }
         }                                                                      //                       1 2 3 4 5     54321
        else                                                                    // Two reflections: swap AaBbCcDdEe to EDCBAedcba - this is somewhat arbitrary
         {for(int i = 0; i < n; ++i)                                            // Each coordinate pair
           {final int j = 2*i, 𝗷 = j + 1;                                       // x,y vertices for this coordinate pair
            final float
              Ax = v[0*𝗻+j], Ay = v[0*𝗻+𝗷],
              ax = v[1*𝗻+j], ay = v[1*𝗻+𝗷],
              Bx = v[2*𝗻+j], By = v[2*𝗻+𝗷],
              bx = v[3*𝗻+j], by = v[3*𝗻+𝗷],
              Cx = v[4*𝗻+j], Cy = v[4*𝗻+𝗷],
              cx = v[5*𝗻+j], cy = v[5*𝗻+𝗷],
              Dx = v[6*𝗻+j], Dy = v[6*𝗻+𝗷],
              dx = v[7*𝗻+j], dy = v[7*𝗻+𝗷],
              Ex = v[8*𝗻+j], Ey = v[8*𝗻+𝗷],
              ex = v[9*𝗻+j], ey = v[9*𝗻+𝗷];
            v[0*𝗻+j] = Ex; v[0*𝗻+𝗷] = Ey;                                       // Swap
            v[1*𝗻+j] = Dx; v[1*𝗻+𝗷] = Dy;
            v[2*𝗻+j] = Cx; v[2*𝗻+𝗷] = Cy;
            v[3*𝗻+j] = Bx; v[3*𝗻+𝗷] = By;
            v[4*𝗻+j] = Ax; v[4*𝗻+𝗷] = Ay;
            v[5*𝗻+j] = ex; v[5*𝗻+𝗷] = ey;
            v[6*𝗻+j] = dx; v[6*𝗻+𝗷] = dy;
            v[7*𝗻+j] = cx; v[7*𝗻+𝗷] = cy;
            v[8*𝗻+j] = bx; v[8*𝗻+𝗷] = by;
            v[9*𝗻+j] = ax; v[9*𝗻+𝗷] = ay;
           }
         }
       }
      void swapColours()                                                        // Swap blocks of colours so that when reflections are present, all of layer 1 is above all of layer 0 and all of the reflections are drawn under the unreflected vertices
       {if (mirror1 == null) return;                                            // No mirrors present - order has been normalized so we do not need to check mirror2
        final int 𝗻 = mirrorBlockSize;                                          // Number of entries in vertex array for base unmirrored section by layer, number of points to reflect
        final int[]𝗰 = colours;                                                 // Shorten name         1 2     21
        if (mirror2 == null)                                                    // One reflection: swap AaBb to BAba
         {for(int i = 0; i < 𝗻; ++i)                                            // Each coordinate pair
           {final int 𝗶 = i,                                                    // Finalize to optimize
              A = 𝗰[0*𝗻+𝗶],
              a = 𝗰[1*𝗻+𝗶],
              B = 𝗰[2*𝗻+𝗶],
              b = 𝗰[3*𝗻+𝗶];
            𝗰[0*𝗻+𝗶] = B;                                                       // Swap
            𝗰[1*𝗻+𝗶] = A;
            𝗰[2*𝗻+𝗶] = b;
            𝗰[3*𝗻+𝗶] = a;
           }
         }                                                                      //                       1 2 3 4 5     54321
        else                                                                    // Two reflections: swap AaBbCcDdEe to EDCBAedcba - this is somewhat arbitrary
         {for(int i = 0; i < 𝗻; ++i)                                            // Each coordinate pair
           {final int 𝗶 = i,                                                    // Finalize to optimize
              A = 𝗰[0*𝗻+𝗶],
              a = 𝗰[1*𝗻+𝗶],
              B = 𝗰[2*𝗻+𝗶],
              b = 𝗰[3*𝗻+𝗶],
              C = 𝗰[4*𝗻+𝗶],
              c = 𝗰[5*𝗻+𝗶],
              D = 𝗰[6*𝗻+𝗶],
              d = 𝗰[7*𝗻+𝗶],
              E = 𝗰[8*𝗻+𝗶],
              e = 𝗰[9*𝗻+𝗶];
            𝗰[0*𝗻+𝗶] = E;                                                       // Swap
            𝗰[1*𝗻+𝗶] = D;
            𝗰[2*𝗻+𝗶] = C;
            𝗰[3*𝗻+𝗶] = B;
            𝗰[4*𝗻+𝗶] = A;
            𝗰[5*𝗻+𝗶] = e;
            𝗰[6*𝗻+𝗶] = d;
            𝗰[7*𝗻+𝗶] = c;
            𝗰[8*𝗻+𝗶] = b;
            𝗰[9*𝗻+𝗶] = a;
           }
         }
       }
      void tracingVertices()                                                    // If tracing rotation is in effect - copy and rotate the drawing around the centre into the upper rotation block
       {if (!makeTracing) return;                                               // No tracing rotation present
        final int 𝗻 = tracingBlockSize*coordsPerVertexPair/2, n = 𝗻 * 2;        // Number of coordinates, distance to copy and rotate
        final float[]v = vertices;                                              // Shorten name         1 2     21
        final float  a = 𝞈Rotation.angle, 𝘅 = cd(a), 𝘆 = sd(a),                 // Unit vector of rotation
          X = f(tracingRotationCentreX, cx), Y = f(tracingRotationCentreY, cy), // Coordinates of centre of rotation
          dx = 𝞈Translation.aldx, dy = 𝞈Translation.aldy;                       // Translation tracker
        final int m = mirrorBlockSize;                                          // Number of vertices in a mirror block
        final Matrix 𝝻 = tracingMatrix; 𝝻.reset();                              // Tracing matrix
        𝝻.postTranslate(dx, dy);                                                // Translation is first as it is expressed in the unrotated frame
        𝝻.postRotate(a, X, Y);                                                  // Rotation next
        𝝻.mapPoints(v, n, v, 0, 𝗻);                                             // Trace all points
        if (!traceAll)                                                          // Zero vertices that requested that they be exempted from tracing
         {for(int i = 0; i < n; i+=4)                                           // Each coordinate pair - OPT: use a matrix instead of this loop
           {if (doNotTrace[(i/4)%m])                                            // Zero vertex because it requested no tracing
             {final int 𝗶 = n+i;
              v[𝗶] = v[𝗶+1] = v[𝗶+2] = v[𝗶+3] = 0;
             }
           }
         }
       }
      void tracingColours()                                                     // If tracing rotation is in effect - duplicate the colour block
       {if (!makeTracing) return;                                               // No rotation present
        final int  n = tracingBlockSize*2;                                      // Number of entries to be rotated
        final int[]c = colours;                                                 // Shorten name
        System.arraycopy(c, 0, c, n, n);                                        // Duplicate colours
        if (tracingRotationOpacity != 1)                                        // Multiply in rotation opacity if present
         {final float o = tracingRotationOpacity;                               // Overall opacity to be multiplied in
          if (o == 0) {for(int i = 0; i < n; ++i) c[n+i] &= 0x00ffffff;}        // Special case - zero
          else
           {for(int i = 0; i < n; ++i)                                          // Multiply opacity
             {final int 𝗶 = i, 𝗰 = c[n+𝗶], a = i((𝗰>>24)*o)<<24;                // Finalize to optimize, new opacity
              c[n+𝗶] = (𝗰&0x00ffffff)|a;                                        // Set opacity
             }
           }
         }
       }
      void draw()                                                               // Draw vertices minus final fire break
       {allocate();                                                             // Allocate vertices arrays
        loadVertices();                                                         // Load the vertices
        reflectVertices();                                                      // Reflect vertices if necessary
        reflectColours();                                                       // Reflect colours if neessary
        swapVertices();                                                         // Swap blocks of vertices so that when reflections are present, all of layer 1 is above all of layer 0 and all of the reflections are drawn under the unreflected vertices
        swapColours();                                                          // Swap blocks of vertices so that when reflections are present, all of layer 1 is above all of layer 0 and all of the reflections are drawn under the unreflected vertices
        tracingVertices();                                                      // Rotate a copy of the vertices if rotation is in effect
        tracingColours();                                                       // Duplicate the colours if rotation is in effect
        canvas.drawVertices(Canvas.VertexMode.TRIANGLE_STRIP,                   // Draw vertices
          numberOfCoordinates, vertices,
          0, null, 0, colours, 0, null, 0, 0, paint);
       }
      void showVertices()                                                       // Show the vertices with numbers drawn by each vertex
       {for(int i = 0; i < numberOfVertexPairs; ++i)
         {final float x = vertices[coordsPerVertexPair*i+0], y = vertices[coordsPerVertexPair*i+1];
          final float X = vertices[coordsPerVertexPair*i+2], Y = vertices[coordsPerVertexPair*i+3];
          paint.setTextSize(32);
          paint.setColor(Colours.Cyan);
          canvas.drawText(""+i, x, y, paint);
          paint.setColor(Colours.Magenta);
          canvas.drawText(""+i, X, Y, paint);
         }
       }
      SelectedItem findTopMostSelectedItem(final float x, final float y)        // Find the top most selected item if an item has been selected
       {SelectedItem S = null;                                                  // Currently top most selected item
        for(final Item i: items)                                                // Each possible item from bottom to top
         {final SelectedItem s = i.containsPoint(x, y);                         // Does this item contain the selection point
          if (s != null) S = s;                                                 // This item becomes the currently top most selected item if selected
         }
        return S;                                                               // Return top most item if there is is one
       }
//------------------------------------------------------------------------------
// 𝕮reate mirrors
//------------------------------------------------------------------------------
      Mirror createMirror(int n) {return createMirror(n == 1 ? 𝞈1 : 𝞈2, n);}    // Default mirror 1 - horizontal, 2 - vertical, two sided
      Mirror createMirror(final 𝝮 𝞇, int n)                                     // Horizontal(1) or vertical(2) mirror at centre, two sided
       {//return createMirror(𝞇, n, cx, cy, n == 1 ? 0 : 90);                   // Should mirror two be special? Probably not!
        return createMirror(𝞇, n, cx, cy, 0);
       }
      Mirror createMirror(final 𝝮 𝞇, int n, float a)                            // Mirror at centre with angle(a) and specified tracker
       {return createMirror(𝞇, n, cx, cy, a);
       }
      Mirror createMirror(int n, float X, float Y, float a)                     // Mirror from polar coordinates with no tracker, two sided
       {return createMirror(null, n, X, Y, a);
       }
      Mirror createMirror(int n, float X, float Y, float a, boolean both)       // Mirror from polar coordinates with no tracker, one or two sided
       {return createMirror(null, n, X, Y, a, both, true, true, true);
       }
      Mirror createMirror(𝝮 𝞇, int n, float X, float Y, float a)                // Mirror from polar coordinates origin at point (X, Y), angle(a), two sided
       {return createMirror(𝞇, n, X, Y, a, true, true, true, true);
       }
      Mirror createMirror(final 𝝮 𝞇, final int n, final float X, final float Y, // Mirror from polar coordinates origin at point (X, Y), angle(a) and one or two sided
        final float a,                                                          // Angle of mirror
        final boolean both,                                                     // Both sides reflect
        final boolean reflectReal,                                              // Reflect real vertices
        final boolean reflectReflections,                                       // Reflect reflected vertices
        final boolean reflectMirrors)                                           // Reflect mirrors
       {final int r = mirrorsRequired();
        if ((r & n) == 0) return null;                                          // Mirror not required
        final float 𝗮 = a + (𝞇 != null ? 𝞇.angle : 0);                          // Mirror has no rotation tracker
//        final float 𝗮 = a;                                                    // Mirror rotation
        final Mirror 𝞅 = new Mirror(X, Y, X + cd(𝗮), Y + sd(𝗮),                           // Create the reflector of the mirror
          both, reflectReal, reflectReflections);                               // What gets reflected
        if (mirror1 == null) mirror1 = 𝞅; else mirror2 = 𝞅;                     // Normalize mirror positions
        final boolean m1 = mirror1 == 𝞅;                                        // Which mirror
        new Diameter()
         {{n = m1 ? "M1" : "M2";                                                // Name mirror
           x = X; y = Y; a = 𝗮;                                                 // Orientation of mirror
           r = M(w, h);                                                         // So we never see the ends of the reflections of the diameters representing the mirrors even when they have been rotated by reflection
           𝞈 = 𝞇;                                                               // Rotation from dragging mirror
           c1 = m1 ? 0xff0000 : 0x00ff00;                                       // Mirror 1 absorbs red, mirror2 absorbs green
           c2 = m1 ? 0x0000ff : 0x0000ff;
           C  = m1 ? 0xff00ff : 0x00ffff;
           C  = null;                                                           // Try mirrors without central line to better distinguish them from geometric items
           reflect = reflectMirrors;                                            // Whether this mirror can reflect other mirrors or not
         }};
        return 𝞅;                                                               // Return created mirror
       }
//------------------------------------------------------------------------------
// 𝕾elected item
//------------------------------------------------------------------------------
      class SelectedItem
       {final Drawing.Item item;                                                // Selected item
        final int reflectionNone=0, reflectionPrimary=1, reflectionSecondary=2; // 0 - not a reflection, 1 - primary reflection (moves in opposite direction), 2 - secondary reflection (moves in same direction but at twice the speed)
        final int reflectionDepth;                                              // How many mirrors the selection has been reflected in
        SelectedItem(Drawing.Item Item, int Block)
         {item = Item;
          final int m = mirrors(), b = Block;                                   // Number of mirrors, block
          reflectionDepth =
            m == 2 ? (b == 0 || b == 1 ? 2 : b == 2 || b == 3 ? 1 : 0) :        // Secondary reflection
            m == 1 ? (b == 0 ? 1 : 0) : 0;                                      // Primary or no reflection
         }
        void updateItemPosition(boolean start, float x, float y)
         {if (selectedItem != null)
           {selectedItem.item.updateItemPosition(start, reflectionDepth, x, y);
           }
         }
       } // SelectedItem
//------------------------------------------------------------------------------
// 𝕾hapes are variations on a drawable item
//------------------------------------------------------------------------------
      abstract class Values                                                     // Values used to characterize an item
       {float x = cx, y = cy;                                                   // Position of an item
        float r = baseRadius(), R = baseRadius();                               // Inner and outer radii for background
        float a, A;                                                             // Start angle, arc length
        float o = 1;                                                            // Opacity 0 to 1
        int c1, c2;                                                             // Colours of background
        Integer C;                                                              // Colours of foreground or null if foreground not to be drawn
        String n;                                                               // Name of item interned for fast compares to identify the selected item - should be unique across this drawing
        𝝮 𝞈;                                                                    // Drag rotation angle around point(𝕩,𝕪)
        Float 𝕩, 𝕪;                                                             // Centre around which rotation is measured: uses point(x,y) if not set. tracingRotationCentreX/Y is the centre around which the drawing is actually rotated
        boolean reflect = true, trace = true;                                   // Reflect/Trace this item by default
        Matrix 𝝻;                                                               // Transformation matrix to be applied to vertices describing this item if not null
        private int N;                                                          // Number of vertices
        void updateValuesWithPosition(boolean start, int reflectionDepth,       // Whether this is the start of a drag or the continuation the reflection depth of the item selected
          float X, float Y)                                                     // Update item values from drag
         {if (𝞈 != null)                                                        // Update tracker associated with this item
           {𝞈.updateMotion                                                      // Tracker exists
             (start, reflectionDepth, 𝕩 != null ? 𝕩:cx, 𝕪 != null ? 𝕪:cy, X, Y);// Start of drag, reflection depth of selected item, direction of rotation about specified centre - changed default rotation point to centre of drawing while coding ArrowHead
           }
         }
        public String toString()                                                // Dump item values
         {return (n != null ? n : "")+"(x="+x+" y="+y+
          " r="+r+" R="+R+" a="+a+" A="+A+" N="+N+
          " colours=("+c1+", "+c2+", "+C+") o="+o+")";
        }
       } // Values
      abstract class Item extends Values                                        // A drawable item with a default set of values
       {Values values = this;                                                   // Load the item and add it to the list of items to be drawn
        boolean selected = false;                                               // Item has been selected
        boolean flash()                                                         // Periodically change colours to flash selected item
         {if (!selected || values.𝞈 == null) return false;                      // No flashing if not selected or no tracker attached even if this item is selected
          final double s = Since(pressedTime), c = s / flashCycle;
          return i(c) % 2 == 0;
         }
        int startVertexPair, endVertexPair;                                     // Start and end position in vertex array of layer 0 measured in vertex pairs
        float update;                                                           // An update amount for this item as a result of dragging
        float x() {return values.x;}                                            // Start angle of 0
        float y() {return values.y;}                                            // Sweep angle
        float r() {return layer == 0 ? r0() : r1();}                            // Inner radius for current layer
        float R() {return layer == 0 ? R0() : R1();}                            // Outer radius for current layer
        float r0(){return values.r;}                                            // Inner radius - lower layer
        float R0(){return values.R;}                                            // Outer radius - lower layer
        float r1(){return values.r;}                                            // Inner radius - upper layer
        float R1(){return values.R;}                                            // Outer radius - upper layer
        float rR(){return (values.r+values.R) / 2f;}                            // Average of radii in lower layer
        float a() {return values.a;}                                            // Start angle in degrees
        float A() {return values.A;}                                            // Sweep angle
        float o() {return values.o;}                                            // Opacity
        int  c1() {return values.c1;}                                           // Colours
        int  c2() {return values.c2;}
        int  C1() {return layer == 0 ? (flash() ? c2() : c1()) : C();}          // Colours depending on whether this item is the selected item selected
        int  C2() {return layer == 0 ? (flash() ? c1() : c2()) : C();}
        Integer C() {return values.C;}
        boolean reflect() {return values.reflect;}                              // Reflect this item or not
        boolean trace()   {return values.trace;}                                // Trace this item or not
        Item() {items.push(this); if (n != null) n = n.intern();}               // Load the item and add it to the list of items to be drawn, intern the name if present so we can do fast compares ;later
        int numberOfVertexPairs() {return 0;}                                   // The number of vertices this item uses
        void addVertices(final int start) {}                                    // Add the vertices starting at this position in the vertices array
        void addColours (final int start)                                       // Add the colours starting at this position in the colours array
         {final  float o = o() *                                                // Scale opacity
           (layer == 0 ? defaultOpacity0() : defaultOpacity1());
           final int
            n = numberOfVertexPairs(), s = start,                               // Number of vertex pairs, start position vertex array
            am = -1>>>8, om = i(o) << 24,                                       // Opacity converted to alpha and shifted into position
            c1 = (C1() & am) | om, c2 = (C2() & am) | om;                       // Colour with opacity set
          for(int i = 0; i < n; i++)                                            // The ring vertices in pairs
           {final int j = 2*(s + i);                                            // Position in colours array
            colours[j] = c1; colours[j+1] = c2;                                 // Colour at each vertex
           }
         }
        void setSelectedColours()                                               // Set selected colours from unselected colours
         {selected = selectedItem != null &&                                    // An item has been selected
          values.n == selectedItem.item.values.n;                               // and the name matches - fast compare OK because of use of intern() on name
         }
        int loadVertex(int s, float X, float Y, float x, float y)               // Load a vertex from a vector(x,y) from the origin at(X, Y)
         {vertices[s+0] = X + x;                                                // X component of vertex
          vertices[s+1] = Y + y;                                                // Y component of vertex
          return s+2;                                                           // New position in vertex array
         }
        int rotateAndLoadVertex                                                 // Load a vertex from a vector(x,y) relative to the origin(X,Y)
         (int s, float X, float Y, float x, float y, float sA, float cA)        // Rotated by an angle with the specified sine and cosine
         {return loadVertex(s, X, Y, x*cA-y*sA, x*sA+y*cA);
         }
        int loadVertexPair(int s, float x, float y, float 𝘅, float 𝘆)           // Load a pair of vertices(x,y) and (𝘅,𝘆) starting at slot(s)
         {vertices[s+0] = x; vertices[s+1] = y;                                 // First vertex
          vertices[s+2] = 𝘅; vertices[s+3] = 𝘆;                                 // Second  vertex
          return s+4;                                                           // New position in vertex array
         }
        SelectedItem containsPoint(final float x, final float y)                // Whether the point (x,y) is contained in any of the triangles constituting this item's lowest layer
         {final int S = startVertexPair * coordsPerVertexPair,                  // Start of first triangle
                    T = endVertexPair   * coordsPerVertexPair - 6;              // Start of last triangle
          final float[]p = vertices;                                            // Shorten name
          final int r = 2*tracingBlocks();                                      // Number of tracing rotation blocks
          final int m = mirrorBlocks();                                         // Number of vertex mirror blocks
          final int n = mirrorBlockSize*coordsPerVertexPair / 2;                // Number of entries in vertex array for base unmirrored section in lower layer
          for(int k = r; k > 0; --k)                                            // Rotation block - examine all blocks so that we can select by touching a reflection
           {for(int j = k*m-1; j >= 0; --j)                                     // Reflection block
             {final int b = j * n, s = b + S, t = b + T;                        // Block start and end for this item in lower layer
              for(int i = s; i <= t; i += 2)                                    // Each triangle in the strip in the item in the lower layer in the block
               {final int 𝗶 = i;                                                // Finalize to optimize
                 if (inside(x, y, p[𝗶+0],p[𝗶+1],p[𝗶+2],p[𝗶+3],p[𝗶+4],p[𝗶+5]))   // See if the point is inside the current triangle
                 {return new SelectedItem(this, j);                             // Found a containing triangle in block j
                 }
               }
             }
           }
          return null;                                                          // Nowhere in this item
         }
        void mR(float m) {final float t = outerThickness();  r = m-t; R = m+t;} // Given the middle radius sets the inner and outer background radius using outerThickness()
        void mR()        {mR(baseRadius());}                                    // Use the default radius
        void updateItemPosition(boolean start, int depth, float x, float y)     // Update item position - start of interaction, reflection depth, touch position
         {values.updateValuesWithPosition(start, depth, x, y);
         }
       } // Item
//------------------------------------------------------------------------------
// 𝖁arious geometric shapes
//------------------------------------------------------------------------------
      abstract class Arc extends Item                                           // Arc with centre at x,y inner radius r, outer radius R, start angle a, sweep angle A
       {Arc() {}
        Arc(Values Values) {values = Values;}
        float r1()  {return rR() - innerThickness();}                           // Upper layer inner radius of line
        float R1()  {return rR() + innerThickness();}                           // Upper layer outer radius of line
        int numberOfVertexPairs() {return 65;}                                  // The number of vertex pairs this item uses
        void addVertices(final int start)                                       // Add the vertices starting at this position in the vertices array
         {final int n = numberOfVertexPairs();
          final float x = x(), y = y(), a = a(), A = A(), r = r(), R = R();
          for(int i = 0; i <= n; i++)                                           // The ring vertices in pairs
           {final int j = coordsPerVertexPair * (start + i);                    // Position in vertices array
            final float 𝝷 = a + A*i/(n-1), s = sd(𝝷), c = cd(𝝷);                // Angles, sin, cos thereof
            vertices[j+0] = x + r * c; vertices[j+1] = y + r * s;
            vertices[j+2] = x + R * c; vertices[j+3] = y + R * s;
           }
         }
       } // Arc
      abstract class Angle extends Arc                                          // Angle with centre at x,y radius R, start angle a and sweep angle A
       {Angle() {}
        Angle(Values Values) {values = Values;}
        float r0() {return 0;}                                                  // A ring with inner radius == 0 in the lower layer
        float r1() {return 0;}                                                  // A ring with inner radius == 0 in the upper layer
       } // Angle
      abstract class Ring extends Arc                                           // Ring with centre at x,y inner radius r, outer radius R
       {Ring() {}
        Ring(Values Values) {values = Values;}
        float A() {return 360;}                                                 // A ring has sweep angle of 2𝝿
       } // Ring
      abstract class Circle extends Ring                                        // Circle with centre at x,y radius R
       {Circle() {}
        Circle(Values Values) {values = Values;}
        float r0() {return 0;}                                                  // A ring with inner radius == 0 in the lower layer
        float r1() {return 0;}                                                  // A ring with inner radius == 0 in the upper layer
       } // Circle
      abstract class SemiCircle extends Circle                                  // Semi Circle with centre at x,y radius R, rotation a
       {SemiCircle() {}
        SemiCircle(Values Values) {values = Values;}
        float A() {return 180;}                                                 // Sweep angle of a semi circle is 𝝿
       } // SemiCircle
      abstract class SemiRing extends Arc                                       // Arc with centre at x,y inner radius r, outer radius R, start angle a, sweep angle A
       {SemiRing() {}
        SemiRing(Values Values) {values = Values;}
        float  A(){return -180;}                                                // Arc angle of semi-circle is 𝝿
        final int nArc = 64, nDiameter = 2;                                     // The number of vertex pairs this item uses for each component
        int numberOfVertexPairs() {return nArc+nDiameter;}                      // The number of vertex pairs this item uses in total
        void addVertices(final int start)                                       // Add the vertices starting at this position in the vertices array
         {final int n = numberOfVertexPairs();
          final float x = x(), y = y(), a = a(), r = r(), R = R(),
            𝗮 = sd(a),    𝕒 = cd(a),                                            // Rotation of item: sine and cosine - used to rotate vectors
            d = f(Math.abs(R-r)/2f),                                            // Thickness of arc
            𝘅 = os(d, r), 𝕩 = os(d, R),                                         // X from centre to inner corner, outer corner
            𝝰 = at(R, d), 𝝱 = at(r, d),                                         // Increase in sweep of outer arc, decrease in sweep of inner arc
            A = 2*𝝰-A();                                                        // Sweep angle of outer arc

          int s = coordsPerVertexPair * start;                                  // Start position in vertices array
          s = rotateAndLoadVertex(s, x, y, -𝘅, -d, 𝗮, 𝕒);                       // Base upper left
          s = rotateAndLoadVertex(s, x, y, -𝕩, +d, 𝗮, 𝕒);                       // Base lower left
          s = rotateAndLoadVertex(s, x, y, +𝘅, -d, 𝗮, 𝕒);                       // Base upper right
          s = rotateAndLoadVertex(s, x, y, +𝕩, +d, 𝗮, 𝕒);                       // Base lower right

          for(int i = 1; i <= nArc; i++)                                        // The ring vertices in pairs
           {final float 𝝷 = -𝝰 + A*i/nArc, sin = sd(a-𝝷), cos = cd(a-𝝷);        // Angles, sin, cos thereof of radius to outer arc
            s = 𝝷 > 𝝱 && 𝝷 < 180 - 𝝱 ?                                          // At a corner?
              loadVertex         (s, x, y, r * cos, r * sin) :                   // Not at a corner
              rotateAndLoadVertex(s, x, y, 𝘅 * (𝝷 < 𝝱 ? +1 : -1), -d, 𝗮, 𝕒);    // At a corner
            s = loadVertex       (s, x, y, R * cos, R * sin);                   // Outer edge
           }
         }
       } // SemiRing
      abstract class Rectangle extends Item                                     // Rectangle centred at x,y width r, height R, angle a
       {Rectangle() {}
        Rectangle(Values Values) {values = Values;}
//      final int nVertexPairsPerSide = 64;                                     // Number of vertex pairs per side
        final int nVertexPairsPerSide = 2;                                      // Number of vertex pairs per side
        int numberOfVertexPairs() {return 2*nVertexPairsPerSide+2;}             // The number of vertex pairs this item uses
        void addVertices(final int start)                                       // Add the vertices starting at this position in the vertices array
         {final float a = -a(),      𝗮 = a + 90, r = r(), R = R(),
                      x = x(),       y = y(),
                      𝘅 = r * cd(a), 𝘆 = r * sd(a),                             // Rotated sides
                      𝕩 = R * cd(𝗮), 𝕪 = R * sd(𝗮);
          final int n = nVertexPairsPerSide;                                    // Shorten name
          int s = coordsPerVertexPair * start;                                  // Starting vertex
          for(int i = 0; i < n; ++i)                                            // Area below major axis
           {final float dx = 2*𝘅*i / (n-1), dy = 2*𝘆*i / (n-1);                 // Position along major axis from left hand end
            s = loadVertex(s, x, y, -𝘅 + dx,      𝘆 - dy);                      // Major axis
            s = loadVertex(s, x, y, -𝘅 + dx + 𝕩,  𝘆 - dy - 𝕪);                  // Lower edge
           }
          s   = loadVertex(s, x, y,  𝘅, -𝘆);                                    // Position on major axis ready to do upper edge Lower edge
          s   = loadVertex(s, x, y,  𝘅, -𝘆);                                    // Swap colours
          for(int i = 0; i < n; ++i)                                            // Area above major axis
           {final float dx = 2*𝘅*i / (n-1), dy = 2*𝘆*i / (n-1);                 // Position along major axis from right hand end
            s = loadVertex(s, x, y,  𝘅 - dx,     -𝘆 + dy);                      // Major axis
            s = loadVertex(s, x, y,  𝘅 - dx - 𝕩, -𝘆 + dy + 𝕪);                  // Upper edge
           }
          s   = loadVertex(s, x, y, -𝘅, +𝘆);                                    // Position on major axis ready to do upper edge Lower edge
          s   = loadVertex(s, x, y, -𝘅, +𝘆);                                    // Swap colours
         }
       } // Rectangle
      abstract class Square extends Rectangle                                   // Square centred at x,y width=height=2*R, angle a
       {Square() {}
        Square(Values Values) {values = Values;}
        float r0() {return super.R0();}
        float r1() {return super.R1();}
       } // Square
      abstract class Diameter extends Item                                      // Diameter with centre at x,y radius r, angle a with the necessary accessories to turn it into a radius
       {final Item diameterItem = this;
        protected boolean radius() {return false;}                              // Override to true if constructing a radius rather than a diameter
        Diameter() {}
        Diameter(Values Values) {values = Values;}
         {new Rectangle(this)                                                   // Rectangle for main bar
           {float R0() {return outerThickness();}
            float R1() {return innerThickness();}
            int   c1() {return diameterItem.c1();}
            int   c2() {return diameterItem.c2();}
            Integer C() {return diameterItem.C();}
            float r0() {return super.r0() / (radius() ? 2f : 1f);}              // Why super and not diameterItem ???
            float r1() {return super.r1() / (radius() ? 2f : 1f);}
            float x()  {return super.x() + (radius() ? super.r0()/2f * cd(super.a()) : 0f);}
            float y()  {return super.y() + (radius() ? super.r0()/2f * sd(super.a()) : 0f);}
            boolean reflect() {return diameterItem.reflect();}                  // Whether this diameter is being reflected or not
           };
          placeSemiCircle(-1);                                                  // Left end semi-circle
          placeSemiCircle(+1);                                                  // Right end semi-circle
         }
        private SemiCircle placeSemiCircle(final int sign)
         {return new SemiCircle(this)                                           // Circle at one end or the other
           {float R0() {return outerThickness();}
            float R1() {return innerThickness();}
            int   c1() {return diameterItem.c1();}
            int   c2() {return diameterItem.c2();}
            Integer C() {return diameterItem.C();}
            float  x() {return diameterItem.x() + left(sign) * diameterItem.r() * cd(diameterItem.a());}
            float  y() {return diameterItem.y() + left(sign) * diameterItem.r() * sd(diameterItem.a());}
            float  a() {return diameterItem.a() - sign * 90;}
            boolean reflect() {return diameterItem.reflect();}                  // Whether this diameter is being reflected or not
           };
         }
        private   int left(int sign) {return sign < 0 && radius() ? 0 : sign;}  // Places left hand end of radius
        void bp(final float x, final float y, final float 𝘅, final float 𝘆)     // Diameter between point(x,y), and point(𝘅, 𝘆) with centre half way along
         {final float 𝕩 = 𝘅 - x, 𝕪 = 𝘆 - y, d = d(𝕩, 𝕪);
          values.x = x + 𝕩 / 2;
          values.y = y + 𝕪 / 2;
          values.r = d / 2;
          values.a = at(𝕩, 𝕪);
         }
       } // Diameter
      abstract class Radius extends Diameter                                    // Radius with centre at x,y radius r
       {Radius() {}
        Radius(Values Values) {values = Values;}
        protected boolean radius() {return true;}                               // Override to true of constructing a radius rather than a diameter
       } // Radius
      abstract class Cross extends Item                                         // Cross with centre at x,y inner radius r, outer radius R
       {final Item crossItem = this;
        Cross() {}
        Cross(Values Values) {values = Values;}
         {new Rectangle(this)                                                   // Unswapped rectangle
           {float R1() {return innerThickness();}
           };
          new Rectangle(this)                                                   // Swapped rectangle
           {float R1() {return innerThickness();}
            float a()  {return crossItem.a()+90;}
           };
         }
       } // Cross
      abstract class RightAngle extends Item                                    // Right angle
       {RightAngle()
         {r = 2.5f*(outerThickness() - 1.5f*innerThickness());                  // Default inner size
          R = 2.5f*(outerThickness() + 1.5f*innerThickness());                  // Default outer size
         }
        RightAngle(Values Values) {values = Values;}
        float r1() {return rR() - innerThickness();}                            // Upper layer inner radius of line
        float R1() {return rR() + innerThickness();}                            // Upper layer outer radius of line
        int numberOfVertexPairs() {return 3;}                                   // The number of vertex pairs this item uses
        void addVertices(final int start)                                       // Add the vertices starting at this position in the vertices array
         {final float a = a(),    𝗮 = a + 90,
                      r = r(),    R = R(),
                      c = cd(a),  s = sd(a),
                      𝘅 = r * c,  𝘆 = r * s,
                      𝕩 = -𝘆,     𝕪 = 𝘅,
                      𝗫 = R * c,  𝗬 = R * s,
                      𝕏 = -𝗬,     𝕐 = 𝗫;
          final int j = coordsPerVertexPair * start;                            // Position in vertices array
          vertices[j+ 0] = x + 𝘅;     vertices[j+ 1] = y + 𝘆;
          vertices[j+ 2] = x + 𝗫;     vertices[j+ 3] = y + 𝗬;
          vertices[j+ 4] = x + 𝘅 + 𝕩; vertices[j+ 5] = y + 𝘆 + 𝕪;
          vertices[j+ 6] = x + 𝗫 + 𝕏; vertices[j+ 7] = y + 𝗬 + 𝕐;
          vertices[j+ 8] = x + 𝕩;     vertices[j+ 9] = y + 𝕪;
          vertices[j+10] = x + 𝕏;     vertices[j+11] = y + 𝕐;
         }
        float dd() {return 2*outerThickness();}                                 // Default dimension for right angle
       } // RightAngle
      void rightAngles(final float 𝘅, final float 𝘆, final float 𝗮,             // Draw right angles at point(cx,cy) with angle 𝗮
        final int 𝗰1,  final int 𝗰2,  final float c, final int mask)            // with background colours 𝗰1, 𝗰2, relative angular closeness c to right angles masked by: 0b....
       {if (c >= 1) return;                                                     // Angle opacity is zero so skip draw
        for(int i = 0; i < 4; ++i)                                              // Each right angle
         {if ((mask & (1<<i)) > 0)                                              // Mask of right angles to be drawn
           {final int 𝗶 = i;                                                    // Angle of right angle
            new RightAngle() {{x = 𝘅; y = 𝘆; a = 𝗶*90+𝗮; c1 = 𝗰1; c2 = 𝗰2; C = 𝝺w; o = 1 - sq(c);}};
           }
         }
       }
      abstract class Triangle extends Item                                      // Right angle
       {final Item triangleItem = this;
        Triangle(final float X, final float Y, final float 𝗫, final float 𝗬, final float 𝕏, final float 𝕐)
         {new Diameter()
          {{bp(X, Y, 𝗫, 𝗬);}
            int    c1() {return triangleItem.values.c1;}
            int    c2() {return triangleItem.values.c2;}
            Integer C() {return triangleItem.values.C;}
          };
          new Diameter()
          {{bp(𝗫, 𝗬, 𝕏, 𝕐);}
            int    c1() {return triangleItem.values.c1;}
            int    c2() {return triangleItem.values.c2;}
            Integer C() {return triangleItem.values.C;}
          };
          new Diameter()
          {{bp(𝕏, 𝕐, X, Y);}
            int    c1() {return triangleItem.values.c1;}
            int    c2() {return triangleItem.values.c2;}
            Integer C() {return triangleItem.values.C;}
           };
         }
       } // Triangle
      abstract class PolyArea extends Item                                      // Polygonal Area represented as one triangle per side
       {final float[]xy;                                                        // Coordinates array
        final float x, y;                                                       // Centre coordinates
        PolyArea(final float...XY)                                              // Array of coordinates of vertices
         {xy = XY; x = centre(0); y = centre(1);
         }
        int numberOfVertexPairs() {return 1+xy.length/2;}                       // The number of vertex pairs this item uses
        float centre(final int offset)                                          // Centre coordinate in either X(0) or Y(1)
         {final int n = xy.length;
          float c = xy[offset];                                                 // Coordinate sum
          for(int i = 2+offset; i < n; i += 2) c = (c + xy[i]) / 2;             // Sum like coordinates
          return c;                                                             // Average coordinate
         }
        void addVertices(final int start)                                       // Add the vertices starting at this position in the vertices array
         {final int n = numberOfVertexPairs() - 1;                              // Number of vertex pairs
          int s = coordsPerVertexPair * start;                                  // Starting vertex
          for(int i = 0; i < n; ++i)                                            // Each vertex pair except the final one
           {s = loadVertexPair(s, x, y, xy[2*i], xy[2*i+1]);                    // Add vertex
           }
          s = loadVertexPair  (s, x, y, xy[0], xy[1]);                          // Final closing vertex
         }
       } // PolyArea
      abstract class Tab extends Item                                           // Angle tab with centre at x,y radius R, start angle a and sweep angle A with both interiors and exterior angles shown
       {final Item tabItem = this;                                              // Tabs are not reflected or traced
        Tab() {}
         {new Angle(this)                                                       // Specified angle
           {float A() {return nd(tabItem.A());}
            int  c1() {return tabItem.c1();}
            int  c2() {return tabItem.c1();}
            boolean reflect() {return false; /*tabItem.reflect();*/}
            boolean trace()   {return false;}
           };
          new Angle(this)                                                       // Obverse angle
           {float A() {return nd(tabItem.A())-360;}
            int  c1() {return tabItem.c2();}
            int  c2() {return tabItem.c2();}
            boolean reflect() {return false; /*tabItem.reflect();*/}
            boolean trace()   {return false;}
           };
         }
       } // Tab
//------------------------------------------------------------------------------
// 𝕾pecial purpose items - tracing controllers
//------------------------------------------------------------------------------
      void offerToRotateTracing()                                               // Tracing rotation controller in standard position rotating about centre with full opacity
       {final float r = baseRadius() / 2;
        offerToRotateTracing(cx + r, cy + r, cx, cy, 1);
       }
      void offerToRotateTracing(float x, float y)                               // Tracing controller at point(x,y) rotating about(cx, cy) with full opacity
       {offerToRotateTracing(x, y, cx, cy, 1);
       }
      void offerToRotateTracing(final float 𝘅, final float 𝘆,                   // Draw the tracing controller: a circle at point(𝘅,𝘆) which when dragged rotates the tracing about(X,Y) with the specified opacity
        final float X, final float Y, final float opacity)
       {new Circle()                                                            // Draw the controller
        {{n = "TracingRotationController";                                      // Unique name so that this item can be selected
          𝞈 = 𝞈Rotation;                                                        // Rotation tracker to use
          tracingRotationCentreX = X; tracingRotationCentreY = Y;               // Centre of rotation
          tracingRotationOpacity = opacity;                                     // Opacity of the drawing
          x = 𝘅; y = 𝘆;                                                         // Position of the controller
          𝕩 = f(X, cx);    𝕪 = f(Y, cy);                                        // Centre of rotation - this is the centre around which rotation is measured - but tracingRotationCentreX/Y is the centre around which the drawing is actually rotated
          R = rotationTracingControllerRadius();                                // Size of the controller
          c1 = 𝝺sb; c2 = 𝝺sc;                                                   // The colours of the controller
          reflect = false;                                                      // Do not reflect the controller
        }};
       }
      float rotationTracingControllerRadius() {return 4*outerThickness();}      // Size of the tracing rotation controller

      void pointerPressed () {}                                                 // Pointer pressed
      void pointerDragged () {}                                                 // Pointer dragged
      void pointerReleased() {}                                                 // Pointer released

      void underlay() {}                                                        // Highlights drawn under the drawing
      void overlay () {}                                                        // Highlights drawn over the drawing
//------------------------------------------------------------------------------
// 𝕽emember the position of lines and circles drawn during the overlay phase so
// that we can find the colour of the one the user is touching and then adjust
// the opacity of the other lines to make the touched line clearer. As speed is
// desirable and there cannot be so many lines and circles on the screen for
// the user to touch: use a fixed area of no great size as it will be searched
// sequentially.
// I spent 𝕮hristmas 𝕰ve writing this, but in fact dashed lines turned out to be
// much more effective visually and much faster to implement - so there.
//------------------------------------------------------------------------------
//    class ColourAtTouch                                                       // Save the positions of some lines and circles and their associated colour so that we can retrieve the color of a specific point at a later time
//     {final int 𝗻;                                                            // Number of objects to save
//      final boolean[]circleNotLine;                                           // True - circle, false - line
//      final float[]x,y,𝘅,𝘆,𝗿;                                                 // Line start/Circle centre, line end, radius
//      final int[]𝗰;                                                           // Colour associated with line or circle
//      int p = 0;                                                              // Position in save area
//      ColourAtTouch(final int n)                                              // Size of save area
//       {𝗻 = n;                                                                // Save area size
//        circleNotLine = new boolean[𝗻];  𝗰 = new int[𝗻];                      // Allocate save areas
//        x = new float[𝗻]; y = new float[𝗻]; 𝘅 = new float[𝗻]; 𝘆 = new float[𝗻];
//        𝗿 = 𝘅;                                                                // Reuse unused space
//       }
//      void line(float X, float Y, float 𝗫, float 𝗬, int C)                    // Line definition
//       {x[p] = X; y[p] = Y; 𝘅[p] = 𝗫; 𝘆[p] = 𝗬;                               // Start and end coordinates
//        𝗰[p] = C; if (p < 𝗻-1) ++p;                                           // Save colour of line and move up if room
//       }
//      void circle(float X, float Y, float R, int C)                           // Circle definition
//       {x[p] = X; y[p] = Y; 𝗿[p] = R;                                         // Centre coordinates and radius
//        circleNotLine[p] = true;                                              // Mark as circle
//        𝗰[p] = C; if (p < 𝗻-1) ++p;                                           // Save colour of circle and move up if room
//       }
//      int touchedColour(float X, float Y)                                     // Find the uppermost colour the user is touching
//       {for(int I = p - 1; I >= 0; --I)                                       // Go backwards to get latest element drawn
//         {final int i = I;                                                    // Finalize
//          if (circleNotLine[i])                                               // Circle
//           {final float d = d(X - x[i], Y - y[i]), r = 𝗿[i];                  // Distance from centre
//            if (d > 0.99*r && d < 1.01*r) return 𝗰[i];                        // Touching the circle's circumference!
//           }
//          else                                                                // Line
//           {final float d = d(X-x[i], Y-y[i]) + d(𝘅[i]-X, 𝘆[i]-Y),            // Distance from each end
//              𝗱 = d(𝘅[i]-x[i], 𝘆[i]-y[i]);                                    // Length of line segment
//            if (d >= 0.999*𝗱 && d < 1.02*𝗱) return 𝗰[i];                      // On line by Schartz inequality
//           }
//         }
//        return 0;                                                             // Unlikely to draw with this colour
//       }
//     } // ColourAtTouch
//
//    final ColourAtTouch ColourAtTouch = ColourAtTouchWanted() == 0 ? null :
//      new ColourAtTouch(ColourAtTouchWanted());                               // Allocate only if facility needed
//
//    void drawLine(final RectF r) {drawLine(r.left, r.top, r.right, r.bottom);}// These draw routines save their colours so we can question what colour the user is touching
//    void drawLine(final float x, final float y, final float 𝘅, final float 𝘆) // Draw line and save colour
//     {if (ColourAtTouch != null) ColourAtTouch.line(x, y, 𝘅, 𝘆, paint.getColor());
//      canvas.drawLine(x, y, 𝘅, 𝘆, paint);
//     }
//    void drawLines(final float[]lines)                                        // Draw a set of lines and save colour
//     {if (ColourAtTouch != null)
//       {final int c = paint.getColor(), n = lines.length;
//        final float[]l = lines;                                               // Shorten name
//        for(int p = 0; p < n; p += 4)                                         // Each line
//         {final int i = p;
//          ColourAtTouch.line(l[i+0], l[i+1], l[i+2], l[i+3], c);              // Save line details
//         }
//       }
//      canvas.drawLines(lines, paint);
//     }
//    void drawCircle(final float x, final float y, final float 𝗿)              // Draw circle and save colour
//     {if (ColourAtTouch != null) ColourAtTouch.circle(x, y, 𝗿, paint.getColor());
//      canvas.drawCircle(x, y, 𝗿, paint);
//     }
     } // Drawing
//------------------------------------------------------------------------------
// 𝖀seful routines for creating drawings
//------------------------------------------------------------------------------
    float nearZero(final float a)                                               // Nearness of an angle to zero, when nearness is zero, the angles are the same, nearness is 1 at angles close enough, nearness is always positive
     {return (1f - cd(a))/ sdCloseEnoughAngle;
     }
    float nearAngle(final float a, final float 𝗮)                               // Nearness of two angles
     {return SQ((a(a - 𝗮) % 360) / closeEnoughAngle);                           // Tends to zero as at maximum nearness - squaring gives us more on time at the top
     }
    float nearHorizontal(final float a)                                         // Nearness of an angle to the horizontal
     {final float x = a(a) % 180;
      return SQ((x > 90 ? 180 - x : x) / closeEnoughAngle);                     // Square makes a flatter top
     }
    float nearRightAngles(final float 𝗮)                                        // Nearness to right angles for angle 𝗮
     {return 4*m(a(a(sd(𝗮     ))-1), a(a(cd(𝗮     ))-1)) / sdCloseEnoughAngle;  // Fraction away from right angles
     }
    float nearHexAngles  (final float 𝗮)                                        // Nearness to hexagonal angles for angle 𝗮
     {return 1*m(a(a(sd(𝗮*1.5f))-1), a(a(cd(𝗮*1.5f))-1)) / sdCloseEnoughAngle;  // Fraction away from hexagonal angles
     }
    float nearAngles(final float 𝗮, int n)                                      // Nearness to an angle of a circle divided into n pieces
     {final float d = 360f/n, m = a(𝗮) % d, 𝗻 = m > d / 2 ? d - m : m,          // % sign of result depends only on sign of dividend
        f =  𝗻 / closeEnoughAngle;                                              // Fraction of closeEnoughAngle away from n angle
      return f * f;                                                             // Approaches 0 on a parabola at point of closest encounter
     }
    boolean notHorizontal(final float 𝗮) {return a(sd(𝗮)) > 0.25;}              // Not horizontal
    int quadrantMask(final float a, final int m1, final int m2,                 // Choose a mask based on quadrant
                                    final int m3, final int m4)
      {return a < 90 ? m1 :  a < 180 ? m2 :  a < 270 ? m3 : m4;
      }
//------------------------------------------------------------------------------
// 𝖀seful functions from Vectors2D - see: metacpan.org/author/PRBRENAN
//------------------------------------------------------------------------------
    final float nearness = 1e-6f;                                               // The length at which digital geometry departs from reality
    boolean intersection(final PointF p,                                        // Intersection of two lines loaded into point(p)
      final float x11, final float y11, final float x12, final float y12,
      final float x21, final float y21, final float x22, final float y22)
     {final float x = x11 - x21, 𝘅 = x12 - x11, 𝕩 = x22 - x21,
                  y = y11 - y21, 𝘆 = y12 - y11, 𝕪 = y22 - y21,
                  d = 𝘆*𝕩 - 𝘅*𝕪;
      if (a(d) < nearness) return false;                                        // Points too close relative to intersection
      final float l = (x*𝕪 - y*𝕩) / d;                                          // Fraction to intersection
      p.set(x11 + l*𝘅, y11 + l*𝘆);                                              // Load result
      return true;                                                              // Result is valid
     }
    Float pointToLine(final float x, final float y,                             // Signed distance from point(x,y) to line through point(𝘅,𝘆) and point(𝕩,𝕪). The sign is positive when the point is to the right of the line when the observer stands at point(𝘅,𝘆) and looks at point(𝕩,𝕪)
      final float 𝘅, final float 𝘆, final float 𝕩, final float 𝕪)
     {final float X = 𝕩 - 𝘅, Y = 𝕪 - 𝘆, d = d(X, Y);                            // Vector along line, length of vector
      if (d < nearness) return null;                                            // Complain if line is not well  defined - a fraction less than 1 is adequate as we measure in pixels
      return (Y*(𝘅-x) + X*(y-𝘆)) / d;                                           // Signed distance from line
     }
    boolean pointToLine(final PointF 𝗽, final float x, final float y,           // Sets point(𝗽) to the vector from point(x,y) to the nearest point on the line through point(𝘅,𝘆) and point(𝕩,𝕪)
      final float 𝘅, final float 𝘆, final float 𝕩, final float 𝕪)
     {final float X = 𝕩 - 𝘅, Y = 𝕪 - 𝘆, d = d(X, Y);                            // Vector along line, length of vector
      if (d < nearness) return false;                                           // Complain if line is not well  defined - a fraction less than 1 is adequate as we measure in pixels
      final float 𝗱 = (Y*(𝘅-x) + X*(y-𝘆)) / (d*d);                              // Signed fraction of line segment length giving distance from line to point
      𝗽.x = Y*𝗱; 𝗽.y = -X*𝗱;                                                    // Vector from point to nearest point on the defined line
      return true;                                                              // Result is valid
     }
    boolean pointOnLine(final PointF 𝗽, final float x, final float y,           // Sets point(𝗽) to the closest position to point(x,y) on the line through point(𝘅,𝘆) and point(𝕩,𝕪)
      final float 𝘅, final float 𝘆, final float 𝕩, final float 𝕪)
     {if (pointToLine(𝗽, x, y, 𝘅, 𝘆, 𝕩, 𝕪))                                     // Vector to line
       {𝗽.x += x; 𝗽.y += y;                                                     // Position on line
        return true;                                                            // Result is valid
       }
      return false;                                                              // Result is not valid
     }
    boolean inside(final float X, final float Y, final float x, final float y,  // Test whether point(X,Y) is inside the triangle formed by points: (x,y), (𝘅, 𝘆), (𝕩, 𝕪), with tests to try to detect outside as quickly as possible
                   final float 𝘅, final float 𝘆, final float 𝕩, final float 𝕪)
     {final boolean n = false;                                                  // Shorten value
      final Float a = pointToLine(X, Y, x, y, 𝘅, 𝘆); if (a==null) return n;     // Direction of point from each line
      final Float b = pointToLine(X, Y, 𝘅, 𝘆, 𝕩, 𝕪); if (b==null) return n;
                                                     if (a*b < 0) return n;     // Different signs means different sides
      final Float c = pointToLine(X, Y, 𝕩, 𝕪, x, y); if (c==null) return n;
      return b*c > 0;                                                           // Different signs means different sides - transitively due to test above
     }
    float angle(float x, float y, float 𝘅, float 𝘆)                             // Angle of line to point(x,y) from point(𝘅,𝘆) measured in degrees clockwise from the x axis returned as a result in the range 0 to 360
     {return nd(at(x - 𝘅, y - 𝘆));
     }
    float angle(float x, float y, float 𝘅, float 𝘆, float 𝕩, float 𝕪)           // Angle of line from point(𝕩,𝕪) through point(𝘅,𝘆) to point(x,y) measured in degrees clockwise from the x axis returned as a result in the range 0 to 360
     {return nd(at(𝕩 - 𝘅, 𝕪 - 𝘆) - at(x - 𝘅, y - 𝘆));
     }
    int nearestVertex(final float X, final float Y,                             // Find the index of the vertex(0,1,2) of the vertex that  point(X,Y) is nearest too
      final float x, final float y,
      final float 𝘅, final float 𝘆, final float 𝕩, final float 𝕪)
     {final float d = d(X, Y, x, y), 𝗱 = d(X, Y, 𝘅, 𝘆), 𝕕 = d(X, Y, 𝕩, 𝕪);      // Distances
      return d <= 𝗱 & d < 𝕕 ? 0 : 𝗱 <= d & 𝗱 < 𝕕 ? 1 : 2;                       // Index of closest vertex ignoring the complicated problems of equidistant vertices
     }
    float heightAndPositionOfAltitude(final PointF 𝗽,                           // Return height of altitude and set 𝗽 to foot of altitude of a triangle with a side from point(x,y) to point(𝘅,𝘆) and angle(a) at point(x,y) and 𝗮 at point(𝘅,𝘆)
      final float x, final float y, final float 𝘅, final float 𝘆,               // Line segment of base of triangle
      final float a, final float 𝗮)                                             // Angles at each end - no checks for bad angles like 0 or 90
     {final float 𝕩 = 𝘅-x, 𝕪 = 𝘆-y, d = d(𝕩, 𝕪), A = td(a), 𝗔 = td(𝗮),          // Vector between vertices, distance between vertices
        h = d * A * 𝗔 / (A + 𝗔), w = h / A;
      𝗽.x = x + w/d * 𝕩;                                                        // Position of foot of altitude
      𝗽.y = y + w/d * 𝕪;
      return h;                                                                 // Height of altitude
     }
    float opposite(float x, float 𝘅) {return 2*x - 𝘅;}                          // Return 𝘅 reflected in x
    void opposite(final PointF p, final PointF m, final PointF r)               // Reflect point(p) through point(m) to set point(r)
     {opposite(p.x, p.y, m, r);
     }
    void opposite(float x, float y, final PointF m, final PointF r)             // Reflect point(x,y) through point(m) to set point(r)
     {r.x = opposite(m.x, x);
      r.y = opposite(m.y, y);
     }
   } // DisplayDrawing
//------------------------------------------------------------------------------
// 𝕮reate a test drawing that tests each type of drawable item
//------------------------------------------------------------------------------
  class TestDrawing extends DisplayDrawing
   {TestDrawing(final Activity Activity) {super(Activity.this);}                // Create display

    Drawing loadDrawing()                                                       // Load the drawing
     {final float fraction  = f((t() - startTime) % period) / period,
        𝗳 = 360 * fraction;
      return new Drawing()                                                      // Create the drawing
      {{final float base = baseRadius(), change = base / 2f;
        new Ring      () {{n = "Ring";       x =  500; y = 550; r = base; R = base + fraction * change; c1 = 𝝺r;                   c2 = 𝝺g;                 C = 𝝺w;             }};
        new Circle    () {{n = "Circle";     x = 1500; y = 550;           R = base - fraction * change; c1 = 𝝺g;                   c2 = 𝝺sb;                                    }};
        new Cross     () {{n = "Cross";      x = 1000; y = 550; r = 400;  R = 100; a = -𝗳;              c1 = Colours.ImperialBlue; c2 = Colours.LightGreen; C = 𝝺r;             }}; // The cross rotates anti-clockwise due to the negative angle
        new Rectangle () {{n = "Rectangle";  x = 1000; y = 900; r = base; R = 100; a = +𝗳;              c1 = 𝝺y;                   c2 = Colours.Cyan;                           }};
        new Diameter  () {{n = "Diameter";   x =  500; y = 550; r = 400;  R =  32; a = +𝗳;              c1 = 𝝺y;                   c2 = Colours.Cyan;       C = Colours.Magenta;}};
        new Arc       () {{n = "Arc";        x = 1600; y = 800; r = 150;  R = 200; A=a= 𝗳;              c1 = Colours.DarkYellow;   c2 = Colours.LightCyan;                      }};
        new SemiRing  () {{n = "SemiRing";   x = 1000; y = 700; r = 400;  R = 550; a = +𝗳;              c1 = 𝝺sg;                  c2 = 𝝺sc;                C = 𝝺w;             }};
        new RightAngle() {{n = "RightAngle"; x = 1600; y = 250; r = 150;  R = 200; a = +𝗳;              c1 = Colours.DarkMagenta;  c2 = 𝝺g;                 C = 𝝺w;             }};
        new Triangle(1500, 300, 1500, 50, 1750, 300) {{n = "Triangle"; c1 = 𝝺sb; c2 = 𝝺sc; C = 𝝺w;}}; // Does not rotate
      }};
     }
   } // TestDrawing
//------------------------------------------------------------------------------
// 𝕯raw a test rectangle - the first thing to be reflected!
//------------------------------------------------------------------------------
  class DrawRectangle extends DisplayDrawing
   {DrawRectangle(final Activity Activity) {super(Activity.this);}              // Create display
    Drawing loadDrawing()                                                       // Load the drawing
     {final float f = f((t() - startTime) % period) / period;                   // Fraction of rotation
      return new Drawing()                                                      // Create the drawing
      {{new Rectangle (){{n="R1"; x=w/4; y=h/4; r=0.2f*w; R=0.2f*h; c1=𝝺sc; c2=𝝺sg;}};
        createMirror(𝞈1, 1, cx, cy,  0, true,true,true,false);                  // Mirrors that do not reflect each other
        createMirror(𝞈2, 2, cx, cy, 90, true,true,true,false);
        offerToRotateTracing();                                                 // Offer to rotate tracing
      }};
     }
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
   } // DrawRectangle
//------------------------------------------------------------------------------
// 𝕯raw a test triangle
//------------------------------------------------------------------------------
  class DrawTriangle extends DisplayDrawing
   {DrawTriangle(final Activity Activity) {super(Activity.this);}
    Drawing loadDrawing()
     {return new Drawing()
       {{new Triangle(100, 100, 1900, 1000, 100, 1000) {{n = "T"; c1 = 𝝺sb; c2 = 𝝺sc; C = 𝝺w;}};
       }};
     }
   } // DrawTriangle
//------------------------------------------------------------------------------
// 𝕯raw a test circle
//------------------------------------------------------------------------------
  class DrawCircle extends DisplayDrawing
   {DrawCircle(final Activity Activity) {super(Activity.this);}
    Drawing loadDrawing()
     {return new Drawing()
       {{new Circle() {{n = "C"; R=baseRadius(); c1 = 𝝺sb; c2 = 𝝺sc;}};
       }};
     }
   } // DrawCircle
//------------------------------------------------------------------------------
// 𝕯raw a test tab
//------------------------------------------------------------------------------
  class DrawTab extends DisplayDrawing
   {DrawTab(final Activity Activity) {super(Activity.this);}
    Drawing loadDrawing()
     {return new Drawing()
       {{new Tab() {{n = "T"; A = +90; a = -45; c1 = 𝝺sb; c2 = 𝝺sc;}};
       }};
     }
   } // DrawTab
//------------------------------------------------------------------------------
// 𝕯raw a test polygonal area
//------------------------------------------------------------------------------
  class DrawPolyArea extends DisplayDrawing
   {DrawPolyArea(final Activity Activity) {super(Activity.this);}
    Drawing loadDrawing()
     {return new Drawing()
       {{new PolyArea(1000, 100, 1900, 1000, 100, 1000) {{n = "T"; c1 = 𝝺sb; c2 = 𝝺sc;}};
       }};
     }
   } // DrawPolyArea
//------------------------------------------------------------------------------
// 𝕬nimated drawing
//------------------------------------------------------------------------------
  abstract class AnimatedDrawing extends DisplayDrawing
   {final 𝝮 fore = new 𝝮("fore"), back = new 𝝮("back"), back2 = new 𝝮("back2"); // Effect of dragging
    float target = 180, targetTime = 0;                                         // Target in degrees, time spent in proximity to target
    double lastDrawTime = T();                                                  // Time of last draw in seconds

    AnimatedDrawing(final Activity Activity){super(Activity.this);}             // Create animated drawing

    void pointerPressed() {fore.speed = 0;}                                     // Pointer pressed stops rotation of upper items

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {}           // Recentre on change of orientation

    float friction    (final float speed) {return -speed;}                      // Friction
    float acceleration(final float angle) {return angle/2;}                     // Acceleration towards target

    Drawing loadDrawing()                                                       // Draw a circle the upper semicircle of which falls to make the lower semicircle.
     {final double T = T();                                                     // Current time in seconds
      final float  t = f(T - lastDrawTime);                                     // Time since last draw in seconds
      if (!pressed)                                                             // Free wheel only if not pressed
       {final int N = 100;                                                      // Number of interpolation steps used to get a more accurate position
        final float dt = t / N;
        for(int i = 0; i < N; ++i)                                              // Interpolate
         {fore.angle += fore.speed * dt;                                        // Update angle from motion
          fore.speed += dt*friction(fore.speed);                                // Reduce velocity by friction
          fore.speed += dt*acceleration(target - fore.angle);                   // Update angular velocity from acceleration
          if (a(fore.angle - target) < closeEnoughAngle) targetTime += dt;      // Track time near target
          if (targetTime > flipTime)
           {target += ff() ? +180 : -180;
            targetTime = 0;
           }
         }
       }
      volumeLevel(cd((target - fore.angle)/2));                                 // Volume tracks angle to target
      lastDrawTime = T;

      return animatedDrawing();                                                 // Create the drawing
     }

    abstract Drawing animatedDrawing();                                         // Override this method to draw the drawing using 'fore' and 'back'
   } // AnimatedDrawing
//------------------------------------------------------------------------------
// 𝕿wo semi circles make a circle - a rotated semi circle stays in the circle
// and divides the circle in half
//------------------------------------------------------------------------------
  class TwoSemiCirclesMakeACircle extends AnimatedDrawing
   {TwoSemiCirclesMakeACircle(final Activity Activity) {super(Activity.this);}  // Create display
    Drawing animatedDrawing()
     {return new Drawing()
      {{new Ring    () {{n = "C";  𝞈 = back; x = cx; y = cy; mR(); c1 = 𝝺sg; c2 = 𝝺sc; C = 𝝺w;}};
        new SemiRing() {{n = "S1"; 𝞈 = back; x = cx; y = cy; mR(); c1 = 𝝺sg; c2 = 𝝺sc; C = 𝝺w; a =            back.angle;}};
        new SemiRing() {{n = "S2"; 𝞈 = fore; x = cx; y = cy; mR(); c1 = 𝝺so; c2 = 𝝺ss; C = 𝝺w; a = fore.angle+back.angle;}};
      }};
     }
   } // TwoSemiCirclesMakeACircle
//------------------------------------------------------------------------------
// 𝕽eflect a line in a mirror to make a snowflake when the original, the mirror
// and the reflection form a hexagon. Show right angles when the original and
// the reflection are collinear.
//------------------------------------------------------------------------------
  class SnowFlake extends DisplayDrawing
   {final 𝝮 back = new 𝝮("back");                                               // Effect of dragging
    SnowFlake(final Activity Activity) {super(Activity.this);}                  // Create display
    Drawing loadDrawing()                                                       // Load the drawing
     {return new Drawing()                                                      // Create the drawing
      {{final float b = baseRadius(), 𝗮 = back.angle-60, n = nearRightAngles(𝗮);// Radius, angle of mirror
        final boolean nh = notHorizontal(𝗮);                                    // Not horizontal
        createMirror(𝞈1, 1, 𝗮);                                                 // Mirror along moving diameter
        final float 𝕙 = nearHexAngles(𝗮);                                       // Nearness to a hexagon
        if (𝕙 < 1 && nh)                                                        // Draw hexagon if close and not horizontal
         {for(int i = 0; i < 3; ++i)                                            // Each start point
           {final int 𝗶 = i, 𝗷 = i + 1;                                         // End point
            new Diameter() {{n = "H1"; bp(cx+b*cd(𝗮+𝗶*60), cy+b*sd(𝗮+𝗶*60), cx+b*cd(𝗮+𝗷*60), cy+b*sd(𝗮+𝗷*60)); c1 = c2 = 0; C = 𝝺w; o = 1-𝕙;}}; // Sides of the hexagon
           }
         }

        if (nh) rightAngles(cx, cy, 𝗮, 𝝺so, 𝝺sc, n, 3);                         // Draw right angles if close and not horizontal
        new Diameter() {{n = "D1";           r = b; x = cx; y = cy;         c1 = 𝝺sg; c2 = 𝝺sc; C = 𝝺w;}};  // Fixed
        new Diameter() {{n = "D2"; 𝞈 = back; r = b; x = cx; y = cy; a = 𝗮; c1 = 𝝺so; c2 = 𝝺ss; C = 𝝺w; o = 1f/2f;}};  // Movable mirror - opacity is lowered because it reflects onto itself all the time
      }};
     }
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
   } // SnowFlake
//------------------------------------------------------------------------------
// 𝕷ines rotate onto themselves twice in one full circle - possibly the back
// tracking should be eliminated
//------------------------------------------------------------------------------
  class LinesRotateTwice extends AnimatedDrawing
   {LinesRotateTwice(final Activity Activity) {super(Activity.this);}           // Create drawing
    Drawing animatedDrawing()
     {return new Drawing()
      {{final float d = M(w, h) / 2f;                                           // Size of diameter
        new Diameter() {{n = "D1"; 𝞈 = back; x = cx; y = cy; r = d; c1 = 𝝺sg; c2 = 𝝺sc; C = 𝝺w; a =            back.angle;}};
        new Diameter() {{n = "D2"; 𝞈 = fore; x = cx; y = cy; r = d; c1 = 𝝺so; c2 = 𝝺sv; C = 𝝺w; a = fore.angle+back.angle;}};
      }};
     }
   } // LinesRotateTwice
//------------------------------------------------------------------------------
// 𝕽otated radius makes a circle - diameter rotated draws the same circle
// with both ends of the diameter
//------------------------------------------------------------------------------
  class RotatedRadiusMakesACircle extends AnimatedDrawing
   {RotatedRadiusMakesACircle(final Activity Activity) {super(Activity.this);}  // Create drawing
    float friction    (final float speed) {return -2*speed;}                    // Friction
    Drawing animatedDrawing()
     {return new Drawing()
      {{final float b = baseRadius();                                           // Standard radius
        new Arc()      {{n = "A1"; 𝞈 = back; x = cx; y = cy; mR();  c1 = 𝝺ss; c2 = 𝝺sC; C = 𝝺w; a =            back.angle; A = fore.angle;}};
        new Arc()      {{n = "A2"; 𝞈 = back; x = cx; y = cy; mR();  c1 = 𝝺ss; c2 = 𝝺sC; C = 𝝺w; a = 180 +      back.angle; A = fore.angle;}};
        new Diameter() {{n = "S1"; 𝞈 = back; x = cx; y = cy; r = b; c1 = 𝝺sg; c2 = 𝝺sc; C = 𝝺w; a =            back.angle;}};
        new Diameter() {{n = "S2"; 𝞈 = fore; x = cx; y = cy; r = b; c1 = 𝝺so; c2 = 𝝺sv; C = 𝝺w; a = fore.angle+back.angle;}};
      }};
     }
   } // RotatedRadiusMakesACircle
//------------------------------------------------------------------------------
// 𝕽eflected radius makes a circle - uses a mirror to reflect a radius in
// different directions to make a circle
//------------------------------------------------------------------------------
  class ReflectedRadiusMakesACircle extends AnimatedDrawing                     // Use of Snowflake picks up back angle
   {ReflectedRadiusMakesACircle(final Activity Activity) {super(Activity.this);}// Create drawing
    Drawing animatedDrawing()
     {final float 𝗮 = fore.angle, n = nearHorizontal(𝗮), z = nearZero(𝗮);       // Radius, angle of mirror, nearness to zero degrees
      return new Drawing()                                                      // Create the drawing
      {{createMirror(𝞈1, 1, 𝗮);                                                 // Mirror along moving diameter

        if (n < 1) rightAngles(cx, cy, 𝗮, 𝝺so, 𝝺sc, n, z < 1 ? 3 : 12);         // Draw right angles if close and not horizontal
        new Ring    () {{n = "C";           mR();   c1 = 𝝺sb; c2 = 𝝺sr; C = 𝝺w;}};
        new Diameter() {{n = "D"; 𝞈 = fore; a = 𝗮; c1 = 𝝺sg; c2 = 𝝺sc; C = 𝝺w;}};
        new Radius  () {{n = "R";           a = 90; c1 = 𝝺sV; c2 = 𝝺so; C = 𝝺w;}};
      }};
     }
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
   } // ReflectedRadiusMakesACircle
//------------------------------------------------------------------------------
// 𝕺pposite angles are equal as demonstrated by rotation
//------------------------------------------------------------------------------
  class OppositeAnglesAreEqual extends RotatedRadiusMakesACircle
   {OppositeAnglesAreEqual(final Activity Activity) {super(Activity.this);}     // Create drawing
    Drawing animatedDrawing()                                                   // Animated drawing
     {return new Drawing()
      {{final float b = baseRadius(),                                           // Standard radius
          𝗯 = 90+fore.angle % 180, 𝕓 = (180 - a(𝗯)) * Math.signum(𝗯),           // Angle of upper diameter
          𝗕 =    back.angle % 180;                                              // Angle of lower diameter
        new Arc()      {{n = "A1"; 𝞈 = back; x = cx; y = cy; mR();  c1 = 𝝺ss; c2 = 𝝺sC; C = 𝝺w; a =    +𝗕;   A = 𝗯;}}; // Small angle
        new Arc()      {{n = "A2"; 𝞈 = back; x = cx; y = cy; mR();  c1 = 𝝺ss; c2 = 𝝺sC; C = 𝝺w; a = 180+𝗕;   A = 𝗯;}};

        new Arc()      {{n = "A3"; 𝞈 = back; x = cx; y = cy; mR();  c1 = 𝝺sv; c2 = 𝝺so; C = 𝝺w; a =     𝗯+𝗕; A = 𝕓;}}; // Large angle
        new Arc()      {{n = "A4"; 𝞈 = back; x = cx; y = cy; mR();  c1 = 𝝺sv; c2 = 𝝺so; C = 𝝺w; a = 180+𝗯+𝗕; A = 𝕓;}};

        new Diameter() {{n = "D1"; 𝞈 = back; x = cx; y = cy; r = b; c1 = 𝝺sg; c2 = 𝝺sc; C = 𝝺w; a = 0+𝗕;}};
        new Diameter() {{n = "D2"; 𝞈 = fore; x = cx; y = cy; r = b; c1 = 𝝺so; c2 = 𝝺sv; C = 𝝺w; a = 𝗯+𝗕;}};
        if (a(cd(𝗯)) < sd(closeEnoughAngle))                                    // Right angles required
         {for(int i = 0; i < 4; ++i)                                            // Each right angle
           {final float 𝗮 = i*90+𝗕;                                             // Angle of right angle
            new RightAngle() {{x = cx; y = cy; a = 𝗮; c1 = 𝝺so; c2 = 𝝺sc; C = 𝝺w;}};
           }
         }
      }};
     }
   } // OppositeAnglesAreEqual
//------------------------------------------------------------------------------
// 𝕯iameters are at right angles to tangents - a right angle triangle rotating
// inside a circle with a mirror on a shorter side of the triangle
//------------------------------------------------------------------------------
  class TangentToDiameter extends RotatedRadiusMakesACircle
   {TangentToDiameter(final Activity Activity) {super(Activity.this);}          // Create drawing
    Drawing animatedDrawing()
     {return new Drawing()
      {{final float b = baseRadius(),                                           // Standard radius
          𝝰 = fore.angle,                                                       // Normalized angle
          𝝱 = 𝝰 > 90 ? 𝝰 - 90 : 𝝰 + 270,                                        // Position of point on circumference
          𝝲 = 𝝱/2 + (𝝱 > 180 ? -45 : 135),                                      // Right right angle
          X = cx + b*cd(𝝱), Y = cy + b*sd(𝝱),                                   // Coordinate position of point on circumference
          c = closeEnoughAngle;                                                 // Range of right angle
        new Ring()     {{n = "R";                                  x = cx; y = cy; mR(); c1 = 𝝺ss; c2 = 𝝺sC; C = 𝝺w;}};
        new Diameter() {{n = "D1";           r = b; a = 90;        x = cx; y = cy;       c1 = 𝝺sg; c2 = 𝝺sc; C = 𝝺w;}};  // Vertical
        new Diameter() {{n = "D2"; 𝞈 = fore; bp(𝕩 = cx, (𝕪 = cy)-b, X, Y); r = b;       c1 = 𝝺so; c2 = 𝝺sv; C = 𝝺w;}};  // Connector to top of display
        new Diameter() {{n = "D3"; 𝞈 = fore; bp(𝕩 = cx, (𝕪 = cy)+b, X, Y); r = b;       c1 = 𝝺sb; c2 = 𝝺sr; C = 𝝺w;}};  // Connector to foot of display
        rightAngles(X, Y, 𝝲, 𝝺sb, 𝝺sr, 0, quadrantMask(𝝱, 1, 2, 8, 1));
        createMirror(𝞈1, 1, X, Y, angle(X, Y, cx, cy + (Y < cy ? +b : -b))).opacity(a(cd(𝝰)));
      }};
     }
   } // TangentToDiameter
  class TangentToDiameterMirrored extends TangentToDiameter
   {TangentToDiameterMirrored(final Activity Activity) {super(Activity.this);}  // Create drawing
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
   } // TangentToDiameterMirrored
//------------------------------------------------------------------------------
// 𝕿wo equilateral triangles reflected in two mirrors make a hexagon
//------------------------------------------------------------------------------
  class TwoEquilateralTrianglesReflectedInTwoMirrorsMakeAHexagon
    extends AnimatedDrawing
   {TwoEquilateralTrianglesReflectedInTwoMirrorsMakeAHexagon                    // Create drawing
     (final Activity Activity)
     {super(Activity.this);
     }
    Drawing animatedDrawing()
     {return new Drawing()
      {{final float b = baseRadius(), 𝝳x = b/2f, 𝝳y = b * sq(3f/4f);            // Standard radius, coordinates of apex of equilateral triangle
        new Triangle(cx-b, cy, cx, cy, cx-𝝳x, cy-𝝳y) {{n = "T1"; c1 = 𝝺sV; c2 = 𝝺so; C = 𝝺y;}};
        if (drawBothTriangles())
        new Triangle(cx-b, cy, cx, cy, cx-𝝳x, cy+𝝳y) {{n = "T2"; c1 = 𝝺so; c2 = 𝝺sV; C = 𝝺y;}};
        new Ring()     {{n = "R1";  mR(); x = cx - b;            c1 = 𝝺sg; c2 = 𝝺sc; C = 𝝺w;}};
        new Arc()      {{n = "R2";  mR(); a = 120; A = 120;      c1 = 𝝺ss; c2 = 𝝺sC; C = 𝝺w;}};
        createMirror(𝞈1, 1, angle(cx, cy, cx-𝝳x, cy-𝝳y));
        createMirror(𝞈1, 2, angle(cx, cy, cx-𝝳x, cy+𝝳y));
      }};
     }
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
    boolean drawBothTriangles() {return true;}                                  // Draw one or both triangles
   } // TwoEquilateralTrianglesReflectedInTwoMirrorsMakeAHexagon
//------------------------------------------------------------------------------
// 𝕻 entagon and other polygons
//------------------------------------------------------------------------------
  class Pentagon extends DisplayDrawing
   {Pentagon(final Activity Activity) {super(Activity.this);}                   // Create display
    final Paint paint = new Paint();
    Integer polygonDrawnAtAngle;                                                // Polygon drawn at angle or null if not drawn
    Drawing loadDrawing()                                                       // Load the drawing
     {return new Drawing()                                                      // Create the drawing
      {{new Ring() {{mR(); c1 = 𝝺sc; c2 = 𝝺sg; C = 𝝺w;}};                       // Circle
        createMirror(1);                                                        // Mirror - x axis
        createMirror(2);                                                        // Mirror - y axis
      }};
     }
    void overlay()
     {polygonDrawnAtAngle = null;
      if (nearAngles(mirrorSeperation(), 2) > 1)                                // Axes are apart
       {drawPolygon( 5, 1,  36, Colours.Red);                                   // 5,10 sided
        drawPolygon( 5, 2,  72, Colours.Green);
        drawPolygon(10, 1, 108, Colours.Yellow);
        drawPolygon(10, 2, 144, Colours.BurntOrange);
        drawPolygon(10, 3, 216, Colours.Magenta);
        drawPolygon(10, 4, 252, Colours.Violet);
        drawPolygon( 5, 1, 288, Colours.ElectricBlue);
        drawPolygon( 5, 2, 324, Colours.ElectricYellow);

        drawPolygon( 8, 1,  45, Colours.Red);                                   // 4,8 sided
        drawPolygon( 4, 1,  90, Colours.Yellow);
        drawPolygon( 8, 2, 135, Colours.Green);
        drawPolygon( 8, 3, 225, Colours.BurntOrange);
        drawPolygon( 4, 1, 270, Colours.ElectricBlue);
        drawPolygon( 8, 1, 315, Colours.ElectricYellow);

        drawPolygon( 6, 1,  60, Colours.Red);                                   // 6 sided
        drawPolygon( 6, 2, 120, Colours.Green);
        drawPolygon( 6, 1, 240, Colours.ElectricBlue);
        drawPolygon( 6, 2, 300, Colours.ElectricYellow);
       }
     }
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
    void drawPolygon(int sides, int step, int atAngle, int colour)              // Draw a polygon with specified number of sides, amount of stellation - 1 for plain polygon, atAngle in the specified colour made suitably translucent
     {drawPolygon(cx, cy, baseRadius(), 𝞈1.angle, sides, step, atAngle, mirrorSeperation(), colour);
     }
    void drawPolygon(float X, float Y, float r, float a,                        // Draw a polygon with centre(X, Y), radius(r), angular offset of vertices(a)
      int sides, int step,                                                      // Number of sides, amount of stellation - 1 for plain polygon
      int atAngle, float angle, int colour)                                     // Draw polygon when angle is near atAngle in the specified colour made suitably translucent
     {final float n = nearAngle(angle, atAngle);                                // Proximity to angle
      if (n > 1) return;                                                        // Not near angle at which polygon is to be drawn
      polygonDrawnAtAngle = atAngle;                                            // record angle of polygon drawn
      final float 𝝳 = 360f / sides;                                             // Interior angle of polygon
      final float[]lines = new float[4*sides], l = lines;                       // Lines buffer
      paint.setColor(colour);                                                   // Colour of polygon
      final int alpha = i(255*(1f-n));                                          // Alpha increases as we close in on the right position
      paint.setAlpha(alpha);
      paint.setStyle(Paint.Style.FILL_AND_STROKE);
      paint.setStrokeWidth(1);                                                  // Circle at each vertex

      for(int i = 0; i < sides; ++i)
       {final int j = 4 * i;                                                    // Position in line buffer
        final float                                                             // Line
          x = X + r*cd(i*𝝳+a),        y = Y + r*sd(i*𝝳+a),                      // Start
          𝘅 = X + r*cd(i*𝝳+a+step*𝝳), 𝘆 = Y + r*sd(i*𝝳+a+step*𝝳);               // End
        l[j+0] = x; l[j+1] = y;                                                 // Load lines buffer
        l[j+2] = 𝘅; l[j+3] = 𝘆;
        canvas.drawCircle(x, y, innerThickness()/2, paint);
       }

      paint.setStrokeWidth(innerThickness()*2);                                 // Standard thickness
      canvas.drawLines(lines, paint);                                           // Paint lines with one subroutine call
     } // drawPolygon
    void pointerReleased()                                                      // Pointer released
     {if (polygonDrawnAtAngle != null)                                          // A polygon was drawn
       {if (selectedItem != null)                                               // As a result of an item being selected
         {final 𝝮 𝞈 = selectedItem.item.values.𝞈;                               // The selected item had a rotation tracker
          if (𝞈1 == 𝞈 || 𝞈2 == 𝞈)                                               // The selected item was a mirror
           {final float 𝝳 = mirrorSeperation() - polygonDrawnAtAngle;           // Offset from nearest interesting angle
            𝞈1.angle -= 𝝳/2; 𝞈2.angle += 𝝳/2;                                   // Spread the offset across both mirrors
           }
         }
       }
     }
   } // Pentagon
//------------------------------------------------------------------------------
// 𝕽eflected diameter
//------------------------------------------------------------------------------
  class ReflectedDiameter extends DisplayDrawing
   {ReflectedDiameter(final Activity Activity) {super(Activity.this);}          // Create display
    float 𝗮 = 45, 𝗯, 𝗰;                                                         // Lesser, greater angle from mirrors to radius
    Drawing loadDrawing()                                                       // Load the drawing
     {return new Drawing()                                                      // Create the drawing
      {{//new Ring()     {{mR();           c1 = 𝝺sb; c2 = 𝝺so; C = 𝝺w;}};       // Circle
        𝗯 = 2*(𝞈1.angle % 90 - 𝗮); 𝗰 = 𝗯 > 0 ? 𝗯-180 : 180+𝗯;
        new Arc()      {{n = "A11"; mR(); c1 = 𝝺so; c2 = 𝝺sb; C = 𝝺w; a = 𝗮;       A = 𝗯;}};
        new Arc()      {{n = "A12"; mR(); c1 = 𝝺sr; c2 = 𝝺ss; C = 𝝺w; a = 𝗮;       A = 𝗰;}};
        new Arc()      {{n = "A21"; mR(); c1 = 𝝺so; c2 = 𝝺sb; C = 𝝺w; a = 𝗮 + 180; A = 𝗯;}};
        new Arc()      {{n = "A22"; mR(); c1 = 𝝺sr; c2 = 𝝺ss; C = 𝝺w; a = 𝗮 + 180; A = 𝗰;}};
        createMirror(𝞈1, 1);                                                    // Mirror
        createMirror(𝞈1, 2);                                                    // Mirror fixed at right angles to first mirror
        new Radius() {{n = "D1"; a = 𝗮; c1 = 𝝺y; c2 = 𝝺y; C = 𝝺w;}};            // Diameter
        rightAngles(cx, cy, 𝞈1.angle, 𝝺sb, 𝝺sr, 0, 15);
      }};
     }
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
    int overlayRequired() {return -1;}                                          // Choose which overlay is required
   } // ReflectedDiameter
//------------------------------------------------------------------------------
// ℑsosceles Triangle
//------------------------------------------------------------------------------
  class IsoscelesTriangle extends DisplayDrawing
   {IsoscelesTriangle(final Activity Activity) {super(Activity.this);}          // Create display
    float radius, dx;                                                           // Radius of circles, offset to centre of first circle from centre of drawing
    Drawing loadDrawing()                                                       // Load the drawing
     {return new Drawing()                                                      // Create the drawing
      {{new Ring()   {{n="R"; 𝞈=𝞈1; mR(); x += (dx = 𝞈1.aldx-(radius = (r+R)/2)); c1=𝝺sb; c2=𝝺so; C=𝝺w;}}; // Left/top circle
        new Radius() {{n="r"; x += dx; r = radius; a = ac(-dx, radius); c1=𝝺sp; c2=𝝺sg; C=Colours.Red;   }}; // Radius of left circle
        createMirror(null, 1);                                                  // Fixed mirror
        createMirror(null, 2);                                                  // Fixed mirror at right angles
        if (a(dx) >= radius) rightAngles(cx, cy, 0, 𝝺sb, 𝝺sr, 0, 15);           // Show right angles if nothing else interesting happening
        offerToRotateTracing();                                                 // Offer to rotate tracing
      }};
     }
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
    void overlay()                                                              // Overlay solution
     {final float dy = sq(SQ(radius)-SQ(dx)), Dx = dx-radius;                   // Radius of circle
      setPaint(𝝺sb);                                                            // Draw half angle rhombus
      canvas.drawLine(cx+Dx, cy, cx, cy - dy, paint);
      canvas.drawLine(cx+Dx, cy, cx, cy + dy, paint);
      canvas.drawLine(cx-Dx, cy, cx, cy - dy, paint);
      canvas.drawLine(cx-Dx, cy, cx, cy + dy, paint);
     }
    void pointerReleased()                                                      // Pointer released - move to special positions if close to them
     {if (selectedItem != null && selectedItem.item.𝞈 == 𝞈Rotation)
       {final float A = ac(-dx, radius);
        sr(A, 45); sr(A, 60); sr(A, 90); sr(A, 120); sr(A, 180);
        sra(45); sra(135); sra(235); sra(315);
        sra(90); sra(180); sra(270); sra(360);
       }
     }
    void sr(final float A, final float a)                                       // Set radius when close to interesting radii
     {if (nearAngle(A, a) < 1)
       {𝞈1.angle = a;
        𝞈1.aldx  = radius-radius*cd(a);
       }
     }
   } // IsoscelesTriangle
//------------------------------------------------------------------------------
// 𝕳alf Angle
//------------------------------------------------------------------------------
// Possibly use reflect=trace=false to reduce the complexity of this drawing
  class HalfAngle extends DisplayDrawing
   {HalfAngle(final Activity Activity) {super(Activity.this);}                  // Create display

    Drawing loadDrawing()                                                       // Load the drawing
     {final float 𝗮 = 𝞈1.angle-90, c𝗮 = cd(𝗮), s𝗮 = sd(𝗮), 𝗿 = 3*m(w,h)/4,      // Angle to be halved, cos, sin, radius
        𝝳 = h / 3, 𝘅 = cx+𝗿*(c𝗮-1)/2, 𝘆 = cy+𝗿*s𝗮/2+𝝳;
      final 𝝮 𝝰 = 𝞈Translation;                                                 // Translation of tracing tracker
      return new Drawing()                                                      // Create the drawing
       {float baseRadius() {return 𝗿;}
         {createMirror(null, 1, 𝘅, 𝘆, 𝗮/2);                                     // Mirror lower left to upper right
          createMirror(null, 2, 𝘅, 𝘆, 𝗮/2+90);                                  // Mirror upper left to lower right
          new Ring()    {{n = "R";  mR();            y += 𝝳; c1=𝝺sc; c2=𝝺sb; C=𝝺w; reflect=false;}}; // Centre circle
          new Ring()    {{n = "S";  mR(); x -= 𝗿;    y += 𝝳; c1=𝝺sg; c2=𝝺so; C=𝝺w; reflect=false;}}; // Left circle
          new Diameter(){{n = "d";  𝞈 = 𝞈1; a = 𝗮;  y += 𝝳; c1=𝝺sp; c2=𝝺sg; C=𝝺r;}};               // Radius
          new Angle()   {{n = "a1"; 𝞈 = 𝝰;  A = 𝗮;   R = 𝗿/3;         y += 𝝳; c1=𝝺sb; c2=𝝺sr;}};    // Centre angle
          new Angle()   {{n = "A1"; 𝞈 = 𝝰;  A = 𝗮;   R = 𝗿/3; x -= 𝗿; y += 𝝳; c1=𝝺sb; c2=𝝺sr;}};    // Left large angle
          new Angle()   {{n = "a2"; 𝞈 = 𝝰;  A = 𝗮/2; R = 𝗿/4; x -= 𝗿; y += 𝝳; c1=𝝺sb; c2=𝝺y; reflect=false;}}; // Left small angle
         }
        void pointerReleased()                                                  // Pointer released - move to special positions if close to them
         {if (selectedItem != null && selectedItem.item.𝞈 == 𝝰)
           {if (d(a(𝝰.aldx)-𝗿, 𝝰.aldy) < outerThickness())                      // Lower corners
             {𝝰.aldx = 𝝰.aldx > 0 ? +𝗿 : -𝗿;
              𝝰.aldy = 0;
             }
            else if (d(𝝰.aldx-c𝗮*𝗿,   s𝗮*𝗿 - 𝝰.aldy) < outerThickness())        // Upper left corner from lower left corner
             {𝝰.aldx = 𝗿*c𝗮;
              𝝰.aldy = 𝗿*s𝗮;
             }
            else if (d(𝗿-c𝗮*𝗿+𝝰.aldx, s𝗮*𝗿 - 𝝰.aldy) < outerThickness())        // Upper left corner from lower right corner
             {𝝰.aldx = 𝗿*c𝗮 - 𝗿;
              𝝰.aldy = 𝗿*s𝗮;
             }
           }
         }
       };
     }
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
   } // HalfAngle
//------------------------------------------------------------------------------
// 𝕬rrow head
//------------------------------------------------------------------------------
  class ArrowHead extends DisplayDrawing
   {ArrowHead(final Activity Activity)                                          // Create display
     {super(Activity.this);
      𝞈2.angle = 135; 𝞈3.angle = 45;                                            // Initialize angles
     }
    Drawing loadDrawing()                                                       // Load the drawing
     {𝞈1.angle = nd(𝞈1.angle);                                                  // Position of apex
      𝞈2.angle = ndr(0, 180, 𝞈2.angle);                                         // Constrain right central radius angular position
      𝞈3.angle = ndr(0, 180, 𝞈3.angle);                                         // Constrain left central radius angular position
      final float 𝗿 = baseRadius(),                                             // Radius of circle
        𝝰 = nd(𝞈1.angle),                                                       // Angular position of circumference angle
        𝝱 = nd(𝞈1.angle + 𝞈2.angle),                                            // Angular position of right radius must be
        𝝲 = nd(𝞈1.angle + 𝞈3.angle + 180),                                      // Angular position of left radius
        ax = cx+𝗿*cd(𝝰), ay = cy+𝗿*sd(𝝰),                                       // Apex coordinates
        rx = cx+𝗿*cd(𝝱), ry = cy+𝗿*sd(𝝱),                                       // Right radius coordinates
        lx = cx+𝗿*cd(𝝲), ly = cy+𝗿*sd(𝝲),                                       // Left radius coordinates
        ra = angle(rx, ry, ax, ay),                                             // Right radius to apex
        rc = angle(rx, ry, cx, cy),                                             // Right radius to centre
        ca = angle(cx, cy, ax, ay),                                             // Centre to apex
        ral = angle(rx, ry, ax, ay, lx, ly),                                    // Right, apex, left
        rcl = angle(rx, ry, cx, cy, lx, ly),                                    // Right centre left
        rac = angle(rx, ry, ax, ay, cx, cy),                                    // Right apex centre
        cal = angle(cx, cy, ax, ay, lx, ly);                                    // Centre apex, left
      return new Drawing()                                                      // Create the drawing
      {{new Ring(){{n = "C";  𝞈=𝞈1; mR(); c1=𝝺sc; c2=𝝺sb; C=𝝺w;}};              // Circle

        new Angle(){{n = "a0"; 𝞈=𝞈1; x=ax; y=ay; A=ral; a=ra; R = 𝗿/1.25f; c1=𝝺sc; c2=𝝺sc;}}; // Whole apex angle
        new Angle(){{n = "c0"; 𝞈=𝞈1;             A=rcl; a=rc; R = 𝗿/1.25f; c1=𝝺sc; c2=𝝺sc;}}; // Whole centre angle

        new Diameter(){{n = "D0"; 𝞈=𝞈1; a=𝝰;              c1=𝝺sg; c2=𝝺so; C=𝝺w;}}; // Centre diameter
        new Diameter(){{n = "DL"; 𝞈=𝞈3; bp(ax,ay, lx,ly); c1=𝝺sp; c2=𝝺sg; C=𝝺w;}}; // Left   diameter
        new Diameter(){{n = "DR"; 𝞈=𝞈2; bp(ax,ay, rx,ry); c1=𝝺sp; c2=𝝺sg; C=𝝺w;}}; // Right  diameter

        new Radius(){{n = "RL"; 𝞈=𝞈2; a=𝝱; c1=𝝺y; c2=𝝺sr; C=𝝺w;}};              // Left  centre radius
        new Radius(){{n = "RR"; 𝞈=𝞈3; a=𝝲; c1=𝝺y; c2=𝝺sr; C=𝝺w;}};              // Right centre radius

        new Radius(){{n = "rL"; 𝞈=𝞈3; a=𝝲; x=ax; y= ay; c1=𝝺y; c2=𝝺sr; C=𝝺y;}}; // Left  apex radius
        new Radius(){{n = "rR"; 𝞈=𝞈2; a=𝝱; x=ax; y= ay; c1=𝝺y; c2=𝝺sr; C=𝝺b;}}; // Right apex radius

        new Radius(){{n = "rl"; 𝞈=𝞈3; x=lx; y=ly; a= 𝝰; c1=𝝺sg; c2=𝝺so; C=𝝺b;}};// Left  parallel radius
        new Radius(){{n = "rR"; 𝞈=𝞈2; x=rx; y=ry; a= 𝝰; c1=𝝺sg; c2=𝝺so; C=𝝺y;}};// Right parallel radius

        new Angle(){{n = "aR"; 𝞈=𝞈1; x=ax; y=ay; A=1*rac; a=ra; R = 𝗿/1.5f; c1=𝝺y;  c2=𝝺y;}};  // Right half of apex angle
        new Angle(){{n = "aL"; 𝞈=𝞈1; x=ax; y=ay; A=1*cal; a=ca; R = 𝗿/1.5f; c1=𝝺sb; c2=𝝺sb;}}; // Left  half of apex angle
        new Angle(){{n = "cR"; 𝞈=𝞈1;             A=2*rac; a=rc; R = 𝗿/1.5f; c1=𝝺y;  c2=𝝺y;}};  // Right half of centre angle
        new Angle(){{n = "cL"; 𝞈=𝞈1;             A=2*cal; a=ca; R = 𝗿/1.5f; c1=𝝺sb; c2=𝝺sb;}}; // Left half of centre angle
       }
        void pointerReleased() {}                                               // Pointer released - move to special positions if close to them
       };
     }
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
   } // ArrowHead
//------------------------------------------------------------------------------
// 𝕿ick - arrow head off to the side
//------------------------------------------------------------------------------
  class Tick extends DisplayDrawing
   {Tick(final Activity Activity)                                               // Create display
     {super(Activity.this);
      𝞈2.angle = 45; 𝞈3.angle = 45;                                             // Initialize angles
     }
    Drawing loadDrawing()                                                       // Load the drawing
     {𝞈1.angle = nd(𝞈1.angle);                                                  // Position of apex
      𝞈2.angle = ndr(0, 180,           𝞈2.angle);                               // Constrain right central radius angular position
      𝞈3.angle = ndr(0, 180-𝞈2.angle, 𝞈3.angle);                                // Constrain left central radius angular position
      final float 𝗿 = baseRadius(),                                             // Radius of circle
        𝝰 = nd(𝞈1.angle),                                                       // Angular position of circumference angle
        𝝱 = nd(𝞈1.angle + 𝞈2.angle),                                            // Angular position of right radius must be
        𝝲 = nd(𝞈1.angle + 𝞈2.angle + 𝞈3.angle),                                 // Angular position of left radius
        ax = cx+𝗿*cd(𝝰), ay = cy+𝗿*sd(𝝰),                                       // Apex coordinates
        rx = cx+𝗿*cd(𝝱), ry = cy+𝗿*sd(𝝱), Rx = cx-𝗿*cd(𝝱), Ry = cy-𝗿*sd(𝝱),     // Right radius coordinates and opposite
        lx = cx+𝗿*cd(𝝲), ly = cy+𝗿*sd(𝝲), Lx = cx-𝗿*cd(𝝲), Ly = cy-𝗿*sd(𝝲),     // Left radius coordinates and opposite
        la = angle(lx, ly, ax, ay),                                             // Left  radius to apex
        ra = angle(rx, ry, ax, ay),                                             // Right radius to apex
        laR = angle(lx, ly, ax, ay, Rx, Ry),                                    // left, apex, Right
        Lcr = angle(Lx, Ly, cx, cy, rx, ry),                                    // Left, centre, right
        ral = angle(rx, ry, ax, ay, lx, ly),                                    // right, apex, left
        raL = angle(rx, ry, ax, ay, Lx, Ly),                                    // right, apex, Left
        rcl = angle(rx, ry, cx, cy, lx, ly),                                    // right centre left
        rcL = angle(rx, ry, cx, cy, Lx, Ly);                                    // right centre Left
      return new Drawing()                                                      // Create the drawing
      {{new Ring()    {{n = "C";  𝞈=𝞈1; mR();                                  c1=𝝺sc; c2=𝝺sb; C=𝝺w;}}; // Circle

        new Angle()   {{n = "ae"; 𝞈=𝞈1; x=ax; y=ay; A=raL; a=ra;    R=𝗿/1.50f; c1=𝝺sc; c2=𝝺sc;}};        // Apex full exterior angle
        new Angle()   {{n = "ce"; 𝞈=𝞈1;             A=rcL; a=𝝱;     R=𝗿/1.25f; c1=𝝺sc; c2=𝝺sc;}};        // Centre full exterior angle

//      new Diameter(){{n = "dc"; 𝞈=𝞈1;                    a=𝝰;                c1=𝝺so; c2=𝝺sp; C=𝝺w;}};  // Central diameter
        new Diameter(){{n = "dL"; 𝞈=𝞈2;                    a=𝝱;                c1=𝝺sg; c2=𝝺so; C=𝝺sc;}}; // Right diameter
        new Diameter(){{n = "dR"; 𝞈=𝞈3;                    a=𝝲;                c1=𝝺sp; c2=𝝺sg; C=𝝺sc;}}; // Left   diameter
        new Diameter(){{n = "ar"; 𝞈=𝞈2; bp(ax,ay, rx,ry);                      c1=𝝺sg; c2=𝝺so; C=𝝺y;}};  // Right short side
        new Diameter(){{n = "al"; 𝞈=𝞈3; bp(ax,ay, lx,ly);                      c1=𝝺sp; c2=𝝺sg; C=𝝺y;}};  // Left  long side

        new Diameter(){{n = "aR"; 𝞈=𝞈2; bp(ax,ay, Rx,Ry);                      c1=𝝺sg; c2=𝝺so; C=𝝺y;}};  // Right long side
        new Diameter(){{n = "aL"; 𝞈=𝞈3; bp(ax,ay, Lx,Ly);                      c1=𝝺sp; c2=𝝺sg; C=𝝺y;}};  // Left  long side

        new Angle()   {{n = "a2"; 𝞈=𝞈1; x=ax; y=ay; A=laR; a=la;    R=𝗿/2;     c1=𝝺sb; c2=𝝺sb;}}; // Apex full interior apex angle
        new Angle()   {{n = "ar"; 𝞈=𝞈1; x=ax; y=ay; A=ral; a=ra;    R=𝗿/2;     c1=𝝺y;  c2=𝝺y;}};  // Apex far right apex angle
        new Angle()   {{n = "al"; 𝞈=𝞈1; x=ax; y=ay; A=ral; a=ra+90; R=𝗿/2;     c1=𝝺y;  c2=𝝺y;}};  // Apex far left  apex angle
        new Angle()   {{n = "ci"; 𝞈=𝞈1;             A=Lcr; a=𝝲;     R=𝗿/1.50f; c1=𝝺sb; c2=𝝺sb;}}; // Centre full interior angle
        new Angle()   {{n = "cl"; 𝞈=𝞈1;             A=rcl; a=𝝱;     R=𝗿/1.50f; c1=𝝺y;  c2=𝝺y;}};  // Centre right angle
        new Angle()   {{n = "cl"; 𝞈=𝞈1;             A=rcl; a=𝝱+180; R=𝗿/1.50f; c1=𝝺y;  c2=𝝺y;}};  // Centre left angle
       }
        void pointerReleased() {}                                               // Pointer released - move to special positions if close to them
        void overlay()                                                          // Overlay solution
         {setPaint(𝝺ws);
          canvas.drawLine(lx,ly, rx,ry, paint);
          canvas.drawLine(Lx,Ly, Rx,Ry, paint);
         }
       };
     }
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
   } // Tick
//------------------------------------------------------------------------------
// 𝕻arallel lines preserve angles
//------------------------------------------------------------------------------
  class ParallelLinesPreserveAngles extends DisplayDrawing
   {ParallelLinesPreserveAngles(final Activity Activity) {super(Activity.this);}// Create display

    Drawing loadDrawing()                                                       // Load the drawing
     {final float 𝗮 = nd(𝞈1.angle+45), c𝗮 = cd(𝗮), s𝗮 = sd(𝗮), 𝗿 = d(w,h)/2,    // Angle to be preserved, cos, sin, radius
        𝝳 = h / 4, 𝝙 = 𝝳 / s𝗮;
      final 𝝮 𝝰 = 𝞈Translation;                                                 // Translation of tracing tracker
      return new Drawing()                                                      // Create the drawing
       {float baseRadius() {return 𝗿;}
         {new Diameter(){{𝞈 = 𝝰; y -= 𝝳; c1=𝝺sg; c2=𝝺sp; C=𝝺w;}};               // Upper parallel line
          new Diameter(){{𝞈 = 𝝰; y += 𝝳; c1=𝝺so; c2=𝝺sb; C=𝝺w;}};               // Lower parallel line
          if ((𝗮 < 340 && 𝗮 > 220) || (𝗮 > 20 && 𝗮 < 160))
           {for(final float 𝝱: new float[]{0, 180})
             {new Angle() {{n = "a1"; 𝞈 = 𝝰; a =   𝝱; A =     𝗮; R = 𝝙; x += 𝝙*c𝗮; y += 𝝳; c1=𝝺sb; c2=𝝺y;}}; // Centre angle
              new Angle() {{n = "A1"; 𝞈 = 𝝰; a =   𝝱; A =     𝗮; R = 𝝙; x -= 𝝙*c𝗮; y -= 𝝳; c1=𝝺sb; c2=𝝺y;}}; // Left large angle

              new Angle() {{n = "a2"; 𝞈 = 𝝰; a = 𝗮+𝝱; A = 180-𝗮; R = 𝝙; x += 𝝙*c𝗮; y += 𝝳; c1=𝝺r; c2=𝝺sg;}}; // Centre angle
              new Angle() {{n = "A2"; 𝞈 = 𝝰; a = 𝗮+𝝱; A = 180-𝗮; R = 𝝙; x -= 𝝙*c𝗮; y -= 𝝳; c1=𝝺r; c2=𝝺sg;}}; // Left large angle
             }
           }
          new Diameter(){{n = "D";  𝞈 = 𝞈1; a  = 𝗮; c1=𝝺so; c2=𝝺sb; C=𝝺r;}};    // Crossing line whose angles are going to be preserved
         }
        void pointerReleased()                                                  // Pointer released - move to special positions if close to them
         {if (selectedItem != null && selectedItem.item.𝞈 == 𝝰)
           {final float
              d = 4*outerThickness(), dx = 2*𝝙*c𝗮, dy = 2 * 𝝳,
              d1 = d(𝝰.aldx + dx, 𝝰.aldy + dy),
              d2 = d(𝝰.aldx - dx, 𝝰.aldy - dy);
            if      (d1 < d) {𝝰.aldx = -dx; 𝝰.aldy = -dy;}                      // Bottom moved to top
            else if (d2 < d) {𝝰.aldx = +dx; 𝝰.aldy = +dy;}                      // Top moved to bottom
            else if (d(𝝰.aldx, 𝝰.aldy) < d) 𝝰.aldx = 𝝰.aldy = 0;                // Return to start
           }
         }
       };
     }
   } // ParallelLinesPreserveAngles
//------------------------------------------------------------------------------
// ℑnterior angles of a triangle make a line
//------------------------------------------------------------------------------
  class InteriorAnglesOfATriangleMakeALine extends DisplayDrawing
   {InteriorAnglesOfATriangleMakeALine(final Activity Activity)                 // Create display
     {super(Activity.this);
     }
    Drawing loadDrawing()                                                       // Load the drawing
     {final float 𝘄 = 5*w/12, 𝕙 = 5*h/12, 𝗵 = 𝕙+𝞈2.aldy, 𝝳 = 𝗵/2,               // Dimensions of quarter rectangle, angle
        x1  = cx-𝘄,        y1 = cy+𝗵,                                           // Lower left corner of triangle
        x2  = cx+𝘄,        y2 = cy+𝗵,                                           // Lower right corner of triangle
        x3  = cx+𝞈1.aldx,  y3 = cy-𝗵,                                           // Apex of triangle
        x13 = (x1+x3)/2,  x23 = (x2+x3)/2,                                      // Between points in x
        a1  = angle(x1, y1, x3, y3), a2 = angle(x2, y2, x3, y3), a3 = a1-a2;    // Angles of triangle
      final float t = outerThickness();
      final boolean diamond = a(𝞈1.aldx)<t, square = a(a(x2-x1)/2-a(y1-y3))<t;  // Square or diamond
      final 𝝮 𝝰 = 𝞈Translation;                                                 // Translation of tracing tracker
      return new Drawing()                                                      // Create the drawing
       { {createMirror(1);                                                      // Mirror along x axis
          if (!drawAngles())
           {new PolyArea(x13,y1-𝗵,  x3,cy, x3,y1) {{n="p1"; 𝞈=𝞈1; c1=𝝺y;  c2=𝝺y;}};  // Yellow
            new PolyArea(x1, y1,   x13,cy, x1,cy) {{n="p1"; 𝞈=𝞈1; c1=𝝺sb; c2=𝝺sb;}}; // Blue

            new PolyArea(x23,y2-𝗵,  x3,cy, x3,y2) {{n="p1"; 𝞈=𝞈1; c1=𝝺sg; c2=𝝺sg;}}; // Green
            new PolyArea(x2, y2,   x23,cy, x2,cy) {{n="p1"; 𝞈=𝞈1; c1=𝝺sr; c2=𝝺sr;}}; // red
           }
          new Diameter() {{𝞈=𝞈2; bp(x1, y1, x2, y2); c1=𝝺sp; c2=𝝺sg; C=𝝺w;}};   // Base
          new Diameter() {{𝞈=𝞈1; bp(x1, y1, x3, y3); c1=𝝺sg; c2=𝝺sr; C=𝝺w;}};   // Left
          new Diameter() {{𝞈=𝞈1; bp(x2, y2, x3, y3); c1=𝝺sr; c2=𝝺sp; C=𝝺w;}};   // Right
          if (drawAngles())
           {final float[]𝕒 = 𝝳 > 0 ?                                            // Angles
              new float[]{180+a1, 180,    a2,     a1,  0}:
              new float[]{    a1, a2, 180+a2, 180+a1, 180+a2};
            final float[]𝔸 = 𝝳 > 0 ?
              new float[]{180-a1,     a2, a3, 180-a1, a2} :
              new float[]{180-a1, 360-a2, a3, 180-a1, 360-a2};
            new Angle() {{𝞈=𝞈1; n="a1"; a=𝕒[0]; A=𝔸[0]; R=𝝳; x=x1; y=y1; c1=𝝺sp; c2=𝝺sg;}}; // Left angle
            new Angle() {{𝞈=𝞈1; n="a2"; a=𝕒[1]; A=𝔸[1]; R=𝝳; x=x2; y=y2; c1=𝝺so; c2=𝝺sb;}}; // Right angle
            new Angle() {{𝞈=𝞈1; n="a3"; a=𝕒[2]; A=𝔸[2]; R=𝝳; x=x3; y=y3; c1=𝝺y;  c2=𝝺y;}};  // Apex angle
            new Angle() {{𝞈=𝞈1; n="A1"; a=𝕒[3]; A=𝔸[3]; R=𝝳; x=x3; y=y3; c1=𝝺sp; c2=𝝺sg;}}; // Left angle
            new Angle() {{𝞈=𝞈1; n="A2"; a=𝕒[4]; A=𝔸[4]; R=𝝳; x=x3; y=y3; c1=𝝺so; c2=𝝺sb;}}; // Right angle
           }
         }
        void overlay()
         {if (diamond)                                                          // Rhombus
           {setPaint(square ? 𝝺b : 𝝺r);                                         // Draw square or rhombus
            final float
              𝘅1 = x3, 𝘆1 = y3+2*𝗵, 𝘅2 = x13, 𝘆2=y3+𝗵,
              𝘅3 = x3, 𝘆3 = y3,     𝘅4 = x23, 𝘆4=y3+𝗵;
            canvas.drawLine(𝘅1,𝘆1, 𝘅2,𝘆2, paint);
            canvas.drawLine(𝘅2,𝘆2, 𝘅3,𝘆3, paint);
            canvas.drawLine(𝘅3,𝘆3, 𝘅4,𝘆4, paint);
            canvas.drawLine(𝘅4,𝘆4, 𝘅1,𝘆1, paint);
           }
         }
        void pointerReleased()                                                  // Engage rhombus or square
         {if (diamond)                                                          // Close to a rhombus
           {𝞈1.aldx = 0;                                                        // Make into a rhombus
            if (square) 𝞈2.aldy = 𝘄-𝕙;                                          // Make square if close to square
           }
         }
       };
     }
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
    boolean drawAngles() {return false;}                                        // Draw angles else draw areas
   } // InteriorAnglesOfATriangleMakeALine
//------------------------------------------------------------------------------
// 𝕿he Theorem of Pythagoras - see tests/Pythagoras.dxf
//------------------------------------------------------------------------------
  class Pythagoras extends DisplayDrawing
   {final int N = 4;                                                            // Number of right angle triangles
    final Matrix[]m = new Matrix[N];                                            // Preallocated transformation matrices to move each right angle triangle
    final int 𝝺os = 𝝺ws, 𝝺cc = 𝝺fL, 𝝺bb = 𝝺fs, 𝝺aa = 𝝺fm;                       // Colour scheme - outer square, cc, bb, aa
    int dragCorner;                                                             // Corner we are dragging on
    String lastDragged;                                                         // Last corner dragged
    float pxy;                                                                  // Fractional progress complete on transition in x and y
    boolean dfx, dfy;                                                           // Transition finished in x and y
    Pythagoras(final Activity Activity)                                         // Create display
     {super(Activity.this);
      𝞈1.angle = 60;
      for(int i = 0; i < N; ++i) m[i] = new Matrix();                           // Preallocate transformation matrices
     }
    Drawing loadDrawing()                                                       // Load the drawing
     {final float 𝗿 = baseRadius(), 𝗱 = 2 * 𝗿, close = 1f/8*𝗱,                  // Half side, full side of outer square
        f = foa(𝞈1.angle, 180),                                                 // Fraction of outer square used to make one side of right angle triangle
        𝗮 = (1f-f) * 𝗱, 𝗯 = 𝗱 - 𝗮, 𝝰 = at(𝗮, 𝗯),                                // Dimensions and angle of right angle
        x1 = cx+𝗿, y1 = cy-𝗿, 𝘅1 = x1-𝗯, 𝘆1 = y1+𝗯,                             // Corners of outer square and inner square
        x2 = cx-𝗿, y2 = cy-𝗿, 𝘅2 = x2+𝗯, 𝘆2 = y2+𝗯,
        x3 = cx-𝗿, y3 = cy+𝗿, 𝘅3 = x3+𝗯, 𝘆3 = y3-𝗯,
        x4 = cx+𝗿, y4 = cy+𝗿, 𝘅4 = x4-𝗯, 𝘆4 = y4-𝗯;

      return new Drawing()                                                      // Create the drawing
       { {final float 𝘅 = 𝞈2.aldx, 𝘆 = 𝞈2.aldy;                                 // Amount dragged
          for(int i = 0; i < N; ++i) m[i].reset();                              // Reset transformation matrices then update position of right angle triangles corresponding to the one being dragged
          if      (𝘅 > 0 && 𝘆 > 0) drag(2, 2,1,0, r(-𝗮, 0, -𝘆*𝗮/𝗯), r( 0, 𝗮, 𝘅),r( 0, 𝗯, 𝘆), r(-𝗯, 0, -𝘅*𝗯/𝗮));
          else if (𝘅 < 0 && 𝘆 < 0) drag(4, 0,3,2, r( 0, 𝗮, -𝘆*𝗮/𝗯), r(-𝗮, 0, 𝘅),r(-𝗯, 0, 𝘆), r( 0, 𝗯, -𝘅*𝗯/𝗮));
          else if (𝘅 > 0 && 𝘆 < 0) drag(3, 1,2,3, r( 0, 𝗯, -𝘆*𝗯/𝗮), r( 0, 𝗯, 𝘅),r(-𝗮, 0, 𝘆), r(-𝗮, 0, -𝘅*𝗮/𝗯));
          else                     drag(1, 3,0,1, r(-𝗯, 0, -𝘆*𝗯/𝗮), r(-𝗯, 0, 𝘅),r( 0, 𝗮, 𝘆), r( 0, 𝗮, -𝘅*𝗮/𝗯));

          if      (smallerSquares(2, x1, y1, 𝘅1, 𝘆1, x3, y3)) {}                // Smaller squares
          else if (smallerSquares(4, x3, y3, 𝘅3, 𝘆3, x1, y1)) {}
          else if (smallerSquares(3, x2, y2, 𝘅2, 𝘆2, x4, y4)) {}
          else if (smallerSquares(1, x4, y4, 𝘅4, 𝘆4, x2, y2)) {}

          square(𝝺cc, "sc", sq(1f-pxy), 𝘅2,y2, x3,𝘆3, 𝘅4,y4, x1,𝘆1);            // Square cc - hang onto opacity - her rather than earlier so we can drag it to change the dimensions of the right angle triangles

          new PolyArea(x1, y1, 𝘅2, y1, x1, 𝘆1){{n="D1"; 𝞈=𝞈2; c1=𝝺sV; c2=𝝺sg; 𝝻=m[0];}}; // Sides of inner square
          new PolyArea(x2, y2, x2, 𝘆3, 𝘅2, y2){{n="D2"; 𝞈=𝞈2; c1=𝝺sg; c2=𝝺sb; 𝝻=m[1];}}; // in anti clock wise order
          new PolyArea(x3, y3, 𝘅4, y3, x3, 𝘆3){{n="D3"; 𝞈=𝞈2; c1=𝝺sb; c2=𝝺sc; 𝝻=m[2];}}; // from upper right
          new PolyArea(x4, y4, x4, 𝘆1, 𝘅4, y4){{n="D4"; 𝞈=𝞈2; c1=𝝺sc; c2=𝝺sV; 𝝻=m[3];}}; // corner

          offerToRotateTracing(cx+𝗿, cy+𝗿-rotationTracingControllerRadius());   // Tracing rotation controller in non standard position
         }
        void drag(int corner, int m1, int m2, int m3,                           // Corner, associated transformation matrices
          float y1, float x2, float y2, float x3)                               // Translations for each matrix
         {dragCorner = corner;
          m[m1].postTranslate( 0, y1);
          m[m2].postTranslate(x2, y2);
          m[m3].postTranslate(x3, 0);
         }
        boolean smallerSquares(int c,                                           // Squares aa.bb
          float x1, float y1, float x2, float y2, float x3, float y3)
         {if (c != dragCorner) return false;                                    // Not the corner being dragged
          final float o =  sq(pxy);                                             // Bring opacity up faster
          square(𝝺aa, "sa", o, x1,y1, x1,y2, x2,y2, x2,y1);                     // Square aa
          square(𝝺bb, "sb", o, x3,y3, x3,y2, x2,y2, x2,y3);                     // Square bb
          return true;                                                          // Processed dragged corner
         }
        void square(final int Colour, final String Name, final float Opacity,   // Create a full square with specified colour and name
          final float x1, final float y1, final float x2, final float y2,       // coordinates of corners
          final float x3, final float y3, final float x4, final float y4)
         {final 𝝮 𝞇 = Name == "sc" ? 𝞈1 : null;                                 // Square cc can resize the right angle triangles
          final int c = Colour;                                                 // Shorten name
          new PolyArea(x1,y1,x2,y2,x3,y3,x4,y4) {{n=Name; 𝞈=𝞇; c1=c; c2=c; o=Opacity;}}; // Create square
         }
        void outlineSquare(int colour,                                          // Outline square
          float x1, float y1, float x2, float y2,
          float x3, float y3, float x4, float y4)
         {setPaint(colour);
          canvas.drawLine(x1,y1, x2,y2, paint);
          canvas.drawLine(x2,y2, x3,y3, paint);
          canvas.drawLine(x3,y3, x4,y4, paint);
          canvas.drawLine(x4,y4, x1,y1, paint);
         }
        void underlay() {outlineSquare(𝝺os, x1,y1, x2,y2, x3,y3, x4,y4);}       // Exterior containing square
        void pointerPressed()                                                   // Pointer pressed - check that we are till dragging the same item
         {if (selectedItem != null && !rotationControllerSelected())            // Reset drawing if we start dragging on a different corner otherwise it gets confusing
           {if (lastDragged != selectedItem.item.n || (dfx && dfy))             // Reset if a different corner is chosen or if the drag has been completed
             {lastDragged = selectedItem.item.n;
              reset();
             }
           }
         }
        void pointerDragged()                                                   // Pointer dragged
         {if      (finishDrag(2,  𝗮,  𝗯)) {}
          else if (finishDrag(4, -𝗮, -𝗯)) {}
          else if (finishDrag(3,  𝗯, -𝗮)) {}
          else if (finishDrag(1, -𝗯,  𝗮)) {}
         }
        void pointerReleased()                                                  // Pointer  released
         {if (rotationControllerSelected())
           {sra(90); sra(180); sra(270); sra(360);                              // Set rotation tracker to right angles if near one
           }
          else if (d(𝞈2.aldx, 𝞈2.aldy) < close) reset();                        // Reset drawing to initial state if close to start on release
         }
        void reset() {𝞈2.aldx = 𝞈2.aldy = 0; dfx = dfy = false; pxy = 0;}       // Restart drag
        boolean finishDrag(int corner, float 𝗮, float 𝗯)                        // Finish the drag if we are close enough for the specified corner
         {final float c = close;                                                // Close enough
          if (dragCorner == corner)                                             // Check we are on specified corner
           {final float x = 𝞈2.aldx, y = 𝞈2.aldy;                               // Amount dragged
            if (dfx = (𝗮 < 0 ? x < 𝗮 + c : x > 𝗮 - c)) 𝞈2.aldx = 𝗮;             // Far enough in x
            if (dfy = (𝗯 < 0 ? y < 𝗯 + c : y > 𝗯 - c)) 𝞈2.aldy = 𝗯;             // Far enough in y
            pxy = a(x / 𝗮 * y / 𝗯);                                             // Fractional progress along drag
            return true;                                                        // Dragged corner has been processed
           }
          return false;                                                         // Wrong corner
         }
       };
     }
   } // Pythagoras
//------------------------------------------------------------------------------
// 𝕾quaring a rectangle by applying the Theorem of Pythagoras
//------------------------------------------------------------------------------
  class SquaringARectangle extends DisplayDrawing
   {final float fs = 0.20f;                                                     // Initial fractional shortest side of rectangle
    SquaringARectangle(final Activity Activity) {super(Activity.this);}         // Create display
    Drawing loadDrawing()                                                       // Load the drawing
     {final float 𝗿 = baseRadius(), 𝗱 = 2 * 𝗿,                                  // Dimension of outer square
        𝗮 = r(0, 𝗱, (1f-fs)*𝗱 +                                                 // Constrained drag of inner rectangle dimensions
          -𝞈1.aldy+𝞈3.aldy-𝞈2.aldx+𝞈4.aldx
          -𝞈1.aldx+𝞈3.aldx+𝞈2.aldy-𝞈4.aldy),
        𝗯 = 𝗱-𝗮,
        𝝰 = a(𝗮-𝗯)/2, 𝝱 = os(𝝰, 𝗿),                                             // Location of radius through vertical side of the inner square or its extension
        x1 = cx+𝗿, y1 = cy-𝗿, 𝘅1 = x1-𝗯, 𝘆1 = y1+𝗯,                             // Corners of outer square and inner square
        x2 = cx-𝗿, y2 = cy-𝗿, 𝘅2 = x2+𝗯, 𝘆2 = y2+𝗯,
        x3 = cx-𝗿, y3 = cy+𝗿, 𝘅3 = x3+𝗯, 𝘆3 = y3-𝗯,
        x4 = cx+𝗿, y4 = cy+𝗿, 𝘅4 = x4-𝗯, 𝘆4 = y4-𝗯,
        X  = cx-𝝰, Y  = cy-𝝱, 𝗫 = cx+𝝰, 𝗬 = cy+𝝱;                               // Diameter coordinates

      return new Drawing()                                                      // Create the drawing
       { {new PolyArea(x1,y1, 𝘅2,y1, 𝘅2,𝘆2, x1,𝘆1){{n="D1"; 𝞈=𝞈1; c1=𝝺sV; c2=𝝺sg;}}; // Sides of inner square
          new PolyArea(x2,y2, x2,𝘆3, 𝘅3,𝘆3, 𝘅2,y2){{n="D2"; 𝞈=𝞈2; c1=𝝺sb; c2=𝝺sc;}}; // in anti clock wise order
          new PolyArea(x3,y3, 𝘅4,y3, 𝘅4,𝘆4, x3,𝘆3){{n="D3"; 𝞈=𝞈3; c1=𝝺sg; c2=𝝺sb;}}; // from upper right
          new PolyArea(x4,y4, x4,𝘆1, 𝘅1,𝘆1, 𝘅4,y4){{n="D4"; 𝞈=𝞈4; c1=𝝺sc; c2=𝝺sV;}}; // corner
         }
        void overlay()
         {final float t = innerThickness(), 𝘁 = 2 * t;                          // Thickness of overlay lines
          setPaint(𝝺o, t); canvas.drawRect(x1,y1, x3,y3, paint);                // Outer square
          setPaint(𝝺r, t); canvas.drawRect(𝘅1,𝘆1, 𝘅3,𝘆3, paint);                // Outer square
          setPaint(𝝺y, t); canvas.drawCircle(cx, cy, 𝗿, paint);                 // Circle
          setPaint(𝝺y, 𝘁); canvas.drawLine(X,Y, 𝗫,𝗬, paint);                    // Diameter
          setPaint(𝝺r, 𝘁); canvas.drawLine(X,𝗬, 𝗫,𝗬, paint);                    // Side of central square
          setPaint(𝝺g, 𝘁); canvas.drawLine(X,Y, X,𝗬, paint);                    // Side of square with same area as 4 rectangles = outer square - inner square
         }
        void pointerReleased()
         {final float c = 2*outerThickness();                                   // Closeness
          final boolean                                                         // Close to things
            line = a(𝗮) < c || a(𝗯) < c, square = a(𝗮-𝗯) < c, half = a(𝝰-𝝱) < c;
// Upgrade to Goal
//          if      (square) achieved(SquaringARectangle.this, "Square");       // Achieved
//          else if (line)   achieved(SquaringARectangle.this, "Line");
//          else if (half)   achieved(SquaringARectangle.this, "Half");
          if      (line)   zero(0);                                             // Improve: reset if rectangle is close to being a line
          else if (half)   zero((𝗱-(𝗱-𝗱/sq(2))/2)-fs*𝗱);                        // Improve to half
          else if (square) zero(𝗱/2-fs*𝗱);                                      // Improve to square
         }
        void zero(final float d)                                                // Reset drag
         {𝞈1.aldx = d;
          𝞈1.aldy=𝞈2.aldx=𝞈2.aldy=𝞈3.aldx=𝞈3.aldy=𝞈4.aldx=𝞈4.aldy=0;
         }
       };
     }
   } // SquaringARectangle
//------------------------------------------------------------------------------
// 𝕮ompare the length of the left diameter with the length of the right diameter
// to demonstrate that lengths can be compared at a distance
//------------------------------------------------------------------------------
  class ComparingLengths extends DisplayDrawing
   {final float r1 = 0.7f, r2 = 0.8f;                                           // Initial fractional length of diameters vs height of screen
    ComparingLengths(final Activity Activity) {super(Activity.this);}           // Create display
    Drawing loadDrawing()                                                       // Load the drawing
     {final float 𝗿 = baseRadius(), 𝗱 = 2*𝗿,                                    // Circle dimensions
        x1 = cx-𝗿, y1 = cy, x2 = cx+𝗿, y2 = cy,                                 // Positions of centres of circles
        𝗿1 = r(2*outerThickness(), 𝗱, 𝗿*r1+𝞈1.radial),                          // Left radius
        𝗿2 = r(2*outerThickness(), 𝗱, 𝗿*r2+𝞈2.radial);                          // Right radius
      return new Drawing()                                                      // Create the drawing
       {final float a1, a2, 𝗮, dx, dy, 𝘅1, 𝘆1, 𝘅2, 𝘆2;                          // Angles of each diameter, angles of reflection of left diameter, vector along left diameter, coordinates of each end reflected in the first mirror
         {createMirror(null, 1, cx, cy, 90, false, true, false, false);         // Fixed mirror through centre
          a1=a1(); a2=a2(); dx=𝗿1*cd(a1); dy=𝗿1*sd(a1);                         // Diameter angles, vector along the left diameter
          mirror1.reflectInMirror(x1+dx, y1+dy);                                // Reflect position of centre of left diameter
          𝘅1 = mirror1.𝖃; 𝘆1 = mirror1.𝖄;                                       // Reflected position of centre of left diameter
          mirror1.reflectInMirror(x1-dx, y1-dy);                                // Reflect position of other end of the left diameter
          𝘅2 = mirror1.𝖃; 𝘆2 = mirror1.𝖄;                                       // Reflected position of other point along the left diameter
          𝗮 = angle(𝘅1, 𝘆1, 𝘅2, 𝘆2);                                            // Angle of reflected left diameter - now on the right
          createMirror(𝞈3, 2, x2, y2, (𝗮+a2)/2, true, false, true, false);      // Moveable mirror through right intersection angled to reflect the reflection of the left diameter onto the right diameter so their lengths can be compared
          new Diameter() {{n="c1"; 𝕩=x=x1; 𝕪=y=y1; r=𝗿1; a=a1; 𝞈=𝞈1; c1=𝝺w; c2=𝝺w; C=𝝺y;}}; // Left diameter
          new Diameter() {{n="c2"; 𝕩=x=x2; 𝕪=y=y2; r=𝗿2; a=a2; 𝞈=𝞈2; c1=𝝺sr;c2=𝝺ss;C=𝝺w;}}; // Right diameter with which the left diameter will be compared
         }
        float a1() {return 𝞈1.angle-90;}                                        // Angle of left diameter
        float a2() {return 𝞈2.angle-60;}                                        // Angle of right diameter
        void overlay()
         {final float 𝕩1, 𝕪1, 𝕩2, 𝕪2;                                           // Coordinates of left diameter reflected in first mirror and then again in the second mirror
          mirror2.reflectInMirror(𝘅1, 𝘆1);
          𝕩1 = mirror2.𝖃; 𝕪1 = mirror2.𝖄;                                       // One end
          mirror2.reflectInMirror(𝘅2, 𝘆2);
          𝕩2 = mirror2.𝖃; 𝕪2 = mirror2.𝖄;                                       // Other end
          setPaint(𝝺y); canvas.drawLine(𝕩1, 𝕪1, 𝕩2, 𝕪2, paint);                 // Line joining ends of doubly reflected left diameter
          setPaint(𝝺y); canvas.drawLine(x1-dx, y1-dy, x1+dx, y1+dy, paint);     // Original
         }
        void pointerReleased()
         {if (a(𝗿2-𝗿1) < outerThickness()) 𝞈1.radial += 𝗿2 - 𝗿1;                // Improve length if possible
         }
       };
     }
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
   } // ComparingLengths
//------------------------------------------------------------------------------
// 𝕿he Schwartz inequality - a line is the shortest path between two points
//------------------------------------------------------------------------------
  class SchwartzInequality extends DisplayDrawing
   {SchwartzInequality(final Activity Activity) {super(Activity.this);}         // Create display
    Drawing loadDrawing()                                                       // Load the drawing
     {final float
        𝘄 = 0.9f*w,                       𝗵 = 0.9f*h,                           // Dimensions of bounding rectangle
        x1 = cx-𝘄/2,                      y1 = cy+𝗵/2,                          // Coordinates of the corners of the triangle
        x2 = cx+r(-𝘄/2, +𝘄/2, 𝞈1.aldx),  y2 = cy-𝗵/2+r(0, 0.9f*𝗵, 𝞈1.aldy),     // Apex
        x3 = cx+𝘄/2,                      y3 = cy+𝗵/2,                          // Right
        𝗥 = a(y1-y2),                                                           // Radius of angle
        𝝰 = angle(x3, y3, x2, y2, x1, y1), 𝝱 = angle(x3, y3, x2, y2);           // Angle of drag angle

      return new Drawing()                                                      // Create the drawing
       { {new Ring    (){{n="c1"; c1=𝝺sg; c2=𝝺sb; C=𝝺w; x=x1; y=y1;mR(x2-x1);}};// Circles
          new Ring    (){{n="c2"; c1=𝝺sb; c2=𝝺sg; C=𝝺w; x=x3; y=y3;mR(x3-x2);}};
          new Diameter(){{n="d1"; c1=𝝺sp; c2=𝝺sg; C=𝝺w; bp(x1, y1, x3, y3);}};  // Base
          new Diameter(){{n="d2"; c1=𝝺sg; c2=𝝺sr; C=𝝺w; bp(x1, y1, x2, y2);}};  // Left
          new Diameter(){{n="d3"; c1=𝝺sr; c2=𝝺sp; C=𝝺w; bp(x2, y2, x3, y3);}};  // Right
          new Angle   (){{n="a1"; c1=𝝺sr; c2=𝝺sb; 𝞈=𝞈1;x=x2;y=y2;A=𝝰;a=𝝱;R=𝗥;}};// Apex angle - draggable
         }
       };
     }
   } // SchwartzInequality
//------------------------------------------------------------------------------
// 𝕮ongruent triangles
//------------------------------------------------------------------------------
  class CongruentTriangles extends DisplayDrawing
   {CongruentTriangles(final Activity Activity)                                 // Create display
     {super(Activity.this);
      𝞈1.angle = isosceles() ? -120 : -60; 𝞈2.angle =  60; 𝞈3.angle = 180;      // Initial positions of the corners of the triangle
     }
    Drawing loadDrawing()                                                       // Load the drawing
     {final boolean i = isosceles();                                            // Draw an isoceles else a scalene triangle
      final float 𝗿 = baseRadius(), 𝕣 = 𝗿*sq(3f/4f), R = 𝗿/2,                   // Dimensions, of an equilateral triangle
        𝝰  = nd(𝞈1.angle), 𝝱 = 𝝰 * (isosceles() ? -1 : -2),                     // Angles of corners in circle
        x1 = cx+𝗿,         y1 = cy,                                             // Coordinates of the corners of the triangle
        x2 = cx+𝗿*cd(𝝰),   y2 = cy+𝗿*sd(𝝰),
        x3 = cx+𝗿*cd(𝝱),   y3 = cy+𝗿*sd(𝝱);
      return new Drawing()                                                      // Create the drawing
       { {new Ring    (){{n="c1"; c1=𝝺sg; c2=𝝺sb; C=𝝺w; mR();}};
          createMirror(null, 1, 90);                                            // Fixed mirror along y-axis
          new Diameter(){{n="d1"; c1=𝝺y; c2=𝝺y; C=𝝺w; bp(x1, y1, x2, y2);}};
          new Diameter(){{n="d2"; c1=𝝺y; c2=𝝺y; C=𝝺w; bp(x2, y2, x3, y3);}};
          new Diameter(){{n="d3"; c1=𝝺y; c2=𝝺y; C=𝝺w; bp(x3, y3, x1, y1);}};
          new Tab     (){{n="t1"; c1=𝝺g; c2=𝝺b; 𝞈=𝞈1; x=x2; y=y2; R=r/4; A=angle(x3,y3,x2,y2,x1,y1); a=angle(x3,y3,x2,y2); reflect=false;}};
          offerToRotateTracing(cx+𝗿, cy+𝗿);                                     // Tracing rotation controller in non standard position
         }
        void underlay()                                                         // Overlay right angles to mirror
         {final Paint p = paint; final Canvas c = canvas;                       // Shorten names
          setPaint(𝝺w); c.drawLine(cx-𝗿, cy,   cx+𝗿, cy,   p);                  // Horizontal diameter
          setPaint(𝝺r); c.drawLine(cx-R, cy-𝕣, cx+R, cy+𝕣, p); c.drawLine(cx-R, cy+𝕣, cx+R, cy-𝕣, p); // 60
          setPaint(𝝺g); c.drawLine(cx-𝕣, cy-R, cx+𝕣, cy+R, p); c.drawLine(cx-𝕣, cy+R, cx+𝕣, cy-R, p); // 30
         }
        void pointerReleased()                                                  // Pointer  released
         {sra(90); sra(180); sra(270); sra(360);                                // Set rotation tracker to right angles if near one
          for(int i = 0; i < 12; ++i) sra1(30*i);                               // Set triangle to multiple of 30 degrees
         }
       };
     }
    boolean isosceles()   {return false;}                                       // Draw an isosceles triangle else a scalene triangle
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
   } // CongruentTriangles
  class CongruentIsocelesTriangles extends CongruentTriangles                   // An isosceles triangle can be rotated on to its reflection
   {CongruentIsocelesTriangles(final Activity Activity) {super(Activity);}
    boolean isosceles() {return true;}
   } // CongruentIsocelesTriangles
  class CongruentScaleneTriangles extends CongruentTriangles                    // A scalene triangle cannot be rotated on to its reflection
   {CongruentScaleneTriangles(final Activity Activity) {super(Activity);}
   } // CongruentScaleneTriangles
//------------------------------------------------------------------------------
// 𝕯raggable triangle: basis for drawings of triangles
//------------------------------------------------------------------------------
  class DraggableTriangle extends DisplayDrawing
   {final PointF 𝗽 = new PointF(), 𝗾 = new PointF(),                            // Intersection points
      c = new PointF(), 𝗰 = new PointF(), 𝕔 = new PointF();                     // Corner opposite shortest, longest, remaining side
    float fx() {return -0.45f;} float fy() {return fx();}                       // Fractional offset off initial position from centre
    float f𝘅() {return -fx();}  float f𝘆() {return 0;}
    float f𝕩() {return  fx();}  float f𝕪() {return -fy();}

    int 𝝺t11() {return 𝝺sV;} int 𝝺t12() {return 𝝺ss;}                           // Angle colours
    int 𝝺t21() {return 𝝺sg;} int 𝝺t22() {return 𝝺sr;}
    int 𝝺t31() {return 𝝺sb;} int 𝝺t32() {return 𝝺y;}

    int 𝝺d11() {return 𝝺sr;} int 𝝺d12() {return 𝝺sg;} int 𝝺d1() {return 𝝺w;}    // Diameter colours = colours of sides
    int 𝝺d21() {return 𝝺sb;} int 𝝺d22() {return 𝝺sr;} int 𝝺d2() {return 𝝺w;}
    int 𝝺d31() {return 𝝺sg;} int 𝝺d32() {return 𝝺sb;} int 𝝺d3() {return 𝝺w;}

    boolean writeAnglesInDegrees() {return true;}                               // Override to supress angles being show numerically in degrees

    float dragTabsRadius() {return m(w,h)/4;}                                   // Radius of drag tabs

    class ReferenceTriangle                                                     // Convenient Description of a triangle
     {float a, 𝗮, 𝕒, x, y, 𝘅, 𝘆, 𝕩, 𝕪;
      ReferenceTriangle() {}
      ReferenceTriangle set(float A, float 𝗔, float 𝔸,
        float X, float Y, float 𝗫, float 𝗬, float 𝕏, float 𝕐)
       {a = A; 𝗮 = 𝗔; 𝕒 = 𝔸; x = X; y = Y; 𝘅 = 𝗫; 𝘆 = 𝗬; 𝕩 = 𝕏; 𝕪 = 𝕐;
        return this;
       }
     }
    final ReferenceTriangle 𝘁 = new ReferenceTriangle();                        // Description of current triangle

    AngleGoal fixedAngleGoal(final double a, final double 𝗮, final String name) // Create a fixed angle goal
     {return new AngleGoal(name)
       {float A() {return (float)a;}
        float 𝗔() {return (float)𝗮;}
       };
     }

    final AngleGoal                                                             // Goals for the user
      𝝲Equilateral         = fixedAngleGoal(60, 60, "Equilateral"),
      𝝲EquilateralHalf     = fixedAngleGoal(30, 60, "EquilateralHalf"),
      𝝲IsoscelesRightAngle = fixedAngleGoal(45, 45, "IsoscelesRightAngle"),
      𝝲Line                = fixedAngleGoal( 0,  0, "Line"),
      𝝲RightAngle          = new AngleGoal("RightAngle") {float A() {return ma();} float 𝗔() {return 90-ma();}},
      𝝲Isosceles           = new AngleGoal("Isosceles")  {float A() {return ia();} float 𝗔() {return    𝗶a();}};

    void setGoal() {setCurrentGoal(𝝲Isosceles);}                                // Override this goal as desired

    DraggableTriangle(final Activity Activity) {super(Activity.this);}          // Create display
    Drawing loadDrawing()                                                       // Load the drawing
     {final float
        x1 = r(0, w, cx+fx()*w+𝞈1.aldx),   y1 = r(0, h, cy+fy()*h+𝞈1.aldy),     // Top left
        x2 = r(0, w, cx+f𝘅()*w+𝞈2.aldx),   y2 = r(0, h, cy+f𝘆()*h+𝞈2.aldy),     // Middle right
        x3 = r(0, w, cx+f𝕩()*w+𝞈3.aldx),   y3 = r(0, h, cy+f𝕪()*h+𝞈3.aldy),     // Bottom left
        𝗥  = dragTabsRadius(),                                                  // Radius of drag tabs
        𝗮  = angle(x3, y3, x1, y1, x2, y2),  𝕒 = angle(x2, y2, x1, y1)-𝗮,       // Drag tabs, sweep angle, angular position
        𝗯  = angle(x1, y1, x2, y2, x3, y3),  𝕓 = angle(x3, y3, x2, y2)-𝗯,
        𝗰  = angle(x2, y2, x3, y3, x1, y1),  𝕔 = angle(x1, y1, x3, y3)-𝗰;

      int C11=𝝺t11(),C12=𝝺t12(), C21=𝝺t21(),C22=𝝺t22(), C31=𝝺t31(),C32=𝝺t32();  // Initial angle colours
      final boolean ab = nearAngle(𝗮, 𝗯) < 1,                                   // Angles that are close in value have the same colours
                    ac = nearAngle(𝗮, 𝗰) < 1,
                    bc = nearAngle(𝗯, 𝗰) < 1;
      if      (ab && ac && bc) {C31 = C21 = C11; C32 = C22 = C12;}              // Equilateral
      else if (ab)             {C21 = C11; C22 = C12;}                          // Isosceles various
      else if (ac)             {C31 = C11; C32 = C12;}
      else if (bc)             {C31 = C21; C32 = C22;}
      final int c11=C11, c12=C12, c21=C21, c22=C22, c31=C31, c32=C32;           // Initial angle colours

      𝘁.set(𝗮, 𝗯, 𝗰, x1,y1, x2,y2, x3,y3);                                      // Parameter list to overrides in a compact form
      final 𝝮 𝞃 = translate() ? 𝞈Translation : null;                            // Allow translation or not

      setGoal();                                                                // Set the goal for the user

      return new Drawing()                                                      // Create the drawing
       { {underlayItems(this);                                                  // Override to draw items under the triangle
          new Tab     (){{n="t1"; c1=c11; c2=c12; 𝞈=𝞈1;x=x1;y=y1;A=𝗮;a=𝕒;R=𝗥; reflect=false; trace=false;}}; // Drag tabs
          new Tab     (){{n="t2"; c1=c21; c2=c22; 𝞈=𝞈2;x=x2;y=y2;A=𝗯;a=𝕓;R=𝗥; reflect=false; trace=false;}}; // Turning off tracing makes it easier to see what is happening when dragging but as a consequence one cannot offer to rotate the items in the drawing.
          new Tab     (){{n="t3"; c1=c31; c2=c32; 𝞈=𝞈3;x=x3;y=y3;A=𝗰;a=𝕔;R=𝗥; reflect=false; trace=false;}};
          new Diameter(){{n="d1"; c1=𝝺d11(); c2=𝝺d12(); C=𝝺d1(); 𝞈=𝞃; bp(x1, y1, x3, y3);    trace=false;}};    // Sides
          new Diameter(){{n="d2"; c1=𝝺d21(); c2=𝝺d22(); C=𝝺d2(); 𝞈=𝞃; bp(x1, y1, x2, y2);    trace=false;}};    // Turning off tracing makes it easier to see what is happening when dragging
          new Diameter(){{n="d3"; c1=𝝺d31(); c2=𝝺d32(); C=𝝺d3(); 𝞈=𝞃; bp(x2, y2, x3, y3);    trace=false;}};
          overlayItems(this);                                                   // Override to draw items over the triangle
          if (writeAnglesInDegrees())                                           // Write angles if requested
           {writeAngles(x1, y1, x2, y2, x3, y3,
            c12, irnd(nt(𝗮)), c22, irnd(nt(𝗯)), c32);
           }
         }
        void underlay       () {drawUnderlay  ();}                              // Call to overridable underlay() method
        void overlay        () {drawOverlay   ();}                              // Call to overridable overlay() method
        void pointerPressed () {pointerPressed();}                              // Call to overridable pointerPressed() method
        void pointerDragged () {pointerDragged();}                              // Call to overridable pointerDragged() method
        void pointerReleased() {pointerReleased();}                             // Call to overridable pointerReleased() method
       };
     }
    void drawUnderlay          () {}                                            // Underlay drawing
    void drawOverlay           () {}                                            // Overlay drawing
    void pointerPressed        () {}                                            // Pointer pressed
    void pointerDragged        () {}                                            // Pointer dragged
    void pointerReleased       () {}                                            // Pointer released
    void underlayItems(Drawing 𝗱) {}                                            // Override to add additional items to be drawn under the triangle
    void overlayItems (Drawing 𝗱) {}                                            // Override to add additional items to be drawn on top of the triangle
    boolean translate() {return false;}                                         // Allow translation
    void i1(PointF 𝗽) {𝞈1.aldx = 𝗽.x-cx - fx()*w; 𝞈1.aldy = 𝗽.y-cy - fy()*h;}   // Improve corner 1
    void i2(PointF 𝗽) {𝞈2.aldx = 𝗽.x-cx - f𝘅()*w; 𝞈2.aldy = 𝗽.y-cy - f𝘆()*h;}   // Improve corner 2
    void i3(PointF 𝗽) {𝞈3.aldx = 𝗽.x-cx - f𝕩()*w; 𝞈3.aldy = 𝗽.y-cy - f𝕪()*h;}   // Improve corner 3
    private void writeAngles                                                    // Write the angles, but only the different angles so that we avoid the problem of things adding up to 180
     (float x1, float y1, float x2, float y2, float x3, float y3,
      int   a,  int   𝗮,  int   b,  int   𝗯,  int  c)
     {final int 𝗰 = 180 - 𝗮 - 𝗯;                                                // Make sure the angles add up to 180
      final Paint p = paint;
      final float 𝗵 = h/9f, 𝕙 = h/8f, l = w/3f, u = h/3f;                       // Text height, quadrants - with apologies to left handed people
      final boolean
        left = (x1 > l && x2 > l) || (x1 > l && x3 > l) || (x2 > l && x3 > l),  // Draw left
        up   = (y1 > u && y2 > u) || (y1 > u && y3 > u) || (y2 > u && y3 > u);  // Draw upper
      p.setTextSize(𝗵);
      final float
        𝘄 = p.measureText("000"),                                               // Width of largest number
        𝘅 = left ? 𝘄 + w / 16f : 15f*w/16f,                                     // X of end of string
        𝘆 = up ? h / 16f : 15f*h/16f - 3 * 𝕙;                                   // Y  of base
      p.setStyle(Paint.Style.FILL);
      p.setTextAlign(Paint.Align.RIGHT);                                        // Align numbers right
      p.setPathEffect(null);                                                    // Make sure that no path effect is in effect
      if      (a == b && b == c) w(a, b, c, 60, 60, 60,    𝘅, 𝘆, 𝕙);            // Make sure the numbers always add up to 180
      else if (a == b)           w(a, b, c, 𝗮, 𝗮, 180-2*𝗮, 𝘅, 𝘆, 𝕙);
      else if (a == c)           w(a, b, c, 𝗮, 180-2*𝗮, 𝗮, 𝘅, 𝘆, 𝕙);
      else if (b == c)           w(a, b, c, 180-2*𝗯, 𝗯, 𝗯, 𝘅, 𝘆, 𝕙);
      else                       w(a, b, c, 𝗮, 𝗯, 180-𝗮-𝗯, 𝘅, 𝘆, 𝕙);
      p.setTextAlign(Paint.Align.LEFT);                                         // This paint is shared and this is the oinly method in which we touch the alignment so it is easiest to reset here
     }
    private void w                                                              // Write a set of angles
     (int c, int 𝗰, int 𝕔, int a, int 𝗮, int 𝕒, float x, float y, float h)
     {w(c, a, x, y, 1, h);
      w(𝗰, 𝗮, x, y, 2, h);
      w(𝕔, 𝕒, x, y, 3, h);
     }
    private int w(int colour, int a, float x, float y, int r, float h)          // Write an angle on a row
     {paint.setColor(colour);
      canvas.drawText(""+a+"°", x, y+r*h, paint);
      return r+1;
     }
    int irnd(float f) {return Math.round(nd(f));}                               // Round to nearest integer
    void sortSides(final float x, final float y, final float 𝘅, final float 𝘆,  // Vertices of triangle which we want to sort by opposite side length, the sorted results are in PointF c, 𝗰, 𝕔 in ascending order of angle
      final float 𝕩, final float 𝕪)
     {final float d = d(𝕩-𝘅,𝕪-𝘆), 𝗱 = d(𝕩-x,𝕪-y), 𝕕 = d(𝘅-x,𝘆-y);               // Length of the sides
      if      (d<=𝗱 && d<=𝕕)                                                    // Corner x,y is opposite shortest side
       {c.x = x; c.y = y;
        if (𝗱 > 𝕕) {𝕔.x = 𝘅; 𝕔.y = 𝘆; 𝗰.x = 𝕩; 𝗰.y = 𝕪;}                        // Side order is shortest x,y, longest=𝘅,𝘆, middle=𝕩,𝕪
        else       {𝕔.x = 𝕩; 𝕔.y = 𝕪; 𝗰.x = 𝘅; 𝗰.y = 𝘆;}                        // Side order is shortest x,y, longest=𝕩,𝕪, middle=𝘅,𝘆,
       }
      else if (𝗱<=d && 𝗱<=𝕕)                                                    // Corner 𝘅,𝘆 is opposite shortest side
       {c.x = 𝘅; c.y = 𝘆;
        if (d > 𝕕) {𝕔.x = x; 𝕔.y = y; 𝗰.x = 𝕩; 𝗰.y = 𝕪;}                        // Side order is shortest 𝘅,𝘆, longest=x,y, middle=𝕩,𝕪
        else       {𝕔.x = 𝕩; 𝕔.y = 𝕪; 𝗰.x = x; 𝗰.y = y;}                        // Side order is shortest 𝘅,𝘆, longest=𝕩,𝕪, middle=x,y,
       }
      else                                                                      // Corner 𝕩,𝕪 is opposite shortest side
       {c.x = 𝕩; c.y = 𝕪;
        if (d > 𝗱) {𝕔.x = x; 𝕔.y = y; 𝗰.x = 𝘅; 𝗰.y = 𝘆;}                        // Side order is shortest 𝕩,𝕪, longest=x,y, middle=𝘅,𝘆
        else       {𝕔.x = 𝘅; 𝕔.y = 𝘆; 𝗰.x = x; 𝗰.y = y;}                        // Side order is shortest 𝕩,𝕪, longest=𝘅,𝘆, middle=x,y,
       }
     }
    abstract class AngleGoal extends Goal                                       // Triangle with specified angles
     {abstract float A();                                                       // Angles associated with this goal after any modifications
      abstract float 𝗔();
      float aA, 𝗮A, 𝕒A, a𝗔, 𝗮𝗔, 𝕒𝗔, a𝔸, 𝗮𝔸, 𝕒𝔸;                                 // Each angle possibility in a match up

      AngleGoal(final String Name) {super(Name);}                               // Name of the goal
      private float equiAngle, smallestAngle;                                   // Equi angle and smallest angle of an isoceles triangle adjusted so that we can safely represent these angles as integers
      float ma()                                                                // Minimum angle of the triangle
       {final float r = m(nt(𝘁.a), m(nt(𝘁.𝗮), nt(𝘁.𝕒)));
        return r;
       }
      private int apex(float a, float 𝗮, float 𝕒)                               // Find number of angle at apex of isosceles triangle else 0 if no angle is obviously the apex
       {return nearAngle(a, 𝗮) < 1 ? 3 :
               nearAngle(a, 𝕒) < 1 ? 2 :
               nearAngle(𝗮, 𝕒) < 1 ? 1 : 0;
       }
      private int ma(float a, float 𝗮, float 𝕒)                                 // Find number of minimum angle
       {return a <= 𝗮 && a <= 𝕒 ? 1 : 𝗮 <= a && 𝗮 <= 𝕒 ? 2 : 3;
       }
      private void 𝕚a()                                                         // Isoceles angles
       {final float a = nt(𝘁.a), 𝗮 = nt(𝘁.𝗮), 𝕒 = nt(𝘁.𝕒);
        final int m = ma(a, 𝗮, 𝕒), x = apex(a, 𝗮, 𝕒);                           // Minimum angle, apex angle numbers
        final float i = irnd(m == 1 ? a : m == 2 ? 𝗮 : 𝕒);                      // Apex angle
        if (m == x)                                                             // Apex angle is the minimum angle
         {smallestAngle = i;                                                    // Apex angle
          equiAngle = (180 - smallestAngle) / 2;                                // Equi-angle is largest
         }
        else smallestAngle = equiAngle = i;                                     // Smallest angle is an equi-angle
       }
      float ia() {𝕚a(); return smallestAngle;}                                  // Isoceles angle - smallest angle
      float 𝗶a() {𝕚a(); return equiAngle;}                                      // Isoceles angle - equi-angle

      void matchUpAngles()                                                      // See how well each pair of angle combinations matchs up
       {final float
          a = nt(𝘁.a), 𝗮 = nt(𝘁.𝗮), 𝕒 = nt(𝘁.𝕒),                                // Actual angles
          A = A(), 𝗔 = 𝗔(), 𝔸 = 180 - A - 𝗔;                                    // Target Angles
        aA = nearAngle(a, A);  𝗮A = nearAngle(𝗮, A); 𝕒A = nearAngle(𝕒, A);
        a𝗔 = nearAngle(a, 𝗔); 𝗮𝗔 = nearAngle(𝗮, 𝗔); 𝕒𝗔 = nearAngle(𝕒, 𝗔);
        a𝔸 = nearAngle(a, 𝔸); 𝗮𝔸 = nearAngle(𝗮, 𝔸); 𝕒𝔸 = nearAngle(𝕒, 𝔸);
       }

      boolean checkAchieved()                                                   // Nearness to a triangle specified by two of its angles which can be interior or exterior
       {final float a = nt(𝘁.a), 𝗮 = nt(𝘁.𝗮), 𝕒 = nt(𝘁.𝕒),                      // Interior angles
          x = 𝘁.x, y = 𝘁.y, 𝘅 = 𝘁.𝘅, 𝘆 = 𝘁.𝘆, 𝕩 = 𝘁.𝕩, 𝕪 = 𝘁.𝕪;
        matchUpAngles();                                                        // Match up angles
        final boolean r =                                                       // Did we achieve this goal
         (aA < 1 && 𝗮𝗔 < 1) || (a𝗔 < 1 && 𝗮𝔸 < 1) || (a𝔸 < 1 && 𝗮A < 1) ||      // Go around one way to see if we can get a fit of two angles
         (𝗮A < 1 && a𝗔 < 1) || (𝗮𝗔 < 1 && a𝔸 < 1) || (𝗮𝔸 < 1 && aA < 1);        // Or try the other way around
        return r;
       }

      float howClose()                                                          // How close we are to the goal
       {matchUpAngles();                                                        // Match up angles
        final float[]C = new float[]                                            // Possible fits
         {aA + 𝗮𝗔 + 𝕒𝔸, aA + 𝕒𝗔 + 𝗮𝔸,
          𝗮A + a𝗔 + 𝕒𝔸, 𝗮A + 𝕒𝗔 + a𝔸,
          𝕒A + a𝗔 + 𝗮𝔸, 𝕒A + 𝗮𝗔 + a𝔸};
        float m = C[0];
        for(float c: C) if (c < m) m = c;                                       // Find best fit
        return m / 3;                                                           // Average closeness at best fit
       }

      void improveUserEffort()                                                  // Improve the triangle to match the goal exactly
       {final float a = nt(𝘁.a), 𝗮 = nt(𝘁.𝗮), 𝕒 = nt(𝘁.𝕒),                      // Interior angles
          x = 𝘁.x, y = 𝘁.y, 𝘅 = 𝘁.𝘅, 𝘆 = 𝘁.𝘆, 𝕩 = 𝘁.𝕩, 𝕪 = 𝘁.𝕪;
        sortSides(x,y, 𝘅,𝘆, 𝕩,𝕪);                                               // Opposite shortest, longest, other
        final float A = A(), 𝗔 = 𝗔();                                           // Target angles
        final float 𝗵 = heightAndPositionOfAltitude(𝗽, c.x,c.y, 𝗰.x,𝗰.y, A,𝗔);  // Altitude on longest side, position of altitude
        if (pointToLine(𝗾, 𝕔.x, 𝕔.y, c.x, c.y, 𝗰.x, 𝗰.y))                       // Sets point(𝗾) to the point closest to point(x,y) on the line through point(𝘅,𝘆) and point(𝕩,𝕪)
         {final float d = 𝗾.length();                                           // Length of altitude to current apex
          𝗽.x = 𝗽.x - 𝗵 * 𝗾.x / d; 𝗽.y = 𝗽.y - 𝗵 * 𝗾.y / d;                     // New position of apex scaled to the right length
          final int i = nearestVertex(𝗽.x, 𝗽.y, x, y, 𝘅, 𝘆, 𝕩, 𝕪);              // Index of nearest vertex
          if (i == 0) i1(𝗽); else if (i == 1) i2(𝗽); else i3(𝗽);                // Make current nearest apex the new apex
          final float                                                           // How far out of the frame we are
            X = 𝗽.x < 0 ? -𝗽.x : 𝗽.x > w ? w - 𝗽.x : 0,
            Y = 𝗽.y < 0 ? -𝗽.y : 𝗽.y > h ? h - 𝗽.y : 0;
          𝞈1.aldx += X; 𝞈1.aldy += Y;                                           // Adjust to bring back into frame if necessary
          𝞈2.aldx += X; 𝞈2.aldy += Y;
          𝞈3.aldx += X; 𝞈3.aldy += Y;
         }
       }
     } // AngleGoal
   } // DraggableTriangle
//------------------------------------------------------------------------------
// 𝕾ierpinski's triangular gasket - draggable slowly as the refresh rate is low!
//------------------------------------------------------------------------------
  class Sierpinski extends DraggableTriangle
   {final int[]colour = new int[]
     {𝝺y, 𝝺sb, 𝝺so, 𝝺sB,  𝝺sp, 𝝺sc, 𝝺sr, 𝝺sC,  𝝺ss, 𝝺sg, 𝝺sv, 𝝺sG,  𝝺sV};       // Colours for each level
    float dragTabsRadius() {return m(cx, cy)*sq(3);}                            // Radius of drag tabs

    Sierpinski(final Activity Activity) {super(Activity.this);}                 // Create display

    void drawOverlay()                                                          // Overlay normal to each diameter
     {triangle(8, 𝘁.x, 𝘁.y, 𝘁.𝘅, 𝘁.𝘆, 𝘁.𝕩, 𝘁.𝕪);
     }
    void triangle(final int depth, final float x1, final float y1,              // Depth to go, position of triangle to draw and sub divide
      final float x2, final float y2, final float x3, final float y3)
     {if (depth < 0) return;                                                    // Stop at some point
      triangle(depth-1, x1,y1, (x1+x2)/2, (y1+y2)/2, (x1+x3)/2, (y1+y3)/2);     // Interior triangles
      triangle(depth-1, x2,y2, (x1+x2)/2, (y1+y2)/2, (x2+x3)/2, (y2+y3)/2);
      triangle(depth-1, x3,y3, (x1+x3)/2, (y1+y3)/2, (x2+x3)/2, (y2+y3)/2);
      setPaint(colour[depth], 1);                                               // Set paint
      canvas.drawLines(new float[]{x1,y1,x2,y2,x2,y2,x3,y3,x3,y3,x1,y1},paint); // Exterior triangle
     }
   } // Sierpinski
//------------------------------------------------------------------------------
// 𝕰uler's splendid line! https://en.wikipedia.org/wiki/Euler_line
//------------------------------------------------------------------------------
  class EulerLine extends DraggableTriangle
   {EulerLine(final Activity Activity)                                          // Create display
     {super(Activity.this);
      overlayMirror = new Stack<Mirror>();                                      // Allocate mirrors for overlaid drawing
     }
    final RectF                                                                 // Preallocated lines
      v2 = new RectF(), 𝘃2 = new RectF(),                                       // Angle bisectors - in-circle
      s2 = new RectF(), 𝘀2 = new RectF(), 𝕤2 = new RectF(),                     // Side normals - circum-circle
      a2 = new RectF(), 𝗮2 = new RectF(), 𝕒2 = new RectF(),                     // Altitudes: perpendicular from vertex to opposite side - orthocentre
      c2 = new RectF(), 𝗰2 = new RectF(), 𝕔2 = new RectF();                     // Centroids: line from vertex to mid point of opposite side - centroid
    final PointF                                                                // Preallocated important points - most found by findEulersLine()
      𝗽 = new PointF(),                                                         // Intersection point
      e = new PointF(),                                                         // Start  of Euler's line at circumcentre
      𝗲 = new PointF(),                                                         // End of Euler's line at orthocentre
      𝝼 = new PointF(),                                                         // Nine point centre
      𝗸 = new PointF();                                                         // Centroid
    Float er, 𝝼r;                                                               // Radius of circumcircle and radius of nine point circle - set by findEulersLine()
    final float t = 4*innerThickness();                                         // Thickness of line used to draw resulting circles
    final float dashWidth() {return 32f;}                                       // Dash width
    final float  𝗱 = dashWidth();
    final float[]𝕕 = new float[]{𝗱,𝗱};                                          // Dash details
    final DashPathEffect
      𝕕1 = new DashPathEffect(𝕕,0), 𝕕2 = new DashPathEffect(𝕕,  𝗱/2),           // Dash for lines and circles of overlay
      𝕕3 = new DashPathEffect(𝕕,𝗱), 𝕕4 = new DashPathEffect(𝕕,3*𝗱/2),
      𝕕InTriangle    = new DashPathEffect(new float[]{𝗱/2,𝗱/2}, 0),             // Dash pattern for inset quarter triangle
      𝕕InCircle      = new DashPathEffect(new float[]{𝗱/2,𝗱/2}, 0),             // Dash pattern for incircle circumference
      𝕕InCircleRadii = new DashPathEffect(new float[]{𝗱/2,𝗱/4}, 0),             // Dash pattern for incircle radii to touch points
      𝕕InCircleAway  = new DashPathEffect(new float[]{𝗱/4,𝗱/4}, 0);             // Dash pattern for incircle radii away from touch points

    boolean q, 𝗾, 𝕢;                                                            // Isoceles with this angle at the apex = dash lines from this vertex
    boolean circumCircle        () {return true;}                               // Draw green circle through the vertices of the triangle
    boolean ninePointCircle     () {return true;}                               // Draw red circle through the mid point of each side
    boolean ninePointCircleRadii() {return true;}                               // Draw radii of nine point circle
    boolean ninePointCircleAway () {return true;}                               // Draw away radii of nine point circle
    boolean centroid            () {return true;}                               // Draw yellow lines from vertices to mid point of the opposite side
    boolean orthoCentre         () {return true;}                               // Draw blue lines from vertices to nearest point on the opposite side
    boolean reflectNinePointCircleDiameter() {return false;}                    // Reflect in a diameter drawn through the NinPointCircle at right angles to Euler's line when close to isoscleses
    boolean inTriangle          () {return true;}                               // Draw the quarter triangle
    boolean inCircle            () {return true;}                               // Draw the inscribed circle
    boolean inCircleRadii       () {return true;}                               // Draw the inscribed circle radii
    boolean inCircleAway        () {return true;}                               // Draw the inscribed circle away radii

    int 𝝺CircumCircle   () {return 𝝺g;}                                         // Default colours at the moment the radii must be the same as the circles
    int 𝝺InCircle       () {return 𝝺c;}
    int 𝝺NinePointCircle() {return 𝝺r;}
    int 𝝺Centroid       () {return 𝝺y;}
    int 𝝺OrthoCentre    () {return 𝝺b;}
    int 𝝺EulersLine     () {return 𝝺w;}                                         // Pretty in Pink but clashes with reference triangle and so white
    int 𝝺Triangle       () {return 𝝺o;}                                         // Outline of triangle when tracing
    int 𝝺InTriangle     () {return 𝝺sp;}                                        // Sides of the inner quarter triangle to complete the octagon/pentagon associated with some goals

    class EulerGoal extends AngleGoal
     {final float a, 𝗮;                                                         // Angles
      final boolean inTriangle, inCircle;                                       // Show InTriangle, InCircle
      EulerGoal(final int InTriangle, final int InCircle,                       // > 0 -enable- more compact than true/false
        final double A, final double 𝗔, final String name)                      // Create angle goals for Eulers line
       {super(name);
        a = (float)A; 𝗮 = (float)𝗔;                                             // Save angles
        inTriangle = InTriangle > 0;                                            // Convert int to boolean
        inCircle   = InCircle   > 0;
       }
      float A() {return a;}  float 𝗔() {return 𝗮;}                              // All The Euler goals have fixed angles
     } // EulerGoal

    final EulerGoal                                                             // Goals for user to achieve
      𝝲0649 = new EulerGoal(1,1, 06, 49, "06-49 - 9. on circumcircle and ir meets bisector at base - difficult to be accurate"),
      𝝲1642 = new EulerGoal(1,1, 16, 42, "16-42 - 9. on circumcircle and ic intersects qt+c possible: c+9c+b+ic possible: 9c+cc  at ccr"),
      𝝲1840 = new EulerGoal(1,1, 18, 40, "18-40 - 9. on circumcircle and ic tangential to Eulers line"),
      𝝲1872 = new EulerGoal(0,1, 18, 72, "18-72 - altitude on centroid and incircle in right angle triangle"),
      𝝲2039 = new EulerGoal(1,1, 20, 39, "20-39 - 9. on circumcircle and ic intersects centroid"),
      𝝲2238 = new EulerGoal(1,1, 22, 38, "22-38 - 9. on circumcircle and ic tangential to central bisector"),
      𝝲2273 = new EulerGoal(0,1, 22, 73, "43-55 - altitude on centroid and incircle+orthocentre on incircle"),
/*ok*/𝝲2323 = new EulerGoal(0,0, 22.5,  22.5,  "Octagon"),                          // The altitudes cross an extension of the reference sides at right angles
      𝝲2345 = new EulerGoal(0,0, 23, 45, "23-45"                                                       ),
      𝝲2367 = new EulerGoal(1,1, 23, 67, "23-67 - c+c+ic  and RA"),
      𝝲2436 = new EulerGoal(1,1, 24, 36, "24-36 - 9. on circumcircle and ir touches Eulers Line"),
      𝝲2452 = new EulerGoal(1,1, 24, 52, "24-52 - a+b+c and e+i+r"),
      𝝲2474 = new EulerGoal(0,1, 24, 74, "24-74 - a+9pr+ic and c+a+q"),
      𝝲2733 = new EulerGoal(1,1, 27, 33, "27-33 - 9. on circumcircle and ir touches central bisector"),
      𝝲2745 = new EulerGoal(0,0, 27, 45, "27-45 perhaps: a+c+qt"                                       ),
      𝝲2828 = new EulerGoal(0,0, 28, 28, "IntersectionOfNinePointCentreWithCircumCircleAtSideBisectors"),
      𝝲2853 = new EulerGoal(1,1, 28, 53, "28-53 - a+b+c and b+c+ic"),
/*ok*/𝝲3030 = new EulerGoal(0,0, 30, 30, "Hexagon: IntersectionOfNinePointCentreAtAVertex"                     ),  // Altitudes are tangential to nine point circle which is an exicircle of the bisectors of the sides of the reference triangle and its base being tangential to the bisectors
      𝝲3045 = new EulerGoal(0,0, 30, 45, "30-45"                                                      ),
      𝝲3060 = new EulerGoal(1,1, 30, 60, "30-60 - b+c+ir and right angle"),
      𝝲3165 = new EulerGoal(0,1, 31, 65, "31-65 - a+b+9rc+ic"),
      𝝲3166 = new EulerGoal(1,1, 31, 66, "31-66 - a+b+qt+ic"),
      𝝲3258 = new EulerGoal(1,1, 32, 58, "32-58 - a+qt+nr+b"),
      𝝲3267 = new EulerGoal(0,1, 32, 67, "32-67 - altitude, centroid, side bisector on incircle"),
      𝝲3345 = new EulerGoal(0,0, 33, 45, "33-45"                                                      ),
/*ok*/𝝲3434 = new EulerGoal(0,0, 34.26, 34.26, "DiameterOfNinePointCircleIsChordOfCircumCircle"        ),  // See tests/Euler-34-34.pl
      𝝲3473 = new EulerGoal(1,1, 34, 73, "34-73 - b+b+q+ic perhaps: b+qt+ic"),
/*ok*/𝝲3555 = new EulerGoal(0,0, 35.26, 54.74, "CentroidAltitudeBisectorInRATriangle"                  ),  // see ../tests
      𝝲3561 = new EulerGoal(0,1, 35, 61, "35-61 - a+ic+qt and orthocentre on incircle, possible: b+c+ir"),
      𝝲3567 = new EulerGoal(0,1, 35, 67, "35-67 - a+ir+qt+c"                                           ),
/*ok*/𝝲3636 = new EulerGoal(0,0, 36, 36, "Pentagon"                                                    ),  // Drag apex of reference triangle to apex of pentagon drawn inside nine point circle
      𝝲3753 = new EulerGoal(1,1, 37, 53, "37-53 - 9c+ir and right angled"),
/*ok*/𝝲3838 = new EulerGoal(0,0, 37.76, 37.76, "IntersectionOfCentroidAndCircumCircle"                 ),  // Divides Euler's line into 6 equal pieces
      𝝲4056 = new EulerGoal(0,1, 40, 56, "40-56 Altitude on side bisector and incircle radius"         ),
      𝝲4155 = new EulerGoal(0,1, 41, 55, "41-55 - altitude on centroid and incircle"                   ),
      𝝲4260 = new EulerGoal(0,0, 42, 60, "42-60"                                                       ),
/*ok*/𝝲4545 = new EulerGoal(0,0, 45, 45, "IsocelesRightAngleTriangle"                                  ),  // Altitudes plus side bisectors make a square with Euler's line and the nine point circle radii making diagonals
      𝝲4557 = new EulerGoal(0,0, 45, 57, "a+b+c and a+b+base"                                          ),
      𝝲4559 = new EulerGoal(1,0, 45, 59, "45-59 - on intriangle"                                       ),
      𝝲4561 = new EulerGoal(1,0, 45, 61, "45-61 - on intriangle"                                       ),
      𝝲4564 = new EulerGoal(1,0, 45, 63, "45-63 - on intriangle"                                       ),
      𝝲4658 = new EulerGoal(1,0, 46, 58, "46-58 - a+qt+ir"                                             ),
      𝝲4667 = new EulerGoal(1,0, 46, 67, "46-67 - a+qt+iw iosceles"                                    ),
      𝝲4848 = new EulerGoal(1,0, 48, 48, "48-48 - 9. on incircle in isosceles triangle - might have to constrain drawing to iosceles for this to be meaningful"),
      𝝲5059 = new EulerGoal(1,0, 50, 59, "50-59 - on intriangle"                                       ),
      𝝲5178 = new EulerGoal(1,0, 51, 78, "51-78 - a+b+it iosceles"                                     ),
      𝝲5264 = new EulerGoal(1,0, 52, 64, "52-64 - b+c+qt iosceles"                                     ),
      𝝲5555 = new EulerGoal(1,0, 55, 55, "55-55 - inscribed triangle bisected by Euler's line"         ),
      𝝲𝝲 = null;


    EulerGoal currentGoal;                                                      // The current goal
    void setGoal()                                                              // Override this goal as desired
     {currentGoal = 𝝲3555;
      super.setCurrentGoal(currentGoal);
     }

    void overlayItems(Drawing 𝗱)                                                // Draw additional items -  inthis case Euler's line as a diameter
     {if (findEulersLine())                                                     // Find points on Euler's line
       {𝗱.new Diameter() {{n="el"; c1=𝝺sc; c2=𝝺sg; C=𝝺EulersLine();
        𝞈=𝞈Rotation; 𝕩 = 𝝼.x; 𝕪 = 𝝼.y;                                          // Make Euler's line a rotation controller about the centre of the nine point circle
        bp(e.x, e.y, 𝗲.x, 𝗲.y); reflect = false; trace=false;}};                // Switching off tracing of this item helps to simplify the complex image that we get when tracing
       }
     }

    void drawOverlay()                                                          // Draw overlay: either one standard one, or a bright one over a pale one
     {if (!findEulersLine()) return;                                            // Find Eulers line and proceed if we can, else draw nothing
      if (makeTracing())                                                        // Tracing of overlay
       {drawOverlay(true, Integer.valueOf(96), null, 0, 0);                     // Pale - but undashed
        drawOverlay(true, null, 𝕕1, 𝞈Translation.aldx, 𝞈Translation.aldy);      // Strong but dashed
       }
      else
       {drawOverlay(false, null, null, 0, 0);                                   // Standard overlay
       }
     }
    void drawOverlay(final boolean tracing, final Integer Opacity,              // Draw the overlay possibly with a non default opacity and dashes
      final DashPathEffect Dash, final float Dx, final float Dy)                // Optionally translated
     {final float a=𝘁.a, 𝗮=𝘁.𝗮, 𝕒=𝘁.𝕒, x=𝘁.x, y=𝘁.y, 𝘅=𝘁.𝘅, 𝘆=𝘁.𝘆, 𝕩=𝘁.𝕩, 𝕪=𝘁.𝕪;
      final int 𝝺cc = 𝝺CircumCircle(), 𝝺oc = 𝝺OrthoCentre(), 𝝺cd = 𝝺Centroid(), // Finalize colours of lines/circle/radii in overlay
        𝝺nc = 𝝺NinePointCircle(), 𝝺t = 𝝺Triangle();
      final boolean tracingUpper = Dash != null;

      q = nearAngle(𝗮,𝕒) < 1; 𝗾 = nearAngle(a,𝕒) < 1; 𝕢 = nearAngle(a,𝗮) < 1;   // Closeness to isoceles
      overlayMirror.clear();                                                    // Clear mirror stack as mirrors are not at a fixed location
      canvas.save();                                                            // Save canvas translation so we can restore it later
// Rotation action
      if (tracingUpper)                                                         // Draw tracing upper layer
       {if (false) {}
//      if      (currentGoal == 𝝲i9v)                                           // Goal specific rotations obtained by dragging on Euler's line
//       {if      (q) canvas.rotate(𝞈Rotation.angle, x, y);                     // Shows that the triangle formed by the alitudes is equilateral
//        else if (𝗾) canvas.rotate(𝞈Rotation.angle, 𝘅, 𝘆);
//        else if (𝕢) canvas.rotate(𝞈Rotation.angle, 𝕩, 𝕪);
//       }
        else if (currentGoal == 𝝲4545) canvas.rotate(𝞈Rotation.angle, 𝝼.x, 𝝼.y);// Hexagon
        else if (currentGoal == 𝝲2323) canvas.rotate(𝞈Rotation.angle, e.x, e.y);// Pentagon with centre at circumcentre
        else if (currentGoal == 𝝲2323) canvas.rotate(𝞈Rotation.angle, 𝝼.x, 𝝼.y);// Octagon with centre at nine point circle centre
       }
// Initialize canvas
      canvas.translate(Dx, Dy);                                                 // Allows us to temporarily see a bit more of the drawing
      opacityOverride = Opacity;                                                // Possibly change opacity
      dashOverride = Dash;                                                      // Possibly make everying dashed
// Mirrors
      if (reflectNinePointCircleDiameter() && (q || 𝗾 || 𝕢) && !(q && 𝗾 && 𝕢))  // Reflect in a diameter drawn through the NinePointCircle at right angles to Euler's line when close to isosceles but avoid am equliateral becuase too sensitive
       {final float dx = 𝗲.y-e.y, dy = e.x-𝗲.x;                                 // Mirror vector
        overlayMirror.push(new Mirror(𝝼.x, 𝝼.y, 𝝼.x+dx, 𝝼.y+dy));               // Save mirror
       }
//    if (currentGoal == 𝝲iab)                                                  // Mirrors through centre of circumcircle allow creation of equilateral triangle, square, pentagon, hexagon
//     {createAMirrorIfNotParallelToEulerLine(x, y, e, 𝗲);
//      createAMirrorIfNotParallelToEulerLine(𝘅, 𝘆, e, 𝗲);
//      createAMirrorIfNotParallelToEulerLine(𝕩, 𝕪, e, 𝗲);
//     }
// Completed goals
      if (currentGoal != null && currentGoal.achievedGoal() && !pressed)        // Achieved goal
       {if (currentGoal == 𝝲2323)                                               // Draw completion of octagon with centre at nine point circle centre
         {if      (q) drawOppositeAngleOfQuarterTriangle(x, y, 𝘅, 𝘆, 𝕩, 𝕪);
          else if (𝗾) drawOppositeAngleOfQuarterTriangle(𝘅, 𝘆, 𝕩, 𝕪, x, y);
          else if (𝕢) drawOppositeAngleOfQuarterTriangle(𝕩, 𝕪, x, y, 𝘅, 𝘆);
         }
        else if (currentGoal == 𝝲3555)                                          // User goal achieved
         {sortSides(x, y, 𝘅, 𝘆, 𝕩, 𝕪);                                          // Sort the sides and put the results in c, 𝗰, 𝕔
          if (findOrthoCentre(𝗽, (c.x+𝗰.x)/2, (c.y+𝗰.y)/2, 𝗰.x, 𝗰.y, 𝕔.x, 𝕔.y))
           {setPaint(𝝺w, t); drawCircle(𝗽.x, 𝗽.y, 32);
           }
         }
        else if (currentGoal == 𝝲3636) drawPentagon(𝝼, 𝗲, 𝝼r);                  // User goal achieved - show pentagon
       }
// Draw triangle
      if (true)                                                                 // Draw the outline of the triangle
       {if (tracing) setPaint(𝝺t, 𝕕InTriangle); else setPaint(𝝺t);
        drawLine(x, y, 𝘅, 𝘆); drawLine(𝘅, 𝘆, 𝕩, 𝕪); drawLine(𝕩, 𝕪, x, y);
       }
// Draw quarter triangle
      if (inTriangle() || (currentGoal != null && currentGoal.inTriangle))      // Draw the quarter triangle
       {setPaint(𝝺InTriangle(), 𝕕InTriangle);
        drawLine((x+𝘅)/2, (y+𝘆)/2, (𝘅+𝕩)/2, (𝘆+𝕪)/2);
        drawLine((𝘅+𝕩)/2, (𝘆+𝕪)/2, (𝕩+x)/2, (𝕪+y)/2);
        drawLine((𝕩+x)/2, (𝕪+y)/2, (x+𝘅)/2, (y+𝘆)/2);
       }
// Draw InCircle
      if (inCircle() || (currentGoal != null && currentGoal.inCircle))          // Draw the in circle
       {drawInCircle(x,y, 𝘅,𝘆, 𝕩,𝕪);                                            // Draw in-circle if requested
       }
// Draw elements of Euler's line
      if (circumCircle())                                                       // Draw Circum-circle if requested
       {drawCircleThroughThreePoints(𝕕1, 𝝺cc, 𝝺cc, 0, 0, a, 𝗮, 𝕒, x, y, 𝘅, 𝘆, 𝕩, 𝕪);
       }

      if (orthoCentre()) drawOrthoCentre(𝕕2, 𝝺oc, x, y, 𝘅, 𝘆, 𝕩, 𝕪);            // Draw perpendicular lines through orthocentre if requested

      if (centroid())                                                           // Draw lines through centroid if requested
       {fMid(c2, x,y, 𝘅,𝘆, 𝕩,𝕪);                                                 // Find perpendicular from each vertex to opposite side
        fMid(𝗰2, 𝘅,𝘆, 𝕩,𝕪, x,y);
        fMid(𝕔2, 𝕩,𝕪, x,y, 𝘅,𝘆);
        setPaint(𝝺cd);                                                          // Color of lines through centroid
        setDash(𝕕3); drawLine(c2);
        set𝗗ash(𝕕3); drawLine(𝗰2);
        set𝔻ash(𝕕3); drawLine(𝕔2);
       }

      if (ninePointCircle())                                                    // Draw nine point circle if requested
       {final float X = (x + 𝘅)/2, Y = (y + 𝘆)/2,                               // Mid points
                    𝗫 = (𝘅 + 𝕩)/2, 𝗬 = (𝘆 + 𝕪 )/2,
                    𝕏 = (x + 𝕩)/2, 𝕐 = (y + 𝕪)/2,
          A = angle(𝗫, 𝗬, X, Y, 𝕏, 𝕐),                                          // Angle of each vertex
          𝗔 = angle(𝕏, 𝕐, 𝗫, 𝗬, X, Y),
          𝔸 = angle(X, Y, 𝕏, 𝕐, 𝗫, 𝗬);
        drawCircleThroughThreePoints(𝕕4, 𝝺nc, 0, 𝝺nc, 𝝺nc, A, 𝗔, 𝔸, X, Y, 𝗫, 𝗬, 𝕏, 𝕐);
       }

      if ((q || 𝗾 || 𝗾) && !(q && 𝗾 && 𝗾)) drawMirrors(𝝺nc, 𝝺w, 𝕕1, 𝕕3);        // Draw any mirrors that have been created

      drawEulersLine();                                                         // Draw Euler's line and show divison of line between centres
      opacityOverride = null;                                                   // Back to normal opacity
      dashOverride = Dash;                                                      // Remove dashes
      canvas.restore();                                                         // Restore canvas translation
     }
    void drawOppositeAngleOfQuarterTriangle                                     // Draw the opposite angle of the quarter triangle
     (final float x, final float y, final float 𝘅, final float 𝘆,               // Vertices of triangle
      final float 𝕩, final float 𝕪)
     {setPaint(𝝺InTriangle());
      drawLine((x+𝘅)/2, (y+𝘆)/2, (𝘅+𝕩)/2, (𝘆+𝕪)/2);
      drawLine((x+𝕩)/2, (y+𝕪)/2, (𝘅+𝕩)/2, (𝘆+𝕪)/2);
     }
    boolean findEulersLine()                                                    // Find Eulers line
     {final float a=𝘁.a, 𝗮=𝘁.𝗮, 𝕒=𝘁.𝕒, x=𝘁.x, y=𝘁.y, 𝘅=𝘁.𝘅, 𝘆=𝘁.𝘆, 𝕩=𝘁.𝕩, 𝕪=𝘁.𝕪;
      if (findCentreOfCircleThroughThreePoints(e, x,y, 𝘅,𝘆, 𝕩,𝕪) &&             // Find Circum centre - one end of Euler's line
          findCentroid                        (𝗸, x,y, 𝘅,𝘆, 𝕩,𝕪) &&             // Centroid
          findCentreOfNinePointCircle         (𝝼, x,y, 𝘅,𝘆, 𝕩,𝕪) &&             // Centre of Nine point circle
          findOrthoCentre                     (𝗲, x,y, 𝘅,𝘆, 𝕩,𝕪))               // Find orthoCentre - other end of Euler's line
       {er = d(e.x-x, e.y-y);                                                   // Radius is distance from the centre to a corner
        𝝼r = d(𝝼.x-(x+𝘅)/2, 𝝼.y-(y+𝘆)/2);                                       // Radius is distance from the centre to a mid point
        return true;
       }
      return false;
     }
    void drawEulersLine()                                                       // Draw Euler's line and show divison of line between centres
     {final float a=𝘁.a, 𝗮=𝘁.𝗮, 𝕒=𝘁.𝕒, x=𝘁.x, y=𝘁.y, 𝘅=𝘁.𝘅, 𝘆=𝘁.𝘆, 𝕩=𝘁.𝕩, 𝕪=𝘁.𝕪;
      if (findEulersLine())                                                     // Find Euler's line
       {setPaint(𝝺EulersLine(), 𝕕1); drawLine(e, 𝗲);                            // Draw Euler's line
       }
     }
    void createAMirrorIfNotParallelToEulerLine                                  // Create a mirror unless it would be parallel to Euler's line
     (final float x, final float y, final PointF e, final PointF 𝗲)
     {if (nearAngle(angle(x, y, e.x, e.y, 𝗲.x, 𝗲.y), 0) > 1)                    // Not parallel to Euler's line
       {overlayMirror.push(new Mirror(e.x, e.y, x, y));                         // Save mirror
       }
     }

    void setDash(DashPathEffect dash) {paint.setPathEffect(q ? dash : dashOverride);}   // Goto dash effect for lines that would otherwise be on top of each other
    void set𝗗ash(DashPathEffect dash) {paint.setPathEffect(𝗾 ? dash : dashOverride);}
    void set𝔻ash(DashPathEffect dash) {paint.setPathEffect(𝕢 ? dash : dashOverride);}

    boolean findOrthoCentre(PointF 𝗰,                                           // Place centre of circle throught three points in 𝗰 and return true is the centre was found else false
      final float x, final float y, final float 𝘅, final float 𝘆,               // Vertices of triangle
      final float 𝕩, final float 𝕪)
     {fPerp(a2, x,y, 𝘅,𝘆, 𝕩,𝕪);                                                 // Find perpendicular from each vertex to opposite side
      fPerp(𝗮2, 𝘅,𝘆, 𝕩,𝕪, x,y);
      fPerp(𝕒2, 𝕩,𝕪, x,y, 𝘅,𝘆);
      return intersectionPoint(𝗰, a2, 𝗮2);                                        // Find orthoCentre if possible
     }
    void drawOrthoCentre(final DashPathEffect dash, final int 𝝺,                // Draw  ortho-centre in this colour
      final float x, final float y, final float 𝘅, final float 𝘆,               // Vertices of triangle
      final float 𝕩, final float 𝕪)
     {if (orthoCentre())                                                        // Draw perpendicular lines through orthocentre - there is no obvious circle through this point
       {fPerp(a2, x,y, 𝘅,𝘆, 𝕩,𝕪);                                               // Find perpendicular from each vertex to opposite side
        fPerp(𝗮2, 𝘅,𝘆, 𝕩,𝕪, x,y);
        fPerp(𝕒2, 𝕩,𝕪, x,y, 𝘅,𝘆);
        if (findOrthoCentre(𝗲, x,y, 𝘅,𝘆, 𝕩,𝕪))                                  // Find orthoCentre if possible
         {setPaint(𝝺);
          setDash(dash);
          drawLine(a2.left, a2.top, 𝗲.x, 𝗲.y                   );                 // Draw lines from vertices to intersection and to
          drawLine(                 𝗲.x, 𝗲.y, a2.right, a2.bottom);

          set𝗗ash(dash);
          drawLine(𝗮2.left, 𝗮2.top, 𝗲.x, 𝗲.y                   );
          drawLine(                 𝗲.x, 𝗲.y, 𝗮2.right, 𝗮2.bottom);

          set𝔻ash(dash);
          drawLine(𝕒2.left, 𝕒2.top, 𝗲.x, 𝗲.y                   );
          drawLine(                 𝗲.x, 𝗲.y, 𝕒2.right, 𝕒2.bottom);
         }
       }
     }
    boolean findCentroid(PointF 𝗰,                                              // Find centroid and place it in point(𝗰)
      final float x, final float y, final float 𝘅, final float 𝘆,               // Vertices of triangle
      final float 𝕩, final float 𝕪)
     {fMid(c2, x,y, 𝘅,𝘆, 𝕩,𝕪);                                                 // Find perpendicular from each vertex to opposite side
      fMid(𝗰2, 𝘅,𝘆, 𝕩,𝕪, x,y);
      return intersectionPoint(𝗰, c2, 𝗰2);                                      // Centroid is at intersection of line from apex to opposite side
     }
    boolean findCentreOfNinePointCircle(PointF 𝗰,                               // Find centre of nine point circle and place it in point(𝗰)
      final float x, final float y, final float 𝘅, final float 𝘆,               // Vertices of triangle
      final float 𝕩, final float 𝕪)
     {final float X = (x + 𝘅)/2, Y = (y + 𝘆)/2,                                 // Mid points
                  𝗫 = (𝘅 + 𝕩)/2, 𝗬 = (𝘆 + 𝕪)/2,
                  𝕏 = (x + 𝕩)/2, 𝕐 = (y + 𝕪)/2;
      return findCentreOfCircleThroughThreePoints(𝗰, X, Y, 𝗫, 𝗬, 𝕏, 𝕐);         // Centre of mid points
     }
    boolean findCentreOfCircleThroughThreePoints(PointF 𝗰,                      // Place centre of circle throught three points in 𝗰, bisectors in n,𝗻,𝕟 and return true is the centrewas found else false
      final float x, final float y, final float 𝘅, final float 𝘆,               // Vertices of triangle
      final float 𝕩, final float 𝕪)
     {fSide(s2, x,y, 𝘅,𝘆);                                                      // Find bisector of each side n,𝗻,𝕟 are predefined to save allocations
      fSide(𝘀2, 𝘅,𝘆, 𝕩,𝕪);
      fSide(𝕤2, 𝕩,𝕪, x,y);
      return intersectionPoint(𝗰, s2, 𝘀2);                                      // Intersection of side bisectors
     }
    final PointF drawCircleThroughThreePoints = new PointF();                   // Work area    final PointF drawCircleThroughThreePointsOpposite = new PointF();           // Work area
    void drawCircleThroughThreePoints(final DashPathEffect dash,                // Draw a circle through three supplied points if possible and return true with the centre in 𝗽, else false
      final int 𝝺Circle, final int 𝝺Radii,                                      // Colours
      final int 𝝺RadiiVertices,  final int 𝝺AwayVertices,                       // Colours for radii to and away from the vertices
      final float 𝗮, final float 𝗯, final float 𝗰,                              // Angles of triangle
      final float x, final float y, final float 𝘅, final float 𝘆,               // Vertices of triangle
      final float 𝕩, final float 𝕪)
     {if (!findCentreOfCircleThroughThreePoints(𝗽, x,y, 𝘅,𝘆, 𝕩,𝕪)) return;      // Find centre of circle - return if it cannot be found
      final float r = d(𝗽.x - x, 𝗽.y - y);                                      // Radius of circle through corners
      setPaint(𝝺Circle, t);                                                     // For bigCircle()
      if (!bigCircle(𝗮, r, 𝕩,𝕪, 𝗽, 𝘅,𝘆) &&                                      // Big radius circle draw - skia graphics fail for large radii
          !bigCircle(𝗯, r, x,y, 𝗽, 𝕩,𝕪) &&
          !bigCircle(𝗰, r, 𝘅,𝘆, 𝗽, x,y))
       {if (𝝺Radii != 0)                                                        // Draw radii along bisectors
         {setPaint(𝝺Radii);
          set𝔻ash(dash); dSide(𝗽, s2, r); dSide(𝗽, s2, -r);                     // Dashed if close to isosceles
          setDash(dash); dSide(𝗽, 𝘀2, r); dSide(𝗽, 𝘀2, -r);
          set𝗗ash(dash); dSide(𝗽, 𝕤2, r); dSide(𝗽, 𝕤2, -r);
         }
        if (𝝺RadiiVertices != 0)                                                // Draw radii to vertices
         {setPaint(𝝺RadiiVertices);
          set𝔻ash(dash); drawLine(𝗽, x, y);                                     // Dashed if close to isosceles
          setDash(dash); drawLine(𝗽, 𝘅, 𝘆);
          set𝗗ash(dash); drawLine(𝗽, 𝕩, 𝕪);
         }
        if (𝝺AwayVertices != 0)                                                 // Draw radii away from vertices
         {final PointF q = drawCircleThroughThreePoints;
          setPaint(𝝺AwayVertices, 𝕕InCircleAway);                               // Always dashed
          opposite(x, y, 𝗽, q); drawLine(𝗽, q);
          opposite(𝘅, 𝘆, 𝗽, q); drawLine(𝗽, q);
          opposite(𝕩, 𝕪, 𝗽, q); drawLine(𝗽, q);
         }
        if (𝝺Circle != 0)                                                       // Draw circle
         {setPaint(𝝺Circle, t); drawCircle(𝗽.x, 𝗽.y, d(𝗽.x-x, 𝗽.y-y));
         }
       }
     }
    final PointF drawInCircleCentre = new PointF();                             // Preallocated work area
    final PointF drawInCircleTouch  = new PointF();                             // Preallocated work area
    void drawInCircle                                                           // Draw the in-circle through the specified vertices if possible
     (final float x, final float y, final float 𝘅, final float 𝘆,               // Vertices
      final float 𝕩, final float 𝕪)
     {final PointF 𝗽 = drawInCircleCentre;                                      // Name work area
      if (!icc(𝗽, x,y, 𝘅,𝘆, 𝕩,𝕪)) return;                                       // Find centre of in circle, return if it is not possible to do so
      if (inCircleRadii() || inCircleAway())                                    // Radii requested
       {final PointF p = drawInCircleTouch;                                     // Name work area
        if (pointOnLine(p, 𝗽.x, 𝗽.y, x, y, 𝘅, 𝘆)) drawInCircleRadius(𝗽, p);
        if (pointOnLine(p, 𝗽.x, 𝗽.y, 𝘅, 𝘆, 𝕩, 𝕪)) drawInCircleRadius(𝗽, p);
        if (pointOnLine(p, 𝗽.x, 𝗽.y, 𝕩, 𝕪, x, y)) drawInCircleRadius(𝗽, p);
       }
      if (inCircle())                                                           // InCircle requested
       {final Float r = pointToLine(𝗽.x, 𝗽.y, x, y, 𝘅, 𝘆);
        if (r != null)                                                          // Draw circle if we have a radius
         {setPaint(𝝺InCircle(), t, 𝕕InCircle);
          drawCircle(𝗽.x, 𝗽.y, r);
         }
       }
     }
    final PointF drawInCircleRadius = new PointF();                             // Preallocated work area
    void drawInCircleRadius(final PointF c, final PointF p)                     // Draw the in-circle radii
     {if (inCircleRadii())                                                      // Radius to touch point on side
       {setPaint(𝝺InCircle(), 𝕕InCircleRadii); drawLine(c, p);
       }
      if (inCircleAway())                                                       // Radius away - opposite to the radius to the touch point on the side
       {final PointF q = drawInCircleRadius;
        setPaint(𝝺InCircle(), 𝕕InCircleAway); opposite(p, c, q); drawLine(c, q);
       }
     }
    boolean icc(final PointF 𝗰, final float x, final float y,                   // Find centre of in-circle and place it in point 𝗰 and return true if teh centre has been found else return false
      final float 𝘅, final float 𝘆, final float 𝕩, final float 𝕪)
     {bisectAngle(v2, x,y, 𝘅,𝘆, 𝕩,𝕪);                                           // Find bisector of each angle
      bisectAngle(𝘃2, 𝘅,𝘆, 𝕩,𝕪, x,y);
      return intersectionPoint(𝗰, v2, 𝘃2);                                      // Find centre of in-circle and place it in point 𝗽
     }
    void drawPentagon(final PointF 𝗰, final PointF 𝗮, final float r)            // Draw a pentagon in a circle centred at point(𝗰), radius(r) and with its apex pointing at 𝗮
     {final float X = cd(72), Y = os(X, 1), d = d(𝗰, 𝗮),                        // Coordinates of first corner of a pentagon centred at point(0,0) with apex at point(1,0)
        x = (𝗮.x-𝗰.x)/d, y = (𝗮.y-𝗰.y)/d,                                       // Unit vector towards apex = corner 1
        𝘅 = X*x-Y*y, 𝘆 = X*y+Y*x,  𝗫 = X*x+Y*y, 𝗬 = X*y-Y*x,                    // Corners 2,5 of unit pentagon pointing at point(𝗮)
        𝕩 = X*𝘅-Y*𝘆, 𝕪 = X*𝘆+Y*𝘅,  𝕏 = X*𝗫+Y*𝗬, 𝕐 = X*𝗬-Y*𝗫,                    // Corners 3,4
       cx = 𝗰.x, cy = 𝗰.y;                                                      // Centre of pentagon
      setPaint(𝝺InTriangle());
      drawLine(cx+r*x, cy+r*y, cx+r*𝘅, cy+r*𝘆);                                 // Scale pentagon up and draw it
      drawLine(cx+r*x, cy+r*y, cx+r*𝗫, cy+r*𝗬);
      drawLine(cx+r*𝘅, cy+r*𝘆, cx+r*𝕩, cy+r*𝕪);
      drawLine(cx+r*𝗫, cy+r*𝗬, cx+r*𝕏, cy+r*𝕐);
      drawLine(cx+r*𝕩, cy+r*𝕪, cx+r*𝕏,  cy+r*𝕐);
     }
    boolean bigCircle(final float 𝗮, final float 𝗿,                             // Draw a segment of a big circle spanning an angle of 𝗮  with radius 𝗿
      final float x, final float y, final PointF 𝗽,                             // Starting at point(x,y), centred at point(𝗽)
      final float 𝘅, final float 𝘆)                                             // Finishing at point (𝘅,𝘆)
     {final int N = 100;                                                        // Number of line segments
      if (nearAngle(𝗮, 180) > 1) return false;                                  // Not a big circle around this angle
      final float a = (2*𝗮-360)/N, b = angle(x, y, 𝗽.x, 𝗽.y);                   // Interior angle step (avoid division in loop), start angle
      final float[]L = new float[4*(N+1)];                                      // Line segments
      L[0] = x; L[1] = y;                                                       // Duplicate first line to avoid if statement in following loop
      final float cx = 𝗽.x, cy = 𝗽.y;                                           // Centre of circle optimized for easy access
      for(int i = 1; i <= N; ++i)                                               // Load line segments
       {final float A = b + i * a, dx = 𝗿*cd(A), dy = 𝗿*sd(A);
        L[4*i+0] = L[4*i-2] = cx+dx;                                            // Start of this line segment is the same as the end of the last segment
        L[4*i+1] = L[4*i-1] = cy+dy;
       }
      L[4*N+2] = 𝘅; L[4*N+3] = 𝘆;                                               // Duplicate first line to avoid if statement in following loop
      drawLines(L);                                                             // Paint lines with one subroutine call
      return true;
     }
    void fSide(final RectF n,                                                   // Find line (n) at right angles through the centre of the specified line segment
      final float x, final float y, final float 𝘅, final float 𝘆)
     {final float 𝕩 = 𝘆 - y, 𝕪 = x - 𝘅;
      n.set((x+𝘅)/2, (y+𝘆)/2, (x+𝘅)/2-𝕩, (y+𝘆)/2-𝕪);                            // Normal
     }
    void fPerp(final RectF 𝗮, final float x, final float y,                     // Find line(𝗮) from vertex(x,y) perpendicular to line through point(𝘅,𝘆) and point(𝕩,𝕪)
      final float 𝘅, final float 𝘆, final float 𝕩, final float 𝕪)
     {if (pointToLine(𝗽, x,y, 𝘅,𝘆, 𝕩,𝕪))                                        // Vector to line
       {𝗮.set(x, y, x+𝗽.x, y+𝗽.y);                                              // Point, intersection point
       }
     }
    void fMid(final RectF 𝗮, final float x, final float y,
      final float 𝘅, final float 𝘆, final float 𝕩, final float 𝕪)               // Find line(𝗮) from vertex(x,y) to line through mid point of line segment through point(𝘅,𝘆) and point(𝕩,𝕪)
     {𝗮.set(x, y, (𝘅+𝕩)/2, (𝘆+𝕪)/2);
     }
    void bisectAngle(final RectF 𝗯, final float x, final float y,               // Find line which bisects the angle at point(𝘅,𝘆) and load it into 𝗯
      final float 𝘅, final float 𝘆, final float 𝕩, final float 𝕪)
     {final float                                                               // Split angle
        a = angle(𝕩,𝕪, 𝘅,𝘆, x,y),                                               // Angle sweep
        b = angle(𝕩,𝕪, 𝘅,𝘆),                                                    // Angle position
        c = a/2+b;                                                              // Angular direction of bisector
      𝗯.set(𝘅, 𝘆, 𝘅+cd(c), 𝘆+sd(c));                                            // Bisector
     }
    boolean intersectionPoint(final PointF 𝗽, final RectF l, final RectF L)     // Find intersection of two lines expressed as rectangles and place the result in 𝗽 and return true else return false
     {return intersection(𝗽, l.left, l.top, l.right, l.bottom,
                             L.left, L.top, L.right, L.bottom);
     }
    void dSide(final PointF 𝗽, final RectF 𝗿, final float r)                    // Draw bisection of each side from centre point(𝗽p) through half of each side(𝗿) to circle radius(r)
     {final float x = 𝗿.left - 𝗽.x, y = 𝗿.top - 𝗽.y, d = d(x, y);               // Vector from circle centre to side, length of vector
      if (d < 1e-3) return;                                                     // So close there is no need to draw a line
      drawLine(𝗽.x, 𝗽.y, 𝗽.x + r * x / d, 𝗽.y + r * y / d);                     // Line from centre of circle to circumference
     }
    boolean translate() {return true;}                                          // Allow translation by dragging on the sides as opposed to the tabs so we can temporarily see other parts of the drawing - reset when the user touches someting other than a side of the reference triangle
   } // EulerLine
//------------------------------------------------------------------------------
// 𝕼uarter Triangles: show quarter triangles generated by halving the sides and
// by reflecting the apex
//------------------------------------------------------------------------------
  class QuarterTriangles extends DraggableTriangle
   {float fx1() {return  0.20f;} float fy1() {return -0.45f;}                   // Fractional offset off initial position from centre
    float fx2() {return  0.45f;} float fy2() {return  0.45f;}
    float fx3() {return -0.45f;} float fy3() {return  0.45f;}

    int 𝝺d21() {return 𝝺m;} int 𝝺d22() {return 𝝺y;}                             // Diameter colour for the all important side 2 background

    QuarterTriangles(final Activity Activity) {super(Activity.this);}           // Create display
    void underlayItems(Drawing 𝗱)                                               // Override to add additional items to be drawn under the triangle
     {final float x1=𝘁.x, y1=𝘁.y, x2=𝘁.𝘅, y2=𝘁.𝘆, x3=𝘁.𝕩, y3=𝘁.𝕪,
        x12 = (x1+x2)/2,   y12 = (y1+y2)/2,                                     // Halfway points
        x31 = (x3+x1)/2,   y31 = (y3+y1)/2,
        𝕩   = (x12+x31)/2, 𝕪   = (y12+y31)/2,
        a   = angle(x3, y3, x2, y2);                                            // Mirror parallel to third side, half way down the other two sides
      𝗱.createMirror(null, 1, 𝕩, 𝕪, a,    false, true, false, true);            // Fixed mirror parallel to third side, half way down the other two sides reflects only real vertices in front of it
      𝗱.createMirror(null, 2, 𝕩, 𝕪, a+90, false, false, true, true);            // Fixed mirror at right angles to the mirror above to create central triangle - only reflects reflected points
     }
    void pointerReleased()                                                      // Pointer released
     {final float
        x1=𝘁.x, y1=𝘁.y, x2=𝘁.𝘅, y2=𝘁.𝘆, x3=𝘁.𝕩, y3=𝘁.𝕪,
        x21 = (x2-x1)/2, y21 = (y2-y1)/2,                                       // Halfway point vectors
        x32 = (x3-x2)/2, y32 = (y3-y2)/2,
        x13 = (x1-x3)/2, y13 = (y1-y3)/2,
        𝘅 = 𝞈Translation.aldx, 𝘆 = 𝞈Translation.aldy;                           // Translation in effect
      if (translationControllerSelected())
       {if (pr(x21, y21) || pr(-x21, -y21) ||
            pr(x32, y32) || pr(-x32, -y32) ||
            pr(x13, y13) || pr(-x13, -y13)) {}
       }
//      else improve();                                                         // Improve triangle
     }
    boolean pr(float x, float y)                                                // Pointer released
     {final float 𝘅 = 𝞈Translation.aldx, 𝘆 = 𝞈Translation.aldy;                 // Translation in effect
      if (d(𝘅-x, 𝘆-y) > outerThickness()) return false;                         // Close enough to a matching triangle
      𝞈Translation.aldx = x;
      𝞈Translation.aldy = y;
      return true;
     }
    void drawOverlay()                                                          // Override to add additional items to be drawn under the triangle
     {overlay(𝝺r, 0, 0);
      overlay(𝝺y, 𝞈Translation.aldx, 𝞈Translation.aldy);
     }
    void overlay(int colour, float dx, float dy)                                // Draw the quarter triangles in the indicated colour, possibly shifted by the translation(dx,dy)
     {final float x1=dx+𝘁.x, y1=dy+𝘁.y, x2=dx+𝘁.𝘅, y2=dy+𝘁.𝘆, x3=dx+𝘁.𝕩, y3=dy+𝘁.𝕪,
        x12 = (x1+x2)/2,   y12 = (y1+y2)/2,                                     // Halfway points
        x31 = (x3+x1)/2,   y31 = (y3+y1)/2,
        𝘅12 = x12-x1,      𝘆12 = y12-y1,                                        // Interior vector
        𝘅   =  x31+𝘅12,    𝘆   =  y31+𝘆12,                                      // The apex of the central triangle
        a   = angle(x3, y3, x2, y2);                                            // Mirror parallel to third side, half way down the other two sides
      setPaint(colour);                                                         // Set colour of triangles
      canvas.drawLines(new float[]{x1,y1,x2,y2, x2,y2,x3,y3, x3,y3,x1,y1},       paint); // Exterior triangle
      canvas.drawLines(new float[]{x12,y12,x31,y31, x31,y31,𝘅,𝘆,  𝘅,𝘆, x12,y12}, paint); // Interior triangle
     }
    int mirrorsRequired() {return mirrorsBoth;}                                 // Set mirrors
    boolean translate() {return true;}                                          // Allow translation
   } // QuarterTriangles
//------------------------------------------------------------------------------
// 𝖀tility functions
//------------------------------------------------------------------------------
  final float 𝝿 = f(Math.PI);                                                   // Pi
  long    t()                   {return System.currentTimeMillis();}            // Time in ms since epoch
  double  T()                   {return t() / 1000d;}                           // Time in seconds since epoch
  int     i(float f)            {return (int)f;}                                // Integer from float
  int     i(double d)           {return (int)d;}                                // Integer from double
  float   d(float x, float y)   {return (float)Math.hypot(x, y);}               // Length of a vector
  float   d(float x, float y,                                                   // Distance between two points
            float 𝘅, float 𝘆)   {return (float)Math.hypot(x-𝘅, y-𝘆);}
  float   d(PointF p, PointF q) {return d(p.x, p.y, q.x, q.y);}                 // Distance between two points
  float   f(int i)              {return (float)i;}                              // Float from integer
  float   f(long l)             {return (float)l;}                              // Float from long
  float   f(Float f, float d)   {return f != null ? f : d;}                     // Float from Box default
  float   f(double d)           {return (float)d;}                              // Float from double
  int     M(int a, int b)       {return a > b ? a : b;}                         // Maximum of two integers
  int     m(int a, int b)       {return a < b ? a : b;}                         // Minimum of two integers
  float   a(float  f)           {return Math.abs(f);}                           // Absolute value
  float   m(float  a, float  b) {return a < b ? a : b;}                         // Minimum of two floats
  float   M(float  a, float  b) {return a > b ? a : b;}                         // Maximum of two floats
  double  m(double a, double b) {return a < b ? a : b;}                         // Minimum of two doubles
  double  M(double a, double b) {return a > b ? a : b;}                         // Maximum of two doubles
  float   r(float s, float f, float v) {return v < s ? s : v > f ? f : v;}      // Range - adjust so that is within range s to f
  double  since(long d)         {return (t() - d)/1000d;}                       // Time interval since something happened in seconds derived from millisecond inputs
  double  Since(double d)       {return  T() - d;}                              // Time interval since something happened in seconds derived from floating second inputs
  void    sleep()               {sleep(1000);}                                  // Default sleep
  void    sleep(long n)         {try {Thread.sleep(n*1000);} catch(InterruptedException e){}} // Specified sleep in seconds
  void    say(String s)         {android.util.Log.e("AppaApps", s);}            // Say something = log a message
  boolean ff()                  {return Math.random() > 0.5;}                   // Fifty-fifty chance
  float   sd(float a)           {return f(Math.sin(Math.toRadians(a)));}        // Sin of degrees as float
  float   cd(float a)           {return f(Math.cos(Math.toRadians(a)));}        // Cos of degrees as float
  float   td(float a)           {return f(Math.tan(Math.toRadians(a)));}        // Tan of degrees as float
  float   sr(float a)           {return f(Math.sin(a));}                        // Sin of radians as float
  float   cr(float a)           {return f(Math.cos(a));}                        // Cos of radians as float
  float   ac(double x, double h){return f(Math.toDegrees(Math.acos(x / h)));}   // acos() in degrees
  float   as(double y, double h){return f(Math.toDegrees(Math.asin(y / h)));}   // asin() in degrees
  float   at(double x, double y){return f(Math.toDegrees(Math.atan2(y, x)));}   // atan() in degrees - two argument form - anticlockwise between -𝝿 and +𝝿 .
  float   os(float  s, float  h){return f(Math.sqrt(h*h-s*s));}                 // Other side of right angle triangle
  double  os(double s, double h){return   Math.sqrt(h*h-s*s) ;}                 // Other side of right angle triangle
  float   nd(float a)  {final float  A = a % 360f; return A >= 0 ? A : 360+A;}  // Normalize degrees to [0,360)
  double  nd(double a) {final double A = a % 360 ; return A >= 0 ? A : 360+A;}  // Normalize degrees to [0,360)
  float   nt(float a)  {final float  A = nd(a);    return A <180 ? A : 360-A;}  // Normalize an angle to the interior of a triangle
  float   sq(float  x)          {return f(Math.sqrt(x));}                       // Square root
  double  sq(double x)          {return   Math.sqrt(x);}                        // Square root
  int     SQ(int    x)          {return x*x;}                                   // Square
  float   SQ(float  x)          {return x*x;}                                   // Square
  double  SQ(double x)          {return x*x;}                                   // Square
  float   xx(float t)           {return f(Math.exp(-t*t));}                     // e**-tt
  float  ndr(float A, float B, float C)                                         // Constrain angle(C) to range(A,B), return nearest angle if out of range
   {final float a = nd(A), b = nd(B), c = nd(C);                                // Normalize
    if ((c >= a && c <= b) && a < b) return c;                                  // In range and range does not cross the cut
    if ((c >= a || c <= b) && b < a) return c ;                                 // In range and does cross the cut
    final float 𝗮 = a > c ? a-c : c-a, 𝕒 = 𝗮 < 180 ? 𝗮 : 360-𝗮;                 // Angular separation for a
    final float 𝗯 = b > c ? b-c : c-b, 𝕓 = 𝗯 < 180 ? 𝗯 : 360-𝗯;                 // Angular separation for b
    return 𝕒 < 𝕓 ? a : b;                                                       // Closest range end
   }
  float foa(float a, float b) {final float r=2*b, c=nd(a)%r; return (c>b ? r-c:c)/b;} // Fraction of oscilating angle a over range b
 }
//javac  -Xlint -cp /home/phil/Android/sdk/build-tools/23.0.0/renderscript/lib/renderscript-v8.jar:/home/phil/vocabulary/supportingDocumentation/libs/Colours.jar:/home/phil/vocabulary/supportingDocumentation/libs/Translation.jar:/home/phil/vocabulary/supportingDocumentation/libs/DejaVu.jar:/home/phil/Android/sdk/platforms/android-23/android.jar:/home/phil/vocabulary/supportingDocumentation/libs/amazon-mobile-associates-1.0.64.0.jar:/home/phil/vocabulary/supportingDocumentation/libs/aws-android-sdk-core-2.2.1.jar:/home/phil/vocabulary/supportingDocumentation/libs/aws-android-sdk-mobilenalytics-2.2.1.jar:/home/phil/vocabulary/supportingDocumentation/libs/aws-android-sdk-s3-2.2.1.jar:/home/phil/vocabulary/supportingDocumentation/libs/json-simple-1.1.1.jar:/home/phil/Amazon/sdk/Android/GameCircle/GameCircleSDK/libs/AmazonInsights-android-sdk-2.1.26.jar:/home/phil/Amazon/sdk/Android/GameCircle/GameCircleSDK/libs/gamecirclesdk.jar:/home/phil/Amazon/sdk/Android/GameCircle/GameCircleSDK/libs/login-with-amazon-sdk.jar:/home/phil/vocabulary/src -d /home/phil/vocabulary/src/z %f
