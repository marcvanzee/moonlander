; Implementation of a genetic algorithm to land a moonlander.
; Originally proposed by Max Knobbout

; Implementation by
; Marc van Zee - marcvanzee@hotmail.com
; University of Utrecht / Artificial Intelligence

; please read the information section for a description of the approach.
; the comments in this code are purely about the flow of the program, except for the fitness function

; include Genetic Algorithm extension
extensions [jge]

; globals:
; velocity/height/fuel/time/crashed:                variables about the state of the current moonlander
; population:                              number - current size of the population
; fitness:                                 list   - containing the fitness values of all the gens
; generation:                              number - what generation we are currently in
; max-generation:                          number - in what generation the most succesful gen was found
; max-fitness:                             number - most succesful fitness so far

globals [ velocity height thrusting fuel time crashed population fitness generation max-generation max-fitness]
breed [rockets rocket]
breed [stars star]

;=========
;========= [GO]: main procedure to start the genetic algorithm
;=========

to go
  ; set all maxima to 0
  reset-max-values
  
  ; loop through the generations with a max set by the user
  while [generation < generations] [
    ; let all the gens run in the background
    simulate-population
 
    ; create a tournament-pool as a fitness-proportional selection
    tournament-pool
    
    ; now we have the tournament-pool, we are going to select two
    ; individuals every time and decide what to do with them
    ; after this, they will be added to the new-population until its the 
    ; same size as the current one.
    let new-population []
  
    while [length new-population < length population] [
      ; pick two individuals
      ; note that there are two members of the population added every time.
      ; this could potentially be dangerous, but the value of population
      ; in the interface can only be increased by 2 and starts with 0.
      ; in this way, it is safe to select two individuals every time.
      let parents (list (item length new-population population)
                        (item ((length new-population) + 1) population))
   
      ; decide what we are going to do with them, this can be three things:
      ; - copy (do nothing);
      ; - mutate
      ; - cross-over
      
      let choose random 3
   
      ; choose = 0 => copy
   
      if choose = 1 [ ; mutate
        ; mutate with a probability set by the user.
        ; muliply by three because there is a 1/3 change of mutation
        let mutated (list (jge:mutation (item 0 parents) (p-mutation * 3))
                          (jge:mutation (item 1 parents) (p-mutation * 3)))
     
        set parents mutated
      ]
      if choose = 2[ ; crossover
        ; cross-over
     
        ; same as mutation
        let children jge:crossover (item 0 parents) (item 1 parents) (p-crossover * 3)
        set parents children
      ]
   
      ; add the (altered) parents to the new population.
      set new-population fput (item 0 parents) new-population
      set new-population fput (item 1 parents) new-population
    
    ]
    ; update the population can go on to the next generation
    set population new-population
    set generation (generation + 1) 
  ]
  
  ; when finished, visualize the best gen
  graph max-gen false
end

;=========
;========= [SETUP]: setup and draw world
;=========

to setup
  ; Default shape for rockets
  set-default-shape rockets "lander 2"
  set-default-shape stars "dot"
  
  ; Clear all
  ca
  ; Draw the ground
  ask patches with [ pycor < 4 ] [ set pcolor pycor + 2 ]
  ; Draw the stars
  create-stars 20 [
    set color white
    set size 0.5
    setxy random-xcor 4 + random 28
  ]
  
  ; load in a grammar, selected by the user
  jge:load-bnf (word "BNF/" BNF-file) "grammar"

  let one 0
  let action ""
  
  generate-population
  set generation 0
end

;=========
;========= [RESET-WORLD]: argument depicts whether or not the simulation is hidden or displayed
;=========

to reset-world [hidden]
  ask rockets [ die ]
  ; Create the rocket
  create-rockets 1 [
    set color red
    set heading 0
    setxy 0 32
    if-else (hidden) [ ht ] [ pd ]
  ]
  set velocity 0
  set height 28
  set thrusting false
  set fuel 80
  set time 0
end

;=========
;========= [THRUST]: Methods for the genetic program
;=========

to thrust-on
  if (fuel > 0) [ set color green set thrusting true ]
