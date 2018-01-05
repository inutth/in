package org.utt.app.opd;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.utt.app.InApp;
import org.utt.app.OPDFrame;
import org.utt.app.dao.DBmanager;
import org.utt.app.util.DateLabelFormatter;
import org.utt.app.util.Setup;


public class OPDPanel extends JPanel implements  Observer{
	ObjectOPD oUserInfo;
	Dimension screen;
	JTabbedPane tabbedPane;
	JSplitPane split,splitR;
	JPanel leftPanel,topMainPanel,midPanel;
	
	Vector<String> columnNames;
	JTable table;
	Vector<Vector<String>> data;
	JPanel bottomPanel;
	JButton ButtonRefresh;
	JScrollPane scrollPane;
	JRadioButton PtStatus1,PtStatus2;
	ButtonGroup PtGroup = new ButtonGroup();
	JPanel topLeftPanel,mainLeftPanel,topMainLeftPanel,pt_info,jPanel ;
	JLabel Label_Date,Label_VN,Label_HN,Label_CID,Label_NAME,Label_Memo,Label_Extra,Label_Extra1;
	JTextField TextField_VN,TextField_HN,TextField_CID;
	JDatePickerImpl picker;
	JTextArea textArea;
	String day="",month="",dateSearch="";
	String ptStatus=" and vnpres.diagoutdatetime is null";
	MainHDFSOPDPanel mainHDFSOPDPanel;
	MedOPDPanel medOPDPanel;
	OpdFormPanel opdFormPanel;
	ScanScope scanScope;
	ScanOPDCardUser scanOPDCardUser;
	/**
	 * 
	 */
	public OPDPanel(ObjectOPD oUserInfo) {
		super();
		this.oUserInfo=oUserInfo;
		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width-5, screen.height-100));
		
		mainHDFSOPDPanel =new MainHDFSOPDPanel(oUserInfo);
		opdFormPanel =new OpdFormPanel(oUserInfo);
		scanScope =new ScanScope(oUserInfo);
		medOPDPanel =new MedOPDPanel(oUserInfo);
		scanOPDCardUser = new ScanOPDCardUser(oUserInfo);
		split = new JSplitPane(split.HORIZONTAL_SPLIT);
		split.setDividerLocation((screen.width*2)/10);
		splitR = new JSplitPane(split.VERTICAL_SPLIT);
		splitR.setDividerLocation(140);
		
		setTop();
		setLeft();
		setRight();
		 
		split.setRightComponent(splitR);
		split.setLeftComponent(leftPanel);
		splitR.setTopComponent(topMainPanel);
		splitR.setBottomComponent(midPanel);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);

	    tabbedPane.addTab("<html>O<br>P<br>D<br> </html>", null, split, "OPD");
		
				
		
		add(tabbedPane, BorderLayout.CENTER);
	}
	public void update(Observable oObservable, Object oObject) {
		oUserInfo = ((ObjectOPD)oObservable); // cast
		Label_Date.setText("วันที่ "+Setup.ShowThaiDate(oUserInfo.GetPtVisitdate()));
		TextField_HN.setText(oUserInfo.GetPtHN());
		TextField_CID.setText(oUserInfo.GetPtCID());
		TextField_VN.setText(oUserInfo.GetPtVN());
		Label_NAME.setText(oUserInfo.GetPtLabel());
		
		textArea.setText(oUserInfo.GetMemo());
		Label_Extra1.setText(oUserInfo.GetMemo1());
	}
	public void setLeft(){
		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension((screen.width*2)/10, screen.height-175));
		leftPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		leftPanel.setLayout(new BorderLayout(0, 0));
		
		topLeftPanel = new JPanel();
		topLeftPanel.setLayout(new BorderLayout(0, 0));
		topLeftPanel.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		leftPanel.add(topLeftPanel, BorderLayout.NORTH);
		
		Label_Date= new JLabel("วันที่ "+Setup.ShowThaiDate(oUserInfo.GetPtVisitdate()),JLabel.CENTER);
		Label_Date.setFont(new Font("Tahoma", Font.PLAIN, 12));
		Label_Date.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		topLeftPanel.add(Label_Date, BorderLayout.NORTH);
		
		mainLeftPanel = new JPanel();
		mainLeftPanel.setPreferredSize(new Dimension((screen.width*2)/10, screen.height-405));
		mainLeftPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		mainLeftPanel.setLayout(new BorderLayout(0, 0));
		leftPanel.add(mainLeftPanel, BorderLayout.CENTER);
		
		topMainLeftPanel = new JPanel();
		mainLeftPanel.add(topMainLeftPanel, BorderLayout.NORTH);
		topMainLeftPanel.setBackground(Setup.getColor());
		topMainLeftPanel.setLayout(null);
		topMainLeftPanel.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		
		JLabel LabelListdata = new JLabel("รายชื่อผู้รับบริการ",JLabel.CENTER);
		LabelListdata.setFont(new Font("Tahoma", Font.PLAIN, 13));
		LabelListdata.setBounds(5,5,100,20);
		topMainLeftPanel.add(LabelListdata);
		
		PtStatus1 = new JRadioButton("All");
		PtStatus1.setBackground(Setup.getColor());
		PtStatus1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ptStatus="";
			}
		});
		PtGroup.add(PtStatus1);
		PtStatus1.setBounds(100, 5, 40, 25);
		topMainLeftPanel.add(PtStatus1);
		
		PtStatus2 = new JRadioButton("New");
		PtStatus2.setBackground(Setup.getColor());
		PtStatus2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ptStatus=" and vnpres.diagoutdatetime is null";
			}
		});
		PtGroup.add(PtStatus2);
		PtStatus2.setSelected(true);
		PtStatus2.setBounds(145, 5, 50, 25);
		topMainLeftPanel.add(PtStatus2);
		
		columnNames = new Vector<String>();
  		columnNames.add("ที่");
		columnNames.add("  VN ");
		columnNames.add("   รายชื่อผู้รับบริการ  ");
		for (int n = 3; n < 6; n++) {	
			columnNames.add(" ");
		}
	
		data = new Vector<Vector<String>>();
		
		DefaultTableModel model = new DefaultTableModel(data, columnNames){
			public Class getColumnClass(int column){
				return getValueAt(0, column).getClass();
			}
		};
		table = new JTable(model){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
				Component c = super.prepareRenderer(renderer, row, column);	
				
				JLabel jc = (JLabel) c;
				
				if (!isRowSelected(row)){
					c.setBackground(getBackground());
					int modelRow = convertRowIndexToModel(row);
					 
					String type = (String)getModel().getValueAt(modelRow, 5);
					 
					String status=type.trim();					  
					if ("1".equals(status)){
						c.setBackground(new Color(206,203,208));					 
		 			}
					 
				}
				return c;
			}
		};
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
		        int col = table.columnAtPoint(e.getPoint());
		        
		        clearValue();
		        
		        String select_vn=table.getValueAt(row,1).toString().trim();
		        oUserInfo.setPtVN(select_vn);
		        String name=table.getValueAt(row,2).toString().trim();
		        oUserInfo.setPtName(name);
		        String hn=table.getValueAt(row,3).toString().trim();
		        oUserInfo.setPtHN(hn);
		        String clinic_pt=table.getValueAt(row,4).toString().trim();
		        oUserInfo.setPtCliniccode(clinic_pt);
		        String inscl="";
		        //System.out.println(select_vn+"*"+hn+"*"+name+"*"+clinic_pt);
		        searchVN(select_vn,hn,clinic_pt);
		        
		        oUserInfo.setPtLabel(" ชื่อ   "+oUserInfo.GetPtName()+"     อายุ .. "+oUserInfo.GetPtAge()+"     :สิทธิ.. "+oUserInfo.GetRightCode()+"   [ NHSO   :  "+ inscl+" ]");
		        //System.out.println("**************"+oUserInfo.GetPtHN()+"------");
			}
		});
		table.setFont(new Font("Tahoma", Font.PLAIN, 13));
		table.setRowHeight(25);		
		table.setFillsViewportHeight(true);
 
		scrollPane = new JScrollPane(table);
		getData();
		
		mainLeftPanel.add(scrollPane, BorderLayout.CENTER);
		
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Setup.getColor());
		mainLeftPanel.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(null);
		 
		bottomPanel.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		ButtonRefresh = new JButton("Refresh");
		ButtonRefresh.setBackground(Setup.getColor());
		ButtonRefresh.setForeground(UIManager.getColor("Button.darkShadow"));

		ButtonRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getData();
				clearValue();
			}
		});
		ButtonRefresh.setBounds((((screen.width*2)/10)/2-45), 2, 90, 25);
		bottomPanel.add(ButtonRefresh);
	}
	public void setTop(){
		topMainPanel = new JPanel();
		topMainPanel.setLayout(new BorderLayout(0, 0));
		topMainPanel.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10), 140));
		topMainPanel.setBackground(Setup.getColor());  
			
		pt_info = new JPanel();		 
		pt_info.setLayout(null);
		pt_info.setBackground(Setup.getColor());
		topMainPanel.add(pt_info, BorderLayout.CENTER);
		
		JLabel Label_Date = new JLabel("วันที่");
		Label_Date.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Date.setBounds(5,12,30,20);
		pt_info.add(Label_Date);
		
		UtilDateModel model = new UtilDateModel();
		model.setSelected(true);
		Properties p = new Properties();
		p.put("text.today", "");
		p.put("text.month", "");
		p.put("text.year", "");
		JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
		picker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		picker.setBackground(Setup.getColor());
		picker.getJFormattedTextField().setBackground(Setup.getColor());
		picker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(picker.getModel().getDay()<10){
					day="0"+picker.getModel().getDay();
				}else{
					day=""+picker.getModel().getDay();
				}
				if(picker.getModel().getMonth()+1<10){
					month="0"+(picker.getModel().getMonth()+1);
				}else{
					month=""+(picker.getModel().getMonth()+1);
				}
				dateSearch=picker.getModel().getYear()+"-"+month+"-"+day;
				oUserInfo.setPtVisitdate(dateSearch.trim());
				getDataClear();
			}
		});
		picker.setShowYearButtons(true);
		picker.setTextEditable(false);
		 
		jPanel = new JPanel();
		jPanel.setBounds(30, 4, 220, 30);
		jPanel.setBackground(Setup.getColor());
		jPanel.add((JComponent)picker);
		pt_info.add(jPanel);
		
		if(picker.getModel().getDay()<10){
			day="0"+picker.getModel().getDay();
		}else{
			day=""+picker.getModel().getDay();
		}
		if(picker.getModel().getMonth()+1<10){
			month="0"+(picker.getModel().getMonth()+1);
		}else{
			month=""+(picker.getModel().getMonth()+1);
		}			
		dateSearch=picker.getModel().getYear()+"-"+month+"-"+day;
		oUserInfo.setPtVisitdate(dateSearch.trim());
		Label_VN = new JLabel("VN");
		Label_VN.setFont(new Font("Tahoma", Font.PLAIN, 13));
		pt_info.add(Label_VN);
		Label_VN.setBounds(270, 12, 20, 14);
		
		TextField_VN = new JTextField();
		//TextField_VN.setEditable(false);
		pt_info.add(TextField_VN);
		TextField_VN.setBounds(290, 10, 80, 20);
		TextField_VN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//clearValue();
				if(TextField_VN.getText().trim().length()==2){
					oUserInfo.setPtVN("0"+TextField_VN.getText());
				}else if(TextField_VN.getText().trim().length()==1){
					oUserInfo.setPtVN("00"+TextField_VN.getText());
					
				}else{
					oUserInfo.setPtVN(TextField_VN.getText().trim());
				}
				
				//System.out.println("a  "+TextField_VN.getText().trim()+"--"+oUserInfo.GetPtVN());
				oUserInfo.setPtHN("");
				oUserInfo.setPtCID("");
				
				oUserInfo.setPtName("");
				oUserInfo.setRightCode("");        
		        oUserInfo.setPtCliniccode("");         
		        oUserInfo.setPtAge("");
		        oUserInfo.setPtLabel("");              
		        oUserInfo.setMemo("");
		        oUserInfo.setMemo1("");
				searchPtvn(oUserInfo.GetPtVN());
				mainHDFSOPDPanel.clear();
		       // mainOldHDFSOPDPanel.clear();
			}
		});
		
		Label_HN = new JLabel("HN");
		Label_HN.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_HN.setBounds(390, 12, 20, 14);
		pt_info.add(Label_HN);
		
		TextField_HN = new JTextField();
		//TextField_HN.setEditable(false);
		TextField_HN.setFont(new Font("Tahoma", Font.PLAIN, 12));
		TextField_HN.setText("");
		TextField_HN.setBounds(410, 10, 80, 20);
		pt_info.add(TextField_HN);
		TextField_HN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!Setup.checkEmptyTextField(TextField_HN) && TextField_HN.getText().trim().length()==7){
					
					oUserInfo.setPtHN(TextField_HN.getText().trim());
					//System.out.println("bb  "+TextField_HN.getText().trim()+"--"+oUserInfo.GetPtHN());
				}
				 
				
				oUserInfo.setPtVN("");
				oUserInfo.setPtCID("");
				
				oUserInfo.setPtName("");
				oUserInfo.setRightCode("");        
		        oUserInfo.setPtCliniccode("");         
		        oUserInfo.setPtAge("");
		        oUserInfo.setPtLabel("");              
		        oUserInfo.setMemo("");
		        oUserInfo.setMemo1("");
				searchPthn(oUserInfo.GetPtHN());
				mainHDFSOPDPanel.clear();
		        //mainOldHDFSOPDPanel.clear();
				
				 
			}
		});
		
		Label_CID = new JLabel("ID");
		Label_CID.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_CID.setBounds(510, 12, 20, 14);
		pt_info.add(Label_CID);
		
		TextField_CID = new JTextField();
		TextField_CID.setFont(new Font("Tahoma", Font.PLAIN, 12));
		TextField_CID.setBounds(530, 10, 150, 20);
		TextField_CID.setText("");
		pt_info.add(TextField_CID);
		TextField_CID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 

			}
		});
		
		Label_NAME = new JLabel(" ชื่อ  ");
		Label_NAME.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Label_NAME.setBounds(4, 30, 970, 30);
		pt_info.add(Label_NAME);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setForeground(new Color(255, 0, 0));
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textArea.setBackground(Setup.getColor());
		textArea.setBounds(70, 70, 900, 40);
		pt_info.add(textArea);
		
		Label_Memo = new JLabel("การแพ้ยา ");
		Label_Memo.setForeground(new Color(255, 0, 0));
		Label_Memo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Memo.setBackground(Setup.getColor());
		Label_Memo.setBounds(10, 65, 55, 30);
		pt_info.add(Label_Memo);
		
		Label_Extra = new JLabel("ข้อมูลเพิ่มเติม");
		Label_Extra.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Setup.SetUnderline(Label_Extra);
		Label_Extra.setBounds(10, 110, 80, 30);
		Label_Extra.setBackground(Setup.getColor());
		pt_info.add(Label_Extra);
		
		Label_Extra1 = new JLabel("");
		Label_Extra1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Setup.SetUnderline(Label_Extra1);
		Label_Extra1.setBounds(100, 110, 400, 30);
		Label_Extra1.setBackground(Setup.getColor());
		pt_info.add(Label_Extra1);
	}
	public void setRight(){
		midPanel = new JPanel();
		midPanel.setLayout(new BorderLayout(0, 0));
		midPanel.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10), screen.height-320));
		//mainPanel.add(midPanel, BorderLayout.CENTER);
		
		tabbedPane = new JTabbedPane();
        //tabbedPane.setBackground(new Color(232, 248, 245  ));
        midPanel.add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.addTab("<html><b>OPD FORM</b></html> " ,new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")),opdFormPanel);
        tabbedPane.addTab("<html><b>EMR</b></html> ",new ImageIcon(getClass().getClassLoader().getResource("images/running.png")) ,mainHDFSOPDPanel);
        //tabbedPane.addTab("<html><b>Old EMR</b></html> ",new ImageIcon(InApp.class.getResource("/org/utt/app/gfx/box.png")) ,mainOldHDFSOPDPanel);
        //tabbedPane.addTab("<html><b>SCAN</b></html> " ,new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")),scanScope);
   
        tabbedPane.addTab("<html><b>MED</b></html> " ,new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")),medOPDPanel);
        tabbedPane.addTab("<html><b>SCAN OPD</b></html> " ,new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")),scanOPDCardUser);
        
        if(InApp.usertype.trim().equals("83")) {
             tabbedPane.addTab("<html><b>SCAN</b></html> " ,new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")),scanScope);

        }
	}
	public void getData(){
		table.setModel(fetchDataOPD());
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(40 );
		columnModel.getColumn(2).setPreferredWidth((screen.width*2)/10-80);
		for (int n = 2; n < 5; n++) {	
			columnModel.getColumn(n+1).setPreferredWidth(0);
			columnModel.getColumn(n+1).setMinWidth(0);
			columnModel.getColumn(n+1).setMaxWidth(0);
		}
		((DefaultTableModel)table.getModel()).fireTableDataChanged();
	}
	public  DefaultTableModel fetchDataOPD(){
		String[] parts=null;
		parts = InApp.userappcode.split("\\,");
		String sql_user1="",sql_user="";
		if(parts.length==1){
			for(int i=0;i<parts.length;i++){
				sql_user1=" and vnpres.clinic='"+parts[i]+"'";
			}
			sql_user=sql_user1;
		}
		else if(parts.length==0){
			sql_user="";
		}
		else if(parts.length>1){
			sql_user1="and vnpres.clinic in (";
			for(int i=0;i<parts.length;i++){
				sql_user1+="'"+parts[i]+"',";				 
			}
			sql_user=sql_user1.substring(0,sql_user1.length()-1)+")";
			 
		}

		Connection conn;
		PreparedStatement stmt,stmt1;
		ResultSet rs,rs1;
		String query="select  vnmst.vn, vnmst.hn, vnpres.diagoutdatetime ,vnpres.clinic from vnmst,vnpres where vnmst.vn=vnpres.vn and vnmst.visitdate=vnpres.visitdate and vnmst.visitdate=?   "+ptStatus+" "+sql_user+"  order by case IsNumeric(vnmst.vn)  when 1 then Replicate('0', 100 - Len(vnmst.vn)) + vnmst.vn else vnmst.vn end "; 
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		try {
			conn=new DBmanager().getConnMSSql();
			stmt = conn.prepareStatement(query);
			stmt.setString(1, Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())); 
			rs = stmt.executeQuery();
			int p=1;
			while (rs.next()) {
			
				String clinic_pt="";
				if(rs.getString(4)!=null){
					clinic_pt=rs.getString(4);
				}
				String pt_status="0";
				if(rs.getString(3)!=null){
					pt_status="1";
				}
				final Vector<String> vstring = new Vector<String>();
				vstring.add(" "+Integer.toString(p) );
				//vstring.add(rs.getString(1)+": "+initname+" "+rs.getString(2).substring(1)+" "+rs.getString(3).substring(1));
				vstring.add(rs.getString(1));
				vstring.add(getName(rs.getString(2).trim()));
				vstring.add(rs.getString(2).trim());
				vstring.add(clinic_pt);
				vstring.add(pt_status);
				 
				data.add(vstring);
				p++;
			}
		
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DefaultTableModel(data, columnNames);
	}
	public String getName(String hn){
		String name_to="",initname="";
		Connection conn;
		PreparedStatement stmt,stmt1,stmt2,stmt3;
		ResultSet rs,rs1,rs2,rs3;
		
		String query_db="select  BirthDateTime,sex,maritalstatus,initialnamecode from patient_info where hn='"+hn.trim()+"'" ; 
		String query_name="select firstname, lastname from patient_name where  suffix='0'  and hn='"+hn.trim()+"'" ; 
		
		conn=new DBmanager().getConnMSSql();
		 
		try {
			stmt = conn.prepareStatement(query_db);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String sex_pt="",mar_pt="";
				Date birthday=rs.getDate(1);
				String age_pt=Setup.AgeInAll(birthday.toString()).trim();
				if(rs.getString(4)==null || rs.getString(4).equals("1") || rs.getString(4).equals("2") || rs.getString(4).equals("3") || rs.getString(4).equals("127") || rs.getString(4).equals("128")){
					sex_pt=rs.getString(2).trim();
					mar_pt=rs.getString(3).trim();				 
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
					/**
					for(int i=0;i<oUserInfo.getInitName().length;i++){
						if(rs.getString(4).trim().equals(oUserInfo.getInitName()[i][0])){
							initname=oUserInfo.getInitName()[i][1];
							break;
						}
						//System.out.println(i+1+". "+labunitname[i][0]);
					}
					*/
					for(int i=0;i<InApp.initname_indb.length;i++){
						if(rs.getString(4).trim().equals(InApp.initname_indb[i][0])){
							initname=InApp.initname_indb[i][1];
							break;
						}
						//System.out.println(i+1+". "+labunitname[i][0]);
					}
					//initname=rs.getString(10).trim();
				}	
				
			}
			stmt.close();
			//

			stmt2 = conn.prepareStatement(query_name);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {			
				if(rs2.getString(1) !=null){
					name_to=" "+initname+" "+rs2.getString(1).substring(1).trim()+" "+rs2.getString(2).substring(1).trim();
				}
							
			}
			stmt2.close();
			//
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name_to;
	}
	public void searchPtvn(String vn) {
		String[] parts=null;
		parts = InApp.userappcode.split("\\,");
		String sql_user1="",sql_user="";
		if(parts.length==1){
			for(int i=0;i<parts.length;i++){
				sql_user1=" and vnpres.clinic='"+parts[i]+"'";
			}
			sql_user=sql_user1;
		}
		else if(parts.length==0){
			sql_user="";
		}
		else if(parts.length>1){
			sql_user1="and vnpres.clinic in (";
			for(int i=0;i<parts.length;i++){
				sql_user1+="'"+parts[i]+"',";				 
			}
			sql_user=sql_user1.substring(0,sql_user1.length()-1)+")";
			 
		}
		if(!Setup.checkEmptyTextField(TextField_VN)){
			int p=0;
			Connection conn;
			PreparedStatement stmt,stmt1;
			ResultSet rs,rs1;
			String query="select  vnmst.vn, vnmst.hn, vnpres.diagoutdatetime ,vnpres.clinic from vnmst,vnpres where vnmst.vn=vnpres.vn and vnmst.visitdate=vnpres.visitdate and vnmst.visitdate=?  and vnpres.vn=?  "+sql_user+" "; 
			Vector<Vector<String>> data = new Vector<Vector<String>>();
			try {
				conn=new DBmanager().getConnMSSql();
				stmt = conn.prepareStatement(query);
				stmt.setString(1, Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())); 
				stmt.setString(2, vn.trim());
				rs = stmt.executeQuery();
				 
				while (rs.next()) {
					oUserInfo.setPtHN(rs.getString(2).trim());
					String res=getClinic(vn,Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())).trim();
			    	//System.out.println("res  --- "+res+"--"+res.substring(0, res.indexOf(":")));
			    	if(res.substring(0, res.indexOf(":")).equals("1")){
			    		searchVN(vn,(rs.getString(2)).trim(),res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    		oUserInfo.setPtCliniccode(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    	}
			    	else if(res.substring(0, res.indexOf(":")).equals("0")){
			    		
			    	}
			    	else{
			    		//int npage=Integer.parseInt(res.substring(0, res.indexOf(":")));
			    		String text=res.substring(res.indexOf(":")+1);
			    		String[] parts1 = text.split("-");
			    		//String fn_[] = new String[npage];
			    		 

			    		String selected=(String)JOptionPane.showInputDialog(null, "กรุณาเลือกห้องตรวจ", "Clinic", JOptionPane.QUESTION_MESSAGE,
			    	        null, parts1, "");
			    		searchVN(vn,(rs.getString(2)).trim(),selected);
			    		oUserInfo.setPtCliniccode(selected);
			    		//System.out.println(selected);
			    	}
			    	String name=getName(rs.getString(2).trim());
			    	oUserInfo.setPtName(name);

				    String inscl="";
				        
				    oUserInfo.setPtLabel(" ชื่อ   "+name+"     อายุ .. "+oUserInfo.GetPtAge()+"     :สิทธิ.. "+oUserInfo.GetRightCode()+"   [ NHSO   :  "+ inscl+" ]");
				    p++;  
				}
			
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(p==0) {
				clearValue();
				JOptionPane.showMessageDialog(null,"ไม่พบ vn ในห้องตรวจนี้","VN Error",JOptionPane.ERROR_MESSAGE);
				
			}
		}
		else if(!Setup.checkEmptyTextField(TextField_VN) && TextField_VN.getText().trim().length()==21){
			String clinic_in=TextField_VN.getText().trim().substring(8,12);
			String hn_in=TextField_VN.getText().trim().substring(14);
			String visitdate_in=Setup.DateInDBMSSQLno(TextField_VN.getText().trim().substring(0,8));
			
			oUserInfo.setPtHN(hn_in);
			
			int p=0;
			Connection conn;
			PreparedStatement stmt,stmt1;
			ResultSet rs,rs1;
			String query="select  vnmst.vn, vnmst.hn, vnpres.diagoutdatetime ,vnpres.clinic from vnmst,vnpres where vnmst.vn=vnpres.vn and vnmst.visitdate=vnpres.visitdate and vnmst.visitdate=?  and vnmat.hn=?  "+sql_user+" "; 
			Vector<Vector<String>> data = new Vector<Vector<String>>();
			try {
				conn=new DBmanager().getConnMSSql();
				stmt = conn.prepareStatement(query);
				stmt.setString(1, Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())); 
				stmt.setString(2,hn_in.trim());
				rs = stmt.executeQuery();
				 
				while (rs.next()) {
					oUserInfo.setPtVN(rs.getString(1).trim());
					String res=getClinic(rs.getString(1).trim(),Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())).trim();
			    	//System.out.println("res  --- "+res+"--"+res.substring(0, res.indexOf(":")));
			    	if(res.substring(0, res.indexOf(":")).equals("1")){
			    		searchVN(rs.getString(1).trim(),(rs.getString(2)).trim(),res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    		oUserInfo.setPtCliniccode(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    	}
			    	else if(res.substring(0, res.indexOf(":")).equals("0")){
			    		
			    	}
			    	else{
			    		//int npage=Integer.parseInt(res.substring(0, res.indexOf(":")));
			    		String text=res.substring(res.indexOf(":")+1);
			    		String[] parts1 = text.split("-");
			    		//String fn_[] = new String[npage];
			    		 

			    		String selected=(String)JOptionPane.showInputDialog(null, "กรุณาเลือกห้องตรวจ", "Clinic", JOptionPane.QUESTION_MESSAGE,
			    	        null, parts1, "");
			    		searchVN(rs.getString(1).trim(),(rs.getString(2)).trim(),selected);
			    		oUserInfo.setPtCliniccode(selected);
			    		//System.out.println(selected);
			    	}
			    	String name=getName(rs.getString(2).trim());
			    	oUserInfo.setPtName(name);

				    String inscl="";
				        
				    oUserInfo.setPtLabel(" ชื่อ   "+name+"     อายุ .. "+oUserInfo.GetPtAge()+"     :สิทธิ.. "+oUserInfo.GetRightCode()+"   [ NHSO   :  "+ inscl+" ]");
				    p++;  
				}
			
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(p==0) {
				clearValue();
				JOptionPane.showMessageDialog(null,"ไม่พบ vn ในห้องตรวจนี้","VN Error",JOptionPane.ERROR_MESSAGE);
				
			}
		}
		else if(!Setup.checkEmptyTextField(TextField_VN) && TextField_VN.getText().trim().length()==25){
			String clinic_in=TextField_VN.getText().trim().substring(8,12);
			String hn_in=TextField_VN.getText().trim().substring(18);
			String visitdate_in=Setup.DateInDBMSSQLno(TextField_VN.getText().trim().substring(0,8));
			
			oUserInfo.setPtHN(hn_in);
			
			int p=0;
			Connection conn;
			PreparedStatement stmt,stmt1;
			ResultSet rs,rs1;
			String query="select  vnmst.vn, vnmst.hn, vnpres.diagoutdatetime ,vnpres.clinic from vnmst,vnpres where vnmst.vn=vnpres.vn and vnmst.visitdate=vnpres.visitdate and vnmst.visitdate=?  and vnmat.hn=?  and vnpres.clinic=? "; 
			Vector<Vector<String>> data = new Vector<Vector<String>>();
			try {
				conn=new DBmanager().getConnMSSql();
				stmt = conn.prepareStatement(query);
				stmt.setString(1, visitdate_in); 
				stmt.setString(2,hn_in.trim());
				stmt.setString(3,clinic_in.trim());
				rs = stmt.executeQuery();
				 
				while (rs.next()) {
					oUserInfo.setPtVN(rs.getString(1).trim());
					String res=getClinic(rs.getString(1).trim(),Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())).trim();
			    	//System.out.println("res  --- "+res+"--"+res.substring(0, res.indexOf(":")));
			    	if(res.substring(0, res.indexOf(":")).equals("1")){
			    		searchVN(rs.getString(1).trim(),(rs.getString(2)).trim(),res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    		oUserInfo.setPtCliniccode(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    	}
			    	else if(res.substring(0, res.indexOf(":")).equals("0")){
			    		
			    	}
			    	else{
			    		//int npage=Integer.parseInt(res.substring(0, res.indexOf(":")));
			    		String text=res.substring(res.indexOf(":")+1);
			    		String[] parts1 = text.split("-");
			    		//String fn_[] = new String[npage];
			    		 

			    		String selected=(String)JOptionPane.showInputDialog(null, "กรุณาเลือกห้องตรวจ", "Clinic", JOptionPane.QUESTION_MESSAGE,
			    	        null, parts1, "");
			    		searchVN(rs.getString(1).trim(),(rs.getString(2)).trim(),selected);
			    		oUserInfo.setPtCliniccode(selected);
			    		//System.out.println(selected);
			    	}
			    	String name=getName(rs.getString(2).trim());
			    	oUserInfo.setPtName(name);

				    String inscl="";
				        
				    oUserInfo.setPtLabel(" ชื่อ   "+name+"     อายุ .. "+oUserInfo.GetPtAge()+"     :สิทธิ.. "+oUserInfo.GetRightCode()+"   [ NHSO   :  "+ inscl+" ]");
				    p++;  
				}
			
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(p==0) {
				clearValue();
				JOptionPane.showMessageDialog(null,"ไม่พบ vn ในห้องตรวจนี้","VN Error",JOptionPane.ERROR_MESSAGE);
				
			}
		}
	}
	public void searchPthn(String hn_in){
		String[] parts=null;
		parts = InApp.userappcode.split("\\,");
		String sql_user1="",sql_user="";
		if(parts.length==1){
			for(int i=0;i<parts.length;i++){
				sql_user1=" and vnpres.clinic='"+parts[i]+"'";
			}
			sql_user=sql_user1;
		}
		else if(parts.length==0){
			sql_user="";
		}
		else if(parts.length>1){
			sql_user1="and vnpres.clinic in (";
			for(int i=0;i<parts.length;i++){
				sql_user1+="'"+parts[i]+"',";				 
			}
			sql_user=sql_user1.substring(0,sql_user1.length()-1)+")";
			 
		}
		if(!Setup.checkEmptyTextField(TextField_HN) && TextField_HN.getText().trim().length()==7){
			
			int p=0;
			Connection conn;
			PreparedStatement stmt,stmt1;
			ResultSet rs,rs1;
			String query="select  vnmst.vn, vnmst.hn, vnpres.diagoutdatetime ,vnpres.clinic from vnmst,vnpres where vnmst.vn=vnpres.vn and vnmst.visitdate=vnpres.visitdate and vnmst.visitdate=?  and vnmst.hn=?  "+sql_user+" "; 
			Vector<Vector<String>> data = new Vector<Vector<String>>();
			try {
				conn=new DBmanager().getConnMSSql();
				stmt = conn.prepareStatement(query);
				stmt.setString(1, Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())); 
				stmt.setString(2, hn_in.trim());
				rs = stmt.executeQuery();
				 
				while (rs.next()) {
					oUserInfo.setPtVN(rs.getString(1).trim());
					String res=getClinic(rs.getString(1).trim(),Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())).trim();
			    	//System.out.println("res  --- "+res+"--"+res.substring(0, res.indexOf(":")));
			    	if(res.substring(0, res.indexOf(":")).equals("1")){
			    		searchVN(rs.getString(1).trim(),(rs.getString(2)).trim(),res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    		oUserInfo.setPtCliniccode(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    	}
			    	else if(res.substring(0, res.indexOf(":")).equals("0")){
			    		
			    	}
			    	else{
			    		//int npage=Integer.parseInt(res.substring(0, res.indexOf(":")));
			    		String text=res.substring(res.indexOf(":")+1);
			    		String[] parts1 = text.split("-");
			    		//String fn_[] = new String[npage];
			    		 

			    		String selected=(String)JOptionPane.showInputDialog(null, "กรุณาเลือกห้องตรวจ", "Clinic", JOptionPane.QUESTION_MESSAGE,
			    	        null, parts1, "");
			    		searchVN(rs.getString(1).trim(),(rs.getString(2)).trim(),selected);
			    		oUserInfo.setPtCliniccode(selected);
			    		//System.out.println(selected);
			    	}
			    	String name=getName(rs.getString(2).trim());
			    	oUserInfo.setPtName(name);

				    String inscl="";
				        
				    oUserInfo.setPtLabel(" ชื่อ   "+name+"     อายุ .. "+oUserInfo.GetPtAge()+"     :สิทธิ.. "+oUserInfo.GetRightCode()+"   [ NHSO   :  "+ inscl+" ]");
				    p++;  
				}
			
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(p==0) {
				clearValue();
				JOptionPane.showMessageDialog(null,"ไม่พบ vn ในห้องตรวจนี้","VN Error",JOptionPane.ERROR_MESSAGE);
				
			}
		}
	}
	public void searchVN(String vn,String hn,String clinic){
		Connection conn;
		PreparedStatement stmt,stmt1,stmt2,stmt3;
		ResultSet rs,rs1,rs2,rs3;
		
		String query_db="select  BirthDateTime from patient_info where hn='"+hn.trim()+"'" ; 
		String query_right="select  rightcode from vnpres where visitdate=? and clinic='"+clinic+"'   and vn='"+vn.trim()+"'" ; 
		String query_ref="select  ref from patient_ref where  reftype='01'  and hn='"+hn.trim()+"'" ; 
		
		conn=new DBmanager().getConnMSSql();
		 
		try {
			stmt = conn.prepareStatement(query_db);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String  right="";
				if(rs.getString(1) !=null){
					Date birthday=rs.getDate(1);
					String age_pt=Setup.AgeInAll(birthday.toString()).trim();
					oUserInfo.setPtAge(age_pt);
				}		
				
			}
			stmt.close();
			//
			stmt1 = conn.prepareStatement(query_right);
			stmt1.setString(1, Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()));
			rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				String  right="";				
				if(rs1.getString(1) !=null){
					for(int i=0;i<InApp.rightname.length;i++){
						if(rs1.getString(1).trim().equals(InApp.rightname[i][0])){
							right=InApp.rightname[i][1];
							break;
						}
						//System.out.println(i+1+". "+labunitname[i][0]);
					}
					oUserInfo.setRightCode(right+"("+rs1.getString(1).trim()+")");
				}
							
			}
			stmt1.close();
			//
			stmt2 = conn.prepareStatement(query_ref);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {			
				if(rs2.getString(1) !=null){
					oUserInfo.setPtCID(rs2.getString(1).trim());
				}
							
			}
			stmt2.close();
			//
			String sql_allergy="select memo from view_MEDICINE_ALLERGIC where hn=? and suffix='8'";
			stmt3 = conn.prepareStatement(sql_allergy);
			stmt3.setString(1, hn);
			rs3 = stmt3.executeQuery();
			String memo1="",memo2="";
			//String memo="";
			while (rs3.next()) {
				if(rs3.getString(1)!=null){
					 memo1=rs3.getString(1).trim();
				}
			}
			rs3.close();
			stmt3.close();
			
			for(int i=0;i<OPDFrame.hn_z515.length;i++){
				if(hn.equals(OPDFrame.hn_z515[i])){
					memo2="Z515";
					break;
				}
				//System.out.println(i+1+". "+labunitname[i][0]);
			}
			
			 
			oUserInfo.setMemo(memo1);
			oUserInfo.setMemo1(memo2);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getDataClear(){
		table.setModel(fetchDataOPDClear());
		//table.setBackground(new Color(229, 234, 243  ));
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(40 );
		columnModel.getColumn(2).setPreferredWidth((screen.width*2)/10-80);
		for (int n = 2; n < 5; n++) {	
			columnModel.getColumn(n+1).setPreferredWidth(0);
			columnModel.getColumn(n+1).setMinWidth(0);
			columnModel.getColumn(n+1).setMaxWidth(0);
		}
		((DefaultTableModel)table.getModel()).fireTableDataChanged();
	}
	public  DefaultTableModel fetchDataOPDClear(){

		Vector<Vector<String>> data1 = new Vector<Vector<String>>();
	
		return new DefaultTableModel(data1, columnNames);
	}
	public String getClinic(String vn_in,String fndate){
		String clinic="";
		int p=0;
		try{
	    	String sql_clinic="select  clinic from vnpres where vn=? and visitdate= '"+fndate+"'  order by clinic";
			Connection conn=new DBmanager().getConnMSSql();
			PreparedStatement stmt = conn.prepareStatement(sql_clinic);
			stmt.setString(1, vn_in);
			 
			ResultSet rs = stmt.executeQuery();
			 
			while (rs.next()) {
				if(rs.getString(1)!=null){
					 
					clinic+=rs.getString(1).trim()+"-";
				}
				p++;
			}
	    	stmt.close();
	    	conn.close();
	    	 
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p+":"+clinic;
	}
	public void clearValue(){
        oUserInfo.setPtVN("");  
       // oUserInfo.setFN("");
        oUserInfo.setPtName("");        
        oUserInfo.setPtHN("");        
        oUserInfo.setRightCode("");        
        oUserInfo.setPtCliniccode("");         
        oUserInfo.setPtAge("");
        oUserInfo.setPtLabel("");       
        oUserInfo.setPtCID("");         
        oUserInfo.setMemo("");
        oUserInfo.setMemo1("");
        //oUserInfo.setFormReport("");
        mainHDFSOPDPanel.clear();
        //mainOldHDFSOPDPanel.clear();
       // medOPDPanel.clear();		
	}

}
