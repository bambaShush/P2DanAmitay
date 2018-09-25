package War.logging;

import War.BusinessLogic.WarController;
import War.Entities.*;
import java.io.IOException;

import java.util.logging.*;

public aspect WarLoggerAspect {
	private Logger logger = Logger.getLogger("War Log");

	public WarLoggerAspect() throws IOException {
		logger.setUseParentHandlers(false);
		Handler handler = new FileHandler("out/logging/War.log");
		handler.setFormatter(new WarLoggerFormatter());
		logger.addHandler(handler);
	}

	private String weaponPrefix(Weapon weapon) {
		return String.format("%-18s %-8s", weapon.getClass().getSimpleName(), " [" + weapon.getId() + "] ");
	}

	private <T extends Weapon> void createLogFile(T weapon) {
		try {
			String name = weaponPrefix(weapon);
			Handler handler = new FileHandler(
					"out/logging/" + weapon.getClass().getSimpleName() + " [" + weapon.getId() + "]" + ".log");
			handler.setFilter(record -> record.getMessage().contains("[" + weapon.getId() + "]"));
			handler.setFormatter(new WarLoggerFormatter());
			logger.addHandler(handler);

			logger.info(name + "CREATED");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	pointcut createLauncherFile(Launcher launcher) : execution (public void addLauncher(Launcher)) && args(launcher);

	before(Launcher launcher): createLauncherFile(launcher){
		createLogFile(launcher);
	}

	pointcut createMissileDestructorFile(MissileDestructor missileDestructor) : execution(* addMissileDestructor(MissileDestructor)) && args(missileDestructor);

	before(MissileDestructor missileDestructor): createMissileDestructorFile(missileDestructor){
		createLogFile(missileDestructor);
	}

	pointcut createLauncherDestructorFile(LauncherDestructor launcherDestructor) : execution(* addLauncherDestructor(LauncherDestructor)) && args(launcherDestructor);

	before(LauncherDestructor launcherDestructor): createLauncherDestructorFile(launcherDestructor){
		createLogFile(launcherDestructor);
	}

	pointcut launchMissileLog(Launcher launcher, Missile missile):
            execution(public void signalAboutMissileLaunched(Launcher, Missile))
            && args(launcher, missile) ;

	before(Launcher launcher, Missile missile):
    		launchMissileLog(launcher, missile){

		String logMessage = weaponPrefix(launcher) + "Launched a missile at: " + WarController.getInstance().getTime()
				+ ", Destination:" + missile.getDestination();
		logger.info(logMessage);

	}

	pointcut missileResultLog(Launcher launcher, Missile missile, boolean hit):
    	execution(public void signalAboutMissileResult(Launcher, Missile, boolean)) && args(launcher, missile, hit);

	after(Launcher launcher, Missile missile, boolean hit) :
    	missileResultLog(launcher, missile, hit){
		String logMessage = weaponPrefix(launcher) + "";

		if (hit)
			logMessage += "Missile Hit! damage: " + missile.getDamage();
		else
			logMessage += "Missile Destructed!";

		logMessage += " at: " + WarController.getInstance().getTime();

		logger.info(logMessage);
	}

	pointcut destructMissileLog(MissileDestructor missileDestructor, Missile missile, long time):
            execution (public void destructMissile(MissileDestructor, Missile, long))
                    && args(missileDestructor, missile, time);

	before(MissileDestructor missileDestructor, Missile missile, long time):
            destructMissileLog(missileDestructor, missile, time){
		String logMessage = weaponPrefix(missileDestructor) + "Try to destruct missile: " + missile.getId();
		logMessage += " at: " + WarController.getInstance().getTime();
		logger.info(logMessage);

	}

	pointcut destructMissileResultLog(MissileDestructor missileDestructor, Missile missile, boolean destruct):
        execution (public void signalAboutMissileDestructionAttempt(MissileDestructor, Missile, boolean))
                && args(missileDestructor, missile, destruct);

	before(MissileDestructor missileDestructor, Missile missile, boolean destruct):
		destructMissileResultLog(missileDestructor, missile, destruct){
		
		String logMessage = weaponPrefix(missileDestructor) + "Destruction of missile " + missile.getId() + ": ";

		if (destruct)
			logMessage += "Succeeded!";
		else
			logMessage += "Missed!";

		logger.info(logMessage);
	}

	pointcut destructLauncherLog(LauncherDestructor launcherDestructor, Launcher launcher, int destructionTime):
            execution (public void destructLauncher(LauncherDestructor, Launcher, int))
                    && args(launcherDestructor, launcher, destructionTime);

	before(LauncherDestructor launcherDestructor, Launcher launcher, int destructionTime):
            destructLauncherLog(launcherDestructor, launcher, destructionTime){
		String logMessage = weaponPrefix(launcherDestructor) + "Trying to destruct launcher " + launcher.getId();

		logMessage += " at: " + WarController.getInstance().getTime();
		logger.info(logMessage);
	}

	pointcut destructLauncherResultLog(LauncherDestructor launcherDestructor, Launcher launcher, boolean succeed):
        execution (public void signalAboutLauncherDestructionAttempt(LauncherDestructor, Launcher, boolean))
                && args(launcherDestructor, launcher, succeed);

	before(LauncherDestructor launcherDestructor, Launcher launcher, boolean succeed):
		destructLauncherResultLog(launcherDestructor, launcher, succeed){
		String logMessage = weaponPrefix(launcherDestructor) + "Destruction of launcher " + launcher.getId() + ": ";

		if (succeed)
			logMessage += "Succeeded!";
		else
			logMessage += "Missed!";

		logMessage += " at: " + WarController.getInstance().getTime();
		logger.info(logMessage);
	}

}
