var simplicityApp = angular.module("simplicityApp", ["ngRoute"]);

console.log("simplicityApp module created");

simplicityApp.config(function($routeProvider) {
    console.log("Initializing ng-router");
    $routeProvider
        .when("/", {
            templateUrl: "ssgview.html",
            controller: "ssgViewController"
        })
        .when("/shipyard", {
            templateUrl: "shipyard.html",
            controller: "shipyardController"
        })
        .when("/diplomacy", {
            templateUrl: "diplomacy.html",
            controller: "diplomacyController"
        })
        .when("/planets", {
            templateUrl: "planets.html",
            controller: "planetsController"
        })
        .when("/ships", {
            templateUrl: "ships.html",
            controller: "shipsController"
        })
        .when("/system/:param", {
            templateUrl: "starsystem.html",
            controller: "systemController"
        })
        .when("/example-system", {
            templateUrl: "starsystem.html",
            controller: "systemController"
        })
        .when("/help", {
            templateUrl: "bug-report.html",
            controller: "bugReportController"
        })
        .when("/options", {
            templateUrl: "options.html",
            controller: "optionsController"
        })
        .when("/research", {
            templateUrl: "research.html",
            controller: "researchController"
        })
        .when("/combat/:param", {
            templateUrl: "combat.html",
            controller: "combatController"
        })
});

simplicityApp.controller('ssgViewController', function($scope, $http) {
    //Scope variables
    $scope.helpMode = false;
    $scope.starSystemGraph = {};
    $scope.noSystemSelected = true;

    //Constants
    var scale = 30;
    var xOffset = 70;
    var yOffset = 40;
    var homeSystemIconsX = [250, 580, 325, -10];
    var homeSystemIconsY = [-10, 190, 460, 255];
    var homeSystemIconNames = ['caticon', 'dogicon', 'horseicon', 'snakeicon'];

    //Regular joe variables
    var starSystems = [];
    var tunnels = [];

    var game = new Phaser.Game(800, 600, Phaser.AUTO, 'phaser-canvas-container',
         { preload: preload, create: create, update: update });

    //Scope functions
    $scope.getHelp = function () {
        $scope.helpMode = !$scope.helpMode;
    }

    $scope.goToSystem = function () {
        window.location.href = "/main.html#/system/" + starSystems[$scope.selectedSystem.index].id;
    };

    //Phaser functions
    function preload () {
        game.load.image('stars', 'assets/starfield.png');
        game.load.image('tinysun', 'assets/tinysun.png');
        game.load.image('caticon', 'assets/races/race1_mapicon.png');
        game.load.image('dogicon', 'assets/races/race2_mapicon.png');
        game.load.image('horseicon', 'assets/races/race3_mapicon.png');
        game.load.image('snakeicon', 'assets/races/race4_mapicon.png');
    }

    function create () {
        console.log("in Create");
        game.add.sprite(0, 0, 'stars');
        var graphics = game.add.graphics(0, 0);
        graphics.lineStyle(3, 0xAAAAAA, 1);
        var systemSprites = [];
        for (i = 0; i < starSystems.length; i++) {
            systemSprites[i] = game.add.sprite(starSystems[i].gridCoordX * scale + xOffset, starSystems[i].gridCoordY * scale + yOffset, 'tinysun');
            systemSprites[i].inputEnabled = true;
            systemSprites[i].anchor.setTo(0.5, 0.5);
            systemSprites[i].events.onInputDown.add(systemClickListener, this);
            systemSprites[i].index = i;
        }
        for (i = 0; i < tunnels.length; i++) {
            graphics.moveTo(tunnels[i].firstSystem.gridCoordX * scale + xOffset, tunnels[i].firstSystem.gridCoordY * scale + yOffset);
            graphics.lineTo(tunnels[i].secondSystem.gridCoordX * scale + xOffset, tunnels[i].secondSystem.gridCoordY * scale + yOffset);
        }
        for (i = 0; i < 4; i++) {
            game.add.sprite(homeSystemIconsX[i] + xOffset, homeSystemIconsY[i] + yOffset, homeSystemIconNames[i]);
        }
    }

    function update () {
    }

    //Other functions
    function getSSGInfo () {
        $http.post("/ssg-info.json")
        .then(
            function successCallback (response) {
                starSystems = response.data.starSystems;
                tunnels = response.data.tunnels;
            },

            function errorCallback (response) {
                console.log("Failed to find SSG info");
            });
    };

    function systemClickListener (systemSprite) {
        $scope.noSystemSelected = false;
        $scope.selectedSystem = systemSprite;
        console.log(starSystems[systemSprite.index]);
        console.log("id of system = " + starSystems[systemSprite.index].id);
        var index = systemSprite.index;
        $scope.selectedSystem.planets = starSystems[index].planets;
        for (i = 0; i < $scope.selectedSystem.planets.length; i++) {
            $scope.selectedSystem.planets[i].icon = getIconStringFromRaceNum($scope.selectedSystem.planets[i].ownerRaceNum);
        }
        $scope.$apply();
    }

    function getIconStringFromRaceNum (ownerRaceNum) {
        switch (ownerRaceNum) {
            case -1:
                return "assets/races/norace_icon.png";
            case 0:
                return "assets/races/race1_icon.jpg";
            case 1:
                return "assets/races/race2_icon.jpg";
            case 2:
                return "assets/races/race3_icon.jpg";
            case 3:
                return "assets/races/race4_icon.jpg";
        }
    }

    getSSGInfo();
});

simplicityApp.controller('diplomacyController', function($scope, $http) {
    $scope.helpMode = false;
    var requiredPcts = [0, 110, 75, 70, 65];
    getPlayerInfo = function () {
        $http.post("/diplomacy-info.json")
        .then(
            function successCallback (response) {
                $scope.players = response.data;
                $scope.requiredPct = requiredPcts[$scope.players.length];
            },

            function errorCallback (response) {
                console.log("No players");
            });
    };
    getPlayerInfo();

    $scope.getHelp = function () {
        $scope.helpMode = !$scope.helpMode;
    }
});

simplicityApp.controller('planetsController', function($scope, $http) {
    $scope.helpMode = false;
    $scope.getHelp = function () {
        $scope.helpMode = !$scope.helpMode;
    }
    getPlanetInfo = function () {
        $http.post("/planets-info.json")
        .then(
            function successCallback (response) {
                $scope.planets = response.data;
                for (i = 0; i < $scope.planets.length; i++) {
                    $scope.planets[i].imageString = "assets/planets/" + $scope.planets[i].imageString + ".png";
                }
            },

            function errorCallback (response) {
                console.log("No planets");
            });
    };
    getPlanetInfo();
});

