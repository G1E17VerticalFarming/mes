/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import mes.domain.Order;
import mes.domain.SingletonMES;
import mes.domain.Status;
import mes.domain.interfaces.DomainReadLogin;
import mes.domain.interfaces.DomainReadProduction;
import mes.domain.interfaces.DomainReadWriteDataLog;
import mes.domain.interfaces.DomainReadWriteGrowthProfile;
import mes.domain.interfaces.DomainReadWriteOrder;
import mes.domain.interfaces.DomainReadWriteProductionBlock;
import mes.domain.interfaces.DomainReadWriteScadaConnections;
import mes.domain.interfaces.DomainWriteDate;
import shared.GrowthProfile;
import shared.Light;
import shared.Log;
import shared.ProductionBlock;

/**
 *
 * @author chris
 */
public class MainController implements Initializable {

    private DomainReadLogin loginMes;
    private DomainReadProduction prodMes;
    private DomainReadWriteDataLog datMes;
    private DomainReadWriteGrowthProfile profMes;
    private DomainReadWriteOrder orderMes;
    private DomainReadWriteProductionBlock prodBlockMes;
    private DomainReadWriteScadaConnections scadConnMes;
    private DomainWriteDate dateMes;
    
    @FXML
    private AnchorPane tab_order_name;
    @FXML
    private Button btnFetchOrders;
    @FXML
    private DatePicker datePickerOrderDate;
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
    private ComboBox<Status> comboBoxPrepOrderStatus;
    @FXML
    private ComboBox<GrowthProfile> comboBoxPrepGrowthProfile;
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
    private TableView<Light> tableViewSequences;
    @FXML
    private TextField txtFieldGpTime;
    @FXML
    private ComboBox<String> comboBoxGpType;
    @FXML
    private TextField txtFieldGpValue;
    @FXML
    private TableView<Log> tableViewDataLogs;
    @FXML
    private TableColumn<Log, Integer> tabDataId;
    @FXML
    private TableColumn<Log, Integer> tabDataProductionBlock;
    @FXML
    private TableColumn<Log, String> tabDataType;
    @FXML
    private TableColumn<Log, Integer> tabDataTimestamp;
    @FXML
    private TableColumn<Log, Integer> tabDataCommand;
    @FXML
    private TableColumn<Log, String> tabDataValue;
    @FXML
    private ComboBox<String> comboBoxLogFilter;
    @FXML
    private TextArea txtAreaLog;
    @FXML
    private ComboBox<ProductionBlock> comboBoxLogProdBlock;
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
    private Label lblSuggestedBlock;
    @FXML
    private ListView<String> listViewScadaConnections;
    @FXML
    private Tab tabScadaCon;
    @FXML
    private Tab tabNewScadaCon;
    @FXML
    private TextField txtFieldScadaIP;
    @FXML
    private TextField txtFieldScadaPort;
    @FXML
    private Button btnAddScadaConnection;
    @FXML
    private TabPane scadaTabPane;

    private GrowthProfile currentGrowthProfile = new GrowthProfile();
    private Order currentOrder;
    
    @FXML
    private Label lblSelectedGPID;
    @FXML
    private TextField txtFieldPrepProdBlock;
    @FXML
    private TableColumn<?, ?> tabSequencesType;
    @FXML
    private TableColumn<?, ?> tabSequencesTime;
    @FXML
    private TableColumn<?, ?> tabSequencesValue;
    @FXML
    private Tab tabDataLog;
    @FXML
    private Button btnPrepOrder;
    @FXML
    private Tab orderTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SingletonMES mainMes = SingletonMES.getInstance();
        this.datMes = mainMes;
        this.dateMes = mainMes;
        this.loginMes = mainMes;
        this.orderMes = mainMes;
        this.prodBlockMes = mainMes;
        this.prodMes = mainMes;
        this.profMes = mainMes;
        this.scadConnMes = mainMes;
        
        datePickerOrderDate.setValue(LocalDate.now());

