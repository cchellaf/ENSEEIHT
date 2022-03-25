package allumettes;


public class Rapide implements Strategie {

	public Rapide() { }


	/** Obtenir la prise d'un joueur.
	 * @return le nombre d'allumettes prises
	 */
	public int getPrise(Jeu jeu) {
		int allumettes = Math.min(jeu.PRISE_MAX, jeu.getNombreAllumettes());
		return allumettes;
	}


	/** Obtenir la stratégie en chaine de caractères
	 * @return chaine stratégie
	 */
	public String getValeur() {
		return "rapide";
	}
}
