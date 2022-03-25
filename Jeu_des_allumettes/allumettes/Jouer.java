package allumettes;
import java.util.Scanner;


/** Lance une partie des 13 allumettes en fonction des arguments fournis
 * sur la ligne de commande.
 * @author	Xavier Crégut
 * @version	$Revision: 1.5 $
 */
public class Jouer {

	public static final int NB_ALLUMETTES = 13;
	private static Scanner sc = new Scanner(System.in);

	/** Obtenir la stratégie du joueur
	 * @param strategie stratégie du joueur en chaine de caractères
	 * @param nom nom du joueur
	 * @return strategie stratégie du joueur
	 */
	public static Strategie getStrategie(String strategie, String nom) {
		switch (strategie) {
		case "humain":
			return new Humain(sc, nom);
		case "naif" :
			return new Naif();
		case "rapide" :
			return new Rapide();
		case "expert" :
			return new Expert();
		case "tricheur" :
			return new Tricheur();
		default :
			System.out.println("La stratégie humaine est exécutée par défaut !");
			return new Humain(sc, nom);
		}
	}

	/** Obtenir le nom du joueur à partir du tableau
	 * contenant les arguments de la ligne de commande.
	 * @param tableau tableau qui contient les arguments de la ligne de commande
	 * @return nom nom du joueur
	 */
	private static String nom(String[] tableau) {
		String nom = "";
		for (int i = 1; i < tableau.length; i++) {
			if (tableau[i].equals("humain")) {
				nom = tableau[i - 1];
			}
		}
		return nom;
	}
	/** Traiter les arguments de la ligne de commande.
	 * En argument sont donnés les deux joueurs sous la forme nom@stratégie.
	 * @param args la description des deux joueurs.
	 * @return tableau tableau contenant les arguments de la ligne de commande.
	 */
	private static String[] traiterLignecmd(String[] args) throws ArrayIndexOutOfBoundsException {
		String word;
		String word1;
		String word2;
		String[] tableau;
		if (args.length > 2) {
			word1 = args[1];
			word2 = args[2];

		} else {
			word1 = args[0];
			word2 = args[1];
		}
		if (word1.contains("@") && word2.contains("@")) {
			word = word1 + "@" + word2;
			tableau = word.split("@");
			return tableau;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/** Lancer une partie. En argument sont donnés les deux joueurs sous
	 * la forme nom@stratégie.
	 * @param args la description des deux joueurs
	 */
	public static void main(String[] args) {
		try {
			verifierNombreArguments(args);
			String[] tableau = traiterLignecmd(args);
			String[] tab1 = {tableau[0], tableau[1]};
			String[] tab2 = {tableau[2], tableau[3]};
			boolean confiant = args[0].contentEquals("-confiant");
			Jeu jeu = new JeuReel(NB_ALLUMETTES);
			Joueur j1 = new Joueur(tableau[0], getStrategie(tableau[1], nom(tab1)));
			Joueur j2 = new Joueur(tableau[2], getStrategie(tableau[3], nom(tab2)));
			Arbitre arbitre = new Arbitre(j1, j2, confiant);
			arbitre.arbitrer(jeu);

		} catch (ConfigurationException e) {
			System.out.println();
			System.out.println("Erreur : " + e.getMessage());
			afficherUsage();
			System.exit(1);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Saisir le nom et la stratégie sous la forme : nom@strategie");
		}
	}


	private static void verifierNombreArguments(String[] args) {
		final int nbJoueurs = 2;
		if (args.length < nbJoueurs) {
			throw new ConfigurationException("Trop peu d'arguments : "
					+ args.length);
		}
		if (args.length > nbJoueurs + 1) {
			throw new ConfigurationException("Trop d'arguments : "
					+ args.length);
		}
	}

	/** Afficher des indications sur la manière d'exécuter cette classe. */
	public static void afficherUsage() {
		System.out.println("\n" + "Usage :"
				+ "\n\t" + "java allumettes.Jouer joueur1 joueur2"
				+ "\n\t\t" + "joueur est de la forme nom@stratégie"
				+ "\n\t\t" + "strategie = naif | rapide | expert | humain | tricheur"
				+ "\n"
				+ "\n\t" + "Exemple :"
				+ "\n\t" + "	java allumettes.Jouer Xavier@humain "
					   + "Ordinateur@naif"
				+ "\n"
				);
	}

}
