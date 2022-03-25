package allumettes;


public class Tricheur implements Strategie {

	public Tricheur() { }


	/** Obtenir la prise d'un joueur.
	 * @return le nombre d'allumettes prises
	 */
	public int getPrise(Jeu jeu) throws CoupInvalideException {
		int allumettes = 0;
	   	while (allumettes == 0) {
			try {
			    System.out.println("[Je triche...]");
			    int i = 1;
			    while (jeu.getNombreAllumettes() > 2) {
				    jeu.retirer(i);
			    }
			    int nombreAllumettes = jeu.getNombreAllumettes();
			    System.out.println("[Allumettes restantes : " + nombreAllumettes + "]");
			    allumettes = 1;
			} catch (CoupInvalideException e) {
				throw e;
	        }
		}
		return allumettes;
	}


	/** Obtenir la stratégie en chaine de caractères
	 * @return chaine stratégie
	 */
	public String getValeur() {
		return "tricheur";
	}
}
