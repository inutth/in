package org.utt.app.util;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;

public class PrintPedSticker implements Printable{
	PageFormat pf;
	Paper paper;
	Book bk;
	DecimalFormat FormatNumber;
	String line1="",line2="",line3="",line4="",line5="",line6="",line7="";
	
	public PrintPedSticker(){
		line1="ALL maintenance wk_____";
		line2="Vincristine  __________ mg IV";
		line3="Prednisolone(5mg)___,___,___tabPox5d";
		line4="Methotrexate(2.5 mg) ____tabPOqอังคาร";
		line5="6-MP    1/2 , 3/4 , 1 tab PO qhs";
		line6="Bactrim              bid เสาร์ อาทิตย์";
		line7="นัด  ___________ สัปดาห์";
		
		pf = new PageFormat();
		paper = new Paper();
		// 20.5x17.5mm
	    double width = Setup.cmsToPixel(50.0, 72);
	    double height = Setup.cmsToPixel(38.0, 72);
	 // 1 mm border...
	    
	    paper.setImageableArea(
	                    Setup.cmsToPixel(0.01, 72),
	                    Setup.cmsToPixel(0.031, 72),
	                   
	                    width - Setup.cmsToPixel(0.01, 72),
	                    height - Setup.cmsToPixel(0.03, 72));
	    pf.setOrientation(PageFormat.PORTRAIT);
		pf.setPaper(paper);	
	}
	public void showPrinter(){
		PrinterJob pj =  PrinterJob.getPrinterJob();
		
		bk = new Book();
		bk.append(this,pf,1);
		pj.setPageable(bk);
		if(pj.printDialog()){ 
			try{
				pj.print();  
			}
			catch(Exception e){
				System.out.println(e);
			}
		}
	 
	}
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		 if (page > 0) {
	         return NO_SUCH_PAGE;
	    }
	    Graphics2D g2d = (Graphics2D)g;
	    g2d.translate(pf.getImageableX(), pf.getImageableY());
	    g2d.setFont(new Font("Cordia New",0,12));
	    int x_start=20;
	    int y_start=25;
	    int y_start1=140;
	    int y_start2=252;
	    int y_start3=367;
	    int xx=150;
	    for(int i=0;i<5;i++){
	    	g2d.drawString(line1,x_start+(i*xx)+20,y_start);
		    g2d.drawString(line2,x_start+(i*xx),y_start+12);
		    g2d.setFont(new Font("Cordia New",0,10));
		    g2d.drawString(line3,x_start+(i*xx),y_start+24);
		    g2d.drawString(line4,x_start+(i*xx),y_start+36);
		    g2d.setFont(new Font("Cordia New",0,12));
		    g2d.drawString(line5,x_start+(i*xx),y_start+48);
		    g2d.drawString(line6,x_start+(i*xx),y_start+60);
		    g2d.drawString(line7,x_start+(i*xx+20),y_start+72);
	    }
	    for(int i=0;i<5;i++){
	    	g2d.drawString(line1,x_start+(i*xx)+20,y_start1);
		    g2d.drawString(line2,x_start+(i*xx),y_start1+12);
		    g2d.setFont(new Font("Cordia New",0,10));
		    g2d.drawString(line3,x_start+(i*xx),y_start1+24);
		    g2d.drawString(line4,x_start+(i*xx),y_start1+36);
		    g2d.setFont(new Font("Cordia New",0,12));
		    g2d.drawString(line5,x_start+(i*xx),y_start1+48);
		    g2d.drawString(line6,x_start+(i*xx),y_start1+60);
		    g2d.drawString(line7,x_start+(i*xx+20),y_start1+72);
	    }
	    for(int i=0;i<5;i++){
	    	g2d.drawString(line1,x_start+(i*xx)+20,y_start2);
		    g2d.drawString(line2,x_start+(i*xx),y_start2+12);
		    g2d.setFont(new Font("Cordia New",0,10));
		    g2d.drawString(line3,x_start+(i*xx),y_start2+24);
		    g2d.drawString(line4,x_start+(i*xx),y_start2+36);
		    g2d.setFont(new Font("Cordia New",0,12));
		    g2d.drawString(line5,x_start+(i*xx),y_start2+48);
		    g2d.drawString(line6,x_start+(i*xx),y_start2+60);
		    g2d.drawString(line7,x_start+(i*xx+20),y_start2+72);
	    }
	    for(int i=0;i<5;i++){
	    	g2d.drawString(line1,x_start+(i*xx)+20,y_start3);
		    g2d.drawString(line2,x_start+(i*xx),y_start3+12);
		    g2d.setFont(new Font("Cordia New",0,10));
		    g2d.drawString(line3,x_start+(i*xx),y_start3+24);
		    g2d.drawString(line4,x_start+(i*xx),y_start3+36);
		    g2d.setFont(new Font("Cordia New",0,12));
		    g2d.drawString(line5,x_start+(i*xx),y_start3+48);
		    g2d.drawString(line6,x_start+(i*xx),y_start3+60);
		    g2d.drawString(line7,x_start+(i*xx+20),y_start3+72);
	    }
	    
	    return PAGE_EXISTS;
	}

}
