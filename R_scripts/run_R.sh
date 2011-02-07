#/bin/bash

R --vanilla --slave < precompute.R

R --vanilla --slave < PrecomputeCovVarForMPT.R

