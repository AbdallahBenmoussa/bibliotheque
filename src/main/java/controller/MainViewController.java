package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import model.Adherent;
import model.Emprunt;
import model.Livre;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML private TextField nomField, prenomField, adresseField, matriculeField;
    @FXML private TextField livreNumeroField, titreField, auteurField, editeurField, anneeField;
    @FXML private TextField empruntMatriculeField, empruntLivreField;

    @FXML private TableView<Adherent> adherentsTable;
    @FXML private TableView<Livre> livresTable;
    @FXML private TableView<Emprunt> empruntsTable;
    @FXML private TableColumn<Adherent, Integer> adherentMatriculeColumn;
    @FXML private TableColumn<Adherent, String> adherentNomColumn;
    @FXML private TableColumn<Adherent, String> adherentPrenomColumn;
    @FXML private TableColumn<Adherent, String> adherentDateInscriptionColumn;
    @FXML private TableColumn<Adherent, Integer> adherentEmpruntsColumn;

    @FXML private TableColumn<Livre, Integer> livreNumeroColumn;
    @FXML private TableColumn<Livre, String> livreTitreColumn;
    @FXML private TableColumn<Livre, String> livreAuteurColumn;
    @FXML private TableColumn<Livre, Integer> livreAnneeColumn;
    @FXML private TableColumn<Livre, String> livreDisponibleColumn;
    @FXML private TableColumn<Livre, String> livreActionsColumn;

    @FXML private TableColumn<Emprunt, Integer> empruntIdColumn;
    @FXML private TableColumn<Emprunt, String> empruntLivreColumn;
    @FXML private TableColumn<Emprunt, String> empruntAdherentColumn;
    @FXML private TableColumn<Emprunt, String> empruntDateColumn;
    @FXML private TableColumn<Emprunt, String> empruntDateRetourColumn;
    @FXML private TableColumn<Emprunt, String> empruntActionsColumn;

    private BibliothequeController bibliothequeController;
    private ObservableList<Adherent> adherentsList;
    private ObservableList<Livre> livresList;
    private ObservableList<Emprunt> empruntsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bibliothequeController = new BibliothequeController();
        initializeTables();
        refreshData();
    }

    private void initializeTables() {
        adherentsList = FXCollections.observableArrayList();
        livresList = FXCollections.observableArrayList();
        empruntsList = FXCollections.observableArrayList();

        adherentsTable.setItems(adherentsList);
        livresTable.setItems(livresList);
        empruntsTable.setItems(empruntsList);

        adherentMatriculeColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        adherentNomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adherentPrenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        adherentDateInscriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate_ins() != null
                        ? cellData.getValue().getDate_ins().toString()
                        : ""));
        adherentEmpruntsColumn.setCellValueFactory(new PropertyValueFactory<>("nb_emprunts_encours"));

        livreNumeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        livreTitreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        livreAuteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        livreAnneeColumn.setCellValueFactory(new PropertyValueFactory<>("annee_publication"));
        livreDisponibleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isDispo() ? "Oui" : "Non"));
        livreActionsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(""));
        livreActionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button rendreButton = new Button("Rendre");
            private final HBox container = new HBox(rendreButton);

            {
                container.setAlignment(Pos.CENTER);
                rendreButton.setOnAction(event -> {
                    Livre livre = getTableView().getItems().get(getIndex());
                    handleLivreRetour(livre);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Livre livre = getTableView().getItems().get(getIndex());
                    rendreButton.setDisable(livre == null || livre.isDispo());
                    setGraphic(container);
                }
                setText(null);
            }
        });

        empruntIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        empruntLivreColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getLivre() != null ? cellData.getValue().getLivre().getTitre() : ""));
        empruntAdherentColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getAdherent() != null ? cellData.getValue().getAdherent().toString() : ""));
        empruntDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getDate_emprunt() != null ? cellData.getValue().getDate_emprunt().toString() : ""));
        empruntDateRetourColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getDate_restitution_prevue() != null
                                ? cellData.getValue().getDate_restitution_prevue().toString()
                                : ""));
        empruntActionsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(""));
        empruntActionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button restituerButton = new Button("Restituer");
            private final Button prolongerButton = new Button("Prolonger");
            private final HBox container = new HBox(10, restituerButton, prolongerButton);

            {
                container.setAlignment(Pos.CENTER);
                restituerButton.setOnAction(event -> {
                    Emprunt emprunt = getTableView().getItems().get(getIndex());
                    handleRestitution(emprunt, false);
                });
                prolongerButton.setOnAction(event -> {
                    Emprunt emprunt = getTableView().getItems().get(getIndex());
                    handleProlongation(emprunt, false);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
                setText(null);
            }
        });
    }

    private void refreshData() {
        adherentsList.setAll(bibliothequeController.getTousLesAdherents());

        livresList.setAll(bibliothequeController.getTousLesLivres());

        empruntsList.setAll(bibliothequeController.getEmpruntsEnCours());
    }

    @FXML
    private void inscrireAdherent() {
        try {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String adresse = adresseField.getText();
            int matricule = Integer.parseInt(matriculeField.getText());

            if (nom.isEmpty() || prenom.isEmpty()) {
                showAlert("Erreur", "Le nom et prénom sont obligatoires.");
                return;
            }

            bibliothequeController.inscrireAdherent(nom, prenom, adresse, matricule);
            refreshData();
            clearAdherentFields();
            showAlert("Succès", "Adhérent inscrit avec succès.");

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le matricule doit être un nombre.");
        }
    }

    @FXML
    private void ajouterLivre() {
        try {
            int numero = Integer.parseInt(livreNumeroField.getText());
            String titre = titreField.getText();
            String auteur = auteurField.getText();
            int annee = Integer.parseInt(anneeField.getText());

            if (titre.isEmpty() || auteur.isEmpty()) {
                showAlert("Erreur", "Le titre et l'auteur sont obligatoires.");
                return;
            }

            bibliothequeController.ajouterLivre(numero, titre, auteur ,annee);
            refreshData();
            clearLivreFields();
            showAlert("Succès", "Livre ajouté avec succès.");

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le numéro et l'année doivent être des nombres.");
        }
    }

    @FXML
    private void realiserEmprunt() {
        try {
            int matricule = Integer.parseInt(empruntMatriculeField.getText());
            int numeroLivre = Integer.parseInt(empruntLivreField.getText());

            boolean success = bibliothequeController.realiserEmprunt(matricule, numeroLivre);

            if (success) {
                refreshData();
                clearEmpruntFields();
                showAlert("Succès", "Emprunt réalisé avec succès.");
            } else {
                showAlert("Erreur", "Impossible de réaliser l'emprunt. Vérifiez la disponibilité du livre et les droits de l'adhérent.");
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le matricule et le numéro de livre doivent être des nombres.");
        }
    }

    @FXML
    private void restituerLivre() {
        Emprunt selectedEmprunt = empruntsTable.getSelectionModel().getSelectedItem();
        handleRestitution(selectedEmprunt, true);
    }

    @FXML
    private void prolongerEmprunt() {
        Emprunt selectedEmprunt = empruntsTable.getSelectionModel().getSelectedItem();
        handleProlongation(selectedEmprunt, true);
    }

    private void clearAdherentFields() {
        nomField.clear();
        prenomField.clear();
        adresseField.clear();
        matriculeField.clear();
    }

    private void clearLivreFields() {
        livreNumeroField.clear();
        titreField.clear();
        auteurField.clear();
        anneeField.clear();
    }

    private void clearEmpruntFields() {
        empruntMatriculeField.clear();
        empruntLivreField.clear();
    }

    private void handleRestitution(Emprunt emprunt, boolean showSelectionWarning) {
        if (emprunt == null) {
            if (showSelectionWarning) {
                showAlert("Erreur", "Veuillez sélectionner un emprunt à restituer.");
            }
            return;
        }

        boolean success = bibliothequeController.restituerLivre(emprunt.getId());
        if (success) {
            refreshData();
            showAlert("Succès", "Livre restitué avec succès.");
        } else {
            showAlert("Erreur", "Erreur lors de la restitution.");
        }
    }

    private void handleProlongation(Emprunt emprunt, boolean showSelectionWarning) {
        if (emprunt == null) {
            if (showSelectionWarning) {
                showAlert("Erreur", "Veuillez sélectionner un emprunt à prolonger.");
            }
            return;
        }

        boolean success = bibliothequeController.prolongerEmprunt(emprunt.getId());
        if (success) {
            refreshData();
            showAlert("Succès", "Emprunt prolongé avec succès.");
        } else {
            showAlert("Erreur", "Erreur lors de la prolongation.");
        }
    }

    private void handleLivreRetour(Livre livre) {
        if (livre == null) {
            showAlert("Erreur", "Aucun livre sélectionné.");
            return;
        }

        if (livre.isDispo()) {
            showAlert("Information", "Ce livre est déjà disponible.");
            return;
        }

        boolean success = bibliothequeController.restituerLivreParNumeroLivre(livre.getNumero());
        if (success) {
            refreshData();
            showAlert("Succès", "Livre marqué comme disponible.");
        } else {
            showAlert("Erreur", "Impossible de marquer le livre comme disponible.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}