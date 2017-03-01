var simplicityLoadGame = angular.module("loadGameApp", []);

simplicityLoadGame.controller('loadGameController', function($scope, $http) {

    console.log("In loadGameController");
    $scope.savedGameIds = [];
    function getCurrentUser () {
        $http.get("/my-user.json")
        .then(
            function successCallback (response) {
                console.log("success cb");
                console.log(response.data);
                $scope.currentUser = response.data.handle;
                $scope.savedGameIds = response.data.savedGameIds;
            },

            function errorCallback (response) {
                console.log("Unable to find user");
            });
    }

    $scope.loadGame = function (gameId) {
        $http.post("/load-game.json", {"gameId": gameId})
        .then(
            function successCallback (response) {
                window.location.href = "/main.html";
            },

            function errorCallback (response) {
                console.log("Unable to load game");
            }
        )
    }

    getCurrentUser();
});