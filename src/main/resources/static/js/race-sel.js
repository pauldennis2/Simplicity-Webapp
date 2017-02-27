var simplicityRaceSelApp = angular.module("simplicityRaceSelApp", []);

simplicityRaceSelApp.controller('raceSelController', function($scope, $http) {

    $scope.newGame = function (raceId) {
        $http.post("/new-empty-game.json", {"raceId": raceId})
        .then(
            function successCallback (response) {
                window.location.href = "/main.html";
            },

            function errorCallback (response) {
                console.log("Unable to start a new game at this time.");
            });
    };
});