module CA {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;
    //   requires javafx.swing;


    opens Main to javafx.fxml;
    exports Main;
    exports Models;
    opens Models to javafx.fxml;
    exports Utilities;
    opens Utilities to javafx.fxml;
}