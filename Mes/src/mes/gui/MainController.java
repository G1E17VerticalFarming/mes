/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.gui;

import shared.ProductionBlock;
import shared.GrowthProfile;
import shared.Log;

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
//import mes.api.Greeting;
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
            ProductionBlock pb = new ProductionBlock();
            pb.setName("hej123");
            //testTa.appendText(HttpOkhttpPostSend.doPostRequest("http://localhost:8080/production_block/", pb));
            testTa.appendText(HttpOkhttpPostSend.doGetRequest("http://localhost:8080/ping/", Boolean.class).toString());
            //ProductionBlock[] pbArr = HttpOkhttpPostSend.doGetRequest("http://localhost:8080/production_block/", ProductionBlock[].class);
            //testTa.appendText(pbArr[0].toString());
            //testTa.appendText(pbArr[1].toString());
            Log log = new Log();
            log.setType("hejejeje");
            //testTa.appendText(HttpOkhttpPostSend.doPostRequest("http://localhost:8080/log/", log));
            
        } catch (IOException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
