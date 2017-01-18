

        package NRUrefbot;
        import battlecode.common.*;


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
                    robotCount = robotsNearMe.length;
                    distanceToInitialGardener = getDistanceToInitialGardener();
                    closestGardener();
                }



                //MapLocation nearestRobotLocation = new MapLocation(999,999);
                System.out.println("Robots near me: " + robotCount + "\r\n");
                //System.out.println("Distance to nearest robot: " + ((robotsNearMe[0].getLocation()).distanceTo(rc.getLocation().add(dir))));
                //float nearestRobotDistance = nearestRobotLocation.distanceTo(rc.getLocation());

//TODO FIRST ARCHON TURN

                if (firstTurn == 0){
                    myTeam = rc.getTeam();
                    enemyTeam = myTeam.opponent();
                    robotsNearMe = rc.senseNearbyRobots();
                    spaceAvailable = howMuchSpace();
                    System.out.println("Space available: " + spaceAvailable + "\r\n");

                    if (spaceAvailable >= 9){
                        archonBehavior = 1;
                        rc.broadcast(1, 001);
                        canBuildAllTrees = findSpaceFreeForTrees(rc.getLocation().add(dir));
                        if (canBuildAllTrees){
                            System.out.println("I can build all the trees!" + "\r\n");
                            if (atPlantLocation() && rc.canHireGardener(dir)){
                                rc.hireGardener(dir);
                                System.out.println("I built a gardener at: " + rc.getLocation().add(dir, 3) + "\r\n");
                                countGardener++;
                                robotsNearMe = rc.senseNearbyRobots();
                                robotJustBuilt = rc.senseRobotAtLocation(rc.getLocation().add(dir, 3));
                                int t = robotJustBuilt.getID()/100;
                                int r = robotJustBuilt.getID() % 100;
                                initialGardenerStartLocation = robotJustBuilt.getLocation();
                                firstGardenerID = t + r;
                                System.out.println("First gardener ID: " + firstGardenerID + "\r\n");
                                rc.broadcast(firstGardenerID, 001);
                                System.out.println("I broadcasted this gardener: " + firstGardenerID + " orders: 001\r\n");
                                lastArchonBuildLocation = initialGardenerStartLocation;
                            }
                            else if (atPlantLocation()){
                                firstTurn++;
                                Clock.yield();
                            }

                        }
                        else {
                            System.out.println("I can't build all the trees but there is space to do it!" + "\r\n");
                            dir = rc.getLocation().directionTo(buildGardenerHere);
                            tryMove(dir);
                            firstTurn++;
                        }
                    }
                    else if (spaceAvailable < 9){
                        archonBehavior = 2;
                        rc.broadcast(1, 002);
                        //broadcastTreeLocations();
                        //canBuildAllTrees = findSpaceFreeForTrees(rc.getLocation().add(dir));
                        //if (canBuildAllTrees){
                        //   System.out.println("I can build all the trees!" + "\r\n");
                        //    atPlantLocation();
                        dir = findBuildSpace(2.01);
                        if (rc.canHireGardener(dir)){
                            rc.hireGardener(dir);
                            System.out.println("I built a gardener at: " + rc.getLocation().add(dir, 3) + "\r\n");
                            countGardener++;
                            robotsNearMe = rc.senseNearbyRobots();
                            robotJustBuilt = rc.senseRobotAtLocation(rc.getLocation().add(dir, 3));
                            int t = robotJustBuilt.getID()/100;
                            int r = robotJustBuilt.getID() % 100;
                            initialGardenerStartLocation = robotJustBuilt.getLocation();
                            firstGardenerID = t + r;
                            System.out.println("First gardener ID: " + firstGardenerID + "\r\n");
                            rc.broadcast(firstGardenerID, 002);
                            System.out.println("I broadcasted this gardener: " + firstGardenerID + " orders: 002\r\n");
                            lastArchonBuildLocation = initialGardenerStartLocation;
                        }

                        else {
                            distanceToEnemy = howFartoEnemy();
                            System.out.println("Distance to enemy: " + distanceToEnemy);
                            firstTurn++;
                            }
                        }

                    }