end
to thrust-off
  set thrusting false
  set color red
end

;=========
;========= [SIMULATION]: the first argument is a string depicting the actual program of the rocket,
;=========               the second is a boolean stating whether or not the simulation is hidden
;=========

to simulate [prog hidden]
  reset-world hidden
  let done false
  while [not done] [
    ask rockets [
      ; Run prog generated by genetic algorithm
      run prog
      
      ; Increase velocity with 1.63 m/s^2
      set velocity velocity + 0.00163
      ; Update height (in km)
      set height ycor - 4
      
      if (thrusting) [
        ; Decrease velocity with 5 m/s^2
        set velocity velocity - 0.005
        ; Decrease fuel with 1L
        set fuel fuel - 1
        if (fuel = 0) [ thrust-off ]
      ]
      ; Update time
      set time time + 1
      
      ; Horizontal movement is 100 m/s^2 (to display curve)   
      set xcor xcor + 0.1
      let nexth height - velocity
    
      if-else (nexth < 0) [
        ; The ship can either crash, bounce or land
        if-else (velocity > 0.05) [
          ; Crash
          set crashed true
          set ycor 4
          set height 0
          set shape "circle"
          set done true
        ] [
          if-else (velocity > 0.01) [
            ; Bounce with 20% absorption of the velocity
            set velocity -0.8 * velocity
          ] [
            ; Land
            set crashed false
            set ycor 4
            set height 0
            set done true
          ]
        ]
      ] [
        if (nexth < 28) [ set ycor ycor - velocity ]
      ]
    ]
  ]
end

;=========
;========= These are here because the < and > symbols are not allowed in the BNF grammar.
;=========

to-report smaller-than [a b]
  report (a < b)
end
to-report greater-than [a b]
  report (a > b)
end

;=========
;========= [CURRENT-FITNESS]: fitness-function
;=========

to-report current-fitness
  ; give the velocity a weight that depends on the time and fuel weight. those are both
  ; adjustable in the interface, and the velocity will be the sum of them.
  
  let velocity-weight time-weight + fuel-weight
  
  let fit 0
  
  ; when we crash, the velocity will be our only fitness, since this is the most
  ; important variable that should change.
  
  ifelse velocity > 0.05 [ ; crash
    
    ; the maximum fitness of a crashed moonlander is 0.1
    ; the values of the velocity can range between 0.05 and 0.3 (empirical result)
    ; therefore the range of the velocity is 0.3 - 0.05 = 0.25
    ; 0.25 * 0.2 = 0.1
    ;
    ;   0.1 - (velicty - 0.05)*0.2
    ; = 0.12 - 0.4 * velocity
    
    set fit velocity-weight * (0.12 - 0.4 * velocity)
    
  ] [
  
    ; if the moonlander doesnt crash, the time and fuel are important too.
    ; first the velocity, it can range between 0.00 and 0.05, simply add it
    ; and multiply by 10 (range becomes 0.0 - 0.5)
    set fit (fit + velocity-weight * velocity * 10)
    
    ; the time can range between 125 and 500 (empirical result)
    ; let values range between 0 and 1, multiplied with the weight
    ; 
    ;   1 - (time - 125) / 500
    ; = 1.25 - time / 500
   
    set fit (fit + time-weight * (1.25 - time / 500))
    
    ; the maximum fuel when landing is 20 (empirical result)
    ; same for time: let it range between 0 and 1, multiplied by weight
    
    if fuel >= 0 and fuel <= 20 [
      set fit (fit + fuel-weight * (fuel / 20))
    ]
  ]

  report fit
end

;=========
;========= [GENERATE-POPULATION]: create a population with valid genomes.
;=========

to generate-population
  set population []
  
  while [length population < population-size] [
    let one jge:individual 4 4 40
    let action jge:phenotype one "grammar" 4 10
    
    ; dont accept not working genomes in initial population
    ; note that this check is not being performed during the evolution.
    ; the reason is trivial; not working genomes may still possess useful
    ; code that might become useful again in a cross-over or a mutation.
    if action != "" [ set population lput one population ]
  ]
