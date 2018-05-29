package persistanceManager.beans;

import persistanceManager.annotations.DBBean;
import persistanceManager.annotations.Ignore;

@DBBean(bean = "inscription", primaryKey = "inscriptionid")
public class Inscription {

	@Ignore(isIgnored = false)
	public int inscriptionid;
	@Ignore(isIgnored = false)
	public int etudiantid;
	@Ignore(isIgnored = false)
	public int coursid;
	@Ignore(isIgnored = true)
	public Etudiant etudiant;
	@Ignore(isIgnored = true)
	public Cours cours;
	
	public int getInscriptionid() {
		return inscriptionid;
	}
	public void setInscriptionid(int inscriptionid) {
		this.inscriptionid = inscriptionid;
	}
	public int getEtudiantid() {
		return etudiantid;
	}
	public void setEtudiantid(int etudiantid) {
		this.etudiantid = etudiantid;
	}
	public int getCoursid() {
		return coursid;
	}
	public void setCoursid(int coursid) {
		this.coursid = coursid;
	}
	public Etudiant getEtudiant() {
		return etudiant;
	}
	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}
	public Cours getCours() {
		return cours;
	}
	public void setCours(Cours cours) {
		this.cours = cours;
	}
	
	public void printInscrip() {
		String out = "Inscription ID : " + this.inscriptionid + "; EtudID : " + this.etudiantid + "; CoursID : " + this.coursid;
		System.out.println(out);
	}

}