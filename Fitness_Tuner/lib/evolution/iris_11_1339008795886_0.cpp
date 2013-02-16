#include <cstdlib>
#include <stdio.h>
#include <math.h>
#include <cmath>
#include <iostream>
#include <iomanip>
using namespace std;

#include "./regression/GaussianModel.h"

double GaussianModel_2167360236087(const double input[3]) {
static const double parameters[6] = {-0.16528082540655914,-0.26775816186129137,-1.0492486717091238,-0.013175956793813197,5.394810367049951,-0.3151581152317906};
return gaussianModelOutput<3>(input,parameters);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167360397770(const double input[3]) {
static const double parameters[6] = {-0.18990168983400138,0.8641464312397806,-0.9432045549148972,-0.1176678098394344,26.379749244820868,-0.42361776761659636};
return gaussianModelOutput<3>(input,parameters);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167360453992(const double input[3]) {
static const double parameters[6] = {-0.6404010150708758,1.6609453996609953,0.7606875531775811,-0.04098685091209154,3.1852108020955736,-0.42536988330881514};
return gaussianModelOutput<3>(input,parameters);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_2167360193903(const double input[3]) {
static double (*models[3])(const double*) = {&GaussianModel_2167360236087,&GaussianModel_2167360397770,&GaussianModel_2167360453992};
return classifierModelOutput<3,3>(input,models);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167360590253(const double input[3]) {
static const double parameters[6] = {1.9733096104046117,-5.81479767476428,-1.0740920712979405,0.8073374936374523,12.997735634098785,1.9022746878632355};
return gaussianModelOutput<3>(input,parameters);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167360645567(const double input[3]) {
static const double parameters[6] = {-0.4850769536917355,1.239379658768878,0.06318073961059695,-0.0035035278223747328,-0.0974677639817034,-0.9219350132202099};
return gaussianModelOutput<3>(input,parameters);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_2167360580405(const double input[3]) {
static double (*models[2])(const double*) = {&GaussianModel_2167360590253,&GaussianModel_2167360645567};
return classifierModelOutput<3,2>(input,models);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167360722881(const double input[3]) {
static const double parameters[6] = {-0.1661210875822206,-0.2649162609072799,-1.0418792605745564,-0.011359114847985127,5.189096192248671,-0.3129582880392843};
return gaussianModelOutput<3>(input,parameters);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167360773656(const double input[3]) {
static const double parameters[6] = {-0.1909868332676427,0.8610217040892363,-0.9245380611548868,-0.1165181515838293,23.73639899571896,-0.42482870446015447};
return gaussianModelOutput<3>(input,parameters);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167360822824(const double input[3]) {
static const double parameters[6] = {-0.6834941438747553,1.6564646073860219,0.8851603755649411,-0.038196046318990574,5.665464986441767,-0.4160398464324285};
return gaussianModelOutput<3>(input,parameters);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_2167360712685(const double input[3]) {
static double (*models[3])(const double*) = {&GaussianModel_2167360722881,&GaussianModel_2167360773656,&GaussianModel_2167360822824};
return classifierModelOutput<3,3>(input,models);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167360891478(const double input[3]) {
static const double parameters[6] = {-0.3090650754839894,-1.5691506768019292,-2.4576839036552385,0.813543501794314,10.662838513280306,0.3110943566380517};
return gaussianModelOutput<3>(input,parameters);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167360941694(const double input[3]) {
static const double parameters[6] = {-0.7888327260401004,0.39223227313372,3.223896085444606,-1.0475074147122094,0.5515365083080379,4.492240062608632};
return gaussianModelOutput<3>(input,parameters);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_2167360882818(const double input[3]) {
static double (*models[2])(const double*) = {&GaussianModel_2167360891478,&GaussianModel_2167360941694};
return classifierModelOutput<3,2>(input,models);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167361021523(const double input[3]) {
static const double parameters[6] = {-0.15251233762485528,-0.27756234812036573,-1.2567498488925226,-0.013665504594353615,10.721138825041544,-0.27358371285485006};
return gaussianModelOutput<3>(input,parameters);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167361075510(const double input[3]) {
static const double parameters[6] = {-0.17741624682775525,0.8293548584164621,-0.7041464719591478,-0.11385912137513432,9.589016117668352,-0.4627274204016593};
return gaussianModelOutput<3>(input,parameters);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167361124120(const double input[3]) {
static const double parameters[6] = {-0.6730322780274944,1.6557457767178783,0.8357706265050711,-0.03915649137175146,4.686176051171238,-0.42350327864220044};
return gaussianModelOutput<3>(input,parameters);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_2167361012653(const double input[3]) {
static double (*models[3])(const double*) = {&GaussianModel_2167361021523,&GaussianModel_2167361075510,&GaussianModel_2167361124120};
return classifierModelOutput<3,3>(input,models);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167361194031(const double input[3]) {
static const double parameters[6] = {-0.17637302329408167,-0.5513382700991292,-2.1582282523487932,0.81518361391749,16.015422112896154,-0.03815711394336308};
return gaussianModelOutput<3>(input,parameters);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167361248088(const double input[3]) {
static const double parameters[6] = {-0.45582997959652005,1.2434511925497507,0.05996521200766898,0.007940228229225282,-0.4983033387220535,-0.8903948984638004};
return gaussianModelOutput<3>(input,parameters);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_2167361184602(const double input[3]) {
static double (*models[2])(const double*) = {&GaussianModel_2167361194031,&GaussianModel_2167361248088};
return classifierModelOutput<3,2>(input,models);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167361318348(const double input[3]) {
static const double parameters[6] = {-0.17679983033219676,-0.2625285569302958,-0.8530210245884021,-0.01097068997243053,2.5028398670065704,-0.339274184385232};
return gaussianModelOutput<3>(input,parameters);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167361368215(const double input[3]) {
static const double parameters[6] = {-0.2137915584125473,0.8297545719639858,-0.739698087754489,-0.12074616400464892,8.088175609713362,-0.4250312638935928};
return gaussianModelOutput<3>(input,parameters);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167361416266(const double input[3]) {
static const double parameters[6] = {-0.6974049152596383,1.6583416124119732,0.9588361301521335,-0.04178919003118698,8.084528427560473,-0.40788646125695194};
return gaussianModelOutput<3>(input,parameters);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_2167361309548(const double input[3]) {
static double (*models[3])(const double*) = {&GaussianModel_2167361318348,&GaussianModel_2167361368215,&GaussianModel_2167361416266};
return classifierModelOutput<3,3>(input,models);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167361494069(const double input[3]) {
static const double parameters[6] = {-0.16535378399254333,-5.6413860251920145,-1.0272651050329447,0.7876778416167495,9.389296274585755,1.7771787142089701};
return gaussianModelOutput<3>(input,parameters);
}
#include "./regression/GaussianModel.h"

double GaussianModel_2167361545682(const double input[3]) {
static const double parameters[6] = {-0.45725246975391853,1.2306809536195207,0.06762072749482638,0.0060039089316537226,-0.5564038782150625,-0.890408807618569};
return gaussianModelOutput<3>(input,parameters);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_2167361485269(const double input[3]) {
static double (*models[2])(const double*) = {&GaussianModel_2167361494069,&GaussianModel_2167361545682};
return classifierModelOutput<3,2>(input,models);
}
#include "./classification/ArbitratingEnsemble.h"

double* ClassifierArbitrating_2167360123014(const double input[3]) {
static double* (*models[8])(const double*) = {&ClassifierModel_2167360193903,&ClassifierModel_2167360580405,&ClassifierModel_2167360712685,&ClassifierModel_2167360882818,&ClassifierModel_2167361012653,&ClassifierModel_2167361184602,&ClassifierModel_2167361309548,&ClassifierModel_2167361485269};
return arbitratingEnsembleOutput<3,8>(input,models);
}
#include "InputNormalization.h"

double* NormalizationPreprocessing_2167361722869(double input[4]) {
static const double imins[4] = {4.3,2.0,1.0,0.1};
static const double iMaxMinusMin[4] = {3.6000000000000005,2.4000000000000004,5.9,2.4};
return inputNormalization<4>(input,imins,iMaxMinusMin);
}
#include "./classification/ConnectableClassifier.h"

double* ConnectableClassifier_2167360078316(double input[3]) {
static double* (*model)(const double*) = {&ClassifierArbitrating_2167360123014};
static double* (*inputProcessingMethods[1])(double*) = {&NormalizationPreprocessing_2167361722869};
static double* (*outputProcessingMethods[0])(double*);
static const bool enabledInputs[3] = {true,true,true};
return connectableClassifierOutput<3,3,1,0>(input,model,enabledInputs,inputProcessingMethods,outputProcessingMethods);
}
int main(int argc, const char** argv) {
    double testInput[2];
    for (double i = 0; i < 10; i = i + 1) {
        for (double j = 0; j < 10; j = j + 1) {
            testInput[0] = i;
            testInput[1] = j;
            double * result = ConnectableClassifier_2167360078316(testInput);
            for (int k = 0; k < 2; k++) {
                cout << setprecision(16) << result[k] << " ";
            }
            cout << endl;
        }
    }
}