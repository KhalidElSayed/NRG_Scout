package nrgscoutclient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class AllianceSwitch extends JPanel {

    public static final int RED = 0;
    public static final int BLUE = 1;
    private Image slider;
    private Color transparent;
    private int setAlliance = 1;
    private int x = 0;

    public AllianceSwitch() {
        slider = new ImageIcon(getClass().getResource("/nrgscoutclient/slider.png")).getImage();
        transparent = this.getBackground();
        this.setDoubleBuffered(true);
        this.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mouseRelease(evt);
            }
        });
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseDragged(java.awt.event.MouseEvent evt) {
                mouseDrag(evt);
            }
        });
    }

    private void mouseDrag(MouseEvent evt) {
        x = Math.min(this.getWidth() / 2 + 1, Math.max(0, evt.getX() - this.getWidth() / 4));
        if (x <= 0 || x >= this.getWidth() / 2 + 1) {
            if (evt.getX() > this.getWidth() / 2) {
                MainGUI.alliance = RED;
            }
            else {
                MainGUI.alliance = BLUE;
            }
            this.setAlliance = MainGUI.alliance;
            MainGUI.saved = false;
        }
    }

    private void mouseRelease(MouseEvent evt) {
        if (evt.getX() > this.getWidth() / 2) {
            x = this.getWidth() / 2 + 1;
            MainGUI.alliance = RED;
        }
        else {
            x = 0;
            MainGUI.alliance = BLUE;
        }
        this.setAlliance = MainGUI.alliance;
        MainGUI.saved = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(MainGUI.alliance == RED ? Color.RED : Color.BLUE);
        //4 corners
        g.setColor(transparent);
        g.fillRect(0, 0, 1, 1);
        g.fillRect(0, this.getHeight() - 1, 1, 1);
        g.fillRect(this.getWidth() - 1, 0, 1, 1);
        g.fillRect(this.getWidth() - 1, this.getHeight() - 1, 1, 1);
        g.setColor(Color.gray);
        g.drawImage(slider, x, 0, this.getWidth() / 2, this.getHeight(), this);
        if (this.setAlliance != MainGUI.alliance) {
            //Force Update
            this.setAlliance = MainGUI.alliance;
            x = this.setAlliance == BLUE ? 0 : this.getWidth() / 2 + 1;
        }
        this.repaint();
    }
}
