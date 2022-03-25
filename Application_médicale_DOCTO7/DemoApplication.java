import org.h2.jdbcx.JdbcDataSource;
import java.awt.*;

import IHM.*;
import Modele.*;
import bdd.BaseDeDonnees;

public class DemoApplication {

    Modele modele;

    public DemoApplication(Modele modele) {
        this.modele = modele;
        new PageLogin(this.modele);
    }

    public static void main(String args[]) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                JdbcDataSource dataSource = new JdbcDataSource();

                //String chemin = "./src/bdd/bdd_projet;IFEXISTS=TRUE";     // IntelliJ
                String chemin = "./bdd/bdd_projet;IFEXISTS=TRUE";           // Eclipse

                dataSource.setURL("jdbc:h2:" + chemin);
                dataSource.setUser("");
                dataSource.setPassword("");

                BaseDeDonnees bdd = new BaseDeDonnees(dataSource);
                Modele modele = new Modele(bdd);

                new DemoApplication(modele);

            }
        });
    }

}
