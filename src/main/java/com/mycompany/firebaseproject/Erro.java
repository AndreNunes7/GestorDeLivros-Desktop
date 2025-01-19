/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.firebaseproject;

/**
 *
 * @author andre
 */
public class Erro {
    private static boolean erro;
    private static String mens;

    public static void setErro(boolean _erro)
    {
    erro=_erro;
    }
    public static void setErro(String _mens)
    {
    erro=true;
    mens=_mens;
    }
    public static boolean getErro() {return erro;}
    public static String getMens() {return mens;}    
}
