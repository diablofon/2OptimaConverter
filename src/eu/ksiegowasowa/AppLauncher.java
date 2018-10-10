package eu.ksiegowasowa;

import eu.ksiegowasowa.swing.ApplicationFrame;

public class AppLauncher {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ApplicationFrame().setVisible(true);
            }
        });
    }
    

}