end

;=========
;========= [SIMULATE-POPULATION]: let all genomes of this population run in the background
;=========

to simulate-population
 let len length population - 1
 let action "" 

 set fitness []
  
 while [len >= 0] [
   let gen item len population
   set action jge:phenotype gen "grammar" 4 10
   simulate action true
   
   ; add the fitness to the list of fitness functions
   let f current-fitness
   set fitness fput f fitness
   
   ; change the best gen if it is better   
   update-max-gen gen f
   
   plot-values f
   
   ; go to the next gen in the population
   set len (len - 1)
 ]
 
 ; plot the best gen of this population
 graph max-gen false
end

;=========
;========= [TOURNAMENT-POOL]: a fitness proportional selection method
;=========

to tournament-pool
  ; tournament-method
  ; select three random individuals from the population
  ; then choose the best one. put this into the new population
  
  let new-population []
  let len length population

  while [length new-population < len] [
    ; generate three individuals       
    let tournament (list (item (random len) fitness)
                         (item (random len) fitness)
                         (item (random len) fitness))

    ; choose the best gen
    let num position (max tournament) fitness   
    set new-population lput (item num population) new-population
  ]
  
  ; when the new population has been filled, replace the current one
  set population new-population
end

;=========
;========= [GRAPH]: method to simulate a run of a gen with possible feedback
;=========

to graph [gen show-results]
  let action jge:phenotype gen "grammar" 4 10
  simulate action false
  if show-results [
    print word "code: " action
    print word "fitness: " current-fitness
  ]
end

;=========
;========= [RESET-MAX-VALUES]
;=========

to reset-max-values
  set max-gen "0"
  set max-fitness 0
  set max-generation 0
end

;=========
;========= [PLOT-VALUES]: can be set in the interface
;=========

to plot-values [gen]
  if plot-options = "plot-all" [
     set-current-plot-pen "gen-fitness"
     plot gen
  ]
   
  if plot-options != "plot-none" [
    set-current-plot-pen "max-fitness"
    plot max-fitness
  ]
end

;=========
;========= [UPDATE-MAX-GEN]
;=========

to update-max-gen [gen f]
  if f > max-fitness [ 
    set max-gen gen 
    set max-fitness f
    set max-generation generation 
  ]
end

;=========
;========= [TEST-GENOME]: button to set the current max-gen
;=========

to test-genome
  graph max-gen true
end
@#$#@#$#@
GRAPHICS-WINDOW
198
10
673
536
-1
-1
15.0
1
10
1
1
1
0
1
0
1
0
30
0
32
0
0
1
ticks

BUTTON
8
10
94
43
Setup
setup
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

MONITOR
684
330
778
375
Velocity (km/s)
velocity
8
1
11

MONITOR
784
329
841
374
Fuel (L)
fuel
17
1
11

MONITOR
847
329
903
374
Thrusting?
thrusting
17
1
11

MONITOR
684
380
779
425
Height (km)
height
8
1
11

MONITOR
784
380
842
425
Time (s)
time
8
1
11

MONITOR
909
329
967
374
Crashed?
crashed
8
1
11

BUTTON
103
10
189
43
run
go
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

BUTTON
919
482
991
541
NIL
test-genome
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

SLIDER
8
150
189
183
p-mutation
p-mutation
0
1
0.08
0.01
1
NIL
HORIZONTAL

SLIDER
8
187
190
220
p-crossover
p-crossover
0
1
0.75
0.01
1
NIL
HORIZONTAL

SLIDER
8
98
189
131
population-size
population-size
0
1000
506
2
1
NIL
HORIZONTAL

MONITOR
847
380
967
425
Generations
generation
17
1
11

SLIDER
8
61
189
94
generations
generations
0
100
50
1
1
NIL
HORIZONTAL

SLIDER
9
255
189
288
time-weight
time-weight
0
1
1
0.01
1
NIL
HORIZONTAL

SLIDER
10
296
189
329
fuel-weight
fuel-weight
0
1
0
0.01
1
NIL
HORIZONTAL

