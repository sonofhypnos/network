# Network

This is the solution to my final assignment to my programming class at KIT. The main class `Network` allows to perform various operations on IP addresses arranged in a Forest (a set of trees). `Network` parses a description for trees of IP addresses in a lisp-like syntax and allows to perform various operations on its forest. For further information see the documentation in the source code.

Examples of tree descriptions are below. The first IP inside an [S-expression](https://en.wikipedia.org/wiki/S-expression) describes denotes the root of a subtree:
```
(85.193.148.81 141.255.1.133 34.49.145.239 231.189.0.127)
(141.255.1.133 122.117.67.158 0.146.197.108)
(231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77)
(85.193.148.81 (141.255.1.133 122.117.67.158 0.146.197.108) 34.49.145.239
(231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77))
```


