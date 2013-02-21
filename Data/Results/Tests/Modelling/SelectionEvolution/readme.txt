Run_2:
Improved ModelGroupFitness:
before = (1 / precision) * (1 / unsimilarity)
after = 1 / (tanh(precision + 1) * tanh(unsimilarity + 1)) [avoid infinity]

further changes in sources
