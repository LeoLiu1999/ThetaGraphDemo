# ThetaGraphDemo

Written by Leo Liu for Algorithms 2

This is a visualizer of the [Theta Graph](https://en.wikipedia.org/wiki/Theta_graph) constructed by examining every pair of vertices, resulting in a O(n^2) time construction. This can be improved to O(n log n) with a sweepline algorithm.

## Launch Instructions

### 1. Clone this repository

#### ssh:

`git clone git@github.com:LeoLiu1999/ThetaGraphDemo.git`

#### https:

`git clone https://github.com/LeoLiu1999/ThetaGraphDemo.git`

### 2. Compile and Run

```
cd ThetaGraphDemo
javac ThetaGraphDemo.java
java ThetaGraph
```

## Instructions

 * Left click to add vertices to the graph.
 * Select the number of sectors with dropdown. (Note: a valid k-Spanner is only generated with n >= 9)
 * Click `Construt Theta Graph` to begin.