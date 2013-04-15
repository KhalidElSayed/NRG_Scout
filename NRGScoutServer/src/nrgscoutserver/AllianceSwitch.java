package nrgscoutserver;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class AllianceSwitch extends JPanel {

    private int alliance = Alliance.ALLIANCE_BLUE;
    private Image slider;
    private Color transparent;
    private int x = 0;

    public AllianceSwitch() {
        slider = new ImageIcon(getClass().getResource("/nrgscoutserver/slider.png")).getImage();
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
                alliance = Alliance.ALLIANCE_RED;
            }
            else {
                alliance = Alliance.ALLIANCE_BLUE;
            }
        }
    }

    private void mouseRelease(MouseEvent evt) {
        if (evt.getX() > this.getWidth() / 2) {
            x = this.getWidth() / 2 + 1;
            alliance = Alliance.ALLIANCE_RED;
        }
        else {
            x = 0;
            alliance = Alliance.ALLIANCE_BLUE;
        }
    }

    public void setAlliance(int alliance) {
        this.alliance = alliance;
        switch (alliance) {
            case Alliance.ALLIANCE_RED:
                x = this.getWidth() / 2 + 1;
                break;
            default:
                x = 0;
                break;
        }
    }
    
    public int getAlliance() {
        return alliance;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(alliance == Alliance.ALLIANCE_RED ? Color.RED : Color.BLUE);
        //4 corners
        g.setColor(transparent);
        g.fillRect(0, 0, 1, 1);
        g.fillRect(0, this.getHeight() - 1, 1, 1);
        g.fillRect(this.getWidth() - 1, 0, 1, 1);
        g.fillRect(this.getWidth() - 1, this.getHeight() - 1, 1, 1);
        g.setColor(Color.gray);
        g.drawImage(slider, x, 0, this.getWidth() / 2, this.getHeight(), this);
        this.repaint();
    }
}
