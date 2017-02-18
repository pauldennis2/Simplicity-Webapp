var simplicityRaceSelApp = angular.module("simplicityRaceSelApp", []);

console.log("In race-sel.js");

simplicityRaceSelApp.controller('raceSelController', function($scope, $http) {
    console.log("Race select controller initialized");

    $scope.raceChange = function () {
        if ($scope.race_selection == "cat") {
            $scope.raceImg = "../assets/races/race1.jpg";
        }
        if ($scope.race_selection == "dog") {
            $scope.raceImg = "../assets/races/race2.jpg";
        }
        if ($scope.race_selection == "horse") {
            $scope.raceImg = "../assets/races/race3.jpg";
        }
        if ($scope.race_selection == "snake") {
            $scope.raceImg = "../assets/races/race4.jpg";
        }
    }

    $scope.raceImg = "../assets/races/no_race.png";
    $scope.newGame = function () {
        console.log("Starting new game");
        var raceId;
        console.log($scope.race_selection);
        if ($scope.race_selection == null) {
            alert("Must choose a race");
        } else {
            if ($scope.race_selection == "cat") {
                raceId = 0;
            }
            if ($scope.race_selection == "dog") {
                raceId = 1;
            }
            if ($scope.race_selection == "horse") {
                raceId = 2;
            }
            if ($scope.race_selection == "snake") {
                raceId = 3;
            }
            console.log("raceId = " + raceId);
            var wrapper = {"raceId": raceId};
            console.log(wrapper);
            $http.post("/new-empty-game.json", wrapper)
            .then(
                function successCallback (response) {
                    console.log("Successfully started a new game. So sayeth the wise Alaundo");
                    console.log(response.data);
                    window.location.href = "/main.html";
                },

                function errorCallback (response) {
                    console.log("Unable to start a new game at this time.");
                });
        }
    };
});