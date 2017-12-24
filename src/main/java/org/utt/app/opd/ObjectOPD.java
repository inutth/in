package org.utt.app.opd;

import java.util.Observable;

public class ObjectOPD extends Observable{
	String visit_date="",vn="",hn="",ptcid="",clinic_code_pt="",pt_name="",ptLabel="",rightcode="",memo="",memo1="",pt_age="";
	
	//
	public void setPtVisitdate(String visit_date ){	
		this.visit_date=visit_date;
		setChanged();
		notifyObservers();
	}
	public String GetPtVisitdate(){		
		return visit_date;
	}
	public void setPtVN(String vn ){		
		this.vn=vn;
		setChanged();
		notifyObservers();
	}
	public String GetPtVN(){	
		return vn;
	}
	public void setPtHN(String hn ){		
		this.hn=hn;
		setChanged();
		notifyObservers();
	}
	public String GetPtHN(){		
		return hn;
	}
	public void setPtCliniccode(String clinic_code_pt ){		
		this.clinic_code_pt=clinic_code_pt;
		setChanged();
		notifyObservers();
	} 
	public String GetPtCliniccode(){		
		return clinic_code_pt;
	}
	public void setPtName(String pt_name ){		
		this.pt_name=pt_name;
		setChanged();
		notifyObservers();
	}
	public String GetPtName(){		
		return pt_name;
	}
	public void setPtLabel(String ptLabel ){	
		this.ptLabel=ptLabel;
		setChanged();
		notifyObservers();
	} 
	public String GetPtLabel(){		
		return ptLabel;
	}
	public void setRightCode(String rightcode ){
		this.rightcode=rightcode;
		setChanged();
		notifyObservers();
	}
	public String GetRightCode(){		
		return rightcode;
	}
	public void setPtCID(String ptcid ){		
		this.ptcid=ptcid;
		setChanged();
		notifyObservers();
	}
	public String GetPtCID(){		
		return ptcid;
	}
	public void setMemo(String memo ){		
		this.memo=memo;
		setChanged();
		notifyObservers();
	}
	public String GetMemo(){		
		return memo;
	}
	public void setMemo1(String memo1 ){		
		this.memo1=memo1;
		setChanged();
		notifyObservers();
	}
	public String GetMemo1(){		
		return memo1;
	}
	public void setPtAge(String pt_age ){		
		this.pt_age=pt_age;
		setChanged();
		notifyObservers();
	}
	public String GetPtAge(){		
		return pt_age;
	}
}
