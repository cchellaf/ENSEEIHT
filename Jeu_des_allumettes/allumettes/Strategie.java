package allumettes;

public interface Strategie {


	/** Obtenir la prise d'un joueur.
	 * @return le nombre d'allumettes prises
	 */
	int getPrise(Jeu jeu) throws CoupInvalideException, NumberFormatException;

	/** Obtenir la stratégie en chaine de caractères
	 * @return chaine stratégie
	 */
	String getValeur();

}
