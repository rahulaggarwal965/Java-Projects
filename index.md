### 3D Engine Design

##### I. Introduction

3D Computer Graphics have become an almost ubiquitous part of modern technology, with applications in games, architectural design, molecular and physical simulation, astronomy, 3D modeling in CAD, and many more.

However, it is important to note that with the rise of 3D computer graphics into mainstream attention, the technology has been continuously abstracted to higher and higher levels, which while important to expanding use cases to the general population, has also meant that the underlying principles–the math, the linear algebra, the rasterization principles, clipping, and the different screen spaces that have become commonalities between the multiple graphics rendering pipelines like OpenGL, DirectX, Vulkan, etc.

The goal of this project was to start from just the ability to render a pixel on a window and go through all the aforementioned techniques from scratch, ending up with a 3D software rendering pipeline I could not only use, but also understand and optimize on the lowest level possible. Also, I thought the project seemed incredibly interesting, as I saw how it could be used for procedural rendering based on data taken from the real world to create simulations for non-human actors to interact with.

---

##### II. A High-level View

<video width-"320" height="180"><source src="../../Desktop/test.m4v"></video>

In terms of how I started this project, although I primarily use C++, I decided to use Java for its built-in cross platform capabilities with the JVM. Additionally, it included an entire library for creating a window and rendering to it, which was incredibly helpful. Although it does not offer the same level of performance or control (Generics) as C++, it provided a suitable platform for development.

