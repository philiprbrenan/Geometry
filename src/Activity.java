//------------------------------------------------------------------------------
// 𝕯raw draggable, mirrored, rotatable, translatable animated geometric shapes
// in two dimensions on Android via Canvas.drawVertices()
// 𝗣𝗵𝗶𝗹𝗶𝗽 𝗥 𝗕𝗿𝗲𝗻𝗮𝗻  at gmail dot com, Appa Apps Ltd Inc 2015/08/14 17:12:39
// I, the author of this work, hereby place this work in the public domain.
// 𝗻𝗯: Rotations are measured in degrees, clockwise, from the x axis.
//------------------------------------------------------------------------------
// 𝝰 𝝱 𝝲 𝝳 𝝷 𝝫 𝞅 𝝮 𝞈 say                                                        // Index
// 𝕽otated radius makes a circle - diameter rotated draws the same circle - need the radius version as well as first step up
// Opposite angles are equal via two way reflection of a radius
// A diameter is the longest line in a circle
// Rotating a right angle triangle makes a rectangle, hence the formula for the area of a triangle
// A diamond is made  by reflecting an isosceles triangle once or a right angle triangle twice
// Rotate a mirror placed on the corner of a rectangle show that the reflection does not merge with the original, conversely a square does as do both if the mirror is through the centre. Construct a square by rotating a reflected rectangle onto itself.
// Rotate parallel lines crossed with a diagonal onto themselves. Likewise a single line
// Flash selection along a sine wave so that the selection flashing is less abrupt
// Resolve lack of name means no selection possible and everything flashing - everything that is selectable should flash slowly when nothing has been pressed fpr 10 or so seconds
// The quadrants of a rectangle have equal areas
// A kite, diamond, square has half the area of the enclosing square/rectangle
// Move items around line 170 into Drawing
// Remove x = cx etc. as they are now the defaults
// Similar triangles by drawing similar triangles inside a triangle or focussed through a point
// Replace OPT: with suggested optimizations in java.util.Arrays and System.arraycopy
// Add do not trace capability so that tabs do not get traced
// Sierpinski's triangle in the 3/exterior version and 1/interior version with the shape of the external triangle changeable by dragging
// Two reflection (via two mirrors) equals a rotation of twice the angle between the mirrors angle
// Use Pythagoras to produce a square with the same area as a rectangle
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
//  Pythagoras
//  SchwartzInequality
//  CongruentIsocelesTriangles
//  CongruentScaleneTriangles
//  Circle3Points
    QuarterTriangles
     (this));
   }
//------------------------------------------------------------------------------
// 𝕮reate drawing surface and draw vertices upon it
//------------------------------------------------------------------------------
  abstract class DisplayDrawing extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener
   {final int 𝝺r = Colours.Red, 𝝺b = Colours.Blue, 𝝺g   = Colours.Green,        // Useful colours
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
      closeEnoughAngle = 8,                                                     // Angle close enough in degrees
      sdCloseEnoughAngle = sd(closeEnoughAngle);                                // Sine of angle close
    final int mirrorsNone=0, mirror1=1, mirror2=2, mirrorsBoth=3;               // Mirror selection
    final 𝝮 𝞈1 = new 𝝮("Tracker1"), 𝞈2 = new 𝝮("Tracker2"),                     // Drag trackers
            𝞈3 = new 𝝮("Tracker3"), 𝞈Rotation = new 𝝮("TracingRotation"),       // Tracing rotation tracker
      𝞈Translation = new 𝝮("TracingTranslation");                               // Tracing translation tracker
    final long startTime = t();                                                 // Start time of animation
    final int period = 10000;                                                   // Default rotation period in ms
    final int loopTime = (debug & debugSlow) > 0 ? 400 : 1;                     // Minimum time between draws
    final Paint paint = new Paint();                                            // Paint for vertices
    final float flashCycle = 1;                                                 // Flash of selected item cycle time
    double pressedTime;                                                         // Time of last press
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
          pressed = true;
          pressedTime = T();
          findTopMostSelectedItem(x, y);                                        // Select the top most touched item
          touchedTopMostItemPosition(x, y);                                     // Update effects of touch on drawing items
          if (!rotationControllerSelected())    𝞈Rotation   .reset();           // Remove tracing if selected item is not a tracing controller
          if (!translationControllerSelected()) 𝞈Translation.reset();           // Remove translation if selected item is not a translation controller
          pointerPressed();                                                     // Forward
        return true;
        case MotionEvent.ACTION_POINTER_DOWN:                                   // Secondary down and dragging an item on the translation tracker
        return true;
        case MotionEvent.ACTION_UP:                                             // Up
        case MotionEvent.ACTION_CANCEL:
          pressed = false;
          draggedTopMostItemPosition(x, y);                                     // Update effects of drag on drawing items
          pointerReleased();                                                    // Pointer release is processed before we reset the selected item - this allows improvement processing to occur on the tracing as well as the original
          selectedItem = null;                                                  // Must be last to allow pointerReleased() to process the selectedItem
        return true;
        case MotionEvent.ACTION_MOVE:                                           // Drag
          draggedTopMostItemPosition(x, y);                                     // Process primary pointer move on selected item
          pointerDragged();
        return true;
        default:
          pressed = false;
        return false;
       }
     }

    void pointerPressed () {if (drawing != null) drawing.pointerPressed ();}    // Pointer pressed
    void pointerDragged () {if (drawing != null) drawing.pointerDragged ();}    // Pointer dragged
    void pointerReleased() {if (drawing != null) drawing.pointerReleased();}    // Pointer released

    void volumeLevel(final float v) {}                                          // Set  volume level

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
    void achieved(Object c, String a)                                           // Achievement - override to receive any achievements that the user achieves
     {//say("Achieved "+a+" in class "+c.getClass().getEnclosingClass().getSimpleName());
      say("Achieved "+a+" in class "+c.getClass().getSimpleName());
     }
// Move the following into Drawing
    void underlay() {if (drawing != null) drawing.underlay();}                  // Highlights drawn under the drawing
    void overlay()  {if (drawing != null) drawing.overlay();}                   // Highlights drawn over the drawing
    float innerThickness()                                                      // Half the thickness of central lines in the upper layer of this drawing
     {final float t = canvas == null ?  16f : m(64, M(8, sq(m(w, h))));         // Size to canvas
      return t / 8f;                                                            // Scaled
     }
    float outerThickness() {return 8 * innerThickness();}                       // Outer thickness based on inner thickness
    void setPaint() {setPaint(𝝺w);}                                             // Set paint to known state for drawing overlays with default colour
    void setPaint(final int colour)                                             // Set paint to known state for drawing overlays with supplied colour
     {paint.setColor(colour);                                                   // Set colour
//    paint.setAlpha(defaultOpacity1()); if setPaint() is only used for overlays, then I think full opacity is required // Full opacity
      paint.setAlpha(255);                                                      // Full opacity
      paint.setStyle(Paint.Style.FILL_AND_STROKE);                              // Fill and Stroke
      paint.setStrokeWidth(innerThickness()*2);                                 // Standard thickness
     }
    void setPaint(final int colour, float strokeWidth)                          // Set paint to known state for drawing overlays with supplied colour
     {setPaint(colour);                                                         // Set general characteristics
      paint.setStyle(Paint.Style.STROKE);                                       // Fill and Stroke
      paint.setStrokeWidth(strokeWidth);                                        // Fill and Stroke
     }
    int defaultOpacity0() {return 255* 8/16;}                                   // Default opacity for lower layer
    int defaultOpacity1() {return 255*10/16;}                                   // Default opacity for upper layer
