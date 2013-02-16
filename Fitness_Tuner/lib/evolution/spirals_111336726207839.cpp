#include <cstdlib>
#include <stdio.h>
#include <math.h>
#include <cmath>
#include <iostream>
#include <iomanip>
using namespace std;

#include "./regression/StackingEnsemble.h"

double ModelStacking_4720135026605(const double input[2]) {
static double (*models[7])(const double*) = {&null,&null,&null,&null,&null,&null,&null};
return stackingEnsembleOutput<7,2>(input,models);
}
#include "./regression/StackingEnsemble.h"

double ModelStacking_4720135158814(const double input[2]) {
static double (*models[7])(const double*) = {&null,&null,&null,&null,&null,&null,&null};
return stackingEnsembleOutput<7,2>(input,models);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_4720134987144(const double input[2]) {
static double (*models[2])(const double*) = {&ModelStacking_4720135026605,&ModelStacking_4720135158814};
return classifierModelOutput<2,2>(input,models);
}
#include "InputNormalization.h"

double* NormalizationPreprocessing_4720135287322(double input[2]) {
static const double imins[2] = {-6.0,-6.5};
static const double iMaxMinusMin[2] = {12.0,13.0};
return inputNormalization<2>(input,imins,iMaxMinusMin);
}
#include "./classification/ConnectableClassifier.h"

double* ConnectableClassifier_4720134939513(double input[2]) {
static double* (*model)(const double*) = {&ClassifierModel_4720134987144};
static double* (*inputProcessingMethods[1])(double*) = {&NormalizationPreprocessing_4720135287322};
static double* (*outputProcessingMethods[0])(double*);
static const bool enabledInputs[2] = {true,true};
return connectableClassifierOutput<2,2,1,0>(input,model,enabledInputs,inputProcessingMethods,outputProcessingMethods);
}
int main(int argc, const char** argv) {
    double testInput[2];
    for (double i = 0; i < 10; i = i + 1) {
        for (double j = 0; j < 10; j = j + 1) {
            testInput[0] = i;
            testInput[1] = j;
            double * result = ConnectableClassifier_4720134939513(testInput);
            for (int k = 0; k < 2; k++) {
                cout << setprecision(16) << result[k] << " ";
            }
            cout << endl;
        }
    }
}
