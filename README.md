# Compiler of Java-- programs to Java bytecodes

* **Group:** g3h

## Elements
* [Bernardo Manuel Esteves dos Santos](https://github.com/bernas670), 201706534 
* [Joana Catarina Teixeira Ferreira](https://github.com/joanaferreira0011), 201705722 
* [João Nuno Carvalho de Matos](https://github.com/joaonmatos), 201705471
* Vítor Hugo Leite Gonçalves, 201703917 

## Project grade
* **Global grade of the project:** 18.55 out of 20

## Project description

### Summary
This project is an implementation of a [Java--](https://cs.fit.edu/~ryan/cse4251/mini_java_grammar.html) compiler. 
This project was developed for the Compilers course of the Integrated Master in Informatics and Computing Engineering of the Faculty of Engineering of the University of Porto.

### Execute
To execute our programm you must checkout **master** branch, compile the programm by using **gradle build**.
* to generate the code for a file, use the following command:
```
java -jar comp2020-3h.jar file_you_want_to_generate <options>
```
The following **options** are available, and several can be used at the same time:
* **-t:** display AST
* **-s:** display Symbol Table
* **-c:** generate code
* **-r=n:** is not implemented in the master branch, but we will talk about it later
* **-o:** to generate code with some optimizations

After using this command a file with the extension .j will be created with the same name as the file passed as an argument.

To generate a file of type .class just use the file that was generated with the previous command as follows:

```
 java -jar jasmin.jar your_file.j 
```

Finally, this command creates a .class file that will be used to run the program with the following command:

```
java your_file.class
```

### Dealing with syntatic errors
Regarding syntatic errors, our program does not abort right after the 1st error. That is, the program has a syntactic error counter, which is incremented by 1 each time it encounters an error. If the counter value is 10, the program automatically aborts and prints all errors, if the program finds less than 10 errors it waits for the completion of the syntactic analysis and in the end it aborts and shows the exceptions found.

The variable that controls this count is present in the javacc folder, in the Parser.jjt file.


### Semantic analysis
The semantic analysis will check several semantic rules.
It verifies:
* If the type of an assignee is the same as the assigned one
* If operations are done with the same type
* If an access to an array is actualy done over an array type var
* If an access to an array is done correctly (with an int as index)
* If a boolean operation is done between booleans
* If the condition in an *if* or *while* returns a boolean
* If all variables are initialized correctly
* Return a WARNING instead of an ERROR if the variable is only initialized in one branch of an *if* statement or inside a *while* statement
* If, when a method is invoked, the arguments match the parameters


### Intermediate representations (IRs)
In order to implement out ```-o``` optimization, we needed to change our original AST, and as such, during the optimization procedure the original AST is optimized.

Even though it is not finished, in order to implemente the ```-r=``` optimization, we implemented a tree to check the control flow in the livenessAnalysis branch with all the instructions that affect register allocation. This would be used to perform a liveness analysis.



### Code generation

To generate the code, we traverse the AST one more time.
Firstly we process the class attributes and constructor, and after this we iterate through the methods declarations. Inside each method we iterate through each node.
It is relevant to mention that during the semantic analysis, we collected some information that is useful to generate code, such as the methods signatures and variable types.

#### -o optimization
We managed to implement Constant Propagation and Constant Folding, as well as Dead Code Elimination.

**Important note:**
Despite not being present in the **master** branch with the final version of the project, the -r=n optimization was partially implemented and is present in the **livenessAnalysis** branch (view [here](https://bitbucket.org/specsfeup/comp2020-3h/src/livenessAnalysis/)).
In order to perform this optimization, the option -r = n must be added to the command line arguments, where n is the maximum value of registers to be used in the allocation of variables.
If this option is requested, at the end of the semantic analysis the AST goes through a LivenessAnalysis class in which only the nodes that somehow form an instruction are analyzed, and from that node we are able to extract the information from the variables defined and / or used in that same instruction. , later this information will be used to compute the LiveIn and LiveOut of each node in the function calculateLivenessAnalysis () of the same file, which implements the algorithm of the slides of the theoretical classes. However, it was not possible to implement graph coloring to know the minimum number of records to use and we were also unable to determine the successors of each node.


### Overview
The compiler will perform the following steps:
* Parser that performs a synthatic analysis - javacc parser.jjt 
* Generate an AST (abstract syntax tree)
* Create a Symbol Table that stores scopes and symbols (*SymbolTable* class)
* A **descriptors** package was created to represent the symbols and store information about them. 
* Perform a Semantic Analysis (*SemanticAnalysis* class)
* Code generation (*.j* files)
* Perform some optimizations in code generation (as mentioned above)
* The first steps of the register allocation optimization were implemented but not finished: construction of the control flow tree and a simple forward liveness analysis (this should be changed to a backwards one)


    
### Task distribution

#### Check point 1
* Active collaboration of all elements of the group.
Everyone helped to pass the provided grammar into code.
Error recovery was done by João and Vítor and the construction of the AST was essentially done by Bernardo, with the help of the other elements
#### Check point 2
* Work essentially divided into 3 blocks.
1. Correction of errors of the first delivery - Bernardo.
2. Construction of the symbol table and semantic analysis - Joana and Vitor
3. Code generation requested - Bernardo
#### Check point 3
Bernardo was responsible for ending code generation, and the remaining elements responsible for building tests that demonstrated our compiler. Joana and Vítor changed some things in the semantic analysis.
#### Final delivery
The -o optimization was done by Bernardo, and the -r optimization, although it was not finalized, was done by Joana and Vítor, who also made documentation of the code with the help of Joao.

### Pros
* grammar almost all of it LL (1), except in one case where we were unable to
* code documentation(most of it)
* we follow the description of the project and everything that was requested during the semester
* The SymbolTable class is easier to read because of the use of auxiliary structures to build and traverse them, such as a stack
* The *Descriptor* class was re-used for several purposes, such as the symbol table, code generation but also in the (not finished) ```-r=``` optimization

### Cons
* the lack of unfinished optimization -r
* code modularity, althought it's good, it could be better
