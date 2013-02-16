#include <cstdlib>
#include <stdio.h>
#include <math.h>
#include <cmath>
#include <iostream>
#include <iomanip>
using namespace std;

#include "./regression/ExpModel.h"

double ExpModel_970554901531(const double input[12]) {
static const double parameters[16] = {-2.1229352262827437,-0.276879665161405,-0.13343333403975946,0.3354656887391484,-0.31909503310346604,-1.0433077077693662,0.7803899821011565,-0.06914685404966904,0.3673903793593636,-0.39017000789785555,-0.2716244433084762,-0.07362615454729925,-0.5307415297617095,1.3445203513747368,-2.548366745407308E-4,1.0101293390581114};
return expModelOutput<12>(input,parameters);
}
#include "./regression/ExpModel.h"

double ExpModel_970555175239(const double input[13]) {
static const double parameters[17] = {-1.7406219117075443,-0.2633356918348307,-0.1964067348752897,0.12251338919044144,0.0881598571666318,-0.8758678414507308,0.6385331037349067,0.0957643176236323,0.060101386158098145,-0.17946332136059814,-0.04917740283451146,-0.08171418234430111,1.3261883415926368,-0.3092351426267258,0.5275478899965159,-0.07483283601390507,0.6180932272706204};
return expModelOutput<13>(input,parameters);
}
#include "./regression/ExpModel.h"

double ExpModel_970555301582(const double input[14]) {
static const double parameters[18] = {-1.1595682630837525,0.3389598069323683,-0.12267219470097869,0.19919372196561902,0.010388375367885304,-0.8213001689557455,0.3699962355280103,-0.29041952312400765,-0.020999612745560867,0.10914956226377635,-0.0981548369407153,0.22461064392031915,1.111770536899067,1.1251925812384456,-0.30918802692015146,0.6205820161900816,-0.3249369401839246,0.31580290912039327};
return expModelOutput<14>(input,parameters);
}
#include "./regression/ExpModel.h"

double ExpModel_970555425900(const double input[15]) {
static const double parameters[19] = {-2.789890805733116,-0.11733214319143512,0.1129365485384877,0.26582965826990335,0.17474238576859014,-1.9687663815194156,1.1382903336188184,-0.2162967169407963,0.10041705782646906,0.11273509253422158,0.1272222482449255,-0.5292184456381861,2.6973765698571346,2.4888636539261575,2.4655886960434574,-1.0264670724312506,0.890363494373783,-0.5944627908437974,0.0783743108698933};
return expModelOutput<15>(input,parameters);
}
#include "./regression/ExpModel.h"

double ExpModel_970555562161(const double input[16]) {
static const double parameters[20] = {-2.6979741818596077,-0.7947005328846105,-0.026537111057414013,-0.1651674279859759,0.1415827444727936,-1.6880537678542966,1.5197962302560541,0.1495138707897512,0.5251456844673303,-0.6347914409481101,0.15265119419444034,-0.27308251250131527,1.9112515665832566,1.8886835017249517,1.8510026293672397,1.5116105195778886,-0.5327568720599075,0.7784592310074239,-0.5296281111506671,0.0947899410057654};
return expModelOutput<16>(input,parameters);
}
#include "./regression/CascadeGenEnsemble.h"

double ModelCascadeGen_970554841049(const double input[12]) {
static double (*models[5])(const double*) = {&ExpModel_970554901531,&ExpModel_970555175239,&ExpModel_970555301582,&ExpModel_970555425900,&ExpModel_970555562161};
return cascadeGenEnsembleOutput<5,12>(input,models);
}
#include "OutputNormalization.h"

double NormalizationPreprocessing_970555817500(double input) {
return outputNormalization(input,1.73,36.24);
}
#include "InputNormalization.h"

double* NormalizationPreprocessing_970555862408(double input[12]) {
static const double imins[12] = {5.0,0.00632,0.0,0.46,38.5,3.561,2.9,1.1296,1.0,187.0,12.6,0.32};
static const double iMaxMinusMin[12] = {45.0,88.96988,100.0,27.279999999999998,48.599999999999994,5.218999999999999,97.1,10.9969,23.0,524.0,9.4,396.58};
return inputNormalization<12>(input,imins,iMaxMinusMin);
}
#include "./regression/ConnectableModel.h"

double ConnectableModel_970554789995(double input[12]) {
static double (*model)(const double*) = {&ModelCascadeGen_970554841049};
static double* (*inputProcessingMethods[1])(double*) = {&NormalizationPreprocessing_970555862408};
static double (*outputProcessingMethods[1])(const double) = {&NormalizationPreprocessing_970555817500};
static const bool enabledInputs[12] = {true,true,true,true,true,true,true,true,true,true,true,true};
return connectableModelOutput<12,12,1,1>(input,model,enabledInputs,inputProcessingMethods,outputProcessingMethods);
}
int main(int argc, const char** argv) {
    double testInput[1];
    for (double i = 0; i < 10; i = i + 0.25) {
            testInput[0] = i;
            double result = ConnectableModel_970554789995(testInput);
            cout << setprecision(16) << result << endl;
    }
}