package allumettes;

public class Expert implements Strategie {

	public Expert() { }


	/** Obtenir la prise d'un joueur.
	 * @return le nombre d'allumettes prises
	 */
	public int getPrise(Jeu jeu) {
		int allumettes;
		if (jeu.getNombreAllumettes() == 1) {
			allumettes = 1;
		} else if ((jeu.getNombreAllumettes() - 1) % (jeu.PRISE_MAX + 1) == 0) {
			allumettes = new java.util.Random().nextInt(jeu.PRISE_MAX) + 1;
		} else {
			allumettes = (jeu.getNombreAllumettes() - 1) % (jeu.PRISE_MAX + 1);
		}
	    return allumettes;
	}

	public String getValeur() {
		return "expert";
	}
}
