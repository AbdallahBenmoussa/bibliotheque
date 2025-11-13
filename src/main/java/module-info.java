module com.example.tpgl {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires transitive javafx.graphics;

    opens model to javafx.fxml;
    exports model;
    exports application;
    opens application to javafx.fxml;
    exports controller;
    opens controller to javafx.fxml;
}