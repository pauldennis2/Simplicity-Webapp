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
        .when("/system", {
            templateUrl: "starsystem.html",
            controller: "systemController"
        })
        .when("/help", {
            templateUrl: "help.html"
        })
        .when("/options", {
            templateUrl: "options.html",
            controller: "optionsController"
        })
        .when("/research", {
            templateUrl: "research.html",
            controller: "researchController"
        })
        .when("/combat", {
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
        $http.post("/ssg-main-info.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("Found info");
                starSystems = response.data.starSystems;
                console.log("starSystems = " + starSystems);
                tunnels = response.data.tunnels;
                console.log("tunnels = " + tunnels);
            },

            function errorCallback (response) {
                console.log("Failed to find SSG info");
            });
    };
    getSSGInfo();
    var scale = 30;
    /*var starSystems = [];
    starSystems[0] = {};
    starSystems[0].x = 5;
    starSystems[0].y = 2;

    starSystems[1] = {};
    starSystems[1].x = 8;
    starSystems[1].y = 4;*/
    var xOffset = 20;
    var yOffset = 20;
    function preload () {
        game.load.image('stars', 'assets/starfield.png');
        game.load.image('tinysun', 'assets/tinysun.png');
    }

    function create () {
        game.add.sprite(0, 0, 'stars');
        var graphics = game.add.graphics(0, 0);
        graphics.lineStyle(3, 0x002266, 1);
        var systemSprites = [];
        for (i = 0; i < starSystems.length; i++) {
            systemSprites[i] = game.add.sprite(starSystems[i].gridCoordX * scale + xOffset, starSystems[i].gridCoordY * scale + yOffset, 'tinysun');
            systemSprites[i].inputEnabled = true;
            systemSprites[i].anchor.setTo(0.5, 0.5);
            systemSprites[i].events.onInputDown.add(listener, this);
            systemSprites[i].systemName = starSystems[i].name;
        }
        for (i = 0; i < tunnels.length; i++) {
            var x1 = tunnels[i].firstSystem.gridCoordX;
            var y1 = tunnels[i].firstSystem.gridCoordY;
            var x2 = tunnels[i].secondSystem.gridCoordX;
            var y2 = tunnels[i].secondSystem.gridCoordY;

            graphics.moveTo(x1 * scale + xOffset, y1 * scale + yOffset);
            graphics.lineTo(x2 * scale + xOffset, y2 * scale + yOffset);
        }
    }

    function listener (sprite) {
        console.log("Someone clicked a system! HOORAY!");
        $scope.selectedSystem = sprite.systemName;
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

simplicityApp.controller('optionsController', function($scope, $http) {
    console.log("Initializing optionsController");
});

simplicityApp.controller('combatController', function($scope, $http) {
    console.log("Initializing combatController");
    var game = new Phaser.Game(800, 580, Phaser.AUTO, 'phaser-canvas-container',
     { preload: preload, create: create, update: update });

    getStarSystemInfo = function () {
        console.log("Getting star system info");
        var wrapper = {"gameId": 1, "playerId":2};

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

    $scope.friendSelected = {};
    $scope.friendSelected.name = "Voyager";
    $scope.friendSelected.damage = 50;

    $scope.friendSelected.healthPct = "95%";
    $scope.friendSelected.health = 95;
    $scope.friendSelected.maxHealth = 100;

    $scope.friendSelected.shieldPct = "75%";
    $scope.friendSelected.shieldHealth = 150;
    $scope.friendSelected.maxShield = 200;

    $scope.friendSelected.energyPct = "100%";
    $scope.friendSelected.energy = 400;
    $scope.friendSelected.maxEnergy = 400;

    $scope.enemySelected = {};
    $scope.enemySelected.name = "Tempest";
    $scope.enemySelected.damage = 35;

    $scope.enemySelected.healthPct = "100%";
    $scope.enemySelected.health = 75;
    $scope.enemySelected.maxHealth = 75;

    $scope.enemySelected.shieldPct = "75%";
    $scope.enemySelected.shieldHealth = 45;
    $scope.enemySelected.maxShield = 60;

    $scope.enemySelected.energyPct = "50%";
    $scope.enemySelected.energy = 100;
    $scope.enemySelected.maxEnergy = 200;

    function preload() {
        game.load.image('stars', 'assets/starfield.png');
        game.load.image('destroyer', 'assets/destroyerblue.png');
        game.load.image('enemy', 'assets/enemy_ship.png');
    }

    function create() {
        game.add.sprite(0, 0, 'stars');

        var destroyer1 = game.add.sprite(50, 50, 'destroyer');
        var destroyer2 = game.add.sprite(50, 200, 'destroyer');

        var enemies = [];

        for (i = 0; i < 3; i++) {
            enemies[i] = game.add.sprite(500, 50 + 100*i, 'enemy');
            enemies[i].inputEnabled = true;
            enemies[i].events.onInputDown.add(enemyListener, this);
        }
    }

    function enemyListener (sprite) {
        console.log("Element name = " + sprite.elementName);
        $scope.selectedElement = {};
        $scope.selectedElement.name = sprite.elementName;
        //$scope.selectedElement.health = sprite.shipHealth;
        //$scope.selectedElement.maxHealth = sprite.maxHealth;
        if (sprite.shipHealth != null) {
            $scope.selectedElement.health = sprite.shipHealth + "/" + sprite.maxHealth;
        }
        if (sprite.population != null) {
            $scope.selectedElement.population = "Population: " + sprite.population;
        }
        $scope.selectedElement.travelTime = sprite.travelTime;
        $scope.$apply();
    }

    function update() {
        //Called every frame
    }
});

simplicityApp.controller('systemController', function($scope, $http) {
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

    getStarSystemInfo();
    $scope.selected = "Nothing";
    var explosions;
    var wormholes;
    var wormhole;
    var addAnimations = false;
    var systemText;

    function preload() {
        game.load.image('stars', 'assets/starfield.png');
        game.load.image('sun', 'assets/smallsun.png');
        game.load.image('earth', 'assets/earth.png');
        game.load.image('zebulon', 'assets/zebulon.png');
        game.load.image('destroyer', 'assets/destroyerblue.png');
        game.load.image('fighter', 'assets/fighter.png');
        game.load.image('tunnel', 'assets/green_wormhole_small_clear.png');
        game.load.spritesheet('kaboom', 'assets/explode.png', 128, 128);
        game.load.spritesheet('wormhole', 'assets/black_wormhole.png', 128, 128);
    }

    function create() {
        game.physics.startSystem(Phaser.Physics.ARCADE);

        var ringCenterX = 300;
        var ringCenterY = 200;
        //  A simple background for our game
        game.add.sprite(0, 0, 'stars');
        game.add.sprite(350, 250, 'sun');

        var planetCoordsX = [];
        planetCoordsX[0] = 350;
        planetCoordsX[1] = 430;
        planetCoordsX[2] = 525;

        var planetCoordsY = [];
        planetCoordsY[0] = 105;
        planetCoordsY[1] = 205;
        planetCoordsY[2] = 360;

        var tunnelCoordsX = [];
        tunnelCoordsX[0] = 20;
        tunnelCoordsX[1] = 600;
        tunnelCoordsX[2] = 350;

        var tunnelCoordsY = [];
        tunnelCoordsY[0] = 400;
        tunnelCoordsY[1] = 400;
        tunnelCoordsY[2] = 475;

        var planetNames = [];
        planetNames[0] = 'earth';
        planetNames[1] = 'zebulon';
        planetNames[2] = 'earth';

        var graphics = game.add.graphics(100, 100);
        var planets = [];
        graphics.lineStyle(1, 0x6C6C6C, 1);
        for(i = 0; i < $scope.starSystemInfo.starSystem.planets.length; i++) {
        //for (i = 0; i < $scope.starSystemInfo.numPlanets; i++) {
            graphics.drawCircle(ringCenterX, ringCenterY, 100 + i*50);
            //game.add.sprite(planetCoordsX[i], planetCoordsY[i], planetNames[i]);
            //Todo fix to use the image string coming in
            planets[i] = game.add.sprite(planetCoordsX[i], planetCoordsY[i], $scope.starSystemInfo.starSystem.planets[i].imageString);
            planets[i].inputEnabled = true;
            planets[i].events.onInputDown.add(listener, this);
            planets[i].elementName = $scope.starSystemInfo.starSystem.planets[i].name;
            planets[i].population = $scope.starSystemInfo.starSystem.planets[i].size;
            //ships[i].events.onInputDown.add(listener, this);
            //ships[i].elementName = $scope.starSystemInfo.ships[i].name;
        }

        fighter = game.add.sprite(500, 20, 'fighter');

        /*for (i = 0; i < $scope.starSystemInfo.ships.length; i++) {
            game.add.sprite(20, 20 + i*100, 'destroyer');
        }
        for (i = 0; i < $scope.starSystemInfo.starSystem.tunnels.length; i++) {
            game.add.sprite(20, 400 + i*80, 'tunnel')
        }*/

        if (addAnimations) {
            var tunnel = game.add.sprite(20, 400, 'tunnel');
            tunnel.inputEnabled = true;
            tunnel.events.onInputDown.add(listener, this);
            tunnel.elementName = "Tunnel";
            tunnel.alpha = 0.0;
        }


        systemText = game.add.text(200 , 25, $scope.starSystemInfo.starSystem.name + " System" , { font: '28px Arial', fill: '#fff' });
        systemText.visible = true;

        var tunnels = [];
        for (i = 0; i < $scope.starSystemInfo.tunnels.length; i++) {
            tunnels[i] = game.add.sprite(tunnelCoordsX[i], tunnelCoordsY[i], 'tunnel');
            tunnels[i].inputEnabled = true;
            tunnels[i].events.onInputDown.add(listener, this);
            tunnels[i].elementName = "Tunnel: " + $scope.starSystemInfo.tunnels[i].name;
            tunnels[i].travelTime = $scope.starSystemInfo.tunnels[i].length + " turns journey";
        }

        if (addAnimations) {
            wormholes = game.add.group();
            wormholes.createMultiple(10, 'wormhole');
            //tunnel.animations.add('wormhole');
            wormholes.forEach(setupDiamond, this);
            wormhole = wormholes.getFirstExists(false);
            wormhole.reset(20, 400);
            wormhole.play('wormhole', 20, true, false);
        }

        /*var destroyer = game.add.sprite(20, 20, 'destroyer');
        destroyer.inputEnabled = true;
        destroyer.input.enableDrag();
        destroyer.events.onDragStop.add(onDragStop, this);
        destroyer.events.onInputDown.add(listener, this);*/
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
            ships[i].events.onKilled.add(onKill, this);
            ships[i].animations.add('kaboom');
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

    function onKill() {
        var explosionX = 200;
        var explosionY = 200;
        var frameRate = 10;
        console.log("He's dead, Jim");
        var explosion = explosions.getFirstExists(false);
        explosion.reset(explosionX, explosionY);
        //Last two args are loop, kill on complete
        explosion.play('kaboom', frameRate, false, true);
        //explosion.play(10);
    }

    function listener (sprite) {
        console.log("Element name = " + sprite.elementName);
        $scope.selectedElement = {};
        $scope.selectedElement.name = sprite.elementName;
        //$scope.selectedElement.health = sprite.shipHealth;
        //$scope.selectedElement.maxHealth = sprite.maxHealth;
        if (sprite.shipHealth != null) {
            $scope.selectedElement.health = sprite.shipHealth + "/" + sprite.maxHealth;
        }
        if (sprite.population != null) {
            $scope.selectedElement.population = "Population: " + sprite.population;
        }
        $scope.selectedElement.travelTime = sprite.travelTime;
        $scope.$apply();
    }

    function onDragStop (sprite, pointer) {
        console.log("Location: " + pointer.x + ", " + pointer.y);
        //sun is at 350 250, if we drop it there kill
        if (pointer.x > 375 && pointer.x < 450) {
            if (pointer.y > 250 && pointer.y < 325) {
                console.log("The ship is in the sun. DESTWOY IT");
                sprite.kill();
            }
        }
    }
});