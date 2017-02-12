var simpleLobbyApp = angular.module("simpleLobbyApp", []);

console.log("In simple-lobby.js");

simpleLobbyApp.controller('simpleLobbyController', function($scope, $http) {
    console.log("In simpleLobbyController");

    $scope.users = [];
    getLobbyUsers = function () {
        console.log("Getting user info");
        $http.get("/users.json")
        .then(
            function successCallback (response) {
                console.log("Found users");
                if ($scope.users.length != response.data.length) {
                    console.log("$scope.users.length = " + $scope.users.length);
                    console.log("response.data.length = " + response.data.length);
                    $scope.users = response.data;
                    console.log(" New Users = " + $scope.users);
                }
            },

            function errorCallback (response) {
                console.log("Problem finding users");
            });
    };
    getLobbyUsers();
    window.setInterval(function(){
      getLobbyUsers();
    }, 5000);
});
