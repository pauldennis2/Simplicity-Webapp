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
            templateUrl: "help.html",
            controller: "helpController"
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
    console.log("Initializing ssgViewController");
    var game = new Phaser.Game(800, 600, Phaser.AUTO, 'phaser-canvas-container',
         { preload: preload, create: create, update: update });

    $scope.starSystemGraph = {};
    var starSystems = [];
    var tunnels = [];
    getSSGInfo = function () {
        console.log("Getting ssg info");
        var wrapper = {"gameId": 1, "playerId":2};
        $http.post("/ssg-info.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("Found info");
                starSystems = response.data.starSystems;
                console.log("starSystems = " + starSystems);
                tunnels = response.data.tunnels;
                console.log("tunnels = " + tunnels);
                preload();
                create();
            },

            function errorCallback (response) {
                console.log("Failed to find SSG info");
            });
    };
    $scope.goToDagobah = function () {
        console.log("Go to the Dagobah system Luke");
        console.log("Selected system = " + $scope.selectedSystem.name);
        window.location.href = "/main.html#/system/" + selectedSystemId;
    };
    $scope.noSystemSelected = true;

    getSSGInfo();
    var scale = 30;
    var xOffset = 20;
    var yOffset = 20;
    function preload () {
        console.log("In preload");
        game.load.image('stars', 'assets/starfield.png');
        game.load.image('tinysun', 'assets/tinysun.png');
        game.load.image('caticon', 'assets/races/race1_icon.jpg');
        game.load.image('dogicon', 'assets/races/race2_icon.jpg');
        game.load.image('horseicon', 'assets/races/race3_icon.jpg');
        game.load.image('snakeicon', 'assets/races/race4_icon.jpg');
    }
    var homeSystemIconsX = [];
    homeSystemIconsX[0] = 270;
    homeSystemIconsX[1] = 610;
    homeSystemIconsX[2] = 265;
    homeSystemIconsX[3] = 25;
    var homeSystemIconsY = [];
    homeSystemIconsY[0] = 10;
    homeSystemIconsY[1] = 285;
    homeSystemIconsY[2] = 490;
    homeSystemIconsY[3] = 220;

    var homeSystemIconNames = [];
    homeSystemIconNames[0] = 'caticon';
    homeSystemIconNames[1] = 'dogicon';
    homeSystemIconNames[2] = 'horseicon';
    homeSystemIconNames[3] = 'snakeicon';

    function create () {
        console.log("in Create");
        game.add.sprite(0, 0, 'stars');
        var graphics = game.add.graphics(0, 0);
        graphics.lineStyle(3, 0x002266, 1);
        var systemSprites = [];
        for (i = 0; i < starSystems.length; i++) {
            systemSprites[i] = game.add.sprite(starSystems[i].gridCoordX * scale + xOffset, starSystems[i].gridCoordY * scale + yOffset, 'tinysun');
            systemSprites[i].inputEnabled = true;
            systemSprites[i].anchor.setTo(0.5, 0.5);
            systemSprites[i].events.onInputDown.add(systemClickListener, this);
            systemSprites[i].systemName = starSystems[i].name;
            systemSprites[i].index = i;
        }
        for (i = 0; i < tunnels.length; i++) {
            var x1 = tunnels[i].firstSystem.gridCoordX;
            var y1 = tunnels[i].firstSystem.gridCoordY;
            var x2 = tunnels[i].secondSystem.gridCoordX;
            var y2 = tunnels[i].secondSystem.gridCoordY;

            graphics.moveTo(x1 * scale + xOffset, y1 * scale + yOffset);
            graphics.lineTo(x2 * scale + xOffset, y2 * scale + yOffset);
        }
        for (i = 0; i < 4; i++) {
            game.add.sprite(homeSystemIconsX[i], homeSystemIconsY[i], homeSystemIconNames[i]);
        }
    }
    var selectedSystemId;
    function systemClickListener (systemSprite) {
        $scope.noSystemSelected = false;
        $scope.selectedSystem = {};
        $scope.selectedSystem.name = systemSprite.systemName;
        console.log(starSystems[systemSprite.index]);
        console.log("id of system = " + starSystems[systemSprite.index].id);
        selectedSystemId = starSystems[systemSprite.index].id;
        var index = systemSprite.index;
        $scope.selectedSystem.planets = starSystems[index].planets;
        $scope.$apply();
    }

    function update () {
    }
});


