package gson;

import War.BusinessLogic.WarController;
import War.BusinessLogic.WarControllerFacade;
import War.Entities.Missile;
import War.Entities.LauncherDestructor;
import War.Entities.MissileDestructor;
import War.Entities.Launcher;
import gson.Adapters.GsonAdapters;
import gson.entities.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.function.Function;

public class JsonHandler implements Runnable{
    private WarControllerFacade controller = WarController.getInstance();
    private GsonAdapters adapter = new GsonAdapters();
    private ExecutorService gsonServices = Executors.newCachedThreadPool();

    private HashMap<gson.entities.Missile, Missile> gsonMissilesToEntities = new HashMap<>();
    private HashMap<gson.entities.Launcher, Launcher> gsonLaunchersToEntities = new HashMap<>();
    private HashMap<Destructor_, LauncherDestructor> gsonLauncherDestructorsToEntities = new HashMap<>();
    private HashMap<Destructor, MissileDestructor> gsonMissileDestructorsToEntities = new HashMap<>();

    private gson.entities.War gWar;

    public JsonHandler(gson.entities.War gWar){
        setgWar(gWar);
    }

    public void setgWar(gson.entities.War gWar) {
        this.gWar = gWar;
    }

    @Override
    public void run() {
    	
        ExecutorService es = Executors.newFixedThreadPool(3);

        addLaunchers(gWar.getMissileLaunchers().getLauncher());

        addMissileDestructors(gWar.getMissileDestructors().getDestructor());

        addLauncherDestructors(gWar.getMissileLauncherDestructors().getDestructor());


        es.execute(() -> launchMissiles(gWar.getMissileLaunchers().getLauncher()));

        es.execute(() -> destructMissiles(gWar.getMissileDestructors().getDestructor()));

        es.execute(() -> destructLaunchers(gWar.getMissileLauncherDestructors().getDestructor()));
         
        WarController.startTime();

    }
    
    

    private void addLaunchers(List<gson.entities.Launcher> jLaunchers){
        for (gson.entities.Launcher launcher : jLaunchers){
            Launcher entityLauncher = adapter.adaptLauncher(launcher);
            controller.addLauncher(entityLauncher);
            gsonLaunchersToEntities.put(launcher, entityLauncher);
            for(gson.entities.Missile missile : launcher.getMissile()){
                gsonMissilesToEntities.put(missile, adapter.adaptMissile(missile));
            }
        }
    }

    private void addMissileDestructors(List<gson.entities.Destructor> jMissileDestructors){
        for (gson.entities.Destructor missileDestructor : jMissileDestructors){
            MissileDestructor missileDestructorEntity = adapter.adaptMissileDestructor(missileDestructor);
            controller.addMissileDestructor(missileDestructorEntity);
            gsonMissileDestructorsToEntities.put(missileDestructor, missileDestructorEntity);
        }
    }

    private void addLauncherDestructors(List<gson.entities.Destructor_> jLauncherDestructors){
        for (gson.entities.Destructor_ launcherDestructor : jLauncherDestructors){
            LauncherDestructor launcherDestructorEntity = adapter.adaptLauncherDestructor(launcherDestructor);
            controller.addLauncherDestructor(launcherDestructorEntity);
            gsonLauncherDestructorsToEntities.put(launcherDestructor, launcherDestructorEntity);
        }
    }

    private void launchMissiles(List<gson.entities.Launcher> jlaunchers){
    	Comparator<gson.entities.Missile> missileComp = (m1, m2) -> Long.compare(m1.getLaunchTime() + m1.getFlyTime(), m2.getLaunchTime() + m2.getFlyTime());
        for(gson.entities.Launcher launcher : jlaunchers){
        	List<gson.entities.Missile> missiles = launcher.getMissile(); 
        	missiles.sort(missileComp);
            for(gson.entities.Missile missile : missiles){
                long time = missile.getLaunchTime();
                Launcher launcherParam = gsonLaunchersToEntities.get(launcher);
                if(controller.retrieveLaunchers().contains(launcherParam)) {
                	runWithDelay((e) -> {
                    	try {
                                controller.addMissile(launcherParam, gsonMissilesToEntities.get(missile));
                    	}catch(NoSuchElementException ex) {
                    		
                    	}
                                return null; },
                            time );
                }
            }
        }
    }


    private void destructMissiles(List<gson.entities.Destructor> jMissileDestructors) {
    	Comparator<DestructedMissile> missileComp = (d1, d2) -> Long.compare(d1.getDestructAfterLaunch(), d2.getDestructAfterLaunch());
        for (gson.entities.Destructor missileDestructor : jMissileDestructors) {
        	
        	List<DestructedMissile> destructedMissiles = missileDestructor.getDestructedMissile();
        	destructedMissiles.sort(missileComp);
        	
            for (DestructedMissile gDestructedMissile : destructedMissiles) {
                for (gson.entities.Missile missile : gsonMissilesToEntities.keySet()) {
                    if (gDestructedMissile.getId().equals(missile.getId())) {
                       runWithDelay(e -> {
                                    controller.destructMissile(
                                            adapter.adaptMissileDestructor(missileDestructor),
                                            gsonMissilesToEntities.get(missile),
                                            gDestructedMissile.getDestructAfterLaunch() /*+ missile.getLaunchTime()*/);
                                    return null;
                                    },
                                missile.getLaunchTime());

                    }
                }
            }
        }
    }

    private void destructLaunchers(List<gson.entities.Destructor_> jlauncherDestructors) {
    	Comparator<DestructedLauncher> launcherComp = (d1, d2) -> Long.compare(d1.getDestructTime(), d2.getDestructTime());

        for (gson.entities.Destructor_ launcherDestructor : jlauncherDestructors) {
        	List<DestructedLauncher>destructedLaunchers = launcherDestructor.getDestructedLauncher();
        	destructedLaunchers.sort(launcherComp);
        	
            for (DestructedLauncher gsonDestructedLauncher : destructedLaunchers) {
                for (gson.entities.Launcher gsonLauncher : gsonLaunchersToEntities.keySet()) {
                    if (gsonDestructedLauncher.getId().equals(gsonLauncher.getId())) {
                            int time = gsonDestructedLauncher.getDestructTime();
                           runWithDelay((e) -> {
                                        try {
                                            controller.destructLauncher(
                                                    gsonLauncherDestructorsToEntities.get(launcherDestructor),
                                                    gsonLaunchersToEntities.get(gsonLauncher),
                                                    time);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        return null;
                                    } ,
                                    0);
                            break;
                    }
                }
            }
        }
    }

    private void runWithDelay(Function<Void,Void> task, double delay){
        gsonServices.execute(() ->{
            try {
            	if(delay > 0)
            		Thread.sleep((long)delay * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task.apply(null);
        });
    }
}
