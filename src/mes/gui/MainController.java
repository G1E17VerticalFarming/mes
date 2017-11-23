/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import mes.domain.IMes;
import mes.domain.Order;
import mes.domain.SingletonMES;

/**
 *
 * @author chris
 */
public class MainController implements Initializable {

    @FXML
    private AnchorPane tab_order_name;
    @FXML
    private TextField textField_prep_order;
    @FXML
    private TextField textField_prep_id;
    @FXML
    private ComboBox<?> comboBox_prep_order_status;
    @FXML
    private ComboBox<?> comboBox_prep_growth_profile;
    @FXML
    private TextField textField_prep_amount;
    @FXML
    private Button button_prep_prepare;
    @FXML
    private TableView<?> tableView_gp;
    @FXML
    private TableColumn<?, ?> tab_gp_id;
    @FXML
    private TableColumn<?, ?> tab_gp_name;
    @FXML
    private TextField textField_gp_celcius;
    @FXML
    private TextField textField_gp_water_level;
    @FXML
    private TextField textField_gp_moisture;
    @FXML
    private TextField textField_gp_name;
    @FXML
    private TableView<?> tableView_sequences;
    @FXML
    private TableColumn<?, ?> tab_sequences_sequence;
    @FXML
    private TableColumn<?, ?> tab_sequences_type;
    @FXML
    private TableColumn<?, ?> tab_sequences_time;
    @FXML
    private TableColumn<?, ?> tab_sequences_value;
    @FXML
    private TextField textField_gp_time;
    @FXML
    private ComboBox<?> comboBox_gp_type;
    @FXML
    private TextField textField_gp_value;
    @FXML
    private Button button_add_sequence;
    @FXML
    private Button button_sequence_delete;
    @FXML
    private Label label_sequence_selected;
    @FXML
    private PasswordField passwordField_password;
    @FXML
    private TextField textField_username;
    @FXML
    private Button button_login;
    @FXML
    private Button btnFetchOrders;
    @FXML
    private DatePicker datePickerOrderDate;
    
    private IMes MES = SingletonMES.getInstance();
    @FXML
    private Tab tabOrders;
    @FXML
    private Tab tabScada;
    @FXML
    private Tab tabGrowthProfiles;
    @FXML
    private Tab tabStatistics;
    @FXML
    private TableColumn<Order, Integer> tabOrderId;
    @FXML
    private TableColumn<Order, String> tabOrderFetchedTime;
    @FXML
    private TableColumn<Order, Integer> tabOrderQty;
    @FXML
    private TableColumn<Order, String> tabOrderStatus;
    @FXML
    private TableColumn<Order, String> tabOrderProdName;
    @FXML
    private TableColumn<Order, String> tabOrderProdBegin;
    @FXML
    private TableColumn<Order, String> tabOrderProdEnd;
    @FXML
    private TableView tableViewOrderPicker;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePickerOrderDate.setValue(LocalDate.now());
    }    

    @FXML
    private void handleMESLogin(ActionEvent event) {
        tabOrders.setDisable(false);
        tabScada.setDisable(false);
        tabGrowthProfiles.setDisable(false);
        tabStatistics.setDisable(false);
    }

    @FXML
    private void handleFetchOrdersFromDate(ActionEvent event) {
        LocalDate date = datePickerOrderDate.getValue(); 
        MES.setDate(date);
        this.setOrderTableView(MES.fetchOrders());
    }
    
    private void setOrderTableView(List orders) {
        ObservableList<Order> orderList = FXCollections.observableArrayList(orders);
        tableViewOrderPicker.setItems(orderList);
        
        tabOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabOrderFetchedTime.setCellValueFactory(new PropertyValueFactory<>("fetchedTime"));
        tabOrderQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tabOrderStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tabOrderProdName.setCellValueFactory(new PropertyValueFactory<>("productionName"));
        tabOrderProdBegin.setCellValueFactory(new PropertyValueFactory<>("productionBegin"));
        tabOrderProdEnd.setCellValueFactory(new PropertyValueFactory<>("productionEnd"));
        
        
        
    }
    
}