simplicityApp.controller('shipsController', function($scope, $http) {
    //Scope variables
    $scope.helpMode = false;
    $scope.fighterChoice = "assets/ships/fighter/fighter_";
    $scope.colonizerChoice = "/assets/ships/colonizer/colonizer_";
    $scope.destroyerChoice = "assets/ships/destroyer/destroyer_";
    $scope.cruiserChoice = "assets/ships/cruiser/cruiser_";
    //Regular variables
    var chassisNames = [];
    var chassisCosts = [];
    var destroyerResearched;
    var cruiserResearched;

    //Scope functions
    $scope.getHelp = function () {
        $scope.helpMode = !$scope.helpMode;
    }

    $scope.selectShipType = function (typeNumber) {
        $scope.shipTypeNumber = typeNumber;
    }

    $scope.purchaseShip = function () {
        var newShip = {};
        var index = $scope.shipTypeNumber;

        if (index === 2) {
            if(!destroyerResearched) {
                alert("Destroyer tech not yet researched");
                return;
            }
        }
        if (index === 3) {
            if(!cruiserResearched) {
                alert("Cruiser tech not yet researched");
                return;
            }
        }

        if ($scope.shipName === "" || $scope.shipName == null) {
            newShip.name = chassisNames[index];
        } else {
            newShip.name = $scope.shipName;
        }
        if ($scope.totalProduction > chassisCosts[index]) {
            $scope.totalProduction -= chassisCosts[index];
            newShip.chassis = chassisNames[index].toUpperCase();
            createShip(newShip);
        } else {
            alert("Not enough production");
        }
    }

    function getShipInfo () {
        $http.post("/ships-info.json")
        .then(
            function successCallback (response) {
                $scope.ships = response.data;
            },

            function errorCallback (response) {
                console.log("No ships");
            });
    };

    function getShipyardInfo () {
        $http.post("/shipyard-info.json")
        .then(
            function successCallback (response) {
                chassisNames = response.data.possibleShips;
                chassisCosts = response.data.possibleShipCosts;
                $scope.totalProduction = response.data.productionAvailable;
                for (i = 0; i < chassisNames.length; i++) {
                    chassisNames[i] = chassisNames[i].toLowerCase();
                    chassisNames[i] = chassisNames[i].substring(0, 1).toUpperCase() + chassisNames[i].substring(1);
                }
                destroyerResearched = response.data.destroyerEnabled;
                cruiserResearched = response.data.cruiserEnabled;
                if (destroyerResearched) {
                    $scope.destroyerEnabled = "img-normal";
                } else {
                    $scope.destroyerEnabled = "img-grey";
                }
                if (cruiserResearched) {
                    $scope.cruiserEnabled = "img-normal";
                } else {
                    $scope.cruiserEnabled = "img-grey";
                }
                $scope.fighterChoice += response.data.raceColor;
                $scope.destroyerChoice += response.data.raceColor;
                $scope.colonizerChoice += response.data.raceColor;
                $scope.cruiserChoice += response.data.raceColor;
            },

            function errorCallback (response) {
                console.log("Unable to find shipyard data");
            });
    }

    function createShip (starship) {
        $http.post("/create-ship.json", starship)
        .then(
            function successCallback (response) {
                $scope.ships.push(response.data);
            },

            function errorCallback (response) {
                console.log("Failed to create ship");
            });
    }

    getShipInfo();
    getShipyardInfo();
});

simplicityApp.controller('researchController', function($scope, $http) {
    $scope.helpMode = false;

    $scope.purchaseTech = function (techNumber) {
        console.log("Trying to purchase tech number " + techNumber);
        if (techNumber === 0 && $scope.researchInfo.researchPoolTotal < 40) {
            alert('Not enough research points');
            return;
        }
        if (techNumber === 1 && $scope.researchInfo.researchPoolTotal < 50) {
            alert('Not enough research points');
            return;
        }
        if (techNumber === 2 && $scope.researchInfo.researchPoolTotal < 90) {
            alert('Not enough research points');
            return;
        }
        if (techNumber > 2) {
            alert("That tech hasn't been programmed in yet.");
        }

        $http.post("/research-tech.json", {"techId": techNumber})
        .then(
            function successCallback (response) {
                $scope.researchInfo = response.data;
                if (!$scope.researchInfo.firstTechResearched) {
                    $scope.firstTechImageClass = "img-grey";
                } else {
                    $scope.firstTechImageClass = "img-normal";
                }
                if (!$scope.researchInfo.secondTechResearched) {
                    $scope.secondTechImageClass = "img-grey";
                } else {
                    $scope.secondTechImageClass = "img-normal";
                }
                if (!$scope.researchInfo.cruiserTechResearched) {
                    $scope.cruiserTechImageClass = "img-grey";
                } else {
                    $scope.cruiserTechImageClass = "img-normal";
                }
            },

            function errorCallback (response) {
                console.log("Unable to research tech")
            });
    }

    $scope.getHelp = function () {
        $scope.helpMode = !$scope.helpMode;
    }

    function getResearchInfo () {
        $http.post("/research-info.json")
        .then(
            function successCallback (response) {
                console.log(response.data);
                $scope.researchInfo = response.data;
                if (!$scope.researchInfo.firstTechResearched) {
                    $scope.firstTechImageClass = "img-grey";
                } else {
                    $scope.firstTechImageClass = "img-normal";
                }

                if (!$scope.researchInfo.secondTechResearched) {
                    $scope.secondTechImageClass = "img-grey";
                } else {
                    $scope.secondTechImageClass = "img-normal";
                }
                $scope.cruiserResearchAvailable = !($scope.researchInfo.firstTechResearched && $scope.researchInfo.secondTechResearched);
                if (!$scope.researchInfo.cruiserTechResearched) {
                    $scope.cruiserTechImageClass = "img-grey";
                } else {
                    $scope.cruiserTechImageClass = "img-normal";
                }
            },

            function errorCallback (response) {
                console.log("No planets");
            });
    };

    getResearchInfo();
});

simplicityApp.controller('mainController', function($scope, $http) {

    $scope.advanceTurn = function () {
        $http.post("/process-turn.json")
        .then(
            function successCallback (response) {
                $scope.currentTurn = response.data;
            },

            function errorCallback (response) {
                console.log("Could not find turn info");
            });
    };

    function getCurrentTurn () {
        $http.post("/get-turn-number.json")
        .then(
            function successCallback (response) {
                $scope.currentTurn = response.data;
            },

            function errorCallback (response) {
                console.log("Unable to find turn number");
            });
    }
    getCurrentTurn();
});

simplicityApp.controller('optionsController', function($scope, $http) {

    getGameId = function () {
        $http.get("/game-id.json")
        .then(
            function successCallback (response) {
                $scope.gameId = response.data;
            },

            function errorCallback (response) {
                console.log("Unable to find gameId. Sorry.");
            });
    }
    getGameId();
});

