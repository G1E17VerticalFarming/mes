/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import main.java.Application;
import mes.api.Greeting;
import mes.api.HttpOkhttpPostSend;

/**
 *
 * @author chris
 */
public class MainController implements Initializable {

    @FXML
    private Button testBt;
    @FXML
    private TextArea testTa;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void test(ActionEvent event) {
        try {
            //Greeting greeting = new Greeting(2132, "dsadsaww");
            
            testTa.appendText(HttpOkhttpPostSend.doPostRequest("http://localhost:8080/user/", new Greeting(123, "dsawwwwda")));   
        } catch (IOException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
