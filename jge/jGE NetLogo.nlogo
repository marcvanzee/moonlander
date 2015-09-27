extensions [jge]


to loadBNF

  jge:load-bnf "bnf/BNFInt.txt" "bnf_int"
  output-print jge:bnf-grammar "bnf_int"

end


to GEMapping

  output-print jge:phenotype "1010000001011111" "bnf_int" 4 2

end


to crossover

let parentA "00000000"
let parentB "111111111111"
let prop 0.9
let offspringA "[null]"
let offspringB "[null]"

output-print word "Parent A: " parentA
output-print word "Parent B: " parentB
output-print word "Crossover Propability: " prop

let offspring jge:crossover parentA parentB prop

set offspringA item 0 offspring
set offspringB item 1 offspring

output-print word "Offspring A: " offspringA
output-print word "Offspring B: " offspringB

output-print ""

end


to crossover-fixed-length

let parentA "0000000000"
let parentB "1111111111"
let prop 0.9
let offspringA "[null]"
let offspringB "[null]"

output-print word "Parent A: " parentA
output-print word "Parent B: " parentB
output-print word "Crossover Propability: " prop

let offspring jge:crossover-fixed-length parentA parentB prop

set offspringA item 0 offspring
set offspringB item 1 offspring

output-print word "Offspring A: " offspringA
output-print word "Offspring B: " offspringB

output-print ""

end


to mutation

let offspring "0000000000"
let prop 0.1

output-print word "Original: " offspring
set offspring jge:mutation offspring prop
output-print word "Mutated : " offspring

output-print ""


end


to individual

  output-print jge:individual 1 2 4

end


to population

  output-print jge:population 4 2 1 4

end


to runsample

  clear-all

  jge:load-bnf "bnf/BNFNetLogoDemo.txt" "bnf_nl"
  output-print jge:bnf-grammar "bnf_nl"
  
  let genotype genome
  let action jge:phenotype genotype "bnf_nl" 4 0
  
  output-print word "Genotype: " genotype  
  output-print word "Action  : " action

  crt 1
  
  ask turtle 0 [set heading 0]
  ask turtle 0 [setxy 0 0]
  ask turtle 0 [wait 0.5]
  ask turtle 0 [run action]


end


to is-binary

  output-print "Is Binary?"
  output-print word "000101101010   " jge:is-binary? "000101101010"
  output-print word "111111111111   " jge:is-binary? "111111111111"
  output-print word "000000000000   " jge:is-binary? "000000000000"
  output-print word "abcdabcdabcd   " jge:is-binary? "abcdabcdabcd"
  output-print word "232323232323   " jge:is-binary? "232323232323"
  output-print word "200010110100   " jge:is-binary? "200010110100"
  output-print word "200000000000   " jge:is-binary? "200000000000"
  output-print word "000000000002   " jge:is-binary? "000000000002"
  output-print word "[empty string] " jge:is-binary? ""
  

end


to version

  output-print jge:version  

end
@#$#@#$#@
GRAPHICS-WINDOW
483
10
812
242
13
8
11.824
1
10
1
1
1
0
1
1
1
-13
13
-8
8
0
0
1
ticks

BUTTON
7
11
99
44
Load BNF
loadBNF
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

BUTTON
6
62
102
95
GE Mapping
GEMapping
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

BUTTON
114
10
215
43
Crossover
crossover
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

BUTTON
115
110
219
143
Mutation
mutation
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

INPUTBOX
7
178
354
238
genome
00000010000000010001000110100001000110110001
1
0
String

TEXTBOX
10
158
160
176
Use NetLogo \"run\" command
11
0.0
1

BUTTON
361
180
457
213
Run Sample
runsample
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

BUTTON
229
11
325
44
Individual
individual
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

BUTTON
229
62
325
95
Population
population
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

OUTPUT
9
309
813
603
12

BUTTON
335
60
442
93
Extension Version
version
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

BUTTON
707
264
810
297
Clear Output
clear-output
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

BUTTON
334
12
441
45
Is Binary?
is-binary
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

BUTTON
115
62
219
95
Crossover FL
crossover-fixed-length
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL

@#$#@#$#@
WHAT IS IT?
-----------
This model demonstrates the basic functionality of the jGE NetLogo extension.


HOW IT WORKS
------------
Every button calls one or more primitives of the jGE NetLogo extension. In this way
the functionality of the jGE NetLogo primitives is demonstrated.


HOW TO USE IT
-------------
Investigate the NetLogo code in the Procedures tab, play around with it, and use the buttons in the model's Interface tab. In this way you will become familiar with the usage of the jGE NetLogo extension. 


THINGS TO NOTICE
----------------
Read about Grammatical Evolution http://www.grammatical-evolution.org
and jGE Library http://www.bangor.ac.uk/~eep201/jge
in order to become familiar with both the Grammatical Evolution evolutionary algorithm 
and its Java implementation.


THINGS TO TRY
-------------
Try to play around with the code in the Procedures tab.


EXTENDING THE MODEL
-------------------
Try to add Grammatical Evolution to some of the evolutionary models (see Sample Models -> Biology -> Evolution) of NetLogo.


NETLOGO FEATURES
----------------
The NetLogo "run" primitive is used for the execution of the evolved behaviour of agents in the case where the BNF Grammar defines a valid sequence of NetLogo action commands for an agent.


CREDITS AND REFERENCES
----------------------
All contents (C) 2008 Loukas Georgiou.

Email: eep201@bangor.ac.uk, loukas@informatics.bangor.ac.uk
Web Site: http://www.bangor.ac.uk/~eep201/

jGE NetLogo extension is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

jGE NetLogo extension is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
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
-0.2 0 0.0 1.0
0.0 1 1.0 0.0
0.2 0 0.0 1.0
link direction
true
0
Line -7500403 true 150 150 90 180
Line -7500403 true 150 150 210 180

@#$#@#$#@
0
@#$#@#$#@