simplicityApp.controller('bugReportController', function($scope, $http) {
    console.log("Initializing bugReportController");
});

simplicityApp.controller('combatController', function($scope, $http, $routeParams) {

    //Scope variables
    $scope.combatLogMode = false;
    $scope.logToggle = "View Log";
    $scope.helpMode = false;
    $scope.combatLog = [];
    $scope.bothSelected = false;

    //Constants
    var DESTROYER_SPACE = 96;
    var FIGHTER_SPACE = 64;
    var CRUISER_SPACE = 110;
    var friendCrosshairXOffset = -85;
    var friendCrosshairYOffset = 0;
    var enemyCrosshairXOffset = 90;
    var enemyCrosshairYOffset = 0;

    //Regular variables
    var game = new Phaser.Game(800, 600, Phaser.AUTO, 'phaser-canvas-container',
     { preload: preload, create: create, update: update });
    var friendShips = [];
    var friendSprites = [];
    var friendShieldSprites = [];

    var enemyShips = [];
    var enemySprites = [];
    var enemyShieldSprites = [];

    var explosions;
    var enemiesRemaining;
    var combatLogIndex = 0;

    var lasers;
    var friendCrosshair;
    var enemyCrosshair;

    var friendChosen = false;
    var enemyChosen = false;
    var friendSelectedIndex;
    var enemySelectedIndex;
    var laser;
    var xDestination;

    //Unused at the moment
    var firstShieldAlpha = 1.0;
    var goingDown = true;
    var animateFirstShield = false;


    $scope.getHelp = function () {
        $scope.helpMode = !$scope.helpMode;
        $scope.combatLogMode = false;
    }

    $scope.viewLog = function () {
        $scope.combatLogMode = !$scope.combatLogMode;
        if ($scope.combatLogMode) {
            $scope.logToggle = "Hide Log";
        } else {
            $scope.logToggle = "View Log";
        }
        $scope.helpMode = false;
    }

    function getCombatInfo (systemId) {
        var wrapper = {"systemId": systemId};
        $http.post("/empty-combat-info.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("Found combat info");
                friendShips = response.data.friendShips;
                enemyShips = response.data.enemyShips;
                console.log(friendShips);
                console.log(enemyShips);
                enemiesRemaining = enemyShips.length;
            },

            function errorCallback (response) {
                console.log("Did not find combat info");
            });
    };
    if ($routeParams.param != null) {
        getCombatInfo($routeParams.param);
    }

    function preload() {
        game.load.image('stars', 'assets/starfield.png');
        game.load.spritesheet('destruction_explosion', 'assets/anims/destruction_explosion.png');

        game.load.image('green-destroyer', 'assets/ships/destroyer/destroyer_green.png');
        game.load.image('ltblue-destroyer', 'assets/ships/destroyer/destroyer_ltblue.png');
        game.load.image('red-destroyer', 'assets/ships/destroyer/destroyer_red.png');
        game.load.image('gold-destroyer', 'assets/ships/destroyer/destroyer_gold.png');
        game.load.image('purple-destroyer', 'assets/ships/destroyer/destroyer_purple.png');
        game.load.image('destroyer-shield', 'assets/ships/destroyer/destroyer_shield.png');

        game.load.image('green-fighter', 'assets/ships/fighter/fighter_green.png');
        game.load.image('ltblue-fighter', 'assets/ships/fighter/fighter_ltblue.png');
        game.load.image('red-fighter', 'assets/ships/fighter/fighter_red.png');
        game.load.image('gold-fighter', 'assets/ships/fighter/fighter_gold.png');
        game.load.image('purple-fighter', 'assets/ships/fighter/fighter_purple.png');
        game.load.image('fighter-shield', 'assets/ships/fighter/fighter_shield.png');
        
        game.load.image('green-cruiser', 'assets/ships/cruiser/cruiser_green.png');
        game.load.image('ltblue-cruiser', 'assets/ships/cruiser/cruiser_ltblue.png');
        game.load.image('red-cruiser', 'assets/ships/cruiser/cruiser_red.png');
        game.load.image('gold-cruiser', 'assets/ships/cruiser/cruiser_gold.png');
        game.load.image('purple-cruiser', 'assets/ships/cruiser/cruiser_purple.png');
        game.load.image('cruiser-shield', 'assets/ships/cruiser/cruiser_shield.png');

        game.load.image('laser', 'assets/red_beam_small.png');

        game.load.image('friend-crosshair', 'assets/crosshair_green2.png');
        game.load.image('enemy-crosshair', 'assets/crosshair_red2.png');

        game.load.spritesheet('explosion', 'assets/anims/explode.png');
    }

    function getImageStringFromShip (ship) {
        var chassis = ship.chassis;
        chassis = chassis.toLowerCase();
        var raceId = ship.ownerRaceNum;
        switch (raceId) {
            case 0:
                return "ltblue-" + chassis;
            case 1:
                return "red-" + chassis;
            case 2:
                return "gold-" + chassis;
            case 3:
                return "green-" + chassis;
            default:
                return "purple-" + chassis;
        }
    }

    function getShieldStringFromShip (ship) {
        var chassisName = ship.chassis.toLowerCase();
        return chassisName + '-shield';
    }

    function create() {
        game.add.sprite(0, 0, 'stars');
        var yLocation = 50;
        var secondColumn = 0;
        lasers = game.add.group();
        lasers.enableBody = true;
        lasers.physicsBodyType = Phaser.Physics.ARCADE;
        lasers.createMultiple(2, 'laser');

        friendCrosshair = game.add.sprite(0, 0, 'friend-crosshair');
        friendCrosshair.anchor.setTo(0.5, 0.5);
        friendCrosshair.alpha = 0.0;

        enemyCrosshair = game.add.sprite(0, 0, 'enemy-crosshair');
        enemyCrosshair.anchor.setTo(0.5, 0.5);
        enemyCrosshair.alpha = 0.0;

        for (i = 0; i < friendShips.length; i++) {
            if (chassis === "FIGHTER") {
                yLocation += FIGHTER_SPACE/2;
            } else if (chassis === "DESTROYER") {
                yLocation += DESTROYER_SPACE/2;
            } else if (chassis === "CRUISER") {
                yLocation += CRUISER_SPACE/2;
            } else {
                console.log("Painting error occurred. Consult manual page 56. (Just kidding, there is no manual)")
            }
            friendSprites[i] = game.add.sprite(100 + secondColumn, yLocation, getImageStringFromShip(friendShips[i]));
            friendShieldSprites[i] = game.add.sprite(100 + secondColumn, yLocation, getShieldStringFromShip(friendShips[i]));
            var chassis = friendShips[i].chassis;
            if (chassis === "FIGHTER") {
                yLocation += FIGHTER_SPACE/2;
            } else if (chassis === "DESTROYER") {
                yLocation += DESTROYER_SPACE/2;
            } else if (chassis === "CRUISER") {
                yLocation += CRUISER_SPACE/2;
            } else {
                console.log("Painting error occurred. Consult manual page 56. (Just kidding, there is no manual)")
            }
            if (yLocation > 500) {
                yLocation -= 500;
                secondColumn = 160;
            }

            friendShieldSprites[i].anchor.setTo(0.5, 0.5);
            friendSprites[i].anchor.setTo(0.5, 0.5);
            friendSprites[i].inputEnabled = true;
            friendSprites[i].events.onInputDown.add(friendListener, this);
            friendSprites[i].index = i;
        }
        yLocation = 20;
        secondColumn = 0;
        for (i = 0; i < enemyShips.length; i++) {
            if (chassis === "FIGHTER") {
                yLocation += FIGHTER_SPACE/2;
            } else if (chassis === "DESTROYER") {
                yLocation += DESTROYER_SPACE/2;
            } else if (chassis === "CRUISER") {
                yLocation += CRUISER_SPACE/2;
            } else {
                console.log("Painting error occurred. Consult manual page 56. (Just kidding, there is no manual)")
            }
            enemySprites[i] = game.add.sprite(600 - secondColumn, yLocation, getImageStringFromShip(enemyShips[i]));
            enemyShieldSprites[i] = game.add.sprite(600 - secondColumn, yLocation, getShieldStringFromShip(enemyShips[i]));
            var chassis = enemyShips[i].chassis;
            if (chassis === "FIGHTER") {
                yLocation += FIGHTER_SPACE/2;
            } else if (chassis === "DESTROYER") {
                yLocation += DESTROYER_SPACE/2;
            } else if (chassis === "CRUISER") {
                yLocation += CRUISER_SPACE/2;
            } else {
                console.log("Painting error occurred. Consult manual page 56. (Just kidding, there is no manual)")
            }
            if (yLocation > 500) {
                yLocation -= 500;
                secondColumn = 160;
            }

            enemyShieldSprites[i].anchor.setTo(0.5, 0.5);
            enemySprites[i].anchor.setTo(0.5, 0.5);
            enemySprites[i].inputEnabled = true;
            enemySprites[i].events.onInputDown.add(enemyListener, this);
            enemySprites[i].index = i;
            enemySprites[i].scale.x *= -1;
            enemyShieldSprites[i].scale.x *= -1;
        }

        explosions = game.add.group();
        explosions.createMultiple(4, 'destruction_explosion');
        setupExplosions();
        //explosions.forEach(setupInvader, this);
    }

    function setupInvader (invader) {
        invader.animations.add('destruction_explosion');
    }

    function setupExplosions () {
        for (i = 0; i < enemySprites.length; i++) {
            enemySprites[i].animations.add('destruction_explosion');
        }
    }

    function friendListener (sprite) {
        friendChosen = true;
        friendCrosshair.y = sprite.y + friendCrosshairYOffset;
        friendCrosshair.x = sprite.x + friendCrosshairXOffset;
        friendCrosshair.alpha = 1.0;

        $scope.bothSelected = friendChosen && enemyChosen;
        $scope.friendSelected = friendShips[sprite.index];
        friendSelectedIndex = sprite.index;
        var ship = friendShips[sprite.index];
        $scope.friendSelected.healthPct = "" + (ship.health/ship.maxHealth)*100 + "%";
        $scope.friendSelected.shieldPct = "" + (ship.shieldHealth/ship.maxShieldHealth)*100 + "%";
        $scope.friendSelected.energyPct = "" + (ship.currentReservePower/ship.maxReservePower)*100 + "%";
        if (!$scope.friendSelected.alreadyFired) {
            $scope.greyIfFired = "red-text";
        } else {
            $scope.greyIfFired = "grey-text";
        }
        $scope.$apply();
    }

    function enemyListener (sprite) {
        enemyChosen = true;

        enemyCrosshair.y = sprite.y + enemyCrosshairYOffset;
        if (sprite.x > 550) {
            enemyCrosshair.x = sprite.x + enemyCrosshairXOffset;
        } else {
            enemyCrosshair.x = sprite.x + 45;
        }

        enemyCrosshair.alpha = 1.0;

        $scope.bothSelected = friendChosen && enemyChosen;
        $scope.enemySelected = enemyShips[sprite.index];
        enemySelectedIndex = sprite.index;
        var ship = enemyShips[sprite.index];
        $scope.enemySelected.healthPct = "" + (ship.health/ship.maxHealth)*100 + "%";
        $scope.enemySelected.shieldPct = "" + (ship.shieldHealth/ship.maxShieldHealth)*100 + "%";
        $scope.enemySelected.energyPct = "" + (ship.currentReservePower/ship.maxReservePower)*100 + "%";
        $scope.$apply();
    }

    function update() {
        if (laser != null) {
            game.physics.arcade.overlap(laser, enemySprites[enemySelectedIndex], laserHitsEnemy, null, this);
        }
        //Called every frame
        if (animateFirstShield) {
            if (goingDown) {
                firstShieldAlpha -= 0.003;
                if (firstShieldAlpha < 0.7) {
                    goingDown = false;
                }
            } else {
                firstShieldAlpha += 0.003;
                if (firstShieldAlpha > 0.97) {
                    goingDown = true;
                }
            }
            friendShieldSprites[0].alpha = firstShieldAlpha;
        }

        if (laser != null) {
            if (laser.x > xDestination) {
                laser.kill();
            }
        }
    }

    $scope.passTurn = function () {
        console.log("Passing turn");

        $scope.combatLog[combatLogIndex] = "Player ended turn. Doing enemy attacks.";
        combatLogIndex++;

        doEnemyAttacks(0);

        $scope.friendSelected = null;
        $scope.enemySelected = null;
        for (i = 0; i < friendShips.length; i++) {
            if (friendShips[i] == null) {
                continue;
            }
            friendShips[i].alreadyFired = false;
            friendShips[i].currentReservePower += friendShips[i].powerPerTurn;
            if (friendShips[i].currentReservePower > friendShips[i].maxReservePower) {
                friendShips[i].currentReservePower = friendShips[i].maxReservePower;
            }
            friendShips[i].shieldHealth += friendShips[i].regenRate;
            //Todo make this cost power
            if (friendShips[i].shieldHealth > friendShips[i].maxShieldHealth) {
                friendShips[i].shieldHealth = friendShips[i].maxShieldHealth;
            }
        }
        
        for (i = 0; i < enemyShips.length; i++) {
            if (enemyShips[i] == null) {
                continue;
            }
            enemyShips[i].alreadyFired = false;
            enemyShips[i].currentReservePower += enemyShips[i].powerPerTurn;
            if (enemyShips[i].currentReservePower > enemyShips[i].maxReservePower) {
                enemyShips[i].currentReservePower = enemyShips[i].maxReservePower;
            }
            enemyShips[i].shieldHealth += enemyShips[i].regenRate;
            //Todo make this cost power
            if (enemyShips[i].shieldHealth > enemyShips[i].maxShieldHealth) {
                enemyShips[i].shieldHealth = enemyShips[i].maxShieldHealth;
            }
        }


    }

    function doEnemyAttacks (attackerIndex) {
        console.log("In doEnemyAttacks(attackerIndex) with attackerIndex = " + attackerIndex);
        while (enemyShips[attackerIndex] == null) {
            console.log("Ship is dead. Incrementing index");
            attackerIndex++;
            if (attackerIndex >= enemyShips.length) {
                return;
            }
        }
        console.log("Doing attack for " + enemyShips[attackerIndex].name);
        if (enemyShips[attackerIndex].currentReservePower == 0) {
            console.log("ship has no energy. Incrementing index");
            attackerIndex++;
            if (attackerIndex >= enemyShips.length) {
                return;
            }
        }
        var damage = enemyShips[attackerIndex].damage;
        if (enemyShips[attackerIndex].currentReservePower < damage) {
            damage = enemyShips[attackerIndex].currentReservePower;
            enemyShips[attackerIndex].currentReservePower = 0;
        } else {
            enemyShips[attackerIndex].currentReservePower -= damage;
        }
        var target = null;
        var index;
        var friendShipRealDamage;
        for (j = 0; j < friendShips.length && target == null; j++) { //Find the first non-null target
            target = friendShips[j];
            index = j;
            if (friendShips[j] != null) {
                friendShipRealDamage = friendShips[j].damage;
            }
        }
        if (target == null) {
            alert("Combat is over. The evil CPU killed all ur ships brah.");
            return;
        }
        $scope.combatLog[combatLogIndex] = enemyShips[attackerIndex].name + " fired at " + target.name + " for " + damage + " damage.";
        combatLogIndex++;
        target.damage = damage;
        target.shieldsUp = true;
        $http.post("/process-attack.json", target)
        .then(
            function successCallback (response) {
                console.log(response.data);
                friendShips[index] = response.data;
                friendShips[index].damage = friendShipRealDamage;

                friendShieldSprites[index].alpha =
                    (friendShips[index].shieldHealth/friendShips[index].maxShieldHealth)/2 + 0.5;
                if (friendShips[index].shieldHealth == 0) {
                    friendShieldSprites[index].alpha = 0.0;
                }
                if (friendShips[index].health <= 0) {
                    console.log("Another one bites the dust");
                    $scope.combatLog[combatLogIndex] = friendShips[index].name + " was destroyed.";
                    combatLogIndex++;
                    friendShips[index] = null;
                    friendSprites[index].kill();
                    friendShieldSprites.alpha = 0.0;
                }

                if (attackerIndex < enemyShips.length - 1) {
                    attackerIndex++;
                    doEnemyAttacks(attackerIndex);
                } else {
                    $scope.combatLog[combatLogIndex] = "Done with enemy attacks.";
                    combatLogIndex++;
                }
            },

            function errorCallback (response) {
                console.log("Error processing enemy attack");
                console.log(response.data);
            });

    }

    $scope.fireWeapons = function () {
        if (laser == null) {
            laser = lasers.getFirstExists(false);
        }
        laser.anchor.setTo(0.5, 0.5);
        laser.reset(friendSprites[friendSelectedIndex].x, friendSprites[friendSelectedIndex].y);
        laser.rotation = game.physics.arcade.moveToObject(laser, enemySprites[enemySelectedIndex], 1200);
        xDestination = enemySprites[enemySelectedIndex].x - 10;
        console.log("xDestination = " + xDestination);
        console.log("enemySprites[enemySelectedIndex] = ");
        console.log(enemySprites[enemySelectedIndex]);

        function laserHitsEnemy () {
            console.log("EXPLOSIONS*****!!!!!");
        }

        if ($scope.friendSelected.currentReservePower == 0) {
            console.log("No energy. exiting function");
            return;
        }
        console.log("Firing weapon");
        var damage = $scope.friendSelected.damage;
        if ($scope.friendSelected.currentReservePower < damage) {
            damage = $scope.friendSelected.currentReservePower;
            $scope.friendSelected.currentReservePower = 0;
        } else {
            $scope.friendSelected.currentReservePower -= damage;
        }
        $scope.combatLog[combatLogIndex] = $scope.friendSelected.name + " fired at " + $scope.enemySelected.name + " for " + damage + " damage.";
        combatLogIndex++;
        $scope.friendSelected.energyPct = "" + ($scope.friendSelected.currentReservePower/$scope.friendSelected.maxReservePower)*100 + "%";
        var enemyShip = $scope.enemySelected;
        var enemyShipRealDamage = enemyShip.damage;
        //We're pulling a switcheroo so that we don't have to create a wrapper to hold the damage for the friend ship
        enemyShip.damage = damage;
        enemyShip.shieldsUp = true;
        $http.post("/process-attack.json", enemyShip)
        .then(
            function successCallback (response) {
                console.log(response.data);
                enemyShips[enemySelectedIndex] = response.data;
                enemyShips[enemySelectedIndex].damage = enemyShipRealDamage;

                $scope.friendSelected.alreadyFired = true;
                $scope.greyIfFired = "grey-text";

                $scope.enemySelected = enemyShips[enemySelectedIndex];
                enemyShieldSprites[enemySelectedIndex].alpha =
                 (enemyShips[enemySelectedIndex].shieldHealth/enemyShips[enemySelectedIndex].maxShieldHealth)/2 + 0.5;
                if (enemyShips[enemySelectedIndex].shieldHealth == 0) {
                    enemyShieldSprites[enemySelectedIndex].alpha = 0;
                }
                $scope.enemySelected.healthPct = ""
                 + (enemyShips[enemySelectedIndex].health/enemyShips[enemySelectedIndex].maxHealth)*100 + "%";
                $scope.enemySelected.shieldPct = ""
                 + (enemyShips[enemySelectedIndex].shieldHealth/enemyShips[enemySelectedIndex].maxShieldHealth)*100 + "%";
                $scope.enemySelected.energyPct = ""
                 + (enemyShips[enemySelectedIndex].currentReservePower/enemyShips[enemySelectedIndex].maxReservePower)*100 + "%";
                if ($scope.enemySelected.health <= 0) {
                    console.log("He's dead, Jim");
                    $scope.combatLog[combatLogIndex] = $scope.enemySelected.name + " was destroyed.";
                    combatLogIndex++;
                    enemyShips[enemySelectedIndex] = null;
                    enemySprites[enemySelectedIndex].kill();
                    enemyShieldSprites[enemySelectedIndex].alpha = 0.0;
                    $scope.enemySelected = null;
                    $scope.bothSelected = false;
                    var explosion = explosions.getFirstExists(false);
                    explosion.reset(20, 20);
                    explosion.play('destruction_explosion', 30, false, true);
                    enemiesRemaining--;
                    if (enemiesRemaining == 0) {
                        alert("Combat is over! You win");
                        $scope.combatOver = true;
                    }
                }
            },

            function errorCallback (response) {
                console.log("Unable to find combat action response");
            });
    };
});

