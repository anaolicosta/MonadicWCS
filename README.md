## How to configure it

The file ``config.properties`` configures the runtime behavior of this project. 

Some properties point to java classes with the implementation you wish to test in the current run.

The java class ``main.ConfigurableProperties`` implements the methods that read those properties and make them available to other classes.

## Outuput files after running the program

In the root directory:

* __accuracy.csv__: Summary of experiments. It has a row for each pattern tested. 
It shows the accuracy before abduction, and after abduction for both credulous and skeptical strategies.

* __answers.csv__: List the conclusions for each syllogism and for each of the
patterns considered. 


In _syllogisms_ directory and for each _pattern_:

* __description_[pattern].txt__: A more detailed description of the 
intermediate results.
