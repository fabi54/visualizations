#include <cstdlib>
#include <stdio.h>
#include <math.h>
#include <cmath>
#include <iostream>
#include <iomanip>
using namespace std;

#include "NeuralNet.h"

double* RapidNeuralNetClassifier_6320814500092(const double input[4]) {
double * result = new double[3];
result[0]=sigmoidNeural(4.404893163661791*sigmoidNeural(-1.4655301975773698*sigmoidNeural(0.2961452019453649*(input[0]-0.5)/0.5+0.3670878800454527*(input[1]-0.5)/0.5+-2.0099634142715943*(input[2]-0.5)/0.5+-1.428721192889837*(input[3]-0.5)/0.5+-0.04353069315608201)+-2.1301209134828314*sigmoidNeural(0.755301853150692*(input[0]-0.5)/0.5+0.6414472257812988*(input[1]-0.5)/0.5+-2.5551530835536136*(input[2]-0.5)/0.5+-1.7838270716959457*(input[3]-0.5)/0.5+0.34320654125081707)+2.3906009250484233*sigmoidNeural(-0.7016339529808838*(input[0]-0.5)/0.5+-0.9270449607807927*(input[1]-0.5)/0.5+2.0095361973128*(input[2]-0.5)/0.5+1.4406359999534528*(input[3]-0.5)/0.5+0.199199197669801)+-0.6297222925531978*sigmoidNeural(0.04910299967807794*(input[0]-0.5)/0.5+0.06159361390684677*(input[1]-0.5)/0.5+-1.1497994235297964*(input[2]-0.5)/0.5+-0.8634725076328534*(input[3]-0.5)/0.5+-0.20047329013423643)+-1.9503730583097738*sigmoidNeural(0.5860442320535056*(input[0]-0.5)/0.5+0.7139585083815517*(input[1]-0.5)/0.5+-2.3627357479523066*(input[2]-0.5)/0.5+-1.7560830559735956*(input[3]-0.5)/0.5+0.21623888269539632)+-2.265030030948421*sigmoidNeural(0.7836455676265551*(input[0]-0.5)/0.5+0.948985137786096*(input[1]-0.5)/0.5+-2.506324255713226*(input[2]-0.5)/0.5+-1.8518621008185108*(input[3]-0.5)/0.5+0.2889141963908535)+-2.006475639469029*sigmoidNeural(0.6719849617054308*(input[0]-0.5)/0.5+0.6375251158220641*(input[1]-0.5)/0.5+-2.4574840319035935*(input[2]-0.5)/0.5+-1.7551302122546153*(input[3]-0.5)/0.5+0.27049006108073037)+-1.9819148792419483*sigmoidNeural(0.6728370337504231*(input[0]-0.5)/0.5+0.5963916555315318*(input[1]-0.5)/0.5+-2.4391439665741084*(input[2]-0.5)/0.5+-1.7224740051558778*(input[3]-0.5)/0.5+0.2559096253159797)+1.2097250608712637*sigmoidNeural(-0.36995295064678335*(input[0]-0.5)/0.5+-0.47125867563778473*(input[1]-0.5)/0.5+1.1924635537304673*(input[2]-0.5)/0.5+0.7989622331550941*(input[3]-0.5)/0.5+0.10648417228505032)+-0.5793154877005062*sigmoidNeural(-0.05410682904476424*(input[0]-0.5)/0.5+0.17041127458468036*(input[1]-0.5)/0.5+-1.0471090539847177*(input[2]-0.5)/0.5+-0.7412008039497219*(input[3]-0.5)/0.5+-0.2809103396849757)+-2.279394333045271*sigmoidNeural(0.816859103323715*(input[0]-0.5)/0.5+0.772505851742145*(input[1]-0.5)/0.5+-2.593007403617786*(input[2]-0.5)/0.5+-1.824902843860694*(input[3]-0.5)/0.5+0.34621995587466)+-2.2877783969766043*sigmoidNeural(0.8273715129505309*(input[0]-0.5)/0.5+1.0278382578922198*(input[1]-0.5)/0.5+-2.524314269333443*(input[2]-0.5)/0.5+-1.8370783281605634*(input[3]-0.5)/0.5+0.3000343166341949)+-2.208077029716016*sigmoidNeural(0.7430984301281386*(input[0]-0.5)/0.5+0.7893786906514905*(input[1]-0.5)/0.5+-2.5432808069080233*(input[2]-0.5)/0.5+-1.7757953641669137*(input[3]-0.5)/0.5+0.3088835722268646)+-0.9588008591265094*sigmoidNeural(0.1319924571839464*(input[0]-0.5)/0.5+0.23987833028192457*(input[1]-0.5)/0.5+-1.5441127887594583*(input[2]-0.5)/0.5+-1.0996304875154017*(input[3]-0.5)/0.5+-0.12117414406985307)+-0.783601230855237*sigmoidNeural(0.0441843542791344*(input[0]-0.5)/0.5+0.15936233263181368*(input[1]-0.5)/0.5+-1.3359521729709205*(input[2]-0.5)/0.5+-0.907336116295823*(input[3]-0.5)/0.5+-0.2049163194617054)+2.4164305106123387)+4.500702550515069*sigmoidNeural(-1.3459199795813357*sigmoidNeural(0.2961452019453649*(input[0]-0.5)/0.5+0.3670878800454527*(input[1]-0.5)/0.5+-2.0099634142715943*(input[2]-0.5)/0.5+-1.428721192889837*(input[3]-0.5)/0.5+-0.04353069315608201)+-2.0028350082215707*sigmoidNeural(0.755301853150692*(input[0]-0.5)/0.5+0.6414472257812988*(input[1]-0.5)/0.5+-2.5551530835536136*(input[2]-0.5)/0.5+-1.7838270716959457*(input[3]-0.5)/0.5+0.34320654125081707)+2.130740642648275*sigmoidNeural(-0.7016339529808838*(input[0]-0.5)/0.5+-0.9270449607807927*(input[1]-0.5)/0.5+2.0095361973128*(input[2]-0.5)/0.5+1.4406359999534528*(input[3]-0.5)/0.5+0.199199197669801)+-0.6146004372583043*sigmoidNeural(0.04910299967807794*(input[0]-0.5)/0.5+0.06159361390684677*(input[1]-0.5)/0.5+-1.1497994235297964*(input[2]-0.5)/0.5+-0.8634725076328534*(input[3]-0.5)/0.5+-0.20047329013423643)+-1.8985858550204506*sigmoidNeural(0.5860442320535056*(input[0]-0.5)/0.5+0.7139585083815517*(input[1]-0.5)/0.5+-2.3627357479523066*(input[2]-0.5)/0.5+-1.7560830559735956*(input[3]-0.5)/0.5+0.21623888269539632)+-2.204009937894536*sigmoidNeural(0.7836455676265551*(input[0]-0.5)/0.5+0.948985137786096*(input[1]-0.5)/0.5+-2.506324255713226*(input[2]-0.5)/0.5+-1.8518621008185108*(input[3]-0.5)/0.5+0.2889141963908535)+-1.9535719570587224*sigmoidNeural(0.6719849617054308*(input[0]-0.5)/0.5+0.6375251158220641*(input[1]-0.5)/0.5+-2.4574840319035935*(input[2]-0.5)/0.5+-1.7551302122546153*(input[3]-0.5)/0.5+0.27049006108073037)+-1.8651024914001628*sigmoidNeural(0.6728370337504231*(input[0]-0.5)/0.5+0.5963916555315318*(input[1]-0.5)/0.5+-2.4391439665741084*(input[2]-0.5)/0.5+-1.7224740051558778*(input[3]-0.5)/0.5+0.2559096253159797)+1.052336046358191*sigmoidNeural(-0.36995295064678335*(input[0]-0.5)/0.5+-0.47125867563778473*(input[1]-0.5)/0.5+1.1924635537304673*(input[2]-0.5)/0.5+0.7989622331550941*(input[3]-0.5)/0.5+0.10648417228505032)+-0.4771542561113005*sigmoidNeural(-0.05410682904476424*(input[0]-0.5)/0.5+0.17041127458468036*(input[1]-0.5)/0.5+-1.0471090539847177*(input[2]-0.5)/0.5+-0.7412008039497219*(input[3]-0.5)/0.5+-0.2809103396849757)+-2.1327464533679734*sigmoidNeural(0.816859103323715*(input[0]-0.5)/0.5+0.772505851742145*(input[1]-0.5)/0.5+-2.593007403617786*(input[2]-0.5)/0.5+-1.824902843860694*(input[3]-0.5)/0.5+0.34621995587466)+-2.25552860697803*sigmoidNeural(0.8273715129505309*(input[0]-0.5)/0.5+1.0278382578922198*(input[1]-0.5)/0.5+-2.524314269333443*(input[2]-0.5)/0.5+-1.8370783281605634*(input[3]-0.5)/0.5+0.3000343166341949)+-2.0580105250791823*sigmoidNeural(0.7430984301281386*(input[0]-0.5)/0.5+0.7893786906514905*(input[1]-0.5)/0.5+-2.5432808069080233*(input[2]-0.5)/0.5+-1.7757953641669137*(input[3]-0.5)/0.5+0.3088835722268646)+-0.9097627704686536*sigmoidNeural(0.1319924571839464*(input[0]-0.5)/0.5+0.23987833028192457*(input[1]-0.5)/0.5+-1.5441127887594583*(input[2]-0.5)/0.5+-1.0996304875154017*(input[3]-0.5)/0.5+-0.12117414406985307)+-0.6826575078247978*sigmoidNeural(0.0441843542791344*(input[0]-0.5)/0.5+0.15936233263181368*(input[1]-0.5)/0.5+-1.3359521729709205*(input[2]-0.5)/0.5+-0.907336116295823*(input[3]-0.5)/0.5+-0.2049163194617054)+2.4201941100504807)+1.3616337112642343*sigmoidNeural(-0.40386241143949053*sigmoidNeural(0.2961452019453649*(input[0]-0.5)/0.5+0.3670878800454527*(input[1]-0.5)/0.5+-2.0099634142715943*(input[2]-0.5)/0.5+-1.428721192889837*(input[3]-0.5)/0.5+-0.04353069315608201)+-1.0273805725037104*sigmoidNeural(0.755301853150692*(input[0]-0.5)/0.5+0.6414472257812988*(input[1]-0.5)/0.5+-2.5551530835536136*(input[2]-0.5)/0.5+-1.7838270716959457*(input[3]-0.5)/0.5+0.34320654125081707)+3.3868404033018784*sigmoidNeural(-0.7016339529808838*(input[0]-0.5)/0.5+-0.9270449607807927*(input[1]-0.5)/0.5+2.0095361973128*(input[2]-0.5)/0.5+1.4406359999534528*(input[3]-0.5)/0.5+0.199199197669801)+0.5169535250951409*sigmoidNeural(0.04910299967807794*(input[0]-0.5)/0.5+0.06159361390684677*(input[1]-0.5)/0.5+-1.1497994235297964*(input[2]-0.5)/0.5+-0.8634725076328534*(input[3]-0.5)/0.5+-0.20047329013423643)+-1.0074659802242953*sigmoidNeural(0.5860442320535056*(input[0]-0.5)/0.5+0.7139585083815517*(input[1]-0.5)/0.5+-2.3627357479523066*(input[2]-0.5)/0.5+-1.7560830559735956*(input[3]-0.5)/0.5+0.21623888269539632)+-1.5619508517969494*sigmoidNeural(0.7836455676265551*(input[0]-0.5)/0.5+0.948985137786096*(input[1]-0.5)/0.5+-2.506324255713226*(input[2]-0.5)/0.5+-1.8518621008185108*(input[3]-0.5)/0.5+0.2889141963908535)+-0.9823437412373784*sigmoidNeural(0.6719849617054308*(input[0]-0.5)/0.5+0.6375251158220641*(input[1]-0.5)/0.5+-2.4574840319035935*(input[2]-0.5)/0.5+-1.7551302122546153*(input[3]-0.5)/0.5+0.27049006108073037)+-0.9289455466382006*sigmoidNeural(0.6728370337504231*(input[0]-0.5)/0.5+0.5963916555315318*(input[1]-0.5)/0.5+-2.4391439665741084*(input[2]-0.5)/0.5+-1.7224740051558778*(input[3]-0.5)/0.5+0.2559096253159797)+2.0531347268902898*sigmoidNeural(-0.36995295064678335*(input[0]-0.5)/0.5+-0.47125867563778473*(input[1]-0.5)/0.5+1.1924635537304673*(input[2]-0.5)/0.5+0.7989622331550941*(input[3]-0.5)/0.5+0.10648417228505032)+0.4186346265111839*sigmoidNeural(-0.05410682904476424*(input[0]-0.5)/0.5+0.17041127458468036*(input[1]-0.5)/0.5+-1.0471090539847177*(input[2]-0.5)/0.5+-0.7412008039497219*(input[3]-0.5)/0.5+-0.2809103396849757)+-1.3481954508682155*sigmoidNeural(0.816859103323715*(input[0]-0.5)/0.5+0.772505851742145*(input[1]-0.5)/0.5+-2.593007403617786*(input[2]-0.5)/0.5+-1.824902843860694*(input[3]-0.5)/0.5+0.34621995587466)+-1.6788244494553344*sigmoidNeural(0.8273715129505309*(input[0]-0.5)/0.5+1.0278382578922198*(input[1]-0.5)/0.5+-2.524314269333443*(input[2]-0.5)/0.5+-1.8370783281605634*(input[3]-0.5)/0.5+0.3000343166341949)+-1.2876112720072457*sigmoidNeural(0.7430984301281386*(input[0]-0.5)/0.5+0.7893786906514905*(input[1]-0.5)/0.5+-2.5432808069080233*(input[2]-0.5)/0.5+-1.7757953641669137*(input[3]-0.5)/0.5+0.3088835722268646)+0.19880051114257802*sigmoidNeural(0.1319924571839464*(input[0]-0.5)/0.5+0.23987833028192457*(input[1]-0.5)/0.5+-1.5441127887594583*(input[2]-0.5)/0.5+-1.0996304875154017*(input[3]-0.5)/0.5+-0.12117414406985307)+0.31000786062264635*sigmoidNeural(0.0441843542791344*(input[0]-0.5)/0.5+0.15936233263181368*(input[1]-0.5)/0.5+-1.3359521729709205*(input[2]-0.5)/0.5+-0.907336116295823*(input[3]-0.5)/0.5+-0.2049163194617054)+4.286255029498083)+-5.556748511783075);
result[1]=sigmoidNeural(-4.45889104657035*sigmoidNeural(-1.4655301975773698*sigmoidNeural(0.2961452019453649*(input[0]-0.5)/0.5+0.3670878800454527*(input[1]-0.5)/0.5+-2.0099634142715943*(input[2]-0.5)/0.5+-1.428721192889837*(input[3]-0.5)/0.5+-0.04353069315608201)+-2.1301209134828314*sigmoidNeural(0.755301853150692*(input[0]-0.5)/0.5+0.6414472257812988*(input[1]-0.5)/0.5+-2.5551530835536136*(input[2]-0.5)/0.5+-1.7838270716959457*(input[3]-0.5)/0.5+0.34320654125081707)+2.3906009250484233*sigmoidNeural(-0.7016339529808838*(input[0]-0.5)/0.5+-0.9270449607807927*(input[1]-0.5)/0.5+2.0095361973128*(input[2]-0.5)/0.5+1.4406359999534528*(input[3]-0.5)/0.5+0.199199197669801)+-0.6297222925531978*sigmoidNeural(0.04910299967807794*(input[0]-0.5)/0.5+0.06159361390684677*(input[1]-0.5)/0.5+-1.1497994235297964*(input[2]-0.5)/0.5+-0.8634725076328534*(input[3]-0.5)/0.5+-0.20047329013423643)+-1.9503730583097738*sigmoidNeural(0.5860442320535056*(input[0]-0.5)/0.5+0.7139585083815517*(input[1]-0.5)/0.5+-2.3627357479523066*(input[2]-0.5)/0.5+-1.7560830559735956*(input[3]-0.5)/0.5+0.21623888269539632)+-2.265030030948421*sigmoidNeural(0.7836455676265551*(input[0]-0.5)/0.5+0.948985137786096*(input[1]-0.5)/0.5+-2.506324255713226*(input[2]-0.5)/0.5+-1.8518621008185108*(input[3]-0.5)/0.5+0.2889141963908535)+-2.006475639469029*sigmoidNeural(0.6719849617054308*(input[0]-0.5)/0.5+0.6375251158220641*(input[1]-0.5)/0.5+-2.4574840319035935*(input[2]-0.5)/0.5+-1.7551302122546153*(input[3]-0.5)/0.5+0.27049006108073037)+-1.9819148792419483*sigmoidNeural(0.6728370337504231*(input[0]-0.5)/0.5+0.5963916555315318*(input[1]-0.5)/0.5+-2.4391439665741084*(input[2]-0.5)/0.5+-1.7224740051558778*(input[3]-0.5)/0.5+0.2559096253159797)+1.2097250608712637*sigmoidNeural(-0.36995295064678335*(input[0]-0.5)/0.5+-0.47125867563778473*(input[1]-0.5)/0.5+1.1924635537304673*(input[2]-0.5)/0.5+0.7989622331550941*(input[3]-0.5)/0.5+0.10648417228505032)+-0.5793154877005062*sigmoidNeural(-0.05410682904476424*(input[0]-0.5)/0.5+0.17041127458468036*(input[1]-0.5)/0.5+-1.0471090539847177*(input[2]-0.5)/0.5+-0.7412008039497219*(input[3]-0.5)/0.5+-0.2809103396849757)+-2.279394333045271*sigmoidNeural(0.816859103323715*(input[0]-0.5)/0.5+0.772505851742145*(input[1]-0.5)/0.5+-2.593007403617786*(input[2]-0.5)/0.5+-1.824902843860694*(input[3]-0.5)/0.5+0.34621995587466)+-2.2877783969766043*sigmoidNeural(0.8273715129505309*(input[0]-0.5)/0.5+1.0278382578922198*(input[1]-0.5)/0.5+-2.524314269333443*(input[2]-0.5)/0.5+-1.8370783281605634*(input[3]-0.5)/0.5+0.3000343166341949)+-2.208077029716016*sigmoidNeural(0.7430984301281386*(input[0]-0.5)/0.5+0.7893786906514905*(input[1]-0.5)/0.5+-2.5432808069080233*(input[2]-0.5)/0.5+-1.7757953641669137*(input[3]-0.5)/0.5+0.3088835722268646)+-0.9588008591265094*sigmoidNeural(0.1319924571839464*(input[0]-0.5)/0.5+0.23987833028192457*(input[1]-0.5)/0.5+-1.5441127887594583*(input[2]-0.5)/0.5+-1.0996304875154017*(input[3]-0.5)/0.5+-0.12117414406985307)+-0.783601230855237*sigmoidNeural(0.0441843542791344*(input[0]-0.5)/0.5+0.15936233263181368*(input[1]-0.5)/0.5+-1.3359521729709205*(input[2]-0.5)/0.5+-0.907336116295823*(input[3]-0.5)/0.5+-0.2049163194617054)+2.4164305106123387)+-3.822552346027522*sigmoidNeural(-1.3459199795813357*sigmoidNeural(0.2961452019453649*(input[0]-0.5)/0.5+0.3670878800454527*(input[1]-0.5)/0.5+-2.0099634142715943*(input[2]-0.5)/0.5+-1.428721192889837*(input[3]-0.5)/0.5+-0.04353069315608201)+-2.0028350082215707*sigmoidNeural(0.755301853150692*(input[0]-0.5)/0.5+0.6414472257812988*(input[1]-0.5)/0.5+-2.5551530835536136*(input[2]-0.5)/0.5+-1.7838270716959457*(input[3]-0.5)/0.5+0.34320654125081707)+2.130740642648275*sigmoidNeural(-0.7016339529808838*(input[0]-0.5)/0.5+-0.9270449607807927*(input[1]-0.5)/0.5+2.0095361973128*(input[2]-0.5)/0.5+1.4406359999534528*(input[3]-0.5)/0.5+0.199199197669801)+-0.6146004372583043*sigmoidNeural(0.04910299967807794*(input[0]-0.5)/0.5+0.06159361390684677*(input[1]-0.5)/0.5+-1.1497994235297964*(input[2]-0.5)/0.5+-0.8634725076328534*(input[3]-0.5)/0.5+-0.20047329013423643)+-1.8985858550204506*sigmoidNeural(0.5860442320535056*(input[0]-0.5)/0.5+0.7139585083815517*(input[1]-0.5)/0.5+-2.3627357479523066*(input[2]-0.5)/0.5+-1.7560830559735956*(input[3]-0.5)/0.5+0.21623888269539632)+-2.204009937894536*sigmoidNeural(0.7836455676265551*(input[0]-0.5)/0.5+0.948985137786096*(input[1]-0.5)/0.5+-2.506324255713226*(input[2]-0.5)/0.5+-1.8518621008185108*(input[3]-0.5)/0.5+0.2889141963908535)+-1.9535719570587224*sigmoidNeural(0.6719849617054308*(input[0]-0.5)/0.5+0.6375251158220641*(input[1]-0.5)/0.5+-2.4574840319035935*(input[2]-0.5)/0.5+-1.7551302122546153*(input[3]-0.5)/0.5+0.27049006108073037)+-1.8651024914001628*sigmoidNeural(0.6728370337504231*(input[0]-0.5)/0.5+0.5963916555315318*(input[1]-0.5)/0.5+-2.4391439665741084*(input[2]-0.5)/0.5+-1.7224740051558778*(input[3]-0.5)/0.5+0.2559096253159797)+1.052336046358191*sigmoidNeural(-0.36995295064678335*(input[0]-0.5)/0.5+-0.47125867563778473*(input[1]-0.5)/0.5+1.1924635537304673*(input[2]-0.5)/0.5+0.7989622331550941*(input[3]-0.5)/0.5+0.10648417228505032)+-0.4771542561113005*sigmoidNeural(-0.05410682904476424*(input[0]-0.5)/0.5+0.17041127458468036*(input[1]-0.5)/0.5+-1.0471090539847177*(input[2]-0.5)/0.5+-0.7412008039497219*(input[3]-0.5)/0.5+-0.2809103396849757)+-2.1327464533679734*sigmoidNeural(0.816859103323715*(input[0]-0.5)/0.5+0.772505851742145*(input[1]-0.5)/0.5+-2.593007403617786*(input[2]-0.5)/0.5+-1.824902843860694*(input[3]-0.5)/0.5+0.34621995587466)+-2.25552860697803*sigmoidNeural(0.8273715129505309*(input[0]-0.5)/0.5+1.0278382578922198*(input[1]-0.5)/0.5+-2.524314269333443*(input[2]-0.5)/0.5+-1.8370783281605634*(input[3]-0.5)/0.5+0.3000343166341949)+-2.0580105250791823*sigmoidNeural(0.7430984301281386*(input[0]-0.5)/0.5+0.7893786906514905*(input[1]-0.5)/0.5+-2.5432808069080233*(input[2]-0.5)/0.5+-1.7757953641669137*(input[3]-0.5)/0.5+0.3088835722268646)+-0.9097627704686536*sigmoidNeural(0.1319924571839464*(input[0]-0.5)/0.5+0.23987833028192457*(input[1]-0.5)/0.5+-1.5441127887594583*(input[2]-0.5)/0.5+-1.0996304875154017*(input[3]-0.5)/0.5+-0.12117414406985307)+-0.6826575078247978*sigmoidNeural(0.0441843542791344*(input[0]-0.5)/0.5+0.15936233263181368*(input[1]-0.5)/0.5+-1.3359521729709205*(input[2]-0.5)/0.5+-0.907336116295823*(input[3]-0.5)/0.5+-0.2049163194617054)+2.4201941100504807)+7.207458327664761*sigmoidNeural(-0.40386241143949053*sigmoidNeural(0.2961452019453649*(input[0]-0.5)/0.5+0.3670878800454527*(input[1]-0.5)/0.5+-2.0099634142715943*(input[2]-0.5)/0.5+-1.428721192889837*(input[3]-0.5)/0.5+-0.04353069315608201)+-1.0273805725037104*sigmoidNeural(0.755301853150692*(input[0]-0.5)/0.5+0.6414472257812988*(input[1]-0.5)/0.5+-2.5551530835536136*(input[2]-0.5)/0.5+-1.7838270716959457*(input[3]-0.5)/0.5+0.34320654125081707)+3.3868404033018784*sigmoidNeural(-0.7016339529808838*(input[0]-0.5)/0.5+-0.9270449607807927*(input[1]-0.5)/0.5+2.0095361973128*(input[2]-0.5)/0.5+1.4406359999534528*(input[3]-0.5)/0.5+0.199199197669801)+0.5169535250951409*sigmoidNeural(0.04910299967807794*(input[0]-0.5)/0.5+0.06159361390684677*(input[1]-0.5)/0.5+-1.1497994235297964*(input[2]-0.5)/0.5+-0.8634725076328534*(input[3]-0.5)/0.5+-0.20047329013423643)+-1.0074659802242953*sigmoidNeural(0.5860442320535056*(input[0]-0.5)/0.5+0.7139585083815517*(input[1]-0.5)/0.5+-2.3627357479523066*(input[2]-0.5)/0.5+-1.7560830559735956*(input[3]-0.5)/0.5+0.21623888269539632)+-1.5619508517969494*sigmoidNeural(0.7836455676265551*(input[0]-0.5)/0.5+0.948985137786096*(input[1]-0.5)/0.5+-2.506324255713226*(input[2]-0.5)/0.5+-1.8518621008185108*(input[3]-0.5)/0.5+0.2889141963908535)+-0.9823437412373784*sigmoidNeural(0.6719849617054308*(input[0]-0.5)/0.5+0.6375251158220641*(input[1]-0.5)/0.5+-2.4574840319035935*(input[2]-0.5)/0.5+-1.7551302122546153*(input[3]-0.5)/0.5+0.27049006108073037)+-0.9289455466382006*sigmoidNeural(0.6728370337504231*(input[0]-0.5)/0.5+0.5963916555315318*(input[1]-0.5)/0.5+-2.4391439665741084*(input[2]-0.5)/0.5+-1.7224740051558778*(input[3]-0.5)/0.5+0.2559096253159797)+2.0531347268902898*sigmoidNeural(-0.36995295064678335*(input[0]-0.5)/0.5+-0.47125867563778473*(input[1]-0.5)/0.5+1.1924635537304673*(input[2]-0.5)/0.5+0.7989622331550941*(input[3]-0.5)/0.5+0.10648417228505032)+0.4186346265111839*sigmoidNeural(-0.05410682904476424*(input[0]-0.5)/0.5+0.17041127458468036*(input[1]-0.5)/0.5+-1.0471090539847177*(input[2]-0.5)/0.5+-0.7412008039497219*(input[3]-0.5)/0.5+-0.2809103396849757)+-1.3481954508682155*sigmoidNeural(0.816859103323715*(input[0]-0.5)/0.5+0.772505851742145*(input[1]-0.5)/0.5+-2.593007403617786*(input[2]-0.5)/0.5+-1.824902843860694*(input[3]-0.5)/0.5+0.34621995587466)+-1.6788244494553344*sigmoidNeural(0.8273715129505309*(input[0]-0.5)/0.5+1.0278382578922198*(input[1]-0.5)/0.5+-2.524314269333443*(input[2]-0.5)/0.5+-1.8370783281605634*(input[3]-0.5)/0.5+0.3000343166341949)+-1.2876112720072457*sigmoidNeural(0.7430984301281386*(input[0]-0.5)/0.5+0.7893786906514905*(input[1]-0.5)/0.5+-2.5432808069080233*(input[2]-0.5)/0.5+-1.7757953641669137*(input[3]-0.5)/0.5+0.3088835722268646)+0.19880051114257802*sigmoidNeural(0.1319924571839464*(input[0]-0.5)/0.5+0.23987833028192457*(input[1]-0.5)/0.5+-1.5441127887594583*(input[2]-0.5)/0.5+-1.0996304875154017*(input[3]-0.5)/0.5+-0.12117414406985307)+0.31000786062264635*sigmoidNeural(0.0441843542791344*(input[0]-0.5)/0.5+0.15936233263181368*(input[1]-0.5)/0.5+-1.3359521729709205*(input[2]-0.5)/0.5+-0.907336116295823*(input[3]-0.5)/0.5+-0.2049163194617054)+4.286255029498083)+-3.3259987022987496);
result[2]=sigmoidNeural(-2.982043070772167*sigmoidNeural(-1.4655301975773698*sigmoidNeural(0.2961452019453649*(input[0]-0.5)/0.5+0.3670878800454527*(input[1]-0.5)/0.5+-2.0099634142715943*(input[2]-0.5)/0.5+-1.428721192889837*(input[3]-0.5)/0.5+-0.04353069315608201)+-2.1301209134828314*sigmoidNeural(0.755301853150692*(input[0]-0.5)/0.5+0.6414472257812988*(input[1]-0.5)/0.5+-2.5551530835536136*(input[2]-0.5)/0.5+-1.7838270716959457*(input[3]-0.5)/0.5+0.34320654125081707)+2.3906009250484233*sigmoidNeural(-0.7016339529808838*(input[0]-0.5)/0.5+-0.9270449607807927*(input[1]-0.5)/0.5+2.0095361973128*(input[2]-0.5)/0.5+1.4406359999534528*(input[3]-0.5)/0.5+0.199199197669801)+-0.6297222925531978*sigmoidNeural(0.04910299967807794*(input[0]-0.5)/0.5+0.06159361390684677*(input[1]-0.5)/0.5+-1.1497994235297964*(input[2]-0.5)/0.5+-0.8634725076328534*(input[3]-0.5)/0.5+-0.20047329013423643)+-1.9503730583097738*sigmoidNeural(0.5860442320535056*(input[0]-0.5)/0.5+0.7139585083815517*(input[1]-0.5)/0.5+-2.3627357479523066*(input[2]-0.5)/0.5+-1.7560830559735956*(input[3]-0.5)/0.5+0.21623888269539632)+-2.265030030948421*sigmoidNeural(0.7836455676265551*(input[0]-0.5)/0.5+0.948985137786096*(input[1]-0.5)/0.5+-2.506324255713226*(input[2]-0.5)/0.5+-1.8518621008185108*(input[3]-0.5)/0.5+0.2889141963908535)+-2.006475639469029*sigmoidNeural(0.6719849617054308*(input[0]-0.5)/0.5+0.6375251158220641*(input[1]-0.5)/0.5+-2.4574840319035935*(input[2]-0.5)/0.5+-1.7551302122546153*(input[3]-0.5)/0.5+0.27049006108073037)+-1.9819148792419483*sigmoidNeural(0.6728370337504231*(input[0]-0.5)/0.5+0.5963916555315318*(input[1]-0.5)/0.5+-2.4391439665741084*(input[2]-0.5)/0.5+-1.7224740051558778*(input[3]-0.5)/0.5+0.2559096253159797)+1.2097250608712637*sigmoidNeural(-0.36995295064678335*(input[0]-0.5)/0.5+-0.47125867563778473*(input[1]-0.5)/0.5+1.1924635537304673*(input[2]-0.5)/0.5+0.7989622331550941*(input[3]-0.5)/0.5+0.10648417228505032)+-0.5793154877005062*sigmoidNeural(-0.05410682904476424*(input[0]-0.5)/0.5+0.17041127458468036*(input[1]-0.5)/0.5+-1.0471090539847177*(input[2]-0.5)/0.5+-0.7412008039497219*(input[3]-0.5)/0.5+-0.2809103396849757)+-2.279394333045271*sigmoidNeural(0.816859103323715*(input[0]-0.5)/0.5+0.772505851742145*(input[1]-0.5)/0.5+-2.593007403617786*(input[2]-0.5)/0.5+-1.824902843860694*(input[3]-0.5)/0.5+0.34621995587466)+-2.2877783969766043*sigmoidNeural(0.8273715129505309*(input[0]-0.5)/0.5+1.0278382578922198*(input[1]-0.5)/0.5+-2.524314269333443*(input[2]-0.5)/0.5+-1.8370783281605634*(input[3]-0.5)/0.5+0.3000343166341949)+-2.208077029716016*sigmoidNeural(0.7430984301281386*(input[0]-0.5)/0.5+0.7893786906514905*(input[1]-0.5)/0.5+-2.5432808069080233*(input[2]-0.5)/0.5+-1.7757953641669137*(input[3]-0.5)/0.5+0.3088835722268646)+-0.9588008591265094*sigmoidNeural(0.1319924571839464*(input[0]-0.5)/0.5+0.23987833028192457*(input[1]-0.5)/0.5+-1.5441127887594583*(input[2]-0.5)/0.5+-1.0996304875154017*(input[3]-0.5)/0.5+-0.12117414406985307)+-0.783601230855237*sigmoidNeural(0.0441843542791344*(input[0]-0.5)/0.5+0.15936233263181368*(input[1]-0.5)/0.5+-1.3359521729709205*(input[2]-0.5)/0.5+-0.907336116295823*(input[3]-0.5)/0.5+-0.2049163194617054)+2.4164305106123387)+-3.4154769129412603*sigmoidNeural(-1.3459199795813357*sigmoidNeural(0.2961452019453649*(input[0]-0.5)/0.5+0.3670878800454527*(input[1]-0.5)/0.5+-2.0099634142715943*(input[2]-0.5)/0.5+-1.428721192889837*(input[3]-0.5)/0.5+-0.04353069315608201)+-2.0028350082215707*sigmoidNeural(0.755301853150692*(input[0]-0.5)/0.5+0.6414472257812988*(input[1]-0.5)/0.5+-2.5551530835536136*(input[2]-0.5)/0.5+-1.7838270716959457*(input[3]-0.5)/0.5+0.34320654125081707)+2.130740642648275*sigmoidNeural(-0.7016339529808838*(input[0]-0.5)/0.5+-0.9270449607807927*(input[1]-0.5)/0.5+2.0095361973128*(input[2]-0.5)/0.5+1.4406359999534528*(input[3]-0.5)/0.5+0.199199197669801)+-0.6146004372583043*sigmoidNeural(0.04910299967807794*(input[0]-0.5)/0.5+0.06159361390684677*(input[1]-0.5)/0.5+-1.1497994235297964*(input[2]-0.5)/0.5+-0.8634725076328534*(input[3]-0.5)/0.5+-0.20047329013423643)+-1.8985858550204506*sigmoidNeural(0.5860442320535056*(input[0]-0.5)/0.5+0.7139585083815517*(input[1]-0.5)/0.5+-2.3627357479523066*(input[2]-0.5)/0.5+-1.7560830559735956*(input[3]-0.5)/0.5+0.21623888269539632)+-2.204009937894536*sigmoidNeural(0.7836455676265551*(input[0]-0.5)/0.5+0.948985137786096*(input[1]-0.5)/0.5+-2.506324255713226*(input[2]-0.5)/0.5+-1.8518621008185108*(input[3]-0.5)/0.5+0.2889141963908535)+-1.9535719570587224*sigmoidNeural(0.6719849617054308*(input[0]-0.5)/0.5+0.6375251158220641*(input[1]-0.5)/0.5+-2.4574840319035935*(input[2]-0.5)/0.5+-1.7551302122546153*(input[3]-0.5)/0.5+0.27049006108073037)+-1.8651024914001628*sigmoidNeural(0.6728370337504231*(input[0]-0.5)/0.5+0.5963916555315318*(input[1]-0.5)/0.5+-2.4391439665741084*(input[2]-0.5)/0.5+-1.7224740051558778*(input[3]-0.5)/0.5+0.2559096253159797)+1.052336046358191*sigmoidNeural(-0.36995295064678335*(input[0]-0.5)/0.5+-0.47125867563778473*(input[1]-0.5)/0.5+1.1924635537304673*(input[2]-0.5)/0.5+0.7989622331550941*(input[3]-0.5)/0.5+0.10648417228505032)+-0.4771542561113005*sigmoidNeural(-0.05410682904476424*(input[0]-0.5)/0.5+0.17041127458468036*(input[1]-0.5)/0.5+-1.0471090539847177*(input[2]-0.5)/0.5+-0.7412008039497219*(input[3]-0.5)/0.5+-0.2809103396849757)+-2.1327464533679734*sigmoidNeural(0.816859103323715*(input[0]-0.5)/0.5+0.772505851742145*(input[1]-0.5)/0.5+-2.593007403617786*(input[2]-0.5)/0.5+-1.824902843860694*(input[3]-0.5)/0.5+0.34621995587466)+-2.25552860697803*sigmoidNeural(0.8273715129505309*(input[0]-0.5)/0.5+1.0278382578922198*(input[1]-0.5)/0.5+-2.524314269333443*(input[2]-0.5)/0.5+-1.8370783281605634*(input[3]-0.5)/0.5+0.3000343166341949)+-2.0580105250791823*sigmoidNeural(0.7430984301281386*(input[0]-0.5)/0.5+0.7893786906514905*(input[1]-0.5)/0.5+-2.5432808069080233*(input[2]-0.5)/0.5+-1.7757953641669137*(input[3]-0.5)/0.5+0.3088835722268646)+-0.9097627704686536*sigmoidNeural(0.1319924571839464*(input[0]-0.5)/0.5+0.23987833028192457*(input[1]-0.5)/0.5+-1.5441127887594583*(input[2]-0.5)/0.5+-1.0996304875154017*(input[3]-0.5)/0.5+-0.12117414406985307)+-0.6826575078247978*sigmoidNeural(0.0441843542791344*(input[0]-0.5)/0.5+0.15936233263181368*(input[1]-0.5)/0.5+-1.3359521729709205*(input[2]-0.5)/0.5+-0.907336116295823*(input[3]-0.5)/0.5+-0.2049163194617054)+2.4201941100504807)+-6.885584744209511*sigmoidNeural(-0.40386241143949053*sigmoidNeural(0.2961452019453649*(input[0]-0.5)/0.5+0.3670878800454527*(input[1]-0.5)/0.5+-2.0099634142715943*(input[2]-0.5)/0.5+-1.428721192889837*(input[3]-0.5)/0.5+-0.04353069315608201)+-1.0273805725037104*sigmoidNeural(0.755301853150692*(input[0]-0.5)/0.5+0.6414472257812988*(input[1]-0.5)/0.5+-2.5551530835536136*(input[2]-0.5)/0.5+-1.7838270716959457*(input[3]-0.5)/0.5+0.34320654125081707)+3.3868404033018784*sigmoidNeural(-0.7016339529808838*(input[0]-0.5)/0.5+-0.9270449607807927*(input[1]-0.5)/0.5+2.0095361973128*(input[2]-0.5)/0.5+1.4406359999534528*(input[3]-0.5)/0.5+0.199199197669801)+0.5169535250951409*sigmoidNeural(0.04910299967807794*(input[0]-0.5)/0.5+0.06159361390684677*(input[1]-0.5)/0.5+-1.1497994235297964*(input[2]-0.5)/0.5+-0.8634725076328534*(input[3]-0.5)/0.5+-0.20047329013423643)+-1.0074659802242953*sigmoidNeural(0.5860442320535056*(input[0]-0.5)/0.5+0.7139585083815517*(input[1]-0.5)/0.5+-2.3627357479523066*(input[2]-0.5)/0.5+-1.7560830559735956*(input[3]-0.5)/0.5+0.21623888269539632)+-1.5619508517969494*sigmoidNeural(0.7836455676265551*(input[0]-0.5)/0.5+0.948985137786096*(input[1]-0.5)/0.5+-2.506324255713226*(input[2]-0.5)/0.5+-1.8518621008185108*(input[3]-0.5)/0.5+0.2889141963908535)+-0.9823437412373784*sigmoidNeural(0.6719849617054308*(input[0]-0.5)/0.5+0.6375251158220641*(input[1]-0.5)/0.5+-2.4574840319035935*(input[2]-0.5)/0.5+-1.7551302122546153*(input[3]-0.5)/0.5+0.27049006108073037)+-0.9289455466382006*sigmoidNeural(0.6728370337504231*(input[0]-0.5)/0.5+0.5963916555315318*(input[1]-0.5)/0.5+-2.4391439665741084*(input[2]-0.5)/0.5+-1.7224740051558778*(input[3]-0.5)/0.5+0.2559096253159797)+2.0531347268902898*sigmoidNeural(-0.36995295064678335*(input[0]-0.5)/0.5+-0.47125867563778473*(input[1]-0.5)/0.5+1.1924635537304673*(input[2]-0.5)/0.5+0.7989622331550941*(input[3]-0.5)/0.5+0.10648417228505032)+0.4186346265111839*sigmoidNeural(-0.05410682904476424*(input[0]-0.5)/0.5+0.17041127458468036*(input[1]-0.5)/0.5+-1.0471090539847177*(input[2]-0.5)/0.5+-0.7412008039497219*(input[3]-0.5)/0.5+-0.2809103396849757)+-1.3481954508682155*sigmoidNeural(0.816859103323715*(input[0]-0.5)/0.5+0.772505851742145*(input[1]-0.5)/0.5+-2.593007403617786*(input[2]-0.5)/0.5+-1.824902843860694*(input[3]-0.5)/0.5+0.34621995587466)+-1.6788244494553344*sigmoidNeural(0.8273715129505309*(input[0]-0.5)/0.5+1.0278382578922198*(input[1]-0.5)/0.5+-2.524314269333443*(input[2]-0.5)/0.5+-1.8370783281605634*(input[3]-0.5)/0.5+0.3000343166341949)+-1.2876112720072457*sigmoidNeural(0.7430984301281386*(input[0]-0.5)/0.5+0.7893786906514905*(input[1]-0.5)/0.5+-2.5432808069080233*(input[2]-0.5)/0.5+-1.7757953641669137*(input[3]-0.5)/0.5+0.3088835722268646)+0.19880051114257802*sigmoidNeural(0.1319924571839464*(input[0]-0.5)/0.5+0.23987833028192457*(input[1]-0.5)/0.5+-1.5441127887594583*(input[2]-0.5)/0.5+-1.0996304875154017*(input[3]-0.5)/0.5+-0.12117414406985307)+0.31000786062264635*sigmoidNeural(0.0441843542791344*(input[0]-0.5)/0.5+0.15936233263181368*(input[1]-0.5)/0.5+-1.3359521729709205*(input[2]-0.5)/0.5+-0.907336116295823*(input[3]-0.5)/0.5+-0.2049163194617054)+4.286255029498083)+3.3658083824358735);
normalize(result,3);
return result;
}
#include "InputNormalization.h"