simplicityApp.controller('systemController', function($scope, $http, $routeParams) {

    var explosions;
    var wormholes;
    var wormholeAnims = [];
    var addAnimations = true;
    var systemText;
    var tunnels = [];
    var graphics;
    var colonizeGraphics;
    var planetCoordsX = [430, 350, 525];
    var planetCoordsY = [205, 105, 360];
    var tunnelCoordsX = [20, 620, 350];
    var tunnelCoordsY = [400, 400, 490];
    var ships = [];
    var game = new Phaser.Game(800, 600, Phaser.AUTO, 'phaser-canvas-container',
                                      { preload: preload, create: create, update: update });

    getSpecificStarSystemInfo = function (systemId) {
        $http.post("/specific-system-info.json", {"systemId": systemId})
        .then(
            function successCallback (response) {
                $scope.starSystemInfo = response.data;
            },

            function errorCallback (response) {
                console.log("Unable to find system info");
            });
    }
    if ($routeParams.param != null) {
        getSpecificStarSystemInfo($routeParams.param);
    }

    function colonizePlanet (planet, ship) {
        console.log(wrapper);
        $http.post("/colonize-planet.json", {"planetId": planet.id, "shipId": ship.id})
        .then(
            function successCallback (response) {
                ship.kill();
            },

            function errorCallback (response) {
                console.log("Unable to colonize planet?");
            });
    }
    
    function getImageStringFromShip (ship) {
        console.log("ship = ");
        console.log(ship);
        var chassis = ship.chassis;
        chassis = chassis.toLowerCase();
        var raceId = ship.ownerRaceNum;
        switch (raceId) {
            case 0:
                return "ltblue-" + chassis;
            case 1:
                return "red-" + chassis;
            case 2:
                return "gold-" + chassis;
            case 3:
                return "green-" + chassis;
        }
    }

    function preload() {
        game.load.image('stars', 'assets/starfield.png');
        game.load.image('sun', 'assets/smallsun.png');
        game.load.image('tunnel', 'assets/single_wormhole.png');
        game.load.spritesheet('kaboom', 'assets/anims/explode.png', 128, 128);
        game.load.spritesheet('wormhole', 'assets/anims/Effect117.png', 128, 128);

        game.load.image('planet1', 'assets/planets/planet1.png');
        game.load.image('planet2', 'assets/planets/planet2.png');
        game.load.image('planet3', 'assets/planets/planet3.png');
        game.load.image('planet4', 'assets/planets/planet4.png');
        game.load.image('planet5', 'assets/planets/planet5.png');

        game.load.image('planet6', 'assets/planets/planet6.png');
        game.load.image('planet7', 'assets/planets/planet7.png');
        game.load.image('planet8', 'assets/planets/planet8.png');
        game.load.image('planet9', 'assets/planets/planet9.png');
        game.load.image('planet10', 'assets/planets/planet10.png');
        
        game.load.image('green-destroyer', 'assets/ships/destroyer/destroyer_green.png');
        game.load.image('ltblue-destroyer', 'assets/ships/destroyer/destroyer_ltblue.png');
        game.load.image('red-destroyer', 'assets/ships/destroyer/destroyer_red.png');
        game.load.image('gold-destroyer', 'assets/ships/destroyer/destroyer_gold.png');
        game.load.image('purple-destroyer', 'assets/ships/destroyer/destroyer_purple.png');
        
        game.load.image('green-fighter', 'assets/ships/fighter/fighter_green.png');
        game.load.image('ltblue-fighter', 'assets/ships/fighter/fighter_ltblue.png');
        game.load.image('red-fighter', 'assets/ships/fighter/fighter_red.png');
        game.load.image('gold-fighter', 'assets/ships/fighter/fighter_gold.png');
        game.load.image('purple-fighter', 'assets/ships/fighter/fighter_purple.png');
        
        game.load.image('green-colonizer', 'assets/ships/colonizer/colonizer_green.png');
        game.load.image('ltblue-colonizer', 'assets/ships/colonizer/colonizer_ltblue.png');
        game.load.image('red-colonizer', 'assets/ships/colonizer/colonizer_red.png');
        game.load.image('gold-colonizer', 'assets/ships/colonizer/colonizer_gold.png');
        game.load.image('purple-colonizer', 'assets/ships/colonizer/colonizer_purple.png');

        game.load.image('green-cruiser', 'assets/ships/cruiser/cruiser_green.png');
        game.load.image('ltblue-cruiser', 'assets/ships/cruiser/cruiser_ltblue.png');
        game.load.image('red-cruiser', 'assets/ships/cruiser/cruiser_red.png');
        game.load.image('gold-cruiser', 'assets/ships/cruiser/cruiser_gold.png');
        game.load.image('purple-cruiser', 'assets/ships/cruiser/cruiser_purple.png');
    }

    function create() {
        game.physics.startSystem(Phaser.Physics.ARCADE);
        var ringCenterX = 400;
        var ringCenterY = 300;
        //  A simple background for our game
        game.add.sprite(0, 0, 'stars');
        game.add.sprite(350, 250, 'sun');

        graphics = game.add.graphics(0, 0);
        colonizeGraphics = game.add.graphics(0, 0);
        var planets = [];
        graphics.lineStyle(1, 0x6C6C6C, 1);
        for(i = 0; i < $scope.starSystemInfo.starSystem.planets.length; i++) {
            graphics.drawCircle(ringCenterX, ringCenterY, 100 + i*50);
            planets[i] = game.add.sprite(planetCoordsX[i], planetCoordsY[i], $scope.starSystemInfo.starSystem.planets[i].imageString);
            planets[i].inputEnabled = true;
            planets[i].events.onInputDown.add(listener, this);
            planets[i].elementName = $scope.starSystemInfo.starSystem.planets[i].name;
            planets[i].size = $scope.starSystemInfo.starSystem.planets[i].size;
            planets[i].population = $scope.starSystemInfo.starSystem.planets[i].population;
            planets[i].turnsToGrowth = $scope.starSystemInfo.starSystem.planets[i].turnsToGrowth;
            planets[i].productionPct = $scope.starSystemInfo.starSystem.planets[i].productionPct;
            planets[i].id = $scope.starSystemInfo.starSystem.planets[i].id;
            var ownerRaceNum = $scope.starSystemInfo.starSystem.planets[i].ownerRaceNum;
            planets[i].icon = getIconStringFromRaceNum(ownerRaceNum);

        }

        function getIconStringFromRaceNum (ownerRaceNum) {
            switch (ownerRaceNum) {
                case -1:
                    return "assets/races/norace_icon.png";
                case 0:
                    return "assets/races/race1_icon.jpg";
                case 1:
                    return "assets/races/race2_icon.jpg";
                case 2:
                    return "assets/races/race3_icon.jpg";
                case 3:
                    return "assets/races/race4_icon.jpg";
            }
        }

        if (addAnimations) {
            var tunnel = game.add.sprite(20, 400, 'tunnel');
            tunnel.inputEnabled = true;
            tunnel.events.onInputDown.add(listener, this);
            tunnel.elementName = "Tunnel";
            tunnel.alpha = 0.0;

            wormholes = game.add.group();
            wormholes.createMultiple(3, 'wormhole');
            //tunnel.animations.add('wormhole');
            wormholes.forEach(setupDiamond, this);

            for (i = 0; i < $scope.starSystemInfo.tunnels.length; i++) {
                wormholeAnims[i] = wormholes.getFirstExists(false);
                wormholeAnims[i].reset(tunnelCoordsX[i], tunnelCoordsY[i]);
                wormholeAnims[i].play('wormhole', 20, true, false);
                wormholeAnims[i].alpha = 0.0;
            }
        }

        systemText = game.add.text(200, 25, $scope.starSystemInfo.starSystem.name + " System" , { font: '28px Arial', fill: '#fff' });
        systemText.visible = true;

        for (i = 0; i < $scope.starSystemInfo.tunnels.length; i++) {
            tunnels[i] = game.add.sprite(tunnelCoordsX[i], tunnelCoordsY[i], 'tunnel');
            tunnels[i].inputEnabled = true;
            tunnels[i].events.onInputDown.add(listener, this);
            tunnels[i].elementName = "Tunnel: " + $scope.starSystemInfo.tunnels[i].name;
            tunnels[i].travelTime = $scope.starSystemInfo.tunnels[i].length + " turns journey";
            tunnels[i].index = i;
        }

        explosions = game.add.group();
        explosions.createMultiple(10, 'kaboom');
        for (i = 0; i < $scope.starSystemInfo.ships.length; i++) {
            console.log("Creating ship sprite " + i);
            console.log(getImageStringFromShip($scope.starSystemInfo.ships[i]));
            ships[i] = game.add.sprite(20, 20 + i*100, getImageStringFromShip($scope.starSystemInfo.ships[i]));
            ships[i].inputEnabled = true;
            ships[i].input.enableDrag();
            ships[i].events.onDragStop.add(onDragStop, this);
            ships[i].events.onInputDown.add(listener, this);
            ships[i].elementName = $scope.starSystemInfo.ships[i].name;
            ships[i].shipHealth = $scope.starSystemInfo.ships[i].health;
            ships[i].maxHealth = $scope.starSystemInfo.ships[i].maxHealth;
            ships[i].shipId = $scope.starSystemInfo.ships[i].id;
            ships[i].type = $scope.starSystemInfo.ships[i].chassis;
            ships[i].id = $scope.starSystemInfo.ships[i].id;
            ships[i].shipIndex = i;
            ships[i].icon = "";
            switch (ownerRaceNum) {
                case -1:
                    ships[i].icon = "assets/races/norace_icon.png";
                    break;

                case 0:
                    ships[i].icon = "assets/races/race1_icon.jpg";
                    break;

                case 1:
                    ships[i].icon = "assets/races/race2_icon.jpg";
                    break;

                case 2:
                    ships[i].icon = "assets/races/race3_icon.jpg";
                    break;

                case 3:
                    ships[i].icon = "assets/races/race4_icon.jpg";
                    break;
            }
        }
    }

    function setupDiamond (tunnel) {
        if (addAnimations) {
            tunnel.animations.add('wormhole');
        }
    }

    function update() {
        //Called every frame
    }

    $scope.planetSelected = false;
    $scope.shipSelected = false;
    $scope.tunnelSelected = false;
    
    function listener (sprite) {
        $scope.shipSelected = false;
        $scope.planetSelected = false;
        $scope.tunnelSelected = false;
        $scope.selectedElement = {};
        $scope.selectedElement.name = sprite.elementName;
        $scope.selectedElement.id = sprite.id;

        if (sprite.shipHealth != null) {
            $scope.shipSelected = true;
            $scope.selectedElement.icon = sprite.icon;
            $scope.selectedElement.health = "Health: " + sprite.shipHealth + "/" + sprite.maxHealth;
            $scope.selectedElement.shipIndex = sprite.shipIndex;
        }
        if (sprite.size != null) {
            $scope.planetSelected = true;
            $scope.selectedElement.icon = sprite.icon;
            $scope.selectedElement.population = sprite.population;
            $scope.selectedElement.productionPct = sprite.productionPct;
            $scope.selectedElement.id = sprite.id;
            console.log("icon = " + sprite.icon);
            console.log("$scope.selectedElement.productionPct = " + $scope.selectedElement.productionPct);
            $scope.outputSlider = 100 * $scope.selectedElement.productionPct;
            console.log("output slider = " + $scope.outputSlider);
            if (sprite.population > 0) {
                $scope.selectedElement.population = "Population: " + sprite.population;
                if (sprite.turnsToGrowth == -1) {
                    $scope.selectedElement.turnsToGrowth = "Population not growing";
                } else {
                    $scope.selectedElement.turnsToGrowth = "Turns to growth: " + sprite.turnsToGrowth;
                }
            } else {
                $scope.selectedElement.population = null;
            }
            $scope.selectedElement.size = "Size: " + sprite.size;
        }
        if (sprite.travelTime != null) {
            $scope.tunnelSelected = true;
            $scope.selectedElement.icon = "assets/wormhole_icon.png";
            $scope.selectedElement.travelTime = sprite.travelTime;
        }

        $scope.$apply();
    }

    function onDragStop (sprite, pointer) {
        console.log("Location: " + pointer.x + ", " + pointer.y);

        var x = pointer.x;
        var y = pointer.y;
        if ($scope.tunnelsOpen) {
            var nearTunnelIndex = -1;
            if (x > 0 && x < 130) {
                if (y > 365 && y < 495) {
                    nearTunnelIndex = 0;
                }
            }
            if (x > 580 && x < 720) {
                if (y > 350 && y < 480) {
                    nearTunnelIndex = 1;
                }
            }
            if (x > 300 && x < 450) {
                if (y > 450 && y < 580) {
                    nearTunnelIndex = 2;
                }
            }
            if ($scope.starSystemInfo.tunnels[nearTunnelIndex] != null) { //If there is a tunnel there
                console.log("Entering tunnel " + nearTunnelIndex);
                console.log($scope.starSystemInfo.tunnels[nearTunnelIndex]);
                //Figure out if we are "firstSystem" or "secondSystem" in the tunnel
                var firstSystemName = $scope.starSystemInfo.tunnels[nearTunnelIndex].firstSystem.name;
                var secondSystemName = $scope.starSystemInfo.tunnels[nearTunnelIndex].secondSystem.name;
                var tunnelId = $scope.starSystemInfo.tunnels[nearTunnelIndex].id;
                if ($scope.starSystemInfo.starSystem.name === firstSystemName) {
                    console.log("We are the 1st system. Sending ship to the 2nd system which is " + secondSystemName);
                    enterTunnel($scope.starSystemInfo.tunnels[nearTunnelIndex].secondSystem.id, tunnelId, sprite.shipId);
                    sprite.kill();
                } else if ($scope.starSystemInfo.starSystem.name === secondSystemName) {
                    console.log("We are the 2nd system. Sending ship to the 1st system which is " + firstSystemName);
                    enterTunnel($scope.starSystemInfo.tunnels[nearTunnelIndex].firstSystem.id, tunnelId, sprite.shipId);
                    sprite.kill();
                } else {
                    console.log("I am so confused. Please help!");
                }
            }
        }

        if (colonizeModeActive) {
            var nearPlanetIndex = -1;
            if (x > 350 && x < 500) {
                if (y > 180 && y < 260) {
                    nearPlanetIndex = 0;
                }
            }
            if (x > 330 && x < 410) {
                if (y > 80 && y < 150) {
                    nearPlanetIndex = 1;
                }
            }
            if (x > 480 && x < 585) {
                if (y > 325 && y < 410) {
                    nearPlanetIndex = 2;
                }
            }
            var planet = $scope.starSystemInfo.starSystem.planets[nearPlanetIndex];
            if (planet != null) {
                if (sprite.type === "COLONIZER") {
                    console.log("We are a colonizer. So we can colonize that planet");
                    if (planet.ownerRaceNum > -1) {
                        console.log("But it's taken");
                    } else {
                        console.log("And it's free!");
                        colonizePlanet(planet, sprite);
                    }

                } else {
                    console.log("Selected ship is not a colonizer");
                    if (planet.ownerRaceNum > -1) {
                        console.log("But the planet was already taken anyway");
                    } else {
                        console.log("Too bad because that planet was free");
                    }
                }
            }
        }

    }
    $scope.helpMode = false;
    $scope.getHelp = function () {
        $scope.helpMode = !$scope.helpMode;
    }

    enterTunnel = function (destinationId, tunnelId, shipId) {
        //console.log("destinationId = " + destinationId + " , tunnelId = " + tunnelId + ", shipId = " + shipId);
        var wrapper = {"systemId": destinationId, "tunnelId": tunnelId, "shipId": shipId};
        $http.post("/enter-tunnel.json", wrapper)
        .then(
            function successCallback (response) {
                console.log(response.data);
            },

            function errorCallback (response) {
                console.log("Error callback for enter tunnel");
            });
    }
    $scope.tunnelsOpen = false;

    $scope.tunnelToggle = "Open Tunnels";

    $scope.scrapShip = function (sprite) {
        $scope.selectedElement = null;
        $scope.shipSelected = false;
        console.log("Scrapping ship");
        console.log("id = " + sprite.id);
        var wrapper = {"shipId": sprite.id};
        ships[sprite.shipIndex].kill();
        $http.post("/scrap-ship.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("heard back");
                console.log(response.data);

            },

            function errorCallback (response) {
                console.log("Error callback for scrapping ship");
            });
    }

    $scope.openTunnels = function () {
        $scope.tunnelsOpen = !$scope.tunnelsOpen;
        if ($scope.tunnelsOpen) {
            for (i = 0; i < $scope.starSystemInfo.tunnels.length; i++) {
                wormholeAnims[i].alpha = 1.0;
                tunnels[i].alpha = 0.0;
            }
            $scope.tunnelToggle = "Close Tunnels";
        } else {
            for (i = 0; i < $scope.starSystemInfo.tunnels.length; i++) {
                wormholeAnims[i].alpha = 0.0;
                tunnels[i].alpha = 1.0;
            }
            $scope.tunnelToggle = "Open Tunnels";
        }
    }

    /*$scope.goToDagobah = function () {
            console.log("Go to the Dagobah system Luke");
            console.log("Selected system = " + $scope.selectedSystem.name);
            window.location.href = "/main.html#/system/" + selectedSystemId;
        };*/

    $scope.enterCombat = function () {
        console.log("EVERYBODY WAS KUNG FU FITING");
        console.log("Starting a fight in star system with id = " + $scope.starSystemInfo.starSystem.id);
        window.location.href = "/main.html#/combat/" + $scope.starSystemInfo.starSystem.id;
    }

    $scope.colonizeToggle = "Colonize Mode: OFF";
    var colonizeModeActive = false;
    var greenLine;
    $scope.colonizeMode = function () {
        var planets = $scope.starSystemInfo.starSystem.planets;
        colonizeModeActive = !colonizeModeActive;
        if (colonizeModeActive) {
            if (greenLine == null) {
                colonizeGraphics.lineStyle(5, 0x009933, 1);
                for (i = 0; i < planets.length; i++) {
                    if (planets[i].ownerRaceNum == -1) {
                        colonizeGraphics.drawCircle(planetCoordsX[i] + 44, planetCoordsY[i] + 44, 30);
                    }
                }
            }
            colonizeGraphics.alpha = 1.0;
            $scope.colonizeToggle = "Colonize Mode: ON";
        } else {
            colonizeGraphics.alpha = 0.0;
            $scope.colonizeToggle = "Colonize Mode: OFF";
        }
    }
    /*graphics.lineStyle(1, 0x6C6C6C, 1);
            for(i = 0; i < $scope.starSystemInfo.starSystem.planets.length; i++) {
                graphics.drawCircle(ringCenterX, ringCenterY, 100 + i*50);*/

    $scope.changeOutput = function (planet) {
        console.log("The outputs they are a changin'");
        console.log(planet);
        console.log("$scope.outputSlider = " + $scope.outputSlider);
        var wrapper = {"planetId": planet.id, "productionNumber": $scope.outputSlider};
        $http.post("/change-output.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("Successfully changed planet's production. Probably.")
                console.log(response.data);
            },

            function errorCallback (response) {
                console.log("Error callback for changeOutput");
            });
    }
});
