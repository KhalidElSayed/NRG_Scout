package nrgscoutclient;

public class Main {
    
    public static void main(String[] mainMethodArguments) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("GTK+".equals(info.getName()) || "Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainGUI().setVisible(true);
    }
}