PLOT
684
10
1131
322
fitness
population
fitness
0.0
10.0
0.0
1.5
true
true
PENS
"max-fitness" 1.0 0 -2674135 true
"gen-fitness" 1.0 2 -16777216 true

CHOOSER
992
331
1131
376
plot-options
plot-options
"plot-all" "plot-max-fitness" "plot-none"
0

CHOOSER
10
364
188
409
BNF-file
BNF-file
"BNF-Final.txt" "BNF-FuelOptimized.txt" "BNF-TimeOptimized.txt" "BNF-First.txt"
0

MONITOR
684
430
824
475
generation of max-gen
max-generation
17
1
11

MONITOR
829
430
967
475
maximal fitness
max-fitness
17
1
11

INPUTBOX
685
481
914
541
max-gen
10111000000000011110110010001011011100101101011110111111111001100110111000010100001110011010111100110101010001101000001110101100000110110000110110111100011101100001100001101011100011111111001110110111000011101011110001011111100110101010011101110001110101010010100010011000110011000011110011100101001001011111101011110111111111001100110111000010100001110100
1
0
String

@#$#@#$#@
WHAT IS IT?
-----------
A simulation that tries to land a moonlander as good as possible using genetic programming. The optimalisation goal is to let the moonlander land as quick as possible with as much remaining fuel as possible.

I have divided this goal into two sub-goals:
(1) Landing the moonlander as quick as possible;
(2) Landing the moonlander with as much remaining fuel as possible.

I have first tried to optimize these subgoals as much as possible, and then combined the solutions to find an optimal combination. This optimal state is relative; there is no way of knowing whether fuel is more important than time. One thing that is sure is that when one subgoal is optimally met, the other will not be. Therefore I have left the choice for an optimum as an option in the GUI.

-- Please not that this simulation comes with a PDF that contains several example simulations --

HOW IT WORKS
------------
I will describe the process I went through stepwise to ground my choices.

STEP 1. WRITING A BNF
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

STEP 2. WRITING A FITNESS FUNCTION
Because of the empirical character of my simulation, I wanted to be able to alter the values of the fitness function while I was simulating. Therefore, the first version of my fitness function (that I used to find optima for both subgoals) contained the variables <velocity>, <time> and <fuel> which all could be altered used sliders ranging from 0 to 100:

let vel velocity-weight * (1 - velocity * 2)
let fue fuel-weight * fuel / 80 
let tim time-weight * (1 - (time - 100) / 500)
  
let fit vel + fue + tim
  
if crashed = true [ 
  set fit (fit * 0.25)
]

I used this fitness function to find the optima for fuel and time. Once I knew these optima, I was able to narrow the values of my fitness function down. See the code for a more detailed description on how the fitness function finally looks and why.

STEP 3. WRITING A GENETIC ALGORITHM

I used the tournament pool instead of fitness proportional selection. The tournament pool is described in the dictate by Marco Wiering (Chapter 3. Eolutionary Computation p.54). I used this because it is more easy to implement, and I consider it an elegant solution.
It also solves several problems that arise when using fitness proportional selection:
- danger of premature convergence, since good individuals with a much larger fitness
  can quickly take over the whole population.
- little selection pressure if the values all lie close to each other

First a population of a number of individuals (chosen by the user) is created, and filtered from not working programs (meaning they contain non-terminals of the BNF). Then the population will evolve over a number of generations determined in the interface. At every step, the fitness of every individual will be determined and saved in a seperate list. A new population is then generated using the tournament pools. The tournament pool consists of three randomly chosen individuals from the population and saves the most fit one in the new population. This is iterated for the number of inidividuals in the population. Finally, the mutation and crossover function supplied by JGE are applied to the generation. This is repeated until the generation max has been reached. 

The bitstring of the most fit individual is saved, together with the fitness value and the generation it was born.

HOW TO USE IT
-------------
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

THINGS TO NOTICE
----------------
I have included a PDF where I shortly describe several outcomes of the simulation.
It is included in the direction of this file (simulations.pdf).

It is also interesting to play around with the different BNF files. BNF-First.txt clearly shows different results than my final BNF, which I find very pleasing.

