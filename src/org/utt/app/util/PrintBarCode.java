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
import java.util.GregorianCalendar;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.output.OutputException;

public class PrintBarCode implements Printable{
	PageFormat pf;
	Paper paper;
	Book bk;
	DecimalFormat FormatNumber;
	Barcode barcode;
	String [][] bc;
	GregorianCalendar day ;
	
	public PrintBarCode(){
		day = new GregorianCalendar();
		pf = new PageFormat();
		paper = new Paper();
		bc = new String[3][10];
		// 20.5x17.5mm
	    double width = Setup.cmsToPixel(20, 72);
	    double height = Setup.cmsToPixel(22.2, 72);
	 // 1 mm border...
	    paper.setImageableArea(
                Setup.cmsToPixel(0.01, 72),
                Setup.cmsToPixel(0.031, 72),
                width - Setup.cmsToPixel(0.01, 72),
                height - Setup.cmsToPixel(0.03, 72));
	    // Orientation
	    pf.setOrientation(PageFormat.PORTRAIT);
	    pf.setPaper(paper);
	}
	public void setDataBarcode(int partid){
		int k = partid;
		for(int i=0;i<10;i++){
			for(int j=0;j<3;j++){				 
				String strI =Integer.toString(k);
				if(strI.trim().length()==1){
					bc[j][i]=Setup.getThaiYear(day).trim()+"0000"+strI.trim();	
				}
				else if(strI.trim().length()==2){
					bc[j][i]=Setup.getThaiYear(day).trim()+"000"+strI.trim();	
				}
				else if(strI.trim().length()==3){
					bc[j][i]=Setup.getThaiYear(day).trim()+"00"+strI.trim();	
				}
				else if(strI.trim().length()==4){
					bc[j][i]=Setup.getThaiYear(day).trim()+"0"+strI.trim();	
				}
				else if(strI.trim().length()==5){
					bc[j][i]=Setup.getThaiYear(day).trim()+strI.trim();	
				}
				
				k++;
				}
		}
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
	    
	    int x_start=85;
	    int y_start=15;
	    for(int j=0;j<10;j++){
	    	for(int i=0;i<3;i++){

	    			try {
	    					//String bc=Integer.toString(i);
	    					addBarcode(bc[i][j]);
	    					barcode.draw(g2d,x_start+(i*150),y_start+(j*63));
	    				} catch (OutputException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
    	 
	    		}
	    	}
	
	    return PAGE_EXISTS;
	}
	public Barcode addBarcode(String inbarcode){
		String inBarcode=inbarcode;		
		try {
			barcode = BarcodeFactory.createCode128(inBarcode);
		} catch (BarcodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	int gw = barcode.getWidth();
    	barcode.setFont(new Font("Arial", Font.PLAIN, 8));
    	barcode.setBarHeight(2);
    	barcode.setBarWidth(1);
    	return barcode;
	}

}

