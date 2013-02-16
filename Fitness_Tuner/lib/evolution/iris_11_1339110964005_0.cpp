#include <cstdlib>
#include <stdio.h>
#include <math.h>
#include <cmath>
#include <iostream>
#include <iomanip>
using namespace std;

#include "./regression/GaussianModel.h"

double GaussianModel_7293762848315(const double input[4]) {
static const double parameters[7] = {0.20669553765629034,0.682595795120616,-0.8476024584269891,-0.1991643947423763,-0.046793610184749396,3.1929131098792047,-0.16997899282520326};
return gaussianModelOutput<4>(input,parameters);
}
#include "./regression/GaussianModel.h"

double GaussianModel_7293762972074(const double input[4]) {
static const double parameters[7] = {0.5260871961955911,0.33055402258764066,0.42198782660849077,0.3644361802789965,-0.14780337047202222,0.5731057835611914,-0.5711656258661667};
return gaussianModelOutput<4>(input,parameters);
}
#include "./regression/PolynomialModel.h"

double PolynomialModel_7293763031229(const double input[4]) {
static const double parameters[17] = {0.9455143261105547,1.0886097772494736,2.5311034751653096,1.87814511060742,-4.082526301423741,-7.1051907336838624,-17.43928971016794,-14.00727739699505,4.524141893241449,10.77315830191236,34.61703975664932,28.63264642626018,-1.8012695333734428,-4.886755759086782,-18.961440650549775,-16.019187405236153,-0.0122314049981469};
static const int powers[16][4] = {{1,-1,-1,-1},{-1,1,-1,-1},{-1,-1,1,-1},{-1,-1,-1,1},{2,-1,-1,-1},{-1,2,-1,-1},{-1,-1,2,-1},{-1,-1,-1,2},{3,-1,-1,-1},{-1,3,-1,-1},{-1,-1,3,-1},{-1,-1,-1,3},{4,-1,-1,-1},{-1,4,-1,-1},{-1,-1,4,-1},{-1,-1,-1,4}};
return polynomialModelOutput<4,16>(input,parameters,powers);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_7293762797261(const double input[4]) {
static double (*models[3])(const double*) = {&GaussianModel_7293762848315,&GaussianModel_7293762972074,&PolynomialModel_7293763031229};
return classifierModelOutput<4,3>(input,models);
}
#include "InputNormalization.h"

double* NormalizationPreprocessing_7293763292995(double input[4]) {
static const double imins[4] = {4.3,2.0,1.0,0.1};
static const double iMaxMinusMin[4] = {3.6000000000000005,2.4000000000000004,5.9,2.4};
return inputNormalization<4>(input,imins,iMaxMinusMin);
}
#include "./classification/ConnectableClassifier.h"

double* ConnectableClassifier_7293762744601(double input[4]) {
static double* (*model)(const double*) = {&ClassifierModel_7293762797261};
static double* (*inputProcessingMethods[1])(double*) = {&NormalizationPreprocessing_7293763292995};
static double* (*outputProcessingMethods[0])(double*);
static const bool enabledInputs[4] = {true,true,true,true};
return connectableClassifierOutput<4,4,1,0>(input,model,enabledInputs,inputProcessingMethods,outputProcessingMethods);
}
int main(int argc, const char** argv) {
    double testInput[2];
    for (double i = 0; i < 10; i = i + 1) {
        for (double j = 0; j < 10; j = j + 1) {
            testInput[0] = i;
            testInput[1] = j;
            double * result = ConnectableClassifier_7293762744601(testInput);
            for (int k = 0; k < 2; k++) {
                cout << setprecision(16) << result[k] << " ";
            }
            cout << endl;
        }
    }
}