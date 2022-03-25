package allumettes;


public class Joueur {

	private String nom;           // Nom du joueur
	private Strategie strategie;  // Stratégie du joueur


	/** Modéliser un joueur à partir de son nom et sa stratégie.
	 * @param nom nom du joueur
	 * @param strategie stratégie du joueur
	 */
	public Joueur(String nom, Strategie strategie) {
		this.nom = nom;
		this.strategie = strategie;
	}

	/** Obtenir le nom du joueur.
	 * @return nom du joueur
	 */
	public String getNom() {
		return this.nom;
	}

	/** Obtenir la stratégie du joueur.
	 * @return strategie du joueur
	 */
	public Strategie getStrategie() {
		return this.strategie;
	}

	/** initialiser la stratégie du joueur.
	 * @param strategie strategie que le joueur va suivre
	 */
	public void setStrategie(Strategie strategie) {
		this.strategie = strategie;
	}


	/** Demander au joueur le nombre d'allumettes qu'il veut prendre pour un jeu donné
	 * @param jeu jeu
	 * @return nombre d'allumettes que le joueur a pris
	 */
	public int getPrise(Jeu jeu) throws CoupInvalideException, NumberFormatException {
		return this.strategie.getPrise(jeu);
	}


}

