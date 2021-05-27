import java.lang.Enum;
import java.util.ArrayList;

public class Cellule {

    /** attributs */
    protected enum  Etat { normal,inondé , submergé; }
    protected enum  Zone {air, eau, terre, feu, normal ; }
    protected Etat etat ;
    protected Zone zone;
    protected Model modele;
    protected ArrayList <Player> players;
    private final int x, y;
    protected boolean artefact;
    protected boolean heliport;


    /** constructeur */
    public Cellule(Model modele, int x, int y) {
        this.players = new ArrayList<>();
        this.modele = modele;
        this.etat = Etat.normal;
         this.zone = Zone.normal ;
        this.x = x;
        this.y = y;
        this.artefact = false;
        this.heliport = false;
    }

    /** getter Etat */
    public Etat getEtat(){
        return this.etat;
    }

    /** return true si la case est vide */
    public boolean estVide() { return this.players.isEmpty(); }

    /** ajoute un joueur a la cellule */
    public void addPlayer(Player p){
        this.players.add(p);
    }

    /** supprime un joueur de la cellule */
    public void deletePlayer(Player p){
        this.players.remove(p);
    }

    /** change la zone actuelle en une zone donnee  */
    public void changeZone(Zone element ){
        this.zone = element;
    }

    /** return la zone de la zellule */
    public Zone getZone(){
        return this.zone;
    }

    /** return true si la case a l Etat normal */
    public boolean verifieNormal(){
        return this.etat == Etat.normal ;
    }

    /** return true si la case a l Etat inonde */
    public boolean verifieInonde(){
        return this.etat == Etat.inondé ;
    }

    /** return true si la case a l Etat submerge */
    public boolean verifieSubmerge(){
        return this.etat == Etat.submergé ;
    }

    /** change l etat actuel en un Etat donne */
    public void changeEtat(Etat a ){
        this.etat = a;
    }
    /** return x de la cellule */
    public  int getX(){
        return x;
    }

    /** return y de la cellule */
    public int getY(){
        return y;
    }

    /** return true si la cellule est l heliport */
    public boolean isHeliport(){return this.heliport;}

    /** return true si la cellule est l artefact */
    public boolean isArtefact() {
        return artefact;
    }

    /** defini l heliport de la cellule comme true*/
    public void setHeliportTrue() {
        this.heliport = true;
    }

    /** defini l artefact de la zone en normal */
    public void setArtefact() {
        this.artefact = !this.artefact;
    }


}
