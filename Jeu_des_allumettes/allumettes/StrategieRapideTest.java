package allumettes;
import org.junit.*;
import static org.junit.Assert.*;

/**
* Classe de test de la stratégie rapide.
* @author	CHELLAF EL HAMMOUD Chaimae
*/

public class StrategieRapideTest {

	// La stratégie choisie par le joueur
	private Strategie strategie; 
	
	
	// initialiser la stratégie par la stratégie rapide
	@Before public void setUp() {
		this.strategie = new Rapide();
	}
	
	// Tester la méthode getValeur
	@Test public void testerGetValeur() {
		assertEquals(this.strategie.getValeur(), "rapide");
	}
	
	// Tester la méthode getPrise
	@Test public void testerGetPrise() throws NumberFormatException, CoupInvalideException {
		Jeu jeu = new JeuReel(13);
		assertEquals(this.strategie.getPrise(jeu), jeu.PRISE_MAX);
	}
 }
