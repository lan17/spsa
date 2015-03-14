

# Scene Tree #
## ANode ##

A scene tree is a way to represent a scene where each object is related to another spatially via parent/child relationship.  ANode class is super class to all other objects in a tree.  It stores position, scale and rotation.  However these values are always in relation to node's parent.

In this way it is easy to construct a fairly complex structure as a tree, then move parts of it by moving the root of that part, and having all children of this node move rigidly with the parent, as well as rotate or scale with the parent.

Here is an example of this concept: http://spsa.googlecode.com/files/racer.exe.  It was made in C++, but features the same idea as explained here (I actually ported SPSA from C++ into Java at some point).  This program recursively creates a tree of cubes, where each parent has 6 children which are smaller and have certain offset from the parent.

| Figure 1.  If you change spacial properties of parent (larger cube), it will affect spacial properties of all it's children as well. |
|:-------------------------------------------------------------------------------------------------------------------------------------|
| <img src='http://spsa.googlecode.com/files/fract.jpg' width='640' height='400'> </tbody></table>

ANode contains render method which performs OpenGL transformations based on ANode's rotation, translation and scale vectors.  This method should be called by child classes in their overloaded render method.<br>
<br>
<h2>Lights and Materials ##

SPSA contains wrapper classes to handle light sources and material properties.  LightNode inherits from ANode while Material class does not.  This is because location of a light source is important, but Material changes material properties globally, and it's position is irrelevant.

You should be mindful of both your light sources properties as well as material properties when trying to get correct visual effect.

To familiarize yourself with lights and material properties in OpenGL please read Red Book guide: http://glprogramming.com/red/chapter05.html