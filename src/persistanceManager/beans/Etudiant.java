package persistanceManager.beans;
import java.util.List;

import persistanceManager.annotations.DBBean;
import persistanceManager.annotations.DBLink;
import persistanceManager.annotations.Ignore;

@DBBean(bean = "etudiant", primaryKey = "etudiantid")
public class Etudiant {
	
	@Ignore(isIgnored = false)
	public int etudiantid;
	@Ignore(isIgnored = false)
	public String fname;
	@Ignore(isIgnored = false)
	public String lname;
	@Ignore(isIgnored = false)
	public int age;
	
	@Ignore(isIgnored = false)
	@DBLink(beanName = "inscription")
	public List<Inscription> inscriptions;
	
	public int getEtudiantid() {
		return etudiantid;
	}
	public void setEtudiantid(int etudiantid) {
		this.etudiantid = etudiantid;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public List<Inscription> getInscriptions() {
		return inscriptions;
	}
	public void setInscriptions(List<Inscription> inscriptions) {
		this.inscriptions = inscriptions;
	}
	public void printEtud() {
		String out = "Etudiant\n ID : " + this.etudiantid + "; Prénom Nom : " + this.fname + " " + this.lname + "; Age : " + this.age;
		System.out.println(out);
		for(int i = 0; i < inscriptions.size(); i++) {
			System.out.print(" ");
			inscriptions.get(i).printInscrip();
		}
	}
		
}
