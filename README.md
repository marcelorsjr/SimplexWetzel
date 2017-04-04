# Simplex Wetzel

This is an implementation of the Simplex algorithm purposed by Wetzel. This project is part of the Systems Optimization course, offered in the program of computer science at the  Pontifical Catholic University of Minas Gerais, Brazil (PUC Minas).

## Getting Started
### Requirements

This project was written in Java, so you will need the JDK installed to compile the project.

### Development Environment

This is an eclipse project. So, after cloning the project, export to the pre-defined Eclipse Workspace, or get the .java files in the /src directory.

## Usage

Using is simple, just have to define, using the right format, your "Objective Function" and an array of "Restrictions", passing it to the “SimplexWetzel” constructor.

### Expressions format
* Objective Function
    * "MAX Ax1 + Bx2 + Cx3 ..."
    * "MIN Ax1 + Bx2 + Cx3 ..."
* Restriction
    * "Ax1 + Bx2 ... <= C"
    * "Ax1 + Bx2 ... >= C"

### Objective Function

The objective function only needs the formatted expression in its constructor
```java
public ObjectiveFunction(String typedObjectiveFunction)
```

### Restriction
The Restriction Constructor needs an integer parameter called "size".
```java
public Restriction(String typedInequation, int size)
```
This is the number of terms contained in the objective function

### Example

```java
ObjectiveFunction of = new ObjectiveFunction("MIN x1 + 2x2");
Restriction[] r = new Restriction[3];
r[0] = new Restriction("8x1 + 2x2 >= 16", of.getCoefficients().lenght);
r[1] = new Restriction("x1 + x2 <= 6", of.getCoefficients().lenght);
r[2] = new Restriction("2x1 + 7x2 >= 28", of.getCoefficients().lenght);
		
SimplexWetzel simplex = new SimplexWetzel(r, of);
simplex.solve();
```

Example results:
```
********** OPTIMAL SOLUTION FOUND **********

FO(x) -> MIN Z = 8.461538461538462
x1 = 1.0769230769230775
x2 = 3.6923076923076925
x3 = 5.329070518200751E-15
x4 = 1.23076923076923
x5 = 0.0
```

