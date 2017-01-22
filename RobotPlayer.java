package NRUrefbot;
import battlecode.common.*;

import java.util.ArrayList;


public strictfp class RobotPlayer {
    static RobotController rc;

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    @SuppressWarnings("unused")

    public static float PI = (float)Math.PI;
    public static float plant_iteration = (PI/2);
    public static Direction plant_direction = new Direction(plant_iteration);
    public static float water_iteration = plant_iteration;
    public static int countGardener = 0;
    public static int countTree = 0;
    public static int firstTurn = 0;
    public static int robotCount = 0;
    //public static float my_direction = (PI/2);
    public static RobotInfo[] robotsNearMe;
    public static RobotInfo[] enemyRobotsNearMe;
    public static TreeInfo[] treesNearMe;
    public static TreeInfo[] myTrees;
    public static Team myTeam;
    public static Team enemyTeam;
    public static Direction dir;
    public static Direction move = Direction.getEast();
    public static float distanceToNearestGardener = 0;
    public static float distanceToInitialGardener = 0;
    public static float distanceToEnemy = 0;
    public static MapLocation initialGardenerStartLocation;
    public static MapLocation lastArchonBuildLocation;
    public static MapLocation[] initialEnemyArchonLocation;
    public static MapLocation nearestTree;
    public static MapLocation buildGardenerHere;
    public static float spaceAvailable;
    public static int archonBehavior = 0;
    public static int missionOrders = 0;
    public static int myOrders = 0;
    public static int myBroadcastChannel = 0;
    public static int firstGardenerID = 0;
    public static int firstLumberjackID = 0;
    public static int broadcastData = 0;
    public static int broadcastDataX = 0;
    public static int broadcastDataY = 0;
    public static String broadcastParse;
    public static float treeLocationX = 0;
    public static float treeLocationY = 0;
    public static boolean canBuildAllTrees;
    public static int searchedForTrees = 0;
    public static MapLocation myEdgeFacingPlantLocation;
    public static float distanceToPlantLocation;
    public static Direction directionToPlantLocation;
    public static int currentGardenerID = 0;
    public static int stuckTime = 0;
    public static int unstuckTime = 0;
    public static int currentLumberjackID = 0;
    public static MapLocation lumberjackStartLoc;
    public static int lumberjackGoHome = 0;
    public static int gardenerTreeCount = 0;
    public static RobotInfo robotJustBuilt;
    public static int archonWaitingToPlant = 0;
    public static RobotInfo[] enemyRobots;
    public static boolean enemyNearby;
    public static boolean noMoveLocation = false;
    public static int searchCooldown = 5;
    public static int gardenersNearMeCount = 0;
    public static int archonFrustrationLevel = 0;
    private static final int GARDENER_BUILD_LUMBERJACK_BROADCAST = 4;
    private static final int GARDENER_BUILD_LUMBERJACK_RECEIVE = 5;
    private static final int GARDENER_BUILD_SOLDIER_BROADCAST = 6;
    private static final int GARDENER_BUILD_SOLDIER_RECEIVE = 7;
    public static int gardenerBehavior = 0;
    public static int currentSoldierID = 0;
    public static MapLocation soldierStartLocation;
    public static int lumberjackCount = 0;
    public static int howManyLumberjacksWeWant = 0;
    public static int loopBreaker = 0;
    public static ArrayList<RobotInfo> soldiersCreated = new ArrayList<>();
    public static ArrayList<RobotInfo> scoutsCreated = new ArrayList<>();
    public static ArrayList<RobotInfo> lumberjacksCreated = new ArrayList<>();
    public static int howManySoldiersWeWant = 0;
    public static int soldierCount = 0;
    public static int gardenerBuildLineDistance = 0;
    public static Direction gardenerBuildLineDirection;
    public static MapLocation gardenerBuiltFirstLineTree;
    public static int iPlanted = 0;
    //public static boolean builtTree = false;




    public static void run(RobotController rc) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        RobotPlayer.rc = rc;

        // Here, we've separated the controls into a different method for each RobotType.
        // You can add the missing ones or rewrite this into your own control structure.
        switch (rc.getType()) {
            case ARCHON:
                runArchon();
                break;
            case GARDENER:
                runGardener();
                break;
            case SOLDIER:
                runSoldier();
                break;
            case LUMBERJACK:
                runLumberjack();
                break;
            case SCOUT:
                runScout();
                break;
            case TANK:
                runTank();
                break;
        }
    }

    static void runArchon() throws GameActionException {
        System.out.println("I'm an archon!\r\n");

        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {

                //TODO Victory Point count
                //TODO Bullet Sense
                //TODO Game AI
                //TODO Movement

                // Generate a random direction


                dir = Direction.getSouth();
                System.out.println("My behavior is: " + archonBehavior + "\r\n");

                //Direction newNorth = Direction.getNorth();
                //newNorth.getNorth();

                // Move randomly

                if (firstTurn != 0) {

                    robotsNearMe = rc.senseNearbyRobots();
                    gardenersNearMeCount = 0;
                    for (int i = robotsNearMe.length; i --> 0;){
                        if (robotsNearMe[i].getType() == RobotType.GARDENER){
                            gardenersNearMeCount++;
                        }
                    }
                    robotCount = robotsNearMe.length;
                    closestGardener();
                }



                //MapLocation nearestRobotLocation = new MapLocation(999,999);
                //System.out.println("Robots near me: " + robotCount + "\r\n");
                //System.out.println("Distance to nearest robot: " + ((robotsNearMe[0].getLocation()).distanceTo(rc.getLocation().add(dir))));
                //float nearestRobotDistance = nearestRobotLocation.distanceTo(rc.getLocation());

//TODO FIRST ARCHON TURN

                if (firstTurn == 0){
                    myTeam = rc.getTeam();
                    enemyTeam = myTeam.opponent();
                    distanceToEnemy = howFartoEnemy();
                    System.out.println("Distance to enemy: " + distanceToEnemy);
                    robotsNearMe = rc.senseNearbyRobots();
                    spaceAvailable = howMuchSpace();
                    System.out.println("Space available: " + spaceAvailable + "\r\n");

                    if (spaceAvailable >= 9 && distanceToEnemy >= 60){
                        archonBehavior = 1;
                        howManyLumberjacksWeWant = 3;
                        rc.broadcast(GARDENER_BUILD_LUMBERJACK_BROADCAST, howManyLumberjacksWeWant);
                        rc.broadcast(1, 001);
                        canBuildAllTrees = findSpaceFreeForTrees(rc.getLocation().add(randomDirection()));
                        if (canBuildAllTrees){
                            System.out.println("I can build all the trees!" + "\r\n");
                            if (atPlantLocation(2) && rc.canHireGardener(dir)){
                                buildGardener(dir, 003);
                                firstTurn++;
                            }
                            else if (atPlantLocation(2)){
                                firstTurn++;
                            }

                        }
                        else {
                            System.out.println("I can't build all the trees but there is space to do it!" + "\r\n");
                            move = rc.getLocation().directionTo(buildGardenerHere);
                            tryMove(move);
                            firstTurn++;
                        }
                    }
                    else if (spaceAvailable >= 9 && distanceToEnemy < 60){

                    }

                    else if (spaceAvailable > 0 && distanceToEnemy < 40){
                        archonBehavior = 4;
                        rc.broadcast(1, 004);
                        dir = findBuildSpace(1.01);
                        howManyLumberjacksWeWant = 5;
                        rc.broadcast(GARDENER_BUILD_LUMBERJACK_BROADCAST, howManyLumberjacksWeWant);
                        if (rc.canHireGardener(dir)){
                            buildGardener(dir, 004);
                            firstTurn++;
                        }
                    }

                    else if (spaceAvailable > 0 && distanceToEnemy > 40){
                        archonBehavior = 3;
                        rc.broadcast(1, 003);
                        dir = findBuildSpace(1.01);
                        howManyLumberjacksWeWant = 5;
                        rc.broadcast(GARDENER_BUILD_LUMBERJACK_BROADCAST, howManyLumberjacksWeWant);
                        if (rc.canHireGardener(dir)){
                            buildGardener(dir, 003);
                            firstTurn++;
                        }
                    }

                    else if (spaceAvailable == 0 && distanceToEnemy > 40){
                        archonBehavior = 3;
                        rc.broadcast(1, 003);
                        dir = findBuildSpace(1.01);
                        howManyLumberjacksWeWant = 5;
                        rc.broadcast(GARDENER_BUILD_LUMBERJACK_BROADCAST, howManyLumberjacksWeWant);
                        if (rc.canHireGardener(dir)){
                            buildGardener(dir, 003);
                            firstTurn++;
                        }
                    }

                    else if (spaceAvailable == 0 && distanceToEnemy < 40){
                        archonBehavior = 4;
                        rc.broadcast(1, 004);
                        dir = findBuildSpace(1.01);
                        howManyLumberjacksWeWant = 2;
                        rc.broadcast(GARDENER_BUILD_LUMBERJACK_BROADCAST, howManyLumberjacksWeWant);
                        if (rc.canHireGardener(dir)){
                            buildGardener(dir, 004);
                            firstTurn++;
                        }

                    }

                }

//TODO NORMAL ARCHON TURN

                else if (firstTurn != 0  && gardenersNearMeCount < 3) {
                    if (!noMoveLocation && searchedForTrees < 5) {
                        canBuildAllTrees = findSpaceFreeForTrees(rc.getLocation().add(randomDirection(), (float) (Math.random() * 7)));
                        if (canBuildAllTrees && spaceAvailable < 9) {
                            System.out.println("I can build all the trees!" + "\r\n");
                            if (atPlantLocation(2) && rc.canHireGardener(dir)) {
                                buildGardener(dir, 002);
                            } else if (atPlantLocation(2)) {
                                System.out.println("Will wait here till I can plant!" + "\r\n");
                                archonWaitingToPlant = 1;
                                noMoveLocation = true;
                            } else {
                                System.out.println("I need to get to the plant spot!" + "\r\n");
                                move = rc.getLocation().directionTo(buildGardenerHere);
                                tryMove(move);
                                noMoveLocation = true;
                                archonWaitingToPlant = 1;
                            }
                        }
                        if (canBuildAllTrees && spaceAvailable >= 9) {
                            System.out.println("I can build all the trees!" + "\r\n");
                            if (atPlantLocation(2) && rc.canHireGardener(dir)) {
                                buildGardener(dir, 003);
                            } else if (atPlantLocation(2)) {
                                System.out.println("Will wait here till I can plant!" + "\r\n");
                                archonWaitingToPlant = 1;
                                noMoveLocation = true;
                            } else {
                                System.out.println("I need to get to the plant spot!" + "\r\n");
                                move = rc.getLocation().directionTo(buildGardenerHere);
                                tryMove(move);
                                noMoveLocation = true;
                                archonWaitingToPlant = 1;
                            }
                        }
                    } else if (!noMoveLocation && searchedForTrees >= 5 && searchCooldown < 10) {
                        System.out.println("No luck here! Moving elsewhere!" + "\r\n");
                        tryMove(randomDirection());
                        searchCooldown++;
                        archonFrustrationLevel++;
                    } else if (!noMoveLocation && searchedForTrees >= 5 && searchCooldown > 0) {
                        System.out.println("No luck here! Moving elsewhere!" + "\r\n");
                        System.out.println("Search cooldown: " + searchCooldown + "\r\n");
                        tryMove(randomDirection());
                        searchedForTrees = 0;
                        searchCooldown = 0;
                    } else if (noMoveLocation && !atPlantLocation(2)) {
                        System.out.println("Moving to my plant location!" + "\r\n");
                        move = rc.getLocation().directionTo(buildGardenerHere);
                        tryMove(move);
                        System.out.println("Distance to go: " + rc.getLocation().add(move, 2).distanceTo(buildGardenerHere) + "\r\n");
                    } else if (noMoveLocation && rc.canHireGardener(dir) && archonWaitingToPlant == 1 && atPlantLocation(2)) {
                        buildGardener(dir, 002);
                        archonWaitingToPlant = 0;
                        noMoveLocation = false;
                        System.out.println("This worked!" + "\r\n");
                    } else if (noMoveLocation && archonWaitingToPlant == 1 && atPlantLocation(2)) {
                        System.out.println("Will wait here till I can plant!" + "\r\n");
                    } else if (stuckTime < 20) {
                        System.out.println("Can't find space for trees!" + "\r\n");
                        tryMove(randomDirection());
                        stuckTime++;
                    } else if (stuckTime >= 20) {
                        tryMove(Direction.getNorth());
                        System.out.println("I'm stuck!" + "\r\n");
                        unstuckTime++;

                        if (unstuckTime >= 20) {
                            stuckTime = 0;
                            unstuckTime = 0;
                        }
                    }
                }
                else if (firstTurn != 0  && gardenersNearMeCount >= 3) {
                    getAwayFromGardeners();
                    tryMove(move);
                }

                if (archonFrustrationLevel == 50) {
                    //rc.broadcast(GARDENER_BUILD_LUMBERJACK_BROADCAST, 003);
                }

                float teamBulletCount = rc.getTeamBullets();
                if (teamBulletCount > 500) {
                    rc.donate(200);
                }

                Clock.yield();


            } catch (Exception e) {
                System.out.println("Archon Exception");
                e.printStackTrace();
            }
        }
    }

    private static void buildGardener(Direction buildDir, int orders){
        System.out.println("I can build a gardener here!" + buildGardenerHere + "\r\n");
        while(true){
            try{
                rc.hireGardener(buildDir);
                System.out.println("I built a gardener at: " + rc.getLocation().add(buildDir, 3) + "\r\n");
                countGardener++;
                robotsNearMe = rc.senseNearbyRobots();
                robotJustBuilt = rc.senseRobotAtLocation(rc.getLocation().add(buildDir, 3));
                int t = robotJustBuilt.getID() / 100;
                int r = robotJustBuilt.getID() % 100;
                lastArchonBuildLocation = robotJustBuilt.getLocation();
                currentGardenerID = t + r;
                System.out.println("Current gardener ID: " + currentGardenerID + "\r\n");
                rc.broadcast(currentGardenerID, orders);
                System.out.println("I broadcasted this gardener orders: " + archonBehavior + "\r\n");
                break;
            } catch (Exception e) {
                System.out.println("Archon Exception");
                e.printStackTrace();
            }
        }

    }

    public static boolean atPlantLocation(int radius){
        myEdgeFacingPlantLocation = rc.getLocation().add(rc.getLocation().directionTo(buildGardenerHere), radius);
        distanceToPlantLocation = buildGardenerHere.distanceTo(myEdgeFacingPlantLocation);
        directionToPlantLocation = new Direction(myEdgeFacingPlantLocation, buildGardenerHere);
        System.out.println("My edge pointing at the Plant Location: " + myEdgeFacingPlantLocation + "\r\n");
        System.out.println("Distance from my edge to Plant Location: " + distanceToPlantLocation + "\r\n");
        dir = directionToPlantLocation;
        if (distanceToPlantLocation > (float)1.6){
            return false;
        }
        else if (distanceToPlantLocation <= (float)1.6 && outsideMyRadius(rc.getLocation(), buildGardenerHere)){
            System.out.println("No need to flip direction!" + "\r\n");
            return true;
        }
        else if (distanceToPlantLocation <= (float)1.6 && !outsideMyRadius(rc.getLocation(), buildGardenerHere)){
            System.out.println("Direction must be flipped!" + "\r\n");
            dir = directionToPlantLocation.opposite();
            return true;
        }
        return false;
    }

    public static boolean outsideMyRadius(MapLocation myCenter, MapLocation buildSite){
        float a = myCenter.distanceTo(buildSite);
        if (a > 2){
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean outsideAnotherGarden(MapLocation buildSite, RobotInfo[] robotLocations){
        for (int i = robotLocations.length; i --> 0;){
            if (robotLocations[i].getLocation().distanceTo(buildSite) < 6){
                return false;
            }
        }
        return true;
    }

    public static void closestGardener() {
        robotsNearMe = rc.senseNearbyRobots();
        for (int i = robotsNearMe.length; i-- > 0; ) {
            if (robotsNearMe[i].getType() == RobotType.GARDENER) {
                float a = getDistanceToGardener(i);
                float b = distanceToInitialGardener;
                System.out.println("Gardener being evaluated: " + a);
                System.out.println("Distance value: " + distanceToNearestGardener);
                System.out.println("Distance to initial gardener: " + b);
                if (a <= b) {
                    distanceToNearestGardener = a;
                    System.out.println("Distance to nearest gardener: " + a);
                }
            }
        }
    }

    public static void getAwayFromGardeners() {
        robotsNearMe = rc.senseNearbyRobots(10);
        MapLocation z = initialEnemyArchonLocation[0];
        MapLocation y = initialEnemyArchonLocation[0];
        MapLocation x = initialEnemyArchonLocation[0];
        float w = 10;
        float v = 10;
        float u = 10;
        for (int i = robotsNearMe.length; i-- > 0; ) {
            if (robotsNearMe[i].getType() == RobotType.GARDENER) {
                System.out.println("Found a gardener at: " + robotsNearMe[i].getLocation() + "\r\n");
                if (robotsNearMe[i].getLocation().distanceTo(rc.getLocation()) < w) {
                    w = robotsNearMe[i].getLocation().distanceTo(rc.getLocation());
                    z = robotsNearMe[i].getLocation();
                }
                if (robotsNearMe[i].getLocation().distanceTo(rc.getLocation()) < v) {
                    w = v;
                    v = robotsNearMe[i].getLocation().distanceTo(rc.getLocation());
                    z = y;
                    y = robotsNearMe[i].getLocation();
                }
                if (robotsNearMe[i].getLocation().distanceTo(rc.getLocation()) < u) {
                    v = u;
                    u = robotsNearMe[i].getLocation().distanceTo(rc.getLocation());
                    y = x;
                    x = robotsNearMe[i].getLocation();
                }
            }
        }
        System.out.println("Third closest gardener is at: " + z + " and at a distance of: " + w + " and an angle to: " + rc.getLocation().directionTo(z) +  "\r\n");
        System.out.println("Second closest gardener is at: " + y + " and at a distance of: " + v + " and an angle to: " + rc.getLocation().directionTo(y) +  "\r\n");
        System.out.println("Closest gardener is at: " + x + " and at a distance of: " + u + " and an angle to: " + rc.getLocation().directionTo(x) + "\r\n");

        Float third = rc.getLocation().directionTo(z).opposite().radians;
        Float second = rc.getLocation().directionTo(y).opposite().radians;
        Float first = rc.getLocation().directionTo(x).opposite().radians;


        float ye = ((float)((double)first*(1/u)) + (float)((double)second*(1/v)) + (float)((double)third*(1/w)))/3;
        move = new Direction(ye);
        while(true) {
            try {
                rc.setIndicatorLine(rc.getLocation(), rc.getLocation().add(ye, 5), 255, 0, 0);
                break;
            } catch (Exception e) {
                System.out.println("Lumberjack Exception");
                e.printStackTrace();
            }
        }

    }


    private static float getDistanceToGardener(int x){
        return robotsNearMe[x].getLocation().distanceTo(getDirectionArchonPoints());
    }

    private static float getDistanceToInitialGardener(){
        return initialGardenerStartLocation.distanceTo(getDirectionArchonPoints());
    }

    private static MapLocation getDirectionArchonPoints(){
        return (rc.getLocation().add(dir, 3));
    }

    private static float howMuchSpace() {
        float a;
        float b;
        float c = 100;
        treesNearMe = rc.senseNearbyTrees();
        countTree = treesNearMe.length;
        if (countTree == 0){
            System.out.println("No trees!");
            return c;
        }
        if (countTree <= 5){
            for (int i = countTree; i --> 0;){
                b = treesNearMe[i].getRadius();
                a = treesNearMe[i].getLocation().distanceTo(getDirectionArchonPoints());
                if ((a-b) < c){
                    c = a-b;
                    nearestTree = treesNearMe[i].getLocation();
                    //System.out.println("Distance to this tree edge: " + c);
                }
            }

        }
        if (countTree > 5){
            return 0;
        }

        System.out.println("Distance to nearest tree edge: " + c);
        System.out.println("Location of nearest tree: " + nearestTree);
        return c;
    }

    private static void broadcastTreeLocations(){

        for (int i = countTree; i --> 0;){
            String a = treesNearMe[i].getLocation().toString();
            System.out.println("String of MapLocation: " + a + "\r\n");
            a = a.replace("[", "");
            a = a.replace("]", "");
            a = a.replace(",", "");
            a = a.replace(".", "");
            a = a.replace(" ", "");
            System.out.println("Fixed String of MapLocation: " + a + "\r\n");
            a = a.substring(0, 3) + a.substring(9, 12);
            System.out.println("Shortened: " + a + "\r\n");
            //String[] parsed = a.split(" ");
            broadcastData = Integer.parseInt(a);
            //broadcastDataY = Integer.parseInt(parsed[1]);
            System.out.println("Integer X of MapLocation: " + broadcastData + "\r\n");
            //System.out.println("Integer Y of MapLocation: " + broadcastDataY + "\r\n");
            while(true){
                try{
                    rc.broadcast(22+i, broadcastData);
                    //rc.broadcast(23+i, broadcastDataY);
                    System.out.println("I broadcasted: " + broadcastData + "\r\n");
                    break;
                } catch (Exception e) {
                    System.out.println("broadcastTreeLocations Exception");
                    e.printStackTrace();
                }
            }
        }
    }

    private static float howFartoEnemy() {
        initialEnemyArchonLocation = rc.getInitialArchonLocations(enemyTeam);
        return initialEnemyArchonLocation[0].distanceTo(getDirectionArchonPoints());


    }
    private static void whereToMove(MapLocation lastBuild, MapLocation here){
        //Direction angleBetweenGardenerLocations = new Direction(lastBuild, here);
        //System.out.println("The direction to the last gardener is: " + angleBetweenGardenerLocations);
        //if (angleBetweenGardenerLocations.radians < .05 && distanceToInitialGardener > 5){
        //    move = Direction.getNorth();
        //}
        //else if (angleBetweenGardenerLocations.radians == (PI/2) && distanceToInitialGardener > 5){
        //    move = Direction.getWest();
        //}
        if (countGardener == 1){
            move = Direction.getEast();
        }
        else if (countGardener == 2){
            move = Direction.getNorth();
        }
        else if (countGardener == 3){
            move = Direction.getWest();
        }
        else if (countGardener == 4){
            move = randomDirection();
        }
    }

    private static boolean findSpaceFreeForTrees(MapLocation thisLocation){
        int spaceCheck = 0;
        searchedForTrees++;
        Direction directionToFace;
        robotsNearMe = rc.senseNearbyRobots();
        while(rc.canSenseAllOfCircle(thisLocation, 2) && spaceCheck < 30){
            try {
                boolean a;
                boolean b;
                boolean c;
                boolean d;
                a = rc.isCircleOccupiedExceptByThisRobot(thisLocation, 2);
                b = rc.onTheMap(thisLocation, 2);
                c = outsideAnotherGarden(thisLocation, robotsNearMe);

                if (!a && b && c){
                    buildGardenerHere = thisLocation;
                    System.out.println("You can build a gardener at: " + buildGardenerHere + "\r\n");
                    return true;
                }
                else {
                    directionToFace = randomDirection();
                    System.out.println("New Direction: " + directionToFace + "\r\n");
                    thisLocation = thisLocation.add(directionToFace, 1);
                    System.out.println("This location is now: " + thisLocation + "\r\n");
                    rc.setIndicatorLine(rc.getLocation(), thisLocation, 0, 255, 0);
                    spaceCheck++;
                }
            }
            catch (Exception e) {
                System.out.println("Lumberjack Exception");
                e.printStackTrace();
            }
        }

        System.out.println("Find Space returned false! Spacecheck was: " + spaceCheck + "\r\n");
        return false;

    }

    static void runGardener() throws GameActionException {
        System.out.println("I'm a gardener!");
        while (true) {
            try {
                if (firstTurn == 0) {
                    int t = rc.getID()/100;
                    int r = rc.getID() % 100;
                    myBroadcastChannel = t + r;
                    missionOrders = rc.readBroadcast(1);
                    myTeam = rc.getTeam();
                    myOrders = rc.readBroadcast(myBroadcastChannel);
                    howManyLumberjacksWeWant = rc.readBroadcast(GARDENER_BUILD_LUMBERJACK_BROADCAST);
                    howManySoldiersWeWant = rc.readBroadcast(GARDENER_BUILD_SOLDIER_BROADCAST);
                    System.out.println("We want this many lumberjacks: " + howManyLumberjacksWeWant + "\r\n");
                    System.out.println("We want this many soldiers: " + howManySoldiersWeWant + "\r\n");
                    System.out.println("My Broadcast Channel is: " + myBroadcastChannel + "\r\n");
                    System.out.println("My orders are: " + myOrders + "\r\n");
                    dir = randomDirection();
                    if (myOrders == 002) {
                        buildSoldierHere(dir, 001);
                        firstTurn++;
                    }
                    if (myOrders == 001) {
                        firstTurn++;
                        letsTryPlantingTrees(plant_direction);
                        waterInDirection(treeDirectionToWater());
                    }
                    if (myOrders == 003) {
                        dir = findBuildSpace(1.01);
                        buildSoldierHere(dir, 001);
                        firstTurn++;
                    }
                    if (myOrders == 004) {
                        dir = findBuildSpace(1.01);
                        buildSoldierHere(dir, 004);
                        firstTurn++;
                    }
                }
                else if (firstTurn != 0) {
                    System.out.println("My orders are: " + myOrders + "\r\n");
                    myTrees = rc.senseNearbyTrees(2, myTeam);
                    lumberjackCount = rc.readBroadcast(GARDENER_BUILD_LUMBERJACK_RECEIVE);
                    System.out.println("Lumberjack count is: " + lumberjackCount + "\r\n");
                    myOrders = rc.readBroadcast(myBroadcastChannel);
                    if (lumberjackCount >= howManyLumberjacksWeWant){
                        rc.broadcast(myBroadcastChannel, 003);
                        myOrders = 000;
                    }
                    if (myOrders == 000) {
                        letsTryPlantingTrees(plant_direction);
                        waterInDirection(treeDirectionToWater());
                    }

                    else if (myOrders == 001) {
                        letsTryPlantingTrees(plant_direction);
                        waterInDirection(treeDirectionToWater());
                        System.out.println("Number of trees planted: " + myTrees.length + "\r\n");
                        dir = findBuildSpace(1.01);
                        buildLumberjackHere(dir, myOrders);

                        letsTryPlantingTrees(plant_direction);
                        waterInDirection(treeDirectionToWater());
                    }
                    else if (myOrders == 002 && myTrees.length >= 3){
                        System.out.println("Number of trees planted: " + myTrees.length + "\r\n");
                        System.out.println("I shouldn't be building trees!" + "\r\n");
                        dir = findBuildSpace(1.01);
                        buildLumberjackHere(dir, myOrders);
                        waterInDirection(treeDirectionToWater());
                    }
                    else if (myOrders == 003 && iPlanted == 0 && !noMoveLocation){
                        lookForTreeBuildingLine();
                        if (gardenerBuildLineDistance > 0){
                            if (rc.canPlantTree(gardenerBuildLineDirection)){
                                System.out.println("Trying to plant a tree!" + "\r\n");
                                letsTryPlantingTrees(gardenerBuildLineDirection);
                                iPlanted++;
                                treesNearMe = rc.senseNearbyTrees(rc.getLocation().add(gardenerBuildLineDirection), 1, null);
                                nearestTree = treesNearMe[0].getLocation();
                                tryMove(gardenerBuildLineDirection);
                                gardenerBuiltFirstLineTree = nearestTree;
                                noMoveLocation = true;
                                buildGardenerHere = nearestTree.add(gardenerBuildLineDirection, 4);
                            }
                            else {
                                System.out.println("Can't plant a tree here yet!" + "\r\n");
                            }
                        }
                        else if (gardenerBuildLineDistance == 0){
                            System.out.println("No line to plant!" + "\r\n");
                            tryMove(randomDirection());
                        }
                    }
                    else if (myOrders == 003 && iPlanted < 10 && noMoveLocation){
                        if (atPlantLocation(1) && rc.canPlantTree(rc.getLocation().directionTo(buildGardenerHere))){
                            letsTryPlantingTrees(rc.getLocation().directionTo(buildGardenerHere));
                            iPlanted++;
                            treesNearMe = rc.senseNearbyTrees(rc.getLocation().add(gardenerBuildLineDirection), 1, null);
                            nearestTree = treesNearMe[0].getLocation();
                            tryMove(gardenerBuildLineDirection);
                            noMoveLocation = true;
                            waterInDirection(treeDirectionToWater());
                            buildGardenerHere = nearestTree.add(gardenerBuildLineDirection, 5);
                        }
                        else if (atPlantLocation(1)) {

                        }
                        else if (!atPlantLocation(1)){
                            tryMove(gardenerBuildLineDirection);
                        }
                    }
                    else if (myOrders == 003 && iPlanted >= 10 && noMoveLocation){
                        move = rc.getLocation().directionTo(gardenerBuiltFirstLineTree);
                        tryMove(move);
                        waterInDirection(treeDirectionToWater());
                    }


//                    else if (myOrders == 003 && myTrees.length < 3){
//                        System.out.println("Number of trees planted: " + myTrees.length + "\r\n");
//                        dir = findBuildSpace(1.01);
//                        buildLumberjackHere(dir, myOrders);
//                        letsTryPlantingTrees(plant_direction);
//                        waterInDirection(treeDirectionToWater());
//                    }
//                    else if (myOrders == 003 && myTrees.length >= 3){
//                        System.out.println("Number of trees planted: " + myTrees.length + "\r\n");
//                        System.out.println("I shouldn't be building trees!" + "\r\n");
//                        dir = findBuildSpace(1.01);
//                        buildLumberjackHere(dir, myOrders);
//                        waterInDirection(treeDirectionToWater());
//                    }
                    else if (myOrders == 4 && myTrees.length < 2){
                        System.out.println("Number of trees planted: " + myTrees.length + "\r\n");
                        System.out.println("I shouldn't be building trees!" + "\r\n");
                        buildSoldierHere(dir, 004);
                        letsTryPlantingTrees(plant_direction);
                        waterInDirection(treeDirectionToWater());
                    }
                    else if (myOrders == 4 && myTrees.length >= 2){
                        System.out.println("Number of trees planted: " + myTrees.length + "\r\n");
                        System.out.println("I shouldn't be building trees!" + "\r\n");
                        buildSoldierHere(dir, 004);
                        waterInDirection(treeDirectionToWater());
                    }
                }
                Clock.yield();
            } catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }
    }

    private static void lookForTreeBuildingLine(){
        int north = 0;
        int east = 1;
        int south = 2;
        int west = 3;
        int northDistance = 0;
        int eastDistance = 0;
        int southDistance = 0;
        int westDistance = 0;
        MapLocation firstCheck;
        MapLocation secondCheck;
        MapLocation thirdCheck;
        MapLocation fourthCheck;
        MapLocation fifthCheck;
        while(true){
            try{
            firstCheck = rc.getLocation().add(Direction.getNorth(), 2);
            rc.setIndicatorDot(firstCheck, 255, 255, 0);
            if (!rc.isCircleOccupiedExceptByThisRobot(firstCheck, 1) && rc.onTheMap(firstCheck, 1)) {
                northDistance = 1;
                secondCheck = firstCheck.add(Direction.getNorth(), 1);
                rc.setIndicatorDot(secondCheck, 255, 255, 0);
                if (!rc.isCircleOccupied(secondCheck, 1) && rc.onTheMap(secondCheck, 1)){
                    northDistance = 2;
                    thirdCheck = secondCheck.add(Direction.getNorth(), 1);
                    rc.setIndicatorDot(thirdCheck, 255, 255, 0);
                    if (!rc.isCircleOccupied(thirdCheck, 1) && rc.onTheMap(thirdCheck, 1)){
                        northDistance = 3;
                        fourthCheck = thirdCheck.add(Direction.getNorth(), 1);
                        rc.setIndicatorDot(fourthCheck, 255, 255, 0);
                        if (!rc.isCircleOccupied(fourthCheck, 1) && rc.onTheMap(fourthCheck, 1)){
                            northDistance = 4;
                            fifthCheck = fourthCheck.add(Direction.getNorth(), 1);
                            rc.setIndicatorDot(fifthCheck, 255, 255, 0);
                            if (!rc.isCircleOccupied(fifthCheck, 1) && rc.onTheMap(fifthCheck, 1)){
                                northDistance = 5;
                            }
                        }
                    }
                }
            }
            firstCheck = rc.getLocation().add(Direction.getEast(), 2);
            rc.setIndicatorDot(firstCheck, 255, 255, 0);
                if (!rc.isCircleOccupiedExceptByThisRobot(firstCheck, 1) && rc.onTheMap(firstCheck, 1)) {
                eastDistance = 1;
                secondCheck = firstCheck.add(Direction.getEast(), 1);
                rc.setIndicatorDot(secondCheck, 255, 255, 0);
                if (!rc.isCircleOccupied(secondCheck, 1) && rc.onTheMap(secondCheck, 1)){
                    eastDistance = 2;
                    thirdCheck = secondCheck.add(Direction.getEast(), 1);
                    rc.setIndicatorDot(thirdCheck, 255, 255, 0);
                    if (!rc.isCircleOccupied(thirdCheck, 1) && rc.onTheMap(thirdCheck, 1)){
                        eastDistance = 3;
                        fourthCheck = thirdCheck.add(Direction.getEast(), 1);
                        rc.setIndicatorDot(fourthCheck, 255, 255, 0);
                        if (!rc.isCircleOccupied(fourthCheck, 1) && rc.onTheMap(fourthCheck, 1)){
                            eastDistance = 4;
                            fifthCheck = fourthCheck.add(Direction.getEast(), 1);
                            rc.setIndicatorDot(fifthCheck, 255, 255, 0);
                            if (!rc.isCircleOccupied(fifthCheck, 1) && rc.onTheMap(fifthCheck, 1)){
                                eastDistance = 5;
                            }
                        }
                    }
                }
            }
            firstCheck = rc.getLocation().add(Direction.getSouth(), 2);
            rc.setIndicatorDot(firstCheck, 255, 255, 0);
                if (!rc.isCircleOccupiedExceptByThisRobot(firstCheck, 1) && rc.onTheMap(firstCheck, 1)) {
                southDistance = 1;
                secondCheck = firstCheck.add(Direction.getSouth(), 1);
                rc.setIndicatorDot(secondCheck, 255, 255, 0);
                    if (!rc.isCircleOccupied(secondCheck, 1) && rc.onTheMap(secondCheck, 1)){
                    southDistance = 2;
                    thirdCheck = secondCheck.add(Direction.getSouth(), 1);
                    rc.setIndicatorDot(thirdCheck, 255, 255, 0);
                        if (!rc.isCircleOccupied(thirdCheck, 1) && rc.onTheMap(thirdCheck, 1)){
                        southDistance = 3;
                        fourthCheck = thirdCheck.add(Direction.getSouth(), 1);
                        rc.setIndicatorDot(fourthCheck, 255, 255, 0);
                            if (!rc.isCircleOccupied(fourthCheck, 1) && rc.onTheMap(fourthCheck, 1)){
                            southDistance = 4;
                            fifthCheck = fourthCheck.add(Direction.getSouth(), 1);
                            rc.setIndicatorDot(fifthCheck, 255, 255, 0);
                                if (!rc.isCircleOccupied(fifthCheck, 1) && rc.onTheMap(fifthCheck, 1)){
                                southDistance = 5;
                            }
                        }
                    }
                }
            }
            firstCheck = rc.getLocation().add(Direction.getWest(), 2);
            rc.setIndicatorDot(firstCheck, 255, 255, 0);
                if (!rc.isCircleOccupiedExceptByThisRobot(firstCheck, 1) && rc.onTheMap(firstCheck, 1)) {
                westDistance = 1;
                secondCheck = firstCheck.add(Direction.getWest(), 1);
                rc.setIndicatorDot(secondCheck, 255, 255, 0);
                    if (!rc.isCircleOccupied(secondCheck, 1) && rc.onTheMap(secondCheck, 1)){
                    westDistance = 2;
                    thirdCheck = secondCheck.add(Direction.getWest(), 1);
                    rc.setIndicatorDot(thirdCheck, 255, 255, 0);
                        if (!rc.isCircleOccupied(thirdCheck, 1) && rc.onTheMap(thirdCheck, 1)){
                        westDistance = 3;
                        fourthCheck = thirdCheck.add(Direction.getWest(), 1);
                        rc.setIndicatorDot(fourthCheck, 255, 255, 0);
                            if (!rc.isCircleOccupied(fourthCheck, 1) && rc.onTheMap(fourthCheck, 1)){
                            westDistance = 4;
                            fifthCheck = fourthCheck.add(Direction.getWest(), 1);
                            rc.setIndicatorDot(fifthCheck, 255, 255, 0);
                                if (!rc.isCircleOccupied(fifthCheck, 1) && rc.onTheMap(fifthCheck, 1)){
                                westDistance = 5;
                            }
                        }
                    }
                }
            }
                System.out.println("Build distance to the north: " + northDistance + "\r\n");
                System.out.println("Build distance to the east: " + eastDistance + "\r\n");
                System.out.println("Build distance to the south: " + southDistance + "\r\n");
                System.out.println("Build distance to the west: " + westDistance + "\r\n");
            int largest = Math.max(northDistance,Math.max(eastDistance,Math.max(southDistance,westDistance)));
            gardenerBuildLineDistance = largest;
            if (northDistance >= eastDistance && northDistance >= westDistance && northDistance >= southDistance){
                gardenerBuildLineDirection = Direction.getNorth();
            }
            if (eastDistance >= northDistance && eastDistance >= westDistance && eastDistance >= southDistance){
                gardenerBuildLineDirection = Direction.getEast();
            }
            if (westDistance >= eastDistance && westDistance >= northDistance && westDistance >= southDistance){
                gardenerBuildLineDirection = Direction.getWest();
            }
            if (southDistance >= eastDistance && southDistance >= northDistance && southDistance >= westDistance){
                gardenerBuildLineDirection = Direction.getSouth();
            }
                System.out.println("Build distance: " + gardenerBuildLineDistance + "\r\n");
                System.out.println("Build direction: " + gardenerBuildLineDirection + "\r\n");
            break;





            } catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }

    }

    private static void buildLumberjackHere(Direction myDir, int orders) {
        while (rc.canBuildRobot(RobotType.LUMBERJACK, myDir)) {
            try {
                rc.buildRobot(RobotType.LUMBERJACK, myDir);
                System.out.println("I built a lumberjack!" + "\r\n");
                robotsNearMe = rc.senseNearbyRobots();
                robotJustBuilt = rc.senseRobotAtLocation(rc.getLocation().add(myDir, 2));
                lumberjacksCreated.add(robotJustBuilt);
                int z = robotJustBuilt.getID() / 100;
                int x = robotJustBuilt.getID() % 100;
                currentLumberjackID = z + x;
                System.out.println("Current lumberjack ID: " + currentLumberjackID + "\r\n");
                rc.broadcast(currentLumberjackID, orders);
                System.out.println("I assigned the lumberjack the following broadcast ID: " + currentLumberjackID + "\r\n");
                System.out.println("I assigned the lumberjack the following orders: " + orders + "\r\n");
                updateLumberjackCount();
                break;
            } catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }
    }

    private static void buildSoldierHere(Direction myDir, int orders) {
        System.out.println("Trying to build a Soldier!" + "\r\n");
        for (int i = 0; i < 11; i++){
            myDir = randomDirection();
            if (rc.canBuildRobot(RobotType.SOLDIER, myDir)){
                System.out.println("Trying a new direction at: " + myDir + "\r\n");
                break;
            }
        }
        while (rc.canBuildRobot(RobotType.SOLDIER, myDir)){
            try {
                rc.buildRobot(RobotType.SOLDIER, myDir);
                soldierCount = rc.readBroadcast(GARDENER_BUILD_SOLDIER_RECEIVE);
                soldierCount++;
                rc.broadcast(GARDENER_BUILD_SOLDIER_RECEIVE, soldierCount);
                System.out.println("I built a soldier!" + "\r\n");
                robotsNearMe = rc.senseNearbyRobots();
                robotJustBuilt = rc.senseRobotAtLocation(rc.getLocation().add(myDir, 2));
                soldiersCreated.add(robotJustBuilt);
                int z = robotJustBuilt.getID() / 100;
                int x = robotJustBuilt.getID() % 100;
                currentLumberjackID = z + x;
                System.out.println("Current soldier ID: " + currentLumberjackID + "\r\n");
                rc.broadcast(currentLumberjackID, orders);
                System.out.println("I assigned the soldier the following broadcast ID: " + currentLumberjackID + "\r\n");
                System.out.println("I assigned the soldier the following orders: " + orders + "\r\n");
                break;
            } catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }
    }

    private static void broadcastToMyRobots(ArrayList robotArray, int myOrders){
        RobotInfo x;
        while(true){
            try{
                for (int i = soldiersCreated.size(); i --> 0;){
                    x = soldiersCreated.get(i);
                    int z = x.getID() / 100;
                    int y = x.getID() % 100;
                    int broadID = z + y;
                    System.out.println("I assigned the soldier the following broadcast ID: " + currentLumberjackID + "\r\n");
                    System.out.println("I assigned the soldier the following orders: " + myOrders + "\r\n");
                    rc.broadcast(broadID, myOrders);
                }
                break;
            } catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }

    }

    private static void letsTryPlantingTrees(Direction myDir){
        System.out.println("Trying to build a tree!" + "\r\n");
        while(true){
            try{
                if (rc.canPlantTree(myDir)) {
                    rc.plantTree(myDir);
                    System.out.println("I planted a tree!" + "\r\n");
                    break;
                } else {
                    changePlantingDirection();
                    break;
                }
            } catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }
    }

    private static void changePlantingDirection(){
        plant_iteration = plant_iteration + (PI / 3);
        plant_direction = new Direction(plant_iteration);
    }

    private static Direction treeDirectionToWater(){
        myTrees = rc.senseNearbyTrees(2, myTeam);
        System.out.println("I am sensing this many trees: " + myTrees.length + "\r\n");
        float health = 55;
        Direction treeDir = new Direction(0, 0);
        for (int i = 0; i < myTrees.length; i++){
            float x = myTrees[i].getHealth();
            System.out.println("Tree ID: " + myTrees[i].getID() + " has health of: " + x + "\r\n");
            if (x < health){
                health = x;
                treeDir = rc.getLocation().directionTo(myTrees[i].getLocation());
            }
        }
        System.out.println("Tree at direction: " + treeDir + " has lowest health of: " + health + "\r\n");
        return treeDir;
    }

    private static void waterInDirection(Direction myDir){


        //water_iteration = water_iteration - (PI/3);
        MapLocation waterHere = rc.getLocation().add(myDir, 2);
        //waterHere = waterHere.add(water_iteration, 2);
        boolean triedToWater = false;
        while(!triedToWater){
            try{
                if(rc.canWater(waterHere)){
                    rc.water(waterHere);
                    System.out.println("I watered a tree!\r\n");
                    //System.out.println(water_iteration +"\r\n");
                    System.out.print(waterHere +"\r\n");
                    triedToWater = true;
                }
                else {
                    System.out.println("Couldn't water!\r\n");
                    //System.out.println(water_iteration +"\r\n");
                    System.out.print(waterHere +"\r\n");
                    triedToWater = true;
                }
            }catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }
    }

    static void runSoldier() throws GameActionException {
        System.out.println("I'm an soldier!");
        enemyTeam = rc.getTeam().opponent();


        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {

                if (firstTurn == 0) {
                    firstTurn++;
                    int t = rc.getID()/100;
                    int r = rc.getID() % 100;
                    myBroadcastChannel = t + r;
                    missionOrders = rc.readBroadcast(1);
                    myTeam = rc.getTeam();
                    myOrders = rc.readBroadcast(myBroadcastChannel);
                    System.out.println("My Broadcast Channel is: " + myBroadcastChannel + "\r\n");
                    System.out.println("My orders are: " + myOrders + "\r\n");
                    dir = randomDirection();
                    enemyRobotsNearMe = rc.senseNearbyRobots(-1, enemyTeam);
                    distanceToEnemy = howFartoEnemy();
                    soldierStartLocation = rc.getLocation();

                    if (myOrders == 000){
                        tryMove(dir);
                        if (isEnemyNearby(enemyRobotsNearMe)){
                            MapLocation aimSpot = enemyRobotsNearMe[0].getLocation();
                            if (isLineOfSightClear(rc.getLocation(), aimSpot)){
                                fireAtEnemy(aimSpot);
                            }
                        }
                    }
                    if (myOrders == 001) {
                        tryMove(dir);
                        if (isEnemyNearby(enemyRobotsNearMe)){
                            MapLocation aimSpot = enemyRobotsNearMe[0].getLocation();
                            if (isLineOfSightClear(rc.getLocation(), aimSpot)){
                                fireAtEnemy(aimSpot);
                            }
                        }
                    }
                    if (myOrders == 002) {
                        tryMove(dir);
                        if (isEnemyNearby(enemyRobotsNearMe)){
                            MapLocation aimSpot = enemyRobotsNearMe[0].getLocation();
                            if (isLineOfSightClear(rc.getLocation(), aimSpot)){
                                fireAtEnemy(aimSpot);
                            }
                        }
                    }
                    if (myOrders == 003) {
                        tryMove(dir);
                        if (isEnemyNearby(enemyRobotsNearMe)){
                            MapLocation aimSpot = enemyRobotsNearMe[0].getLocation();
                            if (isLineOfSightClear(rc.getLocation(), aimSpot)){
                                fireAtEnemy(aimSpot);
                            }
                        }
                    }
                    if (myOrders == 004) {
                        initialEnemyArchonLocation = rc.getInitialArchonLocations(enemyTeam);
                        dir = rc.getLocation().directionTo(initialEnemyArchonLocation[0]);
                        tryMove(dir);
                        if (isEnemyNearby(enemyRobotsNearMe)){
                            MapLocation aimSpot = enemyRobotsNearMe[0].getLocation();
                            if (isLineOfSightClear(rc.getLocation(), aimSpot)){
                                fireAtEnemy(aimSpot);
                            }
                        }
                    }

                }

                else if (firstTurn != 0){
                    myOrders = rc.readBroadcast(myBroadcastChannel);
                    System.out.println("My orders are: " + myOrders + "\r\n");
                    missionOrders = rc.readBroadcast(1);
                    if (myOrders == 000){
                        tryMove(dir);
                        if (isEnemyNearby(enemyRobotsNearMe)){
                            MapLocation aimSpot = enemyRobotsNearMe[0].getLocation();
                            if (isLineOfSightClear(rc.getLocation(), aimSpot)){
                                fireAtEnemy(aimSpot);
                            }
                        }
                    }
                    if (myOrders == 001) {
                        dir = randomDirection();
                        if (rc.getLocation().distanceTo(soldierStartLocation) < 10){
                            tryMove(dir);
                        }
                        else {
                            dir = rc.getLocation().directionTo(soldierStartLocation);
                            tryMove(dir);
                        }

                        if (isEnemyNearby(enemyRobotsNearMe)){
                            MapLocation aimSpot = enemyRobotsNearMe[0].getLocation();
                            if (isLineOfSightClear(rc.getLocation(), aimSpot)){
                                fireAtEnemy(aimSpot);
                            }
                        }
                    }
                    if (myOrders == 002) {
                        tryMove(dir);
                        if (isEnemyNearby(enemyRobotsNearMe)){
                            MapLocation aimSpot = enemyRobotsNearMe[0].getLocation();
                            if (isLineOfSightClear(rc.getLocation(), aimSpot)){
                                fireAtEnemy(aimSpot);
                            }
                        }
                    }
                    if (myOrders == 003) {
                        tryMove(dir);
                        if (isEnemyNearby(enemyRobotsNearMe)){
                            MapLocation aimSpot = enemyRobotsNearMe[0].getLocation();
                            if (isLineOfSightClear(rc.getLocation(), aimSpot)){
                                fireAtEnemy(aimSpot);
                            }
                        }
                    }
                    if (myOrders == 004) {
                        initialEnemyArchonLocation = rc.getInitialArchonLocations(enemyTeam);
                        dir = rc.getLocation().directionTo(initialEnemyArchonLocation[0]);
                        tryMove(dir);
                        if (isEnemyNearby(enemyRobotsNearMe)){
                            MapLocation aimSpot = enemyRobotsNearMe[0].getLocation();
                            if (isLineOfSightClear(rc.getLocation(), aimSpot)){
                                fireAtEnemy(aimSpot);
                            }
                        }
                    }
                }

                Clock.yield();

            } catch (Exception e) {
                System.out.println("Soldier Exception");
                e.printStackTrace();
            }
        }
    }

    private static boolean isEnemyNearby(RobotInfo[] robots){
        if (robots.length > 0){
            if (robots[0].getLocation().distanceTo(rc.getLocation()) < 4){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    private static boolean isLineOfSightClear(MapLocation myLocation, MapLocation enemyLocation) {
        System.out.println("Checking Line of Sight!\r\n");
        float distance = myLocation.distanceTo(enemyLocation);
        Direction dir = myLocation.directionTo(enemyLocation);
        boolean nothingInTheWay;

        while (true) {
            try {
                for (float i = 0; i < distance; i++) {
                    nothingInTheWay = rc.isCircleOccupied(myLocation.add(dir, distance), 1);
                    if (!nothingInTheWay){
                        System.out.println("Found something in the way at: " + myLocation.add(dir, distance) + "\r\n");
                        return false;
                    }
                    else {
                        System.out.println("Nothing in the way!\r\n");
                        return true;
                    }
                }

            } catch (Exception e) {
                System.out.println("Soldier Exception");
                e.printStackTrace();
            }
        }
    }

    private static void fireAtEnemy(MapLocation aimLoc){
        if (rc.canFireSingleShot()){
            while(true){
                try{
                    rc.fireSingleShot(rc.getLocation().directionTo(aimLoc));
                    break;
                } catch (Exception e) {
                    System.out.println("Soldier Exception");
                    e.printStackTrace();
                }
            }

        }
    }

    private static Direction findBuildSpace(double radius){
        boolean noSmallSpace = true;
        while (noSmallSpace) {
            try {
                dir = randomDirection();
                noSmallSpace = rc.isCircleOccupied(rc.getLocation().add(dir), (float) radius);
                break;
            } catch (Exception e) {
                System.out.println("Archon Exception");
                e.printStackTrace();
            }
        }
        return dir;
    }

    static void runScout() throws GameActionException {
        System.out.println("I'm a scout!");
        while(true){
            try{

            } catch (Exception e) {
                System.out.println("Scout Exception");
                e.printStackTrace();
            }
        }
    }

    static void runTank() throws GameActionException {
        System.out.println("I'm a tank!");
        while(true){
            try{
                enemyTeam = rc.getTeam().opponent();
                initialEnemyArchonLocation = rc.getInitialArchonLocations(enemyTeam);
                dir = rc.getLocation().directionTo(initialEnemyArchonLocation[0]);
                tryMove(dir);

            } catch (Exception e) {
                System.out.println("Tank Exception");
                e.printStackTrace();
            }
        }
    }



    static void runLumberjack() throws GameActionException {
        System.out.println("I'm a lumberjack!");
        while (true) {
            try {
                if (firstTurn == 0) {
                    firstTurn++;
                    enemyTeam = rc.getTeam().opponent();
                    initialEnemyArchonLocation = rc.getInitialArchonLocations(enemyTeam);
                    lumberjackStartLoc = rc.getLocation();
                    int t = rc.getID()/100;
                    int r = rc.getID() % 100;
                    myBroadcastChannel = t + r;
                    missionOrders = rc.readBroadcast(1);
                    myTeam = rc.getTeam();
                    myOrders = rc.readBroadcast(myBroadcastChannel);
                    System.out.println("My Broadcast Channel is: " + myBroadcastChannel + "\r\n");
                    System.out.println("My orders are: " + myOrders + "\r\n");
                    dir = randomDirection();
                    if (myOrders == 000) {
                        rc.broadcast(myBroadcastChannel, 002);
                        myOrders = 002;
                    }
                    if (myOrders == 002) {
                        if (senseTreesForChopping()){
                            System.out.println("Nearest tree at: " + treesNearMe[0].getLocation() + "\r\n");
                            dir = rc.getLocation().directionTo(treesNearMe[0].getLocation());
                            nearestTree = treesNearMe[0].getLocation();
                            tryMove(dir);
                            canShakeChopWillShakeChop(nearestTree);
                        }
                        else {
                            System.out.println("Not sensing any trees!\r\n");
                            dir = rc.getLocation().directionTo(initialEnemyArchonLocation[0]);
                            tryMove(dir);
                        }
                    }
                    if (myOrders == 3){
                        lumberjackStartLoc = rc.getLocation();
                        if (senseTreesForChopping()){
                            System.out.println("Nearest tree at: " + treesNearMe[0].getLocation() + "\r\n");
                            dir = rc.getLocation().directionTo(treesNearMe[0].getLocation());
                            nearestTree = treesNearMe[0].getLocation();
                            tryMove(dir);
                            canShakeChopWillShakeChop(nearestTree);
                        }
                        else {
                            dir = rc.getLocation().directionTo(initialEnemyArchonLocation[0]);
                            System.out.println("Not sensing any trees!\r\n");
                            tryMove(dir);
                            myOrders = 2;
                        }
                    }
                }

                else if (firstTurn != 0){
                    System.out.println("My Broadcast Channel is: " + myBroadcastChannel + "\r\n");
                    System.out.println("My orders are: " + myOrders + "\r\n");
                    myOrders = rc.readBroadcast(myBroadcastChannel);
                    enemyNearby = false;
                    enemyRobots = rc.senseNearbyRobots(7, enemyTeam);
                    if (enemyRobots.length > 0){
                        dir = rc.getLocation().directionTo(enemyRobots[0].getLocation());
                        enemyNearby = true;
                    }
                    if (myOrders == 000 && !enemyNearby) {
                        rc.broadcast(myBroadcastChannel, 002);
                        myOrders = 002;
                    }
                    if (myOrders == 001 && enemyNearby){
                        tryMove(dir);
                        if (rc.canStrike()){
                            rc.strike();
                        }
                    }
                    if (myOrders == 002 && !enemyNearby) {
                        if (senseTreesForChopping()){
                            dir = rc.getLocation().directionTo(treesNearMe[0].getLocation());
                            tryMove(dir);
                            System.out.println("I want to move this way: " + dir +  "\r\n");
                            nearestTree = treesNearMe[0].getLocation();
                            canShakeChopWillShakeChop(nearestTree);
                        }
                        else {
                            System.out.println("I want to move towards the enemy!" + "\r\n");
                            dir = rc.getLocation().directionTo(initialEnemyArchonLocation[0]);
                            tryMove(dir);
                        }
                    }

                    if (myOrders == 002 && enemyNearby){
                        tryMove(dir);
                        if (rc.canStrike()){
                            rc.strike();
                        }
                    }

                    if (myOrders == 3 && loopBreaker >= 15){
                        myOrders = 2;
                        rc.broadcast(myBroadcastChannel, myOrders);
                    }

                    if (myOrders == 3 && lumberjackGoHome == 0){
                        if (rc.getLocation().distanceTo(lumberjackStartLoc) < 10){
                            System.out.println("I'm " + rc.getLocation().distanceTo(lumberjackStartLoc) + " away and I'm going to try chopping!" + "\r\n");
                            if (senseTreesForChopping()){
                                nearestTree = treesNearMe[0].getLocation();
                                dir = rc.getLocation().directionTo(treesNearMe[0].getLocation());
                                tryMove(dir);
                                System.out.println("I want to move this way: " + dir +  "\r\n");
                                canShakeChopWillShakeChop(nearestTree);
                            }
                            else {
                                System.out.println("No trees nearby. Moving home." + "\r\n");
                                dir = rc.getLocation().directionTo(lumberjackStartLoc);
                                tryMove(dir);
                                lumberjackGoHome++;
                            }
                        }
                        else if (rc.getLocation().distanceTo(lumberjackStartLoc) > 10){
                            System.out.println("I'm " + rc.getLocation().distanceTo(lumberjackStartLoc) + " away and I'm going to try moving home!" + "\r\n");
                            dir = rc.getLocation().directionTo(lumberjackStartLoc);
                            tryMove(dir);
                            lumberjackGoHome++;
                        }
                    }
                    else if (myOrders == 3 && lumberjackGoHome <= 8){
                        System.out.println("I've been moving towards home for 8 or fewer turns." + "\r\n");
                        dir = rc.getLocation().directionTo(lumberjackStartLoc);
                        tryMove(dir);
                        lumberjackGoHome++;
                    }
                    else if (myOrders == 3 && lumberjackGoHome == 9){
                        System.out.println("I am going to try and cut some trees now." + "\r\n");
                        dir = rc.getLocation().directionTo(lumberjackStartLoc);
                        tryMove(dir);
                        lumberjackGoHome = 0;
                        loopBreaker++;
                    }
                }

                Clock.yield();

            } catch (Exception e) {
                System.out.println("Lumberjack Exception");
                e.printStackTrace();
            }
        }
    }

    private static boolean senseTreesForChopping(){
        while(true){
            try{
                treesNearMe = rc.senseNearbyTrees(1, Team.NEUTRAL);
                if (treesNearMe.length == 0){
                    treesNearMe = rc.senseNearbyTrees(2, Team.NEUTRAL);
                    if (treesNearMe.length == 0){
                        treesNearMe = rc.senseNearbyTrees(3, Team.NEUTRAL);
                        if (treesNearMe.length == 0){
                            treesNearMe = rc.senseNearbyTrees(4, Team.NEUTRAL);
                            if (treesNearMe.length == 0){
                                treesNearMe = rc.senseNearbyTrees(5, Team.NEUTRAL);
                                if (treesNearMe.length == 0){
                                    treesNearMe = rc.senseNearbyTrees(6, Team.NEUTRAL);
                                    if (treesNearMe.length == 0){
                                        treesNearMe = rc.senseNearbyTrees(7, Team.NEUTRAL);
                                        if (treesNearMe.length == 0){
                                            System.out.println("No trees for chopping!" + "\r\n");
                                            return false;
                                        }
                                        else {
                                            System.out.println("Tree within: " + treesNearMe[0].getLocation().distanceTo(rc.getLocation()) + "\r\n");
                                            return true;
                                        }
                                    }
                                    else {
                                        System.out.println("Tree within: " + treesNearMe[0].getLocation().distanceTo(rc.getLocation()) + "\r\n");
                                        return true;
                                    }
                                }
                                else {
                                    System.out.println("Tree within: " + treesNearMe[0].getLocation().distanceTo(rc.getLocation()) + "\r\n");
                                    return true;
                                }
                            }
                            else {
                                System.out.println("Tree within: " + treesNearMe[0].getLocation().distanceTo(rc.getLocation()) + "\r\n");
                                return true;
                            }
                        }
                        else {
                            System.out.println("Tree within: " + treesNearMe[0].getLocation().distanceTo(rc.getLocation()) + "\r\n");
                            return true;
                        }
                    }
                    else {
                        System.out.println("Tree within: " + treesNearMe[0].getLocation().distanceTo(rc.getLocation()) + "\r\n");
                        return true;
                    }
                }
                else {
                    System.out.println("Tree within: " + treesNearMe[0].getLocation().distanceTo(rc.getLocation()) + "\r\n");
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Lumberjack Exception");
                e.printStackTrace();
            }
        }
    }

    private static void updateLumberjackCount(){
        while(true) {
            try {
                System.out.println("Updating lumberjack count!");
                int x = rc.readBroadcast(GARDENER_BUILD_LUMBERJACK_RECEIVE);
                x++;
                rc.broadcast(GARDENER_BUILD_LUMBERJACK_RECEIVE, x);
                System.out.println("Updated lumberjack count to: " + x + "\r\n");
                break;
            } catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }
    }



    private static void canShakeChopWillShakeChop(MapLocation treeLocation){
        if (rc.canShake(treeLocation)){
            while(true){
                try {
                    rc.shake(treeLocation);
                    break;
                }catch (Exception e) {
                    System.out.println("Lumberjack Exception");
                    e.printStackTrace();
                }
            }
        }
        if (rc.canChop(nearestTree)){
            while(true){
                try{
                    rc.chop(nearestTree);
                    break;
                }catch (Exception e) {
                    System.out.println("Lumberjack Exception");
                    e.printStackTrace();
                }
            }

        }
    }


    /**
     * Returns a random Direction
     * @return a random Direction
     */
    static Direction randomDirection() {
        return new Direction((float)Math.random() * 4 * (float)Math.PI);
    }

    /**
     * Attempts to move in a given direction, while avoiding small obstacles directly in the path.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir) throws GameActionException {
        return tryMove(dir,30,4);
    }

    /**
     * Attempts to move in a given direction, while avoiding small obstacles direction in the path.
     *
     * @param dir The intended direction of movement
     * @param degreeOffset Spacing between checked directions (degrees)
     * @param checksPerSide Number of extra directions checked on each side, if intended direction was unavailable
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir, float degreeOffset, int checksPerSide) throws GameActionException {

        // First, try intended direction
        if (rc.canMove(dir)) {
            rc.move(dir);
            System.out.println("Moved in direction: " + dir + "\r\n");
            return true;
        }

        // Now try a bunch of similar angles
        boolean moved = false;
        int currentCheck = 1;
        System.out.println("Had to change direction!\r\n");
        while(currentCheck<=checksPerSide) {
            // Try the offset of the left side
            if(rc.canMove(dir.rotateLeftDegrees(degreeOffset*currentCheck))) {
                rc.move(dir.rotateLeftDegrees(degreeOffset*currentCheck));
                System.out.println("Moved in direction: " + dir + "\r\n");
                return true;
            }
            // Try the offset on the right side
            if(rc.canMove(dir.rotateRightDegrees(degreeOffset*currentCheck))) {
                rc.move(dir.rotateRightDegrees(degreeOffset*currentCheck));
                System.out.println("Moved in direction: " + dir + "\r\n");
                return true;
            }
            // No move performed, try slightly further
            currentCheck++;
        }

        // A move never happened, so return false.
        return false;
    }

    /**
     * A slightly more complicated example function, this returns true if the given bullet is on a collision
     * course with the current robot. Doesn't take into account objects between the bullet and this robot.
     *
     * @param bullet The bullet in question
     * @return True if the line of the bullet's path intersects with this robot's current position.
     */
    static boolean willCollideWithMe(BulletInfo bullet) {
        MapLocation myLocation = rc.getLocation();

        // Get relevant bullet information
        Direction propagationDirection = bullet.dir;
        MapLocation bulletLocation = bullet.location;

        // Calculate bullet relations to this robot
        Direction directionToRobot = bulletLocation.directionTo(myLocation);
        float distToRobot = bulletLocation.distanceTo(myLocation);
        float theta = propagationDirection.radiansBetween(directionToRobot);

        // If theta > 90 degrees, then the bullet is traveling away from us and we can break early
        if (Math.abs(theta) > Math.PI/2) {
            return false;
        }

        // distToRobot is our hypotenuse, theta is our angle, and we want to know this length of the opposite leg.
        // This is the distance of a line that goes from myLocation and intersects perpendicularly with propagationDirection.
        // This corresponds to the smallest radius circle centered at our location that would intersect with the
        // line that is the path of the bullet.
        float perpendicularDist = (float)Math.abs(distToRobot * Math.sin(theta)); // soh cah toa :)

        return (perpendicularDist <= rc.getType().bodyRadius);
    }
}
