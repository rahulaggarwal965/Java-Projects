### 3D Engine Design

#### I. Introduction

3D Computer Graphics have become an almost ubiquitous part of modern technology, with applications in games, architectural design, molecular and physical simulation, astronomy, 3D modeling in CAD, and many more.

However, it is important to note that with the rise of 3D computer graphics into mainstream attention, the technology has been continuously abstracted to higher and higher levels, which while important to expanding use cases to the general population, has also meant that the underlying principles–the math, the linear algebra, the rasterization principles, clipping, and the different screen spaces that have become commonalities between the multiple graphics rendering pipelines like OpenGL, DirectX, Vulkan, etc.

The goal of this project was to start from just the ability to render a pixel on a window and go through all the aforementioned techniques from scratch, ending up with a 3D software rendering pipeline I could not only use, but also understand and optimize on the lowest level possible. Also, I thought the project seemed incredibly interesting, as I saw how it could be used for procedural rendering based on data taken from the real world to create simulations for non-human actors to interact with.

---

#### II. A High-level View

<video width="640" height="360" autoplay loop controls><source src="media/test.m4v" type="video/mp4"></video>

**Where does the Software 3D Engine start?**

Well, in this case, in order to render things in three dimensions, we must first do so in two dimensions. The two dimensional rendering system is quite intuitive, as the window itself is defined in two dimensions-it has a width and a height. One of the first things we can do is consider how we can draw a line to the screen.

Computer screens have a certain granularity to them, commonly referred to as the resolution of the screen: How many pixels wide and how many pixels tall. However, as we know from math, lines are defined by **two points**, meaning that from a mathematical perspective, they are infinitesimally thin; we should not be able to see them. We can solve that problem, however, by utilizing a technique called *rasterization*. The details will be left further down, but the basic idea can be observed here:

![](media/line_rasterization.png)

Essentially, we take that line and fill in the pixels that the line *intersects* with, and because the pixels are so small, we are left with a line that is disjointed when observed from very close distances, but smooth to the naked eye.

From there we can begin to draw shapes to the screen; specifically, we want to be able to render any n-sided polygon, and fill the contained pixels with the color of our choice. One way to do this would be to define a unique rendering function for every shape we need:
> For example, if we were rendering a rectangle/square, we could start from the top left corner of the rectangle and draw **width** many pixels across and **height** many pixels down.

However

From there, the next step is to draw shapes to the screen, specifically **polygons**. Now, if all we were doing was making a two dimensional game engine, the most optimal way to render any shape would be unique to that shape. For example, if we were rendering a rectangle/square, we could easily start from the top left corner of the rectangle, and draw *width* many pixels across and *height* many pixels down. However, the complexity of creating rendering rules for the many shapes that exist is just not worth it when there is a better way:

We can split an *n-sided* polygon into

---

In terms of how I started this project, although I primarily use C++, I decided to use Java for its built-in cross platform capabilities with the JVM. Additionally, it included an entire library for creating a window and rendering to it, which was incredibly helpful. Although it does not offer the same level of performance or control (Generics) as C++, it provided a suitable platform for development.
