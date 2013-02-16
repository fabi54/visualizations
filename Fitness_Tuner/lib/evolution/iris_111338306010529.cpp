#include <cstdlib>
#include <stdio.h>
#include <math.h>
#include <cmath>
#include <iostream>
#include <iomanip>
using namespace std;

#include "./regression/SigmoidModel.h"

double SigmoidModel_4670165363093(const double input[4]) {
static const double parameters[7] = {-42.39882661894646,34.501232501268284,-43.893454296800115,-22.489469741475826,14.888736229343854,1.0000001897576694,-1.33229874760916E-7};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4670165515697(const double input[4]) {
static const double parameters[7] = {-0.23381659611026762,-0.019183426843631307,0.28609402027725167,0.3738976411426078,-0.10176930498170782,0.23494417861712646,0.21401535264428986};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4670165565563(const double input[4]) {
static const double parameters[7] = {-353.6230722906586,75.87351094466293,1074.3728494225081,294.2342825037289,-735.5548435141031,0.9622182807420834,-5.641204351242038E-4};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_4670165316090(const double input[4]) {
static double (*models[3])(const double*) = {&SigmoidModel_4670165363093,&SigmoidModel_4670165515697,&SigmoidModel_4670165565563};
return classifierModelOutput<4,3>(input,models);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4670165711113(const double input[4]) {
static const double parameters[7] = {3.77095085589187,-8.861431455954547,0.5837217720019202,-4.4417827525763816,3.55484193198249,0.24009561003544946,0.7175981266325372};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4670165760840(const double input[4]) {
static const double parameters[7] = {0.01226697450482421,0.344686273451555,-0.0146819172231525,0.03545381247286,0.13546955705743596,-0.2482392552383454,0.1535947081111197};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_4670165697145(const double input[4]) {
static double (*models[2])(const double*) = {&SigmoidModel_4670165711113,&SigmoidModel_4670165760840};
return classifierModelOutput<4,2>(input,models);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4670165835361(const double input[4]) {
static const double parameters[7] = {-46.82464158569045,41.21455159053004,-88.56984141990282,8.990065184516356,13.20356669584159,1.0000002222656208,-2.972720579494837E-9};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4670165896332(const double input[4]) {
static const double parameters[7] = {0.14105535265271194,-0.6257238329672883,0.5959859216755369,-0.1187501969236206,-0.08921715461759881,0.7803713337568,-0.035804315339777695};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4670165943126(const double input[4]) {
static const double parameters[7] = {-16.745436639124897,-52.230641295662934,93.47713169825295,141.9595572314869,-128.27080371699253,0.9851699258052188,0.00603018213587137};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_4670165822021(const double input[4]) {
static double (*models[3])(const double*) = {&SigmoidModel_4670165835361,&SigmoidModel_4670165896332,&SigmoidModel_4670165943126};
return classifierModelOutput<4,3>(input,models);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4670166013316(const double input[4]) {
static const double parameters[7] = {240.28230981652518,-1574.6719878062247,83.16156197203497,-724.6803675199201,599.5083909606565,0.15532383814613926,0.792684256401315};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4670166060878(const double input[4]) {
static const double parameters[7] = {0.15807069782148048,0.07283702789789043,-0.006209844792308957,0.13049179781730857,-0.25793612835945817,0.21398302832583838,-0.08877235200023478};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_4670166000535(const double input[4]) {
static double (*models[2])(const double*) = {&SigmoidModel_4670166013316,&SigmoidModel_4670166060878};
return classifierModelOutput<4,2>(input,models);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4670166142243(const double input[4]) {
static const double parameters[7] = {-40.38621918827104,41.07611343240867,-75.11669975143442,-75.94760122560362,20.29223574205782,1.0000001504591387,-1.0768775733855504E-9};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4670166200491(const double input[4]) {
static const double parameters[7] = {5.940092959559389,-30.617266753804447,22.04463075575638,14.43935065546211,0.5294559873591047,0.5012038242787193,0.001671814752660365};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4670166255177(const double input[4]) {
static const double parameters[7] = {-20.269155213916164,-177.41483731472368,564.764342418755,634.1628097625638,-728.068682808484,0.9988809343933083,0.010358135701647389};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_4670166129951(const double input[4]) {
static double (*models[3])(const double*) = {&SigmoidModel_4670166142243,&SigmoidModel_4670166200491,&SigmoidModel_4670166255177};
return classifierModelOutput<4,3>(input,models);
}
#include "./classification/ArbitratingEnsemble.h"

double* ClassifierArbitrating_4670165267131(const double input[4]) {
static double* (*models[5])(const double*) = {&ClassifierModel_4670165316090,&ClassifierModel_4670165697145,&ClassifierModel_4670165822021,&ClassifierModel_4670166000535,&ClassifierModel_4670166129951};
return arbitratingEnsembleOutput<4,5>(input,models);
}
#include "InputNormalization.h"

double* NormalizationPreprocessing_4670166399678(double input[4]) {
static const double imins[4] = {4.3,2.0,1.0,0.1};
static const double iMaxMinusMin[4] = {3.6000000000000005,2.4000000000000004,5.9,2.4};
return inputNormalization<4>(input,imins,iMaxMinusMin);
}
#include "./classification/ConnectableClassifier.h"

double* ConnectableClassifier_4670165213912(double input[4]) {
static double* (*model)(const double*) = {&ClassifierArbitrating_4670165267131};
static double* (*inputProcessingMethods[1])(double*) = {&NormalizationPreprocessing_4670166399678};
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
            double * result = ConnectableClassifier_4670165213912(testInput);
            for (int k = 0; k < 2; k++) {
                cout << setprecision(16) << result[k] << " ";
            }
            cout << endl;
        }
    }
}