package swing;
import javax.swing.*;
import java.awt.*;

public class Window extends JFrame{
    
    private CardLayout showing; //permite ver si enseño el login o el xat
    private JPanel panel;

    private LoginWindow loginWindow;
    private XatWindow xatWindow;
    
    public Window(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            
        }
        this.setDefaultLookAndFeelDecorated(true);
        showing = new CardLayout();
        panel = new JPanel(showing);
        
        add(panel);
        ONlogin();
        //ONxat("aa");
        setVisible(true);
        pack();
    }

    public void ONlogin(){
        loginWindow = new LoginWindow();        
        //panel.add(loginWindow, "1"); 
        panel.add(loginWindow); 
        showing.show(panel, "1"); //el 1 es el nombre de referencia a ese panel

        setTitle("XAT SAD v1");
        panel.validate(); //It means invalid content is asked for all the sizes and all the subcomponents' sizes are set to proper values by LayoutManager
        panel.repaint(); //internamente llama al método update
        setResizable(false);

        Dimension dim = new Dimension(200,100);        
        setSize(dim);
        
        
        setLocationRelativeTo(null);
        panel.setPreferredSize(dim);
        loginWindow.setPreferredSize(dim);;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void ONxat(String username){
        xatWindow = new XatWindow("Hello " + username);
        panel.add(xatWindow, "2");
        showing.show(panel, "2");

        setTitle("Session of: " + username);
        panel.validate(); //It means invalid content is asked for all the sizes and all the subcomponents' sizes are set to proper values by LayoutManager
        panel.repaint(); //internamente llama al método update
        setResizable(false);

        Dimension dim = new Dimension(500,500);        
        setSize(dim);

        setLocationRelativeTo(null);
        panel.setPreferredSize(dim);
        loginWindow.setPreferredSize(dim);;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public XatWindow getXatWindow(){
        return xatWindow;
    }

    public LoginWindow getLoginWindow(){
        return loginWindow;
    }

}
