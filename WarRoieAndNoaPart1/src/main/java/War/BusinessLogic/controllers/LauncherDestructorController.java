package War.BusinessLogic.controllers;

import War.BusinessLogic.WarController;
import War.Entities.Launcher;
import War.Entities.LauncherDestructor;

public class LauncherDestructorController extends DestructorController<Launcher, LauncherDestructor, LauncherController>{

    public LauncherDestructorController(LauncherDestructor launcherDestructor){
        super(launcherDestructor);
    }

	@Override
	protected void signalAboutSuccess(LauncherDestructor destructor, Launcher destructible) {
		WarController.getInstance().signalAboutLauncherDestructionAttempt(destructor, destructible, true);
	}

	@Override
	protected void signalAboutFailure(LauncherDestructor destructor, Launcher destructible) {
		WarController.getInstance().signalAboutLauncherDestructionAttempt(destructor, destructible, false);

	}

}