//------------------------------------------------------------------------------
// 𝕸irror
//------------------------------------------------------------------------------
    class 𝝫                                                                     // Mirror specification
     {final float x, y, 𝘅, 𝘆, 𝕏, 𝕐;                                             // Mirror is through point(x,y) and point(𝘅, 𝘆), vector parallel to mirror
      final float 𝕩, 𝕪;                                                         // Unit vector normal to mirror pointing at the reflection
      final boolean both;                                                       // False - only stuff in front of the mirror is reflected as if we were observing from inside the drawing, else if true - everything is reflected as if we were observing from above
      final boolean reflectReal;                                                // True: reflect real vertices
      final boolean reflectReflections;                                         // True: reflect real reflections
      float opacity = 1;                                                        // 0-1 opacity multiplier for this mirror
      float 𝖃, 𝖄;                                                               // Reflection of the latest supplied point - not thread safe but much faster
      boolean reflection;                                                       // True - reflection, false - real
      𝝫(float X, float Y, float A)                                              // Mirror line extends through point(x,y) at angle A
       {this( X, Y, X + cd(A), Y + sd(A), true, true, true);
       }
      𝝫(float X, float Y, float 𝗫, float 𝗬)                                     // Mirror line extends through point(x,y) and point(𝘅, 𝘆)
       {this( X, Y, 𝗫, 𝗬, true, true, true);
       }
      𝝫(float X, float Y, float 𝗫, float 𝗬,                                     // Mirror line extends through point(x,y) and point(𝘅, 𝘆) and reflects on one or both sides
        boolean Both, boolean ReflectReal, boolean ReflectReflections)
       {both = Both;                                                            // Whether to reflect on one or both sides
        reflectReal = ReflectReal;                                              // Reflect real vertices
        reflectReflections = ReflectReflections;                                 // Reflect reflected vertices
        x = X; y = Y; 𝘅 = 𝗫; 𝘆 = 𝗬;                                             // Mirror line
        𝕏 = 𝘅 - x; 𝕐 = 𝘆 - y;                                                   // Vector parallel to mirror
        final float d = d(𝕏, 𝕐);                                                // Length of vector parallel to mirror
        𝕩 = 𝕐 / d; 𝕪 = -𝕏 / d;                                                  // Unit vector normal to mirror pointing at the reflection
       }
      boolean reflect(final float X, final float Y)                             // Point to reflect
       {final float d = pointToLine(X, Y, x, y, 𝘅, 𝘆);                          // Distance to mirror
        reflection = d >= 0;                                                    // True - the returned point is to the left of the line and is thus in reflected space.
        final int D = reflection || both ? 2 : 1;                               // Dump stuff behind the mirror in the mirror unless both specified
        𝖃 = X + D * d * 𝕩; 𝖄 = Y + D * d * 𝕪;
        return reflection;                                                      // True - the returned point is to the left of the line and is thus in reflected space.
       }
      𝝫 opacity(final float Opacity) {opacity = Opacity; return this;}          // Set opacity
     } // 𝝫
    int mirrorsRequired() {return mirrorsNone;}                                 // Override to specify which mnirrors should be drawn
