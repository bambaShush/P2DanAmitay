package War.BusinessLogic.controllers;

import War.BusinessLogic.WarController;
import War.Entities.Missile;
import War.Entities.MissileDestructor;

public class MissileDestructorController extends DestructorController<Missile, MissileDestructor, MissileController>{

    public MissileDestructorController(MissileDestructor missileDestructor){
        super(missileDestructor);
    }

	@Override
	protected void signalAboutSuccess(MissileDestructor destructor, Missile destructible) {
		WarController.getInstance().signalAboutMissileDestructionAttempt(destructor, destructible, true);
	}

	@Override
	protected void signalAboutFailure(MissileDestructor destructor, Missile destructible) {
		WarController.getInstance().signalAboutMissileDestructionAttempt(destructor, destructible, false);
	}

   
}