        // Fetch data for combo boxes
        comboBoxPrepOrderStatus.getItems().addAll(orderMes.fetchStatuses());
        comboBoxPrepGrowthProfile.getItems().addAll(profMes.fetchGrowthProfiles());
        comboBoxLogFilter.getItems().addAll(datMes.fetchLogFilters());
        comboBoxGpType.getItems().addAll(profMes.getGrowthProfileLightTypes());
        comboBoxLogProdBlock.getItems().addAll(prodBlockMes.fetchActiveProductionBlocks());

        comboBoxLogFilter.getSelectionModel().selectFirst();
        comboBoxGpType.getSelectionModel().selectFirst();
        comboBoxLogProdBlock.getSelectionModel().selectFirst();

        // Fetch data for list/tableviews
        this.setGrowthProfileTableView(profMes.fetchGrowthProfiles());
        this.setScadaConnectionsListView(scadConnMes.fetchScadaConnections());
        this.setDataLogsTableView(datMes.fetchDataLogs(comboBoxLogFilter.getSelectionModel().getSelectedItem()));

        // Add listeners to limit certain textfields
        this.addScadaPortListener();
        this.addGrowthProfileListeners();
    }

    @FXML
    private void handleMESLogin(ActionEvent event) {
        if (loginMes.loginCertified(txtFieldUsername.getText(), pswdFieldPassword.getText())) {
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
        dateMes.setDate(date);
        this.setOrderTableView(orderMes.fetchOrders());
    }

    private void addScadaPortListener() {
        // Scada port text field - Forces the text field to only accept numeric input
        txtFieldScadaPort.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtFieldScadaPort.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void addGrowthProfileListeners() {
        txtFieldGpWaterLevel.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtFieldGpWaterLevel.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        txtFieldGpMoisture.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtFieldGpMoisture.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        txtFieldGpValue.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtFieldGpValue.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void setOrderTableView(List orders) {
        ObservableList<Order> orderList = FXCollections.observableArrayList(orders);
        tableViewOrderPicker.setItems(orderList);

        tabOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabOrderFetchedTime.setCellValueFactory(new PropertyValueFactory<>("fetchedTime"));
        tabOrderQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tabOrderStatus.setCellValueFactory(new PropertyValueFactory<>("status".toString())); // Not actually calling toString on a string, but instead on a Status object
        tabOrderProdName.setCellValueFactory(new PropertyValueFactory<>("productionName"));
        tabOrderProdBegin.setCellValueFactory(new PropertyValueFactory<>("productionBegin"));
        tabOrderProdEnd.setCellValueFactory(new PropertyValueFactory<>("productionEnd"));
    }

    private void setScadaConnectionsListView(List scadaConnections) {
        ObservableList<String> scadaConnectionsList = FXCollections.observableArrayList(scadaConnections);

        listViewScadaConnections.getItems().addAll(scadaConnectionsList);
    }

    private void setDataLogsTableView(List dataLogs) {
        ObservableList<Log> logs = FXCollections.observableArrayList(dataLogs);
        tableViewDataLogs.setItems(logs);

        tabDataId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabDataProductionBlock.setCellValueFactory(new PropertyValueFactory<>("block"));
        tabDataTimestamp.setCellValueFactory(new PropertyValueFactory<>("unixTimestamp"));
        tabDataCommand.setCellValueFactory(new PropertyValueFactory<>("cmd"));
        tabDataValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        tabDataType.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    private void setGrowthProfileTableView(List growthProfiles) {
        ObservableList<GrowthProfile> growthProfileList = FXCollections.observableArrayList(growthProfiles);
        tableViewGrowthProfile.setItems(growthProfileList);

        tabGpId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabGpName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    @FXML
    private void handleSelectOrder(ActionEvent event) {
        currentOrder = tableViewOrderPicker.getSelectionModel().getSelectedItem();

        // Check that Order is not null, as that causes errors
        if (currentOrder != null) {
            comboBoxPrepOrderStatus.getSelectionModel().select(currentOrder.getStatus().getId() - 1);

            pickedOrderTab.setText(currentOrder.getProductionName());

            txtFieldPrepOrder.setText(currentOrder.getProductionName());
            txtFieldPrepAmount.setText(Integer.toString(currentOrder.getQuantity()));
            txtFieldPrepID.setText(Integer.toString(currentOrder.getId()));

            pickedOrderTab.setDisable(false);
            txtFieldPrepOrder.setDisable(false);
            txtFieldPrepAmount.setDisable(false);
            comboBoxPrepOrderStatus.setDisable(false);
            comboBoxPrepGrowthProfile.setDisable(false);
            btnPrepOrder.setDisable(false);
            orderTabPane.getSelectionModel().select(pickedOrderTab);

            if (currentOrder.getStatus().getValue().equals("Færdig")) {
                comboBoxPrepGrowthProfile.getSelectionModel().select(prodMes.fetchProduction(currentOrder).getGrowthProfile());
                txtFieldPrepProdBlock.setText(Integer.toString(prodMes.fetchProduction(currentOrder).getBlock().getId()));
                txtFieldPrepOrder.setDisable(true);
                txtFieldPrepAmount.setDisable(true);
                comboBoxPrepOrderStatus.setDisable(true);
                comboBoxPrepGrowthProfile.setDisable(true);
                btnPrepOrder.setDisable(true);
            } else if(currentOrder.getStatus().getValue().equals("Under produktion")) {
                comboBoxPrepGrowthProfile.getSelectionModel().select(prodMes.fetchProduction(currentOrder).getGrowthProfile());
                txtFieldPrepOrder.setDisable(true);
                txtFieldPrepAmount.setDisable(true);
                comboBoxPrepGrowthProfile.setDisable(true);
                txtFieldPrepProdBlock.setText(Integer.toString(prodMes.fetchProduction(currentOrder).getBlock().getId()));
            }
        }
    }

    @FXML
    private void handlePrepareOrder(ActionEvent event) {
        orderMes.prepareOrder(currentOrder, comboBoxPrepOrderStatus.getSelectionModel().getSelectedItem(),
                comboBoxPrepGrowthProfile.getSelectionModel().getSelectedItem(),Integer.parseInt(txtFieldPrepProdBlock.getText()));
        
        pickedOrderTab.setDisable(true);
        orderTabPane.getSelectionModel().select(orderTab);
    }

    @FXML
    private void handleSelectGrowthProfile(ActionEvent event) {
        currentGrowthProfile = tableViewGrowthProfile.getSelectionModel().getSelectedItem();

        txtFieldGpName.setText(currentGrowthProfile.getName());
        txtFieldGpCelcius.setText(Integer.toString(currentGrowthProfile.getTemperature()));
        txtFieldGpWaterLevel.setText(Integer.toString(currentGrowthProfile.getWaterLevel()));
        txtFieldGpMoisture.setText(Integer.toString(currentGrowthProfile.getMoisture()));

        profMes.setTempGrowthProfileLights(currentGrowthProfile.getLightSequence());

        this.setGpLightTableView(profMes.getTempGrowthProfileLights());

        lblSelectedGPID.setText(Integer.toString(currentGrowthProfile.getId()));
    }

    @FXML
    private void handleAddSequence(ActionEvent event) {
        profMes.addGrowthProfileLight(profMes.createGrowthProfileLight(comboBoxGpType.getSelectionModel().getSelectedIndex(),
                Integer.parseInt(txtFieldGpTime.getText()), Integer.parseInt(txtFieldGpValue.getText())));

        tableViewSequences.getItems().clear();
        this.setGpLightTableView(profMes.getTempGrowthProfileLights());
    }

    @FXML
    private void handleDeleteSequence(ActionEvent event) {
        if (tableViewSequences.getSelectionModel().getSelectedItem() != null) {
            profMes.removeTempGrowthProfileLight(tableViewSequences.getSelectionModel().getSelectedIndex());

            tableViewSequences.getItems().clear();
            this.setGpLightTableView(profMes.getTempGrowthProfileLights());
        }
    }

    @FXML
    private void handleSaveGrowthProfile(ActionEvent event) {
        if (currentGrowthProfile != null) {
            currentGrowthProfile.setName(txtFieldGpName.getText());
            currentGrowthProfile.setMoisture(Integer.parseInt(txtFieldGpMoisture.getText()));
            currentGrowthProfile.setTemperature(Integer.parseInt(txtFieldGpCelcius.getText()));
            currentGrowthProfile.setWaterLevel(Integer.parseInt(txtFieldGpWaterLevel.getText()));
            profMes.saveGrowthProfile(currentGrowthProfile);
            this.updateGrowthProfilesTableView();
        }
    }

    @FXML
    private void handleDeleteGrowthProfile(ActionEvent event) {
        if (currentGrowthProfile != null) {
            profMes.deleteGrowthProfile(currentGrowthProfile.getId());
            this.updateGrowthProfilesTableView();
        }
    }

    @FXML
    private void handleNewDataLog(ActionEvent event) {
        tabNewDataLog.setDisable(false);
        statisticsTabPane.getSelectionModel().select(tabNewDataLog);
    }

    @FXML
    private void handleSaveDataLog(ActionEvent event) {
        datMes.saveDataLog(comboBoxLogProdBlock.getSelectionModel().getSelectedItem().getId(),
                txtAreaLog.getText(), comboBoxLogProdBlock.getSelectionModel().getSelectedItem().getBatchId());
        
        tableViewDataLogs.getItems().clear();
        this.setDataLogsTableView(datMes.fetchDataLogs(comboBoxLogFilter.getSelectionModel().getSelectedItem()));
        
        tabNewDataLog.setDisable(true);
        statisticsTabPane.getSelectionModel().select(tabDataLog);
        txtAreaLog.clear();
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

    @FXML
    private void handleAddNewScadaConnection(ActionEvent event) {
        tabNewScadaCon.setDisable(false);
        scadaTabPane.getSelectionModel().select(tabNewScadaCon);

    }

    @FXML
    private void handleRemoveScadaConnection(ActionEvent event) {
        if (listViewScadaConnections.getSelectionModel().getSelectedItem() != null) {
            scadConnMes.deleteScadaConnection(listViewScadaConnections.getSelectionModel().getSelectedItem());
            this.updateScadaConnectionsListView();
        }
    }

    @FXML
    private void handleAddScadaConnection(ActionEvent event) {
        scadConnMes.saveScadaConnection(txtFieldScadaIP.getText(), Integer.parseInt(txtFieldScadaPort.getText()));

        // Update List View
        this.updateScadaConnectionsListView();

        // Go back
        scadaTabPane.getSelectionModel().select(tabScadaCon);
        tabNewScadaCon.setDisable(true);
    }

    private void updateScadaConnectionsListView() {
        // Clear
        listViewScadaConnections.getItems().clear();

        // Rebuild
        this.setScadaConnectionsListView(scadConnMes.fetchScadaConnections());
    }

    private void updateGrowthProfilesTableView() {
        // Clear
        tableViewGrowthProfile.getItems().clear();

        // Rebuild
        this.setGrowthProfileTableView(profMes.fetchGrowthProfiles());
    }

    private void setGpLightTableView(List lights) {
        ObservableList<Light> lightList = FXCollections.observableArrayList(lights);
        tableViewSequences.setItems(lightList);

        tabSequencesType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tabSequencesTime.setCellValueFactory(new PropertyValueFactory<>("runTimeUnix"));
        tabSequencesValue.setCellValueFactory(new PropertyValueFactory<>("powerLevel"));
    }

    @FXML
    private void handleUpdateDataLogTableView(ActionEvent event) {
        tableViewDataLogs.getItems().clear();
        this.setDataLogsTableView(datMes.fetchDataLogs(comboBoxLogFilter.getSelectionModel().getSelectedItem()));
    }

    @FXML
    private void handleUpdateScadaConnection(ActionEvent event) {
        this.updateScadaConnectionsListView();
    }

}
