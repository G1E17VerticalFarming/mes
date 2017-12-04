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
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import mes.domain.IMes;
import mes.domain.Order;
import mes.domain.SingletonMES;
import shared.GrowthProfile;

/**
 *
 * @author chris
 */
public class MainController implements Initializable {

    @FXML
    private AnchorPane tab_order_name;
    @FXML
    private TableColumn<?, ?> tab_sequences_sequence;
    @FXML
    private TableColumn<?, ?> tab_sequences_type;
    @FXML
    private TableColumn<?, ?> tab_sequences_time;
    @FXML
    private TableColumn<?, ?> tab_sequences_value;
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
    private TableView<Order> tableViewOrderPicker;
    @FXML
    private TextField txtFieldPrepOrder;
    @FXML
    private TextField txtFieldPrepID;
    @FXML
    private ComboBox<?> comboBoxPrepOrderStatus;
    @FXML
    private ComboBox<?> comboBoxPrepGrowthProfile;
    @FXML
    private TextField txtFieldPrepAmount;
    @FXML
    private Tab pickedOrderTab;
    @FXML
    private TabPane orderTabPane;
    @FXML
    private TableView<GrowthProfile> tableViewGrowthProfile;
    @FXML
    private TableColumn<GrowthProfile, Integer> tabGpId;
    @FXML
    private TableColumn<GrowthProfile, String> tabGpName;
    @FXML
    private TextField txtFieldGpCelcius;
    @FXML
    private TextField txtFieldGpWaterLevel;
    @FXML
    private TextField txtFieldGpMoisture;
    @FXML
    private TextField txtFieldGpName;
    @FXML
    private TableView<?> tableViewSequences;
    @FXML
    private TextField txtFieldGpTime;
    @FXML
    private ComboBox<?> comboBoxGpType;
    @FXML
    private TextField txtFieldGpValue;
    @FXML
    private Label lblSequenceSelected;
    @FXML
    private TableView<?> tableViewDataLogs;
    @FXML
    private TableColumn<?, ?> tabDataId;
    @FXML
    private TableColumn<?, ?> tabDataProductionBlock;
    @FXML
    private TableColumn<?, ?> tabDataType;
    @FXML
    private TableColumn<?, ?> tabDataTimestamp;
    @FXML
    private TableColumn<?, ?> tabDataCommand;
    @FXML
    private TableColumn<?, ?> tabDataValue;
    @FXML
    private ComboBox<?> comboBoxLogFilter;
    @FXML
    private ComboBox<?> comboBoxLogType;
    @FXML
    private DatePicker datePickerLog;
    @FXML
    private ComboBox<?> comboBoxLogCmd;
    @FXML
    private TextArea txtAreaLog;
    @FXML
    private ComboBox<?> comboBoxLogProdBlock;
    @FXML
    private Tab tabNewDataLog;
    @FXML
    private TabPane statisticsTabPane;
    @FXML
    private PasswordField pswdFieldPassword;
    @FXML
    private TextField txtFieldUsername;
    @FXML
    private Label lblLoginStatus;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnLogout;
    @FXML
    private ComboBox<?> comboBoxPrepProdBlock;
    @FXML
    private Label lblSuggestedBlock;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePickerOrderDate.setValue(LocalDate.now());
        this.setGrowthProfileTableView(MES.fetchGrowthProfiles());
    }

    @FXML
    private void handleMESLogin(ActionEvent event) {
        if (MES.loginCertified(txtFieldUsername.getText(), pswdFieldPassword.getText())) {
            lblLoginStatus.setText("");

            // Enable tabs
            tabOrders.setDisable(false);
            tabScada.setDisable(false);
            tabGrowthProfiles.setDisable(false);
            tabStatistics.setDisable(false);

            // Enable logout button
            btnLogout.setVisible(true);

            // Hide login elements
            btnLogin.setVisible(false);
            lblLoginStatus.setVisible(false);
            txtFieldUsername.setVisible(false);
            pswdFieldPassword.setVisible(false);

        } else {
            lblLoginStatus.setText("ERROR! Wrong username or password.");
        }
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

    private void setGrowthProfileTableView(List growthProfiles) {
        ObservableList<GrowthProfile> growthProfileList = FXCollections.observableArrayList(growthProfiles);
        tableViewGrowthProfile.setItems(growthProfileList);

        tabGpId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabGpName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    @FXML
    private void handleSelectOrder(ActionEvent event) {
        Order currentOrder = tableViewOrderPicker.getSelectionModel().getSelectedItem();
        
        // Check that Order is not null, as that causes errors
        if(currentOrder != null) {
        pickedOrderTab.setText(currentOrder.getProductionName());

        txtFieldPrepOrder.setText(currentOrder.getProductionName());
        txtFieldPrepAmount.setText(Integer.toString(currentOrder.getQuantity()));
        txtFieldPrepID.setText(Integer.toString(currentOrder.getId()));

        pickedOrderTab.setDisable(false);
        orderTabPane.getSelectionModel().select(pickedOrderTab);
        }
    }

    @FXML
    private void handlePrepareOrder(ActionEvent event) {
        MES.prepareOrder();
    }

    @FXML
    private void handleSelectGrowthProfile(ActionEvent event) {
        GrowthProfile currentGrowthProfile = tableViewGrowthProfile.getSelectionModel().getSelectedItem();

        txtFieldGpName.setText(currentGrowthProfile.getName());
        txtFieldGpCelcius.setText(Integer.toString(currentGrowthProfile.getTemperature()));
        txtFieldGpWaterLevel.setText(Integer.toString(currentGrowthProfile.getWaterLevel()));
        txtFieldGpMoisture.setText(Integer.toString(currentGrowthProfile.getMoisture()));
    }

    @FXML
    private void handleAddSequence(ActionEvent event) {
    }

    @FXML
    private void handleDeleteSequence(ActionEvent event) {
    }

    @FXML
    private void handleSaveGrowthProfile(ActionEvent event) {
    }

    @FXML
    private void handleDeleteGrowthProfile(ActionEvent event) {
    }

    @FXML
    private void handleNewDataLog(ActionEvent event) {
        datePickerLog.setValue(LocalDate.now());
        tabNewDataLog.setDisable(false);
        statisticsTabPane.getSelectionModel().select(tabNewDataLog);
    }

    @FXML
    private void handleSaveDataLog(ActionEvent event) {
    }

    @FXML
    private void handleMESLogout(ActionEvent event) {
        // Disable tabs
        tabOrders.setDisable(true);
        tabScada.setDisable(true);
        tabGrowthProfiles.setDisable(true);
        tabStatistics.setDisable(true);

        // Disable logout button
        btnLogout.setVisible(false);

        // Show login elements
        btnLogin.setVisible(true);
        lblLoginStatus.setVisible(true);
        txtFieldUsername.setVisible(true);
        pswdFieldPassword.setVisible(true);
        
        // Reset login input fields
        txtFieldUsername.setText("");
        pswdFieldPassword.setText("");
    }

}
