//Calculates 8 mm Pitot Calibration Table and prepare LaTeX Output
//JLJ@BasicAirData
//Range of validity 
//CIMP 81 600 hPa < p < 1100 hPa
//15 ◦C < t < 27 ◦C.
//https://www.nist.gov/sites/default/files/documents/calibrations/CIPM-2007.pdf
//Density of Air
//https://en.wikipedia.org/wiki/Density_of_air
funcprot(0)
function [rhoair]=rhoairID(T, p,RH)
    //Function to calculate the density of humid air with gas law
    //SI Constants
    Rd=287.058;//Specific gas constant for dry air, 287.058 J/(kg·K)
    Rv=461.495;//Specific gas constant for water vapor, 461.495 J/(kg·K)
    Md=0.028964;// Molar mass of dry air kg/mol
    Mv=0.018016;//Molar mass of water vapor kg/mol
    R=8.314;// Universal GAS constant J/(K·mol)
    //Input 
    //T=288.15;//Deg Kelvin Atm
    //p=101325; //Atm Pressure (Pa)
    //RH=0.5; //Relative humidity
    //Processing
    TC=T-273.15;//Deg C
    psat=6.1078*10^(7.5*TC/(TC+237.3))*100;//Vapor saturation pressure (Pa)
    pv=RH*psat;//Water vapor pressure
    pd=p-pv;//Partial pressure of dry air (Pa)
    rhoair=1/(R*T)*(pd*Md+pv*Mv);
endfunction
function [rhoair]=rhoairCIMP81(T,p,h)
    R1 = 8.314510;//J/(mol°K)
    xco2 = 0.0004;//Co2 fraction
    A = 1.2378847e-5;
    B = -1.9121316e-2;
    C = 33.93711047;
    D = -6.3431645e3;
    alfa = 1.00062;
    bet = 3.14e-8;
    gama = 5.6e-7;
    a0 = 1.58123e-6;
    a1 = -2.9331e-8;
    a2 = 1.1043e-10;
    b0 = 5.707e-6;
    b1 = -2.051e-8;
    c0 = 1.9898e-4;
    c1 = -2.376e-6;
    d = 1.83e-11;
    e = -0.765e-8;
    Ma = 28.9635 + 12.011 * (xco2 - 0.0004);
    Mv = 18.01528;
    Rair=R1/Ma*1000;
    psv = 1 * exp(A * T^2 + B * T + C + D / T); //Sat. pressure
    t = T - 273.15;
    f = alfa + bet * p + gama*t^2;
    xv = h * f * psv / p;
    Z = 1 - p / T * (a0 + a1 * t + a2 * t^2 + (b0 + b1 * t) * xv + (c0 + c1 * t) * xv^2) + p^2 / T^2 * (d + e * xv^2); //Z Air
    rhoair = p * Ma / (Z * R1 * T) * (1 - xv * (1 - Mv / Ma)) * 0.001; //Air Density
endfunction
function [rho]=rhoair(T,p,RH)
   //Wrapper function to get air density
   if ( (T>(15+273.10)) & (T<(27+273.15)) & (p>60000) & (p<110000) ) then
    rho=rhoairCIMP81(T,p,RH);
    else
    rho=rhoairID(T,p,RH);
    end 
endfunction
function [qvec, TASP]=TAS(rhoair)
qvec=15:5:1500; //Dynamic pressure values vector
[r c]=size(qvec)
for i=1:c
    TASP(1,i)=(2*qvec(1,i)/rhoair)^0.5;
 //   disp(IAS(1,i));
end
    
endfunction

//IAS Curve
[qvec, TASP]=TAS(1.225);
plot(qvec(1,:),TASP(1,:));
//Plot a set of curves at different relative humidity
T=288.15;
p=101325;
//RH=0:0.5:1;
RH=0;
[r c]=size(RH)
for i=1:c
    rhon=rhoair(T,p,RH(1,i));
    //Temperature dependent lines
    //+20 deg per time, bend left
    for j=1:8
    for k=0:3 
    Tm=T+k*20;
    q1=200*j;
    rho1=rhon;
    rho2=rhoair(Tm,p,RH(1,i));
    q2=q1*(rho2/rho1);
    plot([q1 q2],[0 (2*q2/rho1)^0.5],'red');
    //Now pressure accounting
    //If no pressure change
    pvec=[60000 80000 101325];
    [r c]=size(pvec);
    for l=1:c
    rho2=rhoair(Tm,pvec(1,l),RH(1,i));
    q2=q1*(rho2/rho1);
    if l==1 then
    plot([q2  0],[(2*q2/rho1)^0.5 (2*q1/rho1)^0.5 ],'blue');   
    end
    if l==2 then
    plot([q2  0],[(2*q2/rho1)^0.5 (2*q1/rho1)^0.5 ],'black');
    end
    if l==3 then
    plot([q2  0],[(2*q2/rho1)^0.5 (2*q1/rho1)^0.5 ],'green');
    end
    end

    end
    end
    [qvec, TASP]=TAS(rhon);
    plot(qvec(1,:),TASP(1,:));
end
set(gca(),"grid",[1 1]); 



