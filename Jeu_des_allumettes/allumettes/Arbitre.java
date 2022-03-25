package allumettes;

public class Arbitre {

	private Joueur joueur1;
	private Joueur joueur2;
	private boolean confiant;


	public Arbitre(Joueur j1, Joueur j2) {
		this.joueur1 = j1;
		this.joueur2 = j2;
	}


	public Arbitre(Joueur j1, Joueur j2, boolean confiant) {
		this(j1, j2);
		this.confiant = confiant;
	}



	public void arbitrer(Jeu jeu) {
		Joueur j1 = this.joueur1;
		Joueur j2 = this.joueur2;
		Jeu proxy;
		if (confiant) {
			proxy = jeu;
		} else {
			proxy = new Proxy(jeu);
		}
		boolean tour1 = true;
		boolean fin = false;
		int nombre = jeu.getNombreAllumettes();
	    System.out.println("Nombre d'allumettes restantes : " + nombre);
		while (jeu.getNombreAllumettes() > 0 && !fin) {
			Joueur joueur = j1;
			if (!tour1) {
				joueur = j2;
			}
	     	try {
				int i = joueur.getPrise(proxy);
				if (i == 1 | i == 0 | i == -1) {
					System.out.println(joueur.getNom() + " prend " + i + " allumette.");
				} else  {
					System.out.println(joueur.getNom() + " prend " + i + " allumettes.");
				}
				jeu.retirer(i);
				int nbAllumettes = jeu.getNombreAllumettes();
				if (nbAllumettes > 0) {
				    System.out.println("Nombre d'allumettes restantes : " + nbAllumettes);
				}
				tour1 = !tour1;
			} catch (CoupInvalideException e) {
				System.out.println("Impossible ! " + e.getMessage());
				System.out.println();
				int nbAllumettes = jeu.getNombreAllumettes();
			    System.out.println("Nombre d'allumettes restantes : " + nbAllumettes);
			} catch (OperationInterditeException e) {
				System.out.println("Abandon de la partie car " + joueur.getNom() + " triche !");
				fin = true;
			}
		}

		//Affichage du résultat : le gagnant et le perdant
		if (!fin) {
			System.out.println((tour1 ? j2 : j1).getNom() + " perd !");
			System.out.println((tour1 ? j1 : j2).getNom() + " gagne !");
		}
	}
}