simplicityApp.controller('diplomacyController', function($scope, $http) {
    console.log("Initializing diplomacyController");
    $scope.players = [];
    var requiredPcts = [];
    requiredPcts[2] = 75;
    requiredPcts[3] = 70;
    requiredPcts[4] = 65;
    getPlayerInfo = function () {
        console.log("Getting player info");
        var wrapper = {"gameId": 1, "playerId":2};
        $http.post("/diplomacy-info.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("Found players");
                console.log(response.data);
                $scope.players = response.data;
                $scope.requiredPct = requiredPcts[$scope.players.length];
            },

            function errorCallback (response) {
                console.log("No players");
            });
    };
    getPlayerInfo();
});

simplicityApp.controller('planetsController', function($scope, $http) {
    console.log("Initializing planetsController");
    getPlanetInfo = function () {
        console.log("Getting planets info");
        var wrapper = {"gameId": 1, "playerId":2};
        $scope.planets = [];
        $http.post("/planets-info.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("Found planets");
                console.log(response.data);
                $scope.planets = response.data;
            },

            function errorCallback (response) {
                console.log("No planets");
            });
    };
    getPlanetInfo();
});

simplicityApp.controller('shipsController', function($scope, $http) {
    console.log("Initializing shipsController");
    getShipInfo = function () {
        console.log("Getting ship info");
        var wrapper = {"gameId": 1, "playerId":2};
        $scope.ships = [];
        $http.post("/ships-info.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("Found ships");
                console.log(response.data);
                $scope.ships = response.data;
            },

            function errorCallback (response) {
                console.log("No ships");
            });
    };
    getShipInfo();

    $scope.unlockedShips = [];
    $scope.unlockedShips[0] = "Colonizer";
    $scope.unlockedShips[1] = "Fighter";
    $scope.unlockedShips[2] = "Destroyer";

    $scope.addToQueue = function () {
        console.log("Aye aye we will totally make that ship for you - NOT!");
    }
});

simplicityApp.controller('researchController', function($scope, $http) {
    console.log("Initializing researchController");
    getResearchInfo = function () {
        console.log("Getting research info");
        var wrapper = {"gameId": 1, "playerId":2};

        $http.post("/research-info.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("Found research info");
                console.log(response.data);
                $scope.researchInfo = response.data;
            },

            function errorCallback (response) {
                console.log("No planets");
            });
    };
    getResearchInfo();
});

simplicityApp.controller('mainController', function($scope, $http, $rootScope) {
    console.log("Initializing mainController");
    $scope.currentTurn = 0;
    $scope.advanceTurn = function () {
        console.log("Seasons change... time passes by...");

        $http.post("/process-turn.json")
        .then(
            function successCallback (response) {
                $rootScope.researchAmt = response.data.researchAmt;
                $rootScope.productionAmt = response.data.productionAmt;

                $rootScope.researchTotal += response.data.researchAmt;
                $rootScope.productionTotal += response.data.productionAmt;

                console.log("total research = " + $rootScope.researchTotal);
                console.log("total production = " + $rootScope.productionTotal);
                $scope.currentTurn++;
            },

            function errorCallback (response) {
                console.log("Could not find turn info");
            });
    };
});

simplicityApp.controller('optionsController', function($scope, $http) {
    console.log("Initializing optionsController");
});

simplicityApp.controller('helpController', function($scope, $http) {
    console.log("Initializing helpController");
});