EXTENDING THE MODEL
-------------------
The fitness function could be optimized to work better when combining fuel and time, and so to find a maximum. Currently it falls into an optimum of one of the two too often.


NETLOGO FEATURES
----------------
JGE genetic programming algorithm.


RELATED MODELS
--------------
-


CREDITS AND REFERENCES
----------------------
Marc van Zee / F093385
@#$#@#$#@
default
true
0
Polygon -7500403 true true 150 5 40 250 150 205 260 250

airplane
true
0
Polygon -7500403 true true 150 0 135 15 120 60 120 105 15 165 15 195 120 180 135 240 105 270 120 285 150 270 180 285 210 270 165 240 180 180 285 195 285 165 180 105 180 60 165 15

arrow
true
0
Polygon -7500403 true true 150 0 0 150 105 150 105 293 195 293 195 150 300 150

box
false
0
Polygon -7500403 true true 150 285 285 225 285 75 150 135
Polygon -7500403 true true 150 135 15 75 150 15 285 75
Polygon -7500403 true true 15 75 15 225 150 285 150 135
Line -16777216 false 150 285 150 135
Line -16777216 false 150 135 15 75
Line -16777216 false 150 135 285 75

bug
true
0
Circle -7500403 true true 96 182 108
Circle -7500403 true true 110 127 80
Circle -7500403 true true 110 75 80
Line -7500403 true 150 100 80 30
Line -7500403 true 150 100 220 30

butterfly
true
0
Polygon -7500403 true true 150 165 209 199 225 225 225 255 195 270 165 255 150 240
Polygon -7500403 true true 150 165 89 198 75 225 75 255 105 270 135 255 150 240
Polygon -7500403 true true 139 148 100 105 55 90 25 90 10 105 10 135 25 180 40 195 85 194 139 163
Polygon -7500403 true true 162 150 200 105 245 90 275 90 290 105 290 135 275 180 260 195 215 195 162 165
Polygon -16777216 true false 150 255 135 225 120 150 135 120 150 105 165 120 180 150 165 225
Circle -16777216 true false 135 90 30
Line -16777216 false 150 105 195 60
Line -16777216 false 150 105 105 60

car
false
0
Polygon -7500403 true true 300 180 279 164 261 144 240 135 226 132 213 106 203 84 185 63 159 50 135 50 75 60 0 150 0 165 0 225 300 225 300 180
Circle -16777216 true false 180 180 90
Circle -16777216 true false 30 180 90
Polygon -16777216 true false 162 80 132 78 134 135 209 135 194 105 189 96 180 89
Circle -7500403 true true 47 195 58
Circle -7500403 true true 195 195 58

circle
false
0
Circle -7500403 true true 0 0 300

circle 2
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240

cow
false
0
Polygon -7500403 true true 200 193 197 249 179 249 177 196 166 187 140 189 93 191 78 179 72 211 49 209 48 181 37 149 25 120 25 89 45 72 103 84 179 75 198 76 252 64 272 81 293 103 285 121 255 121 242 118 224 167
Polygon -7500403 true true 73 210 86 251 62 249 48 208
Polygon -7500403 true true 25 114 16 195 9 204 23 213 25 200 39 123

cylinder
false
0
Circle -7500403 true true 0 0 300

dot
false
0
Circle -7500403 true true 90 90 120

face happy
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 255 90 239 62 213 47 191 67 179 90 203 109 218 150 225 192 218 210 203 227 181 251 194 236 217 212 240

face neutral
false
0
Circle -7500403 true true 8 7 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Rectangle -16777216 true false 60 195 240 225

face sad
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 168 90 184 62 210 47 232 67 244 90 220 109 205 150 198 192 205 210 220 227 242 251 229 236 206 212 183

fish
false
0
Polygon -1 true false 44 131 21 87 15 86 0 120 15 150 0 180 13 214 20 212 45 166
Polygon -1 true false 135 195 119 235 95 218 76 210 46 204 60 165
Polygon -1 true false 75 45 83 77 71 103 86 114 166 78 135 60
Polygon -7500403 true true 30 136 151 77 226 81 280 119 292 146 292 160 287 170 270 195 195 210 151 212 30 166
Circle -16777216 true false 215 106 30

