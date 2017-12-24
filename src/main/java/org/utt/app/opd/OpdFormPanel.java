package org.utt.app.opd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.media.jai.PlanarImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.utt.app.InApp;
import org.utt.app.OPDFrame;
import org.utt.app.opd.ObjectOPD;
import org.utt.app.dao.DBmanager;
import org.utt.app.util.Setup;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JREmptyDataSource;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import javax.swing.JPanel;

public class OpdFormPanel extends JPanel implements Observer{
	ObjectOPD oUserInfo;
	Dimension screen;
	JTable tableOPDform;
	JSplitPane split;
	JPanel LeftSection,MainSection;
	Vector<String> columnNamesOPDform;
	JPanel MiddleSection,mid2,mid1;
	JScrollPane scrollPaneOPDform,scrollPaneImg;
	JLabel Label_IMG;
	ImageIcon icon;
	GregorianCalendar day ;
	String nameBefore="";
	/**
	 *  
	 */
	public OpdFormPanel(ObjectOPD oUserInfo) {
		super();
		this.oUserInfo=oUserInfo;
		day = new GregorianCalendar();
		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width-(screen.width*2)/10, screen.height-175));
		split = new JSplitPane(split.HORIZONTAL_SPLIT);
		split.setDividerLocation(200);
	 
		LeftSection = new JPanel();
		LeftSection.setPreferredSize(new Dimension(200, screen.height-175));
		split.setLeftComponent(LeftSection);
		LeftSection.setLayout(new BorderLayout(0, 0));
		
		JLabel label_info = new JLabel("แบบฟอร์ม");
		label_info.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_info.setHorizontalAlignment(SwingConstants.CENTER);
		LeftSection.add(label_info, BorderLayout.NORTH);
		
		columnNamesOPDform = new Vector<String>();		
		columnNamesOPDform.add("");
		columnNamesOPDform.add("");
		columnNamesOPDform.add("");
		columnNamesOPDform.add("");
		columnNamesOPDform.add("");
		columnNamesOPDform.add("");
		
		Vector<Vector<String>> dataOPDform = new Vector<Vector<String>>();
		DefaultTableModel modelOPDform = new DefaultTableModel(dataOPDform, columnNamesOPDform){
			public Class getColumnClass(int column){
				return getValueAt(0, column).getClass();
			}
		};
		tableOPDform = new JTable(modelOPDform){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
				Component c = super.prepareRenderer(renderer, row, column);			
				if (!isRowSelected(row)){
					c.setBackground(getBackground());
				}
				return c;
			}
		};
		
	 	
		tableOPDform = new JTable();
		tableOPDform.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String clinic_name="",clinic_scan="";
				int row_tableOPDform = tableOPDform.rowAtPoint(e.getPoint());
		        int col_tableOPDform = tableOPDform.columnAtPoint(e.getPoint());
		        String form_code=tableOPDform.getValueAt(row_tableOPDform,1).toString().trim();
		        String typeofprint=tableOPDform.getValueAt(row_tableOPDform,5).toString().trim();
		        String form_clinic_name=tableOPDform.getValueAt(row_tableOPDform,4).toString().trim();
		        String page=tableOPDform.getValueAt(row_tableOPDform,3).toString().trim();
		        String clinic_pt=oUserInfo.GetPtCliniccode();
		        for(int i=0;i<OPDFrame.clinicname.length;i++){
					if(clinic_pt.equals(OPDFrame.clinicname[i][0])){
						clinic_name=OPDFrame.clinicname[i][1];
					}
					//System.out.println(i+1+". "+clinic_name[i][0]);
				}
				for(int i=0;i<OPDFrame.clinicno.length;i++){
					if(clinic_pt.equals(OPDFrame.clinicno[i][0])){
						clinic_scan=OPDFrame.clinicno[i][1];
					}
					//System.out.println(i+1+". "+clinic_name[i][0]);
				}
				
		        if(form_code.equals("00208")){
					panelreport(form_code,typeofprint,clinic_name,clinic_scan,form_clinic_name,page);
					
				}else{
					createreport(form_code,typeofprint,clinic_name,clinic_scan,form_clinic_name,page);
					 
				}
				 
			}
		});
		tableOPDform.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tableOPDform.setRowHeight(25);		
		tableOPDform.setFillsViewportHeight(true);
 
		scrollPaneOPDform = new JScrollPane(tableOPDform);
		getDataForm();
		LeftSection.add(scrollPaneOPDform, BorderLayout.CENTER);
		
		MainSection = new JPanel();
		MainSection.setPreferredSize(new Dimension((screen.width-(screen.width*2)/10)-200, screen.height-175));
		MainSection.setLayout(new BorderLayout(0, 0));
		split.setRightComponent(MainSection);
		
		MiddleSection = new JPanel();
		MiddleSection.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10)-300, screen.height-175));
		MainSection.add(MiddleSection, BorderLayout.CENTER);
		MiddleSection.setLayout(null);
		
		mid2 = new JPanel();
		mid2.setBounds(5, 40, (screen.width-(screen.width*2)/10)-285, 410);
		MiddleSection.add(mid2);
		mid2.setLayout(new BorderLayout(0, 0));	
		Label_IMG = new JLabel("");
		Label_IMG.setHorizontalAlignment(SwingConstants.CENTER);
		
		scrollPaneImg = new JScrollPane(Label_IMG);
		mid2.add(scrollPaneImg, BorderLayout.CENTER);
		
		mid1 = new JPanel();
		mid1.setBounds(5, 5, (screen.width-(screen.width*2)/10)-300, 30);
		MiddleSection.add(mid1);
		mid1.setLayout(null);

		 
		
		add(split, BorderLayout.CENTER);
	}
	public void update(Observable oObservable, Object oObject) {		
		oUserInfo = ((ObjectOPD)oObservable); // cast
	}
	public void getDataForm(){
		tableOPDform.setModel(fetchDataForm());
		TableColumnModel columnModelOPDform = tableOPDform.getColumnModel();
		columnModelOPDform.getColumn(0).setPreferredWidth(30);
		columnModelOPDform.getColumn(1).setPreferredWidth(0);
		columnModelOPDform.getColumn(1).setMinWidth(0);
		columnModelOPDform.getColumn(1).setMaxWidth(0);
		columnModelOPDform.getColumn(2).setPreferredWidth(370);
		for (int n = 2; n < 5; n++) {	
			columnModelOPDform.getColumn(n+1).setPreferredWidth(0);
			columnModelOPDform.getColumn(n+1).setMinWidth(0);
			columnModelOPDform.getColumn(n+1).setMaxWidth(0);
		}

		((DefaultTableModel)tableOPDform.getModel()).fireTableDataChanged();
	}
	public  DefaultTableModel fetchDataForm(){
		String[] parts=null;
		parts = InApp.userappcode.split("\\,");
		String sql_user1="",sql_user="";
		if(parts.length==1){
			for(int i=0;i<parts.length;i++){
				sql_user1=" and clinic='"+parts[i]+"'";
			}
			sql_user=sql_user1;
		}
		else if(parts.length==0){
			sql_user="";
		}
		else if(parts.length>1){
			sql_user1="and clinic in (";
			for(int i=0;i<parts.length;i++){
				sql_user1+="'"+parts[i]+"',";				 
			}
			sql_user=sql_user1.substring(0,sql_user1.length()-1)+")";
		}
		Connection conn;
		PreparedStatement stmt;
		ResultSet rs;
		
		Vector<Vector<String>> dataForm = new Vector<Vector<String>>();
		String sql_formopd = "select form_code,form_name,page,form_name_print,typeofprint  from formopd where type='1'  "+sql_user+"  order by clinic,orderpage";
		//System.out.println(sql_formopd);
		try {
			conn=new DBmanager().getConnMySql();
			stmt = conn.prepareStatement(sql_formopd);
			rs = stmt.executeQuery();
			int p=1;
			while (rs.next()) {
				final Vector<String> vstringForm = new Vector<String>();
				vstringForm.add(" "+Integer.toString(p) );
				vstringForm.add(rs.getString(1).trim());	           
	        	vstringForm.add(rs.getString(2));
	        	if(rs.getString(3) ==null){
	        		vstringForm.add("88");
	        	}else{
	        	vstringForm.add(rs.getString(3).trim());
	        	}
	        	if(rs.getString(4) ==null){
	        		vstringForm.add("");
	        	}else{
	        	vstringForm.add(rs.getString(4).trim());
	        	}
	        	if(rs.getString(5) ==null){
	        		vstringForm.add("1");
	        	}else{
	        	vstringForm.add(rs.getString(5).trim());
	        	}
	        	//System.out.println(rs.getString(1).trim()+""+rs.getString(2).trim()+"--"+rs.getString(5).trim());
				dataForm.add(vstringForm);
				p++;
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DefaultTableModel(dataForm, columnNamesOPDform);
	}
	public void panelreport(String formcode,String type,String clinic_name,String clinic_scan,String form_clinic_name,String page){
		if(oUserInfo.GetPtHN().length() !=7){
			JOptionPane.showMessageDialog(this,"Please select Patient","Patient Error",JOptionPane.ERROR_MESSAGE);

		}else{
			JButton ButtonView = new JButton("View");
			ButtonView.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
						view();	 
				}
			});
			ButtonView.setBounds(20, 4, 89, 23);
			mid1.add(ButtonView);
			
			JButton ButtonPrint = new JButton("print");
			ButtonPrint.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createreport(formcode,type,clinic_name,clinic_scan,form_clinic_name,page);
					ClearFile();
				}
			});
			ButtonPrint.setBounds(110, 4, 89, 23);
			mid1.add(ButtonPrint);
			
			mid1.validate();
			mid1.repaint();
		}
		 
	}
	public void view(){
		nameBefore="";
		String folder="C:\\utthscan\\";
		File f = new File(folder);
		File[] listOfFiles = f.listFiles();
		String file_name[]=new String[listOfFiles.length];
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {     		
        		file_name[i]=listOfFiles[i].getName();
        	}
		}
		if(file_name.length>1){
			JOptionPane.showMessageDialog(this,"มีจำนวนไฟล์ไม่ถูกต้อง ","ข้อผิดพลาด",JOptionPane.WARNING_MESSAGE); 
		}else{
			for (int i = 0; i < file_name.length; i++) {			
				if(file_name[i] !=null){
					//System.out.println("file from scan show.."+file_name[i]);
					nameBefore=folder+file_name[i];
					break;
				} 
				else{			
					JOptionPane.showMessageDialog(this,"ไม่มีไฟล์นี้แล้ว กรุณา Scanไฟล์แล้วทำงานต่อไป ","ข้อผิดพลาด",JOptionPane.WARNING_MESSAGE); 
			 		  break;
				}
			}
		}
		if(nameBefore.equals("")){
			JOptionPane.showMessageDialog(this,"กรุณาตรวจสอบ Folder C:\\utthscan","ข้อผิดพลาด",JOptionPane.WARNING_MESSAGE); 
		}else{
			FileInputStream in;
			try {
				in = new FileInputStream(nameBefore);
				FileChannel channel = in.getChannel();
			    ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
			    channel.read(buffer);
			    Image image = load(buffer.array());			 
				Image imageScaled = image.getScaledInstance(560, 600,  Image.SCALE_SMOOTH);
				icon = new ImageIcon(imageScaled);
				
				Label_IMG.setIcon(icon);
				//close load
				channel.close();
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     
		}
		
		mid2.validate();
		mid2.repaint();
	}
	public Image load(byte[] data) throws IOException{
		Image image = null;
	    SeekableStream stream = new ByteArraySeekableStream(data);
	    String[] names = ImageCodec.getDecoderNames(stream);
	    ImageDecoder dec = 
	      ImageCodec.createImageDecoder(names[0], stream, null);
	    RenderedImage im = dec.decodeAsRenderedImage();
	    image = PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
	    return image;
	}
	public void ClearFile(){		
		File folder = new File("C:\\utthscan\\");
		File[] listOfFiles = folder.listFiles();
        //System.out.println(listOfFiles.length);
        String file_name_input[]=new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
        	if (listOfFiles[i].isFile()) {     		
        		listOfFiles[i].delete();
        	}
        	else if(listOfFiles[i].isDirectory()){
        		listOfFiles[i].delete();
        	}
        }
	}
	public void createreport(String report_code,String typeofprint,String clinic_name,String clinic_scan,String form_clinic_name,String page){
		String h_print=typeofprint.trim();
    	String er1="",lab="";
    	if(report_code.trim().equals("00041") || report_code.trim().equals("00049")){
    		er1="เวลาคัดแยก..........................น.";
    	}
    	else if(report_code.trim().equals("09001")){
    		//System.out.println("x-ray..");
    	}
    	int h_page=0;
    	if(h_print.equals("1")){
    		h_page=430;
    	}else if(h_print.equals("2")){
    		h_page=840;
    	}
    	//System.out.println(h_print+"x-ray.."+h_page+"--org/utt/app/report/"+report_code.trim()+".jrxml");
    	String confFile="report/"+report_code.trim()+".jrxml"; 	
    	
    	
    	String filename=Setup.ConverttoScanDate(Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()))+clinic_scan+page+"0001"+oUserInfo.GetPtHN().trim();
    	//System.out.println(filename+"**clinic pt "+clinic_name+"** clinic scan "+clinic_scan);
    	//right=label.substring(label.indexOf(":")+1);
    	InputStream is;
		JasperReportBuilder report = DynamicReports.report(); 

		report.setTemplate(template()).setPageFormat(595, h_page, PageOrientation.PORTRAIT);
		//report.setPageMargin(DynamicReports.margin().setLeft(10).setTop(10).setRight(10).setBottom(10));
		//report.title(mainDetail(er1));
		String date_print="";
    	//System.out.println("same"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()));
    	//System.out.println("same"+Setup.GetDateNow());
    	if(Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()).trim().equals(Setup.GetDateNow().trim())){
    		date_print=Setup.ConvertDateTimePrint(day)+" น. ";
    	}else{
    		date_print=Setup.ShowThaiDate1(Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()).trim())+" เวลา.......................น.";
    	}
    	try {
			is = this.getClass().getClassLoader().getResourceAsStream(confFile );
			report.setTemplateDesign(is);
			//report.setPageMargin(DynamicReports.margin().setLeft(50).setRight(10).setTop(10).setBottom(10));
			if(report_code.trim().equals("00802")){
				report.setPageMargin(DynamicReports.margin().setLeft(50).setRight(10).setTop(10).setBottom(10));
				report.setParameter("line1","โรงพยาบาลอุตรดิตถ์   "+form_clinic_name);
				report.setParameter("line2", "ชื่อ " +oUserInfo.GetPtName()+" อายุ "+oUserInfo.GetPtAge());
				report.setParameter("line3", "HN:"+oUserInfo.GetPtHN()+" ICD: "+oUserInfo.GetPtCID()+"    นักกายภาพบำบัด..............................");
				 
			}else if(report_code.trim().equals("01000") || report_code.trim().equals("01001")){
				report.setPageMargin(DynamicReports.margin().setLeft(10).setRight(10).setTop(10).setBottom(10));
				report.setParameter("line1","โรงพยาบาลอุตรดิตถ์   ห้องตรวจ: "+clinic_name);
				report.setParameter("line2",form_clinic_name);
				report.setParameter("line3", "ชื่อ " +oUserInfo.GetPtName()+" อายุ "+oUserInfo.GetPtAge()+" HN:"+oUserInfo.GetPtHN()+" VN:"+oUserInfo.GetPtVN()+" เลขที่บัตรประชาชน: "+oUserInfo.GetPtCID());
				report.setParameter("line4","วันที่ "+date_print +" "+er1+" "+oUserInfo.GetRightCode());
				report.setParameter("line5","***การแพ้ยา***"+oUserInfo.GetMemo());
				report.setParameter("line6",oUserInfo.GetMemo1());
				report.setParameter("line7",report_code.trim()+".jrxml");
				 
			}
			else if(report_code.trim().equals("00208")){
				String img=nameBefore;
				//ImageIO.write(tif, "png", new File("test.png"));
				report.setPageMargin(DynamicReports.margin().setLeft(50).setRight(10).setTop(10).setBottom(10));
				report.setParameter("line1","โรงพยาบาลอุตรดิตถ์   ห้องตรวจ: "+clinic_name);
				report.setParameter("line2",form_clinic_name);
				report.setParameter("line3", "ชื่อ " +oUserInfo.GetPtName()+" อายุ "+oUserInfo.GetPtAge()+" HN:"+oUserInfo.GetPtHN()+" VN:"+oUserInfo.GetPtVN()+" เลขที่บัตรประชาชน: "+oUserInfo.GetPtCID());
				report.setParameter("line4","วันที่ "+date_print +" "+er1+" "+oUserInfo.GetRightCode());
				report.setParameter("line5","***การแพ้ยา***"+oUserInfo.GetMemo());
				report.setParameter("line6",oUserInfo.GetMemo1());
				report.setParameter("line7",report_code.trim()+".jrxml");
				report.setParameter("line8",img);
			}
			else if(report_code.trim().substring(0,3).equals("022")){
				//String img=nameBefore;
				//ImageIO.write(tif, "png", new File("test.png"));
				report.setPageMargin(DynamicReports.margin().setLeft(50).setRight(10).setTop(10).setBottom(10));
				report.setParameter("line1","โรงพยาบาลอุตรดิตถ์   ห้องตรวจ: "+clinic_name);
				report.setParameter("line2",form_clinic_name);
				report.setParameter("line3", "ชื่อ " +oUserInfo.GetPtName()+" อายุ "+oUserInfo.GetPtAge()+" เลขที่บัตรประชาชน: "+oUserInfo.GetPtCID());
				report.setParameter("line8", "HN:    "+oUserInfo.GetPtHN()+"   VN:   "+oUserInfo.GetPtVN()+"    AN: ................................. เตียง................................");
				report.setParameter("line4","วันที่ "+date_print +" "+er1+" "+oUserInfo.GetRightCode());
				report.setParameter("line5","***การแพ้ยา***"+oUserInfo.GetMemo());
				report.setParameter("line6",oUserInfo.GetMemo1());
				report.setParameter("line7",report_code.trim()+".jrxml");

			}
			else if(report_code.trim().substring(0,3).equals("023")){
				//String img=nameBefore;
				//ImageIO.write(tif, "png", new File("test.png"));
				report.setPageMargin(DynamicReports.margin().setLeft(50).setRight(10).setTop(10).setBottom(10));
				report.setParameter("line1","โรงพยาบาลอุตรดิตถ์   ห้องตรวจ: "+clinic_name);
				report.setParameter("line2",form_clinic_name);
				report.setParameter("line3", "ชื่อ " +oUserInfo.GetPtName()+" อายุ "+oUserInfo.GetPtAge()+" เลขที่บัตรประชาชน: "+oUserInfo.GetPtCID());
				report.setParameter("line8", "HN:    "+oUserInfo.GetPtHN()+"   VN:   "+oUserInfo.GetPtVN()+"    AN: ................................. เตียง................................");
				report.setParameter("line4","วันที่ "+date_print +" "+er1+" "+oUserInfo.GetRightCode());
				report.setParameter("line5","***การแพ้ยา***"+oUserInfo.GetMemo());
				report.setParameter("line6",oUserInfo.GetMemo1());
				report.setParameter("line7",report_code.trim()+".jrxml");

			}
			else if(report_code.trim().equals("00230") || report_code.trim().equals("00231") || report_code.trim().equals("00232")){
				//String img=nameBefore;
				//ImageIO.write(tif, "png", new File("test.png"));
				report.setPageMargin(DynamicReports.margin().setLeft(50).setRight(10).setTop(10).setBottom(10));
				//report.setParameter("line1","โรงพยาบาลอุตรดิตถ์   ห้องตรวจ: "+clinic_name);
				report.setParameter("line1",form_clinic_name);
				report.setParameter("line3", "ชื่อ " +oUserInfo.GetPtName()+" อายุ "+oUserInfo.GetPtAge()+" เลขที่บัตรประชาชน: "+oUserInfo.GetPtCID());
				report.setParameter("line4", "HN:    "+oUserInfo.GetPtHN()+"   สิทธิครั้งที่แล้ว:   "+oUserInfo.GetRightCode());
				
				report.setParameter("line7",report_code.trim()+".jrxml");

			}
			else{
				report.setPageMargin(DynamicReports.margin().setLeft(50).setRight(10).setTop(10).setBottom(10));
				report.setParameter("line1","โรงพยาบาลอุตรดิตถ์   ห้องตรวจ: "+clinic_name);
				report.setParameter("line2",form_clinic_name);
				report.setParameter("line3", "ชื่อ " +oUserInfo.GetPtName()+" อายุ "+oUserInfo.GetPtAge()+" HN:"+oUserInfo.GetPtHN()+" VN:"+oUserInfo.GetPtVN()+" เลขที่บัตรประชาชน: "+oUserInfo.GetPtCID());
				report.setParameter("line4","วันที่ "+date_print +" "+er1+" "+oUserInfo.GetRightCode());
				report.setParameter("line5","***การแพ้ยา***"+oUserInfo.GetMemo());
				report.setParameter("line6",oUserInfo.GetMemo1());
				report.setParameter("line7",report_code.trim()+".jrxml");
				report.setParameter("labresult",lab);
			}
			if(report_code.trim().equals("00230")){
				report.setParameter("barcode",oUserInfo.GetPtHN().trim());	
			}else{
				report.setParameter("barcode",filename);	
			}
			//report.setParameter("barcode",filename);		
			report.setDataSource(new JREmptyDataSource());
			//report.print(true);
			
			//report.print();
			//if(report_code.trim().equals("00049")){
				report.show(false);
			//}else{
				 
			//}
			//report.show(false);
		} catch (DRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}

}
