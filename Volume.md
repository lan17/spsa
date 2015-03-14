# Summary #

Lev has worked on this project in the summer of 2008. It is for visualizing scalar volume data.

features include:
  * Uses Java OpenGL and webstart. This means it can be run on a wide variety of OS from a single .jnlp/jar file.
  * Load binary floating point values files (that represent a 3d array) and visualize them using OpenGL's 3D texture feature.
  * Use Gaussian Shader (GLSL) to choose mean and standard deviation to isolate a certain range in the data to visualize. Note that this reefers to normalized data that is between [0,1].
  * Use linear shader to color different data regions with gradually different colors for more colorful pictures. (You cannot control the gradient right now. I should probably build it in).
  * Create iso-surfaces using marching cubes algorithm around the mean and standard deviation you chose in gaussian shader. This works pretty fast, but contains some bugs, specifically sometimes the direction of normal vectors are reveresed, producing undesirable effects. (I haven't figured out how to fix this problem yet.)
  * Use fixed function or per-pixel phong shading on generated surfaces.

# How to run #

Here is the webstart jnlp file - http://giga2.cs.ohiou.edu/~neiman/jstart/jung/volume.jnlp.

You will need a JVM of at least 1.6 and a video card capable of supporting GLSL shaders.

This should run on Windows XP/Vista, MacOS, Solaris and various versions of Linux.  If it doesn't and you encounter some problems please do not hesitate to write me at lev.neiman@gmail.com

# Screenshots #

The data that was used to generate the following screenshots has been taken from: http://graphics.stanford.edu/data/voldata/

| Human head using linear shader. | <img src='http://spsa.googlecode.com/files/skull_linear.jpg' width='640' height='400'> <table><thead><th> <a href='full_size_head_linear.md'>Full Resolution Screenshot</a> </th></thead><tbody>
<tr><td> Same data with Gaussian shader. </td><td> <img src='http://spsa.googlecode.com/files/skull.jpg' width='640' height='400'> </td><td> <a href='full_size_head_gauss.md'>Full Resolution Screenshot</a> </td></tr>
<tr><td> Iso surface generated about same mean/standard deviation as was used in above screenshot.  Uses fixed function OpenGL lighting. </td><td> <img src='http://spsa.googlecode.com/files/head_iso.jpg' width='640' height='400'> </td><td> <a href='full_size_head_iso.md'>Full Resolution Screenshot</a> </td></tr>
<tr><td> Teddy bear using low slice gauss shader. </td><td> <img src='http://spsa.googlecode.com/files/bear_gauss.jpg' width='640' height='400'> </td><td> <a href='full_size_bear_gauss.md'>Full Resolution Screenshot</a> </td></tr>
<tr><td> Iso surface generated from this teddy bear.  Uses per-pixel phong lighting. </td><td> <img src='http://spsa.googlecode.com/files/bear_iso.jpg' width='640' height='400'> </td><td> <a href='full_size_bear_iso.md'>Full Resolution Screenshot</a> </td></tr></tbody></table>

Next screenshots are of wavefront data.<br>
<br>
<table><thead><th> Using Gauss shader on wavefront with some imperfections inside of it. </th><th> <img src='http://spsa.googlecode.com/files/wave_front_gauss_1.png' width='640' height='400'> </th><th> <a href='full_size_wavefront_gauss1.md'>Full Resolution Screenshot</a> </th></thead><tbody>
<tr><td> Inside the wavefront.  Still using Gauss shader. </td><td> <img src='http://spsa.googlecode.com/files/Wave_front_gauss_inside.png' width='640' height='400'> </td><td> <a href='full_size_wave_front_inside1.md'>Full Resolution Screenshot</a> </td></tr>
<tr><td> Looking at the surface of the same wavefront. </td><td> <img src='http://spsa.googlecode.com/files/wavefront_surface.png' width='640' height='400'> </td><td> <a href='full_size_wave_front_surface.md'>Full Resolution Screenshot</a> </td></tr>
<tr><td> Iso surface about this wavefront with mean and deviation close to above's screenshots. </td><td> <img src='http://spsa.googlecode.com/files/wavefront_iso_surface2.png' width='640' height='400'> </td><td> <a href='full_size_wavefront_isosurface1.md'>Full Resolution Screenshot</a> </td></tr>
<tr><td> "Inverted" iso surface </td><td> <img src='http://spsa.googlecode.com/files/wave_front_iso_surface6.png' width='640' height='400'> </td><td> <a href='full_size_wavefront_isosurface2.md'>Full Resolution Screenshot</a> </td></tr>
<tr><td> 2 iso surfaces combined together along with the 3d texture itself, which "glows" from within the larger surface. </td><td> <img src='http://spsa.googlecode.com/files/wave_front_iso_gauss.jpg' width='640'> </td><td> <a href='full_size_wave_front_gauss_iso_surface.md'>Full Resolution Screenshot</a> </td></tr>