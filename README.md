# This project has been rewritten with much better code. Go check out [JavaPathtracer](https://github.com/adrian154/JavaPathtracer)

# java-pathtracer
A pathtracer written in Java.

## Features
- Unbiased Monte Carlo pathtracing
- Multithreaded rendering
- Some geometric primitives (spheres, planes)
- Mesh loading (limited OBJ support)
- Textures (but not yet for meshes)
- Live preview

## Usage

- `--primary-rays <number of primary rays>` - adjusts number of primary (eye) rays.
- `--secondary-rays <number of secondary rays>` - ditto, for secondary rays.
- `--nogui` - disables live preview.
- `--threads <number of threads>` - controls number of threads for rendering.

## Gallery

![image](https://i.imgur.com/F1XXFTq.png)

![image](https://i.imgur.com/JH117oj.png)

There's some holes in the bunny's shadow because of a pretty stupid bug I made. Long story short, just make all your planes double-sided.

![image](https://i.imgur.com/7pwz5Uq.png)

![image](https://i.imgur.com/KgyNIkD.png)

![image](https://i.imgur.com/pNEzsmA.png)

<!--
![image](https://i.imgur.com/H55EZgu.png)
-->

![image](https://i.imgur.com/SrznmQm.png)