double* NormalizationPreprocessing_6320819769066(double input[4]) {
static const double imins[4] = {4.3,2.0,1.0,0.1};
static const double iMaxMinusMin[4] = {3.6000000000000005,2.4000000000000004,5.9,2.4};
return inputNormalization<4>(input,imins,iMaxMinusMin);
}
#include "./classification/ConnectableClassifier.h"

double* ConnectableClassifier_6320814440518(double input[4]) {
static double* (*model)(const double*) = {&RapidNeuralNetClassifier_6320814500092};
static double* (*inputProcessingMethods[1])(double*) = {&NormalizationPreprocessing_6320819769066};
static double* (*outputProcessingMethods[0])(double*);
static const bool enabledInputs[3] = {true,true,true};
return connectableClassifierOutput<3,4,1,0>(input,model,enabledInputs,inputProcessingMethods,outputProcessingMethods);
}
int main(int argc, const char** argv) {
    double testInput[2];
    for (double i = 0; i < 10; i = i + 1) {
        for (double j = 0; j < 10; j = j + 1) {
            testInput[0] = i;
            testInput[1] = j;
            double * result = ConnectableClassifier_6320814440518(testInput);
            for (int k = 0; k < 2; k++) {
                cout << setprecision(16) << result[k] << " ";
            }
            cout << endl;
        }
    }
}