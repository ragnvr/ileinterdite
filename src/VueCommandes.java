
import java.util .*;
import java.awt .*;
import java.awt.event .*;
import javax.swing .*;
/**
 * Une classe pour représenter la zone contenant le bouton.
 * <p>
 * Cette zone n'aura pas à être mise à jour et ne sera donc pas un observateur.
 * En revanche, comme la zone précédente, celle-ci est un panneau [JPanel].
 */
class VueCommandes extends JPanel {
    /**
     * Pour que le bouton puisse transmettre ses ordres, on garde une
     * référence au modèle.
     */
    private Model modele;
    private ArrayList <Player> allPlayers;
    private Player j1 ;
    private boolean activeMove;
    /**
     * Constructeur.
     */
    public VueCommandes(Model modele) {

        this.modele = modele;
        this.allPlayers=  modele.getAllPlayers();
        this.j1 = this.allPlayers.get(0);
        this.activeMove = true;
        /**
         * On crée un nouveau bouton, de classe [JButton], en précisant le
         * texte qui doit l'étiqueter.
         * Puis on ajoute ce bouton au panneau [this].
         */

        /** creation boutons */
//        JButton boutoninonde = new JButton("fin");

        JButton devant = new JButton("↑");
        JButton derriere = new JButton("↓");
        JButton gauche = new JButton("←");
        JButton droite = new JButton("→");
        JButton secheMilieu = new JButton("seche ici");

        JButton altJoueur = new JButton("next player");

        JButton actMove = new JButton("active Move");
        JButton actSeche = new JButton("active Seche");

        JButton recupArtefact = new JButton("recuperer");
        JButton altVueKeys = new JButton("vue keys");
        JButton altVueArtefacts = new JButton("vue artefacts");

        JButton livai = new JButton("uber");

        /** ajouts boutons au panneau */
//        this.add(boutoninonde);

        this.add(devant);
        this.add(derriere);
        this.add(gauche);
        this.add(droite);
        this.add(secheMilieu);

        this.add(altJoueur);

        this.add(actMove);
        this.add(actSeche);

        this.add(recupArtefact);
        this.add(altVueKeys);
        this.add(altVueArtefacts);
        this.add(livai);


        /**
         * Le bouton, lorsqu'il est cliqué par l'utilisateur, produit un
         * événement, de classe [ActionEvent].
         *
         * On a ici une variante du schéma observateur/observé : un objet
         * implémentant une interface [ActionListener] va s'inscrire pour
         * "écouter" les événements produits par le bouton, et recevoir
         * automatiquements des notifications.
         * D'autres variantes d'auditeurs pour des événements particuliers :
         * [MouseListener], [KeyboardListener], [WindowListener].
         *
         * Cet observateur va enrichir notre schéma Modèle-Vue d'une couche
         * intermédiaire Contrôleur, dont l'objectif est de récupérer les
         * événements produits par la vue et de les traduire en instructions
         * pour le modèle.
         * Cette strate intermédiaire est potentiellement riche, et peut
         * notamment traduire les mêmes événements de différentes façons en
         * fonction d'un état de l'application.
         * Ici nous avons un seul bouton réalisant une seule action, notre
         * contrôleur sera donc particulièrement simple. Cela nécessite
         * néanmoins la création d'une classe dédiée.
         */
     //   Controleur ctrl = new Controleur(modele,j1);
        /** Enregistrement du contrôleur comme auditeur du bouton. */

    //     boutoninonde.addActionListener(ctrl);
    //    devant.addActionListener(ctrl);
        Model m = this.modele;

//        boutoninonde.addActionListener(e -> { modele.inonde(); });

        devant.addActionListener(e -> { if(m.isFinDeJeu()) return; if (this.activeMove) m.avance(j1); else {m.seche(j1, "haut");} ;});
        derriere.addActionListener(e -> { if(m.isFinDeJeu()) return;if (this.activeMove) m.recule(j1); else {m.seche(j1, "bas");} });
        gauche.addActionListener(e -> { if(m.isFinDeJeu()) return;if (this.activeMove) m.gauche(j1); else {m.seche(j1, "gauche");} });
        droite.addActionListener(e -> { if(m.isFinDeJeu()) return;if (this.activeMove) m.droite(j1); else {m.seche(j1, "droite");} });
        secheMilieu.addActionListener(e ->{ if(m.isFinDeJeu()) return;if (!this.activeMove) m.seche(j1, "milieu");  } );
    //si le movement (activeMove) est active on bouge sinon c le sechage qui est active alors on seche
        altJoueur.addActionListener(e -> {  if(m.isFinDeJeu()) return;this.j1 =  m.changementJoueur(); } );
        actMove.addActionListener(e ->{ if(m.isFinDeJeu()) return;this.activeMove = true; } );
        actSeche.addActionListener(e ->{ if(m.isFinDeJeu()) return;this.activeMove = false; } );

        recupArtefact.addActionListener(e ->{ if(m.isFinDeJeu()) return; m.recuperArtefact(j1);  } );

        altVueArtefacts.addActionListener(e ->{if(m.isFinDeJeu()) return; m.altVueKeys(false); } );
        altVueKeys.addActionListener(e ->{if(m.isFinDeJeu()) return; m.altVueKeys(true); } );

        livai.addActionListener(e ->{if(m.isFinDeJeu()) return; m.allPlayersToHelico(); } );



                                                                                /**
                                                                                 * Variante : une lambda-expression qui évite de créer une classe
                                                                                 * spécifique pour un contrôleur simplissime.
                                                                                 *
                                                                                 JButton boutonAvance = new JButton(">");
                                                                                 this.add(boutonAvance);
                                                                                 boutonAvance.addActionListener(e -> { modele.avance(); });
                                                                                 *
                                                                                 */

    }
}
/** Fin de la vue. */