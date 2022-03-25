package allumettes;


public class Naif implements Strategie {

	public Naif() { }


	/** Obtenir la prise d'un joueur.
	 * @return le nombre d'allumettes prises
	 */
	public int getPrise(Jeu jeu) {
		int allumettes;
		allumettes = new java.util.Random().nextInt(jeu.PRISE_MAX) + 1;
	    return allumettes;
	}


	/** Obtenir la stratégie en chaine de caractères
	 * @return chaine stratégie
	 */
	public String getValeur() {
		return "naif";
	 }
}
