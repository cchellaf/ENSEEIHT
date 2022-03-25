package allumettes;
import java.util.Scanner;


public class Humain implements Strategie {

	private Scanner sc;
    private String nom;


	public Humain(Scanner sc, String nom) {
		this.sc = sc;
		this.nom = nom;
	}

	/** Obtenir la prise d'un joueur.
	 * @return le nombre d'allumettes prises
	 */
	public int getPrise(Jeu jeu) throws CoupInvalideException, NumberFormatException {
		int allumettes = 0;
		boolean sortie = false;
		while (!sortie) {
			System.out.print(this.nom + ", combien d'allumettes ? ");
			String entree = this.sc.nextLine();
			if (entree.equals("triche")) {
			    jeu.retirer(1);
			    int nbAllumettes = jeu.getNombreAllumettes();
			    System.out.println("[Une allumette en moins, plus que " + nbAllumettes + ". Chut !]");
		    } else {
		    	try {
				    allumettes = Integer.parseInt(entree);
				    sortie = true;
				} catch (NumberFormatException f) {
					System.out.println("Vous devez donner un entier.");
			    }
		    }
		}
		return allumettes;

	}


	/** Obtenir la stratégie en chaine de caractères
	 * @return chaine stratégie
	 */
	public String getValeur() {
		return "humain";
	}

}
