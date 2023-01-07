module org.pawles.checkers.checkers {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens org.pawles.checkers.checkers to javafx.fxml;
    exports org.pawles.checkers.checkers;
    exports org.pawles.checkers.objects;
    exports org.pawles.checkers.client;
}