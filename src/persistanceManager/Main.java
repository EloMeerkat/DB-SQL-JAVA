package persistanceManager;
import java.util.ArrayList;
import java.util.List;

import persistanceManager.annotations.Ignore;
import persistanceManager.beans.Cours;
import persistanceManager.beans.Etudiant;
import persistanceManager.beans.Inscription;
import persistanceManager.dbConnect.DataBase;

public class Main {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		String url = "jdbc:postgresql://localhost:5432/TP2";
		String userID = "postgres";
		String password = "zi9cM4S3";
		
		DataBase db= new DataBase(url, userID, password);

		Etudiant ah = new Etudiant();
		ah.setAge(23); ah.setEtudiantid(4157); ah.setFname("Heine"); ah.setLname("Axel");
		Etudiant tt = new Etudiant();
		tt.setAge(9); tt.setEtudiantid(2557); tt.setFname("Titi"); tt.setLname("Toto"); tt.setInscriptions(null);
		
		Inscription i1 = new Inscription();
		i1.setCoursid(8); i1.setEtudiantid(4157); i1.setInscriptionid(10);
		Inscription i2 = new Inscription();
		i2.setCoursid(1); i2.setEtudiantid(4157); i2.setInscriptionid(11);
		Inscription i3 = new Inscription();
		i3.setCoursid(2); i3.setEtudiantid(4157); i3.setInscriptionid(12);
		Inscription i4 = new Inscription();
		i4.setCoursid(3); i4.setEtudiantid(4157); i4.setInscriptionid(13);
		
		List<Inscription> listI = new ArrayList<Inscription>();
		listI.add(i1); listI.add(i2); listI.add(i3); listI.add(i4);
		ah.setInscriptions(listI);
		
		List<Etudiant> listEtud = new ArrayList<Etudiant>();
		listEtud.add(ah); listEtud.add(tt);
		
		db.bulkInsert(listEtud);
		
		List<Etudiant> listE = db.retreiveList(Etudiant.class, "");
		List<Cours> listC = db.retreiveList(Cours.class, "");
		db.closeConnection();

		for(int i = 0; i < listE.size(); i++) {
			listE.get(i).printEtud();
		}
		for(int i = 0; i < listC.size(); i++) {
			listC.get(i).printCours();
		}
	}

}
