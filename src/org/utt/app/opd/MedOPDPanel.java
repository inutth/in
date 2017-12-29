package org.utt.app.opd;

import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.utt.app.OPDFrame;
import org.utt.app.dao.DBmanager;
import org.utt.app.util.Setup;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JREmptyDataSource;

public class MedOPDPanel extends JPanel implements  Observer{
	ObjectOPD oUserInfo;
	Dimension screen;
	JPanel LeftSection,MainSection,mid1,mid2,mid3;
	JSplitPane split;
	JTable table,tableMed;
	Vector<String> columnNames,columnNamesMed;
	JScrollPane scrollPane,scrollPaneMed;
	
	JButton ButtonSearch, ButtonMedPrint;
	GregorianCalendar day ;
	public MedOPDPanel(ObjectOPD oUserInfo) {
		super();
		this.oUserInfo=oUserInfo;
		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width-((screen.width*2)/10), screen.height-175));
		day = new GregorianCalendar();
		split = new JSplitPane(split.HORIZONTAL_SPLIT);
		split.setDividerLocation(200);
		
		LeftSection = new JPanel();
		LeftSection.setPreferredSize(new Dimension(200, screen.height-175));
		split.setLeftComponent(LeftSection);
		LeftSection.setLayout(new BorderLayout(0, 0));
		
		JLabel label = new JLabel("ประวัติการรักษา");
		label.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		LeftSection.add(label, BorderLayout.NORTH);
		

		columnNames = new Vector<String>();		
		columnNames.add("");
		columnNames.add("");
		columnNames.add("");
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		DefaultTableModel model = new DefaultTableModel(data, columnNames){
			public Class getColumnClass(int column){
				return getValueAt(0, column).getClass();
			}
		};
		table = new JTable(model){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
				Component c = super.prepareRenderer(renderer, row, column);			
				if (!isRowSelected(row)){
					c.setBackground(getBackground());
				}
				return c;
			}
		};
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {			 
				int row = table.rowAtPoint(e.getPoint());
		        int col = table.columnAtPoint(e.getPoint());
		        String fm=table.getValueAt(row, 2).toString().trim();
		        String visitdate=fm.substring(0,fm.indexOf("@"));
		        String vn=fm.substring(fm.indexOf("@")+1);
		         
		        getDataMed(vn, visitdate);
		        //System.out.println(vn+"--"+oUserInfo.GetPtHN()+"*****"+table.getValueAt(row, 2).toString().trim());
		        //System.out.println(vn+"---"+visitdate);
			}
		});
		
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setRowHeight(25);		
		table.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(table);	 
		LeftSection.add(scrollPane, BorderLayout.CENTER);
		
		MainSection = new JPanel();
		MainSection.setPreferredSize(new Dimension((screen.width-(screen.width*2)/10)-200, screen.height-175));
		MainSection.setLayout(new BorderLayout(0, 0));
		split.setRightComponent(MainSection);
		
		mid1 = new JPanel();
		mid1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		mid1.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10-480), 40));
		MainSection.add(mid1, BorderLayout.NORTH);
		mid1.setLayout(null);
		
		ButtonSearch = new JButton("Show");
		ButtonSearch.setBounds(12, 10, 100, 25);
		ButtonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if(oUserInfo.GetPtHN().equals("")){
					JOptionPane.showMessageDialog(null,"กรุณาเลือก HN ","ข้อผิดพลาด",JOptionPane.WARNING_MESSAGE); 
					 
				}else{
					getData(oUserInfo.GetPtHN());
				}
			}
		});
		ButtonSearch.setFont(new Font("Tahoma", Font.PLAIN, 13));
		mid1.add(ButtonSearch);
		
		mid2 = new JPanel();
		mid2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//mid2.setBackground(new Color(206,203,208));
		mid2.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10-480), screen.height-265));
		MainSection.add(mid2, BorderLayout.CENTER);
		mid2.setLayout(new BorderLayout(0, 0));
		
		columnNamesMed = new Vector<String>();	
		columnNamesMed.add("");
		columnNamesMed.add("");
		columnNamesMed.add("");
		columnNamesMed.add("");
		Vector<Vector<String>> dataMed = new Vector<Vector<String>>();
		DefaultTableModel modelMed = new DefaultTableModel(dataMed, columnNamesMed){
			public Class getColumnClass(int column){
				return getValueAt(0, column).getClass();
			}
		};
		tableMed = new JTable(modelMed){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
				Component c = super.prepareRenderer(renderer, row, column);			
				if (!isRowSelected(row)){
					c.setBackground(getBackground());
				}
				return c;
			}
			 public Class getColumnClass(int column) {
	                switch (column) {
	                    case 0:
	                        return String.class;
	                    case 1:
	                        return Boolean.class;
	                    case 2:
	                        return String.class;
	                    case 3:
	                        return String.class;
	                    default:
	                        return Boolean.class;
	                }
	            }
		};
		tableMed.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableMed.setRowHeight(25);		
		tableMed.setFillsViewportHeight(true);
		tableMed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {			 
				int row = tableMed.rowAtPoint(e.getPoint());
		        int col = tableMed.columnAtPoint(e.getPoint());
		        String fm=tableMed.getValueAt(row, 1).toString().trim();
		        String drug=tableMed.getValueAt(row, 2).toString().trim();
		       // String status=fm.substring(fm.indexOf("@")+1);
		        if(fm.equals("true")) {
		        	System.out.println(fm+"---"+drug);
		        }
		 
		        //System.out.println(vn+"--"+oUserInfo.GetPtHN()+"*****"+table.getValueAt(row, 2).toString().trim());
		         
			}
		});
		
		
		scrollPaneMed = new JScrollPane(tableMed);
		mid2.add(scrollPaneMed, BorderLayout.CENTER);
		
		mid3 = new JPanel();
		mid3.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//mid2.setBackground(new Color(206,203,208));
		mid3.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10-480), 50));
		mid2.add(mid3, BorderLayout.NORTH);
		mid3.setLayout(null);
		ButtonMedPrint = new JButton("Print");
		ButtonMedPrint.setBounds(150, 10, 100, 25);
		ButtonMedPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int li=0;
				String [] med_o=null;
				for (int i = 0; i < tableMed.getRowCount(); i++) {
					Boolean chked = Boolean.valueOf(tableMed.getValueAt(i, 1).toString());
					String dataCol1 = tableMed.getValueAt(i, 2).toString();
					String drug=tableMed.getValueAt(i, 2).toString().trim();
					 
					if (chked) {						
						System.out.println(li+"Print---"+drug);
						li++;
					}
					med_o= new String[li] ;
				}
				for (int i = 0; i < tableMed.getRowCount(); i++) {
					Boolean chked = Boolean.valueOf(tableMed.getValueAt(i, 1).toString());
					String dataCol1 = tableMed.getValueAt(i, 2).toString();
					String drug=tableMed.getValueAt(i, 2).toString().trim();
					 
					if (chked) {						
						 med_o[i]=drug;
					}
					 
				}
				String clinic_name="",clinic_scan="";
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
				String confFile="report/10004.jrxml"; 	
				String filename=Setup.ConverttoScanDate(Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()))+clinic_scan+"130001"+oUserInfo.GetPtHN().trim();
				String date_print="",lab="";
				if(Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()).trim().equals(Setup.GetDateNow().trim())){
		    		date_print=Setup.ConvertDateTimePrint(day)+" น. ";
		    	}else{
		    		date_print=Setup.ShowThaiDate1(Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()).trim())+" เวลา.......................น.";
		    	}
				InputStream is;
				JasperReportBuilder report = DynamicReports.report(); 
				report.setTemplate(template()).setPageFormat(595, 840, PageOrientation.PORTRAIT);
				is = this.getClass().getClassLoader().getResourceAsStream(confFile );
				try {
					report.setTemplateDesign(is);
					
					report.setPageMargin(DynamicReports.margin().setLeft(50).setRight(10).setTop(10).setBottom(10));
					report.setParameter("line1","โรงพยาบาลอุตรดิตถ์   ห้องตรวจ: "+clinic_name);
					report.setParameter("line2",clinic_name);
					report.setParameter("line3", "ชื่อ " +oUserInfo.GetPtName()+" อายุ "+oUserInfo.GetPtAge()+" HN:"+oUserInfo.GetPtHN()+" VN:"+oUserInfo.GetPtVN()+" เลขที่บัตรประชาชน: "+oUserInfo.GetPtCID());
					report.setParameter("line4","วันที่ "+date_print +"  "+oUserInfo.GetRightCode());
					report.setParameter("line5","***การแพ้ยา***"+oUserInfo.GetMemo());
					report.setParameter("line6",oUserInfo.GetMemo1());
					report.setParameter("line7","10004.jrxml");
					report.setParameter("labresult",lab);
					report.setParameter("barcode",filename);
					for(int i=0;i<med_o.length;i++) {
						report.setParameter("med"+(i+1),med_o[i]);
					}
					for(int i=med_o.length;i<20;i++) {
						report.setParameter("med"+(i+1),"");
					}
					report.setDataSource(new JREmptyDataSource());
					//report.print(true);
					report.show(false);
				} catch (DRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			}
		});
		ButtonMedPrint.setFont(new Font("Tahoma", Font.PLAIN, 13));
		mid1.add(ButtonMedPrint);
		add(split, BorderLayout.CENTER);
		
		
	}
	public void update(Observable oObservable, Object oObject) {
		oUserInfo = ((ObjectOPD)oObservable); // cast
	}
	public void getData(String hn){
		table.setModel(fetchData(hn));
		TableColumnModel columnModel = table.getColumnModel();		
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(220);
		columnModel.getColumn(2).setPreferredWidth(0);
		columnModel.getColumn(2).setMinWidth(0);
		columnModel.getColumn(2).setMaxWidth(0);
		((DefaultTableModel)table.getModel()).fireTableDataChanged();
		
	}
	public  DefaultTableModel fetchData(String hn){
		 
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Connection conn ;
		PreparedStatement stmt ;
		String query =  "select distinct visitdate,vn from vnmst where visitdate > '"+Setup.GetDate6Mo()+"'  and hn=? order by visitdate desc ";
		
		try {

			conn=new DBmanager().getConnMSSql();
	        stmt = conn.prepareStatement(query);
	        stmt.setString(1,hn);
	        ResultSet rs = stmt.executeQuery();
	        int p=1;
	        while (rs.next()) {
	        	 
	        	final Vector<String> vstring = new Vector<String>();
	        	vstring.add(Integer.toString(p) );
	        	String drug_date=rs.getString(1).trim().substring(0, 10);
	        	String vn=rs.getString(2).trim();
	            vstring.add(""+Setup.ShowThaiDate1(drug_date));
	            vstring.add(drug_date+"@"+vn);
	        	data.add(vstring);
	        	p++;
	        	//System.out.println("*****"+drug_date+"---"+Setup.ShowThaiDate1(drug_date));
	        }
	        stmt.close();
	    	conn.close();
	    	//System.out.println("*****"+Setup.GetDate6Mo());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DefaultTableModel(data, columnNames);
	}
	public void getDataMed(String vn,String visitdate){
		tableMed.setModel(fetchDataMed(vn,visitdate));
		TableColumnModel columnModelMed = tableMed.getColumnModel();		
		columnModelMed.getColumn(0).setPreferredWidth(40);
		columnModelMed.getColumn(1).setPreferredWidth(40);
		columnModelMed.getColumn(2).setPreferredWidth(720);
		columnModelMed.getColumn(3).setPreferredWidth(0);
		columnModelMed.getColumn(3).setMinWidth(0);
		columnModelMed.getColumn(3).setMaxWidth(0);
		((DefaultTableModel)tableMed.getModel()).fireTableDataChanged();
		
	}
	public  DefaultTableModel fetchDataMed(String vn,String visitdate){
		//System.out.println(vn+"*****"+visitdate);
		Vector<Vector<Object>> dataMed = new Vector<Vector<Object>>();
		Connection conn ;
		PreparedStatement stmt ;
		String query =  "select  stock_master.shortname,vnmedicine.dosecode,vnmedicine.dosetype,vnmedicine.doseunitcode,vnmedicine.doseqtycode ,vnmedicine.auxlabel1,vnmedicine.RETURNDRUGREASON from vnmedicine,stock_master where "
				+ " vnmedicine.stockcode=stock_master.stockcode and vnmedicine.vn=? and vnmedicine.visitdate=?  and vnmedicine.cxldatetime is null ";
		
		try {

			conn=new DBmanager().getConnMSSql();
	        stmt = conn.prepareStatement(query);
	        stmt.setString(1,vn.trim());
	        stmt.setString(2,visitdate.trim());
	        ResultSet rs = stmt.executeQuery();
	        int p=1;
	        while (rs.next()) {
	        	String drug="",ned="",dc="",dt="",dqc="",aux="",duc="";
	        	final Vector<Object> vstring = new Vector<Object>();
	        	vstring.add(Integer.toString(p) );

	        	if(rs.getString(1) !=null){
	        		drug=rs.getString(1).trim();
	        	}
	        	if(rs.getString(2) !=null){
	        		//dc=rs.getString(2).trim();
	        		for(int i=0;i<OPDFrame.getDoseCode().length;i++){
						if(rs.getString(2).trim().equals(OPDFrame.getDoseCode()[i][0])){
							dc=OPDFrame.getDoseCode()[i][1];
							//System.out.println(i+1+". "+oUserInfo.getDoseCode()[i][1]);
						}
						//System.out.println(i+1+". "+oUserInfo.getAuxLabel1()[i][1]);
					}
	        	}
	        	if(rs.getString(3) !=null){
	        		//dt=rs.getString(3).trim();
	        		for(int i=0;i<OPDFrame.getDoseType().length;i++){
						if(rs.getString(3).trim().equals(OPDFrame.getDoseType()[i][0])){
							dt=OPDFrame.getDoseType()[i][1];
							//System.out.println(i+1+". "+oUserInfo.getDoseType()[i][1]);
						}
						//System.out.println(i+1+". "+oUserInfo.getDoseType()[i][1]);
					}
	        	}
	        	if(rs.getString(4) !=null){
	        		//dt=rs.getString(3).trim();
	        		for(int i=0;i<OPDFrame.getDoseUnitCode().length;i++){
						if(rs.getString(4).trim().equals(OPDFrame.getDoseUnitCode()[i][0])){
							duc=OPDFrame.getDoseUnitCode()[i][1];
							//System.out.println(i+1+". "+oUserInfo.getDoseUnitCode()[i][1]);
						}
						//System.out.println(i+1+". "+oUserInfo.getDoseType()[i][1]);
					}
	        	}
	        	if(rs.getString(5) !=null){
	        		dqc=rs.getString(5).trim();
	        	}
	        	if(rs.getString(6) !=null){
	        		//aux=rs.getString(6).trim();
	        		for(int i=0;i<OPDFrame.getAuxLabel1().length;i++){
						if(rs.getString(6).trim().equals(OPDFrame.getAuxLabel1()[i][0])){
							aux=" --- "+OPDFrame.getAuxLabel1()[i][1]+" --- ";
							//System.out.println(i+1+". "+oUserInfo.getAuxLabel1()[i][1]);
						}
						//System.out.println(i+1+". "+oUserInfo.getAuxLabel1()[i][1]);
					}
	        	}
	        	if(rs.getString(7) !=null){
	        		ned="NED ["+rs.getString(7).trim()+"]";
	        	}
	        	vstring.add(false);
	            vstring.add(""+drug+"  "+dt+"  "+dqc+"  "+duc+" "+dc+"  "+aux+"  "+ned);
	            vstring.add(vn);
	        	dataMed.add(vstring);
	        	 
	        	p++;
	        	//System.out.println("*****"+drug+"/ "+dt+"/ "+dqc+"/ "+duc+"/ "+dc+"/ "+aux+"/ "+ned);
	        }
	        stmt.close();
	    	conn.close();
	    	//System.out.println(vn+"*****"+visitdate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return new DefaultTableModel(dataMed, columnNamesMed);
	}
	public void clear(){
		table.setModel(fetchDataClear());
		TableColumnModel columnModel = table.getColumnModel();		
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(220);
		columnModel.getColumn(2).setPreferredWidth(0);
		columnModel.getColumn(2).setMinWidth(0);
		columnModel.getColumn(2).setMaxWidth(0);
		((DefaultTableModel)table.getModel()).fireTableDataChanged();
	       
	}
	public  DefaultTableModel fetchDataClear(){
		 
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		return new DefaultTableModel(data, columnNames);
	}
	public void printMed() {
		 
	}

}
