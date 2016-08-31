 /*
 * Free software, use at your own risk.
 * BasicAirData 2016
 * JLJ
 */
import java.io.*;
import static java.lang.System.out;
import java.lang.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Extend HttpServlet class
public class AirProperties extends HttpServlet {

    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type
        String title = "Calculation Results";
        String docType
                = "<!doctype html public \"-//w3c//dtd html 4.0 "
                + "transitional//en\">\n";
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {  //Magari un catch ?
            // PrintWriter out = response.getWriter();

            try {
                double rho; //Air Density 
                double mu; //Speed of sound 
                double sos; //Speed of sound 
                double R1, xco2, A, B, C, D, alfa, bet, gama, a0, a1, a2, b0, b1, c0, c1, d, e, Ma, Mv, psv, f, xv, Z, t, Rair;

                double T = Double.parseDouble(HtmlFilter.filter(request.getParameter("T")));
                double p = Double.parseDouble(HtmlFilter.filter(request.getParameter("p")));
                double h = Double.parseDouble(HtmlFilter.filter(request.getParameter("h")));
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
                Rair = R1 / Ma * 1000;
                psv = 1 * Math.exp(A * Math.pow(T, 2) + B * T + C + D / T); //Sat. pressure 
                t = T - 273.15;
                f = alfa + bet * p + gama * Math.pow(t, 2);
                xv = h * f * psv / p;
                Z = 1 - p / T * (a0 + a1 * t + a2 * Math.pow(t, 2) + (b0 + b1 * t) * xv + (c0 + c1 * t) * Math.pow(xv, 2)) + Math.pow(p, 2) / Math.pow(T, 2) * (d + e * Math.pow(xv, 2)); //Z Air 
                rho = p * Ma / (Z * R1 * T) * (1 - xv * (1 - Mv / Ma)) * 0.001; //Air Density 
                //Calculate viscosity. Sutherland's formula 
                mu = 18.27 * (291.15 + 120) / (T + 120) * Math.pow((T / 291.15), (3 / 2)); //10^-6 Pas 
                //Calculate Speed of Sound 
                sos = Math.sqrt(1.4 * Rair * T); //Cp fixed to 1.4; 

//   if (){
                //   }
                out.println("\n" + docType
                        + "<html>\n"
                        + "<head><title>" + title + "</title>\n"
                        + "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.basicairdata.eu/wp-content/themes/vantage-zen-child/style.css\">\n"
                        + "</head>\n"
                        + "<body style=\"background-color:#fcfcfc\">\n"
                        + "<b><p align=\"center\">" + title + "</p></b>\n"
                        + "<p><b>Input Values</b></p>"
                        + "<ul>\n"
                        + "<li><b>Temperature [°K]</b>: "
                        + String.format("%1$,.1f", T) + "\n"
                        + "<li><b>Pressure [Pa]</b>: "
                        + String.format("%1$,.1f", p) + "\n"
                        + "<li><b>Relative Humidity [0-1]</b>: "
                        + String.format("%1$,.1f", h) + "\n"
                        + "</ul>\n"
                        + "<p><b>Calculated Values</b></p>"
                        + "<ul>\n"
                        + "  <li><b>Density [kg/m^3]</b>: "
                        + String.format("%1$,.4f", rho)
                        + "  <li><b>Viscosity [10e-6Pas]</b>: "
                        + String.format("%1$,.1f", mu) + "\n"
                        + "  <li><b>Vapor Pressure [Pa]</b>: "
                        + String.format("%1$,.1f", psv) + "\n"
                        + "  <li><b>Compressibility Factor</b>: "
                        + String.format("%1$,.6f", Z) + "\n"
                        + "  <li><b>Speed of Sound [m/s]</b>: "
                        + String.format("%1$,.1f", sos) + "\n"
                        + "</ul>\n"
                        + "</body></html>");
            } catch (NumberFormatException e) {
                out.println("\n" + docType
                        + "<html>\n"
                        + "<head>\n"
                        + "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.basicairdata.eu/wp-content/themes/vantage/style.css\">\n"
                        + "</head>\n"
                        + "<body style=\"background-color:#fcfcfc\">\n"
                        + "<b><p align=\"center\">" + title + "</p></b>\n"
                        + "<p>Check your input values, only numeric values are allowed, "
                        + "please remove the following offending data</p>"
                        + e
                        + "</body></html>");
            }

        } finally {
            out.close();
        }
    }
}
