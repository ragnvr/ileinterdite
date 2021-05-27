import javax.swing.*;
import java.awt.*;
import java.lang.InterruptedException.*;
import java.util.ArrayList;


class VueGrille extends JPanel implements Observer  {
    /** On maintient une référence vers le modèle. */
    private Model modele;
    /** Définition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 100;
    private ArrayList<Player> allPlayers;
    /** Constructeur. */
    public VueGrille(Model modele) {
        this.modele = modele;
        this.allPlayers = modele.getAllPlayers();
        /** On enregistre la vue [this] en tant qu'observateur de [modele]. */

        modele.addObserver(this);
        /**
         * Définition et application d'une taille fixe pour cette zone de
         * l'interface, calculée en fonction du nombre de cellules et de la
         * taille d'affichage.
         */
        Dimension dim = new Dimension(TAILLE*modele.getDimension(),
                TAILLE*modele.getDimension());
        this.setPreferredSize(dim);
    }

    /**
     * L'interface Observer demande de fournir une méthode update, qui
     * sera appelée lorsque la vue sera notifiée d'un changement dans le
     * modèle. Ici on se content de réafficher toute la grille avec la méthode
     * prédéfinie repaint.
     */

    /**
     * Les éléments graphiques comme [JPanel] possèdent une méthode
     * [paintComponent] qui définit l'action à accomplir pour afficher cet
     * élément. On la redéfinit ici pour lui confier l'affichage des cellules.
     *
     * La classe [Graphics] regroupe les éléments de style sur le dessin,
     * comme la couleur actuelle.
     */


    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, modele.getDimension()*TAILLE, modele.getDimension()*TAILLE);
        /** Pour chaque cellule... */
        for(int i=0; i<modele.getDimension(); i++) {
            for(int j=0; j<modele.getDimension(); j++) {
                /**
                 * Appeler une fonction d'affichage auxiliaire.
                 * On lui fournit les informations de dessin g et les
                 * coordonnées du coin en haut à gauche.
                 */
                int cste = this.TAILLE-10;;
                paintEtat(g,modele.getCellule(i, j), (j)*TAILLE, (i)*TAILLE);
                paintCelluleSpe(g,modele.getCellule(i, j), (j)*TAILLE, (i)*TAILLE);
                for ( Player p : this.allPlayers){
                    paintJoueur(g,p,modele.getCellule(i, j), (j)*TAILLE, (i)*TAILLE, cste);
                    cste -= this.TAILLE/5;
                }
                paint(g,modele.getCellule(i, j), (j)*TAILLE, (i)*TAILLE);
            }
        }

        if (modele.isFinDeJeu()) {
            if (modele.checkEndGame()) Endgame_msg(g);
            if (modele.checkWin()) winGame(g);
        }

    }


    private void paint(Graphics g,Cellule c, int x, int y) {
        /** Sélection d'une couleur. **/

        if (c.getZone() == Cellule.Zone.air) {
            g.setColor(Color.gray);
        } else if (c.getZone() == Cellule.Zone.feu) {
            g.setColor(Color.orange);
        } else if (c.getZone() == Cellule.Zone.terre) {
            g.setColor(Color.green);
        } else if (c.getZone() == Cellule.Zone.eau) {
            g.setColor(Color.blue);

        }else{
            g.setColor(Color.cyan);
        }
        /** Coloration d'un rectangle. */
        g.drawRect(x, y, TAILLE, TAILLE);
    }

    /**
     * affichage de l etat d une case donnee
     * @param g
     * @param c
     * @param x
     * @param y
     */
    private void paintEtat(Graphics g, Cellule c, int x, int y) {


        if (c.getEtat() == Cellule.Etat.normal) {
            g.setColor(Color.white);
            g.fillRect(x, y, TAILLE, TAILLE);
        }
        if (c.getEtat() == Cellule.Etat.inondé) {
            g.setColor(Color.cyan);
            g.fillRect(x, y, TAILLE, TAILLE);
        }
        if  (c.getEtat() == Cellule.Etat.submergé){
            g.setColor(Color.black);
            g.fillRect(x, y, TAILLE, TAILLE);
        }
    }

    /**afficher un joueur
     *
     * @param g
     * @param b
     * @param c
     * @param x
     * @param y
     * @param cnste
     */
    private void paintJoueur(Graphics g,Player b, Cellule c, int x, int y, int cnste) {
        int x1= b.x;
        int y1=b.y;
        //si ya le joueur
        if(c.getX() ==x1 && c.getY()==y1) {
            if (b.getMainPlayer()){
                if (modele.isVueKey()){
                    g.setColor(Color.red);
                    g.drawString(b.role + " : " + b.nbMoves + " : " + b.toStringKeys(), x, y + cnste);
                }
                else {
                    g.setColor(Color.red);
                    g.drawString(b.role + " : " + b.nbMoves + " : " + b.toStringArtefacts(), x, y + cnste);
                }

            } else {
                if (modele.isVueKey()){
                    g.setColor(Color.magenta);
                    g.drawString(b.role + " : " + b.toStringKeys(), x, y + cnste);
                }
                else {
                    g.setColor(Color.magenta);
                    g.drawString(b.role + " : " + b.toStringArtefacts(), x, y + cnste);
                }           //    g.drawRect(x, y, TAILLE, TAILLE);
            }
        }
    }

    /**afficher cellule spe
     *
     * @param g
     * @param c
     * @param x
     * @param y
     */
    private void paintCelluleSpe (Graphics g, Cellule c, int x, int y) {

        /** Sélection*/
        if (c.isHeliport()) {
            g.setColor(Color.BLACK);
            g.drawString("Heliport" , x , y+ 10);
        }
        if (c.isArtefact()) {
            if (c.modele.checkCelluleLose(c.getX(),c.getY())){
                g.setColor(Color.red);
//                g.fillRect(x, y, TAILLE, TAILLE);
                g.drawString("Artefact : " + c.getZone(), x , y+10);
//                try {
//                    Thread.sleep(5000);
//                }
//                catch (InterruptedException e) {  }
            } else {
                g.setColor(Color.BLACK);
                g.drawString("Artefact : " + c.getZone(), x, y + 10);
            }
        }

    }

    /** mise a jour de l affichage */
    public void update()  {
        repaint();
    }


    /** affiche la defaite de l utilisateur
     *
     * @param g
     */
    private void Endgame_msg(Graphics g){
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {  }

        g.setColor(Color.red);
        g.fillRect(0,0,TAILLE*modele.getDimension(),TAILLE*modele.getDimension());
        g.setColor(Color.white);
        g.drawString("LOSE ",TAILLE*modele.getDimension()/2,TAILLE*modele.getDimension()/2);
        System.out.println("Dommage vous avez perdu...");

    }

    /** affiche la victoire de l utilisateur
     *
     * @param g
     */
    private void winGame(Graphics g){

        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {  }

        g.setColor(Color.green);
        g.fillRect(0,0,600,600);
        g.setColor(Color.white);
        g.drawString("WIN ",300,300);
        System.out.println("Felicitation vous avez gagne !");

    }


}
