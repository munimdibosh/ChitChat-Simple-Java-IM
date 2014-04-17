
import com.cb.chitchat.MainClient;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mangoesmobile
 */
public class ChitChat {
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setUpLookAndFeel();

                    MainClient client=new MainClient();
                    client.setLocationRelativeTo(null);
                    client.setVisible(true);



            }

            private void setUpLookAndFeel() throws HeadlessException {
                try {
                    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception e) {
                    // If Nimbus is not available, you can set the GUI to another look and feel.
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception ex) {
                    }
                    int val = JOptionPane.showConfirmDialog(new JFrame(), "Please make sure that you have at least JRE Java SE 6 Update 10 (6u10) release\n" + "installed to enable polished UI." + "\nOk-to continue,Cancel-to open latest JRE download page.", "", JOptionPane.OK_CANCEL_OPTION);
                    if (val == JOptionPane.CANCEL_OPTION) {
                        String url = "http://www.oracle.com/technetwork/java/javase/downloads/index.html";
                        try {
                            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

        });
    }

}