flag
false
0
Rectangle -7500403 true true 60 15 75 300
Polygon -7500403 true true 90 150 270 90 90 30
Line -7500403 true 75 135 90 135
Line -7500403 true 75 45 90 45

flower
false
0
Polygon -10899396 true false 135 120 165 165 180 210 180 240 150 300 165 300 195 240 195 195 165 135
Circle -7500403 true true 85 132 38
Circle -7500403 true true 130 147 38
Circle -7500403 true true 192 85 38
Circle -7500403 true true 85 40 38
Circle -7500403 true true 177 40 38
Circle -7500403 true true 177 132 38
Circle -7500403 true true 70 85 38
Circle -7500403 true true 130 25 38
Circle -7500403 true true 96 51 108
Circle -16777216 true false 113 68 74
Polygon -10899396 true false 189 233 219 188 249 173 279 188 234 218
Polygon -10899396 true false 180 255 150 210 105 210 75 240 135 240

house
false
0
Rectangle -7500403 true true 45 120 255 285
Rectangle -16777216 true false 120 210 180 285
Polygon -7500403 true true 15 120 150 15 285 120
Line -16777216 false 30 120 270 120

lander 2
true
0
Polygon -7500403 true true 135 205 120 235 180 235 165 205
Polygon -16777216 false false 135 205 120 235 180 235 165 205
Line -7500403 true 75 30 195 30
Polygon -7500403 true true 195 150 210 165 225 165 240 150 240 135 225 120 210 120 195 135
Polygon -16777216 false false 195 150 210 165 225 165 240 150 240 135 225 120 210 120 195 135
Polygon -7500403 true true 75 75 105 45 195 45 225 75 225 135 195 165 105 165 75 135
Polygon -16777216 false false 75 75 105 45 195 45 225 75 225 120 225 135 195 165 105 165 75 135
Polygon -16777216 true false 217 90 210 75 180 60 180 90
Polygon -16777216 true false 83 90 90 75 120 60 120 90
Polygon -16777216 false false 135 165 120 135 135 75 150 60 165 75 180 135 165 165
Circle -7500403 true true 120 15 30
Circle -16777216 false false 120 15 30
Line -7500403 true 150 0 150 45
Polygon -1184463 true false 90 165 105 210 195 210 210 165
Line -1184463 false 210 165 245 239
Line -1184463 false 237 221 194 207
Rectangle -1184463 true false 221 245 261 238
Line -1184463 false 90 165 55 239
Line -1184463 false 63 221 106 207
Rectangle -1184463 true false 39 245 79 238
Polygon -16777216 false false 90 165 105 210 195 210 210 165
Rectangle -16777216 false false 221 237 262 245
Rectangle -16777216 false false 38 237 79 245

leaf
false
0
Polygon -7500403 true true 150 210 135 195 120 210 60 210 30 195 60 180 60 165 15 135 30 120 15 105 40 104 45 90 60 90 90 105 105 120 120 120 105 60 120 60 135 30 150 15 165 30 180 60 195 60 180 120 195 120 210 105 240 90 255 90 263 104 285 105 270 120 285 135 240 165 240 180 270 195 240 210 180 210 165 195
Polygon -7500403 true true 135 195 135 240 120 255 105 255 105 285 135 285 165 240 165 195

line
true
0
Line -7500403 true 150 0 150 300

line half
true
0
Line -7500403 true 150 0 150 150

pentagon
false
0
Polygon -7500403 true true 150 15 15 120 60 285 240 285 285 120

person
false
0
Circle -7500403 true true 110 5 80
Polygon -7500403 true true 105 90 120 195 90 285 105 300 135 300 150 225 165 300 195 300 210 285 180 195 195 90
Rectangle -7500403 true true 127 79 172 94
Polygon -7500403 true true 195 90 240 150 225 180 165 105
Polygon -7500403 true true 105 90 60 150 75 180 135 105

