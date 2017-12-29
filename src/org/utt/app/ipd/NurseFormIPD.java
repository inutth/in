 package org.utt.app.ipd;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.utt.app.InApp;
import org.utt.app.ipd.ObjectIPD;
import org.utt.app.util.ComboItem;
import org.utt.app.dao.DBmanager;
import org.utt.app.util.PrintIPDLabLabel;
import org.utt.app.util.Setup;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JREmptyDataSource;

public class NurseFormIPD extends JPanel implements Observer,ActionListener{
	ObjectIPD oUserInfo;
	Dimension screen;
	JPanel LeftSection,MainSection,MiddleSection,RightSection,mid1,mid2,right1;
	JTable tableIPDform;
	Vector<String> columnNamesIPDform;
	JScrollPane scrollPaneIPDform;
	JSplitPane split;
	JLabel Label_Info,Label_Row1,Label_Row2,Label_Setting;
	String an1="",an2="",age1="",age2="",hn1="",hn2="",name1="",name2="";
	JButton ButtonPrint;
	JComboBox cbDR,cbRow1,cbRow2;
	String formcode="",doctorname="";
	String name="",an="",hn="",age="",bed="",memo="",cid="";
	String wardcode,department_id,ward_name_short,wardname,department,departmentName;
	GregorianCalendar day ;
	/**
	 * Create the panel.
	 */
	public NurseFormIPD(ObjectIPD oUserInfo) {
		super();
		this.oUserInfo=oUserInfo;
		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width-(screen.width*2)/10, screen.height-175));
		 
		cbRow1 =new JComboBox(); 
		cbRow1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		cbRow2 =new JComboBox();
		cbRow2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		day = new GregorianCalendar();
		
		split = new JSplitPane(split.HORIZONTAL_SPLIT);
		split.setDividerLocation(200);
		
		LeftSection = new JPanel();
		LeftSection.setPreferredSize(new Dimension(200, screen.height-175));
		split.setLeftComponent(LeftSection);
		LeftSection.setLayout(new BorderLayout(0, 0));
		
		JLabel label = new JLabel("แบบฟอร์ม");
		label.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		LeftSection.add(label, BorderLayout.NORTH);
		 
		
		columnNamesIPDform = new Vector<String>();		
		columnNamesIPDform.add("");
		columnNamesIPDform.add("");
		
		Vector<Vector<String>> dataIPDform = new Vector<Vector<String>>();
		DefaultTableModel modelIPDform = new DefaultTableModel(dataIPDform, columnNamesIPDform){
			public Class getColumnClass(int column){
				return getValueAt(0, column).getClass();
			}
		};
		tableIPDform = new JTable(modelIPDform){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
				Component c = super.prepareRenderer(renderer, row, column);			
				if (!isRowSelected(row)){
					c.setBackground(getBackground());
				}
				return c;
			}
		};
		
	 	
		tableIPDform = new JTable();
		tableIPDform.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int row = tableIPDform.rowAtPoint(e.getPoint());
		        int col = tableIPDform.columnAtPoint(e.getPoint());	        
		        formcode=tableIPDform.getValueAt(row, 0).toString().trim();
		        String form_name=tableIPDform.getValueAt(row, 1).toString().trim();
		        Label_Info.setText(form_name);
		        if(formcode.equals("0019")){
		        	//PrintLabLabel();
		        	 formPanel(formcode);
		        }
		        else if(formcode.equals("0016")){
		        	mid2.removeAll();
		        	createForm(formcode,"2");
		        	mid2.revalidate();
		    		mid2.repaint();
		        }
		        else{
		        	mid2.removeAll();
		        	//PrintFORM(formcode);
		        	createForm(formcode,"1");
		        	mid2.revalidate();
		    		mid2.repaint();
		        }
			}
		});
		tableIPDform.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableIPDform.setRowHeight(25);		
		tableIPDform.setFillsViewportHeight(true);
 
		scrollPaneIPDform = new JScrollPane(tableIPDform);
		//getValueWard();
		//setInit();
		//getDataForm();
		LeftSection.add(scrollPaneIPDform, BorderLayout.CENTER);
		
		MainSection = new JPanel();
		MainSection.setPreferredSize(new Dimension((screen.width-(screen.width*2)/10)-200, screen.height-175));
		MainSection.setLayout(new BorderLayout(0, 0));
		split.setRightComponent(MainSection);
		add(split, BorderLayout.CENTER);
		
		RightSection = new JPanel();
		RightSection.setPreferredSize(new Dimension(200, screen.height-175));
		MainSection.add(RightSection, BorderLayout.EAST);
		RightSection.setLayout(new BorderLayout(0, 0));
		
		right1 = new JPanel();
		right1.setBorder(new TitledBorder(null, "\u0E23\u0E32\u0E22\u0E0A\u0E37\u0E48\u0E2D\u0E41\u0E1E\u0E17\u0E22\u0E4C", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		right1.setPreferredSize(new Dimension(200, 50));
		RightSection.add(right1, BorderLayout.NORTH);
		right1.setLayout(null);
		
		 
		cbDR = new JComboBox();
		cbDR.setFont(new Font("Tahoma", Font.PLAIN, 13));		 
		
		cbDR.setSize(180, 250);
		//cbDR.setSelectedIndex(0);
		cbDR.setBounds(5, 15, 180, 20);
		right1.add(cbDR);
		
		MiddleSection = new JPanel();
		MiddleSection.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10+400), screen.height-175));
		MainSection.add(MiddleSection, BorderLayout.CENTER);
		MiddleSection.setLayout(new BorderLayout(0, 0));
		
		mid1 = new JPanel();
		mid1.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10+400), 30));
		MiddleSection.add(mid1, BorderLayout.NORTH);
		mid1.setLayout(null);
		
		Label_Info = new JLabel(" ");
		Label_Info.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Label_Info.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Info.setBounds(20, 5, 300, 20);
		mid1.add(Label_Info);
		
		mid2 = new JPanel();
		MiddleSection.add(mid2, BorderLayout.CENTER);
		mid2.setLayout(null);
		
	}
	public void setComboBox() {
		wardcode= InApp.reportcode.trim();
		getValueWard();
		setInit();
		cbDR.setSelectedIndex(0);
		getDataForm();

	}
	public void update(Observable oObservable, Object oObject) {
		
		oUserInfo = ((ObjectIPD)oObservable); // cast
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ButtonPrint){			 
			PrintLabLabel();
			 
		}
		else if(e.getSource() == cbRow1){
			String valueRow1=((ComboItem)cbRow1.getSelectedItem()).getValue();
			an1=valueRow1.substring(0, valueRow1.indexOf(":")).trim();
			name1=valueRow1.substring(valueRow1.indexOf(":")+1, valueRow1.indexOf("*")).trim();
			hn1=valueRow1.substring(valueRow1.indexOf("*")+1, valueRow1.indexOf("#")).trim();		 
			age1=valueRow1.substring(valueRow1.indexOf("#")+1).trim();
			 
		}
		else if(e.getSource() == cbRow2){
			String valueRow2=((ComboItem)cbRow2.getSelectedItem()).getValue();
			an2=valueRow2.substring(0, valueRow2.indexOf(":")).trim();
			name2=valueRow2.substring(valueRow2.indexOf(":")+1, valueRow2.indexOf("*")).trim();
			hn2=valueRow2.substring(valueRow2.indexOf("*")+1, valueRow2.indexOf("#")).trim();			 
			age2=valueRow2.substring(valueRow2.indexOf("#")+1).trim();
			 
		}
	}
	public void getDataForm(){
		tableIPDform.setModel(fetchDataForm());
		TableColumnModel columnModelIPDform = tableIPDform.getColumnModel();		
		columnModelIPDform.getColumn(0).setPreferredWidth(0);
		columnModelIPDform.getColumn(0).setMinWidth(0);
		columnModelIPDform.getColumn(0).setMaxWidth(0);
		columnModelIPDform.getColumn(1).setPreferredWidth(260);

		((DefaultTableModel)tableIPDform.getModel()).fireTableDataChanged();
		 
	}
	public  DefaultTableModel fetchDataForm(){
		String[] parts=null;
		parts = InApp.userappcode.split("\\,");
		String sql_user1="",sql_user="";
		if(parts.length==1){
			for(int i=0;i<parts.length;i++){
				sql_user1=" and formipdnurse.ward_code='"+parts[i]+"'";
			}
			sql_user=sql_user1;
		}
		else if(parts.length==0){
			sql_user="";
		}
		else if(parts.length>1){
			sql_user1="and formipdnurse.ward_code in (";
			for(int i=0;i<parts.length;i++){
				sql_user1+="'"+parts[i]+"',";				 
			}
			sql_user=sql_user1.substring(0,sql_user1.length()-1)+")";
		}
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Connection conn ;
		PreparedStatement stmt ;
		String query =  "select distinct formipdnurse.form_code, formipd_master.form_name from formipdnurse,formipd_master where formipdnurse.form_code=formipd_master.form_code and formipdnurse.type='1'  "+sql_user+" order by formipdnurse.form_code" ;
		try {
			conn=new DBmanager().getConnMySql();
	        stmt = conn.prepareStatement(query);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	        	final Vector<String> vstring = new Vector<String>();
	        	vstring.add(" "+rs.getString(1).trim());
	        	vstring.add(" "+rs.getString(2).trim());
	      
	        	data.add(vstring);
	        }
	        stmt.close();
	    	conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DefaultTableModel(data, columnNamesIPDform);
	}
	public void getValueWard(){
		 
		Connection conn;
		PreparedStatement stmt;
		String query =  "select ward_name, ward_department,department_id,ward_name_short from ward_ipd where  status='1'  and ward_code='"+wardcode+"'" ;
		
		try {
			conn=new DBmanager().getConnMySql();
	        stmt = conn.prepareStatement(query);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {	        	 
	        	wardname=rs.getString(1).trim();
		    	department=rs.getString(2).trim();
		    	department_id=rs.getString(3).trim();
		    	ward_name_short=rs.getString(4).trim();
	        }
	        stmt.close();
	    	conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createForm(String formcode,String typeofprint)   {
		//System.out.println(formcode);
		name=oUserInfo.GetPtName().trim();
		an=oUserInfo.GetPtAN().trim();
		hn=oUserInfo.GetPtHN().trim();
		age=oUserInfo.GetPtAge().trim();
		bed=oUserInfo.GetPtBed();
		doctorname=((ComboItem)cbDR.getSelectedItem()).getKey();
		
		
		cid=oUserInfo.GetPtCID();
		String date_print=Setup.ConvertDateTimePrint(day)+" น. ";
		String label=oUserInfo.GetPtLabel();
		String right=label.substring(label.indexOf(":")+1);
		String h_print=typeofprint.trim();
		String confFile="report/"+formcode.trim()+".jrxml";
		InputStream is;
		JasperReportBuilder report = DynamicReports.report(); 
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(confFile );
			report.setTemplateDesign(is);
			
			if(formcode.trim().equals("0021") || formcode.trim().equals("0022") || formcode.trim().equals("0023") || formcode.trim().equals("0024")){
				String inBarcode=Setup.DateInDBMSSQLRef43no543(oUserInfo.GetPtVisitdate())+formcode+InApp.reportcode.trim()+"0000"+an;
				report.setParameter("barcode",inBarcode);
				report.setParameter("line3", "ชื่อ " +name+" อายุ "+age+" เลขที่บัตรประชาชน: "+cid);
				report.setParameter("line8", "HN:    "+hn+"   AN:   "+an+"   Bed: "+bed+"    VN: .................................");
				report.setParameter("line4","วันที่ "+date_print +"  "+right);
				report.setParameter("line5","***การแพ้ยา***"+oUserInfo.GetMemo());
				report.setParameter("line6",oUserInfo.GetMemo1());
				report.setParameter("line7",formcode.trim()+".jrxml");
				//System.out.println(inBarcode);
				report.setDataSource(new JREmptyDataSource());
			}
			else if(formcode.trim().equals("0026")){
				String inBarcode=Setup.DateInDBMSSQLRef43no543(oUserInfo.GetPtVisitdate())+formcode+InApp.reportcode.trim()+"0000"+an;
				report.setParameter("barcode",inBarcode);
				report.setParameter("line2", "ชื่อ " +name+"    อายุ "+age+"    HN: "+hn+"  AN: "+an+"  แพทย์: " +doctorname);
				report.setParameter("line1", " "+bed);
				
				report.setParameter("line7",formcode.trim()+".jrxml");
				//System.out.println(inBarcode);
				report.setDataSource(new JREmptyDataSource());
			}
			else if( formcode.trim().equals("0027")){
				String inBarcode=Setup.DateInDBMSSQLRef43no543(oUserInfo.GetPtVisitdate())+formcode+InApp.reportcode.trim()+"0000"+an;
				report.setParameter("barcode",inBarcode);
				report.setParameter("line2", "ชื่อ " +name+"    อายุ "+age+"    HN: "+hn+"  AN: "+an);
				report.setParameter("line1", " "+bed);
				
				report.setParameter("line7",formcode.trim()+".jrxml");
				//System.out.println(inBarcode);
				report.setDataSource(new JREmptyDataSource());
			}
			else{
				String inBarcode=oUserInfo.GetPtVisitdate()+formcode+an;
				report.setParameter("line1", "ชื่อ " +name+" อายุ "+age+"   HN:"+hn+"   AN:"+an+"   Bed: "+bed);
				report.setParameter("line2", "Dapartment : "+department+ "   Ward: "+wardname+"   Attending Physician: " +doctorname);
	 			report.setParameter("barcode",inBarcode);
	 			report.setParameter("line3",formcode.trim()+".jrxml");
				report.setDataSource(new JREmptyDataSource());
			}
			 
			 
			report.print(true);
			//report.show(false);
		} catch (DRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void formPanel(String informcode){
		mid2.removeAll();
		String formcode=informcode.trim();
		if(formcode.equals("0019")){
			//list of patient
			Label_Row1 = new JLabel("Row 1 ",JLabel.CENTER);		 
			Label_Row1.setFont(new Font("Tahoma", Font.PLAIN, 13));
			Label_Row1.setBounds(10,35,100,20);
			mid2.add(Label_Row1);
			Label_Row2 = new JLabel("Row 2  ",JLabel.CENTER);	
			Label_Row2.setFont(new Font("Tahoma", Font.PLAIN, 13));
			Label_Row2.setBounds(10,75,100,20);
			mid2.add(Label_Row2);
			cbRow1.setSize(300, 20);
			cbRow2.setSize(300, 20);
			cbRow1.setSelectedIndex(0);
			cbRow1.setBounds(120, 35, 300, 20);
			cbRow1.addActionListener(this);	
			mid2.add(cbRow1);
			
			cbRow2.setSelectedIndex(0);
			cbRow2.setBounds(120, 75, 300, 20);
			cbRow2.addActionListener(this);	
			mid2.add(cbRow2);
			
			Label_Setting = new JLabel("หมายเหตุ: ขอบกระดาษด้านบนที่ห่างจากสติ๊กเกอร์แถวแรก ไม่ควรเกิน 2.5 มิลลิเมตร  ",JLabel.CENTER);		 
			Label_Setting.setFont(new Font("Tahoma", Font.PLAIN, 13));
			Label_Setting.setBounds(22,150,500,20);
			mid2.add(Label_Setting);
			
			ButtonPrint = new JButton("Print");				 
			ButtonPrint.setBounds(200,280,120,60);
			mid2.add(ButtonPrint);
			ButtonPrint.addActionListener(this);
			ButtonPrint.setMnemonic(KeyEvent.VK_V);
		}else{
			mid2.removeAll();
		}
		mid2.revalidate();
		mid2.repaint();
	}
	public void setInit(){
		
		String[] parts=null;
		parts = InApp.userappcode.split("\\,");
		String sql_user1="",sql_user="";
		if(parts.length==1){
			for(int i=0;i<parts.length;i++){
				sql_user1=" and ward='"+parts[i]+"'";
			}
			sql_user=sql_user1;
		}
		else if(parts.length>1){
			sql_user1="and ward in (";
			for(int i=0;i<parts.length;i++){
				sql_user1+="'"+parts[i]+"',";				 
			}
			sql_user=sql_user1.substring(0,sql_user1.length()-1)+")";
		}
		 
		int p=0;
		String queryDR =  "select  doctor_name,doctor_id from doctor where department=? and status='1' order by doctor_id" ;
		String query="select an,firstname,lastname,hn,usedrightcode,outdatetime,BirthDateTime,sex,maritalstatus,initialnamecode,ref,bedno from in_view_pt_ipd  where  OUTDATETIME  is null  and reftype='01'  and suffix='0' "+sql_user+" order by bedno ";			

		try {

			Connection conn3=new DBmanager().getConnMSSql();
			
			PreparedStatement stmtRow = conn3.prepareStatement(query);
			ResultSet rsRow = stmtRow.executeQuery();
		 
			while (rsRow.next()) {	
				String sex_pt="",mar_pt="";
				String initname="",right="";
				Date birthday=rsRow.getDate(7);
				String age_pt=Setup.AgeInAll(birthday.toString()).trim();
				if(rsRow.getString(10)==null || rsRow.getString(10).equals("1") || rsRow.getString(10).equals("2") || rsRow.getString(10).equals("3") || rsRow.getString(10).equals("127") || rsRow.getString(10).equals("128")){
					sex_pt=rsRow.getString(8).trim();
					mar_pt=rsRow.getString(9).trim();				 
					int age_y_int= Integer.parseInt(Setup.AgeInYear(birthday.toString()).trim());
					if(age_y_int<15){
						if(sex_pt.equals("1")){
							initname="ด.ช.";
						}else if(sex_pt.equals("2")){
							initname="ด.ญ.";
						}
					}else if(age_y_int>=15){
						if(sex_pt.equals("1")){
							initname="นาย";
						}else if(sex_pt.equals("2")){
							if(mar_pt.equals("1")){
								initname="นางสาว";
							}else {
								initname="นาง";
							}
							
						}
					}					 
					
				}else{
					for(int i=0;i<InApp.initname_indb.length;i++){
						if(rsRow.getString(10).trim().equals(InApp.initname_indb[i][0])){
							initname=InApp.initname_indb[i][1];
						}
						//System.out.println(i+1+". "+labunitname[i][0]);
					}
					//initname=rs.getString(10).trim();
				}
				//System.out.println("AN: "+rsRow.getString(1).trim()+"   "+initname+" "+rsRow.getString(2).trim().substring(1)+" "+rsRow.getString(3).trim().substring(1)+"***"+rsRow.getString(1).trim()+":"+initname+" "+rsRow.getString(2).trim().substring(1)+" "+rsRow.getString(3).trim().substring(1)+"*"+rsRow.getString(4).trim()+"#"+age_pt);
				
				cbRow1.addItem(new ComboItem("AN: "+rsRow.getString(1).trim()+"   "+initname+" "+rsRow.getString(2).trim().substring(1)+" "+rsRow.getString(3).trim().substring(1),rsRow.getString(1).trim()+":"+initname+" "+rsRow.getString(2).trim().substring(1)+" "+rsRow.getString(3).trim().substring(1)+"*"+rsRow.getString(4).trim()+"#"+age_pt));
				cbRow2.addItem(new ComboItem("AN: "+rsRow.getString(1).trim()+"   "+initname+" "+rsRow.getString(2).trim().substring(1)+" "+rsRow.getString(3).trim().substring(1),rsRow.getString(1).trim()+":"+initname+" "+rsRow.getString(2).trim().substring(1)+" "+rsRow.getString(3).trim().substring(1)+"*"+rsRow.getString(4).trim()+"#"+age_pt));
				
			}
			stmtRow.close();
			conn3.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			 
			Connection conn=new DBmanager().getConnMySql();
			PreparedStatement stmtDR = conn.prepareStatement(queryDR);
			stmtDR.setString(1,department_id);
			ResultSet rsDR = stmtDR.executeQuery();
			
			while (rsDR.next()) {	
				//System.out.println(rsDR.getString(1).trim()+" -"+rsDR.getString(2).trim());
				cbDR.addItem(new ComboItem(rsDR.getString(1).trim(),rsDR.getString(2).trim()));
			}
			rsDR.close();
			stmtDR.close();			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PrintLabLabel() {
		PrintIPDLabLabel pr1= new PrintIPDLabLabel();
		
		pr1.setName1(name1);
		pr1.setHN1(hn1);
		pr1.setAN1(an1);
		pr1.setAge1(age1);
		pr1.setDate(oUserInfo.GetPtVisitdate());
		pr1.setWardNameShort(ward_name_short);
		pr1.setPosition("1");
		
		pr1.setName2(name2);
		pr1.setHN2(hn2);
		pr1.setAN2(an2);
		pr1.setAge2(age2);
		
		pr1.showPrinter();
	}
	
	
}

