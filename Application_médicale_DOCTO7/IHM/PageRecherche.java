package IHM;

import Modele.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class PageRecherche extends JFrame {

    private Utilisateur utilisateur;

    private Modele modele;

    final private JButton bRechercher = new JButton("Rechercher");

    final private JButton bQuitter = new JButton("Quitter");

    final private JComboBox comboBoxSpecialites;

    public PageRecherche(Utilisateur utilisateur, Modele modele) {
        super();
        this.utilisateur = utilisateur;
        this.modele = modele;
        this.getContentPane().setLayout(new BorderLayout());
        JLabel titre = new JLabel("Bienvenue sur Docto7 " + this.utilisateur.getLogin());
        titre.setHorizontalAlignment(JLabel.CENTER);
        this.getContentPane().add(titre, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(new JLabel("Specialite"));
        java.util.List<String> listeSpecialites = new ArrayList<>();
        try {
            listeSpecialites = this.modele.getSpecialites();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        comboBoxSpecialites = new JComboBox(listeSpecialites.toArray());
        centerPanel.add(comboBoxSpecialites);

        this.getContentPane().add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 2));
        southPanel.add(bRechercher);
        bRechercher.addActionListener(new ActionRechercher());
        southPanel.add(bQuitter);
        bQuitter.addActionListener(new ActionQuitter());
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);

        this.pack();
        this.setTitle("Espace Recherche");
        this.setVisible(true);
        this.setSize(new Dimension(300, 300));
        this.setLocation(500, 500);
    }

    private class ActionRechercher implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Utilisateur utilisateur = PageRecherche.this.utilisateur;
            Modele modele = PageRecherche.this.modele;
            String specialite = (String) PageRecherche.this.comboBoxSpecialites.getSelectedItem();
            new PageResultat(utilisateur, modele, specialite);
            PageRecherche.this.dispose();
        }
    }

    private class ActionQuitter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new PageLogin(PageRecherche.this.modele);
            PageRecherche.this.dispose();
        }
    }
}
