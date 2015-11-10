# marclander
### Opmizing moonlanding using genetic programming

**Note: tested on Netlogo 4.2**

[Download simulations report](simulations.pdf)

### What is it?

A simulation that tries to land a moonlander as good as possible using genetic programming. The optimalisation goal is to let the moonlander land as quick as possible with as much remaining fuel as possible.

I have divided this goal into two sub-goals:

(1) Landing the moonlander as quick as possible;

(2) Landing the moonlander with as much remaining fuel as possible.

I have first tried to optimize these subgoals as much as possible, and then combined the solutions to find an optimal combination. This optimal state is relative; there is no way of knowing whether fuel is more important than time. One thing that is sure is that when one subgoal is optimally met, the other will not be. Therefore I have left the choice for an optimum as an option in the GUI.

-- Please not that this simulation comes with a PDF that contains several example simulations --

### How it works

I will describe the process I went through stepwise to ground my choices.

#### STEP 1. WRITING A BNF
The most important thing I have found out when designing this simulation is that a genetic program is not only about the fitness function, but that the true strength is in a combination of the fitness function with the BNF.
The BNF defines the boundaries that a genetic algorithm can explore, while the fitness can steer the evolution of a population.
Therefore my BNF is also a result of several iterations of writing one, testing it, evaluating the results and writing another one. In this way, the genetic program can be seen more as a work-horse than as a solution finder; it quickly searches the optimal solution of your BNF.

I started off by defining the maximum boundaries of all the variables, and I took them quite broad. It doesn't matter if (for example) 10% of the search space has no value, since a proper fitness function will filter them out. These were the start values:

velocity: 0 - 0.5
fuel: 0-80
time: 100-500
height: 0-30

The first BNF is called BNF-First.txt (BNFs can be found in the folder "BNF").

After doing several tests, I found out that in all the optimal solutions (both in fuel as in time), the variables <fuel> and <time> where missing! Apparently, they play no important role in a program that succesfully lands the moonlander. Therefore, I removed them from the BNF.

From this point on, I started to find optima for my subgoals, which led to two different BNFs. I did this by playing around with the first BNF, and everytime I found a good solution I altered the fitness function a little (see below of an explanation of the fitness function), together with the BNF. This finally let to the BNF for fuel optimalisation (BNF-Fuel.txt) and time optimalisation (BNF-Time.txt).

The final step of finding an optima for the overall goal was simple; putting the BNFs together! Actually there was only one rule that needed to be concatenated:

(BNFMoonlanderTime.txt)
<var> ::= velocity 0.00<digit> | height <digit>.<digit05><digit>

(BNFMoonlanderFuel.txt)
<var> ::= velocity 0.0<digit05> | height <digit>.<digit02><digit05>

led to:

(BNFMoonlanderFinal.txt)
<var> ::= velocity 0.00<digit> | height <digit>.<digit05><digit> | velocity 0.0<digit05> | height <digit>.<digit02><digit05>

Now with these expressions, the BNF is specialized in optima for fuel and time, but nothing in between. As I said before, time and fuel are fighting for fitness: using little fuel costs time and a short time costs a lot of fuel. Therefore the maximum of these two values should lie somewhere inbetween. This means that the BNF should also accept values inbetween, so I also added:

velocity 0.0<digit05><digit> | height <digit>.<digit05><digit>

to the BNF, which can represent all the values between the two extremes.

#### STEP 2. WRITING A FITNESS FUNCTION

Because of the empirical character of my simulation, I wanted to be able to alter the values of the fitness function while I was simulating. Therefore, the first version of my fitness function (that I used to find optima for both subgoals) contained the variables <velocity>, <time> and <fuel> which all could be altered used sliders ranging from 0 to 100:

let vel velocity-weight * (1 - velocity * 2)
let fue fuel-weight * fuel / 80 
let tim time-weight * (1 - (time - 100) / 500)

let fit vel + fue + tim

if crashed = true [ 
set fit (fit * 0.25)
]

I used this fitness function to find the optima for fuel and time. Once I knew these optima, I was able to narrow the values of my fitness function down. See the code for a more detailed description on how the fitness function finally looks and why.

#### STEP 3. WRITING A GENETIC ALGORITHM

I used the tournament pool instead of fitness proportional selection. The tournament pool is described in the dictate by Marco Wiering (Chapter 3. Eolutionary Computation p.54). I used this because it is more easy to implement, and I consider it an elegant solution.
It also solves several problems that arise when using fitness proportional selection:
- danger of premature convergence, since good individuals with a much larger fitness
can quickly take over the whole population.
- little selection pressure if the values all lie close to each other

First a population of a number of individuals (chosen by the user) is created, and filtered from not working programs (meaning they contain non-terminals of the BNF). Then the population will evolve over a number of generations determined in the interface. At every step, the fitness of every individual will be determined and saved in a seperate list. A new population is then generated using the tournament pools. The tournament pool consists of three randomly chosen individuals from the population and saves the most fit one in the new population. This is iterated for the number of inidividuals in the population. Finally, the mutation and crossover function supplied by JGE are applied to the generation. This is repeated until the generation max has been reached.

The bitstring of the most fit individual is saved, together with the fitness value and the generation it was born.

### How to use it

The following elements can be set in the interface:

- generations & population-size.
For a good and solid optimum, use a population size of 300-500 and let it run for 50 generations. In this way, the graph also shows a clear pattern. Results can also be achieved with lower values, but they are not garantueed to find an optimum. Still, the results are often fine with a population of 150.

For the quickest landing, set population to 500+ and set time-weight and fuel-weight to 1.

- mutation & crossover propability
I used a mutation value of 5-10% and a crossover rate of 75%. Mutation stands for exploration and crossover for exploitation. Since I have specialized my BNF to a very big extend, I don't need a lot of exploring. In stead, I need to exploit the possibilities of the BNF a lot.

- time-weight & fuel-weight
Set to which value the algorithm should optimize. Notice that when both the values are set to 1, there is a big change that the optimum will lie at one of the two values (meaning, a very low time or a very high fuel), completely wiping out the other value.

This has two reasons:

[1] The fitness of one value is equal to the sum of the two. There are basically three optimum: (low time) (low fuel) (both half). It can happen that the simulation falls in one of the three. This can be solved by altering the fitness function. For example; multiplying the values of time and fuel, giving them a higher value when put together.

[2] The crossover/mutation value. As said, my simulation does a lot of exploitation and therefore has the risk of fallen into a local optimum quickly. This can be solved by changing the mutation and crossover values.

- BNF-File: choose a file that I have previously used. But for the simulation to work correctly, choose "BNF-Final.txt".

- Test-genome button: simulate the current max-gen.

- Plot-options: turn off to make the simulation go faster.

The graph shows the fitness of every gen in black and the maximal fitness so far in red.

### Things to notice

I have included a PDF where I shortly describe several outcomes of the simulation.
It is included in the direction of this file (simulations.pdf).

It is also interesting to play around with the different BNF files. BNF-First.txt clearly shows different results than my final BNF, which I find very pleasing.
