package swing;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JPanel {
    private JLabel infoLabel;
    private JTextField usernameTF; //TextField y no TextArea pq el primero sólo deja introducir una sola línea
    private JButton loginButton;
    private GridBagConstraints gbConstraints;

    public LoginWindow(){
        super(new GridBagLayout());

        gbConstraints = new GridBagConstraints();
       
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;

        //Mensaje inficativo
        infoLabel = new JLabel("Write you username");
        gbConstraints.weightx = 1.0;
        gbConstraints.weighty=1.0;
        gbConstraints.insets = new Insets(5,5,0,5);
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;        
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 0;
        add(infoLabel,gbConstraints);

        //introduccion del username
        usernameTF = new JTextField();
        gbConstraints.weightx=10.0;
        gbConstraints.weighty=0.0;
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.insets = new Insets(5,5,0,5);
        gbConstraints.gridx=0;
        gbConstraints.gridy = 1;
        add(usernameTF, gbConstraints);

        //Botón
        loginButton = new JButton("In");
        gbConstraints.weightx=10.0;
        gbConstraints.weighty = 0;
        
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.anchor = GridBagConstraints.NORTH;
        gbConstraints.insets = new Insets(5,5,5,5);
        gbConstraints.gridx=0;
        gbConstraints.gridy =2;
        add(loginButton, gbConstraints);

        
    }

    public JTextField getUsername(){
        return usernameTF;
    }

    public JButton getLoginButton(){
        return loginButton;       
    }
}
