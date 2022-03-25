package allumettes;

public class JeuReel implements Jeu {

	private int nombreAllumettes;

	public JeuReel(int nombreAllumettes) {
		this.nombreAllumettes = nombreAllumettes;
	}

	/** Obtenir le nombre d'allumettes encore en jeu.
	 * @return nombre d'allumettes encore en jeu
	 */
	public int getNombreAllumettes() {
		return this.nombreAllumettes;
	}


	/** Retirer des allumettes.  Le nombre d'allumettes doit Ãªtre compris
	 * entre 1 et PRISE_MAX, dans la limite du nombre d'allumettes encore
	 * en jeu.
	 * @param nbPrises nombre d'allumettes prises.
	 * @throws CoupInvalideException tentative de prendre un nombre invalide d'alumettes
	 */
	public void retirer(int nbPrises) throws CoupInvalideException {
		if (nbPrises > this.nombreAllumettes) {
			throw new CoupInvalideException(nbPrises, "> " + this.nombreAllumettes);
		} else if (nbPrises < 1) {
			throw new CoupInvalideException(nbPrises, "< 1");
		} else if (nbPrises > PRISE_MAX) {
			throw new CoupInvalideException(nbPrises, "> " + PRISE_MAX);
		} else {
			this.nombreAllumettes -= nbPrises;
		}
	}
}
