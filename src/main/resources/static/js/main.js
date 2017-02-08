var simplicityApp = angular.module("simplicityApp", ["ngRoute"]);

console.log("simplicityApp module created");

simplicityApp.config(function($routeProvider) {
    console.log("Initializing ng-router");
    $routeProvider
        .when("/", {
            templateUrl: "ssgview.html",
            //controller: "spaController"
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
});

simplicityApp.controller('mainController', function($scope, $http) {
    console.log("Initializing mainController");
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

simplicityApp.controller('systemController', function($scope, $http) {
    console.log("Initializing systemController");
    var game = new Phaser.Game(800, 600, Phaser.AUTO, 'phaser-canvas-container',
     { preload: preload, create: create, update: update });

    getStarSystemInfo = function () {
        console.log("Getting star system info");
        var wrapper = {"gameId": 1, "playerId":2};

        $http.post("/simple-system-info.json", wrapper)
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

    function preload() {
        game.load.image('stars', 'assets/starfield.png');
        game.load.image('sun', 'assets/smallsun.png');
        game.load.image('earth', 'assets/earth.png');
        game.load.image('zebulon', 'assets/zebulon.png');
        game.load.image('destroyer', 'assets/destroyerblue.png');
        game.load.image('fighter', 'assets/fighter.png');
        game.load.image('tunnel', 'assets/green_wormhole_small_clear.png');
        game.load.spritesheet('kaboom', 'assets/explode.png', 128, 128);
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

        var planetNames = [];
        planetNames[0] = 'earth';
        planetNames[1] = 'zebulon';
        planetNames[2] = 'earth';

        var graphics = game.add.graphics(100, 100);

        graphics.lineStyle(1, 0x6C6C6C, 1);
        //for(i = 0; i < $scope.starSystemInfo.starSystem.planets.length; i++) {
        for (i = 0; i < $scope.starSystemInfo.numPlanets; i++) {
            graphics.drawCircle(ringCenterX, ringCenterY, 100 + i*50);
            game.add.sprite(planetCoordsX[i], planetCoordsY[i], planetNames[i]);
            //Todo fix to use the image string coming in
        }

        fighter = game.add.sprite(500, 20, 'fighter');

        /*for (i = 0; i < $scope.starSystemInfo.ships.length; i++) {
            game.add.sprite(20, 20 + i*100, 'destroyer');
        }
        for (i = 0; i < $scope.starSystemInfo.starSystem.tunnels.length; i++) {
            game.add.sprite(20, 400 + i*80, 'tunnel')
        }*/

        var tunnel = game.add.sprite(20, 400, 'tunnel');
        tunnel.inputEnabled = true;
        tunnel.events.onInputDown.add(listener, this);
        tunnel.elementName = "Tunnel";

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
            ships[i].animations.add('kaboom', [0,1,2,3], 4, true);

            //player.animations.add('player_idle', [8,9,10,11,12], 4, true);
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
        $scope.selectedElement.health = sprite.shipHealth;
        $scope.selectedElement.maxHealth = sprite.maxHealth;
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
