package allumettes;

public class Proxy implements Jeu {

	private Jeu jeu;

	public Proxy(Jeu jeu) {
		this.jeu = jeu;
	}

	/** Obtenir le nombre d'allumettes encore en jeu.
	 * @return nombre d'allumettes encore en jeu
	 */
	public int getNombreAllumettes() {
		return this.jeu.getNombreAllumettes();
	}

	/** Retirer des allumettes.  Le nombre d'allumettes doit Ãªtre compris
	 * entre 1 et PRISE_MAX, dans la limite du nombre d'allumettes encore
	 * en jeu.
	 * @param nbPrises nombre d'allumettes prises.
	 * @throws OperationInterditeException
	 */
	public void retirer(int nbPrises) throws CoupInvalideException {
		throw new OperationInterditeException();
	}

}