//TODO NORMAL ARCHON TURN

                else if (firstTurn != 0 /*&& countGardener < 4 /*&& archonBehavior == 1*/ && robotsNearMe.length < 3) {
                    if (rc.canHireGardener(dir) && archonWaitingToPlant == 1){
                        archonWaitingToPlant = 0;
                    }
                    else if (archonWaitingToPlant == 1){
                        Clock.yield();
                    }
                    canBuildAllTrees = findSpaceFreeForTrees(lastArchonBuildLocation.add(randomDirection(), 4));
                    if (canBuildAllTrees){
                        System.out.println("I can build all the trees!" + "\r\n");
                        if (atPlantLocation()){
                            if (rc.canHireGardener(dir)){
                                System.out.println("I can build a gardener here!" + buildGardenerHere + "\r\n");
                                rc.hireGardener(dir);
                                System.out.println("I built a gardener at: " + rc.getLocation().add(dir, 3) + "\r\n");
                                countGardener++;
                                robotsNearMe = rc.senseNearbyRobots();
                                robotJustBuilt = rc.senseRobotAtLocation(rc.getLocation().add(dir, 3));
                                int t = robotJustBuilt.getID()/100;
                                int r = robotJustBuilt.getID() % 100;
                                lastArchonBuildLocation = robotJustBuilt.getLocation();
                                currentGardenerID = t + r;
                                System.out.println("Current gardener ID: " + currentGardenerID + "\r\n");
                                if (archonBehavior == 1){
                                    rc.broadcast(currentGardenerID, 001);
                                    System.out.println("I broadcasted this gardener: " + currentGardenerID + " orders: 001\r\n");
                                }
                                if (archonBehavior == 2){
                                    rc.broadcast(currentGardenerID, 002);
                                    System.out.println("I broadcasted this gardener: " + currentGardenerID + " orders: 002\r\n");
                                }
                            }
                            else if(atPlantLocation()) {
                                System.out.println("Will wait here till I can plant!" + "\r\n");
                                archonWaitingToPlant = 1;
                                Clock.yield();
                            }
                        }
                        else {
                            System.out.println("I need to get to a different spot!" + "\r\n");
                            tryMove(dir);
                        }
                    }
                    else if (stuckTime < 20){
                        System.out.println("Can't find space for trees!" + "\r\n");
                        tryMove(dir);
                        stuckTime++;
                    }
                    else if (stuckTime >= 20){
                        tryMove(Direction.getNorth());
                        System.out.println("I'm stuck!" + "\r\n");
                        unstuckTime++;

                        if (unstuckTime >= 20){
                            stuckTime = 0;
                            unstuckTime = 0;
                        }
                    }
                }
                else if (firstTurn != 0 && countGardener < 4 && archonBehavior == 0){

                }

                //TODO Build

                // Randomly attempt to build a gardener in this direction

                //TODO Command and Control
                //TODO yield

                // Broadcast archon's location for other robots on the team to know ******EDITED OUT BY TAD ON 10 JAN******
                //MapLocation myLocation = rc.getLocation();
                //rc.broadcast(0,(int)myLocation.x);
                //rc.broadcast(1,(int)myLocation.y);

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                float teamBulletCount = rc.getTeamBullets();
                if (teamBulletCount > 500){
                    rc.donate(200);
                }

                Clock.yield();

            } catch (Exception e) {
                System.out.println("Archon Exception");
                e.printStackTrace();
            }
        }
    }

    public static boolean atPlantLocation(){
        myEdgeFacingPlantLocation = rc.getLocation().add(rc.getLocation().directionTo(buildGardenerHere), 2);
        distanceToPlantLocation = buildGardenerHere.distanceTo(myEdgeFacingPlantLocation);
        directionToPlantLocation = new Direction(myEdgeFacingPlantLocation, buildGardenerHere);
        System.out.println("My edge pointing at the Plant Location: " + myEdgeFacingPlantLocation + "\r\n");
        System.out.println("Distance from my edge to Plant Location: " + distanceToPlantLocation + "\r\n");
        dir = directionToPlantLocation;
        if (distanceToPlantLocation > 1.1){
            return false;
        }
        else if (distanceToPlantLocation <= 1.1 && outsideMyRadius(rc.getLocation(), buildGardenerHere)){
            System.out.println("No need to flip direction!" + "\r\n");
            return true;
        }
        else if (distanceToPlantLocation <= 1.1 && !outsideMyRadius(rc.getLocation(), buildGardenerHere)){
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
            if (robotLocations[i].getLocation().distanceTo(buildSite) < 4){
                return false;
            }
        }
        return true;
    }

    public static void closestGardener() {
        for (int i = robotCount; i-- > 0; ) {
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
                    System.out.println("Distance to this tree edge: " + c);
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
        Direction directionToFace = Direction.getNorth();
        while(rc.canSenseAllOfCircle(thisLocation, 3) && spaceCheck < 10){
            try {
                boolean a;
                boolean b;
                boolean c;
                a = rc.isCircleOccupiedExceptByThisRobot(thisLocation, 2);
                b = rc.onTheMap(thisLocation, 3);
                c = outsideAnotherGarden(thisLocation, robotsNearMe);
                if (!a && b && c){
                    buildGardenerHere = thisLocation;
                    System.out.println("You can build a gardener at: " + buildGardenerHere + "\r\n");
                    return true;
                }
                else {
                    directionToFace = directionToFace.rotateLeftDegrees((float)Math.ceil(Math.random() * 1000));
                    System.out.println("Rotating Left: " + directionToFace + "\r\n");
                    thisLocation = thisLocation.add(directionToFace, 1);
                    System.out.println("This location is now: " + thisLocation + "\r\n");
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


        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {

                //TODO Bullet Sense
                //TODO Receive Orders
                if (firstTurn == 0) {
                    int t = rc.getID()/100;
                    int r = rc.getID() % 100;
                    myBroadcastChannel = t + r;
                    missionOrders = rc.readBroadcast(1);
                    myTeam = rc.getTeam();
                    myOrders = rc.readBroadcast(myBroadcastChannel);
                    System.out.println("My Broadcast Channel is: " + myBroadcastChannel + "\r\n");
                    System.out.println("My orders are: " + myOrders + "\r\n");
                    dir = randomDirection();
                    if (myOrders == 002 && rc.canBuildRobot(RobotType.LUMBERJACK, dir)) {
                        rc.buildRobot(RobotType.LUMBERJACK, dir);
                        System.out.println("I built a lumberjack!" + "\r\n");
                        robotsNearMe = rc.senseNearbyRobots();
                        robotJustBuilt = rc.senseRobotAtLocation(rc.getLocation().add(dir, 2));
                        int z = robotJustBuilt.getID()/100;
                        int x = robotJustBuilt.getID() % 100;
                        currentLumberjackID = z + x;
                        System.out.println("Current lumberjack ID: " + currentLumberjackID + "\r\n");
                        rc.broadcast(currentLumberjackID, 002);
                        System.out.println("I assigned the lumberjack the following broadcast ID: " + currentLumberjackID + "\r\n");
                        firstTurn++;
                    }
                    if (myOrders == 001) {
                        firstTurn++;
                        if (rc.canPlantTree(plant_direction)) {
                            rc.plantTree(plant_direction);
                            System.out.println("I planted a tree!" + "\r\n");
                        } else {
                            System.out.println("I couldn't plant a tree. Currently facing: " + plant_direction + "\r\n");
                        }

                        plant_iteration = plant_iteration + (PI / 3);
                        plant_direction = new Direction(plant_iteration);

                        waterInDirection(treeDirectionToWater());
                    }
                    if (myOrders == 003) {
                        dir = findBuildSpace(1.01);
                        if (rc.canBuildRobot(RobotType.LUMBERJACK, dir)) {
                            rc.buildRobot(RobotType.LUMBERJACK, dir);
                            System.out.println("I built a lumberjack!" + "\r\n");
                            robotsNearMe = rc.senseNearbyRobots();
                            robotJustBuilt = rc.senseRobotAtLocation(rc.getLocation().add(dir, 2));
                            int z = robotJustBuilt.getID()/100;
                            int x = robotJustBuilt.getID() % 100;
                            currentLumberjackID = z + x;
                            System.out.println("Current lumberjack ID: " + currentLumberjackID + "\r\n");
                            rc.broadcast(currentLumberjackID, 003);
                            System.out.println("I assigned the lumberjack the following broadcast ID: " + currentLumberjackID + "\r\n");
                            firstTurn++;
                        }

                    }
                }

                //TODO Movement
                //TODO Build

                if (firstTurn != 0) {
                    myTrees = rc.senseNearbyTrees(1, myTeam);
                    if (myOrders == 000) {
                        if (rc.canPlantTree(plant_direction)) {
                            rc.plantTree(plant_direction);
                            System.out.println("I planted a tree!" + "\r\n");
                        } else {
                            System.out.println("I couldn't plant a tree. Currently facing: " + plant_direction + "\r\n");
                        }

                        plant_iteration = plant_iteration + (PI / 3);
                        plant_direction = new Direction(plant_iteration);

                        waterInDirection(treeDirectionToWater());
                    }

                    if (myOrders == 001) {
                        if (rc.canPlantTree(plant_direction)) {
                            rc.plantTree(plant_direction);
                            System.out.println("I planted a tree!" + "\r\n");
                        } else {
                            System.out.println("I couldn't plant a tree. Currently facing: " + plant_direction + "\r\n");
                        }

                        plant_iteration = plant_iteration + (PI / 3);
                        plant_direction = new Direction(plant_iteration);

                        waterInDirection(treeDirectionToWater());
                    }


                    if (myOrders == 002){
                        dir = findBuildSpace(1.01);
                        if (rc.canBuildRobot(RobotType.LUMBERJACK, dir)){
                            rc.buildRobot(RobotType.LUMBERJACK, dir);
                            System.out.println("I built a lumberjack!" + "\r\n");
                            robotsNearMe = rc.senseNearbyRobots();
                            robotJustBuilt = rc.senseRobotAtLocation(rc.getLocation().add(dir, 2));
                            int z = robotJustBuilt.getID()/100;
                            int x = robotJustBuilt.getID() % 100;
                            currentLumberjackID = z + x;
                            System.out.println("Current lumberjack ID: " + currentLumberjackID + "\r\n");
                            rc.broadcast(currentLumberjackID, 003);
                            System.out.println("I assigned the lumberjack the following broadcast ID: " + currentLumberjackID + "\r\n");
                        }
                        else {
                            if (rc.canPlantTree(plant_direction)) {
                                rc.plantTree(plant_direction);
                                System.out.println("I planted a tree!" + "\r\n");
                            } else {
                                System.out.println("I couldn't plant a tree. Currently facing: " + plant_direction + "\r\n");
                            }

                            plant_iteration = plant_iteration + (PI / 3);
                            plant_direction = new Direction(plant_iteration);

                            waterInDirection(treeDirectionToWater());

                        }
                    }
                }
                Clock.yield();
            } catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }
    }

//    private static int findBroadcastChannel(int channel){
//        int x = channel;
//        int y;
//        boolean a = true;
//        while(a){
//            try {
//                y = rc.readBroadcast(x);
//                if (y == 0){
//                    System.out.println("The broadcast channel is: " + x);
//                    return x;
//                }
//                else {
//                    x = x++;
//                }
//
//
//            } catch (Exception e) {
//                System.out.println("This isn't working!");
//                e.printStackTrace();
//            }
//        }
//        System.out.println("Bad return: " + x);
//        return x;
//    }

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
        Team enemy = rc.getTeam().opponent();

        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {

                //TODO Bullet Sense
                //TODO Receive Orders
                //TODO Movement
                //TODO Attack
                //TODO yield

                MapLocation myLocation = rc.getLocation();

                // See if there are any nearby enemy robots
                RobotInfo[] robots = rc.senseNearbyRobots(-1, enemy);

                // If there are some...
                if (robots.length > 0) {
                    // And we have enough bullets, and haven't attacked yet this turn...
                    if (rc.canFireSingleShot()) {
                        // ...Then fire a bullet in the direction of the enemy.
                        rc.fireSingleShot(rc.getLocation().directionTo(robots[0].location));
                    }
                }

                // Move randomly
                tryMove(randomDirection());

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Soldier Exception");
                e.printStackTrace();
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



    static void runLumberjack() throws GameActionException {
        System.out.println("I'm a lumberjack!");



        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
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
                    if (myOrders == 001) {
                        broadcastData = rc.readBroadcast(22);
                        broadcastParse = Integer.toString(broadcastData);
                        String a = broadcastParse.substring(0, 3);
                        String b = broadcastParse.substring(3, 6);
                        treeLocationX = Float.parseFloat(a);
                        treeLocationY = Float.parseFloat(b);
                        nearestTree = new MapLocation(treeLocationX, treeLocationY);
                        System.out.println("The nearest tree is at: " + nearestTree + "\r\n");
                        dir = nearestTree.directionTo(rc.getLocation());
                        System.out.println("I want to move this way: " + dir + "\r\n");
                        tryMove(dir);
                        if (rc.canShake(nearestTree)){
                            rc.shake(nearestTree);
                        }
                        if (rc.canChop(nearestTree)){
                            rc.chop(nearestTree);
                        }
                    }
                    if (myOrders == 002) {
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
                                                    System.out.println("Not sensing any trees!\r\n");
                                                    dir = rc.getLocation().directionTo(initialEnemyArchonLocation[0]);
                                                    tryMove(dir);
                                                    Clock.yield();

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        System.out.println("Nearest tree at: " + treesNearMe[0].getLocation() + "\r\n");
                        dir = rc.getLocation().directionTo(treesNearMe[0].getLocation());
                        nearestTree = treesNearMe[0].getLocation();
                        tryMove(dir);
                        if (rc.canShake(nearestTree)){
                            rc.shake(nearestTree);
                        }
                        if (rc.canChop(nearestTree)){
                            rc.chop(nearestTree);
                        }

                    }
                    if (myOrders == 3){
                        lumberjackStartLoc = rc.getLocation();
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
                                                    dir = rc.getLocation().directionTo(initialEnemyArchonLocation[0]);
                                                    System.out.println("Not sensing any trees!\r\n");
                                                    tryMove(dir);
                                                    myOrders = 2;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        dir = rc.getLocation().directionTo(treesNearMe[0].getLocation());
                        nearestTree = treesNearMe[0].getLocation();
                        tryMove(dir);
                        if (rc.canShake(nearestTree)){
                            rc.shake(nearestTree);
                        }
                        if (rc.canChop(nearestTree)){
                            rc.chop(nearestTree);
                        }

                    }
                }


                else if (firstTurn != 0){
                    enemyNearby = false;
                    enemyRobots = rc.senseNearbyRobots(7, enemyTeam);
                    if (enemyRobots.length > 0){
                        dir = rc.getLocation().directionTo(enemyRobots[0].getLocation());
                        enemyNearby = true;
                    }
                    if (myOrders == 001 && !enemyNearby) {
                        broadcastData = rc.readBroadcast(22);
                        broadcastParse = Integer.toString(broadcastData);
                        String a = broadcastParse.substring(0, 3);
                        String b = broadcastParse.substring(3, 6);
                        treeLocationX = Float.parseFloat(a);
                        treeLocationY = Float.parseFloat(b);
                        nearestTree = new MapLocation(treeLocationX, treeLocationY);
                        System.out.println("The nearest tree is at: " + nearestTree + "\r\n");
                        dir = rc.getLocation().directionTo(nearestTree);
                        System.out.println("I want to move this way: " + dir + "\r\n");
                        tryMove(dir);
                        if (rc.canShake(nearestTree)){
                            rc.shake(nearestTree);
                            System.out.println("I'm shaking this tree!" + "\r\n");
                        }
                        if (rc.canChop(nearestTree)){
                            rc.chop(nearestTree);
                            System.out.println("I'm chopping this tree!" + "\r\n");
                        }
                    }
                    if (myOrders == 001 && enemyNearby){
                        tryMove(dir);
                        if (rc.canStrike()){
                            rc.strike();
                        }
                    }
                    if (myOrders == 002 && !enemyNearby) {
                        treesNearMe = rc.senseNearbyTrees(2, Team.NEUTRAL);
                        if (treesNearMe.length > 0){
                            dir = rc.getLocation().directionTo(treesNearMe[0].getLocation());
                            tryMove(dir);
                            System.out.println("I want to move this way: " + dir +  "\r\n");
                            nearestTree = treesNearMe[0].getLocation();
                            if (rc.canShake(nearestTree)){
                                rc.shake(nearestTree);
                                System.out.println("I'm shaking this tree!" + "\r\n");
                            }
                            if (rc.canChop(nearestTree)){
                                rc.chop(nearestTree);
                                System.out.println("I'm chopping this tree!" + "\r\n");
                            }
                        }
                        else {
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

                    if (myOrders == 3 && lumberjackGoHome == 0){
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
                                                    dir = rc.getLocation().directionTo(lumberjackStartLoc);
                                                    tryMove(dir);
                                                    lumberjackGoHome++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        dir = rc.getLocation().directionTo(treesNearMe[0].getLocation());
                        nearestTree = treesNearMe[0].getLocation();
                        tryMove(dir);
                        if (rc.canShake(nearestTree)){
                            rc.shake(nearestTree);
                        }
                        if (rc.canChop(nearestTree)){
                            rc.chop(nearestTree);
                        }
                    }
                    if (myOrders == 3 && lumberjackGoHome < 7){
                        dir = rc.getLocation().directionTo(lumberjackStartLoc);
                        tryMove(dir);
                        lumberjackGoHome = 0;
                    }
                }

                Clock.yield();


                //TODO Bullet Sense
                //TODO Receive Orders
                //TODO Movement
                //TODO Attack
                //TODO yield


                // See if there are any enemy robots within striking range (distance 1 from lumberjack's radius)
                //RobotInfo[] robots = rc.senseNearbyRobots(RobotType.LUMBERJACK.bodyRadius+GameConstants.LUMBERJACK_STRIKE_RADIUS, enemy);

                //if(robots.length > 0 && !rc.hasAttacked()) {
                // Use strike() to hit all nearby robots!
                //    rc.strike();
                //} else {
                // No close robots, so search for robots within sight radius
                //    robots = rc.senseNearbyRobots(-1,enemy);
                // If there is a robot, move towards it
                //    if(robots.length > 0) {
//                        MapLocation myLocation = rc.getLocation();
//                        MapLocation enemyLocation = robots[0].getLocation();
//                        Direction toEnemy = myLocation.directionTo(enemyLocation);
//
//                        tryMove(toEnemy);
//                    } else {
//                        // Move Randomly
//                        tryMove(randomDirection());
//                    }
//                }
//
//                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
//                Clock.yield();
//
            } catch (Exception e) {
                System.out.println("Lumberjack Exception");
                e.printStackTrace();
            }
        }
    }


    /**
     * Returns a random Direction
     * @return a random Direction
     */
    static Direction randomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }

    /**
     * Attempts to move in a given direction, while avoiding small obstacles directly in the path.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir) throws GameActionException {
        return tryMove(dir,20,3);
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
            return true;
        }

        // Now try a bunch of similar angles
        boolean moved = false;
        int currentCheck = 1;

        while(currentCheck<=checksPerSide) {
            // Try the offset of the left side
            if(rc.canMove(dir.rotateLeftDegrees(degreeOffset*currentCheck))) {
                rc.move(dir.rotateLeftDegrees(degreeOffset*currentCheck));
                return true;
            }
            // Try the offset on the right side
            if(rc.canMove(dir.rotateRightDegrees(degreeOffset*currentCheck))) {
                rc.move(dir.rotateRightDegrees(degreeOffset*currentCheck));
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
