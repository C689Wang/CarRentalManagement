package carrental;

import carrental.menu.StartMenu;


public class Main {
    static String databaseName;

    public static void main(String[] args) {
        try {
            // Database name is based on command line arguments.
            // Arguments should be -databaseFileName followed by actual name for database
            for (int i = 0; i < args.length; i++) {
                if ("-databaseFileName".equals(args[i])) {
                    if ((i + 1) < args.length) {
                        databaseName = args[i + 1];
                        break;
                    } else {
                        databaseName = "test";
                    }
                } else {
                    databaseName = "test";
                }
            }

            StartMenu actionMenu = new StartMenu(databaseName);
            actionMenu.menuAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}