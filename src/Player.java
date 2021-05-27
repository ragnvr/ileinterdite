import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Observable {
    /**
     * attributs du player
     */
    public int y; //Position du joueur
    public int x;
    private Model modele;
    private Cellule cellule;
    private boolean mainPlayer;
    protected int nbMoves;
    public String role;
    private HashMap<Cellule.Zone, Boolean> artefacts;
    private HashMap<Cellule.Zone, Boolean> keys;
    int nbKeys; // max 2
    private int nbArtefacts;

//    private String cle1; // valeur : feu, eau, terre, air ou none
//    private String artefact1;
//    private String cle2;
//    private String artefact2;


    // the player controller associated with the player
    //  public PlayerController pc;


    // character class of the player
    //  public CharacterClasses chClass = CharacterClasses.Default;

    /**
     * constructeur du Player
     */
    public Player(Model m, int x, int y, String role) {
        this.modele = m;
        this.x = x;
        this.y = y;
        this.role = role;
        this.artefacts = new HashMap<>();
        this.keys = new HashMap<>();
        this.cellule = modele.getCellule(x, y);
        this.modele.addPlayer(x, y, this);
        this.nbMoves = 0;
        this.mainPlayer = false;
        this.initKeysAndArtefacts();
        this.nbKeys = 0;
        this.nbArtefacts = 0;
        // pc = new PlayerController();
        //  this.chClass = chClass;
    }

    /**
     * initialise les cles et les artefacts du joueur
     */
    private void initKeysAndArtefacts() {

        this.keys.put(Cellule.Zone.air, false);
        this.keys.put(Cellule.Zone.feu, false);
        this.keys.put(Cellule.Zone.eau, false);
        this.keys.put(Cellule.Zone.terre, false);

        this.artefacts.put(Cellule.Zone.air, false);
        this.artefacts.put(Cellule.Zone.feu, false);
        this.artefacts.put(Cellule.Zone.eau, false);
        this.artefacts.put(Cellule.Zone.terre, false);

    }



    /**
     * retourne les cles possedees par le joueur
     */
    public ArrayList<Cellule.Zone> clePossede() {

        ArrayList<Cellule.Zone> resultat = new ArrayList<>();
        if (nbKeys == 0) return resultat;

        if (keys.get(Cellule.Zone.air)) resultat.add(Cellule.Zone.air);
        if (keys.get(Cellule.Zone.eau)) resultat.add(Cellule.Zone.eau);
        if (keys.get(Cellule.Zone.feu)) resultat.add(Cellule.Zone.feu);
        if (keys.get(Cellule.Zone.terre)) resultat.add(Cellule.Zone.terre);

        return resultat;
    }

    /**
     * retourne les artefacts possedees par le joueur
     */
    private ArrayList<Cellule.Zone> artefactsPossede() {
        ArrayList<Cellule.Zone> resultat = new ArrayList<>();
        if (nbArtefacts == 0) return resultat;

        if (artefacts.get(Cellule.Zone.air)) resultat.add(Cellule.Zone.air);
        if (artefacts.get(Cellule.Zone.eau)) resultat.add(Cellule.Zone.eau);
        if (artefacts.get(Cellule.Zone.feu)) resultat.add(Cellule.Zone.feu);
        if (artefacts.get(Cellule.Zone.terre)) resultat.add(Cellule.Zone.terre);

        return resultat;
    }

    /**
     * rajoute une cle pour le joueur
     */
    public void addKey(Cellule.Zone zone) {
        this.keys.replace(zone, true);
        this.nbKeys++;
    }

    /**
     * rajoute un artefact pour le joueur
     */
    public void addArtefact(Cellule.Zone zone) {
        this.artefacts.replace(zone, true);
        this.nbArtefacts++;
        //this.nbMoves++;
    }

    public void removeKey(Cellule.Zone zone) {
        this.keys.replace(zone, false);
        this.nbKeys--;
    }


    /**
     *
     * @return texte des artefacts
     */
    public String toStringArtefacts() {
        String s = "";
        if (this.nbArtefacts == 0) return s;
        ArrayList<Cellule.Zone> clePossede = artefactsPossede();
        if (clePossede.contains(Cellule.Zone.air)) s = s + " air ";
        if (clePossede.contains(Cellule.Zone.eau)) s = s + " eau ";
        if (clePossede.contains(Cellule.Zone.feu)) s = s + " feu ";
        if (clePossede.contains(Cellule.Zone.terre)) s = s + " terre ";

        return s;
    }

    /**
     *
     * @return texte des keys
     */
    public String toStringKeys() {
        String s = "";
        if (this.nbKeys == 0) return s;
        ArrayList<Cellule.Zone> clePossede = clePossede();
        if (clePossede.contains(Cellule.Zone.air)) s = s + " air ";
        if (clePossede.contains(Cellule.Zone.eau)) s = s + " eau ";
        if (clePossede.contains(Cellule.Zone.feu)) s = s + " feu ";
        if (clePossede.contains(Cellule.Zone.terre)) s = s + " terre ";

        return s;
    }

    /**
     * return le nombre de artefacts
     */
    public int getNbArtefacts() {
        return nbArtefacts;
    }

    /**
     * return le nombre de cle
     */
    public int getNbKeys() {
        return nbKeys;
    }

    /**
     * return true si c est le perso principal
     */
    public boolean getMainPlayer() {
        return this.mainPlayer;
    }

    /**
     * change le mainPlayer
     */
    public void setMainPlayer() {
        this.mainPlayer = !this.mainPlayer;

    }

    /**
     * fais avancer le Player
     */
    public void avance() {
        if (this.nbMoves < 3) {
            ArrayList<Cellule> voisins = modele.voisins(x, y);
            int x1;
            Cellule c;

            for (Cellule d : voisins) {
                x1 = this.cellule.getX() - 1;
                c = new Cellule(modele, x1, y);

                if (d.getX() == c.getX() && d.getY() == c.getY()) {
                    this.modele.deletePlayer(x, y, this);
                    this.x = x1;
                    this.y = y;
                    this.cellule = modele.getCellule(x, y);
                    this.modele.addPlayer(x, y, this);
                    this.nbMoves++;
                }
            }
        }
        notifyObservers();
    }

    /**
     * fais reculer le Player
     */
    public void recule() {
        if (this.nbMoves < 3) {

            ArrayList<Cellule> voisins = modele.voisins(x, y);
            int x1;
            Cellule c;

            for (Cellule d : voisins) {
                x1 = this.cellule.getX() + 1;
                c = new Cellule(modele, x1, y);

                if (d.getX() == c.getX() && d.getY() == c.getY()) {
                    this.modele.deletePlayer(x, y, this);
                    this.x = x1;
                    this.y = y;
                    this.cellule = modele.getCellule(x, y);
                    this.modele.addPlayer(x, y, this);
                    this.nbMoves++;
                }
            }
        }
        notifyObservers();
    }

    /**
     * le Player va a droite
     */
    public void droite() {
        if (this.nbMoves < 3) {
            ArrayList<Cellule> voisins = modele.voisins(x, y);
            int y1;
            Cellule c;

            for (Cellule d : voisins) {
                y1 = this.cellule.getY() + 1;
                c = new Cellule(modele, x, y1);

                if (d.getX() == c.getX() && d.getY() == c.getY()) {
                    this.modele.deletePlayer(x, y, this);
                    this.x = x;
                    this.y = y1;
                    this.cellule = modele.getCellule(x, y);
                    this.modele.addPlayer(x, y, this);
                    this.nbMoves++;
                }
            }
        }
        notifyObservers();
    }

    /**
     * le Player va a gauche
     */
    public void gauche() {
        if (this.nbMoves < 3) {
            ArrayList<Cellule> voisins = modele.voisins(x, y);
            int y1;
            Cellule c;

            for (Cellule d : voisins) {
                y1 = this.cellule.getY() - 1;
                c = new Cellule(modele, x, y1);

                if (d.getX() == c.getX() && d.getY() == c.getY()) {
                    this.modele.deletePlayer(x, y, this);
                    this.x = x;
                    this.y = y1;
                    this.cellule = modele.getCellule(x, y);
                    this.modele.addPlayer(x, y, this);
                    this.nbMoves++;
                }
            }
        }
        notifyObservers();
    }

    /**
     * remet le nbMoves du player a 0
     */
    public void nbMovesToZero() {
        this.nbMoves = 0;
    }


    /**
     * change la cellule du joueur
     * @param i
     * @param j
     */
    public void changeCellule(int i, int j) {
        this.x = i;
        this.y = j;
        this.cellule = this.modele.getCellule(i, j);
    }

    /**
     *
     * @return la cellule du joueur
     */
    public Cellule getCellule() {
        return cellule;
    }
}