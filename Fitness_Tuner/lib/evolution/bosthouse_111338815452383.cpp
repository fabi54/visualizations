#include <cstdlib>
#include <stdio.h>
#include <math.h>
#include <cmath>
#include <iostream>
#include <iomanip>
using namespace std;

#include "./regression/SigmoidModel.h"

double SigmoidModel_4219222697141(const double input[12]) {
static const double parameters[15] = {-2.803732593669136,-0.5236178214262711,-0.3587569781576084,0.18768357355800605,-0.2437624628253502,-1.7439651539632357,0.9373652608545867,-0.1421043008148853,0.31052496363803433,-0.2334080198491538,-0.3809794354291805,-0.18577296747983865,-0.0919178227053759,1.7812148318602767,0.03141553617808271};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4219222894513(const double input[12]) {
static const double parameters[15] = {-3.3310631734557425,-0.6847781868745183,-0.31712459118704667,0.3112241998840993,-0.12120428488765628,-1.4551161287292906,0.9877423770949512,-0.07696246855259775,0.43424174980178537,-0.5087770294065986,-0.16957483370337229,-0.039420801273424684,-0.6838852084002262,2.047210668987345,0.04582118759673845};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4219223004722(const double input[12]) {
static const double parameters[15] = {-2.9815403905778037,-0.8570292756226208,-0.5662606793594354,0.453453780320994,-0.30769611521420626,-2.0846146622059467,1.2074610988550663,0.06558171459946896,0.686484054581413,-0.6641419922860117,-0.3608863084410771,-0.0077283553269755405,-0.7398104259273108,2.257650284837097,0.04793747715948197};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4219223107808(const double input[12]) {
static const double parameters[15] = {-2.549887050582538,-0.1800501943933911,-0.6971556306619076,0.36327360945998677,-0.396221241950631,-1.3032230275534538,1.3424632689351006,0.46004904325074103,0.2822002187854382,-0.10059658496262042,-0.34813261741604845,-0.17312131206563752,-1.4160375251444721,2.5450608387362927,0.04110861471448988};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4219223243859(const double input[12]) {
static const double parameters[15] = {-2.856808360238401,-0.034810074040308406,-0.06531112720865273,0.26004919531034965,-0.7229940752543588,-1.3492417931577347,1.490312216001648,-0.22097099511040638,0.4190858686586096,-0.22464079819942248,-0.2486734238043608,-0.13295169860461992,-0.08855719556492685,1.1391881922283418,0.014823282395723548};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4219223348201(const double input[12]) {
static const double parameters[15] = {-3.0713128133627894,-0.845048835155609,-0.22550234829388777,0.5667839834764663,-0.19175343396786654,-1.8790686701198525,0.6767119934841701,-0.2968764580022504,0.4872712393435343,-0.48716041607010335,-0.13778376979541856,0.013968496713264687,-0.4230788188891712,2.011589232003722,0.04556937030071756};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/SigmoidModel.h"

double SigmoidModel_4219223447516(const double input[12]) {
static const double parameters[15] = {-3.1879786939103463,-0.6400443339994556,-0.011652086380692997,0.3721932498980093,-0.3682782871548924,-1.469832699994333,1.018942805524633,-0.38105360820203094,0.49719809651121316,-0.6460765751755037,-0.29113012791097825,-0.012639386477100362,-0.3139000459021556,1.8023394942460662,0.03570839572504938};
return sigmoidModelOutput<12>(input,parameters);
}
#include "./regression/BaggingEnsemble.h"

double ModelBagging_4219222637427(const double input[12]) {
static double (*models[7])(const double*) = {&SigmoidModel_4219222697141,&SigmoidModel_4219222894513,&SigmoidModel_4219223004722,&SigmoidModel_4219223107808,&SigmoidModel_4219223243859,&SigmoidModel_4219223348201,&SigmoidModel_4219223447516};
return baggingEnsembleOutput<7,12>(input,models);
}
#include "OutputNormalization.h"

double NormalizationPreprocessing_4219223675547(double input) {
return outputNormalization(input,1.73,36.24);
}
#include "InputNormalization.h"

double* NormalizationPreprocessing_4219223729744(double input[12]) {
static const double imins[12] = {5.0,0.00632,0.0,0.46,38.5,3.561,2.9,1.1296,1.0,187.0,12.6,0.32};
static const double iMaxMinusMin[12] = {45.0,88.96988,100.0,27.279999999999998,48.599999999999994,5.218999999999999,97.1,10.9969,23.0,524.0,9.4,396.58};
return inputNormalization<12>(input,imins,iMaxMinusMin);
}
#include "./regression/ConnectableModel.h"

double ConnectableModel_4219222587072(double input[12]) {
static double (*model)(const double*) = {&ModelBagging_4219222637427};
static double* (*inputProcessingMethods[1])(double*) = {&NormalizationPreprocessing_4219223729744};
static double (*outputProcessingMethods[1])(const double) = {&NormalizationPreprocessing_4219223675547};
static const bool enabledInputs[12] = {true,true,true,true,true,true,true,true,true,true,true,true};
return connectableModelOutput<12,12,1,1>(input,model,enabledInputs,inputProcessingMethods,outputProcessingMethods);
}
int main(int argc, const char** argv) {
    double testInput[1];
    for (double i = 0; i < 10; i = i + 0.25) {
            testInput[0] = i;
            double result = ConnectableModel_4219222587072(testInput);
            cout << setprecision(16) << result << endl;
    }
}