var simplicityLobbyApp = angular.module("simplicityLobbyApp", []);

console.log("In lobby.js");

simplicityLobbyApp.controller('lobbyController', function($scope, $http) {
    console.log("Lobbycontroller initialized");
    $scope.alphaUsers = [];
    $scope.bakerUsers = [];
    $scope.charlieUsers = [];
    $scope.deltaUsers = [];

    users = [];

    users[0] = {"email":"James", "ready": false, "leader": true};
    users[1] = {"email":"Paul", "ready": false, "leader": false};
    users[2] = {"email":"Jean-Luc", "ready": false, "leader": false};
    users[3] = {"email":"Kathryn", "ready": false, "leader": false};

    currentUserIndex = 1;

    $scope.raceImg = "../assets/1.jpg";

    $scope.alphaUsers[0] = users[0];
    $scope.alphaUsers[1] = users[1];
    $scope.alphaUsers[2] = users[2];
    $scope.alphaUsers[3] = users[3];

    /*$scope.moveToAlpha = function () {
        console.log("Moving to alpha");
        $scope.alphaUsers.unshift(users[0]);
        $scope.bakerUsers.shift();
    }
    $scope.moveToBaker = function () {
        console.log("Moving you to baker");
        //$scope.alphaUsers[0] = null;
        //$scope.bakerUsers[0] = users[0];
        $scope.bakerUsers.unshift(users[0]);
        $scope.alphaUsers.shift();
    }
    $scope.moveToCharlie = function () {
        console.log("Moving to charlie");
        //$scope.charlieUsers[charlieUsers.length] = users[0];
    }
    $scope.moveToDelta = function () {
        console.log("Moving to delta");
        //$scope.deltaUsers[deltaUsers.length] = users[0];
    }*/

    $scope.moveToGroup = function (group) {
        var index = group.indexOf(users[currentUserIndex]);
        if (index < 0 && group.length < 4) { //If we are NOT in the group and the group is not full
            $scope.bakerStart = false;
            var alphaIndex = $scope.alphaUsers.indexOf(users[currentUserIndex]);
            var bakerIndex = $scope.bakerUsers.indexOf(users[currentUserIndex]);
            var charlieIndex = $scope.charlieUsers.indexOf(users[currentUserIndex]);
            var deltaIndex = $scope.deltaUsers.indexOf(users[currentUserIndex]);
            //console.log("a = " + alphaIndex + ", b =  " + bakerIndex + ", c = " + charlieIndex + ", d = " + deltaIndex);
            //Remove the user from the group they were in
            if (alphaIndex > -1) {
                //$scope.alphaUsers.shift();
                $scope.alphaUsers.splice(alphaIndex, 1);
            }
            if (bakerIndex > -1) {
                $scope.bakerUsers.splice(bakerIndex, 1);
            }
            if (charlieIndex > -1) {
                $scope.charlieUsers.splice(charlieIndex, 1);
            }
            if (deltaIndex > -1) {
                $scope.deltaUsers.splice(deltaIndex, 1);
            }
            //Add the user to the selected group
            group.push(users[currentUserIndex]);
            //update leader status
            if (users[currentUserIndex].leader) {
                if (alphaIndex > -1) {
                    if ($scope.alphaUsers[0] != null) {
                        $scope.alphaUsers[0].leader = true;
                    }
                }
                if (bakerIndex > -1) {
                    if ($scope.bakerUsers[0] != null) {
                        $scope.bakerUsers[0].leader = true;
                    }
                }
                if (charlieIndex > -1) {
                    if($scope.charlieUsers[0] != null) {
                        $scope.charlieUsers[0].leader = true;
                    }
                }
                if (deltaIndex > -1) {
                    if ($scope.deltaUsers[0] != null) {
                        $scope.deltaUsers[0].leader = true;
                    }
                }
            }
            if (group.length == 1) {
                users[currentUserIndex].leader = true;
            } else {
                users[currentUserIndex].leader = false;
            }
        }
        //group.shift();
        //$scope.deltaUsers.unshift(users[currentUserIndex]);
    }

    /*fruits.push('Mango');
    // ["Strawberry", "Banana", "Mango"]

    var pos = fruits.indexOf('Banana');
    // 1
    Remove an item by Index Position

    var removedItem = fruits.splice(pos, 1); // this is how to remove an item,

    // ["Strawberry", "Mango"]*/
});