plant
false
0
Rectangle -7500403 true true 135 90 165 300
Polygon -7500403 true true 135 255 90 210 45 195 75 255 135 285
Polygon -7500403 true true 165 255 210 210 255 195 225 255 165 285
Polygon -7500403 true true 135 180 90 135 45 120 75 180 135 210
Polygon -7500403 true true 165 180 165 210 225 180 255 120 210 135
Polygon -7500403 true true 135 105 90 60 45 45 75 105 135 135
Polygon -7500403 true true 165 105 165 135 225 105 255 45 210 60
Polygon -7500403 true true 135 90 120 45 150 15 180 45 165 90

sheep
false
0
Rectangle -7500403 true true 151 225 180 285
Rectangle -7500403 true true 47 225 75 285
Rectangle -7500403 true true 15 75 210 225
Circle -7500403 true true 135 75 150
Circle -16777216 true false 165 76 116

square
false
0
Rectangle -7500403 true true 30 30 270 270

square 2
false
0
Rectangle -7500403 true true 30 30 270 270
Rectangle -16777216 true false 60 60 240 240

star
false
0
Polygon -7500403 true true 151 1 185 108 298 108 207 175 242 282 151 216 59 282 94 175 3 108 116 108

target
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240
Circle -7500403 true true 60 60 180
Circle -16777216 true false 90 90 120
Circle -7500403 true true 120 120 60

tree
false
0
Circle -7500403 true true 118 3 94
Rectangle -6459832 true false 120 195 180 300
Circle -7500403 true true 65 21 108
Circle -7500403 true true 116 41 127
Circle -7500403 true true 45 90 120
Circle -7500403 true true 104 74 152

triangle
false
0
Polygon -7500403 true true 150 30 15 255 285 255

triangle 2
false
0
Polygon -7500403 true true 150 30 15 255 285 255
Polygon -16777216 true false 151 99 225 223 75 224

truck
false
0
Rectangle -7500403 true true 4 45 195 187
Polygon -7500403 true true 296 193 296 150 259 134 244 104 208 104 207 194
Rectangle -1 true false 195 60 195 105
Polygon -16777216 true false 238 112 252 141 219 141 218 112
Circle -16777216 true false 234 174 42
Rectangle -7500403 true true 181 185 214 194
Circle -16777216 true false 144 174 42
Circle -16777216 true false 24 174 42
Circle -7500403 false true 24 174 42
Circle -7500403 false true 144 174 42
Circle -7500403 false true 234 174 42

turtle
true
0
Polygon -10899396 true false 215 204 240 233 246 254 228 266 215 252 193 210
Polygon -10899396 true false 195 90 225 75 245 75 260 89 269 108 261 124 240 105 225 105 210 105
Polygon -10899396 true false 105 90 75 75 55 75 40 89 31 108 39 124 60 105 75 105 90 105
Polygon -10899396 true false 132 85 134 64 107 51 108 17 150 2 192 18 192 52 169 65 172 87
Polygon -10899396 true false 85 204 60 233 54 254 72 266 85 252 107 210
Polygon -7500403 true true 119 75 179 75 209 101 224 135 220 225 175 261 128 261 81 224 74 135 88 99

wheel
false
0
Circle -7500403 true true 3 3 294
Circle -16777216 true false 30 30 240
Line -7500403 true 150 285 150 15
Line -7500403 true 15 150 285 150
Circle -7500403 true true 120 120 60
Line -7500403 true 216 40 79 269
Line -7500403 true 40 84 269 221
Line -7500403 true 40 216 269 79
Line -7500403 true 84 40 221 269

x
false
0
Polygon -7500403 true true 270 75 225 30 30 225 75 270
Polygon -7500403 true true 30 75 75 30 270 225 225 270

@#$#@#$#@
NetLogo 4.1
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
default
0.0
-0.2 0 1.0 0.0
0.0 1 1.0 0.0
0.2 0 1.0 0.0
link direction
true
0
Line -7500403 true 150 150 90 180
Line -7500403 true 150 150 210 180

@#$#@#$#@
0
@#$#@#$#@
