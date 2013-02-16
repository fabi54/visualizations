#include <cstdlib>
#include <stdio.h>
#include <math.h>
#include <cmath>
#include <iostream>
#include <iomanip>
using namespace std;

#include "./regression/SigmoidModel.h"

double SigmoidModel_6871983644611(const double input[4]) {
static const double parameters[7] = {-23.182436725179365,26.810932958161633,-46.50076457755718,-48.32655625780084,16.47131811310704,1.0000002093554439,-1.0863799960200626E-9};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871983892129(const double input[4]) {
static const double parameters[7] = {15.733547400377812,-23.379259578105867,46.381088430273365,41.3365429994598,-12.684355543931982,-1.000000866008719,1.0000008906969304};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871983952821(const double input[4]) {
static const double parameters[7] = {92.09995781353409,-40.551224697486596,-149.22239699379662,-99.79186797893483,70.003273129892,1.0000000888453178,-8.765991923242444E-8};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871984120929(const double input[4]) {
static const double parameters[7] = {37.20464926910108,6.316415845330656,-55.57959075167703,-72.05390220787153,16.2397341693511,1.0000002016179017,-1.5638960417094367E-10};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871984255234(const double input[4]) {
static const double parameters[7] = {-35.273700312623056,74.53558764192161,-97.9574954301367,-82.28575516373385,50.332488510557525,1.0000000880691216,-8.840426074006996E-8};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871984382624(const double input[4]) {
static const double parameters[7] = {-91.24434249385513,131.25187286064156,-184.31369810854792,-174.26963387025287,116.33484683211329,1.0000000809773006,-8.016714209271132E-8};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871984516300(const double input[4]) {
static const double parameters[7] = {-14.675751073778619,23.717340727770047,-42.39307445428364,-38.647513650912906,9.630851792235251,1.000002133426012,6.352049221551227E-9};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871984648370(const double input[4]) {
static const double parameters[7] = {1.4308419531167518,-12.372835036778289,-67.96371205506658,-56.522773475911265,36.54582629420807,1.0000020908244311,-1.3802156184197032E-7};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/BaggingEnsemble.h"

double ModelBagging_6871983593976(const double input[4]) {
static double (*models[8])(const double*) = {&SigmoidModel_6871983644611,&SigmoidModel_6871983892129,&SigmoidModel_6871983952821,&SigmoidModel_6871984120929,&SigmoidModel_6871984255234,&SigmoidModel_6871984382624,&SigmoidModel_6871984516300,&SigmoidModel_6871984648370};
return baggingEnsembleOutput<8,4>(input,models);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871984865087(const double input[4]) {
static const double parameters[7] = {14.670614962995316,-37.41177623421895,28.974156829804837,17.694474938634023,0.8946834017879259,0.5684304230670012,-9.035428315283378E-6};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871984996668(const double input[4]) {
static const double parameters[7] = {690.6734342738422,-1303.2150927610155,791.0864402466893,-1202.6015551581397,406.2158509838877,0.7167416996213748,0.04596955275905423};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871985053658(const double input[4]) {
static const double parameters[7] = {-878.4373387152835,-585.2281199900615,1932.8654562412617,-1361.6949525122002,209.77984052314986,0.6876335824709423,0.12682398518241575};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871985116795(const double input[4]) {
static const double parameters[7] = {-3.2301584229731772,11.047574156340357,-9.743619473700736,-6.068467004301823,-0.6828290499854082,-0.5082746514622484,0.4943978104008958};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871985171061(const double input[4]) {
static const double parameters[7] = {-845.6442828384158,-313.2164313202146,3654.6638044321053,-2341.532940684802,-103.16774882115286,0.5690990209596047,-0.0010764157670913434};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871985306413(const double input[4]) {
static const double parameters[7] = {286.4299037043945,186.94436224267335,-1176.3333759424295,734.0217710289494,-22.936553257047176,-0.551931621774194,0.5537281774189808};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871985362286(const double input[4]) {
static const double parameters[7] = {254.40772593044534,-1240.6426286823385,751.9881876535188,-708.4936853364989,345.4236899357402,0.48846727251613076,0.010667575940143495};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871985419416(const double input[4]) {
static const double parameters[7] = {-1.6691037119203664,-2.483945934590271,4.0596872389264265,-2.871634964074819,-1.7301938359274642,7.6160143686821495,-0.13309461388803098};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/BaggingEnsemble.h"

double ModelBagging_6871984854960(const double input[4]) {
static double (*models[8])(const double*) = {&SigmoidModel_6871984865087,&SigmoidModel_6871984996668,&SigmoidModel_6871985053658,&SigmoidModel_6871985116795,&SigmoidModel_6871985171061,&SigmoidModel_6871985306413,&SigmoidModel_6871985362286,&SigmoidModel_6871985419416};
return baggingEnsembleOutput<8,4>(input,models);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871985487302(const double input[4]) {
static const double parameters[7] = {0.03317657054182886,-0.03981833736668615,-0.038032465785086934,-0.06448164323663103,-0.39869076242448426,-68.67968683498763,27.10788518419909};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871985543315(const double input[4]) {
static const double parameters[7] = {-0.5962060728662764,-0.06952184882492275,1.964886740620955,1.9975578372216396,-6.7433755322008615,53.658942637575706,-0.1281384044755092};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871985630546(const double input[4]) {
static const double parameters[7] = {-103.76807581866501,894.9770702522328,-1109.9242803585366,-2073.4018739436856,1864.001559611674,-1.0000110664838273,1.0000103263349633};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871985687048(const double input[4]) {
static const double parameters[7] = {191.59023743526706,804.0452672007059,-615.5436883174029,-1664.749664253768,1013.3879627408692,-0.9677418889541802,0.9677418988256267};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871985741524(const double input[4]) {
static const double parameters[7] = {-4.631797136329047,-1.2392318073414499,10.970154690139807,3.7293413699000983,-7.7420187476682525,1.532173382508621,-0.047471795472935945};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871985798235(const double input[4]) {
static const double parameters[7] = {1.8021359620563757E-4,-0.038584051102676785,0.021768686602321653,-0.14315580054674523,0.7258600117432554,-43.53658561827127,28.94846283067948};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871985931492(const double input[4]) {
static const double parameters[7] = {-172.82158983609688,-224.2615867523642,805.6561879041753,280.8604407453397,-541.3434283301356,0.9814820243203453,-4.2489532172607635E-7};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_6871986083955(const double input[4]) {
static const double parameters[7] = {-154.6106446311171,-292.9674523020062,848.6307571520505,249.37723363071024,-518.4560465832185,0.9468268439815279,-0.001069819914215885};
return sigmoidModelOutput<4>(input,parameters);
}
#include "./regression/BaggingEnsemble.h"

double ModelBagging_6871985478083(const double input[4]) {
static double (*models[8])(const double*) = {&SigmoidModel_6871985487302,&SigmoidModel_6871985543315,&SigmoidModel_6871985630546,&SigmoidModel_6871985687048,&SigmoidModel_6871985741524,&SigmoidModel_6871985798235,&SigmoidModel_6871985931492,&SigmoidModel_6871986083955};
return baggingEnsembleOutput<8,4>(input,models);
}
#include "./classification/ClassifierModel.h"

double* ClassifierModel_6871983555703(const double input[4]) {
static double (*models[3])(const double*) = {&ModelBagging_6871983593976,&ModelBagging_6871984854960,&ModelBagging_6871985478083};
return classifierModelOutput<4,3>(input,models);
}
#include "InputNormalization.h"

double* NormalizationPreprocessing_6871986358012(double input[4]) {
static const double imins[4] = {4.3,2.0,1.0,0.1};
static const double iMaxMinusMin[4] = {3.6000000000000005,2.4000000000000004,5.9,2.4};
return inputNormalization<4>(input,imins,iMaxMinusMin);
}
#include "./classification/ConnectableClassifier.h"

double* ConnectableClassifier_6871983502345(double input[4]) {
static double* (*model)(const double*) = {&ClassifierModel_6871983555703};
static double* (*inputProcessingMethods[1])(double*) = {&NormalizationPreprocessing_6871986358012};
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
            double * result = ConnectableClassifier_6871983502345(testInput);
            for (int k = 0; k < 2; k++) {
                cout << setprecision(16) << result[k] << " ";
            }
            cout << endl;
        }
    }
}