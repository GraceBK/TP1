package exo1_2;/*
 * Cay S. Horstmann & Gary Cornell, Core Java
 * Published By Sun Microsystems Press/Prentice-Hall
 * Copyright (C) 1997 Sun Microsystems Inc.
 * All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this
 * software and its documentation for NON-COMMERCIAL purposes
 * and without fee is hereby granted provided that this
 * copyright notice appears in all copies.
 *
 * THE AUTHORS AND PUBLISHER MAKE NO REPRESENTATIONS OR
 * WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHORS
 * AND PUBLISHER SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED
 * BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

/**
 * @version 1.00 11 Mar 1997
 * @author Cay Horstmann
 * petites modifs pour le TP...
 */

import java.lang.reflect.*;

public class ArrayGrowTest
{  public static void main(String[] args)
{  int[] a = { 1, 2, 3 };
    Personne[] b = { new Personne("Maurice Chombier"), new Personne("François Pignon") };
    //a = (int[])goodArrayGrow(a);
    //arrayPrint(a);
    b = (Personne[])goodArrayGrow(b);
    arrayPrint(b);

    System.out.println
            ("The following call will generate an exception.");
    b = (Personne[])badArrayGrow(b);
}

    static Object[] badArrayGrow(Object[] a)
    {  int newLength = a.length * 11 / 10 + 10;
        Object[] newArray = new Object[newLength];
        System.arraycopy(a, 0, newArray, 0, a.length);
        return newArray;
    }

    /* goodArrayGrow avec a en commentaire */
    static Object[] goodArrayGrow(Object []a)
    {
        Class cl = a.getClass();
        if (!cl.isArray()) return null;
        Class componentType = cl.getComponentType();
        int length = Array.getLength(a);
        int newLength = length * 11 / 10 + 10;

        Object newArray = Array.newInstance(componentType,
                newLength);
        System.arraycopy(a, 0, newArray, 0, length);
        return (Object[]) newArray;
    }

    /* goodArrayGrow avec a non commenté */




    static void arrayPrint(Object a)
    {  Class cl = a.getClass();
        if (!cl.isArray()) return;
        Class componentType = a.getClass().getComponentType();
        int length = Array.getLength(a);
        System.out.println(componentType.getName()
                + "[" + length + "]");
        for (int i = 0; i < Array.getLength(a); i++)
            System.out.println(Array.get(a, i));
    }
}

class Personne {
    private String nom;

    public Personne(String nom) {
        this.nom = nom;
    }

    public String toString() {
        return nom;
    }
}