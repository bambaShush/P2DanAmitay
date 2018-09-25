package War.View;

import War.BusinessLogic.WarControllerFacade;

import java.util.concurrent.ExecutionException;

public abstract class AbstractWarView implements ViewFacade{
    enum Menu {ADD_LAUNCHER, ADD_LAUNCHER_DESTRUCTOR, ADD_MISSILE_DESTRUCTOR, LAUNCH_MISSILE,
        DESTRUCT_LAUNCHER_DESTRUCTOR, DESTRUCT_MISSILE, DESTRUCT_LAUNCHER, SHOW_STATS, EXIT}
    private WarControllerFacade controller;

    public static final String MENU[] ={
            "Add Launcher",
            "Add Launcher Destructor",
            "Add Missile Destructor",
            "Launch Missile",
            "Destruct a Launcher Destructor(???)",
            "Destruct Missile",
            "Destruct Launcher",
            "Show Statistics",
            "Exit"
    };

    public String menuToString(Menu menu){
        return MENU[menu.ordinal()];
    }

    public AbstractWarView(WarControllerFacade controller) {
        setController(controller);
    }

    public abstract void start();

    public WarControllerFacade getController() {
        return controller;
    }

    private void setController(WarControllerFacade controller) {
        this.controller = controller;
    }

    public void handleChoice(Menu menuChoice) throws ExecutionException, InterruptedException {
        switch (menuChoice){
            case ADD_LAUNCHER:
                addLauncher();
                break;
            case ADD_LAUNCHER_DESTRUCTOR:
                addLauncherDestructor();
                break;
            case ADD_MISSILE_DESTRUCTOR:
                addMissileDestructor();
                break;
            case LAUNCH_MISSILE:
                launchMissile();
                break;
            case DESTRUCT_LAUNCHER_DESTRUCTOR:
                //Canceled
                break;
            case DESTRUCT_MISSILE:
                destructMissile();
                break;
            case DESTRUCT_LAUNCHER:
                destructLauncher();
                break;
            case SHOW_STATS:
                showStatistics();
                break;
            case EXIT:
                exit();
                break;
        }
    }
}
