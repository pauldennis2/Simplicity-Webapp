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

    $scope.raceImg = "../assets/races/race1.jpg";


});