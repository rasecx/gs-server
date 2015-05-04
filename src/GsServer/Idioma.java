/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GsServer;

/**
 *
 * @author jcferreira
 */
public class Idioma {
    
    
    public static final int PT_BR =0;
    public static final int PT =1;
    public static final int EN=2;
    public static final int ES=3;

    private static final String DESC_PT_BR ="Português Brasil";
    private static final String DESC_PT ="Português Portugal";
    private static final String DESC_EN ="Inglês";
    private static final String DESC_ES ="Espanhol";
    
    private static final String VAL_PT_BR ="pt";
    private static final String VAL_PT ="po";
    private static final String VAL_EN ="eng";
    private static final String VAL_ES ="es";
    
    public static String lingus[]={DESC_PT_BR,DESC_PT,DESC_EN,DESC_ES};

    public static String SelecionaLinguagem[]={VAL_PT_BR,VAL_PT,VAL_EN,VAL_ES};
   
    
    
}
