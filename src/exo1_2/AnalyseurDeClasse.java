package exo1_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;

public class AnalyseurDeClasse {

    public static void analyseClasse(String nomClasse) throws ClassNotFoundException {
        // Récupération d'un objet de type Class correspondant au nom passé en paramètres
        Class cl = getClasse(nomClasse);

        afficheEnTeteClasse(cl);

        System.out.println();
        afficheAttributs(cl);

        System.out.println();
        afficheConstructeurs(cl);

        System.out.println();
        afficheMethodes(cl);

        // L'accolade fermante de fin de classe !
        System.out.println("}");
    }

    /**
     * Retourne la classe dont le nom est passé en paramètre
     * @param nomClasse
     * @return
     * @throws ClassNotFoundException
     */
    public static Class getClasse(String nomClasse) throws ClassNotFoundException {
        return Class.forName(nomClasse);
    }

    /**
     * Cette méthode affiche par ex "public class Toto extends Tata implements Titi, Tutu {"
     * @param cl
     */
    public static void afficheEnTeteClasse(Class cl) {
        //  Affichage du modifier et du nom de la classe
        Modifier.toString(cl.getModifiers());
        cl.getName();
        cl.getSuperclass();

        System.out.print(Modifier.toString(cl.getModifiers()) + " class ");
        System.out.print(cl.getName());


        // Récupération de la superclasse si elle existe (null si cl est le type Object)
        Class supercl = cl.getSuperclass();

        // On ecrit le "extends " que si la superclasse est non nulle et
        // différente de Object
        if (supercl != null) {
            System.out.print(" extends " + supercl.getName());
        }

        // Affichage des interfaces que la classe implemente
        // CODE A ECRIRE
        assert supercl != null;
        if (supercl.getInterfaces() != null) {
            System.out.print(" implement ");
        }

        // Enfin, l'accolade ouvrante !
        System.out.print(" {\n");
    }

    public static void afficheAttributs(Class cl) {
        System.out.println("--> ATTRIBUTS");
        cl.getDeclaredFields(); // retourne un tableau
        cl.getTypeName();
        cl.getName();
        for (int i = 0; i < cl.getDeclaredFields().length; i++) {
            System.out.println("\t" + cl.getDeclaredFields()[i] + ";");
        }
    }

    public static void afficheConstructeurs(Class cl) {
        System.out.println("--> CONSTRCTEURS");
        cl.getConstructors(); // retourne un tableau
        cl.getTypeName();
        cl.getName();
        for (int i = 0; i < cl.getConstructors().length; i++) {
            System.out.println("\t" + cl.getConstructors()[i] + ";");
        }
    }

    public static void afficheMethodes(Class cl) {
        System.out.println("--> METHODES");
        cl.getMethods(); // retourne un tableau
        cl.getTypeName();
        cl.getName();
        for (int i = 0; i < cl.getMethods().length; i++) {
            System.out.println("\t" + cl.getMethods()[i] + ";");
        }
        System.out.println("{}");
    }

    public static String litChaineAuClavier() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }

    public static void main(String[] args) {
        boolean ok = false;

        while (!ok) {
            try {
                System.out.print("Entrez le nom d'une classe (ex : java.util.Date): ");
                String nomClasse = litChaineAuClavier();

                analyseClasse(nomClasse);

                ok = true;
            } catch (ClassNotFoundException e) {
                System.out.println("Classe non trouvée.");
            } catch (IOException e) {
                System.out.println("Erreur d'E/S!");
            }
        }
    }
}
