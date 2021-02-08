Master Thesis: Online Automated Multivariate Web Design Optimization
================

by Laurens Verspeek

VU University Amsterdam

## Abstract
Nowadays, there are over 1 billion websites online in the world wide web. Modern web
applications need to adapt to many different users. The user preferences are often hard
to completely determine at design time. Therefore, these web applications are typically
tested and refined at run-time to better determine the user preferences or to discover
changed user preferences. These user-intensive websites increasingly rely on online 
controlled experiments, where different variants are being tested on live visitors.
Technological advances in web design imply a large search space. A common strategy is
sampling this huge search space. Current optimization strategies, such as A/B testing
or multivariate testing, use manual sampling, and are therefore time-consuming for the
designer. The variants the designer samples from the large search space are static and
cannot change during the test. After the test results are interpreted, the designer can
setup new variants for the next static test. In this thesis we propose an online automated
multivariate web design optimization system (AMOS) based on a genetic algorithm.
This optimization technique will take away the manual part in website optimization by
automating the sampling from the search space with a genetic algorithm. We will use
a modified genetic algorithm, which uses fluid generations and keeps track of previous
generations. By doing this we will get dynamic variants during the test and faster
results. To validate the new optimization algorithm, it is implemented and tested on a
live website with real users.

## Thesis
[Download Thesis PDF](master_thesis_AMOS_laurens_verspeek.pdf)

## System Architecture
System architecture of the implementation of AMOS into a framework
![System Architecture Flowchart](paper/imgs/flowchart.png?raw=true "System Architecture Flowchart")

## Screenshot Results
![Test Results](paper/imgs/comparegen.png?raw=true "Test Results")