simplicityApp.controller('combatController', function($scope, $http) {
    console.log("Initializing combatController");
    var game = new Phaser.Game(800, 550, Phaser.AUTO, 'phaser-canvas-container',
     { preload: preload, create: create, update: update });
    var friendShips = [];
    var enemyShips = [];

    var enemiesRemaining;
    getCombatInfo = function () {
        $http.post("/combat-info.json")
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
    getCombatInfo();

    function preload() {
        game.load.image('stars', 'assets/starfield.png');
        game.load.image('destroyer', 'assets/ships/destroyerblue.png');
        game.load.image('enemy', 'assets/ships/enemy_ship.png');
        game.load.spritesheet('destruction_explosion', 'assets/anims/destruction_explosion.png');
    }

    var friendSprites = [];
    var enemySprites = [];

    function create() {
        game.add.sprite(0, 0, 'stars');


        for (i = 0; i < friendShips.length; i++) {
            friendSprites[i] = game.add.sprite(50, 50 + 150*i, friendShips[i].imageString);
            friendSprites[i].inputEnabled = true;
            friendSprites[i].events.onInputDown.add(friendListener, this);
            friendSprites[i].index = i;
        }

        for (i = 0; i < enemyShips.length; i++) {
            enemySprites[i] = game.add.sprite(500, 50 + 100*i, enemyShips[i].imageString);
            enemySprites[i].inputEnabled = true;
            enemySprites[i].events.onInputDown.add(enemyListener, this);
            enemySprites[i].index = i;
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

    var friendChosen = false;
    var enemyChosen = false;
    var friendSelectedIndex;
    var enemySelectedIndex;
    $scope.bothSelected = false;
    function friendListener (sprite) {
        friendChosen = true;
        $scope.bothSelected = friendChosen && enemyChosen;
        $scope.friendSelected = friendShips[sprite.index];
        friendSelectedIndex = sprite.index;
        var ship = friendShips[sprite.index];
        $scope.friendSelected.healthPct = "" + (ship.health/ship.maxHealth)*100 + "%";
        $scope.friendSelected.shieldPct = "" + (ship.shieldHealth/ship.maxShieldHealth)*100 + "%";
        $scope.friendSelected.energyPct = "" + (ship.currentReservePower/ship.maxReservePower)*100 + "%";
        $scope.$apply();
    }

    function enemyListener (sprite) {
        enemyChosen = true;
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
        //Called every frame
    }

    $scope.fireWeapons = function () {
        $scope.friendSelected.currentReservePower
        var damage = $scope.friendSelected.damage;
        if ($scope.friendSelected.currentReservePower < damage) {
            damage = $scope.friendSelected.currentReservePower;
            $scope.friendSelected.currentReservePower = 0;
        } else {
            $scope.friendSelected.currentReservePower -= damage;
        }
        $scope.friendSelected.energyPct = "" + ($scope.friendSelected.currentReservePower/$scope.friendSelected.maxReservePower)*100 + "%";
        var enemyShip = $scope.enemySelected;
        var enemyShipRealDamage = enemyShip.damage;
        //We're pulling a switcheroo so that we don't have to create a wrapper to hold the damage for the friend ship
        enemyShip.damage = damage;
        enemyShip.shieldsUp = true;
        $http.post("/process-attack.json", enemyShip)
        .then(
            function successCallback (response) {
                enemyShips[enemySelectedIndex] = response.data;
                enemyShips[enemySelectedIndex].damage = enemyShipRealDamage;
                $scope.enemySelected = enemyShips[enemySelectedIndex];
                $scope.enemySelected.healthPct = "" + (enemyShips[enemySelectedIndex].health/enemyShips[enemySelectedIndex].maxHealth)*100 + "%";
                $scope.enemySelected.shieldPct = "" + (enemyShips[enemySelectedIndex].shieldHealth/enemyShips[enemySelectedIndex].maxShieldHealth)*100 + "%";
                $scope.enemySelected.energyPct = "" + (enemyShips[enemySelectedIndex].currentReservePower/enemyShips[enemySelectedIndex].maxReservePower)*100 + "%";
                if ($scope.enemySelected.health <= 0) {
                    console.log("He's dead, Jim");
                    enemyShips[enemySelectedIndex] = null;
                    enemySprites[enemySelectedIndex].kill();
                    $scope.enemySelected = null;
                    $scope.bothSelected = false;
                    var explosion = explosions.getFirstExists(false);
                    explosion.reset(20, 20);
                    explosion.play('destruction_explosion', 30, false, true);
                    enemiesRemaining--;
                    if (enemiesRemaining == 0) {
                        alert("Combat is over! You win");
                    }
                }
            },
            /*function successCallback (response) {
                $scope.enemySelected = response.data;
                $scope.enemySelected.damage = enemyShipRealDamage;
                enemyShips[enemySelectedIndex] = $scope.enemySelected;
                if ($scope.enemySelected.health <= 0) {
                    console.log("He's dead, Jim");
                    enemyShips[enemySelectedIndex] = null;
                    enemySprites[enemySelectedIndex].kill();
                }
            },*/

            function errorCallback (response) {
                console.log("Unable to find combat action response");
            });
    };
});

simplicityApp.controller('systemController', function($scope, $http, $routeParams) {
    console.log("Initializing systemController");
    var game = new Phaser.Game(800, 600, Phaser.AUTO, 'phaser-canvas-container',
                                      { preload: preload, create: create, update: update });

    getStarSystemInfo = function () {
        console.log("Getting star system info");
        var wrapper = {"gameId": 1, "playerId":2};

        //$http.post("/simple-system-info.json", wrapper)
        $http.post("/system-info.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("Found system info");
                console.log(response.data);
                $scope.starSystemInfo = response.data;
            },

            function errorCallback (response) {
                console.log("No sys info");
            });
    };

    getSpecificStarSystemInfo = function (systemId) {
        console.log("Getting *specific* star system info");
        var wrapper = {"systemId": systemId};
        console.log("For system with id = " + systemId);
        $http.post("/specific-system-info.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("Found specific system info");
                console.log(response.data);
                $scope.starSystemInfo = response.data;
            },

            function errorCallback (response) {
                console.log("Unable to find system info");
            });
    }
    console.log("$routeParams.param = " + $routeParams.param);
    if ($routeParams.param != null) {
        getSpecificStarSystemInfo($routeParams.param);
    } else {
        getStarSystemInfo();
    }
    $scope.selected = "Nothing";
    var explosions;
    var wormholes;
    var wormholeAnims = [];
    var addAnimations = true;
    var systemText;

    function preload() {
        game.load.image('stars', 'assets/starfield.png');
        game.load.image('sun', 'assets/smallsun.png');;
        game.load.image('destroyer', 'assets/ships/destroyerblue.png');
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
    }
    var tunnels = [];
    var graphics;
    var colonizeGraphics;
    var planetCoordsX = [];
    var planetCoordsY = [];
    function create() {
        game.physics.startSystem(Phaser.Physics.ARCADE);
        var ringCenterX = 400;
        var ringCenterY = 300;
        //  A simple background for our game
        game.add.sprite(0, 0, 'stars');
        game.add.sprite(350, 250, 'sun');

        planetCoordsX[0] = 430;
        planetCoordsX[1] = 350;
        planetCoordsX[2] = 525;

        planetCoordsY[0] = 205;
        planetCoordsY[1] = 105;
        planetCoordsY[2] = 360;

        var tunnelCoordsX = [];
        tunnelCoordsX[0] = 20;
        tunnelCoordsX[1] = 620;
        tunnelCoordsX[2] = 350;

        var tunnelCoordsY = [];
        tunnelCoordsY[0] = 400;
        tunnelCoordsY[1] = 400;
        tunnelCoordsY[2] = 490;

        graphics = game.add.graphics(0, 0);
        colonizeGraphics = game.add.graphics(0, 0);
        var planets = [];
        graphics.lineStyle(1, 0x6C6C6C, 1);
        for(i = 0; i < $scope.starSystemInfo.starSystem.planets.length; i++) {
            graphics.drawCircle(ringCenterX, ringCenterY, 100 + i*50);
            //Todo fix to use the image string coming in
            planets[i] = game.add.sprite(planetCoordsX[i], planetCoordsY[i], $scope.starSystemInfo.starSystem.planets[i].imageString);
            planets[i].inputEnabled = true;
            planets[i].events.onInputDown.add(listener, this);
            planets[i].elementName = $scope.starSystemInfo.starSystem.planets[i].name;
            planets[i].population = $scope.starSystemInfo.starSystem.planets[i].size;
            var ownerRaceNum = $scope.starSystemInfo.starSystem.planets[i].ownerRaceNum;
            switch (ownerRaceNum) {
                case -1:
                    planets[i].icon = "assets/races/norace_icon.png";
                    break;

                case 0:
                    planets[i].icon = "assets/races/race1_icon.jpg";
                    break;

                case 1:
                    planets[i].icon = "assets/races/race2_icon.jpg";
                    break;

                case 2:
                    planets[i].icon = "assets/races/race3_icon.jpg";
                    break;

                case 3:
                    planets[i].icon = "assets/races/race4_icon.jpg";
                    break;
            }
        }

        if (addAnimations) {
            var tunnel = game.add.sprite(20, 400, 'tunnel');
            tunnel.inputEnabled = true;
            tunnel.events.onInputDown.add(listener, this);
            tunnel.elementName = "Tunnel";
            tunnel.alpha = 0.0;
        }


        systemText = game.add.text(200 , 25, $scope.starSystemInfo.starSystem.name + " System" , { font: '28px Arial', fill: '#fff' });
        systemText.visible = true;


        for (i = 0; i < $scope.starSystemInfo.tunnels.length; i++) {
            tunnels[i] = game.add.sprite(tunnelCoordsX[i], tunnelCoordsY[i], 'tunnel');
            tunnels[i].inputEnabled = true;
            tunnels[i].events.onInputDown.add(listener, this);
            tunnels[i].elementName = "Tunnel: " + $scope.starSystemInfo.tunnels[i].name;
            tunnels[i].travelTime = $scope.starSystemInfo.tunnels[i].length + " turns journey";
            tunnels[i].index = i;
        }

        if (addAnimations) {
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

        var ships = [];
        explosions = game.add.group();
        explosions.createMultiple(10, 'kaboom');
        for (i = 0; i < $scope.starSystemInfo.ships.length; i++) {
            ships[i] = game.add.sprite(20, 20 + i*100, 'destroyer');
            ships[i].inputEnabled = true;
            ships[i].input.enableDrag();
            ships[i].events.onDragStop.add(onDragStop, this);
            ships[i].events.onInputDown.add(listener, this);
            ships[i].elementName = $scope.starSystemInfo.ships[i].name;
            ships[i].shipHealth = $scope.starSystemInfo.ships[i].health;
            ships[i].maxHealth = $scope.starSystemInfo.ships[i].maxHealth;
            ships[i].shipId = $scope.starSystemInfo.ships[i].id;
            ships[i].type = $scope.starSystemInfo.ships[i].chassis;
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
        if (sprite.shipHealth != null) {
            $scope.shipSelected = true;
            $scope.selectedElement.icon = "assets/races/race1_icon.jpg";
            $scope.selectedElement.health = sprite.shipHealth + "/" + sprite.maxHealth;
        }
        if (sprite.population != null) {
            $scope.planetSelected = true;
            $scope.outputSlider = 60;
            $scope.selectedElement.icon = sprite.icon;

            $scope.selectedElement.population = "Population: " + sprite.population;
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

    enterTunnel = function (destinationId, tunnelId, shipId) {
        console.log("Attempting to enter tunnel using the following ids:");
        console.log("destinationId = " + destinationId + " , tunnelId = " + tunnelId + ", shipId = " + shipId);
        var wrapper = {"systemId": destinationId, "tunnelId": tunnelId, "shipId": shipId};
        $http.post("/enter-tunnel.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("Got response");
                console.log(response.data);
            },

            function errorCallback (response) {
                console.log("Error callback for enter tunnel");
            });
    }
    $scope.tunnelsOpen = false;

    $scope.tunnelToggle = "Open Tunnels";

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

    $scope.enterCombat = function () {
        console.log("EVERYBODY WAS KUNG FU FITING");
    }

    var colonizeModeActive = false;
    var greenLine;
    $scope.colonizeMode = function () {
        console.log("Colonizing a planet. Feeling very Bri'ish");
        var planets = $scope.starSystemInfo.starSystem.planets;
        colonizeModeActive = !colonizeModeActive;
        if (colonizeModeActive) {
            if (greenLine == null) {
                colonizeGraphics.lineStyle(5, 0x009933, 1);
                greenLine = colonizeGraphics.drawCircle(100, 100, 30);
                for (i = 0; i < planets.length; i++) {
                    if (planets[i].ownerRaceNum == -1) {
                        colonizeGraphics.drawCircle(planetCoordsX[i] + 44, planetCoordsY[i] + 44, 30);
                    }
                }
            }
            greenLine.alpha = 1.0;
        } else {
            if (greenLine != null) {
                greenLine.alpha = 0.0;
            }
        }
    }

    /*graphics.lineStyle(1, 0x6C6C6C, 1);
            for(i = 0; i < $scope.starSystemInfo.starSystem.planets.length; i++) {
                graphics.drawCircle(ringCenterX, ringCenterY, 100 + i*50);*/

    $scope.changeOutput = function () {
        console.log("The outputs they are a changin'");
    }
});