//------------------------------------------------------------------------------
// 𝕿rack rotation about a centre so that the user can drag circularly
//------------------------------------------------------------------------------
    class 𝝮                                                                     // Track drag rotation
     {final String name;                                                        // Name of rotation
      float Dx, Dy, lastTime, lastAngle;                                        // Start of last drag, time of last drag, angle at last drag
      float angle, speed, aldx, aldy;                                           // Current angle, angular speed, accumulated linear drag in x, accumulated linear drag in y
      𝝮(final String Name) {name = Name;}                                       // Name to facilitate debugging
      𝝮(final String Name, float Angle) {name = Name; angle = Angle;}           // Name to facilitate debugging, initial angle
      void updateMotion(boolean start,                                          // Start or continuation of drag
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
        if (!start)                                                             // Accumulated linear drag accounting for only upto a single mirror at the moment
         {final float dx = X - Dx, dy = Y - Dy;                                 // Delta in normal coordinates
          if      (reflectionDepth == 0) {aldx += dx; aldy += dy;}              // No reflection
          else if (reflectionDepth == 1)                                        // Reflected delta when on mirror present, where parallel to the mirror is not transformed, but away from the mirror is reversed
           {final 𝝫 m = drawing.mirror1;
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
      boolean[]doNotReflect, doNotTrace;                                        // Whether this vertex should be reflected or not, traced or not
      int layer;                                                                // Current layer
      int numberOfCoordinates, numberOfVertexPairs;                             // Number of coordinates, number of pairs of points
      int mirrorBlockSize, rotationBlockSize;                                   // Size of unmirrored base in pairs of coordinate pairs, size of rotation block = all mirrored data
      𝝫 mirror1, mirror2;                                                       // Mirror definition chosen or null for no mirror - upto two mirrors allowed
      float tracingRotationOpacity = 1;                                         // Opacity multiplier for rotated tracing if present
      Float tracingRotationCentreX;                                             // Centre of rotation of drawing - X - else centre of drawing
      Float tracingRotationCentreY;                                             // Centre of rotation of drawing - Y - else centre of drawing
      int mirrors()      {return mirror2 != null ? 2 : mirror1 != null ? 1 : 0;}// Number of active mirrors
      int mirrorBlocks() {return mirror2 != null ? 5 : mirror1 != null ? 2 : 1;}// Number of blocks of data = unmirrored plus mirrored - mirrors are normalized so abbreviated tests are ok
      int tracingBlocks(){return makeTracing ? 2 : 1;}                          // Double the number of vertices if a tracing of the drawing is being rotated
      boolean makeTracing = a(𝞈Rotation.angle) > closeEnoughAngle || d(𝞈Translation.aldx, 𝞈Translation.aldy) > innerThickness();  // Do tracing id tracing angle shows movement from zero
      void allocate()                                                           // Allocate the vertex arrays
       {for(final Item i: items) i.setSelectedColours();                        // Set selected colours for each item based in unselected colours
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
        rotationBlockSize   = N * M;                                            // Block subject to rotation = all mirrored data
        numberOfVertexPairs = N * M * R;                                        // Number of pairs of points including fire breaks
        numberOfCoordinates = coordsPerVertexPair * numberOfVertexPairs;        // Number of coordinates including fire breaks
        vertices            = new float[numberOfCoordinates];                   // A vertex is represented by two coordinates
        colours             = new int  [numberOfCoordinates];                   // The colours array must be the same size, only the first half is actually loaded
        doNotReflect        = new boolean[mirrorBlockSize];                     // Reflect this vertex or not - chosen this way around for default and therefore fast initialization of this array
        doNotTrace          = new boolean[mirrorBlockSize];                     // Trace this vertex or not - chosen this way around for default and therefore fast initialization of this array
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
            for(int j = s; j < N; ++j)                                          // Set reflection and tracing for this vertex
             {doNotReflect[j] = !i.reflect();                                   // Set whether this vertex should be reflected - OPT: use java.lang.Arrays.fill()
              doNotTrace  [j] = !i.trace();                                     // Set whether this vertex should be traced    - OPT: use java.lang.Arrays.fill()
             }
            final int 𝗲 = N * n;                                                // Half fire break at end
            v[𝗲+0] = v[𝗲+2] = v[𝗲-2];                                           // X
            v[𝗲+1] = v[𝗲+3] = v[𝗲-1];                                           // Y
            N += vertexPairsPerFireBreak/2;                                     // Size of fire break is two pairs of coordinates - the 2 is acceptable because we mention a pair
           }
         }
       }
      void reflectVertices()                                                    // Reflect each point
       {final 𝝫 m1 = mirror1, m2 = mirror2;                                     // Mirror order will have been normalized when the mirrors were activated
        if (m1 == null) return;                                                 // No mirrors present
        final int 𝗻 = mirrorBlockSize*coordsPerVertexPair, n = 𝗻 / 2;           // Number of entries in vertex array for base unmirrored section, number of points to reflect
        for(int i = 0; i < n; ++i)                                              // Each coordinate pair
         {if (doNotReflect[i/2]) continue;                                      // Skip reflection of vertex if not reflected
          final int j = 2*i, 𝗷 = j + 1;                                         // x,y vertices for this coordinate pair
          final float x = vertices[j], y = vertices[𝗷];                         // Coordinates of vertex

          m1.reflect(x, y);                                                     // Reflect original in first mirror
          final float x1 = m1.𝖃, y1 = m1.𝖄;
          if (m1.reflectReal)                                                   // Reflect real vertices
           {vertices  [1*𝗻+j] = x1;   vertices[1*𝗻+𝗷] = y1;
           }

          if (m2 != null)
           {m2.reflect(x, y);                                                   // Reflect original in second mirror
            final float x2 = m2.𝖃, y2 = m2.𝖄;
            if (m2.reflectReal)                                                 // Reflect real vertices
             {vertices[2*𝗻+j] = x2;   vertices[2*𝗻+𝗷] = y2;
             }
            if (m1.reflectReflections)                                          // Reflect reflection in second mirror in first mirror
             {m1.reflect(x2, y2);
              final float x12 = m1.𝖃, y12 = m1.𝖄;
              vertices[3*𝗻+j] = x12; vertices[3*𝗻+𝗷] = y12;
             }

            if (m2.reflectReflections)                                          // Reflect reflection in second mirror in first mirror
             {m2.reflect(x1, y1);
              final float x21 = m2.𝖃, y21 = m2.𝖄;
              vertices[4*𝗻+j] = x21; vertices[4*𝗻+𝗷] = y21;
             }
           }
         }
       }
      void reflectColours()                                                     // Reflect colour for each point by halving their opacity
       {if (mirror1 == null) return;                                            // No mirrors present
        final boolean 𝗺 = mirror2 != null;                                      // Presence of each mirror
        final 𝝫 m1 = mirror1, m2 = mirror2;                                     // Shorten names of mirrors whose order will already have been normalized
        final float   o1 =      m1.opacity;                                     // Opacity of mirrors
        final float   o2 = 𝗺 ? m2.opacity : 1;
        final boolean u1 = o1 != 1, u2 = o2 != 1;                               // Opacity requires adjustment
        final int 𝗻 = mirrorBlockSize*coordsPerVertexPair, n = 𝗻 / 2;           // Number of entries in vertex array for base unmirrored section, number of points to reflect
        final int p4 = 0xff;                                                    // Mask for least significant byte

        for(int i = 0; i < n; ++i)                                              // Reflect color
         {final int C = colours[i],                                             // Colour
            a  = (C>>24) & p4, r = (C>>16) & p4, g = (C>>8) & p4, b = C & p4,   // Alpha,  Colour components
            𝗴 = g>>1,                                                           // Filtered colour components for mirror 2 which absorbs green
            a0 = a,                                                             // Filtered alpha for mirror 1 - no change in opacity - let colour filtering do the job
            a1 = u1 ? i(o1 * a0) : a0;                                          // Filtered alpha for mirror 1 including mirror opacity if necessary

          colours[1*n+i] = a1<<24 | r<<16 | 𝗴<<8 | b;                           // Mirror 1 absorbs green light

          if (𝗺)                                                                // Mirror 2
           {final int                                                           // Colour
              a2 = u2 ? i(o2 * a0) : a0,                                        // Filtered alpha for mirror 2
              𝗿  = r>>1,                                                        // Filtered colour components for mirror 1 which absorbs red
              a3 = (a1*a2)>>8;                                                  // Filtered alpha for reflections in both mirrors
            colours[2*n+i] = a2<<24 | 𝗿<<16 | g<<8 | b;                         // Mirror 2 absorbs red light
            colours[3*n+i] = colours[4*n+i] = a3<<24 | 𝗿<<16 | 𝗴<<8 | b;        // Secondary reflections
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
           {final int
              A = 𝗰[0*𝗻+i],
              a = 𝗰[1*𝗻+i],
              B = 𝗰[2*𝗻+i],
              b = 𝗰[3*𝗻+i];
            𝗰[0*𝗻+i] = B;                                                       // Swap
            𝗰[1*𝗻+i] = A;
            𝗰[2*𝗻+i] = b;
            𝗰[3*𝗻+i] = a;
           }
         }                                                                      //                       1 2 3 4 5     54321
        else                                                                    // Two reflections: swap AaBbCcDdEe to EDCBAedcba - this is somewhat arbitrary
         {for(int i = 0; i < 𝗻; ++i)                                            // Each coordinate pair
           {final int
              A = 𝗰[0*𝗻+i],
              a = 𝗰[1*𝗻+i],
              B = 𝗰[2*𝗻+i],
              b = 𝗰[3*𝗻+i],
              C = 𝗰[4*𝗻+i],
              c = 𝗰[5*𝗻+i],
              D = 𝗰[6*𝗻+i],
              d = 𝗰[7*𝗻+i],
              E = 𝗰[8*𝗻+i],
              e = 𝗰[9*𝗻+i];
            𝗰[0*𝗻+i] = E;                                                       // Swap
            𝗰[1*𝗻+i] = D;
            𝗰[2*𝗻+i] = C;
            𝗰[3*𝗻+i] = B;
            𝗰[4*𝗻+i] = A;
            𝗰[5*𝗻+i] = e;
            𝗰[6*𝗻+i] = d;
            𝗰[7*𝗻+i] = c;
            𝗰[8*𝗻+i] = b;
            𝗰[9*𝗻+i] = a;
           }
         }
       }
      void tracingVertices()                                                    // If tracing rotation is in effect - copy and rotate the drawing around the centre into the upper rotation block
       {if (!makeTracing) return;                                               // No tracing rotation present
        final int 𝗻 = rotationBlockSize*coordsPerVertexPair/2, n = 𝗻 * 2;       // Number of coordinates, distance to copy and rotate
        final float[]v = vertices;                                              // Shorten name         1 2     21
        final float  a = 𝞈Rotation.angle, 𝘅 = cd(a), 𝘆 = sd(a),                 // Unit vector of rotation
          X = f(tracingRotationCentreX, cx), Y = f(tracingRotationCentreY, cy), // Coordinates of centre of rotation
          dx = 𝞈Translation.aldx, dy = 𝞈Translation.aldy;                       // Translation tracker
        final int m = mirrorBlockSize;                                          // Number of vertices in a mirror block
        for(int i = 0; i < 𝗻; ++i)                                              // Each coordinate pair - OPT: use a matrix instead of this loop
         {if (doNotTrace[(i%m)/2]) continue;                                    // Skip tracing of vertex if not traced
          final float x = v[2*i], y = v[2*i+1], 𝕩 = x - X, 𝕪 = y - Y;           // Vector from centre
          v[n+2*i] = X + 𝘅*𝕩 - 𝘆*𝕪 + dx; v[n+2*i+1] = Y + 𝘅*𝕪 + 𝘆*𝕩 + dy;       // Vector rotated around centre and translated if required
         }
       }
      void tracingColours()                                                     // If tracing rotation is in effect - duplicate the colour block
       {if (!makeTracing) return;                                               // No rotation present
        final int  n = rotationBlockSize*2;                                     // Number of entries to be rotated
        final int[]c = colours;                                                 // Shorten name
        for(int i = 0; i < n; ++i) c[n+i] = c[i];                               // Duplicate colours - OPT: use System.arraycopy instead
        if (tracingRotationOpacity != 1)                                        // Multiply in rotation opacity if present
         {final float o = tracingRotationOpacity;                               // Overall opacity to be multiplied in
          if (o == 0) {for(int i = 0; i < n; ++i) c[n+i] &= 0x00ffffff;}        // Special case - zero
          else
           {for(int i = 0; i < n; ++i)                                          // Multiply opacity
             {final int 𝗰 = c[n+i], a = i((𝗰>>24)*o)<<24;                       // New opacity
              c[n+i] = (𝗰&0x00ffffff)|a;                                        // Set opacity
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
// Create mirrors
//------------------------------------------------------------------------------
      𝝫 createMirror(int n) {return createMirror(n == 1 ? 𝞈1 : 𝞈2, n);}         // Default mirror 1 - horizontal, 2 - vertical, two sided
      𝝫 createMirror(final 𝝮 𝞇, int n)                                          // Horizontal(1) or vertical(2) mirror at centre, two sided
       {return createMirror(𝞇, n, cx, cy, n == 1 ? 0 : 90, true, true, true);
       }
      𝝫 createMirror(final 𝝮 𝞇, int n, float a)                                 // Mirror at centre with angle(a) and specified tracker
       {return createMirror(𝞇, n, cx, cy, a, true, true, true);
       }
      𝝫 createMirror(int n, float X, float Y, float a)                          // Mirror from polar coordinates with no tracker, two sided
       {return createMirror(null, n, X, Y, a, true, true, true);
       }
      𝝫 createMirror(int n, float X, float Y, float a, boolean both)            // Mirror from polar coordinates with no tracker, one or two sided
       {return createMirror(null, n, X, Y, a, both, true, true);
       }
      𝝫 createMirror(𝝮 𝞇, int n, float X, float Y, float a)                     // Mirror from polar coordinates origin at point (X, Y), angle(a), two sided
       {return createMirror(𝞇, n, X, Y, a, true, true, true);
       }
      𝝫 createMirror(final 𝝮 𝞇, final int n, final float X, final float Y,      // Mirror from polar coordinates origin at point (X, Y), angle(a) and one or two sided
        final float a,                                                          // Angle of mirror
        final boolean both,                                                     // Both sides reflect
        final boolean reflectReal, final boolean reflectReflections)            // Reflect real vertices, reflect reflected vertices
       {final int r = mirrorsRequired();
        if ((r & n) == 0) return null;                                          // Mirror not required
 //     final float 𝗮 = a + (𝞇 != null ? 𝞇.angle : 0);                          // Mirror has no rotation tracker
        final float 𝗮 = a;                                                      // Mirror rotation
        final 𝝫 𝞅 = new 𝝫(X, Y, X + cd(𝗮), Y + sd(𝗮),                           // Create the reflector of the mirror
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
         }};
        return 𝞅;                                                               // Return created mirror
       }
//------------------------------------------------------------------------------
// Selected item
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
        𝝮 𝞈;                                                                    // Drag rotation angle around point(x,y)
        Float 𝕩, 𝕪;                                                             // Centre of rotation in x,y uses point(x,y) if not set
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
               {if (inside(x, y, p[i+0],p[i+1],p[i+2],p[i+3],p[i+4],p[i+5]))    // See if the point is inside the current triangle
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
        final int nVertexPairsPerSide = 64;                                     // Number of vertex pairs per side
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
            float r0() {return super.r0() / (radius() ? 2f : 1f);}
            float r1() {return super.r1() / (radius() ? 2f : 1f);}
            float x()  {return super.x() + (radius() ? super.r0()/2f * cd(super.a()) : 0f);}
            float y()  {return super.y() + (radius() ? super.r0()/2f * sd(super.a()) : 0f);}
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
      abstract class PolyArea extends Item                                      // Polygonal Area
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
// Special purpose items - tracing controllers
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
          x = 𝘅; y = 𝘆;                                                         // Position of the controller
          𝕩 = f(X, cx);    𝕪 = f(Y, cy);                                        // Centre of rotation
          R = rotationTracingControllerRadius();                                // Size of the controller
          c1 = 𝝺sb; c2 = 𝝺sc;                                                   // The colours of the controller
          reflect = false;                                                      // Do not reflect the controller
          tracingRotationOpacity = opacity;                                     // Opacity of the drawing
        }};
       }
      float rotationTracingControllerRadius() {return 4*outerThickness();}      // Size of the tracing rotation controller

      void pointerPressed () {}                                                 // Pointer pressed
      void pointerDragged () {}                                                 // Pointer dragged
      void pointerReleased() {}                                                 // Pointer released

      void underlay() {}                                                        // Highlights drawn under the drawing
      void overlay () {}                                                        // Highlights drawn over the drawing

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
    boolean intersection(PointF p,                                              // Intersection of two lines loaded into point(p)
      float x11, float y11, float x12, float y12,
      float x21, float y21, float x22, float y22)
     {final float x = x11 - x21, 𝘅 = x12 - x11, 𝕩 = x22 - x21,
                  y = y11 - y21, 𝘆 = y12 - y11, 𝕪 = y22 - y21,
                  d = 𝘆*𝕩 - 𝘅*𝕪;
      if (a(d) < 1e-6) return false;                                            // Points too close relative to intersection
      final float l = (x*𝕪 - y*𝕩) / d;                                          // Fraction to intersection
      p.set(x11 + l*𝘅, y11 + l*𝘆);                                              // Load result
      return true;                                                              // Result is valid
     }
    Float pointToLine(float x, float y, float 𝘅, float 𝘆, float 𝕩, float 𝕪)     // Signed distance from point(x,y) to line through point(𝘅,𝘆) and point(𝕩,𝕪). The sign is positive when the point is to the right of the line when the observer stands at point(𝘅,𝘆) and looks at point(𝕩,𝕪)
     {final float lx = 𝕩 - 𝘅, ly = 𝕪 - 𝘆, d = d(lx, ly);                        // Vector along line, length of vector
      if (d < 1e-6) return null;                                                // Complain if line is not well  defined - a fraction less than 1 is adequate as we measure in pixels
      return (-ly*x + ly*𝘅 + lx*y - lx*𝘆) / d;                                  // Signed distance from line
     }
    boolean pointToLine(PointF 𝗽, float x, float y,                             // Sets vector(p) to point from the point(x,y) to the nearest point on the line through point(𝘅,𝘆) and point(𝕩,𝕪)
      float 𝘅, float 𝘆, float 𝕩, float 𝕪)
     {final float lx = 𝕩 - 𝘅, ly = 𝕪 - 𝘆, d = d(lx, ly);                        // Vector along line, length of vector
      if (d < 1e-6) return false;                                               // Complain if line is not well  defined - a fraction less than 1 is adequate as we measure in pixels
      final float 𝗱 = (-ly*x + ly*𝘅 + lx*y - lx*𝘆) / (d*d);                     // Signed fraction of line segment length giving distance from line to point
      𝗽.x = ly*𝗱; 𝗽.y = -lx*𝗱;                                                  // Vector from point to nearest point on the defined line
      return true;                                                              // Result is valid
     }
    boolean inside(float X, float Y, float x, float y,                          // Test whether point(X,Y) is inside the triangle formed by points: (x,y), (𝘅, 𝘆), (𝕩, 𝕪)
                   float 𝘅, float 𝘆, float 𝕩, float 𝕪)
     {final Float a = pointToLine(X, Y, x, y, 𝘅, 𝘆);                            // Direction of point from each line
      final Float b = pointToLine(X, Y, 𝘅, 𝘆, 𝕩, 𝕪);
      final Float c = pointToLine(X, Y, 𝕩, 𝕪, x, y);
      if (a == null || b == null || c == null) return false;
      return (a > 0 && b > 0 && c > 0) || (a < 0 && b < 0 && c < 0);            // Directions must all be the same to be inside
     }
    float angle(float x, float y, float 𝘅, float 𝘆)                             // Angle of line to point(x,y) from point(𝘅,𝘆) measured in degrees clockwise from the x axis returned as a result in the range 0 to 360
     {return nd(at(x - 𝘅, y - 𝘆));
     }
    float angle(float x, float y, float 𝘅, float 𝘆, float 𝕩, float 𝕪)           // Angle of line from point(𝕩,𝕪) through point(𝘅,𝘆) to point(x,y) measured in degrees clockwise from the x axis returned as a result in the range 0 to 360
     {return nd(at(𝕩 - 𝘅, 𝕪 - 𝘆) - at(x - 𝘅, y - 𝘆));
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
      {{new Rectangle (){{n = "R1"; x = w/4; y =   h/4; r = 0.2f*w; R = 0.2f*h; /*a = 360*f*/; c1 = 𝝺sc; c2 = 𝝺sg;}};
//      new Rectangle (){{n = "R2"; x = w/2; y = 3*h/4; r = 0.4f*w; R = 0.2f*h; /*a = 360*f*/; c1 = 𝝺sc; c2 = 𝝺sg;}};
        createMirror(1);                                                        // Mirror - x axis
        createMirror(2);                                                        // Mirror - y axis
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
// Animated drawing
//------------------------------------------------------------------------------
  abstract class AnimatedDrawing extends DisplayDrawing
   {final 𝝮 fore = new 𝝮("fore"), back = new 𝝮("back"), back2 = new 𝝮("back2"); // Effect of dragging
    float target = 180, targetTime = 0;                                         // Target in degrees, time spent in proximity to target
    double lastDrawTime = T();                                                  // Time of last draw in seconds

    AnimatedDrawing(final Activity Activity){super(Activity.this);}             // Create animated drawing

    void pointerPressed() {fore.speed = 0;}                                     // Pointer pressed stops rotation of upper items

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {} // Recentre on change of orientation

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
// Reflect a line in a mirror to make a snowflake when the original, the mirror
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
// Two equilateral triangles reflected in two mirrors make a hexagon
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
// Reflected diameter
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
// Isosceles Triangle
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
// Half Angle
//------------------------------------------------------------------------------
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
// Arrow head
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
// Tick - arrow head off to the side
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
// Parallel lines preserve angles
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
// Interior Angles of a Triangle make a Line
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
// The Theorem of Pythagoras - see tests/Pythagoras.dxf
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
// The Schwartz inequality - a line is the shortest path between two points
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
// Congruent triangles
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
// Draggable triangle: basis for drawings of triangles
//------------------------------------------------------------------------------
  class DraggableTriangle extends DisplayDrawing
   {final PointF 𝗽 = new PointF();                                              // Intersection point
    float fx1() {return -0.45f;} float fy1() {return fx1();}                    // Fractional offset off initial position from centre
    float fx2() {return -fx1();} float fy2() {return 0;}
    float fx3() {return  fx1();} float fy3() {return -fy1();}

    int 𝝺t11() {return 𝝺sV;} int 𝝺t12() {return 𝝺ss;}                           // Angle colours
    int 𝝺t21() {return 𝝺sg;} int 𝝺t22() {return 𝝺sr;}
    int 𝝺t31() {return 𝝺sb;} int 𝝺t32() {return 𝝺y;}

    int 𝝺d11() {return 𝝺sr;} int 𝝺d12() {return 𝝺sg;} int 𝝺d1() {return 𝝺w;}    // Diameter colours = colours of sides
    int 𝝺d21() {return 𝝺sb;} int 𝝺d22() {return 𝝺sr;} int 𝝺d2() {return 𝝺w;}
    int 𝝺d31() {return 𝝺sg;} int 𝝺d32() {return 𝝺sb;} int 𝝺d3() {return 𝝺w;}

    DraggableTriangle(final Activity Activity) {super(Activity.this);}          // Create display
    Drawing loadDrawing()                                                       // Load the drawing
     {final float
        x1 = r(0, w, cx+fx1()*w+𝞈1.aldx),   y1 = r(0, h, cy+fy1()*h+𝞈1.aldy),   // Top left
        x2 = r(0, w, cx+fx2()*w+𝞈2.aldx),   y2 = r(0, h, cy+fy2()*h+𝞈2.aldy),   // Middle right
        x3 = r(0, w, cx+fx3()*w+𝞈3.aldx),   y3 = r(0, h, cy+fy3()*h+𝞈3.aldy),   // Bottom left
        𝗥  = m(w,h)/4,                                                          // Radius of drag tabs
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

      final float[]𝘁 = new float[]{𝗮, 𝗯, 𝗰, x1, y1, x2, y2, x3, y3};            // Parameter list to overrides
      final 𝝮 𝞃 = translate() ? 𝞈Translation : null;                            // Allow translation or not

      return new Drawing()                                                      // Create the drawing
       { {underlayItems(this, 𝘁);                                               // Override to draw items under the triangle
          new Diameter(){{n="d1"; c1=𝝺d11(); c2=𝝺d12(); C=𝝺d1(); 𝞈=𝞃; bp(x1, y1, x3, y3);}};   // Sides
          new Diameter(){{n="d2"; c1=𝝺d21(); c2=𝝺d22(); C=𝝺d2(); 𝞈=𝞃; bp(x1, y1, x2, y2);}};
          new Diameter(){{n="d3"; c1=𝝺d31(); c2=𝝺d32(); C=𝝺d3(); 𝞈=𝞃; bp(x2, y2, x3, y3);}};
          new Tab     (){{n="t1"; c1=c11; c2=c12; 𝞈=𝞈1;x=x1;y=y1;A=𝗮;a=𝕒;R=𝗥; reflect=false;}};// Drag tabs
          new Tab     (){{n="t2"; c1=c21; c2=c22; 𝞈=𝞈2;x=x2;y=y2;A=𝗯;a=𝕓;R=𝗥; reflect=false;}};
          new Tab     (){{n="t3"; c1=c31; c2=c32; 𝞈=𝞈3;x=x3;y=y3;A=𝗰;a=𝕔;R=𝗥; reflect=false;}};
          overlayItems(this, 𝘁);                                                // Override to draw items over the triangle
         }
        void underlay       () {underlay       (𝘁);}                            // Call to overridable underlay() method
        void overlay        () {overlay        (𝘁);}                            // Call to overridable overlay() method
        void pointerReleased() {pointerReleased(𝘁);}                            // Call to overridable pointerReleased() method
       };
     }
    void underlay                (final float[]𝘁) {}                            // Underlay drawing
    void overlay                 (final float[]𝘁) {}                            // Overlay drawing
    void pointerReleased         (final float[]𝘁) {improve(𝘁);}                 // Pointer released - make isosceles or equilateral if close to same
    void underlayItems(Drawing 𝗱, final float[]𝘁) {}                            // Override to add additional items to be drawn under the triangle
    void overlayItems (Drawing 𝗱, final float[]𝘁) {}                            // Override to add additional items to be drawn on top of the triangle
    boolean translate() {return false;}                                         // Allow translation
    boolean improve(final float[]𝘁)                                             // Make equilateral, right angles or isosceles if close to same
     {final float 𝗮=𝘁[0], 𝗯=𝘁[1], 𝗰=𝘁[2],
        x1=𝘁[3], y1=𝘁[4], x2=𝘁[5], y2=𝘁[6], x3=𝘁[7], y3=𝘁[8];
      if      (isEqui(𝗯, 𝗰, x2,y2, x3,y3))        i1();                         // Move to improve equilateral triangle
      else if (isEqui(𝗮, 𝗰, x3,y3, x1,y1))        i2();
      else if (isEqui(𝗮, 𝗯, x1,y1, x2,y2))        i3();
      else if (isRIso(𝗯, 𝗰, x2,y2, x3,y3))        i1();                         // Move to improve right angle triangle - isosceles
      else if (isRIso(𝗮, 𝗰, x3,y3, x1,y1))        i2();
      else if (isRIso(𝗮, 𝗯, x1,y1, x2,y2))        i3();
      else if (isRA  (𝗯, x1,y1, x2,y2, x3,y3))    i1();                         // Move to improve right angle triangle - half isosceles
      else if (isRA  (𝗰, x2,y2, x3,y3, x1,y1))    i2();
      else if (isRA  (𝗮, x3,y3, x1,y1, x2,y2))    i3();
      else if (isIso (𝗯, 𝗰, x1,y1, x2,y2, x3,y3)) i1();                         // Move to improve isosceles triangle
      else if (isIso (𝗮, 𝗰, x2,y2, x3,y3, x1,y1)) i2();
      else if (isIso (𝗮, 𝗯, x3,y3, x1,y1, x2,y2)) i3();
      else return false;                                                        // Unimproved
      return true;                                                              // Improved
     }
    boolean isEqui(float 𝗮, float 𝗯, float x2, float y2, float x3, float y3)    // If close enough to an equilateral triangle, then move to point(p) to improve it. Angles from base, line of base
     {if (nearAngle(𝗮, 𝗯) > 1) return false;                                    // Not close enough
      final boolean e1 = nearAngle(𝗮,60) < 1, e2 = nearAngle(𝗮,300) < 1;        // Possible equilateral triangle angles
      if (!e1 && !e2) return false;                                             // Not close enough to equilateral triangle angles
      final float a = (e1 ? +1: -1) * sq(3f)/2f;                                // Direction of apex from base
      𝗽.x = (x2+x3)/2 + a*(y3-y2); 𝗽.y = (y2+y3)/2 - a*(x3-x2);                 // Desired position of apex of equilateral triangle
      achieved(this, "Equilateral");                                            // Achieved equilateral triangle
      return true;                                                              // Improved equilateral triangle
     }
    boolean isRIso(float 𝗮, float 𝗯, float x2, float y2, float x3, float y3)    // If close enough to a right angle triangle, then move to point(p) to improve it. Angles from base, line of base
     {if (nearAngle(𝗮, 𝗯) > 1) return false;                                    // Not close enough
      final boolean e1 = nearAngle(𝗮,45) < 1, e2 = nearAngle(𝗮,315) < 1;        // Possible right angles triangles
      if (!e1 && !e2) return false;                                             // Not close enough to right angle triangle angles
      final float a = (e1 ? +1: -1) / 2f;                                       // Direction of apex from base
      𝗽.x = (x2+x3)/2 + a*(y3-y2); 𝗽.y = (y2+y3)/2 - a*(x3-x2);                 // Desired position of apex of equilateral triangle
      achieved(this, "IsoscelesRightAngle");                                    // Achieved isosceles right angle
      return true;                                                              // Improved equilateral triangle
     }
    boolean isRA(float 𝗯,                                                       // If close enough to a right angle triangle, then move to point(p) to improve it. Angles from base, line of base
      float x1, float y1, float x2, float y2, float x3, float y3)               // Corner being considered, corresponding opposite side
     {if (nearAngle(𝗯, 90) > 1 && nearAngle(𝗯, 270) > 1) return false;          // Not close enough to right angle triangle angles
      if (!pointToLine(𝗽, x1,y1, x2,y2, x3,y3)) return false;                   // Points to close to be useful
      𝗽.x = x2-𝗽.x; 𝗽.y = y2-𝗽.y;                                               // Desired position of point
      achieved(this, "RightAngle");                                             // Achieved right angle
      return true;                                                              // Improved equilateral triangle
     }
    boolean isIso(float 𝗮, float 𝗯,                                             // If close enough to an isosceles triangle, then move to point(p) to improve it
      float x1, float y1, float x2, float y2, float x3, float y3)               // Corner being considered, corresponding opposite side
     {if (nearAngle(𝗮, 𝗯) > 1) return false;                                    // Not close enough
      if (!pointToLine(𝗽, x1,y1, x2,y2, x3,y3)) return false;                   // Try to improve an almost isosceles triangle Vector from apex to base
      𝗽.x = (x2+x3)/2-𝗽.x; 𝗽.y = (y2+y3)/2-𝗽.y;                                 // Desired position of apex of isosceles triangle
      achieved(this, "Isosceles");                                              // Achieved isosceles triangle
      return true;                                                              // Improved isosceles triangle
     }
    void i1() {𝞈1.aldx = 𝗽.x-cx - fx1()*w; 𝞈1.aldy = 𝗽.y-cy - fy1()*h;}         // Improve corner 1
    void i2() {𝞈2.aldx = 𝗽.x-cx - fx2()*w; 𝞈2.aldy = 𝗽.y-cy - fy2()*h;}         // Improve corner 2
    void i3() {𝞈3.aldx = 𝗽.x-cx - fx3()*w; 𝞈3.aldy = 𝗽.y-cy - fy3()*h;}         // Improve corner 3
   } // DraggableTriangle
//------------------------------------------------------------------------------
// Circle through three points
//------------------------------------------------------------------------------
// Add centre of normals though the angles
  class Circle3Points extends DraggableTriangle
   {final RectF s1 = new RectF(), s2 = new RectF(), s3 = new RectF();           // Angle bisectors
    final RectF n1 = new RectF(), n2 = new RectF(), n3 = new RectF();           // Side normals
    final PointF 𝗽 = new PointF();                                              // Intersection point
    Circle3Points(final Activity Activity) {super(Activity.this);}              // Create display

    void overlay(final float[]𝘁)                                                // Overlay normal to each diameter
     {final float 𝗮=𝘁[0],  𝗯=𝘁[1], 𝗰=𝘁[2],
        x1=𝘁[3], y1=𝘁[4], x2=𝘁[5], y2=𝘁[6], x3=𝘁[7], y3=𝘁[8];
      final int t = 20;                                                         // Thickness of line used to draw resulting circles
      fSide (n1, x1,y1, x2,y2);                                                 // Find bisector of each side
      fSide (n2, x2,y2, x3,y3);
      fSide (n3, x3,y3, x1,y1);
      fAngle(s1, x1,y1, x2,y2, x3,y3);                                          // Find bisector of each angle
      fAngle(s2, x2,y2, x3,y3, x1,y1);
      fAngle(s3, x3,y3, x1,y1, x2,y2);
      final Canvas c = canvas; final Paint p = paint;                           // Shorten names
      if (intersectionPoint(n1, n2))                                            // Draw exterior circle - centre will be at 𝗽
       {final float r = d(𝗽.x - x1, 𝗽.y - y1);                                  // Radius of circle through corners
        setPaint(𝝺y, t);                                                        // For bigCircle()
        if (bigCircle(𝗮, r, x3,y3, x1,y1, x2,y2) ||                             // Big radius circle draw - skia graphics fail for large radii
            bigCircle(𝗯, r, x1,y1, x2,y2, x3,y3) ||
            bigCircle(𝗰, r, x2,y2, x3,y3, x1,y1)) {}
        else                                                                    // Normal circle draw
         {setPaint(𝝺y);    dSide(n1, r); dSide(n2, r); dSide(n3, r);
          setPaint(𝝺y, t); c.drawCircle(𝗽.x, 𝗽.y, d(𝗽.x-x1, 𝗽.y-y1), p);
         }
       }
      if (intersectionPoint(s1, s2))                                            // Draw interior circle
       {setPaint(𝝺r);
        dAngle(𝗽, x1,y1); dAngle(𝗽, x2,y2); dAngle(𝗽, x3,y3);
        final Float d = pointToLine(𝗽.x, 𝗽.y, x1, y1, x2, y2);
        setPaint(𝝺r, t); c.drawCircle(𝗽.x, 𝗽.y, d, p);
       }
     }
    boolean bigCircle(float 𝗮, float 𝗿,                                         // Draw a segment of a big circle spanning an angle of 𝗮  with radius 𝗿
      float x1, float y1, float ax, float ay, float x2, float y2)               // starting at point(x1,y1), centred at point(ax,ay), finishing at point (x2,y2)
     {final int N = 100;                                                        // Number of line segments
      if (nearAngle(𝗮, 180) > 1) return false;                                  // Not a big circle around this angle
      final float a = (2*𝗮-360)/N, b = angle(x1, y1, 𝗽.x, 𝗽.y);                 // Interior angle step (avoid division in loop), start angle
      final float[]L = new float[4*(N+1)];                                      // Line segments
      L[0] = x1; L[1] = y1;                                                     // Duplicate first line to avoid if statement in following loop
      final float cx = 𝗽.x, cy = 𝗽.y;                                           // Centre of circle optimized for easy access
      for(int i = 1; i <= N; ++i)                                               // Load line segments
       {final float A = b + i * a, dx = 𝗿*cd(A), dy = 𝗿*sd(A);
        L[4*i+0] = L[4*i-2] = cx+dx;                                            // Start of this line segment is the same as the end of the last segment
        L[4*i+1] = L[4*i-1] = cy+dy;
       }
      L[4*N+2] = x2; L[4*N+3] = y2;                                             // Duplicate first line to avoid if statement in following loop
      canvas.drawLines(L, paint);                                               // Paint lines with one subroutine call
      return true;
     }
    void fSide(RectF 𝗻, float x1, float y1, float x2, float y2)                 // Find line (n) at right angles through the centre of the specified line
     {final float x = x2 - x1, y = y2 - y1, 𝕩 = y, 𝕪 = -x;
      𝗻.set(x1+x/2, y1+y/2, x1+x/2-𝕩, y1+y/2-𝕪);                                // Normal
     }
    void fAngle(RectF 𝗯,                                                        // Find line(b) which bisects the angle at point(x2,y2)
      float x1, float y1, float x2, float y2, float x3, float y3)
     {final float                                                               // Split angle
        a = angle(x3,y3, x2,y2, x1,y1),                                         // Angle sweep
        b = angle(x3,y3, x2,y2),                                                // Angle position
        c = a/2+b;                                                              // Angular direction of bisector
      𝗯.set(x2, y2, x2+cd(c), y2+sd(c));                                        // Bisector
     }
    boolean intersectionPoint(RectF l, RectF L)                                 // Find intersection of two lines expressed as rectangles
     {return intersection(𝗽, l.left,l.top,l.right,l.bottom,
                             L.left,L.top,L.right,L.bottom);
     }
    void dSide(RectF 𝗿, float r)                                                // Draw bisection of each side from centre point(p) through half of each side(𝗿) to circle radius(r)
     {final float x = 𝗿.left - 𝗽.x, y = 𝗿.top - 𝗽.y, d = d(x, y);               // Vector from circle centre to side, length of vector
      if (d < 1) return;                                                        // So close there is no need to draw a line
      canvas.drawLine(𝗽.x, 𝗽.y, 𝗽.x + r * x / d, 𝗽.y + r * y / d, paint);       // Line from centre of circle to circumference
     }
    void dAngle(PointF p, float x, float y)                                     // Draw bisection of each angle
     {canvas.drawLine(p.x, p.y, x, y, paint);
     }

    void pointerReleased(final float[]𝘁)                                        // Pointer released - make isosceles or equilateral if close to same
     {final float 𝗮=𝘁[0], 𝗯=𝘁[1], 𝗰=𝘁[2];                                       // Angles
      improve(𝘁);
      if (nearAngle(180, 𝗮) < 1 ||
          nearAngle(180, 𝗯) < 1 ||
          nearAngle(180, 𝗰) < 1) achieved(this, "Line");                        // Achieved straight line
     }
   } // Circle3Points
//------------------------------------------------------------------------------
// Quarter Triangles: show quarter triangles generated by halving the sides and
// by reflecting the apex
//------------------------------------------------------------------------------
  class QuarterTriangles extends DraggableTriangle
   {float fx1() {return  0.20f;} float fy1() {return -0.45f;}                   // Fractional offset off initial position from centre
    float fx2() {return  0.45f;} float fy2() {return  0.45f;}
    float fx3() {return -0.45f;} float fy3() {return  0.45f;}

    int 𝝺d21() {return 𝝺m;} int 𝝺d22() {return 𝝺y;}                             // Diameter colour for the all important side 2 background

    QuarterTriangles(final Activity Activity) {super(Activity.this);}           // Create display
    void underlayItems(Drawing 𝗱, final float[]𝘁)                               // Override to add additional items to be drawn under the triangle
     {final float 𝗮=𝘁[0],  𝗯=𝘁[1],  𝗰=𝘁[2],
        x1=𝘁[3], y1=𝘁[4], x2=𝘁[5], y2=𝘁[6], x3=𝘁[7], y3=𝘁[8],
        x12 = (x1+x2)/2,   y12 = (y1+y2)/2,                                     // Halfway points
        x31 = (x3+x1)/2,   y31 = (y3+y1)/2,
        𝕩   = (x12+x31)/2, 𝕪   = (y12+y31)/2,
        a   = angle(x3, y3, x2, y2);                                            // Mirror parallel to third side, half way down the other two sides
      𝗱.createMirror(null, 1, 𝕩, 𝕪, a,    false, true, false);                  // Fixed mirror parallel to third side, half way down the other two sides reflects only real vertices in front of it
      𝗱.createMirror(null, 2, 𝕩, 𝕪, a+90, false, false, true);                  // Fixed mirror at right angles to the mirror above to create central triangle - only reflects reflected points
     }
    void pointerReleased(final float[]𝘁)                                        // Pointer released
     {final float 𝗮=𝘁[0], 𝗯=𝘁[1], 𝗰=𝘁[2],
        x1=𝘁[3], y1=𝘁[4], x2=𝘁[5], y2=𝘁[6], x3=𝘁[7], y3=𝘁[8],
        x21 = (x2-x1)/2, y21 = (y2-y1)/2,                                       // Halfway point vectors
        x32 = (x3-x2)/2, y32 = (y3-y2)/2,
        x13 = (x1-x3)/2, y13 = (y1-y3)/2,
        𝘅 = 𝞈Translation.aldx, 𝘆 = 𝞈Translation.aldy;                           // Translation in effect
      if (translationControllerSelected())
       {if (pr(x21, y21) || pr(-x21, -y21) ||
            pr(x32, y32) || pr(-x32, -y32) ||
            pr(x13, y13) || pr(-x13, -y13)) {}
       }
      else improve(𝘁);                                                          // Improve triangle
     }
    boolean pr(float x, float y)                                                // Pointer released
     {final float 𝘅 = 𝞈Translation.aldx, 𝘆 = 𝞈Translation.aldy;                 // Translation in effect
      if (d(𝘅-x, 𝘆-y) > outerThickness()) return false;                         // Close enough to a matching triangle
      𝞈Translation.aldx = x;
      𝞈Translation.aldy = y;
      return true;
     }
    void overlay(final float[]𝘁)                                                // Override to add additional items to be drawn under the triangle
     {overlay(𝘁, 𝝺r, 0, 0);
      overlay(𝘁, 𝝺y, 𝞈Translation.aldx, 𝞈Translation.aldy);
     }
    void overlay(final float[]𝘁, int colour, float dx, float dy)                // Draw the quarter triangles in the indicated colour, possibly shifted by the translation(dx,dy)
     {final float 𝗮=𝘁[0], 𝗯=𝘁[1], 𝗰=𝘁[2],
        x1=dx+𝘁[3], y1=dy+𝘁[4], x2=dx+𝘁[5], y2=dy+𝘁[6], x3=dx+𝘁[7], y3=dy+𝘁[8],
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
  float   sr(float a)           {return f(Math.sin(a));}                        // Sin of radians as float
  float   cr(float a)           {return f(Math.cos(a));}                        // Cos of radians as float
  float   ac(double x, double h){return f(Math.toDegrees(Math.acos(x / h)));}   // acos() in degrees
  float   as(double y, double h){return f(Math.toDegrees(Math.asin(y / h)));}   // asin() in degrees
  float   at(double x, double y){return f(Math.toDegrees(Math.atan2(y, x)));}   // atan() in degrees - two argument form - anticlockwise between -𝝿 and +𝝿 .
  float   os(double s, double h){return f(Math.sqrt(h*h-s*s));}                 // Other side of right angle triangle
  float   nd(float a)  {final float  A = a % 360f; return A >= 0 ? A : 360+A;}  // Normalize degrees to [0,360)
  double  nd(double a) {final double A = a % 360f; return A >= 0 ? A : 360+A;}  // Normalize degrees to [0,360)
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
