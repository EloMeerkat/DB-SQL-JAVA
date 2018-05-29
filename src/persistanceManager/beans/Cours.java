package persistanceManager.beans;
import java.util.List;

import persistanceManager.annotations.DBBean;
import persistanceManager.annotations.DBLink;
import persistanceManager.annotations.Ignore;

@DBBean(bean = "cours", primaryKey = "coursid")
public class Cours {
	
	@Ignore(isIgnored = false)
	public int coursid;
	@Ignore(isIgnored = false)
	public String name;
	@Ignore(isIgnored = false)
	public String sigle;
	@Ignore(isIgnored = false)
	public String description;
	
	@Ignore(isIgnored = false)
	@DBLink(beanName = "inscription")
	public List<Inscription> inscriptions;
	
	public int getCoursid() {
		return coursid;
	}
	public void setCoursid(int coursid) {
		this.coursid = coursid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSigle() {
		return sigle;
	}
	public void setSigle(String sigle) {
		this.sigle = sigle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Inscription> getInscriptions() {
		return inscriptions;
	}
	public void setInscriptions(List<Inscription> inscriptions) {
		this.inscriptions = inscriptions;
	}
	public void printCours() {
		String out = "Cours \n ID : " + this.coursid + "; Nom : " + this.name + "; Sigle : " + this.sigle + "; Description : " + this.description;
		System.out.println(out);
		for(int i = 0; i < inscriptions.size(); i++) {
			System.out.print(" ");
			inscriptions.get(i).printInscrip();
		}
	}
		
}
