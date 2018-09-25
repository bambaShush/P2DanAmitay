package War.View;

import War.BusinessLogic.Statistics;
import War.BusinessLogic.WarControllerFacade;
import War.Entities.*;
import War.View.subscribers.ConsoleViewSubscriber;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class ConsoleView extends AbstractWarView {
    private Scanner scanner = new Scanner(System.in);
    
    
    public ConsoleView(WarControllerFacade warControllerFacade){
        super(warControllerFacade);
        new ConsoleViewSubscriber(warControllerFacade, this);
        
    }

    public void printMenu(){
        for(int i = 0 ; i < MENU.length ; i++){
            System.out.println((i+ 1) + ") " + MENU[i]);
        }
        System.out.println();
    }

    @Override
    public void start(){
        int choice = 0;

        do {
            printMenu();
            choice = scanner.nextInt();
            Menu menuChoice = Menu.values()[choice - 1];
            try {
                handleChoice(menuChoice);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }while(choice != 9);

    }

    @Override
    public void addLauncher() {
        System.out.println("Launcher hides while not launching? (y - yes, n - no, anything else - random");
        char hideable = scanner.next().charAt(0);
        Launcher launcher;
        switch (hideable){
            case 'y': case 'Y':
                launcher = new HideableLauncher();
                break;

            case 'n': case 'N':
                launcher = new Launcher();
                break;

            default:
                launcher = new Random().nextBoolean() ? new HideableLauncher() : new Launcher();
                break;
        }

        getController().addLauncher(launcher);
    }

    @Override
    public void addLauncherDestructor() {
        System.out.println("Creating Launcher Destructor, please choose destructor type:\n1)Plane\n2)Ship");
        int destructorType = scanner.nextInt();

        LauncherDestructor.DestructorType type  = LauncherDestructor.DestructorType.values()[--destructorType];
        LauncherDestructor launcherDestructor = new LauncherDestructor(type);
        getController().addLauncherDestructor(launcherDestructor);
    }

    @Override
    public void addMissileDestructor() {
        System.out.println("Creating missile destructor...");
        MissileDestructor missileDestructor = new MissileDestructor();
        getController().addMissileDestructor(missileDestructor);
    }

    @Override
    public void launchMissile() throws NoSuchElementException, ExecutionException, InterruptedException {
        String destination;
        double potentialDamage;
        Launcher launcherToLaunchWith = null;
        Set<Launcher> launchers = getController().retrieveLaunchers();

        if(launchers.isEmpty())
            throw new NoSuchElementException("No launchers currently, please add launchers before launching missiles, " +
                    "you can add a new launcher choosing " + (Menu.ADD_LAUNCHER.ordinal() + 1) + " in the main menu");

        launcherToLaunchWith = getWeaponFromUser(launchers, Launcher.class, "launching a missile");

        if(launcherToLaunchWith == null)
            return;
        else
            System.out.println("You Chose:\n"+launcherToLaunchWith);

        System.out.println("Please enter the missile destination:");
        destination = scanner.next();
        System.out.println("Please enter the potential damage:");
        potentialDamage = scanner.nextDouble();
        Launcher finalLauncherToLaunchWith = launcherToLaunchWith;
        Thread thread = new Thread(() -> {
            try {
                getController().addMissile(finalLauncherToLaunchWith, new Destination(destination), potentialDamage);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
        });
        thread.start();
    }

    private <T extends Weapon> T getWeaponFromUser(Set<T> weapons, Class<T> type, String action){
        T weaponToUse = null;
        System.out.println("For "+ action + " please select a "+ type.getSimpleName().toLowerCase() + ":");

        if(weapons.isEmpty()){
            System.out.println("No "+type.getSimpleName()+"s! ");
            return null;
        }

        for(T weapon : weapons)
            System.out.println(weapon);

        do {
            System.out.println("Please enter "+ type.getSimpleName().toLowerCase() +" ID or -1 to cancel the operation:");
            String id = scanner.next();

            if(id.equals("-1")){
                return null;
            }

            for (T weapon : weapons) {
                if (weapon.getId().equals(id)) {
                    weaponToUse = weapon;
                }
            }

            if(weaponToUse == null){
                System.out.println("Invalid " + type.getClass().getSimpleName().toLowerCase() + " ID please try again");
            }

        }while(weaponToUse == null);

        return weaponToUse;
    }

    @Override
    public void destructMissile() {
        Set<MissileDestructor> missileDestructors = getController().retrieveMissileDestructors();

        MissileDestructor missileDestructor = getWeaponFromUser(missileDestructors, MissileDestructor.class, "destructing a missile");

        Set<Missile> missiles = getController().retrieveActiveMissiles();
        Missile missile = getWeaponFromUser(missiles, Missile.class, "destructing a missile");
        getController().destructMissile(missileDestructor, missile);
    }

    @Override
    public void destructLauncher() {
        Set<LauncherDestructor> launcherDestructors =
                getController().retrieveLauncherDestructors();

        LauncherDestructor launcherDestructor =
                getWeaponFromUser(launcherDestructors, LauncherDestructor.class, "destructing a launcher");

        Set<Launcher> launchers = getController().retrieveLaunchers();
        Launcher launcher = getWeaponFromUser(launchers, Launcher.class, "destructing a launcher");
        
        try {
			getController().destructLauncher(launcherDestructor, launcher);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }

    @Override
    public void showStatistics() {
        Statistics statistics = getController().getStatistics();

        System.out.println(statistics);
    }

    @Override
    public void exit() {
    	scanner.close();
        getController().exit();
    }


}
