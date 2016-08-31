/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author larra
 */
// Import required java libraries
import java.io.*;
import static java.lang.System.out;
import java.lang.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Extend HttpServlet class
public class RangeFinder extends HttpServlet {

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
            
            try{
                double k1,k2;
            double rhobase = 1.225; //kg/m^3
            double Vmax = Double.parseDouble(HtmlFilter.filter(request.getParameter("MaximumAirspeed")));
            double c = Double.parseDouble(HtmlFilter.filter(request.getParameter("cFactor")));
            double uCAS = Double.parseDouble(HtmlFilter.filter(request.getParameter("uCAS")));
            double uc = Double.parseDouble(HtmlFilter.filter(request.getParameter("uc")));
            double q = java.lang.Math.pow((Vmax / c), 2) * rhobase / 2;
            //Gaussian Distribution. Confidence level (3 sigma) 99.73%
            uCAS=uCAS/3;
            uc=uc/3;
            k1=c/(Math.sqrt(2*rhobase*q));
            k2=Math.sqrt(2*q/rhobase);
            double uq=(uCAS*uCAS-k2*k2*uc*uc)/(k1*k1); // Indeed It is uq^2
            if (uq>0){
                uq=Math.sqrt(uq);
            }else{
                uq=0.0;
            }      
            out.println("\n" + docType
                    + "<html>\n"
                    + "<head><title>" + title + "</title>\n"
                    + "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.basicairdata.eu/wp-content/themes/vantage-zen-child/style.css\">\n"
                    +"</head>\n"
                    + "<body style=\"background-color:#fcfcfc\">\n"
                    + "<b><p align=\"center\">" + title + "</p></b>\n"
                    + "<p><b>Input Values</b></p>"
                    + "<ul>\n"
                    + "<li><b>Desired Airspeed [m/s]</b>: "
                    + String.format("%1$,.4f", Vmax) + "\n"
                    + "<li><b>c factor value</b>: "
                    + String.format("%1$,.3f", c) + "\n"
                    + "<li><b>Desired confidence interval for CAS [m/s]</b>: "
                    + String.format("%1$,.1f", uCAS*3) + "\n"
                    + "<li><b>Confidence interval of c</b>: "
                    + String.format("%1$,.3f", uc*3) + "\n"
                    + "</ul>\n"
                    + "<p><b>Calculated Values</b></p>"
                    + "<ul>\n"
                    + "  <li><b>Differential pressure at specified airspeed [Pa]</b>: "
                    + String.format("%1$,.1f", q)
                    + "  <li><b>Differential pressure measurement required confidence interval [Pa]</b>: "
                    + String.format("%1$,.1f", 3*uq) + "\n"
                    + "  <li><b>Differential pressure measurement required confidence interval [%] </b>: "
                    + String.format("%1$,.2f", 3*uq/q*100) + "\n"
                    + "</ul>\n"
                    + "</body></html>");
            }
            catch (NumberFormatException e) {
     out.println("\n" + docType
                    + "<html>\n"
                    + "<head>\n"
                    + "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.basicairdata.eu/wp-content/themes/vantage/style.css\">\n"
                    +"</head>\n"
                    + "<body style=\"background-color:#fcfcfc\">\n"
                    + "<b><p align=\"center\">" + title + "</p></b>\n"
                    + "<p>Check your input values, only numeric values are allowed, "
                    + "please remove the following offending data</p>"       
                    + e
                    + "</body></html>");
                    }
            
        }
         finally { 
            out.close();       
    }
    }
}
