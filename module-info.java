//module apps {
//	requires javafx.controls;
//	requires javafx.base;
//	requires java.sql;
//	
//	opens application to javafx.graphics, javafx.fxml;
//}
//module apps {
//    requires javafx.controls;
//    requires javafx.base;
//    //requires javafx.fxml;
//    requires java.sql;
//
//    opens application to javafx.fxml, javafx.graphics, javafx.base;
//    //opens data to javafx.base;
//}
module apps {
requires javafx.controls;
requires java.sql;
requires javafx.graphics;
requires java.desktop;

opens application to javafx.graphics, javafx.fxml;
}