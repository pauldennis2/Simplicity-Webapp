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
                console.log("Found planets");
                console.log(response.data);
                $scope.planets = response.data;
            },

            function errorCallback (response) {
                console.log("No planets");
            });
    };
    getResearchInfo();
});
