//Indicated Airspeed Table Generator
//JLJ@basicairdata.eu
//Air Standard Density 1.225 kg/m^3, T = 15°C, p = 101 325 Pa. 
qc=1:1531 ;//Pitot-Static tube dynamic pressure
rho=1.225;
p=101325;
T=15+273.15;
[r c]=size(qc);
fd = mopen('C:\Users\larraguetajunior\OneDrive\sito\web\2017\Calibration Table for Pitot\test_table.txt','wt');
for i=1:c
    IAS(1,i)=((2*qc(1,i))/rho)^0.5;
//    outstr=string(qc(1,i)) + ' & ' + string(IAS(1,i)) + ' & ' + string(IAS(1,i)*3.6)+ ' & ' +string(IAS(1,i)/0.3048) + ' & ' + string(IAS(1,i)*3.6/1.852)+   " \\ \hline";
    outstr=msprintf('%.0f',qc(1,i)) + ' & ' + msprintf('%.2f',qc(1,i)*0.102155) + ' & ' +msprintf('%.5f',qc(1,i)*0.000145) + ' & ' +msprintf('%.5f',qc(1,i)*0.004022)+ ' & ' + msprintf('%.2f',IAS(1,i)) + ' & ' + msprintf('%.2f',IAS(1,i)*3.6)+ ' & ' +msprintf('%.2f',IAS(1,i)/0.3048) + ' & ' + msprintf('%.2f',IAS(1,i)*3.6/1.852)+   " \\ \hline";
    mputl(outstr,fd);
end
//outstr=string(qc(1,1)) + ' & ' + string(2) + ' & ' + string(3)+ ' & ' +string(4) + ' & ' + string(5)+   " \\ \hline";

//4 & 5 & 6 & 7 & 1.852 km \\ \hline
mclose(fd);
