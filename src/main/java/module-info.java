module com.eventually {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.mail;
    requires java.sql;
    requires activation;

    opens com.eventually.view to javafx.fxml;
    opens com.eventually to javafx.fxml;

    exports com.eventually;
    exports com.eventually.view;
}