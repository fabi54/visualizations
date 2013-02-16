#include <cstdlib>
#include <stdio.h>
#include <math.h>
#include <cmath>
#include <iostream>
#include <iomanip>
using namespace std;

#include "./regression/SigmoidModel.h"

double SigmoidModel_2764699408561(const double input[12]) {
static const double parameters[15] = {3.2475601402999517,0.24664452076826357,0.21100137549150816,-0.6390207928442119,0.49800723358579213,1.847405221984269,-1.4223531254598374,-0.5467569630812815,-0.359168155912148,0.40620892517414814,0.6604979536115116,0.14416312983360638,0.4709000779283342,-1.8817954588052717,1.9214726085395772};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_2764699589241(const double input[12]) {
static const double parameters[15] = {-2.129490621117799,0.035686003614581685,-0.31173265861796384,0.1781895881176601,-0.004140193730453978,-1.6847018108433462,0.5176593135803899,-0.08731231664618852,0.12103160326836182,-0.15676648653485908,-0.05533682587089127,-0.09462209478710042,-0.7180767483648367,2.6764689589312205,-0.0029215213673494593};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_2764699698891(const double input[12]) {
static const double parameters[15] = {3.4660884408003914,0.46393321283512573,0.1357231425729988,-0.32971058676854337,0.8133401241783282,2.316579041108684,-1.3117373410368298,0.4559144139047752,-0.6191497695965228,0.38766971455150506,0.38761073049775946,-0.04134147151174578,-0.3305020011343159,-1.3757587403750822,1.4171153032577812};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_2764699794993(const double input[12]) {
static const double parameters[15] = {-3.3387325507353167,-0.7496470221121736,-0.10679928008438094,0.6340943017856062,-0.41896750558133883,-1.4343273166645087,1.2002372571652282,-0.09603882846481888,0.5020910691298267,-0.6652295757183264,-0.2533782550418389,-0.18161228748559408,-0.6601185125760598,1.9969777615400255,0.05131249136693481};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_2764699898916(const double input[12]) {
static const double parameters[15] = {0.7537843795619227,-0.0662788469274392,0.03291104501853877,0.07196552216858645,-0.06695726025487797,0.47901368383977044,-0.20363908921865967,0.0954822035632813,-0.1967639138856949,0.10631454747574508,0.20146942863870385,0.07970690936213452,-0.42845957378304883,-2.5572910082232547,1.6887605722872518};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_2764699998999(const double input[12]) {
static const double parameters[15] = {3.463180455414898,1.66066729428291,0.552678599865938,-0.12574644280593875,0.07612172194536106,1.7522370105744458,-0.9682827850322175,0.07539670822973397,-0.6577987753929102,0.5218881289905664,0.5408095604719296,0.04551363841356244,0.4098016859382608,-2.4077672629141254,2.455865159804231};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_2764700129881(const double input[12]) {
static const double parameters[15] = {-4.587066024282216,-1.1633881235104502,-0.28443074116720246,0.7878899802964582,-0.6962520655213823,-2.376492823106682,1.1923627812193962,-0.02203674301820184,0.7574160836764976,-0.7896465688106165,-0.01171997671618905,0.06484744411594467,0.2656910809588028,1.3033957127192861,0.06691956920047194};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_2764700229754(const double input[12]) {
static const double parameters[15] = {2.745131036051921,1.3034879626252058,-0.01880461470326386,-0.17519912128070814,0.5945062442585858,2.1932025788251797,-1.3862680141906256,0.5319670480296793,-0.4820126352065388,0.5020405694192068,0.23720953085629704,0.353701336430877,0.7173273363049878,-3.3599220603575968,3.4133135900519154};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/BoostingEnsemble.h"

double ModelBoostingRT_2764699346403(const double input[12]) {
static double (*models[8])(const double*) = {&SigmoidModel_2764699408561,&SigmoidModel_2764699589241,&SigmoidModel_2764699698891,&SigmoidModel_2764699794993,&SigmoidModel_2764699898916,&SigmoidModel_2764699998999,&SigmoidModel_2764700129881,&SigmoidModel_2764700229754};
static const double modelWeights[8] = {0.2294563079483446,0.15850761049170725,0.1347682459416393,0.11136641145353048,0.10777651432060674,0.08756239348707255,0.086813253544955,0.08374926281214413};
return boostingEnsembleOutput<8,12>(input,models,modelWeights);
}
#include "OutputNormalization.h"

double NormalizationPreprocessing_2764700488377(double input) {
return outputNormalization(input,1.73,36.24);
}
#include "InputNormalization.h"

double* NormalizationPreprocessing_2764700526719(double input[12]) {
static const double imins[12] = {5.0,0.00632,0.0,0.46,38.5,3.561,2.9,1.1296,1.0,187.0,12.6,0.32};
static const double iMaxMinusMin[12] = {45.0,88.96988,100.0,27.279999999999998,48.599999999999994,5.218999999999999,97.1,10.9969,23.0,524.0,9.4,396.58};
return inputNormalization<12>(input,imins,iMaxMinusMin);
}
#include "./regression/ConnectableModel.h"

double ConnectableModel_2764699300098(double input[12]) {
static double (*model)(const double*) = {&ModelBoostingRT_2764699346403};
static double* (*inputProcessingMethods[1])(double*) = {&NormalizationPreprocessing_2764700526719};
static double (*outputProcessingMethods[1])(const double) = {&NormalizationPreprocessing_2764700488377};
static const bool enabledInputs[12] = {true,true,true,true,true,true,true,true,true,true,true,true};
return connectableModelOutput<12,12,1,1>(input,model,enabledInputs,inputProcessingMethods,outputProcessingMethods);
}
int main(int argc, const char** argv) {
    double testInput[1];
    for (double i = 0; i < 10; i = i + 0.25) {
            testInput[0] = i;
            double result = ConnectableModel_2764699300098(testInput);
            cout << setprecision(16) << result << endl;
    }
}